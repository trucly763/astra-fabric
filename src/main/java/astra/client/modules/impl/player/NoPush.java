/*    */ package shame.astra.client.modules.impl.player;
/*    */ import shame.astra.client.modules.Module;
/*    */ import shame.astra.client.modules.settings.Setting;
/*    */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*    */ import shame.astra.client.modules.settings.implement.ListSetting;
/*    */ 
/*    */ public class NoPush extends Module {
/*    */   @Generated
/*    */   public void setCollisionList(ListSetting collisionList) {
/* 10 */     this.collisionList = collisionList;
/*    */   }
/*    */   
/* 13 */   public static NoPush INSTANCE = new NoPush();
/*    */   
/* 15 */   private ListSetting collisionList = new ListSetting("Коллизия", new BooleanSetting[] { new BooleanSetting("Блоки", true), new BooleanSetting("Вода", false), new BooleanSetting("Удочик", true), new BooleanSetting("Игроки", true) }); @Generated public ListSetting getCollisionList() { return this.collisionList; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NoPush() {
/* 22 */     super("NoPush", "Отключает коллизию", Module.ModuleCategory.MISC);
/* 23 */     addSettings(new Setting[] { (Setting)this.collisionList });
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\player\NoPush.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */