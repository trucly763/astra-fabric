/*     */ package shame.astra.client.modules.impl.render;
/*     */ 
/*     */ import com.mojang.blaze3d.platform.GlStateManager;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.class_1297;
/*     */ import net.minecraft.class_276;
/*     */ import net.minecraft.class_284;
/*     */ import net.minecraft.class_286;
/*     */ import net.minecraft.class_287;
/*     */ import net.minecraft.class_289;
/*     */ import net.minecraft.class_290;
/*     */ import net.minecraft.class_293;
/*     */ import net.minecraft.class_5944;
/*     */ import net.minecraft.class_6367;
/*     */ import net.minecraft.class_761;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.lwjgl.opengl.GL30;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.EventRender;
/*     */ import shame.astra.api.events.implement.EventUpdate;
/*     */ import shame.astra.api.utils.color.ColorUtils;
/*     */ import shame.astra.api.utils.render.ShaderUtils;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*     */ import shame.astra.client.modules.settings.implement.ListSetting;
/*     */ import shame.astra.mixin.WorldRendererAccessor;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ShaderEsp
/*     */   extends Module
/*     */ {
/*  36 */   public static ShaderEsp INSTANCE = new ShaderEsp();
/*     */   
/*     */   private static final float EPSILON = 0.001F;
/*     */   private static final long OUTLINE_RETRY_DELAY_MS = 3000L;
/*     */   private static final double MAX_RANGE = 256.0D;
/*     */   private static final float FILL_ALPHA = 0.7F;
/*     */   private static final int FILL_MIN_ITERATIONS = 2;
/*     */   private static final float GLOW_VALUE = 0.55F;
/*     */   private static final float WIDTH_VALUE = 0.9F;
/*  45 */   private final ListSetting targets = new ListSetting("Цели", new BooleanSetting[] { new BooleanSetting("Игроки", true), new BooleanSetting("Кристаллы", true), new BooleanSetting("Предметы", false), new BooleanSetting("Себя", false) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   private final BooleanSetting fill = new BooleanSetting("Заливка", false);
/*     */   
/*  54 */   private final List<class_276> bloomBuffers = new ArrayList<>();
/*     */   private class_276 depthCopyBuffer;
/*  56 */   private int bloomWidth = -1;
/*  57 */   private int bloomHeight = -1;
/*     */   private boolean outlineReady;
/*     */   private boolean hasOutlineTargetsCached;
/*     */   private long nextOutlineRetryAt;
/*     */   
/*     */   public ShaderEsp() {
/*  63 */     super("ShaderESP", "Красивая обводка энтити", Module.ModuleCategory.RENDER);
/*  64 */     addSettings(new Setting[] { (Setting)this.targets, (Setting)this.fill });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  69 */     super.onEnable();
/*  70 */     this.outlineReady = false;
/*  71 */     this.nextOutlineRetryAt = 0L;
/*  72 */     tryEnsureOutlineProcessor();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  77 */     for (class_276 fb : this.bloomBuffers) {
/*  78 */       fb.method_1238();
/*     */     }
/*  80 */     this.bloomBuffers.clear();
/*  81 */     if (this.depthCopyBuffer != null) {
/*  82 */       this.depthCopyBuffer.method_1238();
/*  83 */       this.depthCopyBuffer = null;
/*     */     } 
/*  85 */     this.bloomWidth = -1;
/*  86 */     this.bloomHeight = -1;
/*  87 */     this.outlineReady = false;
/*  88 */     this.hasOutlineTargetsCached = false;
/*  89 */     this.nextOutlineRetryAt = 0L;
/*  90 */     super.onDisable();
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onUpdate(EventUpdate event) {
/*  95 */     if (!isEnable())
/*  96 */       return;  if (mc.field_1687 == null || mc.field_1769 == null) {
/*  97 */       this.outlineReady = false;
/*  98 */       this.hasOutlineTargetsCached = false;
/*     */       return;
/*     */     } 
/* 101 */     this.hasOutlineTargetsCached = hasOutlineTargets();
/* 102 */     if (!this.hasOutlineTargetsCached) {
/* 103 */       this.outlineReady = false;
/*     */       return;
/*     */     } 
/* 106 */     if (!this.outlineReady && System.currentTimeMillis() >= this.nextOutlineRetryAt) {
/* 107 */       tryEnsureOutlineProcessor();
/*     */     }
/*     */   }
/*     */   
/*     */   @EventLink(priority = 200)
/*     */   public void onRender2D(EventRender.Default event) {
/* 113 */     if (!isEnable() || mc.field_1687 == null || mc.field_1724 == null || mc.field_1769 == null)
/* 114 */       return;  boolean hasGlow = true;
/* 115 */     boolean hasFill = this.fill.isState();
/* 116 */     if (!hasGlow && !hasFill)
/* 117 */       return;  if (!this.hasOutlineTargetsCached)
/* 118 */       return;  if (!tryEnsureOutlineProcessor())
/*     */       return; 
/* 120 */     class_276 outlineBuffer = getOutlineSourceFramebuffer();
/* 121 */     if (outlineBuffer == null || outlineBuffer.method_30277() == 0)
/*     */       return; 
/* 123 */     class_276 mainBuffer = mc.method_1522();
/*     */     
/* 125 */     ensureDepthCopyBuffer(mainBuffer.field_1482, mainBuffer.field_1481);
/*     */     
/* 127 */     int iterations = Math.max(1, Math.min(8, (int)Math.ceil(1.125D)));
/* 128 */     int fillTexture = 0;
/* 129 */     if (hasFill) {
/* 130 */       int fillIterations = Math.max(2, Math.min(6, iterations + 1));
/* 131 */       fillTexture = runKawaseBloom(outlineBuffer.method_30277(), fillIterations);
/*     */     } 
/*     */ 
/*     */     
/* 135 */     int blurredTexture = hasGlow ? runKawaseBloom(outlineBuffer.method_30277(), iterations) : fillTexture;
/* 136 */     int color = getOutlineColor();
/*     */     
/* 138 */     mainBuffer.method_1235(false);
/* 139 */     RenderSystem.enableBlend();
/* 140 */     RenderSystem.disableDepthTest();
/* 141 */     RenderSystem.colorMask(true, true, true, false);
/*     */     
/* 143 */     if (hasFill) {
/* 144 */       class_5944 fillShader = mc.method_62887().method_62947(ShaderUtils.shaderEspFill);
/* 145 */       if (fillShader != null) {
/* 146 */         RenderSystem.blendFuncSeparate(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA, GlStateManager.class_4535.ZERO, GlStateManager.class_4534.ONE);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 152 */         RenderSystem.setShader(ShaderUtils.shaderEspFill);
/* 153 */         RenderSystem.setShaderTexture(0, outlineBuffer.method_30277());
/* 154 */         RenderSystem.setShaderTexture(1, (fillTexture == 0) ? blurredTexture : fillTexture);
/* 155 */         setUniform(fillShader, "color", ColorUtils.redf(color), ColorUtils.greenf(color), ColorUtils.bluef(color));
/* 156 */         setUniform(fillShader, "alpha", 0.7F);
/* 157 */         setUniform(fillShader, "time", (float)(System.currentTimeMillis() % 100000L) / 1000.0F);
/* 158 */         drawFullscreenQuad();
/*     */       } 
/*     */     } 
/*     */     
/* 162 */     if (hasGlow) {
/* 163 */       class_5944 glowShader = mc.method_62887().method_62947(ShaderUtils.shaderEspGlow);
/* 164 */       if (glowShader != null) {
/* 165 */         RenderSystem.blendFuncSeparate(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA, GlStateManager.class_4535.ZERO, GlStateManager.class_4534.ONE);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 171 */         RenderSystem.setShader(ShaderUtils.shaderEspGlow);
/* 172 */         RenderSystem.setShaderTexture(0, blurredTexture);
/* 173 */         RenderSystem.setShaderTexture(1, outlineBuffer.method_30277());
/* 174 */         setUniform(glowShader, "color", ColorUtils.redf(color), ColorUtils.greenf(color), ColorUtils.bluef(color));
/* 175 */         setUniform(glowShader, "color2", ColorUtils.redf(color), ColorUtils.greenf(color), ColorUtils.bluef(color));
/* 176 */         setUniform(glowShader, "exposure", 0.05075F);
/* 177 */         setUniform(glowShader, "time", (float)(System.currentTimeMillis() % 100000L) / 1000.0F);
/* 178 */         setUniform(glowShader, "animate", 1.0F);
/* 179 */         drawFullscreenQuadWithDepthTest(mainBuffer, outlineBuffer);
/*     */       } 
/*     */     } 
/*     */     
/* 183 */     RenderSystem.colorMask(true, true, true, true);
/* 184 */     RenderSystem.disableDepthTest();
/* 185 */     RenderSystem.disableBlend();
/* 186 */     RenderSystem.defaultBlendFunc();
/* 187 */     RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
/* 188 */     RenderSystem.setShaderTexture(0, 0);
/* 189 */     RenderSystem.setShaderTexture(1, 0);
/* 190 */     mainBuffer.method_1235(true);
/*     */   }
/*     */   
/*     */   private void drawFullscreenQuadWithDepthTest(class_276 mainBuffer, class_276 outlineBuffer) {
/* 194 */     if (this.depthCopyBuffer == null) {
/* 195 */       drawFullscreenQuad();
/*     */       
/*     */       return;
/*     */     } 
/* 199 */     GL30.glBindFramebuffer(36008, mainBuffer.field_1476);
/* 200 */     GL30.glBindFramebuffer(36009, this.depthCopyBuffer.field_1476);
/* 201 */     GL30.glBlitFramebuffer(0, 0, mainBuffer.field_1482, mainBuffer.field_1481, 0, 0, this.depthCopyBuffer.field_1482, this.depthCopyBuffer.field_1481, 256, 9728);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 208 */     GL30.glBindFramebuffer(36008, outlineBuffer.field_1476);
/* 209 */     GL30.glBindFramebuffer(36009, mainBuffer.field_1476);
/* 210 */     GL30.glBlitFramebuffer(0, 0, outlineBuffer.field_1482, outlineBuffer.field_1481, 0, 0, mainBuffer.field_1482, mainBuffer.field_1481, 256, 9728);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 217 */     mainBuffer.method_1235(false);
/* 218 */     RenderSystem.enableDepthTest();
/* 219 */     RenderSystem.depthFunc(515);
/* 220 */     RenderSystem.depthMask(false);
/*     */     
/* 222 */     drawFullscreenQuad();
/*     */     
/* 224 */     RenderSystem.depthMask(true);
/* 225 */     RenderSystem.disableDepthTest();
/*     */     
/* 227 */     GL30.glBindFramebuffer(36008, this.depthCopyBuffer.field_1476);
/* 228 */     GL30.glBindFramebuffer(36009, mainBuffer.field_1476);
/* 229 */     GL30.glBlitFramebuffer(0, 0, this.depthCopyBuffer.field_1482, this.depthCopyBuffer.field_1481, 0, 0, mainBuffer.field_1482, mainBuffer.field_1481, 256, 9728);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 236 */     mainBuffer.method_1235(false);
/*     */   }
/*     */   
/*     */   private void ensureDepthCopyBuffer(int width, int height) {
/* 240 */     if (this.depthCopyBuffer != null && (
/* 241 */       this.depthCopyBuffer.field_1482 != width || this.depthCopyBuffer.field_1481 != height)) {
/* 242 */       this.depthCopyBuffer.method_1238();
/* 243 */       this.depthCopyBuffer = null;
/*     */     } 
/*     */     
/* 246 */     if (this.depthCopyBuffer == null) {
/* 247 */       this.depthCopyBuffer = (class_276)new class_6367(width, height, true);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean tryEnsureOutlineProcessor() {
/* 252 */     if (mc.field_1687 == null || mc.field_1769 == null) {
/* 253 */       this.outlineReady = false;
/* 254 */       return false;
/*     */     } 
/* 256 */     class_276 outlines = getOutlineSourceFramebuffer();
/* 257 */     if (outlines != null && outlines.method_30277() != 0) {
/* 258 */       this.outlineReady = true;
/* 259 */       return true;
/*     */     } 
/* 261 */     if (this.outlineReady) {
/* 262 */       this.outlineReady = false;
/*     */     }
/* 264 */     if (System.currentTimeMillis() < this.nextOutlineRetryAt) {
/* 265 */       return false;
/*     */     }
/*     */     try {
/* 268 */       mc.field_1769.method_3296();
/* 269 */       outlines = getOutlineSourceFramebuffer();
/* 270 */       this.outlineReady = (outlines != null && outlines.method_30277() != 0);
/* 271 */       if (!this.outlineReady) {
/* 272 */         this.nextOutlineRetryAt = System.currentTimeMillis() + 3000L;
/*     */       }
/* 274 */       return this.outlineReady;
/* 275 */     } catch (Throwable ignored) {
/* 276 */       this.outlineReady = false;
/* 277 */       this.nextOutlineRetryAt = System.currentTimeMillis() + 3000L;
/* 278 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private class_276 getOutlineSourceFramebuffer() {
/* 283 */     class_761 class_761 = mc.field_1769; if (class_761 instanceof WorldRendererAccessor) { WorldRendererAccessor accessor = (WorldRendererAccessor)class_761;
/* 284 */       class_276 raw = accessor.astra$getEntityOutlineFramebufferRaw();
/* 285 */       if (raw != null && raw.method_30277() != 0) {
/* 286 */         return raw;
/*     */       } }
/*     */     
/* 289 */     return mc.field_1769.method_22990();
/*     */   }
/*     */   
/*     */   public boolean shouldOutline(class_1297 entity) {
/* 293 */     if (!isEnable() || entity == null || mc.field_1724 == null || mc.field_1687 == null) return false; 
/* 294 */     if (!entity.method_5805()) return false; 
/* 295 */     if (entity.method_31481()) return false; 
/* 296 */     if (entity == mc.field_1724 && !this.targets.is("Себя")) return false; 
/* 297 */     if (entity.method_5858((class_1297)mc.field_1724) > 65536.0D) return false;
/*     */     
/* 299 */     if (entity instanceof net.minecraft.class_1657) {
/* 300 */       return this.targets.is("Игроки");
/*     */     }
/* 302 */     if (entity instanceof net.minecraft.class_1511) {
/* 303 */       return this.targets.is("Кристаллы");
/*     */     }
/* 305 */     if (entity instanceof net.minecraft.class_1542) {
/* 306 */       return this.targets.is("Предметы");
/*     */     }
/* 308 */     return false;
/*     */   }
/*     */   
/*     */   private boolean hasOutlineTargets() {
/* 312 */     if (mc.field_1687 == null || mc.field_1724 == null) {
/* 313 */       return false;
/*     */     }
/* 315 */     for (class_1297 entity : mc.field_1687.method_18112()) {
/* 316 */       if (shouldOutline(entity)) {
/* 317 */         return true;
/*     */       }
/*     */     } 
/* 320 */     return false;
/*     */   }
/*     */   
/*     */   public int getOutlineColor() {
/* 324 */     return ColorUtils.setAlphaColor(ColorUtils.getThemeColor(), 255) & 0xFFFFFF;
/*     */   }
/*     */   
/*     */   private int runKawaseBloom(int sourceTexture, int iterations) {
/* 328 */     ensureBloomBuffers(iterations);
/* 329 */     if (this.bloomBuffers.isEmpty()) {
/* 330 */       return sourceTexture;
/*     */     }
/*     */     
/* 333 */     int currentTexture = sourceTexture;
/* 334 */     class_5944 downShader = mc.method_62887().method_62947(ShaderUtils.shaderHandsKawaseDown);
/* 335 */     class_5944 upShader = mc.method_62887().method_62947(ShaderUtils.shaderHandsKawaseUp);
/* 336 */     if (downShader == null || upShader == null) {
/* 337 */       return currentTexture;
/*     */     }
/*     */     int i;
/* 340 */     for (i = 0; i < iterations; i++) {
/* 341 */       class_276 dst = this.bloomBuffers.get(i);
/* 342 */       dst.method_1236(0.0F, 0.0F, 0.0F, 0.0F);
/* 343 */       dst.method_1230();
/* 344 */       dst.method_1235(true);
/*     */       
/* 346 */       RenderSystem.setShader(ShaderUtils.shaderHandsKawaseDown);
/* 347 */       RenderSystem.setShaderTexture(0, currentTexture);
/* 348 */       setHandsKawaseUniforms(downShader, dst.field_1482, dst.field_1481, 1.0F + i);
/* 349 */       drawFullscreenQuad();
/* 350 */       currentTexture = dst.method_30277();
/*     */     } 
/*     */     
/* 353 */     for (i = iterations - 1; i >= 1; i--) {
/* 354 */       class_276 dst = this.bloomBuffers.get(i - 1);
/* 355 */       dst.method_1236(0.0F, 0.0F, 0.0F, 0.0F);
/* 356 */       dst.method_1230();
/* 357 */       dst.method_1235(true);
/*     */       
/* 359 */       RenderSystem.setShader(ShaderUtils.shaderHandsKawaseUp);
/* 360 */       RenderSystem.setShaderTexture(0, currentTexture);
/* 361 */       setHandsKawaseUniforms(upShader, dst.field_1482, dst.field_1481, 1.0F + i);
/* 362 */       setUniform(upShader, "color", 1.0F, 1.0F, 1.0F);
/* 363 */       drawFullscreenQuad();
/* 364 */       currentTexture = dst.method_30277();
/*     */     } 
/*     */     
/* 367 */     mc.method_1522().method_1235(true);
/* 368 */     return currentTexture;
/*     */   }
/*     */   
/*     */   private void ensureBloomBuffers(int iterations) {
/* 372 */     int w = mc.method_22683().method_4489();
/* 373 */     int h = mc.method_22683().method_4506();
/*     */     
/* 375 */     if (this.bloomWidth != w || this.bloomHeight != h) {
/* 376 */       for (class_276 fb : this.bloomBuffers) {
/* 377 */         fb.method_1238();
/*     */       }
/* 379 */       this.bloomBuffers.clear();
/* 380 */       this.bloomWidth = w;
/* 381 */       this.bloomHeight = h;
/*     */     } 
/*     */     
/* 384 */     while (this.bloomBuffers.size() > iterations) {
/* 385 */       int last = this.bloomBuffers.size() - 1;
/* 386 */       ((class_276)this.bloomBuffers.get(last)).method_1238();
/* 387 */       this.bloomBuffers.remove(last);
/*     */     } 
/*     */     
/* 390 */     for (int i = 0; i < iterations; i++) {
/* 391 */       int tw = Math.max(2, w >> i + 1);
/* 392 */       int th = Math.max(2, h >> i + 1);
/* 393 */       if (i >= this.bloomBuffers.size()) {
/* 394 */         class_6367 class_6367 = new class_6367(tw, th, false);
/* 395 */         setLinearFiltering((class_276)class_6367);
/* 396 */         this.bloomBuffers.add(class_6367);
/*     */       }
/*     */       else {
/*     */         
/* 400 */         class_276 fb = this.bloomBuffers.get(i);
/* 401 */         if (fb.field_1482 != tw || fb.field_1481 != th) {
/* 402 */           fb.method_1238();
/* 403 */           class_6367 class_6367 = new class_6367(tw, th, false);
/* 404 */           setLinearFiltering((class_276)class_6367);
/* 405 */           this.bloomBuffers.set(i, class_6367);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private void setLinearFiltering(class_276 fb) {
/* 411 */     RenderSystem.bindTexture(fb.method_30277());
/* 412 */     GL11.glTexParameteri(3553, 10241, 9729);
/* 413 */     GL11.glTexParameteri(3553, 10240, 9729);
/* 414 */     RenderSystem.bindTexture(0);
/*     */   }
/*     */   
/*     */   private void setUniform(class_5944 shader, String name, float value) {
/* 418 */     class_284 uniform = shader.method_34582(name);
/* 419 */     if (uniform != null) uniform.method_1251(value); 
/*     */   }
/*     */   
/*     */   private void setUniform(class_5944 shader, String name, float x, float y) {
/* 423 */     class_284 uniform = shader.method_34582(name);
/* 424 */     if (uniform != null) uniform.method_1255(x, y); 
/*     */   }
/*     */   
/*     */   private void setUniform(class_5944 shader, String name, float x, float y, float z) {
/* 428 */     class_284 uniform = shader.method_34582(name);
/* 429 */     if (uniform != null) uniform.method_1249(x, y, z); 
/*     */   }
/*     */   
/*     */   private void setHandsKawaseUniforms(class_5944 shader, int texWidth, int texHeight, float offset) {
/* 433 */     setUniform(shader, "uSize", Math.max(1, texWidth), Math.max(1, texHeight));
/* 434 */     setUniform(shader, "uOffset", offset, offset);
/* 435 */     setUniform(shader, "uHalfPixel", 0.5F / Math.max(1, texWidth), 0.5F / Math.max(1, texHeight));
/*     */   }
/*     */   
/*     */   private void drawFullscreenQuad() {
/* 439 */     float width = Math.max(mc.method_22683().method_4486(), 1);
/* 440 */     float height = Math.max(mc.method_22683().method_4502(), 1);
/* 441 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
/* 442 */     buffer.method_22912(0.0F, 0.0F, 0.0F).method_22913(0.0F, 1.0F).method_22915(1.0F, 1.0F, 1.0F, 1.0F);
/* 443 */     buffer.method_22912(0.0F, height, 0.0F).method_22913(0.0F, 0.0F).method_22915(1.0F, 1.0F, 1.0F, 1.0F);
/* 444 */     buffer.method_22912(width, height, 0.0F).method_22913(1.0F, 0.0F).method_22915(1.0F, 1.0F, 1.0F, 1.0F);
/* 445 */     buffer.method_22912(width, 0.0F, 0.0F).method_22913(1.0F, 1.0F).method_22915(1.0F, 1.0F, 1.0F, 1.0F);
/* 446 */     class_286.method_43433(buffer.method_60800());
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\ShaderEsp.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */