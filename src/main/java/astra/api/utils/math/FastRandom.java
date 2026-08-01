/*     */ package shame.astra.api.utils.math;
/*     */ 
/*     */ import java.util.Random;
/*     */ import java.util.concurrent.ThreadLocalRandom;
/*     */ 
/*     */ public class FastRandom extends Random {
/*   7 */   private final ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
/*   8 */   private Random random = null;
/*     */   
/*     */   private volatile boolean seedSet;
/*     */   
/*     */   private void validateRandom() {
/*  13 */     if (this.random == null) {
/*  14 */       this.random = new Random(this.seed);
/*  15 */       this.seedUpdated = false;
/*  16 */     } else if (this.seedUpdated) {
/*  17 */       this.random.setSeed(this.seed);
/*  18 */       this.seedUpdated = false;
/*     */     } 
/*     */   }
/*     */   private volatile boolean seedUpdated; private volatile long seed;
/*     */   public static long mix(long left, long right) {
/*  23 */     left *= left * 6364136223846793005L + 1442695040888963407L;
/*  24 */     return left + right;
/*     */   }
/*     */   public void setSeed(long seed) {
/*  27 */     this.seed = seed;
/*  28 */     this.seedSet = true;
/*  29 */     this.seedUpdated = true;
/*     */   }
/*     */   
/*     */   public void nextBytes(byte[] bytes) {
/*  33 */     if (this.seedSet) {
/*  34 */       validateRandom();
/*  35 */       this.random.nextBytes(bytes);
/*     */     } else {
/*  37 */       this.threadLocalRandom.nextBytes(bytes);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int nextInt() {
/*  42 */     if (this.seedSet) {
/*  43 */       validateRandom();
/*  44 */       return this.random.nextInt();
/*     */     } 
/*  46 */     return this.threadLocalRandom.nextInt();
/*     */   }
/*     */ 
/*     */   
/*     */   public int nextInt(int bound) {
/*  51 */     if (this.seedSet) {
/*  52 */       validateRandom();
/*  53 */       return this.random.nextInt(bound);
/*     */     } 
/*  55 */     return this.threadLocalRandom.nextInt(bound);
/*     */   }
/*     */ 
/*     */   
/*     */   public long nextLong() {
/*  60 */     if (this.seedSet) {
/*  61 */       validateRandom();
/*  62 */       return this.random.nextLong();
/*     */     } 
/*  64 */     return this.threadLocalRandom.nextLong();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean nextBoolean() {
/*  69 */     if (this.seedSet) {
/*  70 */       validateRandom();
/*  71 */       return this.random.nextBoolean();
/*     */     } 
/*  73 */     return this.threadLocalRandom.nextBoolean();
/*     */   }
/*     */ 
/*     */   
/*     */   public float nextFloat() {
/*  78 */     if (this.seedSet) {
/*  79 */       validateRandom();
/*  80 */       return this.random.nextFloat();
/*     */     } 
/*  82 */     return this.threadLocalRandom.nextFloat();
/*     */   }
/*     */ 
/*     */   
/*     */   public double nextDouble() {
/*  87 */     if (this.seedSet) {
/*  88 */       validateRandom();
/*  89 */       return this.random.nextDouble();
/*     */     } 
/*  91 */     return this.threadLocalRandom.nextDouble();
/*     */   }
/*     */ 
/*     */   
/*     */   public double nextGaussian() {
/*  96 */     if (this.seedSet) {
/*  97 */       validateRandom();
/*  98 */       return this.random.nextGaussian();
/*     */     } 
/* 100 */     return this.threadLocalRandom.nextGaussian();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\math\FastRandom.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */