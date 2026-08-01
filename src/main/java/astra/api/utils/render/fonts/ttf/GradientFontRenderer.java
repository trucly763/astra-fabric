/*     */ package shame.astra.api.utils.render.fonts.ttf;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import java.awt.Font;
/*     */ import net.minecraft.class_10142;
/*     */ import net.minecraft.class_287;
/*     */ import net.minecraft.class_289;
/*     */ import net.minecraft.class_290;
/*     */ import net.minecraft.class_293;
/*     */ import org.joml.Matrix4f;
/*     */ 
/*     */ public class GradientFontRenderer extends MCFontRenderer {
/*     */   public GradientFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics) {
/*  13 */     super(font, antiAlias, fractionalMetrics);
/*     */   }
/*     */   
/*     */   public int drawGradientString(String text, float x, float y, int topColor, int bottomColor, boolean dropShadow, boolean horizontal) {
/*     */     int i;
/*  18 */     if (dropShadow) {
/*  19 */       i = renderGradientString(text, x + 1.0F, y + 1.0F, topColor, bottomColor, true, horizontal);
/*  20 */       i = Math.max(i, renderGradientString(text, x, y, topColor, bottomColor, false, horizontal));
/*     */     } else {
/*  22 */       i = renderGradientString(text, x, y, topColor, bottomColor, false, horizontal);
/*     */     } 
/*  24 */     return i;
/*     */   }
/*     */   
/*     */   private int renderGradientString(String text, float x, float y, int startColor, int endColor, boolean dropShadow, boolean horizontal) {
/*  28 */     if (text == null) {
/*  29 */       return 0;
/*     */     }
/*  31 */     if ((startColor & 0xFC000000) == 0) {
/*  32 */       startColor |= 0xFF000000;
/*     */     }
/*  34 */     if ((endColor & 0xFC000000) == 0) {
/*  35 */       endColor |= 0xFF000000;
/*     */     }
/*  37 */     if (dropShadow) {
/*  38 */       startColor = (startColor & 0xFCFCFC) >> 2 | startColor & 0xFF000000;
/*  39 */       endColor = (endColor & 0xFCFCFC) >> 2 | endColor & 0xFF000000;
/*     */     } 
/*  41 */     float posX = x;
/*  42 */     float posY = y;
/*  43 */     return renderGradientStringAtPos(text, posX, posY, dropShadow, startColor, endColor, horizontal);
/*     */   }
/*     */   
/*     */   private int renderGradientStringAtPos(String text, float posX, float posY, boolean shadow, int startColor, int endColor, boolean horizontal) {
/*  47 */     float totalWidth = getStringWidth(text);
/*  48 */     float currentCountWidth = 0.0F;
/*     */     
/*  50 */     RenderSystem.enableBlend();
/*  51 */     RenderSystem.defaultBlendFunc();
/*     */     
/*  53 */     Matrix4f matrix = new Matrix4f();
/*     */     
/*  55 */     for (int i = 0; i < text.length(); i++) {
/*  56 */       char c0 = text.charAt(i);
/*     */       
/*  58 */       if (c0 == ' ' || c0 == ' ') {
/*  59 */         posX += 4.0F;
/*     */ 
/*     */       
/*     */       }
/*  63 */       else if (c0 < this.charData.length && this.charData[c0] != null) {
/*     */         
/*  65 */         float charWidth = ((this.charData[c0]).width - 8 + this.charOffset);
/*     */         
/*  67 */         if (horizontal) {
/*  68 */           float firstMix = currentCountWidth / totalWidth;
/*  69 */           float lastMix = (currentCountWidth + charWidth) / totalWidth;
/*  70 */           int firstColor = colorMix(startColor, endColor, firstMix);
/*  71 */           int lastColor = colorMix(startColor, endColor, lastMix);
/*  72 */           renderGradientChar(c0, posX, posY, firstColor, lastColor, true, matrix);
/*  73 */           currentCountWidth += charWidth;
/*     */         } else {
/*  75 */           renderGradientChar(c0, posX, posY, startColor, endColor, false, matrix);
/*     */         } 
/*  77 */         posX += charWidth;
/*     */       } 
/*     */     } 
/*  80 */     RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
/*  81 */     return (int)posX;
/*     */   }
/*     */   
/*     */   private int colorMix(int startColor, int endColor, float mix) {
/*  85 */     float startAlpha = (startColor >> 24 & 0xFF) / 255.0F;
/*  86 */     float startRed = (startColor >> 16 & 0xFF) / 255.0F;
/*  87 */     float startGreen = (startColor >> 8 & 0xFF) / 255.0F;
/*  88 */     float startBlue = (startColor & 0xFF) / 255.0F;
/*  89 */     float endAlpha = (endColor >> 24 & 0xFF) / 255.0F;
/*  90 */     float endRed = (endColor >> 16 & 0xFF) / 255.0F;
/*  91 */     float endGreen = (endColor >> 8 & 0xFF) / 255.0F;
/*  92 */     float endBlue = (endColor & 0xFF) / 255.0F;
/*  93 */     int mixAlpha = (int)(((1.0F - mix) * startAlpha + mix * endAlpha) * 255.0F);
/*  94 */     int mixRed = (int)(((1.0F - mix) * startRed + mix * endRed) * 255.0F);
/*  95 */     int mixGreen = (int)(((1.0F - mix) * startGreen + mix * endGreen) * 255.0F);
/*  96 */     int mixBlue = (int)(((1.0F - mix) * startBlue + mix * endBlue) * 255.0F);
/*  97 */     return mixAlpha << 24 | mixRed << 16 | mixGreen << 8 | mixBlue;
/*     */   }
/*     */   
/*     */   private void renderGradientChar(char ch, float posX, float posY, int startColor, int endColor, boolean horizontal, Matrix4f matrix) {
/* 101 */     if (ch >= this.charData.length || this.charData[ch] == null)
/*     */       return; 
/* 103 */     float startAlpha = (startColor >> 24 & 0xFF) / 255.0F;
/* 104 */     float startRed = (startColor >> 16 & 0xFF) / 255.0F;
/* 105 */     float startGreen = (startColor >> 8 & 0xFF) / 255.0F;
/* 106 */     float startBlue = (startColor & 0xFF) / 255.0F;
/* 107 */     float endAlpha = (endColor >> 24 & 0xFF) / 255.0F;
/* 108 */     float endRed = (endColor >> 16 & 0xFF) / 255.0F;
/* 109 */     float endGreen = (endColor >> 8 & 0xFF) / 255.0F;
/* 110 */     float endBlue = (endColor & 0xFF) / 255.0F;
/*     */     
/* 112 */     CFont.CharData charData = this.charData[ch];
/* 113 */     float charXPos = charData.storedX;
/* 114 */     float charYPos = charData.storedY;
/* 115 */     int charWidth = charData.width;
/* 116 */     float width = charWidth - 0.01F;
/*     */     
/* 118 */     float u0 = charXPos / 512.0F;
/* 119 */     float v0 = charYPos / 512.0F;
/* 120 */     float u1 = (charXPos + width - 1.0F) / 512.0F;
/* 121 */     float v1 = (charYPos + 7.99F) / 512.0F;
/*     */     
/* 123 */     RenderSystem.setShaderTexture(0, this.glTextureId);
/* 124 */     RenderSystem.setShader(class_10142.field_53880);
/*     */     
/* 126 */     class_289 tessellator = class_289.method_1348();
/* 127 */     class_287 buffer = tessellator.method_60827(class_293.class_5596.field_27382, class_290.field_1575);
/*     */     
/* 129 */     if (horizontal) {
/* 130 */       buffer.method_22918(matrix, posX, posY, 0.0F).method_22913(u0, v0).method_22915(startRed, startGreen, startBlue, startAlpha);
/* 131 */       buffer.method_22918(matrix, posX, posY + 7.99F, 0.0F).method_22913(u0, v1).method_22915(startRed, startGreen, startBlue, startAlpha);
/* 132 */       buffer.method_22918(matrix, posX + width - 1.0F, posY + 7.99F, 0.0F).method_22913(u1, v1).method_22915(endRed, endGreen, endBlue, endAlpha);
/* 133 */       buffer.method_22918(matrix, posX + width - 1.0F, posY, 0.0F).method_22913(u1, v0).method_22915(endRed, endGreen, endBlue, endAlpha);
/*     */     } else {
/* 135 */       buffer.method_22918(matrix, posX, posY, 0.0F).method_22913(u0, v0).method_22915(startRed, startGreen, startBlue, startAlpha);
/* 136 */       buffer.method_22918(matrix, posX, posY + 7.99F, 0.0F).method_22913(u0, v1).method_22915(endRed, endGreen, endBlue, endAlpha);
/* 137 */       buffer.method_22918(matrix, posX + width - 1.0F, posY + 7.99F, 0.0F).method_22913(u1, v1).method_22915(endRed, endGreen, endBlue, endAlpha);
/* 138 */       buffer.method_22918(matrix, posX + width - 1.0F, posY, 0.0F).method_22913(u1, v0).method_22915(startRed, startGreen, startBlue, startAlpha);
/*     */     } 
/*     */     
/* 141 */     class_286.method_43433(buffer.method_60800());
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\render\fonts\ttf\GradientFontRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */