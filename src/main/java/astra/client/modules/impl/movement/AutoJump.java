/*    */ package shame.astra.client.modules.impl.movement;
/*    */ 
/*    */ import shame.astra.api.events.EventLink;
/*    */ import shame.astra.api.events.implement.EventUpdate;
/*    */ import shame.astra.api.storages.implement.helpertstorages.enumvar.ModuleClass;
/*    */ import shame.astra.client.modules.Module;
/*    */ import shame.astra.client.modules.impl.combat.Aura;
/*    */ 
/*    */ public class AutoJump
/*    */   extends Module {
/* 11 */   public static AutoJump INSTANCE = new AutoJump();
/*    */   
/*    */   public AutoJump() {
/* 14 */     super("AutoJump", "Прыгает автоматически при ауре", Module.ModuleCategory.MOVEMENT);
/*    */   }
/*    */   
/*    */   @EventLink
/*    */   public void onUpdate(EventUpdate event) {
/* 19 */     if (mc.field_1724 == null || mc.field_1687 == null)
/*    */       return; 
/* 21 */     Aura aura = ModuleClass.aura;
/*    */     
/* 23 */     if (aura == null || !aura.isEnable())
/*    */       return; 
/* 25 */     if (aura.getTarget() != null && 
/* 26 */       mc.field_1724.method_24828())
/* 27 */       mc.field_1724.method_6043(); 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\movement\AutoJump.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */