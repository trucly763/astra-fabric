/*     */ package shame.astra.client.modules.impl.player;
/*     */ import net.minecraft.class_1268;
/*     */ import net.minecraft.class_1657;
/*     */ import net.minecraft.class_1713;
/*     */ import net.minecraft.class_1799;
/*     */ import net.minecraft.class_1802;
/*     */ import net.minecraft.class_2596;
/*     */ import net.minecraft.class_2815;
/*     */ import net.minecraft.class_2868;
/*     */ import net.minecraft.class_746;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.EventUpdate;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.impl.movement.Sprint;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*     */ 
/*     */ public class AutoEat extends Module {
/*  19 */   public static final AutoEat INSTANCE = new AutoEat();
/*     */   
/*     */   private static final String BARITONE_API_CLASS = "baritone.api.BaritoneAPI";
/*     */   
/*  23 */   private final FloatSetting hungerBars = new FloatSetting("Плашки голода", 6.0F, 1.0F, 10.0F, 1.0F);
/*     */   
/*     */   private boolean eating;
/*     */   private boolean sprintPaused;
/*     */   private boolean swappedFromInventory;
/*  28 */   private int originalSlot = -1;
/*  29 */   private int swappedInventorySlot = -1;
/*     */   
/*     */   public AutoEat() {
/*  32 */     super("AutoEat", "Автоматически ест при низком голоде", Module.ModuleCategory.PLAYER);
/*  33 */     addSettings(new Setting[] { (Setting)this.hungerBars });
/*     */   }
/*     */   
/*     */   public static boolean shouldSuppressCombat() {
/*  37 */     return (INSTANCE != null && INSTANCE.isEnable() && INSTANCE.eating);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  42 */     stopEating();
/*  43 */     super.onDisable();
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onUpdate(EventUpdate event) {
/*  48 */     if (mc.field_1724 == null || mc.field_1687 == null || mc.field_1761 == null) {
/*  49 */       stopEating();
/*     */       
/*     */       return;
/*     */     } 
/*  53 */     if (mc.field_1755 != null) {
/*  54 */       stopEating();
/*     */       
/*     */       return;
/*     */     } 
/*  58 */     if ((mc.field_1724.method_31549()).field_7477 || mc.field_1724.method_7325()) {
/*  59 */       stopEating();
/*     */       
/*     */       return;
/*     */     } 
/*  63 */     if (!this.eating) {
/*  64 */       if (!shouldStartEating()) {
/*     */         return;
/*     */       }
/*  67 */       this.eating = true;
/*  68 */       this.originalSlot = (mc.field_1724.method_31548()).field_7545;
/*     */     } 
/*     */     
/*  71 */     tickEating();
/*     */   }
/*     */   
/*     */   private void tickEating() {
/*  75 */     class_746 player = mc.field_1724;
/*  76 */     if (player == null) {
/*  77 */       stopEating();
/*     */       
/*     */       return;
/*     */     } 
/*  81 */     pauseBaritone();
/*     */     
/*  83 */     if (!this.sprintPaused) {
/*  84 */       Sprint.pushPause(0L);
/*  85 */       this.sprintPaused = true;
/*     */     } 
/*     */     
/*  88 */     mc.field_1690.field_1886.method_23481(false);
/*     */     
/*  90 */     if (!needsFood()) {
/*  91 */       if (!player.method_6115()) {
/*  92 */         stopEating();
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/*  97 */     if (!ensureFoodReady()) {
/*  98 */       stopEating();
/*     */       
/*     */       return;
/*     */     } 
/* 102 */     class_1268 eatingHand = getEatingHand(player);
/* 103 */     if (eatingHand == null) {
/* 104 */       stopEating();
/*     */       
/*     */       return;
/*     */     } 
/* 108 */     mc.field_1690.field_1904.method_23481(true);
/*     */     
/* 110 */     if (!player.method_6115() || player.method_6058() != eatingHand) {
/* 111 */       mc.field_1761.method_2919((class_1657)player, eatingHand);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean shouldStartEating() {
/* 116 */     return (needsFood() && !mc.field_1724.method_6115() && (isValidFood(mc.field_1724.method_6079()) || findFoodSlot() != -1));
/*     */   }
/*     */   
/*     */   private boolean needsFood() {
/* 120 */     return (mc.field_1724 != null && mc.field_1724
/* 121 */       .method_7344().method_7586() < 20 && mc.field_1724
/* 122 */       .method_7344().method_7586() <= getFoodThreshold());
/*     */   }
/*     */   
/*     */   private int getFoodThreshold() {
/* 126 */     return Math.round(this.hungerBars.get()) * 2;
/*     */   }
/*     */   
/*     */   private boolean ensureFoodReady() {
/* 130 */     class_746 player = mc.field_1724;
/* 131 */     if (player == null) {
/* 132 */       return false;
/*     */     }
/*     */     
/* 135 */     if (isValidFood(player.method_6079())) {
/* 136 */       return true;
/*     */     }
/*     */     
/* 139 */     if (isValidFood(player.method_6047())) {
/* 140 */       return true;
/*     */     }
/*     */     
/* 143 */     int foodSlot = findFoodSlot();
/* 144 */     if (foodSlot == -1) {
/* 145 */       return false;
/*     */     }
/*     */     
/* 148 */     if (foodSlot < 9) {
/* 149 */       this.swappedFromInventory = false;
/* 150 */       this.swappedInventorySlot = -1;
/* 151 */       selectHotbarSlot(foodSlot);
/* 152 */       return isValidFood(player.method_6047());
/*     */     } 
/*     */     
/* 155 */     selectHotbarSlot((this.originalSlot == -1) ? (player.method_31548()).field_7545 : this.originalSlot);
/* 156 */     swapInventorySlotWithHotbar(foodSlot, (player.method_31548()).field_7545);
/* 157 */     this.swappedFromInventory = true;
/* 158 */     this.swappedInventorySlot = foodSlot;
/* 159 */     return isValidFood(player.method_6047());
/*     */   }
/*     */   
/*     */   private class_1268 getEatingHand(class_746 player) {
/* 163 */     if (player == null) {
/* 164 */       return null;
/*     */     }
/* 166 */     if (isValidFood(player.method_6079())) {
/* 167 */       return class_1268.field_5810;
/*     */     }
/* 169 */     if (isValidFood(player.method_6047())) {
/* 170 */       return class_1268.field_5808;
/*     */     }
/* 172 */     return null;
/*     */   }
/*     */   
/*     */   private int findFoodSlot() {
/* 176 */     class_746 player = mc.field_1724;
/* 177 */     if (player == null) {
/* 178 */       return -1;
/*     */     }
/*     */     
/* 181 */     int selected = (player.method_31548()).field_7545;
/* 182 */     if (isValidFood(player.method_31548().method_5438(selected))) {
/* 183 */       return selected;
/*     */     }
/*     */     int slot;
/* 186 */     for (slot = 0; slot < 9; slot++) {
/* 187 */       if (slot != selected)
/*     */       {
/*     */         
/* 190 */         if (isValidFood(player.method_31548().method_5438(slot))) {
/* 191 */           return slot;
/*     */         }
/*     */       }
/*     */     } 
/* 195 */     for (slot = 9; slot < 36; slot++) {
/* 196 */       if (isValidFood(player.method_31548().method_5438(slot))) {
/* 197 */         return slot;
/*     */       }
/*     */     } 
/*     */     
/* 201 */     return -1;
/*     */   }
/*     */   
/*     */   private boolean isValidFood(class_1799 stack) {
/* 205 */     if (stack == null || stack.method_7960()) {
/* 206 */       return false;
/*     */     }
/*     */     
/* 209 */     if (stack.method_31574(class_1802.field_8463) || stack.method_31574(class_1802.field_8367) || stack.method_31574(class_1802.field_8233)) {
/* 210 */       return false;
/*     */     }
/*     */     
/* 213 */     return (stack.method_7976() == class_1839.field_8950);
/*     */   }
/*     */   
/*     */   private void selectHotbarSlot(int slot) {
/* 217 */     if (mc.field_1724 == null || slot < 0 || slot > 8 || (mc.field_1724.method_31548()).field_7545 == slot) {
/*     */       return;
/*     */     }
/*     */     
/* 221 */     (mc.field_1724.method_31548()).field_7545 = slot;
/* 222 */     if (mc.method_1562() != null) {
/* 223 */       mc.method_1562().method_52787((class_2596)new class_2868(slot));
/*     */     }
/*     */   }
/*     */   
/*     */   private void swapInventorySlotWithHotbar(int inventorySlot, int hotbarSlot) {
/* 228 */     if (mc.field_1724 == null || mc.field_1761 == null || inventorySlot < 9 || inventorySlot > 35 || hotbarSlot < 0 || hotbarSlot > 8) {
/*     */       return;
/*     */     }
/*     */     
/* 232 */     mc.field_1761.method_2906(0, inventorySlot, hotbarSlot, class_1713.field_7791, (class_1657)mc.field_1724);
/* 233 */     if (mc.method_1562() != null) {
/* 234 */       mc.method_1562().method_52787((class_2596)new class_2815(0));
/*     */     }
/*     */   }
/*     */   
/*     */   private void stopEating() {
/* 239 */     if (mc.field_1690 != null) {
/* 240 */       mc.field_1690.field_1904.method_23481(false);
/*     */     }
/*     */     
/* 243 */     if (this.sprintPaused) {
/* 244 */       Sprint.popPause();
/* 245 */       this.sprintPaused = false;
/*     */     } 
/*     */     
/* 248 */     restoreHeldItem();
/* 249 */     this.eating = false;
/*     */   }
/*     */   
/*     */   private void restoreHeldItem() {
/* 253 */     if (mc.field_1724 == null || mc.field_1761 == null) {
/* 254 */       resetSwapState();
/*     */       
/*     */       return;
/*     */     } 
/* 258 */     if (this.swappedFromInventory && this.swappedInventorySlot != -1) {
/* 259 */       int hotbarSlot = (this.originalSlot == -1) ? (mc.field_1724.method_31548()).field_7545 : this.originalSlot;
/* 260 */       selectHotbarSlot(hotbarSlot);
/* 261 */       swapInventorySlotWithHotbar(this.swappedInventorySlot, hotbarSlot);
/*     */     } 
/*     */     
/* 264 */     if (this.originalSlot != -1) {
/* 265 */       selectHotbarSlot(this.originalSlot);
/*     */     }
/*     */     
/* 268 */     resetSwapState();
/*     */   }
/*     */   
/*     */   private void resetSwapState() {
/* 272 */     this.swappedFromInventory = false;
/* 273 */     this.swappedInventorySlot = -1;
/* 274 */     this.originalSlot = -1;
/*     */   }
/*     */   
/*     */   private void pauseBaritone() {
/*     */     try {
/* 279 */       Object baritone = getPrimaryBaritone();
/* 280 */       if (baritone == null) {
/* 281 */         cancelVanillaBreaking();
/*     */         
/*     */         return;
/*     */       } 
/* 285 */       Object pathing = invoke(baritone, "getPathingBehavior");
/* 286 */       if (pathing == null || !Boolean.TRUE.equals(invoke(pathing, "hasPath"))) {
/* 287 */         cancelVanillaBreaking();
/*     */         
/*     */         return;
/*     */       } 
/* 291 */       Object input = invoke(baritone, "getInputOverrideHandler");
/* 292 */       if (input != null) {
/* 293 */         input.getClass().getMethod("clearAllKeys", new Class[0]).invoke(input, new Object[0]);
/* 294 */         Object blockBreakHelper = input.getClass().getMethod("getBlockBreakHelper", new Class[0]).invoke(input, new Object[0]);
/* 295 */         if (blockBreakHelper != null) {
/* 296 */           blockBreakHelper.getClass().getMethod("stopBreakingBlock", new Class[0]).invoke(blockBreakHelper, new Object[0]);
/*     */         }
/*     */       } 
/* 299 */       pathing.getClass().getMethod("requestPause", new Class[0]).invoke(pathing, new Object[0]);
/* 300 */       cancelVanillaBreaking();
/* 301 */     } catch (Throwable ignored) {
/* 302 */       cancelVanillaBreaking();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void cancelVanillaBreaking() {
/*     */     try {
/* 308 */       if (mc.field_1761 != null) {
/* 309 */         mc.field_1761.method_2925();
/*     */       }
/* 311 */     } catch (Throwable throwable) {}
/*     */   }
/*     */ 
/*     */   
/*     */   private static Object getPrimaryBaritone() throws ReflectiveOperationException {
/* 316 */     Class<?> apiClass = Class.forName("baritone.api.BaritoneAPI");
/* 317 */     Object provider = apiClass.getMethod("getProvider", new Class[0]).invoke(null, new Object[0]);
/* 318 */     return (provider == null) ? null : provider.getClass().getMethod("getPrimaryBaritone", new Class[0]).invoke(provider, new Object[0]);
/*     */   }
/*     */   
/*     */   private static Object invoke(Object target, String methodName) throws ReflectiveOperationException {
/* 322 */     return target.getClass().getMethod(methodName, new Class[0]).invoke(target, new Object[0]);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\player\AutoEat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */