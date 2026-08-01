/*    */ package shame.astra.client.modules.impl.movement;
/*    */ 
/*    */ import net.minecraft.class_2596;
/*    */ import net.minecraft.class_2828;
/*    */ import shame.astra.api.events.EventLink;
/*    */ import shame.astra.api.events.implement.EventUpdate;
/*    */ import shame.astra.client.modules.Module;
/*    */ 
/*    */ public class GrimNoFall extends Module {
/* 10 */   public static GrimNoFall INSTANCE = new GrimNoFall();
/*    */   
/*    */   public GrimNoFall() {
/* 13 */     super("NoFall", "Убирает урон от падения", Module.ModuleCategory.MOVEMENT);
/*    */   }
/*    */   
/*    */   @EventLink
/*    */   public void onUpdate(EventUpdate ignored) {
/* 18 */     if (mc.field_1724 == null || mc.method_1562() == null)
/* 19 */       return;  if (!mc.field_1724.method_24828() && mc.field_1724.field_6017 > 1.0F) {
/* 20 */       mc.method_1562().method_52787((class_2596)new class_2828.class_2830(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.0E-9D, mc.field_1724.method_23321(), mc.field_1724.method_36454(), mc.field_1724.method_36455(), true, false));
/* 21 */       mc.field_1724.method_38785();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\movement\GrimNoFall.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */