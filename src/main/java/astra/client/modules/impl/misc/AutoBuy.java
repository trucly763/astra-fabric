/*    */ package shame.astra.client.modules.impl.misc;
/*    */ 
/*    */ import shame.astra.client.modules.Module;
/*    */ import shame.astra.client.modules.settings.Setting;
/*    */ import shame.astra.client.modules.settings.implement.BindSetting;
/*    */ 
/*    */ public class AutoBuy extends Module {
/*  8 */   public static AutoBuy INSTANCE = new AutoBuy();
/*    */   
/* 10 */   public BindSetting openKey = new BindSetting("Бинд гуи", -1);
/*    */   
/*    */   public AutoBuy() {
/* 13 */     super("AutoBuy", Module.ModuleCategory.MISC);
/* 14 */     addSettings(new Setting[] { (Setting)this.openKey });
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\misc\AutoBuy.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */