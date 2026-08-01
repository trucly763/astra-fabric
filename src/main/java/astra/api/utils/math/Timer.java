/*    */ package shame.astra.api.utils.math;
/*    */ 
/*    */ import lombok.Generated;
/*    */ import shame.astra.api.QClient;
/*    */ 
/*    */ public class Timer implements QClient {
/*    */   @Generated
/*  8 */   public void setStartTime(long startTime) { this.startTime = startTime; } @Generated public void setMillis(long millis) { this.millis = millis; }
/*    */   
/* 10 */   private long startTime = System.currentTimeMillis(); private long millis; @Generated public long getStartTime() { return this.startTime; } @Generated
/*    */   public long getMillis() {
/* 12 */     return this.millis;
/*    */   }
/*    */   public Timer() {
/* 15 */     reset();
/*    */   }
/*    */   
/*    */   public static Timer create() {
/* 19 */     return new Timer();
/*    */   }
/*    */   
/*    */   public boolean finished(long delay) {
/* 23 */     return (System.currentTimeMillis() - delay >= this.millis);
/*    */   }
/*    */   
/*    */   public void reset() {
/* 27 */     this.millis = System.currentTimeMillis();
/*    */   }
/*    */   
/*    */   public long getElapsedTime() {
/* 31 */     return System.currentTimeMillis() - this.millis;
/*    */   }
/*    */   
/*    */   public double deltaTime() {
/* 35 */     return (mc.method_47599() > 0) ? (1.0D / mc.method_47599()) : 1.0D;
/*    */   }
/*    */   public boolean every(long ms) {
/* 38 */     boolean passed = (getMillis(System.nanoTime() - this.millis) >= ms);
/* 39 */     if (passed)
/* 40 */       reset(); 
/* 41 */     return passed;
/*    */   }
/*    */   public boolean passed(long time) {
/* 44 */     return (System.currentTimeMillis() - this.startTime > time);
/*    */   }
/*    */   public long getMillis(long time) {
/* 47 */     return time / 1000000L;
/*    */   }
/*    */   
/*    */   public long getTime() {
/* 51 */     return System.currentTimeMillis() - this.startTime;
/*    */   }
/*    */   public void setTime(long time) {
/* 54 */     this.startTime = time;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\math\Timer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */