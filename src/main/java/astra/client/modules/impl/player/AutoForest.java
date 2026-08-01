/*     */ package shame.astra.client.modules.impl.player;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.class_1268;
/*     */ import net.minecraft.class_2338;
/*     */ import net.minecraft.class_2350;
/*     */ import net.minecraft.class_2382;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_2596;
/*     */ import net.minecraft.class_2626;
/*     */ import net.minecraft.class_2680;
/*     */ import net.minecraft.class_2846;
/*     */ import net.minecraft.class_3481;
/*     */ import net.minecraft.class_3959;
/*     */ import net.minecraft.class_3965;
/*     */ import net.minecraft.class_634;
/*     */ import net.minecraft.class_636;
/*     */ import net.minecraft.class_638;
/*     */ import net.minecraft.class_746;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.EventPacket;
/*     */ import shame.astra.api.utils.bot.BotSessionManager;
/*     */ import shame.astra.api.utils.chat.ChatUtils;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*     */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*     */ import shame.astra.client.modules.settings.implement.ModeSetting;
/*     */ 
/*     */ public class AutoForest extends Module {
/*  36 */   public static AutoForest INSTANCE = new AutoForest();
/*     */   
/*     */   private static final double MAX_RANGE = 4.0D;
/*     */   
/*     */   private static final double MAX_RANGE_SQ = 16.0D;
/*     */   private static final long DEFAULT_BREAK_DELAY_MS = 3L;
/*     */   private static final float AUTO_FAST_BREAK_SPEED = 1.0F;
/*     */   private static final float DEFAULT_PACKETS_PER_SECOND = 100.0F;
/*     */   private static final long VISUAL_TTL_MS = 300000L;
/*     */   private static final long NICK_REMINDER_DELAY_MS = 5000L;
/*     */   private static final String MODE_NORMAL_ALIAS = "normal";
/*     */   private static final String MODE_FAST_ALIAS = "fast";
/*  48 */   private final ModeSetting breakMode = new ModeSetting("Режим ломания", "Обычный", new String[] { "Обычный", "Быстрый" });
/*  49 */   private final FloatSetting packetsPerSecond = (new FloatSetting("Пакетов в секунду", 100.0F, 1.0F, 100.0F, 1.0F))
/*  50 */     .visible(() -> Boolean.valueOf(this.breakMode.is("Быстрый")));
/*  51 */   private final FloatSetting breakRadius = new FloatSetting("Радиус", 4.0F, 1.0F, 6.0F, 0.5F);
/*  52 */   private final BooleanSetting swing = new BooleanSetting("Махать рукой", true);
/*  53 */   private final BooleanSetting autoSell = new BooleanSetting("Авто продажа дерева", true);
/*  54 */   private final BooleanSetting autoPay = new BooleanSetting("AutoPay", false);
/*  55 */   private final BooleanSetting preserveVisuals = new BooleanSetting("Сохранять визуализацию", true);
/*     */   
/*     */   private final FloatSetting payAmount;
/*     */   
/*     */   private final FloatSetting intervalSeconds;
/*     */   
/*     */   private final Map<class_2338, class_2680> preservedBlocks;
/*     */   private final Map<class_2338, Long> lastUpdateTime;
/*     */   private final Set<class_2338> managedBlocks;
/*     */   private boolean currentSessionEnabled;
/*     */   private class_2338 targetPos;
/*     */   private String payTarget;
/*     */   private long lastBreakTime;
/*     */   private long lastPacketTime;
/*     */   private long lastSellTime;
/*     */   private long lastPayTime;
/*     */   private long lastNickReminderTime;
/*     */   
/*     */   public AutoForest() {
/*  74 */     super("AutoForest", "Автоматически ломает бревна и переводит деньги", Module.ModuleCategory.PLAYER); Objects.requireNonNull(this.autoPay); this.payAmount = (new FloatSetting("Сумма перевода", 1000.0F, 500.0F, 25000.0F, 500.0F)).visible(this.autoPay::isState); this.intervalSeconds = new FloatSetting("Задержка", 20.0F, 1.0F, 60.0F, 1.0F); this.preservedBlocks = new HashMap<>(); this.lastUpdateTime = new HashMap<>(); this.managedBlocks = new HashSet<>(); this.payTarget = "";
/*  75 */     addSettings(new Setting[] { (Setting)this.breakMode, (Setting)this.packetsPerSecond, (Setting)this.breakRadius, (Setting)this.swing, (Setting)this.autoSell, (Setting)this.autoPay, (Setting)this.preserveVisuals, (Setting)this.payAmount, (Setting)this.intervalSeconds });
/*  76 */     EventInvoker.register(this);
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onUpdate(EventUpdate event) {
/*  81 */     if (this.currentSessionEnabled && mc.field_1724 != null && mc.field_1687 != null) {
/*  82 */       tickCurrentSession();
/*     */     }
/*     */     
/*  85 */     tickFrozenBots();
/*     */   }
/*     */   
/*     */   private void tickCurrentSession() {
/*  89 */     if (mc.field_1724 == null || mc.field_1687 == null || mc.method_1562() == null) {
/*  90 */       this.targetPos = null;
/*     */       
/*     */       return;
/*     */     } 
/*  94 */     long now = System.currentTimeMillis();
/*  95 */     long scheduleDelay = Math.max(1000L, (long)(this.intervalSeconds.get() * 500.0F));
/*     */     
/*  97 */     if (this.autoSell.isState() && now - this.lastSellTime >= scheduleDelay) {
/*  98 */       mc.method_1562().method_45730("sellwood");
/*  99 */       this.lastSellTime = now;
/*     */     } 
/*     */     
/* 102 */     if (this.autoPay.isState()) {
/* 103 */       if (this.payTarget.isBlank()) {
/* 104 */         if (now - this.lastNickReminderTime >= 5000L) {
/* 105 */           this.lastNickReminderTime = now;
/* 106 */           ChatUtils.sendMessage("Укажите ник для перевода через .autoles pay <nick>");
/*     */         } 
/* 108 */       } else if (now - this.lastPayTime >= scheduleDelay + 200L) {
/* 109 */         mc.method_1562().method_45730("pay " + this.payTarget + " " + (int)this.payAmount.get());
/* 110 */         this.lastPayTime = now;
/*     */       } 
/*     */     }
/*     */     
/* 114 */     if (this.targetPos != null && (!isLog(this.targetPos) || !isInRange(this.targetPos) || !isVisible(this.targetPos))) {
/* 115 */       this.targetPos = null;
/*     */     }
/*     */     
/* 118 */     if (this.targetPos == null) {
/* 119 */       this.targetPos = findNearestLog();
/*     */     }
/*     */     
/* 122 */     if (this.targetPos != null) {
/* 123 */       breakTarget(now);
/*     */     }
/*     */     
/* 126 */     if (this.preserveVisuals.isState()) {
/* 127 */       updateVisualization(now);
/*     */     }
/*     */   }
/*     */   
/*     */   private void tickFrozenBots() {
/* 132 */     for (BotSessionManager.BotConnection bot : BotSessionManager.getConnections()) {
/* 133 */       SessionState state = bot.autoForestState();
/* 134 */       if (state == null || !state.enabled() || bot.player() == null || bot.world() == null || bot.handler() == null) {
/*     */         continue;
/*     */       }
/*     */       
/*     */       try {
/* 139 */         tickBotSession(bot, state);
/* 140 */       } catch (Exception ignored) {
/* 141 */         state.enabled(false);
/* 142 */         state.targetPos(null);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void tickBotSession(BotSessionManager.BotConnection bot, SessionState state) {
/* 148 */     if (bot.player().method_31481() || !bot.player().method_5805()) {
/* 149 */       state.enabled(false);
/* 150 */       state.targetPos(null);
/*     */       
/*     */       return;
/*     */     } 
/* 154 */     long now = System.currentTimeMillis();
/* 155 */     long scheduleDelay = Math.max(1000L, (long)(Math.max(1.0F, state.intervalSeconds()) * 500.0F));
/*     */     
/* 157 */     if (state.autoSell() && now - state.lastSellTime() >= scheduleDelay) {
/* 158 */       bot.handler().method_45730("sellwood");
/* 159 */       state.lastSellTime(now);
/*     */     } 
/*     */     
/* 162 */     if (state.autoPay()) {
/* 163 */       if (state.payTarget().isBlank()) {
/* 164 */         if (now - state.lastNickReminderTime() >= 5000L) {
/* 165 */           state.lastNickReminderTime(now);
/*     */         }
/* 167 */       } else if (now - state.lastPayTime() >= scheduleDelay + 200L) {
/* 168 */         bot.handler().method_45730("pay " + state.payTarget() + " " + (int)state.payAmount());
/* 169 */         state.lastPayTime(now);
/*     */       } 
/*     */     }
/*     */     
/* 173 */     if (state.targetPos() != null && (!isLog(bot.world(), state.targetPos()) || !isInRange(bot.player(), state.targetPos()) || !isVisible(bot.world(), bot.player(), state.targetPos()))) {
/* 174 */       state.targetPos(null);
/*     */     }
/*     */     
/* 177 */     if (state.targetPos() == null) {
/* 178 */       state.targetPos(findNearestLog(bot.world(), bot.player(), state.breakRadius()));
/*     */     }
/*     */     
/* 181 */     if (state.targetPos() == null) {
/*     */       return;
/*     */     }
/*     */     
/* 185 */     if ("fast".equals(state.modeAlias())) {
/* 186 */       long interval = Math.max(1L, (long)(1000.0F / Math.max(1.0F, state.packetsPerSecond())));
/* 187 */       if (now - state.lastPacketTime() < interval) {
/*     */         return;
/*     */       }
/*     */       
/* 191 */       performFastBreak(bot.handler(), bot.interactionManager(), bot.player(), bot.world(), state.targetPos(), state.swing());
/* 192 */       state.lastPacketTime(now);
/*     */       
/*     */       return;
/*     */     } 
/* 196 */     if (now - state.lastBreakTime() < 3L) {
/*     */       return;
/*     */     }
/*     */     
/* 200 */     if (bot.interactionManager() != null) {
/* 201 */       bot.interactionManager().method_2910(state.targetPos(), class_2350.field_11036);
/* 202 */       bot.interactionManager().method_2902(state.targetPos(), class_2350.field_11036);
/*     */     } else {
/* 204 */       performFastBreak(bot.handler(), bot.interactionManager(), bot.player(), bot.world(), state.targetPos(), state.swing());
/*     */     } 
/*     */     
/* 207 */     if (state.swing()) {
/* 208 */       bot.handler().method_52787((class_2596)new class_2879(class_1268.field_5808));
/*     */     }
/* 210 */     state.lastBreakTime(now);
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onPacket(EventPacket event) {
/* 215 */     if (!this.currentSessionEnabled || !this.preserveVisuals.isState() || mc.field_1724 == null || mc.field_1687 == null) {
/*     */       return;
/*     */     }
/*     */     
/* 219 */     if (event.getType() == EventPacket.Type.SEND) { class_2596 class_2596 = event.getPacket(); if (class_2596 instanceof class_2846) { class_2846 packet = (class_2846)class_2596;
/* 220 */         handleDigPacket(packet);
/*     */         return; }
/*     */        }
/*     */     
/* 224 */     if (event.getType() == EventPacket.Type.RECEIVE) { class_2596 class_2596 = event.getPacket(); if (class_2596 instanceof class_2626) { class_2626 packet = (class_2626)class_2596;
/* 225 */         class_2338 pos = packet.method_11309();
/* 226 */         class_2680 savedState = this.preservedBlocks.get(pos);
/* 227 */         if (savedState == null) {
/*     */           return;
/*     */         }
/*     */         
/* 231 */         class_2680 serverState = packet.method_11308();
/* 232 */         if (serverState.method_26215() || !serverState.equals(savedState)) {
/* 233 */           event.cancel();
/* 234 */           setClientBlock(pos, savedState);
/* 235 */           this.lastUpdateTime.put(pos, Long.valueOf(System.currentTimeMillis()));
/*     */         }  }
/*     */        }
/*     */   
/*     */   }
/*     */   private void breakTarget(long now) {
/* 241 */     if (this.targetPos == null || mc.field_1724 == null || mc.field_1724.field_3944 == null || mc.field_1761 == null) {
/*     */       return;
/*     */     }
/*     */     
/* 245 */     if (this.breakMode.is("Быстрый")) {
/* 246 */       long interval = Math.max(1L, (long)(1000.0F / Math.max(1.0F, this.packetsPerSecond.get())));
/* 247 */       if (now - this.lastPacketTime < interval) {
/*     */         return;
/*     */       }
/*     */       
/* 251 */       performFastBreak(this.targetPos);
/* 252 */       this.lastPacketTime = now;
/*     */       
/*     */       return;
/*     */     } 
/* 256 */     if (now - this.lastBreakTime < 3L) {
/*     */       return;
/*     */     }
/*     */     
/* 260 */     mc.field_1761.method_2910(this.targetPos, class_2350.field_11036);
/* 261 */     mc.field_1761.method_2902(this.targetPos, class_2350.field_11036);
/* 262 */     if (this.swing.isState()) {
/* 263 */       mc.field_1724.method_6104(class_1268.field_5808);
/*     */     }
/* 265 */     this.lastBreakTime = now;
/*     */   }
/*     */   
/*     */   private void performFastBreak(class_2338 pos) {
/* 269 */     if (mc.field_1724 == null || mc.field_1687 == null || mc.field_1724.field_3944 == null) {
/*     */       return;
/*     */     }
/*     */     
/* 273 */     performFastBreak(mc.field_1724.field_3944, mc.field_1761, mc.field_1724, mc.field_1687, pos, this.swing.isState());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void performFastBreak(class_634 handler, class_636 interactionManager, class_746 player, class_638 world, class_2338 pos, boolean shouldSwing) {
/* 282 */     if (handler == null || player == null || pos == null) {
/*     */       return;
/*     */     }
/*     */     
/* 286 */     boolean accelerated = false;
/* 287 */     if (interactionManager != null && world != null) {
/* 288 */       interactionManager.method_2910(pos, class_2350.field_11036);
/* 289 */       accelerated = FastBreak.accelerateClientBreak(interactionManager, player, world, pos, class_2350.field_11036, 1.0F, shouldSwing);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 300 */     if (!accelerated) {
/* 301 */       FastBreak.packetBreak(handler, player, pos, class_2350.field_11036, shouldSwing);
/*     */     }
/*     */   }
/*     */   
/*     */   private class_2338 findNearestLog() {
/* 306 */     return findNearestLog(mc.field_1687, mc.field_1724, this.breakRadius.get());
/*     */   }
/*     */   
/*     */   private class_2338 findNearestLog(class_638 world, class_746 player, float radiusValue) {
/* 310 */     if (player == null || world == null) {
/* 311 */       return null;
/*     */     }
/*     */     
/* 314 */     class_2338 playerPos = player.method_24515();
/* 315 */     int radius = Math.round(radiusValue);
/*     */     
/* 317 */     return class_2338.method_20437(playerPos
/* 318 */         .method_10069(-radius, -radius, -radius), playerPos
/* 319 */         .method_10069(radius, radius, radius))
/*     */       
/* 321 */       .map(class_2338::method_10062)
/* 322 */       .filter(pos -> isLog(world, pos))
/* 323 */       .filter(pos -> isInRange(player, pos))
/* 324 */       .filter(pos -> isVisible(world, player, pos))
/* 325 */       .min(Comparator.comparingDouble(pos -> player.method_5707(class_243.method_24953((class_2382)pos))))
/* 326 */       .orElse(null);
/*     */   }
/*     */   
/*     */   private boolean isInRange(class_2338 pos) {
/* 330 */     return isInRange(mc.field_1724, pos);
/*     */   }
/*     */   
/*     */   private boolean isInRange(class_746 player, class_2338 pos) {
/* 334 */     return (player != null && player.method_5707(class_243.method_24953((class_2382)pos)) <= 16.0D);
/*     */   }
/*     */   
/*     */   private boolean isVisible(class_2338 pos) {
/* 338 */     return isVisible(mc.field_1687, mc.field_1724, pos);
/*     */   }
/*     */   
/*     */   private boolean isVisible(class_638 world, class_746 player, class_2338 pos) {
/* 342 */     if (player == null || world == null) {
/* 343 */       return false;
/*     */     }
/*     */     
/* 346 */     class_243 eyePos = player.method_33571();
/* 347 */     class_243 targetCenter = class_243.method_24953((class_2382)pos);
/* 348 */     class_3965 hit = world.method_17742(new class_3959(eyePos, targetCenter, class_3959.class_3960.field_17558, class_3959.class_242.field_1348, (class_1297)player));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 356 */     return (hit == null || hit.method_17783() == class_239.class_240.field_1333 || pos.equals(hit.method_17777()));
/*     */   }
/*     */   
/*     */   private boolean isLog(class_2338 pos) {
/* 360 */     return isLog(mc.field_1687, pos);
/*     */   }
/*     */   
/*     */   private boolean isLog(class_638 world, class_2338 pos) {
/* 364 */     return (world != null && world.method_8320(pos).method_26164(class_3481.field_15475));
/*     */   }
/*     */   
/*     */   private void handleDigPacket(class_2846 packet) {
/* 368 */     class_2846.class_2847 action = packet.method_12363();
/* 369 */     if (action != class_2846.class_2847.field_12968 && action != class_2846.class_2847.field_12973) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 374 */     class_2338 pos = packet.method_12362();
/* 375 */     if (!isLog(pos)) {
/*     */       return;
/*     */     }
/*     */     
/* 379 */     class_2680 state = mc.field_1687.method_8320(pos);
/* 380 */     if (state.method_26215()) {
/*     */       return;
/*     */     }
/*     */     
/* 384 */     this.preservedBlocks.put(pos, state);
/* 385 */     this.managedBlocks.add(pos);
/* 386 */     this.lastUpdateTime.put(pos, Long.valueOf(System.currentTimeMillis()));
/* 387 */     setClientBlock(pos, state);
/*     */   }
/*     */   
/*     */   private void updateVisualization(long now) {
/* 391 */     class_638 clientWorld, class_6381 = mc.field_1687; if (class_6381 instanceof class_638) { clientWorld = class_6381; }
/*     */     else
/*     */     { return; }
/*     */     
/* 395 */     Set<class_2338> toRemove = new HashSet<>();
/*     */     
/* 397 */     for (Map.Entry<class_2338, class_2680> entry : this.preservedBlocks.entrySet()) {
/* 398 */       class_2338 pos = entry.getKey();
/* 399 */       class_2680 savedState = entry.getValue();
/* 400 */       class_2680 currentState = clientWorld.method_8320(pos);
/*     */       
/* 402 */       if (currentState == null || !currentState.equals(savedState)) {
/* 403 */         clientWorld.method_8652(pos, savedState, 0);
/* 404 */         this.lastUpdateTime.put(pos, Long.valueOf(now));
/*     */       } 
/*     */       
/* 407 */       Long lastSeen = this.lastUpdateTime.get(pos);
/* 408 */       if (lastSeen != null && now - lastSeen.longValue() > 300000L) {
/* 409 */         toRemove.add(pos);
/*     */       }
/*     */     } 
/*     */     
/* 413 */     for (class_2338 pos : toRemove) {
/* 414 */       this.preservedBlocks.remove(pos);
/* 415 */       this.lastUpdateTime.remove(pos);
/* 416 */       this.managedBlocks.remove(pos);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void restoreVisualState() {
/* 421 */     class_638 clientWorld, class_6381 = mc.field_1687; if (class_6381 instanceof class_638) { clientWorld = class_6381; }
/* 422 */     else { this.preservedBlocks.clear();
/* 423 */       this.lastUpdateTime.clear();
/* 424 */       this.managedBlocks.clear();
/*     */       
/*     */       return; }
/*     */     
/* 428 */     for (class_2338 pos : this.managedBlocks) {
/* 429 */       clientWorld.method_8652(pos, mc.field_1687.method_8320(pos), 0);
/*     */     }
/*     */     
/* 432 */     this.preservedBlocks.clear();
/* 433 */     this.lastUpdateTime.clear();
/* 434 */     this.managedBlocks.clear();
/*     */   }
/*     */   
/*     */   private void setClientBlock(class_2338 pos, class_2680 state) {
/* 438 */     class_638 class_638 = mc.field_1687; if (class_638 instanceof class_638) { class_638 clientWorld = class_638;
/* 439 */       clientWorld.method_8652(pos, state, 0); }
/*     */   
/*     */   }
/*     */   
/*     */   public List<String> getModeSuggestions() {
/* 444 */     return List.of("normal", "fast");
/*     */   }
/*     */   
/*     */   public boolean setModeAlias(String alias) {
/* 448 */     if (alias == null || alias.isBlank()) {
/* 449 */       return false;
/*     */     }
/*     */     
/* 452 */     switch (alias.trim().toLowerCase(Locale.ROOT)) { case "normal": case "default":
/*     */       case "обычный":
/* 454 */         this.breakMode.set(this.breakMode.getMods().get(0));
/*     */ 
/*     */ 
/*     */       
/*     */       case "fast":
/*     */       case "quick":
/*     */       case "быстрый":
/* 461 */         this.breakMode.set(this.breakMode.getMods().get(1));
/*     */         return !(this.breakMode.getMods().size() < 2); }
/*     */     
/*     */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getModeAlias() {
/* 469 */     if (this.breakMode.getMods().size() > 1 && this.breakMode.is(this.breakMode.getMods().get(1))) {
/* 470 */       return "fast";
/*     */     }
/* 472 */     return "normal";
/*     */   }
/*     */   
/*     */   public void enableForCurrentSession() {
/* 476 */     this.currentSessionEnabled = true;
/* 477 */     resetRuntimeState();
/* 478 */     restoreVisualState();
/*     */   }
/*     */   
/*     */   public void disableForCurrentSession() {
/* 482 */     this.currentSessionEnabled = false;
/* 483 */     restoreVisualState();
/* 484 */     resetRuntimeState();
/*     */   }
/*     */   
/*     */   public boolean isCurrentSessionEnabled() {
/* 488 */     return this.currentSessionEnabled;
/*     */   }
/*     */   
/*     */   public void setSwingEnabled(boolean value) {
/* 492 */     this.swing.setState(value);
/*     */   }
/*     */   
/*     */   public boolean isSwingEnabled() {
/* 496 */     return this.swing.isState();
/*     */   }
/*     */   
/*     */   public void setAutoSellEnabled(boolean value) {
/* 500 */     this.autoSell.setState(value);
/*     */   }
/*     */   
/*     */   public boolean isAutoSellEnabled() {
/* 504 */     return this.autoSell.isState();
/*     */   }
/*     */   
/*     */   public void setAutoPayEnabled(boolean value) {
/* 508 */     this.autoPay.setState(value);
/* 509 */     if (!value) {
/* 510 */       this.lastNickReminderTime = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isAutoPayEnabled() {
/* 515 */     return this.autoPay.isState();
/*     */   }
/*     */   
/*     */   public void setPreserveVisualsEnabled(boolean value) {
/* 519 */     this.preserveVisuals.setState(value);
/*     */   }
/*     */   
/*     */   public boolean isPreserveVisualsEnabled() {
/* 523 */     return this.preserveVisuals.isState();
/*     */   }
/*     */   
/*     */   public void setPacketsPerSecond(float value) {
/* 527 */     this.packetsPerSecond.setValue(value);
/*     */   }
/*     */   
/*     */   public float getPacketsPerSecond() {
/* 531 */     return this.packetsPerSecond.get();
/*     */   }
/*     */   
/*     */   public void setBreakRadius(float value) {
/* 535 */     this.breakRadius.setValue(value);
/*     */   }
/*     */   
/*     */   public float getBreakRadius() {
/* 539 */     return this.breakRadius.get();
/*     */   }
/*     */   
/*     */   public void setPayAmount(float value) {
/* 543 */     this.payAmount.setValue(value);
/*     */   }
/*     */   
/*     */   public float getPayAmount() {
/* 547 */     return this.payAmount.get();
/*     */   }
/*     */   
/*     */   public void setIntervalSeconds(float value) {
/* 551 */     this.intervalSeconds.setValue(value);
/*     */   }
/*     */   
/*     */   public float getIntervalSeconds() {
/* 555 */     return this.intervalSeconds.get();
/*     */   }
/*     */   
/*     */   public boolean setPayTarget(String target) {
/* 559 */     String trimmed = (target == null) ? "" : target.trim();
/* 560 */     if (trimmed.isEmpty()) {
/* 561 */       return false;
/*     */     }
/*     */     
/* 564 */     this.payTarget = trimmed;
/* 565 */     this.lastNickReminderTime = 0L;
/* 566 */     return true;
/*     */   }
/*     */   
/*     */   public String getPayTarget() {
/* 570 */     return this.payTarget;
/*     */   }
/*     */   
/*     */   public boolean capturePayTargetFromChat(String message) {
/* 574 */     return false;
/*     */   }
/*     */   
/*     */   public void clearPayTarget() {
/* 578 */     this.payTarget = "";
/* 579 */     this.lastNickReminderTime = 0L;
/*     */   }
/*     */   
/*     */   public SessionState captureState() {
/* 583 */     SessionState state = new SessionState();
/* 584 */     state.enabled(this.currentSessionEnabled);
/* 585 */     state.modeAlias(getModeAlias());
/* 586 */     state.packetsPerSecond(this.packetsPerSecond.get());
/* 587 */     state.breakRadius(this.breakRadius.get());
/* 588 */     state.swing(this.swing.isState());
/* 589 */     state.autoSell(this.autoSell.isState());
/* 590 */     state.autoPay(this.autoPay.isState());
/* 591 */     state.preserveVisuals(this.preserveVisuals.isState());
/* 592 */     state.payAmount(this.payAmount.get());
/* 593 */     state.intervalSeconds(this.intervalSeconds.get());
/* 594 */     state.payTarget(this.payTarget);
/* 595 */     state.targetPos(this.targetPos);
/* 596 */     state.lastBreakTime(this.lastBreakTime);
/* 597 */     state.lastPacketTime(this.lastPacketTime);
/* 598 */     state.lastSellTime(this.lastSellTime);
/* 599 */     state.lastPayTime(this.lastPayTime);
/* 600 */     state.lastNickReminderTime(this.lastNickReminderTime);
/* 601 */     state.preservedBlocks(new HashMap<>(this.preservedBlocks));
/* 602 */     state.lastUpdateTime(new HashMap<>(this.lastUpdateTime));
/* 603 */     state.managedBlocks(new HashSet<>(this.managedBlocks));
/* 604 */     return state;
/*     */   }
/*     */   
/*     */   public void applyState(SessionState state) {
/* 608 */     if (state == null) {
/* 609 */       resetToDefaults();
/*     */       
/*     */       return;
/*     */     } 
/* 613 */     this.currentSessionEnabled = state.enabled();
/* 614 */     setModeAlias(state.modeAlias());
/* 615 */     this.packetsPerSecond.setValue(state.packetsPerSecond());
/* 616 */     this.breakRadius.setValue(state.breakRadius());
/* 617 */     this.swing.setState(state.swing());
/* 618 */     this.autoSell.setState(state.autoSell());
/* 619 */     this.autoPay.setState(state.autoPay());
/* 620 */     this.preserveVisuals.setState(state.preserveVisuals());
/* 621 */     this.payAmount.setValue(state.payAmount());
/* 622 */     this.intervalSeconds.setValue(state.intervalSeconds());
/* 623 */     this.payTarget = state.payTarget();
/* 624 */     this.targetPos = state.targetPos();
/* 625 */     this.lastBreakTime = state.lastBreakTime();
/* 626 */     this.lastPacketTime = state.lastPacketTime();
/* 627 */     this.lastSellTime = state.lastSellTime();
/* 628 */     this.lastPayTime = state.lastPayTime();
/* 629 */     this.lastNickReminderTime = state.lastNickReminderTime();
/* 630 */     this.preservedBlocks.clear();
/* 631 */     this.preservedBlocks.putAll(state.preservedBlocks());
/* 632 */     this.lastUpdateTime.clear();
/* 633 */     this.lastUpdateTime.putAll(state.lastUpdateTime());
/* 634 */     this.managedBlocks.clear();
/* 635 */     this.managedBlocks.addAll(state.managedBlocks());
/*     */   }
/*     */   
/*     */   public void resetToDefaults() {
/* 639 */     this.currentSessionEnabled = false;
/* 640 */     setModeAlias("normal");
/* 641 */     this.packetsPerSecond.setValue(100.0F);
/* 642 */     this.breakRadius.setValue(4.0F);
/* 643 */     this.swing.setState(true);
/* 644 */     this.autoSell.setState(true);
/* 645 */     this.autoPay.setState(false);
/* 646 */     this.preserveVisuals.setState(true);
/* 647 */     this.payAmount.setValue(1000.0F);
/* 648 */     this.intervalSeconds.setValue(20.0F);
/* 649 */     this.payTarget = "";
/* 650 */     restoreVisualState();
/* 651 */     resetRuntimeState();
/*     */   }
/*     */   
/*     */   private void resetRuntimeState() {
/* 655 */     this.targetPos = null;
/* 656 */     this.lastBreakTime = 0L;
/* 657 */     this.lastPacketTime = 0L;
/* 658 */     this.lastSellTime = 0L;
/* 659 */     this.lastPayTime = 0L;
/* 660 */     this.lastNickReminderTime = 0L;
/* 661 */     this.preservedBlocks.clear();
/* 662 */     this.lastUpdateTime.clear();
/* 663 */     this.managedBlocks.clear();
/*     */   }
/*     */   
/*     */   public static final class SessionState {
/*     */     private boolean enabled;
/* 668 */     private String modeAlias = "normal";
/* 669 */     private float packetsPerSecond = 100.0F;
/* 670 */     private float breakRadius = 4.0F;
/*     */     private boolean swing = true;
/*     */     private boolean autoSell = true;
/*     */     private boolean autoPay;
/*     */     private boolean preserveVisuals = true;
/* 675 */     private float payAmount = 1000.0F;
/* 676 */     private float intervalSeconds = 20.0F;
/* 677 */     private String payTarget = "";
/*     */     private class_2338 targetPos;
/*     */     private long lastBreakTime;
/*     */     private long lastPacketTime;
/*     */     private long lastSellTime;
/*     */     private long lastPayTime;
/*     */     private long lastNickReminderTime;
/* 684 */     private Map<class_2338, class_2680> preservedBlocks = new HashMap<>();
/* 685 */     private Map<class_2338, Long> lastUpdateTime = new HashMap<>();
/* 686 */     private Set<class_2338> managedBlocks = new HashSet<>();
/*     */     
/* 688 */     public boolean enabled() { return this.enabled; }
/* 689 */     public void enabled(boolean value) { this.enabled = value; }
/* 690 */     public String modeAlias() { return this.modeAlias; }
/* 691 */     public void modeAlias(String value) { this.modeAlias = (value == null) ? "normal" : value; }
/* 692 */     public float packetsPerSecond() { return this.packetsPerSecond; }
/* 693 */     public void packetsPerSecond(float value) { this.packetsPerSecond = value; }
/* 694 */     public float breakRadius() { return this.breakRadius; }
/* 695 */     public void breakRadius(float value) { this.breakRadius = value; }
/* 696 */     public boolean swing() { return this.swing; }
/* 697 */     public void swing(boolean value) { this.swing = value; }
/* 698 */     public boolean autoSell() { return this.autoSell; }
/* 699 */     public void autoSell(boolean value) { this.autoSell = value; }
/* 700 */     public boolean autoPay() { return this.autoPay; }
/* 701 */     public void autoPay(boolean value) { this.autoPay = value; }
/* 702 */     public boolean preserveVisuals() { return this.preserveVisuals; }
/* 703 */     public void preserveVisuals(boolean value) { this.preserveVisuals = value; }
/* 704 */     public float payAmount() { return this.payAmount; }
/* 705 */     public void payAmount(float value) { this.payAmount = value; }
/* 706 */     public float intervalSeconds() { return this.intervalSeconds; }
/* 707 */     public void intervalSeconds(float value) { this.intervalSeconds = value; }
/* 708 */     public String payTarget() { return (this.payTarget == null) ? "" : this.payTarget; }
/* 709 */     public void payTarget(String value) { this.payTarget = (value == null) ? "" : value; }
/* 710 */     public class_2338 targetPos() { return this.targetPos; }
/* 711 */     public void targetPos(class_2338 value) { this.targetPos = value; }
/* 712 */     public long lastBreakTime() { return this.lastBreakTime; }
/* 713 */     public void lastBreakTime(long value) { this.lastBreakTime = value; }
/* 714 */     public long lastPacketTime() { return this.lastPacketTime; }
/* 715 */     public void lastPacketTime(long value) { this.lastPacketTime = value; }
/* 716 */     public long lastSellTime() { return this.lastSellTime; }
/* 717 */     public void lastSellTime(long value) { this.lastSellTime = value; }
/* 718 */     public long lastPayTime() { return this.lastPayTime; }
/* 719 */     public void lastPayTime(long value) { this.lastPayTime = value; }
/* 720 */     public long lastNickReminderTime() { return this.lastNickReminderTime; }
/* 721 */     public void lastNickReminderTime(long value) { this.lastNickReminderTime = value; }
/* 722 */     public Map<class_2338, class_2680> preservedBlocks() { return this.preservedBlocks; }
/* 723 */     public void preservedBlocks(Map<class_2338, class_2680> value) { this.preservedBlocks = (value == null) ? new HashMap<>() : value; }
/* 724 */     public Map<class_2338, Long> lastUpdateTime() { return this.lastUpdateTime; }
/* 725 */     public void lastUpdateTime(Map<class_2338, Long> value) { this.lastUpdateTime = (value == null) ? new HashMap<>() : value; }
/* 726 */     public Set<class_2338> managedBlocks() { return this.managedBlocks; } public void managedBlocks(Set<class_2338> value) {
/* 727 */       this.managedBlocks = (value == null) ? new HashSet<>() : value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\player\AutoForest.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */