/*    */ package shame.astra.client.modules.impl.player;
/*    */ 
/*    */ import shame.astra.api.events.EventLink;
/*    */ import shame.astra.api.events.implement.EventBinding;
/*    */ import shame.astra.client.modules.Module;
/*    */ import shame.astra.client.modules.settings.Setting;
/*    */ import shame.astra.client.modules.settings.implement.BindSetting;
/*    */ 
/*    */ public class HelpMessage extends Module {
/* 10 */   public static HelpMessage INSTANCE = new HelpMessage();
/*    */   
/* 12 */   private final BindSetting bind = new BindSetting("Бинд", -1);
/*    */   
/*    */   public HelpMessage() {
/* 15 */     super("HelpMessage", "Отправляет координаты в глобальный чат", Module.ModuleCategory.PLAYER);
/* 16 */     addSettings(new Setting[] { (Setting)this.bind });
/*    */   }
/*    */   
/*    */   @EventLink
/*    */   public void onBinding(EventBinding event) {
/* 21 */     if (mc.field_1724 == null || mc.method_1562() == null || mc.field_1755 != null) {
/*    */       return;
/*    */     }
/*    */     
/* 25 */     if (event.getKey() != this.bind.getKey()) {
/*    */       return;
/*    */     }
/*    */     
/* 29 */     int x = mc.field_1724.method_31477();
/* 30 */     int y = mc.field_1724.method_31478();
/* 31 */     int z = mc.field_1724.method_31479();
/* 32 */     mc.method_1562().method_45729("! " + x + " " + y + " " + z);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\player\HelpMessage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */