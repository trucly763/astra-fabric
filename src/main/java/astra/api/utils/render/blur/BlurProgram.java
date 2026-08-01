/*     */ package shame.astra.api.utils.render.blur;
/*     */ 
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import lombok.Generated;
/*     */ import net.minecraft.class_276;
/*     */ import net.minecraft.class_284;
/*     */ import net.minecraft.class_287;
/*     */ import net.minecraft.class_289;
/*     */ import net.minecraft.class_293;
/*     */ import net.minecraft.class_5944;
/*     */ import net.minecraft.class_6367;
/*     */ import org.lwjgl.opengl.GL30;
/*     */ import shame.astra.api.utils.render.ShaderUtils;
/*     */ 
/*     */ public class BlurProgram implements QClient {
/*     */   private static BlurProgram instance;
/*     */   
/*     */   @Generated
/*  19 */   public static class_276 getBuffer1() { return buffer1; } private static class_276 buffer1; private static class_276 buffer2; @Generated
/*     */   public static class_276 getBuffer2() {
/*  21 */     return buffer2;
/*     */   }
/*     */   
/*  24 */   private int lastWidth = -1;
/*  25 */   private int lastHeight = -1;
/*  26 */   private long lastUpdateTime = 0L;
/*     */   
/*     */   private boolean requestedThisFrame = true;
/*  29 */   private float blurOffset = 1.0F; @Generated public void setBlurOffset(float blurOffset) { this.blurOffset = blurOffset; }
/*     */ 
/*     */   
/*  32 */   private final int iterations = 4;
/*     */   
/*     */   public static BlurProgram getInstance() {
/*  35 */     if (instance == null) {
/*  36 */       instance = new BlurProgram();
/*     */     }
/*  38 */     return instance;
/*     */   }
/*     */   
/*     */   public void beginFrame() {
/*  42 */     boolean shouldDraw = this.requestedThisFrame;
/*  43 */     this.requestedThisFrame = false;
/*  44 */     if (!shouldDraw) {
/*     */       return;
/*     */     }
/*  47 */     draw();
/*     */   }
/*     */   
/*     */   public void request() {
/*  51 */     this.requestedThisFrame = true;
/*     */   }
/*     */   
/*     */   private void draw() {
/*  55 */     long currentTime = System.currentTimeMillis();
/*  56 */     if (currentTime - this.lastUpdateTime < 16L) {
/*     */       return;
/*     */     }
/*  59 */     this.lastUpdateTime = currentTime;
/*     */     
/*  61 */     int width = mc.method_22683().method_4489();
/*  62 */     int height = mc.method_22683().method_4506();
/*     */     
/*  64 */     if (buffer1 == null || buffer2 == null || this.lastWidth != width || this.lastHeight != height) {
/*  65 */       if (buffer1 != null) {
/*  66 */         buffer1.method_1238();
/*     */       }
/*  68 */       if (buffer2 != null) {
/*  69 */         buffer2.method_1238();
/*     */       }
/*  71 */       buffer1 = (class_276)new class_6367(width, height, false);
/*  72 */       buffer2 = (class_276)new class_6367(width, height, false);
/*     */       
/*  74 */       setLinearFiltering(buffer1);
/*  75 */       setLinearFiltering(buffer2);
/*     */       
/*  77 */       this.lastWidth = width;
/*  78 */       this.lastHeight = height;
/*     */     } 
/*     */     
/*  81 */     RenderSystem.enableBlend();
/*  82 */     RenderSystem.defaultBlendFunc();
/*     */     
/*  84 */     class_5944 kawaseDown = mc.method_62887().method_62947(ShaderUtils.kawaseDown);
/*  85 */     class_5944 kawaseUp = mc.method_62887().method_62947(ShaderUtils.kawaseUp);
/*     */     
/*  87 */     buffer1.method_1236(0.0F, 0.0F, 0.0F, 0.0F);
/*  88 */     buffer1.method_1230();
/*  89 */     buffer1.method_1235(true);
/*     */     
/*  91 */     RenderSystem.setShader(ShaderUtils.kawaseDown);
/*  92 */     mc.method_1522().method_35610();
/*  93 */     RenderSystem.setShaderTexture(0, mc.method_1522().method_30277());
/*     */     
/*  95 */     setKawaseUniforms(kawaseDown, width, height);
/*  96 */     drawQuad(mc.method_22683().method_4486(), mc.method_22683().method_4502());
/*     */     
/*  98 */     mc.method_1522().method_1242();
/*  99 */     buffer1.method_1240();
/*     */     
/* 101 */     class_276[] buffers = { buffer1, buffer2 };
/*     */     int i;
/* 103 */     for (i = 1; i < 4; i++) {
/* 104 */       int srcIndex = (i + 1) % 2;
/* 105 */       int dstIndex = i % 2;
/*     */       
/* 107 */       class_276 src = buffers[srcIndex];
/* 108 */       class_276 dst = buffers[dstIndex];
/*     */       
/* 110 */       dst.method_1236(0.0F, 0.0F, 0.0F, 0.0F);
/* 111 */       dst.method_1230();
/* 112 */       dst.method_1235(true);
/*     */       
/* 114 */       RenderSystem.setShader(ShaderUtils.kawaseDown);
/* 115 */       src.method_35610();
/* 116 */       RenderSystem.setShaderTexture(0, src.method_30277());
/*     */       
/* 118 */       setKawaseUniforms(kawaseDown, src.field_1482, src.field_1481);
/* 119 */       drawQuad(mc.method_22683().method_4486(), mc.method_22683().method_4502());
/*     */       
/* 121 */       src.method_1242();
/* 122 */       dst.method_1240();
/*     */     } 
/*     */     
/* 125 */     for (i = 0; i < 4; i++) {
/* 126 */       int srcIndex = i % 2;
/* 127 */       int dstIndex = (i + 1) % 2;
/*     */       
/* 129 */       class_276 src = buffers[srcIndex];
/* 130 */       class_276 dst = buffers[dstIndex];
/*     */       
/* 132 */       dst.method_1236(0.0F, 0.0F, 0.0F, 0.0F);
/* 133 */       dst.method_1230();
/* 134 */       dst.method_1235(true);
/*     */       
/* 136 */       RenderSystem.setShader(ShaderUtils.kawaseUp);
/* 137 */       src.method_35610();
/* 138 */       RenderSystem.setShaderTexture(0, src.method_30277());
/*     */       
/* 140 */       setKawaseUniforms(kawaseUp, src.field_1482, src.field_1481);
/* 141 */       drawQuad(mc.method_22683().method_4486(), mc.method_22683().method_4502());
/*     */       
/* 143 */       src.method_1242();
/* 144 */       dst.method_1240();
/*     */     } 
/*     */     
/* 147 */     RenderSystem.disableBlend();
/* 148 */     mc.method_1522().method_1235(true);
/* 149 */     RenderSystem.setShaderTexture(0, 0);
/*     */   }
/*     */   
/*     */   private void setLinearFiltering(class_276 framebuffer) {
/* 153 */     RenderSystem.bindTexture(framebuffer.method_30277());
/* 154 */     GL30.glTexParameteri(3553, 10241, 9729);
/* 155 */     GL30.glTexParameteri(3553, 10240, 9729);
/* 156 */     RenderSystem.bindTexture(0);
/*     */   }
/*     */   
/*     */   private void setKawaseUniforms(class_5944 shader, int texWidth, int texHeight) {
/* 160 */     class_284 resolutionUniform = shader.method_34582("Resolution");
/* 161 */     class_284 offsetUniform = shader.method_34582("Offset");
/* 162 */     class_284 saturationUniform = shader.method_34582("Saturation");
/* 163 */     class_284 tintIntensityUniform = shader.method_34582("TintIntensity");
/* 164 */     class_284 tintColorUniform = shader.method_34582("TintColor");
/*     */     
/* 166 */     if (resolutionUniform != null) resolutionUniform.method_1255(1.0F / texWidth, 1.0F / texHeight); 
/* 167 */     if (offsetUniform != null) offsetUniform.method_1251(this.blurOffset); 
/* 168 */     if (saturationUniform != null) saturationUniform.method_1251(1.0F); 
/* 169 */     if (tintIntensityUniform != null) tintIntensityUniform.method_1251(0.0F); 
/* 170 */     if (tintColorUniform != null) tintColorUniform.method_1249(1.0F, 1.0F, 1.0F); 
/*     */   }
/*     */   
/*     */   private void drawQuad(float width, float height) {
/* 174 */     class_287 builder = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
/* 175 */     builder.method_22912(0.0F, 0.0F, 0.0F).method_22913(0.0F, 1.0F).method_22915(1.0F, 1.0F, 1.0F, 1.0F);
/* 176 */     builder.method_22912(0.0F, height, 0.0F).method_22913(0.0F, 0.0F).method_22915(1.0F, 1.0F, 1.0F, 1.0F);
/* 177 */     builder.method_22912(width, height, 0.0F).method_22913(1.0F, 0.0F).method_22915(1.0F, 1.0F, 1.0F, 1.0F);
/* 178 */     builder.method_22912(width, 0.0F, 0.0F).method_22913(1.0F, 1.0F).method_22915(1.0F, 1.0F, 1.0F, 1.0F);
/* 179 */     class_286.method_43433(builder.method_60800());
/*     */   }
/*     */   
/*     */   public static int getTexture() {
/* 183 */     getInstance().request();
/* 184 */     return (buffer1 != null) ? buffer1.method_30277() : 0;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\render\blur\BlurProgram.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */