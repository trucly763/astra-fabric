/*    */ package shame.astra.client.modules.impl.player;
/*    */ 
/*    */ import shame.astra.api.events.EventLink;
/*    */ import shame.astra.api.events.implement.EventUpdate;
/*    */ import shame.astra.client.modules.Module;
/*    */ import shame.astra.mixin.ILivingEntity;
/*    */ 
/*    */ public class NoJumpDelay
/*    */   extends Module {
/* 10 */   public static NoJumpDelay INSTANCE = new NoJumpDelay();
/*    */   
/*    */   public NoJumpDelay() {
/* 13 */     super("NoJumpDelay", "Убирает задержку на прыжок", Module.ModuleCategory.PLAYER);
/*    */   }
/*    */   
/*    */   @EventLink
/*    */   public void onEvent(EventUpdate event) {
/* 18 */     if (mc.field_1724 == null || mc.field_1687 == null)
/*    */       return; 
/* 20 */     ((ILivingEntity)mc.field_1724).setJumpingCooldown(0);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\player\NoJumpDelay.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */