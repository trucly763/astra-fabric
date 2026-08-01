/*     */ package shame.astra.api.utils.combat;
/*     */ import net.minecraft.class_1294;
/*     */ import net.minecraft.class_1309;
/*     */ import net.minecraft.class_1802;
/*     */ import net.minecraft.class_1922;
/*     */ import net.minecraft.class_2246;
/*     */ import net.minecraft.class_2248;
/*     */ import net.minecraft.class_2338;
/*     */ import net.minecraft.class_2374;
/*     */ import net.minecraft.class_238;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_2680;
/*     */ import net.minecraft.class_3532;
/*     */ import shame.astra.api.QClient;
/*     */ import shame.astra.api.storages.implement.helpertstorages.enumvar.ModuleClass;
/*     */ 
/*     */ public final class IdealHitUtils implements QClient {
/*     */   @Generated
/*     */   private IdealHitUtils() {
/*  20 */     throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
/*     */   }
/*     */   private static final int WATER_CRIT_INTENT_TICKS = 8; private static final int WATER_CRIT_CONTACT_TICKS = 10;
/*     */   private static final double WATER_CRIT_MIN_UPWARD_VELOCITY = 0.05D;
/*  24 */   private static int lastWaterContactAge = Integer.MIN_VALUE;
/*  25 */   private static int lastWaterCritIntentAge = Integer.MIN_VALUE;
/*     */   
/*     */   public static float getAICooldown() {
/*  28 */     if (mc.field_1724.method_6047().method_7909() == class_1802.field_8162) return 0.9F;
/*     */     
/*  30 */     if (mc.field_1724.method_6047().method_7909() instanceof net.minecraft.class_1743 || mc.field_1724.method_6047().method_7909() instanceof net.minecraft.class_1821)
/*  31 */       return 0.95F; 
/*  32 */     return 0.93F;
/*     */   }
/*     */   
/*     */   public static boolean canAIFall() {
/*  36 */     class_2338 posWater = class_2338.method_49638((class_2374)mc.field_1724.method_19538().method_1031(0.0D, -0.4000000059604645D, 0.0D));
/*  37 */     if (mc.field_1687.method_8320(posWater).method_27852(class_2246.field_10382)) return true; 
/*  38 */     return ((getBlock(0.0D, 3.0D, 0.0D) == class_2246.field_10124 && getBlock(0.0D, 2.0D, 0.0D) == class_2246.field_10124 && getBlock(0.0D, 1.0D, 0.0D) == class_2246.field_10124) || mc.field_1724.field_6017 < (
/*  39 */       (getBlock(0.0D, 2.0D, 0.0D) != class_2246.field_10124) ? 0.08F : 0.6F) || mc.field_1724.field_6017 > 1.2F);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean canCritical(class_1309 target) {
/*  44 */     updateWaterCritState();
/*     */     
/*  46 */     boolean packetCrits = ModuleClass.packetCriticals.isEnable();
/*  47 */     boolean hasSlowFalling = mc.field_1724.method_6059(class_1294.field_5906);
/*  48 */     boolean inCobweb = isInCobweb();
/*  49 */     boolean smartCrit = ModuleClass.aura.smartCrit.isState();
/*     */     
/*  51 */     if (packetCrits && inCobweb) {
/*  52 */       return true;
/*     */     }
/*     */     
/*  55 */     if (packetCrits && hasSlowFalling) {
/*  56 */       return ((mc.field_1724.method_18798()).field_1351 < 0.0D && mc.field_1724.field_6017 > 0.0F);
/*     */     }
/*     */     
/*  59 */     if (isTryingWaterCrit()) {
/*  60 */       return isWaterCritWindow();
/*     */     }
/*     */ 
/*     */     
/*  64 */     boolean isCritPossible = (!mc.field_1724.method_24828() && (mc.field_1724.method_18798()).field_1351 < 0.0D && mc.field_1724.field_6017 > 0.0F);
/*     */ 
/*     */     
/*  67 */     if (isNoJumpDelayCeilingCritIntent()) {
/*  68 */       return isNoJumpDelayCeilingCritWindow();
/*     */     }
/*     */     
/*  71 */     if (isNoJumpDelayJumpCritIntent()) {
/*  72 */       return isNoJumpDelayJumpCritWindow();
/*     */     }
/*     */     
/*  75 */     if (cannotPerformCrit()) {
/*  76 */       return true;
/*     */     }
/*     */     
/*  79 */     if (smartCrit) {
/*  80 */       return (mc.field_1724.method_24828() || isCritPossible);
/*     */     }
/*     */     
/*  83 */     return isCritPossible;
/*     */   }
/*     */   
/*     */   private static boolean isNoJumpDelayCeilingCritIntent() {
/*  87 */     return (ModuleClass.noJumpDelay.isEnable() && mc.field_1690 != null && mc.field_1690.field_1903
/*     */       
/*  89 */       .method_1434() && 
/*  90 */       hasLowCeilingForJumpCrit());
/*     */   }
/*     */   
/*     */   private static boolean isNoJumpDelayJumpCritIntent() {
/*  94 */     return (ModuleClass.noJumpDelay.isEnable() && mc.field_1690 != null && mc.field_1690.field_1903
/*     */       
/*  96 */       .method_1434());
/*     */   }
/*     */   
/*     */   private static boolean isNoJumpDelayCeilingCritWindow() {
/* 100 */     return (mc.field_1724 != null && 
/* 101 */       !mc.field_1724.method_24828() && 
/* 102 */       (mc.field_1724.method_18798()).field_1351 <= 0.01D && 
/* 103 */       !mc.field_1724.method_5799() && 
/* 104 */       !mc.field_1724.method_5869() && 
/* 105 */       !mc.field_1724.method_5771() && 
/* 106 */       !mc.field_1724.method_6101() && 
/* 107 */       !mc.field_1724.method_5765() && 
/* 108 */       !(mc.field_1724.method_31549()).field_7479);
/*     */   }
/*     */   
/*     */   public static boolean isNoJumpDelayJumpCritWindow() {
/* 112 */     if (mc.field_1724 != null && mc.field_1687 != null) if (ModuleClass.noJumpDelay
/*     */         
/* 114 */         .isEnable() && mc.field_1690 != null && mc.field_1690.field_1903
/*     */         
/* 116 */         .method_1434() && 
/* 117 */         !mc.field_1724.method_24828() && 
/* 118 */         (mc.field_1724.method_18798()).field_1351 < 0.0D && 
/* 119 */         !mc.field_1724.method_5799() && 
/* 120 */         !mc.field_1724.method_5869() && 
/* 121 */         !mc.field_1724.method_5771() && 
/* 122 */         !mc.field_1724.method_6101() && 
/* 123 */         !mc.field_1724.method_5765() && 
/* 124 */         !(mc.field_1724.method_31549()).field_7479 && 
/* 125 */         !mc.field_1724.method_6059(class_1294.field_5902) && 
/* 126 */         !mc.field_1724.method_6059(class_1294.field_5906) && 
/* 127 */         !mc.field_1724.method_6059(class_1294.field_5919) && 
/* 128 */         !mc.field_1724.method_6128() && 
/* 129 */         !isInCobweb()); 
/*     */     return false;
/*     */   }
/*     */   private static boolean hasLowCeilingForJumpCrit() {
/* 133 */     if (mc.field_1724 == null || mc.field_1687 == null) {
/* 134 */       return false;
/*     */     }
/*     */     
/* 137 */     class_238 box = mc.field_1724.method_5829().method_1011(0.03D);
/* 138 */     class_238 headBox = new class_238(box.field_1323, box.field_1325, box.field_1321, box.field_1320, box.field_1325 + 0.32D, box.field_1324);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 147 */     for (class_2338 pos : class_2338.method_10094(
/* 148 */         class_3532.method_15357(headBox.field_1323), class_3532.method_15357(headBox.field_1322), class_3532.method_15357(headBox.field_1321), 
/* 149 */         class_3532.method_15357(headBox.field_1320), class_3532.method_15357(headBox.field_1325), class_3532.method_15357(headBox.field_1324))) {
/* 150 */       class_2680 state = mc.field_1687.method_8320(pos);
/* 151 */       if (!state.method_26215() && !state.method_26220((class_1922)mc.field_1687, pos).method_1110()) {
/* 152 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 156 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean canPacketCrit() {
/* 160 */     return (isInCobweb() || mc.field_1724.method_6059(class_1294.field_5906));
/*     */   }
/*     */   
/*     */   private static void updateWaterCritState() {
/* 164 */     if (mc.field_1724 == null || mc.field_1687 == null) {
/* 165 */       lastWaterContactAge = Integer.MIN_VALUE;
/* 166 */       lastWaterCritIntentAge = Integer.MIN_VALUE;
/*     */       
/*     */       return;
/*     */     } 
/* 170 */     boolean nearWaterSurface = isNearWaterSurface();
/* 171 */     if (!nearWaterSurface) {
/*     */       return;
/*     */     }
/*     */     
/* 175 */     lastWaterContactAge = mc.field_1724.field_6012;
/*     */     
/* 177 */     if (isWaterCritIntentState()) {
/* 178 */       lastWaterCritIntentAge = mc.field_1724.field_6012;
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean isWaterCritIntentState() {
/* 183 */     if (mc.field_1724 == null || mc.field_1690 == null) {
/* 184 */       return false;
/*     */     }
/*     */     
/* 187 */     return (mc.field_1690.field_1903.method_1434() && 
/* 188 */       !mc.field_1724.method_24828() && 
/* 189 */       !mc.field_1724.method_5869() && 
/* 190 */       (mc.field_1724.method_18798()).field_1351 > 0.05D);
/*     */   }
/*     */   
/*     */   private static boolean isTryingWaterCrit() {
/* 194 */     if (mc.field_1724 == null || mc.field_1690 == null || !mc.field_1690.field_1903.method_1434()) {
/* 195 */       return false;
/*     */     }
/*     */     
/* 198 */     return (mc.field_1724.field_6012 - lastWaterCritIntentAge <= 8 && mc.field_1724.field_6012 - lastWaterContactAge <= 10);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isWaterCritWindow() {
/* 203 */     return (mc.field_1724 != null && 
/* 204 */       !mc.field_1724.method_24828() && 
/* 205 */       !mc.field_1724.method_5799() && 
/* 206 */       !mc.field_1724.method_5869() && mc.field_1724.field_6017 > 0.0F && 
/*     */       
/* 208 */       (mc.field_1724.method_18798()).field_1351 < 0.0D);
/*     */   }
/*     */   
/*     */   private static boolean isNearWaterSurface() {
/* 212 */     if (mc.field_1724 == null || mc.field_1687 == null) {
/* 213 */       return false;
/*     */     }
/*     */     
/* 216 */     class_2338 below = class_2338.method_49638((class_2374)mc.field_1724.method_19538().method_1031(0.0D, -0.4000000059604645D, 0.0D));
/* 217 */     return (mc.field_1724.method_5799() || mc.field_1724
/* 218 */       .method_5869() || mc.field_1687
/* 219 */       .method_8320(below).method_27852(class_2246.field_10382));
/*     */   }
/*     */   
/*     */   private static boolean cannotPerformCrit() {
/* 223 */     double effectiveJumpHeight = mc.field_1724.method_49476();
/* 224 */     class_243 jumpVec = new class_243(0.0D, effectiveJumpHeight, 0.0D);
/* 225 */     class_243 allowedMovement = ((IEntity)mc.field_1724).invokeAdjustMovementForCollisions(jumpVec);
/*     */     
/* 227 */     boolean cobweb = isInCobweb();
/*     */     
/* 229 */     class_2338 posWater = class_2338.method_49638((class_2374)mc.field_1724.method_19538().method_1031(0.0D, (mc.field_1724.method_17682() / 2.0F), 0.0D));
/*     */     
/* 231 */     return (mc.field_1724.method_5771() || mc.field_1724
/* 232 */       .method_6101() || mc.field_1687
/* 233 */       .method_8320(posWater).method_27852(class_2246.field_10382) || mc.field_1724
/* 234 */       .method_6059(class_1294.field_5902) || mc.field_1724
/* 235 */       .method_6059(class_1294.field_5906) || mc.field_1724
/* 236 */       .method_6059(class_1294.field_5919) || cobweb || mc.field_1724
/*     */       
/* 238 */       .method_6128() || mc.field_1724
/* 239 */       .method_5765() || 
/* 240 */       (mc.field_1724.method_31549()).field_7479 || mc.field_1724
/* 241 */       .method_5799() || (allowedMovement.field_1351 < mc.field_1724
/* 242 */       .method_49476() - 0.5D && mc.field_1724.method_24828()));
/*     */   }
/*     */   
/*     */   public static boolean isInCobweb() {
/* 246 */     class_238 box = mc.field_1724.method_5829();
/* 247 */     for (class_2338 pos : class_2338.method_10094(
/* 248 */         class_3532.method_15357(box.field_1323), class_3532.method_15357(box.field_1322), class_3532.method_15357(box.field_1321), 
/* 249 */         class_3532.method_15357(box.field_1320), class_3532.method_15357(box.field_1325), class_3532.method_15357(box.field_1324))) {
/* 250 */       if (mc.field_1687.method_8320(pos).method_27852(class_2246.field_10343)) {
/* 251 */         return true;
/*     */       }
/*     */     } 
/* 254 */     return false;
/*     */   }
/*     */   
/*     */   public static class_2248 getBlock(double x, double y, double z) {
/* 258 */     return mc.field_1687.method_8320(mc.field_1724.method_24515().method_10069((int)x, (int)y, (int)z)).method_26204();
/*     */   }
/*     */   
/*     */   public static boolean findFall(float fallDistance) {
/* 262 */     class_243 rotationVec = mc.field_1724.method_5720();
/* 263 */     double tempVelocityX = (mc.field_1724.method_18798()).field_1352;
/* 264 */     double tempVelocityY = (mc.field_1724.method_18798()).field_1351;
/* 265 */     double tempVelocityZ = (mc.field_1724.method_18798()).field_1350;
/*     */     
/* 267 */     float n = class_3532.method_15362(mc.field_1724.method_36455() * 0.017453292F);
/* 268 */     n = (float)((n * n) * Math.min(rotationVec.method_1033() / 0.4D, 1.0D));
/*     */     
/* 270 */     class_243 vec3d = (new class_243(tempVelocityX, tempVelocityY, tempVelocityZ)).method_1031(0.0D, 0.08D * (-1.0D + n * 0.75D), 0.0D);
/* 271 */     tempVelocityY = vec3d.field_1351 * 0.9800000190734863D;
/*     */     
/* 273 */     return (tempVelocityY < fallDistance);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\combat\IdealHitUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */