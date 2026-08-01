/*    */ package shame.astra.api.utils.math;
/*    */ 
/*    */ public class StopWatch {
/*    */   @Generated
/*    */   public long getLastMS() {
/*  6 */     return this.lastMS;
/*  7 */   } public long lastMS = System.currentTimeMillis();
/*    */   public void reset() {
/*  9 */     this.lastMS = System.currentTimeMillis();
/*    */   }
/*    */   public boolean isReached(long time) {
/* 12 */     return (System.currentTimeMillis() - this.lastMS > time);
/*    */   }
/*    */   public void setLastMS(long newValue) {
/* 15 */     this.lastMS = System.currentTimeMillis() + newValue;
/*    */   }
/*    */   public void setTime(long time) {
/* 18 */     this.lastMS = time;
/*    */   }
/*    */   
/*    */   public long getTime() {
/* 22 */     return System.currentTimeMillis() - this.lastMS;
/*    */   }
/*    */   public boolean isRunning() {
/* 25 */     return (System.currentTimeMillis() - this.lastMS <= 0L);
/*    */   }
/*    */   public boolean hasTimeElapsed() {
/* 28 */     return (this.lastMS < System.currentTimeMillis());
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\math\StopWatch.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */