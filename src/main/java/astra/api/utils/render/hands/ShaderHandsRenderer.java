/*     */ package shame.astra.api.utils.render.hands;
/*     */ 
/*     */ import com.mojang.blaze3d.platform.GlStateManager;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.class_276;
/*     */ import net.minecraft.class_284;
/*     */ import net.minecraft.class_286;
/*     */ import net.minecraft.class_287;
/*     */ import net.minecraft.class_289;
/*     */ import net.minecraft.class_290;
/*     */ import net.minecraft.class_293;
/*     */ import net.minecraft.class_5944;
/*     */ import net.minecraft.class_6367;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.lwjgl.opengl.GL30;
/*     */ import shame.astra.api.QClient;
/*     */ import shame.astra.api.storages.implement.helpertstorages.enumvar.ModuleClass;
/*     */ import shame.astra.api.utils.color.ColorUtils;
/*     */ import shame.astra.api.utils.render.ShaderUtils;
/*     */ import shame.astra.astra;
/*     */ import shame.astra.client.modules.impl.render.ShaderHands;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ShaderHandsRenderer
/*     */   implements QClient
/*     */ {
/*     */   private static final float EPSILON = 0.001F;
/*     */   private static ShaderHandsRenderer instance;
/*     */   private class_276 beforeBuffer;
/*     */   private class_276 afterBuffer;
/*     */   private class_276 maskBuffer;
/*  35 */   private final List<class_276> bloomBuffers = new ArrayList<>();
/*  36 */   private int width = -1;
/*  37 */   private int height = -1;
/*     */   private boolean hasBeforeCapture;
/*     */   private boolean pendingComposite;
/*  40 */   private int configuredBeforeDepthTex = -1;
/*  41 */   private int configuredAfterDepthTex = -1;
/*     */   
/*     */   public static ShaderHandsRenderer getInstance() {
/*  44 */     if (instance == null) instance = new ShaderHandsRenderer(); 
/*  45 */     return instance;
/*     */   }
/*     */   
/*     */   public void captureBeforeHands() {
/*  49 */     ShaderHands module = getModule();
/*  50 */     if (!isEffectEnabled(module)) {
/*  51 */       invalidateState();
/*     */       return;
/*     */     } 
/*  54 */     ensureBuffers();
/*  55 */     if (this.beforeBuffer == null)
/*  56 */       return;  copyMainFramebuffer(this.beforeBuffer);
/*  57 */     this.hasBeforeCapture = true;
/*     */   }
/*     */   
/*     */   public void captureAfterHands() {
/*  61 */     ShaderHands module = getModule();
/*  62 */     if (!isEffectEnabled(module)) {
/*  63 */       invalidateState();
/*     */       return;
/*     */     } 
/*  66 */     ensureBuffers();
/*  67 */     if (this.beforeBuffer == null || this.afterBuffer == null || this.maskBuffer == null)
/*  68 */       return;  if (!this.hasBeforeCapture)
/*     */       return; 
/*  70 */     copyMainFramebuffer(this.afterBuffer);
/*  71 */     this.pendingComposite = true;
/*     */   }
/*     */   
/*     */   public void renderOverlayIfPending() {
/*  75 */     if (!this.pendingComposite)
/*  76 */       return;  ensureBuffers();
/*  77 */     if (this.beforeBuffer == null || this.afterBuffer == null || this.maskBuffer == null)
/*  78 */       return;  ShaderHands module = getModule();
/*  79 */     if (!isEffectEnabled(module)) {
/*  80 */       invalidateState();
/*     */       
/*     */       return;
/*     */     } 
/*  84 */     class_5944 maskShader = mc.method_62887().method_62947(ShaderUtils.shaderHandsMaskDiff);
/*  85 */     if (maskShader == null) {
/*  86 */       invalidateState();
/*     */       return;
/*     */     } 
/*  89 */     this.maskBuffer.method_1236(0.0F, 0.0F, 0.0F, 0.0F);
/*  90 */     this.maskBuffer.method_1230();
/*  91 */     this.maskBuffer.method_1235(false);
/*  92 */     RenderSystem.disableDepthTest();
/*  93 */     RenderSystem.disableBlend();
/*  94 */     RenderSystem.setShader(ShaderUtils.shaderHandsMaskDiff);
/*  95 */     RenderSystem.setShaderTexture(0, this.beforeBuffer.method_30277());
/*  96 */     RenderSystem.setShaderTexture(1, this.afterBuffer.method_30277());
/*  97 */     int beforeDepth = this.beforeBuffer.method_30278();
/*  98 */     int afterDepth = this.afterBuffer.method_30278();
/*  99 */     if (beforeDepth != 0 && beforeDepth != this.configuredBeforeDepthTex) {
/* 100 */       configureDepthTexture(beforeDepth);
/* 101 */       this.configuredBeforeDepthTex = beforeDepth;
/*     */     } 
/* 103 */     if (afterDepth != 0 && afterDepth != this.configuredAfterDepthTex) {
/* 104 */       configureDepthTexture(afterDepth);
/* 105 */       this.configuredAfterDepthTex = afterDepth;
/*     */     } 
/* 107 */     RenderSystem.setShaderTexture(2, beforeDepth);
/* 108 */     RenderSystem.setShaderTexture(3, afterDepth);
/* 109 */     drawFullscreenQuad();
/* 110 */     RenderSystem.enableDepthTest();
/*     */     
/* 112 */     float glowValue = module.glow.get();
/* 113 */     float fillValue = module.fill.get();
/* 114 */     float alphaValue = module.alpha.get();
/* 115 */     float outlineValue = module.outline.get();
/*     */     
/* 117 */     boolean hasGlow = (glowValue > 0.001F);
/* 118 */     boolean hasFill = (fillValue > 0.001F && alphaValue > 0.001F);
/*     */ 
/*     */     
/* 121 */     int color1 = astra.INSTANCE.themeStorage.getThemes().getTheme().getName().equals("Rainbow") ? ColorUtils.getThemeColor(0) : (astra.INSTANCE.themeStorage.getThemes().getTheme()).color[0];
/* 122 */     int color2 = color1;
/*     */     
/* 124 */     if (module.mode.is("Красивый")) {
/* 125 */       renderPrettyMode(module, color1, color2, glowValue, fillValue, alphaValue, outlineValue);
/* 126 */       invalidateState();
/*     */       
/*     */       return;
/*     */     } 
/* 130 */     int blurredMaskTexture = 0;
/* 131 */     if (hasGlow) {
/* 132 */       int iterations = Math.max(3, Math.min(8, 4 + Math.round(outlineValue * 0.7F)));
/* 133 */       blurredMaskTexture = runKawaseBloom(iterations);
/*     */     } 
/*     */     
/* 136 */     mc.method_1522().method_1235(true);
/* 137 */     RenderSystem.enableBlend();
/* 138 */     RenderSystem.colorMask(true, true, true, false);
/* 139 */     RenderSystem.disableDepthTest();
/*     */     
/* 141 */     class_5944 glowShader = hasGlow ? mc.method_62887().method_62947(ShaderUtils.shaderHandsGlow) : null;
/* 142 */     if (glowShader != null) {
/* 143 */       RenderSystem.blendFuncSeparate(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE, GlStateManager.class_4535.ZERO, GlStateManager.class_4534.ONE);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 149 */       RenderSystem.setShader(ShaderUtils.shaderHandsGlow);
/* 150 */       RenderSystem.setShaderTexture(0, blurredMaskTexture);
/* 151 */       RenderSystem.setShaderTexture(1, this.maskBuffer.method_30277());
/* 152 */       setUniform(glowShader, "color", ColorUtils.redf(color1), ColorUtils.greenf(color1), ColorUtils.bluef(color1));
/* 153 */       setUniform(glowShader, "color2", ColorUtils.redf(color2), ColorUtils.greenf(color2), ColorUtils.bluef(color2));
/* 154 */       setUniform(glowShader, "exposure", 1.0F + glowValue * 1.8F);
/* 155 */       drawFullscreenQuad();
/*     */     } 
/*     */     
/* 158 */     if (hasFill) {
/* 159 */       class_5944 overlayShader = mc.method_62887().method_62947(ShaderUtils.shaderHandsOverlay);
/* 160 */       if (overlayShader == null) {
/* 161 */         restoreCompositeState();
/* 162 */         invalidateState();
/*     */         return;
/*     */       } 
/* 165 */       RenderSystem.blendFuncSeparate(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA, GlStateManager.class_4535.ZERO, GlStateManager.class_4534.ONE);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 171 */       RenderSystem.setShader(ShaderUtils.shaderHandsOverlay);
/* 172 */       RenderSystem.setShaderTexture(0, this.maskBuffer.method_30277());
/* 173 */       setUniform(overlayShader, "color", ColorUtils.redf(color1), ColorUtils.greenf(color1), ColorUtils.bluef(color1));
/* 174 */       setUniform(overlayShader, "fill", fillValue);
/* 175 */       setUniform(overlayShader, "alpha", alphaValue);
/* 176 */       drawFullscreenQuad();
/*     */     } 
/*     */     
/* 179 */     restoreCompositeState();
/* 180 */     invalidateState();
/*     */   }
/*     */   
/*     */   public void invalidateState() {
/* 184 */     this.hasBeforeCapture = false;
/* 185 */     this.pendingComposite = false;
/* 186 */     this.configuredBeforeDepthTex = -1;
/* 187 */     this.configuredAfterDepthTex = -1;
/*     */   }
/*     */   
/*     */   private int runKawaseBloom(int iterations) {
/* 191 */     ensureBloomBuffers(iterations);
/* 192 */     if (this.bloomBuffers.isEmpty()) {
/* 193 */       return this.maskBuffer.method_30277();
/*     */     }
/*     */     
/* 196 */     int currentTexture = this.maskBuffer.method_30277();
/* 197 */     class_5944 downShader = mc.method_62887().method_62947(ShaderUtils.shaderHandsKawaseDown);
/* 198 */     class_5944 upShader = mc.method_62887().method_62947(ShaderUtils.shaderHandsKawaseUp);
/* 199 */     if (downShader == null || upShader == null) {
/* 200 */       return currentTexture;
/*     */     }
/*     */     int i;
/* 203 */     for (i = 0; i < iterations; i++) {
/* 204 */       class_276 dst = this.bloomBuffers.get(i);
/* 205 */       dst.method_1236(0.0F, 0.0F, 0.0F, 0.0F);
/* 206 */       dst.method_1230();
/* 207 */       dst.method_1235(true);
/*     */       
/* 209 */       RenderSystem.setShader(ShaderUtils.shaderHandsKawaseDown);
/* 210 */       RenderSystem.setShaderTexture(0, currentTexture);
/* 211 */       setHandsKawaseUniforms(downShader, dst.field_1482, dst.field_1481, 1.0F + i);
/* 212 */       drawFullscreenQuad();
/*     */       
/* 214 */       currentTexture = dst.method_30277();
/*     */     } 
/*     */     
/* 217 */     for (i = iterations - 1; i >= 1; i--) {
/* 218 */       class_276 dst = this.bloomBuffers.get(i - 1);
/* 219 */       dst.method_1236(0.0F, 0.0F, 0.0F, 0.0F);
/* 220 */       dst.method_1230();
/* 221 */       dst.method_1235(true);
/*     */       
/* 223 */       RenderSystem.setShader(ShaderUtils.shaderHandsKawaseUp);
/* 224 */       RenderSystem.setShaderTexture(0, currentTexture);
/* 225 */       setHandsKawaseUniforms(upShader, dst.field_1482, dst.field_1481, 1.0F + i);
/* 226 */       setUniform(upShader, "color", 1.0F, 1.0F, 1.0F);
/* 227 */       drawFullscreenQuad();
/*     */       
/* 229 */       currentTexture = dst.method_30277();
/*     */     } 
/*     */     
/* 232 */     mc.method_1522().method_1235(true);
/* 233 */     return currentTexture;
/*     */   }
/*     */   
/*     */   private void copyMainFramebuffer(class_276 target) {
/* 237 */     int readFbo = GL11.glGetInteger(36010);
/* 238 */     int drawFbo = GL11.glGetInteger(36006);
/*     */     
/* 240 */     GL30.glBindFramebuffer(36008, (mc.method_1522()).field_1476);
/* 241 */     GL30.glBindFramebuffer(36009, target.field_1476);
/*     */     
/* 243 */     GL30.glBlitFramebuffer(0, 0, this.width, this.height, 0, 0, this.width, this.height, 16640, 9728);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 250 */     GL30.glBindFramebuffer(36008, readFbo);
/* 251 */     GL30.glBindFramebuffer(36009, drawFbo);
/* 252 */     mc.method_1522().method_1235(true);
/*     */   }
/*     */   
/*     */   private void configureDepthTexture(int depthTex) {
/* 256 */     RenderSystem.bindTexture(depthTex);
/* 257 */     GL11.glTexParameteri(3553, 34892, 0);
/* 258 */     GL11.glTexParameteri(3553, 10241, 9728);
/* 259 */     GL11.glTexParameteri(3553, 10240, 9728);
/* 260 */     RenderSystem.bindTexture(0);
/*     */   }
/*     */   
/*     */   private void ensureBuffers() {
/* 264 */     int w = mc.method_22683().method_4489();
/* 265 */     int h = mc.method_22683().method_4506();
/* 266 */     if (w == this.width && h == this.height && this.beforeBuffer != null && this.afterBuffer != null && this.maskBuffer != null)
/*     */       return; 
/* 268 */     if (this.beforeBuffer != null) this.beforeBuffer.method_1238(); 
/* 269 */     if (this.afterBuffer != null) this.afterBuffer.method_1238(); 
/* 270 */     if (this.maskBuffer != null) this.maskBuffer.method_1238(); 
/* 271 */     for (class_276 fb : this.bloomBuffers) {
/* 272 */       fb.method_1238();
/*     */     }
/* 274 */     this.bloomBuffers.clear();
/*     */     
/* 276 */     this.beforeBuffer = (class_276)new class_6367(w, h, true);
/* 277 */     this.afterBuffer = (class_276)new class_6367(w, h, true);
/* 278 */     this.maskBuffer = (class_276)new class_6367(w, h, true);
/* 279 */     this.width = w;
/* 280 */     this.height = h;
/* 281 */     this.configuredBeforeDepthTex = -1;
/* 282 */     this.configuredAfterDepthTex = -1;
/*     */   }
/*     */   
/*     */   private void ensureBloomBuffers(int iterations) {
/* 286 */     while (this.bloomBuffers.size() > iterations) {
/* 287 */       int last = this.bloomBuffers.size() - 1;
/* 288 */       ((class_276)this.bloomBuffers.get(last)).method_1238();
/* 289 */       this.bloomBuffers.remove(last);
/*     */     } 
/*     */     
/* 292 */     for (int i = 0; i < iterations; i++) {
/* 293 */       int w = Math.max(2, this.width >> i + 1);
/* 294 */       int h = Math.max(2, this.height >> i + 1);
/*     */       
/* 296 */       if (i >= this.bloomBuffers.size()) {
/* 297 */         class_6367 class_6367 = new class_6367(w, h, false);
/* 298 */         setLinearFiltering((class_276)class_6367);
/* 299 */         this.bloomBuffers.add(class_6367);
/*     */       }
/*     */       else {
/*     */         
/* 303 */         class_276 fb = this.bloomBuffers.get(i);
/* 304 */         if (fb.field_1482 != w || fb.field_1481 != h) {
/* 305 */           fb.method_1238();
/* 306 */           class_6367 class_6367 = new class_6367(w, h, false);
/* 307 */           setLinearFiltering((class_276)class_6367);
/* 308 */           this.bloomBuffers.set(i, class_6367);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private void setLinearFiltering(class_276 fb) {
/* 314 */     RenderSystem.bindTexture(fb.method_30277());
/* 315 */     GL11.glTexParameteri(3553, 10241, 9729);
/* 316 */     GL11.glTexParameteri(3553, 10240, 9729);
/* 317 */     RenderSystem.bindTexture(0);
/*     */   }
/*     */   
/*     */   private ShaderHands getModule() {
/* 321 */     if (astra.INSTANCE == null || ModuleClass.INSTANCE == null) return null; 
/* 322 */     return ModuleClass.shaderHands;
/*     */   }
/*     */   
/*     */   private void renderPrettyMode(ShaderHands module, int color1, int color2, float glowValue, float fillValue, float alphaValue, float outlineValue) {
/* 326 */     class_5944 shader = mc.method_62887().method_62947(ShaderUtils.blockOverlay);
/* 327 */     if (shader == null)
/*     */       return; 
/* 329 */     mc.method_1522().method_1235(false);
/* 330 */     RenderSystem.enableBlend();
/* 331 */     RenderSystem.defaultBlendFunc();
/* 332 */     RenderSystem.disableDepthTest();
/*     */     
/* 334 */     RenderSystem.setShader(ShaderUtils.blockOverlay);
/* 335 */     RenderSystem.setShaderTexture(0, this.maskBuffer.method_30277());
/*     */     
/* 337 */     setUniform(shader, "texelSize", 1.0F / 
/* 338 */         Math.max(1, mc.method_22683().method_4489()), 1.0F / 
/* 339 */         Math.max(1, mc.method_22683().method_4506()));
/* 340 */     setUniform(shader, "color", ColorUtils.redf(color1), ColorUtils.greenf(color1), ColorUtils.bluef(color1));
/* 341 */     setUniform(shader, "color2", ColorUtils.redf(color2), ColorUtils.greenf(color2), ColorUtils.bluef(color2));
/* 342 */     setUniform(shader, "time", (float)(System.currentTimeMillis() % 100000L) / 1000.0F);
/* 343 */     setUniform(shader, "speed", module.waveSpeed.get());
/* 344 */     setUniform(shader, "scale", module.waveScale.get());
/* 345 */     setUniform(shader, "outline", outlineValue);
/* 346 */     setUniform(shader, "glow", glowValue);
/* 347 */     setUniform(shader, "fill", fillValue);
/* 348 */     setUniform(shader, "alpha", alphaValue);
/* 349 */     setUniform(shader, "outlineOnly", 0.0F);
/* 350 */     drawFullscreenQuad();
/*     */     
/* 352 */     RenderSystem.enableDepthTest();
/* 353 */     RenderSystem.disableBlend();
/* 354 */     RenderSystem.defaultBlendFunc();
/* 355 */     restoreCompositeState();
/*     */   }
/*     */   
/*     */   private void restoreCompositeState() {
/* 359 */     RenderSystem.colorMask(true, true, true, true);
/* 360 */     RenderSystem.depthMask(true);
/* 361 */     RenderSystem.enableDepthTest();
/* 362 */     RenderSystem.enableCull();
/* 363 */     RenderSystem.disableBlend();
/* 364 */     RenderSystem.defaultBlendFunc();
/* 365 */     RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
/* 366 */     RenderSystem.setShaderTexture(0, 0);
/* 367 */     RenderSystem.setShaderTexture(1, 0);
/* 368 */     RenderSystem.setShaderTexture(2, 0);
/* 369 */     RenderSystem.setShaderTexture(3, 0);
/* 370 */     mc.method_1522().method_1235(true);
/*     */   }
/*     */   
/*     */   private boolean isEffectEnabled(ShaderHands module) {
/* 374 */     if (module == null || !module.isEnable()) return false; 
/* 375 */     boolean hasGlow = (module.glow.get() > 0.001F);
/* 376 */     boolean hasFill = (module.fill.get() > 0.001F && module.alpha.get() > 0.001F);
/* 377 */     return (hasGlow || hasFill);
/*     */   }
/*     */   
/*     */   private void setUniform(class_5944 shader, String name, float v) {
/* 381 */     class_284 u = shader.method_34582(name);
/* 382 */     if (u != null) u.method_1251(v); 
/*     */   }
/*     */   
/*     */   private void setUniform(class_5944 shader, String name, float x, float y) {
/* 386 */     class_284 u = shader.method_34582(name);
/* 387 */     if (u != null) u.method_1255(x, y); 
/*     */   }
/*     */   
/*     */   private void setUniform(class_5944 shader, String name, float x, float y, float z) {
/* 391 */     class_284 u = shader.method_34582(name);
/* 392 */     if (u != null) u.method_1249(x, y, z); 
/*     */   }
/*     */   
/*     */   private void setHandsKawaseUniforms(class_5944 shader, int texWidth, int texHeight, float offset) {
/* 396 */     setUniform(shader, "uSize", Math.max(1, texWidth), Math.max(1, texHeight));
/* 397 */     setUniform(shader, "uOffset", offset, offset);
/* 398 */     setUniform(shader, "uHalfPixel", 0.5F / Math.max(1, texWidth), 0.5F / Math.max(1, texHeight));
/*     */   }
/*     */   
/*     */   private void drawFullscreenQuad() {
/* 402 */     float sw = Math.max(mc.method_22683().method_4486(), 1);
/* 403 */     float sh = Math.max(mc.method_22683().method_4502(), 1);
/* 404 */     class_287 b = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
/* 405 */     b.method_22912(0.0F, 0.0F, 0.0F).method_22913(0.0F, 1.0F).method_22915(1.0F, 1.0F, 1.0F, 1.0F);
/* 406 */     b.method_22912(0.0F, sh, 0.0F).method_22913(0.0F, 0.0F).method_22915(1.0F, 1.0F, 1.0F, 1.0F);
/* 407 */     b.method_22912(sw, sh, 0.0F).method_22913(1.0F, 0.0F).method_22915(1.0F, 1.0F, 1.0F, 1.0F);
/* 408 */     b.method_22912(sw, 0.0F, 0.0F).method_22913(1.0F, 1.0F).method_22915(1.0F, 1.0F, 1.0F, 1.0F);
/* 409 */     class_286.method_43433(b.method_60800());
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\render\hands\ShaderHandsRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */