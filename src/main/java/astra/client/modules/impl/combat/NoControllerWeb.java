/*    */ package shame.astra.client.modules.impl.combat;
/*    */ 
/*    */ import net.minecraft.class_2246;
/*    */ import shame.astra.api.events.EventLink;
/*    */ import shame.astra.api.events.implement.EventBlockCollide;
/*    */ import shame.astra.client.modules.Module;
/*    */ 
/*    */ public class NoControllerWeb
/*    */   extends Module {
/* 10 */   public static NoControllerWeb INSTANCE = new NoControllerWeb();
/*    */   
/*    */   public NoControllerWeb() {
/* 13 */     super("NoControllerWeb", "Позволяет ломать и бить сквозь паутину", Module.ModuleCategory.COMBAT);
/*    */   }
/*    */   
/*    */   @EventLink
/*    */   public void onBlockCollide(EventBlockCollide e) {
/* 18 */     if (mc.field_1687 == null || e.getPos() == null)
/* 19 */       return;  if (mc.field_1687.method_8320(e.getPos()).method_26204() == class_2246.field_10343)
/* 20 */       e.setCancelled(true); 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\combat\NoControllerWeb.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */