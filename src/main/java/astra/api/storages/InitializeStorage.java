/*    */ package shame.astra.api.storages;
/*    */ import shame.astra.api.QClient;
/*    */ import shame.astra.api.events.EventInvoker;
/*    */ import shame.astra.api.storages.implement.CommandStorage;
/*    */ import shame.astra.api.storages.implement.MacroStorage;
/*    */ import shame.astra.api.storages.implement.ModuleStorage;
/*    */ import shame.astra.api.storages.implement.StaffStorage;
/*    */ import shame.astra.api.utils.tps.TPSCalc;
/*    */ import shame.astra.astra;
/*    */ 
/*    */ public class InitializeStorage implements QClient {
/*    */   public void onInitialize() {
/* 13 */     EventInvoker.register(this);
/* 14 */     initStorages();
/*    */   }
/*    */ 
/*    */   
/*    */   public void initStorages() {
/* 19 */     astra.INSTANCE.moduleStorage = new ModuleStorage();
/* 20 */     astra.INSTANCE.themeStorage = new ThemeStorage();
/* 21 */     astra.INSTANCE.tpsCalc = new TPSCalc();
/* 22 */     EventInvoker.register(astra.INSTANCE.tpsCalc);
/* 23 */     astra.INSTANCE.localizationStorage = new LocalizationStorage();
/* 24 */     astra.INSTANCE.freeLookStorage = new FreeLookStorage();
/* 25 */     astra.INSTANCE.rotationStorage = new RotationStorage();
/*    */     
/* 27 */     astra.INSTANCE.friendStorage = new FriendStorage();
/* 28 */     astra.INSTANCE.macroStorage = new MacroStorage();
/* 29 */     astra.INSTANCE.staffStorage = new StaffStorage();
/* 30 */     astra.INSTANCE.waypointStorage = new WaypointStorage();
/* 31 */     astra.INSTANCE.commandStorage = new CommandStorage();
/* 32 */     astra.INSTANCE.configStorage = new ConfigStorage();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\api\storages\InitializeStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */