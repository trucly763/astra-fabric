/*      */ package shame.astra.api.utils.render;
/*      */ import com.mojang.blaze3d.systems.RenderSystem;
/*      */ import java.awt.Color;
/*      */ import java.util.UUID;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import net.minecraft.class_1058;
/*      */ import net.minecraft.class_1068;
/*      */ import net.minecraft.class_1657;
/*      */ import net.minecraft.class_1799;
/*      */ import net.minecraft.class_284;
/*      */ import net.minecraft.class_286;
/*      */ import net.minecraft.class_287;
/*      */ import net.minecraft.class_289;
/*      */ import net.minecraft.class_290;
/*      */ import net.minecraft.class_293;
/*      */ import net.minecraft.class_2960;
/*      */ import net.minecraft.class_332;
/*      */ import net.minecraft.class_4587;
/*      */ import net.minecraft.class_5944;
/*      */ import net.minecraft.class_640;
/*      */ import org.joml.Matrix4f;
/*      */ import shame.astra.api.utils.color.ColorUtils;
/*      */ import shame.astra.api.utils.render.blur.BlurProgram;
/*      */ import shame.astra.api.utils.render.glow.GlowCallback;
/*      */ import shame.astra.api.utils.render.glow.GlowProgram;
/*      */ import shame.astra.api.utils.scissor.ScissorUtils;
/*      */ 
/*      */ public final class RenderUtils implements QClient {
/*      */   @Generated
/*      */   private RenderUtils() {
/*   31 */     throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
/*      */   }
/*   33 */   private static final ConcurrentHashMap<String, class_2960> skinCache = new ConcurrentHashMap<>();
/*   34 */   private static final UUID DEFAULT_SKIN_UUID = new UUID(0L, 0L);
/*      */   
/*      */   public static void drawHudItem(class_332 context, class_1799 stack, float x, float y, float scale, float z) {
/*   37 */     if (context == null || stack == null || stack.method_7960()) {
/*      */       return;
/*      */     }
/*      */     
/*   41 */     class_4587 matrices = context.method_51448();
/*   42 */     RenderSystem.enableBlend();
/*   43 */     RenderSystem.defaultBlendFunc();
/*   44 */     RenderSystem.disableDepthTest();
/*   45 */     RenderSystem.depthMask(false);
/*      */     
/*   47 */     matrices.method_22903();
/*   48 */     matrices.method_46416(x, y, z);
/*   49 */     matrices.method_22905(scale, scale, 1.0F);
/*   50 */     context.method_51427(stack, 0, 0);
/*   51 */     matrices.method_22909();
/*      */     
/*   53 */     RenderSystem.disableDepthTest();
/*   54 */     RenderSystem.depthMask(true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void drawGradient6Rect(class_4587 matrices, float x, float y, float width, float height, float radius, int leftTopColor, int leftBottomColor, int centerTopColor, int centerBottomColor, int rightTopColor, int rightBottomColor) {
/*   62 */     RenderSystem.enableBlend();
/*   63 */     RenderSystem.defaultBlendFunc();
/*      */     
/*   65 */     class_5944 shader = mc.method_62887().method_62947(ShaderUtils.gradient6Rect);
/*      */     
/*   67 */     Matrix4f matrix = matrices.method_23760().method_23761();
/*      */     
/*   69 */     class_284 sizeUniform = shader.method_34582("Size");
/*   70 */     class_284 radiusUniform = shader.method_34582("Radius");
/*   71 */     class_284 smoothnessUniform = shader.method_34582("Smoothness");
/*   72 */     class_284 leftTopColorUniform = shader.method_34582("LeftTopColor");
/*   73 */     class_284 leftBottomColorUniform = shader.method_34582("LeftBottomColor");
/*   74 */     class_284 centerTopColorUniform = shader.method_34582("CenterTopColor");
/*   75 */     class_284 centerBottomColorUniform = shader.method_34582("CenterBottomColor");
/*   76 */     class_284 rightTopColorUniform = shader.method_34582("RightTopColor");
/*   77 */     class_284 rightBottomColorUniform = shader.method_34582("RightBottomColor");
/*      */     
/*   79 */     if (sizeUniform != null) sizeUniform.method_1255(width, height); 
/*   80 */     if (radiusUniform != null) radiusUniform.method_35657(radius, radius, radius, radius); 
/*   81 */     if (smoothnessUniform != null) smoothnessUniform.method_1251(1.0F);
/*      */     
/*   83 */     if (leftTopColorUniform != null) {
/*   84 */       int a = leftTopColor >> 24 & 0xFF;
/*   85 */       if (a == 0) a = 255; 
/*   86 */       leftTopColorUniform.method_35657((leftTopColor >> 16 & 0xFF) / 255.0F, (leftTopColor >> 8 & 0xFF) / 255.0F, (leftTopColor & 0xFF) / 255.0F, a / 255.0F);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   94 */     if (leftBottomColorUniform != null) {
/*   95 */       int a = leftBottomColor >> 24 & 0xFF;
/*   96 */       if (a == 0) a = 255; 
/*   97 */       leftBottomColorUniform.method_35657((leftBottomColor >> 16 & 0xFF) / 255.0F, (leftBottomColor >> 8 & 0xFF) / 255.0F, (leftBottomColor & 0xFF) / 255.0F, a / 255.0F);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  105 */     if (centerTopColorUniform != null) {
/*  106 */       int a = centerTopColor >> 24 & 0xFF;
/*  107 */       if (a == 0) a = 255; 
/*  108 */       centerTopColorUniform.method_35657((centerTopColor >> 16 & 0xFF) / 255.0F, (centerTopColor >> 8 & 0xFF) / 255.0F, (centerTopColor & 0xFF) / 255.0F, a / 255.0F);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  116 */     if (centerBottomColorUniform != null) {
/*  117 */       int a = centerBottomColor >> 24 & 0xFF;
/*  118 */       if (a == 0) a = 255; 
/*  119 */       centerBottomColorUniform.method_35657((centerBottomColor >> 16 & 0xFF) / 255.0F, (centerBottomColor >> 8 & 0xFF) / 255.0F, (centerBottomColor & 0xFF) / 255.0F, a / 255.0F);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  127 */     if (rightTopColorUniform != null) {
/*  128 */       int a = rightTopColor >> 24 & 0xFF;
/*  129 */       if (a == 0) a = 255; 
/*  130 */       rightTopColorUniform.method_35657((rightTopColor >> 16 & 0xFF) / 255.0F, (rightTopColor >> 8 & 0xFF) / 255.0F, (rightTopColor & 0xFF) / 255.0F, a / 255.0F);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  138 */     if (rightBottomColorUniform != null) {
/*  139 */       int a = rightBottomColor >> 24 & 0xFF;
/*  140 */       if (a == 0) a = 255; 
/*  141 */       rightBottomColorUniform.method_35657((rightBottomColor >> 16 & 0xFF) / 255.0F, (rightBottomColor >> 8 & 0xFF) / 255.0F, (rightBottomColor & 0xFF) / 255.0F, a / 255.0F);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  149 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1576);
/*      */     
/*  151 */     buffer.method_22918(matrix, x, y, 0.0F).method_22915(1.0F, 1.0F, 1.0F, 1.0F);
/*  152 */     buffer.method_22918(matrix, x, y + height, 0.0F).method_22915(1.0F, 1.0F, 1.0F, 1.0F);
/*  153 */     buffer.method_22918(matrix, x + width, y + height, 0.0F).method_22915(1.0F, 1.0F, 1.0F, 1.0F);
/*  154 */     buffer.method_22918(matrix, x + width, y, 0.0F).method_22915(1.0F, 1.0F, 1.0F, 1.0F);
/*      */     
/*  156 */     RenderSystem.setShader(ShaderUtils.gradient6Rect);
/*  157 */     class_286.method_43433(buffer.method_60800());
/*      */     
/*  159 */     RenderSystem.disableBlend();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void drawShadow(class_4587 matrices, float x, float y, float width, float height, float radius, float softness, int topLeftColor, int topRightColor, int bottomLeftColor, int bottomRightColor) {
/*  169 */     RenderSystem.enableBlend();
/*  170 */     RenderSystem.defaultBlendFunc();
/*      */     
/*  172 */     class_5944 shader = mc.method_62887().method_62947(ShaderUtils.shadowRect);
/*      */     
/*  174 */     Matrix4f matrix = matrices.method_23760().method_23761();
/*      */     
/*  176 */     float extendedWidth = width + softness * 2.0F;
/*  177 */     float extendedHeight = height + softness * 2.0F;
/*  178 */     float drawX = x - softness;
/*  179 */     float drawY = y - softness;
/*      */     
/*  181 */     class_284 sizeUniform = shader.method_34582("Size");
/*  182 */     class_284 softnessUniform = shader.method_34582("Softness");
/*  183 */     class_284 radiusUniform = shader.method_34582("Radius");
/*  184 */     class_284 topLeftColorUniform = shader.method_34582("TopLeftColor");
/*  185 */     class_284 topRightColorUniform = shader.method_34582("TopRightColor");
/*  186 */     class_284 bottomLeftColorUniform = shader.method_34582("BottomLeftColor");
/*  187 */     class_284 bottomRightColorUniform = shader.method_34582("BottomRightColor");
/*      */     
/*  189 */     if (sizeUniform != null) sizeUniform.method_1255(extendedWidth, extendedHeight); 
/*  190 */     if (softnessUniform != null) softnessUniform.method_1251(softness); 
/*  191 */     if (radiusUniform != null) radiusUniform.method_1251(radius);
/*      */     
/*  193 */     if (topLeftColorUniform != null) {
/*  194 */       int a = topLeftColor >> 24 & 0xFF;
/*  195 */       if (a == 0) a = 255; 
/*  196 */       topLeftColorUniform.method_35657((topLeftColor >> 16 & 0xFF) / 255.0F, (topLeftColor >> 8 & 0xFF) / 255.0F, (topLeftColor & 0xFF) / 255.0F, a / 255.0F);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  204 */     if (topRightColorUniform != null) {
/*  205 */       int a = topRightColor >> 24 & 0xFF;
/*  206 */       if (a == 0) a = 255; 
/*  207 */       topRightColorUniform.method_35657((topRightColor >> 16 & 0xFF) / 255.0F, (topRightColor >> 8 & 0xFF) / 255.0F, (topRightColor & 0xFF) / 255.0F, a / 255.0F);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  215 */     if (bottomLeftColorUniform != null) {
/*  216 */       int a = bottomLeftColor >> 24 & 0xFF;
/*  217 */       if (a == 0) a = 255; 
/*  218 */       bottomLeftColorUniform.method_35657((bottomLeftColor >> 16 & 0xFF) / 255.0F, (bottomLeftColor >> 8 & 0xFF) / 255.0F, (bottomLeftColor & 0xFF) / 255.0F, a / 255.0F);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  226 */     if (bottomRightColorUniform != null) {
/*  227 */       int a = bottomRightColor >> 24 & 0xFF;
/*  228 */       if (a == 0) a = 255; 
/*  229 */       bottomRightColorUniform.method_35657((bottomRightColor >> 16 & 0xFF) / 255.0F, (bottomRightColor >> 8 & 0xFF) / 255.0F, (bottomRightColor & 0xFF) / 255.0F, a / 255.0F);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  237 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1585);
/*      */     
/*  239 */     buffer.method_22918(matrix, drawX, drawY, 0.0F).method_22913(0.0F, 0.0F);
/*  240 */     buffer.method_22918(matrix, drawX, drawY + extendedHeight, 0.0F).method_22913(0.0F, 1.0F);
/*  241 */     buffer.method_22918(matrix, drawX + extendedWidth, drawY + extendedHeight, 0.0F).method_22913(1.0F, 1.0F);
/*  242 */     buffer.method_22918(matrix, drawX + extendedWidth, drawY, 0.0F).method_22913(1.0F, 0.0F);
/*      */     
/*  244 */     RenderSystem.setShader(ShaderUtils.shadowRect);
/*  245 */     class_286.method_43433(buffer.method_60800());
/*      */     
/*  247 */     RenderSystem.disableBlend();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void drawShadow(class_4587 matrices, float x, float y, float width, float height, float radius, int topLeftColor, int topRightColor, int bottomLeftColor, int bottomRightColor) {
/*  252 */     drawShadow(matrices, x, y, width, height, radius, 10.0F, topLeftColor, topRightColor, bottomLeftColor, bottomRightColor);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void drawShadow(class_4587 matrices, float x, float y, float width, float height, float radius, float softness, int color) {
/*  257 */     drawShadow(matrices, x, y, width, height, radius, softness, color, color, color, color);
/*      */   }
/*      */   
/*      */   public static void drawShadow(class_4587 matrices, float x, float y, float width, float height, float radius, int color) {
/*  261 */     drawShadow(matrices, x, y, width, height, radius, 10.0F, color, color, color, color);
/*      */   }
/*      */   
/*      */   public static void drawShadow(class_4587 matrices, float x, float y, float width, float height, int color) {
/*  265 */     drawShadow(matrices, x, y, width, height, 0.0F, 10.0F, color, color, color, color);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void drawShadow(class_4587 matrices, float x, float y, float width, float height, float radius, float softness, int topColor, int bottomColor) {
/*  270 */     drawShadow(matrices, x, y, width, height, radius, softness, topColor, topColor, bottomColor, bottomColor);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void drawShadowHorizontal(class_4587 matrices, float x, float y, float width, float height, float radius, float softness, int leftColor, int rightColor) {
/*  275 */     drawShadow(matrices, x, y, width, height, radius, softness, leftColor, rightColor, leftColor, rightColor);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void drawShadow(class_4587 matrices, float x, float y, float width, float height, float radius, float softness, float offsetX, float offsetY, int color) {
/*  280 */     drawShadow(matrices, x + offsetX, y + offsetY, width, height, radius, softness, color, color, color, color);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void drawShadow(class_4587 matrices, float x, float y, float width, float height, float radius, float softness, float offsetX, float offsetY, int topLeftColor, int topRightColor, int bottomLeftColor, int bottomRightColor) {
/*  286 */     drawShadow(matrices, x + offsetX, y + offsetY, width, height, radius, softness, topLeftColor, topRightColor, bottomLeftColor, bottomRightColor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void drawShadow6(class_4587 matrices, float x, float y, float width, float height, float radius, float softness, int leftTopColor, int leftBottomColor, int centerTopColor, int centerBottomColor, int rightTopColor, int rightBottomColor) {
/*  295 */     RenderSystem.enableBlend();
/*  296 */     RenderSystem.defaultBlendFunc();
/*      */     
/*  298 */     class_5944 shader = mc.method_62887().method_62947(ShaderUtils.shadow6Rect);
/*      */     
/*  300 */     Matrix4f matrix = matrices.method_23760().method_23761();
/*      */     
/*  302 */     float extendedWidth = width + softness * 2.0F;
/*  303 */     float extendedHeight = height + softness * 2.0F;
/*  304 */     float drawX = x - softness;
/*  305 */     float drawY = y - softness;
/*      */     
/*  307 */     class_284 sizeUniform = shader.method_34582("Size");
/*  308 */     class_284 softnessUniform = shader.method_34582("Softness");
/*  309 */     class_284 radiusUniform = shader.method_34582("Radius");
/*  310 */     class_284 leftTopColorUniform = shader.method_34582("LeftTopColor");
/*  311 */     class_284 leftBottomColorUniform = shader.method_34582("LeftBottomColor");
/*  312 */     class_284 centerTopColorUniform = shader.method_34582("CenterTopColor");
/*  313 */     class_284 centerBottomColorUniform = shader.method_34582("CenterBottomColor");
/*  314 */     class_284 rightTopColorUniform = shader.method_34582("RightTopColor");
/*  315 */     class_284 rightBottomColorUniform = shader.method_34582("RightBottomColor");
/*      */     
/*  317 */     if (sizeUniform != null) sizeUniform.method_1255(extendedWidth, extendedHeight); 
/*  318 */     if (softnessUniform != null) softnessUniform.method_1251(softness); 
/*  319 */     if (radiusUniform != null) radiusUniform.method_1251(radius);
/*      */     
/*  321 */     if (leftTopColorUniform != null) {
/*  322 */       int a = leftTopColor >> 24 & 0xFF;
/*  323 */       if (a == 0) a = 255; 
/*  324 */       leftTopColorUniform.method_35657((leftTopColor >> 16 & 0xFF) / 255.0F, (leftTopColor >> 8 & 0xFF) / 255.0F, (leftTopColor & 0xFF) / 255.0F, a / 255.0F);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  332 */     if (leftBottomColorUniform != null) {
/*  333 */       int a = leftBottomColor >> 24 & 0xFF;
/*  334 */       if (a == 0) a = 255; 
/*  335 */       leftBottomColorUniform.method_35657((leftBottomColor >> 16 & 0xFF) / 255.0F, (leftBottomColor >> 8 & 0xFF) / 255.0F, (leftBottomColor & 0xFF) / 255.0F, a / 255.0F);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  343 */     if (centerTopColorUniform != null) {
/*  344 */       int a = centerTopColor >> 24 & 0xFF;
/*  345 */       if (a == 0) a = 255; 
/*  346 */       centerTopColorUniform.method_35657((centerTopColor >> 16 & 0xFF) / 255.0F, (centerTopColor >> 8 & 0xFF) / 255.0F, (centerTopColor & 0xFF) / 255.0F, a / 255.0F);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  354 */     if (centerBottomColorUniform != null) {
/*  355 */       int a = centerBottomColor >> 24 & 0xFF;
/*  356 */       if (a == 0) a = 255; 
/*  357 */       centerBottomColorUniform.method_35657((centerBottomColor >> 16 & 0xFF) / 255.0F, (centerBottomColor >> 8 & 0xFF) / 255.0F, (centerBottomColor & 0xFF) / 255.0F, a / 255.0F);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  365 */     if (rightTopColorUniform != null) {
/*  366 */       int a = rightTopColor >> 24 & 0xFF;
/*  367 */       if (a == 0) a = 255; 
/*  368 */       rightTopColorUniform.method_35657((rightTopColor >> 16 & 0xFF) / 255.0F, (rightTopColor >> 8 & 0xFF) / 255.0F, (rightTopColor & 0xFF) / 255.0F, a / 255.0F);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  376 */     if (rightBottomColorUniform != null) {
/*  377 */       int a = rightBottomColor >> 24 & 0xFF;
/*  378 */       if (a == 0) a = 255; 
/*  379 */       rightBottomColorUniform.method_35657((rightBottomColor >> 16 & 0xFF) / 255.0F, (rightBottomColor >> 8 & 0xFF) / 255.0F, (rightBottomColor & 0xFF) / 255.0F, a / 255.0F);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  387 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1585);
/*      */     
/*  389 */     buffer.method_22918(matrix, drawX, drawY, 0.0F).method_22913(0.0F, 0.0F);
/*  390 */     buffer.method_22918(matrix, drawX, drawY + extendedHeight, 0.0F).method_22913(0.0F, 1.0F);
/*  391 */     buffer.method_22918(matrix, drawX + extendedWidth, drawY + extendedHeight, 0.0F).method_22913(1.0F, 1.0F);
/*  392 */     buffer.method_22918(matrix, drawX + extendedWidth, drawY, 0.0F).method_22913(1.0F, 0.0F);
/*      */     
/*  394 */     RenderSystem.setShader(ShaderUtils.shadow6Rect);
/*  395 */     class_286.method_43433(buffer.method_60800());
/*      */     
/*  397 */     RenderSystem.disableBlend();
/*      */   }
/*      */   
/*      */   public static void drawTexture(class_4587 matrices, class_2960 texture, float x, float y, float width, float height, float u1, float v1, float u2, float v2, int color) {
/*  401 */     RenderSystem.enableBlend();
/*  402 */     RenderSystem.defaultBlendFunc();
/*  403 */     RenderSystem.setShaderTexture(0, texture);
/*      */     
/*  405 */     Matrix4f matrix = matrices.method_23760().method_23761();
/*      */     
/*  407 */     int alpha = color >> 24 & 0xFF;
/*  408 */     if (alpha == 0) alpha = 255; 
/*  409 */     float r = (color >> 16 & 0xFF) / 255.0F;
/*  410 */     float g = (color >> 8 & 0xFF) / 255.0F;
/*  411 */     float b = (color & 0xFF) / 255.0F;
/*  412 */     float a = alpha / 255.0F;
/*      */     
/*  414 */     RenderSystem.setShader(class_10142.field_53880);
/*      */     
/*  416 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
/*  417 */     buffer.method_22918(matrix, x, y, 0.0F).method_22913(u1, v1).method_22915(r, g, b, a);
/*  418 */     buffer.method_22918(matrix, x, y + height, 0.0F).method_22913(u1, v2).method_22915(r, g, b, a);
/*  419 */     buffer.method_22918(matrix, x + width, y + height, 0.0F).method_22913(u2, v2).method_22915(r, g, b, a);
/*  420 */     buffer.method_22918(matrix, x + width, y, 0.0F).method_22913(u2, v1).method_22915(r, g, b, a);
/*  421 */     class_286.method_43433(buffer.method_60800());
/*      */     
/*  423 */     RenderSystem.setShaderTexture(0, 0);
/*  424 */     RenderSystem.disableBlend();
/*      */   }
/*      */   
/*      */   public static void drawImage(class_4587 matrices, class_2960 texture, float x, float y, float width, float height, int color) {
/*  428 */     drawTexture(matrices, texture, x, y, width, height, 0.0F, 0.0F, 1.0F, 1.0F, color);
/*      */   }
/*      */   
/*      */   public static void drawImage(class_4587 matrices, String namespace, String path, float x, float y, float width, float height, int color) {
/*  432 */     drawImage(matrices, class_2960.method_60655(namespace, path), x, y, width, height, color);
/*      */   }
/*      */   
/*      */   public static void drawSprite(class_4587 matrices, class_1058 sprite, float x, float y, float size, int color) {
/*  436 */     drawTexture(matrices, sprite.method_45852(), x, y, size, size, sprite
/*  437 */         .method_4594(), sprite.method_4593(), sprite.method_4577(), sprite.method_4575(), color);
/*      */   }
/*      */   
/*      */   public static void drawPlayerHead(class_4587 matrices, class_1657 player, float x, float y, float size, float radius, float hurtPercent) {
/*  441 */     if (player == null)
/*  442 */       return;  class_2960 skinTexture = getSkinTexture(player);
/*  443 */     drawHeadInternal(matrices, skinTexture, x, y, size, radius, 1.0F, hurtPercent);
/*      */   }
/*      */   
/*      */   public static void drawPlayerHead(class_4587 matrices, String username, float x, float y, float size, float radius) {
/*  447 */     drawPlayerHead(matrices, username, x, y, size, radius, 1.0F, 0.0F);
/*      */   }
/*      */   
/*      */   public static void drawPlayerHead(class_4587 matrices, String username, float x, float y, float size, float radius, float alpha, float hurtPercent) {
/*  451 */     if (username == null || username.isEmpty())
/*  452 */       return;  class_2960 skinTexture = getSkinTextureByName(username);
/*  453 */     drawHeadInternal(matrices, skinTexture, x, y, size, radius, alpha, hurtPercent);
/*      */   }
/*      */   
/*      */   public static void drawPlayerHead(class_4587 matrices, UUID uuid, float x, float y, float size, float radius) {
/*  457 */     drawPlayerHead(matrices, uuid, x, y, size, radius, 1.0F, 0.0F);
/*      */   }
/*      */   
/*      */   public static void drawPlayerHead(class_4587 matrices, UUID uuid, float x, float y, float size, float radius, float alpha, float hurtPercent) {
/*  461 */     if (uuid == null)
/*  462 */       return;  class_2960 skinTexture = getSkinTextureByUUID(uuid);
/*  463 */     drawHeadInternal(matrices, skinTexture, x, y, size, radius, alpha, hurtPercent);
/*      */   }
/*      */   
/*      */   public static void drawPlayerHead(class_4587 matrices, class_640 entry, float x, float y, float size, float radius) {
/*  467 */     drawPlayerHead(matrices, entry, x, y, size, radius, 1.0F, 0.0F);
/*      */   }
/*      */   
/*      */   public static void drawPlayerHead(class_4587 matrices, class_640 entry, float x, float y, float size, float radius, float alpha, float hurtPercent) {
/*  471 */     if (entry == null)
/*  472 */       return;  class_2960 skinTexture = entry.method_52810().comp_1626();
/*  473 */     if (skinTexture == null) {
/*  474 */       skinTexture = class_1068.method_4648(entry.method_2966().getId()).comp_1626();
/*      */     }
/*  476 */     drawHeadInternal(matrices, skinTexture, x, y, size, radius, alpha, hurtPercent);
/*      */   }
/*      */   
/*      */   public static void drawPlayerHead(class_4587 matrices, class_2960 skinTexture, float x, float y, float size, float radius) {
/*  480 */     drawHeadInternal(matrices, skinTexture, x, y, size, radius, 1.0F, 0.0F);
/*      */   }
/*      */   
/*      */   private static void drawHeadInternal(class_4587 matrices, class_2960 skinTexture, float x, float y, float size, float radius, float alpha, float hurtPercent) {
/*  484 */     if (skinTexture == null) {
/*  485 */       skinTexture = class_1068.method_4648(DEFAULT_SKIN_UUID).comp_1626();
/*      */     }
/*      */     
/*  488 */     RenderSystem.enableBlend();
/*  489 */     RenderSystem.defaultBlendFunc();
/*  490 */     RenderSystem.setShaderTexture(0, skinTexture);
/*      */     
/*  492 */     class_5944 shader = mc.method_62887().method_62947(ShaderUtils.face);
/*      */     
/*  494 */     Matrix4f matrix = matrices.method_23760().method_23761();
/*      */     
/*  496 */     class_284 locationUniform = shader.method_34582("location");
/*  497 */     class_284 sizeUniform = shader.method_34582("size");
/*  498 */     class_284 radiusUniform = shader.method_34582("radius");
/*  499 */     class_284 alphaUniform = shader.method_34582("alpha");
/*  500 */     class_284 uUniform = shader.method_34582("u");
/*  501 */     class_284 vUniform = shader.method_34582("v");
/*  502 */     class_284 wUniform = shader.method_34582("w");
/*  503 */     class_284 hUniform = shader.method_34582("h");
/*  504 */     class_284 hurtPercentUniform = shader.method_34582("hurtPercent");
/*      */     
/*  506 */     if (locationUniform != null) locationUniform.method_1255(x, y); 
/*  507 */     if (sizeUniform != null) sizeUniform.method_1255(size, size); 
/*  508 */     if (radiusUniform != null) radiusUniform.method_1251(radius); 
/*  509 */     if (alphaUniform != null) alphaUniform.method_1251(alpha); 
/*  510 */     if (uUniform != null) uUniform.method_1251(0.125F); 
/*  511 */     if (vUniform != null) vUniform.method_1251(0.125F); 
/*  512 */     if (wUniform != null) wUniform.method_1251(0.125F); 
/*  513 */     if (hUniform != null) hUniform.method_1251(0.125F); 
/*  514 */     if (hurtPercentUniform != null) hurtPercentUniform.method_1251(hurtPercent);
/*      */     
/*  516 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1585);
/*      */     
/*  518 */     buffer.method_22918(matrix, x, y, 0.0F).method_22913(0.0F, 0.0F);
/*  519 */     buffer.method_22918(matrix, x, y + size, 0.0F).method_22913(0.0F, 1.0F);
/*  520 */     buffer.method_22918(matrix, x + size, y + size, 0.0F).method_22913(1.0F, 1.0F);
/*  521 */     buffer.method_22918(matrix, x + size, y, 0.0F).method_22913(1.0F, 0.0F);
/*      */     
/*  523 */     RenderSystem.setShader(ShaderUtils.face);
/*  524 */     class_286.method_43433(buffer.method_60800());
/*      */     
/*  526 */     drawHeadOverlay(matrices, skinTexture, x, y, size, radius, alpha, hurtPercent);
/*      */     
/*  528 */     RenderSystem.setShaderTexture(0, 0);
/*  529 */     RenderSystem.disableBlend();
/*      */   }
/*      */   
/*      */   private static void drawHeadOverlay(class_4587 matrices, class_2960 skinTexture, float x, float y, float size, float radius, float alpha, float hurtPercent) {
/*  533 */     RenderSystem.setShaderTexture(0, skinTexture);
/*      */     
/*  535 */     class_5944 shader = mc.method_62887().method_62947(ShaderUtils.face);
/*  536 */     Matrix4f matrix = matrices.method_23760().method_23761();
/*      */     
/*  538 */     class_284 locationUniform = shader.method_34582("location");
/*  539 */     class_284 sizeUniform = shader.method_34582("size");
/*  540 */     class_284 radiusUniform = shader.method_34582("radius");
/*  541 */     class_284 alphaUniform = shader.method_34582("alpha");
/*  542 */     class_284 uUniform = shader.method_34582("u");
/*  543 */     class_284 vUniform = shader.method_34582("v");
/*  544 */     class_284 wUniform = shader.method_34582("w");
/*  545 */     class_284 hUniform = shader.method_34582("h");
/*  546 */     class_284 hurtPercentUniform = shader.method_34582("hurtPercent");
/*      */     
/*  548 */     if (locationUniform != null) locationUniform.method_1255(x, y); 
/*  549 */     if (sizeUniform != null) sizeUniform.method_1255(size, size); 
/*  550 */     if (radiusUniform != null) radiusUniform.method_1251(radius); 
/*  551 */     if (alphaUniform != null) alphaUniform.method_1251(alpha); 
/*  552 */     if (uUniform != null) uUniform.method_1251(0.625F); 
/*  553 */     if (vUniform != null) vUniform.method_1251(0.125F); 
/*  554 */     if (wUniform != null) wUniform.method_1251(0.125F); 
/*  555 */     if (hUniform != null) hUniform.method_1251(0.125F); 
/*  556 */     if (hurtPercentUniform != null) hurtPercentUniform.method_1251(hurtPercent);
/*      */     
/*  558 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1585);
/*      */     
/*  560 */     buffer.method_22918(matrix, x, y, 0.0F).method_22913(0.0F, 0.0F);
/*  561 */     buffer.method_22918(matrix, x, y + size, 0.0F).method_22913(0.0F, 1.0F);
/*  562 */     buffer.method_22918(matrix, x + size, y + size, 0.0F).method_22913(1.0F, 1.0F);
/*  563 */     buffer.method_22918(matrix, x + size, y, 0.0F).method_22913(1.0F, 0.0F);
/*      */     
/*  565 */     RenderSystem.setShader(ShaderUtils.face);
/*  566 */     class_286.method_43433(buffer.method_60800());
/*      */   }
/*      */   
/*      */   private static class_2960 getSkinTexture(class_1657 player) {
/*  570 */     if (mc.method_1562() == null) {
/*  571 */       return class_1068.method_4648(player.method_5667()).comp_1626();
/*      */     }
/*      */     
/*  574 */     class_640 entry = mc.method_1562().method_2871(player.method_5667());
/*  575 */     if (entry != null) {
/*  576 */       return entry.method_52810().comp_1626();
/*      */     }
/*      */     
/*  579 */     return class_1068.method_4648(player.method_5667()).comp_1626();
/*      */   }
/*      */   
/*      */   private static class_2960 getSkinTextureByName(String username) {
/*  583 */     String key = username.toLowerCase(Locale.ROOT);
/*  584 */     class_2960 cachedTexture = skinCache.get(key);
/*  585 */     if (cachedTexture != null) {
/*  586 */       return cachedTexture;
/*      */     }
/*      */     
/*  589 */     if (mc.method_1562() != null) {
/*  590 */       for (class_640 entry : mc.method_1562().method_2880()) {
/*  591 */         if (entry.method_2966().getName().equalsIgnoreCase(username)) {
/*  592 */           class_2960 class_2960 = entry.method_52810().comp_1626();
/*  593 */           skinCache.put(key, class_2960);
/*  594 */           return class_2960;
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*  599 */     if (mc.field_1687 != null) {
/*  600 */       for (class_1657 player : mc.field_1687.method_18456()) {
/*  601 */         if (player.method_5477().getString().equalsIgnoreCase(username)) {
/*  602 */           class_2960 class_2960 = getSkinTexture(player);
/*  603 */           skinCache.put(key, class_2960);
/*  604 */           return class_2960;
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*  609 */     class_2960 texture = class_1068.method_4648(UUID.nameUUIDFromBytes(username.getBytes())).comp_1626();
/*  610 */     skinCache.put(key, texture);
/*  611 */     return texture;
/*      */   }
/*      */   
/*      */   private static class_2960 getSkinTextureByUUID(UUID uuid) {
/*  615 */     String key = uuid.toString();
/*      */     
/*  617 */     if (skinCache.containsKey(key)) {
/*  618 */       return skinCache.get(key);
/*      */     }
/*      */     
/*  621 */     if (mc.method_1562() != null) {
/*  622 */       class_640 entry = mc.method_1562().method_2871(uuid);
/*  623 */       if (entry != null) {
/*  624 */         class_2960 texture = entry.method_52810().comp_1626();
/*  625 */         skinCache.put(key, texture);
/*  626 */         return texture;
/*      */       } 
/*      */     } 
/*      */     
/*  630 */     if (mc.field_1687 != null) {
/*  631 */       class_1657 player = mc.field_1687.method_18470(uuid);
/*  632 */       if (player != null) {
/*  633 */         class_2960 texture = getSkinTexture(player);
/*  634 */         skinCache.put(key, texture);
/*  635 */         return texture;
/*      */       } 
/*      */     } 
/*      */     
/*  639 */     return class_1068.method_4648(uuid).comp_1626();
/*      */   }
/*      */   
/*      */   public static void clearSkinCache() {
/*  643 */     skinCache.clear();
/*      */   }
/*      */   
/*      */   public static void removeSkinFromCache(String username) {
/*  647 */     skinCache.remove(username.toLowerCase(Locale.ROOT));
/*      */   }
/*      */   
/*      */   public static void drawRoundedRect(class_4587 matrices, float x, float y, float width, float height, float radius, int color) {
/*  651 */     drawRoundedRect(matrices, x, y, width, height, radius, radius, radius, radius, color);
/*      */   }
/*      */   
/*      */   public static void drawDefaultHudElementRects(class_4587 matrices, float x, float y, float width, float height, int themeColor) {
/*  655 */     drawDefaultHudElementRects(matrices, x, y, width, height, themeColor, true);
/*      */   }
/*      */   
/*      */   public static void drawDefaultHudElementRects(class_4587 matrices, float x, float y, float width, float height, int themeColor, boolean drawPattern) {
/*  659 */     drawDefaultHudThemedPanel(matrices, x, y, width, height, 3.0F, 3.5F, themeColor);
/*  660 */     if (drawPattern) {
/*  661 */       drawHudSquarePattern(matrices, x, y, width, height, themeColor);
/*      */     }
/*  663 */     drawRoundedRect(matrices, x + width - 14.5F, y + 3.0F, 10.0F, 10.0F, 2.0F, ColorUtils.darken(themeColor, 0.4F));
/*      */   }
/*      */   
/*      */   public static void drawHudSquarePattern(class_4587 matrices, float x, float y, float width, float height, int themeColor) {
/*  667 */     if (width <= 6.0F || height <= 6.0F)
/*      */       return; 
/*  669 */     float clipX = x - 1.0F;
/*  670 */     float clipY = y + 1.0F;
/*  671 */     float clipW = Math.max(1.0F, width - 2.0F);
/*  672 */     float clipH = Math.max(1.0F, height - 2.0F);
/*  673 */     float themeAlphaMul = (themeColor >>> 24 & 0xFF) / 255.0F;
/*  674 */     if (themeAlphaMul <= 0.001F) {
/*      */       return;
/*      */     }
/*      */     
/*  678 */     if (clipH <= 20.0F) {
/*  679 */       float[][] compactSlots = { { 0.05F, 0.08F, 8.6F }, { 0.92F, 0.1F, 8.8F }, { 0.16F, 0.78F, 6.3F }, { 0.77F, 0.8F, 6.5F }, { 0.31F, 0.18F, 6.0F }, { 0.58F, 0.74F, 5.8F }, { 0.45F, 0.45F, 5.1F }, { 0.86F, 0.46F, 5.3F }, { 0.23F, 0.52F, 4.9F }, { 0.67F, 0.3F, 5.0F }, { 0.11F, 0.34F, 5.5F }, { 0.38F, 0.7F, 5.2F }, { 0.72F, 0.16F, 5.7F }, { 0.95F, 0.68F, 5.1F } };
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
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  696 */       float f = Math.min(compactSlots.length, 3.7F + 
/*      */           
/*  698 */           Math.max(0.0F, (clipW - 84.0F) / 32.0F));
/*      */       
/*  700 */       int i = ColorUtils.setAlphaColor(
/*  701 */           ColorUtils.darken(themeColor, 0.62F), 
/*  702 */           Math.max(0, Math.min(255, (int)(82.0F * themeAlphaMul))));
/*      */ 
/*      */       
/*  705 */       ScissorUtils.push();
/*  706 */       ScissorUtils.setFromComponentCoordinates(clipX, clipY, clipW, clipH);
/*      */       
/*  708 */       try { for (int j = 0; j < compactSlots.length; j++) {
/*  709 */           float reveal = f - j;
/*  710 */           if (reveal > 0.0F) {
/*      */ 
/*      */ 
/*      */             
/*  714 */             float alphaMul = Math.max(0.0F, Math.min(1.0F, reveal));
/*  715 */             alphaMul = alphaMul * alphaMul * (3.0F - 2.0F * alphaMul);
/*  716 */             if (alphaMul > 0.02F)
/*      */             
/*      */             { 
/*      */               
/*  720 */               float size = compactSlots[j][2];
/*  721 */               float px = clipX + 0.8F + compactSlots[j][0] * Math.max(1.0F, clipW - size + 1.6F);
/*  722 */               float py = clipY - 1.2F + compactSlots[j][1] * Math.max(1.0F, clipH - size + 2.4F);
/*      */               
/*  724 */               int outlineAlpha = Math.max(0, Math.min(255, (int)(86.0F * alphaMul * themeAlphaMul)));
/*  725 */               if (outlineAlpha > 0)
/*      */               
/*      */               { 
/*  728 */                 int outlineColor = ColorUtils.setAlphaColor(i, outlineAlpha);
/*  729 */                 drawRoundedRectOutline(matrices, px, py, size, size, 0.0F, 0.5F, outlineColor, outlineColor, outlineColor, outlineColor); }  } 
/*      */           } 
/*      */         }  }
/*  732 */       finally { ScissorUtils.unset();
/*  733 */         ScissorUtils.pop(); }
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*  738 */     float[][] slots = { { 0.05F, 4.0F, 9.6F }, { 0.87F, 4.0F, 9.2F }, { 0.5F, 8.0F, 7.4F }, { 0.18F, 13.0F, 6.2F }, { 0.72F, 13.0F, 6.0F }, { 0.07F, 21.0F, 5.6F }, { 0.91F, 21.0F, 5.8F }, { 0.24F, 30.0F, 5.4F }, { 0.66F, 30.0F, 5.5F }, { 0.04F, 38.0F, 6.8F }, { 0.9F, 38.0F, 7.0F }, { 0.15F, 47.0F, 5.4F }, { 0.78F, 47.0F, 5.5F }, { 0.08F, 56.0F, 5.1F }, { 0.92F, 56.0F, 5.2F }, { 0.23F, 65.0F, 5.8F }, { 0.69F, 65.0F, 5.9F }, { 0.52F, 71.0F, 7.2F }, { 0.06F, 74.0F, 7.6F }, { 0.88F, 74.0F, 7.4F }, { 0.14F, 85.0F, 5.7F }, { 0.82F, 85.0F, 5.8F }, { 0.09F, 97.0F, 6.5F }, { 0.9F, 98.0F, 6.6F } };
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
/*      */ 
/*      */     
/*  765 */     int baseCount = 10;
/*  766 */     float extraHeight = Math.max(0.0F, clipH - 24.0F);
/*  767 */     float desiredCount = Math.min(slots.length, baseCount + extraHeight / 10.0F);
/*  768 */     float panelAlpha = Math.max(0.0F, Math.min(1.0F, (clipH - 10.0F) / 16.0F));
/*  769 */     panelAlpha = panelAlpha * panelAlpha * (3.0F - 2.0F * panelAlpha);
/*  770 */     int outlineColorBase = ColorUtils.setAlphaColor(ColorUtils.darken(themeColor, 0.72F), Math.max(0, Math.min(255, (int)(40.0F * themeAlphaMul))));
/*      */     
/*  772 */     ScissorUtils.push();
/*  773 */     ScissorUtils.setFromComponentCoordinates(clipX, clipY, clipW, clipH);
/*      */     
/*  775 */     try { for (int i = 0; i < slots.length; i++) {
/*  776 */         float reveal = desiredCount - i;
/*  777 */         if (reveal <= 0.0F) {
/*      */           continue;
/*      */         }
/*      */         
/*  781 */         float alphaMul = Math.max(0.0F, Math.min(1.0F, reveal));
/*  782 */         alphaMul = alphaMul * alphaMul * (3.0F - 2.0F * alphaMul);
/*  783 */         alphaMul *= panelAlpha;
/*  784 */         if (alphaMul <= 0.015F) {
/*      */           continue;
/*      */         }
/*      */         
/*  788 */         float size = slots[i][2];
/*  789 */         float px = clipX + 2.0F + slots[i][0] * Math.max(1.0F, clipW - size - 4.0F);
/*  790 */         float py = clipY + slots[i][1];
/*  791 */         float bottomLimit = clipY + clipH - 1.0F;
/*  792 */         if (py >= bottomLimit) {
/*      */           continue;
/*      */         }
/*  795 */         if (py + size > bottomLimit) {
/*  796 */           float visible = Math.max(0.0F, Math.min(1.0F, (bottomLimit - py) / Math.max(1.0F, size)));
/*  797 */           visible = visible * visible * (3.0F - 2.0F * visible);
/*  798 */           alphaMul *= visible;
/*  799 */           if (alphaMul <= 0.015F) {
/*      */             continue;
/*      */           }
/*      */         } 
/*      */         
/*  804 */         int outlineAlpha = Math.max(0, Math.min(255, (int)(58.0F * alphaMul * themeAlphaMul)));
/*  805 */         if (outlineAlpha > 0) {
/*      */ 
/*      */           
/*  808 */           int outlineColor = ColorUtils.setAlphaColor(outlineColorBase, outlineAlpha);
/*  809 */           drawRoundedRectOutline(matrices, px, py, size, size, 0.0F, 0.55F, outlineColor, outlineColor, outlineColor, outlineColor);
/*      */         }  continue;
/*      */       }  }
/*  812 */     finally { ScissorUtils.unset();
/*  813 */       ScissorUtils.pop(); }
/*      */   
/*      */   }
/*      */   
/*      */   public static void drawDefaultHudInfoBox(class_4587 matrices, float x, float y, float width, int outerColor, int innerColor) {
/*  818 */     drawRoundedRect(matrices, x - 0.25F, y - 1.25F, width + 0.5F, 9.0F, 1.3F, outerColor);
/*  819 */     drawRoundedRect(matrices, x, y - 1.0F, width, 8.5F, 1.0F, innerColor);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void drawDefaultHudPanel(class_4587 matrices, float x, float y, float width, float height, float gradientRadius, float borderRadius, int borderColor, int topColor, int bottomColor) {
/*  825 */     drawRoundedRect(matrices, x - 0.5F, y - 0.5F, width + 1.0F, height + 1.0F, borderRadius, borderColor);
/*  826 */     drawGradientRect(matrices, x, y, width, height, gradientRadius, topColor, bottomColor);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void drawDefaultHudThemedPanel(class_4587 matrices, float x, float y, float width, float height, float gradientRadius, float borderRadius, int themeColor) {
/*  831 */     drawDefaultHudPanel(matrices, x, y, width, height, gradientRadius, borderRadius, 
/*      */         
/*  833 */         ColorUtils.rgba(50, 50, 50, 255), 
/*  834 */         ColorUtils.darken(themeColor, 0.15F), 
/*  835 */         ColorUtils.darken(themeColor, 0.05F));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void drawWaveHudHeader(class_4587 matrices, float x, float y, float width, float height, float radius, float shadowRadius, float shadowSoftness, int leftTop, int leftBottom, int centerTop, int centerBottom, int rightTop, int rightBottom) {
/*  842 */     drawShadow6(matrices, x, y, width, height, shadowRadius, shadowSoftness, leftTop, leftBottom, centerTop, centerBottom, rightTop, rightBottom);
/*      */     
/*  844 */     drawGradient6Rect(matrices, x, y, width, height, radius, leftTop, leftBottom, centerTop, centerBottom, rightTop, rightBottom);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void drawWaveHudPanel(class_4587 matrices, float x, float y, float width, float height, int bgColor, float headerHeight, float headerRadius, float shadowRadius, float shadowSoftness, int leftTop, int leftBottom, int centerTop, int centerBottom, int rightTop, int rightBottom) {
/*  851 */     drawRoundedRect(matrices, x, y, width, height, 0.0F, bgColor);
/*  852 */     drawWaveHudHeader(matrices, x, y, width, headerHeight, headerRadius, shadowRadius, shadowSoftness, leftTop, leftBottom, centerTop, centerBottom, rightTop, rightBottom);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void drawTargetHudWaveFrame(class_4587 matrices, float x, float y, float width, float height, float padding, float entityBoxSize, float alpha) {
/*  858 */     drawRoundedRect(matrices, x, y, width, height, 0.0F, ColorUtils.applyAlpha(ColorUtils.rgba(40, 40, 40, 255), alpha));
/*  859 */     drawRoundedRect(matrices, x + padding, y + padding, width - padding * 2.0F, height - padding * 2.0F, 0.0F, ColorUtils.applyAlpha(ColorUtils.rgba(20, 20, 20, 255), alpha));
/*  860 */     drawRoundedRect(matrices, x + padding + 2.0F, y + padding + 2.0F, entityBoxSize, entityBoxSize, 0.0F, ColorUtils.applyAlpha(ColorUtils.rgba(40, 40, 40, 255), alpha));
/*  861 */     drawRoundedRect(matrices, x + padding + 3.0F, y + padding + 3.0F, entityBoxSize - 2.0F, entityBoxSize - 2.0F, 0.0F, ColorUtils.applyAlpha(ColorUtils.rgba(25, 25, 25, 255), alpha));
/*      */   }
/*      */   
/*      */   public static void drawTargetHudDefaultPlaceholder(class_4587 matrices, float x, float y, float alpha) {
/*  865 */     drawRoundedRect(matrices, x - 1.0F, y - 1.0F, 22.0F, 22.0F, 1.0F, ColorUtils.applyAlpha(ColorUtils.rgba(21, 21, 21, 255), alpha));
/*      */   }
/*      */   
/*      */   public static void drawTargetHudHealthBars(class_4587 matrices, float x, float y, float width, float trailProgress, float progress, int themeColor, int themecolor2, float alpha) {
/*  869 */     drawRoundedRect(matrices, x, y, width, 5.5F, 1.25F, ColorUtils.applyAlpha(ColorUtils.darken(themeColor, 0.5F), alpha * 0.8F));
/*  870 */     drawRoundedRect(matrices, x, y, width * trailProgress, 5.5F, 1.25F, ColorUtils.applyAlpha(ColorUtils.darken(themeColor, 0.8F), alpha * 0.8F));
/*  871 */     drawGradientRect(matrices, x, y, width * progress, 5.5F, 1.25F, ColorUtils.applyAlpha(themeColor, alpha), ColorUtils.applyAlpha(themecolor2, alpha), true);
/*      */   }
/*      */   
/*      */   public static void drawTargetHudGoldenBars(class_4587 matrices, float x, float y, float width, float height, float trailProgress, float progress, float alpha, float goldenAlpha) {
/*  875 */     int goldenColor = ColorUtils.rgba(255, 215, 0, 255);
/*  876 */     drawRoundedRect(matrices, x, y, width * trailProgress, height, 1.25F, ColorUtils.applyAlpha(ColorUtils.darken(goldenColor, 0.65F), alpha * goldenAlpha * 0.8F));
/*  877 */     drawGradientRect(matrices, x, y, width * progress, height, 1.25F, ColorUtils.applyAlpha(ColorUtils.darken(goldenColor, 0.55F), alpha * goldenAlpha), ColorUtils.applyAlpha(goldenColor, alpha * goldenAlpha), true);
/*      */   }
/*      */   
/*      */   public static void drawTargetHudHeartBase(class_4587 matrices, float x, float y, float alpha) {
/*  881 */     drawRoundedRect(matrices, x, y, 6.2F, 4.5F, 0.0F, ColorUtils.applyAlpha(ColorUtils.rgba(0, 0, 0, 255), alpha));
/*      */   }
/*      */   
/*      */   public static void drawTargetHudHeartFill(class_4587 matrices, float x, float y, float width, int heartColor, int shadowColor) {
/*  885 */     drawShadow(matrices, x + 1.0F, y + 1.0F, width, 2.0F, 0.0F, 8.0F, shadowColor);
/*  886 */     drawRoundedRect(matrices, x, y, width + 1.2F, 4.5F, 0.0F, heartColor);
/*      */   }
/*      */   
/*      */   public static void drawKeyStrokeRect(class_4587 matrices, float x, float y, float width, float height, float radius, int color) {
/*  890 */     drawRoundedRect(matrices, x, y, width, height, radius, color);
/*      */   }
/*      */   
/*      */   public static void drawRoundedRect(class_4587 matrices, float x, float y, float width, float height, float topLeft, float topRight, float bottomRight, float bottomLeft, int color) {
/*  894 */     RenderSystem.enableBlend();
/*  895 */     RenderSystem.defaultBlendFunc();
/*      */     
/*  897 */     class_5944 shader = mc.method_62887().method_62947(ShaderUtils.roundedRect);
/*      */     
/*  899 */     Matrix4f matrix = matrices.method_23760().method_23761();
/*      */     
/*  901 */     class_284 sizeUniform = shader.method_34582("Size");
/*  902 */     class_284 radiusUniform = shader.method_34582("Radius");
/*      */     
/*  904 */     if (sizeUniform != null) sizeUniform.method_1255(width, height); 
/*  905 */     if (radiusUniform != null) radiusUniform.method_35657(topLeft, topRight, bottomRight, bottomLeft);
/*      */     
/*  907 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1576);
/*      */     
/*  909 */     int alpha = color >> 24 & 0xFF;
/*  910 */     if (alpha == 0) alpha = 255; 
/*  911 */     float r = (color >> 16 & 0xFF) / 255.0F;
/*  912 */     float g = (color >> 8 & 0xFF) / 255.0F;
/*  913 */     float b = (color & 0xFF) / 255.0F;
/*  914 */     float a = alpha / 255.0F;
/*      */     
/*  916 */     buffer.method_22918(matrix, x, y, 0.0F).method_22915(r, g, b, a);
/*  917 */     buffer.method_22918(matrix, x, y + height, 0.0F).method_22915(r, g, b, a);
/*  918 */     buffer.method_22918(matrix, x + width, y + height, 0.0F).method_22915(r, g, b, a);
/*  919 */     buffer.method_22918(matrix, x + width, y, 0.0F).method_22915(r, g, b, a);
/*      */     
/*  921 */     RenderSystem.setShader(ShaderUtils.roundedRect);
/*  922 */     class_286.method_43433(buffer.method_60800());
/*      */     
/*  924 */     RenderSystem.disableBlend();
/*      */   }
/*      */   
/*      */   public static void drawRoundCircle(class_4587 matrices, float x, float y, float radius, int color) {
/*  928 */     Matrix4f matrix = matrices.method_23760().method_23761();
/*  929 */     drawRoundedRect(matrices, x - radius / 2.0F, y - radius / 2.0F, radius, radius, radius / 2.0F - 0.5F, color);
/*      */   }
/*      */   
/*      */   public static void drawRingArc(class_4587 matrices, float x, float y, float size, float thickness, float startDeg, float endDeg, int color) {
/*  933 */     if (size <= 0.0F || thickness <= 0.0F)
/*      */       return; 
/*  935 */     float radius = size / 2.0F;
/*  936 */     float start = (float)Math.toRadians(startDeg);
/*  937 */     float end = (float)Math.toRadians(endDeg);
/*  938 */     float twoPi = 6.2831855F;
/*  939 */     if (start < 0.0F) start += twoPi; 
/*  940 */     if (end < 0.0F) end += twoPi; 
/*  941 */     for (; end < start; end += twoPi);
/*  942 */     if (end - start <= 1.0E-4F) {
/*  943 */       end = start + twoPi;
/*      */     }
/*      */     
/*  946 */     RenderSystem.enableBlend();
/*  947 */     RenderSystem.defaultBlendFunc();
/*      */     
/*  949 */     class_5944 shader = mc.method_62887().method_62947(ShaderUtils.ringArc);
/*      */     
/*  951 */     class_284 sizeUniform = shader.method_34582("Size");
/*  952 */     class_284 radiusUniform = shader.method_34582("Radius");
/*  953 */     class_284 thicknessUniform = shader.method_34582("Thickness");
/*  954 */     class_284 startUniform = shader.method_34582("StartAngle");
/*  955 */     class_284 endUniform = shader.method_34582("EndAngle");
/*  956 */     class_284 smoothnessUniform = shader.method_34582("Smoothness");
/*  957 */     class_284 colorModulatorUniform = shader.method_34582("ColorModulator");
/*      */     
/*  959 */     if (sizeUniform != null) sizeUniform.method_1255(size, size); 
/*  960 */     if (radiusUniform != null) radiusUniform.method_1251(radius); 
/*  961 */     if (thicknessUniform != null) thicknessUniform.method_1251(thickness); 
/*  962 */     if (startUniform != null) startUniform.method_1251(start); 
/*  963 */     if (endUniform != null) endUniform.method_1251(end); 
/*  964 */     if (smoothnessUniform != null) smoothnessUniform.method_1251(Math.min(1.0F, thickness * 0.5F)); 
/*  965 */     if (colorModulatorUniform != null) colorModulatorUniform.method_35657(1.0F, 1.0F, 1.0F, 1.0F);
/*      */     
/*  967 */     Matrix4f matrix = matrices.method_23760().method_23761();
/*  968 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1576);
/*      */     
/*  970 */     int alpha = color >> 24 & 0xFF;
/*  971 */     if (alpha == 0) alpha = 255; 
/*  972 */     float r = (color >> 16 & 0xFF) / 255.0F;
/*  973 */     float g = (color >> 8 & 0xFF) / 255.0F;
/*  974 */     float b = (color & 0xFF) / 255.0F;
/*  975 */     float a = alpha / 255.0F;
/*      */     
/*  977 */     buffer.method_22918(matrix, x, y, 0.0F).method_22915(r, g, b, a);
/*  978 */     buffer.method_22918(matrix, x, y + size, 0.0F).method_22915(r, g, b, a);
/*  979 */     buffer.method_22918(matrix, x + size, y + size, 0.0F).method_22915(r, g, b, a);
/*  980 */     buffer.method_22918(matrix, x + size, y, 0.0F).method_22915(r, g, b, a);
/*      */     
/*  982 */     RenderSystem.setShader(ShaderUtils.ringArc);
/*  983 */     class_286.method_43433(buffer.method_60800());
/*      */     
/*  985 */     RenderSystem.disableBlend();
/*      */   }
/*      */   
/*      */   public static void drawGradientRect(class_4587 matrices, float x, float y, float width, float height, float topLeft, float topRight, float bottomRight, float bottomLeft, int topLeftColor, int topRightColor, int bottomLeftColor, int bottomRightColor) {
/*  989 */     RenderSystem.enableBlend();
/*  990 */     RenderSystem.defaultBlendFunc();
/*      */     
/*  992 */     class_5944 shader = mc.method_62887().method_62947(ShaderUtils.gradientRect);
/*      */     
/*  994 */     Matrix4f matrix = matrices.method_23760().method_23761();
/*      */     
/*  996 */     class_284 sizeUniform = shader.method_34582("Size");
/*  997 */     class_284 radiusUniform = shader.method_34582("Radius");
/*  998 */     class_284 smoothnessUniform = shader.method_34582("Smoothness");
/*  999 */     class_284 colorModulatorUniform = shader.method_34582("ColorModulator");
/* 1000 */     class_284 topLeftColorUniform = shader.method_34582("TopLeftColor");
/* 1001 */     class_284 bottomLeftColorUniform = shader.method_34582("BottomLeftColor");
/* 1002 */     class_284 topRightColorUniform = shader.method_34582("TopRightColor");
/* 1003 */     class_284 bottomRightColorUniform = shader.method_34582("BottomRightColor");
/*      */     
/* 1005 */     if (sizeUniform != null) sizeUniform.method_1255(width, height); 
/* 1006 */     if (radiusUniform != null) radiusUniform.method_35657(topLeft, topRight, bottomRight, bottomLeft); 
/* 1007 */     if (smoothnessUniform != null) smoothnessUniform.method_1251(1.0F); 
/* 1008 */     if (colorModulatorUniform != null) colorModulatorUniform.method_35657(1.0F, 1.0F, 1.0F, 1.0F);
/*      */     
/* 1010 */     int tlAlpha = topLeftColor >> 24 & 0xFF;
/* 1011 */     if (tlAlpha == 0) tlAlpha = 255; 
/* 1012 */     if (topLeftColorUniform != null) topLeftColorUniform.method_35657((topLeftColor >> 16 & 0xFF) / 255.0F, (topLeftColor >> 8 & 0xFF) / 255.0F, (topLeftColor & 0xFF) / 255.0F, tlAlpha / 255.0F);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1019 */     int blAlpha = bottomLeftColor >> 24 & 0xFF;
/* 1020 */     if (blAlpha == 0) blAlpha = 255; 
/* 1021 */     if (bottomLeftColorUniform != null) bottomLeftColorUniform.method_35657((bottomLeftColor >> 16 & 0xFF) / 255.0F, (bottomLeftColor >> 8 & 0xFF) / 255.0F, (bottomLeftColor & 0xFF) / 255.0F, blAlpha / 255.0F);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1028 */     int trAlpha = topRightColor >> 24 & 0xFF;
/* 1029 */     if (trAlpha == 0) trAlpha = 255; 
/* 1030 */     if (topRightColorUniform != null) topRightColorUniform.method_35657((topRightColor >> 16 & 0xFF) / 255.0F, (topRightColor >> 8 & 0xFF) / 255.0F, (topRightColor & 0xFF) / 255.0F, trAlpha / 255.0F);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1037 */     int brAlpha = bottomRightColor >> 24 & 0xFF;
/* 1038 */     if (brAlpha == 0) brAlpha = 255; 
/* 1039 */     if (bottomRightColorUniform != null) bottomRightColorUniform.method_35657((bottomRightColor >> 16 & 0xFF) / 255.0F, (bottomRightColor >> 8 & 0xFF) / 255.0F, (bottomRightColor & 0xFF) / 255.0F, brAlpha / 255.0F);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1046 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
/*      */     
/* 1048 */     buffer.method_22918(matrix, x, y, 0.0F).method_22913(0.0F, 0.0F).method_22915(1.0F, 1.0F, 1.0F, 1.0F);
/* 1049 */     buffer.method_22918(matrix, x, y + height, 0.0F).method_22913(0.0F, 1.0F).method_22915(1.0F, 1.0F, 1.0F, 1.0F);
/* 1050 */     buffer.method_22918(matrix, x + width, y + height, 0.0F).method_22913(1.0F, 1.0F).method_22915(1.0F, 1.0F, 1.0F, 1.0F);
/* 1051 */     buffer.method_22918(matrix, x + width, y, 0.0F).method_22913(1.0F, 0.0F).method_22915(1.0F, 1.0F, 1.0F, 1.0F);
/*      */     
/* 1053 */     RenderSystem.setShader(ShaderUtils.gradientRect);
/* 1054 */     class_286.method_43433(buffer.method_60800());
/*      */     
/* 1056 */     RenderSystem.disableBlend();
/*      */   }
/*      */   
/*      */   public static void drawGradientRect(class_4587 matrices, float x, float y, float width, float height, float radius, int topLeftColor, int topRightColor, int bottomLeftColor, int bottomRightColor) {
/* 1060 */     drawGradientRect(matrices, x, y, width, height, radius, radius, radius, radius, topLeftColor, topRightColor, bottomLeftColor, bottomRightColor);
/*      */   }
/*      */   
/*      */   public static void drawGradientRect(class_4587 matrices, float x, float y, float width, float height, float radius, int topColor, int bottomColor) {
/* 1064 */     drawGradientRect(matrices, x, y, width, height, radius, radius, radius, radius, topColor, topColor, bottomColor, bottomColor);
/*      */   }
/*      */   
/*      */   public static void drawGradientRect(class_4587 matrices, float x, float y, float width, float height, int topColor, int bottomColor) {
/* 1068 */     drawGradientRect(matrices, x, y, width, height, 0.0F, 0.0F, 0.0F, 0.0F, topColor, topColor, bottomColor, bottomColor);
/*      */   }
/*      */   
/*      */   public static void drawGradientRect(class_4587 matrices, float x, float y, float width, float height, float radius, int leftColor, int rightColor, boolean horizontal) {
/* 1072 */     if (horizontal) {
/* 1073 */       drawGradientRect(matrices, x, y, width, height, radius, radius, radius, radius, leftColor, rightColor, leftColor, rightColor);
/*      */     } else {
/* 1075 */       drawGradientRect(matrices, x, y, width, height, radius, radius, radius, radius, leftColor, leftColor, rightColor, rightColor);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void drawRoundedRectOutline(class_4587 matrices, float x, float y, float width, float height, float topLeft, float topRight, float bottomRight, float bottomLeft, float outline, int outlineColor) {
/* 1082 */     drawRoundedRectOutline(matrices, x, y, width, height, topLeft, topRight, bottomRight, bottomLeft, outline, outlineColor, outlineColor, outlineColor, outlineColor);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void drawRoundedRectOutline(class_4587 matrices, float x, float y, float width, float height, float radius, float outline, int topLeftColor, int topRightColor, int bottomLeftColor, int bottomRightColor) {
/* 1088 */     drawRoundedRectOutline(matrices, x, y, width, height, radius, radius, radius, radius, outline, topLeftColor, topRightColor, bottomLeftColor, bottomRightColor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void drawRoundedRectOutline(class_4587 matrices, float x, float y, float width, float height, float topLeft, float topRight, float bottomRight, float bottomLeft, float outline, int topLeftColor, int topRightColor, int bottomLeftColor, int bottomRightColor) {
/* 1095 */     if (outline <= 0.0F)
/*      */       return; 
/* 1097 */     RenderSystem.enableBlend();
/* 1098 */     RenderSystem.defaultBlendFunc();
/*      */     
/* 1100 */     class_5944 shader = mc.method_62887().method_62947(ShaderUtils.roundedRectOutline);
/*      */     
/* 1102 */     Matrix4f matrix = matrices.method_23760().method_23761();
/*      */     
/* 1104 */     class_284 sizeUniform = shader.method_34582("Size");
/* 1105 */     class_284 radiusUniform = shader.method_34582("Radius");
/* 1106 */     class_284 smoothnessUniform = shader.method_34582("Smoothness");
/* 1107 */     class_284 colorModulatorUniform = shader.method_34582("ColorModulator");
/* 1108 */     class_284 outlineUniform = shader.method_34582("Outline");
/* 1109 */     class_284 topLeftColorUniform = shader.method_34582("TopLeftColor");
/* 1110 */     class_284 bottomLeftColorUniform = shader.method_34582("BottomLeftColor");
/* 1111 */     class_284 topRightColorUniform = shader.method_34582("TopRightColor");
/* 1112 */     class_284 bottomRightColorUniform = shader.method_34582("BottomRightColor");
/*      */     
/* 1114 */     if (sizeUniform != null) sizeUniform.method_1255(width, height); 
/* 1115 */     if (radiusUniform != null) radiusUniform.method_35657(topLeft, topRight, bottomRight, bottomLeft); 
/* 1116 */     if (smoothnessUniform != null) smoothnessUniform.method_1251(1.0F); 
/* 1117 */     if (colorModulatorUniform != null) colorModulatorUniform.method_35657(1.0F, 1.0F, 1.0F, 1.0F); 
/* 1118 */     if (outlineUniform != null) outlineUniform.method_1251(outline);
/*      */     
/* 1120 */     if (topLeftColorUniform != null) {
/* 1121 */       int a = topLeftColor >> 24 & 0xFF;
/* 1122 */       if (a == 0) a = 255; 
/* 1123 */       topLeftColorUniform.method_35657((topLeftColor >> 16 & 0xFF) / 255.0F, (topLeftColor >> 8 & 0xFF) / 255.0F, (topLeftColor & 0xFF) / 255.0F, a / 255.0F);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1130 */     if (bottomLeftColorUniform != null) {
/* 1131 */       int a = bottomLeftColor >> 24 & 0xFF;
/* 1132 */       if (a == 0) a = 255; 
/* 1133 */       bottomLeftColorUniform.method_35657((bottomLeftColor >> 16 & 0xFF) / 255.0F, (bottomLeftColor >> 8 & 0xFF) / 255.0F, (bottomLeftColor & 0xFF) / 255.0F, a / 255.0F);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1140 */     if (topRightColorUniform != null) {
/* 1141 */       int a = topRightColor >> 24 & 0xFF;
/* 1142 */       if (a == 0) a = 255; 
/* 1143 */       topRightColorUniform.method_35657((topRightColor >> 16 & 0xFF) / 255.0F, (topRightColor >> 8 & 0xFF) / 255.0F, (topRightColor & 0xFF) / 255.0F, a / 255.0F);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1150 */     if (bottomRightColorUniform != null) {
/* 1151 */       int a = bottomRightColor >> 24 & 0xFF;
/* 1152 */       if (a == 0) a = 255; 
/* 1153 */       bottomRightColorUniform.method_35657((bottomRightColor >> 16 & 0xFF) / 255.0F, (bottomRightColor >> 8 & 0xFF) / 255.0F, (bottomRightColor & 0xFF) / 255.0F, a / 255.0F);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1161 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1576);
/*      */     
/* 1163 */     buffer.method_22918(matrix, x, y, 0.0F).method_22915(1.0F, 1.0F, 1.0F, 1.0F);
/* 1164 */     buffer.method_22918(matrix, x, y + height, 0.0F).method_22915(1.0F, 1.0F, 1.0F, 1.0F);
/* 1165 */     buffer.method_22918(matrix, x + width, y + height, 0.0F).method_22915(1.0F, 1.0F, 1.0F, 1.0F);
/* 1166 */     buffer.method_22918(matrix, x + width, y, 0.0F).method_22915(1.0F, 1.0F, 1.0F, 1.0F);
/*      */     
/* 1168 */     RenderSystem.setShader(ShaderUtils.roundedRectOutline);
/* 1169 */     class_286.method_43433(buffer.method_60800());
/*      */     
/* 1171 */     RenderSystem.disableBlend();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void drawBlur(class_4587 matrices, float x, float y, float width, float height, float topLeft, float topRight, float bottomRight, float bottomLeft, int color) {
/* 1176 */     if (BlurProgram.getBuffer2() == null)
/*      */       return; 
/* 1178 */     RenderSystem.enableBlend();
/* 1179 */     RenderSystem.defaultBlendFunc();
/*      */     
/* 1181 */     Matrix4f matrix = matrices.method_23760().method_23761();
/*      */     
/* 1183 */     class_5944 shader = mc.method_62887().method_62947(ShaderUtils.roundedTexture);
/*      */     
/* 1185 */     class_284 sizeUniform = shader.method_34582("Size");
/* 1186 */     class_284 radiusUniform = shader.method_34582("Radius");
/* 1187 */     class_284 smoothnessUniform = shader.method_34582("Smoothness");
/* 1188 */     class_284 colorModulatorUniform = shader.method_34582("ColorModulator");
/*      */     
/* 1190 */     if (sizeUniform != null) sizeUniform.method_1255(width, height); 
/* 1191 */     if (radiusUniform != null) radiusUniform.method_35657(topLeft, topRight, bottomRight, bottomLeft); 
/* 1192 */     if (smoothnessUniform != null) smoothnessUniform.method_1251(0.5F); 
/* 1193 */     if (colorModulatorUniform != null) colorModulatorUniform.method_35657(1.0F, 1.0F, 1.0F, 1.0F);
/*      */     
/* 1195 */     RenderSystem.setShaderTexture(0, BlurProgram.getTexture());
/* 1196 */     RenderSystem.setShader(ShaderUtils.roundedTexture);
/*      */     
/* 1198 */     int screenWidth = mc.method_22683().method_4486();
/* 1199 */     int screenHeight = mc.method_22683().method_4502();
/*      */     
/* 1201 */     float u1 = x / screenWidth;
/* 1202 */     float v1 = (screenHeight - y) / screenHeight;
/* 1203 */     float u2 = (x + width) / screenWidth;
/* 1204 */     float v2 = (screenHeight - y - height) / screenHeight;
/*      */     
/* 1206 */     int alpha = color >> 24 & 0xFF;
/* 1207 */     if (alpha == 0) alpha = 255; 
/* 1208 */     float r = (color >> 16 & 0xFF) / 255.0F;
/* 1209 */     float g = (color >> 8 & 0xFF) / 255.0F;
/* 1210 */     float b = (color & 0xFF) / 255.0F;
/* 1211 */     float a = alpha / 255.0F;
/*      */     
/* 1213 */     class_287 builder = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
/* 1214 */     builder.method_22918(matrix, x, y, 0.0F).method_22913(u1, v1).method_22915(r, g, b, a);
/* 1215 */     builder.method_22918(matrix, x, y + height, 0.0F).method_22913(u1, v2).method_22915(r, g, b, a);
/* 1216 */     builder.method_22918(matrix, x + width, y + height, 0.0F).method_22913(u2, v2).method_22915(r, g, b, a);
/* 1217 */     builder.method_22918(matrix, x + width, y, 0.0F).method_22913(u2, v1).method_22915(r, g, b, a);
/* 1218 */     class_286.method_43433(builder.method_60800());
/*      */     
/* 1220 */     RenderSystem.setShaderTexture(0, 0);
/* 1221 */     RenderSystem.disableBlend();
/*      */   }
/*      */   
/*      */   public static void drawBlur(class_4587 matrices, float x, float y, float width, float height, float radius, int color) {
/* 1225 */     drawBlur(matrices, x, y, width, height, radius, radius, radius, radius, color);
/*      */   }
/*      */   
/*      */   public static void startGlow(float radius, int color, GlowCallback callback, class_4587 matrices) {
/* 1229 */     int a = color >> 24 & 0xFF;
/* 1230 */     int r = color >> 16 & 0xFF;
/* 1231 */     int g = color >> 8 & 0xFF;
/* 1232 */     int b = color & 0xFF;
/* 1233 */     if (a == 0) a = 255; 
/* 1234 */     GlowProgram.getInstance().begin(radius, new Color(r, g, b, a));
/* 1235 */     callback.render();
/* 1236 */     GlowProgram.getInstance().end(matrices, callback);
/*      */   }
/*      */   
/*      */   public static void startGlow(float radius, float intensity, int color, GlowCallback callback, class_4587 matrices) {
/* 1240 */     int a = color >> 24 & 0xFF;
/* 1241 */     int r = color >> 16 & 0xFF;
/* 1242 */     int g = color >> 8 & 0xFF;
/* 1243 */     int b = color & 0xFF;
/* 1244 */     if (a == 0) a = 255; 
/* 1245 */     GlowProgram.getInstance().begin(radius, intensity, new Color(r, g, b, a));
/* 1246 */     callback.render();
/* 1247 */     GlowProgram.getInstance().end(matrices, callback);
/*      */   }
/*      */   
/*      */   public static void drawBlur(class_4587 matrices, float x, float y, float width, float height, float topLeft, float topRight, float bottomRight, float bottomLeft, float blurStrength, int color) {
/* 1251 */     BlurProgram.getInstance().request();
/* 1252 */     if (BlurProgram.getBuffer2() == null)
/*      */       return; 
/* 1254 */     BlurProgram.getInstance().setBlurOffset(blurStrength);
/*      */     
/* 1256 */     RenderSystem.enableBlend();
/* 1257 */     RenderSystem.defaultBlendFunc();
/*      */     
/* 1259 */     Matrix4f matrix = matrices.method_23760().method_23761();
/*      */     
/* 1261 */     class_5944 shader = mc.method_62887().method_62947(ShaderUtils.roundedTexture);
/*      */     
/* 1263 */     class_284 sizeUniform = shader.method_34582("Size");
/* 1264 */     class_284 radiusUniform = shader.method_34582("Radius");
/* 1265 */     class_284 smoothnessUniform = shader.method_34582("Smoothness");
/* 1266 */     class_284 colorModulatorUniform = shader.method_34582("ColorModulator");
/*      */     
/* 1268 */     if (sizeUniform != null) sizeUniform.method_1255(width, height); 
/* 1269 */     if (radiusUniform != null) radiusUniform.method_35657(topLeft, topRight, bottomRight, bottomLeft); 
/* 1270 */     if (smoothnessUniform != null) smoothnessUniform.method_1251(0.5F); 
/* 1271 */     if (colorModulatorUniform != null) colorModulatorUniform.method_35657(1.0F, 1.0F, 1.0F, 1.0F);
/*      */     
/* 1273 */     RenderSystem.setShaderTexture(0, BlurProgram.getTexture());
/* 1274 */     RenderSystem.setShader(ShaderUtils.roundedTexture);
/*      */     
/* 1276 */     int screenWidth = mc.method_22683().method_4486();
/* 1277 */     int screenHeight = mc.method_22683().method_4502();
/*      */     
/* 1279 */     float u1 = x / screenWidth;
/* 1280 */     float v1 = (screenHeight - y) / screenHeight;
/* 1281 */     float u2 = (x + width) / screenWidth;
/* 1282 */     float v2 = (screenHeight - y - height) / screenHeight;
/*      */     
/* 1284 */     int alpha = color >> 24 & 0xFF;
/* 1285 */     if (alpha == 0) alpha = 255; 
/* 1286 */     float r = (color >> 16 & 0xFF) / 255.0F;
/* 1287 */     float g = (color >> 8 & 0xFF) / 255.0F;
/* 1288 */     float b = (color & 0xFF) / 255.0F;
/* 1289 */     float a = alpha / 255.0F;
/*      */     
/* 1291 */     class_287 builder = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
/* 1292 */     builder.method_22918(matrix, x, y, 0.0F).method_22913(u1, v1).method_22915(r, g, b, a);
/* 1293 */     builder.method_22918(matrix, x, y + height, 0.0F).method_22913(u1, v2).method_22915(r, g, b, a);
/* 1294 */     builder.method_22918(matrix, x + width, y + height, 0.0F).method_22913(u2, v2).method_22915(r, g, b, a);
/* 1295 */     builder.method_22918(matrix, x + width, y, 0.0F).method_22913(u2, v1).method_22915(r, g, b, a);
/* 1296 */     class_286.method_43433(builder.method_60800());
/*      */     
/* 1298 */     RenderSystem.setShaderTexture(0, 0);
/* 1299 */     RenderSystem.disableBlend();
/*      */   }
/*      */   
/*      */   public static void drawBlur(class_4587 matrices, float x, float y, float width, float height, float radius, float blurStrength, int color) {
/* 1303 */     drawBlur(matrices, x, y, width, height, radius, radius, radius, radius, blurStrength, color);
/*      */   }
/*      */   
/*      */   public static void drawLiquidGlass(class_4587 matrices, float x, float y, float width, float height, float topLeft, float topRight, float bottomRight, float bottomLeft, int color, float globalAlpha, float fresnelPower, int fresnelColor, float baseAlpha, boolean fresnelInvert, float fresnelMix, float distortStrength, float squirt, boolean clean) {
/*      */     int textureId;
/* 1308 */     if (clean) {
/* 1309 */       textureId = mc.method_1522().method_30277();
/*      */     } else {
/* 1311 */       BlurProgram.getInstance().request();
/* 1312 */       if (BlurProgram.getBuffer1() == null)
/* 1313 */         return;  textureId = BlurProgram.getTexture();
/* 1314 */       if (textureId == 0)
/*      */         return; 
/*      */     } 
/* 1317 */     RenderSystem.enableBlend();
/* 1318 */     RenderSystem.defaultBlendFunc();
/* 1319 */     RenderSystem.disableCull();
/*      */     
/* 1321 */     RenderSystem.setShaderTexture(0, textureId);
/*      */     
/* 1323 */     class_5944 shader = mc.method_62887().method_62947(ShaderUtils.liquidGlass);
/*      */     
/* 1325 */     class_284 globalAlphaUniform = shader.method_34582("GlobalAlpha");
/* 1326 */     class_284 sizeUniform = shader.method_34582("Size");
/* 1327 */     class_284 radiusUniform = shader.method_34582("Radius");
/* 1328 */     class_284 smoothnessUniform = shader.method_34582("Smoothness");
/* 1329 */     class_284 fresnelPowerUniform = shader.method_34582("FresnelPower");
/* 1330 */     class_284 fresnelColorUniform = shader.method_34582("FresnelColor");
/* 1331 */     class_284 fresnelAlphaUniform = shader.method_34582("FresnelAlpha");
/* 1332 */     class_284 baseAlphaUniform = shader.method_34582("BaseAlpha");
/* 1333 */     class_284 fresnelInvertUniform = shader.method_34582("FresnelInvert");
/* 1334 */     class_284 fresnelMixUniform = shader.method_34582("FresnelMix");
/* 1335 */     class_284 distortStrengthUniform = shader.method_34582("DistortStrength");
/* 1336 */     class_284 cornerSmoothnessUniform = shader.method_34582("CornerSmoothness");
/*      */     
/* 1338 */     if (globalAlphaUniform != null) globalAlphaUniform.method_1251(globalAlpha); 
/* 1339 */     if (sizeUniform != null) sizeUniform.method_1255(width, height); 
/* 1340 */     if (radiusUniform != null) radiusUniform.method_35657(topLeft, topRight, bottomRight, bottomLeft); 
/* 1341 */     if (smoothnessUniform != null) smoothnessUniform.method_1251(0.5F); 
/* 1342 */     if (fresnelPowerUniform != null) fresnelPowerUniform.method_1251(fresnelPower);
/*      */     
/* 1344 */     int fAlpha = fresnelColor >> 24 & 0xFF;
/* 1345 */     if (fAlpha == 0) fAlpha = 255; 
/* 1346 */     if (fresnelColorUniform != null) fresnelColorUniform.method_1249((fresnelColor >> 16 & 0xFF) / 255.0F, (fresnelColor >> 8 & 0xFF) / 255.0F, (fresnelColor & 0xFF) / 255.0F);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1351 */     if (fresnelAlphaUniform != null) fresnelAlphaUniform.method_1251(fAlpha / 255.0F); 
/* 1352 */     if (baseAlphaUniform != null) baseAlphaUniform.method_1251(baseAlpha); 
/* 1353 */     if (fresnelInvertUniform != null) fresnelInvertUniform.method_35649(fresnelInvert ? 1 : 0); 
/* 1354 */     if (fresnelMixUniform != null) fresnelMixUniform.method_1251(fresnelMix); 
/* 1355 */     if (distortStrengthUniform != null) distortStrengthUniform.method_1251(distortStrength); 
/* 1356 */     if (cornerSmoothnessUniform != null) cornerSmoothnessUniform.method_1251(squirt);
/*      */     
/* 1358 */     Matrix4f matrix = matrices.method_23760().method_23761();
/*      */     
/* 1360 */     float screenWidth = mc.method_22683().method_4489();
/* 1361 */     float screenHeight = mc.method_22683().method_4506();
/* 1362 */     float scaleFactor = (float)mc.method_22683().method_4495();
/*      */     
/* 1364 */     float scaledX = x * scaleFactor;
/* 1365 */     float scaledY = y * scaleFactor;
/* 1366 */     float scaledW = width * scaleFactor;
/* 1367 */     float scaledH = height * scaleFactor;
/*      */     
/* 1369 */     float u1 = scaledX / screenWidth;
/* 1370 */     float v1 = 1.0F - scaledY / screenHeight;
/* 1371 */     float u2 = (scaledX + scaledW) / screenWidth;
/* 1372 */     float v2 = 1.0F - (scaledY + scaledH) / screenHeight;
/*      */     
/* 1374 */     int alpha = color >> 24 & 0xFF;
/* 1375 */     if (alpha == 0) alpha = 255; 
/* 1376 */     float r = (color >> 16 & 0xFF) / 255.0F;
/* 1377 */     float g = (color >> 8 & 0xFF) / 255.0F;
/* 1378 */     float b = (color & 0xFF) / 255.0F;
/* 1379 */     float a = alpha / 255.0F;
/*      */     
/* 1381 */     RenderSystem.setShader(ShaderUtils.liquidGlass);
/*      */     
/* 1383 */     class_287 builder = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
/* 1384 */     builder.method_22918(matrix, x, y, 0.0F).method_22913(u1, v1).method_22915(r, g, b, a);
/* 1385 */     builder.method_22918(matrix, x, y + height, 0.0F).method_22913(u1, v2).method_22915(r, g, b, a);
/* 1386 */     builder.method_22918(matrix, x + width, y + height, 0.0F).method_22913(u2, v2).method_22915(r, g, b, a);
/* 1387 */     builder.method_22918(matrix, x + width, y, 0.0F).method_22913(u2, v1).method_22915(r, g, b, a);
/* 1388 */     class_286.method_43433(builder.method_60800());
/*      */     
/* 1390 */     RenderSystem.setShaderTexture(0, 0);
/* 1391 */     RenderSystem.enableCull();
/* 1392 */     RenderSystem.disableBlend();
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\render\RenderUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */