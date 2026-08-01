/*    */ package shame.astra.api.storages.implement;
/*    */ 
/*    */ import shame.astra.api.QClient;
/*    */ import shame.astra.api.storages.implement.helpertstorages.enumvar.ModuleClass;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ModuleStorage
/*    */   implements QClient
/*    */ {
/*    */   public ModuleStorage() {
/* 13 */     initModules();
/*    */   }
/*    */   
/*    */   private void initModules() {
/* 17 */     ModuleClass.INSTANCE.initialize();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\api\storages\implement\ModuleStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */