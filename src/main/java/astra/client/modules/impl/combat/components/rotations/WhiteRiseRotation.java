/*     */ package shame.astra.client.modules.impl.combat.components.rotations;
/*     */ 
/*     */ import net.minecraft.class_1297;
/*     */ import net.minecraft.class_1309;
/*     */ import net.minecraft.class_241;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_3532;
/*     */ import shame.astra.api.QClient;
/*     */ import shame.astra.api.storages.implement.RotationStorage;
/*     */ import shame.astra.api.utils.rotate.Rotation;
/*     */ import shame.astra.api.utils.rotate.RotationUtils;
/*     */ import shame.astra.client.modules.impl.combat.Aura;
/*     */ import shame.astra.client.modules.impl.combat.components.RotationsSystem;
/*     */ import shame.astra.client.modules.impl.combat.components.gcd.GCDUtil;
/*     */ import shame.astra.client.modules.impl.combat.components.interpolation.BestPoint;
/*     */ 
/*     */ public class WhiteRiseRotation extends RotationsSystem implements QClient {
/*     */   private final Aura aura;
/*     */   private class_1309 trackedTarget;
/*     */   private float lastYaw;
/*     */   private float lastPitch;
/*     */   private float speedAcceleration;
/*     */   private boolean back;
/*     */   private boolean initialized;
/*     */   private float jitterOffset;
/*     */   private int tickCounter;
/*     */   
/*     */   public WhiteRiseRotation(Aura aura) {
/*  29 */     this.aura = aura;
/*     */   }
/*     */   
/*     */   public void reset() {
/*  33 */     this.trackedTarget = null;
/*  34 */     this.speedAcceleration = 0.0F;
/*  35 */     this.back = false;
/*  36 */     this.jitterOffset = 0.0F;
/*  37 */     this.tickCounter = 0;
/*  38 */     this.initialized = (mc.field_1724 != null);
/*     */     
/*  40 */     if (mc.field_1724 != null) {
/*  41 */       this.lastYaw = mc.field_1724.method_36454();
/*  42 */       this.lastPitch = mc.field_1724.method_36455();
/*     */     } else {
/*  44 */       this.lastYaw = 0.0F;
/*  45 */       this.lastPitch = 0.0F;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onAttack() {}
/*     */ 
/*     */   
/*     */   public void updateRotations(class_1309 target) {
/*  54 */     if (mc.field_1724 == null || target == null)
/*     */       return; 
/*  56 */     if (mc.field_1724.method_6039()) {
/*  57 */       this.rotate = new class_241(mc.field_1724.method_36454(), mc.field_1724.method_36455());
/*  58 */       this.lastYaw = this.rotate.field_1343;
/*  59 */       this.lastPitch = this.rotate.field_1342;
/*     */       
/*     */       return;
/*     */     } 
/*  63 */     if (!this.initialized) {
/*  64 */       this.lastYaw = mc.field_1724.method_36454();
/*  65 */       this.lastPitch = mc.field_1724.method_36455();
/*  66 */       this.initialized = true;
/*     */     } 
/*     */     
/*  69 */     if (this.trackedTarget != target) {
/*  70 */       this.trackedTarget = target;
/*  71 */       this.speedAcceleration = 0.0F;
/*  72 */       this.back = false;
/*  73 */       this.tickCounter = 0;
/*     */     } 
/*     */     
/*  76 */     this.tickCounter++;
/*  77 */     this.jitterOffset = (float)((Math.sin(this.tickCounter * 0.17D) * 0.12D + Math.random() * 0.08D - 0.04D) * 0.699999988079071D);
/*     */     
/*  79 */     class_243 point = BestPoint.getMultipoint((class_1297)target, 128.0D);
/*  80 */     class_241 angle = RotationUtils.getRotations(point);
/*  81 */     float targetYaw = angle.field_1343;
/*  82 */     float targetPitch = angle.field_1342;
/*     */     
/*  84 */     float yawDiff = Math.abs(class_3532.method_15393(targetYaw - this.lastYaw));
/*  85 */     boolean readyToAttack = (mc.field_1724.method_7261(1.0F) > 0.9F && this.aura.getWhiteRiseTicksToAttack() <= 1);
/*     */     
/*  87 */     if (!this.back) {
/*  88 */       float gain = 0.0055F;
/*  89 */       if (yawDiff > 60.0F) {
/*  90 */         gain += 0.028800001F;
/*  91 */       } else if (yawDiff > 30.0F) {
/*  92 */         gain += 0.014400001F;
/*     */       } else {
/*  94 */         gain += 0.0072000003F;
/*     */       } 
/*  96 */       if (readyToAttack) {
/*  97 */         gain += 0.012857143F;
/*     */       }
/*  99 */       this.speedAcceleration += gain * (1.6F + this.jitterOffset);
/* 100 */       if (this.speedAcceleration >= 0.22F) this.back = true; 
/*     */     } else {
/* 102 */       float loss = readyToAttack ? 0.045F : 0.008F;
/* 103 */       this.speedAcceleration -= loss * (2.1F + this.jitterOffset);
/* 104 */       if (this.speedAcceleration <= -0.04F) this.back = false;
/*     */     
/*     */     } 
/* 107 */     float smooth = class_3532.method_15363(this.speedAcceleration, 0.0F, mc.field_1724.method_6128() ? 0.38F : 0.26F);
/* 108 */     if (readyToAttack) {
/* 109 */       smooth = Math.min(smooth + 0.1F, mc.field_1724.method_6128() ? 0.46F : 0.34F);
/*     */     }
/* 111 */     smooth += this.jitterOffset * 0.5F;
/* 112 */     if (this.tickCounter % 7 == 0) smooth += 0.03F;
/*     */     
/* 114 */     float deltaYaw = class_3532.method_15393(targetYaw - this.lastYaw);
/* 115 */     float deltaPitch = targetPitch - this.lastPitch;
/*     */     
/* 117 */     float yawLimit = mc.field_1724.method_6128() ? 42.0F : (readyToAttack ? 28.0F : 20.0F);
/* 118 */     float pitchLimit = mc.field_1724.method_6128() ? 12.0F : (readyToAttack ? 4.5F : 2.8F);
/*     */     
/* 120 */     deltaYaw = class_3532.method_15363(deltaYaw, -yawLimit, yawLimit);
/* 121 */     deltaPitch = class_3532.method_15363(deltaPitch, -pitchLimit, pitchLimit);
/*     */     
/* 123 */     float pitchSpeed = smooth * 0.28F;
/* 124 */     float yawSpeed = smooth * (0.85F + this.jitterOffset * 0.4F);
/*     */     
/* 126 */     float newYaw = this.lastYaw + deltaYaw * yawSpeed;
/* 127 */     float newPitch = this.lastPitch + deltaPitch * pitchSpeed;
/*     */     
/* 129 */     float gcd = GCDUtil.getGCDValue();
/* 130 */     if (gcd > 0.0F) {
/* 131 */       newYaw = this.lastYaw + Math.round((newYaw - this.lastYaw) / gcd) * gcd;
/* 132 */       newPitch = this.lastPitch + Math.round((newPitch - this.lastPitch) / gcd) * gcd;
/*     */     } 
/*     */     
/* 135 */     newPitch = class_3532.method_15363(newPitch, -89.0F, 89.0F);
/*     */     
/* 137 */     Rotation finalRot = new Rotation(newYaw, newPitch);
/* 138 */     float rotSpeed = (mc.field_1724.method_6128() && target.method_6128()) ? 360.0F : 45.0F;
/* 139 */     RotationStorage.update(finalRot, rotSpeed, rotSpeed, rotSpeed, rotSpeed, 0, 1, Aura.clientLook.isState());
/*     */     
/* 141 */     this.rotate = new class_241(finalRot.getYaw(), finalRot.getPitch());
/* 142 */     this.lastYaw = finalRot.getYaw();
/* 143 */     this.lastPitch = finalRot.getPitch();
/*     */   }
/*     */   
/*     */   private class_243 getAimPoint(class_1309 target) {
/* 147 */     class_243 point = BestPoint.getPoint((class_1297)target);
/* 148 */     if (point == null) {
/* 149 */       point = target.method_5829().method_1005();
/*     */     }
/* 151 */     if (shouldUseElytraPredict(target)) {
/* 152 */       return getPredictedPoint(target, point);
/*     */     }
/* 154 */     return point;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\combat\components\rotations\WhiteRiseRotation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */