/*    */ package shame.astra.client.modules.impl.player;
/*    */ 
/*    */ import shame.astra.client.modules.Module;
/*    */ import shame.astra.client.modules.settings.Setting;
/*    */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*    */ 
/*    */ public class ItemScroller extends Module {
/*  8 */   public static ItemScroller INSTANCE = new ItemScroller();
/*    */   
/* 10 */   public final FloatSetting delay = new FloatSetting("Задержка", 50.0F, 0.0F, 200.0F, 1.0F);
/*    */   
/*    */   private long lastQuickMoveAt;
/*    */   
/*    */   public ItemScroller() {
/* 15 */     super("ItemScroller", "Убирает задержку перемещения предметов", Module.ModuleCategory.PLAYER);
/* 16 */     addSettings(new Setting[] { (Setting)this.delay });
/*    */   }
/*    */   
/*    */   public boolean canQuickMove() {
/* 20 */     long now = System.currentTimeMillis();
/* 21 */     if (now - this.lastQuickMoveAt < (long)this.delay.get()) {
/* 22 */       return false;
/*    */     }
/*    */     
/* 25 */     this.lastQuickMoveAt = now;
/* 26 */     return true;
/*    */   }
/*    */   
/*    */   public void resetTimer() {
/* 30 */     this.lastQuickMoveAt = 0L;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 35 */     resetTimer();
/* 36 */     super.onDisable();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\player\ItemScroller.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */