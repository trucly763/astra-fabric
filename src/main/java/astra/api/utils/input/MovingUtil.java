/*     */ package shame.astra.api.utils.input;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import lombok.Generated;
/*     */ import net.minecraft.class_10185;
/*     */ import net.minecraft.class_1297;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_3532;
/*     */ import net.minecraft.class_746;
/*     */ import shame.astra.api.QClient;
/*     */ import shame.astra.api.events.implement.EventMoveInput;
/*     */ import shame.astra.api.storages.implement.FreeLookStorage;
/*     */ 
/*     */ public final class MovingUtil implements QClient {
/*     */   public static boolean hasPlayerMovement() {
/*  16 */     return (mc.field_1724.field_3913.field_3905 != 0.0F || mc.field_1724.field_3913.field_3907 != 0.0F);
/*     */   }
/*     */   
/*     */   public static double[] calculateDirection(double distance) {
/*  20 */     float forward = mc.field_1724.field_3913.field_3905;
/*  21 */     float sideways = mc.field_1724.field_3913.field_3907;
/*  22 */     float yaw = mc.field_1724.method_36454();
/*  23 */     if (forward != 0.0F) {
/*  24 */       if (sideways > 0.0F) {
/*  25 */         yaw += (forward > 0.0F) ? -45.0F : 45.0F;
/*  26 */       } else if (sideways < 0.0F) {
/*  27 */         yaw += (forward > 0.0F) ? 45.0F : -45.0F;
/*     */       } 
/*     */       
/*  30 */       sideways = 0.0F;
/*  31 */       forward = (forward > 0.0F) ? 1.0F : -1.0F;
/*     */     } 
/*     */     
/*  34 */     double sinYaw = Math.sin(Math.toRadians((yaw + 90.0F)));
/*  35 */     double cosYaw = Math.cos(Math.toRadians((yaw + 90.0F)));
/*  36 */     double xMovement = forward * distance * cosYaw + sideways * distance * sinYaw;
/*  37 */     double zMovement = forward * distance * sinYaw - sideways * distance * cosYaw;
/*  38 */     return new double[] { xMovement, zMovement };
/*     */   }
/*     */   
/*     */   public static double getSpeedSqrt(class_1297 entity) {
/*  42 */     double dx = entity.method_23317() - entity.field_6014;
/*  43 */     double dy = entity.method_23318() - entity.field_6036;
/*  44 */     double dz = entity.method_23321() - entity.field_5969;
/*  45 */     return Math.sqrt(dx * dx + dz * dz + dy * dy);
/*     */   }
/*     */   
/*     */   public static void setVelocity(double velocity) {
/*  49 */     double[] direction = calculateDirection(velocity);
/*  50 */     ((class_746)Objects.<class_746>requireNonNull(mc.field_1724)).method_18800(direction[0], mc.field_1724.method_18798().method_10214(), direction[1]);
/*     */   }
/*     */   
/*     */   public static void setVelocity(double velocity, double y) {
/*  54 */     double[] direction = calculateDirection(velocity);
/*  55 */     ((class_746)Objects.<class_746>requireNonNull(mc.field_1724)).method_18800(direction[0], y, direction[1]);
/*     */   }
/*     */   
/*     */   public static double getDegreesRelativeToView(class_243 positionRelativeToPlayer, float yaw) {
/*  59 */     float optimalYaw = (float)Math.atan2(-positionRelativeToPlayer.field_1352, positionRelativeToPlayer.field_1350);
/*  60 */     double currentYaw = Math.toRadians(class_3532.method_15393(yaw));
/*  61 */     return Math.toDegrees(class_3532.method_15338(optimalYaw - currentYaw));
/*     */   }
/*     */   
/*     */   public static class_10185 getDirectionalInputForDegrees(class_10185 input, double dgs, float deadAngle) {
/*  65 */     boolean forwards = input.comp_3159();
/*  66 */     boolean backwards = input.comp_3160();
/*  67 */     boolean left = input.comp_3161();
/*  68 */     boolean right = input.comp_3162();
/*  69 */     if (dgs >= (-90.0F + deadAngle) && dgs <= (90.0F - deadAngle)) {
/*  70 */       forwards = true;
/*  71 */     } else if (dgs < (-90.0F - deadAngle) || dgs > (90.0F + deadAngle)) {
/*  72 */       backwards = true;
/*     */     } 
/*     */     
/*  75 */     if (dgs >= (0.0F + deadAngle) && dgs <= (180.0F - deadAngle)) {
/*  76 */       right = true;
/*  77 */     } else if (dgs >= (-180.0F + deadAngle) && dgs <= (0.0F - deadAngle)) {
/*  78 */       left = true;
/*     */     } 
/*     */     
/*  81 */     return new class_10185(forwards, backwards, left, right, input.comp_3163(), input.comp_3164(), input.comp_3165());
/*     */   }
/*     */   
/*     */   public static void fixMovementFocus(EventMoveInput event, float yaw) {
/*  85 */     float forward = event.getForward();
/*  86 */     float strafe = event.getStrafe();
/*  87 */     if (forward != 0.0F || strafe != 0.0F) {
/*  88 */       double targetAngle = class_3532.method_15338(Math.toDegrees(direction(yaw, forward, strafe)));
/*  89 */       float bestForward = 0.0F;
/*  90 */       float bestStrafe = 0.0F;
/*  91 */       float smallestDifference = Float.MAX_VALUE;
/*     */       float testForward;
/*  93 */       for (testForward = -1.0F; testForward <= 1.0F; testForward++) {
/*  94 */         float testStrafe; for (testStrafe = -1.0F; testStrafe <= 1.0F; testStrafe++) {
/*  95 */           if (testForward != 0.0F || testStrafe != 0.0F) {
/*  96 */             double testAngle = class_3532.method_15338(Math.toDegrees(direction(yaw, testForward, testStrafe)));
/*  97 */             float difference = Math.abs(class_3532.method_15393((float)(targetAngle - testAngle)));
/*  98 */             if (difference < smallestDifference) {
/*  99 */               smallestDifference = difference;
/* 100 */               bestForward = testForward;
/* 101 */               bestStrafe = testStrafe;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 107 */       event.setForward(bestForward);
/* 108 */       event.setStrafe(bestStrafe);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void fixMovementFree(EventMoveInput event) {
/* 113 */     float forward = event.getForward();
/* 114 */     float strafe = event.getStrafe();
/* 115 */     double angle = class_3532.method_15338(Math.toDegrees(direction(mc.field_1724.method_6128() ? mc.field_1724.method_36454() : FreeLookStorage.getFreeYaw(), forward, strafe)));
/* 116 */     if (forward != 0.0F || strafe != 0.0F) {
/* 117 */       float closestForward = 0.0F;
/* 118 */       float closestStrafe = 0.0F;
/* 119 */       float closestDifference = Float.MAX_VALUE;
/*     */       float predictedForward;
/* 121 */       for (predictedForward = -1.0F; predictedForward <= 1.0F; predictedForward++) {
/* 122 */         float predictedStrafe; for (predictedStrafe = -1.0F; predictedStrafe <= 1.0F; predictedStrafe++) {
/* 123 */           if (predictedStrafe != 0.0F || predictedForward != 0.0F) {
/* 124 */             double predictedAngle = class_3532.method_15338(Math.toDegrees(direction(mc.field_1724.method_36454(), predictedForward, predictedStrafe)));
/* 125 */             double difference = Math.abs(angle - predictedAngle);
/* 126 */             if (difference < closestDifference) {
/* 127 */               closestDifference = (float)difference;
/* 128 */               closestForward = predictedForward;
/* 129 */               closestStrafe = predictedStrafe;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 135 */       event.setForward(closestForward);
/* 136 */       event.setStrafe(closestStrafe);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static double direction(float rotationYaw, float moveForward, float moveStrafing) {
/* 141 */     if (moveForward < 0.0F) {
/* 142 */       rotationYaw += 180.0F;
/*     */     }
/*     */     
/* 145 */     float forward = 1.0F;
/* 146 */     if (moveForward < 0.0F) {
/* 147 */       forward = -0.5F;
/*     */     }
/*     */     
/* 150 */     if (moveForward > 0.0F) {
/* 151 */       forward = 0.5F;
/*     */     }
/*     */     
/* 154 */     if (moveStrafing > 0.0F) {
/* 155 */       rotationYaw -= 90.0F * forward;
/*     */     }
/*     */     
/* 158 */     if (moveStrafing < 0.0F) {
/* 159 */       rotationYaw += 90.0F * forward;
/*     */     }
/*     */     
/* 162 */     return Math.toRadians(rotationYaw);
/*     */   }
/*     */   
/*     */   public static class_10185 getDirectionalInputForDegrees(class_10185 input, double dgs) {
/* 166 */     return getDirectionalInputForDegrees(input, dgs, 20.0F);
/*     */   }
/*     */   
/*     */   @Generated
/*     */   private MovingUtil() {
/* 171 */     throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\input\MovingUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */