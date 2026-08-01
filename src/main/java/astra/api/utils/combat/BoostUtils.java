/*    */ package shame.astra.api.utils.combat;
/*    */ import net.minecraft.class_3532;
/*    */ import shame.astra.api.QClient;
/*    */ 
/*    */ public final class BoostUtils implements QClient {
/*    */   @Generated
/*    */   private BoostUtils() {
/*  8 */     throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
/*    */   }
/*    */   
/*    */   public static double getBoost() {
/* 12 */     int[] vectors = { -45, 45, 135, -135 };
/* 13 */     int[] addVectors = { -90, 90, 180, -180, 0 };
/* 14 */     int[] pitchVectors = { -45, 45 };
/*    */     
/* 16 */     float lastYaw = mc.field_1724.field_5982;
/* 17 */     float lastPitch = mc.field_1724.field_6004;
/* 18 */     int minDist = findClosestVector(lastYaw, vectors);
/* 19 */     float maxDist = Math.abs(class_3532.method_15393(lastYaw) - vectors[minDist]);
/* 20 */     int addMinDist = findClosestVector(lastYaw, addVectors);
/* 21 */     float addMaxDist = Math.abs(class_3532.method_15393(lastYaw) - addVectors[addMinDist]);
/* 22 */     float countableSpeed = (minDist == -1) ? 1.5F : (1.95F - maxDist * 0.56F / 45.0F);
/* 23 */     if (addMaxDist < 10.0F) countableSpeed += 0.1F - 0.1F * addMaxDist / 10.0F; 
/* 24 */     int pitchMinDist = findClosestVector(lastPitch, pitchVectors);
/* 25 */     float pitchMaxDist = Math.abs(Math.abs(lastPitch) - Math.abs(pitchVectors[pitchMinDist]));
/*    */     
/* 27 */     if (pitchMaxDist < 26.0F) {
/* 28 */       countableSpeed = Math.max(1.94F, countableSpeed);
/* 29 */       countableSpeed += 0.05F - pitchMaxDist * 0.05F / 26.0F;
/*    */     } 
/*    */     
/* 32 */     countableSpeed = Math.min(2.045F, countableSpeed);
/* 33 */     if (mc.field_1724.field_6004 > -55.0F && mc.field_1724.field_6004 < -19.0F) { countableSpeed = 1.91F; }
/* 34 */     else if (mc.field_1724.field_6004 < -55.0F) { countableSpeed = 1.54F; }
/* 35 */      if (mc.field_1724.field_6004 > 19.0F && mc.field_1724.field_6004 < 55.0F) { countableSpeed = 1.8F; }
/* 36 */     else if (mc.field_1724.field_6004 > 55.0F) { countableSpeed = 1.54F; }
/*    */     
/* 38 */     return countableSpeed;
/*    */   }
/*    */   
/*    */   private static int findClosestVector(float lastYaw, int[] vectors) {
/* 42 */     int index = 0;
/* 43 */     int minDistIndex = -1;
/* 44 */     float minDist = Float.MAX_VALUE;
/*    */     
/* 46 */     for (int vector : vectors) {
/* 47 */       float dist = Math.abs(class_3532.method_15393(lastYaw) - vector);
/* 48 */       if (dist < minDist) {
/* 49 */         minDist = dist;
/* 50 */         minDistIndex = index;
/*    */       } 
/*    */       
/* 53 */       index++;
/*    */     } 
/*    */     
/* 56 */     return minDistIndex;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\combat\BoostUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */