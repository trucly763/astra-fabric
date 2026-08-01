/*    */ package shame.astra.client.modules.impl.player;
/*    */ import net.minecraft.class_1657;
/*    */ import net.minecraft.class_1713;
/*    */ import net.minecraft.class_1738;
/*    */ import net.minecraft.class_1792;
/*    */ import net.minecraft.class_1799;
/*    */ import shame.astra.api.events.implement.EventUpdate;
/*    */ import shame.astra.client.modules.Module;
/*    */ import shame.astra.client.modules.settings.Setting;
/*    */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*    */ 
/*    */ public class AutoArmor extends Module {
/* 13 */   public static AutoArmor INSTANCE = new AutoArmor();
/*    */   
/* 15 */   private final FloatSetting delay = new FloatSetting("Задержка", 25.0F, 1.0F, 1000.0F, 1.0F);
/* 16 */   private long lastEquipTime = 0L;
/*    */   
/*    */   public AutoArmor() {
/* 19 */     super("AutoArmor", "Автоматически одевает броню", Module.ModuleCategory.PLAYER);
/* 20 */     addSettings(new Setting[] { (Setting)this.delay });
/*    */   }
/*    */   
/*    */   @EventLink
/*    */   public void onEvent(EventUpdate event) {
/* 25 */     if (mc.field_1724 == null || mc.field_1687 == null)
/* 26 */       return;  if (isMoving())
/*    */       return; 
/* 28 */     long currentTime = System.currentTimeMillis();
/* 29 */     if ((float)(currentTime - this.lastEquipTime) < this.delay.get())
/*    */       return; 
/* 31 */     for (int i = 0; i < 4; i++) {
/* 32 */       class_1799 currentArmor = mc.field_1724.method_31548().method_7372(i);
/*    */       
/* 34 */       if (currentArmor.method_7960())
/* 35 */         for (int j = 0; j < 36; j++) {
/* 36 */           class_1799 stack = mc.field_1724.method_31548().method_5438(j);
/*    */           
/* 38 */           if (!stack.method_7960()) { class_1792 class_1792 = stack.method_7909(); if (class_1792 instanceof class_1738) { class_1738 armorItem = (class_1738)class_1792;
/* 39 */               if (getArmorSlotIndex(armorItem) == i) {
/* 40 */                 int slotToEquip = j;
/*    */                 
/* 42 */                 if (j < 9) {
/* 43 */                   slotToEquip = j + 36;
/*    */                 }
/*    */                 
/* 46 */                 mc.field_1761.method_2906(0, slotToEquip, 0, class_1713.field_7794, (class_1657)mc.field_1724);
/* 47 */                 this.lastEquipTime = currentTime;
/*    */                 return;
/*    */               }  }
/*    */              }
/*    */         
/*    */         }  
/*    */     } 
/*    */   }
/*    */   
/*    */   private boolean isMoving() {
/* 57 */     return (mc.field_1724.field_3913.field_3905 != 0.0F || mc.field_1724.field_3913.field_3907 != 0.0F);
/*    */   }
/*    */   
/*    */   private int getArmorSlotIndex(class_1738 armor) {
/* 61 */     String itemName = armor.toString().toLowerCase();
/*    */     
/* 63 */     if (itemName.contains("helmet") || itemName.contains("skull"))
/* 64 */       return 3; 
/* 65 */     if (itemName.contains("chestplate") || itemName.contains("tunic"))
/* 66 */       return 2; 
/* 67 */     if (itemName.contains("leggings") || itemName.contains("pants"))
/* 68 */       return 1; 
/* 69 */     if (itemName.contains("boots") || itemName.contains("shoes")) {
/* 70 */       return 0;
/*    */     }
/*    */     
/* 73 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\player\AutoArmor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */