/*    */ package shame.astra.api.utils.animation;
/*    */ 
/*    */ import net.minecraft.class_3532;
/*    */ 
/*    */ public class AnimationUtils {
/*    */   private float currentValue;
/*    */   private float targetValue;
/*    */   private float fromValue;
/*    */   private float speed;
/*    */   private Easing easing;
/*    */   private long startTime;
/*    */   private double duration;
/*    */   private boolean isRunning;
/*    */   
/*    */   public AnimationUtils(float initialValue, float speed, Easing easing) {
/* 16 */     this.currentValue = initialValue;
/* 17 */     this.targetValue = initialValue;
/* 18 */     this.fromValue = initialValue;
/* 19 */     this.speed = speed;
/* 20 */     this.easing = (easing != null) ? easing : Easings.LINEAR;
/* 21 */     this.startTime = 0L;
/* 22 */     this.duration = 0.0D;
/* 23 */     this.isRunning = false;
/*    */   }
/*    */   
/*    */   public AnimationUtils(float initialValue, float speed) {
/* 27 */     this(initialValue, speed, Easings.LINEAR);
/*    */   }
/*    */   
/*    */   public void update(float target) {
/* 31 */     if (this.targetValue != target || !this.isRunning) {
/* 32 */       this.targetValue = target;
/* 33 */       this.fromValue = this.currentValue;
/* 34 */       this.startTime = System.nanoTime();
/* 35 */       this.duration = 1.0D / this.speed * 2.0D;
/* 36 */       this.isRunning = true;
/*    */     } 
/*    */     
/* 39 */     if (isDone()) {
/* 40 */       this.currentValue = this.targetValue;
/* 41 */       this.isRunning = false;
/*    */       
/*    */       return;
/*    */     } 
/* 45 */     double part = calculatePart();
/* 46 */     float easedPart = (float)this.easing.ease(part);
/* 47 */     this.currentValue = class_3532.method_16439(easedPart, this.fromValue, this.targetValue);
/*    */   }
/*    */   
/*    */   private double calculatePart() {
/* 51 */     if (!this.isRunning) return 1.0D; 
/* 52 */     long now = System.nanoTime();
/* 53 */     double elapsed = (now - this.startTime) / 1.0E9D;
/* 54 */     return class_3532.method_15350(elapsed / this.duration, 0.0D, 1.0D);
/*    */   }
/*    */   
/*    */   public float getValue() {
/* 58 */     return this.currentValue;
/*    */   }
/*    */   
/*    */   public void setValue(float value) {
/* 62 */     this.currentValue = value;
/* 63 */     this.targetValue = value;
/* 64 */     this.fromValue = value;
/* 65 */     this.isRunning = false;
/*    */   }
/*    */   
/*    */   public float getTarget() {
/* 69 */     return this.targetValue;
/*    */   }
/*    */   
/*    */   public void setSpeed(float speed) {
/* 73 */     this.speed = speed;
/* 74 */     this.duration = 1.0D / speed;
/*    */   }
/*    */   
/*    */   public void setEasing(Easing easing) {
/* 78 */     this.easing = (easing != null) ? easing : Easings.LINEAR;
/*    */   }
/*    */   
/*    */   public boolean isDone() {
/* 82 */     return (calculatePart() >= 1.0D);
/*    */   }
/*    */   
/*    */   public boolean isAlive() {
/* 86 */     return !isDone();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\animation\AnimationUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */