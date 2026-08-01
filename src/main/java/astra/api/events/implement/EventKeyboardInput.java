/*    */ package shame.astra.api.events.implement;
/*    */ 
/*    */ 
/*    */ public class EventKeyboardInput extends Event {
/*    */   private float movementForward;
/*    */   private float movementSideways;
/*    */   
/*    */   @Generated
/*  9 */   public EventKeyboardInput(float movementForward, float movementSideways) { this.movementForward = movementForward; this.movementSideways = movementSideways; } @Generated public void setMovementForward(float movementForward) { this.movementForward = movementForward; } @Generated public void setMovementSideways(float movementSideways) { this.movementSideways = movementSideways; }
/*    */   @Generated
/* 11 */   public float getMovementForward() { return this.movementForward; } @Generated public float getMovementSideways() { return this.movementSideways; }
/*    */   
/*    */   public void setYaw(float yaw, float yaw2) {
/* 14 */     float forward = getMovementForward();
/* 15 */     float sideways = getMovementSideways();
/* 16 */     double angle = class_3532.method_15338(Math.toDegrees(direction(yaw2, forward, sideways)));
/* 17 */     if (forward == 0.0F && sideways == 0.0F)
/* 18 */       return;  float closestForward = 0.0F, closestSideways = 0.0F, closestDifference = Float.MAX_VALUE;
/*    */     float predictedForward;
/* 20 */     for (predictedForward = -1.0F; predictedForward <= 1.0F; predictedForward++) {
/* 21 */       float predictedSideways; for (predictedSideways = -1.0F; predictedSideways <= 1.0F; predictedSideways++) {
/* 22 */         if (predictedSideways != 0.0F || predictedForward != 0.0F) {
/*    */           
/* 24 */           double predictedAngle = class_3532.method_15338(Math.toDegrees(direction(yaw, predictedForward, predictedSideways)));
/* 25 */           double difference = Math.abs(angle - predictedAngle);
/*    */           
/* 27 */           if (difference < closestDifference) {
/* 28 */             closestDifference = (float)difference;
/* 29 */             closestForward = predictedForward;
/* 30 */             closestSideways = predictedSideways;
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     } 
/* 35 */     setMovementForward(closestForward);
/* 36 */     setMovementSideways(closestSideways);
/*    */   }
/*    */   
/*    */   private double direction(float yaw, double movementForward, double movementSideways) {
/* 40 */     if (movementForward < 0.0D) yaw += 180.0F; 
/* 41 */     float forward = 1.0F;
/* 42 */     if (movementForward < 0.0D) { forward = -0.5F; }
/* 43 */     else if (movementForward > 0.0D) { forward = 0.5F; }
/* 44 */      if (movementSideways > 0.0D) yaw -= 90.0F * forward; 
/* 45 */     if (movementSideways < 0.0D) yaw += 90.0F * forward; 
/* 46 */     return Math.toRadians(yaw);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\api\events\implement\EventKeyboardInput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */