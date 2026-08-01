/*    */ package shame.astra.client.modules.impl.movement;
/*    */ 
/*    */ import shame.astra.api.events.EventLink;
/*    */ import shame.astra.api.events.implement.EventUpdate;
/*    */ import shame.astra.client.modules.Module;
/*    */ import shame.astra.client.modules.settings.Setting;
/*    */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*    */ 
/*    */ public class TpBack
/*    */   extends Module {
/* 11 */   public static TpBack INSTANCE = new TpBack();
/*    */   
/*    */   private boolean isDead = false;
/*    */   private boolean waitingForRespawn = false;
/* 15 */   private int tickCounter = 0;
/*    */ 
/*    */   
/* 18 */   public FloatSetting delay = new FloatSetting("Задержка", 5.0F, 1.0F, 20.0F, 1.0F);
/*    */   
/*    */   public TpBack() {
/* 21 */     super("TpBack", "Возвращает на точки смерти", Module.ModuleCategory.MOVEMENT);
/* 22 */     addSettings(new Setting[] { (Setting)this.delay });
/*    */   }
/*    */ 
/*    */   
/*    */   @EventLink
/*    */   public void onEvent(EventUpdate event) {
/* 28 */     if (mc.field_1724 == null || mc.field_1687 == null)
/*    */       return; 
/* 30 */     int i = ((mc.field_1724.method_6032() <= 0.0F) ? 1 : 0) & ((mc.field_1724.field_6213 > 0) ? 1 : 0);
/*    */     
/* 32 */     if (i != 0 && !this.isDead) {
/* 33 */       this.isDead = true;
/* 34 */       mc.field_1724.field_3944.method_45729("/sethome astra");
/* 35 */       mc.field_1724.method_7331();
/* 36 */       this.waitingForRespawn = true;
/* 37 */       this.tickCounter = 0;
/*    */     } 
/*    */     
/* 40 */     if (this.waitingForRespawn && i == 0) {
/* 41 */       this.tickCounter++;
/* 42 */       if (this.tickCounter >= this.delay.get()) {
/* 43 */         mc.field_1724.field_3944.method_45729("/home astra");
/* 44 */         this.waitingForRespawn = false;
/* 45 */         this.tickCounter = 0;
/*    */       } 
/*    */     } 
/*    */     
/* 49 */     if (i == 0 && !this.waitingForRespawn)
/* 50 */       this.isDead = false; 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\movement\TpBack.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */