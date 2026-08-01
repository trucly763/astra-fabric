/*    */ package shame.astra.api.utils.math;
/*    */ 
/*    */ import lombok.Generated;
/*    */ 
/*    */ public class TimerUtils {
/*    */   private long millis;
/*    */   
/*    */   public TimerUtils() {
/*  9 */     reset();
/*    */   }
/*    */   
/*    */   public boolean finished(float delay) {
/* 13 */     return ((float)System.currentTimeMillis() - delay >= (float)this.millis);
/*    */   }
/*    */   
/*    */   public boolean finished(long delay) {
/* 17 */     return (System.currentTimeMillis() - this.millis >= delay);
/*    */   }
/*    */   
/*    */   public void reset() {
/* 21 */     this.millis = System.currentTimeMillis();
/*    */   }
/*    */   
/*    */   public long getElapsedTime() {
/* 25 */     return System.currentTimeMillis() - this.millis;
/*    */   }
/*    */   
/*    */   @Generated
/*    */   public long getMillis() {
/* 30 */     return this.millis;
/*    */   }
/*    */   
/*    */   @Generated
/*    */   public void setMillis(long millis) {
/* 35 */     this.millis = millis;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\math\TimerUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */