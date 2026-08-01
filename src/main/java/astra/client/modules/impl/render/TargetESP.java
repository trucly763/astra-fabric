/*      */ package shame.astra.client.modules.impl.render;
/*      */ 
/*      */ import com.mojang.blaze3d.systems.RenderSystem;
/*      */ import java.util.ArrayList;
/*      */ import java.util.concurrent.CopyOnWriteArrayList;
/*      */ import net.minecraft.class_10142;
/*      */ import net.minecraft.class_1309;
/*      */ import net.minecraft.class_243;
/*      */ import net.minecraft.class_286;
/*      */ import net.minecraft.class_287;
/*      */ import net.minecraft.class_289;
/*      */ import net.minecraft.class_290;
/*      */ import net.minecraft.class_293;
/*      */ import net.minecraft.class_2960;
/*      */ import net.minecraft.class_3532;
/*      */ import net.minecraft.class_4587;
/*      */ import net.minecraft.class_7833;
/*      */ import org.joml.Matrix4f;
/*      */ import org.joml.Quaternionf;
/*      */ import org.joml.Vector3f;
/*      */ import org.joml.Vector3fc;
/*      */ import shame.astra.api.events.EventLink;
/*      */ import shame.astra.api.events.implement.Event3DRender;
/*      */ import shame.astra.api.events.implement.EventRender;
/*      */ import shame.astra.api.storages.implement.helpertstorages.enumvar.ModuleClass;
/*      */ import shame.astra.api.utils.animation.Easings;
/*      */ import shame.astra.api.utils.color.ColorUtils;
/*      */ import shame.astra.client.modules.Module;
/*      */ import shame.astra.client.modules.impl.combat.Aura;
/*      */ import shame.astra.client.modules.settings.Setting;
/*      */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*      */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*      */ import shame.astra.client.modules.settings.implement.ModeSetting;
/*      */ 
/*      */ 
/*      */ public class TargetESP
/*      */   extends Module
/*      */ {
/*   39 */   public static TargetESP INSTANCE = new TargetESP();
/*      */   private static final float GHOST_ALPHA_MULT = 0.6F;
/*      */   private static final float CELKA_SPEED_MULT = 1.2F;
/*      */   private static final float SCALE_FACTOR = 0.007F;
/*      */   static final long CUBE_ATTACH_LIFE_MS = 560L;
/*      */   static final long CUBE_FADE_LIFE_MS = 320L;
/*      */   static final int MAX_CUBE_PARTICLES = 72;
/*   46 */   static final byte[][] CUBE_EDGES = new byte[][] { { -1, -1, -1, 1, -1, -1 }, { 1, -1, -1, 1, -1, 1 }, { 1, -1, 1, -1, -1, 1 }, { -1, -1, 1, -1, -1, -1 }, { -1, 1, -1, 1, 1, -1 }, { 1, 1, -1, 1, 1, 1 }, { 1, 1, 1, -1, 1, 1 }, { -1, 1, 1, -1, 1, -1 }, { -1, -1, -1, -1, 1, -1 }, { 1, -1, -1, 1, 1, -1 }, { 1, -1, 1, 1, 1, 1 }, { -1, -1, 1, -1, 1, 1 } };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   52 */   private final ModeSetting mode = new ModeSetting("Режим", "Картинка 1", new String[] { "Картинка 1", "Картинка 2", "Кольцо", "Души", "Кубы", "Кристаллы" });
/*   53 */   private final FloatSetting size = new FloatSetting("Размер", 1.15F, 0.6F, 2.5F, 0.05F);
/*   54 */   private final FloatSetting ringRadius = new FloatSetting("Радиус кольца", 0.5F, 0.3F, 1.5F, 0.05F);
/*   55 */   private final FloatSetting ringSpeed = new FloatSetting("Скорость кольца", 1.0F, 0.3F, 3.0F, 0.1F);
/*   56 */   private final FloatSetting rotateSpeed = new FloatSetting("Скорость вращения", 1.2F, 0.2F, 4.0F, 0.05F);
/*   57 */   private final BooleanSetting hurtColor = new BooleanSetting("Окрашивание при ударе", true);
/*   58 */   private final FloatSetting bmwGhostCount = new FloatSetting("Кол-во призраков", 3.0F, 2.0F, 5.0F, 1.0F);
/*   59 */   private final FloatSetting bmwGhostLife = new FloatSetting("Время жизни (мс)", 350.0F, 150.0F, 500.0F, 25.0F);
/*   60 */   private final FloatSetting bmwStrengthXZ = new FloatSetting("Цикл XZ", 2000.0F, 1000.0F, 5000.0F, 100.0F);
/*   61 */   private final FloatSetting bmwStrengthY = new FloatSetting("Цикл Y", 1700.0F, 1000.0F, 5000.0F, 100.0F);
/*   62 */   private float appearValue = 0.0F;
/*   63 */   private float scaleValue = 0.0F;
/*   64 */   private float rotProgress = 0.0F;
/*   65 */   private float rotFrom = -280.0F;
/*   66 */   private float rotTo = 280.0F;
/*   67 */   private long lastRotateUpdate = System.currentTimeMillis();
/*   68 */   private class_1309 lastTarget = null;
/*   69 */   private class_1309 lastHandledTarget = null;
/*   70 */   private class_243 lastTargetPos = null;
/*   71 */   private float lastTargetHeight = 1.8F;
/*   72 */   private float lastTargetWidth = 0.6F;
/*   73 */   private final CopyOnWriteArrayList<GlowPoint> bmwPoints = new CopyOnWriteArrayList<>();
/*   74 */   private float crystalRotationAngle = 0.0F;
/*   75 */   private float crystalAnimation = 0.0F;
/*   76 */   private float spawnAccumulator = 0.0F;
/*   77 */   private long lastCubeTime = 0L;
/*   78 */   private final ArrayList<CubeParticle> cubeParticles = new ArrayList<>();
/*   79 */   private final ArrayList<CubeParticle> renderCubeParticles = new ArrayList<>();
/*      */   private static final float SPAWN_INTERVAL = 0.022F;
/*      */   private static final int PARTICLES_PER_SPAWN = 1;
/*      */   
/*      */   public TargetESP() {
/*   84 */     super("TargetESP", "Отображения таргета", Module.ModuleCategory.RENDER);
/*   85 */     this.size.visible(this::isImageMode);
/*   86 */     this.rotateSpeed.visible(this::isImageMode);
/*   87 */     this.bmwGhostCount.visible(() -> Boolean.valueOf(this.mode.is("Райдер")));
/*   88 */     this.bmwGhostLife.visible(() -> Boolean.valueOf(this.mode.is("Райдер")));
/*   89 */     this.bmwStrengthXZ.visible(() -> Boolean.valueOf(this.mode.is("Райдер")));
/*   90 */     this.bmwStrengthY.visible(() -> Boolean.valueOf(this.mode.is("Райдер")));
/*   91 */     this.ringRadius.visible(() -> Boolean.valueOf(this.mode.is("Кольцо")));
/*   92 */     this.ringSpeed.visible(() -> Boolean.valueOf(this.mode.is("Кольцо")));
/*   93 */     addSettings(new Setting[] { (Setting)this.mode, (Setting)this.size, (Setting)this.rotateSpeed, (Setting)this.hurtColor, (Setting)this.ringRadius, (Setting)this.ringSpeed, (Setting)this.bmwGhostCount, (Setting)this.bmwGhostLife, (Setting)this.bmwStrengthXZ, (Setting)this.bmwStrengthY });
/*      */   }
/*      */ 
/*      */   
/*      */   public void onDisable() {
/*   98 */     this.appearValue = 0.0F;
/*   99 */     this.scaleValue = 0.0F;
/*  100 */     this.lastTarget = null;
/*  101 */     this.lastHandledTarget = null;
/*  102 */     this.lastTargetPos = null;
/*  103 */     this.rotProgress = 0.0F;
/*  104 */     this.rotFrom = -280.0F;
/*  105 */     this.rotTo = 280.0F;
/*  106 */     this.bmwPoints.clear();
/*  107 */     this.crystalRotationAngle = 0.0F;
/*  108 */     this.crystalAnimation = 0.0F;
/*  109 */     this.spawnAccumulator = 0.0F;
/*  110 */     this.lastCubeTime = 0L;
/*  111 */     this.cubeParticles.clear();
/*  112 */     this.renderCubeParticles.clear();
/*  113 */     super.onDisable();
/*      */   }
/*      */   
/*      */   private boolean isImageMode() {
/*  117 */     return (this.mode.is("Картинка 1") || this.mode.is("Картинка 2"));
/*      */   }
/*      */   
/*      */   private class_2960 getCaptureTexture() {
/*  121 */     if (this.mode.is("Картинка 2")) {
/*  122 */       return class_2960.method_60655("astra", "textures/targetesp/targetesp_3.png");
/*      */     }
/*  124 */     return class_2960.method_60655("astra", "textures/targetesp/targetesp_2.png");
/*      */   }
/*      */   
/*      */   private class_2960 getBloomTexture() {
/*  128 */     return class_2960.method_60655("astra", "textures/targetesp/bloom.png");
/*      */   }
/*      */   
/*      */   private int getESPColor() {
/*  132 */     int color = ColorUtils.getThemeColor();
/*  133 */     if ((color >> 24 & 0xFF) == 0) {
/*  134 */       color |= 0xFF000000;
/*      */     }
/*  136 */     return color;
/*      */   }
/*      */   
/*      */   private float animateTo(float current, float target, float delta) {
/*  140 */     if (current < target) {
/*  141 */       current = Math.min(current + delta, target);
/*  142 */     } else if (current > target) {
/*  143 */       current = Math.max(current - delta, target);
/*      */     } 
/*  145 */     return current;
/*      */   }
/*      */   
/*      */   private float getDistanceScale(class_243 cameraPos, double worldX, double worldY, double worldZ) {
/*  149 */     double dx = worldX - cameraPos.field_1352;
/*  150 */     double dy = worldY - cameraPos.field_1351;
/*  151 */     double dz = worldZ - cameraPos.field_1350;
/*  152 */     double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
/*  153 */     return (float)Math.max(0.1D, distance * 0.007000000216066837D);
/*      */   }
/*      */   
/*      */   @EventLink(priority = -100)
/*      */   public void onRender3D(Event3DRender event) {
/*  158 */     if (mc == null || mc.field_1724 == null || mc.field_1687 == null)
/*      */       return; 
/*  160 */     Aura aura = ModuleClass.aura;
/*  161 */     boolean auraEnabled = (aura != null && aura.isEnable());
/*  162 */     class_1309 target = auraEnabled ? aura.getTarget() : null;
/*  163 */     boolean hasTarget = (target != null && target.method_5805());
/*  164 */     float speed = 0.05F;
/*  165 */     this.appearValue = animateTo(this.appearValue, hasTarget ? 1.0F : 0.0F, speed);
/*  166 */     this.scaleValue = animateTo(this.scaleValue, hasTarget ? 1.0F : 0.5F, speed);
/*      */     
/*  168 */     if (hasTarget) {
/*  169 */       this.lastTarget = target;
/*  170 */       this.lastHandledTarget = target;
/*      */     } 
/*  172 */     if (this.mode.is("Кристаллы")) {
/*  173 */       float crystalSpeed = hasTarget ? 0.07F : 0.045F;
/*  174 */       this.crystalAnimation = animateTo(this.crystalAnimation, hasTarget ? 1.0F : 0.0F, crystalSpeed);
/*  175 */       if (hasTarget) {
/*  176 */         this.crystalRotationAngle += 0.8F;
/*      */       }
/*      */     } 
/*      */     
/*  180 */     if (this.appearValue <= 0.001F && !hasTarget && (
/*  181 */       !this.mode.is("Кристаллы") || this.crystalAnimation <= 0.001F)) {
/*  182 */       this.lastTarget = null;
/*  183 */       this.lastTargetPos = null;
/*      */       
/*      */       return;
/*      */     } 
/*  187 */     if (hasTarget && target != null) {
/*  188 */       float td = event.getTickDelta();
/*  189 */       this
/*      */ 
/*      */         
/*  192 */         .lastTargetPos = new class_243(class_3532.method_16436(td, target.field_6038, target.method_23317()), class_3532.method_16436(td, target.field_5971, target.method_23318()), class_3532.method_16436(td, target.field_5989, target.method_23321()));
/*      */       
/*  194 */       this.lastTargetHeight = target.method_17682();
/*  195 */       this.lastTargetWidth = target.method_17681();
/*      */     } 
/*  197 */     if (this.lastTargetPos == null)
/*      */       return; 
/*  199 */     if (this.mode.is("Райдер")) {
/*  200 */       if (hasTarget && target != null) {
/*  201 */         addBMWGhosts(target, event.getTickDelta(), 
/*  202 */             Math.max(1, Math.round(this.bmwGhostCount.getValue().floatValue())), 
/*  203 */             Math.max(1, Math.round(this.bmwGhostLife.getValue().floatValue())), 
/*  204 */             getESPColor());
/*      */       }
/*  206 */       this.bmwPoints.removeIf(GlowPoint::shouldRemove);
/*  207 */       drawBMW3D(event);
/*      */       return;
/*      */     } 
/*  210 */     if (this.mode.is("Кристаллы")) {
/*  211 */       class_1309 crystalTarget = hasTarget ? target : this.lastTarget;
/*  212 */       if ((crystalTarget != null || this.lastTargetPos != null) && this.crystalAnimation > 0.01F) {
/*  213 */         renderCrystals3D(event.getMatrices(), crystalTarget, event.getTickDelta());
/*      */       }
/*      */       return;
/*      */     } 
/*  217 */     if (isImageMode()) {
/*  218 */       renderMarker3D(event);
/*      */     }
/*  220 */     if (this.mode.is("Души")) {
/*  221 */       drawSouls3D(event);
/*      */     }
/*  223 */     if (this.mode.is("Призраки")) {
/*  224 */       drawCelka3D(event);
/*      */     }
/*  226 */     if (this.mode.is("Кольцо")) {
/*  227 */       drawRing3D(event);
/*      */     }
/*  229 */     if (this.mode.is("Кубы")) {
/*  230 */       renderCubes(event, target, hasTarget);
/*      */     }
/*      */   }
/*      */   
/*      */   private void renderCubes(Event3DRender event, class_1309 target, boolean hasTarget) {
/*  235 */     long now = System.currentTimeMillis();
/*  236 */     if (this.lastCubeTime == 0L) this.lastCubeTime = now; 
/*  237 */     float dt = Math.min((float)(now - this.lastCubeTime) / 1000.0F, 0.1F);
/*  238 */     this.lastCubeTime = now;
/*  239 */     if (!Float.isFinite(dt) || mc.field_1773 == null || mc.field_1773.method_19418() == null) {
/*      */       return;
/*      */     }
/*      */     
/*  243 */     if (hasTarget && target != null) {
/*  244 */       this.lastTarget = target;
/*  245 */       this.spawnAccumulator += dt;
/*  246 */       while (this.spawnAccumulator >= 0.022F) {
/*  247 */         this.spawnAccumulator -= 0.022F;
/*  248 */         if (this.cubeParticles.size() >= 72) {
/*      */           break;
/*      */         }
/*  251 */         for (int i2 = 0; i2 < 1; i2++) {
/*  252 */           double rand = Math.random() * 360.0D;
/*  253 */           double px = Math.cos(Math.toRadians(rand)) * 0.7D;
/*  254 */           double py = 0.02D + Math.random() * 0.1D;
/*  255 */           double pz = Math.sin(Math.toRadians(rand)) * 0.7D;
/*  256 */           this.cubeParticles.add(new CubeParticle(target, px, py, pz));
/*      */         } 
/*      */       } 
/*      */     } else {
/*  260 */       this.spawnAccumulator = 0.0F;
/*      */     } 
/*      */     
/*  263 */     this.renderCubeParticles.clear();
/*  264 */     for (int i = this.cubeParticles.size() - 1; i >= 0; i--) {
/*  265 */       CubeParticle particle = this.cubeParticles.get(i);
/*      */       try {
/*  267 */         particle.update(dt, now, hasTarget ? target : null);
/*  268 */         if (particle.shouldRemove(now)) {
/*  269 */           this.cubeParticles.remove(i);
/*      */         } else {
/*  271 */           this.renderCubeParticles.add(particle);
/*      */         } 
/*  273 */       } catch (Throwable ignored) {
/*  274 */         this.cubeParticles.remove(i);
/*      */       } 
/*      */     } 
/*      */     
/*  278 */     if (this.renderCubeParticles.isEmpty()) {
/*      */       return;
/*      */     }
/*      */     
/*  282 */     float partialTicks = event.getTickDelta();
/*  283 */     class_4587 matrices = event.getMatrices();
/*  284 */     class_243 camPos = mc.field_1773.method_19418().method_19326();
/*  285 */     class_1309 colorTarget = hasTarget ? target : this.lastTarget;
/*  286 */     float hurtPC = getHurtPC(colorTarget);
/*  287 */     int baseColor = getESPColor();
/*  288 */     int redColor = ColorUtils.rgb(255, 3, 3);
/*      */     
/*  290 */     RenderSystem.enableBlend();
/*  291 */     RenderSystem.enableDepthTest();
/*  292 */     RenderSystem.disableCull();
/*  293 */     RenderSystem.depthMask(false);
/*  294 */     RenderSystem.blendFunc(770, 1);
/*  295 */     RenderSystem.setShader(class_10142.field_53876);
/*      */     
/*  297 */     class_287 faceBuilder = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1576);
/*  298 */     boolean hasFaces = false;
/*  299 */     for (int j = 0, size = this.renderCubeParticles.size(); j < size; j++) {
/*  300 */       CubeParticle particle = this.renderCubeParticles.get(j);
/*      */       try {
/*  302 */         int particleColor = particle.getRenderColor(baseColor, redColor, hurtPC, now);
/*  303 */         if ((particleColor >> 24 & 0xFF) > 0 && 
/*  304 */           particle.appendCubeFaces(faceBuilder, matrices, camPos, partialTicks, particleColor)) {
/*  305 */           hasFaces = true;
/*      */         }
/*  307 */       } catch (Throwable throwable) {}
/*      */     } 
/*      */     
/*  310 */     if (hasFaces) class_286.method_43433(faceBuilder.method_60800());
/*      */     
/*  312 */     class_287 lineBuilder = class_289.method_1348().method_60827(class_293.class_5596.field_29344, class_290.field_1576);
/*  313 */     boolean hasLines = false;
/*  314 */     for (int k = 0, m = this.renderCubeParticles.size(); k < m; k++) {
/*  315 */       CubeParticle particle = this.renderCubeParticles.get(k);
/*      */       try {
/*  317 */         int particleColor = particle.getRenderColor(baseColor, redColor, hurtPC, now);
/*  318 */         if ((particleColor >> 24 & 0xFF) > 0 && 
/*  319 */           particle.appendCubeLines(lineBuilder, matrices, camPos, partialTicks, particleColor)) {
/*  320 */           hasLines = true;
/*      */         }
/*  322 */       } catch (Throwable throwable) {}
/*      */     } 
/*      */     
/*  325 */     if (hasLines) class_286.method_43433(lineBuilder.method_60800());
/*      */     
/*  327 */     RenderSystem.setShader(class_10142.field_53880);
/*  328 */     RenderSystem.setShaderTexture(0, getBloomTexture());
/*  329 */     class_287 bloomBuilder = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
/*  330 */     boolean hasBloom = false;
/*  331 */     float camYaw = mc.field_1773.method_19418().method_19330();
/*  332 */     float camPitch = mc.field_1773.method_19418().method_19329();
/*  333 */     for (int n = 0, i1 = this.renderCubeParticles.size(); n < i1; n++) {
/*  334 */       CubeParticle particle = this.renderCubeParticles.get(n);
/*      */       try {
/*  336 */         int particleColor = particle.getRenderColor(baseColor, redColor, hurtPC, now);
/*  337 */         if (particle.appendBloom(bloomBuilder, matrices, camPos, camYaw, camPitch, partialTicks, particleColor, now)) {
/*  338 */           hasBloom = true;
/*      */         }
/*  340 */       } catch (Throwable throwable) {}
/*      */     } 
/*      */     
/*  343 */     if (hasBloom) class_286.method_43433(bloomBuilder.method_60800());
/*      */     
/*  345 */     RenderSystem.depthMask(true);
/*  346 */     RenderSystem.defaultBlendFunc();
/*  347 */     RenderSystem.disableBlend();
/*  348 */     RenderSystem.enableCull();
/*  349 */     RenderSystem.enableDepthTest();
/*      */   } private void drawRing3D(Event3DRender event) {
/*      */     class_243 vec;
/*      */     float entityHeight;
/*  353 */     if (this.appearValue <= 0.001F || this.lastTargetPos == null)
/*      */       return; 
/*  355 */     float partialTicks = mc.method_61966().method_60637(true);
/*      */ 
/*      */     
/*  358 */     class_1309 target = this.lastTarget;
/*      */     
/*  360 */     if (target != null && target.method_5805()) {
/*      */ 
/*      */ 
/*      */       
/*  364 */       vec = new class_243(class_3532.method_16436(partialTicks, target.field_6038, target.method_23317()), class_3532.method_16436(partialTicks, target.field_5971, target.method_23318()), class_3532.method_16436(partialTicks, target.field_5989, target.method_23321()));
/*      */       
/*  366 */       entityHeight = target.method_17682();
/*      */     } else {
/*  368 */       vec = this.lastTargetPos;
/*  369 */       entityHeight = this.lastTargetHeight;
/*      */     } 
/*      */     
/*  372 */     class_243 cam = mc.field_1773.method_19418().method_19326();
/*  373 */     double x = vec.field_1352 - cam.field_1352;
/*  374 */     double y = vec.field_1351 - cam.field_1351;
/*  375 */     double z = vec.field_1350 - cam.field_1350;
/*      */     
/*  377 */     double duration = 2000.0D / this.ringSpeed.get();
/*  378 */     double elapsed = (System.currentTimeMillis() % (long)duration);
/*  379 */     boolean side = (elapsed > duration / 2.0D);
/*  380 */     double progress = elapsed / duration / 2.0D;
/*      */     
/*  382 */     if (side) {
/*  383 */       progress--;
/*      */     } else {
/*  385 */       progress = 1.0D - progress;
/*      */     } 
/*      */     
/*  388 */     progress = (progress < 0.5D) ? (2.0D * progress * progress) : (1.0D - Math.pow(-2.0D * progress + 2.0D, 2.0D) / 2.0D);
/*  389 */     double eased = entityHeight / 1.2D * ((progress > 0.5D) ? (1.0D - progress) : progress) * (side ? -1 : true);
/*      */     
/*  391 */     int baseCol = getESPColor();
/*  392 */     float hurtPC = getHurtPC(target);
/*  393 */     int redCol = ColorUtils.rgb(255, 3, 3);
/*  394 */     int mainColor = overCol(baseCol, redCol, hurtPC);
/*      */     
/*  396 */     int colorWithAlpha = setAlpha(mainColor, 0.88235295F * this.appearValue);
/*  397 */     int colorTransparent = setAlpha(mainColor, 0.003921569F * this.appearValue);
/*  398 */     int colorFull = setAlpha(mainColor, this.appearValue);
/*  399 */     double radius = this.ringRadius.get();
/*      */     
/*  401 */     class_4587 matrices = event.getMatrices();
/*  402 */     Matrix4f matrix = matrices.method_23760().method_23761();
/*      */     
/*  404 */     RenderSystem.depthMask(false);
/*  405 */     RenderSystem.disableDepthTest();
/*  406 */     RenderSystem.enableBlend();
/*  407 */     RenderSystem.blendFunc(770, 1);
/*  408 */     RenderSystem.disableCull();
/*  409 */     RenderSystem.setShader(class_10142.field_53876);
/*      */     
/*  411 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27380, class_290.field_1576);
/*  412 */     for (int i = 0; i <= 360; i++) {
/*  413 */       double rad = Math.toRadians(i);
/*  414 */       float px = (float)(x + Math.cos(rad) * radius);
/*  415 */       float pz = (float)(z + Math.sin(rad) * radius);
/*  416 */       float py1 = (float)(y + entityHeight * progress);
/*  417 */       float py2 = (float)(y + entityHeight * progress + eased);
/*      */       
/*  419 */       buffer.method_22918(matrix, px, py1, pz).method_39415(colorWithAlpha);
/*  420 */       buffer.method_22918(matrix, px, py2, pz).method_39415(colorTransparent);
/*      */     } 
/*  422 */     class_286.method_43433(buffer.method_60800());
/*      */     
/*  424 */     RenderSystem.lineWidth(1.5F);
/*  425 */     class_287 lineBuffer = class_289.method_1348().method_60827(class_293.class_5596.field_29345, class_290.field_1576);
/*  426 */     for (int j = 0; j <= 360; j++) {
/*  427 */       double rad = Math.toRadians(j);
/*  428 */       float px = (float)(x + Math.cos(rad) * radius);
/*  429 */       float pz = (float)(z + Math.sin(rad) * radius);
/*  430 */       float py = (float)(y + entityHeight * progress);
/*      */       
/*  432 */       lineBuffer.method_22918(matrix, px, py, pz).method_39415(colorFull);
/*      */     } 
/*  434 */     class_286.method_43433(lineBuffer.method_60800());
/*      */     
/*  436 */     RenderSystem.enableCull();
/*  437 */     RenderSystem.disableBlend();
/*  438 */     RenderSystem.depthMask(true);
/*  439 */     RenderSystem.enableDepthTest();
/*      */   }
/*      */   
/*      */   private int setAlpha(int color, float alpha) {
/*  443 */     alpha = Math.max(0.0F, Math.min(1.0F, alpha));
/*  444 */     return color & 0xFFFFFF | (int)(alpha * 255.0F) << 24;
/*      */   }
/*      */ 
/*      */   
/*      */   @EventLink(priority = -100)
/*      */   public void onRender2D(EventRender.Default event) {
/*  450 */     if (!this.mode.is("Кристаллы") || this.crystalAnimation <= 0.001F || this.lastTargetPos == null)
/*  451 */       return;  class_1309 crystalTarget = (this.lastTarget != null && this.lastTarget.method_5805()) ? this.lastTarget : null;
/*  452 */     drawCrystalGlow2D(event.getContext().method_51448(), crystalTarget);
/*      */   }
/*      */   
/*      */   private int multAlpha(int color, float mult) {
/*  456 */     int a = (int)((color >> 24 & 0xFF) * mult);
/*  457 */     a = Math.max(0, Math.min(255, a));
/*  458 */     return a << 24 | color & 0xFFFFFF;
/*      */   }
/*      */   
/*      */   private int replAlpha(int color, int alpha) {
/*  462 */     alpha = Math.max(0, Math.min(255, alpha));
/*  463 */     return alpha << 24 | color & 0xFFFFFF;
/*      */   }
/*      */   
/*      */   int overCol(int color1, int color2, float factor) {
/*  467 */     factor = Math.max(0.0F, Math.min(1.0F, factor));
/*  468 */     int r1 = color1 >> 16 & 0xFF, g1 = color1 >> 8 & 0xFF, b1 = color1 & 0xFF, a1 = color1 >> 24 & 0xFF;
/*  469 */     int r2 = color2 >> 16 & 0xFF, g2 = color2 >> 8 & 0xFF, b2 = color2 & 0xFF, a2 = color2 >> 24 & 0xFF;
/*  470 */     int r = (int)(r1 + (r2 - r1) * factor);
/*  471 */     int g = (int)(g1 + (g2 - g1) * factor);
/*  472 */     int b = (int)(b1 + (b2 - b1) * factor);
/*  473 */     int a = (int)(a1 + (a2 - a1) * factor);
/*  474 */     return a << 24 | r << 16 | g << 8 | b;
/*      */   }
/*      */   
/*      */   private float getHurtPC(class_1309 target) {
/*  478 */     if (!this.hurtColor.isState() || target == null) return 0.0F; 
/*  479 */     float partialTicks = (mc != null) ? mc.method_61966().method_60637(true) : 0.0F;
/*  480 */     float hurtTicks = class_3532.method_15363(target.field_6235 - partialTicks, 0.0F, 10.0F);
/*  481 */     float progress = hurtTicks / 10.0F;
/*  482 */     return progress * progress * (3.0F - 2.0F * progress);
/*      */   }
/*      */   
/*      */   private void drawBillboard(class_4587 matrices, class_243 cameraPos, double worldX, double worldY, double worldZ, float baseScreenSize, int color, float rotation) {
/*  486 */     float distScale = getDistanceScale(cameraPos, worldX, worldY, worldZ);
/*  487 */     float half = baseScreenSize * distScale * 0.5F;
/*  488 */     drawBillboardInternal(matrices, cameraPos, worldX, worldY, worldZ, half, color, rotation);
/*      */   }
/*      */   
/*      */   private void drawStaticBillboard(class_4587 matrices, class_243 cameraPos, double worldX, double worldY, double worldZ, float worldSize, int color, float rotation) {
/*  492 */     float half = worldSize * 0.5F;
/*  493 */     drawBillboardInternal(matrices, cameraPos, worldX, worldY, worldZ, half, color, rotation);
/*      */   }
/*      */   
/*      */   private void drawBillboardInternal(class_4587 matrices, class_243 cameraPos, double worldX, double worldY, double worldZ, float half, int color, float rotation) {
/*  497 */     int r = color >> 16 & 0xFF;
/*  498 */     int g = color >> 8 & 0xFF;
/*  499 */     int b = color & 0xFF;
/*  500 */     int a = color >> 24 & 0xFF;
/*  501 */     if (a <= 0)
/*      */       return; 
/*  503 */     matrices.method_22903();
/*  504 */     matrices.method_22904(worldX - cameraPos.field_1352, worldY - cameraPos.field_1351, worldZ - cameraPos.field_1350);
/*  505 */     matrices.method_22907(class_7833.field_40716.rotationDegrees(-mc.field_1773.method_19418().method_19330()));
/*  506 */     matrices.method_22907(class_7833.field_40714.rotationDegrees(mc.field_1773.method_19418().method_19329()));
/*  507 */     if (rotation != 0.0F) {
/*  508 */       matrices.method_22907(class_7833.field_40718.rotationDegrees(rotation));
/*      */     }
/*      */     
/*  511 */     Matrix4f matrix = matrices.method_23760().method_23761();
/*  512 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
/*  513 */     buffer.method_22918(matrix, -half, -half, 0.0F).method_22913(0.0F, 1.0F).method_1336(r, g, b, a);
/*  514 */     buffer.method_22918(matrix, -half, half, 0.0F).method_22913(0.0F, 0.0F).method_1336(r, g, b, a);
/*  515 */     buffer.method_22918(matrix, half, half, 0.0F).method_22913(1.0F, 0.0F).method_1336(r, g, b, a);
/*  516 */     buffer.method_22918(matrix, half, -half, 0.0F).method_22913(1.0F, 1.0F).method_1336(r, g, b, a);
/*  517 */     class_286.method_43433(buffer.method_60800());
/*  518 */     matrices.method_22909();
/*      */   }
/*      */   
/*      */   private void renderMarker3D(Event3DRender event) {
/*  522 */     if (this.lastTargetPos == null || this.appearValue <= 0.001F)
/*      */       return; 
/*  524 */     class_243 cam = mc.field_1773.method_19418().method_19326();
/*  525 */     double worldX = this.lastTargetPos.field_1352;
/*  526 */     double worldY = this.lastTargetPos.field_1351 + ((this.lastTargetHeight + 0.4F) * 0.5F);
/*  527 */     double worldZ = this.lastTargetPos.field_1350;
/*      */     
/*  529 */     float baseSize = this.size.getValue().floatValue() * 12.0F;
/*  530 */     float renderSize = baseSize * this.scaleValue;
/*      */     
/*  532 */     long now = System.currentTimeMillis();
/*  533 */     float dt = Math.max(0.001F, (float)(now - this.lastRotateUpdate) / 1000.0F);
/*  534 */     this.lastRotateUpdate = now;
/*  535 */     float cycleDuration = Math.max(0.35F, 2.2F / this.rotateSpeed.getValue().floatValue());
/*  536 */     this.rotProgress += dt / cycleDuration;
/*  537 */     while (this.rotProgress >= 1.0F) {
/*  538 */       this.rotProgress--;
/*  539 */       this.rotFrom = this.rotTo;
/*  540 */       this.rotTo = (this.rotTo > 0.0F) ? -280.0F : 280.0F;
/*      */     } 
/*      */     
/*  543 */     float accel = (float)Easings.SINE_IN_OUT.ease(this.rotProgress);
/*  544 */     float rotation = class_3532.method_16439(accel, this.rotFrom, this.rotTo);
/*      */     
/*  546 */     float hurtPC = getHurtPC(this.lastTarget);
/*  547 */     int baseColor = multAlpha(getESPColor(), this.appearValue);
/*  548 */     int redColor = multAlpha(ColorUtils.rgb(255, 3, 3), this.appearValue);
/*  549 */     int color = overCol(baseColor, redColor, hurtPC);
/*      */     
/*  551 */     RenderSystem.enableBlend();
/*  552 */     RenderSystem.disableDepthTest();
/*  553 */     RenderSystem.depthMask(false);
/*  554 */     RenderSystem.disableCull();
/*  555 */     RenderSystem.blendFunc(770, 1);
/*  556 */     RenderSystem.setShader(class_10142.field_53880);
/*  557 */     RenderSystem.setShaderTexture(0, getCaptureTexture());
/*      */     
/*  559 */     drawBillboard(event.getMatrices(), cam, worldX, worldY, worldZ, renderSize, color, rotation);
/*      */     
/*  561 */     RenderSystem.enableCull();
/*  562 */     RenderSystem.depthMask(true);
/*  563 */     RenderSystem.enableDepthTest();
/*  564 */     RenderSystem.defaultBlendFunc();
/*  565 */     RenderSystem.disableBlend();
/*      */   } private void drawSouls3D(Event3DRender event) {
/*      */     class_243 vec;
/*      */     float height;
/*  569 */     if (this.appearValue <= 0.001F || this.lastTargetPos == null)
/*      */       return; 
/*  571 */     float partialTicks = mc.method_61966().method_60637(true);
/*      */ 
/*      */     
/*  574 */     class_1309 target = this.lastTarget;
/*  575 */     if (target != null && target.method_5805()) {
/*      */ 
/*      */ 
/*      */       
/*  579 */       vec = new class_243(class_3532.method_16436(partialTicks, target.field_6038, target.method_23317()), class_3532.method_16436(partialTicks, target.field_5971, target.method_23318()), class_3532.method_16436(partialTicks, target.field_5989, target.method_23321()));
/*      */       
/*  581 */       height = target.method_17682();
/*      */     } else {
/*  583 */       vec = this.lastTargetPos;
/*  584 */       height = this.lastTargetHeight;
/*      */     } 
/*      */     
/*  587 */     class_243 cam = mc.field_1773.method_19418().method_19326();
/*  588 */     double baseX = vec.field_1352;
/*  589 */     double baseY = vec.field_1351 + (height / 2.0F);
/*  590 */     double baseZ = vec.field_1350;
/*  591 */     double radius = 0.7D;
/*  592 */     float fixedSize = 4.0F;
/*  593 */     long time = System.currentTimeMillis();
/*  594 */     float hurtPC = getHurtPC(target);
/*  595 */     int baseCol = getESPColor();
/*  596 */     int redCol = ColorUtils.rgb(255, 3, 3);
/*      */     
/*  598 */     RenderSystem.disableDepthTest();
/*  599 */     RenderSystem.enableBlend();
/*  600 */     RenderSystem.depthMask(false);
/*  601 */     RenderSystem.disableCull();
/*  602 */     RenderSystem.blendFunc(770, 1);
/*  603 */     RenderSystem.setShader(class_10142.field_53880);
/*  604 */     RenderSystem.setShaderTexture(0, getBloomTexture());
/*      */     
/*  606 */     class_4587 matrices = event.getMatrices();
/*      */     int i;
/*  608 */     for (i = 0; i < 20; i++) {
/*  609 */       float trailFactor = 1.0F - i / 20.0F * 0.7F;
/*  610 */       double angle = 0.15D * (time - i * 10.0D) / 25.0D;
/*  611 */       double s = Math.sin(angle) * radius;
/*  612 */       double c = Math.cos(angle) * radius;
/*  613 */       double worldX = baseX + s;
/*  614 */       double worldY = baseY + c;
/*  615 */       double worldZ = baseZ - c;
/*      */       
/*  617 */       float sz = fixedSize * trailFactor;
/*  618 */       float alphaTrail = this.appearValue * 0.6F;
/*  619 */       int col = multAlpha(baseCol, alphaTrail * this.appearValue);
/*  620 */       int red = multAlpha(redCol, alphaTrail * this.appearValue);
/*  621 */       int color = overCol(col, red, hurtPC);
/*      */       
/*  623 */       drawStaticBillboard(matrices, cam, worldX, worldY, worldZ, sz * 0.12F, color, 0.0F);
/*  624 */       int glowColor = multAlpha(color, 0.45F);
/*  625 */       drawStaticBillboard(matrices, cam, worldX, worldY, worldZ, sz * 0.21F, glowColor, 0.0F);
/*      */     } 
/*      */     
/*  628 */     for (i = 0; i < 20; i++) {
/*  629 */       float trailFactor = 1.0F - i / 20.0F * 0.7F;
/*  630 */       double angle = 0.15D * (time - i * 10.0D) / 25.0D;
/*  631 */       double s = Math.sin(angle) * radius;
/*  632 */       double c = Math.cos(angle) * radius;
/*  633 */       double worldX = baseX - s;
/*  634 */       double worldY = baseY + s;
/*  635 */       double worldZ = baseZ - c;
/*      */       
/*  637 */       float sz = fixedSize * trailFactor;
/*  638 */       float alphaTrail = this.appearValue * 0.6F;
/*  639 */       int col = multAlpha(baseCol, alphaTrail * this.appearValue);
/*  640 */       int red = multAlpha(ColorUtils.rgb(235, 7, 7), alphaTrail * this.appearValue);
/*  641 */       int color = overCol(col, red, hurtPC);
/*      */       
/*  643 */       drawStaticBillboard(matrices, cam, worldX, worldY, worldZ, sz * 0.12F, color, 0.0F);
/*  644 */       int glowColor = multAlpha(color, 0.45F);
/*  645 */       drawStaticBillboard(matrices, cam, worldX, worldY, worldZ, sz * 0.21F, glowColor, 0.0F);
/*      */     } 
/*      */     
/*  648 */     for (i = 0; i < 20; i++) {
/*  649 */       float trailFactor = 1.0F - i / 20.0F * 0.7F;
/*  650 */       double angle = 0.15D * (time - i * 10.0D) / 25.0D;
/*  651 */       double s = Math.sin(angle) * radius;
/*  652 */       double c = Math.cos(angle) * radius;
/*  653 */       double worldX = baseX - s;
/*  654 */       double worldY = baseY - s;
/*  655 */       double worldZ = baseZ + c;
/*      */       
/*  657 */       float sz = fixedSize * trailFactor;
/*  658 */       float alphaTrail = this.appearValue * 0.6F;
/*  659 */       int col = multAlpha(baseCol, alphaTrail * this.appearValue);
/*  660 */       int red = multAlpha(redCol, alphaTrail * this.appearValue);
/*  661 */       int color = overCol(col, red, hurtPC);
/*      */       
/*  663 */       drawStaticBillboard(matrices, cam, worldX, worldY, worldZ, sz * 0.12F, color, 0.0F);
/*  664 */       int glowColor = multAlpha(color, 0.45F);
/*  665 */       drawStaticBillboard(matrices, cam, worldX, worldY, worldZ, sz * 0.21F, glowColor, 0.0F);
/*      */     } 
/*      */     
/*  668 */     RenderSystem.enableCull();
/*  669 */     RenderSystem.enableDepthTest();
/*  670 */     RenderSystem.defaultBlendFunc();
/*  671 */     RenderSystem.disableBlend();
/*  672 */     RenderSystem.depthMask(true);
/*      */   }
/*      */   
/*      */   private void addBMWGhosts(class_1309 entity, float partialTicks, int cornersCount, int maxTime, int colorBase) {
/*  676 */     float xzRange = 0.7F;
/*  677 */     float yRange = entity.method_17682();
/*  678 */     int delayXZ = (int)this.bmwStrengthXZ.getValue().floatValue();
/*  679 */     int delayY = (int)this.bmwStrengthY.getValue().floatValue();
/*  680 */     long time = System.currentTimeMillis();
/*  681 */     float rotateProgress = (float)(time % delayXZ) / delayXZ;
/*  682 */     float xzRotate = rotateProgress * 360.0F;
/*  683 */     float yProgress = (float)(time % delayY) / delayY;
/*  684 */     float yLrpPC = 0.5F - 0.5F * class_3532.method_15362(yProgress * 6.2831855F);
/*      */     
/*  686 */     for (int corner = 0; corner < cornersCount; corner++) {
/*  687 */       float cornersPC = corner / cornersCount;
/*  688 */       double yawRad = Math.toRadians(class_3532.method_15393(cornersPC * 360.0F + xzRotate));
/*  689 */       float offsetX = -((float)Math.sin(yawRad)) * xzRange;
/*  690 */       float offsetY = yRange * yLrpPC;
/*  691 */       float offsetZ = (float)Math.cos(yawRad) * xzRange;
/*  692 */       this.bmwPoints.add(new GlowPoint(offsetX, offsetY, offsetZ, maxTime, colorBase));
/*      */     } 
/*      */   }
/*      */   private void drawBMW3D(Event3DRender event) {
/*      */     class_243 basePos;
/*  697 */     if (this.bmwPoints.isEmpty() || this.appearValue <= 0.001F)
/*      */       return; 
/*  699 */     class_1309 renderTarget = (this.lastTarget != null) ? this.lastTarget : this.lastHandledTarget;
/*  700 */     if (renderTarget == null && this.lastTargetPos == null)
/*      */       return; 
/*  702 */     float partialTicks = mc.method_61966().method_60637(true);
/*      */     
/*  704 */     if (renderTarget != null && renderTarget.method_5805()) {
/*      */ 
/*      */ 
/*      */       
/*  708 */       basePos = new class_243(class_3532.method_16436(partialTicks, renderTarget.field_6038, renderTarget.method_23317()), class_3532.method_16436(partialTicks, renderTarget.field_5971, renderTarget.method_23318()), class_3532.method_16436(partialTicks, renderTarget.field_5989, renderTarget.method_23321()));
/*      */     } else {
/*      */       
/*  711 */       basePos = this.lastTargetPos;
/*      */     } 
/*      */     
/*  714 */     if (basePos == null)
/*      */       return; 
/*  716 */     class_243 cam = mc.field_1773.method_19418().method_19326();
/*  717 */     float hurtPC = getHurtPC(renderTarget);
/*  718 */     float fixedScreenSize = 6.0F;
/*      */     
/*  720 */     RenderSystem.disableDepthTest();
/*  721 */     RenderSystem.enableBlend();
/*  722 */     RenderSystem.depthMask(false);
/*  723 */     RenderSystem.disableCull();
/*  724 */     RenderSystem.blendFunc(770, 1);
/*  725 */     RenderSystem.setShader(class_10142.field_53880);
/*  726 */     RenderSystem.setShaderTexture(0, getBloomTexture());
/*      */     
/*  728 */     class_4587 matrices = event.getMatrices();
/*      */     
/*  730 */     for (GlowPoint point : this.bmwPoints) {
/*  731 */       float timePC = point.getTimeProgress();
/*  732 */       float trailFactor = 1.0F - timePC * 0.6F;
/*      */       
/*  734 */       double worldX = basePos.field_1352 + point.x;
/*  735 */       double worldY = basePos.field_1351 + point.y;
/*  736 */       double worldZ = basePos.field_1350 + point.z;
/*      */       
/*  738 */       float sz = fixedScreenSize * trailFactor;
/*  739 */       int alpha = (int)(255.0F * this.appearValue * trailFactor * 0.8F);
/*  740 */       alpha = Math.max(0, Math.min(255, alpha));
/*  741 */       int col = replAlpha(point.baseColor, alpha);
/*  742 */       int red = replAlpha(ColorUtils.rgb(255, 3, 3), alpha);
/*  743 */       int finalColor = overCol(col, red, hurtPC);
/*      */       
/*  745 */       drawBillboard(matrices, cam, worldX, worldY, worldZ, sz, finalColor, 0.0F);
/*      */     } 
/*      */     
/*  748 */     RenderSystem.enableCull();
/*  749 */     RenderSystem.enableDepthTest();
/*  750 */     RenderSystem.defaultBlendFunc();
/*  751 */     RenderSystem.disableBlend();
/*  752 */     RenderSystem.depthMask(true);
/*      */   }
/*      */   private void drawCelka3D(Event3DRender event) {
/*      */     class_243 vec;
/*  756 */     if (this.appearValue <= 0.001F || this.lastTargetPos == null)
/*      */       return; 
/*  758 */     float partialTicks = mc.method_61966().method_60637(true);
/*      */ 
/*      */     
/*  761 */     class_1309 target = this.lastTarget;
/*  762 */     if (target != null && target.method_5805()) {
/*      */ 
/*      */ 
/*      */       
/*  766 */       vec = new class_243(class_3532.method_16436(partialTicks, target.field_6038, target.method_23317()), class_3532.method_16436(partialTicks, target.field_5971, target.method_23318()), class_3532.method_16436(partialTicks, target.field_5989, target.method_23321()));
/*      */       
/*  768 */       float entityHeight = target.method_17682();
/*      */     } else {
/*  770 */       vec = this.lastTargetPos;
/*  771 */       float entityHeight = this.lastTargetHeight;
/*      */     } 
/*      */     
/*  774 */     class_243 cam = mc.field_1773.method_19418().method_19326();
/*  775 */     double bx = vec.field_1352;
/*  776 */     double by = vec.field_1351;
/*  777 */     double bz = vec.field_1350;
/*  778 */     double t = System.currentTimeMillis() / 384.61539872299335D * 1.2000000476837158D;
/*  779 */     double tv = System.currentTimeMillis() / 666.6666666666666D * 1.2000000476837158D;
/*  780 */     int baseCol = getESPColor();
/*  781 */     float fixedSize = 4.0F;
/*      */     
/*  783 */     RenderSystem.disableDepthTest();
/*  784 */     RenderSystem.enableBlend();
/*  785 */     RenderSystem.depthMask(false);
/*  786 */     RenderSystem.disableCull();
/*  787 */     RenderSystem.blendFunc(770, 1);
/*  788 */     RenderSystem.setShader(class_10142.field_53880);
/*  789 */     RenderSystem.setShaderTexture(0, getBloomTexture());
/*      */     
/*  791 */     class_4587 matrices = event.getMatrices();
/*      */     
/*  793 */     float radius = 0.65F;
/*  794 */     for (int k = 0; k < 4; k++) {
/*  795 */       for (int j = 0; j < 20; j++) {
/*  796 */         float kf = j / 20.0F;
/*  797 */         float sizeFactor = 1.0F - kf * 0.55F;
/*      */         
/*  799 */         double tj = t - j * 0.05D;
/*  800 */         double tvj = tv - j * 0.05D;
/*  801 */         double cyc = (Math.sin(tvj) + 1.0D) * 0.5D;
/*      */         
/*  803 */         double baseAngle = Math.toRadians(k * 90.0D + tj * 50.0D % 360.0D);
/*  804 */         double offX = Math.cos(baseAngle) * radius;
/*  805 */         double offZ = Math.sin(baseAngle) * radius;
/*      */ 
/*      */         
/*  808 */         double offY = (k % 2 == 0) ? (0.1D + 1.7D * cyc) : (1.8D - 1.7D * cyc);
/*      */         
/*  810 */         double worldX = bx + offX;
/*  811 */         double worldY = by + offY;
/*  812 */         double worldZ = bz + offZ;
/*      */         
/*  814 */         float sz = fixedSize * sizeFactor;
/*  815 */         int finalAlpha = (int)(255.0F * this.appearValue * 0.6F);
/*  816 */         int color = replAlpha(baseCol, finalAlpha);
/*      */         
/*  818 */         drawBillboard(matrices, cam, worldX, worldY, worldZ, sz, color, 0.0F);
/*  819 */         int glowColor = multAlpha(color, 0.45F);
/*  820 */         drawBillboard(matrices, cam, worldX, worldY, worldZ, sz * 1.75F, glowColor, 0.0F);
/*      */       } 
/*  822 */       radius *= -1.0F;
/*      */     } 
/*      */     
/*  825 */     RenderSystem.enableCull();
/*  826 */     RenderSystem.enableDepthTest();
/*  827 */     RenderSystem.defaultBlendFunc();
/*  828 */     RenderSystem.disableBlend();
/*  829 */     RenderSystem.depthMask(true);
/*      */   }
/*      */   
/*      */   private void renderCrystals3D(class_4587 ms, class_1309 target, float partialTicks) {
/*      */     class_243 renderPos;
/*  834 */     if (this.lastTargetPos == null || this.crystalAnimation <= 0.01F)
/*      */       return; 
/*  836 */     class_243 cameraPos = mc.field_1773.method_19418().method_19326();
/*  837 */     int baseColor = ColorUtils.getThemeColor();
/*  838 */     int color = multAlpha(baseColor, this.crystalAnimation);
/*  839 */     int glowColor = multAlpha(baseColor, this.crystalAnimation * 0.28F);
/*  840 */     float hurtPC = getHurtPC(target);
/*  841 */     if (hurtPC > 0.0F) {
/*  842 */       int hurtColor = multAlpha(ColorUtils.rgb(255, 3, 3), this.crystalAnimation);
/*  843 */       color = overCol(color, hurtColor, hurtPC);
/*  844 */       glowColor = overCol(glowColor, multAlpha(hurtColor, 0.65F), hurtPC);
/*      */     } 
/*      */     
/*  847 */     float entityWidth = (target != null) ? target.method_17681() : this.lastTargetWidth;
/*  848 */     float entityHeight = (target != null) ? target.method_17682() : this.lastTargetHeight;
/*  849 */     float width = entityWidth * 1.5F;
/*      */ 
/*      */     
/*  852 */     if (target != null && target.method_5805()) {
/*      */ 
/*      */ 
/*      */       
/*  856 */       renderPos = new class_243(class_3532.method_16436(partialTicks, target.field_6038, target.method_23317()), class_3532.method_16436(partialTicks, target.field_5971, target.method_23318()), class_3532.method_16436(partialTicks, target.field_5989, target.method_23321()));
/*      */     } else {
/*      */       
/*  859 */       renderPos = this.lastTargetPos;
/*      */     } 
/*      */     
/*  862 */     RenderSystem.disableDepthTest();
/*  863 */     RenderSystem.enableBlend();
/*  864 */     RenderSystem.depthMask(false);
/*  865 */     RenderSystem.disableCull();
/*      */     
/*  867 */     float orbitScale = 1.2F - 0.5F * this.crystalAnimation;
/*  868 */     ms.method_22903();
/*  869 */     ms.method_22904(renderPos.field_1352 - cameraPos.field_1352, renderPos.field_1351 - cameraPos.field_1351, renderPos.field_1350 - cameraPos.field_1350);
/*      */     
/*  871 */     RenderSystem.defaultBlendFunc();
/*  872 */     RenderSystem.setShader(class_10142.field_53876);
/*      */     
/*  874 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27379, class_290.field_1576);
/*      */     
/*  876 */     for (int i = 0; i < 360; i += 20) {
/*  877 */       float angleRad = (float)Math.toRadians((i + this.crystalRotationAngle));
/*  878 */       float sin = (float)(Math.sin(angleRad) * width * orbitScale);
/*  879 */       float cos = (float)(Math.cos(angleRad) * width * orbitScale);
/*  880 */       float crystalSize = 0.1F;
/*  881 */       float yOffset = 0.1F + entityHeight * Math.abs(class_3532.method_15374(i));
/*      */       
/*  883 */       float offsetX = sin;
/*  884 */       float offsetY = yOffset;
/*  885 */       float offsetZ = cos;
/*  886 */       float targetCenterY = entityHeight / 2.0F;
/*  887 */       float dirX = -offsetX;
/*  888 */       float dirY = targetCenterY - offsetY;
/*  889 */       float dirZ = -offsetZ;
/*      */       
/*  891 */       float length = (float)Math.sqrt((dirX * dirX + dirY * dirY + dirZ * dirZ));
/*  892 */       if (length >= 0.001F) {
/*      */         
/*  894 */         dirX /= length;
/*  895 */         dirY /= length;
/*  896 */         dirZ /= length;
/*  897 */         ms.method_22903();
/*  898 */         ms.method_46416(offsetX, offsetY, offsetZ);
/*  899 */         Vector3f initial = new Vector3f(0.0F, 1.0F, 0.0F);
/*  900 */         Vector3f dir = new Vector3f(dirX, dirY, dirZ);
/*  901 */         Vector3f axis = new Vector3f();
/*  902 */         initial.cross((Vector3fc)dir, axis);
/*  903 */         float axisLen = axis.length();
/*  904 */         if (axisLen >= 0.001F) {
/*  905 */           axis.div(axisLen);
/*  906 */           float dot = Math.max(-1.0F, Math.min(1.0F, initial.dot((Vector3fc)dir)));
/*  907 */           float angle = (float)Math.acos(dot);
/*  908 */           ms.method_22907((new Quaternionf()).setAngleAxis(angle, axis.x, axis.y, axis.z));
/*      */         } 
/*  910 */         renderCrystalShape(buffer, ms.method_23760().method_23761(), crystalSize, color);
/*      */         
/*  912 */         ms.method_22909();
/*      */       } 
/*      */     } 
/*  915 */     class_286.method_43433(buffer.method_60800());
/*      */     
/*  917 */     ms.method_22909();
/*      */     
/*  919 */     float glowBaseSize = 4.5F + entityWidth * 3.0F;
/*  920 */     float outerGlowSize = glowBaseSize * 1.28F;
/*  921 */     RenderSystem.blendFunc(770, 1);
/*  922 */     RenderSystem.setShader(class_10142.field_53880);
/*  923 */     RenderSystem.setShaderTexture(0, getBloomTexture());
/*      */     
/*  925 */     for (int j = 0; j < 360; j += 20) {
/*  926 */       float angleRad = (float)Math.toRadians((j + this.crystalRotationAngle));
/*  927 */       float sin = (float)(Math.sin(angleRad) * width * orbitScale);
/*  928 */       float cos = (float)(Math.cos(angleRad) * width * orbitScale);
/*  929 */       float yOffset = 0.1F + entityHeight * Math.abs(class_3532.method_15374(j));
/*      */       
/*  931 */       double worldX = renderPos.field_1352 + sin;
/*  932 */       double worldY = renderPos.field_1351 + yOffset;
/*  933 */       double worldZ = renderPos.field_1350 + cos;
/*  934 */       drawBillboard(ms, cameraPos, worldX, worldY, worldZ, outerGlowSize, multAlpha(glowColor, 0.24F), this.crystalRotationAngle + j);
/*  935 */       drawBillboard(ms, cameraPos, worldX, worldY, worldZ, glowBaseSize, glowColor, -(this.crystalRotationAngle + j * 0.5F));
/*      */     } 
/*      */     
/*  938 */     RenderSystem.enableDepthTest();
/*  939 */     RenderSystem.enableCull();
/*  940 */     RenderSystem.defaultBlendFunc();
/*  941 */     RenderSystem.disableBlend();
/*  942 */     RenderSystem.depthMask(true);
/*      */   }
/*      */   
/*      */   private void renderCrystalShape(class_287 buffer, Matrix4f matrix, float size, int color) {
/*  946 */     int r = color >> 16 & 0xFF;
/*  947 */     int g = color >> 8 & 0xFF;
/*  948 */     int b = color & 0xFF;
/*  949 */     int a = color >> 24 & 0xFF;
/*      */     
/*  951 */     float w = 0.34F * size / 0.1F;
/*  952 */     float h = 1.15F * size / 0.1F;
/*  953 */     w = 0.06F;
/*  954 */     h = 0.2F;
/*  955 */     tri(buffer, matrix, 0.0F, h, 0.0F, w, 0.0F, 0.0F, 0.0F, 0.0F, w, r, g, b, a);
/*  956 */     tri(buffer, matrix, 0.0F, h, 0.0F, 0.0F, 0.0F, w, -w, 0.0F, 0.0F, r, g, b, a);
/*  957 */     tri(buffer, matrix, 0.0F, h, 0.0F, -w, 0.0F, 0.0F, 0.0F, 0.0F, -w, r, g, b, a);
/*  958 */     tri(buffer, matrix, 0.0F, h, 0.0F, 0.0F, 0.0F, -w, w, 0.0F, 0.0F, r, g, b, a);
/*  959 */     tri(buffer, matrix, 0.0F, -h, 0.0F, w, 0.0F, 0.0F, 0.0F, 0.0F, w, r, g, b, a);
/*  960 */     tri(buffer, matrix, 0.0F, -h, 0.0F, 0.0F, 0.0F, w, -w, 0.0F, 0.0F, r, g, b, a);
/*  961 */     tri(buffer, matrix, 0.0F, -h, 0.0F, -w, 0.0F, 0.0F, 0.0F, 0.0F, -w, r, g, b, a);
/*  962 */     tri(buffer, matrix, 0.0F, -h, 0.0F, 0.0F, 0.0F, -w, w, 0.0F, 0.0F, r, g, b, a);
/*      */   }
/*      */   
/*      */   private float[] project2D(double worldX, double worldY, double worldZ) {
/*  966 */     return null;
/*      */   }
/*      */   
/*      */   private double getScale(double worldX, double worldY, double worldZ) {
/*  970 */     class_243 cam = mc.field_1773.method_19418().method_19326();
/*  971 */     double dx = worldX - cam.field_1352;
/*  972 */     double dy = worldY - cam.field_1351;
/*  973 */     double dz = worldZ - cam.field_1350;
/*  974 */     double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
/*  975 */     return Math.max(0.5D, 8.0D / Math.max(0.1D, distance));
/*      */   }
/*      */   
/*      */   private void drawTexturedRect2D(class_4587 matrix, float x, float y, float width, float height, int color) {
/*  979 */     int r = color >> 16 & 0xFF;
/*  980 */     int g = color >> 8 & 0xFF;
/*  981 */     int b = color & 0xFF;
/*  982 */     int a = color >> 24 & 0xFF;
/*  983 */     if (a <= 0)
/*  984 */       return;  Matrix4f mat = matrix.method_23760().method_23761();
/*  985 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
/*  986 */     buffer.method_22918(mat, x, y, 0.0F).method_22913(0.0F, 0.0F).method_1336(r, g, b, a);
/*  987 */     buffer.method_22918(mat, x, y + height, 0.0F).method_22913(0.0F, 1.0F).method_1336(r, g, b, a);
/*  988 */     buffer.method_22918(mat, x + width, y + height, 0.0F).method_22913(1.0F, 1.0F).method_1336(r, g, b, a);
/*  989 */     buffer.method_22918(mat, x + width, y, 0.0F).method_22913(1.0F, 0.0F).method_1336(r, g, b, a);
/*  990 */     class_286.method_43433(buffer.method_60800());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void drawCrystalGlow2D(class_4587 matrix, class_1309 target) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void tri(class_287 buffer, Matrix4f matrix, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, int r, int g, int b, int a) {
/* 1002 */     buffer.method_22918(matrix, x1, y1, z1).method_1336(r, g, b, a);
/* 1003 */     buffer.method_22918(matrix, x2, y2, z2).method_1336(r, g, b, a);
/* 1004 */     buffer.method_22918(matrix, x3, y3, z3).method_1336(r, g, b, a);
/*      */   }
/*      */   private static class GlowPoint { final float x;
/*      */     final float y;
/*      */     final float z;
/*      */     final long startTime;
/*      */     final int maxLife;
/*      */     final int baseColor;
/*      */     
/*      */     GlowPoint(float x, float y, float z, int maxLife, int baseColor) {
/* 1014 */       this.x = x;
/* 1015 */       this.y = y;
/* 1016 */       this.z = z;
/* 1017 */       this.startTime = System.currentTimeMillis();
/* 1018 */       this.maxLife = maxLife;
/* 1019 */       this.baseColor = baseColor;
/*      */     }
/*      */     
/*      */     boolean shouldRemove() {
/* 1023 */       return (System.currentTimeMillis() - this.startTime >= this.maxLife);
/*      */     }
/*      */     
/*      */     float getTimeProgress() {
/* 1027 */       return class_3532.method_15363((float)(System.currentTimeMillis() - this.startTime) / this.maxLife, 0.0F, 1.0F);
/*      */     }
/*      */     
/*      */     int getColor(float timePC) {
/* 1031 */       int a = (int)((this.baseColor >> 24 & 0xFF) * (1.0F - timePC));
/* 1032 */       a = Math.max(0, Math.min(255, a));
/* 1033 */       return a << 24 | this.baseColor & 0xFFFFFF;
/*      */     } }
/*      */ 
/*      */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\TargetESP.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */