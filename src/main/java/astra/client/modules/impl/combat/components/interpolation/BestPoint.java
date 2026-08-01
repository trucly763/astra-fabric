/*     */ package shame.astra.client.modules.impl.combat.components.interpolation;
/*     */ import net.minecraft.class_1297;
/*     */ import net.minecraft.class_238;
/*     */ import net.minecraft.class_239;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_3959;
/*     */ import net.minecraft.class_3965;
/*     */ import shame.astra.api.QClient;
/*     */ import shame.astra.api.utils.combat.RayTraceUtil;
/*     */ import shame.astra.api.utils.math.MathUtils;
/*     */ import shame.astra.api.utils.rotate.Rotation;
/*     */ 
/*     */ public final class BestPoint implements QClient {
/*     */   @Generated
/*     */   private BestPoint() {
/*  16 */     throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
/*  17 */   } private static class_243 rotationPoint = class_243.field_1353;
/*  18 */   private static class_243 rotationMotion = class_243.field_1353;
/*     */   
/*     */   public static class_243 getRotationPoint() {
/*  21 */     return rotationPoint;
/*     */   }
/*     */   
/*     */   public static class_243 getNearestPoint(class_1297 entity) {
/*  25 */     class_238 box = entity.method_5829();
/*  26 */     double step = 0.1D;
/*  27 */     class_243 bestVec = null;
/*  28 */     double closestDistance = Double.MAX_VALUE;
/*     */     double x;
/*  30 */     for (x = box.field_1323; x <= box.field_1320; x += step) {
/*  31 */       double y; for (y = box.field_1322; y <= box.field_1325; y += step) {
/*  32 */         double z; for (z = box.field_1321; z <= box.field_1324; z += step) {
/*  33 */           class_243 sample = new class_243(x, y, z);
/*  34 */           double dist = mc.field_1724.method_33571().method_1022(sample);
/*  35 */           if (dist < closestDistance) {
/*  36 */             closestDistance = dist;
/*  37 */             bestVec = sample;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*  42 */     return bestVec;
/*     */   }
/*     */   public static class_243 getPoint(class_1297 target) {
/*  45 */     class_238 box = target.method_5829();
/*     */     
/*  47 */     double width = box.field_1320 - box.field_1323;
/*  48 */     double height = box.field_1325 - box.field_1322;
/*  49 */     double depth = box.field_1324 - box.field_1321;
/*     */     
/*  51 */     double baseX = box.field_1323 + width / 2.0D;
/*  52 */     double baseY = box.field_1322 + height * 0.7D;
/*  53 */     double baseZ = box.field_1321 + depth / 2.0D;
/*     */     
/*  55 */     double time = System.currentTimeMillis() / 50.0D;
/*     */     
/*  57 */     int id = target.method_5628();
/*     */ 
/*     */     
/*  60 */     double offsetX = Math.sin(time + id) * width * 0.45D;
/*     */     
/*  62 */     double offsetY = Math.cos(time * 0.8D + id) * height * 0.1D;
/*     */     
/*  64 */     double offsetZ = Math.cos(time * 1.2D + id) * depth * 0.45D;
/*     */     
/*  66 */     return new class_243(baseX + offsetX, baseY + offsetY, baseZ + offsetZ);
/*     */   }
/*     */   public static class_243 getPoint2(class_1297 target) {
/*  69 */     class_238 box = target.method_5829();
/*     */     
/*  71 */     double width = box.field_1320 - box.field_1323;
/*  72 */     double height = box.field_1325 - box.field_1322;
/*  73 */     double depth = box.field_1324 - box.field_1321;
/*     */     
/*  75 */     double baseX = box.field_1323 + width / 2.0D;
/*  76 */     double baseY = box.field_1322 + height * 0.65D;
/*  77 */     double baseZ = box.field_1321 + depth / 2.0D;
/*     */     
/*  79 */     double time = System.currentTimeMillis() / 65.0D;
/*     */     
/*  81 */     int id = target.method_5628();
/*     */ 
/*     */     
/*  84 */     double offsetX = Math.sin(time + id) * width * 0.7D;
/*     */     
/*  86 */     double offsetY = Math.cos(time * 0.8D + id) * height * 0.4D;
/*     */     
/*  88 */     double offsetZ = Math.cos(time * 1.2D + id) * depth * 0.7D;
/*     */     
/*  90 */     return new class_243(baseX + offsetX, baseY + offsetY, baseZ + offsetZ);
/*     */   }
/*     */   
/*     */   public static class_243 getNearestVisiblePoint(class_1297 target, class_243 preferredPoint, double range) {
/*  94 */     if (preferredPoint == null || mc.field_1724 == null || mc.field_1687 == null) {
/*  95 */       return preferredPoint;
/*     */     }
/*     */     
/*  98 */     if (isPointVisible(target, preferredPoint, range)) {
/*  99 */       return preferredPoint;
/*     */     }
/*     */     
/* 102 */     class_238 box = target.method_5829();
/* 103 */     double step = 0.12D;
/* 104 */     class_243 bestPoint = null;
/* 105 */     double bestDistance = Double.MAX_VALUE;
/*     */     double x;
/* 107 */     for (x = box.field_1323; x <= box.field_1320; x += step) {
/* 108 */       double y; for (y = box.field_1322; y <= box.field_1325; y += step) {
/* 109 */         double z; for (z = box.field_1321; z <= box.field_1324; z += step) {
/* 110 */           class_243 sample = new class_243(x, y, z);
/* 111 */           if (isPointVisible(target, sample, range)) {
/*     */ 
/*     */ 
/*     */             
/* 115 */             double distanceToCurrent = sample.method_1025(preferredPoint);
/* 116 */             if (distanceToCurrent < bestDistance) {
/* 117 */               bestDistance = distanceToCurrent;
/* 118 */               bestPoint = sample;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 124 */     return (bestPoint != null) ? bestPoint : preferredPoint;
/*     */   }
/*     */   
/*     */   private static boolean isPointVisible(class_1297 target, class_243 point, double range) {
/* 128 */     class_243 eyePos = mc.field_1724.method_33571();
/* 129 */     double distance = eyePos.method_1022(point);
/* 130 */     if (distance > range) {
/* 131 */       return false;
/*     */     }
/*     */     
/* 134 */     class_243 direction = point.method_1020(eyePos).method_1029();
/* 135 */     if (!RayTraceUtil.rayTrace(direction, distance + 0.2D, target.method_5829())) {
/* 136 */       return false;
/*     */     }
/*     */     
/* 139 */     class_3965 blockHit = RayTraceUtil.raycast(eyePos, point, class_3959.class_3960.field_17558, (class_1297)mc.field_1724);
/* 140 */     return (blockHit.method_17783() == class_239.class_240.field_1333 || eyePos.method_1025(blockHit.method_17784()) >= eyePos.method_1025(point) - 1.0E-4D);
/*     */   }
/*     */   
/*     */   public static class_243 getMultipoint(class_1297 target, double distance) {
/* 144 */     float minMotionXZ = 0.005F;
/* 145 */     float maxMotionXZ = 0.015F;
/*     */     
/* 147 */     float minMotionY = 0.0015F;
/* 148 */     float maxMotionY = 0.015F;
/*     */     
/* 150 */     double lenghtX = target.method_5829().method_17939();
/* 151 */     double lenghtY = target.method_5829().method_17940();
/* 152 */     double lenghtZ = target.method_5829().method_17941();
/*     */     
/* 154 */     if (rotationMotion.equals(class_243.field_1353)) {
/* 155 */       rotationMotion = new class_243(MathUtils.randomBest(-0.019999999552965164D, 0.019999999552965164D), MathUtils.randomBest(-0.019999999552965164D, 0.019999999552965164D), MathUtils.randomBest(-0.019999999552965164D, 0.019999999552965164D));
/*     */     }
/* 157 */     if (rotationPoint.equals(class_243.field_1353)) {
/* 158 */       rotationPoint = new class_243(0.0D, lenghtY * 0.5D, 0.0D);
/*     */     }
/* 160 */     rotationPoint = rotationPoint.method_1019(rotationMotion);
/*     */     
/* 162 */     double safeX = (lenghtX - 0.1D) / 2.0D;
/* 163 */     double safeZ = (lenghtZ - 0.1D) / 2.0D;
/*     */     
/* 165 */     if (rotationPoint.field_1352 >= safeX) {
/* 166 */       rotationMotion = new class_243(-MathUtils.randomBest(minMotionXZ, maxMotionXZ), rotationMotion.method_10214(), rotationMotion.method_10215());
/* 167 */     } else if (rotationPoint.field_1352 <= -safeX) {
/* 168 */       rotationMotion = new class_243(MathUtils.randomBest(minMotionXZ, maxMotionXZ), rotationMotion.method_10214(), rotationMotion.method_10215());
/*     */     } 
/* 170 */     if (rotationPoint.field_1351 >= lenghtY * 0.75D) {
/* 171 */       rotationMotion = new class_243(rotationMotion.method_10216(), -MathUtils.randomBest(minMotionY, maxMotionY), rotationMotion.method_10215());
/* 172 */     } else if (rotationPoint.field_1351 <= lenghtY * 0.3D) {
/* 173 */       rotationMotion = new class_243(rotationMotion.method_10216(), MathUtils.randomBest(minMotionY, maxMotionY), rotationMotion.method_10215());
/*     */     } 
/* 175 */     if (rotationPoint.field_1350 >= safeZ) {
/* 176 */       rotationMotion = new class_243(rotationMotion.method_10216(), rotationMotion.method_10214(), -MathUtils.randomBest(minMotionXZ, maxMotionXZ));
/* 177 */     } else if (rotationPoint.field_1350 <= -safeZ) {
/* 178 */       rotationMotion = new class_243(rotationMotion.method_10216(), rotationMotion.method_10214(), MathUtils.randomBest(minMotionXZ, maxMotionXZ));
/*     */     } 
/* 180 */     rotationPoint.method_1031(MathUtils.randomBest(-0.05000000074505806D, 0.05000000074505806D), 0.0D, MathUtils.randomBest(-0.05000000074505806D, 0.05000000074505806D));
/*     */ 
/*     */ 
/*     */     
/* 184 */     if (!RayTraceUtil.rayTrace(mc.field_1724.method_5720(), distance, target.method_5829())) {
/* 185 */       float halfBox = (float)(lenghtX / 2.0D) * 0.8F;
/*     */       
/*     */       float x1;
/* 188 */       for (x1 = -halfBox; x1 <= halfBox; x1 += 0.1F) {
/* 189 */         float z1; for (z1 = -halfBox; z1 <= halfBox; z1 += 0.1F) {
/* 190 */           float y1; for (y1 = (float)(lenghtY * 0.9D); y1 >= lenghtY * 0.3D; y1 -= 0.1F) {
/*     */             
/* 192 */             class_243 v1 = new class_243(target.method_23317() + x1, target.method_23318() + y1, target.method_23321() + z1);
/*     */             
/* 194 */             Rotation rotation = RotationUtils.fromVec3d(v1);
/* 195 */             if (RayTraceUtil.rayTrace(rotation.toVector(), distance, target.method_5829())) {
/* 196 */               rotationPoint = new class_243(x1, y1, z1);
/* 197 */               return target.method_19538().method_1019(rotationPoint);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 204 */     return target.method_19538().method_1019(rotationPoint);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\combat\components\interpolation\BestPoint.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */