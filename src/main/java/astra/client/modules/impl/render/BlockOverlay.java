/*     */ package shame.astra.client.modules.impl.render;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import net.minecraft.class_10142;
/*     */ import net.minecraft.class_1922;
/*     */ import net.minecraft.class_2338;
/*     */ import net.minecraft.class_238;
/*     */ import net.minecraft.class_239;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_265;
/*     */ import net.minecraft.class_276;
/*     */ import net.minecraft.class_284;
/*     */ import net.minecraft.class_286;
/*     */ import net.minecraft.class_287;
/*     */ import net.minecraft.class_289;
/*     */ import net.minecraft.class_290;
/*     */ import net.minecraft.class_293;
/*     */ import net.minecraft.class_3965;
/*     */ import net.minecraft.class_5944;
/*     */ import net.minecraft.class_6367;
/*     */ import org.joml.Matrix4f;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.lwjgl.opengl.GL30;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.Event3DRender;
/*     */ import shame.astra.api.events.implement.EventRender;
/*     */ import shame.astra.api.storages.implement.helpertstorages.Theme;
/*     */ import shame.astra.api.utils.color.ColorUtils;
/*     */ import shame.astra.api.utils.render.ShaderUtils;
/*     */ import shame.astra.astra;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*     */ import shame.astra.client.modules.settings.implement.ModeSetting;
/*     */ 
/*     */ public class BlockOverlay extends Module {
/*  36 */   public static BlockOverlay INSTANCE = new BlockOverlay();
/*  37 */   private final ModeSetting mode = new ModeSetting("Режим", "Шейдер", new String[] { "Шейдер", "Нитки" });
/*  38 */   private final FloatSetting waveSpeed = (new FloatSetting("Скорость волн", 1.2F, 0.1F, 5.0F, 0.1F))
/*  39 */     .visible(() -> Boolean.valueOf(this.mode.is("Шейдер")));
/*  40 */   private final FloatSetting waveScale = (new FloatSetting("Частота волн", 1.0F, 1.0F, 3.0F, 0.1F))
/*  41 */     .visible(() -> Boolean.valueOf(this.mode.is("Шейдер")));
/*  42 */   private final FloatSetting lineSpeed = (new FloatSetting("Скорость нитей", 1.4F, 0.1F, 5.0F, 0.1F))
/*  43 */     .visible(() -> Boolean.valueOf(this.mode.is("Нитки")));
/*  44 */   private final FloatSetting lineJitter = (new FloatSetting("Изгиб нитей", 0.55F, 0.0F, 1.5F, 0.01F))
/*  45 */     .visible(() -> Boolean.valueOf(this.mode.is("Нитки")));
/*  46 */   private final FloatSetting outline = new FloatSetting("Ширина обводки", 1.1F, 0.1F, 5.0F, 0.1F);
/*  47 */   private final FloatSetting glow = new FloatSetting("Сила свечения", 1.0F, 0.0F, 5.0F, 0.1F);
/*  48 */   private final FloatSetting fill = new FloatSetting("Заливка", 0.6F, 0.0F, 1.0F, 0.01F);
/*  49 */   private final FloatSetting alpha = new FloatSetting("Прозрачность", 1.0F, 0.0F, 1.0F, 0.01F);
/*  50 */   private final FloatSetting smooth = new FloatSetting("Плавность", 0.24F, 0.05F, 0.6F, 0.01F);
/*     */   
/*     */   private class_276 maskBuffer;
/*  53 */   private int fbWidth = -1;
/*  54 */   private int fbHeight = -1;
/*     */   
/*     */   private boolean hasMask;
/*     */   private class_2338 lastBlockPos;
/*     */   private class_238 displayBox;
/*     */   private class_238 targetBox;
/*  60 */   private int cachedThemeColor1 = -1;
/*  61 */   private int cachedThemeColor2 = -1;
/*     */   
/*     */   public BlockOverlay() {
/*  64 */     super("BlockOverlay", "Block overlay shader", Module.ModuleCategory.RENDER);
/*  65 */     addSettings(new Setting[] { (Setting)this.mode, (Setting)this.waveSpeed, (Setting)this.waveScale, (Setting)this.lineSpeed, (Setting)this.lineJitter, (Setting)this.outline, (Setting)this.glow, (Setting)this.fill, (Setting)this.alpha, (Setting)this.smooth });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  70 */     this.hasMask = false;
/*  71 */     this.lastBlockPos = null;
/*  72 */     this.displayBox = null;
/*  73 */     this.targetBox = null;
/*  74 */     super.onDisable();
/*     */   }
/*     */   
/*     */   @EventLink(priority = -100)
/*     */   public void onRender3D(Event3DRender event) {
/*  79 */     if (mc == null || mc.field_1687 == null || mc.field_1724 == null)
/*     */       return; 
/*  81 */     class_238 worldBox = getTargetedBlockBox();
/*  82 */     if (worldBox == null) {
/*  83 */       this.hasMask = false;
/*  84 */       this.lastBlockPos = null;
/*  85 */       this.displayBox = null;
/*  86 */       this.targetBox = null;
/*     */       
/*     */       return;
/*     */     } 
/*  90 */     if (this.displayBox == null || this.targetBox == null || this.lastBlockPos == null) {
/*  91 */       this.displayBox = worldBox;
/*  92 */       this.targetBox = worldBox;
/*     */     } else {
/*  94 */       this.targetBox = worldBox;
/*  95 */       this.displayBox = lerpBox(this.displayBox, this.targetBox, this.smooth.get());
/*     */     } 
/*  97 */     this.lastBlockPos = class_2338.method_49637(worldBox.field_1323, worldBox.field_1322, worldBox.field_1321);
/*  98 */     updateCachedThemeColors();
/*     */     
/* 100 */     class_243 cam = event.getCamera().method_19326();
/* 101 */     class_238 localBox = this.displayBox.method_989(-cam.field_1352, -cam.field_1351, -cam.field_1350);
/* 102 */     Matrix4f matrix = event.getMatrices().method_23760().method_23761();
/*     */     
/* 104 */     ensureMaskBuffer();
/* 105 */     if (this.maskBuffer == null)
/*     */       return; 
/* 107 */     this.hasMask = true;
/* 108 */     this.maskBuffer.method_1236(0.0F, 0.0F, 0.0F, 0.0F);
/* 109 */     this.maskBuffer.method_1230();
/* 110 */     copyMainDepthToMask();
/* 111 */     this.maskBuffer.method_1235(false);
/*     */     
/* 113 */     RenderSystem.enableBlend();
/* 114 */     RenderSystem.defaultBlendFunc();
/* 115 */     RenderSystem.disableCull();
/* 116 */     RenderSystem.enableDepthTest();
/* 117 */     RenderSystem.depthMask(false);
/* 118 */     RenderSystem.setShader(class_10142.field_53876);
/* 119 */     drawMaskBox(matrix, localBox);
/* 120 */     RenderSystem.depthMask(true);
/* 121 */     RenderSystem.disableDepthTest();
/* 122 */     RenderSystem.enableCull();
/* 123 */     RenderSystem.disableBlend();
/*     */     
/* 125 */     mc.method_1522().method_1235(false);
/*     */     
/* 127 */     if (this.mode.is("Нитки")) {
/* 128 */       drawAnimatedWeb(matrix, localBox);
/*     */       return;
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventLink(priority = 200)
/*     */   public void onRender2D(EventRender.Default event) {
/* 135 */     if (!this.hasMask || this.maskBuffer == null)
/* 136 */       return;  if (this.mode.is("Нитки"))
/*     */       return; 
/* 138 */     class_5944 shader = mc.method_62887().method_62947(ShaderUtils.blockOverlay);
/* 139 */     if (shader == null)
/*     */       return; 
/* 141 */     boolean lineMode = this.mode.is("Нитки");
/* 142 */     int color1 = this.cachedThemeColor1;
/* 143 */     int color2 = this.cachedThemeColor2;
/*     */     
/* 145 */     mc.method_1522().method_1235(false);
/* 146 */     RenderSystem.enableBlend();
/* 147 */     RenderSystem.defaultBlendFunc();
/* 148 */     RenderSystem.enableDepthTest();
/*     */     
/* 150 */     RenderSystem.setShader(ShaderUtils.blockOverlay);
/* 151 */     RenderSystem.setShaderTexture(0, this.maskBuffer.method_30277());
/*     */     
/* 153 */     setUniform(shader, "texelSize", 1.0F / Math.max(1, mc.method_22683().method_4489()), 1.0F / Math.max(1, mc.method_22683().method_4506()));
/* 154 */     setUniform(shader, "color", ColorUtils.redf(color1), ColorUtils.greenf(color1), ColorUtils.bluef(color1));
/* 155 */     setUniform(shader, "color2", ColorUtils.redf(color2), ColorUtils.greenf(color2), ColorUtils.bluef(color2));
/* 156 */     setUniform(shader, "time", (float)(System.currentTimeMillis() % 100000L) / 1000.0F);
/* 157 */     setUniform(shader, "speed", this.waveSpeed.get());
/* 158 */     setUniform(shader, "scale", this.waveScale.get());
/* 159 */     setUniform(shader, "outline", this.outline.get());
/* 160 */     setUniform(shader, "glow", lineMode ? 0.0F : this.glow.get());
/* 161 */     setUniform(shader, "fill", lineMode ? 0.0F : this.fill.get());
/* 162 */     setUniform(shader, "alpha", lineMode ? 1.0F : this.alpha.get());
/* 163 */     setUniform(shader, "outlineOnly", lineMode ? 1.0F : 0.0F);
/*     */     
/* 165 */     drawFullscreenQuad();
/*     */     
/* 167 */     RenderSystem.enableDepthTest();
/* 168 */     RenderSystem.disableBlend();
/* 169 */     RenderSystem.defaultBlendFunc();
/* 170 */     RenderSystem.setShaderTexture(0, 0);
/*     */   }
/*     */   
/*     */   private void setUniform(class_5944 shader, String name, float value) {
/* 174 */     class_284 uniform = shader.method_34582(name);
/* 175 */     if (uniform != null) uniform.method_1251(value); 
/*     */   }
/*     */   
/*     */   private void setUniform(class_5944 shader, String name, float x, float y) {
/* 179 */     class_284 uniform = shader.method_34582(name);
/* 180 */     if (uniform != null) uniform.method_1255(x, y); 
/*     */   }
/*     */   
/*     */   private void setUniform(class_5944 shader, String name, float x, float y, float z) {
/* 184 */     class_284 uniform = shader.method_34582(name);
/* 185 */     if (uniform != null) uniform.method_1249(x, y, z); 
/*     */   }
/*     */   
/*     */   private void ensureMaskBuffer() {
/* 189 */     int w = mc.method_22683().method_4489();
/* 190 */     int h = mc.method_22683().method_4506();
/* 191 */     if (this.maskBuffer == null || this.fbWidth != w || this.fbHeight != h) {
/* 192 */       if (this.maskBuffer != null) {
/* 193 */         this.maskBuffer.method_1238();
/*     */       }
/* 195 */       this.maskBuffer = (class_276)new class_6367(w, h, true);
/* 196 */       this.fbWidth = w;
/* 197 */       this.fbHeight = h;
/*     */     } 
/*     */   }
/*     */   
/*     */   private class_238 getTargetedBlockBox() {
/* 202 */     class_239 hit = mc.field_1765;
/* 203 */     if (hit instanceof class_3965) { class_3965 blockHit = (class_3965)hit; if (hit.method_17783() == class_239.class_240.field_1332) {
/*     */ 
/*     */ 
/*     */         
/* 207 */         class_2338 pos = blockHit.method_17777();
/* 208 */         if (pos == null) return null; 
/* 209 */         if (mc.field_1687.method_8320(pos).method_26215()) return null;
/*     */         
/* 211 */         class_265 shape = mc.field_1687.method_8320(pos).method_26218((class_1922)mc.field_1687, pos);
/* 212 */         class_238 box = shape.method_1110() ? new class_238(pos) : shape.method_1107().method_996(pos);
/* 213 */         return box.method_1014(0.002D);
/*     */       }  }
/*     */     
/*     */     return null; } private class_238 lerpBox(class_238 a, class_238 b, float t) {
/* 217 */     return new class_238(a.field_1323 + (b.field_1323 - a.field_1323) * t, a.field_1322 + (b.field_1322 - a.field_1322) * t, a.field_1321 + (b.field_1321 - a.field_1321) * t, a.field_1320 + (b.field_1320 - a.field_1320) * t, a.field_1325 + (b.field_1325 - a.field_1325) * t, a.field_1324 + (b.field_1324 - a.field_1324) * t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void drawAnimatedWeb(Matrix4f matrix, class_238 box) {
/* 228 */     int strandsPerFace = 5;
/* 229 */     int samples = 18;
/* 230 */     float t = (float)(System.currentTimeMillis() % 100000L) / 1000.0F * this.lineSpeed.get();
/* 231 */     float lineWidth = 0.0025F;
/* 232 */     float bendBase = 0.06F + this.lineJitter.get() * 0.2F;
/* 233 */     int baseAlpha = Math.max(20, Math.min(255, (int)(this.alpha.get() * 210.0F)));
/* 234 */     int themeColor = this.cachedThemeColor1;
/* 235 */     long seed = (this.lastBlockPos != null) ? this.lastBlockPos.method_10063() : 1L;
/*     */     
/* 237 */     RenderSystem.enableBlend();
/* 238 */     RenderSystem.defaultBlendFunc();
/* 239 */     RenderSystem.disableCull();
/* 240 */     RenderSystem.enableDepthTest();
/* 241 */     RenderSystem.depthMask(false);
/* 242 */     RenderSystem.setShader(class_10142.field_53876);
/* 243 */     drawFilledBox(matrix, box, ColorUtils.setAlphaColor(themeColor, (int)(this.alpha.get() * this.fill.get() * 170.0F)));
/*     */     
/* 245 */     for (int face = 0; face < 6; face++) {
/* 246 */       int[] neighbors = faceNeighbors(face);
/* 247 */       for (int strand = 0; strand < strandsPerFace; strand++) {
/* 248 */         int key = face * 1000 + strand * 53;
/* 249 */         int adj = neighbors[strand % neighbors.length];
/* 250 */         double phase = t * (0.95D + rand01(seed, key + 1) * 0.55D) + strand * 0.83D + face * 1.11D;
/* 251 */         double edgeT = clamp01(0.5D + Math.sin(phase * 1.37D + rand01(seed, key + 2) * 6.2831853D) * 0.38D);
/*     */         
/* 253 */         class_243 pivot = edgePoint(box, face, adj, edgeT, 0.0015D);
/* 254 */         class_243 start = facePoint(box, face, 
/* 255 */             clamp01(0.5D + (rand01(seed, key + 3) - 0.5D) * 0.46D), 
/* 256 */             clamp01(0.5D + (rand01(seed, key + 4) - 0.5D) * 0.46D), 0.0015D);
/*     */         
/* 258 */         class_243 end = facePoint(box, adj, 
/* 259 */             clamp01(0.5D + (rand01(seed, key + 5) - 0.5D) * 0.46D), 
/* 260 */             clamp01(0.5D + (rand01(seed, key + 6) - 0.5D) * 0.46D), 0.0015D);
/*     */ 
/*     */         
/* 263 */         class_243[] basisA = faceBasis(face);
/* 264 */         class_243[] basisB = faceBasis(adj);
/* 265 */         class_243 normalA = faceNormal(face);
/* 266 */         class_243 normalB = faceNormal(adj);
/*     */         
/* 268 */         double bendA = bendBase * (0.7D + rand01(seed, key + 7)) * Math.sin(phase * 1.9D + rand01(seed, key + 8) * 6.2831853D);
/*     */         
/* 270 */         double bendB = bendBase * (0.7D + rand01(seed, key + 9)) * Math.cos(phase * 1.7D + rand01(seed, key + 10) * 6.2831853D);
/*     */         
/* 272 */         class_243 dirA = pivot.method_1020(start);
/* 273 */         class_243 c1a = start.method_1019(dirA.method_1021(0.38D)).method_1019(basisA[0].method_1021(bendA)).method_1019(basisA[1].method_1021(-bendA * 0.55D));
/* 274 */         class_243 c2a = start.method_1019(dirA.method_1021(0.76D)).method_1019(basisA[0].method_1021(-bendA * 0.65D)).method_1019(basisA[1].method_1021(bendA * 0.4D));
/*     */         
/* 276 */         class_243 dirB = end.method_1020(pivot);
/* 277 */         class_243 c1b = pivot.method_1019(dirB.method_1021(0.24D)).method_1019(basisB[0].method_1021(bendB)).method_1019(basisB[1].method_1021(bendB * 0.45D));
/* 278 */         class_243 c2b = pivot.method_1019(dirB.method_1021(0.62D)).method_1019(basisB[0].method_1021(-bendB * 0.7D)).method_1019(basisB[1].method_1021(-bendB * 0.35D));
/*     */         
/* 280 */         int alphaLine = Math.max(18, Math.min(255, (int)(baseAlpha * (0.74D + 0.26D * Math.sin(phase * 2.6D)))));
/* 281 */         int color = ColorUtils.setAlphaColor(themeColor, alphaLine);
/* 282 */         drawBezierRibbon(matrix, start, c1a, c2a, pivot, normalA, samples, color, lineWidth);
/* 283 */         drawBezierRibbon(matrix, pivot, c1b, c2b, end, normalB, samples, color, lineWidth);
/*     */       } 
/*     */     } 
/*     */     
/* 287 */     RenderSystem.enableDepthTest();
/* 288 */     RenderSystem.depthMask(true);
/* 289 */     RenderSystem.enableCull();
/* 290 */     RenderSystem.disableBlend();
/*     */   }
/*     */   
/*     */   private void copyMainDepthToMask() {
/* 294 */     if (this.maskBuffer == null)
/*     */       return; 
/* 296 */     int readFbo = GL11.glGetInteger(36010);
/* 297 */     int drawFbo = GL11.glGetInteger(36006);
/* 298 */     int w = mc.method_22683().method_4489();
/* 299 */     int h = mc.method_22683().method_4506();
/*     */     
/* 301 */     GL30.glBindFramebuffer(36008, (mc.method_1522()).field_1476);
/* 302 */     GL30.glBindFramebuffer(36009, this.maskBuffer.field_1476);
/* 303 */     GL30.glBlitFramebuffer(0, 0, w, h, 0, 0, w, h, 256, 9728);
/* 304 */     GL30.glBindFramebuffer(36008, readFbo);
/* 305 */     GL30.glBindFramebuffer(36009, drawFbo);
/*     */   }
/*     */   
/*     */   private class_243 cubicBezier(class_243 p0, class_243 p1, class_243 p2, class_243 p3, float t) {
/* 309 */     double it = 1.0D - t;
/* 310 */     double it2 = it * it;
/* 311 */     double t2 = (t * t);
/* 312 */     return p0.method_1021(it2 * it)
/* 313 */       .method_1019(p1.method_1021(3.0D * it2 * t))
/* 314 */       .method_1019(p2.method_1021(3.0D * it * t2))
/* 315 */       .method_1019(p3.method_1021(t2 * t));
/*     */   }
/*     */   
/*     */   private void drawBezierRibbon(Matrix4f matrix, class_243 p0, class_243 p1, class_243 p2, class_243 p3, class_243 faceNormal, int samples, int color, float halfWidth) {
/* 319 */     class_243[] points = new class_243[samples + 1];
/* 320 */     for (int s = 0; s <= samples; s++) {
/* 321 */       float u = s / samples;
/* 322 */       points[s] = cubicBezier(p0, p1, p2, p3, u);
/*     */     } 
/*     */     
/* 325 */     class_287 quads = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1576);
/* 326 */     for (int i = 0; i < samples; i++) {
/* 327 */       class_243 a = points[i];
/* 328 */       class_243 b = points[i + 1];
/* 329 */       class_243 dir = b.method_1020(a);
/* 330 */       if (dir.method_1027() >= 1.0E-6D) {
/*     */         
/* 332 */         class_243 perp = faceNormal.method_1036(dir).method_1029().method_1021(halfWidth);
/* 333 */         class_243 aL = a.method_1019(perp);
/* 334 */         class_243 aR = a.method_1020(perp);
/* 335 */         class_243 bL = b.method_1019(perp);
/* 336 */         class_243 bR = b.method_1020(perp);
/*     */         
/* 338 */         quads.method_22918(matrix, (float)aL.field_1352, (float)aL.field_1351, (float)aL.field_1350).method_39415(color);
/* 339 */         quads.method_22918(matrix, (float)aR.field_1352, (float)aR.field_1351, (float)aR.field_1350).method_39415(color);
/* 340 */         quads.method_22918(matrix, (float)bR.field_1352, (float)bR.field_1351, (float)bR.field_1350).method_39415(color);
/* 341 */         quads.method_22918(matrix, (float)bL.field_1352, (float)bL.field_1351, (float)bL.field_1350).method_39415(color);
/*     */       } 
/* 343 */     }  class_286.method_43433(quads.method_60800());
/*     */   }
/*     */   
/*     */   private void updateCachedThemeColors() {
/* 347 */     if (astra.INSTANCE == null || astra.INSTANCE.themeStorage == null || astra.INSTANCE.themeStorage.getThemes() == null) {
/* 348 */       this.cachedThemeColor1 = ColorUtils.getThemeColor(0);
/* 349 */       this.cachedThemeColor2 = ColorUtils.getThemeColor(180);
/*     */       
/*     */       return;
/*     */     } 
/* 353 */     Theme theme = astra.INSTANCE.themeStorage.getThemes().getTheme();
/* 354 */     if (theme == null) {
/* 355 */       this.cachedThemeColor1 = ColorUtils.getThemeColor(0);
/* 356 */       this.cachedThemeColor2 = ColorUtils.getThemeColor(180);
/*     */       
/*     */       return;
/*     */     } 
/* 360 */     if (!"Rainbow".equals(theme.getName())) {
/* 361 */       int base = (theme.color != null && theme.color.length > 0) ? theme.color[0] : ColorUtils.getThemeColor(0);
/* 362 */       this.cachedThemeColor1 = base;
/* 363 */       this.cachedThemeColor2 = base;
/*     */     } else {
/* 365 */       this.cachedThemeColor1 = ColorUtils.getThemeColor();
/* 366 */       this.cachedThemeColor2 = ColorUtils.getThemeColor(180);
/*     */     } 
/*     */   }
/*     */   
/*     */   private int[] faceNeighbors(int face) {
/* 371 */     switch (face) { case 0: case 1:
/* 372 */         (new int[4])[0] = 2; (new int[4])[1] = 3; (new int[4])[2] = 4; (new int[4])[3] = 5;
/* 373 */       case 2: case 3: (new int[4])[0] = 0; (new int[4])[1] = 1; (new int[4])[2] = 4; (new int[4])[3] = 5; }
/* 374 */      return new int[] { 0, 1, 2, 3 };
/*     */   }
/*     */ 
/*     */   
/*     */   private class_243[] faceBasis(int face) {
/* 379 */     switch (face) { case 0: case 1:
/* 380 */         (new class_243[2])[0] = new class_243(1.0D, 0.0D, 0.0D); (new class_243[2])[1] = new class_243(0.0D, 0.0D, 1.0D);
/* 381 */       case 2: case 3: (new class_243[2])[0] = new class_243(1.0D, 0.0D, 0.0D); (new class_243[2])[1] = new class_243(0.0D, 1.0D, 0.0D); }
/* 382 */      return new class_243[] { new class_243(0.0D, 0.0D, 1.0D), new class_243(0.0D, 1.0D, 0.0D) };
/*     */   }
/*     */ 
/*     */   
/*     */   private class_243 faceNormal(int face) {
/* 387 */     switch (face) { case 0: case 1: case 2: case 3: case 4:  }  return 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 393 */       new class_243(1.0D, 0.0D, 0.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   private class_243 edgePoint(class_238 box, int faceA, int faceB, double t, double inset) {
/* 398 */     double x = Double.NaN;
/* 399 */     double y = Double.NaN;
/* 400 */     double z = Double.NaN;
/*     */     
/* 402 */     double[] fixedA = faceFixedCoords(box, faceA, inset);
/* 403 */     if (!Double.isNaN(fixedA[0])) x = fixedA[0]; 
/* 404 */     if (!Double.isNaN(fixedA[1])) y = fixedA[1]; 
/* 405 */     if (!Double.isNaN(fixedA[2])) z = fixedA[2];
/*     */     
/* 407 */     double[] fixedB = faceFixedCoords(box, faceB, inset);
/* 408 */     if (!Double.isNaN(fixedB[0])) x = fixedB[0]; 
/* 409 */     if (!Double.isNaN(fixedB[1])) y = fixedB[1]; 
/* 410 */     if (!Double.isNaN(fixedB[2])) z = fixedB[2];
/*     */     
/* 412 */     double tt = clamp01(t);
/* 413 */     if (Double.isNaN(x)) x = lerp(box.field_1323, box.field_1320, tt); 
/* 414 */     if (Double.isNaN(y)) y = lerp(box.field_1322, box.field_1325, tt); 
/* 415 */     if (Double.isNaN(z)) z = lerp(box.field_1321, box.field_1324, tt); 
/* 416 */     return new class_243(x, y, z);
/*     */   }
/*     */   
/*     */   private double[] faceFixedCoords(class_238 box, int face, double inset) {
/* 420 */     switch (face) { case 0:
/* 421 */         (new double[3])[0] = Double.NaN; (new double[3])[1] = box.field_1325 - inset; (new double[3])[2] = Double.NaN;
/* 422 */       case 1: (new double[3])[0] = Double.NaN; (new double[3])[1] = box.field_1322 + inset; (new double[3])[2] = Double.NaN;
/* 423 */       case 2: (new double[3])[0] = Double.NaN; (new double[3])[1] = Double.NaN; (new double[3])[2] = box.field_1321 + inset;
/* 424 */       case 3: (new double[3])[0] = Double.NaN; (new double[3])[1] = Double.NaN; (new double[3])[2] = box.field_1324 - inset;
/* 425 */       case 4: (new double[3])[0] = box.field_1323 + inset; (new double[3])[1] = Double.NaN; (new double[3])[2] = Double.NaN; }
/* 426 */      return new double[] { box.field_1320 - inset, Double.NaN, Double.NaN };
/*     */   }
/*     */ 
/*     */   
/*     */   private class_243 facePoint(class_238 box, int face, double u, double v, double inset) {
/* 431 */     u = clamp01(u);
/* 432 */     v = clamp01(v);
/* 433 */     switch (face) { case 0: case 1: case 2: case 3: case 4:  }  return 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 439 */       new class_243(box.field_1320 - inset, lerp(box.field_1322, box.field_1325, v), lerp(box.field_1321, box.field_1324, u));
/*     */   }
/*     */ 
/*     */   
/*     */   private double rand01(long seed, int salt) {
/* 444 */     long x = seed + -7046029254386353131L * (salt + 1L);
/* 445 */     x ^= x >>> 30L;
/* 446 */     x *= -4658895280553007687L;
/* 447 */     x ^= x >>> 27L;
/* 448 */     x *= -7723592293110705685L;
/* 449 */     x ^= x >>> 31L;
/* 450 */     return (x & 0xFFFFFFL) / 1.6777216E7D;
/*     */   }
/*     */   
/*     */   private double lerp(double a, double b, double t) {
/* 454 */     return a + (b - a) * t;
/*     */   }
/*     */   
/*     */   private double clamp01(double v) {
/* 458 */     return Math.max(0.0D, Math.min(1.0D, v));
/*     */   }
/*     */   
/*     */   private void drawFilledBox(Matrix4f matrix, class_238 box, int color) {
/* 462 */     class_287 b = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1576);
/*     */ 
/*     */     
/* 465 */     b.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_39415(color);
/* 466 */     b.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_39415(color);
/* 467 */     b.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_39415(color);
/* 468 */     b.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_39415(color);
/*     */     
/* 470 */     b.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1321).method_39415(color);
/* 471 */     b.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1321).method_39415(color);
/* 472 */     b.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324).method_39415(color);
/* 473 */     b.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1324).method_39415(color);
/*     */     
/* 475 */     b.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_39415(color);
/* 476 */     b.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_39415(color);
/* 477 */     b.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1321).method_39415(color);
/* 478 */     b.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1321).method_39415(color);
/*     */     
/* 480 */     b.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_39415(color);
/* 481 */     b.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1324).method_39415(color);
/* 482 */     b.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324).method_39415(color);
/* 483 */     b.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_39415(color);
/*     */     
/* 485 */     b.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_39415(color);
/* 486 */     b.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1321).method_39415(color);
/* 487 */     b.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1324).method_39415(color);
/* 488 */     b.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_39415(color);
/*     */     
/* 490 */     b.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_39415(color);
/* 491 */     b.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_39415(color);
/* 492 */     b.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324).method_39415(color);
/* 493 */     b.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1321).method_39415(color);
/*     */     
/* 495 */     class_286.method_43433(b.method_60800());
/*     */   }
/*     */   
/*     */   private void drawMaskBox(Matrix4f matrix, class_238 box) {
/* 499 */     class_287 b = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1576);
/* 500 */     int white = -1;
/*     */ 
/*     */     
/* 503 */     b.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_39415(white);
/* 504 */     b.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_39415(white);
/* 505 */     b.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_39415(white);
/* 506 */     b.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_39415(white);
/*     */     
/* 508 */     b.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1321).method_39415(white);
/* 509 */     b.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1321).method_39415(white);
/* 510 */     b.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324).method_39415(white);
/* 511 */     b.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1324).method_39415(white);
/*     */     
/* 513 */     b.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_39415(white);
/* 514 */     b.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_39415(white);
/* 515 */     b.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1321).method_39415(white);
/* 516 */     b.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1321).method_39415(white);
/*     */     
/* 518 */     b.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_39415(white);
/* 519 */     b.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1324).method_39415(white);
/* 520 */     b.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324).method_39415(white);
/* 521 */     b.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_39415(white);
/*     */     
/* 523 */     b.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_39415(white);
/* 524 */     b.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1321).method_39415(white);
/* 525 */     b.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1324).method_39415(white);
/* 526 */     b.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_39415(white);
/*     */     
/* 528 */     b.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_39415(white);
/* 529 */     b.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_39415(white);
/* 530 */     b.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324).method_39415(white);
/* 531 */     b.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1321).method_39415(white);
/*     */     
/* 533 */     class_286.method_43433(b.method_60800());
/*     */   }
/*     */   
/*     */   private void drawFullscreenQuad() {
/* 537 */     class_287 b = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
/* 538 */     float width = Math.max(mc.method_22683().method_4486(), 1);
/* 539 */     float height = Math.max(mc.method_22683().method_4502(), 1);
/* 540 */     b.method_22912(0.0F, 0.0F, 0.0F).method_22913(0.0F, 1.0F).method_22915(1.0F, 1.0F, 1.0F, 1.0F);
/* 541 */     b.method_22912(0.0F, height, 0.0F).method_22913(0.0F, 0.0F).method_22915(1.0F, 1.0F, 1.0F, 1.0F);
/* 542 */     b.method_22912(width, height, 0.0F).method_22913(1.0F, 0.0F).method_22915(1.0F, 1.0F, 1.0F, 1.0F);
/* 543 */     b.method_22912(width, 0.0F, 0.0F).method_22913(1.0F, 1.0F).method_22915(1.0F, 1.0F, 1.0F, 1.0F);
/* 544 */     class_286.method_43433(b.method_60800());
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\BlockOverlay.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */