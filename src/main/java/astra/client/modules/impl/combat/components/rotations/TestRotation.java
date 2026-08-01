/*     */ package shame.astra.client.modules.impl.combat.components.rotations;
/*     */ 
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParser;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.ThreadLocalRandom;
/*     */ import net.minecraft.class_1309;
/*     */ import net.minecraft.class_238;
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
/*     */ 
/*     */ public class TestRotation
/*     */   extends RotationsSystem
/*     */   implements QClient
/*     */ {
/*  31 */   private static final Path DATASET_PATH = Path.of(System.getProperty("user.home"), new String[] { "Desktop", "data.json" });
/*     */   
/*  33 */   private final List<DatasetFrame> frames = new ArrayList<>();
/*     */   
/*     */   private class_1309 trackedTarget;
/*     */   
/*     */   private class_1309 trackedRotationTarget;
/*     */   private class_243 currentAimPoint;
/*     */   private class_243 targetAimPoint;
/*  40 */   private long lastModified = Long.MIN_VALUE;
/*     */   
/*     */   private long lastLoadAttempt;
/*     */   
/*     */   private boolean datasetReady;
/*     */   private int playbackIndex;
/*     */   private int aimPointTicks;
/*     */   private int aimPointRefreshTicks;
/*     */   private int smoothProfileTicks;
/*     */   private float smoothYawStep;
/*     */   private float smoothPitchStep;
/*     */   private float smoothYaw;
/*     */   private float smoothPitch;
/*  53 */   private float yawSmoothFactor = 1.0F;
/*  54 */   private float pitchSmoothFactor = 1.0F;
/*     */   
/*     */   private boolean hasRotationState;
/*     */   
/*     */   public void reset() {
/*  59 */     this.trackedTarget = null;
/*  60 */     this.trackedRotationTarget = null;
/*  61 */     this.currentAimPoint = null;
/*  62 */     this.targetAimPoint = null;
/*  63 */     this.playbackIndex = 0;
/*  64 */     this.aimPointTicks = 0;
/*  65 */     this.aimPointRefreshTicks = 0;
/*  66 */     this.smoothProfileTicks = 0;
/*  67 */     this.smoothYawStep = 0.0F;
/*  68 */     this.smoothPitchStep = 0.0F;
/*  69 */     this.smoothYaw = 0.0F;
/*  70 */     this.smoothPitch = 0.0F;
/*  71 */     this.yawSmoothFactor = 1.0F;
/*  72 */     this.pitchSmoothFactor = 1.0F;
/*  73 */     this.hasRotationState = false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateRotations(class_1309 target) {
/*  79 */     if (mc.field_1724 == null || target == null) {
/*     */       return;
/*     */     }
/*     */     
/*  83 */     boolean focus = shouldFocus();
/*  84 */     ensureDatasetLoaded();
/*  85 */     class_243 aimPoint = selectAimPoint(target, focus);
/*  86 */     class_241 rot = RotationUtils.getRotations(aimPoint);
/*     */     
/*  88 */     if (!this.datasetReady || this.frames.isEmpty()) {
/*  89 */       RotationStorage.update(new Rotation(rot.field_1343, 
/*  90 */             class_3532.method_15363(rot.field_1342, -89.0F, 89.0F)), 360.0F, 360.0F, 45.0F, 45.0F, 0, 1, Aura.clientLook
/*     */           
/*  92 */           .isState());
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  97 */     float currentYaw = mc.field_1724.method_36454();
/*  98 */     float currentPitch = mc.field_1724.method_36455();
/*  99 */     syncRotationState(target, currentYaw, currentPitch);
/*     */     
/* 101 */     float remainingYaw = class_3532.method_15393(rot.field_1343 - this.smoothYaw);
/* 102 */     float remainingPitch = rot.field_1342 - this.smoothPitch;
/*     */     
/* 104 */     DatasetFrame frame = pickFrame(remainingYaw, remainingPitch, focus);
/* 105 */     updateSmoothProfile(frame, remainingYaw, remainingPitch, focus);
/*     */     
/* 107 */     float gcd = Math.max(GCDUtil.getGCDValue(), 1.0E-4F);
/* 108 */     float yawStep = buildAxisStep(remainingYaw, frame, true, focus);
/* 109 */     float pitchStep = buildAxisStep(remainingPitch, frame, false, focus);
/*     */     
/* 111 */     yawStep += buildJitter(frame, remainingYaw, true, gcd);
/* 112 */     pitchStep += buildJitter(frame, remainingPitch, false, gcd);
/*     */     
/* 114 */     this.smoothYawStep = smoothAxisStep(this.smoothYawStep, yawStep, remainingYaw, true, focus);
/* 115 */     this.smoothPitchStep = smoothAxisStep(this.smoothPitchStep, pitchStep, remainingPitch, false, focus);
/*     */     
/* 117 */     float quantizedYawStep = quantizeDelta(this.smoothYawStep, remainingYaw, gcd, true);
/* 118 */     float quantizedPitchStep = quantizeDelta(this.smoothPitchStep, remainingPitch, gcd, false);
/*     */     
/* 120 */     this.smoothYaw = class_3532.method_15393(this.smoothYaw + quantizedYawStep);
/* 121 */     this.smoothPitch = class_3532.method_15363(this.smoothPitch + quantizedPitchStep, -89.0F, 89.0F);
/*     */     
/* 123 */     RotationStorage.update(new Rotation(this.smoothYaw, this.smoothPitch), 360.0F, 360.0F, 45.0F, 45.0F, 0, 1, Aura.clientLook
/*     */ 
/*     */         
/* 126 */         .isState());
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean shouldFocus() {
/* 131 */     float cooldown = mc.field_1724.method_7261(1.5F);
/*     */     
/* 133 */     boolean fallingForCrit = (!mc.field_1724.method_24828() && (mc.field_1724.method_18798()).field_1351 < 0.0D && mc.field_1724.field_6017 > 0.0F);
/*     */     
/* 135 */     return (cooldown >= 0.88F || fallingForCrit);
/*     */   }
/*     */   
/*     */   private void ensureDatasetLoaded() {
/* 139 */     long now = System.currentTimeMillis();
/* 140 */     if (!shouldReload(now)) {
/*     */       return;
/*     */     }
/*     */     
/* 144 */     this.lastLoadAttempt = now;
/* 145 */     long modified = readLastModified();
/* 146 */     if (this.datasetReady && modified == this.lastModified) {
/*     */       return;
/*     */     }
/*     */     
/* 150 */     this.frames.clear();
/* 151 */     this.datasetReady = false;
/*     */     
/* 153 */     if (!Files.exists(DATASET_PATH, new java.nio.file.LinkOption[0])) {
/* 154 */       this.lastModified = Long.MIN_VALUE;
/*     */       return;
/*     */     } 
/*     */     
/* 158 */     try { Reader reader = Files.newBufferedReader(DATASET_PATH); 
/* 159 */       try { JsonArray array = JsonParser.parseReader(reader).getAsJsonArray();
/* 160 */         for (JsonElement element : array) {
/* 161 */           if (!element.isJsonObject()) {
/*     */             continue;
/*     */           }
/*     */           
/* 165 */           DatasetFrame frame = parseFrame(element.getAsJsonObject());
/* 166 */           if (frame != null) {
/* 167 */             this.frames.add(frame);
/*     */           }
/*     */         } 
/*     */         
/* 171 */         this.datasetReady = !this.frames.isEmpty();
/* 172 */         this.lastModified = modified;
/* 173 */         reset();
/* 174 */         if (reader != null) reader.close();  } catch (Throwable throwable) { if (reader != null) try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException|IllegalStateException ignored)
/* 175 */     { this.datasetReady = false;
/* 176 */       this.lastModified = Long.MIN_VALUE;
/* 177 */       reset(); }
/*     */   
/*     */   }
/*     */   
/*     */   private boolean shouldReload(long now) {
/* 182 */     if (!this.datasetReady || this.frames.isEmpty()) {
/* 183 */       return (now - this.lastLoadAttempt >= 1500L);
/*     */     }
/* 185 */     return (now - this.lastLoadAttempt >= 3000L);
/*     */   }
/*     */   
/*     */   private long readLastModified() {
/*     */     try {
/* 190 */       return Files.exists(DATASET_PATH, new java.nio.file.LinkOption[0]) ? Files.getLastModifiedTime(DATASET_PATH, new java.nio.file.LinkOption[0]).toMillis() : Long.MIN_VALUE;
/* 191 */     } catch (IOException ignored) {
/* 192 */       return Long.MIN_VALUE;
/*     */     } 
/*     */   }
/*     */   
/*     */   private DatasetFrame parseFrame(JsonObject object) {
/* 197 */     float fromYaw = getFloat(object, "fromYaw");
/* 198 */     float toYaw = getFloat(object, "toYaw");
/* 199 */     float fromPitch = getFloat(object, "fromPitch");
/* 200 */     float toPitch = getFloat(object, "toPitch");
/*     */     
/* 202 */     float signedYaw = class_3532.method_15393(toYaw - fromYaw);
/* 203 */     float signedPitch = toPitch - fromPitch;
/* 204 */     float absYaw = Math.abs(signedYaw);
/* 205 */     float absPitch = Math.abs(signedPitch);
/*     */     
/* 207 */     float deltaYaw = Math.max(getFloat(object, "deltaYaw"), absYaw);
/* 208 */     float deltaPitch = Math.max(getFloat(object, "deltaPitch"), absPitch);
/* 209 */     if (deltaYaw <= 0.0F && deltaPitch <= 0.0F) {
/* 210 */       return null;
/*     */     }
/*     */     
/* 213 */     DatasetFrame frame = new DatasetFrame();
/* 214 */     frame.deltaYaw = deltaYaw;
/* 215 */     frame.deltaPitch = deltaPitch;
/* 216 */     frame
/*     */       
/* 218 */       .signedYaw = (signedYaw != 0.0F) ? signedYaw : (Math.signum(getFloat(object, "jitterYawDir")) * deltaYaw);
/* 219 */     frame
/*     */       
/* 221 */       .signedPitch = (signedPitch != 0.0F) ? signedPitch : (Math.signum(getFloat(object, "jitterPitchDir")) * deltaPitch);
/* 222 */     frame.rotationSpeed = Math.max(getFloat(object, "rotationSpeed"), 0.0F);
/* 223 */     frame.jitterScore = Math.max(getFloat(object, "jitterScore"), 0.0F);
/* 224 */     frame.jitterYawSpeed = Math.max(getFloat(object, "jitterYawSpeed"), 0.0F);
/* 225 */     frame.jitterPitchSpeed = Math.max(getFloat(object, "jitterPitchSpeed"), 0.0F);
/* 226 */     frame.isJittering = getBoolean(object, "isJittering");
/* 227 */     frame.attacking = getBoolean(object, "attacking");
/* 228 */     frame.combatFrame = getBoolean(object, "isCombatFrame");
/* 229 */     frame.instantSnap = getBoolean(object, "isInstantSnap");
/* 230 */     frame.timeDeltaMs = Math.max(1L, object.has("timeDeltaMs") ? object.get("timeDeltaMs").getAsLong() : 50L);
/* 231 */     return frame;
/*     */   }
/*     */   
/*     */   private DatasetFrame pickFrame(float remainingYaw, float remainingPitch, boolean focus) {
/* 235 */     float pressure = Math.abs(remainingYaw) + Math.abs(remainingPitch) * 0.82F;
/* 236 */     int size = this.frames.size();
/* 237 */     int window = Math.min(size, focus ? 78 : 56);
/* 238 */     int bestIndex = this.playbackIndex % size;
/* 239 */     float bestScore = Float.MAX_VALUE;
/*     */     
/* 241 */     for (int i = 0; i < window; i++) {
/* 242 */       int index = (this.playbackIndex + i) % size;
/* 243 */       DatasetFrame frame = this.frames.get(index);
/* 244 */       float framePressure = frame.deltaYaw + frame.deltaPitch * 0.82F;
/* 245 */       float score = Math.abs(framePressure - pressure);
/*     */       
/* 247 */       if (focus) {
/* 248 */         if (!frame.isCombatLike()) {
/* 249 */           score += 3.0F;
/*     */         }
/* 251 */         if (frame.instantSnap) {
/* 252 */           score -= 0.5F;
/*     */         }
/* 254 */       } else if (frame.isCombatLike()) {
/* 255 */         score += 1.6F;
/*     */       } 
/*     */       
/* 258 */       if (pressure < 10.0F && frame.isJittering) {
/* 259 */         score -= Math.min(frame.jitterScore, 2.6F) * 0.2F;
/*     */       }
/*     */       
/* 262 */       score += i * 0.032F;
/* 263 */       if (score < bestScore) {
/* 264 */         bestScore = score;
/* 265 */         bestIndex = index;
/*     */       } 
/*     */     } 
/*     */     
/* 269 */     this.playbackIndex = (bestIndex + 1) % size;
/* 270 */     return this.frames.get(bestIndex);
/*     */   }
/*     */   
/*     */   private void updateSmoothProfile(DatasetFrame frame, float remainingYaw, float remainingPitch, boolean focus) {
/* 274 */     if (this.smoothProfileTicks > 0) {
/* 275 */       this.smoothProfileTicks--;
/*     */       
/*     */       return;
/*     */     } 
/* 279 */     ThreadLocalRandom random = ThreadLocalRandom.current();
/* 280 */     float pressure = class_3532.method_15363((Math.abs(remainingYaw) + Math.abs(remainingPitch)) / 32.0F, 0.0F, 1.0F);
/* 281 */     float timePressure = class_3532.method_15363((float)frame.timeDeltaMs / 120.0F, 0.0F, 1.0F);
/*     */     
/* 283 */     float yawMin = focus ? 0.94F : 0.86F;
/* 284 */     float yawMax = focus ? 1.12F : 1.04F;
/* 285 */     float pitchMin = focus ? 0.92F : 0.84F;
/* 286 */     float pitchMax = focus ? 1.08F : 1.0F;
/*     */     
/* 288 */     this.yawSmoothFactor = random.nextFloat(yawMin, yawMax + pressure * 0.08F + timePressure * 0.04F);
/* 289 */     this.pitchSmoothFactor = random.nextFloat(pitchMin, pitchMax + pressure * 0.06F + timePressure * 0.03F);
/*     */     
/* 291 */     if (frame.isCombatLike()) {
/* 292 */       this.yawSmoothFactor *= 1.02F;
/* 293 */       this.pitchSmoothFactor *= 1.015F;
/*     */     } 
/*     */     
/* 296 */     this.smoothProfileTicks = random.nextInt(focus ? 2 : 3, focus ? 6 : 8);
/*     */   }
/*     */   
/*     */   private float buildAxisStep(float remaining, DatasetFrame frame, boolean yawAxis, boolean focus) {
/* 300 */     float desiredAbs = Math.abs(remaining);
/* 301 */     if (desiredAbs <= 1.0E-4F) {
/* 302 */       return 0.0F;
/*     */     }
/*     */     
/* 305 */     float template = yawAxis ? frame.deltaYaw : frame.deltaPitch;
/* 306 */     float speedBoost = 0.3F + class_3532.method_15363(frame.rotationSpeed * (yawAxis ? 3.4F : 2.8F), 0.0F, yawAxis ? 0.2F : 0.16F);
/* 307 */     float pressureBoost = class_3532.method_15363(desiredAbs / (yawAxis ? 105.0F : 82.0F), 0.09F, yawAxis ? 0.52F : 0.46F);
/* 308 */     float step = Math.max(template * Math.max(speedBoost, pressureBoost), yawAxis ? 0.03F : 0.024F);
/*     */     
/* 310 */     if (frame.instantSnap) {
/* 311 */       step = Math.max(step, desiredAbs * (yawAxis ? 0.085F : 0.065F));
/*     */     }
/*     */     
/* 314 */     if (frame.attacking || frame.combatFrame) {
/* 315 */       step *= yawAxis ? 1.02F : 1.015F;
/*     */     }
/*     */     
/* 318 */     step *= yawAxis ? 0.5F : 0.46F;
/*     */     
/* 320 */     float finishThreshold = yawAxis ? 6.0F : 4.0F;
/* 321 */     if (desiredAbs < finishThreshold) {
/* 322 */       float finishBoost = 1.0F + (finishThreshold - desiredAbs) / finishThreshold * 0.18F;
/* 323 */       step *= finishBoost;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 328 */     float maxStep = yawAxis ? Math.max(0.48F, desiredAbs * (frame.instantSnap ? 0.11F : 0.065F)) : Math.max(0.34F, desiredAbs * (frame.instantSnap ? 0.09F : 0.058F));
/* 329 */     step = Math.min(step, maxStep);
/* 330 */     step = Math.min(step, desiredAbs);
/* 331 */     return Math.signum(remaining) * step;
/*     */   }
/*     */   
/*     */   private float buildJitter(DatasetFrame frame, float remaining, boolean yawAxis, float gcd) {
/* 335 */     float desiredAbs = Math.abs(remaining);
/* 336 */     if (desiredAbs > (yawAxis ? 6.5F : 4.8F)) {
/* 337 */       return 0.0F;
/*     */     }
/*     */     
/* 340 */     float speed = yawAxis ? frame.jitterYawSpeed : frame.jitterPitchSpeed;
/* 341 */     float base = gcd * class_3532.method_15363(frame.jitterScore * 0.01F, 0.0F, yawAxis ? 0.15F : 0.11F);
/* 342 */     base += gcd * class_3532.method_15363(speed * (yawAxis ? 1.3F : 1.0F), 0.0F, yawAxis ? 0.1F : 0.07F);
/*     */     
/* 344 */     if (frame.isJittering) {
/* 345 */       base *= 1.05F;
/*     */     }
/*     */     
/* 348 */     if (base <= 0.0F) {
/* 349 */       return 0.0F;
/*     */     }
/*     */     
/* 352 */     float direction = ThreadLocalRandom.current().nextBoolean() ? 1.0F : -1.0F;
/* 353 */     float jitter = base * ThreadLocalRandom.current().nextFloat(0.3F, 0.95F) * direction;
/* 354 */     if (Math.abs(jitter) > desiredAbs && Math.signum(jitter) == Math.signum(remaining)) {
/* 355 */       jitter = remaining;
/*     */     }
/*     */     
/* 358 */     return jitter;
/*     */   }
/*     */   
/*     */   private void syncRotationState(class_1309 target, float currentYaw, float currentPitch) {
/* 362 */     if (!this.hasRotationState || this.trackedRotationTarget != target) {
/* 363 */       this.trackedRotationTarget = target;
/* 364 */       this.smoothYaw = currentYaw;
/* 365 */       this.smoothPitch = currentPitch;
/* 366 */       this.smoothYawStep = 0.0F;
/* 367 */       this.smoothPitchStep = 0.0F;
/* 368 */       this.yawSmoothFactor = 1.0F;
/* 369 */       this.pitchSmoothFactor = 1.0F;
/* 370 */       this.smoothProfileTicks = 0;
/* 371 */       this.hasRotationState = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   private float smoothAxisStep(float currentStep, float desiredStep, float remaining, boolean yawAxis, boolean focus) {
/* 376 */     float desiredAbs = Math.abs(remaining);
/* 377 */     if (desiredAbs <= 1.0E-4F) {
/* 378 */       return 0.0F;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 383 */     float baseAlpha = yawAxis ? (focus ? 0.092F : 0.06F) : (focus ? 0.082F : 0.055F);
/* 384 */     float alpha = baseAlpha * (yawAxis ? this.yawSmoothFactor : this.pitchSmoothFactor);
/* 385 */     float smoothed = currentStep + (desiredStep - currentStep) * class_3532.method_15363(alpha, 0.025F, 0.16F);
/*     */     
/* 387 */     float minCap = yawAxis ? 0.13F : 0.1F;
/*     */ 
/*     */     
/* 390 */     float capScale = yawAxis ? (focus ? 0.056F : 0.036F) : (focus ? 0.046F : 0.032F);
/* 391 */     float randomFactor = yawAxis ? this.yawSmoothFactor : this.pitchSmoothFactor;
/* 392 */     float maxCap = minCap + desiredAbs * capScale * class_3532.method_15363(randomFactor, 0.88F, 1.18F);
/*     */     
/* 394 */     float finishThreshold = yawAxis ? 5.5F : 3.8F;
/* 395 */     if (desiredAbs < finishThreshold) {
/* 396 */       maxCap *= 1.12F;
/*     */     }
/*     */     
/* 399 */     smoothed = class_3532.method_15363(smoothed, -maxCap, maxCap);
/* 400 */     if (Math.abs(remaining) < Math.abs(smoothed) && Math.signum(remaining) == Math.signum(smoothed)) {
/* 401 */       smoothed = remaining;
/*     */     }
/*     */     
/* 404 */     return smoothed;
/*     */   }
/*     */   
/*     */   private float quantizeDelta(float wantedDelta, float remaining, float gcd, boolean yawAxis) {
/* 408 */     float limited = wantedDelta;
/* 409 */     if (Math.abs(remaining) < Math.abs(limited) && Math.signum(remaining) == Math.signum(limited)) {
/* 410 */       limited = remaining;
/*     */     }
/*     */     
/* 413 */     float quantized = Math.round(limited / gcd) * gcd;
/* 414 */     if (quantized == 0.0F && Math.abs(limited) >= gcd * 0.2F) {
/* 415 */       quantized = Math.signum(limited) * gcd;
/*     */     }
/*     */     
/* 418 */     if (Math.abs(remaining) < Math.abs(quantized) && Math.signum(remaining) == Math.signum(quantized)) {
/* 419 */       quantized = remaining;
/*     */     }
/*     */     
/* 422 */     if (!yawAxis) {
/* 423 */       quantized = class_3532.method_15363(quantized, -89.0F, 89.0F);
/*     */     }
/* 425 */     return quantized;
/*     */   }
/*     */ 
/*     */   
/*     */   private class_243 selectAimPoint(class_1309 target, boolean focus) {
/* 430 */     if (this.trackedTarget != target || this.currentAimPoint == null || this.targetAimPoint == null) {
/* 431 */       this.trackedTarget = target;
/* 432 */       this.targetAimPoint = createAimPoint(target, focus);
/* 433 */       this.currentAimPoint = this.targetAimPoint;
/* 434 */       this.aimPointTicks = 0;
/* 435 */       this.aimPointRefreshTicks = randomRefreshTicks(focus);
/* 436 */       return this.currentAimPoint;
/*     */     } 
/*     */     
/* 439 */     if (this.aimPointTicks++ >= this.aimPointRefreshTicks) {
/* 440 */       this.targetAimPoint = createAimPoint(target, focus);
/* 441 */       this.aimPointTicks = 0;
/* 442 */       this.aimPointRefreshTicks = randomRefreshTicks(focus);
/*     */     } 
/*     */     
/* 445 */     float lerp = focus ? 0.06F : 0.04F;
/* 446 */     this
/*     */ 
/*     */       
/* 449 */       .currentAimPoint = new class_243(class_3532.method_16436(lerp, this.currentAimPoint.field_1352, this.targetAimPoint.field_1352), class_3532.method_16436(lerp, this.currentAimPoint.field_1351, this.targetAimPoint.field_1351), class_3532.method_16436(lerp, this.currentAimPoint.field_1350, this.targetAimPoint.field_1350));
/*     */     
/* 451 */     return this.currentAimPoint;
/*     */   }
/*     */   
/*     */   private int randomRefreshTicks(boolean focus) {
/* 455 */     return ThreadLocalRandom.current().nextInt(focus ? 7 : 10, focus ? 13 : 18);
/*     */   }
/*     */   
/*     */   private class_243 createAimPoint(class_1309 target, boolean focus) {
/* 459 */     class_238 box = getPredictedBox(target);
/* 460 */     ThreadLocalRandom random = ThreadLocalRandom.current();
/* 461 */     double x = class_3532.method_16436(random.nextDouble(0.45D, 0.55D), box.field_1323, box.field_1320);
/* 462 */     double y = class_3532.method_16436(random.nextDouble(focus ? 0.53D : 0.49D, focus ? 0.7D : 0.76D), box.field_1322, box.field_1325);
/* 463 */     double z = class_3532.method_16436(random.nextDouble(0.45D, 0.55D), box.field_1321, box.field_1324);
/* 464 */     return new class_243(x, y, z);
/*     */   }
/*     */   
/*     */   private float getFloat(JsonObject object, String key) {
/* 468 */     return object.has(key) ? object.get(key).getAsFloat() : 0.0F;
/*     */   }
/*     */   
/*     */   private boolean getBoolean(JsonObject object, String key) {
/* 472 */     return (object.has(key) && object.get(key).getAsBoolean());
/*     */   }
/*     */   
/*     */   private static class DatasetFrame {
/*     */     float deltaYaw;
/*     */     float deltaPitch;
/*     */     float signedYaw;
/*     */     float signedPitch;
/*     */     float rotationSpeed;
/*     */     float jitterScore;
/*     */     float jitterYawSpeed;
/*     */     float jitterPitchSpeed;
/*     */     long timeDeltaMs;
/*     */     boolean isJittering;
/*     */     boolean attacking;
/*     */     boolean combatFrame;
/*     */     boolean instantSnap;
/*     */     
/*     */     boolean isCombatLike() {
/* 491 */       return (this.attacking || this.combatFrame || this.instantSnap);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\combat\components\rotations\TestRotation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */