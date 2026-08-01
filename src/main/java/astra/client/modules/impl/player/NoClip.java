/*    */ package shame.astra.client.modules.impl.player;
/*    */ 
/*    */ import shame.astra.api.events.EventLink;
/*    */ import shame.astra.api.events.implement.EventUpdate;
/*    */ import shame.astra.client.modules.Module;
/*    */ 
/*    */ public class NoClip
/*    */   extends Module
/*    */ {
/* 10 */   public static NoClip INSTANCE = new NoClip();
/*    */   public NoClip() {
/* 12 */     super("NoClip", "Позволяте проходить через блоки", Module.ModuleCategory.PLAYER);
/*    */   }
/*    */ 
/*    */   
/*    */   @EventLink
/*    */   public void onUpdate(EventUpdate ignored) {
/* 18 */     if (mc.field_1724 == null)
/*    */       return; 
/* 20 */     if (mc.field_1724.field_6012 % 35 == 0) {
/* 21 */       mc.field_1724.field_3944.method_45729("/gmsp");
/* 22 */     } else if (mc.field_1724.field_6012 % 35 == 2) {
/* 23 */       mc.field_1724.field_3944.method_45729("/gms");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\player\NoClip.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */