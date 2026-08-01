/*     */ package shame.astra.api.utils.render.fonts.msdf;
/*     */ 
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import net.minecraft.class_284;
/*     */ import net.minecraft.class_286;
/*     */ import net.minecraft.class_287;
/*     */ import net.minecraft.class_289;
/*     */ import net.minecraft.class_290;
/*     */ import net.minecraft.class_293;
/*     */ import net.minecraft.class_4587;
/*     */ import net.minecraft.class_4588;
/*     */ import net.minecraft.class_5944;
/*     */ import org.joml.Matrix4f;
/*     */ import shame.astra.api.QClient;
/*     */ import shame.astra.api.utils.render.ShaderUtils;
/*     */ 
/*     */ public class Font implements QClient {
/*     */   private static final char FORMATTING_CODE_PREFIX = '§';
/*     */   private final MsdfFont font;
/*     */   private final float size;
/*     */   
/*     */   public Font(MsdfFont font, float size) {
/*  23 */     this.font = font;
/*  24 */     this.size = size;
/*     */   }
/*     */   
/*     */   public Font(String name, float size) {
/*  28 */     this.font = MsdfFont.builder().atlas(name).data(name).build();
/*  29 */     this.size = size;
/*     */   }
/*     */   
/*     */   public void drawString(class_4587 matrixStack, String text, double x, double y, int color) {
/*  33 */     draw(matrixStack, text, (float)x, (float)y, color);
/*     */   }
/*     */   
/*     */   public void drawString(class_4587 matrixStack, String text, float x, float y, int color) {
/*  37 */     draw(matrixStack, text, x, y, color);
/*     */   }
/*     */   
/*     */   public void drawString(String text, float x, float y, int color) {
/*  41 */     class_4587 stack = new class_4587();
/*  42 */     draw(stack, text, x, y, color);
/*     */   }
/*     */   
/*     */   public void drawCenteredString(class_4587 matrixStack, String text, double x, double y, int color) {
/*  46 */     draw(matrixStack, text, (float)(x - getStringWidth(text) / 2.0D), (float)y, color);
/*     */   }
/*     */   
/*     */   public void drawCenteredString(class_4587 matrixStack, String text, float x, float y, int color) {
/*  50 */     draw(matrixStack, text, x - getStringWidth(text) / 2.0F, y, color);
/*     */   }
/*     */   
/*     */   public void drawRight(class_4587 matrixStack, String text, double x, double y, int color) {
/*  54 */     draw(matrixStack, text, (float)(x - getStringWidth(text)), (float)y, color);
/*     */   }
/*     */   
/*     */   public void drawRight(class_4587 matrixStack, String text, float x, float y, int color) {
/*  58 */     draw(matrixStack, text, x - getStringWidth(text), y, color);
/*     */   }
/*     */   
/*     */   public void draw(class_4587 stack, String text, double x, double y, int color) {
/*  62 */     draw(stack, text, (float)x, (float)y, color);
/*     */   }
/*     */   
/*     */   public void draw(class_4587 stack, String text, float x, float y, int color) {
/*  66 */     if (text == null || text.isEmpty())
/*     */       return; 
/*  68 */     float localSize = this.size * 0.5F;
/*  69 */     if (!hasDrawableGlyphs(text, localSize))
/*  70 */       return;  y -= 1.5F;
/*     */     
/*  72 */     RenderSystem.enableBlend();
/*  73 */     RenderSystem.defaultBlendFunc();
/*  74 */     RenderSystem.disableCull();
/*     */     
/*  76 */     class_5944 shader = mc.method_62887().method_62947(ShaderUtils.fontsMsdf);
/*  77 */     if (shader == null)
/*     */       return; 
/*  79 */     setupShaderUniforms(shader, color);
/*     */     
/*  81 */     RenderSystem.setShaderTexture(0, this.font.getTextureId());
/*  82 */     this.font.setFiltered();
/*     */     
/*  84 */     Matrix4f matrix = stack.method_23760().method_23761();
/*  85 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
/*     */     
/*  87 */     this.font.applyGlyphs(matrix, (class_4588)buffer, localSize, text, 0.0F, x, y + this.font
/*  88 */         .getBaselineHeight() * localSize, 0.0F, 255, 255, 255, 255);
/*     */ 
/*     */     
/*  91 */     RenderSystem.setShader(ShaderUtils.fontsMsdf);
/*  92 */     class_286.method_43433(buffer.method_60800());
/*     */     
/*  94 */     RenderSystem.setShaderTexture(0, 0);
/*  95 */     RenderSystem.enableCull();
/*  96 */     RenderSystem.disableBlend();
/*     */   }
/*     */   
/*     */   public void drawGradientStringHorizontal(String text, float x, float y, int leftColor, int rightColor) {
/* 100 */     class_4587 stack = new class_4587();
/* 101 */     drawGradientStringHorizontal(stack, text, x, y, leftColor, rightColor);
/*     */   }
/*     */   
/*     */   public void drawGradientStringHorizontal(class_4587 stack, String text, float x, float y, int leftColor, int rightColor) {
/* 105 */     if (text == null || text.isEmpty())
/*     */       return; 
/* 107 */     float totalWidth = getStringWidth(text);
/* 108 */     float currentX = x;
/*     */     
/* 110 */     for (int i = 0; i < text.length(); i++) {
/* 111 */       char c = text.charAt(i);
/* 112 */       String charStr = String.valueOf(c);
/* 113 */       float charWidth = getStringWidth(charStr);
/*     */       
/* 115 */       float progress = (totalWidth > 0.0F) ? ((currentX - x) / totalWidth) : 0.0F;
/* 116 */       int color = interpolateColor(leftColor, rightColor, progress);
/*     */       
/* 118 */       draw(stack, charStr, currentX, y, color);
/* 119 */       currentX += charWidth;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void drawGradientStringHorizontal(class_4587 stack, String text, float x, float y, int topLeftColor, int topRightColor, int bottomLeftColor, int bottomRightColor) {
/* 124 */     if (text == null || text.isEmpty())
/*     */       return; 
/* 126 */     float totalWidth = getStringWidth(text);
/* 127 */     float currentX = x;
/*     */     
/* 129 */     for (int i = 0; i < text.length(); i++) {
/* 130 */       char c = text.charAt(i);
/* 131 */       String charStr = String.valueOf(c);
/* 132 */       float charWidth = getStringWidth(charStr);
/*     */       
/* 134 */       float progress = (totalWidth > 0.0F) ? ((currentX - x) / totalWidth) : 0.0F;
/*     */       
/* 136 */       int topColor = interpolateColor(topLeftColor, topRightColor, progress);
/* 137 */       int bottomColor = interpolateColor(bottomLeftColor, bottomRightColor, progress);
/* 138 */       int color = interpolateColor(topColor, bottomColor, 0.5F);
/*     */       
/* 140 */       draw(stack, charStr, currentX, y, color);
/* 141 */       currentX += charWidth;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void drawGradientStringVertical(class_4587 stack, String text, float x, float y, int topColor, int bottomColor) {
/* 146 */     if (text == null || text.isEmpty())
/* 147 */       return;  int color = interpolateColor(topColor, bottomColor, 0.5F);
/* 148 */     draw(stack, text, x, y, color);
/*     */   }
/*     */   
/*     */   public void drawStringWithFade(class_4587 stack, String text, float x, float y, float maxWidth, int color) {
/* 152 */     if (text == null || text.isEmpty())
/* 153 */       return;  if (maxWidth <= 1.0F)
/*     */       return; 
/* 155 */     int originalAlpha = color >>> 24 & 0xFF;
/* 156 */     if (originalAlpha == 0) originalAlpha = 255; 
/* 157 */     if (originalAlpha <= 4)
/*     */       return; 
/* 159 */     float localSize = this.size * 0.5F;
/* 160 */     y -= 1.5F;
/*     */     
/* 162 */     RenderSystem.enableBlend();
/* 163 */     RenderSystem.defaultBlendFunc();
/* 164 */     RenderSystem.disableCull();
/*     */     
/* 166 */     class_5944 shader = mc.method_62887().method_62947(ShaderUtils.fontsMsdf);
/* 167 */     if (shader == null)
/*     */       return; 
/* 169 */     class_284 textureSizeUniform = shader.method_34582("TextureSize");
/* 170 */     class_284 rangeUniform = shader.method_34582("Range");
/* 171 */     class_284 thicknessUniform = shader.method_34582("Thickness");
/* 172 */     class_284 edgeStrengthUniform = shader.method_34582("EdgeStrength");
/* 173 */     class_284 colorUniform = shader.method_34582("Color");
/* 174 */     class_284 outlineUniform = shader.method_34582("Outline");
/* 175 */     class_284 outlineThicknessUniform = shader.method_34582("OutlineThickness");
/* 176 */     class_284 outlineColorUniform = shader.method_34582("OutlineColor");
/*     */     
/* 178 */     if (textureSizeUniform != null) textureSizeUniform.method_1255(this.font.getAtlasWidth(), this.font.getAtlasHeight()); 
/* 179 */     if (rangeUniform != null) rangeUniform.method_1251(this.font.getRange()); 
/* 180 */     if (thicknessUniform != null) thicknessUniform.method_1251(0.0F); 
/* 181 */     if (edgeStrengthUniform != null) edgeStrengthUniform.method_1251(0.5F); 
/* 182 */     if (outlineUniform != null) outlineUniform.method_35649(0); 
/* 183 */     if (outlineThicknessUniform != null) outlineThicknessUniform.method_1251(0.0F); 
/* 184 */     if (outlineColorUniform != null) outlineColorUniform.method_35657(1.0F, 1.0F, 1.0F, 1.0F);
/*     */     
/* 186 */     RenderSystem.setShaderTexture(0, this.font.getTextureId());
/* 187 */     this.font.setFiltered();
/*     */     
/* 189 */     float currentX = x;
/* 190 */     float fadeZoneWidth = 25.0F;
/* 191 */     float fadeStartX = x + maxWidth - fadeZoneWidth;
/*     */     
/* 193 */     for (int i = 0; i < text.length(); i++) {
/* 194 */       String charStr = String.valueOf(text.charAt(i));
/* 195 */       float charWidth = getStringWidth(charStr);
/*     */       
/* 197 */       if (currentX > x + maxWidth && i > 0) {
/*     */         break;
/*     */       }
/*     */       
/* 201 */       int finalColor = color;
/* 202 */       if (currentX > fadeStartX) {
/* 203 */         float progressIntoFade = (currentX - fadeStartX) / fadeZoneWidth;
/* 204 */         progressIntoFade = Math.max(0.0F, Math.min(1.0F, progressIntoFade));
/*     */         
/* 206 */         float fadeFactor = (float)Math.cos(progressIntoFade * Math.PI / 2.0D);
/*     */         
/* 208 */         int newAlpha = (int)(originalAlpha * fadeFactor);
/* 209 */         finalColor = color & 0xFFFFFF | newAlpha << 24;
/*     */       } 
/*     */       
/* 212 */       if ((finalColor >>> 24 & 0xFF) > 4) {
/* 213 */         float[] rgba = extractRgba(finalColor);
/* 214 */         if (colorUniform != null) colorUniform.method_35657(rgba[0], rgba[1], rgba[2], rgba[3]);
/*     */         
/* 216 */         Matrix4f matrix = stack.method_23760().method_23761();
/* 217 */         class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
/*     */         
/* 219 */         this.font.applyGlyphs(matrix, (class_4588)buffer, localSize, charStr, 0.0F, currentX, y + this.font
/* 220 */             .getBaselineHeight() * localSize, 0.0F, 255, 255, 255, 255);
/*     */ 
/*     */         
/* 223 */         RenderSystem.setShader(ShaderUtils.fontsMsdf);
/* 224 */         class_286.method_43433(buffer.method_60800());
/*     */       } 
/*     */       
/* 227 */       currentX += charWidth;
/*     */     } 
/*     */     
/* 230 */     RenderSystem.setShaderTexture(0, 0);
/* 231 */     RenderSystem.enableCull();
/* 232 */     RenderSystem.disableBlend();
/*     */   }
/*     */   
/*     */   public void drawAnimatedGradientStringHorizontal(String text, float x, float y, int leftColor, int rightColor, float speed) {
/* 236 */     class_4587 stack = new class_4587();
/* 237 */     drawAnimatedGradientStringHorizontal(stack, text, x, y, leftColor, rightColor, speed, 1.15F);
/*     */   }
/*     */   
/*     */   public void drawAnimatedGradientStringHorizontal(class_4587 stack, String text, float x, float y, int leftColor, int rightColor, float speed) {
/* 241 */     drawAnimatedGradientStringHorizontal(stack, text, x, y, leftColor, rightColor, speed, 1.15F);
/*     */   }
/*     */   
/*     */   public void drawAnimatedGradientStringHorizontal(String text, float x, float y, int leftColor, int rightColor, float speed, float waveScale) {
/* 245 */     class_4587 stack = new class_4587();
/* 246 */     drawAnimatedGradientStringHorizontal(stack, text, x, y, leftColor, rightColor, speed, waveScale);
/*     */   }
/*     */   
/*     */   public void drawAnimatedGradientStringHorizontal(class_4587 stack, String text, float x, float y, int leftColor, int rightColor, float speed, float waveScale) {
/* 250 */     if (text == null || text.isEmpty())
/*     */       return; 
/* 252 */     float totalWidth = getStringWidth(text);
/* 253 */     float currentX = x;
/* 254 */     double timeOffset = System.currentTimeMillis() * 0.001D * Math.max(0.01F, speed) % 2.0D;
/* 255 */     float safeWaveScale = Math.max(0.01F, waveScale);
/*     */     
/* 257 */     for (int i = 0; i < text.length(); i++) {
/* 258 */       char c = text.charAt(i);
/* 259 */       String charStr = String.valueOf(c);
/* 260 */       float charWidth = getStringWidth(charStr);
/*     */       
/* 262 */       float baseProgress = (totalWidth > 0.0F) ? ((currentX - x) / totalWidth) : 0.0F;
/* 263 */       float animatedProgress = pingPong01(baseProgress * safeWaveScale + (float)timeOffset);
/* 264 */       int color = interpolateColor(leftColor, rightColor, animatedProgress);
/*     */       
/* 266 */       draw(stack, charStr, currentX, y, color);
/* 267 */       currentX += charWidth;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void drawStringWithOutline(class_4587 stack, String text, float x, float y, int color, int outlineColor) {
/* 272 */     if (text == null || text.isEmpty())
/*     */       return; 
/* 274 */     draw(stack, text, x - 1.0F, y, outlineColor);
/* 275 */     draw(stack, text, x + 1.0F, y, outlineColor);
/* 276 */     draw(stack, text, x, y - 1.0F, outlineColor);
/* 277 */     draw(stack, text, x, y + 1.0F, outlineColor);
/* 278 */     draw(stack, text, x, y, color);
/*     */   }
/*     */   
/*     */   public void drawStringWithShadow(class_4587 stack, String text, float x, float y, int color) {
/* 282 */     if (text == null || text.isEmpty())
/*     */       return; 
/* 284 */     int shadowColor = 1426063360;
/* 285 */     draw(stack, text, x + 1.0F, y + 1.0F, shadowColor);
/* 286 */     draw(stack, text, x, y, color);
/*     */   }
/*     */   
/*     */   public void drawParagraph(class_4587 stack, String text, double x, double y, int defaultColor) {
/* 290 */     drawParagraph(stack, text, (float)x, (float)y, defaultColor);
/*     */   }
/*     */   
/*     */   public void drawParagraph(class_4587 stack, String text, float x, float y, int defaultColor) {
/* 294 */     if (text == null || text.isEmpty())
/*     */       return; 
/* 296 */     float localSize = this.size * 0.5F;
/* 297 */     y -= 1.5F;
/*     */     
/* 299 */     RenderSystem.enableBlend();
/* 300 */     RenderSystem.defaultBlendFunc();
/* 301 */     RenderSystem.disableCull();
/*     */     
/* 303 */     class_5944 shader = mc.method_62887().method_62947(ShaderUtils.fontsMsdf);
/* 304 */     if (shader == null)
/*     */       return; 
/* 306 */     class_284 textureSizeUniform = shader.method_34582("TextureSize");
/* 307 */     class_284 rangeUniform = shader.method_34582("Range");
/* 308 */     class_284 thicknessUniform = shader.method_34582("Thickness");
/* 309 */     class_284 edgeStrengthUniform = shader.method_34582("EdgeStrength");
/* 310 */     class_284 colorUniform = shader.method_34582("Color");
/*     */     
/* 312 */     if (textureSizeUniform != null) textureSizeUniform.method_1255(this.font.getAtlasWidth(), this.font.getAtlasHeight()); 
/* 313 */     if (rangeUniform != null) rangeUniform.method_1251(this.font.getRange()); 
/* 314 */     if (thicknessUniform != null) thicknessUniform.method_1251(0.0F); 
/* 315 */     if (edgeStrengthUniform != null) edgeStrengthUniform.method_1251(0.5F);
/*     */     
/* 317 */     RenderSystem.setShaderTexture(0, this.font.getTextureId());
/* 318 */     this.font.setFiltered();
/*     */     
/* 320 */     float currentX = x;
/* 321 */     int currentColor = defaultColor;
/* 322 */     StringBuilder segment = new StringBuilder();
/*     */     
/* 324 */     for (int i = 0; i < text.length(); i++) {
/* 325 */       char c = text.charAt(i);
/*     */       
/* 327 */       if (c == '§' && i + 1 < text.length()) {
/* 328 */         if (!segment.isEmpty()) {
/* 329 */           drawSegment(stack, colorUniform, segment.toString(), currentX, y + this.font
/* 330 */               .getBaselineHeight() * localSize, localSize, currentColor);
/* 331 */           currentX += getStringWidth(segment.toString());
/* 332 */           segment.setLength(0);
/*     */         } 
/*     */         
/* 335 */         char code = text.charAt(i + 1);
/* 336 */         int newColor = getColorFromCode(code, defaultColor);
/* 337 */         if (newColor != -1) {
/* 338 */           currentColor = newColor;
/*     */         }
/* 340 */         i++;
/*     */       } else {
/* 342 */         segment.append(c);
/*     */       } 
/*     */     } 
/*     */     
/* 346 */     if (!segment.isEmpty()) {
/* 347 */       drawSegment(stack, colorUniform, segment.toString(), currentX, y + this.font
/* 348 */           .getBaselineHeight() * localSize, localSize, currentColor);
/*     */     }
/*     */     
/* 351 */     RenderSystem.setShaderTexture(0, 0);
/* 352 */     RenderSystem.enableCull();
/* 353 */     RenderSystem.disableBlend();
/*     */   }
/*     */   
/*     */   private void drawSegment(class_4587 stack, class_284 colorUniform, String text, float x, float y, float size, int color) {
/* 357 */     if (!hasDrawableGlyphs(text, size))
/*     */       return; 
/* 359 */     float[] rgba = extractRgba(color);
/* 360 */     if (colorUniform != null) colorUniform.method_35657(rgba[0], rgba[1], rgba[2], rgba[3]);
/*     */     
/* 362 */     Matrix4f matrix = stack.method_23760().method_23761();
/* 363 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
/*     */     
/* 365 */     this.font.applyGlyphs(matrix, (class_4588)buffer, size, text, 0.0F, x, y, 0.0F, 255, 255, 255, 255);
/*     */     
/* 367 */     RenderSystem.setShader(ShaderUtils.fontsMsdf);
/* 368 */     class_286.method_43433(buffer.method_60800());
/*     */   }
/*     */   
/*     */   private boolean hasDrawableGlyphs(String text, float renderSize) {
/* 372 */     return (text != null && !text.isEmpty() && this.font.getWidth(text, renderSize) > 0.0F);
/*     */   }
/*     */   
/*     */   private void setupShaderUniforms(class_5944 shader, int color) {
/* 376 */     class_284 textureSizeUniform = shader.method_34582("TextureSize");
/* 377 */     class_284 rangeUniform = shader.method_34582("Range");
/* 378 */     class_284 thicknessUniform = shader.method_34582("Thickness");
/* 379 */     class_284 edgeStrengthUniform = shader.method_34582("EdgeStrength");
/* 380 */     class_284 colorUniform = shader.method_34582("Color");
/* 381 */     class_284 outlineUniform = shader.method_34582("Outline");
/* 382 */     class_284 outlineThicknessUniform = shader.method_34582("OutlineThickness");
/* 383 */     class_284 outlineColorUniform = shader.method_34582("OutlineColor");
/*     */     
/* 385 */     if (textureSizeUniform != null) textureSizeUniform.method_1255(this.font.getAtlasWidth(), this.font.getAtlasHeight()); 
/* 386 */     if (rangeUniform != null) rangeUniform.method_1251(this.font.getRange()); 
/* 387 */     if (thicknessUniform != null) thicknessUniform.method_1251(0.0F); 
/* 388 */     if (edgeStrengthUniform != null) edgeStrengthUniform.method_1251(0.5F); 
/* 389 */     if (outlineUniform != null) outlineUniform.method_35649(0); 
/* 390 */     if (outlineThicknessUniform != null) outlineThicknessUniform.method_1251(0.0F); 
/* 391 */     if (outlineColorUniform != null) outlineColorUniform.method_35657(0.0F, 0.0F, 0.0F, 1.0F);
/*     */     
/* 393 */     float[] rgba = extractRgba(color);
/* 394 */     if (colorUniform != null) colorUniform.method_35657(rgba[0], rgba[1], rgba[2], rgba[3]); 
/*     */   }
/*     */   
/*     */   private int getColorFromCode(char code, int defaultColor) {
/* 398 */     int alpha = defaultColor >> 24 & 0xFF;
/* 399 */     if (alpha == 0) alpha = 255;
/*     */     
/* 401 */     switch (code) { case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9': case 'A': case 'a': case 'B': case 'b': case 'C': case 'c': case 'D': case 'd': case 'E': case 'e': case 'F': case 'f': case 'R': case 'r':  }  return 
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
/* 419 */       -1;
/*     */   }
/*     */ 
/*     */   
/*     */   private float[] extractRgba(int color) {
/* 424 */     int a = color >> 24 & 0xFF;
/* 425 */     int r = color >> 16 & 0xFF;
/* 426 */     int g = color >> 8 & 0xFF;
/* 427 */     int b = color & 0xFF;
/* 428 */     if (a == 0) a = 255; 
/* 429 */     return new float[] { r / 255.0F, g / 255.0F, b / 255.0F, a / 255.0F };
/*     */   }
/*     */   
/*     */   public static int interpolateColor(int color1, int color2, float progress) {
/* 433 */     progress = Math.max(0.0F, Math.min(1.0F, progress));
/*     */     
/* 435 */     int a1 = color1 >> 24 & 0xFF;
/* 436 */     int r1 = color1 >> 16 & 0xFF;
/* 437 */     int g1 = color1 >> 8 & 0xFF;
/* 438 */     int b1 = color1 & 0xFF;
/*     */     
/* 440 */     int a2 = color2 >> 24 & 0xFF;
/* 441 */     int r2 = color2 >> 16 & 0xFF;
/* 442 */     int g2 = color2 >> 8 & 0xFF;
/* 443 */     int b2 = color2 & 0xFF;
/*     */     
/* 445 */     if (a1 == 0) a1 = 255; 
/* 446 */     if (a2 == 0) a2 = 255;
/*     */     
/* 448 */     int a = (int)(a1 + (a2 - a1) * progress);
/* 449 */     int r = (int)(r1 + (r2 - r1) * progress);
/* 450 */     int g = (int)(g1 + (g2 - g1) * progress);
/* 451 */     int b = (int)(b1 + (b2 - b1) * progress);
/*     */     
/* 453 */     return a << 24 | r << 16 | g << 8 | b;
/*     */   }
/*     */   
/*     */   private static float pingPong01(float value) {
/* 457 */     float wrapped = value % 2.0F;
/* 458 */     if (wrapped < 0.0F) wrapped += 2.0F; 
/* 459 */     return (wrapped > 1.0F) ? (2.0F - wrapped) : wrapped;
/*     */   }
/*     */   
/*     */   public float getStringWidth(String text) {
/* 463 */     if (text == null) return 0.0F; 
/* 464 */     return this.font.getWidth(stripFormattingCodes(text), this.size) / 2.0F;
/*     */   }
/*     */   
/*     */   public float getWidth(String text) {
/* 468 */     return getStringWidth(text);
/*     */   }
/*     */   
/*     */   public float getHeight() {
/* 472 */     return this.size;
/*     */   }
/*     */   
/*     */   public float getFontHeight() {
/* 476 */     return this.size;
/*     */   }
/*     */   
/*     */   public MsdfFont getFont() {
/* 480 */     return this.font;
/*     */   }
/*     */   
/*     */   public float getSize() {
/* 484 */     return this.size;
/*     */   }
/*     */   
/*     */   private String stripFormattingCodes(String text) {
/* 488 */     if (text == null || text.indexOf('§') < 0) {
/* 489 */       return text;
/*     */     }
/*     */     
/* 492 */     StringBuilder clean = new StringBuilder(text.length());
/* 493 */     for (int i = 0; i < text.length(); i++) {
/* 494 */       char current = text.charAt(i);
/* 495 */       if (current == '§' && i + 1 < text.length()) {
/* 496 */         i++;
/*     */       } else {
/*     */         
/* 499 */         clean.append(current);
/*     */       } 
/* 501 */     }  return clean.toString();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\render\fonts\msdf\Font.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */