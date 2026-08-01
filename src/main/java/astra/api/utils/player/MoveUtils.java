/*    */ package shame.astra.api.utils.player;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import net.minecraft.class_243;
/*    */ import net.minecraft.class_310;
/*    */ import net.minecraft.class_3532;
/*    */ import net.minecraft.class_746;
/*    */ import shame.astra.api.utils.input.MovingUtil;
/*    */ 
/*    */ 
/*    */ public class MoveUtils
/*    */ {
/* 13 */   private static final class_310 mc = class_310.method_1551();
/*    */   
/*    */   public static void setMotion(double motion) {
/* 16 */     if (mc.field_1724 == null)
/*    */       return; 
/* 18 */     double forward = mc.field_1724.field_3913.field_3905;
/* 19 */     double strafe = mc.field_1724.field_3913.field_3907;
/* 20 */     float yaw = mc.field_1724.method_36454();
/*    */     
/* 22 */     if (forward == 0.0D && strafe == 0.0D) {
/* 23 */       mc.field_1724.method_18800(0.0D, (mc.field_1724.method_18798()).field_1351, 0.0D);
/*    */     } else {
/* 25 */       if (forward != 0.0D) {
/* 26 */         if (strafe > 0.0D) {
/* 27 */           yaw += ((forward > 0.0D) ? -45 : 45);
/* 28 */         } else if (strafe < 0.0D) {
/* 29 */           yaw += ((forward > 0.0D) ? 45 : -45);
/*    */         } 
/* 31 */         strafe = 0.0D;
/* 32 */         if (forward > 0.0D) {
/* 33 */           forward = 1.0D;
/* 34 */         } else if (forward < 0.0D) {
/* 35 */           forward = -1.0D;
/*    */         } 
/*    */       } 
/*    */ 
/*    */       
/* 40 */       double motionX = forward * motion * class_3532.method_15362((float)Math.toRadians((yaw + 90.0F))) + strafe * motion * class_3532.method_15374((float)Math.toRadians((yaw + 90.0F)));
/*    */       
/* 42 */       double motionZ = forward * motion * class_3532.method_15374((float)Math.toRadians((yaw + 90.0F))) - strafe * motion * class_3532.method_15362((float)Math.toRadians((yaw + 90.0F)));
/*    */       
/* 44 */       mc.field_1724.method_18800(motionX, (mc.field_1724.method_18798()).field_1351, motionZ);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static double getSpeed() {
/* 49 */     if (mc.field_1724 == null) return 0.0D; 
/* 50 */     class_243 velocity = mc.field_1724.method_18798();
/* 51 */     return Math.sqrt(velocity.field_1352 * velocity.field_1352 + velocity.field_1350 * velocity.field_1350);
/*    */   }
/*    */   
/*    */   public static void setVelocity(double velocity) {
/* 55 */     double[] direction = MovingUtil.calculateDirection(velocity);
/* 56 */     ((class_746)Objects.<class_746>requireNonNull(mc.field_1724)).method_18800(direction[0], mc.field_1724.method_18798().method_10214(), direction[1]);
/*    */   }
/*    */   
/*    */   public static void setVelocity(double velocity, double y) {
/* 60 */     double[] direction = MovingUtil.calculateDirection(velocity);
/* 61 */     ((class_746)Objects.<class_746>requireNonNull(mc.field_1724)).method_18800(direction[0], y, direction[1]);
/*    */   }
/*    */   
/*    */   public static void strafe() {
/* 65 */     strafe(getSpeed());
/*    */   }
/*    */   
/*    */   public static void strafe(double speed) {
/* 69 */     if (mc.field_1724 == null)
/*    */       return; 
/* 71 */     float yaw = mc.field_1724.method_36454();
/* 72 */     double forward = mc.field_1724.field_3913.field_3905;
/* 73 */     double strafe = mc.field_1724.field_3913.field_3907;
/*    */     
/* 75 */     if (forward == 0.0D && strafe == 0.0D) {
/* 76 */       mc.field_1724.method_18800(0.0D, (mc.field_1724.method_18798()).field_1351, 0.0D);
/*    */       
/*    */       return;
/*    */     } 
/* 80 */     if (forward != 0.0D) {
/* 81 */       if (strafe > 0.0D) {
/* 82 */         yaw += (forward > 0.0D) ? -45.0F : 45.0F;
/* 83 */       } else if (strafe < 0.0D) {
/* 84 */         yaw += (forward > 0.0D) ? 45.0F : -45.0F;
/*    */       } 
/* 86 */       strafe = 0.0D;
/* 87 */       forward = (forward > 0.0D) ? 1.0D : -1.0D;
/*    */     } 
/*    */     
/* 90 */     double rad = Math.toRadians((yaw + 90.0F));
/* 91 */     double motionX = forward * speed * Math.cos(rad) + strafe * speed * Math.sin(rad);
/* 92 */     double motionZ = forward * speed * Math.sin(rad) - strafe * speed * Math.cos(rad);
/*    */     
/* 94 */     mc.field_1724.method_18800(motionX, (mc.field_1724.method_18798()).field_1351, motionZ);
/*    */   }
/*    */   
/*    */   public static boolean isMoving() {
/* 98 */     if (mc.field_1724 == null) return false; 
/* 99 */     return (mc.field_1724.field_3913.field_3905 != 0.0F || mc.field_1724.field_3913.field_3907 != 0.0F);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\player\MoveUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */