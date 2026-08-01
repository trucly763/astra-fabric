/*    */ package shame.astra.client.modules.impl.combat.components.rotations;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.concurrent.ThreadLocalRandom;
/*    */ import net.minecraft.class_1309;
/*    */ import net.minecraft.class_238;
/*    */ import net.minecraft.class_241;
/*    */ import net.minecraft.class_243;
/*    */ import net.minecraft.class_3532;
/*    */ import shame.astra.api.QClient;
/*    */ import shame.astra.api.storages.implement.RotationStorage;
/*    */ import shame.astra.api.utils.rotate.Rotation;
/*    */ import shame.astra.api.utils.rotate.RotationUtils;
/*    */ import shame.astra.client.modules.impl.combat.Aura;
/*    */ import shame.astra.client.modules.impl.combat.components.RotationsSystem;
/*    */ 
/*    */ public class LegitRotation
/*    */   extends RotationsSystem
/*    */   implements QClient
/*    */ {
/*    */   public void updateRotations(class_1309 target) {
/* 22 */     class_243 eyePos = mc.field_1724.method_5836(1.0F);
/* 23 */     class_243 lookVec = mc.field_1724.method_5828(1.0F);
/* 24 */     class_243 reachVec = eyePos.method_1019(lookVec.method_1021(999.0D));
/*    */     
/* 26 */     class_238 box = getPredictedBox(target);
/*    */     
/* 28 */     double shrinkXZ = target.method_6128() ? -0.5D : 0.10000000149011612D;
/* 29 */     double shrinkY = target.method_6128() ? -0.5D : 0.10000000149011612D;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 37 */     box = new class_238(box.field_1323 + box.method_17939() * shrinkXZ / 2.0D, box.field_1322, box.field_1321 + box.method_17941() * shrinkXZ / 2.0D, box.field_1320 - box.method_17939() * shrinkXZ / 2.0D, box.field_1325 - box.method_17940() * shrinkY, box.field_1324 - box.method_17941() * shrinkXZ / 2.0D);
/*    */ 
/*    */     
/* 40 */     Optional<class_243> hit = box.method_992(eyePos, reachVec);
/* 41 */     boolean inside = box.method_1006(eyePos);
/*    */     
/* 43 */     if (hit.isPresent() || inside) {
/* 44 */       Aura.adjYaw = class_3532.method_15363(Aura.adjYaw - ThreadLocalRandom.current().nextFloat(0.005F, 0.02F), 0.0F, 1.0F);
/* 45 */       Aura.adjPitch = class_3532.method_15363(Aura.adjPitch - ThreadLocalRandom.current().nextFloat(0.005F, 0.02F), 0.0F, 1.0F);
/*    */     }
/* 47 */     else if (mc.field_1724.method_6128()) {
/* 48 */       Aura.adjYaw = class_3532.method_15363(Aura.adjYaw + ThreadLocalRandom.current().nextFloat(5.0E-4F, 0.005F), 0.0F, 1.0F);
/* 49 */       Aura.adjPitch = class_3532.method_15363(Aura.adjPitch + ThreadLocalRandom.current().nextFloat(9.0E-4F, 0.009F), 0.0F, 1.0F);
/*    */     }
/* 51 */     else if (target.method_20232()) {
/* 52 */       Aura.adjYaw = class_3532.method_15363(Aura.adjYaw + ThreadLocalRandom.current().nextFloat(9.0E-5F, 0.009F), 0.0F, 1.0F);
/* 53 */       Aura.adjPitch = class_3532.method_15363(Aura.adjPitch + ThreadLocalRandom.current().nextFloat(9.0E-5F, 9.0E-4F), 0.0F, 1.0F);
/*    */     } else {
/* 55 */       Aura.adjYaw = class_3532.method_15363(Aura.adjYaw + ThreadLocalRandom.current().nextFloat(9.0E-5F, 0.009F), 0.0F, 1.0F);
/* 56 */       Aura.adjPitch = class_3532.method_15363(Aura.adjPitch + ThreadLocalRandom.current().nextFloat(9.0E-4F, 0.009F), 0.0F, 1.0F);
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 62 */     class_241 targetRot = RotationUtils.getRotations(getPredictedPoint(target, target.method_30951(1.0F)));
/*    */     
/* 64 */     float currentYaw = mc.field_1724.method_36454();
/* 65 */     float currentPitch = mc.field_1724.method_36455();
/*    */     
/* 67 */     float diffYaw = class_3532.method_15393(targetRot.field_1343 - currentYaw);
/* 68 */     float diffPitch = class_3532.method_15393(targetRot.field_1342 - currentPitch);
/*    */     
/* 70 */     float newYaw = currentYaw + diffYaw * Aura.adjYaw;
/* 71 */     float newPitch = currentPitch + diffPitch * Aura.adjPitch;
/*    */     
/* 73 */     Aura.otvodkaYaw = 0.0F;
/* 74 */     Aura.otvodkaPitch = 0.0F;
/* 75 */     RotationStorage.update(new Rotation(newYaw, newPitch), 360.0F, 360.0F, 40.0F, 35.0F, 1, 1, Aura.clientLook
/*    */ 
/*    */ 
/*    */         
/* 79 */         .isState());
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\combat\components\rotations\LegitRotation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */