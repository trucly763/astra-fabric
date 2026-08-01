/*     */ package shame.astra.api.utils.combat;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.class_1297;
/*     */ import net.minecraft.class_1309;
/*     */ import net.minecraft.class_1675;
/*     */ import net.minecraft.class_238;
/*     */ import net.minecraft.class_239;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_3532;
/*     */ import net.minecraft.class_3959;
/*     */ import net.minecraft.class_3965;
/*     */ import net.minecraft.class_3966;
/*     */ import net.minecraft.class_746;
/*     */ import org.joml.Vector3f;
/*     */ import shame.astra.api.QClient;
/*     */ 
/*     */ public final class RayTraceUtil implements QClient {
/*     */   @Generated
/*     */   private RayTraceUtil() {
/*  20 */     throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class_239 rayTrace(double rayTraceDistance, float yaw, float pitch, class_1297 entity) {
/*  26 */     class_243 startVec = mc.field_1724.method_33571();
/*  27 */     class_243 directionVec = getVectorForRotation(pitch, yaw);
/*     */     
/*  29 */     class_243 endVec = startVec.method_1031(directionVec.field_1352 * rayTraceDistance, directionVec.field_1351 * rayTraceDistance, directionVec.field_1350 * rayTraceDistance);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  35 */     return (class_239)mc.field_1687.method_17742(new class_3959(startVec, endVec, class_3959.class_3960.field_17559, class_3959.class_242.field_1348, entity));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class_3965 raycast(class_243 start, class_243 end, class_3959.class_3960 shapeType) {
/*  45 */     return raycast(start, end, shapeType, (class_1297)mc.field_1724);
/*     */   }
/*     */   
/*     */   public static class_3965 raycast(class_243 start, class_243 end, class_3959.class_3960 shapeType, class_1297 entity) {
/*  49 */     return mc.field_1687.method_17742(new class_3959(start, end, shapeType, class_3959.class_242.field_1348, entity));
/*     */   }
/*     */   
/*     */   public static boolean rayTrace(class_243 clientVec, double range, class_238 box) {
/*  53 */     class_243 cameraVec = ((class_746)Objects.<class_746>requireNonNull(mc.field_1724)).method_33571();
/*  54 */     return (box.method_1006(cameraVec) || box.method_992(cameraVec, cameraVec.method_1019(clientVec.method_1021(range))).isPresent());
/*     */   }
/*     */   
/*     */   public static boolean isViewEntity(class_1309 target, float yaw, float pitch, float distance, boolean ignoreWalls) {
/*  58 */     class_1297 entity = mc.method_1560();
/*     */     
/*  60 */     if (entity == null || mc.field_1687 == null)
/*     */     {
/*  62 */       return false;
/*     */     }
/*  64 */     double reachDistanceSquared = (distance * distance);
/*     */     
/*  66 */     class_243 startVec = entity.method_33571();
/*  67 */     Vector3f directionVec = calculateViewVector(yaw, pitch);
/*  68 */     directionVec.mul(distance, distance, distance);
/*  69 */     class_243 endVec = startVec.method_1031(directionVec.x, directionVec.y, directionVec.z);
/*  70 */     class_238 aabb = target.method_5829();
/*     */     
/*  72 */     class_3966 result = class_1675.method_18075(entity, startVec, endVec, aabb, entityIn -> 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  77 */         (!entityIn.method_7325() && entityIn.method_5805() && entityIn == target), reachDistanceSquared);
/*     */ 
/*     */ 
/*     */     
/*  81 */     return (result != null);
/*     */   }
/*     */   
/*     */   public static Vector3f calculateViewVector(float yaw, float pitch) {
/*  85 */     float pitchRad = pitch * 0.017453292F;
/*  86 */     float yawRad = -yaw * 0.017453292F;
/*  87 */     float cosYaw = class_3532.method_15362(yawRad);
/*  88 */     float sinYaw = class_3532.method_15374(yawRad);
/*  89 */     float cosPitch = class_3532.method_15362(pitchRad);
/*  90 */     float sinPitch = class_3532.method_15374(pitchRad);
/*     */     
/*  92 */     return new Vector3f(sinYaw * cosPitch, -sinPitch, cosYaw * cosPitch);
/*     */   }
/*     */   
/*     */   public static class_243 getVectorForRotation(float pitch, float yaw) {
/*  96 */     float yawRadians = -yaw * 0.017453292F - 3.1415927F;
/*  97 */     float pitchRadians = -pitch * 0.017453292F;
/*     */     
/*  99 */     float cosYaw = class_3532.method_15362(yawRadians);
/* 100 */     float sinYaw = class_3532.method_15374(yawRadians);
/* 101 */     float cosPitch = -class_3532.method_15362(pitchRadians);
/* 102 */     float sinPitch = class_3532.method_15374(pitchRadians);
/*     */     
/* 104 */     return new class_243((sinYaw * cosPitch), sinPitch, (cosYaw * cosPitch));
/*     */   }
/*     */   
/*     */   public static boolean rayTraceSingleEntity(float yaw, float pitch, double distance, class_1297 entity) {
/* 108 */     class_243 eyeVec = mc.field_1724.method_33571();
/* 109 */     class_243 lookVec = mc.field_1724.method_5631(pitch, yaw);
/* 110 */     class_243 extendedVec = eyeVec.method_1019(lookVec.method_1021(distance));
/*     */     
/* 112 */     class_238 AABB = entity.method_5829();
/*     */     
/* 114 */     return (AABB.method_1006(eyeVec) || AABB.method_992(eyeVec, extendedVec).isPresent());
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\combat\RayTraceUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */