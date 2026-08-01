/*     */ package shame.astra.client.modules.impl.render;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_276;
/*     */ import net.minecraft.class_284;
/*     */ import net.minecraft.class_286;
/*     */ import net.minecraft.class_287;
/*     */ import net.minecraft.class_289;
/*     */ import net.minecraft.class_290;
/*     */ import net.minecraft.class_293;
/*     */ import net.minecraft.class_3532;
/*     */ import net.minecraft.class_5944;
/*     */ import net.minecraft.class_6367;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.EventChunkReload;
/*     */ import shame.astra.api.utils.animation.Easings;
/*     */ import shame.astra.api.utils.color.ColorUtils;
/*     */ import shame.astra.api.utils.render.ShaderUtils;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*     */ 
/*     */ public class Sonar extends Module {
/*  27 */   public static Sonar INSTANCE = new Sonar();
/*     */   
/*  29 */   private final FloatSetting duration = new FloatSetting("Длительность", 5.6F, 0.8F, 10.0F, 0.1F);
/*  30 */   private final FloatSetting alpha = new FloatSetting("Яркость", 1.0F, 0.1F, 1.0F, 0.01F);
/*  31 */   private final FloatSetting widthMul = new FloatSetting("Ширина", 1.0F, 0.35F, 2.2F, 0.05F);
/*  32 */   private final FloatSetting sharpness = new FloatSetting("Резкость", 24.0F, 4.0F, 80.0F, 1.0F);
/*     */   
/*     */   private class_276 depthCopyBuffer;
/*  35 */   private int lastFbWidth = -1;
/*  36 */   private int lastFbHeight = -1;
/*     */   
/*     */   private long currentStart;
/*  39 */   private class_243 center = class_243.field_1353;
/*     */   
/*     */   public Sonar() {
/*  42 */     super("Sonar", "Сканирует новые чанки", Module.ModuleCategory.RENDER);
/*  43 */     addSettings(new Setting[] { (Setting)this.duration, (Setting)this.alpha, (Setting)this.widthMul, (Setting)this.sharpness });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  48 */     if (mc.field_1724 != null) {
/*  49 */       ping(mc.field_1724.method_19538());
/*     */     }
/*  51 */     super.onEnable();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  56 */     this.currentStart = 0L;
/*  57 */     deleteDepthCopyFramebuffer();
/*  58 */     super.onDisable();
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onChunkReload(EventChunkReload event) {
/*  63 */     if (mc.field_1724 != null) {
/*  64 */       ping(mc.field_1724.method_19538());
/*     */     }
/*     */   }
/*     */   
/*     */   public void renderFromMixin(Matrix4f positionMatrix, Matrix4f projectionMatrix, class_243 camPos) {
/*  69 */     if (mc.field_1724 == null || mc.field_1687 == null || this.currentStart <= 0L) {
/*     */       return;
/*     */     }
/*     */     
/*  73 */     float durationMs = this.duration.get() * 1000.0F;
/*  74 */     float elapsed = (float)(System.currentTimeMillis() - this.currentStart);
/*  75 */     if (elapsed >= durationMs) {
/*  76 */       this.currentStart = 0L;
/*     */       
/*     */       return;
/*     */     } 
/*  80 */     class_276 framebuffer = mc.method_1522();
/*  81 */     ensureDepthCopyFramebuffer(framebuffer.field_1482, framebuffer.field_1481);
/*  82 */     if (this.depthCopyBuffer == null) {
/*     */       return;
/*     */     }
/*  85 */     this.depthCopyBuffer.method_29329(framebuffer);
/*     */     
/*  87 */     Matrix4f invView = (new Matrix4f((Matrix4fc)positionMatrix)).invert();
/*  88 */     Matrix4f invProj = (new Matrix4f((Matrix4fc)projectionMatrix)).invert();
/*     */     
/*  90 */     float far = mc.field_1773.method_32796();
/*  91 */     float t = class_3532.method_15363(elapsed / durationMs, 0.0F, 1.0F);
/*  92 */     float r1 = lerp(1.0F, far, (float)Easings.QUINT_OUT.ease(t));
/*  93 */     float r2 = lerp(1.0F, far, (float)Easings.QUART_IN_OUT.ease(t));
/*  94 */     float baseRadius = class_3532.method_16439(0.85F, r1, r2);
/*     */     
/*  96 */     float alphaPc = 1.0F - t;
/*  97 */     float alphaWave = ((alphaPc > 0.5F) ? (1.0F - alphaPc) : alphaPc) * 2.0F;
/*  98 */     alphaWave = Math.min(alphaWave * 1.75F, 1.0F);
/*  99 */     float baseAlpha = class_3532.method_15363(this.alpha.get() * alphaWave, 0.0F, 1.0F);
/*     */     
/* 101 */     int c1 = ColorUtils.getThemeColor(0);
/* 102 */     int c2 = ColorUtils.getThemeColor(90);
/* 103 */     int c3 = ColorUtils.getThemeColor(180);
/* 104 */     int c4 = ColorUtils.getThemeColor(270);
/*     */     
/* 106 */     float baseWidth = class_3532.method_15363(6.0F + baseRadius * 0.18F * this.widthMul.get(), 4.0F, Math.max(10.0F, far * 0.42F));
/* 107 */     float baseSharp = this.sharpness.get();
/*     */     
/* 109 */     renderPass(invView, invProj, camPos, framebuffer, baseRadius, baseWidth, baseSharp, 
/*     */ 
/*     */ 
/*     */         
/* 113 */         applyAlpha(c1, baseAlpha), 
/* 114 */         applyAlpha(c2, baseAlpha), 
/* 115 */         applyAlpha(c3, baseAlpha), 
/* 116 */         applyAlpha(c4, baseAlpha));
/*     */     
/* 118 */     RenderSystem.defaultBlendFunc();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void renderPass(Matrix4f invView, Matrix4f invProj, class_243 camPos, class_276 framebuffer, float radius, float width, float sharp, int outerColor, int midColor, int innerColor, int scanlineColor) {
/* 125 */     if (radius <= 0.001F || width <= 0.001F) {
/*     */       return;
/*     */     }
/*     */     
/* 129 */     class_5944 shader = mc.method_62887().method_62947(ShaderUtils.scanEffect);
/*     */     
/* 131 */     class_284 invViewUniform = shader.method_34582("invViewMat");
/* 132 */     class_284 invProjUniform = shader.method_34582("invProjMat");
/* 133 */     class_284 posUniform = shader.method_34582("pos");
/* 134 */     class_284 centerUniform = shader.method_34582("center");
/* 135 */     class_284 radiusUniform = shader.method_34582("radius");
/* 136 */     class_284 widthUniform = shader.method_34582("width");
/* 137 */     class_284 sharpnessUniform = shader.method_34582("sharpness");
/* 138 */     class_284 outerColorUniform = shader.method_34582("outerColor");
/* 139 */     class_284 midColorUniform = shader.method_34582("midColor");
/* 140 */     class_284 innerColorUniform = shader.method_34582("innerColor");
/* 141 */     class_284 scanlineColorUniform = shader.method_34582("scanlineColor");
/* 142 */     class_284 debugModeUniform = shader.method_34582("DebugMode");
/*     */     
/* 144 */     if (invViewUniform != null) invViewUniform.method_1250(invView); 
/* 145 */     if (invProjUniform != null) invProjUniform.method_1250(invProj); 
/* 146 */     if (posUniform != null) posUniform.method_1249((float)camPos.field_1352, (float)camPos.field_1351, (float)camPos.field_1350); 
/* 147 */     if (centerUniform != null) centerUniform.method_1249((float)this.center.field_1352, (float)this.center.field_1351, (float)this.center.field_1350); 
/* 148 */     if (radiusUniform != null) radiusUniform.method_1251(radius); 
/* 149 */     if (widthUniform != null) widthUniform.method_1251(width); 
/* 150 */     if (sharpnessUniform != null) sharpnessUniform.method_1251(sharp); 
/* 151 */     if (outerColorUniform != null) setColor(outerColorUniform, outerColor); 
/* 152 */     if (midColorUniform != null) setColor(midColorUniform, midColor); 
/* 153 */     if (innerColorUniform != null) setColor(innerColorUniform, innerColor); 
/* 154 */     if (scanlineColorUniform != null) setColor(scanlineColorUniform, scanlineColor); 
/* 155 */     if (debugModeUniform != null) debugModeUniform.method_35649(0);
/*     */     
/* 157 */     RenderSystem.enableBlend();
/* 158 */     RenderSystem.blendFunc(770, 1);
/* 159 */     RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
/* 160 */     RenderSystem.disableDepthTest();
/* 161 */     RenderSystem.disableCull();
/* 162 */     RenderSystem.depthMask(false);
/*     */     
/* 164 */     int depthTex = this.depthCopyBuffer.method_30278();
/* 165 */     if (depthTex == 0) {
/* 166 */       depthTex = mc.method_1522().method_30278();
/*     */     }
/* 168 */     RenderSystem.bindTexture(depthTex);
/* 169 */     GL11.glTexParameteri(3553, 34892, 0);
/* 170 */     GL11.glTexParameteri(3553, 10241, 9728);
/* 171 */     GL11.glTexParameteri(3553, 10240, 9728);
/*     */     
/* 173 */     framebuffer.method_1235(false);
/* 174 */     RenderSystem.setShaderTexture(0, depthTex);
/* 175 */     RenderSystem.setShader(ShaderUtils.scanEffect);
/* 176 */     drawFullscreenQuad();
/*     */     
/* 178 */     RenderSystem.depthMask(true);
/* 179 */     RenderSystem.enableCull();
/* 180 */     RenderSystem.enableDepthTest();
/* 181 */     RenderSystem.disableBlend();
/*     */   }
/*     */   
/*     */   private void drawFullscreenQuad() {
/* 185 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1585);
/* 186 */     buffer.method_22912(-1.0F, -1.0F, 0.0F).method_22913(0.0F, 0.0F);
/* 187 */     buffer.method_22912(-1.0F, 1.0F, 0.0F).method_22913(0.0F, 1.0F);
/* 188 */     buffer.method_22912(1.0F, 1.0F, 0.0F).method_22913(1.0F, 1.0F);
/* 189 */     buffer.method_22912(1.0F, -1.0F, 0.0F).method_22913(1.0F, 0.0F);
/* 190 */     class_286.method_43433(buffer.method_60800());
/*     */   }
/*     */   
/*     */   private void ensureDepthCopyFramebuffer(int width, int height) {
/* 194 */     if (this.depthCopyBuffer == null || this.lastFbWidth != width || this.lastFbHeight != height) {
/* 195 */       deleteDepthCopyFramebuffer();
/* 196 */       this.depthCopyBuffer = (class_276)new class_6367(width, height, true);
/* 197 */       this.lastFbWidth = width;
/* 198 */       this.lastFbHeight = height;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void deleteDepthCopyFramebuffer() {
/* 203 */     if (this.depthCopyBuffer != null) {
/* 204 */       this.depthCopyBuffer.method_1238();
/* 205 */       this.depthCopyBuffer = null;
/*     */     } 
/* 207 */     this.lastFbWidth = -1;
/* 208 */     this.lastFbHeight = -1;
/*     */   }
/*     */   
/*     */   private void ping(class_243 pos) {
/* 212 */     this.currentStart = System.currentTimeMillis();
/* 213 */     this.center = pos;
/*     */   }
/*     */   
/*     */   private void setColor(class_284 uniform, int color) {
/* 217 */     int a = color >> 24 & 0xFF;
/* 218 */     int r = color >> 16 & 0xFF;
/* 219 */     int g = color >> 8 & 0xFF;
/* 220 */     int b = color & 0xFF;
/* 221 */     if (a == 0) a = 255; 
/* 222 */     uniform.method_35657(r / 255.0F, g / 255.0F, b / 255.0F, a / 255.0F);
/*     */   }
/*     */   
/*     */   private int applyAlpha(int color, float alphaMul) {
/* 226 */     int a = color >> 24 & 0xFF;
/* 227 */     if (a == 0) a = 255; 
/* 228 */     a = (int)(a * class_3532.method_15363(alphaMul, 0.0F, 1.0F));
/* 229 */     return color & 0xFFFFFF | a << 24;
/*     */   }
/*     */   
/*     */   private float lerp(float a, float b, float t) {
/* 233 */     return a + (b - a) * t;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\Sonar.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */