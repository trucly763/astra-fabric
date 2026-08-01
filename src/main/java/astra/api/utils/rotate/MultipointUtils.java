/*    */ package shame.astra.api.utils.rotate;
/*    */ import net.minecraft.class_1297;
/*    */ import net.minecraft.class_238;
/*    */ import net.minecraft.class_243;
/*    */ import shame.astra.api.QClient;
/*    */ 
/*    */ public final class MultipointUtils implements QClient {
/*    */   @Generated
/*    */   private MultipointUtils() {
/* 10 */     throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
/*    */   } public static class_243 getClosestPoint(class_1297 entity) {
/* 12 */     class_243 eyePos = mc.field_1724.method_33571();
/* 13 */     class_238 box = entity.method_5829();
/* 14 */     double step = 0.1D;
/* 15 */     class_243 bestVec = null;
/* 16 */     double closestDistance = Double.MAX_VALUE;
/*    */     double x;
/* 18 */     for (x = box.field_1323; x <= box.field_1320; x += step) {
/* 19 */       double y; for (y = box.field_1322; y <= box.field_1325; y += step) {
/* 20 */         double z; for (z = box.field_1321; z <= box.field_1324; z += step) {
/* 21 */           class_243 sample = new class_243(x, y, z);
/* 22 */           double dist = eyePos.method_1022(sample);
/* 23 */           if (dist < closestDistance) {
/* 24 */             closestDistance = dist;
/* 25 */             bestVec = sample;
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     } 
/* 30 */     return bestVec;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\rotate\MultipointUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */