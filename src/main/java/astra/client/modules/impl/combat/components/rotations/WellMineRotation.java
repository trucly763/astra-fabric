/*     */ package shame.astra.client.modules.impl.combat.components.rotations;
/*     */ 
/*     */ import net.minecraft.class_1309;
/*     */ import net.minecraft.class_238;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_3532;
/*     */ import shame.astra.api.QClient;
/*     */ import shame.astra.api.storages.implement.RotationStorage;
/*     */ import shame.astra.api.utils.rotate.Rotation;
/*     */ import shame.astra.client.modules.impl.combat.Aura;
/*     */ import shame.astra.client.modules.impl.combat.components.RotationsSystem;
/*     */ 
/*     */ public class WellMineRotation
/*     */   extends RotationsSystem
/*     */   implements QClient {
/*     */   private class_1309 currentTarget;
/*  17 */   private float lastYaw = 0.0F;
/*  18 */   private float lastPitch = 0.0F;
/*     */   
/*  20 */   private float acceleration = 0.0F;
/*     */   
/*     */   private boolean isBack = false;
/*  23 */   private double randomOffsetX = 0.0D;
/*  24 */   private double randomOffsetY = 0.0D;
/*  25 */   private double randomOffsetZ = 0.0D;
/*     */   
/*     */   public void reset() {
/*  28 */     this.currentTarget = null;
/*  29 */     this.acceleration = 0.0F;
/*  30 */     this.isBack = false;
/*  31 */     this.randomOffsetX = 0.0D;
/*  32 */     this.randomOffsetY = 0.0D;
/*  33 */     this.randomOffsetZ = 0.0D;
/*     */     
/*  35 */     if (mc.field_1724 != null) {
/*  36 */       this.lastYaw = mc.field_1724.method_36454();
/*  37 */       this.lastPitch = mc.field_1724.method_36455();
/*     */     } else {
/*  39 */       this.lastYaw = 0.0F;
/*  40 */       this.lastPitch = 0.0F;
/*     */     } 
/*     */   }
/*     */   
/*     */   private float getGCDValue() {
/*  45 */     float sensitivity = (float)(((Double)mc.field_1690.method_42495().method_41753()).doubleValue() * 0.6000000238418579D + 0.20000000298023224D);
/*  46 */     return sensitivity * sensitivity * sensitivity * 1.2F;
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateRandomOffset(class_1309 target) {
/*  51 */     class_238 box = target.method_5829();
/*  52 */     double boxWidth = box.field_1320 - box.field_1323;
/*  53 */     double boxHeight = box.field_1325 - box.field_1322;
/*  54 */     double boxDepth = box.field_1324 - box.field_1321;
/*     */     
/*  56 */     this.randomOffsetX = (Math.random() - 0.5D) * boxWidth * 0.15D;
/*  57 */     this.randomOffsetY = (Math.random() - 0.5D) * boxHeight * 0.15D;
/*  58 */     this.randomOffsetZ = (Math.random() - 0.5D) * boxDepth * 0.15D;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateRotations(class_1309 target) {
/*  64 */     if (mc.field_1724 == null || target == null) {
/*     */       return;
/*     */     }
/*     */     
/*  68 */     if (this.currentTarget != target) {
/*  69 */       this.currentTarget = target;
/*  70 */       this.acceleration = 0.0F;
/*  71 */       this.isBack = false;
/*  72 */       this.lastYaw = mc.field_1724.method_36454();
/*  73 */       this.lastPitch = mc.field_1724.method_36455();
/*  74 */       updateRandomOffset(target);
/*     */     } 
/*     */     
/*  77 */     class_238 box = getPredictedBox(target);
/*  78 */     class_243 eyePos = mc.field_1724.method_33571();
/*  79 */     class_243 centerPoint = box.method_1005().method_1031(this.randomOffsetX, this.randomOffsetY, this.randomOffsetZ);
/*  80 */     class_243 toTarget = centerPoint.method_1020(eyePos);
/*  81 */     float centerYaw = (float)class_3532.method_15338(Math.toDegrees(Math.atan2(toTarget.field_1350, toTarget.field_1352)) - 90.0D);
/*  82 */     float centerPitch = (float)-Math.toDegrees(Math.atan2(toTarget.field_1351, Math.hypot(toTarget.field_1352, toTarget.field_1350)));
/*  83 */     boolean bothGliding = (mc.field_1724.method_6128() && target.method_6128());
/*  84 */     class_243 lookVec = mc.field_1724.method_5828(1.0F);
/*  85 */     class_243 endVec = eyePos.method_1019(lookVec.method_1021(bothGliding ? 1488.0D : 999.0D));
/*  86 */     class_238 shrunkBox = box.method_1014(bothGliding ? 0.0D : -0.5D);
/*  87 */     boolean inBox = shrunkBox.method_992(eyePos, endVec).isPresent();
/*     */     
/*  89 */     if (bothGliding) {
/*  90 */       if (this.isBack) {
/*  91 */         if (this.acceleration >= -0.02F) {
/*  92 */           this.acceleration -= (Math.abs(class_3532.method_15393(centerYaw - this.lastYaw)) > 80.0F) ? 0.15F : 0.02F;
/*     */         }
/*  94 */         if (this.acceleration <= -0.02F) {
/*  95 */           this.isBack = false;
/*  96 */           updateRandomOffset(target);
/*     */         } 
/*     */       } else {
/*  99 */         this.acceleration += 0.0105F;
/* 100 */         if (this.acceleration >= 0.305F || inBox) {
/* 101 */           this.isBack = true;
/*     */         }
/*     */       } 
/* 104 */     } else if (this.isBack) {
/* 105 */       if (this.acceleration >= -0.15F) {
/* 106 */         float slowdownSpeed = (Math.abs(class_3532.method_15393(centerYaw - this.lastYaw)) > 80.0F) ? 0.1F : 0.01F;
/* 107 */         this.acceleration -= slowdownSpeed *= 0.9F + (float)Math.random() * 0.2F;
/*     */       } 
/* 109 */       if (this.acceleration <= -0.15F) {
/* 110 */         this.isBack = false;
/* 111 */         updateRandomOffset(target);
/*     */       } 
/*     */     } else {
/* 114 */       float accelSpeed = 0.0082F + (float)Math.random() * 0.002F - 0.001F;
/* 115 */       this.acceleration += accelSpeed;
/* 116 */       float threshold = 0.184F + (float)Math.random() * 0.03F - 0.015F;
/* 117 */       if (this.acceleration >= threshold || inBox) {
/* 118 */         this.isBack = true;
/*     */       }
/*     */     } 
/*     */     
/* 122 */     float deltaYaw = class_3532.method_15393(centerYaw - this.lastYaw);
/* 123 */     float deltaPitch = centerPitch - this.lastPitch;
/* 124 */     float smooth = Math.max(this.acceleration, 0.0F);
/* 125 */     float humanYawOffset = (float)(Math.sin(System.currentTimeMillis() * 0.001D) * 0.04D);
/* 126 */     float humanPitchOffset = (float)(Math.cos(System.currentTimeMillis() * 0.0015D) * 0.025D);
/* 127 */     if (Math.abs(deltaYaw) > 1.0F || Math.abs(deltaPitch) > 1.0F) {
/* 128 */       humanYawOffset += ((float)Math.random() - 0.5F) * 0.035F;
/* 129 */       humanPitchOffset += ((float)Math.random() - 0.5F) * 0.02F;
/*     */     } 
/*     */     
/* 132 */     float newYaw = this.lastYaw + deltaYaw * class_3532.method_15363(smooth * 1.12F, 0.0F, 1.0F) + humanYawOffset;
/* 133 */     float newPitch = this.lastPitch + deltaPitch * class_3532.method_15363(smooth / 1.88F, 0.0F, 1.0F) + humanPitchOffset;
/* 134 */     float gcd = getGCDValue();
/* 135 */     newYaw -= (newYaw - this.lastYaw) % gcd;
/* 136 */     newPitch -= (newPitch - this.lastPitch) % gcd;
/* 137 */     if (newPitch > 89.0F) {
/* 138 */       newPitch = 89.0F;
/*     */     }
/* 140 */     if (newPitch < -89.0F) {
/* 141 */       newPitch = -89.0F;
/*     */     }
/*     */     
/* 144 */     this.lastYaw = newYaw;
/* 145 */     this.lastPitch = newPitch;
/* 146 */     RotationStorage.update(new Rotation(newYaw, newPitch), 360.0F, 45.0F, 45.0F, 45.0F, 0, 1, Aura.clientLook.isState());
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\combat\components\rotations\WellMineRotation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */