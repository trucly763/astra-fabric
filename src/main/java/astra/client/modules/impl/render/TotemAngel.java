/*      */ package shame.astra.client.modules.impl.render;
/*      */ import com.mojang.blaze3d.systems.RenderSystem;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.concurrent.CopyOnWriteArrayList;
/*      */ import java.util.concurrent.ThreadLocalRandom;
/*      */ import net.minecraft.class_10142;
/*      */ import net.minecraft.class_1297;
/*      */ import net.minecraft.class_243;
/*      */ import net.minecraft.class_2596;
/*      */ import net.minecraft.class_2663;
/*      */ import net.minecraft.class_286;
/*      */ import net.minecraft.class_287;
/*      */ import net.minecraft.class_289;
/*      */ import net.minecraft.class_290;
/*      */ import net.minecraft.class_293;
/*      */ import net.minecraft.class_2960;
/*      */ import net.minecraft.class_3532;
/*      */ import net.minecraft.class_4587;
/*      */ import net.minecraft.class_742;
/*      */ import net.minecraft.class_7833;
/*      */ import org.joml.Matrix4f;
/*      */ import org.lwjgl.opengl.GL11;
/*      */ import shame.astra.api.events.EventLink;
/*      */ import shame.astra.api.events.implement.Event3DRender;
/*      */ import shame.astra.api.events.implement.EventPacket;
/*      */ import shame.astra.api.utils.color.ColorUtils;
/*      */ import shame.astra.client.modules.Module;
/*      */ import shame.astra.client.modules.settings.Setting;
/*      */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*      */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*      */ import shame.astra.client.modules.settings.implement.ListSetting;
/*      */ 
/*      */ public class TotemAngel extends Module {
/*   36 */   public static TotemAngel INSTANCE = new TotemAngel();
/*      */   
/*   38 */   private final ModeSetting mode = new ModeSetting("Режим", "Angel", new String[] { "Angel" });
/*   39 */   private final BooleanSetting visuals = new BooleanSetting("Визуал", true);
/*   40 */   private final BooleanSetting chatInfo = new BooleanSetting("Чат инфо", true);
/*   41 */   private final FloatSetting riseHeight = new FloatSetting("Высота", 4.0F, 0.2F, 10.0F, 0.1F);
/*   42 */   private final FloatSetting duration = new FloatSetting("Длительность", 3.0F, 0.2F, 6.0F, 0.1F);
/*      */   
/*   44 */   private final ListSetting renderModes = new ListSetting("Режим", new BooleanSetting[] { new BooleanSetting("Ангел", true) });
/*      */   
/*      */   private static final float WING_SCALE = 1.0F;
/*      */   
/*      */   private static final float FLAP_SPEED = 1.6F;
/*      */   private static final float FLAP_AMPLITUDE = 25.0F;
/*      */   private static final float GLOW_INTENSITY = 0.1F;
/*      */   private static final float HALO_SIZE = 0.4F;
/*   52 */   private static final class_2960 SPARKLE_TEXTURE = class_2960.method_60655("astra", "textures/particle/sparkle.png");
/*      */   
/*      */   private static final int GREEN_COLOR = -13238485;
/*      */   private static final int YELLOW_COLOR = -3797;
/*   56 */   private final List<TotemGhost> ghosts = new CopyOnWriteArrayList<>();
/*   57 */   private final List<TotemSphereEffect> sphereEffects = new CopyOnWriteArrayList<>();
/*   58 */   private final Map<Integer, Long> recentSphereSpawns = new ConcurrentHashMap<>();
/*      */   
/*      */   public TotemAngel() {
/*   61 */     super("TotemPop", "Отображает эффект и пишет в чат при срабатывании тотема", Module.ModuleCategory.RENDER);
/*   62 */     addSettings(new Setting[] { (Setting)this.renderModes, (Setting)this.mode.visible(() -> Boolean.valueOf(false)), (Setting)this.visuals.visible(() -> Boolean.valueOf(false)), (Setting)this.chatInfo, (Setting)this.riseHeight, (Setting)this.duration });
/*      */   }
/*      */ 
/*      */   
/*      */   public void onDisable() {
/*   67 */     this.ghosts.clear();
/*   68 */     super.onDisable();
/*      */   }
/*      */   
/*      */   private class_2960 getGlowTexture() {
/*   72 */     return class_2960.method_60655("astra", "textures/targetesp/bloom.png");
/*      */   }
/*      */   
/*      */   private class_2960 getSkinTexture() {
/*   76 */     return class_2960.method_60655("astra", "textures/skin/skin.png");
/*      */   }
/*      */   
/*      */   @EventLink
/*      */   public void onPacket(EventPacket event) {
/*   81 */     if (mc.field_1687 == null || mc.field_1724 == null || event.getType() != EventPacket.Type.RECEIVE)
/*      */       return; 
/*   83 */     class_2596 class_2596 = event.getPacket(); if (class_2596 instanceof class_2663) { class_2663 packet = (class_2663)class_2596; if (packet.method_11470() == 35)
/*   84 */         mc.execute(() -> handleTotemPopPacket(packet));  }
/*      */   
/*      */   }
/*      */   private void handleTotemPopPacket(class_2663 packet) {
/*      */     class_742 player;
/*   89 */     if (mc.field_1687 == null || mc.field_1724 == null) {
/*      */       return;
/*      */     }
/*      */     
/*   93 */     class_1297 entity = packet.method_11469((class_1937)mc.field_1687);
/*   94 */     if (entity instanceof class_742) { player = (class_742)entity; }
/*      */     else
/*      */     { return; }
/*      */     
/*   98 */     if (this.renderModes.is("Ангел")) {
/*   99 */       addGhost(player);
/*      */     }
/*      */     
/*  102 */     if (this.chatInfo.isState() && player != mc.field_1724) {
/*  103 */       String name = player.method_5477().getString();
/*  104 */       ChatUtils.sendMessage(name + " §7снёс тотем!");
/*      */     } 
/*      */   }
/*      */   
/*      */   @EventLink
/*      */   public void onRender3D(Event3DRender event) {
/*  110 */     if (mc.field_1687 == null || mc.field_1724 == null)
/*      */       return; 
/*  112 */     if (this.renderModes.is("Ангел") && !this.ghosts.isEmpty()) {
/*  113 */       renderGhosts(event.getMatrices(), event.getTickDelta());
/*      */     }
/*      */   }
/*      */   
/*      */   private void addGhost(class_742 player) {
/*  118 */     float partialTicks = mc.method_61966().method_60637(true);
/*      */     
/*  120 */     double x = class_3532.method_16436(partialTicks, player.field_6038, player.method_23317());
/*  121 */     double y = class_3532.method_16436(partialTicks, player.field_5971, player.method_23318());
/*  122 */     double z = class_3532.method_16436(partialTicks, player.field_5989, player.method_23321());
/*      */     
/*  124 */     float bodyYaw = class_3532.method_16439(partialTicks, player.field_6220, player.field_6283);
/*  125 */     float headYaw = class_3532.method_16439(partialTicks, player.field_6259, player.field_6241);
/*  126 */     float headPitch = class_3532.method_16439(partialTicks, player.field_6004, player.method_36455());
/*  127 */     float limbSwing = player.field_42108.method_48572(partialTicks);
/*  128 */     float limbSwingAmount = player.field_42108.method_48570(partialTicks);
/*  129 */     boolean sneaking = player.method_5715();
/*  130 */     float height = player.method_17682();
/*      */     
/*  132 */     this.ghosts.add(new TotemGhost(new class_243(x, y, z), bodyYaw, headYaw - bodyYaw, headPitch, limbSwing, limbSwingAmount, sneaking, height, 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  141 */           System.currentTimeMillis()));
/*      */   }
/*      */ 
/*      */   
/*      */   private void addSphereEffect(class_742 player) {
/*  146 */     if (player == null || player == mc.field_1724) {
/*      */       return;
/*      */     }
/*      */     
/*  150 */     long now = System.currentTimeMillis();
/*  151 */     this.recentSphereSpawns.entrySet().removeIf(entry -> (now - ((Long)entry.getValue()).longValue() > 1000L));
/*      */     
/*  153 */     Long lastSpawn = this.recentSphereSpawns.get(Integer.valueOf(player.method_5628()));
/*  154 */     if (lastSpawn != null && now - lastSpawn.longValue() < 120L) {
/*      */       return;
/*      */     }
/*  157 */     this.recentSphereSpawns.put(Integer.valueOf(player.method_5628()), Long.valueOf(now));
/*      */     
/*  159 */     double centerY = player.method_23318() + player.method_17682() * 0.62D;
/*  160 */     List<SphereParticle> particles = new ArrayList<>(64);
/*  161 */     ThreadLocalRandom random = ThreadLocalRandom.current();
/*      */     
/*  163 */     for (int i = 0; i < 64; i++) {
/*  164 */       double yaw = random.nextDouble(0.0D, 6.283185307179586D);
/*  165 */       double pitch = random.nextDouble(-0.8D, 0.8D);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  170 */       class_243 direction = (new class_243(Math.cos(yaw) * Math.cos(pitch), Math.sin(pitch) * 0.62D + random.nextDouble(-0.12D, 0.24D), Math.sin(yaw) * Math.cos(pitch))).method_1029();
/*      */       
/*  172 */       particles.add(new SphereParticle(direction, random
/*      */             
/*  174 */             .nextFloat(0.28F, 1.08F), random
/*  175 */             .nextFloat(0.85F, 1.55F), random
/*  176 */             .nextFloat(1.05F, 1.85F), random
/*  177 */             .nextFloat(0.95F, 1.45F), random
/*  178 */             .nextFloat(0.0F, 1.0F), 
/*  179 */             random.nextBoolean() ? -13238485 : -3797));
/*      */     } 
/*      */ 
/*      */     
/*  183 */     this.sphereEffects.add(new TotemSphereEffect(new class_243(player
/*  184 */             .method_23317(), centerY, player.method_23321()), now, random
/*      */           
/*  186 */           .nextFloat(0.0F, 360.0F), particles, 
/*      */           
/*  188 */           createSphereOrbitLines()));
/*      */   }
/*      */ 
/*      */   
/*      */   private void renderGhosts(class_4587 matrices, float tickDelta) {
/*  193 */     class_243 cameraPos = mc.field_1773.method_19418().method_19326();
/*  194 */     long now = System.currentTimeMillis();
/*  195 */     List<TotemGhost> toRemove = new ArrayList<>();
/*      */     
/*  197 */     int themeColor = ColorUtils.getThemeColor();
/*  198 */     float r = ColorUtils.redf(themeColor);
/*  199 */     float g = ColorUtils.greenf(themeColor);
/*  200 */     float b = ColorUtils.bluef(themeColor);
/*      */     
/*  202 */     for (TotemGhost ghost : this.ghosts) {
/*  203 */       float progress = (float)(now - ghost.startTime) / this.duration.get() * 1000.0F;
/*      */       
/*  205 */       if (progress >= 1.0F) {
/*  206 */         toRemove.add(ghost);
/*      */         
/*      */         continue;
/*      */       } 
/*  210 */       double motionY = (this.riseHeight.get() * easeOutCubic(progress));
/*  211 */       float alpha = (1.0F - easeInCubic(progress)) * 0.85F;
/*      */       
/*  213 */       double renderX = ghost.position.field_1352 - cameraPos.field_1352;
/*  214 */       double renderY = ghost.position.field_1351 - cameraPos.field_1351 + motionY;
/*  215 */       double renderZ = ghost.position.field_1350 - cameraPos.field_1350;
/*      */       
/*  217 */       matrices.method_22903();
/*  218 */       matrices.method_22904(renderX, renderY, renderZ);
/*  219 */       matrices.method_22907(class_7833.field_40716.rotationDegrees(-ghost.bodyYaw));
/*      */       
/*  221 */       renderGlowingPlayerModel(matrices, r, g, b, alpha, ghost);
/*  222 */       renderWings(matrices, ghost, progress, tickDelta, themeColor, alpha);
/*  223 */       renderHalo(matrices, ghost, themeColor, alpha);
/*      */       
/*  225 */       matrices.method_22909();
/*      */     } 
/*      */     
/*  228 */     if (!toRemove.isEmpty()) {
/*  229 */       this.ghosts.removeAll(toRemove);
/*      */     }
/*      */   }
/*      */   
/*      */   private void renderSphereEffects(class_4587 matrices, class_243 cameraPos) {
/*  234 */     long now = System.currentTimeMillis();
/*  235 */     float sphereDurationMs = this.duration.get() * 1000.0F;
/*      */     
/*  237 */     this.sphereEffects.removeIf(effect -> ((float)(now - effect.startTime) >= sphereDurationMs));
/*  238 */     if (this.sphereEffects.isEmpty()) {
/*      */       return;
/*      */     }
/*      */     
/*  242 */     RenderSystem.enableBlend();
/*  243 */     RenderSystem.disableCull();
/*  244 */     RenderSystem.disableDepthTest();
/*  245 */     RenderSystem.depthMask(false);
/*      */     
/*  247 */     renderSphereParticles(matrices, cameraPos, now, sphereDurationMs);
/*  248 */     renderSphereArcs(matrices, cameraPos, now, sphereDurationMs);
/*      */     
/*  250 */     RenderSystem.enableDepthTest();
/*  251 */     RenderSystem.depthMask(true);
/*  252 */     RenderSystem.enableCull();
/*  253 */     RenderSystem.defaultBlendFunc();
/*  254 */     RenderSystem.disableBlend();
/*      */   }
/*      */   
/*      */   private void renderSphereParticles(class_4587 matrices, class_243 cameraPos, long now, float durationMs) {
/*  258 */     RenderSystem.blendFunc(770, 1);
/*  259 */     RenderSystem.setShaderTexture(0, SPARKLE_TEXTURE);
/*  260 */     RenderSystem.setShader(class_10142.field_53880);
/*      */     
/*  262 */     float cameraYaw = mc.field_1773.method_19418().method_19330();
/*  263 */     float cameraPitch = mc.field_1773.method_19418().method_19329();
/*  264 */     float baseRadius = 1.18F;
/*  265 */     float baseSize = 0.28F;
/*      */     
/*  267 */     for (TotemSphereEffect effect : this.sphereEffects) {
/*  268 */       float age = (float)(now - effect.startTime) / durationMs;
/*  269 */       float appear = class_3532.method_15363(1.0F - age, 0.0F, 1.0F);
/*  270 */       float burstProgress = easeOutQuad(Math.min(1.0F, age * 1.12F));
/*      */       
/*  272 */       for (SphereParticle particle : effect.particles) {
/*  273 */         float localProgress = class_3532.method_15363(age * particle.timeScale + particle.progressOffset * 0.1F, 0.0F, 1.0F);
/*  274 */         float launchProgress = easeOutQuad(localProgress);
/*  275 */         float radial = (0.34F + launchProgress * (1.2F + particle.spread * 1.05F) + burstProgress * 0.32F) * baseRadius;
/*  276 */         float orbit = (float)now * 0.0012F * particle.rotationScale + particle.progressOffset * 5.4F;
/*  277 */         double swirlScale = ((1.0F - localProgress) * 0.18F);
/*  278 */         double swirlX = Math.cos(orbit) * swirlScale * particle.swirlAmount;
/*  279 */         double swirlY = Math.sin((orbit * 1.3F)) * swirlScale * 0.75D * particle.swirlAmount + (localProgress * 0.08F);
/*  280 */         double swirlZ = Math.sin(orbit) * swirlScale * particle.swirlAmount;
/*  281 */         double dragY = (localProgress * localProgress * 0.14F);
/*      */ 
/*      */ 
/*      */         
/*  285 */         class_243 worldPos = effect.origin.method_1019(particle.direction.method_1021(radial)).method_1031(swirlX, swirlY - dragY, swirlZ);
/*      */         
/*  287 */         double x = worldPos.field_1352 - cameraPos.field_1352;
/*  288 */         double y = worldPos.field_1351 - cameraPos.field_1351;
/*  289 */         double z = worldPos.field_1350 - cameraPos.field_1350;
/*      */         
/*  291 */         int color = setAlpha(particle.color, (int)(255.0F * appear * (0.5F + 0.5F * (1.0F - localProgress))));
/*  292 */         float drawSize = baseSize * (0.68F + particle.spread * 0.34F) * (0.7F + appear * 0.52F);
/*      */         
/*  294 */         matrices.method_22903();
/*  295 */         matrices.method_22904(x, y, z);
/*  296 */         matrices.method_22907(class_7833.field_40716.rotationDegrees(-cameraYaw));
/*  297 */         matrices.method_22907(class_7833.field_40714.rotationDegrees(cameraPitch));
/*  298 */         drawSphereBillboard(matrices.method_23760().method_23761(), drawSize, color);
/*  299 */         matrices.method_22909();
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void renderSphereArcs(class_4587 matrices, class_243 cameraPos, long now, float durationMs) {
/*  305 */     RenderSystem.defaultBlendFunc();
/*  306 */     RenderSystem.setShader(class_10142.field_53876);
/*  307 */     RenderSystem.lineWidth(1.05F);
/*  308 */     GL11.glEnable(2848);
/*  309 */     GL11.glHint(3154, 4354);
/*      */     
/*  311 */     for (TotemSphereEffect effect : this.sphereEffects) {
/*  312 */       float age = (float)(now - effect.startTime) / durationMs;
/*  313 */       float appear = class_3532.method_15363(1.0F - age, 0.0F, 1.0F);
/*  314 */       float grow = easeOutQuad(Math.min(1.0F, age * 1.25F));
/*  315 */       float elapsedSec = (float)(now - effect.startTime) / 1000.0F;
/*  316 */       float scale = 1.18F * (0.78F + grow * 0.1F);
/*      */       
/*  318 */       double x = effect.origin.field_1352 - cameraPos.field_1352;
/*  319 */       double y = effect.origin.field_1351 - cameraPos.field_1351;
/*  320 */       double z = effect.origin.field_1350 - cameraPos.field_1350;
/*      */       
/*  322 */       for (OrbitLine line : effect.orbitLines) {
/*  323 */         matrices.method_22903();
/*  324 */         matrices.method_22904(x, y, z);
/*  325 */         matrices.method_22907(class_7833.field_40716.rotationDegrees(effect.baseRotation + line.baseYaw + elapsedSec * line.speedDeg));
/*  326 */         matrices.method_22907(class_7833.field_40714.rotationDegrees(line.tiltX));
/*  327 */         matrices.method_22907(class_7833.field_40718.rotationDegrees(line.tiltZ));
/*  328 */         drawSphereOrbitArc(matrices, line.radiusX * scale, line.radiusZ * scale, line.yOffset, line.startDeg, line.arcDeg, appear * line.alphaMul, line.startColor, line.endColor);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  339 */         matrices.method_22909();
/*      */       } 
/*      */     } 
/*      */     
/*  343 */     GL11.glDisable(2848);
/*      */   }
/*      */ 
/*      */   
/*      */   private void drawSphereOrbitArc(class_4587 matrices, float radiusX, float radiusZ, float y, float startDeg, float arcDeg, float alphaMul, int startColor, int endColor) {
/*  348 */     Matrix4f matrix = matrices.method_23760().method_23761();
/*  349 */     int segments = 28;
/*  350 */     float from = (float)Math.toRadians(startDeg);
/*  351 */     float to = (float)Math.toRadians((startDeg + arcDeg));
/*      */     
/*  353 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_29345, class_290.field_1576);
/*  354 */     for (int i = 0; i <= segments; i++) {
/*  355 */       float progress = i / segments;
/*  356 */       float angle = class_3532.method_16439(progress, from, to);
/*  357 */       float px = class_3532.method_15362(angle) * radiusX;
/*  358 */       float pz = class_3532.method_15374(angle) * radiusZ;
/*  359 */       float localY = y + class_3532.method_15374(angle * 1.35F) * 0.01F;
/*  360 */       float edgeFade = class_3532.method_15363(1.0F - Math.abs(progress - 0.5F) * 2.0F, 0.0F, 1.0F);
/*  361 */       int color = fadeLerp(startColor, endColor, progress, alphaMul * (0.22F + 0.78F * edgeFade));
/*  362 */       buffer.method_22918(matrix, px, localY, pz).method_39415(color);
/*      */     } 
/*  364 */     class_286.method_43433(buffer.method_60800());
/*      */     
/*  366 */     class_287 echo = class_289.method_1348().method_60827(class_293.class_5596.field_29345, class_290.field_1576);
/*  367 */     for (int j = 0; j <= segments; j++) {
/*  368 */       float progress = j / segments;
/*  369 */       float angle = class_3532.method_16439(progress, from + 0.015F, to - 0.012F);
/*  370 */       float px = class_3532.method_15362(angle) * (radiusX + 0.012F);
/*  371 */       float pz = class_3532.method_15374(angle) * (radiusZ + 0.012F);
/*  372 */       float localY = y + 0.004F + class_3532.method_15374(angle * 1.35F + 0.9F) * 0.008F;
/*  373 */       float edgeFade = class_3532.method_15363(1.0F - Math.abs(progress - 0.5F) * 2.0F, 0.0F, 1.0F);
/*  374 */       int color = fadeLerp(startColor, endColor, progress, alphaMul * 0.14F * edgeFade);
/*  375 */       echo.method_22918(matrix, px, localY, pz).method_39415(color);
/*      */     } 
/*  377 */     class_286.method_43433(echo.method_60800());
/*      */   }
/*      */   
/*      */   private List<OrbitLine> createSphereOrbitLines() {
/*  381 */     List<OrbitLine> lines = new ArrayList<>(5);
/*  382 */     lines.add(new OrbitLine(1.02F, 0.66F, 0.2F, 196.0F, 156.0F, 14.0F, -12.0F, 54.0F, 0.46F, -13238485, -13238485));
/*  383 */     lines.add(new OrbitLine(0.92F, 0.6F, 0.16F, 188.0F, 148.0F, 14.0F, -12.0F, 54.0F, 0.22F, -13238485, -13238485));
/*  384 */     lines.add(new OrbitLine(0.86F, 0.54F, -0.12F, 122.0F, 112.0F, 78.0F, 4.0F, -68.0F, 0.65F, -3797, -3797));
/*  385 */     lines.add(new OrbitLine(0.74F, 0.46F, -0.02F, 314.0F, 88.0F, 62.0F, -18.0F, 76.0F, 0.58F, -13238485, -3797));
/*  386 */     lines.add(new OrbitLine(0.68F, 0.34F, 0.0F, 202.0F, 44.0F, 8.0F, 52.0F, -44.0F, 0.18F, -13238485, -13238485));
/*  387 */     return lines;
/*      */   }
/*      */   
/*      */   private void drawSphereBillboard(Matrix4f matrix, float size, int color) {
/*  391 */     float half = size * 0.5F;
/*  392 */     int r = color >> 16 & 0xFF;
/*  393 */     int g = color >> 8 & 0xFF;
/*  394 */     int b = color & 0xFF;
/*  395 */     int a = color >> 24 & 0xFF;
/*      */     
/*  397 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
/*  398 */     buffer.method_22918(matrix, -half, -half, 0.0F).method_22913(0.0F, 1.0F).method_1336(r, g, b, a);
/*  399 */     buffer.method_22918(matrix, -half, half, 0.0F).method_22913(0.0F, 0.0F).method_1336(r, g, b, a);
/*  400 */     buffer.method_22918(matrix, half, half, 0.0F).method_22913(1.0F, 0.0F).method_1336(r, g, b, a);
/*  401 */     buffer.method_22918(matrix, half, -half, 0.0F).method_22913(1.0F, 1.0F).method_1336(r, g, b, a);
/*  402 */     class_286.method_43433(buffer.method_60800());
/*      */   }
/*      */   
/*      */   private int fadeLerp(int start, int end, float progress, float alphaMul) {
/*  406 */     int sr = start >> 16 & 0xFF;
/*  407 */     int sg = start >> 8 & 0xFF;
/*  408 */     int sb = start & 0xFF;
/*      */     
/*  410 */     int er = end >> 16 & 0xFF;
/*  411 */     int eg = end >> 8 & 0xFF;
/*  412 */     int eb = end & 0xFF;
/*      */     
/*  414 */     int r = class_3532.method_48781(progress, sr, er);
/*  415 */     int g = class_3532.method_48781(progress, sg, eg);
/*  416 */     int b = class_3532.method_48781(progress, sb, eb);
/*  417 */     int a = class_3532.method_15340((int)(255.0F * alphaMul), 0, 255);
/*  418 */     return a << 24 | r << 16 | g << 8 | b;
/*      */   }
/*      */   
/*      */   private int setAlpha(int color, int alpha) {
/*  422 */     return class_3532.method_15340(alpha, 0, 255) << 24 | color & 0xFFFFFF;
/*      */   }
/*      */   
/*      */   private float easeOutQuad(float value) {
/*  426 */     float inv = 1.0F - value;
/*  427 */     return 1.0F - inv * inv;
/*      */   }
/*      */   
/*      */   private void renderSkinPlayerModel(class_4587 matrices, float alpha, TotemGhost ghost) {
/*  431 */     RenderSystem.enableBlend();
/*  432 */     RenderSystem.blendFunc(770, 771);
/*  433 */     RenderSystem.enableDepthTest();
/*  434 */     RenderSystem.depthMask(true);
/*  435 */     RenderSystem.setShaderTexture(0, getSkinTexture());
/*  436 */     RenderSystem.setShader(class_10142.field_53880);
/*      */     
/*  438 */     float unit = 0.0625F;
/*  439 */     float sneakOffset = ghost.sneaking ? 0.25F : 0.0F;
/*      */     
/*  441 */     float limbSwing = ghost.limbSwing;
/*  442 */     float limbSwingAmount = Math.min(1.0F, ghost.limbSwingAmount);
/*  443 */     float legSwing = class_3532.method_15362(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
/*  444 */     float armSwing = class_3532.method_15362(limbSwing * 0.6662F + 3.1415927F) * 2.0F * limbSwingAmount;
/*      */     
/*  446 */     int alphaInt = (int)(alpha * 255.0F);
/*      */     
/*  448 */     matrices.method_22903();
/*  449 */     matrices.method_46416(0.0F, 24.0F * unit - sneakOffset, 0.0F);
/*  450 */     matrices.method_22907(class_7833.field_40716.rotationDegrees(ghost.netHeadYaw));
/*  451 */     matrices.method_22907(class_7833.field_40714.rotationDegrees(ghost.headPitch));
/*  452 */     renderSkinBox(matrices, -4.0F * unit, -8.0F * unit, -4.0F * unit, 8.0F * unit, 8.0F * unit, 8.0F * unit, 8, 8, 16, 16, 64, 64, alphaInt);
/*      */     
/*  454 */     matrices.method_22909();
/*      */     
/*  456 */     matrices.method_22903();
/*  457 */     if (ghost.sneaking) {
/*  458 */       matrices.method_46416(0.0F, 12.0F * unit, 0.0F);
/*  459 */       matrices.method_22907(class_7833.field_40714.rotationDegrees(28.0F));
/*  460 */       matrices.method_46416(0.0F, -12.0F * unit, 0.0F);
/*      */     } 
/*  462 */     renderSkinBox(matrices, -4.0F * unit, 12.0F * unit - sneakOffset, -2.0F * unit, 8.0F * unit, 12.0F * unit, 4.0F * unit, 20, 20, 28, 32, 64, 64, alphaInt);
/*      */     
/*  464 */     matrices.method_22909();
/*      */     
/*  466 */     matrices.method_22903();
/*  467 */     matrices.method_46416(-5.0F * unit, 22.0F * unit - sneakOffset, 0.0F);
/*  468 */     matrices.method_22907(class_7833.field_40714.rotationDegrees(armSwing * 57.295776F));
/*  469 */     renderSkinBox(matrices, -2.0F * unit, -12.0F * unit, -2.0F * unit, 4.0F * unit, 12.0F * unit, 4.0F * unit, 44, 20, 48, 32, 64, 64, alphaInt);
/*      */     
/*  471 */     matrices.method_22909();
/*      */     
/*  473 */     matrices.method_22903();
/*  474 */     matrices.method_46416(5.0F * unit, 22.0F * unit - sneakOffset, 0.0F);
/*  475 */     matrices.method_22907(class_7833.field_40714.rotationDegrees(-armSwing * 57.295776F));
/*  476 */     renderSkinBox(matrices, -2.0F * unit, -12.0F * unit, -2.0F * unit, 4.0F * unit, 12.0F * unit, 4.0F * unit, 36, 52, 40, 64, 64, 64, alphaInt);
/*      */     
/*  478 */     matrices.method_22909();
/*      */     
/*  480 */     matrices.method_22903();
/*  481 */     matrices.method_46416(-2.0F * unit, 12.0F * unit - sneakOffset, 0.0F);
/*  482 */     matrices.method_22907(class_7833.field_40714.rotationDegrees(legSwing * 57.295776F));
/*  483 */     renderSkinBox(matrices, -2.0F * unit, -12.0F * unit, -2.0F * unit, 4.0F * unit, 12.0F * unit, 4.0F * unit, 4, 20, 8, 32, 64, 64, alphaInt);
/*      */     
/*  485 */     matrices.method_22909();
/*      */     
/*  487 */     matrices.method_22903();
/*  488 */     matrices.method_46416(2.0F * unit, 12.0F * unit - sneakOffset, 0.0F);
/*  489 */     matrices.method_22907(class_7833.field_40714.rotationDegrees(-legSwing * 57.295776F));
/*  490 */     renderSkinBox(matrices, -2.0F * unit, -12.0F * unit, -2.0F * unit, 4.0F * unit, 12.0F * unit, 4.0F * unit, 20, 52, 24, 64, 64, 64, alphaInt);
/*      */     
/*  492 */     matrices.method_22909();
/*      */     
/*  494 */     RenderSystem.defaultBlendFunc();
/*  495 */     RenderSystem.disableBlend();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void renderSkinBox(class_4587 matrices, float x, float y, float z, float width, float height, float depth, int u, int v, int u2, int v2, int texWidth, int texHeight, int alpha) {
/*  502 */     Matrix4f matrix = matrices.method_23760().method_23761();
/*      */     
/*  504 */     float x1 = x;
/*  505 */     float y1 = y;
/*  506 */     float z1 = z;
/*  507 */     float x2 = x + width;
/*  508 */     float y2 = y + height;
/*  509 */     float z2 = z + depth;
/*      */     
/*  511 */     float w = width * 16.0F;
/*  512 */     float h = height * 16.0F;
/*  513 */     float d = depth * 16.0F;
/*      */     
/*  515 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
/*      */     
/*  517 */     float uMin = u / texWidth;
/*  518 */     float vMin = v / texHeight;
/*  519 */     float uMax = u2 / texWidth;
/*  520 */     float vMax = v2 / texHeight;
/*      */     
/*  522 */     float frontU1 = (u + d) / texWidth;
/*  523 */     float frontU2 = (u + d + w) / texWidth;
/*  524 */     float frontV1 = (v + d) / texHeight;
/*  525 */     float frontV2 = (v + d + h) / texHeight;
/*      */     
/*  527 */     buffer.method_22918(matrix, x1, y1, z2).method_22913(frontU1, frontV2).method_1336(255, 255, 255, alpha);
/*  528 */     buffer.method_22918(matrix, x2, y1, z2).method_22913(frontU2, frontV2).method_1336(255, 255, 255, alpha);
/*  529 */     buffer.method_22918(matrix, x2, y2, z2).method_22913(frontU2, frontV1).method_1336(255, 255, 255, alpha);
/*  530 */     buffer.method_22918(matrix, x1, y2, z2).method_22913(frontU1, frontV1).method_1336(255, 255, 255, alpha);
/*      */     
/*  532 */     float backU1 = (u + d + w + d) / texWidth;
/*  533 */     float backU2 = (u + d + w + d + w) / texWidth;
/*  534 */     float backV1 = (v + d) / texHeight;
/*  535 */     float backV2 = (v + d + h) / texHeight;
/*      */     
/*  537 */     buffer.method_22918(matrix, x2, y1, z1).method_22913(backU1, backV2).method_1336(255, 255, 255, alpha);
/*  538 */     buffer.method_22918(matrix, x1, y1, z1).method_22913(backU2, backV2).method_1336(255, 255, 255, alpha);
/*  539 */     buffer.method_22918(matrix, x1, y2, z1).method_22913(backU2, backV1).method_1336(255, 255, 255, alpha);
/*  540 */     buffer.method_22918(matrix, x2, y2, z1).method_22913(backU1, backV1).method_1336(255, 255, 255, alpha);
/*      */     
/*  542 */     float topU1 = (u + d) / texWidth;
/*  543 */     float topU2 = (u + d + w) / texWidth;
/*  544 */     float topV1 = v / texHeight;
/*  545 */     float topV2 = (v + d) / texHeight;
/*      */     
/*  547 */     buffer.method_22918(matrix, x1, y2, z1).method_22913(topU1, topV1).method_1336(255, 255, 255, alpha);
/*  548 */     buffer.method_22918(matrix, x1, y2, z2).method_22913(topU1, topV2).method_1336(255, 255, 255, alpha);
/*  549 */     buffer.method_22918(matrix, x2, y2, z2).method_22913(topU2, topV2).method_1336(255, 255, 255, alpha);
/*  550 */     buffer.method_22918(matrix, x2, y2, z1).method_22913(topU2, topV1).method_1336(255, 255, 255, alpha);
/*      */     
/*  552 */     float bottomU1 = (u + d + w) / texWidth;
/*  553 */     float bottomU2 = (u + d + w + w) / texWidth;
/*  554 */     float bottomV1 = v / texHeight;
/*  555 */     float bottomV2 = (v + d) / texHeight;
/*      */     
/*  557 */     buffer.method_22918(matrix, x1, y1, z2).method_22913(bottomU1, bottomV1).method_1336(255, 255, 255, alpha);
/*  558 */     buffer.method_22918(matrix, x1, y1, z1).method_22913(bottomU1, bottomV2).method_1336(255, 255, 255, alpha);
/*  559 */     buffer.method_22918(matrix, x2, y1, z1).method_22913(bottomU2, bottomV2).method_1336(255, 255, 255, alpha);
/*  560 */     buffer.method_22918(matrix, x2, y1, z2).method_22913(bottomU2, bottomV1).method_1336(255, 255, 255, alpha);
/*      */     
/*  562 */     float rightU1 = u / texWidth;
/*  563 */     float rightU2 = (u + d) / texWidth;
/*  564 */     float rightV1 = (v + d) / texHeight;
/*  565 */     float rightV2 = (v + d + h) / texHeight;
/*      */     
/*  567 */     buffer.method_22918(matrix, x1, y1, z1).method_22913(rightU1, rightV2).method_1336(255, 255, 255, alpha);
/*  568 */     buffer.method_22918(matrix, x1, y1, z2).method_22913(rightU2, rightV2).method_1336(255, 255, 255, alpha);
/*  569 */     buffer.method_22918(matrix, x1, y2, z2).method_22913(rightU2, rightV1).method_1336(255, 255, 255, alpha);
/*  570 */     buffer.method_22918(matrix, x1, y2, z1).method_22913(rightU1, rightV1).method_1336(255, 255, 255, alpha);
/*      */     
/*  572 */     float leftU1 = (u + d + w) / texWidth;
/*  573 */     float leftU2 = (u + d + w + d) / texWidth;
/*  574 */     float leftV1 = (v + d) / texHeight;
/*  575 */     float leftV2 = (v + d + h) / texHeight;
/*      */     
/*  577 */     buffer.method_22918(matrix, x2, y1, z2).method_22913(leftU1, leftV2).method_1336(255, 255, 255, alpha);
/*  578 */     buffer.method_22918(matrix, x2, y1, z1).method_22913(leftU2, leftV2).method_1336(255, 255, 255, alpha);
/*  579 */     buffer.method_22918(matrix, x2, y2, z1).method_22913(leftU2, leftV1).method_1336(255, 255, 255, alpha);
/*  580 */     buffer.method_22918(matrix, x2, y2, z2).method_22913(leftU1, leftV1).method_1336(255, 255, 255, alpha);
/*      */     
/*  582 */     class_286.method_43433(buffer.method_60800());
/*      */   }
/*      */   
/*      */   private void renderWings(class_4587 matrices, TotemGhost ghost, float progress, float tickDelta, int themeColor, float alpha) {
/*  586 */     float anim = (float)System.currentTimeMillis() / 50.0F * 0.22F * 1.6F + progress * 2.0F;
/*  587 */     float sin = class_3532.method_15374(anim);
/*  588 */     float cos = class_3532.method_15362(anim);
/*      */     
/*  590 */     float spreadAngle = 18.0F + progress * 15.0F;
/*  591 */     float pitchAngle = 13.0F + cos * 4.0F;
/*  592 */     float rollAngle = sin * 25.0F;
/*      */     
/*  594 */     if (ghost.sneaking) {
/*  595 */       spreadAngle -= 3.0F;
/*  596 */       pitchAngle += 8.0F;
/*      */     } 
/*      */     
/*  599 */     int topColor = ColorUtils.setAlphaColor(themeColor, (int)(132.0F * alpha));
/*  600 */     int bottomColor = ColorUtils.setAlphaColor(ColorUtils.darken(themeColor, 0.85F), (int)(102.0F * alpha));
/*  601 */     int edgeColor = ColorUtils.setAlphaColor(ColorUtils.darken(themeColor, 0.7F), (int)(190.0F * alpha));
/*  602 */     int boneColorA = ColorUtils.setAlphaColor(ColorUtils.darken(themeColor, 0.52F), (int)(175.0F * alpha));
/*  603 */     int boneColorB = ColorUtils.setAlphaColor(ColorUtils.darken(themeColor, 0.58F), (int)(150.0F * alpha));
/*      */     
/*  605 */     RenderSystem.enableBlend();
/*  606 */     RenderSystem.disableCull();
/*  607 */     RenderSystem.disableDepthTest();
/*  608 */     RenderSystem.depthMask(true);
/*  609 */     RenderSystem.blendFunc(770, 771);
/*  610 */     RenderSystem.setShader(class_10142.field_53876);
/*      */     
/*  612 */     matrices.method_22903();
/*      */     
/*  614 */     float sneakOffset = ghost.sneaking ? 0.25F : 0.0F;
/*  615 */     matrices.method_46416(0.0F, 1.3F - sneakOffset, -0.08F);
/*      */     
/*  617 */     if (ghost.sneaking) {
/*  618 */       matrices.method_22907(class_7833.field_40714.rotationDegrees(24.0F));
/*      */     }
/*      */     
/*  621 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1576);
/*  622 */     renderButterflyWing(buffer, matrices, 1.0F, spreadAngle, pitchAngle, rollAngle, 1.0F, topColor, bottomColor, edgeColor, boneColorA, boneColorB);
/*  623 */     renderButterflyWing(buffer, matrices, -1.0F, spreadAngle, pitchAngle, rollAngle, 1.0F, topColor, bottomColor, edgeColor, boneColorA, boneColorB);
/*  624 */     class_286.method_43433(buffer.method_60800());
/*      */     
/*  626 */     RenderSystem.disableDepthTest();
/*  627 */     RenderSystem.depthMask(false);
/*  628 */     RenderSystem.blendFunc(770, 1);
/*  629 */     RenderSystem.setShader(class_10142.field_53876);
/*      */     
/*  631 */     int glowA = ColorUtils.setAlphaColor(themeColor, (int)(72.0F * alpha));
/*  632 */     int glowB = ColorUtils.setAlphaColor(ColorUtils.darken(themeColor, 0.82F), (int)(66.0F * alpha));
/*  633 */     class_287 glowBuffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1576);
/*  634 */     renderButterflyGlow(glowBuffer, matrices, 1.0F, spreadAngle, pitchAngle, rollAngle, 1.0F, glowA, glowB);
/*  635 */     renderButterflyGlow(glowBuffer, matrices, -1.0F, spreadAngle, pitchAngle, rollAngle, 1.0F, glowA, glowB);
/*  636 */     class_286.method_43433(glowBuffer.method_60800());
/*      */     
/*  638 */     matrices.method_22909();
/*      */     
/*  640 */     RenderSystem.enableCull();
/*  641 */     RenderSystem.enableDepthTest();
/*  642 */     RenderSystem.defaultBlendFunc();
/*  643 */     RenderSystem.disableBlend();
/*  644 */     RenderSystem.depthMask(true);
/*      */   }
/*      */ 
/*      */   
/*      */   private void renderButterflyWing(class_287 buffer, class_4587 matrices, float side, float spread, float pitch, float roll, float scale, int topColor, int bottomColor, int edgeColor, int boneColorA, int boneColorB) {
/*  649 */     float root = 0.12F * scale;
/*  650 */     float topW = 1.5F * scale;
/*  651 */     float topH = 0.61F * scale;
/*  652 */     float lowW = 1.1F * scale;
/*  653 */     float lowH = 0.35F * scale;
/*      */     
/*  655 */     matrices.method_22903();
/*  656 */     matrices.method_46416(0.15F * side, 0.0F, -0.17F);
/*  657 */     matrices.method_22907(class_7833.field_40716.rotationDegrees(side * spread));
/*  658 */     matrices.method_22907(class_7833.field_40714.rotationDegrees(pitch));
/*  659 */     matrices.method_22907(class_7833.field_40718.rotationDegrees(side * roll));
/*      */     
/*  661 */     Matrix4f matrix = matrices.method_23760().method_23761();
/*      */     
/*  663 */     addDoubleSidedGradientQuad(buffer, matrix, side * root, 0.02F, 0.0F, side * (root + topW * 0.18F), topH * 0.95F, -0.06F, side * (root + topW), topH * 0.3F, -0.13F, side * (root + topW * 0.2F), 0.06F, -0.03F, topColor, bottomColor);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  671 */     addDoubleSidedGradientQuad(buffer, matrix, side * root, -0.01F, -0.02F, side * (root + lowW * 0.18F), -lowH * 0.94F, -0.1F, side * (root + lowW), -lowH * 0.36F, -0.17F, side * (root + lowW * 0.6F), -0.1F, -0.07F, bottomColor, topColor);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  679 */     addDoubleSidedQuad(buffer, matrix, side * root, 0.012F, 0.01F, side * root, -0.032F, -0.01F, side * (root + topW * 0.56F), -0.008F, -0.08F, side * (root + topW * 0.56F), 0.008F, -0.04F, edgeColor >> 16 & 0xFF, edgeColor >> 8 & 0xFF, edgeColor & 0xFF, edgeColor >> 24 & 0xFF);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  690 */     renderWingBoneLine(buffer, matrix, side * root, 0.0F, -0.02F, side * (root + topW * 0.22F), topH * 0.82F, -0.07F, side * (root + topW), topH * 0.3F, -0.13F, 0.016F * scale, boneColorB, boneColorB);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  696 */     renderWingBoneLine(buffer, matrix, side * root, 0.012F, -0.008F, side * (root + topW * 0.36F), topH * 0.56F, -0.065F, side * (root + topW * 0.86F), topH * 0.26F, -0.115F, 0.012F * scale, boneColorA, boneColorB);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  702 */     renderWingBoneLine(buffer, matrix, side * root, -0.02F, -0.04F, side * (root + lowW * 0.22F), -lowH * 0.84F, -0.11F, side * (root + lowW), -lowH * 0.34F, -0.18F, 0.009F * scale, boneColorB, boneColorB);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  708 */     renderWingBoneLine(buffer, matrix, side * root, -0.004F, -0.018F, side * (root + lowW * 0.34F), -lowH * 0.52F, -0.085F, side * (root + lowW * 0.88F), -lowH * 0.3F, -0.145F, 0.01F * scale, boneColorB, boneColorA);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  715 */     matrices.method_22909();
/*      */   }
/*      */ 
/*      */   
/*      */   private void renderButterflyGlow(class_287 buffer, class_4587 matrices, float side, float spread, float pitch, float roll, float scale, int glowA, int glowB) {
/*  720 */     float root = 0.12F * scale;
/*  721 */     float topW = 1.5F * scale;
/*  722 */     float topH = 0.61F * scale;
/*  723 */     float lowW = 1.1F * scale;
/*  724 */     float lowH = 0.35F * scale;
/*      */     
/*  726 */     matrices.method_22903();
/*  727 */     matrices.method_46416(0.15F * side, 0.0F, -0.17F);
/*  728 */     matrices.method_22907(class_7833.field_40716.rotationDegrees(side * spread));
/*  729 */     matrices.method_22907(class_7833.field_40714.rotationDegrees(pitch));
/*  730 */     matrices.method_22907(class_7833.field_40718.rotationDegrees(side * roll));
/*      */     
/*  732 */     Matrix4f matrix = matrices.method_23760().method_23761();
/*  733 */     renderWingBoneLine(buffer, matrix, side * root, 0.0F, -0.02F, side * (root + topW * 0.2F), topH * 0.86F, -0.08F, side * (root + topW), topH * 0.3F, -0.16F, 0.02F * scale, glowA, glowB);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  739 */     renderWingBoneLine(buffer, matrix, side * root, -0.02F, -0.05F, side * (root + lowW * 0.2F), -lowH * 0.86F, -0.13F, side * (root + lowW), -lowH * 0.32F, -0.2F, 0.018F * scale, glowB, glowA);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  745 */     renderWingBoneLine(buffer, matrix, side * root, 0.012F, -0.008F, side * (root + topW * 0.36F), topH * 0.56F, -0.07F, side * (root + topW * 0.84F), topH * 0.25F, -0.125F, 0.016F * scale, glowA, glowB);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  751 */     matrices.method_22909();
/*      */   }
/*      */   
/*      */   private void renderHalo(class_4587 matrices, TotemGhost ghost, int themeColor, float alpha) {
/*  755 */     float sneakOffset = ghost.sneaking ? 0.25F : 0.0F;
/*  756 */     float rotation = (float)System.currentTimeMillis() / 30.0F % 360.0F;
/*      */     
/*  758 */     matrices.method_22903();
/*  759 */     matrices.method_46416(0.0F, 1.9F - sneakOffset, 0.0F);
/*  760 */     matrices.method_22907(class_7833.field_40714.rotationDegrees(15.0F));
/*  761 */     matrices.method_22907(class_7833.field_40716.rotationDegrees(rotation));
/*      */     
/*  763 */     RenderSystem.enableBlend();
/*  764 */     RenderSystem.disableCull();
/*  765 */     RenderSystem.disableDepthTest();
/*  766 */     RenderSystem.blendFunc(770, 1);
/*  767 */     RenderSystem.setShader(class_10142.field_53876);
/*      */     
/*  769 */     int haloColor = ColorUtils.setAlphaColor(themeColor, (int)(200.0F * alpha));
/*  770 */     int haloGlow = ColorUtils.setAlphaColor(themeColor, (int)(100.0F * alpha));
/*      */     
/*  772 */     Matrix4f matrix = matrices.method_23760().method_23761();
/*      */     
/*  774 */     renderHaloRing(matrix, 0.4F, 0.03F, haloColor);
/*  775 */     renderHaloRing(matrix, 0.42000002F, 0.05F, haloGlow);
/*  776 */     renderHaloRing(matrix, 0.38F, 0.02F, haloGlow);
/*      */     
/*  778 */     RenderSystem.enableCull();
/*  779 */     RenderSystem.enableDepthTest();
/*  780 */     RenderSystem.defaultBlendFunc();
/*  781 */     RenderSystem.disableBlend();
/*      */     
/*  783 */     matrices.method_22909();
/*      */   }
/*      */   
/*      */   private void renderHaloRing(Matrix4f matrix, float radius, float thickness, int color) {
/*  787 */     int segments = 36;
/*  788 */     float angleStep = (float)(6.283185307179586D / segments);
/*      */     
/*  790 */     int r = color >> 16 & 0xFF;
/*  791 */     int g = color >> 8 & 0xFF;
/*  792 */     int b = color & 0xFF;
/*  793 */     int a = color >> 24 & 0xFF;
/*      */     
/*  795 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1576);
/*      */     
/*  797 */     for (int i = 0; i < segments; i++) {
/*  798 */       float angle1 = i * angleStep;
/*  799 */       float angle2 = (i + 1) * angleStep;
/*      */       
/*  801 */       float x1Inner = class_3532.method_15362(angle1) * (radius - thickness / 2.0F);
/*  802 */       float z1Inner = class_3532.method_15374(angle1) * (radius - thickness / 2.0F);
/*  803 */       float x1Outer = class_3532.method_15362(angle1) * (radius + thickness / 2.0F);
/*  804 */       float z1Outer = class_3532.method_15374(angle1) * (radius + thickness / 2.0F);
/*      */       
/*  806 */       float x2Inner = class_3532.method_15362(angle2) * (radius - thickness / 2.0F);
/*  807 */       float z2Inner = class_3532.method_15374(angle2) * (radius - thickness / 2.0F);
/*  808 */       float x2Outer = class_3532.method_15362(angle2) * (radius + thickness / 2.0F);
/*  809 */       float z2Outer = class_3532.method_15374(angle2) * (radius + thickness / 2.0F);
/*      */       
/*  811 */       buffer.method_22918(matrix, x1Inner, 0.01F, z1Inner).method_1336(r, g, b, a);
/*  812 */       buffer.method_22918(matrix, x1Outer, 0.01F, z1Outer).method_1336(r, g, b, a);
/*  813 */       buffer.method_22918(matrix, x2Outer, 0.01F, z2Outer).method_1336(r, g, b, a);
/*  814 */       buffer.method_22918(matrix, x2Inner, 0.01F, z2Inner).method_1336(r, g, b, a);
/*      */       
/*  816 */       buffer.method_22918(matrix, x1Inner, -0.01F, z1Inner).method_1336(r, g, b, a);
/*  817 */       buffer.method_22918(matrix, x2Inner, -0.01F, z2Inner).method_1336(r, g, b, a);
/*  818 */       buffer.method_22918(matrix, x2Outer, -0.01F, z2Outer).method_1336(r, g, b, a);
/*  819 */       buffer.method_22918(matrix, x1Outer, -0.01F, z1Outer).method_1336(r, g, b, a);
/*      */       
/*  821 */       buffer.method_22918(matrix, x1Outer, -0.01F, z1Outer).method_1336(r, g, b, a);
/*  822 */       buffer.method_22918(matrix, x2Outer, -0.01F, z2Outer).method_1336(r, g, b, a);
/*  823 */       buffer.method_22918(matrix, x2Outer, 0.01F, z2Outer).method_1336(r, g, b, a);
/*  824 */       buffer.method_22918(matrix, x1Outer, 0.01F, z1Outer).method_1336(r, g, b, a);
/*      */       
/*  826 */       buffer.method_22918(matrix, x1Inner, 0.01F, z1Inner).method_1336(r, g, b, a);
/*  827 */       buffer.method_22918(matrix, x2Inner, 0.01F, z2Inner).method_1336(r, g, b, a);
/*  828 */       buffer.method_22918(matrix, x2Inner, -0.01F, z2Inner).method_1336(r, g, b, a);
/*  829 */       buffer.method_22918(matrix, x1Inner, -0.01F, z1Inner).method_1336(r, g, b, a);
/*      */     } 
/*      */     
/*  832 */     class_286.method_43433(buffer.method_60800());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addDoubleSidedQuad(class_287 buffer, Matrix4f matrix, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, int r, int g, int b, int a) {
/*  841 */     addQuad(buffer, matrix, x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, r, g, b, a);
/*  842 */     addQuad(buffer, matrix, x4, y4, z4, x3, y3, z3, x2, y2, z2, x1, y1, z1, r, g, b, a);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addDoubleSidedGradientQuad(class_287 buffer, Matrix4f matrix, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, int nearColor, int farColor) {
/*  851 */     int nr = nearColor >> 16 & 0xFF;
/*  852 */     int ng = nearColor >> 8 & 0xFF;
/*  853 */     int nb = nearColor & 0xFF;
/*  854 */     int na = nearColor >> 24 & 0xFF;
/*  855 */     int fr = farColor >> 16 & 0xFF;
/*  856 */     int fg = farColor >> 8 & 0xFF;
/*  857 */     int fb = farColor & 0xFF;
/*  858 */     int fa = farColor >> 24 & 0xFF;
/*      */     
/*  860 */     buffer.method_22918(matrix, x1, y1, z1).method_1336(nr, ng, nb, na);
/*  861 */     buffer.method_22918(matrix, x2, y2, z2).method_1336(fr, fg, fb, fa);
/*  862 */     buffer.method_22918(matrix, x3, y3, z3).method_1336(fr, fg, fb, fa);
/*  863 */     buffer.method_22918(matrix, x4, y4, z4).method_1336(nr, ng, nb, na);
/*      */     
/*  865 */     buffer.method_22918(matrix, x4, y4, z4).method_1336(nr, ng, nb, na);
/*  866 */     buffer.method_22918(matrix, x3, y3, z3).method_1336(fr, fg, fb, fa);
/*  867 */     buffer.method_22918(matrix, x2, y2, z2).method_1336(fr, fg, fb, fa);
/*  868 */     buffer.method_22918(matrix, x1, y1, z1).method_1336(nr, ng, nb, na);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void renderWingBoneLine(class_287 buffer, Matrix4f matrix, float x0, float y0, float z0, float x1, float y1, float z1, float x2, float y2, float z2, float thickness, int colorA, int colorB) {
/*  876 */     float vx1 = x1 - x0;
/*  877 */     float vy1 = y1 - y0;
/*  878 */     float len1 = Math.max(1.0E-4F, (float)Math.sqrt((vx1 * vx1 + vy1 * vy1)));
/*  879 */     float nx1 = -vy1 / len1 * thickness;
/*  880 */     float ny1 = vx1 / len1 * thickness;
/*      */     
/*  882 */     int aR = colorA >> 16 & 0xFF;
/*  883 */     int aG = colorA >> 8 & 0xFF;
/*  884 */     int aB = colorA & 0xFF;
/*  885 */     int aA = colorA >> 24 & 0xFF;
/*  886 */     int bR = colorB >> 16 & 0xFF;
/*  887 */     int bG = colorB >> 8 & 0xFF;
/*  888 */     int bB = colorB & 0xFF;
/*  889 */     int bA = colorB >> 24 & 0xFF;
/*      */     
/*  891 */     addDoubleSidedQuad(buffer, matrix, x0 + nx1, y0 + ny1, z0, x0 - nx1, y0 - ny1, z0, x1 - nx1, y1 - ny1, z1, x1 + nx1, y1 + ny1, z1, aR, aG, aB, aA);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  899 */     float vx2 = x2 - x1;
/*  900 */     float vy2 = y2 - y1;
/*  901 */     float len2 = Math.max(1.0E-4F, (float)Math.sqrt((vx2 * vx2 + vy2 * vy2)));
/*  902 */     float nx2 = -vy2 / len2 * thickness;
/*  903 */     float ny2 = vx2 / len2 * thickness;
/*      */     
/*  905 */     addDoubleSidedQuad(buffer, matrix, x1 + nx2, y1 + ny2, z1, x1 - nx2, y1 - ny2, z1, x2 - nx2, y2 - ny2, z2, x2 + nx2, y2 + ny2, z2, bR, bG, bB, bA);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addQuad(class_287 buffer, Matrix4f matrix, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, int r, int g, int b, int a) {
/*  920 */     buffer.method_22918(matrix, x1, y1, z1).method_1336(r, g, b, a);
/*  921 */     buffer.method_22918(matrix, x2, y2, z2).method_1336(r, g, b, a);
/*  922 */     buffer.method_22918(matrix, x3, y3, z3).method_1336(r, g, b, a);
/*  923 */     buffer.method_22918(matrix, x4, y4, z4).method_1336(r, g, b, a);
/*      */   }
/*      */   
/*      */   private void renderGlowingPlayerModel(class_4587 matrices, float r, float g, float b, float alpha, TotemGhost ghost) {
/*  927 */     RenderSystem.enableBlend();
/*  928 */     RenderSystem.blendFunc(770, 1);
/*  929 */     RenderSystem.disableDepthTest();
/*  930 */     RenderSystem.depthMask(false);
/*  931 */     RenderSystem.disableCull();
/*  932 */     RenderSystem.setShaderTexture(0, getGlowTexture());
/*  933 */     RenderSystem.setShader(class_10142.field_53880);
/*      */     
/*  935 */     float unit = 0.0625F;
/*  936 */     float sneakOffset = ghost.sneaking ? 0.25F : 0.0F;
/*      */     
/*  938 */     float limbSwing = ghost.limbSwing;
/*  939 */     float limbSwingAmount = Math.min(1.0F, ghost.limbSwingAmount);
/*  940 */     float legSwing = class_3532.method_15362(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
/*  941 */     float armSwing = class_3532.method_15362(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
/*      */     
/*  943 */     matrices.method_22903();
/*  944 */     matrices.method_46416(0.0F, 24.0F * unit - sneakOffset, 0.0F);
/*  945 */     matrices.method_22907(class_7833.field_40716.rotationDegrees(ghost.netHeadYaw));
/*  946 */     matrices.method_22907(class_7833.field_40714.rotationDegrees(ghost.headPitch));
/*  947 */     renderGlowBox(matrices, -4.0F * unit, -8.0F * unit, -4.0F * unit, 8.0F * unit, 8.0F * unit, 8.0F * unit, r, g, b, alpha * 0.1F);
/*  948 */     matrices.method_22909();
/*      */     
/*  950 */     matrices.method_22903();
/*  951 */     if (ghost.sneaking) {
/*  952 */       matrices.method_46416(0.0F, 12.0F * unit, 0.0F);
/*  953 */       matrices.method_22907(class_7833.field_40714.rotationDegrees(25.0F));
/*  954 */       matrices.method_46416(0.0F, -12.0F * unit, 0.0F);
/*      */     } 
/*  956 */     renderGlowBox(matrices, -4.0F * unit, 12.0F * unit - sneakOffset, -2.0F * unit, 8.0F * unit, 12.0F * unit, 4.0F * unit, r, g, b, alpha * 0.1F);
/*  957 */     matrices.method_22909();
/*      */     
/*  959 */     float armWidth = 3.0F * unit;
/*      */     
/*  961 */     matrices.method_22903();
/*  962 */     matrices.method_46416(-4.0F * unit - armWidth / 2.0F, 22.0F * unit - sneakOffset, 0.0F);
/*  963 */     matrices.method_22907(class_7833.field_40714.rotationDegrees(armSwing * 57.295776F));
/*  964 */     renderGlowBox(matrices, -armWidth / 2.0F, -10.0F * unit, -2.0F * unit, armWidth, 12.0F * unit, 4.0F * unit, r, g, b, alpha * 0.1F);
/*  965 */     matrices.method_22909();
/*      */     
/*  967 */     matrices.method_22903();
/*  968 */     matrices.method_46416(4.0F * unit + armWidth / 2.0F, 22.0F * unit - sneakOffset, 0.0F);
/*  969 */     matrices.method_22907(class_7833.field_40714.rotationDegrees(-armSwing * 57.295776F));
/*  970 */     renderGlowBox(matrices, -armWidth / 2.0F, -10.0F * unit, -2.0F * unit, armWidth, 12.0F * unit, 4.0F * unit, r, g, b, alpha * 0.1F);
/*  971 */     matrices.method_22909();
/*      */     
/*  973 */     matrices.method_22903();
/*  974 */     matrices.method_46416(-2.0F * unit, 12.0F * unit, 0.0F);
/*  975 */     matrices.method_22907(class_7833.field_40714.rotationDegrees(legSwing * 57.295776F));
/*  976 */     renderGlowBox(matrices, -2.0F * unit, -12.0F * unit, -2.0F * unit, 4.0F * unit, 12.0F * unit, 4.0F * unit, r, g, b, alpha * 0.1F);
/*  977 */     matrices.method_22909();
/*      */     
/*  979 */     matrices.method_22903();
/*  980 */     matrices.method_46416(2.0F * unit, 12.0F * unit, 0.0F);
/*  981 */     matrices.method_22907(class_7833.field_40714.rotationDegrees(-legSwing * 57.295776F));
/*  982 */     renderGlowBox(matrices, -2.0F * unit, -12.0F * unit, -2.0F * unit, 4.0F * unit, 12.0F * unit, 4.0F * unit, r, g, b, alpha * 0.1F);
/*  983 */     matrices.method_22909();
/*      */     
/*  985 */     RenderSystem.enableCull();
/*  986 */     RenderSystem.enableDepthTest();
/*  987 */     RenderSystem.depthMask(true);
/*  988 */     RenderSystem.defaultBlendFunc();
/*  989 */     RenderSystem.disableBlend();
/*      */   }
/*      */   
/*      */   private void renderGlowBox(class_4587 matrices, float x, float y, float z, float width, float height, float depth, float r, float g, float b, float alpha) {
/*  993 */     float centerX = x + width / 2.0F;
/*  994 */     float centerY = y + height / 2.0F;
/*  995 */     float centerZ = z + depth / 2.0F;
/*      */     
/*  997 */     float glowSize = Math.max(width, Math.max(height, depth)) * 1.8F;
/*      */     
/*  999 */     renderGlowSprite(matrices, centerX, centerY, centerZ + depth / 2.0F + 0.01F, glowSize, width, height, r, g, b, alpha);
/* 1000 */     renderGlowSprite(matrices, centerX, centerY, centerZ - depth / 2.0F - 0.01F, glowSize, width, height, r, g, b, alpha);
/*      */     
/* 1002 */     matrices.method_22903();
/* 1003 */     matrices.method_46416(centerX, centerY, centerZ);
/* 1004 */     matrices.method_22907(class_7833.field_40716.rotationDegrees(90.0F));
/* 1005 */     renderGlowSpriteRotated(matrices, 0.0F, 0.0F, depth / 2.0F + 0.01F, glowSize, depth, height, r, g, b, alpha);
/* 1006 */     renderGlowSpriteRotated(matrices, 0.0F, 0.0F, -depth / 2.0F - 0.01F, glowSize, depth, height, r, g, b, alpha);
/* 1007 */     matrices.method_22909();
/*      */     
/* 1009 */     matrices.method_22903();
/* 1010 */     matrices.method_46416(centerX, centerY, centerZ);
/* 1011 */     matrices.method_22907(class_7833.field_40714.rotationDegrees(90.0F));
/* 1012 */     renderGlowSpriteRotated(matrices, 0.0F, 0.0F, height / 2.0F + 0.01F, glowSize, width, depth, r, g, b, alpha);
/* 1013 */     renderGlowSpriteRotated(matrices, 0.0F, 0.0F, -height / 2.0F - 0.01F, glowSize, width, depth, r, g, b, alpha);
/* 1014 */     matrices.method_22909();
/*      */     
/* 1016 */     float innerAlpha = alpha * 0.4F;
/* 1017 */     RenderSystem.setShader(class_10142.field_53876);
/* 1018 */     renderSolidBox(matrices, x, y, z, width, height, depth, r, g, b, innerAlpha);
/* 1019 */     RenderSystem.setShaderTexture(0, getGlowTexture());
/* 1020 */     RenderSystem.setShader(class_10142.field_53880);
/*      */   }
/*      */   
/*      */   private void renderGlowSprite(class_4587 matrices, float x, float y, float z, float glowSize, float boxWidth, float boxHeight, float r, float g, float b, float alpha) {
/* 1024 */     Matrix4f matrix = matrices.method_23760().method_23761();
/*      */     
/* 1026 */     int rInt = (int)(r * 255.0F);
/* 1027 */     int gInt = (int)(g * 255.0F);
/* 1028 */     int bInt = (int)(b * 255.0F);
/* 1029 */     int aInt = (int)(class_3532.method_15363(alpha, 0.0F, 1.0F) * 255.0F);
/*      */     
/* 1031 */     float halfW = glowSize / 2.0F;
/* 1032 */     float halfH = glowSize / 2.0F;
/*      */     
/* 1034 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
/* 1035 */     buffer.method_22918(matrix, x - halfW, y - halfH, z).method_22913(0.0F, 0.0F).method_1336(rInt, gInt, bInt, aInt);
/* 1036 */     buffer.method_22918(matrix, x - halfW, y + halfH, z).method_22913(0.0F, 1.0F).method_1336(rInt, gInt, bInt, aInt);
/* 1037 */     buffer.method_22918(matrix, x + halfW, y + halfH, z).method_22913(1.0F, 1.0F).method_1336(rInt, gInt, bInt, aInt);
/* 1038 */     buffer.method_22918(matrix, x + halfW, y - halfH, z).method_22913(1.0F, 0.0F).method_1336(rInt, gInt, bInt, aInt);
/* 1039 */     class_286.method_43433(buffer.method_60800());
/*      */   }
/*      */   
/*      */   private void renderGlowSpriteRotated(class_4587 matrices, float x, float y, float z, float glowSize, float boxWidth, float boxHeight, float r, float g, float b, float alpha) {
/* 1043 */     Matrix4f matrix = matrices.method_23760().method_23761();
/*      */     
/* 1045 */     int rInt = (int)(r * 255.0F);
/* 1046 */     int gInt = (int)(g * 255.0F);
/* 1047 */     int bInt = (int)(b * 255.0F);
/* 1048 */     int aInt = (int)(class_3532.method_15363(alpha, 0.0F, 1.0F) * 255.0F);
/*      */     
/* 1050 */     float halfW = glowSize / 2.0F;
/* 1051 */     float halfH = glowSize / 2.0F;
/*      */     
/* 1053 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
/* 1054 */     buffer.method_22918(matrix, x - halfW, y - halfH, z).method_22913(0.0F, 0.0F).method_1336(rInt, gInt, bInt, aInt);
/* 1055 */     buffer.method_22918(matrix, x - halfW, y + halfH, z).method_22913(0.0F, 1.0F).method_1336(rInt, gInt, bInt, aInt);
/* 1056 */     buffer.method_22918(matrix, x + halfW, y + halfH, z).method_22913(1.0F, 1.0F).method_1336(rInt, gInt, bInt, aInt);
/* 1057 */     buffer.method_22918(matrix, x + halfW, y - halfH, z).method_22913(1.0F, 0.0F).method_1336(rInt, gInt, bInt, aInt);
/* 1058 */     class_286.method_43433(buffer.method_60800());
/*      */   }
/*      */   
/*      */   private void renderSolidBox(class_4587 matrices, float x, float y, float z, float width, float height, float depth, float r, float g, float b, float alpha) {
/* 1062 */     Matrix4f matrix = matrices.method_23760().method_23761();
/*      */     
/* 1064 */     float x1 = x;
/* 1065 */     float y1 = y;
/* 1066 */     float z1 = z;
/* 1067 */     float x2 = x + width;
/* 1068 */     float y2 = y + height;
/* 1069 */     float z2 = z + depth;
/*      */     
/* 1071 */     int rInt = (int)(r * 255.0F);
/* 1072 */     int gInt = (int)(g * 255.0F);
/* 1073 */     int bInt = (int)(b * 255.0F);
/* 1074 */     int aInt = (int)(class_3532.method_15363(alpha, 0.0F, 1.0F) * 255.0F);
/*      */     
/* 1076 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1576);
/*      */     
/* 1078 */     buffer.method_22918(matrix, x1, y1, z2).method_1336(rInt, gInt, bInt, aInt);
/* 1079 */     buffer.method_22918(matrix, x2, y1, z2).method_1336(rInt, gInt, bInt, aInt);
/* 1080 */     buffer.method_22918(matrix, x2, y2, z2).method_1336(rInt, gInt, bInt, aInt);
/* 1081 */     buffer.method_22918(matrix, x1, y2, z2).method_1336(rInt, gInt, bInt, aInt);
/*      */     
/* 1083 */     buffer.method_22918(matrix, x2, y1, z1).method_1336(rInt, gInt, bInt, aInt);
/* 1084 */     buffer.method_22918(matrix, x1, y1, z1).method_1336(rInt, gInt, bInt, aInt);
/* 1085 */     buffer.method_22918(matrix, x1, y2, z1).method_1336(rInt, gInt, bInt, aInt);
/* 1086 */     buffer.method_22918(matrix, x2, y2, z1).method_1336(rInt, gInt, bInt, aInt);
/*      */     
/* 1088 */     buffer.method_22918(matrix, x1, y1, z1).method_1336(rInt, gInt, bInt, aInt);
/* 1089 */     buffer.method_22918(matrix, x1, y1, z2).method_1336(rInt, gInt, bInt, aInt);
/* 1090 */     buffer.method_22918(matrix, x1, y2, z2).method_1336(rInt, gInt, bInt, aInt);
/* 1091 */     buffer.method_22918(matrix, x1, y2, z1).method_1336(rInt, gInt, bInt, aInt);
/*      */     
/* 1093 */     buffer.method_22918(matrix, x2, y1, z2).method_1336(rInt, gInt, bInt, aInt);
/* 1094 */     buffer.method_22918(matrix, x2, y1, z1).method_1336(rInt, gInt, bInt, aInt);
/* 1095 */     buffer.method_22918(matrix, x2, y2, z1).method_1336(rInt, gInt, bInt, aInt);
/* 1096 */     buffer.method_22918(matrix, x2, y2, z2).method_1336(rInt, gInt, bInt, aInt);
/*      */     
/* 1098 */     buffer.method_22918(matrix, x1, y2, z1).method_1336(rInt, gInt, bInt, aInt);
/* 1099 */     buffer.method_22918(matrix, x1, y2, z2).method_1336(rInt, gInt, bInt, aInt);
/* 1100 */     buffer.method_22918(matrix, x2, y2, z2).method_1336(rInt, gInt, bInt, aInt);
/* 1101 */     buffer.method_22918(matrix, x2, y2, z1).method_1336(rInt, gInt, bInt, aInt);
/*      */     
/* 1103 */     buffer.method_22918(matrix, x1, y1, z2).method_1336(rInt, gInt, bInt, aInt);
/* 1104 */     buffer.method_22918(matrix, x1, y1, z1).method_1336(rInt, gInt, bInt, aInt);
/* 1105 */     buffer.method_22918(matrix, x2, y1, z1).method_1336(rInt, gInt, bInt, aInt);
/* 1106 */     buffer.method_22918(matrix, x2, y1, z2).method_1336(rInt, gInt, bInt, aInt);
/*      */     
/* 1108 */     class_286.method_43433(buffer.method_60800());
/*      */   }
/*      */   
/*      */   private float easeOutCubic(float t) {
/* 1112 */     return 1.0F - (float)Math.pow(1.0D - t, 3.0D);
/*      */   }
/*      */   
/*      */   private float easeInCubic(float t) {
/* 1116 */     return t * t * t;
/*      */   }
/*      */   
/*      */   private static class TotemSphereEffect {
/*      */     private final class_243 origin;
/*      */     private final long startTime;
/*      */     private final float baseRotation;
/*      */     private final List<TotemAngel.SphereParticle> particles;
/*      */     private final List<TotemAngel.OrbitLine> orbitLines;
/*      */     
/*      */     private TotemSphereEffect(class_243 origin, long startTime, float baseRotation, List<TotemAngel.SphereParticle> particles, List<TotemAngel.OrbitLine> orbitLines) {
/* 1127 */       this.origin = origin;
/* 1128 */       this.startTime = startTime;
/* 1129 */       this.baseRotation = baseRotation;
/* 1130 */       this.particles = particles;
/* 1131 */       this.orbitLines = orbitLines;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class OrbitLine
/*      */   {
/*      */     private final float radiusX;
/*      */     private final float radiusZ;
/*      */     private final float yOffset;
/*      */     private final float startDeg;
/*      */     private final float arcDeg;
/*      */     private final float tiltX;
/*      */     private final float tiltZ;
/*      */     private final float speedDeg;
/*      */     private final float alphaMul;
/*      */     private final int startColor;
/*      */     private final int endColor;
/*      */     private final float baseYaw;
/*      */     
/*      */     private OrbitLine(float radiusX, float radiusZ, float yOffset, float startDeg, float arcDeg, float tiltX, float tiltZ, float speedDeg, float alphaMul, int startColor, int endColor) {
/* 1151 */       this.radiusX = radiusX;
/* 1152 */       this.radiusZ = radiusZ;
/* 1153 */       this.yOffset = yOffset;
/* 1154 */       this.startDeg = startDeg;
/* 1155 */       this.arcDeg = arcDeg;
/* 1156 */       this.tiltX = tiltX;
/* 1157 */       this.tiltZ = tiltZ;
/* 1158 */       this.speedDeg = speedDeg;
/* 1159 */       this.alphaMul = alphaMul;
/* 1160 */       this.startColor = startColor;
/* 1161 */       this.endColor = endColor;
/* 1162 */       this.baseYaw = startDeg * 0.35F;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SphereParticle {
/*      */     private final class_243 direction;
/*      */     private final float spread;
/*      */     private final float swirlAmount;
/*      */     private final float rotationScale;
/*      */     private final float timeScale;
/*      */     private final float progressOffset;
/*      */     private final int color;
/*      */     
/*      */     private SphereParticle(class_243 direction, float spread, float swirlAmount, float rotationScale, float timeScale, float progressOffset, int color) {
/* 1176 */       this.direction = direction;
/* 1177 */       this.spread = spread;
/* 1178 */       this.swirlAmount = swirlAmount;
/* 1179 */       this.rotationScale = rotationScale;
/* 1180 */       this.timeScale = timeScale;
/* 1181 */       this.progressOffset = progressOffset;
/* 1182 */       this.color = color;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class TotemGhost
/*      */   {
/*      */     final class_243 position;
/*      */     final float bodyYaw;
/*      */     final float netHeadYaw;
/*      */     final float headPitch;
/*      */     final float limbSwing;
/*      */     final float limbSwingAmount;
/*      */     final boolean sneaking;
/*      */     final float height;
/*      */     final long startTime;
/*      */     
/*      */     TotemGhost(class_243 position, float bodyYaw, float netHeadYaw, float headPitch, float limbSwing, float limbSwingAmount, boolean sneaking, float height, long startTime) {
/* 1199 */       this.position = position;
/* 1200 */       this.bodyYaw = bodyYaw;
/* 1201 */       this.netHeadYaw = netHeadYaw;
/* 1202 */       this.headPitch = headPitch;
/* 1203 */       this.limbSwing = limbSwing;
/* 1204 */       this.limbSwingAmount = limbSwingAmount;
/* 1205 */       this.sneaking = sneaking;
/* 1206 */       this.height = height;
/* 1207 */       this.startTime = startTime;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\TotemAngel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */