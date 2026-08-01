/*     */ package shame.astra.api.utils.render.fonts.ttf;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.image.BufferedImage;
/*     */ import lombok.Generated;
/*     */ import net.minecraft.class_1011;
/*     */ import net.minecraft.class_1043;
/*     */ import net.minecraft.class_287;
/*     */ import net.minecraft.class_2960;
/*     */ import org.joml.Matrix4f;
/*     */ 
/*     */ public class CFont {
/*     */   protected static final int IMG_SIZE = 512;
/*  17 */   protected CharData[] charData = new CharData[1104]; protected Font font; @Generated
/*  18 */   public Font getFont() { return this.font; } protected boolean antiAlias; protected boolean fractionalMetrics; @Generated
/*     */   public boolean isAntiAlias() {
/*  20 */     return this.antiAlias; } @Generated
/*     */   public boolean isFractionalMetrics() {
/*  22 */     return this.fractionalMetrics;
/*     */   }
/*  24 */   protected int fontHeight = -1;
/*  25 */   protected int charOffset = 0; protected class_2960 textureId; protected int glTextureId; @Generated
/*  26 */   public class_2960 getTextureId() { return this.textureId; } @Generated
/*     */   public int getGlTextureId() {
/*  28 */     return this.glTextureId;
/*     */   }
/*  30 */   private static int textureCounter = 0;
/*     */   
/*     */   public CFont(Font font, boolean antiAlias, boolean fractionalMetrics) {
/*  33 */     this.font = font;
/*  34 */     this.antiAlias = antiAlias;
/*  35 */     this.fractionalMetrics = fractionalMetrics;
/*  36 */     setupTexture(font, antiAlias, fractionalMetrics, this.charData);
/*     */   }
/*     */   
/*     */   protected void setupTexture(Font font, boolean antiAlias, boolean fractionalMetrics, CharData[] chars) {
/*  40 */     BufferedImage img = generateFontImage(font, antiAlias, fractionalMetrics, chars);
/*     */     try {
/*  42 */       class_1011 nativeImage = new class_1011(img.getWidth(), img.getHeight(), false);
/*  43 */       for (int y = 0; y < img.getHeight(); y++) {
/*  44 */         for (int x = 0; x < img.getWidth(); x++) {
/*  45 */           int argb = img.getRGB(x, y);
/*  46 */           int a = argb >> 24 & 0xFF;
/*  47 */           int r = argb >> 16 & 0xFF;
/*  48 */           int g = argb >> 8 & 0xFF;
/*  49 */           int b = argb & 0xFF;
/*  50 */           nativeImage.method_61941(x, y, a << 24 | r << 16 | g << 8 | b);
/*     */         } 
/*     */       } 
/*  53 */       class_1043 texture = new class_1043(nativeImage);
/*  54 */       this.glTextureId = texture.method_4624();
/*  55 */       String name = "cfont_" + textureCounter++;
/*  56 */       this.textureId = class_2960.method_60655("customfont", name);
/*  57 */       class_310.method_1551().method_1531().method_4616(this.textureId, (class_1044)texture);
/*  58 */     } catch (Exception e) {
/*  59 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected BufferedImage generateFontImage(Font font, boolean antiAlias, boolean fractionalMetrics, CharData[] chars) {
/*  64 */     BufferedImage bufferedImage = new BufferedImage(512, 512, 2);
/*  65 */     Graphics2D g = (Graphics2D)bufferedImage.getGraphics();
/*  66 */     g.setFont(font);
/*  67 */     g.setColor(new Color(255, 255, 255, 0));
/*  68 */     g.fillRect(0, 0, 512, 512);
/*  69 */     g.setColor(Color.WHITE);
/*  70 */     g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
/*  71 */     g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, antiAlias ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
/*  72 */     g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antiAlias ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
/*  73 */     FontMetrics fontMetrics = g.getFontMetrics();
/*  74 */     int charHeight = 0;
/*  75 */     int positionX = 0;
/*  76 */     int positionY = 1;
/*  77 */     for (int i = 0; i < chars.length; i++) {
/*  78 */       char ch = (char)i;
/*  79 */       if ((ch > 'Џ' && ch < 'ѐ') || ch < 'Ā') {
/*  80 */         CharData charData = new CharData();
/*  81 */         Rectangle2D dimensions = fontMetrics.getStringBounds(String.valueOf(ch), g);
/*  82 */         charData.width = (dimensions.getBounds()).width + 8;
/*  83 */         charData.height = (dimensions.getBounds()).height;
/*  84 */         if (positionX + charData.width >= 512) {
/*  85 */           positionX = 0;
/*  86 */           positionY += charHeight;
/*  87 */           charHeight = 0;
/*     */         } 
/*  89 */         if (charData.height > charHeight) {
/*  90 */           charHeight = charData.height;
/*     */         }
/*  92 */         charData.storedX = positionX;
/*  93 */         charData.storedY = positionY;
/*  94 */         if (charData.height > this.fontHeight) {
/*  95 */           this.fontHeight = charData.height;
/*     */         }
/*  97 */         chars[i] = charData;
/*  98 */         g.drawString(String.valueOf(ch), positionX + 2, positionY + fontMetrics.getAscent());
/*  99 */         positionX += charData.width;
/*     */       } 
/* 101 */     }  return bufferedImage;
/*     */   }
/*     */   
/*     */   public void drawChar(CharData[] chars, char c, float x, float y, Matrix4f matrix, class_287 buffer) {
/*     */     try {
/* 106 */       if (chars[c] == null)
/* 107 */         return;  drawQuad(x, y, (chars[c]).width, (chars[c]).height, (chars[c]).storedX, (chars[c]).storedY, (chars[c]).width, (chars[c]).height, matrix, buffer);
/* 108 */     } catch (Exception exception) {}
/*     */   }
/*     */ 
/*     */   
/*     */   protected void drawQuad(float x, float y, float width, float height, float srcX, float srcY, float srcWidth, float srcHeight, Matrix4f matrix, class_287 buffer) {
/* 113 */     float renderSRCX = srcX / 512.0F;
/* 114 */     float renderSRCY = srcY / 512.0F;
/* 115 */     float renderSRCWidth = srcWidth / 512.0F;
/* 116 */     float renderSRCHeight = srcHeight / 512.0F;
/*     */     
/* 118 */     buffer.method_22918(matrix, x + width, y, 0.0F).method_22913(renderSRCX + renderSRCWidth, renderSRCY);
/* 119 */     buffer.method_22918(matrix, x, y, 0.0F).method_22913(renderSRCX, renderSRCY);
/* 120 */     buffer.method_22918(matrix, x, y + height, 0.0F).method_22913(renderSRCX, renderSRCY + renderSRCHeight);
/* 121 */     buffer.method_22918(matrix, x, y + height, 0.0F).method_22913(renderSRCX, renderSRCY + renderSRCHeight);
/* 122 */     buffer.method_22918(matrix, x + width, y + height, 0.0F).method_22913(renderSRCX + renderSRCWidth, renderSRCY + renderSRCHeight);
/* 123 */     buffer.method_22918(matrix, x + width, y, 0.0F).method_22913(renderSRCX + renderSRCWidth, renderSRCY);
/*     */   }
/*     */   
/*     */   public int getStringHeight(String text) {
/* 127 */     return getFontHeight();
/*     */   }
/*     */   
/*     */   public int getFontHeight() {
/* 131 */     return (this.fontHeight - 8) / 2;
/*     */   }
/*     */   
/*     */   public int getStringWidth(String text) {
/* 135 */     int width = 0;
/* 136 */     for (char c : text.toCharArray()) {
/* 137 */       if (c < this.charData.length && this.charData[c] != null)
/* 138 */         width += (this.charData[c]).width - 8 + this.charOffset; 
/*     */     } 
/* 140 */     return width / 2;
/*     */   }
/*     */   
/*     */   public void setAntiAlias(boolean antiAlias) {
/* 144 */     if (this.antiAlias != antiAlias) {
/* 145 */       this.antiAlias = antiAlias;
/* 146 */       setupTexture(this.font, antiAlias, this.fractionalMetrics, this.charData);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setFractionalMetrics(boolean fractionalMetrics) {
/* 151 */     if (this.fractionalMetrics != fractionalMetrics) {
/* 152 */       this.fractionalMetrics = fractionalMetrics;
/* 153 */       setupTexture(this.font, this.antiAlias, fractionalMetrics, this.charData);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setFont(Font font) {
/* 158 */     this.font = font;
/* 159 */     setupTexture(font, this.antiAlias, this.fractionalMetrics, this.charData);
/*     */   }
/*     */   
/*     */   protected static class CharData {
/*     */     public int width;
/*     */     public int height;
/*     */     public int storedX;
/*     */     public int storedY;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\render\fonts\ttf\CFont.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */