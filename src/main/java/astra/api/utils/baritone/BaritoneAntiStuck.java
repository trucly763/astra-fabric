/*     */ package shame.astra.api.utils.baritone;
/*     */ 
/*     */ import net.minecraft.class_1297;
/*     */ import net.minecraft.class_238;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_310;
/*     */ import net.minecraft.class_3532;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BaritoneAntiStuck
/*     */ {
/*     */   private static final String PROTECTED_BLOCK_MESSAGE = "Извините, но вы не можете сломать блок здесь";
/*     */   private static final long STUCK_TIMEOUT_MS = 7000L;
/*     */   private static final double PROGRESS_DISTANCE_SQ = 1.0D;
/*     */   private static final int RECOVERY_TICKS = 12;
/*     */   private static final double PRIVATE_ESCAPE_DISTANCE_SQ = 2500.0D;
/*     */   private static final long PRIVATE_ESCAPE_TIMEOUT_MS = 25000L;
/*     */   private static final double SIDE_OFFSET = 0.95D;
/*     */   private static final double FORWARD_OFFSET = 0.35D;
/*     */   private static final String BARITONE_API_CLASS = "baritone.api.BaritoneAPI";
/*     */   private static final String INPUT_ENUM_CLASS = "baritone.api.utils.input.Input";
/*     */   private static class_243 anchorPos;
/*     */   private static long lastProgressAtMs;
/*     */   private static int recoveryTicksRemaining;
/*     */   private static boolean strafeRightNext;
/*     */   private static boolean privateEscapePending;
/*     */   private static boolean privateEscapeActive;
/*     */   private static boolean privateEscapeRight;
/*     */   private static class_243 privateEscapeStartPos;
/*     */   private static long privateEscapeStartedAtMs;
/*     */   
/*     */   public static void onGameMessage(String message) {
/*  36 */     if (message == null || !message.contains("Извините, но вы не можете сломать блок здесь")) {
/*     */       return;
/*     */     }
/*  39 */     privateEscapePending = true;
/*     */   }
/*     */   
/*     */   public static void tick() {
/*  43 */     class_310 mc = class_310.method_1551();
/*  44 */     if (mc.field_1724 == null || mc.field_1687 == null) {
/*  45 */       resetState();
/*     */       
/*     */       return;
/*     */     } 
/*     */     try {
/*  50 */       Object baritone = getPrimaryBaritone();
/*  51 */       if (baritone == null) {
/*  52 */         resetState();
/*     */         
/*     */         return;
/*     */       } 
/*  56 */       Object pathing = invoke(baritone, "getPathingBehavior");
/*  57 */       Object input = invoke(baritone, "getInputOverrideHandler");
/*  58 */       if (pathing == null || input == null || !Boolean.TRUE.equals(invoke(pathing, "isPathing"))) {
/*  59 */         clearRecovery(input);
/*  60 */         resetTracking();
/*     */         
/*     */         return;
/*     */       } 
/*  64 */       long now = System.currentTimeMillis();
/*  65 */       class_243 currentPos = mc.field_1724.method_19538();
/*     */       
/*  67 */       if (anchorPos == null) {
/*  68 */         anchorPos = currentPos;
/*  69 */         lastProgressAtMs = now;
/*     */       } 
/*     */       
/*  72 */       if (privateEscapePending && isMiningNow(mc, input)) {
/*  73 */         startPrivateEscape(mc, currentPos);
/*  74 */         privateEscapePending = false;
/*     */       } 
/*     */       
/*  77 */       if (privateEscapeActive) {
/*  78 */         if (currentPos.method_1025(privateEscapeStartPos) >= 2500.0D || now - privateEscapeStartedAtMs >= 25000L) {
/*     */           
/*  80 */           clearAllKeys(input);
/*  81 */           privateEscapeActive = false;
/*  82 */           anchorPos = currentPos;
/*  83 */           lastProgressAtMs = now;
/*     */           
/*     */           return;
/*     */         } 
/*  87 */         applyPrivateEscapeInput(mc, input);
/*  88 */         anchorPos = currentPos;
/*  89 */         lastProgressAtMs = now;
/*     */         
/*     */         return;
/*     */       } 
/*  93 */       if (recoveryTicksRemaining > 0) {
/*  94 */         applyRecoveryInput(mc, input);
/*  95 */         recoveryTicksRemaining--;
/*  96 */         if (recoveryTicksRemaining <= 0) {
/*  97 */           clearAllKeys(input);
/*  98 */           anchorPos = mc.field_1724.method_19538();
/*  99 */           lastProgressAtMs = now;
/*     */         } 
/*     */         
/*     */         return;
/*     */       } 
/* 104 */       if (isMiningNow(mc, input)) {
/* 105 */         anchorPos = currentPos;
/* 106 */         lastProgressAtMs = now;
/*     */         
/*     */         return;
/*     */       } 
/* 110 */       if (!isTryingToMove(input)) {
/* 111 */         anchorPos = currentPos;
/* 112 */         lastProgressAtMs = now;
/*     */         
/*     */         return;
/*     */       } 
/* 116 */       if (currentPos.method_1025(anchorPos) >= 1.0D) {
/* 117 */         anchorPos = currentPos;
/* 118 */         lastProgressAtMs = now;
/*     */         
/*     */         return;
/*     */       } 
/* 122 */       if (now - lastProgressAtMs < 7000L) {
/*     */         return;
/*     */       }
/*     */       
/* 126 */       recoveryTicksRemaining = 12;
/* 127 */       strafeRightNext = chooseRecoverySide(mc, strafeRightNext, true);
/* 128 */       applyRecoveryInput(mc, input);
/* 129 */       anchorPos = currentPos;
/* 130 */       lastProgressAtMs = now;
/* 131 */     } catch (Throwable ignored) {
/* 132 */       resetState();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Object getPrimaryBaritone() throws ReflectiveOperationException {
/* 137 */     Class<?> apiClass = Class.forName("baritone.api.BaritoneAPI");
/* 138 */     Object provider = apiClass.getMethod("getProvider", new Class[0]).invoke(null, new Object[0]);
/* 139 */     return (provider == null) ? null : provider.getClass().getMethod("getPrimaryBaritone", new Class[0]).invoke(provider, new Object[0]);
/*     */   }
/*     */   
/*     */   private static boolean isMiningNow(class_310 mc, Object input) throws ReflectiveOperationException {
/* 143 */     return ((mc.field_1761 != null && mc.field_1761.method_2923()) || 
/* 144 */       isInputForcedDown(input, "CLICK_LEFT"));
/*     */   }
/*     */   
/*     */   private static boolean isTryingToMove(Object input) throws ReflectiveOperationException {
/* 148 */     return (isInputForcedDown(input, "MOVE_FORWARD") || 
/* 149 */       isInputForcedDown(input, "MOVE_BACK") || 
/* 150 */       isInputForcedDown(input, "MOVE_LEFT") || 
/* 151 */       isInputForcedDown(input, "MOVE_RIGHT") || 
/* 152 */       isInputForcedDown(input, "JUMP"));
/*     */   }
/*     */   
/*     */   private static void startPrivateEscape(class_310 mc, class_243 currentPos) {
/* 156 */     privateEscapeActive = true;
/* 157 */     privateEscapeStartPos = currentPos;
/* 158 */     privateEscapeStartedAtMs = System.currentTimeMillis();
/* 159 */     privateEscapeRight = chooseRecoverySide(mc, privateEscapeRight, false);
/*     */   }
/*     */   
/*     */   private static void applyRecoveryInput(class_310 mc, Object input) throws ReflectiveOperationException {
/* 163 */     clearAllKeys(input);
/* 164 */     setInputForceState(input, "MOVE_FORWARD", true);
/* 165 */     setInputForceState(input, strafeRightNext ? "MOVE_RIGHT" : "MOVE_LEFT", true);
/* 166 */     if (mc.field_1724 != null && mc.field_1724.method_24828()) {
/* 167 */       setInputForceState(input, "JUMP", true);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void applyPrivateEscapeInput(class_310 mc, Object input) throws ReflectiveOperationException {
/* 172 */     clearAllKeys(input);
/* 173 */     setInputForceState(input, "MOVE_BACK", true);
/* 174 */     setInputForceState(input, privateEscapeRight ? "MOVE_RIGHT" : "MOVE_LEFT", true);
/* 175 */     if (mc.field_1724 != null && mc.field_1724.method_24828()) {
/* 176 */       setInputForceState(input, "JUMP", true);
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean chooseRecoverySide(class_310 mc, boolean fallbackRight, boolean moveForward) {
/* 181 */     if (mc.field_1724 == null) {
/* 182 */       return fallbackRight;
/*     */     }
/*     */     
/* 185 */     double yawRad = Math.toRadians(mc.field_1724.method_36454());
/* 186 */     class_243 forwardDirection = new class_243(-class_3532.method_15374((float)yawRad), 0.0D, class_3532.method_15362((float)yawRad));
/* 187 */     class_243 left = new class_243(forwardDirection.field_1350, 0.0D, -forwardDirection.field_1352);
/* 188 */     class_243 right = left.method_1021(-1.0D);
/* 189 */     class_243 direction = moveForward ? forwardDirection : forwardDirection.method_1021(-1.0D);
/*     */     
/* 191 */     double leftScore = freeSpaceScore(mc, left.method_1021(0.95D).method_1019(direction.method_1021(0.35D)));
/* 192 */     double rightScore = freeSpaceScore(mc, right.method_1021(0.95D).method_1019(direction.method_1021(0.35D)));
/*     */     
/* 194 */     if (leftScore == rightScore) {
/* 195 */       return fallbackRight;
/*     */     }
/*     */     
/* 198 */     return (rightScore > leftScore);
/*     */   }
/*     */   
/*     */   private static double freeSpaceScore(class_310 mc, class_243 offset) {
/* 202 */     class_238 shifted = mc.field_1724.method_5829().method_997(offset);
/* 203 */     double score = 0.0D;
/* 204 */     if (mc.field_1687.method_8587((class_1297)mc.field_1724, shifted)) {
/* 205 */       score++;
/*     */     }
/* 207 */     if (mc.field_1687.method_8587((class_1297)mc.field_1724, shifted.method_989(0.0D, 1.0D, 0.0D))) {
/* 208 */       score += 0.35D;
/*     */     }
/* 210 */     return score;
/*     */   }
/*     */   
/*     */   private static void clearRecovery(Object input) {
/* 214 */     if (recoveryTicksRemaining > 0 && input != null) {
/*     */       try {
/* 216 */         clearAllKeys(input);
/* 217 */       } catch (ReflectiveOperationException reflectiveOperationException) {}
/*     */     }
/*     */     
/* 220 */     recoveryTicksRemaining = 0;
/* 221 */     if (privateEscapeActive && input != null) {
/*     */       try {
/* 223 */         clearAllKeys(input);
/* 224 */       } catch (ReflectiveOperationException reflectiveOperationException) {}
/*     */     }
/*     */     
/* 227 */     privateEscapeActive = false;
/* 228 */     privateEscapePending = false;
/*     */   }
/*     */   
/*     */   private static void resetTracking() {
/* 232 */     anchorPos = null;
/* 233 */     lastProgressAtMs = 0L;
/*     */   }
/*     */   
/*     */   private static void resetState() {
/* 237 */     recoveryTicksRemaining = 0;
/* 238 */     anchorPos = null;
/* 239 */     lastProgressAtMs = 0L;
/* 240 */     privateEscapePending = false;
/* 241 */     privateEscapeActive = false;
/* 242 */     privateEscapeStartPos = null;
/* 243 */     privateEscapeStartedAtMs = 0L;
/*     */   }
/*     */   
/*     */   private static boolean isInputForcedDown(Object inputOverrideHandler, String inputName) throws ReflectiveOperationException {
/* 247 */     Object input = getInputEnum(inputName);
/*     */ 
/*     */     
/* 250 */     Object result = inputOverrideHandler.getClass().getMethod("isInputForcedDown", new Class[] { input.getClass() }).invoke(inputOverrideHandler, new Object[] { input });
/* 251 */     return Boolean.TRUE.equals(result);
/*     */   }
/*     */   
/*     */   private static void setInputForceState(Object inputOverrideHandler, String inputName, boolean forced) throws ReflectiveOperationException {
/* 255 */     Object input = getInputEnum(inputName);
/* 256 */     inputOverrideHandler.getClass()
/* 257 */       .getMethod("setInputForceState", new Class[] { input.getClass(), boolean.class
/* 258 */         }).invoke(inputOverrideHandler, new Object[] { input, Boolean.valueOf(forced) });
/*     */   }
/*     */   
/*     */   private static void clearAllKeys(Object inputOverrideHandler) throws ReflectiveOperationException {
/* 262 */     inputOverrideHandler.getClass().getMethod("clearAllKeys", new Class[0]).invoke(inputOverrideHandler, new Object[0]);
/*     */   }
/*     */   
/*     */   private static Object getInputEnum(String inputName) throws ReflectiveOperationException {
/* 266 */     Class<?> inputEnum = Class.forName("baritone.api.utils.input.Input");
/*     */     
/* 268 */     Object value = Enum.valueOf((Class)inputEnum.asSubclass(Enum.class), inputName);
/* 269 */     return value;
/*     */   }
/*     */   
/*     */   private static Object invoke(Object target, String methodName) throws ReflectiveOperationException {
/* 273 */     return target.getClass().getMethod(methodName, new Class[0]).invoke(target, new Object[0]);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\baritone\BaritoneAntiStuck.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */