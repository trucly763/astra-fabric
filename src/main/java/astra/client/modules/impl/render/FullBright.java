/*    */ package shame.astra.client.modules.impl.render;
/*    */ 
/*    */ import net.minecraft.class_1293;
/*    */ import net.minecraft.class_1294;
/*    */ import shame.astra.api.events.EventLink;
/*    */ import shame.astra.api.events.implement.EventUpdate;
/*    */ import shame.astra.client.modules.Module;
/*    */ 
/*    */ public class FullBright
/*    */   extends Module {
/* 11 */   public static FullBright INSTANCE = new FullBright();
/*    */   
/*    */   public FullBright() {
/* 14 */     super("FullBright", "Всегда светло", Module.ModuleCategory.RENDER);
/*    */   }
/*    */   
/*    */   @EventLink
/*    */   public void onUpdate(EventUpdate ignored) {
/* 19 */     if (mc.field_1724 == null || mc.field_1687 == null)
/* 20 */       return;  mc.field_1724.method_6092(new class_1293(class_1294.field_5925, 777, 1));
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 25 */     if (mc.field_1724 == null || mc.field_1687 == null)
/* 26 */       return;  mc.field_1724.method_6016(class_1294.field_5925);
/* 27 */     super.onDisable();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\FullBright.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */