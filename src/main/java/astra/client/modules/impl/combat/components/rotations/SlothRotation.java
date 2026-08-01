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
/*     */ 
/*     */ 
/*     */ public class SlothRotation
/*     */   extends RotationsSystem
/*     */   implements QClient
/*     */ {
/*     */   private class_1309 trackedTarget;
/*     */   private float currentYaw;
/*     */   private float currentPitch;
/*     */   private float velocityYaw;
/*     */   private float velocityPitch;
/*     */   private double aimPointX;
/*     */   private double aimPointY;
/*     */   private double aimPointZ;
/*     */   private float noiseAngle;
/*  28 */   private final float noiseAmplitude = 1.8F;
/*     */   
/*     */   private int hitPhase;
/*     */   
/*     */   private int hitTimer;
/*     */   
/*     */   private float pitchBeforeHit;
/*     */   
/*     */   private long firstSeenTime;
/*     */   private int reactionMs;
/*     */   private boolean reactionComplete;
/*     */   private float lastSentYaw;
/*     */   private float lastSentPitch;
/*     */   private float smoothYaw;
/*     */   private float smoothPitch;
/*     */   
/*     */   public void reset() {
/*  45 */     this.trackedTarget = null;
/*  46 */     this.velocityYaw = this.velocityPitch = 0.0F;
/*  47 */     this.aimPointX = this.aimPointY = this.aimPointZ = 0.0D;
/*  48 */     this.noiseAngle = 0.0F;
/*  49 */     this.hitPhase = this.hitTimer = 0;
/*  50 */     this.firstSeenTime = 0L;
/*  51 */     this.reactionComplete = false;
/*  52 */     this.reactionMs = 0;
/*     */     
/*  54 */     if (mc.field_1724 != null) {
/*  55 */       this.currentYaw = mc.field_1724.method_36454();
/*  56 */       this.currentPitch = mc.field_1724.method_36455();
/*  57 */       this.lastSentYaw = this.currentYaw;
/*  58 */       this.lastSentPitch = this.currentPitch;
/*  59 */       this.smoothYaw = this.currentYaw;
/*  60 */       this.smoothPitch = this.currentPitch;
/*     */     } else {
/*  62 */       this.currentYaw = this.currentPitch = 0.0F;
/*  63 */       this.lastSentYaw = this.lastSentPitch = 0.0F;
/*  64 */       this.smoothYaw = this.smoothPitch = 0.0F;
/*     */     } 
/*     */   }
/*     */   
/*     */   private float calcGcd() {
/*  69 */     double s = ((Double)mc.field_1690.method_42495().method_41753()).doubleValue() * 0.6D + 0.2D;
/*  70 */     return (float)(s * s * s * 1.2D);
/*     */   }
/*     */   
/*     */   private void pickAimPoint(class_1309 e) {
/*  74 */     class_238 bb = e.method_5829();
/*  75 */     double w = bb.field_1320 - bb.field_1323;
/*  76 */     double h = bb.field_1325 - bb.field_1322;
/*  77 */     double d = bb.field_1324 - bb.field_1321;
/*     */     
/*  79 */     this.aimPointX = (Math.random() - 0.5D) * w * 0.12D;
/*  80 */     this.aimPointY = (Math.random() - 0.5D) * h * 0.11D;
/*  81 */     this.aimPointZ = (Math.random() - 0.5D) * d * 0.12D;
/*     */   }
/*     */   
/*     */   public void onAttack() {
/*  85 */     this.hitPhase = 1;
/*  86 */     this.hitTimer = 0;
/*  87 */     this.pitchBeforeHit = this.currentPitch;
/*     */   }
/*     */   
/*     */   private float measureAngle(class_1309 e) {
/*  91 */     if (mc.field_1724 == null) return 0.0F;
/*     */     
/*  93 */     class_243 eyes = mc.field_1724.method_33571();
/*  94 */     class_243 mid = e.method_5829().method_1005();
/*  95 */     class_243 delta = mid.method_1020(eyes);
/*     */     
/*  97 */     float needYaw = (float)Math.toDegrees(Math.atan2(delta.field_1350, delta.field_1352)) - 90.0F;
/*  98 */     float needPitch = (float)-Math.toDegrees(Math.atan2(delta.field_1351, delta.method_37267()));
/*     */     
/* 100 */     float dYaw = Math.abs(class_3532.method_15393(needYaw - mc.field_1724.method_36454()));
/* 101 */     float dPitch = Math.abs(needPitch - mc.field_1724.method_36455());
/*     */     
/* 103 */     return dYaw + dPitch;
/*     */   }
/*     */   
/*     */   private int computeReaction(float angle) {
/* 107 */     if (angle > 130.0F) return 140 + (int)(Math.random() * 90.0D); 
/* 108 */     if (angle > 70.0F) return 90 + (int)(Math.random() * 60.0D); 
/* 109 */     if (angle > 30.0F) return 45 + (int)(Math.random() * 35.0D); 
/* 110 */     return 12 + (int)(Math.random() * 20.0D);
/*     */   }
/*     */   
/*     */   private boolean isMovingForward() {
/* 114 */     if (mc.field_1724 == null) return false; 
/* 115 */     return mc.field_1690.field_1894.method_1434();
/*     */   }
/*     */   
/*     */   private boolean isOvertakingTarget(class_1309 target) {
/* 119 */     if (mc.field_1724 == null || target == null) return false;
/*     */     
/* 121 */     class_243 playerPos = mc.field_1724.method_19538();
/* 122 */     class_243 targetPos = target.method_19538();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 127 */     class_243 playerVel = new class_243(mc.field_1724.method_23317() - mc.field_1724.field_6014, mc.field_1724.method_23318() - mc.field_1724.field_6036, mc.field_1724.method_23321() - mc.field_1724.field_5969);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 133 */     class_243 targetVel = new class_243(target.method_23317() - target.field_6014, target.method_23318() - target.field_6036, target.method_23321() - target.field_5969);
/*     */ 
/*     */     
/* 136 */     class_243 toTarget = targetPos.method_1020(playerPos).method_1029();
/*     */     
/* 138 */     double playerSpeedToTarget = playerVel.method_1026(toTarget);
/* 139 */     double targetSpeedToPlayer = targetVel.method_1026(toTarget.method_1021(-1.0D));
/*     */     
/* 141 */     double relativeSpeed = playerSpeedToTarget + targetSpeedToPlayer;
/*     */     
/* 143 */     double distance = Math.sqrt(
/* 144 */         Math.pow(playerPos.field_1352 - targetPos.field_1352, 2.0D) + 
/* 145 */         Math.pow(playerPos.field_1350 - targetPos.field_1350, 2.0D));
/*     */ 
/*     */     
/* 148 */     return (relativeSpeed > 0.05D && distance < 4.0D);
/*     */   }
/*     */   
/*     */   private float[] generateNoise(float dist) {
/* 152 */     this.noiseAngle += 0.042F + (float)(Math.random() * 0.017999999225139618D);
/*     */     
/* 154 */     float scale = class_3532.method_15363(dist / 4.5F, 0.25F, 1.0F);
/* 155 */     float amp = 1.8F * scale;
/*     */     
/* 157 */     float n1 = (float)Math.sin(this.noiseAngle * 0.87D) * 0.38F;
/* 158 */     float n2 = (float)Math.sin(this.noiseAngle * 1.43D + 0.75D) * 0.28F;
/* 159 */     float n3 = (float)Math.cos(this.noiseAngle * 1.18D + 0.35D) * 0.32F;
/* 160 */     float n4 = (float)Math.cos(this.noiseAngle * 1.76D + 1.42D) * 0.23F;
/*     */     
/* 162 */     float yawNoise = (n1 + n2) * amp;
/* 163 */     float pitchNoise = (n3 + n4) * amp * 0.52F;
/*     */     
/* 165 */     yawNoise += ((float)Math.random() - 0.5F) * amp * 0.13F;
/* 166 */     pitchNoise += ((float)Math.random() - 0.5F) * amp * 0.09F;
/*     */     
/* 168 */     return new float[] { yawNoise, pitchNoise };
/*     */   }
/*     */   
/*     */   private float smoothStep(float x) {
/* 172 */     x = class_3532.method_15363(x, 0.0F, 1.0F);
/* 173 */     return x * x * (3.0F - 2.0F * x);
/*     */   }
/*     */   
/*     */   private float accelCurve(float x) {
/* 177 */     x = class_3532.method_15363(x, 0.0F, 1.0F);
/* 178 */     return 1.0F - (1.0F - x) * (1.0F - x);
/*     */   }
/*     */   
/*     */   private float springInterp(float current, float target, float vel, float stiffness, float damping) {
/* 182 */     float diff = target - current;
/* 183 */     float acc = diff * stiffness - vel * damping;
/* 184 */     return vel + acc;
/*     */   }
/*     */   
/*     */   private float smoothLerp(float from, float to, float alpha) {
/* 188 */     alpha = class_3532.method_15363(alpha, 0.0F, 1.0F);
/* 189 */     float delta = class_3532.method_15393(to - from);
/* 190 */     return from + delta * alpha;
/*     */   }
/*     */   
/*     */   private float calculateCurrentAngle(float targetYaw, float targetPitch) {
/* 194 */     float dYaw = Math.abs(class_3532.method_15393(targetYaw - this.currentYaw));
/* 195 */     float dPitch = Math.abs(targetPitch - this.currentPitch);
/* 196 */     return dYaw + dPitch;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateRotations(class_1309 target) {
/* 201 */     if (mc.field_1724 == null || target == null)
/*     */       return; 
/* 203 */     boolean playerFlying = mc.field_1724.method_6128();
/*     */     
/* 205 */     if (this.trackedTarget != target) {
/* 206 */       this.trackedTarget = target;
/*     */       
/* 208 */       this.currentYaw = mc.field_1724.method_36454();
/* 209 */       this.currentPitch = mc.field_1724.method_36455();
/* 210 */       this.lastSentYaw = this.currentYaw;
/* 211 */       this.lastSentPitch = this.currentPitch;
/* 212 */       this.smoothYaw = this.currentYaw;
/* 213 */       this.smoothPitch = this.currentPitch;
/* 214 */       this.velocityYaw = this.velocityPitch = 0.0F;
/*     */       
/* 216 */       pickAimPoint(target);
/*     */       
/* 218 */       this.hitPhase = this.hitTimer = 0;
/* 219 */       this.noiseAngle = (float)(Math.random() * Math.PI * 2.0D);
/*     */       
/* 221 */       float angleDiff = measureAngle(target);
/* 222 */       this.reactionMs = computeReaction(angleDiff);
/* 223 */       this.firstSeenTime = System.currentTimeMillis();
/* 224 */       this.reactionComplete = false;
/*     */     } 
/*     */     
/* 227 */     class_243 eyePos = mc.field_1724.method_33571();
/* 228 */     class_243 targetCenter = getPredictedPoint(target, target.method_5829().method_1005());
/* 229 */     float distance = (float)eyePos.method_1022(targetCenter);
/*     */     
/* 231 */     float gcd = calcGcd();
/*     */     
/* 233 */     if (!this.reactionComplete) {
/* 234 */       long elapsed = System.currentTimeMillis() - this.firstSeenTime;
/*     */       
/* 236 */       if (elapsed < this.reactionMs) {
/* 237 */         float jitterY = ((float)Math.random() - 0.5F) * 0.22F;
/* 238 */         float jitterP = ((float)Math.random() - 0.5F) * 0.14F;
/*     */         
/* 240 */         float f1 = this.lastSentYaw + jitterY;
/* 241 */         float f2 = class_3532.method_15363(this.lastSentPitch + jitterP, -89.0F, 89.0F);
/*     */         
/* 243 */         f1 -= (f1 - this.lastSentYaw) % gcd;
/* 244 */         f2 -= (f2 - this.lastSentPitch) % gcd;
/*     */         
/* 246 */         this.lastSentYaw = f1;
/* 247 */         this.lastSentPitch = f2;
/*     */         
/* 249 */         RotationStorage.update(new Rotation(f1, f2), 360.0F, 45.0F, 45.0F, 45.0F, 0, 1, Aura.clientLook.isState());
/*     */         
/*     */         return;
/*     */       } 
/* 253 */       this.reactionComplete = true;
/*     */     } 
/*     */     
/* 256 */     float[] noise = generateNoise(distance);
/*     */     
/* 258 */     if (this.hitPhase > 0) {
/* 259 */       this.hitTimer++;
/*     */       
/* 261 */       int upDuration = 25;
/* 262 */       int downDuration = 20;
/* 263 */       float targetPitchUp = -89.0F;
/*     */       
/* 265 */       if (this.hitPhase == 1) {
/* 266 */         float t = this.hitTimer / upDuration;
/* 267 */         t = class_3532.method_15363(t, 0.0F, 1.0F);
/* 268 */         float curved = accelCurve(t);
/* 269 */         this.currentPitch = class_3532.method_16439(curved, this.pitchBeforeHit, targetPitchUp);
/*     */         
/* 271 */         if (this.hitTimer >= upDuration) {
/* 272 */           this.hitPhase = 2;
/* 273 */           this.hitTimer = 0;
/*     */         } 
/* 275 */       } else if (this.hitPhase == 2) {
/* 276 */         float goal = this.pitchBeforeHit;
/* 277 */         float t = this.hitTimer / downDuration;
/* 278 */         t = class_3532.method_15363(t, 0.0F, 1.0F);
/* 279 */         float curved = smoothStep(t);
/* 280 */         this.currentPitch = class_3532.method_16439(curved, targetPitchUp, goal);
/*     */         
/* 282 */         if (this.hitTimer >= downDuration) {
/* 283 */           this.hitPhase = 0;
/* 284 */           this.hitTimer = 0;
/*     */         } 
/*     */       } 
/*     */       
/* 288 */       float f1 = this.currentYaw + noise[0];
/* 289 */       float f2 = class_3532.method_15363(this.currentPitch + noise[1], -89.0F, 89.0F);
/*     */       
/* 291 */       f1 -= (f1 - this.lastSentYaw) % gcd;
/* 292 */       f2 -= (f2 - this.lastSentPitch) % gcd;
/*     */       
/* 294 */       this.lastSentYaw = f1;
/* 295 */       this.lastSentPitch = f2;
/*     */       
/* 297 */       RotationStorage.update(new Rotation(f1, f2), 360.0F, 45.0F, 45.0F, 45.0F, 0, 1, Aura.clientLook.isState());
/*     */       
/*     */       return;
/*     */     } 
/* 301 */     if (Math.random() < 0.015D) {
/* 302 */       pickAimPoint(target);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 308 */     class_243 targetVel = new class_243(target.method_23317() - target.field_6014, target.method_23318() - target.field_6036, target.method_23321() - target.field_5969);
/*     */ 
/*     */     
/* 311 */     int predictTicks = shouldUseElytraPredict(target) ? 0 : 2;
/* 312 */     class_243 predictedCenter = targetCenter.method_1019(targetVel.method_1021(predictTicks));
/* 313 */     class_243 aimPos = predictedCenter.method_1031(this.aimPointX, this.aimPointY, this.aimPointZ);
/* 314 */     class_243 direction = aimPos.method_1020(eyePos);
/*     */     
/* 316 */     float wantYaw = (float)class_3532.method_15338(Math.toDegrees(Math.atan2(direction.field_1350, direction.field_1352)) - 90.0D);
/* 317 */     float wantPitch = (float)-Math.toDegrees(Math.atan2(direction.field_1351, direction.method_37267()));
/*     */     
/* 319 */     float diffYaw = class_3532.method_15393(wantYaw - this.currentYaw);
/* 320 */     float diffPitch = wantPitch - this.currentPitch;
/*     */     
/* 322 */     float speedMultiplier = 1.0F;
/*     */     
/* 324 */     if (playerFlying) {
/* 325 */       float currentAngle = calculateCurrentAngle(wantYaw, wantPitch);
/*     */       
/* 327 */       if (currentAngle > 120.0F) {
/* 328 */         speedMultiplier = 0.18F;
/* 329 */       } else if (currentAngle > 80.0F) {
/* 330 */         float t = (currentAngle - 80.0F) / 40.0F;
/* 331 */         speedMultiplier = class_3532.method_16439(smoothStep(t), 0.35F, 0.18F);
/* 332 */       } else if (currentAngle > 25.0F) {
/* 333 */         float t = (currentAngle - 25.0F) / 55.0F;
/* 334 */         speedMultiplier = class_3532.method_16439(smoothStep(t), 0.65F, 0.35F);
/*     */       } else {
/* 336 */         speedMultiplier = 0.65F + 0.35F * (1.0F - currentAngle / 25.0F);
/*     */       } 
/*     */     } else {
/* 339 */       boolean movingForward = isMovingForward();
/* 340 */       boolean overtaking = isOvertakingTarget(target);
/* 341 */       if (movingForward || overtaking) {
/* 342 */         speedMultiplier = 0.5F;
/*     */       }
/*     */     } 
/*     */     
/* 346 */     float stiffness = (0.038F + (float)Math.random() * 0.009F) * speedMultiplier;
/* 347 */     float damping = 0.68F + 0.12F * (1.0F - speedMultiplier);
/*     */     
/* 349 */     float totalDiff = (float)Math.sqrt((diffYaw * diffYaw + diffPitch * diffPitch));
/*     */     
/* 351 */     if (totalDiff > 32.0F) {
/* 352 */       stiffness += 0.018F * speedMultiplier;
/* 353 */     } else if (totalDiff < 4.2F) {
/* 354 */       stiffness *= 0.48F;
/*     */     } 
/*     */     
/* 357 */     stiffness += class_3532.method_15363((distance - 1.6F) / 7.5F, 0.0F, 0.045F) * speedMultiplier;
/*     */     
/* 359 */     this.velocityYaw = springInterp(this.currentYaw, this.currentYaw + diffYaw, this.velocityYaw, stiffness, damping);
/* 360 */     this.velocityPitch = springInterp(this.currentPitch, wantPitch, this.velocityPitch, stiffness * 0.87F, damping);
/*     */     
/* 362 */     float maxVelYaw = 7.5F * speedMultiplier;
/* 363 */     float maxVelPitch = 5.8F * speedMultiplier;
/*     */     
/* 365 */     this.velocityYaw = class_3532.method_15363(this.velocityYaw, -maxVelYaw, maxVelYaw);
/* 366 */     this.velocityPitch = class_3532.method_15363(this.velocityPitch, -maxVelPitch, maxVelPitch);
/*     */     
/* 368 */     this.currentYaw += this.velocityYaw;
/* 369 */     this.currentPitch += this.velocityPitch;
/*     */     
/* 371 */     this.currentPitch = class_3532.method_15363(this.currentPitch, -89.0F, 89.0F);
/*     */     
/* 373 */     float smoothFactor = playerFlying ? (0.3F + speedMultiplier * 0.4F) : 0.85F;
/*     */     
/* 375 */     this.smoothYaw = smoothLerp(this.smoothYaw, this.currentYaw, smoothFactor);
/* 376 */     this.smoothPitch = smoothLerp(this.smoothPitch, this.currentPitch, smoothFactor * 0.95F);
/*     */     
/* 378 */     float outY = this.smoothYaw + noise[0];
/* 379 */     float outP = this.smoothPitch + noise[1];
/* 380 */     outP = class_3532.method_15363(outP, -89.0F, 89.0F);
/*     */     
/* 382 */     outY -= (outY - this.lastSentYaw) % gcd;
/* 383 */     outP -= (outP - this.lastSentPitch) % gcd;
/*     */     
/* 385 */     this.lastSentYaw = outY;
/* 386 */     this.lastSentPitch = outP;
/*     */     
/* 388 */     RotationStorage.update(new Rotation(outY, outP), 360.0F, 45.0F, 45.0F, 45.0F, 0, 1, Aura.clientLook.isState());
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\combat\components\rotations\SlothRotation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */