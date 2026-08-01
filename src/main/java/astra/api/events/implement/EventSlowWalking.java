/*    */ package shame.astra.api.events.implement;
/*    */ 
/*    */ import shame.astra.api.events.Event;
/*    */ 
/*    */ public class EventSlowWalking
/*    */   extends Event {
/*    */   private boolean cancelled;
/*    */   
/*    */   public boolean isCancelled() {
/* 10 */     return this.cancelled;
/*    */   }
/*    */   
/*    */   public void setCancelled(boolean cancelled) {
/* 14 */     this.cancelled = cancelled;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\api\events\implement\EventSlowWalking.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */