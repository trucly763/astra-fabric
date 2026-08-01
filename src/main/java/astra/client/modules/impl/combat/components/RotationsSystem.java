/*    */ package shame.astra.client.modules.impl.combat.components;
/*    */ 
/*    */ import net.minecraft.class_1309;
/*    */ import net.minecraft.class_238;
/*    */ import net.minecraft.class_241;
/*    */ import net.minecraft.class_243;
/*    */ import shame.astra.api.QClient;
/*    */ import shame.astra.api.storages.implement.helpertstorages.enumvar.ModuleClass;
/*    */ import shame.astra.api.utils.combat.PredictUtils;
/*    */ import shame.astra.client.modules.impl.combat.components.gcd.GCDUtil;
/*    */ 
/*    */ public abstract class RotationsSystem
/*    */   implements QClient {
/* 14 */   public class_241 rotate = class_241.field_1340;
/*    */   
/*    */   public abstract void updateRotations(class_1309 paramclass_1309);
/*    */   
/*    */   public static class_241 correctRotation(float yaw, float pitch) {
/* 19 */     if ((yaw == -90.0F && pitch == 90.0F) || yaw == -180.0F) return new class_241(mc.field_1724.method_36454(), mc.field_1724.method_36455());
/*    */     
/* 21 */     float gcd = GCDUtil.getGCD();
/* 22 */     yaw -= yaw % gcd;
/* 23 */     pitch -= pitch % gcd;
/*    */     
/* 25 */     return new class_241(yaw, pitch);
/*    */   }
/*    */   
/*    */   protected boolean shouldUseElytraPredict(class_1309 target) {
/* 29 */     return (mc.field_1724 != null && target != null && mc.field_1724
/*    */       
/* 31 */       .method_6128() && target
/* 32 */       .method_6128() && ModuleClass.elytraTarget != null && ModuleClass.elytraTarget
/*    */       
/* 34 */       .isEnable());
/*    */   }
/*    */   
/*    */   protected int getElytraPredictTicks() {
/* 38 */     if (ModuleClass.elytraTarget == null) {
/* 39 */       return 0;
/*    */     }
/* 41 */     return Math.max(0, ModuleClass.elytraTarget.forward.getValue().intValue());
/*    */   }
/*    */   
/*    */   protected class_243 getPredictedPoint(class_1309 target, class_243 point) {
/* 45 */     if (!shouldUseElytraPredict(target)) {
/* 46 */       return point;
/*    */     }
/*    */     
/* 49 */     return PredictUtils.bypasselytrahacking(target);
/*    */   }
/*    */   
/*    */   protected class_238 getPredictedBox(class_1309 target) {
/* 53 */     class_238 box = target.method_5829();
/* 54 */     if (!shouldUseElytraPredict(target)) {
/* 55 */       return box;
/*    */     }
/* 57 */     class_243 currentCenter = box.method_1005();
/* 58 */     class_243 predictedCenter = getPredictedPoint(target, currentCenter);
/* 59 */     return box.method_997(predictedCenter.method_1020(currentCenter));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\combat\components\RotationsSystem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */