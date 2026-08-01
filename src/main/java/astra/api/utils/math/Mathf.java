/*     */ package shame.astra.api.utils.math;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.RoundingMode;
/*     */ import java.util.concurrent.ThreadLocalRandom;
/*     */ import lombok.Generated;
/*     */ import net.minecraft.class_3532;
/*     */ 
/*     */ public final class Mathf {
/*     */   @Generated
/*     */   private Mathf() {
/*  11 */     throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
/*     */   }
/*     */   public static float clamp01(float x) {
/*  14 */     return (float)clamp(0.0D, 1.0D, x);
/*     */   }
/*     */   
/*     */   public static double getRandom(double min, double max) {
/*  18 */     if (min == max)
/*  19 */       return min; 
/*  20 */     if (min > max) {
/*  21 */       double d = min;
/*  22 */       min = max;
/*  23 */       max = d;
/*     */     } 
/*  25 */     return ThreadLocalRandom.current().nextDouble() * (max - min) + min;
/*     */   }
/*     */   
/*     */   public static float calculateDelta(float a, float b) {
/*  29 */     return a - b;
/*     */   }
/*     */   
/*     */   public static double round(double target, int decimal) {
/*  33 */     double p = Math.pow(10.0D, decimal);
/*  34 */     return Math.round(target * p) / p;
/*     */   }
/*     */   
/*     */   public static Number round(double num, double increment) {
/*  38 */     if (increment <= 0.0D) {
/*  39 */       throw new IllegalArgumentException("Increment must be greater than zero");
/*     */     }
/*  41 */     double roundedValue = Math.round(num / increment) * increment;
/*  42 */     BigDecimal bigDecimal = BigDecimal.valueOf(roundedValue);
/*  43 */     bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
/*  44 */     return Double.valueOf(bigDecimal.doubleValue());
/*     */   }
/*     */   
/*     */   public static String formatTime(long millis) {
/*  48 */     long hours = millis / 3600000L;
/*  49 */     long minutes = millis % 3600000L / 60000L;
/*  50 */     long seconds = millis % 360000L % 60000L / 1000L;
/*  51 */     return String.format("%02d:%02d:%02d", new Object[] { Long.valueOf(hours), Long.valueOf(minutes), Long.valueOf(seconds) });
/*     */   }
/*     */   
/*     */   public static float slerp(float start, float end, float t) {
/*  55 */     t = Math.max(0.0F, Math.min(1.0F, t));
/*  56 */     float startRadians = (float)Math.toRadians(start);
/*  57 */     float endRadians = (float)Math.toRadians(end);
/*     */ 
/*     */     
/*  60 */     float dotProduct = (float)Math.cos(startRadians) * (float)Math.cos(endRadians) + (float)Math.sin(startRadians) * (float)Math.sin(endRadians);
/*     */     
/*  62 */     float angle = (float)Math.acos(dotProduct);
/*     */     
/*  64 */     if (Math.abs(angle) < 0.001F) {
/*  65 */       return start;
/*     */     }
/*     */     
/*  68 */     float factorStart = (float)(Math.sin(((1.0F - t) * angle)) / Math.sin(angle));
/*  69 */     float factorEnd = (float)(Math.sin((t * angle)) / Math.sin(angle));
/*     */     
/*  71 */     float interpolatedValue = start * factorStart + end * factorEnd;
/*  72 */     return (float)class_3532.method_15350(class_3532.method_15338(Math.toDegrees(interpolatedValue)), start, end);
/*     */   }
/*     */   
/*     */   public static double round(double value, int scale, double inc) {
/*  76 */     double halfOfInc = inc / 2.0D;
/*  77 */     double floored = Math.floor(value / inc) * inc;
/*     */     
/*  79 */     if (value >= floored + halfOfInc) {
/*  80 */       return (new BigDecimal(Math.ceil(value / inc) * inc))
/*  81 */         .setScale(scale, RoundingMode.HALF_UP)
/*  82 */         .doubleValue();
/*     */     }
/*  84 */     return (new BigDecimal(floored))
/*  85 */       .setScale(scale, RoundingMode.HALF_UP)
/*  86 */       .doubleValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public static double step(double value, double steps) {
/*  91 */     double a = Math.round(value / steps) * steps;
/*  92 */     a *= 1000.0D;
/*  93 */     a = (int)a;
/*  94 */     a /= 1000.0D;
/*  95 */     return a;
/*     */   }
/*     */   
/*     */   public static double getDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
/*  99 */     double deltaX = x2 - x1;
/* 100 */     double deltaY = y2 - y1;
/* 101 */     double deltaZ = z2 - z1;
/* 102 */     return class_3532.method_15355((float)(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ));
/*     */   }
/*     */   
/*     */   public static double clamp(double min, double max, double n) {
/* 106 */     return Math.max(min, Math.min(max, n));
/*     */   }
/*     */   public static int clamp(int min, int max, int value) {
/* 109 */     return Math.max(min, Math.min(max, value));
/*     */   }
/*     */   
/*     */   public static float normalize(float value, float min, float max) {
/* 113 */     return (value - min) / (max - min);
/*     */   }
/*     */   
/*     */   public static double interporate(double p_219803_0_, double p_219803_2_, double p_219803_4_) {
/* 117 */     return p_219803_2_ + p_219803_0_ * (p_219803_4_ - p_219803_2_);
/*     */   }
/*     */   
/*     */   public static float lerp(float min, float max, float delta) {
/* 121 */     return min + (max - min) * delta;
/*     */   }
/*     */   
/*     */   public static float easeOutExpo(float x) {
/* 125 */     return (x == 1.0F) ? 1.0F : (float)(1.0D - Math.pow(2.0D, (-10.0F * x)));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\math\Mathf.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */