/*     */ package shame.astra.api.utils.math;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.RoundingMode;
/*     */ import java.security.SecureRandom;
/*     */ import java.util.concurrent.ThreadLocalRandom;
/*     */ import net.minecraft.class_1297;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_3532;
/*     */ import shame.astra.api.QClient;
/*     */ 
/*     */ public class MathUtils
/*     */   implements QClient {
/*  14 */   public static FastRandom fastRandomize = new FastRandom();
/*     */   
/*     */   public static double direction(float rotationYaw, double moveForward, double moveStrafing) {
/*  17 */     if (moveForward < 0.0D) rotationYaw += 180.0F;
/*     */     
/*  19 */     float forward = 1.0F;
/*     */     
/*  21 */     if (moveForward < 0.0D) { forward = -0.5F; }
/*  22 */     else if (moveForward > 0.0D) { forward = 0.5F; }
/*     */     
/*  24 */     if (moveStrafing > 0.0D) rotationYaw -= 90.0F * forward; 
/*  25 */     if (moveStrafing < 0.0D) rotationYaw += 90.0F * forward;
/*     */     
/*  27 */     return Math.toRadians(rotationYaw);
/*     */   }
/*     */   
/*     */   public static float randomNew(double min, double max) {
/*  31 */     if (min > max) return (float)(fastRandomize.nextFloat() * (min - max) + max); 
/*  32 */     return (float)(fastRandomize.nextFloat() * (max - min) + min);
/*     */   }
/*     */   
/*     */   public static double getBps(class_1297 player) {
/*  36 */     double dx = player.method_23317() - player.field_6014;
/*  37 */     double dy = player.method_23318() - player.field_6036;
/*  38 */     double dz = player.method_23321() - player.field_5969;
/*  39 */     double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
/*  40 */     return distance * 20.0D;
/*     */   }
/*     */   
/*     */   public static float calculateBPS() {
/*  44 */     if (mc.field_1724 == null) return 0.0F;
/*     */     
/*  46 */     double dx = mc.field_1724.method_23317() - mc.field_1724.field_6014;
/*  47 */     double dy = mc.field_1724.method_23318() - mc.field_1724.field_6036;
/*  48 */     double dz = mc.field_1724.method_23321() - mc.field_1724.field_5969;
/*  49 */     double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
/*     */     
/*  51 */     float timerSpeed = 1.0F;
/*  52 */     float bps = (float)(distance * timerSpeed * 20.0D);
/*  53 */     return Math.round(bps * 10.0F) / 10.0F;
/*     */   }
/*     */   
/*     */   public static double getTargetCompensatedSpeed(class_1297 target) {
/*  57 */     double baseSpeed = 1.5D;
/*     */     
/*  59 */     if (target == null) {
/*  60 */       return 1.5D;
/*     */     }
/*     */     
/*  63 */     double targetBps = calculateBPS();
/*     */     
/*  65 */     double speedFactor = 0.00342D;
/*  66 */     double bonusSpeed = targetBps * 0.00342D;
/*     */     
/*  68 */     return 1.5D + bonusSpeed;
/*     */   }
/*     */   
/*     */   public static float random(float min, float max) {
/*  72 */     SecureRandom secureRandom = new SecureRandom();
/*  73 */     double randA = secureRandom.nextDouble();
/*  74 */     double randB = secureRandom.nextDouble();
/*  75 */     double randC = secureRandom.nextGaussian() * 0.019999999552965164D;
/*  76 */     double smoothFactor = Math.pow(randA, 1.0D + secureRandom.nextDouble() * 0.7D);
/*  77 */     double mixFactor = (randB * 0.8D + 0.1D) * (Math.log1p(randA * 3.0D) * 0.5D + 0.5D);
/*  78 */     return (float)(min + (max - min) * smoothFactor * mixFactor + randC);
/*     */   }
/*     */   
/*     */   public static double randomBest(double min, double max) {
/*  82 */     return ThreadLocalRandom.current().nextDouble() * (max - min) + min;
/*     */   }
/*     */   
/*     */   public static boolean isHovered(double x, double y, double width, double height, double mouseX, double mouseY) {
/*  86 */     return (mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height);
/*     */   }
/*     */ 
/*     */   
/*     */   public static float interpolate(float prev, float to, float value) {
/*  91 */     return prev + (to - prev) * value;
/*     */   }
/*     */   public static class_243 interpolate(class_243 end, class_243 start, float multiple) {
/*  94 */     return new class_243(interpolate(end.method_10216(), start.method_10216(), multiple), interpolate(end.method_10214(), start.method_10214(), multiple), interpolate(end.method_10215(), start.method_10215(), multiple));
/*     */   }
/*     */   public static class_243 interpolate(class_1297 entity, float partialTicks) {
/*  97 */     double posX = class_3532.method_16436(partialTicks, entity.field_6014, entity.method_23317());
/*  98 */     double posY = class_3532.method_16436(partialTicks, entity.field_6036, entity.method_23318());
/*  99 */     double posZ = class_3532.method_16436(partialTicks, entity.field_5969, entity.method_23321());
/* 100 */     return new class_243(posX, posY, posZ);
/*     */   }
/*     */   
/*     */   public static double interpolate(double current, double old, double scale) {
/* 104 */     return old + (current - old) * scale;
/*     */   }
/*     */   
/*     */   public static float round(float number) {
/* 108 */     return Math.round(number * 10.0F) / 10.0F;
/*     */   }
/*     */   
/*     */   public static double round(double num, double increment) {
/* 112 */     double v = Math.round(num / increment) * increment;
/* 113 */     BigDecimal bd = new BigDecimal(v);
/* 114 */     bd = bd.setScale(2, RoundingMode.HALF_UP);
/* 115 */     return bd.doubleValue();
/*     */   }
/*     */   public static float lerp(float current, float old, float scale) {
/* 118 */     return current + (old - current) * clamp(scale, 0.0F, 1.0F);
/*     */   }
/*     */   public static float clamp(float value, float min, float max) {
/* 121 */     if (value <= min) {
/* 122 */       return min;
/*     */     }
/* 124 */     return Math.min(value, max);
/*     */   }
/*     */   public static double clamp(double min, double max, double n) {
/* 127 */     return Math.max(min, Math.min(max, n));
/*     */   }
/*     */   public static <T extends Number> T ler1p(T input, T target, double step) {
/* 130 */     double start = input.doubleValue();
/* 131 */     double end = target.doubleValue();
/* 132 */     double result = start + step * (end - start);
/*     */     
/* 134 */     if (input instanceof Integer)
/* 135 */       return (T)Integer.valueOf((int)Math.round(result)); 
/* 136 */     if (input instanceof Double)
/* 137 */       return (T)Double.valueOf(result); 
/* 138 */     if (input instanceof Float)
/* 139 */       return (T)Float.valueOf((float)result); 
/* 140 */     if (input instanceof Long)
/* 141 */       return (T)Long.valueOf(Math.round(result)); 
/* 142 */     if (input instanceof Short)
/* 143 */       return (T)Short.valueOf((short)(int)Math.round(result)); 
/* 144 */     if (input instanceof Byte) {
/* 145 */       return (T)Byte.valueOf((byte)(int)Math.round(result));
/*     */     }
/* 147 */     throw new IllegalArgumentException("Unsupported type: " + input.getClass().getSimpleName());
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\math\MathUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */