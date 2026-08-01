/*     */ package shame.astra.client.modules.impl.movement;
/*     */ 
/*     */ import net.minecraft.class_1268;
/*     */ import net.minecraft.class_1657;
/*     */ import net.minecraft.class_1713;
/*     */ import net.minecraft.class_1799;
/*     */ import net.minecraft.class_1802;
/*     */ import net.minecraft.class_2596;
/*     */ import net.minecraft.class_2868;
/*     */ import net.minecraft.class_2886;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.EventUpdate;
/*     */ import shame.astra.api.storages.implement.RotationStorage;
/*     */ import shame.astra.api.utils.rotate.Rotation;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*     */ import shame.astra.client.modules.settings.implement.ModeSetting;
/*     */ 
/*     */ public class Spider extends Module {
/*  21 */   public static Spider INSTANCE = new Spider();
/*     */   
/*  23 */   private final ModeSetting mode = new ModeSetting("Мод", "Вода", new String[] { "Вода", "SpookyTime" });
/*  24 */   private final BooleanSetting legit = new BooleanSetting("Легит", false);
/*     */   
/*  26 */   private int lastSlot = -1;
/*     */   private boolean isClimbing = false;
/*  28 */   private int swapBackSlot = -1;
/*     */   private int spookyTicks;
/*  30 */   private int chargeSlot = -1;
/*     */   private boolean charging;
/*     */   
/*     */   public Spider() {
/*  34 */     super("Spider", "Позволяет взбираться по стенам", Module.ModuleCategory.MOVEMENT);
/*  35 */     addSettings(new Setting[] { (Setting)this.mode, (Setting)this.legit });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  40 */     super.onDisable();
/*  41 */     if (mc.field_1724 == null)
/*     */       return; 
/*  43 */     if (this.lastSlot != -1 && this.legit.isState()) {
/*  44 */       (mc.field_1724.method_31548()).field_7545 = this.lastSlot;
/*     */     }
/*     */     
/*  47 */     this.lastSlot = -1;
/*  48 */     this.swapBackSlot = -1;
/*  49 */     this.isClimbing = false;
/*  50 */     this.spookyTicks = 0;
/*  51 */     this.chargeSlot = -1;
/*  52 */     this.charging = false;
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onUpdate(EventUpdate event) {
/*  57 */     if (mc.field_1724 == null || mc.field_1687 == null)
/*     */       return; 
/*  59 */     if (!mc.field_1724.field_5976) {
/*  60 */       stopClimbing();
/*     */       
/*     */       return;
/*     */     } 
/*  64 */     this.isClimbing = true;
/*  65 */     RotationStorage.update(new Rotation(mc.field_1724.method_36454(), 0.0F), 360.0F, 360.0F, 360.0F, 360.0F, 1, 1, false);
/*     */     
/*  67 */     if (this.mode.is("SpookyTime")) {
/*  68 */       processSpookyTime();
/*     */       
/*     */       return;
/*     */     } 
/*  72 */     int bucketSlot = getBucketSlot(false);
/*  73 */     if (bucketSlot == -1)
/*     */       return; 
/*  75 */     useBucket(bucketSlot, this.legit.isState());
/*  76 */     mc.field_1724.method_18800((mc.field_1724.method_18798()).field_1352, 0.36D, (mc.field_1724.method_18798()).field_1350);
/*     */   }
/*     */   
/*     */   private void stopClimbing() {
/*  80 */     if (this.lastSlot != -1 && this.legit.isState()) {
/*  81 */       (mc.field_1724.method_31548()).field_7545 = this.lastSlot;
/*  82 */       this.lastSlot = -1;
/*     */     } 
/*     */     
/*  85 */     if (this.swapBackSlot != -1) {
/*  86 */       mc.field_1761.method_2906(0, this.swapBackSlot, 0, class_1713.field_7794, (class_1657)mc.field_1724);
/*  87 */       this.swapBackSlot = -1;
/*     */     } 
/*     */     
/*  90 */     this.isClimbing = false;
/*  91 */     this.spookyTicks = 0;
/*  92 */     this.chargeSlot = -1;
/*  93 */     this.charging = false;
/*     */   }
/*     */   
/*     */   private void processSpookyTime() {
/*  97 */     int bucketSlot = getBucketSlot(true);
/*  98 */     boolean bucketPulse = (this.spookyTicks % 5 == 0);
/*  99 */     boolean boostPulse = (this.spookyTicks % 4 != 3);
/*     */     
/* 101 */     keepChargeHeld();
/*     */     
/* 103 */     if (bucketSlot != -1 && bucketPulse) {
/* 104 */       useBucket(bucketSlot, false);
/* 105 */       keepChargeHeld();
/*     */     } 
/*     */     
/* 108 */     double y = boostPulse ? 0.18D : 0.03D;
/* 109 */     mc.field_1724.method_18800((mc.field_1724.method_18798()).field_1352, y, (mc.field_1724.method_18798()).field_1350);
/* 110 */     this.spookyTicks++;
/*     */   }
/*     */   
/*     */   private void useBucket(int bucketSlot, boolean legitMode) {
/* 114 */     if (!legitMode) {
/* 115 */       int currentSlot = (mc.field_1724.method_31548()).field_7545;
/* 116 */       boolean bool = (bucketSlot >= 9 && bucketSlot <= 35);
/*     */       
/* 118 */       if (bool) {
/* 119 */         mc.field_1761.method_2906(0, bucketSlot, currentSlot, class_1713.field_7791, (class_1657)mc.field_1724);
/* 120 */         mc.field_1761.method_2919((class_1657)mc.field_1724, class_1268.field_5808);
/* 121 */         mc.field_1761.method_2906(0, bucketSlot, currentSlot, class_1713.field_7791, (class_1657)mc.field_1724);
/*     */       } else {
/* 123 */         mc.field_1724.field_3944.method_52787((class_2596)new class_2868(bucketSlot));
/* 124 */         mc.field_1761.method_2919((class_1657)mc.field_1724, class_1268.field_5808);
/* 125 */         mc.field_1724.field_3944.method_52787((class_2596)new class_2868(currentSlot));
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 130 */     boolean isInventorySwap = (bucketSlot >= 9 && bucketSlot <= 35);
/*     */     
/* 132 */     if (isInventorySwap) {
/* 133 */       mc.field_1761.method_2906(0, bucketSlot, (mc.field_1724.method_31548()).field_7545, class_1713.field_7791, (class_1657)mc.field_1724);
/* 134 */       this.swapBackSlot = bucketSlot;
/* 135 */     } else if ((mc.field_1724.method_31548()).field_7545 != bucketSlot) {
/* 136 */       if (this.lastSlot == -1) {
/* 137 */         this.lastSlot = (mc.field_1724.method_31548()).field_7545;
/*     */       }
/* 139 */       (mc.field_1724.method_31548()).field_7545 = bucketSlot;
/*     */     } 
/*     */     
/* 142 */     mc.field_1761.method_2919((class_1657)mc.field_1724, class_1268.field_5808);
/*     */   }
/*     */   
/*     */   private void keepChargeHeld() {
/* 146 */     if (isChargeItem(mc.field_1724.method_6079())) {
/* 147 */       if (!this.charging || this.spookyTicks % 12 == 0) {
/* 148 */         sendChargeUsePacket(class_1268.field_5810);
/*     */       }
/* 150 */       this.charging = true;
/*     */       
/*     */       return;
/*     */     } 
/* 154 */     if (this.chargeSlot == -1 || !isChargeItem(mc.field_1724.method_31548().method_5438(this.chargeSlot))) {
/* 155 */       this.chargeSlot = getChargeHotbarSlot();
/* 156 */       this.charging = false;
/*     */     } 
/* 158 */     if (this.chargeSlot == -1)
/*     */       return; 
/* 160 */     if ((mc.field_1724.method_31548()).field_7545 != this.chargeSlot) {
/* 161 */       mc.field_1724.field_3944.method_52787((class_2596)new class_2868(this.chargeSlot));
/* 162 */       (mc.field_1724.method_31548()).field_7545 = this.chargeSlot;
/* 163 */       this.charging = false;
/*     */     } 
/*     */     
/* 166 */     if (!this.charging || this.spookyTicks % 12 == 0) {
/* 167 */       sendChargeUsePacket(class_1268.field_5808);
/*     */     }
/* 169 */     this.charging = true;
/*     */   }
/*     */   
/*     */   private void sendChargeUsePacket(class_1268 hand) {
/* 173 */     mc.field_1724.field_3944.method_52787((class_2596)new class_2886(hand, 0, mc.field_1724.method_36454(), mc.field_1724.method_36455()));
/*     */   }
/*     */   private int getBucketSlot(boolean allowLava) {
/*     */     int i;
/* 177 */     for (i = 0; i < 9; i++) {
/* 178 */       class_1799 stack = mc.field_1724.method_31548().method_5438(i);
/* 179 */       if (isBucket(stack, allowLava)) {
/* 180 */         return i;
/*     */       }
/*     */     } 
/*     */     
/* 184 */     if (!this.legit.isState() || this.mode.is("SpookyTime")) {
/* 185 */       for (i = 9; i < 36; i++) {
/* 186 */         class_1799 stack = mc.field_1724.method_31548().method_5438(i);
/* 187 */         if (isBucket(stack, allowLava)) {
/* 188 */           return i;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 193 */     return -1;
/*     */   }
/*     */   
/*     */   private int getChargeHotbarSlot() {
/* 197 */     for (int i = 0; i < 9; i++) {
/* 198 */       if (isChargeItem(mc.field_1724.method_31548().method_5438(i))) {
/* 199 */         return i;
/*     */       }
/*     */     } 
/* 202 */     return -1;
/*     */   }
/*     */   
/*     */   private boolean isBucket(class_1799 stack, boolean allowLava) {
/* 206 */     return (stack.method_7909() == class_1802.field_8705 || (allowLava && stack.method_7909() == class_1802.field_8187));
/*     */   }
/*     */   
/*     */   private boolean isChargeItem(class_1799 stack) {
/* 210 */     return (stack.method_7909() instanceof net.minecraft.class_1753 || stack.method_7909() instanceof net.minecraft.class_1835);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\movement\Spider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */