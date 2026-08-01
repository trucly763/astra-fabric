/*     */ package shame.astra.client.modules.impl.combat;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.class_1297;
/*     */ import net.minecraft.class_1304;
/*     */ import net.minecraft.class_1657;
/*     */ import net.minecraft.class_1713;
/*     */ import net.minecraft.class_1799;
/*     */ import net.minecraft.class_1802;
/*     */ import net.minecraft.class_2596;
/*     */ import net.minecraft.class_2815;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.EventMoveInput;
/*     */ import shame.astra.api.events.implement.EventUpdate;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.impl.movement.Sprint;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*     */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*     */ import shame.astra.client.modules.settings.implement.ListSetting;
/*     */ import shame.astra.client.modules.settings.implement.ModeSetting;
/*     */ 
/*     */ public class AutoTotem extends Module {
/*  23 */   public static AutoTotem INSTANCE = new AutoTotem();
/*     */   
/*  25 */   private final ListSetting triggers = new ListSetting("Брать от", new BooleanSetting[] { new BooleanSetting("Кристалл рядом", true), new BooleanSetting("Кристалл в руке", true), new BooleanSetting("Обсидиан в руке", true), new BooleanSetting("Падения", true) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  31 */   private final FloatSetting hp = new FloatSetting("Брать от хп", 6.0F, 1.0F, 20.0F, 0.5F);
/*  32 */   private final FloatSetting hpOnElytra = new FloatSetting("Хп на элитрах", 10.0F, 1.0F, 20.0F, 0.5F);
/*  33 */   private final FloatSetting crystalRadius = (new FloatSetting("Радиус кристалла", 6.0F, 1.0F, 12.0F, 0.5F))
/*  34 */     .visible(this::isCrystalRadiusVisible);
/*  35 */   private final FloatSetting fallHeight = (new FloatSetting("Высота падения", 10.0F, 3.0F, 50.0F, 1.0F))
/*  36 */     .visible(() -> Boolean.valueOf(this.triggers.is("Падения")));
/*  37 */   private final BooleanSetting saveEnchanted = new BooleanSetting("Сохранять зачар", true);
/*  38 */   private final BooleanSetting returnTotem = new BooleanSetting("Возвращать тотем", true);
/*     */   
/*     */   private final FloatSetting returnDelay;
/*     */   
/*     */   private final BooleanSetting bypassgrim;
/*     */   private final ModeSetting swapVersion;
/*     */   private int bypassTicks;
/*     */   private boolean sprintPaused;
/*     */   private int swapCooldown;
/*     */   private int savedTotemSlot;
/*     */   private class_1799 originalOffhandItem;
/*     */   private boolean totemTakenByUs;
/*     */   private boolean returnMode;
/*     */   private boolean needFastSwap;
/*     */   private int safeTicks;
/*     */   
/*     */   public AutoTotem() {
/*  55 */     super("AutoTotem", "Автоматически берёт тотем в опасности", Module.ModuleCategory.COMBAT); Objects.requireNonNull(this.returnTotem); this.returnDelay = (new FloatSetting("Задержка возврата", 20.0F, 5.0F, 100.0F, 5.0F)).visible(this.returnTotem::isState); this.bypassgrim = new BooleanSetting("Обходить Grim", true); this.swapVersion = new ModeSetting("Версия свапа", "1.21.4", new String[] { "1.21.4", "1.16.5" }); this.savedTotemSlot = -1; this.originalOffhandItem = class_1799.field_8037; this.totemTakenByUs = false; this.returnMode = false; this.needFastSwap = false; this.safeTicks = 0;
/*  56 */     addSettings(new Setting[] { (Setting)this.hp, (Setting)this.hpOnElytra, (Setting)this.saveEnchanted, (Setting)this.bypassgrim, (Setting)this.returnTotem, (Setting)this.swapVersion, (Setting)this.returnDelay, (Setting)this.triggers, (Setting)this.crystalRadius, (Setting)this.fallHeight });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @EventLink
/*     */   public void onInput(EventMoveInput e) {
/*  72 */     if (this.bypassgrim.isState() && this.bypassTicks > 0) {
/*  73 */       if (mc.field_1724 == null)
/*  74 */         return;  mc.field_1724.method_5728(false);
/*  75 */       e.setForward(0.0F);
/*  76 */       e.setStrafe(0.0F);
/*  77 */       e.setJump(false);
/*  78 */       e.setSneak(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onUpdate(EventUpdate e) {
/*  84 */     if (mc.field_1724 == null || mc.field_1687 == null)
/*     */       return; 
/*  86 */     boolean isCrystalDanger = isCrystalDanger();
/*     */     
/*  88 */     if (isCrystalDanger) {
/*  89 */       this.needFastSwap = true;
/*  90 */       this.safeTicks = 0;
/*     */     } 
/*     */     
/*  93 */     if (this.swapCooldown > 0) {
/*  94 */       this.swapCooldown--;
/*     */     }
/*     */     
/*  97 */     if (this.bypassgrim.isState() && this.bypassTicks > 0) {
/*  98 */       mc.field_1724.method_5728(false);
/*  99 */       this.bypassTicks--;
/*     */       
/* 101 */       if (this.bypassTicks <= 0) {
/* 102 */         if (this.returnMode) {
/* 103 */           performReturn();
/*     */         } else {
/* 105 */           performSwap();
/*     */         } 
/* 107 */         restoreSprint();
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 112 */     boolean needTotem = shouldTakeTotem(isCrystalDanger);
/*     */     
/* 114 */     if (needTotem && !hasTotemInOffhand()) {
/* 115 */       int totemSlot = findTotemSlot();
/* 116 */       if (totemSlot == -1)
/*     */         return; 
/* 118 */       if (!this.needFastSwap && this.swapCooldown > 0)
/*     */         return; 
/* 120 */       if (this.originalOffhandItem.method_7960() && !this.totemTakenByUs) {
/* 121 */         this.originalOffhandItem = mc.field_1724.method_6079().method_7972();
/*     */       }
/*     */       
/* 124 */       this.savedTotemSlot = totemSlot;
/* 125 */       this.returnMode = false;
/* 126 */       this.safeTicks = 0;
/*     */       
/* 128 */       if (this.bypassgrim.isState()) {
/* 129 */         disableSprint();
/* 130 */         this.bypassTicks = this.needFastSwap ? 1 : 2;
/* 131 */         this.swapCooldown = this.needFastSwap ? 0 : 2;
/*     */       } else {
/* 133 */         performSwap();
/* 134 */         this.swapCooldown = this.needFastSwap ? 0 : 2;
/*     */       } 
/*     */     } 
/*     */     
/* 138 */     boolean isSafe = !needTotem;
/*     */     
/* 140 */     if (isSafe) {
/* 141 */       this.safeTicks++;
/*     */     } else {
/* 143 */       this.safeTicks = 0;
/*     */     } 
/*     */     
/* 146 */     if (this.returnTotem.isState() && !needTotem && hasTotemInOffhand() && this.totemTakenByUs && this.safeTicks >= this.returnDelay
/* 147 */       .getValue().intValue()) {
/* 148 */       if (!this.needFastSwap && this.swapCooldown > 0)
/*     */         return; 
/* 150 */       this.returnMode = true;
/*     */       
/* 152 */       if (this.bypassgrim.isState()) {
/* 153 */         disableSprint();
/* 154 */         this.bypassTicks = this.needFastSwap ? 1 : 2;
/* 155 */         this.swapCooldown = this.needFastSwap ? 0 : 2;
/*     */       } else {
/* 157 */         performReturn();
/* 158 */         this.swapCooldown = this.needFastSwap ? 0 : 2;
/*     */       } 
/*     */     } 
/*     */     
/* 162 */     if (!isCrystalDanger) {
/* 163 */       this.needFastSwap = false;
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean isCrystalDanger() {
/* 168 */     float radius = this.crystalRadius.getValue().floatValue();
/* 169 */     double radiusSq = (radius * radius);
/*     */     
/* 171 */     if (this.triggers.is("Кристалл рядом")) {
/* 172 */       for (class_1297 entity : mc.field_1687.method_18112()) {
/* 173 */         if (entity instanceof net.minecraft.class_1511 && 
/* 174 */           mc.field_1724.method_5858(entity) <= radiusSq) {
/* 175 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 181 */     if (this.triggers.is("Кристалл в руке")) {
/* 182 */       for (class_1657 player : mc.field_1687.method_18456()) {
/* 183 */         if (player != mc.field_1724 && 
/* 184 */           mc.field_1724.method_5858((class_1297)player) <= radiusSq && (
/* 185 */           player.method_6047().method_31574(class_1802.field_8301) || player
/* 186 */           .method_6079().method_31574(class_1802.field_8301))) {
/* 187 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 193 */     return false;
/*     */   }
/*     */   
/*     */   private boolean shouldTakeTotem(boolean isCrystalDanger) {
/* 197 */     float currentHp = mc.field_1724.method_6032() + mc.field_1724.method_6067();
/* 198 */     boolean isGliding = (mc.field_1724.method_6118(class_1304.field_6174).method_31574(class_1802.field_8833) && mc.field_1724.method_6128());
/*     */     
/* 200 */     float hpThreshold = isGliding ? this.hpOnElytra.getValue().floatValue() : this.hp.getValue().floatValue();
/*     */     
/* 202 */     if (currentHp <= hpThreshold) {
/* 203 */       return true;
/*     */     }
/*     */     
/* 206 */     if (isCrystalDanger) {
/* 207 */       return true;
/*     */     }
/*     */     
/* 210 */     float radius = this.crystalRadius.getValue().floatValue();
/* 211 */     double radiusSq = (radius * radius);
/*     */     
/* 213 */     if (this.triggers.is("Обсидиан в руке")) {
/* 214 */       for (class_1657 player : mc.field_1687.method_18456()) {
/* 215 */         if (player != mc.field_1724 && 
/* 216 */           mc.field_1724.method_5858((class_1297)player) <= radiusSq && (
/* 217 */           player.method_6047().method_31574(class_1802.field_8281) || player.method_6079().method_31574(class_1802.field_8281))) {
/* 218 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 224 */     if (this.triggers.is("Падения") && 
/* 225 */       mc.field_1724.field_6017 >= this.fallHeight.getValue().floatValue() && !isGliding) {
/* 226 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 230 */     return false;
/*     */   }
/*     */   
/*     */   private boolean hasTotemInOffhand() {
/* 234 */     return mc.field_1724.method_6079().method_31574(class_1802.field_8288);
/*     */   }
/*     */   
/*     */   private int findTotemSlot() {
/* 238 */     int normalTotem = -1;
/* 239 */     int enchantedTotem = -1;
/*     */     
/* 241 */     for (int i = 9; i < 45; i++) {
/* 242 */       class_1799 stack = mc.field_1724.field_7498.method_7611(i).method_7677();
/* 243 */       if (stack.method_31574(class_1802.field_8288)) {
/* 244 */         boolean isEnchanted = stack.method_7942();
/*     */         
/* 246 */         if (isEnchanted) {
/* 247 */           if (enchantedTotem == -1) {
/* 248 */             enchantedTotem = i;
/*     */           }
/* 250 */         } else if (normalTotem == -1) {
/* 251 */           normalTotem = i;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 256 */     if (this.saveEnchanted.isState()) {
/* 257 */       return (normalTotem != -1) ? normalTotem : enchantedTotem;
/*     */     }
/* 259 */     return (enchantedTotem != -1) ? enchantedTotem : normalTotem;
/*     */   }
/*     */ 
/*     */   
/*     */   private void performSwap() {
/* 264 */     int totemSlot = findTotemSlot();
/*     */     
/* 266 */     if (totemSlot == -1) {
/*     */       return;
/*     */     }
/*     */     
/* 270 */     this.savedTotemSlot = totemSlot;
/* 271 */     doSwap(totemSlot);
/* 272 */     this.totemTakenByUs = true;
/* 273 */     mc.field_1724.field_3944.method_52787((class_2596)new class_2815(0));
/*     */   }
/*     */   
/*     */   private void performReturn() {
/* 277 */     if (!hasTotemInOffhand()) {
/* 278 */       this.totemTakenByUs = false;
/*     */       
/*     */       return;
/*     */     } 
/* 282 */     if (!this.originalOffhandItem.method_7960()) {
/* 283 */       int slotToReturn = findSlotForItem(this.originalOffhandItem);
/* 284 */       if (slotToReturn != -1) {
/* 285 */         doSwap(slotToReturn);
/*     */       } else {
/* 287 */         if (this.savedTotemSlot == -1) {
/* 288 */           this.savedTotemSlot = 9;
/*     */         }
/* 290 */         doSwap(this.savedTotemSlot);
/*     */       } 
/*     */     } else {
/* 293 */       if (this.savedTotemSlot == -1) {
/* 294 */         this.savedTotemSlot = 9;
/*     */       }
/* 296 */       doSwap(this.savedTotemSlot);
/*     */     } 
/*     */     
/* 299 */     this.totemTakenByUs = false;
/* 300 */     this.savedTotemSlot = -1;
/* 301 */     this.originalOffhandItem = class_1799.field_8037;
/* 302 */     mc.field_1724.field_3944.method_52787((class_2596)new class_2815(0));
/*     */   }
/*     */   
/*     */   private int findSlotForItem(class_1799 item) {
/* 306 */     if (item.method_7960()) return -1;
/*     */     
/* 308 */     for (int i = 9; i < 45; i++) {
/* 309 */       class_1799 stack = mc.field_1724.field_7498.method_7611(i).method_7677();
/* 310 */       if (class_1799.method_7984(stack, item) && class_1799.method_7973(stack, item)) {
/* 311 */         return i;
/*     */       }
/*     */     } 
/* 314 */     return -1;
/*     */   }
/*     */   
/*     */   private void doSwap(int slot) {
/* 318 */     if (this.swapVersion.is("1.16.5")) {
/* 319 */       doSwap1165(slot);
/*     */       
/*     */       return;
/*     */     } 
/* 323 */     doSwap1214(slot);
/*     */   }
/*     */   
/*     */   private void doSwap1214(int slot) {
/* 327 */     if (slot >= 36 && slot <= 44) {
/* 328 */       int hotbarSlot = slot - 36;
/* 329 */       mc.field_1761.method_2906(0, 45, hotbarSlot, class_1713.field_7791, (class_1657)mc.field_1724);
/*     */     } else {
/* 331 */       mc.field_1761.method_2906(0, slot, 0, class_1713.field_7791, (class_1657)mc.field_1724);
/* 332 */       mc.field_1761.method_2906(0, 45, 0, class_1713.field_7791, (class_1657)mc.field_1724);
/* 333 */       mc.field_1761.method_2906(0, slot, 0, class_1713.field_7791, (class_1657)mc.field_1724);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void doSwap1165(int slot) {
/* 338 */     mc.field_1761.method_2906(0, slot, 40, class_1713.field_7791, (class_1657)mc.field_1724);
/*     */   }
/*     */   
/*     */   private void disableSprint() {
/* 342 */     if (this.sprintPaused) {
/*     */       return;
/*     */     }
/*     */     
/* 346 */     Sprint.pushPause(1000L);
/* 347 */     this.sprintPaused = true;
/*     */   }
/*     */   
/*     */   private void restoreSprint() {
/* 351 */     if (!this.sprintPaused) {
/*     */       return;
/*     */     }
/*     */     
/* 355 */     this.sprintPaused = false;
/* 356 */     Sprint.popPause();
/*     */   }
/*     */   
/*     */   private boolean isCrystalRadiusVisible() {
/* 360 */     return (this.triggers.is("Кристалл рядом") || this.triggers
/* 361 */       .is("Кристалл в руке") || this.triggers
/* 362 */       .is("Обсидиан в руке"));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 367 */     this.bypassTicks = 0;
/* 368 */     this.swapCooldown = 0;
/* 369 */     this.savedTotemSlot = -1;
/* 370 */     this.originalOffhandItem = class_1799.field_8037;
/* 371 */     this.totemTakenByUs = false;
/* 372 */     this.returnMode = false;
/* 373 */     this.needFastSwap = false;
/* 374 */     this.safeTicks = 0;
/* 375 */     restoreSprint();
/* 376 */     super.onDisable();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\combat\AutoTotem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */