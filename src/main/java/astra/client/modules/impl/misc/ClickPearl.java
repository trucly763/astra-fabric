/*    */ package shame.astra.client.modules.impl.misc;
/*    */ import net.minecraft.class_1268;
/*    */ import net.minecraft.class_1657;
/*    */ import net.minecraft.class_1802;
/*    */ import shame.astra.api.events.EventLink;
/*    */ import shame.astra.api.events.implement.EventBinding;
/*    */ import shame.astra.api.events.implement.EventUpdate;
/*    */ import shame.astra.api.utils.player.InventoryUtils;
/*    */ import shame.astra.client.modules.Module;
/*    */ import shame.astra.client.modules.settings.Setting;
/*    */ import shame.astra.client.modules.settings.implement.BindSetting;
/*    */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*    */ 
/*    */ public class ClickPearl extends Module {
/* 15 */   public static ClickPearl INSTANCE = new ClickPearl();
/*    */   
/* 17 */   private final BindSetting keyToPearl = new BindSetting("Кнопка", -1);
/* 18 */   private final BooleanSetting bypass = new BooleanSetting("Обход", true);
/*    */   
/*    */   private boolean use;
/*    */   
/*    */   public ClickPearl() {
/* 23 */     super("ClickPearl", "Кидает перку по внутреннему бинду", Module.ModuleCategory.MISC);
/* 24 */     addSettings(new Setting[] { (Setting)this.keyToPearl, (Setting)this.bypass });
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 29 */     this.use = false;
/* 30 */     super.onEnable();
/*    */   }
/*    */   
/*    */   @EventLink
/*    */   public void onEvent(EventBinding event) {
/* 35 */     if (mc.field_1755 != null)
/* 36 */       return;  if (event.getKey() == this.keyToPearl.getKey()) {
/* 37 */       this.use = true;
/*    */     }
/*    */   }
/*    */   
/*    */   @EventLink
/*    */   public void onEvent(EventUpdate event) {
/* 43 */     if (!this.use)
/* 44 */       return;  if (mc.field_1724 == null || mc.field_1687 == null) {
/* 45 */       this.use = false;
/*    */       
/*    */       return;
/*    */     } 
/* 49 */     int oldSlot = (mc.field_1724.method_31548()).field_7545;
/* 50 */     int pearlSlot = InventoryUtils.find(class_1802.field_8634, 0, 36);
/*    */     
/* 52 */     if (pearlSlot == -1) {
/* 53 */       this.use = false;
/*    */       
/*    */       return;
/*    */     } 
/* 57 */     if (pearlSlot > 9) {
/* 58 */       mc.field_1724.method_5728(false);
/*    */     }
/*    */     
/* 61 */     if (this.bypass.isState()) {
/* 62 */       (mc.field_1724.method_31548()).field_7545 = pearlSlot;
/* 63 */       mc.field_1761.method_2919((class_1657)mc.field_1724, class_1268.field_5808);
/* 64 */       (mc.field_1724.method_31548()).field_7545 = oldSlot;
/*    */     } else {
/* 66 */       InventoryUtils.swapAndUseHvH(class_1802.field_8634);
/*    */     } 
/*    */     
/* 69 */     this.use = false;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\misc\ClickPearl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */