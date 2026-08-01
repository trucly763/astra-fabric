/*     */ package shame.astra.client.modules.impl.combat;
/*     */ import net.minecraft.class_1657;
/*     */ import net.minecraft.class_1713;
/*     */ import net.minecraft.class_1792;
/*     */ import net.minecraft.class_1799;
/*     */ import net.minecraft.class_1802;
/*     */ import net.minecraft.class_2596;
/*     */ import net.minecraft.class_2815;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.EventBinding;
/*     */ import shame.astra.api.events.implement.EventMoveInput;
/*     */ import shame.astra.api.events.implement.EventUpdate;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.impl.movement.Sprint;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.BindSetting;
/*     */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*     */ import shame.astra.client.modules.settings.implement.ModeSetting;
/*     */ 
/*     */ public class AutoSwap extends Module {
/*  21 */   public static AutoSwap INSTANCE = new AutoSwap();
/*     */   
/*  23 */   private final ModeSetting firstItem = new ModeSetting("Первый предмет", "Руна", new String[] { "Руна", "Тотем", "Шар", "Гепл", "Щит" });
/*  24 */   private final ModeSetting secondItem = new ModeSetting("Второй предмет", "Тотем", new String[] { "Руна", "Тотем", "Шар", "Гепл", "Щит" });
/*  25 */   private final BindSetting swapKey = new BindSetting("Кнопка свапа", -98);
/*  26 */   private final BooleanSetting bypassgrim = new BooleanSetting("Обходить Grim", true);
/*     */   
/*     */   private int bypassTicks;
/*     */   private boolean sprintPaused;
/*     */   private int swapCooldown;
/*  31 */   private int targetSlot = -1;
/*     */   private boolean needSwap = false;
/*     */   
/*     */   public AutoSwap() {
/*  35 */     super("AutoSwap", "Быстрая смена предметов в офф-хенде", Module.ModuleCategory.COMBAT);
/*  36 */     addSettings(new Setting[] { (Setting)this.firstItem, (Setting)this.secondItem, (Setting)this.swapKey, (Setting)this.bypassgrim });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  41 */     this.needSwap = false;
/*  42 */     this.targetSlot = -1;
/*  43 */     this.bypassTicks = 0;
/*  44 */     this.swapCooldown = 0;
/*  45 */     super.onEnable();
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onBinding(EventBinding event) {
/*  50 */     if (mc.field_1755 != null)
/*  51 */       return;  if (mc.field_1724 == null || mc.field_1687 == null)
/*     */       return; 
/*  53 */     if (event.getKey() == this.swapKey.getKey() && 
/*  54 */       this.swapCooldown == 0) {
/*  55 */       this.needSwap = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @EventLink
/*     */   public void onInput(EventMoveInput e) {
/*  62 */     if (this.bypassgrim.isState() && this.bypassTicks > 0) {
/*  63 */       if (mc.field_1724 == null)
/*  64 */         return;  mc.field_1724.method_5728(false);
/*  65 */       e.setForward(0.0F);
/*  66 */       e.setStrafe(0.0F);
/*  67 */       e.setJump(false);
/*  68 */       e.setSneak(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onUpdate(EventUpdate e) {
/*  74 */     if (mc.field_1724 == null || mc.field_1687 == null)
/*     */       return; 
/*  76 */     if (this.swapCooldown > 0) {
/*  77 */       this.swapCooldown--;
/*     */     }
/*     */     
/*  80 */     if (this.bypassgrim.isState() && this.bypassTicks > 0) {
/*  81 */       mc.field_1724.method_5728(false);
/*  82 */       this.bypassTicks--;
/*     */       
/*  84 */       if (this.bypassTicks == 1) {
/*  85 */         performSwap();
/*     */       }
/*     */       
/*  88 */       if (this.bypassTicks == 0) {
/*  89 */         restoreSprint();
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/*  94 */     if (this.needSwap && this.targetSlot == -1) {
/*  95 */       int slot; this.needSwap = false;
/*     */       
/*  97 */       class_1792 offhand = mc.field_1724.method_6079().method_7909();
/*  98 */       class_1792 first = getItem(this.firstItem.getCurrent());
/*  99 */       class_1792 second = getItem(this.secondItem.getCurrent());
/*     */       
/* 101 */       int firstSlot = findItemSlot(first);
/* 102 */       int secondSlot = findItemSlot(second);
/*     */       
/* 104 */       if (firstSlot == -1 && secondSlot == -1) {
/*     */         return;
/*     */       }
/* 107 */       if (offhand == first && secondSlot != -1) {
/* 108 */         slot = secondSlot;
/* 109 */       } else if (firstSlot != -1) {
/* 110 */         slot = firstSlot;
/*     */       } else {
/* 112 */         slot = secondSlot;
/*     */       } 
/*     */       
/* 115 */       if (slot == -1)
/*     */         return; 
/* 117 */       this.targetSlot = slot;
/*     */       
/* 119 */       if (this.bypassgrim.isState()) {
/* 120 */         disableSprint();
/* 121 */         this.bypassTicks = 2;
/* 122 */         this.swapCooldown = 2;
/*     */       } else {
/* 124 */         performSwap();
/* 125 */         this.swapCooldown = 2;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void performSwap() {
/* 132 */     if (this.targetSlot == -1)
/*     */       return; 
/* 134 */     doSwap(this.targetSlot);
/* 135 */     mc.field_1724.field_3944.method_52787((class_2596)new class_2815(0));
/*     */     
/* 137 */     this.targetSlot = -1;
/*     */   }
/*     */   
/*     */   private void doSwap(int slot) {
/* 141 */     if (slot >= 36 && slot <= 44) {
/* 142 */       int hotbarSlot = slot - 36;
/* 143 */       mc.field_1761.method_2906(0, 45, hotbarSlot, class_1713.field_7791, (class_1657)mc.field_1724);
/*     */     } else {
/* 145 */       mc.field_1761.method_2906(0, slot, 0, class_1713.field_7791, (class_1657)mc.field_1724);
/* 146 */       mc.field_1761.method_2906(0, 45, 0, class_1713.field_7791, (class_1657)mc.field_1724);
/* 147 */       mc.field_1761.method_2906(0, slot, 0, class_1713.field_7791, (class_1657)mc.field_1724);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private int findItemSlot(class_1792 item) {
/* 153 */     for (int i = 9; i < 45; i++) {
/* 154 */       class_1799 stack = mc.field_1724.field_7498.method_7611(i).method_7677();
/* 155 */       if (stack.method_7909() == item) {
/* 156 */         return i;
/*     */       }
/*     */     } 
/* 159 */     return -1;
/*     */   }
/*     */   
/*     */   private class_1792 getItem(String name) {
/* 163 */     switch (name) { case "Руна": case "Тотем": case "Шар": case "Гепл": case "Щит":  }  return 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 169 */       class_1802.field_8162;
/*     */   }
/*     */ 
/*     */   
/*     */   private void disableSprint() {
/* 174 */     if (this.sprintPaused) {
/*     */       return;
/*     */     }
/*     */     
/* 178 */     Sprint.pushPause(1000L);
/* 179 */     this.sprintPaused = true;
/*     */   }
/*     */   
/*     */   private void restoreSprint() {
/* 183 */     if (!this.sprintPaused) {
/*     */       return;
/*     */     }
/*     */     
/* 187 */     this.sprintPaused = false;
/* 188 */     Sprint.popPause();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 193 */     this.bypassTicks = 0;
/* 194 */     this.swapCooldown = 0;
/* 195 */     this.needSwap = false;
/* 196 */     this.targetSlot = -1;
/* 197 */     restoreSprint();
/* 198 */     super.onDisable();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\combat\AutoSwap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */