/*    */ package shame.astra.api.utils.rotate;
/*    */ import net.minecraft.class_1297;
/*    */ import net.minecraft.class_2350;
/*    */ import net.minecraft.class_238;
/*    */ import net.minecraft.class_239;
/*    */ import net.minecraft.class_241;
/*    */ import net.minecraft.class_243;
/*    */ import net.minecraft.class_3532;
/*    */ import net.minecraft.class_3959;
/*    */ import shame.astra.client.modules.impl.combat.components.gcd.GCDUtil;
/*    */ 
/*    */ public final class RotationUtils implements QClient {
/*    */   @Generated
/*    */   private RotationUtils() {
/* 15 */     throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
/*    */   } public static class_239 rayTrace(double dst, float yaw, float pitch) {
/* 17 */     class_243 vec3d = mc.field_1724.method_5836(1.0F);
/* 18 */     class_243 vec3d2 = getRotationVector(pitch, yaw);
/* 19 */     class_243 vec3d3 = vec3d.method_1031(vec3d2.field_1352 * dst, vec3d2.field_1351 * dst, vec3d2.field_1350 * dst);
/* 20 */     return (class_239)mc.field_1687.method_17742(new class_3959(vec3d, vec3d3, class_3959.class_3960.field_17559, class_3959.class_242.field_1348, (class_1297)mc.field_1724));
/*    */   }
/*    */   
/*    */   static class_243 getBestVector(class_1297 entity) {
/* 24 */     class_243 eyePos = mc.field_1724.method_33571();
/* 25 */     class_238 box = entity.method_5829();
/* 26 */     double step = 0.1D;
/* 27 */     class_243 bestVec = null;
/* 28 */     double closestDistance = Double.MAX_VALUE;
/*    */     double x;
/* 30 */     for (x = box.field_1323; x <= box.field_1320; x += step) {
/* 31 */       double y; for (y = box.field_1322; y <= box.field_1325; y += step) {
/* 32 */         double z; for (z = box.field_1321; z <= box.field_1324; z += step) {
/* 33 */           class_243 sample = new class_243(x, y, z);
/* 34 */           double dist = eyePos.method_1022(sample);
/* 35 */           if (dist < closestDistance) {
/* 36 */             closestDistance = dist;
/* 37 */             bestVec = sample;
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     } 
/* 42 */     return bestVec;
/*    */   }
/*    */   
/*    */   public static Rotation fromVec3d(class_243 vector) {
/* 46 */     return new Rotation((float)class_3532.method_15338(Math.toDegrees(Math.atan2(vector.field_1350, vector.field_1352)) - 90.0D), (float)class_3532.method_15338(Math.toDegrees(-Math.atan2(vector.field_1351, Math.hypot(vector.field_1352, vector.field_1350)))));
/*    */   }
/*    */   @NotNull
/*    */   public static class_243 getRotationVector(float yaw, float pitch) {
/* 50 */     return new class_243((class_3532.method_15374(-pitch * 0.017453292F) * class_3532.method_15362(yaw * 0.017453292F)), -class_3532.method_15374(yaw * 0.017453292F), (class_3532.method_15362(-pitch * 0.017453292F) * class_3532.method_15362(yaw * 0.017453292F)));
/*    */   }
/*    */   
/*    */   public static class_241 getRotations(class_1297 entity) {
/* 54 */     return getRotations(entity.method_23317(), entity.method_23318(), entity.method_23321());
/*    */   }
/*    */   
/*    */   public static class_241 getRotations(class_243 vec3d) {
/* 58 */     return getRotations(vec3d.field_1352, vec3d.field_1351, vec3d.field_1350);
/*    */   }
/*    */ 
/*    */   
/*    */   public static class_241 getRotations(double x, double y, double z) {
/* 63 */     double deltaX = x - mc.field_1724.method_23317();
/* 64 */     double deltaY = y - mc.field_1724.method_23320();
/* 65 */     double deltaZ = z - mc.field_1724.method_23321();
/* 66 */     double distance = class_3532.method_15355((float)(deltaX * deltaX + deltaZ * deltaZ));
/*    */     
/* 68 */     float yaw = (float)(class_3532.method_15349(deltaZ, deltaX) * 57.29577951308232D - 90.0D);
/* 69 */     float pitch = (float)(-class_3532.method_15349(deltaY, distance) * 57.29577951308232D);
/* 70 */     return new class_241(yaw, pitch);
/*    */   }
/*    */   
/*    */   public static float[] getRotations(class_2350 direction) {
/* 74 */     switch (direction) { default: throw new MatchException(null, null);
/* 75 */       case field_11033: (new float[2])[0] = mc.field_1724.method_36454(); (new float[2])[1] = 90.0F;
/* 76 */       case field_11036: (new float[2])[0] = mc.field_1724.method_36454(); (new float[2])[1] = -90.0F;
/* 77 */       case field_11043: (new float[2])[0] = 180.0F; (new float[2])[1] = mc.field_1724.method_36455();
/* 78 */       case field_11035: (new float[2])[0] = 0.0F; (new float[2])[1] = mc.field_1724.method_36455();
/* 79 */       case field_11039: (new float[2])[0] = 90.0F; (new float[2])[1] = mc.field_1724.method_36455();
/* 80 */       case field_11034: break; }  return new float[] { -90.0F, mc.field_1724.method_36455() };
/*    */   }
/*    */ 
/*    */   
/*    */   public static float[] correctRotation(float[] rotations) {
/* 85 */     rotations[0] = rotations[0] - rotations[0] % GCDUtil.getGCDValue();
/* 86 */     rotations[1] = rotations[1] - rotations[1] % GCDUtil.getGCDValue();
/* 87 */     return new float[] { rotations[0], rotations[1] };
/*    */   }
/*    */   
/*    */   public static float getFixRotate(float rot) {
/* 91 */     return getDeltaMouse(rot) * GCDUtil.getGCDValue();
/*    */   }
/*    */   
/*    */   public static float getDeltaMouse(float delta) {
/* 95 */     return Math.round(delta / GCDUtil.getGCDValue());
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\rotate\RotationUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */