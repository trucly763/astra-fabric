/*    */ package shame.astra.client.modules.impl.combat.components.rotations;
/*    */ 
/*    */ import net.minecraft.class_1309;
/*    */ import net.minecraft.class_241;
/*    */ import net.minecraft.class_243;
/*    */ import net.minecraft.class_3532;
/*    */ import shame.astra.api.QClient;
/*    */ import shame.astra.client.modules.impl.combat.components.RotationsSystem;
/*    */ import shame.astra.client.modules.impl.combat.components.gcd.GCDUtil;
/*    */ 
/*    */ public class PredictRots
/*    */   extends RotationsSystem
/*    */   implements QClient
/*    */ {
/*    */   public class_241 rotating(class_241 rotation, class_1309 target) {
/* 16 */     class_243 vec = calcPointed(target);
/* 17 */     float rawYaw = (float)class_3532.method_15338(Math.toDegrees(Math.atan2(vec.field_1350, vec.field_1352)) - 90.0D);
/* 18 */     float rawPitch = (float)class_3532.method_15338(Math.toDegrees(-Math.atan2(vec.field_1351, Math.hypot(vec.field_1352, vec.field_1350))));
/* 19 */     float yawDelta = class_3532.method_15393(rawYaw - rotation.field_1343);
/* 20 */     float pitchDelta = class_3532.method_15393(rawPitch - rotation.field_1342);
/* 21 */     if (Math.abs(yawDelta) > 180.0F) {
/* 22 */       yawDelta -= Math.signum(yawDelta) * 360.0F;
/*    */     }
/*    */     
/* 25 */     float additionYaw = class_3532.method_15363(yawDelta, -180.0F, 180.0F);
/* 26 */     float additionPitch = class_3532.method_15363(pitchDelta, -90.0F, 90.0F);
/* 27 */     float yaw = rotation.field_1343 + additionYaw;
/* 28 */     float pitch = rotation.field_1342 + additionPitch;
/*    */     
/* 30 */     float yawFinal = GCDUtil.getFixedRotation(yaw);
/* 31 */     float pitchFinal = GCDUtil.getFixedRotation(pitch);
/*    */     
/* 33 */     return new class_241(yawFinal, pitchFinal);
/*    */   }
/*    */ 
/*    */   
/*    */   private class_243 calcPointed(class_1309 target) {
/* 38 */     if (target != null) {
/* 39 */       class_243 vecPosition = getPredictedPoint(target, target.method_5829().method_1005());
/*    */       
/* 41 */       return new class_243(vecPosition.method_10216() - mc.field_1724.method_23317(), vecPosition.method_10214() - mc.field_1724.method_23318(), vecPosition.method_10215() - mc.field_1724.method_23321());
/*    */     } 
/* 43 */     return class_243.field_1353;
/*    */   }
/*    */   
/*    */   public void updateRotations(class_1309 entity) {}
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\combat\components\rotations\PredictRots.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */