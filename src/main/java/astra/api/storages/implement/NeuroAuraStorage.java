/*     */ package shame.astra.api.storages.implement;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import java.util.concurrent.ThreadLocalRandom;
/*     */ import lombok.Generated;
/*     */ import net.minecraft.class_1309;
/*     */ import net.minecraft.class_238;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_3532;
/*     */ import shame.astra.api.QClient;
/*     */ import shame.astra.api.storages.implement.helpertstorages.NeuroPattern;
/*     */ import shame.astra.api.utils.rotate.Rotation;
/*     */ import shame.astra.client.modules.impl.combat.components.gcd.GCDUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NeuroAuraStorage
/*     */   implements QClient
/*     */ {
/*     */   private static final long MIN_RECORD_INTERVAL = 50L;
/*     */   private static final int MAX_FRAMES = 20000;
/*     */   private static final String PATTERNS_DIRECTORY = "data_patterns";
/*     */   private static final String LEGACY_PATTERNS_DIRECTORY = "neuro_patterns";
/*     */   private static final String PRIMARY_EXTENSION = ".data";
/*     */   private static final String LEGACY_EXTENSION = ".neuro";
/*     */   private static final float SYNC_SCORE_THRESHOLD = 45.0F;
/*     */   private static final float MAX_YAW_CORRECTION = 8.0F;
/*     */   private static final float MAX_PITCH_CORRECTION = 6.0F;
/*  42 */   private final List<NeuroPattern> recordedPatterns = new CopyOnWriteArrayList<>(); @Generated public List<NeuroPattern> getRecordedPatterns() { return this.recordedPatterns; }
/*     */   private boolean isRecording = false; @Generated
/*  44 */   public boolean isRecording() { return this.isRecording; } @Generated
/*  45 */   public void setRecording(boolean isRecording) { this.isRecording = isRecording; }
/*     */   private boolean isUsingNeuro = false; @Generated
/*  47 */   public boolean isUsingNeuro() { return this.isUsingNeuro; } @Generated
/*  48 */   public void setUsingNeuro(boolean isUsingNeuro) { this.isUsingNeuro = isUsingNeuro; }
/*     */   private boolean showStats = true; @Generated
/*  50 */   public boolean isShowStats() { return this.showStats; } @Generated
/*  51 */   public void setShowStats(boolean showStats) { this.showStats = showStats; }
/*     */   
/*  53 */   private String currentPatternName = null; @Generated public String getCurrentPatternName() { return this.currentPatternName; } @Generated
/*  54 */   public void setCurrentPatternName(String currentPatternName) { this.currentPatternName = currentPatternName; }
/*     */   
/*  56 */   private String lastDebugMessage = "Готов!"; @Generated public String getLastDebugMessage() { return this.lastDebugMessage; }
/*     */   
/*  58 */   private int recordedThisSession = 0; @Generated public int getRecordedThisSession() { return this.recordedThisSession; }
/*     */ 
/*     */   
/*  61 */   private long lastRecordTime = 0L;
/*  62 */   private float prevRecordYaw = 0.0F;
/*  63 */   private float prevRecordPitch = 0.0F;
/*     */   
/*     */   private boolean hasRecordedBefore = false;
/*  66 */   private final List<Frame> frames = new CopyOnWriteArrayList<>();
/*  67 */   private int playbackIndex = -1;
/*  68 */   private int ticksSinceSync = 0;
/*  69 */   private float smoothedYawDelta = 0.0F;
/*  70 */   private float smoothedPitchDelta = 0.0F;
/*  71 */   private float smoothedOutputYaw = Float.NaN;
/*  72 */   private float smoothedOutputPitch = Float.NaN;
/*  73 */   private float yawSpeedFactor = 1.0F;
/*  74 */   private float pitchSpeedFactor = 1.0F;
/*  75 */   private int speedProfileTicks = 0;
/*  76 */   private class_243 currentAimPoint = null;
/*  77 */   private class_243 targetRandomPoint = null;
/*  78 */   private int aimPointTicks = 0;
/*  79 */   private class_1309 lastAimTarget = null;
/*     */   private boolean lastWasIdle = true;
/*  81 */   private int attackCount = 0;
/*  82 */   private float randomXOffset = 0.0F;
/*  83 */   private float randomYRatio = 0.66F;
/*  84 */   private float randomZOffset = 0.0F;
/*     */   
/*     */   public NeuroAuraStorage() {
/*  87 */     createPatternsDirectory();
/*     */   }
/*     */   
/*     */   private void createPatternsDirectory() {
/*     */     try {
/*  92 */       Path path = Paths.get("data_patterns", new String[0]);
/*  93 */       if (!Files.exists(path, new java.nio.file.LinkOption[0])) {
/*  94 */         Files.createDirectories(path, (FileAttribute<?>[])new FileAttribute[0]);
/*     */       }
/*  96 */     } catch (IOException e) {
/*  97 */       this.lastDebugMessage = "§cОшибка папки";
/*     */     } 
/*     */   }
/*     */   
/*     */   public void recordTick(class_1309 target, float currentYaw, float currentPitch) {
/* 102 */     if (!this.isRecording || mc.field_1724 == null) {
/*     */       return;
/*     */     }
/*     */     
/* 106 */     long now = System.currentTimeMillis();
/* 107 */     if (now - this.lastRecordTime < 50L) {
/*     */       return;
/*     */     }
/*     */     
/* 111 */     float deltaYaw = 0.0F;
/* 112 */     float deltaPitch = 0.0F;
/*     */     
/* 114 */     if (this.hasRecordedBefore) {
/* 115 */       deltaYaw = class_3532.method_15393(currentYaw - this.prevRecordYaw);
/* 116 */       deltaPitch = currentPitch - this.prevRecordPitch;
/*     */     } 
/*     */     
/* 119 */     float angleYaw = 0.0F;
/* 120 */     float anglePitch = 0.0F;
/* 121 */     double distance = 0.0D;
/* 122 */     boolean hasTarget = (target != null);
/*     */     
/* 124 */     if (hasTarget) {
/* 125 */       AimData aimData = getAimData(target, currentYaw, currentPitch, null, true);
/* 126 */       angleYaw = aimData.angleYaw;
/* 127 */       anglePitch = aimData.anglePitch;
/* 128 */       distance = aimData.distance;
/*     */     } 
/*     */     
/* 131 */     Frame frame = new Frame();
/* 132 */     frame.deltaYaw = deltaYaw;
/* 133 */     frame.deltaPitch = deltaPitch;
/* 134 */     frame.angleYaw = angleYaw;
/* 135 */     frame.anglePitch = anglePitch;
/* 136 */     frame.distance = distance;
/* 137 */     frame.hasTarget = hasTarget;
/* 138 */     frame.smoothness = calculateSmoothness(deltaYaw, deltaPitch);
/*     */     
/* 140 */     this.frames.add(frame);
/* 141 */     while (this.frames.size() > 20000) {
/* 142 */       this.frames.remove(0);
/*     */     }
/*     */     
/* 145 */     if (hasTarget) {
/* 146 */       boolean crit = (mc.field_1724.field_6017 > 0.0F && !mc.field_1724.method_24828());
/* 147 */       String type = (target instanceof net.minecraft.class_1657) ? "player" : "mob";
/* 148 */       this.recordedPatterns.add(new NeuroPattern(angleYaw, anglePitch, deltaYaw, deltaPitch, distance, crit, 0.0D, type, frame.smoothness));
/*     */ 
/*     */ 
/*     */       
/* 152 */       while (this.recordedPatterns.size() > 20000) {
/* 153 */         this.recordedPatterns.remove(0);
/*     */       }
/*     */     } 
/*     */     
/* 157 */     this.prevRecordYaw = currentYaw;
/* 158 */     this.prevRecordPitch = currentPitch;
/* 159 */     this.hasRecordedBefore = true;
/* 160 */     this.lastRecordTime = now;
/* 161 */     this.recordedThisSession++;
/*     */     
/* 163 */     if (this.recordedThisSession % 20 == 0) {
/* 164 */       this.lastDebugMessage = "§aЗапись: §f" + this.frames.size();
/*     */     }
/*     */   }
/*     */   
/*     */   public Rotation getNeuroRotation(class_1309 target, float currentYaw, float currentPitch, boolean idle) {
/* 169 */     if (!this.isUsingNeuro || target == null || mc.field_1724 == null || this.frames.isEmpty()) {
/* 170 */       resetState();
/* 171 */       return null;
/*     */     } 
/*     */     
/* 174 */     if (!idle && this.lastWasIdle) {
/* 175 */       rollNewRandomPoint();
/* 176 */       this.attackCount++;
/*     */     } 
/* 178 */     this.lastWasIdle = idle;
/*     */     
/* 180 */     boolean needSync = (this.playbackIndex < 0 || this.playbackIndex >= this.frames.size());
/* 181 */     AimData aimData = getAimData(target, currentYaw, currentPitch, null, idle);
/* 182 */     boolean airborne = (!mc.field_1724.method_24828() || (mc.field_1724.method_18798()).field_1351 != 0.0D);
/*     */     
/* 184 */     if (Math.abs(aimData.angleYaw) > 110.0F) {
/* 185 */       needSync = true;
/* 186 */       this.smoothedYawDelta = 0.0F;
/* 187 */       this.smoothedPitchDelta = 0.0F;
/* 188 */       this.smoothedOutputYaw = currentYaw;
/* 189 */       this.smoothedOutputPitch = currentPitch;
/*     */     } 
/*     */     
/* 192 */     if (!needSync && this.ticksSinceSync >= 5) {
/* 193 */       Frame currentFrame = this.frames.get(this.playbackIndex);
/* 194 */       float yawDiff = Math.abs(class_3532.method_15393(currentFrame.angleYaw - aimData.angleYaw));
/* 195 */       float pitchDiff = Math.abs(currentFrame.anglePitch - aimData.anglePitch);
/* 196 */       float distDiff = (float)Math.abs(currentFrame.distance - aimData.distance);
/* 197 */       if (yawDiff + pitchDiff + distDiff * 0.3F > 45.0F) {
/* 198 */         needSync = true;
/*     */       }
/*     */     } 
/*     */     
/* 202 */     if (needSync) {
/* 203 */       this.playbackIndex = findBest(aimData.angleYaw, aimData.anglePitch, aimData.distance);
/* 204 */       this.ticksSinceSync = 0;
/*     */     } 
/*     */     
/* 207 */     Frame frame = this.frames.get(this.playbackIndex);
/* 208 */     aimData = getAimData(target, currentYaw, currentPitch, frame, idle);
/*     */     
/* 210 */     float applyYaw = frame.deltaYaw;
/* 211 */     float applyPitch = frame.deltaPitch;
/* 212 */     updateSpeedProfile(idle, airborne, aimData);
/*     */     
/* 214 */     if (Math.abs(frame.angleYaw) > 3.0F && Math.abs(aimData.angleYaw) > 3.0F && Math.signum(frame.angleYaw) != Math.signum(aimData.angleYaw)) {
/* 215 */       applyYaw = -applyYaw;
/*     */     }
/*     */     
/* 218 */     if (Math.abs(frame.anglePitch) > 3.0F && Math.abs(aimData.anglePitch) > 3.0F && Math.signum(frame.anglePitch) != Math.signum(aimData.anglePitch)) {
/* 219 */       applyPitch = -applyPitch;
/*     */     }
/*     */     
/* 222 */     applyYaw = adaptRecordedDelta(applyYaw, aimData.angleYaw, frame.smoothness, idle, 8.0F);
/* 223 */     applyPitch = adaptRecordedDelta(applyPitch, aimData.anglePitch, frame.smoothness, idle, 6.0F);
/*     */     
/* 225 */     if (Math.abs(aimData.angleYaw) < 32.0F) {
/* 226 */       applyYaw = class_3532.method_16439(0.58F, applyYaw, aimData.angleYaw);
/*     */     }
/*     */     
/* 229 */     if (Math.abs(aimData.anglePitch) < 24.0F) {
/* 230 */       applyPitch = class_3532.method_16439(0.52F, applyPitch, aimData.anglePitch);
/*     */     }
/*     */     
/* 233 */     this.smoothedYawDelta = smoothDelta(this.smoothedYawDelta, applyYaw, frame.smoothness);
/* 234 */     this.smoothedPitchDelta = smoothDelta(this.smoothedPitchDelta, applyPitch, frame.smoothness);
/*     */     
/* 236 */     float quantizedYaw = quantizeToMouseStep(this.smoothedYawDelta, aimData.angleYaw);
/* 237 */     float quantizedPitch = quantizeToMouseStep(this.smoothedPitchDelta, aimData.anglePitch);
/* 238 */     quantizedYaw += getMicroJitter(true, idle, airborne, aimData);
/* 239 */     quantizedPitch += getMicroJitter(false, idle, airborne, aimData);
/*     */     
/* 241 */     float rawYaw = class_3532.method_15393(currentYaw + quantizedYaw);
/* 242 */     float rawPitch = class_3532.method_15363(currentPitch + quantizedPitch, -90.0F, 90.0F);
/* 243 */     float finalYaw = smoothOutputRotation(rawYaw, currentYaw, frame.smoothness, idle, true);
/* 244 */     float finalPitch = smoothOutputRotation(rawPitch, currentPitch, frame.smoothness, idle, false);
/*     */     
/* 246 */     this.playbackIndex++;
/* 247 */     this.ticksSinceSync++;
/*     */     
/* 249 */     int skipped = 0;
/* 250 */     while (this.playbackIndex < this.frames.size() && !((Frame)this.frames.get(this.playbackIndex)).hasTarget && skipped < 5) {
/* 251 */       this.playbackIndex++;
/* 252 */       skipped++;
/*     */     } 
/*     */     
/* 255 */     if (this.playbackIndex >= this.frames.size()) {
/* 256 */       float newAngleYaw = class_3532.method_15393(aimData.perfectYaw - finalYaw);
/* 257 */       float newAnglePitch = aimData.perfectPitch - finalPitch;
/* 258 */       this.playbackIndex = findBest(newAngleYaw, newAnglePitch, aimData.distance);
/* 259 */       this.ticksSinceSync = 0;
/*     */     } 
/*     */     
/* 262 */     this.lastDebugMessage = String.format("§a[%d/%d] dY%.2f dP%.2f", new Object[] { Integer.valueOf(this.playbackIndex), Integer.valueOf(this.frames.size()), Float.valueOf(quantizedYaw), Float.valueOf(quantizedPitch) });
/* 263 */     return new Rotation(finalYaw, finalPitch);
/*     */   }
/*     */   
/*     */   private void rollNewRandomPoint() {
/* 267 */     ThreadLocalRandom r = ThreadLocalRandom.current();
/* 268 */     this.randomXOffset = r.nextFloat(-0.38F, 0.38F);
/* 269 */     this.randomYRatio = r.nextFloat(0.4F, 0.85F);
/* 270 */     this.randomZOffset = r.nextFloat(-0.38F, 0.38F);
/*     */   }
/*     */   
/*     */   private AimData getAimData(class_1309 target, float currentYaw, float currentPitch, Frame frame, boolean relaxed) {
/* 274 */     class_243 eyePos = mc.field_1724.method_33571();
/* 275 */     class_243 point = selectAimPoint(target, relaxed);
/* 276 */     double distance = eyePos.method_1022(point);
/*     */     
/* 278 */     double dx = point.field_1352 - eyePos.field_1352;
/* 279 */     double dy = point.field_1351 - eyePos.field_1351;
/* 280 */     double dz = point.field_1350 - eyePos.field_1350;
/* 281 */     double distXZ = Math.sqrt(dx * dx + dz * dz);
/*     */     
/* 283 */     float perfectYaw = (float)Math.toDegrees(Math.atan2(-dx, dz));
/* 284 */     float perfectPitch = (float)Math.toDegrees(Math.atan2(-dy, distXZ));
/*     */     
/* 286 */     AimData aimData = new AimData();
/* 287 */     aimData.targetPoint = point;
/* 288 */     aimData.distance = distance;
/* 289 */     aimData.perfectYaw = perfectYaw;
/* 290 */     aimData.perfectPitch = perfectPitch;
/* 291 */     aimData.angleYaw = class_3532.method_15393(perfectYaw - currentYaw);
/* 292 */     aimData.anglePitch = perfectPitch - currentPitch;
/* 293 */     return aimData;
/*     */   }
/*     */   
/*     */   private float adaptRecordedDelta(float recordedDelta, float currentAngle, float smoothness, boolean idle, float maxCorrection) {
/* 297 */     float correctionWeight = idle ? 0.14F : 0.045F;
/* 298 */     float correctionLimit = idle ? (maxCorrection * 0.65F) : (maxCorrection * 0.3F);
/* 299 */     float correction = class_3532.method_15363(currentAngle - recordedDelta, -correctionLimit, correctionLimit);
/* 300 */     float result = recordedDelta + correction * correctionWeight;
/*     */     
/* 302 */     if (Math.abs(currentAngle) < Math.abs(result) && Math.signum(currentAngle) == Math.signum(result)) {
/* 303 */       result = currentAngle;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 308 */     float preserveFactor = idle ? class_3532.method_15363(1.0F - smoothness * 0.22F, 0.8F, 0.97F) : class_3532.method_15363(1.0F - smoothness * 0.1F, 0.91F, 0.99F);
/* 309 */     result *= preserveFactor;
/*     */     
/* 311 */     if (Math.abs(currentAngle) <= GCDUtil.getGCDValue()) {
/* 312 */       return currentAngle;
/*     */     }
/*     */     
/* 315 */     return result;
/*     */   }
/*     */   
/*     */   private class_243 selectAimPoint(class_1309 target, boolean relaxed) {
/* 319 */     if (target != this.lastAimTarget) {
/* 320 */       this.lastAimTarget = target;
/* 321 */       this.currentAimPoint = null;
/* 322 */       this.targetRandomPoint = null;
/* 323 */       this.aimPointTicks = 0;
/* 324 */       rollNewRandomPoint();
/*     */     } 
/*     */     
/* 327 */     class_238 box = target.method_5829();
/* 328 */     class_243 eyePos = mc.field_1724.method_33571();
/*     */ 
/*     */ 
/*     */     
/* 332 */     class_243 stablePoint = new class_243((box.method_1005()).field_1352, box.field_1322 + box.method_17940() * 0.72D, (box.method_1005()).field_1350);
/*     */     
/* 334 */     if (box.method_1014(0.12D).method_1006(eyePos) || eyePos.method_1025(stablePoint) <= 2.25D) {
/* 335 */       this.currentAimPoint = stablePoint;
/* 336 */       this.targetRandomPoint = stablePoint;
/* 337 */       this.aimPointTicks = 0;
/* 338 */       return stablePoint;
/*     */     } 
/*     */     
/* 341 */     double xCenter = (box.field_1323 + box.field_1320) * 0.5D;
/* 342 */     double zCenter = (box.field_1321 + box.field_1324) * 0.5D;
/* 343 */     double halfW = box.method_17939() * 0.5D;
/* 344 */     double halfD = box.method_17941() * 0.5D;
/* 345 */     double height = box.method_17940();
/*     */     
/* 347 */     class_243 desired = new class_243(xCenter + halfW * this.randomXOffset, box.field_1322 + height * this.randomYRatio, zCenter + halfD * this.randomZOffset);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 353 */     if (this.targetRandomPoint == null) {
/* 354 */       this.targetRandomPoint = desired;
/*     */     } else {
/* 356 */       float driftLerp = relaxed ? 0.13F : 0.07F;
/* 357 */       this
/*     */ 
/*     */         
/* 360 */         .targetRandomPoint = new class_243(class_3532.method_16436(driftLerp, this.targetRandomPoint.field_1352, desired.field_1352), class_3532.method_16436(driftLerp, this.targetRandomPoint.field_1351, desired.field_1351), class_3532.method_16436(driftLerp, this.targetRandomPoint.field_1350, desired.field_1350));
/*     */     } 
/*     */ 
/*     */     
/* 364 */     if (this.currentAimPoint == null) {
/* 365 */       this.currentAimPoint = this.targetRandomPoint;
/* 366 */       this.aimPointTicks = 0;
/* 367 */       return this.currentAimPoint;
/*     */     } 
/*     */     
/* 370 */     float pointLerp = relaxed ? 0.11F : 0.055F;
/* 371 */     this
/*     */ 
/*     */       
/* 374 */       .currentAimPoint = new class_243(class_3532.method_16436(pointLerp, this.currentAimPoint.field_1352, this.targetRandomPoint.field_1352), class_3532.method_16436(pointLerp, this.currentAimPoint.field_1351, this.targetRandomPoint.field_1351), class_3532.method_16436(pointLerp, this.currentAimPoint.field_1350, this.targetRandomPoint.field_1350));
/*     */     
/* 376 */     this.aimPointTicks++;
/* 377 */     return this.currentAimPoint;
/*     */   }
/*     */   
/*     */   private float smoothDelta(float current, float target, float smoothness) {
/* 381 */     float lerpFactor = class_3532.method_15363(0.035F + (1.0F - smoothness) * 0.12F, 0.035F, 0.15F);
/* 382 */     return current + (target - current) * lerpFactor;
/*     */   }
/*     */   
/*     */   private float smoothOutputRotation(float targetRotation, float currentRotation, float smoothness, boolean idle, boolean yawAxis) {
/* 386 */     float previous = yawAxis ? this.smoothedOutputYaw : this.smoothedOutputPitch;
/* 387 */     if (Float.isNaN(previous)) {
/* 388 */       previous = currentRotation;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 393 */     float delta = yawAxis ? class_3532.method_15393(targetRotation - previous) : (targetRotation - previous);
/*     */ 
/*     */ 
/*     */     
/* 397 */     float maxStep = yawAxis ? (idle ? 1.68F : 0.86F) : (idle ? 1.26F : 0.62F);
/*     */ 
/*     */     
/* 400 */     float lerpFactor = idle ? class_3532.method_15363(0.08F + (1.0F - smoothness) * 0.09F, 0.08F, 0.17F) : class_3532.method_15363(0.04F + (1.0F - smoothness) * 0.055F, 0.04F, 0.095F);
/*     */     
/* 402 */     maxStep *= yawAxis ? this.yawSpeedFactor : this.pitchSpeedFactor;
/* 403 */     lerpFactor *= yawAxis ? this.yawSpeedFactor : this.pitchSpeedFactor;
/*     */     
/* 405 */     float smoothed = previous + class_3532.method_15363(delta * lerpFactor, -maxStep, maxStep);
/* 406 */     if (yawAxis) {
/* 407 */       smoothed = class_3532.method_15393(smoothed);
/* 408 */       this.smoothedOutputYaw = smoothed;
/*     */     } else {
/* 410 */       smoothed = class_3532.method_15363(smoothed, -90.0F, 90.0F);
/* 411 */       this.smoothedOutputPitch = smoothed;
/*     */     } 
/* 413 */     return smoothed;
/*     */   }
/*     */   
/*     */   private void updateSpeedProfile(boolean idle, boolean airborne, AimData aimData) {
/* 417 */     if (this.speedProfileTicks > 0) {
/* 418 */       this.speedProfileTicks--;
/*     */       
/*     */       return;
/*     */     } 
/* 422 */     ThreadLocalRandom random = ThreadLocalRandom.current();
/* 423 */     float anglePressure = class_3532.method_15363((Math.abs(aimData.angleYaw) + Math.abs(aimData.anglePitch)) / 35.0F, 0.0F, 1.0F);
/* 424 */     float baseYawMin = idle ? 1.06F : 0.96F;
/* 425 */     float baseYawMax = idle ? 1.34F : 1.12F;
/* 426 */     float basePitchMin = idle ? 1.0F : 0.9F;
/* 427 */     float basePitchMax = idle ? 1.24F : 1.05F;
/*     */     
/* 429 */     this.yawSpeedFactor = random.nextFloat(baseYawMin, baseYawMax + anglePressure * (idle ? 0.1F : 0.16F));
/* 430 */     this.pitchSpeedFactor = random.nextFloat(basePitchMin, basePitchMax + anglePressure * (idle ? 0.08F : 0.12F));
/*     */     
/* 432 */     if (!idle && anglePressure > 0.58F) {
/* 433 */       this.yawSpeedFactor = Math.max(this.yawSpeedFactor, 1.08F + anglePressure * 0.24F);
/* 434 */       this.pitchSpeedFactor = Math.max(this.pitchSpeedFactor, 1.0F + anglePressure * 0.18F);
/*     */     } 
/*     */     
/* 437 */     if (airborne) {
/* 438 */       this.yawSpeedFactor *= 0.97F;
/* 439 */       this.pitchSpeedFactor *= 0.95F;
/*     */     } 
/*     */     
/* 442 */     this.speedProfileTicks = random.nextInt(idle ? 3 : 2, idle ? 7 : 5);
/*     */   }
/*     */   
/*     */   private float getMicroJitter(boolean yawAxis, boolean idle, boolean airborne, AimData aimData) {
/* 446 */     float gcd = GCDUtil.getGCDValue();
/* 447 */     if (gcd <= 0.0F) {
/* 448 */       return 0.0F;
/*     */     }
/*     */     
/* 451 */     float pressure = Math.abs(yawAxis ? aimData.angleYaw : aimData.anglePitch);
/* 452 */     if (!idle && pressure > (yawAxis ? 10.0F : 7.0F)) {
/* 453 */       return 0.0F;
/*     */     }
/*     */     
/* 456 */     float amplitude = yawAxis ? (gcd * 0.018F) : (gcd * 0.012F);
/* 457 */     if (airborne) {
/* 458 */       amplitude *= 0.35F;
/*     */     }
/* 460 */     float wave = (float)Math.sin(((mc.field_1724.field_6012 + (yawAxis ? 0.0F : 7.0F)) * (idle ? 0.42F : 0.28F)));
/* 461 */     return wave * amplitude;
/*     */   }
/*     */   
/*     */   private float quantizeToMouseStep(float delta, float remainingAngle) {
/* 465 */     float gcd = GCDUtil.getGCDValue();
/* 466 */     if (gcd <= 0.0F) {
/* 467 */       return delta;
/*     */     }
/*     */     
/* 470 */     float limited = delta;
/* 471 */     if (Math.abs(remainingAngle) < Math.abs(limited) && Math.signum(remainingAngle) == Math.signum(limited)) {
/* 472 */       limited = remainingAngle;
/*     */     }
/*     */     
/* 475 */     float quantized = Math.round(limited / gcd) * gcd;
/* 476 */     if (quantized == 0.0F && Math.abs(remainingAngle) >= gcd * 0.35F && Math.abs(limited) > 0.001F) {
/* 477 */       quantized = Math.signum(limited) * gcd;
/*     */     }
/*     */     
/* 480 */     if (Math.abs(remainingAngle) < Math.abs(quantized) && Math.signum(remainingAngle) == Math.signum(quantized)) {
/* 481 */       quantized = remainingAngle;
/*     */     }
/*     */     
/* 484 */     return quantized;
/*     */   }
/*     */   
/*     */   private float calculateSmoothness(float deltaYaw, float deltaPitch) {
/* 488 */     float magnitude = Math.abs(deltaYaw) + Math.abs(deltaPitch);
/* 489 */     float base = 1.0F - magnitude / 18.0F;
/* 490 */     float periodic = (float)Math.sin(((this.recordedThisSession + mc.field_1724.field_6012 * 0.31F) * 0.34F)) * 0.012F;
/* 491 */     float noise = ThreadLocalRandom.current().nextFloat(-0.008F, 0.008F);
/* 492 */     return class_3532.method_15363(base + periodic + noise, 0.22F, 0.88F);
/*     */   }
/*     */   
/*     */   private int findBest(float angleYaw, float anglePitch, double distance) {
/* 496 */     int best = 0;
/* 497 */     float bestScore = Float.MAX_VALUE;
/*     */     
/* 499 */     for (int i = 0; i < this.frames.size(); i++) {
/* 500 */       Frame frame = this.frames.get(i);
/* 501 */       if (frame.hasTarget) {
/*     */ 
/*     */ 
/*     */         
/* 505 */         float yawDiff = Math.abs(class_3532.method_15393(frame.angleYaw - angleYaw));
/* 506 */         float pitchDiff = Math.abs(frame.anglePitch - anglePitch);
/* 507 */         float distanceDiff = (float)Math.abs(frame.distance - distance);
/* 508 */         float score = yawDiff + pitchDiff + distanceDiff * 0.3F;
/*     */         
/* 510 */         if (score < bestScore) {
/* 511 */           bestScore = score;
/* 512 */           best = i;
/*     */         } 
/*     */       } 
/*     */     } 
/* 516 */     return best;
/*     */   }
/*     */   
/*     */   public boolean savePatterns(String profileName) {
/* 520 */     if (this.frames.isEmpty()) {
/* 521 */       this.lastDebugMessage = "§cНет записей";
/* 522 */       return false;
/*     */     } 
/*     */     
/* 525 */     try { ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("data_patterns/" + profileName + ".data"));
/*     */       
/* 527 */       try { SaveData data = new SaveData();
/* 528 */         data.patterns = new ArrayList<>(this.recordedPatterns);
/* 529 */         data.frames = new ArrayList<>(this.frames);
/* 530 */         out.writeObject(data);
/* 531 */         this.currentPatternName = profileName;
/* 532 */         this.lastDebugMessage = "§aСохранено " + this.frames.size();
/* 533 */         boolean bool = true;
/* 534 */         out.close(); return bool; } catch (Throwable throwable) { try { out.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException e)
/* 535 */     { this.lastDebugMessage = "§cОшибка сохранения";
/* 536 */       return false; }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean loadPatterns(String profileName) {
/* 542 */     File file = resolveProfileFile(profileName);
/* 543 */     if (!file.exists()) {
/* 544 */       this.lastDebugMessage = "§eНе найдено: " + profileName;
/* 545 */       return false;
/*     */     } 
/*     */     
/* 548 */     try { ObjectInputStream in = new ObjectInputStream(new FileInputStream(file)); 
/* 549 */       try { Object obj = in.readObject();
/* 550 */         this.recordedPatterns.clear();
/* 551 */         this.frames.clear();
/*     */         
/* 553 */         if (obj instanceof SaveData) { SaveData data = (SaveData)obj;
/* 554 */           if (data.patterns != null) {
/* 555 */             this.recordedPatterns.addAll(data.patterns);
/*     */           }
/* 557 */           if (data.frames != null) {
/* 558 */             this.frames.addAll(data.frames);
/*     */           } }
/* 560 */         else if (obj instanceof List) { List<?> list = (List)obj;
/* 561 */           this.recordedPatterns.addAll(list); }
/*     */ 
/*     */         
/* 564 */         if (this.frames.isEmpty() && !this.recordedPatterns.isEmpty()) {
/* 565 */           rebuildFramesFromPatterns();
/*     */         }
/*     */         
/* 568 */         this.currentPatternName = profileName;
/* 569 */         resetState();
/* 570 */         this.lastDebugMessage = "§aЗагружено " + this.frames.size();
/* 571 */         boolean bool = !this.frames.isEmpty() ? true : false;
/* 572 */         in.close(); return bool; } catch (Throwable throwable) { try { in.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException|ClassNotFoundException e)
/* 573 */     { this.lastDebugMessage = "§cОшибка загрузки";
/* 574 */       return false; }
/*     */   
/*     */   }
/*     */   
/*     */   private void rebuildFramesFromPatterns() {
/* 579 */     for (NeuroPattern pattern : this.recordedPatterns) {
/* 580 */       Frame frame = new Frame();
/* 581 */       frame.deltaYaw = pattern.getDeltaYaw();
/* 582 */       frame.deltaPitch = pattern.getDeltaPitch();
/* 583 */       frame.angleYaw = pattern.getYaw();
/* 584 */       frame.anglePitch = pattern.getPitch();
/* 585 */       frame.distance = pattern.getDistance();
/* 586 */       frame.hasTarget = true;
/* 587 */       frame.smoothness = class_3532.method_15363(pattern.getSmoothness(), 0.18F, 0.9F);
/* 588 */       this.frames.add(frame);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean deletePatterns(String profileName) {
/* 593 */     File primaryFile = new File("data_patterns/" + profileName + ".data");
/* 594 */     File legacyFile = new File("neuro_patterns/" + profileName + ".neuro");
/* 595 */     boolean deleted = false;
/*     */     
/* 597 */     if (primaryFile.exists()) {
/* 598 */       deleted = primaryFile.delete();
/*     */     }
/* 600 */     if (legacyFile.exists()) {
/* 601 */       deleted = (legacyFile.delete() || deleted);
/*     */     }
/*     */     
/* 604 */     if (deleted) {
/* 605 */       if (profileName.equals(this.currentPatternName)) {
/* 606 */         this.currentPatternName = null;
/*     */       }
/* 608 */       this.lastDebugMessage = "§aУдалено";
/* 609 */       return true;
/*     */     } 
/*     */     
/* 612 */     return false;
/*     */   }
/*     */   
/*     */   public int getPatternCount() {
/* 616 */     return this.recordedPatterns.size();
/*     */   }
/*     */   
/*     */   public int getFrameCount() {
/* 620 */     return this.frames.size();
/*     */   }
/*     */   
/*     */   public void startRecording() {
/* 624 */     this.recordedPatterns.clear();
/* 625 */     this.frames.clear();
/* 626 */     this.isRecording = true;
/* 627 */     this.isUsingNeuro = false;
/* 628 */     this.recordedThisSession = 0;
/* 629 */     this.lastRecordTime = 0L;
/* 630 */     this.currentPatternName = null;
/* 631 */     this.hasRecordedBefore = false;
/* 632 */     this.prevRecordYaw = 0.0F;
/* 633 */     this.prevRecordPitch = 0.0F;
/* 634 */     resetState();
/* 635 */     this.lastDebugMessage = "§aЗапись";
/*     */   }
/*     */   
/*     */   public void stopRecording() {
/* 639 */     this.isRecording = false;
/* 640 */     this.lastDebugMessage = "§eСтоп: " + this.frames.size();
/*     */   }
/*     */   
/*     */   public void clearPatterns() {
/* 644 */     this.recordedPatterns.clear();
/* 645 */     this.frames.clear();
/* 646 */     this.isRecording = false;
/* 647 */     this.isUsingNeuro = false;
/* 648 */     this.recordedThisSession = 0;
/* 649 */     this.currentPatternName = null;
/* 650 */     this.hasRecordedBefore = false;
/* 651 */     this.prevRecordYaw = 0.0F;
/* 652 */     this.prevRecordPitch = 0.0F;
/* 653 */     resetState();
/* 654 */     this.lastDebugMessage = "§eОчищено";
/*     */   }
/*     */   
/*     */   public void resetState() {
/* 658 */     this.playbackIndex = -1;
/* 659 */     this.ticksSinceSync = 0;
/* 660 */     this.smoothedYawDelta = 0.0F;
/* 661 */     this.smoothedPitchDelta = 0.0F;
/* 662 */     this.smoothedOutputYaw = Float.NaN;
/* 663 */     this.smoothedOutputPitch = Float.NaN;
/* 664 */     this.yawSpeedFactor = 1.0F;
/* 665 */     this.pitchSpeedFactor = 1.0F;
/* 666 */     this.speedProfileTicks = 0;
/* 667 */     this.currentAimPoint = null;
/* 668 */     this.targetRandomPoint = null;
/* 669 */     this.aimPointTicks = 0;
/* 670 */     this.lastAimTarget = null;
/* 671 */     this.lastWasIdle = true;
/* 672 */     this.attackCount = 0;
/* 673 */     rollNewRandomPoint();
/*     */   }
/*     */   
/*     */   public String getStatusString() {
/* 677 */     String status = "§8[§bData§8] §f" + this.frames.size();
/* 678 */     if (this.isRecording) {
/* 679 */       status = status + " §a[REC]";
/*     */     }
/* 681 */     if (this.isUsingNeuro) {
/* 682 */       status = status + " §b[ON " + status + "]";
/*     */     }
/* 684 */     return status;
/*     */   }
/*     */   
/*     */   public List<String> getPatternNames() {
/* 688 */     List<String> names = new ArrayList<>();
/* 689 */     collectPatternNames(names, new File("data_patterns"), ".data");
/* 690 */     collectPatternNames(names, new File("neuro_patterns"), ".neuro");
/* 691 */     return names;
/*     */   }
/*     */   
/*     */   private void collectPatternNames(List<String> names, File directory, String extension) {
/* 695 */     if (!directory.exists() || !directory.isDirectory()) {
/*     */       return;
/*     */     }
/*     */     
/* 699 */     File[] files = directory.listFiles((dir, name) -> name.endsWith(extension));
/* 700 */     if (files == null) {
/*     */       return;
/*     */     }
/*     */     
/* 704 */     for (File file : files) {
/* 705 */       String name = file.getName().replace(extension, "");
/* 706 */       if (!names.contains(name)) {
/* 707 */         names.add(name);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private File resolveProfileFile(String profileName) {
/* 713 */     File primaryFile = new File("data_patterns/" + profileName + ".data");
/* 714 */     if (primaryFile.exists()) {
/* 715 */       return primaryFile;
/*     */     }
/* 717 */     return new File("neuro_patterns/" + profileName + ".neuro");
/*     */   }
/*     */   
/*     */   private static class AimData {
/*     */     class_243 targetPoint;
/*     */     float perfectYaw;
/*     */     float perfectPitch;
/*     */     float angleYaw;
/*     */     float anglePitch;
/*     */     double distance;
/*     */   }
/*     */   
/*     */   private static class Frame implements Serializable {
/*     */     private static final long serialVersionUID = 7L;
/*     */     float deltaYaw;
/*     */     float deltaPitch;
/*     */     float angleYaw;
/*     */     float anglePitch;
/*     */     double distance;
/*     */     boolean hasTarget;
/*     */     float smoothness;
/*     */   }
/*     */   
/*     */   private static class SaveData implements Serializable {
/*     */     private static final long serialVersionUID = 7L;
/*     */     List<NeuroPattern> patterns;
/*     */     List<NeuroAuraStorage.Frame> frames;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\api\storages\implement\NeuroAuraStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */