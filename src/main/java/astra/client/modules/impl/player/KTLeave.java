/*    */ package shame.astra.client.modules.impl.player;
/*    */ 
/*    */ import shame.astra.api.events.EventLink;
/*    */ import shame.astra.api.events.implement.EventBinding;
/*    */ import shame.astra.client.modules.Module;
/*    */ import shame.astra.client.modules.settings.Setting;
/*    */ import shame.astra.client.modules.settings.implement.BindSetting;
/*    */ 
/*    */ public class KTLeave
/*    */   extends Module {
/* 11 */   public static KTLeave INSTANCE = new KTLeave();
/*    */   private boolean hasGM;
/*    */   private double lastX;
/*    */   private double lastY;
/*    */   private double lastZ;
/* 16 */   private BindSetting bind = new BindSetting("Кнопка лива", -1);
/*    */   
/*    */   public KTLeave() {
/* 19 */     super("KTLeave", "Позволяет ливнуть с пвп прямо в кт", Module.ModuleCategory.PLAYER);
/* 20 */     addSettings(new Setting[] { (Setting)this.bind });
/*    */   }
/*    */ 
/*    */   
/*    */   @EventLink
/*    */   public void onKey(EventBinding e) {
/* 26 */     if (mc.field_1724 == null)
/* 27 */       return;  if (e.getKey() == this.bind.getKey()) {
/* 28 */       this.hasGM = !this.hasGM;
/*    */       
/* 30 */       if (this.hasGM) {
/* 31 */         this.lastX = mc.field_1724.method_23317();
/* 32 */         this.lastY = mc.field_1724.method_23318();
/* 33 */         this.lastZ = mc.field_1724.method_23321();
/* 34 */         mc.field_1724.method_5814(mc.field_1724.method_23317() + 10.0D, mc.field_1724.method_23318() + 10.0D, mc.field_1724.method_23321() + 10.0D);
/*    */       } else {
/* 36 */         mc.field_1724.method_5814(this.lastX, this.lastY, this.lastZ);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 43 */     super.onDisable();
/* 44 */     this.hasGM = false;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\player\KTLeave.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */