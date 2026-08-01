/*    */ package shame.astra.api.events.implement;
/*    */ 
/*    */ 
/*    */ public class EventRotation extends Event {
/*    */   private float yaw;
/*    */   
/*    */   @Generated
/*  8 */   public void setYaw(float yaw) { this.yaw = yaw; } private float pitch; private float partialTicks; @Generated public void setPitch(float pitch) { this.pitch = pitch; } @Generated public void setPartialTicks(float partialTicks) { this.partialTicks = partialTicks; } @Generated public EventRotation(float yaw, float pitch, float partialTicks) { this.yaw = yaw; this.pitch = pitch; this.partialTicks = partialTicks; }
/*    */   @Generated
/* 10 */   public float getYaw() { return this.yaw; } @Generated public float getPitch() { return this.pitch; } @Generated
/* 11 */   public float getPartialTicks() { return this.partialTicks; }
/*    */ 
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\api\events\implement\EventRotation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */