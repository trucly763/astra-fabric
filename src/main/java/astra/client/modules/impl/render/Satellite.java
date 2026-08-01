/*     */ package shame.astra.client.modules.impl.render;
/*     */ 
/*     */ import net.minecraft.class_10055;
/*     */ import net.minecraft.class_1297;
/*     */ import net.minecraft.class_1309;
/*     */ import net.minecraft.class_1657;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_2960;
/*     */ import net.minecraft.class_3532;
/*     */ import net.minecraft.class_4050;
/*     */ import net.minecraft.class_4587;
/*     */ import net.minecraft.class_4588;
/*     */ import net.minecraft.class_4597;
/*     */ import net.minecraft.class_4608;
/*     */ import net.minecraft.class_5602;
/*     */ import net.minecraft.class_7308;
/*     */ import net.minecraft.class_7833;
/*     */ import net.minecraft.class_9996;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.Event3DRender;
/*     */ import shame.astra.api.events.implement.EventAttackEntity;
/*     */ import shame.astra.api.events.implement.EventUpdate;
/*     */ import shame.astra.astra;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*     */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*     */ import shame.astra.client.modules.settings.implement.ModeSetting;
/*     */ 
/*     */ public class Satellite
/*     */   extends Module {
/*  32 */   private static final class_2960 ALLAY_TEXTURE = class_2960.method_60656("textures/entity/allay/allay.png");
/*     */   private static final long ATTACK_FOLLOW_TIMEOUT_MS = 3600L;
/*     */   private static final long ATTACK_LAUNCH_DURATION_MS = 560L;
/*     */   private static final long ATTACK_RETURN_DURATION_MS = 920L;
/*  36 */   public static Satellite INSTANCE = new Satellite();
/*     */   
/*  38 */   public final ModeSetting shoulder = new ModeSetting("Плечо", "Правое", new String[] { "Правое", "Левое" });
/*  39 */   public final FloatSetting scale = new FloatSetting("Размер", 0.38F, 0.15F, 1.25F, 0.01F);
/*  40 */   public final FloatSetting offsetX = new FloatSetting("Смещение X", 0.0F, -1.0F, 1.0F, 0.01F);
/*  41 */   public final FloatSetting offsetY = new FloatSetting("Смещение Y", 0.18F, -1.0F, 1.0F, 0.01F);
/*  42 */   public final FloatSetting offsetZ = new FloatSetting("Смещение Z", 0.0F, -1.0F, 1.0F, 0.01F);
/*  43 */   public final FloatSetting rotateX = new FloatSetting("Поворот X", 0.0F, -180.0F, 180.0F, 1.0F);
/*  44 */   public final FloatSetting rotateY = new FloatSetting("Поворот Y", 0.0F, -180.0F, 180.0F, 1.0F);
/*  45 */   public final FloatSetting rotateZ = new FloatSetting("Поворот Z", 0.0F, -180.0F, 180.0F, 1.0F);
/*  46 */   public final BooleanSetting showSelf = new BooleanSetting("Показывать на себе", true);
/*  47 */   public final BooleanSetting showOthers = new BooleanSetting("Показывать на других", true);
/*  48 */   public final BooleanSetting showFriends = new BooleanSetting("Показывать на друзьях", true);
/*  49 */   public final BooleanSetting attackEnemies = new BooleanSetting("Атаковать врагов", true);
/*  50 */   public final BooleanSetting idleAnimation = new BooleanSetting("Idle-анимация", true);
/*  51 */   public final FloatSetting idleSpeed = (new FloatSetting("Скорость idle", 1.0F, 0.1F, 3.0F, 0.05F))
/*  52 */     .visible(() -> Boolean.valueOf(this.idleAnimation.isState()));
/*  53 */   public final FloatSetting idleStrength = (new FloatSetting("Сила idle", 0.35F, 0.0F, 1.5F, 0.05F))
/*  54 */     .visible(() -> Boolean.valueOf(this.idleAnimation.isState()));
/*     */   
/*  56 */   private final class_9996 attackState = new class_9996();
/*     */   private class_7308 attackModel;
/*  58 */   private int attackTargetId = Integer.MIN_VALUE;
/*     */   private long attackStartedAt;
/*     */   private long lastAttackAt;
/*     */   private long attackReturnStartedAt;
/*  62 */   private class_243 attackReturnStartPos = new class_243(0.0D, 0.0D, 0.0D);
/*     */   private float attackOrbitSeed;
/*     */   private float attackCurveSide;
/*     */   private float attackCurveLift;
/*     */   private float attackCurveDepth;
/*     */   private float attackRadiusJitter;
/*     */   private float attackHeightJitter;
/*     */   private float attackBobSeed;
/*     */   private float attackOrbitSpeed;
/*     */   private float attackOrbitDirection;
/*     */   private float attackLookYaw;
/*     */   private float attackLookPitch;
/*     */   private boolean attackLookInitialized;
/*     */   
/*     */   public Satellite() {
/*  77 */     super("Satellite", "Питомец-аллей на плече", Module.ModuleCategory.RENDER);
/*  78 */     addSettings(new Setting[] { (Setting)this.shoulder, (Setting)this.scale, (Setting)this.offsetX, (Setting)this.offsetY, (Setting)this.offsetZ, (Setting)this.rotateX, (Setting)this.rotateY, (Setting)this.rotateZ, (Setting)this.showSelf, (Setting)this.showOthers, (Setting)this.showFriends, (Setting)this.attackEnemies, (Setting)this.idleAnimation, (Setting)this.idleSpeed, (Setting)this.idleStrength });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  99 */     clearAttackTarget();
/* 100 */     super.onDisable();
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onAttack(EventAttackEntity event) {
/* 105 */     if (!this.attackEnemies.isState() || event == null || event.getPlayer() == null || event.getTarget() == null || mc.field_1724 == null) {
/*     */       return;
/*     */     }
/*     */     
/* 109 */     if (event.getPlayer().method_5628() != mc.field_1724.method_5628() || event.getTarget() == mc.field_1724) {
/*     */       return;
/*     */     }
/*     */     
/* 113 */     long now = System.currentTimeMillis();
/* 114 */     if (this.attackTargetId != event.getTarget().method_5628()) {
/* 115 */       this.attackStartedAt = now;
/* 116 */       randomizeAttackPath(now);
/*     */     } 
/*     */     
/* 119 */     this.attackTargetId = event.getTarget().method_5628();
/* 120 */     this.lastAttackAt = now;
/* 121 */     this.attackReturnStartedAt = 0L;
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onUpdate(EventUpdate event) {
/* 126 */     if (!this.attackEnemies.isState()) {
/* 127 */       clearAttackTarget();
/*     */       
/*     */       return;
/*     */     } 
/* 131 */     updateAttackLifecycle();
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onRender3D(Event3DRender event) {
/* 136 */     if (mc.field_1724 == null || mc.field_1687 == null || event == null) {
/*     */       return;
/*     */     }
/*     */     
/* 140 */     float tickDelta = event.getTickDelta();
/* 141 */     long now = System.currentTimeMillis();
/*     */     
/* 143 */     class_1297 target = updateAttackLifecycle();
/* 144 */     if (target == null) {
/*     */       return;
/*     */     }
/*     */     
/* 148 */     ensureAttackModel();
/* 149 */     if (this.attackModel == null) {
/*     */       return;
/*     */     }
/*     */     
/* 153 */     renderAttackSatellite(event, target, getAttackRenderPosition(target, tickDelta, now), tickDelta, now);
/*     */   }
/*     */   
/*     */   private void renderAttackSatellite(Event3DRender event, class_1297 target, class_243 renderPos, float tickDelta, long now) {
/* 157 */     class_243 cameraPos = event.getCamera().method_19326();
/* 158 */     class_243 targetPos = getInterpolatedEntityPos(target, tickDelta);
/* 159 */     float elapsed = (float)(now - this.attackStartedAt) / 1000.0F;
/*     */     
/* 161 */     class_243 focusPos = targetPos.method_1031(0.0D, target.method_17682() * 0.56D, 0.0D);
/* 162 */     float desiredYaw = getLookYaw(renderPos, focusPos);
/* 163 */     float desiredPitch = getLookPitch(renderPos, focusPos);
/*     */     
/* 165 */     if (!this.attackLookInitialized) {
/* 166 */       this.attackLookYaw = desiredYaw;
/* 167 */       this.attackLookPitch = desiredPitch;
/* 168 */       this.attackLookInitialized = true;
/*     */     } else {
/* 170 */       this.attackLookYaw = class_3532.method_17821(0.32F, this.attackLookYaw, desiredYaw);
/* 171 */       this.attackLookPitch = class_3532.method_16439(0.24F, this.attackLookPitch, desiredPitch);
/*     */     } 
/*     */     
/* 174 */     float headYaw = class_3532.method_15363(class_3532.method_15393(desiredYaw - this.attackLookYaw), -85.0F, 85.0F);
/*     */     
/* 176 */     class_4587 matrices = event.getMatrices();
/* 177 */     matrices.method_22903();
/* 178 */     matrices.method_22904(renderPos.field_1352 - cameraPos.field_1352, renderPos.field_1351 - cameraPos.field_1351, renderPos.field_1350 - cameraPos.field_1350);
/* 179 */     matrices.method_22907(class_7833.field_40716.rotationDegrees(180.0F - this.attackLookYaw));
/* 180 */     matrices.method_22905(this.scale.get(), this.scale.get(), this.scale.get());
/* 181 */     matrices.method_22905(-1.0F, -1.0F, 1.0F);
/* 182 */     matrices.method_46416(0.0F, -1.501F, 0.0F);
/*     */     
/* 184 */     this.attackState.field_53328 = mc.field_1724.field_6012 + tickDelta + elapsed * 20.0F;
/* 185 */     this.attackState.field_53450 = elapsed * 6.4F;
/* 186 */     this.attackState.field_53451 = 0.72F + class_3532.method_15374(elapsed * 7.0F + this.attackBobSeed) * 0.12F;
/* 187 */     this.attackState.field_53447 = headYaw;
/* 188 */     this.attackState.field_53448 = this.attackLookPitch;
/* 189 */     this.attackState.field_53333 = false;
/* 190 */     this.attackState.field_53461 = false;
/* 191 */     this.attackState.field_53462 = false;
/* 192 */     this.attackState.field_53456 = false;
/* 193 */     this.attackState.field_53457 = false;
/* 194 */     this.attackState.field_53458 = target.method_5799();
/* 195 */     this.attackState.field_53446 = this.attackLookYaw;
/* 196 */     this.attackState.field_53453 = 1.0F;
/* 197 */     this.attackState.field_53454 = 1.0F;
/* 198 */     class_1309 living = (class_1309)target; this.attackState.field_53465 = (target instanceof class_1309) ? living.method_18376() : class_4050.field_18076;
/* 199 */     this.attackState.field_53449 = 0.0F;
/* 200 */     this.attackState.field_53460 = false;
/* 201 */     this.attackState.field_53237 = false;
/* 202 */     this.attackState.field_53238 = false;
/* 203 */     this.attackState.field_53239 = 0.0F;
/* 204 */     this.attackState.field_53240 = 0.65F;
/*     */     
/* 206 */     this.attackModel.method_42732(this.attackState);
/* 207 */     class_4597.class_4598 immediate = mc.method_22940().method_23000();
/* 208 */     class_4588 vertexConsumer = immediate.getBuffer(this.attackModel.method_23500(ALLAY_TEXTURE));
/* 209 */     this.attackModel.method_60879(matrices, vertexConsumer, 15728880, class_4608.field_21444);
/* 210 */     immediate.method_22993();
/* 211 */     matrices.method_22909();
/*     */   }
/*     */   
/*     */   public boolean shouldRender(class_10055 playerState) {
/* 215 */     if (!isEnable() || mc.field_1724 == null || mc.field_1687 == null || playerState == null || playerState.field_53542) {
/* 216 */       return false;
/*     */     }
/*     */     
/* 219 */     boolean self = (playerState.field_53528 == mc.field_1724.method_5628());
/* 220 */     if (self) {
/* 221 */       if (hasActiveAttackTarget()) {
/* 222 */         return false;
/*     */       }
/*     */       
/* 225 */       return shouldRenderOwnShoulderPet();
/*     */     } 
/*     */     
/* 228 */     class_1297 entity = mc.field_1687.method_8469(playerState.field_53528);
/* 229 */     if (entity instanceof class_1657) { class_1657 player = (class_1657)entity; if (astra.INSTANCE != null && astra.INSTANCE.friendStorage != null && astra.INSTANCE.friendStorage
/*     */ 
/*     */         
/* 232 */         .isFriend(player.method_5477().getString())) {
/* 233 */         return this.showFriends.isState();
/*     */       } }
/*     */     
/* 236 */     return this.showOthers.isState();
/*     */   }
/*     */   
/*     */   public boolean isLeftShoulder() {
/* 240 */     return this.shoulder.is("Левое");
/*     */   }
/*     */   
/*     */   public boolean hasActiveAttackTarget() {
/* 244 */     return (updateAttackLifecycle() != null);
/*     */   }
/*     */   
/*     */   private boolean shouldRenderOwnShoulderPet() {
/* 248 */     return (this.showSelf.isState() && !mc.field_1690.method_31044().method_31034());
/*     */   }
/*     */   
/*     */   private class_1297 updateAttackLifecycle() {
/* 252 */     if (!this.attackEnemies.isState() || mc.field_1687 == null || mc.field_1724 == null || this.attackTargetId == Integer.MIN_VALUE) {
/* 253 */       return null;
/*     */     }
/*     */     
/* 256 */     class_1297 target = mc.field_1687.method_8469(this.attackTargetId);
/* 257 */     if (target == null || target.method_31481() || target == mc.field_1724) {
/* 258 */       clearAttackTarget();
/* 259 */       return null;
/*     */     } 
/*     */     
/* 262 */     if (target instanceof class_1309) { class_1309 living = (class_1309)target; if (!living.method_5805()) {
/* 263 */         clearAttackTarget();
/* 264 */         return null;
/*     */       }  }
/*     */     
/* 267 */     if (mc.field_1724.method_5858(target) > 4096.0D) {
/* 268 */       clearAttackTarget();
/* 269 */       return null;
/*     */     } 
/*     */     
/* 272 */     long now = System.currentTimeMillis();
/* 273 */     if (this.attackReturnStartedAt == 0L && now - this.lastAttackAt > 3600L) {
/* 274 */       float elapsed = (float)(now - this.attackStartedAt) / 1000.0F;
/* 275 */       this.attackReturnStartPos = getOrbitPosition(target, getInterpolatedEntityPos(target, 1.0F), elapsed);
/* 276 */       this.attackReturnStartedAt = now;
/*     */     } 
/*     */     
/* 279 */     if (this.attackReturnStartedAt != 0L && now - this.attackReturnStartedAt > 920L) {
/* 280 */       clearAttackTarget();
/* 281 */       return null;
/*     */     } 
/*     */     
/* 284 */     return target;
/*     */   }
/*     */   
/*     */   private class_243 getAttackRenderPosition(class_1297 target, float tickDelta, long now) {
/* 288 */     class_243 shoulderPos = getShoulderWorldPosition(tickDelta);
/* 289 */     class_243 targetPos = getInterpolatedEntityPos(target, tickDelta);
/* 290 */     float elapsed = (float)(now - this.attackStartedAt) / 1000.0F;
/*     */     
/* 292 */     class_243 orbitPos = getOrbitPosition(target, targetPos, elapsed);
/* 293 */     if (this.attackReturnStartedAt == 0L) {
/* 294 */       float launchProgress = class_3532.method_15363((float)(now - this.attackStartedAt) / 560.0F, 0.0F, 1.0F);
/* 295 */       if (launchProgress < 1.0F) {
/* 296 */         return buildLaunchCurve(shoulderPos, orbitPos, launchProgress);
/*     */       }
/* 298 */       return orbitPos;
/*     */     } 
/*     */     
/* 301 */     float returnProgress = class_3532.method_15363((float)(now - this.attackReturnStartedAt) / 920.0F, 0.0F, 1.0F);
/* 302 */     return buildReturnCurve(this.attackReturnStartPos, shoulderPos, returnProgress);
/*     */   }
/*     */   
/*     */   private class_243 getOrbitPosition(class_1297 target, class_243 targetPos, float elapsed) {
/* 306 */     double baseRadius = Math.max(0.86D, target.method_17681() * 1.05D + 0.46D) * this.attackRadiusJitter;
/* 307 */     double angle = (this.attackOrbitSeed * 0.017453292F + elapsed * this.attackOrbitSpeed * this.attackOrbitDirection);
/* 308 */     double radiusPulse = Math.sin((elapsed * 1.25F + this.attackBobSeed * 0.45F)) * 0.07D;
/* 309 */     double orbitRadius = baseRadius + radiusPulse;
/* 310 */     double orbitX = Math.cos(angle) * orbitRadius;
/* 311 */     double orbitZ = Math.sin(angle) * orbitRadius;
/*     */ 
/*     */ 
/*     */     
/* 315 */     double orbitY = targetPos.field_1351 + target.method_17682() * (0.78D + this.attackHeightJitter) + Math.sin((elapsed * 2.9F + this.attackBobSeed)) * 0.2D + Math.cos((elapsed * 1.8F + this.attackBobSeed * 0.8F)) * 0.08D;
/* 316 */     return new class_243(targetPos.field_1352 + orbitX, orbitY, targetPos.field_1350 + orbitZ);
/*     */   }
/*     */   
/*     */   private class_243 buildLaunchCurve(class_243 start, class_243 end, float progress) {
/* 320 */     float eased = easeInOut(progress);
/* 321 */     class_243 direction = end.method_1020(start);
/* 322 */     class_243 horizontal = new class_243(direction.field_1352, 0.0D, direction.field_1350);
/* 323 */     if (horizontal.method_1027() < 1.0E-4D) {
/* 324 */       horizontal = new class_243(0.0D, 0.0D, 1.0D);
/*     */     } else {
/* 326 */       horizontal = horizontal.method_1029();
/*     */     } 
/*     */     
/* 329 */     class_243 sideways = (new class_243(horizontal.field_1350, 0.0D, -horizontal.field_1352)).method_1029();
/* 330 */     class_243 lift = new class_243(0.0D, this.attackCurveLift, 0.0D);
/* 331 */     class_243 control1 = start.method_1019(sideways.method_1021(this.attackCurveSide * 0.52D)).method_1019(lift.method_1021(0.82D));
/* 332 */     class_243 control2 = end.method_1019(sideways.method_1021(-this.attackCurveSide * 0.28D)).method_1019(horizontal.method_1021(this.attackCurveDepth * 0.18D)).method_1019(lift.method_1021(0.58D));
/* 333 */     return cubicBezier(start, control1, control2, end, eased);
/*     */   }
/*     */   
/*     */   private class_243 buildReturnCurve(class_243 start, class_243 end, float progress) {
/* 337 */     float eased = easeInOut(progress);
/* 338 */     class_243 direction = end.method_1020(start);
/* 339 */     class_243 horizontal = new class_243(direction.field_1352, 0.0D, direction.field_1350);
/* 340 */     if (horizontal.method_1027() < 1.0E-4D) {
/* 341 */       horizontal = new class_243(0.0D, 0.0D, 1.0D);
/*     */     } else {
/* 343 */       horizontal = horizontal.method_1029();
/*     */     } 
/*     */     
/* 346 */     class_243 sideways = (new class_243(horizontal.field_1350, 0.0D, -horizontal.field_1352)).method_1029();
/* 347 */     class_243 lift = new class_243(0.0D, this.attackCurveLift * 0.72D, 0.0D);
/* 348 */     class_243 control1 = start.method_1019(sideways.method_1021(-this.attackCurveSide * 0.24D)).method_1019(lift.method_1021(0.62D));
/* 349 */     class_243 control2 = end.method_1019(sideways.method_1021(this.attackCurveSide * 0.3D)).method_1019(horizontal.method_1021(-this.attackCurveDepth * 0.1D)).method_1019(lift.method_1021(0.22D));
/* 350 */     class_243 bezier = cubicBezier(start, control1, control2, end, eased);
/* 351 */     return (eased > 0.985F) ? end : bezier;
/*     */   }
/*     */   
/*     */   private class_243 getShoulderWorldPosition(float tickDelta) {
/* 355 */     class_243 playerPos = getInterpolatedEntityPos((class_1297)mc.field_1724, tickDelta);
/* 356 */     float bodyYaw = class_3532.method_17821(tickDelta, mc.field_1724.field_6220, mc.field_1724.field_6283);
/* 357 */     float yawRad = bodyYaw * 0.017453292F;
/*     */     
/* 359 */     class_243 forward = new class_243(-class_3532.method_15374(yawRad), 0.0D, class_3532.method_15362(yawRad));
/* 360 */     class_243 right = new class_243(forward.field_1350, 0.0D, -forward.field_1352);
/* 361 */     double side = (isLeftShoulder() ? 1.0D : -1.0D) * mc.field_1724.method_17681() * 0.42D;
/* 362 */     double height = mc.field_1724.method_17682() - (mc.field_1724.method_5715() ? 0.38D : 0.24D);
/* 363 */     double back = 0.0D;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 371 */     class_243 shoulderPos = playerPos.method_1031(0.0D, height, 0.0D).method_1019(right.method_1021(side)).method_1019(forward.method_1021(back)).method_1019(right.method_1021(this.offsetX.get() * 0.65D)).method_1031(0.0D, this.offsetY.get() * 0.45D, 0.0D).method_1019(forward.method_1021(this.offsetZ.get() * 0.35D));
/*     */     
/* 373 */     if (this.idleAnimation.isState()) {
/* 374 */       float time = (mc.field_1724.field_6012 + tickDelta) * (0.7F + this.idleSpeed.get() * 0.65F);
/* 375 */       float bob = class_3532.method_15374(time * 0.42F) * 0.03F * this.idleStrength.get();
/* 376 */       shoulderPos = shoulderPos.method_1031(0.0D, bob, 0.0D);
/*     */     } 
/*     */     
/* 379 */     return shoulderPos;
/*     */   }
/*     */   
/*     */   private class_243 getInterpolatedEntityPos(class_1297 entity, float tickDelta) {
/* 383 */     return new class_243(
/* 384 */         class_3532.method_16436(tickDelta, entity.field_6014, entity.method_23317()), 
/* 385 */         class_3532.method_16436(tickDelta, entity.field_6036, entity.method_23318()), 
/* 386 */         class_3532.method_16436(tickDelta, entity.field_5969, entity.method_23321()));
/*     */   }
/*     */ 
/*     */   
/*     */   private class_243 cubicBezier(class_243 p0, class_243 p1, class_243 p2, class_243 p3, float t) {
/* 391 */     float inv = 1.0F - t;
/* 392 */     double w0 = (inv * inv * inv);
/* 393 */     double w1 = 3.0D * inv * inv * t;
/* 394 */     double w2 = 3.0D * inv * t * t;
/* 395 */     double w3 = (t * t * t);
/* 396 */     return new class_243(p0.field_1352 * w0 + p1.field_1352 * w1 + p2.field_1352 * w2 + p3.field_1352 * w3, p0.field_1351 * w0 + p1.field_1351 * w1 + p2.field_1351 * w2 + p3.field_1351 * w3, p0.field_1350 * w0 + p1.field_1350 * w1 + p2.field_1350 * w2 + p3.field_1350 * w3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private float easeInOut(float value) {
/* 404 */     float clamped = class_3532.method_15363(value, 0.0F, 1.0F);
/* 405 */     return clamped * clamped * clamped * (clamped * (clamped * 6.0F - 15.0F) + 10.0F);
/*     */   }
/*     */   
/*     */   private void ensureAttackModel() {
/* 409 */     if (this.attackModel != null || mc == null) {
/*     */       return;
/*     */     }
/*     */     
/* 413 */     this.attackModel = new class_7308(mc.method_31974().method_32072(class_5602.field_38455));
/*     */   }
/*     */   
/*     */   private void randomizeAttackPath(long now) {
/* 417 */     this.attackOrbitSeed = randomRange(0.0F, 360.0F);
/* 418 */     this.attackCurveSide = randomRange(-1.1F, 1.1F);
/* 419 */     this.attackCurveLift = randomRange(0.48F, 0.96F);
/* 420 */     this.attackCurveDepth = randomRange(-0.42F, 0.42F);
/* 421 */     this.attackRadiusJitter = randomRange(0.92F, 1.24F);
/* 422 */     this.attackHeightJitter = randomRange(-0.06F, 0.14F);
/* 423 */     this.attackBobSeed = randomRange(0.0F, 6.2831855F);
/* 424 */     this.attackOrbitSpeed = randomRange(1.7F, 2.45F);
/* 425 */     this.attackOrbitDirection = (Math.random() > 0.5D) ? 1.0F : -1.0F;
/*     */   }
/*     */   
/*     */   private float randomRange(float min, float max) {
/* 429 */     return min + (float)Math.random() * (max - min);
/*     */   }
/*     */   
/*     */   private float getLookYaw(class_243 from, class_243 to) {
/* 433 */     double dx = to.field_1352 - from.field_1352;
/* 434 */     double dz = to.field_1350 - from.field_1350;
/* 435 */     return (float)Math.toDegrees(Math.atan2(dz, dx)) - 90.0F;
/*     */   }
/*     */   
/*     */   private float getLookPitch(class_243 from, class_243 to) {
/* 439 */     double dx = to.field_1352 - from.field_1352;
/* 440 */     double dy = to.field_1351 - from.field_1351;
/* 441 */     double dz = to.field_1350 - from.field_1350;
/* 442 */     double horizontalDistance = Math.sqrt(dx * dx + dz * dz);
/* 443 */     return class_3532.method_15363((float)-Math.toDegrees(Math.atan2(dy, horizontalDistance)), -35.0F, 35.0F);
/*     */   }
/*     */   
/*     */   private void clearAttackTarget() {
/* 447 */     this.attackTargetId = Integer.MIN_VALUE;
/* 448 */     this.attackStartedAt = 0L;
/* 449 */     this.lastAttackAt = 0L;
/* 450 */     this.attackReturnStartedAt = 0L;
/* 451 */     this.attackReturnStartPos = new class_243(0.0D, 0.0D, 0.0D);
/* 452 */     this.attackLookYaw = 0.0F;
/* 453 */     this.attackLookPitch = 0.0F;
/* 454 */     this.attackLookInitialized = false;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\Satellite.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */