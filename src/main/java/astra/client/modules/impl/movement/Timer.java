/*    */ package shame.astra.client.modules.impl.movement;
/*    */ 
/*    */ import shame.astra.api.events.EventLink;
/*    */ import shame.astra.api.events.implement.EventUpdate;
/*    */ import shame.astra.client.modules.Module;
/*    */ import shame.astra.client.modules.settings.Setting;
/*    */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*    */ 
/*    */ public class Timer
/*    */   extends Module {
/* 11 */   public static Timer INSTANCE = new Timer();
/*    */   
/* 13 */   public FloatSetting speed = new FloatSetting("Скорость", 2.0F, 0.1F, 10.0F, 0.1F);
/*    */   
/*    */   public Timer() {
/* 16 */     super("Timer", "Ускоряет время в игре", Module.ModuleCategory.MOVEMENT);
/* 17 */     addSettings(new Setting[] { (Setting)this.speed });
/*    */   }
/*    */   
/*    */   @EventLink
/*    */   public void onUpdate(EventUpdate event) {
/* 22 */     mc.field_1724.field_28627 = this.speed.getValue().floatValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 27 */     super.onDisable();
/* 28 */     mc.field_1724.field_28627 = 1.0F;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\movement\Timer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */