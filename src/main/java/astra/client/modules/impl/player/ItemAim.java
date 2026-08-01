/*    */ package shame.astra.client.modules.impl.player;
/*    */ 
/*    */ import net.minecraft.class_1297;
/*    */ import net.minecraft.class_1542;
/*    */ import net.minecraft.class_1802;
/*    */ import net.minecraft.class_241;
/*    */ import net.minecraft.class_243;
/*    */ import shame.astra.api.events.EventLink;
/*    */ import shame.astra.api.events.implement.EventUpdate;
/*    */ import shame.astra.api.storages.implement.RotationStorage;
/*    */ import shame.astra.api.utils.rotate.Rotation;
/*    */ import shame.astra.api.utils.rotate.RotationUtils;
/*    */ import shame.astra.client.modules.Module;
/*    */ import shame.astra.client.modules.settings.Setting;
/*    */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*    */ import shame.astra.client.modules.settings.implement.ListSetting;
/*    */ 
/*    */ public class ItemAim
/*    */   extends Module {
/* 20 */   public static ItemAim INSTANCE = new ItemAim();
/* 21 */   public ListSetting element = new ListSetting("Лутать", new BooleanSetting[] { new BooleanSetting("Шары", true), new BooleanSetting("Элитры", true) });
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemAim() {
/* 26 */     super("ItemAim", "Автоматически наводиться на предмет", Module.ModuleCategory.PLAYER);
/* 27 */     addSettings(new Setting[] { (Setting)this.element });
/*    */   }
/*    */ 
/*    */   
/*    */   @EventLink
/*    */   public void onEvent(EventUpdate event) {
/* 33 */     if (mc.field_1724 == null || mc.field_1687 == null)
/*    */       return; 
/* 35 */     class_1542 targetItem = findTargetItem();
/* 36 */     if (targetItem == null)
/*    */       return; 
/* 38 */     class_241 rotations = getItemRotations(targetItem);
/* 39 */     RotationStorage.update(new Rotation(rotations.field_1343, rotations.field_1342), 360.0F, 360.0F, 360.0F, 360.0F, 0, 1, false);
/*    */   }
/*    */   
/*    */   private class_1542 findTargetItem() {
/* 43 */     class_1542 bestItem = null;
/* 44 */     double bestDistance = Double.MAX_VALUE;
/*    */     
/* 46 */     for (class_1297 entity : mc.field_1687.method_18112()) {
/* 47 */       if (entity instanceof class_1542) { class_1542 itemEntity = (class_1542)entity;
/* 48 */         if (!isWantedItem(itemEntity))
/*    */           continue; 
/* 50 */         double distance = mc.field_1724.method_5858((class_1297)itemEntity);
/* 51 */         if (distance < bestDistance) {
/* 52 */           bestDistance = distance;
/* 53 */           bestItem = itemEntity;
/*    */         }  }
/*    */     
/*    */     } 
/* 57 */     return bestItem;
/*    */   }
/*    */   
/*    */   private boolean isWantedItem(class_1542 itemEntity) {
/* 61 */     return ((this.element.is("Шары") && itemEntity.method_6983().method_31574(class_1802.field_8575)) || (this.element
/* 62 */       .is("Элитры") && itemEntity.method_6983().method_31574(class_1802.field_8833)));
/*    */   }
/*    */   
/*    */   private class_241 getItemRotations(class_1542 itemEntity) {
/* 66 */     class_243 targetPos = itemEntity.method_5829().method_1005();
/* 67 */     return RotationUtils.getRotations(targetPos);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\player\ItemAim.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */