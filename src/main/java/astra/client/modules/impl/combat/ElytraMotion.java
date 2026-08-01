/*    */ package shame.astra.client.modules.impl.combat;
/*    */ import net.minecraft.class_1297;
/*    */ import net.minecraft.class_243;
/*    */ import shame.astra.api.events.EventLink;
/*    */ import shame.astra.api.events.implement.EventMove;
/*    */ import shame.astra.api.storages.implement.helpertstorages.enumvar.ModuleClass;
/*    */ import shame.astra.client.modules.Module;
/*    */ import shame.astra.client.modules.settings.Setting;
/*    */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*    */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*    */ 
/*    */ public class ElytraMotion extends Module {
/* 13 */   public static ElytraMotion INSTANCE = new ElytraMotion();
/*    */   
/* 15 */   public FloatSetting distance = new FloatSetting("Дистанция до игрока", 3.0F, 0.0F, 6.0F, 0.1F);
/* 16 */   public BooleanSetting bypass = new BooleanSetting("Обход", false);
/*    */   public ElytraMotion() {
/* 18 */     super("ElytraMotion", "Зависает рядом с игроком на эликах", Module.ModuleCategory.COMBAT);
/* 19 */     addSettings(new Setting[] { (Setting)this.distance, (Setting)this.bypass });
/*    */   }
/*    */   @EventLink
/*    */   public void onMove(EventMove e) {
/* 23 */     if (!isEnable())
/*    */       return; 
/* 25 */     Aura aura = ModuleClass.aura;
/* 26 */     if (mc.field_1724 == null || mc.field_1687 == null || aura.getTarget() == null)
/* 27 */       return;  if (mc.field_1724.method_6128() && mc.field_1724.method_5739((class_1297)aura.getTarget()) < this.distance.getValue().floatValue())
/* 28 */       if (this.bypass.isState()) {
/* 29 */         float yaw = mc.field_1724.method_36454();
/* 30 */         double rad = Math.toRadians(yaw);
/*    */         
/* 32 */         double forward = 0.01D;
/* 33 */         double down = -1.0E-4D;
/*    */         
/* 35 */         double moveX = -Math.sin(rad) * forward;
/* 36 */         double moveZ = Math.cos(rad) * forward;
/*    */         
/* 38 */         e.setMovePos(new class_243(moveX, down, moveZ));
/*    */       } else {
/* 40 */         e.setMovePos(class_243.field_1353);
/*    */       }  
/*    */   }
/*    */   
/*    */   public void onDisable() {}
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\combat\ElytraMotion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */