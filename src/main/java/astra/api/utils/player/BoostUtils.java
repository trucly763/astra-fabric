/*     */ package shame.astra.api.utils.player;
/*     */ 
/*     */ import net.minecraft.class_1294;
/*     */ import net.minecraft.class_1309;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_310;
/*     */ import net.minecraft.class_3532;
/*     */ 
/*     */ public final class BoostUtils
/*     */ {
/*  11 */   private static final class_310 mc = class_310.method_1551();
/*     */   
/*     */   private static final float BASE_HORIZONTAL = 1.61F;
/*     */   
/*     */   private static final float BASE_VERTICAL = 1.5F;
/*  16 */   private static final float[] YAW_TABLE = new float[] { 1.61F, 1.61F, 1.61F, 1.61F, 1.61F, 1.61F, 1.62F, 1.62F, 1.62F, 1.63F, 1.63F, 1.64F, 1.65F, 1.65F, 1.66F, 1.67F, 1.68F, 1.69F, 1.7F, 1.71F, 1.72F, 1.73F, 1.73F, 1.75F, 1.76F, 1.78F, 1.79F, 1.81F, 1.83F, 1.85F, 1.87F, 1.89F, 1.91F, 1.93F, 1.95F, 1.98F, 2.01F, 2.03F, 2.06F, 2.09F, 2.12F, 2.16F, 2.19F, 2.23F, 2.27F, 2.31F, 2.35F, 2.31F, 2.27F, 2.23F, 2.19F, 2.16F, 2.12F, 2.09F, 2.06F, 2.03F, 2.01F, 1.98F, 1.95F, 1.93F, 1.89F, 1.87F, 1.85F, 1.83F, 1.81F, 1.79F, 1.78F, 1.76F, 1.75F, 1.73F, 1.72F, 1.71F, 1.7F, 1.69F, 1.68F, 1.67F, 1.66F, 1.65F, 1.64F, 1.63F, 1.63F, 1.63F, 1.62F, 1.62F, 1.62F, 1.61F, 1.61F, 1.61F, 1.61F, 1.61F, 1.61F };
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
/*  29 */   private static final float[] PITCH_TABLE = new float[] { 1.61F, 1.61F, 1.61F, 1.62F, 1.62F, 1.62F, 1.63F, 1.63F, 1.64F, 1.65F, 1.65F, 1.66F, 1.67F, 1.68F, 1.69F, 1.7F, 1.71F, 1.72F, 1.73F, 1.73F, 1.75F, 1.76F, 1.78F, 1.79F, 1.81F, 1.83F, 1.85F, 1.87F, 1.89F, 1.91F, 1.93F, 1.95F, 1.98F, 2.01F, 2.03F, 2.06F, 2.09F, 2.12F, 2.16F, 2.19F, 2.23F, 2.24F, 2.21F, 2.21F, 2.21F, 2.23F, 2.23F, 2.19F, 2.16F, 2.12F, 2.09F, 2.06F, 2.03F, 2.01F, 1.98F, 1.95F, 1.93F, 1.89F, 1.87F, 1.85F, 1.83F, 1.81F, 1.79F, 1.78F, 1.76F, 1.75F, 1.73F, 1.72F, 1.71F, 1.7F, 1.69F, 1.68F, 1.67F, 1.66F, 1.65F, 1.64F, 1.63F, 1.63F, 1.63F, 1.62F, 1.62F, 1.62F, 1.61F, 1.61F, 1.61F, 1.61F, 1.61F, 1.61F, 1.61F, 1.61F, 1.61F };
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
/*     */   public static class_243 getBoost(class_1309 entity) {
/*  43 */     float speed = getRageSpeed(entity);
/*     */     
/*  45 */     class_243 vec3d = entity.method_5720();
/*  46 */     class_243 oldVelocity = class_243.method_1030(entity.method_36455(), entity.method_36454()).method_1021(speed);
/*     */     
/*  48 */     float f = entity.method_36455() * 0.017453292F;
/*  49 */     double d = Math.sqrt(vec3d.field_1352 * vec3d.field_1352 + vec3d.field_1350 * vec3d.field_1350);
/*  50 */     double e = oldVelocity.method_37267();
/*  51 */     boolean bl = ((entity.method_18798()).field_1351 <= 0.0D);
/*     */     
/*  53 */     double g = (bl && entity.method_6059(class_1294.field_5906)) ? Math.min(entity.method_56989(), 0.01D) : entity.method_56989();
/*  54 */     double h = class_3532.method_33723(Math.cos(f));
/*     */     
/*  56 */     oldVelocity = oldVelocity.method_1031(0.0D, g * (-1.0D + h * 0.75D), 0.0D);
/*     */ 
/*     */     
/*  59 */     if (oldVelocity.field_1351 < 0.0D && d > 0.0D) {
/*  60 */       double i = oldVelocity.field_1351 * -0.1D * h;
/*  61 */       oldVelocity = oldVelocity.method_1031(vec3d.field_1352 * i / d, i, vec3d.field_1350 * i / d);
/*     */     } 
/*     */     
/*  64 */     if (f < 0.0F && d > 0.0D) {
/*  65 */       double i = e * -class_3532.method_15374(f) * 0.04D;
/*  66 */       oldVelocity = oldVelocity.method_1031(-vec3d.field_1352 * i / d, i * 3.2D, -vec3d.field_1350 * i / d);
/*     */     } 
/*     */     
/*  69 */     if (d > 0.0D) {
/*  70 */       oldVelocity = oldVelocity.method_1031((vec3d.field_1352 / d * e - oldVelocity.field_1352) * 0.1D, 0.0D, (vec3d.field_1350 / d * e - oldVelocity.field_1350) * 0.1D);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  77 */     double length = oldVelocity.method_1033();
/*  78 */     return (new class_243(length, length, length)).method_18805(0.99D, 0.98D, 0.99D);
/*     */   }
/*     */   
/*     */   private static float getRageSpeed(class_1309 entity) {
/*  82 */     float yawAbs = Math.abs(class_3532.method_15393(entity.method_36454()));
/*  83 */     float yawFolded = foldYaw(yawAbs);
/*  84 */     float pitchAbs = Math.abs(clampPitch(entity.method_36455()));
/*     */     
/*  86 */     if (pitchAbs >= 70.0F && pitchAbs <= 90.0F) {
/*  87 */       return 1.615F;
/*     */     }
/*     */     
/*  90 */     float yawSpeed = YAW_TABLE[Math.min((int)Math.ceil(yawFolded), 90)];
/*  91 */     int pitchIndex = Math.min((int)Math.ceil(pitchAbs), PITCH_TABLE.length - 1);
/*  92 */     float pitchSpeed = PITCH_TABLE[pitchIndex];
/*     */     
/*  94 */     float speed = (pitchAbs >= 75.0F) ? pitchSpeed : Math.max(yawSpeed, pitchSpeed);
/*  95 */     return Math.max(speed, (pitchAbs >= 75.0F) ? 1.5F : 1.61F);
/*     */   }
/*     */   
/*     */   private static float foldYaw(float yawAbs) {
/*  99 */     float folded180 = (yawAbs > 180.0F) ? (360.0F - yawAbs) : yawAbs;
/* 100 */     return (folded180 > 90.0F) ? (180.0F - folded180) : folded180;
/*     */   }
/*     */   
/*     */   private static float clampPitch(float pitch) {
/* 104 */     return Math.max(-90.0F, Math.min(90.0F, pitch));
/*     */   }
/*     */ 
/*     */   
/*     */   public static class_243 getBoostAntiTarget(class_1309 entity, float speedSetting) {
/* 109 */     float yaw = Math.abs((entity.method_36454() - 360.0F) % 360.0F);
/* 110 */     float pitch = entity.method_36455();
/* 111 */     float absPitch = Math.abs(pitch);
/*     */     
/* 113 */     float baseSpeed = speedSetting;
/* 114 */     float pitchBonus = 0.0F;
/*     */     
/* 116 */     if (absPitch >= 30.0F && absPitch <= 50.0F) { pitchBonus = 0.15F; }
/* 117 */     else if (absPitch >= 25.0F && absPitch <= 55.0F) { pitchBonus = 0.1F; }
/* 118 */     else if (absPitch >= 20.0F && absPitch <= 60.0F) { pitchBonus = 0.05F; }
/*     */     
/* 120 */     float speed = baseSpeed + pitchBonus;
/*     */     
/* 122 */     float[] centers = { 45.0F, 135.0F, 225.0F, 315.0F };
/* 123 */     float minDiff = 9999.0F;
/* 124 */     for (float c : centers) {
/* 125 */       float diff = Math.abs(yaw - c);
/* 126 */       if (diff < minDiff) minDiff = diff;
/*     */     
/*     */     } 
/* 129 */     if (minDiff < 15.0F) { speed += 0.1F; }
/* 130 */     else if (minDiff < 25.0F) { speed += 0.05F; }
/*     */     
/* 132 */     speed = Math.min(speed, 2.8F);
/* 133 */     return new class_243(speed, speed, speed);
/*     */   }
/*     */   
/*     */   public static class_243 getBoostAntiTargetFast(class_1309 entity) {
/* 137 */     float yaw = Math.abs((entity.method_36454() - 360.0F) % 360.0F);
/* 138 */     float pitch = entity.method_36455();
/* 139 */     float absPitch = Math.abs(pitch);
/*     */     
/* 141 */     float speedXZ = 2.5F;
/* 142 */     float speedY = 2.3F;
/*     */     
/* 144 */     if (absPitch >= 35.0F && absPitch <= 50.0F) {
/* 145 */       speedXZ = 2.7F;
/* 146 */       speedY = 2.5F;
/* 147 */     } else if (absPitch >= 30.0F && absPitch <= 55.0F) {
/* 148 */       speedXZ = 2.6F;
/* 149 */       speedY = 2.4F;
/*     */     } 
/*     */     
/* 152 */     float[] centers = { 45.0F, 135.0F, 225.0F, 315.0F };
/* 153 */     float minDiff = 9999.0F;
/* 154 */     for (float c : centers) {
/* 155 */       float diff = Math.abs(yaw - c);
/* 156 */       if (diff < minDiff) minDiff = diff;
/*     */     
/*     */     } 
/* 159 */     if (minDiff < 20.0F) speedXZ += 0.15F;
/*     */     
/* 161 */     return new class_243(speedXZ, speedY, speedXZ);
/*     */   }
/*     */   
/*     */   public static class_243 getBoostAntiTargetWithAura(class_1309 entity, float auraRotatePitch, float auraRotateYaw, float speedSetting) {
/* 165 */     float absPitch = Math.abs(auraRotatePitch);
/* 166 */     float speedXZ = speedSetting;
/* 167 */     float speedY = speedSetting;
/*     */     
/* 169 */     if (absPitch >= 38.0F && absPitch <= 52.0F) {
/* 170 */       speedXZ = Math.min(speedSetting + 0.2F, 2.7F);
/* 171 */       speedY = Math.min(speedSetting + 0.15F, 2.5F);
/* 172 */     } else if (absPitch >= 30.0F && absPitch <= 60.0F) {
/* 173 */       speedXZ = Math.min(speedSetting + 0.1F, 2.6F);
/* 174 */       speedY = Math.min(speedSetting + 0.1F, 2.4F);
/* 175 */     } else if (absPitch >= 25.0F && absPitch <= 65.0F) {
/* 176 */       speedY = speedSetting - 0.05F;
/*     */     } else {
/* 178 */       speedXZ = speedSetting - 0.1F;
/* 179 */       speedY = speedSetting - 0.15F;
/*     */     } 
/*     */     
/* 182 */     return new class_243(speedXZ, speedY, speedXZ);
/*     */   }
/*     */   
/*     */   public static class_243 getBoostslime(class_1309 entity) {
/* 186 */     return getBoostCustom(entity, 42.0F);
/*     */   }
/*     */   
/*     */   public static class_243 getBoostbravo(class_1309 entity) {
/* 190 */     return getBoostCustom(entity, 39.0F);
/*     */   }
/*     */   
/*     */   public static class_243 getBoostrw(class_1309 entity) {
/* 194 */     return getBoostCustom(entity, 33.2F);
/*     */   }
/*     */   
/*     */   public static class_243 getBoostCustom(class_1309 entity, float targetBps) {
/* 198 */     float maxSpeed = targetBps / 20.0F;
/* 199 */     float yaw = Math.abs((entity.method_36454() - 360.0F) % 360.0F);
/* 200 */     float pitch = entity.method_36455();
/* 201 */     float minSpeed = Math.min(maxSpeed * 0.7F, 1.67F);
/*     */     
/* 203 */     float[] centers = { 45.0F, 135.0F, 225.0F, 315.0F };
/* 204 */     float minDiff = 9999.0F;
/* 205 */     for (float c : centers) {
/* 206 */       float diff = Math.abs(yaw - c);
/* 207 */       if (diff < minDiff) minDiff = diff;
/*     */     
/*     */     } 
/* 210 */     float yawFactor = 1.0F - minDiff / 45.0F;
/* 211 */     yawFactor = Math.max(0.0F, Math.min(1.0F, yawFactor));
/*     */     
/* 213 */     float pitchFactor = getPitchFactor(pitch);
/* 214 */     float combinedFactor = yawFactor * pitchFactor;
/* 215 */     float speed = minSpeed + (maxSpeed - minSpeed) * combinedFactor;
/*     */ 
/*     */     
/* 218 */     class_243 vec3d = entity.method_5720();
/* 219 */     class_243 oldVelocity = class_243.method_1030(pitch, entity.method_36454()).method_1021(speed);
/* 220 */     float f = pitch * 0.017453292F;
/* 221 */     double d = Math.sqrt(vec3d.field_1352 * vec3d.field_1352 + vec3d.field_1350 * vec3d.field_1350);
/* 222 */     double e = oldVelocity.method_37267();
/* 223 */     boolean bl = ((entity.method_18798()).field_1351 <= 0.0D);
/* 224 */     double g = (bl && entity.method_6059(class_1294.field_5906)) ? Math.min(entity.method_56989(), 0.01D) : entity.method_56989();
/* 225 */     double h = class_3532.method_33723(Math.cos(f));
/* 226 */     oldVelocity = oldVelocity.method_1031(0.0D, g * (-1.0D + h * 0.75D), 0.0D);
/*     */ 
/*     */     
/* 229 */     if (oldVelocity.field_1351 < 0.0D && d > 0.0D) {
/* 230 */       double i = oldVelocity.field_1351 * -0.1D * h;
/* 231 */       oldVelocity = oldVelocity.method_1031(vec3d.field_1352 * i / d, i, vec3d.field_1350 * i / d);
/*     */     } 
/* 233 */     if (f < 0.0F && d > 0.0D) {
/* 234 */       double i = e * -class_3532.method_15374(f) * 0.04D;
/* 235 */       oldVelocity = oldVelocity.method_1031(-vec3d.field_1352 * i / d, i * 3.2D, -vec3d.field_1350 * i / d);
/*     */     } 
/* 237 */     if (d > 0.0D) {
/* 238 */       oldVelocity = oldVelocity.method_1031((vec3d.field_1352 / d * e - oldVelocity.field_1352) * 0.1D, 0.0D, (vec3d.field_1350 / d * e - oldVelocity.field_1350) * 0.1D);
/*     */     }
/*     */     
/* 241 */     double length = oldVelocity.method_1033();
/* 242 */     return (new class_243(length, length, length)).method_18805(0.99D, 0.98D, 0.99D);
/*     */   }
/*     */   
/*     */   public static class_243 getBoostFixedBps(class_1309 entity, float targetBps) {
/* 246 */     float speed = targetBps / 20.0F;
/* 247 */     return (new class_243(speed, speed, speed)).method_18805(0.99D, 0.98D, 0.99D);
/*     */   }
/*     */   
/*     */   private static float getPitchFactor(float pitch) {
/* 251 */     float absPitch = Math.abs(pitch);
/* 252 */     if (absPitch <= 5.0F) return 1.0F; 
/* 253 */     if (absPitch <= 15.0F) return 0.95F; 
/* 254 */     if (absPitch <= 25.0F) return 0.85F; 
/* 255 */     if (absPitch <= 35.0F) return 0.75F; 
/* 256 */     if (absPitch <= 45.0F) return 0.65F; 
/* 257 */     if (absPitch <= 55.0F) return 0.55F; 
/* 258 */     if (absPitch <= 65.0F) return 0.45F; 
/* 259 */     if (absPitch <= 75.0F) return 0.35F; 
/* 260 */     return 0.25F;
/*     */   }
/*     */   
/*     */   private BoostUtils() {
/* 264 */     throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\player\BoostUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */