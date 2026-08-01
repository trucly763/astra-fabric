/*    */ package shame.astra.client.modules.impl.player;
/*    */ import net.minecraft.class_239;
/*    */ import net.minecraft.class_2596;
/*    */ import net.minecraft.class_2680;
/*    */ import net.minecraft.class_2868;
/*    */ import net.minecraft.class_3965;
/*    */ import shame.astra.api.events.EventLink;
/*    */ import shame.astra.api.events.implement.EventUpdate;
/*    */ import shame.astra.client.modules.Module;
/*    */ import shame.astra.client.modules.settings.Setting;
/*    */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*    */ 
/*    */ public class AutoTool extends Module {
/* 14 */   public static AutoTool INSTANCE = new AutoTool();
/*    */   
/* 16 */   private final BooleanSetting packet = new BooleanSetting("Пакетный", false);
/* 17 */   private final BooleanSetting silent = new BooleanSetting("Видно только для других людей", false);
/*    */   
/* 19 */   private int previousSlot = -1;
/*    */   
/*    */   public AutoTool() {
/* 22 */     super("AutoTool", "При копании берет лучший предмет", Module.ModuleCategory.PLAYER);
/* 23 */     addSettings(new Setting[] { (Setting)this.packet, (Setting)this.silent });
/*    */   }
/*    */   
/*    */   @EventLink
/*    */   public void onEvent(EventUpdate event) {
/* 28 */     if (mc.field_1724 == null || mc.field_1687 == null || mc.field_1761 == null || mc.field_1724.method_7337()) {
/* 29 */       this.previousSlot = -1;
/*    */       
/*    */       return;
/*    */     } 
/* 33 */     if (mc.field_1761.method_2923()) {
/* 34 */       if (this.previousSlot == -1) {
/* 35 */         this.previousSlot = (mc.field_1724.method_31548()).field_7545;
/*    */       }
/*    */       
/* 38 */       int toolSlot = findOptimalTool();
/* 39 */       if (toolSlot != -1) {
/* 40 */         switchToSlot(toolSlot);
/*    */       }
/* 42 */     } else if (this.previousSlot != -1) {
/* 43 */       switchToSlot(this.previousSlot);
/* 44 */       this.previousSlot = -1;
/*    */     } 
/*    */   }
/*    */   
/*    */   private void switchToSlot(int slot) {
/* 49 */     if (slot < 0 || slot > 8)
/* 50 */       return;  if ((mc.field_1724.method_31548()).field_7545 == slot)
/*    */       return; 
/* 52 */     if (this.silent.isState()) {
/* 53 */       mc.method_1562().method_52787((class_2596)new class_2868(slot));
/* 54 */     } else if (this.packet.isState()) {
/* 55 */       (mc.field_1724.method_31548()).field_7545 = slot;
/* 56 */       mc.method_1562().method_52787((class_2596)new class_2868(slot));
/*    */     } else {
/* 58 */       (mc.field_1724.method_31548()).field_7545 = slot;
/*    */     } 
/*    */   }
/*    */   private int findOptimalTool() {
/*    */     class_3965 blockHitResult;
/* 63 */     class_239 hitResult = mc.field_1765;
/*    */     
/* 65 */     if (hitResult instanceof class_3965) { blockHitResult = (class_3965)hitResult; }
/* 66 */     else { return -1; }
/*    */ 
/*    */     
/* 69 */     class_2680 blockState = mc.field_1687.method_8320(blockHitResult.method_17777());
/* 70 */     return findBestToolSlot(blockState);
/*    */   }
/*    */   
/*    */   private int findBestToolSlot(class_2680 blockState) {
/* 74 */     int bestSlot = -1;
/* 75 */     float bestSpeed = 1.0F;
/*    */     
/* 77 */     for (int i = 0; i < 9; i++) {
/* 78 */       float speed = mc.field_1724.method_31548().method_5438(i).method_7924(blockState);
/*    */       
/* 80 */       if (speed > bestSpeed) {
/* 81 */         bestSpeed = speed;
/* 82 */         bestSlot = i;
/*    */       } 
/*    */     } 
/*    */     
/* 86 */     return bestSlot;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 91 */     this.previousSlot = -1;
/* 92 */     super.onDisable();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\player\AutoTool.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */