/*    */ package shame.astra.api.events.implement;
/*    */ 
/*    */ 
/*    */ public class EventMoveInput extends Event {
/*    */   private float forward;
/*    */   private float strafe;
/*    */   
/*    */   @Generated
/*  9 */   public void setForward(float forward) { this.forward = forward; } private boolean jump; private boolean sneak; @Generated public void setStrafe(float strafe) { this.strafe = strafe; } @Generated public void setJump(boolean jump) { this.jump = jump; } @Generated public void setSneak(boolean sneak) { this.sneak = sneak; } @Generated
/* 10 */   public EventMoveInput(float forward, float strafe, boolean jump, boolean sneak) { this.forward = forward; this.strafe = strafe; this.jump = jump; this.sneak = sneak; }
/*    */   @Generated
/* 12 */   public float getForward() { return this.forward; } @Generated
/* 13 */   public float getStrafe() { return this.strafe; } @Generated
/* 14 */   public boolean isJump() { return this.jump; } @Generated public boolean isSneak() { return this.sneak; }
/*    */ 
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\api\events\implement\EventMoveInput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */