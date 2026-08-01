/*    */ package shame.astra.client.modules.impl.movement;
/*    */ 
/*    */ import net.minecraft.class_1309;
/*    */ import net.minecraft.class_238;
/*    */ import net.minecraft.class_243;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ import shame.astra.api.QClient;
/*    */ import shame.astra.api.events.EventLink;
/*    */ import shame.astra.api.events.implement.EventUpdate;
/*    */ import shame.astra.api.storages.implement.helpertstorages.enumvar.ModuleClass;
/*    */ import shame.astra.api.utils.combat.PredictUtils;
/*    */ import shame.astra.client.modules.Module;
/*    */ import shame.astra.client.modules.impl.combat.Aura;
/*    */ import shame.astra.client.modules.settings.Setting;
/*    */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*    */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*    */ 
/*    */ public class Speed
/*    */   extends Module implements QClient {
/* 20 */   public static Speed INSTANCE = new Speed();
/*    */   
/* 22 */   private final FloatSetting speed = new FloatSetting("Скорость", 1.0F, 0.1F, 2.0F, 0.01F);
/* 23 */   private final FloatSetting radius = new FloatSetting("Радиус", 1.0F, 0.01F, 3.0F, 0.1F);
/* 24 */   private final FloatSetting predict = new FloatSetting("Предикт", 1.0F, 0.0F, 5.0F, 0.1F);
/* 25 */   private final BooleanSetting onlyElytra = new BooleanSetting("Только на элитре", false);
/*    */   
/*    */   public Speed() {
/* 28 */     super("Speed", "Дополнительное ускорение", Module.ModuleCategory.MOVEMENT);
/* 29 */     addSettings(new Setting[] { (Setting)this.speed, (Setting)this.radius, (Setting)this.predict, (Setting)this.onlyElytra });
/*    */   }
/*    */   
/*    */   @EventLink
/*    */   private void onUpdate(EventUpdate event) {
/* 34 */     if (mc.field_1724 == null || mc.field_1687 == null)
/*    */       return; 
/* 36 */     collisionSpeed();
/*    */   }
/*    */   
/*    */   private void collisionSpeed() {
/* 40 */     Aura aura = ModuleClass.aura;
/* 41 */     if (aura == null || !aura.isEnable())
/*    */       return; 
/* 43 */     class_1309 target = aura.getTarget();
/* 44 */     if (target == null || target == mc.field_1724)
/*    */       return; 
/* 46 */     if (this.onlyElytra.isState() && !mc.field_1724.method_6128())
/*    */       return; 
/* 48 */     class_238 expandedBox = mc.field_1724.method_5829().method_1014(this.radius.getValue().doubleValue());
/*    */     
/* 50 */     boolean canSpeed = false;
/*    */     
/* 52 */     if (mc.field_1724.method_6128() || target.method_5829().method_994(expandedBox)) {
/* 53 */       if (mc.field_1724.method_6128()) {
/* 54 */         class_243 predictedPos = PredictUtils.predict(target, target.method_19538(), this.predict.getValue().intValue());
/* 55 */         double distanceToPredict = mc.field_1724.method_33571().method_1022(predictedPos);
/* 56 */         double distanceToTarget = mc.field_1724.method_33571().method_1022(target.method_5829().method_1005());
/*    */         
/* 58 */         if (distanceToPredict <= 2.5D || distanceToTarget <= 2.5D) {
/* 59 */           canSpeed = true;
/*    */         }
/*    */       } else {
/* 62 */         canSpeed = true;
/*    */       } 
/*    */     }
/*    */     
/* 66 */     if (canSpeed) {
/* 67 */       class_243 newVelocity = calculateVelocity(target);
/* 68 */       mc.field_1724.method_18799(newVelocity);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   private class_243 calculateVelocity(class_1309 target) {
/* 78 */     class_243 predictedPos = PredictUtils.predict(target, target.method_19538(), this.predict.getValue().intValue());
/* 79 */     double deltaX = predictedPos.field_1352 - mc.field_1724.method_23317();
/* 80 */     double deltaZ = predictedPos.field_1350 - mc.field_1724.method_23321();
/*    */     
/* 82 */     float targetYaw = (float)(Math.toDegrees(Math.atan2(deltaZ, deltaX)) - 90.0D);
/* 83 */     double radYaw = Math.toRadians(targetYaw);
/*    */     
/* 85 */     double force = 0.072D * this.speed.getValue().doubleValue();
/*    */     
/* 87 */     class_243 currentVelocity = mc.field_1724.method_18798();
/*    */     
/* 89 */     return new class_243(currentVelocity.field_1352 + 
/* 90 */         -Math.sin(radYaw) * force, currentVelocity.field_1351, currentVelocity.field_1350 + 
/*    */         
/* 92 */         Math.cos(radYaw) * force);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\movement\Speed.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */