/*     */ package shame.astra.client.modules.impl.render;
/*     */ import com.mojang.blaze3d.platform.GlStateManager;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Random;
/*     */ import net.minecraft.class_10142;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_286;
/*     */ import net.minecraft.class_287;
/*     */ import net.minecraft.class_289;
/*     */ import net.minecraft.class_290;
/*     */ import net.minecraft.class_293;
/*     */ import net.minecraft.class_2960;
/*     */ import net.minecraft.class_3532;
/*     */ import net.minecraft.class_4184;
/*     */ import net.minecraft.class_4587;
/*     */ import net.minecraft.class_7833;
/*     */ import org.joml.Matrix4f;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.Event3DRender;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*     */ import shame.astra.client.modules.settings.implement.ModeSetting;
/*     */ 
/*     */ public class Cubes extends Module {
/*  27 */   public static Cubes INSTANCE = new Cubes();
/*     */   
/*  29 */   private static final class_2960 GLOW_TEX = class_2960.method_60655("astra", "textures/particle/bloom.png");
/*     */   
/*     */   private static final float SPAWN_RADIUS = 12.0F;
/*     */   private static final float PARTICLE_SIZE = 0.18F;
/*     */   private static final float PARTICLE_SPEED = 0.25F;
/*     */   private static final float GLOW_INTENSITY = 1.7F;
/*     */   private static final float MAX_RENDER_DISTANCE_SQ = 900.0F;
/*  36 */   private static final byte[][] CUBE_EDGES = new byte[][] { { -1, -1, -1, 1, -1, -1 }, { 1, -1, -1, 1, -1, 1 }, { 1, -1, 1, -1, -1, 1 }, { -1, -1, 1, -1, -1, -1 }, { -1, 1, -1, 1, 1, -1 }, { 1, 1, -1, 1, 1, 1 }, { 1, 1, 1, -1, 1, 1 }, { -1, 1, 1, -1, 1, -1 }, { -1, -1, -1, -1, 1, -1 }, { 1, -1, -1, 1, 1, -1 }, { 1, -1, 1, 1, 1, 1 }, { -1, -1, 1, -1, 1, 1 } };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  41 */   private static final byte[][] TRIANGLE_EDGES = new byte[][] { { 0, 1 }, { 0, 2 }, { 0, 3 }, { 0, 4 }, { 1, 2 }, { 2, 3 }, { 3, 4 }, { 4, 1 } };
/*     */ 
/*     */ 
/*     */   
/*  45 */   private static final float[] GLOW_SCALES = new float[] { 10.0F, 6.0F, 3.5F };
/*  46 */   private static final float[] GLOW_ALPHA_SCALES = new float[] { 0.06F, 0.14F, 0.25F };
/*     */   
/*  48 */   private final ModeSetting animation = new ModeSetting("Анимация", "Разлет", new String[] { "Разлет", "Падение" });
/*  49 */   private final ModeSetting shape = new ModeSetting("Форма", "Кубы", new String[] { "Кубы", "Треугольники" });
/*  50 */   private final FloatSetting count = new FloatSetting("Количество", 30.0F, 5.0F, 100.0F, 1.0F);
/*  51 */   private final FloatSetting size = new FloatSetting("Размер", 1.0F, 0.1F, 3.0F, 0.1F);
/*  52 */   private final FloatSetting speed = new FloatSetting("Скорость", 1.0F, 0.1F, 5.0F, 0.1F);
/*     */   
/*  54 */   private final List<CubeParticle> cubes = new ArrayList<>();
/*  55 */   private final List<CubeParticle> visibleCubes = new ArrayList<>();
/*  56 */   private final Random random = new Random(); private boolean lastAttackPressed; private float cr;
/*     */   private float cg;
/*     */   private float cb;
/*  59 */   private int updateCounter = 0;
/*     */   
/*     */   public Cubes() {
/*  62 */     super("Cubes", "3D Кубы по миру", Module.ModuleCategory.RENDER);
/*  63 */     addSettings(new Setting[] { (Setting)this.animation, (Setting)this.shape, (Setting)this.count, (Setting)this.size, (Setting)this.speed });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  68 */     super.onEnable();
/*  69 */     this.cubes.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  74 */     super.onDisable();
/*  75 */     this.cubes.clear();
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onRender3D(Event3DRender event) {
/*  80 */     if (mc.field_1724 == null || mc.field_1687 == null)
/*     */       return; 
/*  82 */     boolean attackPressed = mc.field_1690.field_1886.method_1434();
/*  83 */     if (attackPressed && !this.lastAttackPressed) applyHitImpulseFromCrosshair(event.getCamera()); 
/*  84 */     this.lastAttackPressed = attackPressed;
/*     */     
/*  86 */     this.updateCounter++;
/*  87 */     if (this.updateCounter % 2 == 0) updateCubes();
/*     */     
/*  89 */     renderCubes(event);
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onAttack(EventAttackEntity event) {
/*  94 */     if (mc.field_1773 != null && mc.field_1773.method_19418() != null) {
/*  95 */       applyHitImpulseFromCrosshair(mc.field_1773.method_19418());
/*     */     }
/*     */   }
/*     */   
/*     */   private void applyHitImpulseFromCrosshair(class_4184 camera) {
/* 100 */     if (this.cubes.isEmpty() || camera == null)
/*     */       return; 
/* 102 */     class_243 origin = camera.method_19326();
/* 103 */     float yaw = (float)Math.toRadians(camera.method_19330());
/* 104 */     float pitch = (float)Math.toRadians(camera.method_19329());
/*     */     
/* 106 */     double dirX = (-class_3532.method_15374(yaw) * class_3532.method_15362(pitch));
/* 107 */     double dirY = -class_3532.method_15374(pitch);
/* 108 */     double dirZ = (class_3532.method_15362(yaw) * class_3532.method_15362(pitch));
/*     */     
/* 110 */     CubeParticle best = null;
/* 111 */     double bestT = Double.MAX_VALUE;
/*     */     
/* 113 */     for (int i = 0, sz = this.cubes.size(); i < sz; i++) {
/* 114 */       CubeParticle p = this.cubes.get(i);
/* 115 */       double opX = p.x - origin.field_1352;
/* 116 */       double opY = p.y - origin.field_1351;
/* 117 */       double opZ = p.z - origin.field_1350;
/* 118 */       double t = opX * dirX + opY * dirY + opZ * dirZ;
/*     */       
/* 120 */       if (t >= 0.0D && t <= 128.0D) {
/*     */         
/* 122 */         double closestX = origin.field_1352 + dirX * t;
/* 123 */         double closestY = origin.field_1351 + dirY * t;
/* 124 */         double closestZ = origin.field_1350 + dirZ * t;
/* 125 */         double dx = p.x - closestX;
/* 126 */         double dy = p.y - closestY;
/* 127 */         double dz = p.z - closestZ;
/* 128 */         double distSq = dx * dx + dy * dy + dz * dz;
/*     */         
/* 130 */         if (distSq <= 1.32D && t < bestT) {
/* 131 */           bestT = t;
/* 132 */           best = p;
/*     */         } 
/*     */       } 
/* 135 */     }  if (best != null) {
/* 136 */       double force = 0.08D * this.speed.get();
/* 137 */       best.vx = (float)(best.vx + dirX * force);
/* 138 */       best.vy = (float)(best.vy + dirY * force + 0.02D);
/* 139 */       best.vz = (float)(best.vz + dirZ * force);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateCubes() {
/* 144 */     int target = (int)this.count.get();
/* 145 */     int currentSize = this.cubes.size();
/*     */     
/* 147 */     if (currentSize < target) {
/* 148 */       int toAdd = Math.min(target - currentSize, 5);
/* 149 */       for (int j = 0; j < toAdd; ) { this.cubes.add(spawnCube()); j++; } 
/* 150 */     } else if (currentSize > target) {
/* 151 */       this.cubes.subList(target, currentSize).clear();
/*     */     } 
/*     */     
/* 154 */     float spd = 0.25F * this.speed.get();
/* 155 */     float maxR = 12.0F;
/* 156 */     boolean falling = this.animation.is("Падение");
/* 157 */     class_243 playerPos = mc.field_1724.method_19538();
/* 158 */     double maxRSq = (maxR * maxR) * 6.25D;
/*     */     
/* 160 */     for (int i = this.cubes.size() - 1; i >= 0; i--) {
/* 161 */       CubeParticle p = this.cubes.get(i);
/*     */       
/* 163 */       if (falling) {
/* 164 */         p.wobblePhase += 0.06F * spd;
/* 165 */         p.x += (p.vx * spd) + Math.sin((p.wobblePhase + p.wobbleOffset)) * 0.002400000113993883D * spd;
/* 166 */         p.y += (p.vy * spd);
/* 167 */         p.z += (p.vz * spd) + Math.cos((p.wobblePhase * 0.8F + p.wobbleOffset)) * 0.0020000000949949026D * spd;
/* 168 */         p.vy = Math.max(p.vy - 8.0E-5F * spd, -0.032F);
/* 169 */         p.rotX += p.rotSpeedX * 0.2F * spd;
/* 170 */         p.rotY += p.rotSpeedY * 0.2F * spd;
/* 171 */         p.rotZ += p.rotSpeedZ * 0.2F * spd;
/*     */       } else {
/* 173 */         p.x += (p.vx * spd);
/* 174 */         p.y += (p.vy * spd);
/* 175 */         p.z += (p.vz * spd);
/* 176 */         p.rotX += p.rotSpeedX * spd;
/* 177 */         p.rotY += p.rotSpeedY * spd;
/* 178 */         p.rotZ += p.rotSpeedZ * spd;
/* 179 */         p.vx *= 0.995F;
/* 180 */         p.vy *= 0.995F;
/* 181 */         p.vz *= 0.995F;
/*     */       } 
/*     */       
/* 184 */       p.life--;
/*     */       
/* 186 */       double dx = p.x - playerPos.field_1352;
/* 187 */       double dy = p.y - playerPos.field_1351;
/* 188 */       double dz = p.z - playerPos.field_1350;
/* 189 */       double distSq = dx * dx + dy * dy + dz * dz;
/*     */       
/* 191 */       if (p.life <= 0 || distSq > maxRSq || (falling && p.y < playerPos.field_1351 - 2.5D)) {
/* 192 */         this.cubes.remove(i);
/* 193 */         this.cubes.add(spawnCube());
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void renderCubes(Event3DRender e) {
/* 199 */     if (mc.field_1724 == null)
/*     */       return; 
/* 201 */     class_4587 ms = e.getMatrices();
/* 202 */     class_243 cam = e.getCamera().method_19326();
/* 203 */     class_4184 camera = e.getCamera();
/* 204 */     float s = 0.18F * this.size.get();
/* 205 */     float glow = 1.7F;
/*     */     
/* 207 */     int baseRGB = ColorUtils.getThemeColor();
/* 208 */     this.cr = (baseRGB >> 16 & 0xFF) / 255.0F;
/* 209 */     this.cg = (baseRGB >> 8 & 0xFF) / 255.0F;
/* 210 */     this.cb = (baseRGB & 0xFF) / 255.0F;
/*     */     
/* 212 */     this.visibleCubes.clear();
/* 213 */     float yaw = (float)Math.toRadians(camera.method_19330());
/* 214 */     float pitch = (float)Math.toRadians(camera.method_19329());
/* 215 */     double lookX = (-class_3532.method_15374(yaw) * class_3532.method_15362(pitch));
/* 216 */     double lookY = -class_3532.method_15374(pitch);
/* 217 */     double lookZ = (class_3532.method_15362(yaw) * class_3532.method_15362(pitch));
/*     */     
/* 219 */     for (int i = 0, sz = this.cubes.size(); i < sz; i++) {
/* 220 */       CubeParticle p = this.cubes.get(i);
/* 221 */       double dx = p.x - cam.field_1352;
/* 222 */       double dy = p.y - cam.field_1351;
/* 223 */       double dz = p.z - cam.field_1350;
/* 224 */       double distSq = dx * dx + dy * dy + dz * dz;
/*     */       
/* 226 */       if (distSq <= 900.0D && 
/* 227 */         dx * lookX + dy * lookY + dz * lookZ >= -1.0D) {
/* 228 */         p.renderAlpha = getAlpha(p);
/* 229 */         if (p.renderAlpha >= 0.01F)
/*     */         {
/* 231 */           this.visibleCubes.add(p); } 
/*     */       } 
/*     */     } 
/* 234 */     if (this.visibleCubes.isEmpty())
/*     */       return; 
/* 236 */     RenderSystem.enableBlend();
/* 237 */     RenderSystem.disableCull();
/* 238 */     RenderSystem.disableDepthTest();
/* 239 */     RenderSystem.depthMask(false);
/* 240 */     RenderSystem.blendFuncSeparate(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE, GlStateManager.class_4535.ZERO, GlStateManager.class_4534.ONE);
/* 241 */     RenderSystem.setShader(class_10142.field_53880);
/* 242 */     RenderSystem.setShaderTexture(0, GLOW_TEX);
/* 243 */     drawGlowBatch(ms, camera, cam, s, glow);
/*     */     
/* 245 */     RenderSystem.blendFunc(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA);
/* 246 */     RenderSystem.setShader(class_10142.field_53876);
/*     */     
/* 248 */     boolean isCubes = this.shape.is("Кубы");
/* 249 */     boolean isTriangles = this.shape.is("Треугольники");
/*     */     
/* 251 */     if (isCubes) {
/* 252 */       drawCubeFacesBatch(ms, cam, s);
/*     */     }
/*     */     
/* 255 */     if (isTriangles) {
/* 256 */       drawTriangleFacesBatch(ms, cam, s);
/*     */     }
/*     */     
/* 259 */     RenderSystem.blendFunc(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE);
/*     */     
/* 261 */     if (isCubes) {
/* 262 */       drawCubeDashedEdgesBatch(ms, cam, s);
/* 263 */     } else if (isTriangles) {
/* 264 */       drawTriangleDashedEdgesBatch(ms, cam, s);
/*     */     } 
/*     */     
/* 267 */     RenderSystem.depthMask(true);
/* 268 */     RenderSystem.enableCull();
/* 269 */     RenderSystem.defaultBlendFunc();
/* 270 */     RenderSystem.disableBlend();
/*     */   }
/*     */   
/*     */   private void drawGlowBatch(class_4587 ms, class_4184 camera, class_243 cam, float s, float glow) {
/* 274 */     class_287 builder = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
/*     */     
/* 276 */     for (int particleIndex = 0, sz = this.visibleCubes.size(); particleIndex < sz; particleIndex++) {
/* 277 */       CubeParticle p = this.visibleCubes.get(particleIndex);
/* 278 */       float alpha = p.renderAlpha;
/*     */       
/* 280 */       ms.method_22903();
/* 281 */       ms.method_22904(p.x - cam.field_1352, p.y - cam.field_1351, p.z - cam.field_1350);
/* 282 */       ms.method_22907(class_7833.field_40716.rotationDegrees(-camera.method_19330()));
/* 283 */       ms.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
/*     */       
/* 285 */       Matrix4f matrix = ms.method_23760().method_23761();
/* 286 */       for (int i = 0; i < 3; i++) {
/* 287 */         float scale = s * GLOW_SCALES[i] * glow;
/* 288 */         float a = alpha * GLOW_ALPHA_SCALES[i] * glow;
/* 289 */         float hs = scale * 0.5F;
/*     */         
/* 291 */         builder.method_22918(matrix, -hs, hs, 0.0F).method_22913(0.0F, 1.0F).method_22915(this.cr, this.cg, this.cb, a);
/* 292 */         builder.method_22918(matrix, hs, hs, 0.0F).method_22913(1.0F, 1.0F).method_22915(this.cr, this.cg, this.cb, a);
/* 293 */         builder.method_22918(matrix, hs, -hs, 0.0F).method_22913(1.0F, 0.0F).method_22915(this.cr, this.cg, this.cb, a);
/* 294 */         builder.method_22918(matrix, -hs, -hs, 0.0F).method_22913(0.0F, 0.0F).method_22915(this.cr, this.cg, this.cb, a);
/*     */       } 
/* 296 */       ms.method_22909();
/*     */     } 
/*     */     
/* 299 */     class_286.method_43433(builder.method_60800());
/*     */   }
/*     */   
/*     */   private float getAlpha(CubeParticle p) {
/* 303 */     float lifePct = class_3532.method_15363(p.life / p.maxLife, 0.0F, 1.0F);
/* 304 */     float fadeIn = Math.min(1.0F, (p.maxLife - p.life) / 20.0F);
/* 305 */     return lifePct * fadeIn;
/*     */   }
/*     */   
/*     */   private void drawCubeFacesBatch(class_4587 ms, class_243 cam, float s) {
/* 309 */     if (!hasFaceRenderableParticles())
/* 310 */       return;  class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1576);
/* 311 */     for (int i = 0, sz = this.visibleCubes.size(); i < sz; i++) {
/* 312 */       CubeParticle p = this.visibleCubes.get(i);
/* 313 */       float alpha = p.renderAlpha * 0.4F;
/* 314 */       if (alpha >= 0.01F) {
/*     */         
/* 316 */         ms.method_22903();
/* 317 */         ms.method_22904(p.x - cam.field_1352, p.y - cam.field_1351, p.z - cam.field_1350);
/* 318 */         ms.method_22907(class_7833.field_40714.rotationDegrees(p.rotX));
/* 319 */         ms.method_22907(class_7833.field_40716.rotationDegrees(p.rotY));
/* 320 */         ms.method_22907(class_7833.field_40718.rotationDegrees(p.rotZ));
/* 321 */         appendCubeFaces(buffer, ms.method_23760().method_23761(), s, alpha);
/* 322 */         ms.method_22909();
/*     */       } 
/* 324 */     }  class_286.method_43433(buffer.method_60800());
/*     */   }
/*     */   
/*     */   private void drawTriangleFacesBatch(class_4587 ms, class_243 cam, float s) {
/* 328 */     if (!hasFaceRenderableParticles())
/* 329 */       return;  class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27379, class_290.field_1576);
/* 330 */     for (int i = 0, sz = this.visibleCubes.size(); i < sz; i++) {
/* 331 */       CubeParticle p = this.visibleCubes.get(i);
/* 332 */       float alpha = p.renderAlpha * 0.4F;
/* 333 */       if (alpha >= 0.01F) {
/*     */         
/* 335 */         ms.method_22903();
/* 336 */         ms.method_22904(p.x - cam.field_1352, p.y - cam.field_1351, p.z - cam.field_1350);
/* 337 */         ms.method_22907(class_7833.field_40714.rotationDegrees(p.rotX));
/* 338 */         ms.method_22907(class_7833.field_40716.rotationDegrees(p.rotY));
/* 339 */         ms.method_22907(class_7833.field_40718.rotationDegrees(p.rotZ));
/* 340 */         appendTriangleFaces(buffer, ms.method_23760().method_23761(), s, alpha);
/* 341 */         ms.method_22909();
/*     */       } 
/* 343 */     }  class_286.method_43433(buffer.method_60800());
/*     */   }
/*     */   
/*     */   private boolean hasFaceRenderableParticles() {
/* 347 */     for (int i = 0, sz = this.visibleCubes.size(); i < sz; i++) {
/* 348 */       if (((CubeParticle)this.visibleCubes.get(i)).renderAlpha >= 0.025F) return true; 
/*     */     } 
/* 350 */     return false;
/*     */   }
/*     */   
/*     */   private void appendCubeFaces(class_287 buffer, Matrix4f m, float s, float a) {
/* 354 */     buffer.method_22918(m, -s, -s, s).method_22915(this.cr, this.cg, this.cb, a);
/* 355 */     buffer.method_22918(m, s, -s, s).method_22915(this.cr, this.cg, this.cb, a);
/* 356 */     buffer.method_22918(m, s, s, s).method_22915(this.cr, this.cg, this.cb, a);
/* 357 */     buffer.method_22918(m, -s, s, s).method_22915(this.cr, this.cg, this.cb, a);
/*     */     
/* 359 */     buffer.method_22918(m, s, -s, -s).method_22915(this.cr, this.cg, this.cb, a);
/* 360 */     buffer.method_22918(m, -s, -s, -s).method_22915(this.cr, this.cg, this.cb, a);
/* 361 */     buffer.method_22918(m, -s, s, -s).method_22915(this.cr, this.cg, this.cb, a);
/* 362 */     buffer.method_22918(m, s, s, -s).method_22915(this.cr, this.cg, this.cb, a);
/*     */     
/* 364 */     buffer.method_22918(m, -s, s, s).method_22915(this.cr, this.cg, this.cb, a);
/* 365 */     buffer.method_22918(m, s, s, s).method_22915(this.cr, this.cg, this.cb, a);
/* 366 */     buffer.method_22918(m, s, s, -s).method_22915(this.cr, this.cg, this.cb, a);
/* 367 */     buffer.method_22918(m, -s, s, -s).method_22915(this.cr, this.cg, this.cb, a);
/*     */     
/* 369 */     buffer.method_22918(m, -s, -s, -s).method_22915(this.cr, this.cg, this.cb, a);
/* 370 */     buffer.method_22918(m, s, -s, -s).method_22915(this.cr, this.cg, this.cb, a);
/* 371 */     buffer.method_22918(m, s, -s, s).method_22915(this.cr, this.cg, this.cb, a);
/* 372 */     buffer.method_22918(m, -s, -s, s).method_22915(this.cr, this.cg, this.cb, a);
/*     */     
/* 374 */     buffer.method_22918(m, s, -s, s).method_22915(this.cr, this.cg, this.cb, a);
/* 375 */     buffer.method_22918(m, s, -s, -s).method_22915(this.cr, this.cg, this.cb, a);
/* 376 */     buffer.method_22918(m, s, s, -s).method_22915(this.cr, this.cg, this.cb, a);
/* 377 */     buffer.method_22918(m, s, s, s).method_22915(this.cr, this.cg, this.cb, a);
/*     */     
/* 379 */     buffer.method_22918(m, -s, -s, -s).method_22915(this.cr, this.cg, this.cb, a);
/* 380 */     buffer.method_22918(m, -s, -s, s).method_22915(this.cr, this.cg, this.cb, a);
/* 381 */     buffer.method_22918(m, -s, s, s).method_22915(this.cr, this.cg, this.cb, a);
/* 382 */     buffer.method_22918(m, -s, s, -s).method_22915(this.cr, this.cg, this.cb, a);
/*     */   }
/*     */   
/*     */   private void appendTriangleFaces(class_287 buffer, Matrix4f m, float s, float a) {
/* 386 */     float top = s;
/* 387 */     float bottom = -s;
/* 388 */     float halfBase = s * 0.866F;
/*     */     
/* 390 */     buffer.method_22918(m, 0.0F, top, 0.0F).method_22915(this.cr, this.cg, this.cb, a);
/* 391 */     buffer.method_22918(m, -halfBase, bottom, halfBase).method_22915(this.cr, this.cg, this.cb, a);
/* 392 */     buffer.method_22918(m, halfBase, bottom, halfBase).method_22915(this.cr, this.cg, this.cb, a);
/*     */     
/* 394 */     buffer.method_22918(m, 0.0F, top, 0.0F).method_22915(this.cr, this.cg, this.cb, a);
/* 395 */     buffer.method_22918(m, halfBase, bottom, halfBase).method_22915(this.cr, this.cg, this.cb, a);
/* 396 */     buffer.method_22918(m, halfBase, bottom, -halfBase).method_22915(this.cr, this.cg, this.cb, a);
/*     */     
/* 398 */     buffer.method_22918(m, 0.0F, top, 0.0F).method_22915(this.cr, this.cg, this.cb, a);
/* 399 */     buffer.method_22918(m, halfBase, bottom, -halfBase).method_22915(this.cr, this.cg, this.cb, a);
/* 400 */     buffer.method_22918(m, -halfBase, bottom, -halfBase).method_22915(this.cr, this.cg, this.cb, a);
/*     */     
/* 402 */     buffer.method_22918(m, 0.0F, top, 0.0F).method_22915(this.cr, this.cg, this.cb, a);
/* 403 */     buffer.method_22918(m, -halfBase, bottom, -halfBase).method_22915(this.cr, this.cg, this.cb, a);
/* 404 */     buffer.method_22918(m, -halfBase, bottom, halfBase).method_22915(this.cr, this.cg, this.cb, a);
/*     */     
/* 406 */     buffer.method_22918(m, -halfBase, bottom, halfBase).method_22915(this.cr, this.cg, this.cb, a);
/* 407 */     buffer.method_22918(m, halfBase, bottom, halfBase).method_22915(this.cr, this.cg, this.cb, a);
/* 408 */     buffer.method_22918(m, halfBase, bottom, -halfBase).method_22915(this.cr, this.cg, this.cb, a);
/*     */     
/* 410 */     buffer.method_22918(m, -halfBase, bottom, halfBase).method_22915(this.cr, this.cg, this.cb, a);
/* 411 */     buffer.method_22918(m, halfBase, bottom, -halfBase).method_22915(this.cr, this.cg, this.cb, a);
/* 412 */     buffer.method_22918(m, -halfBase, bottom, -halfBase).method_22915(this.cr, this.cg, this.cb, a);
/*     */   }
/*     */   
/*     */   private void drawCubeDashedEdgesBatch(class_4587 ms, class_243 cam, float s) {
/* 416 */     class_287 buf = class_289.method_1348().method_60827(class_293.class_5596.field_29344, class_290.field_1576);
/* 417 */     int lineCount = 0;
/* 418 */     for (int i = 0, sz = this.visibleCubes.size(); i < sz; i++) {
/* 419 */       CubeParticle p = this.visibleCubes.get(i);
/* 420 */       float alpha = p.renderAlpha;
/*     */       
/* 422 */       ms.method_22903();
/* 423 */       ms.method_22904(p.x - cam.field_1352, p.y - cam.field_1351, p.z - cam.field_1350);
/* 424 */       ms.method_22907(class_7833.field_40714.rotationDegrees(p.rotX));
/* 425 */       ms.method_22907(class_7833.field_40716.rotationDegrees(p.rotY));
/* 426 */       ms.method_22907(class_7833.field_40718.rotationDegrees(p.rotZ));
/* 427 */       lineCount += appendCubeDashedEdges(buf, ms.method_23760().method_23761(), s, alpha);
/* 428 */       ms.method_22909();
/*     */     } 
/*     */     
/* 431 */     if (lineCount > 0) {
/* 432 */       class_286.method_43433(buf.method_60800());
/*     */     }
/*     */   }
/*     */   
/*     */   private void drawTriangleDashedEdgesBatch(class_4587 ms, class_243 cam, float s) {
/* 437 */     class_287 buf = class_289.method_1348().method_60827(class_293.class_5596.field_29344, class_290.field_1576);
/* 438 */     int lineCount = 0;
/* 439 */     for (int i = 0, sz = this.visibleCubes.size(); i < sz; i++) {
/* 440 */       CubeParticle p = this.visibleCubes.get(i);
/* 441 */       float alpha = p.renderAlpha;
/*     */       
/* 443 */       ms.method_22903();
/* 444 */       ms.method_22904(p.x - cam.field_1352, p.y - cam.field_1351, p.z - cam.field_1350);
/* 445 */       ms.method_22907(class_7833.field_40714.rotationDegrees(p.rotX));
/* 446 */       ms.method_22907(class_7833.field_40716.rotationDegrees(p.rotY));
/* 447 */       ms.method_22907(class_7833.field_40718.rotationDegrees(p.rotZ));
/* 448 */       lineCount += appendTriangleDashedEdges(buf, ms.method_23760().method_23761(), s, alpha);
/* 449 */       ms.method_22909();
/*     */     } 
/*     */     
/* 452 */     if (lineCount > 0) {
/* 453 */       class_286.method_43433(buf.method_60800());
/*     */     }
/*     */   }
/*     */   
/*     */   private int appendCubeDashedEdges(class_287 buf, Matrix4f mat, float s, float alpha) {
/* 458 */     int color = colorToInt(Math.min(1.0F, this.cr * 1.5F), Math.min(1.0F, this.cg * 1.5F), Math.min(1.0F, this.cb * 1.5F), alpha);
/*     */     
/* 460 */     float dashLen = s * 0.3F;
/* 461 */     float gapLen = s * 0.25F;
/*     */     
/* 463 */     int lineCount = 0;
/*     */     
/* 465 */     for (byte[] edge : CUBE_EDGES) {
/* 466 */       float x1 = edge[0] * s;
/* 467 */       float y1 = edge[1] * s;
/* 468 */       float z1 = edge[2] * s;
/* 469 */       float x2 = edge[3] * s;
/* 470 */       float y2 = edge[4] * s;
/* 471 */       float z2 = edge[5] * s;
/*     */       
/* 473 */       float dx = x2 - x1;
/* 474 */       float dy = y2 - y1;
/* 475 */       float dz = z2 - z1;
/* 476 */       float len = class_3532.method_15355(dx * dx + dy * dy + dz * dz);
/*     */       
/* 478 */       if (len >= 0.001F) {
/*     */         
/* 480 */         float nx = dx / len;
/* 481 */         float ny = dy / len;
/* 482 */         float nz = dz / len;
/*     */         
/* 484 */         float pos = 0.0F;
/* 485 */         boolean drawing = true;
/*     */         
/* 487 */         while (pos < len) {
/* 488 */           float segLen = drawing ? dashLen : gapLen;
/* 489 */           float end = Math.min(pos + segLen, len);
/*     */           
/* 491 */           if (drawing) {
/* 492 */             buf.method_22918(mat, x1 + nx * pos, y1 + ny * pos, z1 + nz * pos).method_39415(color);
/* 493 */             buf.method_22918(mat, x1 + nx * end, y1 + ny * end, z1 + nz * end).method_39415(color);
/* 494 */             lineCount++;
/*     */           } 
/*     */           
/* 497 */           pos = end;
/* 498 */           drawing = !drawing;
/*     */         } 
/*     */       } 
/*     */     } 
/* 502 */     return lineCount;
/*     */   }
/*     */   
/*     */   private int appendTriangleDashedEdges(class_287 buf, Matrix4f mat, float s, float alpha) {
/* 506 */     int color = colorToInt(Math.min(1.0F, this.cr * 1.5F), Math.min(1.0F, this.cg * 1.5F), Math.min(1.0F, this.cb * 1.5F), alpha);
/*     */     
/* 508 */     float dashLen = s * 0.3F;
/* 509 */     float gapLen = s * 0.25F;
/*     */     
/* 511 */     int lineCount = 0;
/*     */     
/* 513 */     float top = s;
/* 514 */     float bottom = -s;
/* 515 */     float halfBase = s * 0.866F;
/*     */     
/* 517 */     for (byte[] edge : TRIANGLE_EDGES) {
/* 518 */       float x1 = trianglePointX(edge[0], halfBase);
/* 519 */       float y1 = (edge[0] == 0) ? top : bottom;
/* 520 */       float z1 = trianglePointZ(edge[0], halfBase);
/* 521 */       float x2 = trianglePointX(edge[1], halfBase);
/* 522 */       float y2 = (edge[1] == 0) ? top : bottom;
/* 523 */       float z2 = trianglePointZ(edge[1], halfBase);
/*     */       
/* 525 */       float dx = x2 - x1;
/* 526 */       float dy = y2 - y1;
/* 527 */       float dz = z2 - z1;
/* 528 */       float len = class_3532.method_15355(dx * dx + dy * dy + dz * dz);
/*     */       
/* 530 */       if (len >= 0.001F) {
/*     */         
/* 532 */         float nx = dx / len;
/* 533 */         float ny = dy / len;
/* 534 */         float nz = dz / len;
/*     */         
/* 536 */         float pos = 0.0F;
/* 537 */         boolean drawing = true;
/*     */         
/* 539 */         while (pos < len) {
/* 540 */           float segLen = drawing ? dashLen : gapLen;
/* 541 */           float end = Math.min(pos + segLen, len);
/*     */           
/* 543 */           if (drawing) {
/* 544 */             buf.method_22918(mat, x1 + nx * pos, y1 + ny * pos, z1 + nz * pos).method_39415(color);
/* 545 */             buf.method_22918(mat, x1 + nx * end, y1 + ny * end, z1 + nz * end).method_39415(color);
/* 546 */             lineCount++;
/*     */           } 
/*     */           
/* 549 */           pos = end;
/* 550 */           drawing = !drawing;
/*     */         } 
/*     */       } 
/*     */     } 
/* 554 */     return lineCount;
/*     */   }
/*     */   
/*     */   private float trianglePointX(int index, float halfBase) {
/* 558 */     switch (index) { case 1: case 4: case 2: case 3:  }  return 
/*     */ 
/*     */       
/* 561 */       0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   private float trianglePointZ(int index, float halfBase) {
/* 566 */     switch (index) { case 1: case 2: case 3: case 4:  }  return 
/*     */ 
/*     */       
/* 569 */       0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   private CubeParticle spawnCube() {
/* 574 */     float vx, vy, vz, r = 12.0F;
/* 575 */     boolean falling = this.animation.is("Падение");
/* 576 */     int life = falling ? (260 + this.random.nextInt(220)) : (420 + this.random.nextInt(420));
/*     */     
/* 578 */     double x = mc.field_1724.method_23317() + (this.random.nextDouble() * 2.0D - 1.0D) * r;
/* 579 */     double y = falling ? (mc.field_1724.method_23318() + 4.0D + this.random.nextDouble() * r * 0.55D) : (mc.field_1724.method_23318() + 2.0D + this.random.nextDouble() * r * 0.8D);
/* 580 */     double z = mc.field_1724.method_23321() + (this.random.nextDouble() * 2.0D - 1.0D) * r;
/*     */     
/* 582 */     float speedMult = this.speed.get();
/*     */ 
/*     */     
/* 585 */     if (falling) {
/* 586 */       vx = (this.random.nextFloat() - 0.5F) * 0.008F * speedMult;
/* 587 */       vy = (-0.012F - this.random.nextFloat() * 0.012F) * speedMult;
/* 588 */       vz = (this.random.nextFloat() - 0.5F) * 0.008F * speedMult;
/*     */     } else {
/* 590 */       float yaw = this.random.nextFloat() * 360.0F;
/* 591 */       float vel = (0.01F + this.random.nextFloat() * 0.02F) * speedMult;
/* 592 */       vx = -class_3532.method_15374((float)Math.toRadians(yaw)) * vel;
/* 593 */       vz = class_3532.method_15362((float)Math.toRadians(yaw)) * vel;
/* 594 */       vy = (this.random.nextFloat() - 0.5F) * 0.01F * speedMult;
/*     */     } 
/*     */     
/* 597 */     return new CubeParticle(x, y, z, vx, vy, vz, this.random
/* 598 */         .nextFloat() * 360.0F, this.random.nextFloat() * 360.0F, this.random.nextFloat() * 360.0F, (this.random
/* 599 */         .nextFloat() - 0.5F) * 1.5F, (this.random.nextFloat() - 0.5F) * 1.5F, (this.random.nextFloat() - 0.5F) * 1.5F, life, 
/* 600 */         (float)(this.random.nextDouble() * Math.PI * 2.0D), this.random.nextFloat() * 10.0F);
/*     */   }
/*     */   
/*     */   private static int colorToInt(float r, float g, float b, float a) {
/* 604 */     return (int)(a * 255.0F) << 24 | (int)(r * 255.0F) << 16 | (int)(g * 255.0F) << 8 | (int)(b * 255.0F);
/*     */   }
/*     */   private static class CubeParticle { double x; double y;
/*     */     double z;
/*     */     float vx;
/*     */     float vy;
/*     */     float vz;
/*     */     float rotX;
/*     */     float rotY;
/*     */     
/*     */     CubeParticle(double x, double y, double z, float vx, float vy, float vz, float rotX, float rotY, float rotZ, float rotSpeedX, float rotSpeedY, float rotSpeedZ, int life, float wobblePhase, float wobbleOffset) {
/* 615 */       this.x = x; this.y = y; this.z = z; this.vx = vx; this.vy = vy; this.vz = vz;
/* 616 */       this.rotX = rotX; this.rotY = rotY; this.rotZ = rotZ;
/* 617 */       this.rotSpeedX = rotSpeedX; this.rotSpeedY = rotSpeedY; this.rotSpeedZ = rotSpeedZ;
/* 618 */       this.life = this.maxLife = life;
/* 619 */       this.wobblePhase = wobblePhase; this.wobbleOffset = wobbleOffset;
/*     */     }
/*     */     
/*     */     float rotZ;
/*     */     float rotSpeedX;
/*     */     float rotSpeedY;
/*     */     float rotSpeedZ;
/*     */     float wobblePhase;
/*     */     float wobbleOffset;
/*     */     float renderAlpha;
/*     */     int life;
/*     */     int maxLife; }
/*     */ 
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\Cubes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */