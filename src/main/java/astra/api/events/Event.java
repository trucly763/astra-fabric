/*    */ package shame.astra.api.events;
/*    */ 
/*    */ import lombok.Generated;
/*    */ 
/*    */ public class Event {
/*    */   @Generated
/*    */   public void setCancelled(boolean cancelled) {
/*  8 */     this.cancelled = cancelled;
/*    */   } private boolean cancelled;
/*    */   @Generated
/*    */   public boolean isCancelled() {
/* 12 */     return this.cancelled;
/*    */   }
/*    */   public void cancel() {
/* 15 */     this.cancelled = true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void call() {
/*    */     try {
/* 22 */       EventInvoker.invoke(this);
/*    */     }
/* 24 */     catch (IllegalAccessException|java.lang.reflect.InvocationTargetException|InstantiationException e) {
/*    */       
/* 26 */       throw new RuntimeException("Failed to Invoke Method", e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\api\events\Event.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */