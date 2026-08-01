/*     */ package shame.astra.client.modules.impl.render;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import net.minecraft.class_10142;
/*     */ import net.minecraft.class_1304;
/*     */ import net.minecraft.class_1657;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_286;
/*     */ import net.minecraft.class_287;
/*     */ import net.minecraft.class_289;
/*     */ import net.minecraft.class_290;
/*     */ import net.minecraft.class_293;
/*     */ import net.minecraft.class_2960;
/*     */ import net.minecraft.class_3532;
/*     */ import net.minecraft.class_3882;
/*     */ import net.minecraft.class_4050;
/*     */ import net.minecraft.class_4587;
/*     */ import net.minecraft.class_4597;
/*     */ import net.minecraft.class_7833;
/*     */ import org.joml.Matrix4f;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.Event3DRender;
/*     */ import shame.astra.api.storages.implement.helpertstorages.Theme;
/*     */ import shame.astra.api.utils.color.ColorUtils;
/*     */ import shame.astra.astra;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*     */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*     */ import shame.astra.client.modules.settings.implement.ListSetting;
/*     */ 
/*     */ public class Cosmetics extends Module {
/*  33 */   public static Cosmetics INSTANCE = new Cosmetics();
/*     */   private static final float PI_STEP = 0.06981317F;
/*     */   private static final float WING_SCALE = 1.0F;
/*     */   private static final float FLAP_SPEED = 1.6F;
/*     */   private static final float FLAP_AMPLITUDE = 25.0F;
/*     */   private static final int NIMBUS_ARMS = 2;
/*     */   private static final int NIMBUS_SEGMENTS = 17;
/*     */   private static final float NIMBUS_RADIUS = 0.45F;
/*     */   private static final float NIMBUS_BASE_SIZE = 0.23F;
/*     */   private static final double NIMBUS_STEP_RADIANS = 0.11D;
/*     */   private static final int NIMBUS_MAX_ALPHA = 255;
/*     */   private static final int NIMBUS_ALPHA_FALLOFF = 9;
/*     */   private static final float NIMBUS_SPEED = 170.0F;
/*     */   private static final float CLASSIC_WING_DEFAULT_SPREAD = 8.0F;
/*     */   private static final int CLASSIC_WING_DEFAULT_ALPHA = 220;
/*  48 */   private static final ClassicWingPoint[] CLASSIC_WING_SHAPE = new ClassicWingPoint[] { new ClassicWingPoint(0.08F, 0.1F, 0.88F), new ClassicWingPoint(0.28F, 0.34F, 0.78F), new ClassicWingPoint(0.56F, 0.82F, 0.62F), new ClassicWingPoint(0.86F, 0.3F, 0.52F), new ClassicWingPoint(1.14F, 0.46F, 0.4F), new ClassicWingPoint(1.24F, 0.04F, 0.3F), new ClassicWingPoint(1.02F, -0.18F, 0.28F), new ClassicWingPoint(1.18F, -0.64F, 0.22F), new ClassicWingPoint(0.86F, -0.46F, 0.2F), new ClassicWingPoint(0.8F, -0.98F, 0.14F), new ClassicWingPoint(0.54F, -0.74F, 0.16F), new ClassicWingPoint(0.3F, -1.16F, 0.12F), new ClassicWingPoint(0.1F, -0.54F, 0.18F) };
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
/*  64 */   private final ListSetting cosmetics = new ListSetting("Косметика", new BooleanSetting[] { new BooleanSetting("Нимб", true), new BooleanSetting("Крылья", true), new BooleanSetting("Крылья 2", false), new BooleanSetting("Китайская шляпа", true) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   private final BooleanSetting butterflyWingAnimation = (new BooleanSetting("Анимация крыльев", true))
/*  71 */     .visible(() -> Boolean.valueOf(this.cosmetics.is("Крылья")));
/*  72 */   private final FloatSetting butterflyWingSize = (new FloatSetting("Размер", 1.0F, 0.65F, 1.8F, 0.05F))
/*  73 */     .visible(() -> Boolean.valueOf(this.cosmetics.is("Крылья")));
/*  74 */   private final BooleanSetting classicWingAnimation = (new BooleanSetting("Анимация крыльев", true))
/*  75 */     .visible(() -> Boolean.valueOf(this.cosmetics.is("Крылья 2")));
/*  76 */   private final FloatSetting classicWingSize = (new FloatSetting("Размер", 1.0F, 0.65F, 1.8F, 0.05F))
/*  77 */     .visible(() -> Boolean.valueOf(this.cosmetics.is("Крылья 2")));
/*     */   
/*     */   private float selfClassicBodyYaw;
/*     */   
/*     */   private boolean selfClassicBodyYawInitialized;
/*     */   private boolean lastButterflySelected;
/*     */   private boolean lastClassicSelected;
/*     */   
/*     */   public Cosmetics() {
/*  86 */     super("Cosmetics", "Визуальные украшения", Module.ModuleCategory.RENDER);
/*  87 */     addSettings(new Setting[] { (Setting)this.cosmetics, (Setting)this.butterflyWingAnimation, (Setting)this.butterflyWingSize, (Setting)this.classicWingAnimation, (Setting)this.classicWingSize });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  92 */     this.selfClassicBodyYawInitialized = false;
/*  93 */     this.lastButterflySelected = false;
/*  94 */     this.lastClassicSelected = false;
/*  95 */     super.onDisable();
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onRender3D(Event3DRender event) {
/* 100 */     if (mc.field_1724 == null || mc.field_1687 == null)
/* 101 */       return;  syncWingSelectionState();
/* 102 */     if (this.cosmetics.is("Нимб")) {
/* 103 */       renderNimbus(event);
/*     */     }
/* 105 */     boolean renderButterfly = this.cosmetics.is("Крылья");
/* 106 */     boolean renderClassic = this.cosmetics.is("Крылья 2");
/* 107 */     if (!renderButterfly && !renderClassic)
/*     */       return; 
/* 109 */     float tickDelta = event.getTickDelta();
/* 110 */     class_4587 matrices = event.getMatrices();
/* 111 */     class_243 cameraPos = event.getCamera().method_19326();
/* 112 */     for (class_1657 player : mc.field_1687.method_18456()) {
/* 113 */       if (!shouldRenderCosmeticForPlayer(player) || (
/* 114 */         player == mc.field_1724 && mc.field_1690.method_31044().method_31034()))
/* 115 */         continue;  if (renderButterfly) {
/* 116 */         renderButterflyWings(player, tickDelta, matrices, cameraPos);
/*     */       }
/* 118 */       if (renderClassic) {
/* 119 */         renderClassicWings(player, tickDelta, matrices, cameraPos);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void renderButterflyWings(class_1657 player, float tickDelta, class_4587 matrices, class_243 cameraPos) {
/* 125 */     if (player.method_6128() || player.method_18376() == class_4050.field_18079 || player.method_20232()) {
/*     */       return;
/*     */     }
/*     */     
/* 129 */     class_243 velocity = player.method_18798();
/* 130 */     float bodyYaw = class_3532.method_16439(tickDelta, player.field_6220, player.field_6283);
/* 131 */     float yawRad = bodyYaw * 0.017453292F;
/* 132 */     class_243 forward = new class_243(-class_3532.method_15374(yawRad), 0.0D, class_3532.method_15362(yawRad));
/* 133 */     class_243 sideways = new class_243(forward.field_1350, 0.0D, -forward.field_1352);
/*     */     
/* 135 */     float forwardMove = (float)(velocity.field_1352 * forward.field_1352 + velocity.field_1350 * forward.field_1350);
/* 136 */     float strafeMove = (float)(velocity.field_1352 * sideways.field_1352 + velocity.field_1350 * sideways.field_1350);
/* 137 */     float verticalMove = (float)velocity.field_1351;
/*     */     
/* 139 */     boolean animated = this.butterflyWingAnimation.isState();
/* 140 */     float smoothLean = animated ? class_3532.method_15363(-forwardMove * 140.0F - verticalMove * 48.0F, -24.0F, 26.0F) : 0.0F;
/* 141 */     float smoothStrafe = animated ? class_3532.method_15363(strafeMove * 90.0F, -10.0F, 10.0F) : 0.0F;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 146 */     float wingSpring = animated ? class_3532.method_15363(Math.abs(forwardMove) * 0.95F + Math.abs(strafeMove) * 0.65F + Math.abs(verticalMove) * 0.75F, 0.0F, 1.7F) : 0.0F;
/*     */     
/* 148 */     float anim = (player.field_6012 + tickDelta) * 0.22F * 1.6F + wingSpring * 0.4F;
/* 149 */     float sin = animated ? class_3532.method_15374(anim) : 0.0F;
/* 150 */     float cos = animated ? class_3532.method_15362(anim) : 0.0F;
/*     */     
/* 152 */     float spreadAngle = 18.0F + wingSpring * 5.0F;
/* 153 */     float pitchAngle = 13.0F + smoothLean * 0.3F + cos * 4.0F;
/* 154 */     float rollAngle = sin * 25.0F + smoothStrafe * 0.75F;
/* 155 */     class_4050 pose = player.method_18376();
/* 156 */     boolean fallFlying = player.method_6128();
/* 157 */     boolean horizontalPose = (pose == class_4050.field_18079 || fallFlying);
/* 158 */     if (horizontalPose) {
/* 159 */       spreadAngle -= 4.0F;
/* 160 */       pitchAngle -= 6.0F;
/* 161 */       rollAngle *= 0.72F;
/*     */     } 
/*     */     
/* 164 */     if (player.method_5715()) {
/* 165 */       spreadAngle -= 3.0F;
/* 166 */       pitchAngle += 8.0F;
/*     */     } 
/*     */     
/* 169 */     double px = class_3532.method_16436(tickDelta, player.field_6014, player.method_23317()) - cameraPos.field_1352;
/* 170 */     double py = class_3532.method_16436(tickDelta, player.field_6036, player.method_23318()) - cameraPos.field_1351;
/* 171 */     double pz = class_3532.method_16436(tickDelta, player.field_5969, player.method_23321()) - cameraPos.field_1350;
/*     */     
/* 173 */     matrices.method_22903();
/* 174 */     matrices.method_22904(px, py, pz);
/* 175 */     matrices.method_22907(class_7833.field_40716.rotationDegrees(-bodyYaw));
/* 176 */     applyBackPoseTransform(matrices, player, tickDelta, pose, fallFlying);
/*     */     
/* 178 */     int theme = resolveCosmeticThemeColor();
/* 179 */     int topColor = ColorUtils.setAlphaColor(theme, 132);
/* 180 */     int bottomColor = ColorUtils.setAlphaColor(ColorUtils.darken(theme, 0.85F), 102);
/* 181 */     int outlineColor = ColorUtils.setAlphaColor(ColorUtils.darken(theme, 0.58F), 214);
/*     */     
/* 183 */     RenderSystem.enableBlend();
/* 184 */     RenderSystem.disableCull();
/* 185 */     RenderSystem.enableDepthTest();
/* 186 */     RenderSystem.depthMask(false);
/* 187 */     RenderSystem.blendFunc(770, 771);
/* 188 */     RenderSystem.setShader(class_10142.field_53876);
/*     */     
/* 190 */     float butterflyScale = 1.0F * this.butterflyWingSize.get();
/* 191 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1576);
/* 192 */     renderButterflyWing(buffer, matrices, 1.0F, spreadAngle, pitchAngle, rollAngle, butterflyScale, topColor, bottomColor);
/* 193 */     renderButterflyWing(buffer, matrices, -1.0F, spreadAngle, pitchAngle, rollAngle, butterflyScale, topColor, bottomColor);
/* 194 */     class_286.method_43433(buffer.method_60800());
/*     */     
/* 196 */     RenderSystem.lineWidth(1.9F);
/* 197 */     class_287 outlineBuffer = class_289.method_1348().method_60827(class_293.class_5596.field_29344, class_290.field_1576);
/* 198 */     renderButterflyWingOutline(outlineBuffer, matrices, 1.0F, spreadAngle, pitchAngle, rollAngle, butterflyScale, outlineColor);
/* 199 */     renderButterflyWingOutline(outlineBuffer, matrices, -1.0F, spreadAngle, pitchAngle, rollAngle, butterflyScale, outlineColor);
/* 200 */     class_286.method_43433(outlineBuffer.method_60800());
/*     */     
/* 202 */     RenderSystem.enableCull();
/* 203 */     RenderSystem.enableDepthTest();
/* 204 */     RenderSystem.defaultBlendFunc();
/* 205 */     RenderSystem.disableBlend();
/* 206 */     RenderSystem.depthMask(true);
/* 207 */     matrices.method_22909();
/*     */   }
/*     */   
/*     */   private void renderClassicWings(class_1657 player, float tickDelta, class_4587 matrices, class_243 cameraPos) {
/* 211 */     if (!player.method_5805() || player.method_5767()) {
/*     */       return;
/*     */     }
/* 214 */     if (player.method_6128() || player.method_18376() == class_4050.field_18079 || player.method_20232()) {
/*     */       return;
/*     */     }
/*     */     
/* 218 */     double px = class_3532.method_16436(tickDelta, player.field_6014, player.method_23317()) - cameraPos.field_1352;
/* 219 */     double py = class_3532.method_16436(tickDelta, player.field_6036, player.method_23318()) - cameraPos.field_1351;
/* 220 */     double pz = class_3532.method_16436(tickDelta, player.field_5969, player.method_23321()) - cameraPos.field_1350;
/*     */     
/* 222 */     float bodyYaw = resolveClassicBodyYaw(player, tickDelta);
/* 223 */     class_243 velocity = player.method_18798();
/* 224 */     float yawRad = bodyYaw * 0.017453292F;
/* 225 */     class_243 forward = new class_243(-class_3532.method_15374(yawRad), 0.0D, class_3532.method_15362(yawRad));
/* 226 */     class_243 sideways = new class_243(forward.field_1350, 0.0D, -forward.field_1352);
/*     */     
/* 228 */     float forwardMove = (float)(velocity.field_1352 * forward.field_1352 + velocity.field_1350 * forward.field_1350);
/* 229 */     float strafeMove = (float)(velocity.field_1352 * sideways.field_1352 + velocity.field_1350 * sideways.field_1350);
/* 230 */     float verticalMove = (float)velocity.field_1351;
/*     */     
/* 232 */     boolean animated = this.classicWingAnimation.isState();
/* 233 */     float smoothLean = animated ? class_3532.method_15363(-forwardMove * 140.0F - verticalMove * 48.0F, -24.0F, 26.0F) : 0.0F;
/* 234 */     float smoothStrafe = animated ? class_3532.method_15363(strafeMove * 90.0F, -10.0F, 10.0F) : 0.0F;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 239 */     float wingSpring = animated ? class_3532.method_15363(Math.abs(forwardMove) * 0.95F + Math.abs(strafeMove) * 0.65F + Math.abs(verticalMove) * 0.75F, 0.0F, 1.7F) : 0.0F;
/*     */     
/* 241 */     float anim = (player.field_6012 + tickDelta) * 0.22F * 1.6F + wingSpring * 0.4F;
/* 242 */     float sin = animated ? class_3532.method_15374(anim) : 0.0F;
/* 243 */     float cos = animated ? class_3532.method_15362(anim) : 0.0F;
/*     */     
/* 245 */     float spreadAngle = 18.0F + wingSpring * 5.0F;
/* 246 */     float pitchAngle = 13.0F + smoothLean * 0.3F + cos * 4.0F;
/* 247 */     float rollAngle = sin * 25.0F + smoothStrafe * 0.75F;
/* 248 */     class_4050 pose = player.method_18376();
/* 249 */     boolean fallFlying = player.method_6128();
/* 250 */     boolean horizontalPose = (pose == class_4050.field_18079 || fallFlying);
/* 251 */     if (horizontalPose) {
/* 252 */       spreadAngle -= 4.0F;
/* 253 */       pitchAngle -= 6.0F;
/* 254 */       rollAngle *= 0.72F;
/*     */     } 
/*     */     
/* 257 */     if (player.method_5715()) {
/* 258 */       spreadAngle -= 3.0F;
/* 259 */       pitchAngle += 8.0F;
/*     */     } 
/*     */     
/* 262 */     ClassicWingPose wingPose = resolveClassicWingPose(player, tickDelta, pose);
/* 263 */     float open = spreadAngle * wingPose.openMultiplier;
/* 264 */     float scale = wingPose.scaleMultiplier * this.classicWingSize.get();
/* 265 */     float animatedSidePitch = wingPose.sidePitch + pitchAngle * 0.18F;
/* 266 */     float animatedSideRoll = wingPose.sideRoll + rollAngle * 0.2F;
/*     */     
/* 268 */     int theme = resolveCosmeticThemeColor();
/* 269 */     int baseColor = ColorUtils.setAlphaColor(theme, 220);
/* 270 */     int glowColor = ColorUtils.setAlphaColor(ColorUtils.interpolate(theme, -1, 0.28F), Math.round(48.4F));
/* 271 */     int coreColor = ColorUtils.setAlphaColor(ColorUtils.interpolate(theme, -1, 0.55F), Math.round(57.199997F));
/* 272 */     int outlineColor = ColorUtils.setAlphaColor(ColorUtils.darken(theme, 0.62F), Math.round(136.4F));
/* 273 */     int ribsColor = ColorUtils.setAlphaColor(ColorUtils.interpolate(theme, -1, 0.28F), Math.round(44.0F));
/*     */     
/* 275 */     RenderSystem.enableBlend();
/* 276 */     RenderSystem.disableCull();
/* 277 */     RenderSystem.enableDepthTest();
/* 278 */     RenderSystem.depthMask(false);
/* 279 */     RenderSystem.blendFunc(770, 771);
/* 280 */     RenderSystem.setShader(class_10142.field_53876);
/*     */     
/* 282 */     matrices.method_22903();
/* 283 */     matrices.method_22904(px, py, pz);
/* 284 */     matrices.method_22907(class_7833.field_40716.rotationDegrees(180.0F - bodyYaw));
/* 285 */     if (wingPose.preTranslateY != 0.0F || wingPose.preTranslateZ != 0.0F) {
/* 286 */       matrices.method_46416(0.0F, wingPose.preTranslateY, wingPose.preTranslateZ);
/*     */     }
/* 288 */     if (wingPose.pitchRotation != 0.0F) {
/* 289 */       matrices.method_22907(class_7833.field_40714.rotationDegrees(wingPose.pitchRotation));
/*     */     }
/* 291 */     if (wingPose.rollRotation != 0.0F) {
/* 292 */       matrices.method_22907(class_7833.field_40718.rotationDegrees(wingPose.rollRotation));
/*     */     }
/* 294 */     matrices.method_46416(0.0F, wingPose.anchorY, wingPose.anchorZ);
/* 295 */     matrices.method_22905(scale, scale, scale);
/*     */     
/* 297 */     renderClassicWingSide(matrices, -1.0F, open, animatedSidePitch, animatedSideRoll, baseColor, glowColor, coreColor, outlineColor, ribsColor, wingPose);
/* 298 */     renderClassicWingSide(matrices, 1.0F, open, animatedSidePitch, animatedSideRoll, baseColor, glowColor, coreColor, outlineColor, ribsColor, wingPose);
/* 299 */     matrices.method_22909();
/*     */     
/* 301 */     RenderSystem.enableCull();
/* 302 */     RenderSystem.enableDepthTest();
/* 303 */     RenderSystem.defaultBlendFunc();
/* 304 */     RenderSystem.disableBlend();
/* 305 */     RenderSystem.depthMask(true);
/*     */   }
/*     */   
/*     */   private void renderNimbus(Event3DRender event) {
/* 309 */     if (mc.field_1724 == null || mc.field_1687 == null || mc.field_1690.method_31044().method_31034()) {
/*     */       return;
/*     */     }
/*     */     
/* 313 */     float tickDelta = event.getTickDelta();
/* 314 */     class_243 camera = event.getCamera().method_19326();
/* 315 */     double x = class_3532.method_16436(tickDelta, mc.field_1724.field_6014, mc.field_1724.method_23317());
/* 316 */     double y = class_3532.method_16436(tickDelta, mc.field_1724.field_6036, mc.field_1724.method_23318()) + mc.field_1724.method_17682() + 0.1D;
/* 317 */     double z = class_3532.method_16436(tickDelta, mc.field_1724.field_5969, mc.field_1724.method_23321());
/*     */     
/* 319 */     int baseColor = resolveCosmeticThemeColor();
/* 320 */     long nowMs = System.currentTimeMillis();
/* 321 */     double radiansPerMillisecond = 0.0029670597283903604D;
/*     */     
/* 323 */     RenderSystem.enableBlend();
/* 324 */     RenderSystem.disableCull();
/* 325 */     RenderSystem.enableDepthTest();
/* 326 */     RenderSystem.depthMask(false);
/* 327 */     RenderSystem.blendFunc(770, 1);
/* 328 */     RenderSystem.setShader(class_10142.field_53880);
/* 329 */     RenderSystem.setShaderTexture(0, getNimbusTexture());
/*     */     
/* 331 */     class_4587 matrices = event.getMatrices();
/* 332 */     for (int arm = 0; arm < 2; arm++) {
/* 333 */       double baseAngle = radiansPerMillisecond * nowMs + arm * Math.PI;
/* 334 */       for (int segment = 0; segment < 17; segment++) {
/* 335 */         double segmentAngle = baseAngle - segment * 0.11D;
/* 336 */         double offsetX = Math.cos(segmentAngle) * 0.44999998807907104D;
/* 337 */         double offsetZ = Math.sin(segmentAngle) * 0.44999998807907104D;
/*     */         
/* 339 */         float progress = segment / Math.max(1, 16);
/* 340 */         float size = 0.23F * (1.0F - progress * 0.7F);
/* 341 */         int alpha = class_3532.method_15340(255 - segment * 9, 0, 255);
/* 342 */         int segmentColor = ColorUtils.setAlphaColor(baseColor, alpha);
/*     */         
/* 344 */         renderNimbusBillboard(matrices, event
/*     */             
/* 346 */             .getCamera().method_19330(), event
/* 347 */             .getCamera().method_19329(), x - camera.field_1352 + offsetX, y - camera.field_1351, z - camera.field_1350 + offsetZ, size, segmentColor);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 357 */     RenderSystem.enableCull();
/* 358 */     RenderSystem.enableDepthTest();
/* 359 */     RenderSystem.depthMask(true);
/* 360 */     RenderSystem.defaultBlendFunc();
/* 361 */     RenderSystem.disableBlend();
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderChinaHat(class_4587 matrixStack, class_4597 vertexConsumerProvider, class_1657 player, class_3882 model) {
/* 366 */     if (!isEnable() || !this.cosmetics.is("Китайская шляпа"))
/* 367 */       return;  if (mc.field_1724 == null || mc.field_1687 == null)
/* 368 */       return;  if (!shouldRenderCosmeticForPlayer(player))
/* 369 */       return;  if (player == mc.field_1724 && mc.field_1690.method_31044().method_31034())
/*     */       return; 
/* 371 */     double radius = (player.method_5829()).field_1320 - (player.method_5829()).field_1323;
/* 372 */     float offset = player.method_6118(class_1304.field_6169).method_7960() ? 0.415F : 0.48F;
/*     */     
/* 374 */     matrixStack.method_22903();
/* 375 */     model.method_2838().method_22703(matrixStack);
/*     */     
/* 377 */     RenderSystem.enableBlend();
/* 378 */     RenderSystem.enableDepthTest();
/* 379 */     RenderSystem.disableCull();
/* 380 */     RenderSystem.defaultBlendFunc();
/* 381 */     RenderSystem.setShader(class_10142.field_53876);
/* 382 */     RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
/*     */     
/* 384 */     RenderSystem.lineWidth(2.0F);
/* 385 */     GL11.glEnable(2848);
/* 386 */     GL11.glHint(3154, 4354);
/*     */     
/* 388 */     matrixStack.method_46416(0.0F, -offset, 0.0F);
/* 389 */     matrixStack.method_22907(class_7833.field_40717.rotationDegrees(180.0F));
/* 390 */     matrixStack.method_22907(class_7833.field_40716.rotationDegrees(90.0F));
/* 391 */     Matrix4f matrix = matrixStack.method_23760().method_23761();
/*     */     
/* 393 */     class_289 tessellator = class_289.method_1348();
/*     */     
/* 395 */     class_287 buffer = tessellator.method_60827(class_293.class_5596.field_27380, class_290.field_1576);
/* 396 */     float y = 0.0F;
/* 397 */     int colorTheme = resolveCosmeticThemeColor();
/* 398 */     int coneColor = ColorUtils.setAlphaColor(colorTheme, 125);
/* 399 */     int outlineColor = ColorUtils.setAlphaColor(ColorUtils.darken(colorTheme, 0.5F), 180);
/* 400 */     for (int i = 0; i <= 180; i++) {
/* 401 */       float iPi = i * 0.06981317F;
/*     */       
/* 403 */       float x = (float)(class_3532.method_15374(iPi) * radius);
/* 404 */       float z = (float)(class_3532.method_15362(iPi) * radius);
/*     */       
/* 406 */       buffer.method_22918(matrix, x, y, z).method_39415(coneColor);
/* 407 */       buffer.method_22918(matrix, 0.0F, 0.3F, 0.0F).method_39415(colorTheme);
/*     */     } 
/* 409 */     class_286.method_43433(buffer.method_60800());
/*     */     
/* 411 */     RenderSystem.depthMask(false);
/* 412 */     buffer = tessellator.method_60827(class_293.class_5596.field_27378, class_290.field_1576);
/* 413 */     float firstX = 0.0F;
/* 414 */     float firstZ = 0.0F;
/* 415 */     boolean firstSet = false;
/* 416 */     for (int j = 0; j <= 180; j++) {
/* 417 */       float iPi = j * 0.06981317F;
/* 418 */       float x = (float)(class_3532.method_15374(iPi) * radius);
/* 419 */       float z = (float)(class_3532.method_15362(iPi) * radius);
/* 420 */       buffer.method_22918(matrix, x, y, z).method_39415(outlineColor);
/* 421 */       if (!firstSet) {
/* 422 */         firstX = x;
/* 423 */         firstZ = z;
/* 424 */         firstSet = true;
/*     */       } 
/*     */     } 
/* 427 */     if (firstSet) {
/* 428 */       buffer.method_22918(matrix, firstX, y, firstZ).method_39415(outlineColor);
/*     */     }
/* 430 */     class_286.method_43433(buffer.method_60800());
/* 431 */     RenderSystem.depthMask(true);
/*     */     
/* 433 */     RenderSystem.enableCull();
/* 434 */     RenderSystem.defaultBlendFunc();
/* 435 */     RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
/* 436 */     GL11.glDisable(2848);
/*     */     
/* 438 */     matrixStack.method_22909();
/*     */   }
/*     */   
/*     */   private class_2960 getNimbusTexture() {
/* 442 */     return class_2960.method_60655("astra", "textures/targetesp/bloom.png");
/*     */   }
/*     */ 
/*     */   
/*     */   private void renderNimbusBillboard(class_4587 matrices, float cameraYaw, float cameraPitch, double x, double y, double z, float size, int color) {
/* 447 */     int a = color >> 24 & 0xFF;
/* 448 */     if (a <= 0) {
/*     */       return;
/*     */     }
/*     */     
/* 452 */     int r = color >> 16 & 0xFF;
/* 453 */     int g = color >> 8 & 0xFF;
/* 454 */     int b = color & 0xFF;
/* 455 */     float half = size * 0.5F;
/*     */     
/* 457 */     matrices.method_22903();
/* 458 */     matrices.method_22904(x, y, z);
/* 459 */     matrices.method_22907(class_7833.field_40716.rotationDegrees(-cameraYaw));
/* 460 */     matrices.method_22907(class_7833.field_40714.rotationDegrees(cameraPitch));
/*     */     
/* 462 */     Matrix4f matrix = matrices.method_23760().method_23761();
/* 463 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
/* 464 */     buffer.method_22918(matrix, -half, -half, 0.0F).method_22913(0.0F, 1.0F).method_1336(r, g, b, a);
/* 465 */     buffer.method_22918(matrix, -half, half, 0.0F).method_22913(0.0F, 0.0F).method_1336(r, g, b, a);
/* 466 */     buffer.method_22918(matrix, half, half, 0.0F).method_22913(1.0F, 0.0F).method_1336(r, g, b, a);
/* 467 */     buffer.method_22918(matrix, half, -half, 0.0F).method_22913(1.0F, 1.0F).method_1336(r, g, b, a);
/* 468 */     class_286.method_43433(buffer.method_60800());
/* 469 */     matrices.method_22909();
/*     */   }
/*     */   
/*     */   private boolean shouldRenderCosmeticForPlayer(class_1657 player) {
/* 473 */     if (mc.field_1724 == null) return false; 
/* 474 */     if (player == mc.field_1724) return true; 
/* 475 */     return (astra.INSTANCE != null && astra.INSTANCE.friendStorage != null && astra.INSTANCE.friendStorage
/*     */       
/* 477 */       .isFriend(player.method_5477().getString()));
/*     */   }
/*     */   
/*     */   private int getStableThemeColor() {
/* 481 */     if (astra.INSTANCE == null || astra.INSTANCE.themeStorage == null || astra.INSTANCE.themeStorage.getThemes() == null) {
/* 482 */       return ColorUtils.getThemeColor(0);
/*     */     }
/* 484 */     Theme theme = astra.INSTANCE.themeStorage.getThemes().getTheme();
/* 485 */     if (theme == null || theme.color == null || theme.color.length == 0) {
/* 486 */       return ColorUtils.getThemeColor(0);
/*     */     }
/* 488 */     return theme.color[0];
/*     */   }
/*     */   
/*     */   private int resolveCosmeticThemeColor() {
/* 492 */     if (astra.INSTANCE == null || astra.INSTANCE.themeStorage == null || astra.INSTANCE.themeStorage.getThemes() == null) {
/* 493 */       return ColorUtils.getThemeColor();
/*     */     }
/*     */     
/* 496 */     Theme theme = astra.INSTANCE.themeStorage.getThemes().getTheme();
/* 497 */     if (theme == null) {
/* 498 */       return ColorUtils.getThemeColor();
/*     */     }
/*     */     
/* 501 */     return "Rainbow".equals(theme.getName()) ? ColorUtils.getThemeColor() : getStableThemeColor();
/*     */   }
/*     */   
/*     */   private void syncWingSelectionState() {
/* 505 */     boolean butterfly = this.cosmetics.is("Крылья");
/* 506 */     boolean classic = this.cosmetics.is("Крылья 2");
/*     */     
/* 508 */     if (butterfly && classic) {
/* 509 */       if (butterfly != this.lastButterflySelected && classic == this.lastClassicSelected) {
/* 510 */         this.cosmetics.set("Крылья 2", false);
/* 511 */         classic = false;
/*     */       } else {
/* 513 */         this.cosmetics.set("Крылья", false);
/* 514 */         butterfly = false;
/*     */       } 
/*     */     }
/*     */     
/* 518 */     this.lastButterflySelected = butterfly;
/* 519 */     this.lastClassicSelected = classic;
/*     */   }
/*     */   
/*     */   private void applyBackPoseTransform(class_4587 matrices, class_1657 player, float tickDelta, class_4050 pose, boolean fallFlying) {
/* 523 */     if (fallFlying) {
/* 524 */       float pitch = player.method_5695(tickDelta);
/* 525 */       float clampedPitch = class_3532.method_15363(pitch, -65.0F, 65.0F);
/*     */       
/* 527 */       matrices.method_46416(0.0F, 0.3F, 0.0F);
/* 528 */       matrices.method_22907(class_7833.field_40714.rotationDegrees(-(90.0F + clampedPitch)));
/* 529 */       matrices.method_46416(0.0F, -0.15F, 0.12F);
/*     */       
/*     */       return;
/*     */     } 
/* 533 */     if (pose == class_4050.field_18079) {
/* 534 */       float pitch = player.method_5695(tickDelta);
/* 535 */       float clampedPitch = class_3532.method_15363(pitch, -65.0F, 65.0F);
/*     */       
/* 537 */       matrices.method_46416(0.0F, 0.3F, 0.0F);
/* 538 */       matrices.method_22907(class_7833.field_40714.rotationDegrees(-(90.0F + clampedPitch)));
/* 539 */       matrices.method_46416(0.0F, -0.15F, 0.12F);
/*     */       
/*     */       return;
/*     */     } 
/* 543 */     if (player.method_5715()) {
/* 544 */       matrices.method_46416(0.0F, 1.15F, 0.0F);
/* 545 */       matrices.method_22907(class_7833.field_40714.rotationDegrees(24.0F));
/* 546 */       matrices.method_46416(0.0F, 0.0F, 0.08F);
/*     */     } else {
/* 548 */       matrices.method_46416(0.0F, 1.3F, 0.08F);
/*     */     } 
/*     */   }
/*     */   
/*     */   private float resolveClassicBodyYaw(class_1657 player, float tickDelta) {
/* 553 */     float targetBodyYaw = class_3532.method_17821(tickDelta, player.field_6220, player.field_6283);
/* 554 */     if (player != mc.field_1724) {
/* 555 */       return targetBodyYaw;
/*     */     }
/*     */     
/* 558 */     if (!this.selfClassicBodyYawInitialized) {
/* 559 */       this.selfClassicBodyYaw = targetBodyYaw;
/* 560 */       this.selfClassicBodyYawInitialized = true;
/* 561 */       return this.selfClassicBodyYaw;
/*     */     } 
/*     */     
/* 564 */     float delta = class_3532.method_15393(targetBodyYaw - this.selfClassicBodyYaw);
/* 565 */     this.selfClassicBodyYaw += class_3532.method_15363(delta, -14.0F, 14.0F);
/* 566 */     return this.selfClassicBodyYaw;
/*     */   }
/*     */   
/*     */   private ClassicWingPose resolveClassicWingPose(class_1657 player, float tickDelta, class_4050 pose) {
/* 570 */     float pitch = player.method_5695(tickDelta);
/*     */     
/* 572 */     if (player.method_6128()) {
/* 573 */       float clampedPitch = class_3532.method_15363(pitch, -65.0F, 65.0F);
/* 574 */       return new ClassicWingPose(1.18F, 0.1F, 0.0F, 0.0F, -(90.0F + clampedPitch), 0.0F, 0.76F, 0.92F, 0.1F, 0.58F, 0.05F, 0.0F, 0.06F, -5.0F, -2.0F, 0.13F);
/*     */     } 
/*     */ 
/*     */     
/* 578 */     if (pose == class_4050.field_18079 || player.method_20232()) {
/* 579 */       float clampedPitch = class_3532.method_15363(pitch, -65.0F, 65.0F);
/* 580 */       float bodyShiftY = player.method_20232() ? 1.1F : 1.18F;
/* 581 */       float bodyShiftZ = player.method_20232() ? 0.18F : 0.12F;
/* 582 */       return new ClassicWingPose(bodyShiftY, bodyShiftZ, 0.18F, 0.48F, -(90.0F + clampedPitch), 0.0F, 0.84F, 0.96F, 0.12F, 0.7F, 0.03F, 0.0F, 0.01F, -7.0F, -3.0F, 0.16F);
/*     */     } 
/*     */ 
/*     */     
/* 586 */     if (player.method_5715()) {
/* 587 */       return new ClassicWingPose(0.0F, 0.0F, 0.96F, 0.1F, 18.0F, 0.0F, 1.0F, 1.0F, 0.18F, 4.5F, 0.06F, 0.0F, 0.02F, -11.0F, -4.0F, 0.12F);
/*     */     }
/*     */ 
/*     */     
/* 591 */     return new ClassicWingPose(0.0F, 0.0F, 1.18F, 0.1F, 0.0F, 0.0F, 1.0F, 1.0F, 0.18F, 4.5F, 0.06F, 0.0F, 0.02F, -11.0F, -4.0F, 0.12F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void renderClassicWingSide(class_4587 matrices, float side, float open, float sidePitch, float sideRoll, int baseColor, int glowColor, int coreColor, int outlineColor, int ribsColor, ClassicWingPose pose) {
/* 598 */     matrices.method_22903();
/* 599 */     matrices.method_46416(side * pose.sideOffset, pose.sideYOffset, pose.sideZOffset);
/* 600 */     matrices.method_22907(class_7833.field_40716.rotationDegrees(side * open));
/* 601 */     matrices.method_22907(class_7833.field_40718.rotationDegrees(side * sideRoll));
/* 602 */     matrices.method_22907(class_7833.field_40714.rotationDegrees(sidePitch));
/*     */     
/* 604 */     RenderSystem.blendFunc(770, 1);
/* 605 */     drawClassicWingLayer(matrices, side, 1.22F, glowColor, ColorUtils.setAlphaColor(glowColor, 0));
/* 606 */     drawClassicWingLayer(matrices, side, 0.84F, coreColor, ColorUtils.setAlphaColor(coreColor, 0));
/*     */     
/* 608 */     RenderSystem.blendFunc(770, 771);
/* 609 */     drawClassicWingLayer(matrices, side, 1.0F, baseColor, ColorUtils.setAlphaColor(baseColor, 10));
/*     */     
/* 611 */     RenderSystem.blendFunc(770, 1);
/* 612 */     drawClassicWingOutline(matrices, side, 1.0F, outlineColor);
/* 613 */     drawClassicWingRibs(matrices, side, 0.96F, ribsColor);
/* 614 */     matrices.method_22909();
/*     */   }
/*     */   
/*     */   private void drawClassicWingLayer(class_4587 matrices, float side, float scale, int rootColor, int edgeColor) {
/* 618 */     Matrix4f matrix = matrices.method_23760().method_23761();
/* 619 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27379, class_290.field_1576);
/*     */     
/* 621 */     for (int i = 0; i < CLASSIC_WING_SHAPE.length; i++) {
/* 622 */       ClassicWingPoint current = CLASSIC_WING_SHAPE[i];
/* 623 */       ClassicWingPoint next = CLASSIC_WING_SHAPE[(i + 1) % CLASSIC_WING_SHAPE.length];
/* 624 */       vertex(buffer, matrix, 0.0F, 0.0F, 0.0F, rootColor);
/* 625 */       vertex(buffer, matrix, side * current.x * scale, current.y * scale, 0.0F, applyClassicWingPointAlpha(edgeColor, current.alphaMultiplier));
/* 626 */       vertex(buffer, matrix, side * next.x * scale, next.y * scale, 0.0F, applyClassicWingPointAlpha(edgeColor, next.alphaMultiplier));
/*     */     } 
/*     */     
/* 629 */     class_286.method_43433(buffer.method_60800());
/*     */   }
/*     */   
/*     */   private void drawClassicWingOutline(class_4587 matrices, float side, float scale, int color) {
/* 633 */     Matrix4f matrix = matrices.method_23760().method_23761();
/* 634 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_29344, class_290.field_1576);
/*     */     
/* 636 */     RenderSystem.lineWidth(1.35F);
/* 637 */     GL11.glEnable(2848);
/* 638 */     for (int i = 0; i < CLASSIC_WING_SHAPE.length; i++) {
/* 639 */       ClassicWingPoint current = CLASSIC_WING_SHAPE[i];
/* 640 */       ClassicWingPoint next = CLASSIC_WING_SHAPE[(i + 1) % CLASSIC_WING_SHAPE.length];
/* 641 */       addLine(buffer, matrix, side * current.x * scale, current.y * scale, 0.0F, side * next.x * scale, next.y * scale, 0.0F, color);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 647 */     class_286.method_43433(buffer.method_60800());
/* 648 */     GL11.glDisable(2848);
/*     */   }
/*     */   
/*     */   private void drawClassicWingRibs(class_4587 matrices, float side, float scale, int color) {
/* 652 */     Matrix4f matrix = matrices.method_23760().method_23761();
/* 653 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_29344, class_290.field_1576);
/* 654 */     int[] ribIndices = { 2, 4, 7, 9, 11 };
/*     */     
/* 656 */     RenderSystem.lineWidth(0.9F);
/* 657 */     for (int ribIndex : ribIndices) {
/* 658 */       ClassicWingPoint point = CLASSIC_WING_SHAPE[ribIndex];
/* 659 */       vertex(buffer, matrix, 0.0F, 0.0F, 0.0F, 
/* 660 */           ColorUtils.setAlphaColor(color, Math.max(8, (int)((color >> 24 & 0xFF) * 0.75F))));
/* 661 */       vertex(buffer, matrix, side * point.x * scale, point.y * scale, 0.0F, 
/* 662 */           applyClassicWingPointAlpha(color, point.alphaMultiplier));
/*     */     } 
/* 664 */     class_286.method_43433(buffer.method_60800());
/*     */   }
/*     */   
/*     */   private int applyClassicWingPointAlpha(int color, float multiplier) {
/* 668 */     int alpha = color >> 24 & 0xFF;
/* 669 */     return ColorUtils.setAlphaColor(color, Math.max(0, Math.min(255, (int)(alpha * multiplier))));
/*     */   }
/*     */   
/*     */   private void vertex(class_287 buffer, Matrix4f matrix, float x, float y, float z, int color) {
/* 673 */     buffer.method_22918(matrix, x, y, z).method_39415(color);
/*     */   }
/*     */ 
/*     */   
/*     */   private void renderButterflyWing(class_287 buffer, class_4587 matrices, float side, float spread, float pitch, float roll, float scale, int topColor, int bottomColor) {
/* 678 */     float root = 0.12F * scale;
/* 679 */     float topW = 1.52F * scale;
/* 680 */     float topH = 0.64F * scale;
/* 681 */     float lowW = 1.14F * scale;
/* 682 */     float lowH = 0.39F * scale;
/*     */     
/* 684 */     matrices.method_22903();
/* 685 */     matrices.method_46416(0.15F * side, 0.0F, -0.17F);
/* 686 */     matrices.method_22907(class_7833.field_40716.rotationDegrees(side * spread));
/* 687 */     matrices.method_22907(class_7833.field_40714.rotationDegrees(pitch));
/* 688 */     matrices.method_22907(class_7833.field_40718.rotationDegrees(side * roll));
/*     */     
/* 690 */     Matrix4f matrix = matrices.method_23760().method_23761();
/* 691 */     addDoubleSidedGradientTriangle(buffer, matrix, side * root, 0.02F, -0.01F, side * (root + topW * 0.22F), topH * 0.98F, -0.06F, side * (root + topW * 0.88F), topH * 0.6F, -0.13F, topColor, bottomColor);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 697 */     addDoubleSidedGradientTriangle(buffer, matrix, side * root, 0.02F, -0.01F, side * (root + topW * 0.88F), topH * 0.6F, -0.13F, side * (root + topW), topH * 0.12F, -0.17F, topColor, bottomColor);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 703 */     addDoubleSidedGradientTriangle(buffer, matrix, side * root, -0.03F, -0.03F, side * (root + lowW * 0.26F), -lowH * 0.96F, -0.11F, side * (root + lowW * 0.84F), -lowH * 0.54F, -0.18F, bottomColor, topColor);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 709 */     addDoubleSidedGradientTriangle(buffer, matrix, side * root, -0.03F, -0.03F, side * (root + lowW * 0.84F), -lowH * 0.54F, -0.18F, side * (root + lowW), -lowH * 0.12F, -0.21F, bottomColor, topColor);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 716 */     matrices.method_22909();
/*     */   }
/*     */ 
/*     */   
/*     */   private void renderButterflyWingOutline(class_287 buffer, class_4587 matrices, float side, float spread, float pitch, float roll, float scale, int outlineColor) {
/* 721 */     float root = 0.12F * scale;
/* 722 */     float topW = 1.52F * scale;
/* 723 */     float topH = 0.64F * scale;
/* 724 */     float lowW = 1.14F * scale;
/* 725 */     float lowH = 0.39F * scale;
/*     */ 
/*     */     
/* 728 */     matrices.method_22903();
/* 729 */     matrices.method_46416(0.15F * side, 0.0F, -0.17F);
/* 730 */     matrices.method_22907(class_7833.field_40716.rotationDegrees(side * spread));
/* 731 */     matrices.method_22907(class_7833.field_40714.rotationDegrees(pitch));
/* 732 */     matrices.method_22907(class_7833.field_40718.rotationDegrees(side * roll));
/*     */     
/* 734 */     Matrix4f matrix = matrices.method_23760().method_23761();
/* 735 */     addLine(buffer, matrix, side * root, 0.02F, -0.01F, side * (root + topW * 0.22F), topH * 0.98F, -0.06F, outlineColor);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 740 */     addLine(buffer, matrix, side * (root + topW * 0.22F), topH * 0.98F, -0.06F, side * (root + topW * 0.88F), topH * 0.6F, -0.13F, outlineColor);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 745 */     addLine(buffer, matrix, side * (root + topW * 0.88F), topH * 0.6F, -0.13F, side * (root + topW), topH * 0.12F, -0.17F, outlineColor);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 750 */     addLine(buffer, matrix, side * root, -0.03F, -0.03F, side * (root + lowW * 0.26F), -lowH * 0.96F, -0.11F, outlineColor);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 755 */     addLine(buffer, matrix, side * (root + lowW * 0.26F), -lowH * 0.96F, -0.11F, side * (root + lowW * 0.84F), -lowH * 0.54F, -0.18F, outlineColor);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 760 */     addLine(buffer, matrix, side * (root + lowW * 0.84F), -lowH * 0.54F, -0.18F, side * (root + lowW), -lowH * 0.12F, -0.21F, outlineColor);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 765 */     addLine(buffer, matrix, side * root, -0.01F, -0.02F, side * (root + topW * 0.6F), 0.08F, -0.08F, outlineColor);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 770 */     matrices.method_22909();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addDoubleSidedQuad(class_287 buffer, Matrix4f matrix, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, int r, int g, int b, int a) {
/* 779 */     addQuad(buffer, matrix, x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, r, g, b, a);
/* 780 */     addQuad(buffer, matrix, x4, y4, z4, x3, y3, z3, x2, y2, z2, x1, y1, z1, r, g, b, a);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addDoubleSidedGradientQuad(class_287 buffer, Matrix4f matrix, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, int nearColor, int farColor) {
/* 789 */     int nr = nearColor >> 16 & 0xFF;
/* 790 */     int ng = nearColor >> 8 & 0xFF;
/* 791 */     int nb = nearColor & 0xFF;
/* 792 */     int na = nearColor >> 24 & 0xFF;
/* 793 */     int fr = farColor >> 16 & 0xFF;
/* 794 */     int fg = farColor >> 8 & 0xFF;
/* 795 */     int fb = farColor & 0xFF;
/* 796 */     int fa = farColor >> 24 & 0xFF;
/*     */     
/* 798 */     buffer.method_22918(matrix, x1, y1, z1).method_1336(nr, ng, nb, na);
/* 799 */     buffer.method_22918(matrix, x2, y2, z2).method_1336(fr, fg, fb, fa);
/* 800 */     buffer.method_22918(matrix, x3, y3, z3).method_1336(fr, fg, fb, fa);
/* 801 */     buffer.method_22918(matrix, x4, y4, z4).method_1336(nr, ng, nb, na);
/*     */     
/* 803 */     buffer.method_22918(matrix, x4, y4, z4).method_1336(nr, ng, nb, na);
/* 804 */     buffer.method_22918(matrix, x3, y3, z3).method_1336(fr, fg, fb, fa);
/* 805 */     buffer.method_22918(matrix, x2, y2, z2).method_1336(fr, fg, fb, fa);
/* 806 */     buffer.method_22918(matrix, x1, y1, z1).method_1336(nr, ng, nb, na);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addDoubleSidedGradientTriangle(class_287 buffer, Matrix4f matrix, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, int nearColor, int farColor) {
/* 814 */     int nr = nearColor >> 16 & 0xFF;
/* 815 */     int ng = nearColor >> 8 & 0xFF;
/* 816 */     int nb = nearColor & 0xFF;
/* 817 */     int na = nearColor >> 24 & 0xFF;
/* 818 */     int fr = farColor >> 16 & 0xFF;
/* 819 */     int fg = farColor >> 8 & 0xFF;
/* 820 */     int fb = farColor & 0xFF;
/* 821 */     int fa = farColor >> 24 & 0xFF;
/*     */     
/* 823 */     buffer.method_22918(matrix, x1, y1, z1).method_1336(nr, ng, nb, na);
/* 824 */     buffer.method_22918(matrix, x2, y2, z2).method_1336(fr, fg, fb, fa);
/* 825 */     buffer.method_22918(matrix, x3, y3, z3).method_1336(fr, fg, fb, fa);
/* 826 */     buffer.method_22918(matrix, x3, y3, z3).method_1336(fr, fg, fb, fa);
/* 827 */     buffer.method_22918(matrix, x2, y2, z2).method_1336(fr, fg, fb, fa);
/* 828 */     buffer.method_22918(matrix, x1, y1, z1).method_1336(nr, ng, nb, na);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void renderWingBoneLine(class_287 buffer, Matrix4f matrix, float x0, float y0, float z0, float x1, float y1, float z1, float x2, float y2, float z2, float thickness, int colorA, int colorB) {
/* 836 */     float vx1 = x1 - x0;
/* 837 */     float vy1 = y1 - y0;
/* 838 */     float len1 = Math.max(1.0E-4F, (float)Math.sqrt((vx1 * vx1 + vy1 * vy1)));
/* 839 */     float nx1 = -vy1 / len1 * thickness;
/* 840 */     float ny1 = vx1 / len1 * thickness;
/*     */     
/* 842 */     int aR = colorA >> 16 & 0xFF;
/* 843 */     int aG = colorA >> 8 & 0xFF;
/* 844 */     int aB = colorA & 0xFF;
/* 845 */     int aA = colorA >> 24 & 0xFF;
/* 846 */     int bR = colorB >> 16 & 0xFF;
/* 847 */     int bG = colorB >> 8 & 0xFF;
/* 848 */     int bB = colorB & 0xFF;
/* 849 */     int bA = colorB >> 24 & 0xFF;
/*     */     
/* 851 */     addDoubleSidedQuad(buffer, matrix, x0 + nx1, y0 + ny1, z0, x0 - nx1, y0 - ny1, z0, x1 - nx1, y1 - ny1, z1, x1 + nx1, y1 + ny1, z1, aR, aG, aB, aA);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 859 */     float vx2 = x2 - x1;
/* 860 */     float vy2 = y2 - y1;
/* 861 */     float len2 = Math.max(1.0E-4F, (float)Math.sqrt((vx2 * vx2 + vy2 * vy2)));
/* 862 */     float nx2 = -vy2 / len2 * thickness;
/* 863 */     float ny2 = vx2 / len2 * thickness;
/*     */     
/* 865 */     addDoubleSidedQuad(buffer, matrix, x1 + nx2, y1 + ny2, z1, x1 - nx2, y1 - ny2, z1, x2 - nx2, y2 - ny2, z2, x2 + nx2, y2 + ny2, z2, bR, bG, bB, bA);
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
/*     */   private void addQuad(class_287 buffer, Matrix4f matrix, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, int r, int g, int b, int a) {
/* 880 */     buffer.method_22918(matrix, x1, y1, z1).method_1336(r, g, b, a);
/* 881 */     buffer.method_22918(matrix, x2, y2, z2).method_1336(r, g, b, a);
/* 882 */     buffer.method_22918(matrix, x3, y3, z3).method_1336(r, g, b, a);
/* 883 */     buffer.method_22918(matrix, x4, y4, z4).method_1336(r, g, b, a);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addLine(class_287 buffer, Matrix4f matrix, float x1, float y1, float z1, float x2, float y2, float z2, int color) {
/* 890 */     int r = color >> 16 & 0xFF;
/* 891 */     int g = color >> 8 & 0xFF;
/* 892 */     int b = color & 0xFF;
/* 893 */     int a = color >> 24 & 0xFF;
/* 894 */     buffer.method_22918(matrix, x1, y1, z1).method_1336(r, g, b, a);
/* 895 */     buffer.method_22918(matrix, x2, y2, z2).method_1336(r, g, b, a);
/*     */   }
/*     */   
/*     */   private static final class ClassicWingPoint {
/*     */     private final float x;
/*     */     private final float y;
/*     */     private final float alphaMultiplier;
/*     */     
/*     */     private ClassicWingPoint(float x, float y, float alphaMultiplier) {
/* 904 */       this.x = x;
/* 905 */       this.y = y;
/* 906 */       this.alphaMultiplier = alphaMultiplier;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class ClassicWingPose
/*     */   {
/*     */     private final float preTranslateY;
/*     */     
/*     */     private final float preTranslateZ;
/*     */     private final float anchorY;
/*     */     private final float anchorZ;
/*     */     private final float pitchRotation;
/*     */     private final float rollRotation;
/*     */     private final float openMultiplier;
/*     */     private final float scaleMultiplier;
/*     */     private final float motionSpreadBoost;
/*     */     private final float flapAmplitude;
/*     */     private final float sideOffset;
/*     */     private final float sideYOffset;
/*     */     private final float sideZOffset;
/*     */     private final float sideRoll;
/*     */     private final float sidePitch;
/*     */     private final float flapSpeed;
/*     */     
/*     */     private ClassicWingPose(float preTranslateY, float preTranslateZ, float anchorY, float anchorZ, float pitchRotation, float rollRotation, float openMultiplier, float scaleMultiplier, float motionSpreadBoost, float flapAmplitude, float sideOffset, float sideYOffset, float sideZOffset, float sideRoll, float sidePitch, float flapSpeed) {
/* 932 */       this.preTranslateY = preTranslateY;
/* 933 */       this.preTranslateZ = preTranslateZ;
/* 934 */       this.anchorY = anchorY;
/* 935 */       this.anchorZ = anchorZ;
/* 936 */       this.pitchRotation = pitchRotation;
/* 937 */       this.rollRotation = rollRotation;
/* 938 */       this.openMultiplier = openMultiplier;
/* 939 */       this.scaleMultiplier = scaleMultiplier;
/* 940 */       this.motionSpreadBoost = motionSpreadBoost;
/* 941 */       this.flapAmplitude = flapAmplitude;
/* 942 */       this.sideOffset = sideOffset;
/* 943 */       this.sideYOffset = sideYOffset;
/* 944 */       this.sideZOffset = sideZOffset;
/* 945 */       this.sideRoll = sideRoll;
/* 946 */       this.sidePitch = sidePitch;
/* 947 */       this.flapSpeed = flapSpeed;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\Cosmetics.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */