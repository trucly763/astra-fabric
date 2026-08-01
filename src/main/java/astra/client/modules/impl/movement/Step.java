/*     */ package shame.astra.client.modules.impl.movement;
/*     */ import net.minecraft.class_1922;
/*     */ import net.minecraft.class_2338;
/*     */ import net.minecraft.class_238;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_265;
/*     */ import net.minecraft.class_2680;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.EventUpdate;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*     */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*     */ import shame.astra.client.modules.settings.implement.ModeSetting;
/*     */ 
/*     */ public class Step extends Module {
/*  17 */   public static Step INSTANCE = new Step();
/*     */   
/*  19 */   public ModeSetting mode = new ModeSetting("Режим", "Vanilla", new String[] { "Vanilla", "NCP", "Motion" });
/*  20 */   public FloatSetting height = new FloatSetting("Высота", 1.0F, 1.0F, 10.0F, 0.5F);
/*  21 */   public BooleanSetting reverse = new BooleanSetting("Reverse", false);
/*  22 */   public FloatSetting reverseHeight = new FloatSetting("Высота Reverse", 1.0F, 1.0F, 10.0F, 0.5F);
/*     */   
/*  24 */   private int timer = 0;
/*     */   
/*     */   public Step() {
/*  27 */     super("Step", "Моментально взбирается на блок", Module.ModuleCategory.MOVEMENT);
/*  28 */     addSettings(new Setting[] { (Setting)this.mode, (Setting)this.height, (Setting)this.reverse, (Setting)this.reverseHeight });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  33 */     super.onEnable();
/*  34 */     this.timer = 0;
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onUpdate(EventUpdate event) {
/*  39 */     if (mc.field_1724 == null || mc.field_1687 == null)
/*     */       return; 
/*  41 */     if (this.reverse.isState() && mc.field_1724.method_24828() && !mc.field_1690.field_1903.method_1434() && 
/*  42 */       !mc.field_1724.method_5715() && !isBlockAbove()) {
/*     */       
/*  44 */       float fallDistance = this.reverseHeight.get();
/*     */       
/*  46 */       if (canFall(fallDistance)) {
/*  47 */         class_243 vel = mc.field_1724.method_18798();
/*  48 */         mc.field_1724.method_18800(vel.field_1352, -fallDistance, vel.field_1350);
/*     */       } 
/*     */     } 
/*     */     
/*  52 */     if (!mc.field_1724.field_5976 || !mc.field_1724.method_24828() || mc.field_1690.field_1903.method_1434()) {
/*  53 */       this.timer = 0;
/*     */       
/*     */       return;
/*     */     } 
/*  57 */     float stepHeight = getStepHeight();
/*     */     
/*  59 */     if (stepHeight > 0.6F && stepHeight <= this.height.get()) {
/*  60 */       if (this.mode.is("Vanilla")) {
/*  61 */         handleVanillaStep(stepHeight);
/*     */       }
/*     */       
/*  64 */       if (this.mode.is("NCP")) {
/*  65 */         handleNCPStep(stepHeight);
/*     */       }
/*     */       
/*  68 */       if (this.mode.is("Motion")) {
/*  69 */         handleMotionStep(stepHeight);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleVanillaStep(float stepHeight) {
/*  75 */     mc.field_1724.method_5814(mc.field_1724
/*  76 */         .method_23317(), mc.field_1724
/*  77 */         .method_23318() + stepHeight, mc.field_1724
/*  78 */         .method_23321());
/*     */   }
/*     */ 
/*     */   
/*     */   private void handleNCPStep(float stepHeight) {
/*  83 */     double[] offsets = null;
/*  84 */     double baseY = mc.field_1724.method_23318();
/*     */     
/*  86 */     if (stepHeight <= 1.0F) {
/*  87 */       offsets = new double[] { 0.42D, 0.753D };
/*  88 */     } else if (stepHeight <= 1.5F) {
/*  89 */       offsets = new double[] { 0.42D, 0.75D, 1.0D, 1.16D, 1.23D, 1.2D };
/*  90 */     } else if (stepHeight <= 2.0F) {
/*  91 */       offsets = new double[] { 0.42D, 0.78D, 0.63D, 0.51D, 0.9D, 1.21D, 1.45D, 1.43D };
/*  92 */     } else if (stepHeight <= 2.5F) {
/*  93 */       offsets = new double[] { 0.425D, 0.821D, 0.699D, 0.599D, 1.022D, 1.372D, 1.652D, 1.869D, 2.019D, 1.907D };
/*  94 */     } else if (stepHeight <= 3.0F) {
/*  95 */       offsets = new double[] { 0.42D, 0.78D, 0.63D, 0.51D, 0.9D, 1.21D, 1.45D, 1.43D, 1.78D, 2.1D, 2.4D, 2.7D };
/*     */     } 
/*     */     
/*  98 */     if (offsets != null) {
/*  99 */       for (double offset : offsets) {
/* 100 */         mc.field_1724.method_5814(mc.field_1724
/* 101 */             .method_23317(), baseY + offset, mc.field_1724
/*     */             
/* 103 */             .method_23321());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void handleMotionStep(float stepHeight) {
/* 110 */     class_243 velocity = mc.field_1724.method_18798();
/* 111 */     double motionY = 0.42D;
/*     */     
/* 113 */     if (stepHeight <= 1.0F) {
/* 114 */       motionY = 0.42D;
/* 115 */     } else if (stepHeight <= 1.5F) {
/* 116 */       motionY = 0.52D;
/* 117 */     } else if (stepHeight <= 2.0F) {
/* 118 */       motionY = 0.62D;
/* 119 */     } else if (stepHeight <= 2.5F) {
/* 120 */       motionY = 0.72D;
/* 121 */     } else if (stepHeight <= 3.0F) {
/* 122 */       motionY = 0.82D;
/*     */     } 
/*     */     
/* 125 */     mc.field_1724.method_18800(velocity.field_1352, motionY, velocity.field_1350);
/*     */   }
/*     */   
/*     */   private float getStepHeight() {
/* 129 */     class_238 box = mc.field_1724.method_5829();
/* 130 */     float maxY = 0.0F;
/*     */     
/* 132 */     double checkDistance = 0.3D;
/* 133 */     double playerYaw = Math.toRadians(mc.field_1724.method_36454());
/* 134 */     double offsetX = -Math.sin(playerYaw) * checkDistance;
/* 135 */     double offsetZ = Math.cos(playerYaw) * checkDistance;
/*     */     double y;
/* 137 */     for (y = 0.6D; y <= this.height.get() + 0.6D; y += 0.1D) {
/* 138 */       class_238 testBox = box.method_989(offsetX, y, offsetZ);
/*     */       
/* 140 */       for (class_2338 pos : class_2338.method_10094(
/* 141 */           (int)Math.floor(testBox.field_1323), 
/* 142 */           (int)Math.floor(testBox.field_1322), 
/* 143 */           (int)Math.floor(testBox.field_1321), 
/* 144 */           (int)Math.floor(testBox.field_1320), 
/* 145 */           (int)Math.floor(testBox.field_1325), 
/* 146 */           (int)Math.floor(testBox.field_1324))) {
/*     */         
/* 148 */         class_2680 state = mc.field_1687.method_8320(pos);
/* 149 */         if (state.method_26215())
/*     */           continue; 
/* 151 */         class_265 shape = state.method_26220((class_1922)mc.field_1687, pos);
/* 152 */         if (shape.method_1110())
/*     */           continue; 
/* 154 */         for (class_238 collisionBox : shape.method_1090()) {
/* 155 */           class_238 offsetBox = collisionBox.method_996(pos);
/* 156 */           float blockHeight = (float)(offsetBox.field_1325 - mc.field_1724.method_23318());
/*     */           
/* 158 */           if (blockHeight > 0.6F && blockHeight <= this.height.get()) {
/* 159 */             maxY = Math.max(maxY, blockHeight);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 165 */     return maxY;
/*     */   }
/*     */   
/*     */   private boolean isBlockAbove() {
/* 169 */     class_238 box = mc.field_1724.method_5829().method_989(0.0D, 1.0D, 0.0D);
/*     */     
/* 171 */     for (class_2338 pos : class_2338.method_10094(
/* 172 */         (int)Math.floor(box.field_1323), 
/* 173 */         (int)Math.floor(box.field_1322), 
/* 174 */         (int)Math.floor(box.field_1321), 
/* 175 */         (int)Math.floor(box.field_1320), 
/* 176 */         (int)Math.floor(box.field_1325), 
/* 177 */         (int)Math.floor(box.field_1324))) {
/*     */       
/* 179 */       if (!mc.field_1687.method_8320(pos).method_26215()) {
/* 180 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 184 */     return false;
/*     */   }
/*     */   
/*     */   private boolean canFall(float distance) {
/* 188 */     class_238 box = mc.field_1724.method_5829();
/*     */     
/* 190 */     for (double y = 0.1D; y <= distance; y += 0.1D) {
/* 191 */       class_238 testBox = box.method_989(0.0D, -y, 0.0D);
/*     */       
/* 193 */       for (class_2338 pos : class_2338.method_10094(
/* 194 */           (int)Math.floor(testBox.field_1323), 
/* 195 */           (int)Math.floor(testBox.field_1322), 
/* 196 */           (int)Math.floor(testBox.field_1321), 
/* 197 */           (int)Math.floor(testBox.field_1320), 
/* 198 */           (int)Math.floor(testBox.field_1325), 
/* 199 */           (int)Math.floor(testBox.field_1324))) {
/*     */         
/* 201 */         if (!mc.field_1687.method_8320(pos).method_26215()) {
/* 202 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 207 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\movement\Step.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */