/*     */ package shame.astra.api.utils.render.glow;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import java.awt.Color;
/*     */ import net.minecraft.class_10366;
/*     */ import net.minecraft.class_276;
/*     */ import net.minecraft.class_286;
/*     */ import net.minecraft.class_287;
/*     */ import net.minecraft.class_289;
/*     */ import net.minecraft.class_293;
/*     */ import net.minecraft.class_310;
/*     */ import net.minecraft.class_4587;
/*     */ import net.minecraft.class_6367;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.lwjgl.opengl.GL30;
/*     */ 
/*     */ public class GlowProgram {
/*  19 */   private static final class_310 mc = class_310.method_1551();
/*     */   
/*     */   private static GlowProgram instance;
/*     */   private class_276 glowBuffer;
/*     */   private int lastWidth;
/*     */   private int lastHeight;
/*  25 */   private float glowRadius = 10.0F;
/*  26 */   private float glowIntensity = 1.0F;
/*  27 */   private Color glowColor = Color.WHITE;
/*     */   
/*     */   private Matrix4f savedProjection;
/*     */   
/*     */   private int savedFbo;
/*     */   
/*     */   private static final int RINGS = 6;
/*     */   private static final int ANGLES_PER_RING = 12;
/*     */   
/*     */   public static GlowProgram getInstance() {
/*  37 */     if (instance == null) {
/*  38 */       instance = new GlowProgram();
/*     */     }
/*  40 */     return instance;
/*     */   }
/*     */   
/*     */   private void checkFramebuffers() {
/*  44 */     int width = mc.method_22683().method_4489();
/*  45 */     int height = mc.method_22683().method_4506();
/*     */     
/*  47 */     if (this.glowBuffer == null || this.lastWidth != width || this.lastHeight != height) {
/*  48 */       if (this.glowBuffer != null) {
/*  49 */         this.glowBuffer.method_1238();
/*     */       }
/*  51 */       this.glowBuffer = (class_276)new class_6367(width, height, false);
/*  52 */       this.lastWidth = width;
/*  53 */       this.lastHeight = height;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void begin(float radius, Color color) {
/*  58 */     begin(radius, 1.0F, color);
/*     */   }
/*     */   
/*     */   public void begin(float radius, float intensity, Color color) {
/*  62 */     checkFramebuffers();
/*     */     
/*  64 */     this.glowRadius = radius;
/*  65 */     this.glowIntensity = intensity;
/*  66 */     this.glowColor = color;
/*     */     
/*  68 */     this.savedProjection = new Matrix4f((Matrix4fc)RenderSystem.getProjectionMatrix());
/*  69 */     this.savedFbo = GL11.glGetInteger(36006);
/*     */     
/*  71 */     GL30.glBindFramebuffer(36160, this.glowBuffer.field_1476);
/*  72 */     GL11.glViewport(0, 0, this.lastWidth, this.lastHeight);
/*  73 */     RenderSystem.clearColor(0.0F, 0.0F, 0.0F, 0.0F);
/*  74 */     RenderSystem.clear(16384);
/*  75 */     RenderSystem.setProjectionMatrix(this.savedProjection, class_10366.field_54954);
/*     */   }
/*     */   
/*     */   public void end(class_4587 matrices, GlowCallback contentCallback) {
/*  79 */     GL30.glBindFramebuffer(36160, this.savedFbo);
/*  80 */     GL11.glViewport(0, 0, mc.method_22683().method_4489(), mc.method_22683().method_4506());
/*  81 */     RenderSystem.setProjectionMatrix(this.savedProjection, class_10366.field_54954);
/*     */     
/*  83 */     renderGlow(matrices);
/*     */     
/*  85 */     if (contentCallback != null) {
/*  86 */       contentCallback.render();
/*     */     }
/*     */   }
/*     */   
/*     */   private float gaussian(float x, float sigma) {
/*  91 */     return (float)Math.exp((-(x * x) / 2.0F * sigma * sigma));
/*     */   }
/*     */   
/*     */   private void renderGlow(class_4587 matrices) {
/*  95 */     RenderSystem.enableBlend();
/*  96 */     RenderSystem.blendFunc(770, 1);
/*  97 */     RenderSystem.disableDepthTest();
/*     */     
/*  99 */     int width = mc.method_22683().method_4486();
/* 100 */     int height = mc.method_22683().method_4502();
/*     */     
/* 102 */     RenderSystem.setShaderTexture(0, this.glowBuffer.method_30277());
/* 103 */     RenderSystem.setShader(class_10142.field_53880);
/*     */     
/* 105 */     GL11.glTexParameteri(3553, 10241, 9729);
/* 106 */     GL11.glTexParameteri(3553, 10240, 9729);
/*     */     
/* 108 */     Matrix4f matrix = matrices.method_23760().method_23761();
/*     */     
/* 110 */     float r = this.glowColor.getRed() / 255.0F;
/* 111 */     float g = this.glowColor.getGreen() / 255.0F;
/* 112 */     float b = this.glowColor.getBlue() / 255.0F;
/* 113 */     float baseAlpha = this.glowColor.getAlpha() / 255.0F * this.glowIntensity;
/*     */     
/* 115 */     float sigma = this.glowRadius * 0.4F;
/*     */ 
/*     */     
/* 118 */     float[] ringWeights = new float[6];
/* 119 */     float totalWeight = 0.0F;
/*     */     int i;
/* 121 */     for (i = 0; i < 6; i++) {
/* 122 */       float distance = this.glowRadius * (i + 1) / 6.0F;
/* 123 */       ringWeights[i] = gaussian(distance, sigma);
/* 124 */       totalWeight += ringWeights[i];
/*     */     } 
/*     */ 
/*     */     
/* 128 */     for (i = 0; i < 6; i++) {
/* 129 */       ringWeights[i] = ringWeights[i] / totalWeight;
/*     */     }
/*     */ 
/*     */     
/* 133 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 138 */     for (int ring = 0; ring < 6; ring++) {
/* 139 */       float distance = this.glowRadius * (ring + 1) / 6.0F;
/* 140 */       float alpha = baseAlpha * ringWeights[ring] * 0.7F;
/*     */       
/* 142 */       if (alpha >= 0.001F) {
/* 143 */         alpha = Math.min(alpha, 1.0F);
/*     */         
/* 145 */         for (int angle = 0; angle < 12; angle++) {
/* 146 */           float a1 = (float)(angle * 2.0D * Math.PI) / 12.0F;
/* 147 */           float ox = (float)Math.cos(a1) * distance;
/* 148 */           float oy = (float)Math.sin(a1) * distance;
/*     */           
/* 150 */           buffer.method_22918(matrix, ox, oy, 0.0F)
/* 151 */             .method_22913(0.0F, 1.0F).method_22915(r, g, b, alpha);
/* 152 */           buffer.method_22918(matrix, ox, height + oy, 0.0F)
/* 153 */             .method_22913(0.0F, 0.0F).method_22915(r, g, b, alpha);
/* 154 */           buffer.method_22918(matrix, width + ox, height + oy, 0.0F)
/* 155 */             .method_22913(1.0F, 0.0F).method_22915(r, g, b, alpha);
/* 156 */           buffer.method_22918(matrix, width + ox, oy, 0.0F)
/* 157 */             .method_22913(1.0F, 1.0F).method_22915(r, g, b, alpha);
/*     */ 
/*     */           
/* 160 */           if (ring > 0) {
/* 161 */             float a2 = (float)((angle + 0.5D) * 2.0D * Math.PI) / 12.0F;
/* 162 */             float innerDist = distance * 0.6F;
/* 163 */             float ox2 = (float)Math.cos(a2) * innerDist;
/* 164 */             float oy2 = (float)Math.sin(a2) * innerDist;
/* 165 */             float alpha2 = alpha * 0.5F;
/*     */             
/* 167 */             buffer.method_22918(matrix, ox2, oy2, 0.0F)
/* 168 */               .method_22913(0.0F, 1.0F).method_22915(r, g, b, alpha2);
/* 169 */             buffer.method_22918(matrix, ox2, height + oy2, 0.0F)
/* 170 */               .method_22913(0.0F, 0.0F).method_22915(r, g, b, alpha2);
/* 171 */             buffer.method_22918(matrix, width + ox2, height + oy2, 0.0F)
/* 172 */               .method_22913(1.0F, 0.0F).method_22915(r, g, b, alpha2);
/* 173 */             buffer.method_22918(matrix, width + ox2, oy2, 0.0F)
/* 174 */               .method_22913(1.0F, 1.0F).method_22915(r, g, b, alpha2);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 179 */     class_286.method_43433(buffer.method_60800());
/*     */ 
/*     */     
/* 182 */     GL11.glTexParameteri(3553, 10241, 9728);
/* 183 */     GL11.glTexParameteri(3553, 10240, 9728);
/* 184 */     RenderSystem.setShaderTexture(0, 0);
/*     */     
/* 186 */     RenderSystem.defaultBlendFunc();
/* 187 */     RenderSystem.enableDepthTest();
/* 188 */     RenderSystem.disableBlend();
/*     */   }
/*     */   
/*     */   public static void startGlow(float radius, int color, GlowCallback callback, class_4587 matrices) {
/* 192 */     startGlow(radius, 1.0F, color, callback, matrices);
/*     */   }
/*     */   
/*     */   public static void startGlow(float radius, float intensity, int color, GlowCallback callback, class_4587 matrices) {
/* 196 */     int a = color >> 24 & 0xFF;
/* 197 */     int r = color >> 16 & 0xFF;
/* 198 */     int g = color >> 8 & 0xFF;
/* 199 */     int b = color & 0xFF;
/* 200 */     if (a == 0) a = 255;
/*     */     
/* 202 */     GlowProgram glow = getInstance();
/* 203 */     glow.begin(radius, intensity, new Color(r, g, b, a));
/* 204 */     callback.render();
/* 205 */     glow.end(matrices, callback);
/*     */   }
/*     */   
/*     */   public void cleanup() {
/* 209 */     if (this.glowBuffer != null) {
/* 210 */       this.glowBuffer.method_1238();
/* 211 */       this.glowBuffer = null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\render\glow\GlowProgram.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */