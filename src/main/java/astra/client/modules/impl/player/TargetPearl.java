/*     */ package shame.astra.client.modules.impl.player;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import net.minecraft.class_1297;
/*     */ import net.minecraft.class_1309;
/*     */ import net.minecraft.class_1657;
/*     */ import net.minecraft.class_1684;
/*     */ import net.minecraft.class_1799;
/*     */ import net.minecraft.class_1802;
/*     */ import net.minecraft.class_1935;
/*     */ import net.minecraft.class_2246;
/*     */ import net.minecraft.class_2338;
/*     */ import net.minecraft.class_2374;
/*     */ import net.minecraft.class_238;
/*     */ import net.minecraft.class_239;
/*     */ import net.minecraft.class_241;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_2596;
/*     */ import net.minecraft.class_2828;
/*     */ import net.minecraft.class_3532;
/*     */ import net.minecraft.class_3959;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.EventBinding;
/*     */ import shame.astra.api.events.implement.EventMoveInput;
/*     */ import shame.astra.api.events.implement.EventUpdate;
/*     */ import shame.astra.api.storages.implement.helpertstorages.enumvar.ModuleClass;
/*     */ import shame.astra.api.utils.math.TimerUtils;
/*     */ import shame.astra.api.utils.player.InventoryUtils;
/*     */ import shame.astra.astra;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.BindSetting;
/*     */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*     */ import shame.astra.client.modules.settings.implement.ModeSetting;
/*     */ 
/*     */ public class TargetPearl extends Module {
/*     */   private static final double MAX_TRACK_DISTANCE = 256.0D;
/*     */   private static final double MIN_LANDING_DISTANCE = 11.0D;
/*     */   private static final long LOCAL_THROW_COOLDOWN_MS = 2500L;
/*  40 */   public static final TargetPearl INSTANCE = new TargetPearl();
/*     */   private static final float DIRECT_MIN_PITCH = -25.0F;
/*  42 */   private final ModeSetting mode = new ModeSetting("Тип", "Автоматический", new String[] { "По бинду", "Автоматический" }); private static final float DIRECT_MAX_PITCH = 35.0F; private static final float PITCH_STEP = 0.25F;
/*  43 */   private final BindSetting bind = (new BindSetting("Бинд", -1))
/*  44 */     .visible(() -> Boolean.valueOf(this.mode.is("По бинду")));
/*  45 */   private final BooleanSetting onlyTarget = new BooleanSetting("Только за противником", false);
/*  46 */   private final BooleanSetting ignoreFriends = new BooleanSetting("Игнорировать друзей", true);
/*     */   
/*  48 */   private final TimerUtils timer = new TimerUtils();
/*     */   
/*     */   private class_1684 targetPearl;
/*  51 */   private int lastHandledPearlId = -1;
/*     */   private long nextThrowAt;
/*     */   private boolean isThrowing;
/*     */   private class_241 serverRotation;
/*     */   
/*     */   public TargetPearl() {
/*  57 */     super("TargetPearl", "Автоматически бросает жемчуг в цель", Module.ModuleCategory.PLAYER);
/*  58 */     addSettings(new Setting[] { (Setting)this.mode, (Setting)this.bind, (Setting)this.onlyTarget, (Setting)this.ignoreFriends });
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onBinding(EventBinding event) {
/*  63 */     if (mc.field_1724 == null || mc.field_1687 == null || mc.field_1755 != null) {
/*     */       return;
/*     */     }
/*     */     
/*  67 */     if (!this.mode.is("По бинду") || event.getKey() != this.bind.getKey()) {
/*     */       return;
/*     */     }
/*     */     
/*  71 */     if (canThrowNow()) {
/*  72 */       aimAndThrowPearl();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @EventLink
/*     */   public void onUpdate(EventUpdate event) {
/*     */     // Byte code:
/*     */     //   0: getstatic shame/astra/client/modules/impl/player/TargetPearl.mc : Lnet/minecraft/class_310;
/*     */     //   3: getfield field_1724 : Lnet/minecraft/class_746;
/*     */     //   6: ifnull -> 18
/*     */     //   9: getstatic shame/astra/client/modules/impl/player/TargetPearl.mc : Lnet/minecraft/class_310;
/*     */     //   12: getfield field_1687 : Lnet/minecraft/class_638;
/*     */     //   15: ifnonnull -> 23
/*     */     //   18: aload_0
/*     */     //   19: invokevirtual resetThrowState : ()V
/*     */     //   22: return
/*     */     //   23: aload_0
/*     */     //   24: getfield lastHandledPearlId : I
/*     */     //   27: iconst_m1
/*     */     //   28: if_icmpeq -> 69
/*     */     //   31: getstatic shame/astra/client/modules/impl/player/TargetPearl.mc : Lnet/minecraft/class_310;
/*     */     //   34: getfield field_1687 : Lnet/minecraft/class_638;
/*     */     //   37: aload_0
/*     */     //   38: getfield lastHandledPearlId : I
/*     */     //   41: invokevirtual method_8469 : (I)Lnet/minecraft/class_1297;
/*     */     //   44: astore_2
/*     */     //   45: aload_2
/*     */     //   46: instanceof net/minecraft/class_1684
/*     */     //   49: ifeq -> 64
/*     */     //   52: aload_2
/*     */     //   53: checkcast net/minecraft/class_1684
/*     */     //   56: astore_3
/*     */     //   57: aload_3
/*     */     //   58: invokevirtual method_5805 : ()Z
/*     */     //   61: ifne -> 69
/*     */     //   64: aload_0
/*     */     //   65: iconst_m1
/*     */     //   66: putfield lastHandledPearlId : I
/*     */     //   69: aload_0
/*     */     //   70: getfield mode : Lshame/astra/client/modules/settings/implement/ModeSetting;
/*     */     //   73: ldc 'Автоматический'
/*     */     //   75: invokevirtual is : (Ljava/lang/String;)Z
/*     */     //   78: ifeq -> 92
/*     */     //   81: aload_0
/*     */     //   82: invokevirtual canThrowNow : ()Z
/*     */     //   85: ifeq -> 92
/*     */     //   88: aload_0
/*     */     //   89: invokevirtual aimAndThrowPearl : ()V
/*     */     //   92: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #78	-> 0
/*     */     //   #79	-> 18
/*     */     //   #80	-> 22
/*     */     //   #83	-> 23
/*     */     //   #84	-> 31
/*     */     //   #85	-> 45
/*     */     //   #86	-> 64
/*     */     //   #90	-> 69
/*     */     //   #91	-> 88
/*     */     //   #93	-> 92
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   57	7	3	pearl	Lnet/minecraft/class_1684;
/*     */     //   45	24	2	handled	Lnet/minecraft/class_1297;
/*     */     //   0	93	0	this	Lshame/astra/client/modules/impl/player/TargetPearl;
/*     */     //   0	93	1	event	Lshame/astra/api/events/implement/EventUpdate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @EventLink
/*     */   public void onMoveInput(EventMoveInput event) {
/*  97 */     if (!isEnable() || !this.isThrowing || this.serverRotation == null) {
/*     */       return;
/*     */     }
/*     */     
/* 101 */     float forward = event.getForward();
/* 102 */     float strafe = event.getStrafe();
/* 103 */     if (forward == 0.0F && strafe == 0.0F) {
/*     */       return;
/*     */     }
/*     */     
/* 107 */     double targetAngle = class_3532.method_15338(Math.toDegrees(direction(this.serverRotation.field_1343, forward, strafe)));
/* 108 */     float bestForward = 0.0F;
/* 109 */     float bestStrafe = 0.0F;
/* 110 */     float smallestDifference = Float.MAX_VALUE;
/*     */     float testForward;
/* 112 */     for (testForward = -1.0F; testForward <= 1.0F; testForward++) {
/* 113 */       float testStrafe; for (testStrafe = -1.0F; testStrafe <= 1.0F; testStrafe++) {
/* 114 */         if (testForward != 0.0F || testStrafe != 0.0F) {
/*     */ 
/*     */ 
/*     */           
/* 118 */           double testAngle = class_3532.method_15338(Math.toDegrees(direction(this.serverRotation.field_1343, testForward, testStrafe)));
/* 119 */           float difference = Math.abs(class_3532.method_15393((float)(targetAngle - testAngle)));
/* 120 */           if (difference < smallestDifference) {
/* 121 */             smallestDifference = difference;
/* 122 */             bestForward = testForward;
/* 123 */             bestStrafe = testStrafe;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 128 */     event.setForward(bestForward);
/* 129 */     event.setStrafe(bestStrafe);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 134 */     resetThrowState();
/* 135 */     this.lastHandledPearlId = -1;
/* 136 */     this.nextThrowAt = 0L;
/* 137 */     this.timer.reset();
/* 138 */     super.onDisable();
/*     */   }
/*     */   
/*     */   private boolean canThrowNow() {
/* 142 */     if (System.currentTimeMillis() < this.nextThrowAt) {
/* 143 */       return false;
/*     */     }
/* 145 */     return (!mc.field_1724.method_7357().method_7904(new class_1799((class_1935)class_1802.field_8634)) && this.timer
/* 146 */       .finished(1000L));
/*     */   }
/*     */   
/*     */   private void aimAndThrowPearl() {
/* 150 */     class_243 landingPosition = getTargetPearlLandingPosition();
/* 151 */     if (landingPosition == null) {
/* 152 */       resetThrowState();
/*     */       
/*     */       return;
/*     */     } 
/* 156 */     float[] rotations = calculateYawPitch(landingPosition);
/* 157 */     if (rotations == null || Float.isNaN(rotations[0]) || Float.isNaN(rotations[1])) {
/* 158 */       resetThrowState();
/*     */       
/*     */       return;
/*     */     } 
/* 162 */     class_243 trajectoryLanding = checkTrajectory(rotations[0], rotations[1]);
/* 163 */     double allowedError = Math.max(3.0D, mc.field_1724.method_19538().method_1022(landingPosition) * 0.12D);
/* 164 */     if (trajectoryLanding == null || landingPosition.method_1022(trajectoryLanding) > allowedError) {
/* 165 */       resetThrowState();
/*     */       
/*     */       return;
/*     */     } 
/* 169 */     if (!hasPearl()) {
/* 170 */       resetThrowState();
/*     */       
/*     */       return;
/*     */     } 
/* 174 */     float previousYaw = mc.field_1724.method_36454();
/* 175 */     float previousPitch = mc.field_1724.method_36455();
/* 176 */     this.isThrowing = true;
/* 177 */     this.serverRotation = new class_241(rotations[0], rotations[1]);
/*     */     
/*     */     try {
/* 180 */       mc.field_1724.method_36456(rotations[0]);
/* 181 */       mc.field_1724.method_36457(rotations[1]);
/* 182 */       mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2831(rotations[0], rotations[1], mc.field_1724
/* 183 */             .method_24828(), mc.field_1724.field_5976));
/*     */       
/* 185 */       InventoryUtils.swapAndUseHvH(class_1802.field_8634);
/* 186 */       this.timer.reset();
/* 187 */       this.nextThrowAt = System.currentTimeMillis() + 2500L;
/* 188 */       if (this.targetPearl != null) {
/* 189 */         this.lastHandledPearlId = this.targetPearl.method_5628();
/*     */       }
/*     */     } finally {
/* 192 */       mc.field_1724.method_36456(previousYaw);
/* 193 */       mc.field_1724.method_36457(previousPitch);
/* 194 */       resetThrowState();
/*     */     } 
/*     */   }
/*     */   
/*     */   private class_243 getTargetPearlLandingPosition() {
/* 199 */     this.targetPearl = getTargetPearl();
/* 200 */     if (this.targetPearl == null || !this.targetPearl.method_5805()) {
/* 201 */       return null;
/*     */     }
/*     */     
/* 204 */     class_243 landingPos = predictPearlLanding(this.targetPearl);
/* 205 */     if (landingPos == null || !isWithinRange(landingPos)) {
/* 206 */       return null;
/*     */     }
/*     */     
/* 209 */     return landingPos;
/*     */   }
/*     */   
/*     */   private class_1684 getTargetPearl() {
/* 213 */     class_238 searchBox = mc.field_1724.method_5829().method_1014(256.0D);
/* 214 */     class_1309 auraTarget = (ModuleClass.INSTANCE != null) ? ModuleClass.aura.getTarget() : null;
/*     */     
/* 216 */     return mc.field_1687.method_8333((class_1297)mc.field_1724, searchBox, entity -> {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           // Byte code:
/*     */           //   0: aload_2
/*     */           //   1: instanceof net/minecraft/class_1684
/*     */           //   4: ifeq -> 80
/*     */           //   7: aload_2
/*     */           //   8: checkcast net/minecraft/class_1684
/*     */           //   11: astore_3
/*     */           //   12: aload_3
/*     */           //   13: invokevirtual method_5805 : ()Z
/*     */           //   16: ifeq -> 80
/*     */           //   19: aload_3
/*     */           //   20: invokevirtual method_24921 : ()Lnet/minecraft/class_1297;
/*     */           //   23: getstatic shame/astra/client/modules/impl/player/TargetPearl.mc : Lnet/minecraft/class_310;
/*     */           //   26: getfield field_1724 : Lnet/minecraft/class_746;
/*     */           //   29: if_acmpeq -> 80
/*     */           //   32: aload_3
/*     */           //   33: invokevirtual method_5628 : ()I
/*     */           //   36: aload_0
/*     */           //   37: getfield lastHandledPearlId : I
/*     */           //   40: if_icmpeq -> 80
/*     */           //   43: aload_0
/*     */           //   44: aload_3
/*     */           //   45: invokevirtual method_24921 : ()Lnet/minecraft/class_1297;
/*     */           //   48: invokevirtual isIgnoredFriend : (Lnet/minecraft/class_1297;)Z
/*     */           //   51: ifne -> 80
/*     */           //   54: aload_0
/*     */           //   55: getfield onlyTarget : Lshame/astra/client/modules/settings/implement/BooleanSetting;
/*     */           //   58: invokevirtual isState : ()Z
/*     */           //   61: ifeq -> 76
/*     */           //   64: aload_1
/*     */           //   65: ifnull -> 80
/*     */           //   68: aload_3
/*     */           //   69: invokevirtual method_24921 : ()Lnet/minecraft/class_1297;
/*     */           //   72: aload_1
/*     */           //   73: if_acmpne -> 80
/*     */           //   76: iconst_1
/*     */           //   77: goto -> 81
/*     */           //   80: iconst_0
/*     */           //   81: ireturn
/*     */           // Line number table:
/*     */           //   Java source line number -> byte code offset
/*     */           //   #221	-> 0
/*     */           //   #216	-> 7
/*     */           //   #217	-> 13
/*     */           //   #218	-> 20
/*     */           //   #219	-> 33
/*     */           //   #220	-> 45
/*     */           //   #221	-> 58
/*     */           // Local variable table:
/*     */           //   start	length	slot	name	descriptor
/*     */           //   12	68	3	pearl	Lnet/minecraft/class_1684;
/*     */           //   0	82	0	this	Lshame/astra/client/modules/impl/player/TargetPearl;
/*     */           //   0	82	1	auraTarget	Lnet/minecraft/class_1309;
/*     */           //   0	82	2	entity	Lnet/minecraft/class_1297;
/* 222 */         }).stream()
/* 223 */       .map(entity -> (class_1684)entity)
/* 224 */       .filter(pearl -> (getHorizontalDistanceTo(pearl) <= 256.0D))
/* 225 */       .min(Comparator.comparingDouble(this::getHorizontalDistanceTo))
/* 226 */       .orElse(null);
/*     */   }
/*     */   private boolean isIgnoredFriend(class_1297 owner) {
/*     */     class_1657 player;
/* 230 */     if (this.ignoreFriends.isState() && owner instanceof class_1657) { player = (class_1657)owner; }
/* 231 */     else { return false; }
/*     */     
/* 233 */     return (astra.INSTANCE != null && astra.INSTANCE.friendStorage != null && astra.INSTANCE.friendStorage
/*     */       
/* 235 */       .isFriend(player.method_5477().getString()));
/*     */   }
/*     */   
/*     */   private double getHorizontalDistanceTo(class_1684 pearl) {
/* 239 */     class_243 playerPos = mc.field_1724.method_19538();
/* 240 */     class_243 pearlPos = pearl.method_19538();
/* 241 */     double dx = pearlPos.field_1352 - playerPos.field_1352;
/* 242 */     double dz = pearlPos.field_1350 - playerPos.field_1350;
/* 243 */     return Math.sqrt(dx * dx + dz * dz);
/*     */   }
/*     */   
/*     */   private class_243 predictPearlLanding(class_1684 pearl) {
/* 247 */     class_243 position = pearl.method_19538();
/* 248 */     class_243 velocity = pearl.method_18798();
/* 249 */     class_243 lastPosition = position;
/*     */     
/* 251 */     for (int i = 0; i < 200; i++) {
/* 252 */       lastPosition = position;
/* 253 */       position = position.method_1019(velocity);
/*     */       
/* 255 */       if (hitsBlock(lastPosition, position) || position.field_1351 <= mc.field_1687.method_31607()) {
/* 256 */         return new class_243(class_3532.method_15357(lastPosition.field_1352) + 0.5D, class_3532.method_15357(lastPosition.field_1351), class_3532.method_15357(lastPosition.field_1350) + 0.5D);
/*     */       }
/*     */       
/* 259 */       velocity = updatePearlMotion(velocity, position);
/*     */     } 
/*     */     
/* 262 */     return new class_243(class_3532.method_15357(lastPosition.field_1352) + 0.5D, class_3532.method_15357(lastPosition.field_1351), class_3532.method_15357(lastPosition.field_1350) + 0.5D);
/*     */   }
/*     */   
/*     */   private class_243 updatePearlMotion(class_243 motion, class_243 position) {
/* 266 */     class_2338 blockPos = class_2338.method_49638((class_2374)position);
/* 267 */     if (mc.field_1687.method_8320(blockPos).method_27852(class_2246.field_10382)) {
/* 268 */       return motion.method_1021(0.8D).method_1031(0.0D, -0.03D, 0.0D);
/*     */     }
/* 270 */     return motion.method_1021(0.99D).method_1031(0.0D, -0.03D, 0.0D);
/*     */   }
/*     */   
/*     */   private boolean isWithinRange(class_243 landingPos) {
/* 274 */     double distanceToLanding = mc.field_1724.method_19538().method_1022(landingPos);
/* 275 */     return (distanceToLanding >= 11.0D && distanceToLanding <= 256.0D);
/*     */   }
/*     */   
/*     */   private float[] calculateYawPitch(class_243 targetPosition) {
/* 279 */     class_243 playerPosition = mc.field_1724.method_19538();
/* 280 */     double dx = targetPosition.field_1352 - playerPosition.field_1352;
/* 281 */     double dy = targetPosition.field_1351 - mc.field_1724.method_23320();
/* 282 */     double dz = targetPosition.field_1350 - playerPosition.field_1350;
/* 283 */     float yaw = (float)Math.toDegrees(Math.atan2(dz, dx)) - 90.0F;
/* 284 */     double horizontalDistance = Math.sqrt(dx * dx + dz * dz);
/* 285 */     double allowedError = Math.max(1.5D, mc.field_1724.method_19538().method_1022(targetPosition) * 0.08D);
/* 286 */     TrajectoryCandidate directCandidate = findBestCandidate(targetPosition, yaw, -25.0F, 35.0F, allowedError, true);
/* 287 */     if (directCandidate != null) {
/* 288 */       return new float[] { yaw, class_3532.method_15363(directCandidate.pitch, -90.0F, 90.0F) };
/*     */     }
/*     */     
/* 291 */     TrajectoryCandidate fallbackCandidate = findBestCandidate(targetPosition, yaw, -85.0F, 85.0F, allowedError, false);
/* 292 */     if (fallbackCandidate == null) {
/* 293 */       double fallbackPitch = -Math.toDegrees(Math.atan2(dy, horizontalDistance)) + 5.0D;
/* 294 */       return new float[] { yaw, class_3532.method_15363((float)fallbackPitch, -90.0F, 90.0F) };
/*     */     } 
/*     */     
/* 297 */     return new float[] { yaw, class_3532.method_15363(fallbackCandidate.pitch, -90.0F, 90.0F) };
/*     */   }
/*     */   
/*     */   private TrajectoryCandidate findBestCandidate(class_243 targetPosition, float yaw, float minPitch, float maxPitch, double allowedError, boolean preferDirect) {
/* 301 */     class_243 playerPosition = mc.field_1724.method_19538();
/* 302 */     double velocity = 1.5D;
/* 303 */     TrajectoryCandidate bestCandidate = null;
/*     */     float pitch;
/* 305 */     for (pitch = minPitch; pitch <= maxPitch; pitch += 0.25F) {
/* 306 */       float pitchRad = (float)Math.toRadians(pitch);
/* 307 */       double vx = (-class_3532.method_15374((float)Math.toRadians(yaw)) * class_3532.method_15362(pitchRad)) * velocity;
/* 308 */       double vy = -class_3532.method_15374(pitchRad) * velocity;
/* 309 */       double vz = (class_3532.method_15362((float)Math.toRadians(yaw)) * class_3532.method_15362(pitchRad)) * velocity;
/* 310 */       class_243 pos = new class_243(playerPosition.field_1352, mc.field_1724.method_23320(), playerPosition.field_1350);
/* 311 */       class_243 motion = new class_243(vx, vy, vz);
/*     */       
/* 313 */       int ticks = 0;
/* 314 */       for (int i = 0; i < 200; ) {
/* 315 */         class_243 previous = pos;
/* 316 */         pos = pos.method_1019(motion);
/* 317 */         motion = updatePearlMotion(motion, pos);
/* 318 */         ticks++;
/*     */         
/* 320 */         if (hitsEntity(previous, pos)) {
/*     */           break;
/*     */         }
/*     */         
/* 324 */         if (!hitsBlock(previous, pos) && pos.field_1351 > mc.field_1687.method_31607()) {
/*     */           i++;
/*     */           continue;
/*     */         } 
/* 328 */         double distanceToTarget = pos.method_1022(targetPosition);
/* 329 */         TrajectoryCandidate candidate = new TrajectoryCandidate(pitch, distanceToTarget, ticks, pos);
/* 330 */         if (isBetterCandidate(candidate, bestCandidate, allowedError, preferDirect)) {
/* 331 */           bestCandidate = candidate;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 337 */     if (bestCandidate == null || bestCandidate.distanceToTarget > allowedError) {
/* 338 */       return null;
/*     */     }
/* 340 */     return bestCandidate;
/*     */   }
/*     */   
/*     */   private boolean isBetterCandidate(TrajectoryCandidate candidate, TrajectoryCandidate currentBest, double allowedError, boolean preferDirect) {
/* 344 */     if (currentBest == null) {
/* 345 */       return true;
/*     */     }
/*     */     
/* 348 */     boolean candidateAccurate = (candidate.distanceToTarget <= allowedError);
/* 349 */     boolean bestAccurate = (currentBest.distanceToTarget <= allowedError);
/* 350 */     if (candidateAccurate != bestAccurate) {
/* 351 */       return candidateAccurate;
/*     */     }
/*     */     
/* 354 */     if (preferDirect && candidateAccurate && bestAccurate) {
/* 355 */       float candidatePitchAbs = Math.abs(candidate.pitch);
/* 356 */       float bestPitchAbs = Math.abs(currentBest.pitch);
/* 357 */       if (Math.abs(candidatePitchAbs - bestPitchAbs) > 0.01F) {
/* 358 */         return (candidatePitchAbs < bestPitchAbs);
/*     */       }
/* 360 */       if (candidate.ticks != currentBest.ticks) {
/* 361 */         return (candidate.ticks < currentBest.ticks);
/*     */       }
/*     */     } 
/*     */     
/* 365 */     if (Math.abs(candidate.distanceToTarget - currentBest.distanceToTarget) > 0.01D) {
/* 366 */       return (candidate.distanceToTarget < currentBest.distanceToTarget);
/*     */     }
/*     */     
/* 369 */     if (candidate.ticks != currentBest.ticks) {
/* 370 */       return (candidate.ticks < currentBest.ticks);
/*     */     }
/*     */     
/* 373 */     if (!preferDirect) {
/* 374 */       return (Math.abs(candidate.pitch) < Math.abs(currentBest.pitch));
/*     */     }
/*     */     
/* 377 */     return false;
/*     */   }
/*     */   
/*     */   private class_243 checkTrajectory(float yaw, float pitch) {
/* 381 */     float yawRad = (float)Math.toRadians(yaw);
/* 382 */     float pitchRad = (float)Math.toRadians(pitch);
/* 383 */     double velocity = 1.5D;
/*     */     
/* 385 */     double x = mc.field_1724.method_23317() - (class_3532.method_15362(yawRad) * 0.16F);
/* 386 */     double y = mc.field_1724.method_23318() + mc.field_1724.method_18381(mc.field_1724.method_18376()) - 0.1D;
/* 387 */     double z = mc.field_1724.method_23321() - (class_3532.method_15374(yawRad) * 0.16F);
/*     */     
/* 389 */     double motionX = (-class_3532.method_15374(yawRad) * class_3532.method_15362(pitchRad)) * velocity;
/* 390 */     double motionY = -class_3532.method_15374(pitchRad) * velocity;
/* 391 */     double motionZ = (class_3532.method_15362(yawRad) * class_3532.method_15362(pitchRad)) * velocity;
/*     */     
/* 393 */     class_243 position = new class_243(x, y, z);
/* 394 */     class_243 motion = new class_243(motionX, motionY, motionZ);
/*     */     
/* 396 */     for (int i = 0; i <= 200; i++) {
/* 397 */       class_243 previous = position;
/* 398 */       position = position.method_1019(motion);
/* 399 */       motion = updatePearlMotion(motion, position);
/*     */       
/* 401 */       if (hitsEntity(previous, position)) {
/* 402 */         return null;
/*     */       }
/*     */       
/* 405 */       if (hitsBlock(previous, position) || position.field_1351 <= mc.field_1687.method_31607()) {
/* 406 */         return new class_243(class_3532.method_15357(position.field_1352) + 0.5D, class_3532.method_15357(position.field_1351), class_3532.method_15357(position.field_1350) + 0.5D);
/*     */       }
/*     */     } 
/*     */     
/* 410 */     return null;
/*     */   }
/*     */   
/*     */   private boolean hitsBlock(class_243 from, class_243 to) {
/* 414 */     return 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 420 */       (mc.field_1687.method_17742(new class_3959(from, to, class_3959.class_3960.field_17558, class_3959.class_242.field_1348, (class_1297)mc.field_1724)).method_17783() == class_239.class_240.field_1332);
/*     */   }
/*     */   
/*     */   private boolean hitsEntity(class_243 from, class_243 to) {
/* 424 */     class_238 searchBox = (new class_238(from, to)).method_1014(0.3D);
/* 425 */     for (class_1297 entity : mc.field_1687.method_8333((class_1297)mc.field_1724, searchBox, entity -> 
/* 426 */         (!entity.method_5805() || entity.method_7325() || entity.field_5960) ? false : ((entity == this.targetPearl) ? false : (!(entity instanceof class_1684))))) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 434 */       if (entity.method_5829().method_1014(0.25D).method_992(from, to).isPresent()) {
/* 435 */         return true;
/*     */       }
/*     */     } 
/* 438 */     return false;
/*     */   }
/*     */   
/*     */   private boolean hasPearl() {
/* 442 */     return (mc.field_1724.method_6047().method_31574(class_1802.field_8634) || mc.field_1724
/* 443 */       .method_6079().method_31574(class_1802.field_8634) || 
/* 444 */       InventoryUtils.find(class_1802.field_8634, 0, 8) != -1 || 
/* 445 */       InventoryUtils.find(class_1802.field_8634, 9, 45) != -1);
/*     */   }
/*     */   
/*     */   private void resetThrowState() {
/* 449 */     this.isThrowing = false;
/* 450 */     this.targetPearl = null;
/* 451 */     this.serverRotation = null;
/*     */   }
/*     */   
/*     */   private static double direction(float rotationYaw, float moveForward, float moveStrafing) {
/* 455 */     if (moveForward < 0.0F) rotationYaw += 180.0F; 
/* 456 */     float forward = 1.0F;
/* 457 */     if (moveForward < 0.0F) { forward = -0.5F; }
/* 458 */     else if (moveForward > 0.0F) { forward = 0.5F; }
/* 459 */      if (moveStrafing > 0.0F) rotationYaw -= 90.0F * forward; 
/* 460 */     if (moveStrafing < 0.0F) rotationYaw += 90.0F * forward; 
/* 461 */     return Math.toRadians(rotationYaw);
/*     */   }
/*     */   
/*     */   private static final class TrajectoryCandidate {
/*     */     private final float pitch;
/*     */     private final double distanceToTarget;
/*     */     private final int ticks;
/*     */     private final class_243 landingPos;
/*     */     
/*     */     private TrajectoryCandidate(float pitch, double distanceToTarget, int ticks, class_243 landingPos) {
/* 471 */       this.pitch = pitch;
/* 472 */       this.distanceToTarget = distanceToTarget;
/* 473 */       this.ticks = ticks;
/* 474 */       this.landingPos = landingPos;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\player\TargetPearl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */