/*    */ package shame.astra.client.modules.impl.misc;
/*    */ 
/*    */ import shame.astra.api.events.EventLink;
/*    */ import shame.astra.api.events.implement.EventPacket;
/*    */ import shame.astra.api.events.implement.EventUpdate;
/*    */ import shame.astra.client.modules.Module;
/*    */ import shame.astra.client.modules.settings.Setting;
/*    */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*    */ 
/*    */ public class XCarry
/*    */   extends Module
/*    */ {
/* 13 */   public static XCarry INSTANCE = new XCarry();
/* 14 */   public BooleanSetting autoDisable = new BooleanSetting("Авто выкл", true);
/*    */   
/*    */   private boolean wasInInventory = false;
/*    */   
/*    */   public XCarry() {
/* 19 */     super("XCarry", "Дополнительные слоты", Module.ModuleCategory.MISC);
/* 20 */     addSettings(new Setting[] { (Setting)this.autoDisable });
/*    */   }
/*    */   
/*    */   @EventLink
/*    */   public void onPacket(EventPacket event) {
/* 25 */     if (mc.field_1724 == null || mc.field_1687 == null)
/*    */       return; 
/* 27 */     if (event.getPacket() instanceof net.minecraft.class_2815 && mc.field_1755 instanceof net.minecraft.class_490) {
/* 28 */       event.cancel();
/* 29 */       this.wasInInventory = true;
/*    */     } 
/*    */   }
/*    */   
/*    */   @EventLink
/*    */   public void onUpdate(EventUpdate event) {
/* 35 */     if (mc.field_1724 == null || mc.field_1687 == null)
/*    */       return; 
/* 37 */     if (this.wasInInventory && mc.field_1755 == null) {
/* 38 */       if (this.autoDisable.isState()) {
/* 39 */         toggle();
/*    */       }
/* 41 */       this.wasInInventory = false;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\misc\XCarry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */