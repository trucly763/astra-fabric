/*     */ package shame.astra.api.utils.render.fonts.ttf;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import java.awt.Color;
/*     */ import net.minecraft.class_10142;
/*     */ import net.minecraft.class_287;
/*     */ import net.minecraft.class_289;
/*     */ import net.minecraft.class_290;
/*     */ import org.joml.Matrix4f;
/*     */ 
/*     */ public class MCFontRenderer extends CFont {
/*  11 */   private final int[] colorCode = new int[32];
/*  12 */   protected CFont.CharData[] boldChars = new CFont.CharData[1104];
/*  13 */   protected CFont.CharData[] italicChars = new CFont.CharData[1104];
/*  14 */   protected CFont.CharData[] boldItalicChars = new CFont.CharData[1104];
/*     */   protected int texBold;
/*     */   protected int texItalic;
/*     */   protected int texItalicBold;
/*     */   
/*     */   public MCFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics) {
/*  20 */     super(font, antiAlias, fractionalMetrics);
/*  21 */     setupBoldItalicIDs();
/*  22 */     for (int index = 0; index < 32; index++) {
/*  23 */       int noClue = (index >> 3 & 0x1) * 85;
/*  24 */       int red = (index >> 2 & 0x1) * 170 + noClue;
/*  25 */       int green = (index >> 1 & 0x1) * 170 + noClue;
/*  26 */       int blue = (index & 0x1) * 170 + noClue;
/*  27 */       if (index == 6) {
/*  28 */         red += 85;
/*     */       }
/*  30 */       if (index >= 16) {
/*  31 */         red /= 4;
/*  32 */         green /= 4;
/*  33 */         blue /= 4;
/*     */       } 
/*  35 */       this.colorCode[index] = (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
/*     */     } 
/*     */   }
/*     */   
/*     */   public float drawStringWithShadow(String text, double x, double y, int color) {
/*  40 */     float shadowWidth = drawString(text, x + 0.5D, y + 0.5D, color, true);
/*  41 */     return Math.max(shadowWidth, drawString(text, x, y, color, false));
/*     */   }
/*     */   public float drawGradientString(String text, float x, float y, int topColor, int bottomColor) {
/*  44 */     if (text == null) return 0.0F;
/*     */     
/*  46 */     x--;
/*     */     
/*  48 */     if ((topColor & 0xFC000000) == 0) topColor |= 0xFF000000; 
/*  49 */     if ((bottomColor & 0xFC000000) == 0) bottomColor |= 0xFF000000;
/*     */     
/*  51 */     float topAlpha = (topColor >> 24 & 0xFF) / 255.0F;
/*  52 */     float topRed = (topColor >> 16 & 0xFF) / 255.0F;
/*  53 */     float topGreen = (topColor >> 8 & 0xFF) / 255.0F;
/*  54 */     float topBlue = (topColor & 0xFF) / 255.0F;
/*  55 */     float botAlpha = (bottomColor >> 24 & 0xFF) / 255.0F;
/*  56 */     float botRed = (bottomColor >> 16 & 0xFF) / 255.0F;
/*  57 */     float botGreen = (bottomColor >> 8 & 0xFF) / 255.0F;
/*  58 */     float botBlue = (bottomColor & 0xFF) / 255.0F;
/*     */     
/*  60 */     double posX = x * 2.0D;
/*  61 */     double posY = (y - 3.0D) * 2.0D;
/*     */     
/*  63 */     RenderSystem.enableBlend();
/*  64 */     RenderSystem.defaultBlendFunc();
/*  65 */     RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
/*     */     
/*  67 */     Matrix4f matrix = new Matrix4f();
/*  68 */     matrix.scale(0.5F, 0.5F, 0.5F);
/*     */     
/*  70 */     CFont.CharData[] currentData = this.charData;
/*  71 */     int size = text.length();
/*     */     
/*  73 */     for (int i = 0; i < size; i++) {
/*  74 */       char character = text.charAt(i);
/*     */       
/*  76 */       if (character >= currentData.length || currentData[character] == null) {
/*  77 */         if (character == ' ' || character == ' ') {
/*  78 */           posX += 8.0D;
/*     */         }
/*     */       }
/*     */       else {
/*     */         
/*  83 */         RenderSystem.setShaderTexture(0, this.glTextureId);
/*  84 */         RenderSystem.setShader(class_10142.field_53880);
/*     */         
/*  86 */         class_289 tessellator = class_289.method_1348();
/*  87 */         class_287 buffer = tessellator.method_60827(class_293.class_5596.field_27382, class_290.field_1575);
/*     */         
/*  89 */         CFont.CharData cd = currentData[character];
/*  90 */         float charXPos = cd.storedX;
/*  91 */         float charYPos = cd.storedY;
/*  92 */         float width = cd.width;
/*  93 */         float height = cd.height;
/*     */         
/*  95 */         float u0 = charXPos / 512.0F;
/*  96 */         float v0 = charYPos / 512.0F;
/*  97 */         float u1 = (charXPos + width) / 512.0F;
/*  98 */         float v1 = (charYPos + height) / 512.0F;
/*     */         
/* 100 */         buffer.method_22918(matrix, (float)posX, (float)posY, 0.0F).method_22913(u0, v0).method_22915(topRed, topGreen, topBlue, topAlpha);
/* 101 */         buffer.method_22918(matrix, (float)posX, (float)posY + height, 0.0F).method_22913(u0, v1).method_22915(botRed, botGreen, botBlue, botAlpha);
/* 102 */         buffer.method_22918(matrix, (float)posX + width, (float)posY + height, 0.0F).method_22913(u1, v1).method_22915(botRed, botGreen, botBlue, botAlpha);
/* 103 */         buffer.method_22918(matrix, (float)posX + width, (float)posY, 0.0F).method_22913(u1, v0).method_22915(topRed, topGreen, topBlue, topAlpha);
/*     */         
/* 105 */         class_286.method_43433(buffer.method_60800());
/*     */         
/* 107 */         posX += (cd.width - 8 + this.charOffset);
/*     */       } 
/*     */     } 
/* 110 */     RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
/* 111 */     return (float)posX / 2.0F;
/*     */   }
/*     */   
/*     */   public float drawGradientStringHorizontal(String text, float x, float y, int leftColor, int rightColor) {
/* 115 */     if (text == null) return 0.0F;
/*     */     
/* 117 */     x--;
/*     */     
/* 119 */     if ((leftColor & 0xFC000000) == 0) leftColor |= 0xFF000000; 
/* 120 */     if ((rightColor & 0xFC000000) == 0) rightColor |= 0xFF000000;
/*     */     
/* 122 */     double posX = x * 2.0D;
/* 123 */     double posY = (y - 3.0D) * 2.0D;
/*     */     
/* 125 */     RenderSystem.enableBlend();
/* 126 */     RenderSystem.defaultBlendFunc();
/* 127 */     RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
/*     */     
/* 129 */     Matrix4f matrix = new Matrix4f();
/* 130 */     matrix.scale(0.5F, 0.5F, 0.5F);
/*     */     
/* 132 */     CFont.CharData[] currentData = this.charData;
/* 133 */     int size = text.length();
/* 134 */     float totalWidth = getStringWidth(text) * 2.0F;
/* 135 */     float currentWidth = 0.0F;
/*     */     
/* 137 */     for (int i = 0; i < size; i++) {
/* 138 */       char character = text.charAt(i);
/*     */       
/* 140 */       if (character >= currentData.length || currentData[character] == null) {
/* 141 */         if (character == ' ' || character == ' ') {
/* 142 */           posX += 8.0D;
/* 143 */           currentWidth += 8.0F;
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 148 */         RenderSystem.setShaderTexture(0, this.glTextureId);
/* 149 */         RenderSystem.setShader(class_10142.field_53880);
/*     */         
/* 151 */         class_289 tessellator = class_289.method_1348();
/* 152 */         class_287 buffer = tessellator.method_60827(class_293.class_5596.field_27382, class_290.field_1575);
/*     */         
/* 154 */         CFont.CharData cd = currentData[character];
/* 155 */         float charXPos = cd.storedX;
/* 156 */         float charYPos = cd.storedY;
/* 157 */         float width = cd.width;
/* 158 */         float height = cd.height;
/* 159 */         float charWidth = (cd.width - 8 + this.charOffset);
/*     */         
/* 161 */         float u0 = charXPos / 512.0F;
/* 162 */         float v0 = charYPos / 512.0F;
/* 163 */         float u1 = (charXPos + width) / 512.0F;
/* 164 */         float v1 = (charYPos + height) / 512.0F;
/*     */         
/* 166 */         float firstMix = (totalWidth <= 0.0F) ? 0.0F : (currentWidth / totalWidth);
/* 167 */         float lastMix = (totalWidth <= 0.0F) ? 1.0F : ((currentWidth + charWidth) / totalWidth);
/* 168 */         int firstColor = colorMix(leftColor, rightColor, firstMix);
/* 169 */         int lastColor = colorMix(leftColor, rightColor, lastMix);
/*     */         
/* 171 */         float firstAlpha = (firstColor >> 24 & 0xFF) / 255.0F;
/* 172 */         float firstRed = (firstColor >> 16 & 0xFF) / 255.0F;
/* 173 */         float firstGreen = (firstColor >> 8 & 0xFF) / 255.0F;
/* 174 */         float firstBlue = (firstColor & 0xFF) / 255.0F;
/* 175 */         float lastAlpha = (lastColor >> 24 & 0xFF) / 255.0F;
/* 176 */         float lastRed = (lastColor >> 16 & 0xFF) / 255.0F;
/* 177 */         float lastGreen = (lastColor >> 8 & 0xFF) / 255.0F;
/* 178 */         float lastBlue = (lastColor & 0xFF) / 255.0F;
/*     */         
/* 180 */         buffer.method_22918(matrix, (float)posX, (float)posY, 0.0F).method_22913(u0, v0).method_22915(firstRed, firstGreen, firstBlue, firstAlpha);
/* 181 */         buffer.method_22918(matrix, (float)posX, (float)posY + height, 0.0F).method_22913(u0, v1).method_22915(firstRed, firstGreen, firstBlue, firstAlpha);
/* 182 */         buffer.method_22918(matrix, (float)posX + width, (float)posY + height, 0.0F).method_22913(u1, v1).method_22915(lastRed, lastGreen, lastBlue, lastAlpha);
/* 183 */         buffer.method_22918(matrix, (float)posX + width, (float)posY, 0.0F).method_22913(u1, v0).method_22915(lastRed, lastGreen, lastBlue, lastAlpha);
/*     */         
/* 185 */         class_286.method_43433(buffer.method_60800());
/*     */         
/* 187 */         posX += charWidth;
/* 188 */         currentWidth += charWidth;
/*     */       } 
/*     */     } 
/* 191 */     RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
/* 192 */     return (float)posX / 2.0F;
/*     */   }
/*     */   
/*     */   private int colorMix(int startColor, int endColor, float mix) {
/* 196 */     float startAlpha = (startColor >> 24 & 0xFF) / 255.0F;
/* 197 */     float startRed = (startColor >> 16 & 0xFF) / 255.0F;
/* 198 */     float startGreen = (startColor >> 8 & 0xFF) / 255.0F;
/* 199 */     float startBlue = (startColor & 0xFF) / 255.0F;
/* 200 */     float endAlpha = (endColor >> 24 & 0xFF) / 255.0F;
/* 201 */     float endRed = (endColor >> 16 & 0xFF) / 255.0F;
/* 202 */     float endGreen = (endColor >> 8 & 0xFF) / 255.0F;
/* 203 */     float endBlue = (endColor & 0xFF) / 255.0F;
/* 204 */     int mixAlpha = (int)(((1.0F - mix) * startAlpha + mix * endAlpha) * 255.0F);
/* 205 */     int mixRed = (int)(((1.0F - mix) * startRed + mix * endRed) * 255.0F);
/* 206 */     int mixGreen = (int)(((1.0F - mix) * startGreen + mix * endGreen) * 255.0F);
/* 207 */     int mixBlue = (int)(((1.0F - mix) * startBlue + mix * endBlue) * 255.0F);
/* 208 */     return mixAlpha << 24 | mixRed << 16 | mixGreen << 8 | mixBlue;
/*     */   }
/*     */   public float drawString(String text, float x, float y, int color) {
/* 211 */     return drawString(text, x, y, color, false);
/*     */   }
/*     */   
/*     */   public float drawCenteredString(String text, float x, float y, int color) {
/* 215 */     return drawString(text, x - getStringWidth(text) / 2.0F, y, color);
/*     */   }
/*     */   
/*     */   public float drawCenteredStringWithShadow(String text, float x, float y, int color) {
/* 219 */     return drawStringWithShadow(text, (x - getStringWidth(text) / 2.0F), y, color);
/*     */   }
/*     */   
/*     */   public float drawString(String text, double x, double y, int color, boolean shadow) {
/* 223 */     x--;
/* 224 */     if (text == null) {
/* 225 */       return 0.0F;
/*     */     }
/* 227 */     if (color == 553648127) {
/* 228 */       color = 16777215;
/*     */     }
/* 230 */     if ((color & 0xFC000000) == 0) {
/* 231 */       color |= 0xFF000000;
/*     */     }
/* 233 */     if (shadow) {
/* 234 */       color = (color & 0xFCFCFC) >> 2 | color & (new Color(20, 20, 20, 200)).getRGB();
/*     */     }
/* 236 */     CFont.CharData[] currentData = this.charData;
/* 237 */     float alpha = (color >> 24 & 0xFF) / 255.0F;
/* 238 */     boolean bold = false;
/* 239 */     boolean italic = false;
/* 240 */     boolean strikethrough = false;
/* 241 */     boolean underline = false;
/* 242 */     x *= 2.0D;
/* 243 */     y = (y - 3.0D) * 2.0D;
/*     */     
/* 245 */     RenderSystem.enableBlend();
/* 246 */     RenderSystem.defaultBlendFunc();
/* 247 */     RenderSystem.setShaderColor((color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F, (color & 0xFF) / 255.0F, alpha);
/*     */     
/* 249 */     Matrix4f matrix = new Matrix4f();
/* 250 */     matrix.scale(0.5F, 0.5F, 0.5F);
/*     */     
/* 252 */     int size = text.length();
/* 253 */     int currentTexture = this.glTextureId;
/* 254 */     RenderSystem.setShaderTexture(0, currentTexture);
/* 255 */     RenderSystem.setShader(class_10142.field_53879);
/*     */     
/* 257 */     for (int i = 0; i < size; i++) {
/* 258 */       char character = text.charAt(i);
/* 259 */       if (String.valueOf(character).equals("§") && i < size - 1) {
/* 260 */         int colorIndex = 21;
/*     */         try {
/* 262 */           colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i + 1));
/* 263 */         } catch (Exception e) {
/* 264 */           e.printStackTrace();
/*     */         } 
/* 266 */         if (colorIndex < 16) {
/* 267 */           bold = false;
/* 268 */           italic = false;
/* 269 */           underline = false;
/* 270 */           strikethrough = false;
/* 271 */           currentTexture = this.glTextureId;
/* 272 */           currentData = this.charData;
/* 273 */           if (colorIndex < 0 || colorIndex > 15) {
/* 274 */             colorIndex = 15;
/*     */           }
/* 276 */           if (shadow) {
/* 277 */             colorIndex += 16;
/*     */           }
/* 279 */           int colorcode = this.colorCode[colorIndex];
/* 280 */           RenderSystem.setShaderColor((colorcode >> 16 & 0xFF) / 255.0F, (colorcode >> 8 & 0xFF) / 255.0F, (colorcode & 0xFF) / 255.0F, alpha);
/* 281 */         } else if (colorIndex == 17) {
/* 282 */           bold = true;
/* 283 */           if (italic) {
/* 284 */             currentTexture = this.texItalicBold;
/* 285 */             currentData = this.boldItalicChars;
/*     */           } else {
/* 287 */             currentTexture = this.texBold;
/* 288 */             currentData = this.boldChars;
/*     */           } 
/* 290 */         } else if (colorIndex == 18) {
/* 291 */           strikethrough = true;
/* 292 */         } else if (colorIndex == 19) {
/* 293 */           underline = true;
/* 294 */         } else if (colorIndex == 20) {
/* 295 */           italic = true;
/* 296 */           if (bold) {
/* 297 */             currentTexture = this.texItalicBold;
/* 298 */             currentData = this.boldItalicChars;
/*     */           } else {
/* 300 */             currentTexture = this.texItalic;
/* 301 */             currentData = this.italicChars;
/*     */           } 
/* 303 */         } else if (colorIndex == 21) {
/* 304 */           bold = false;
/* 305 */           italic = false;
/* 306 */           underline = false;
/* 307 */           strikethrough = false;
/* 308 */           RenderSystem.setShaderColor((color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F, (color & 0xFF) / 255.0F, alpha);
/* 309 */           currentTexture = this.glTextureId;
/* 310 */           currentData = this.charData;
/*     */         } 
/* 312 */         i++;
/*     */       
/*     */       }
/* 315 */       else if (character < currentData.length && currentData[character] != null) {
/*     */         
/* 317 */         RenderSystem.setShaderTexture(0, currentTexture);
/* 318 */         class_289 tessellator = class_289.method_1348();
/* 319 */         class_287 buffer = tessellator.method_60827(class_293.class_5596.field_27379, class_290.field_1585);
/*     */         
/* 321 */         drawChar(currentData, character, (float)x, (float)y, matrix, buffer);
/*     */         
/* 323 */         class_286.method_43433(buffer.method_60800());
/*     */         
/* 325 */         if (strikethrough) {
/* 326 */           drawLine(x, y + ((currentData[character]).height / 2.0F), x + (currentData[character]).width - 8.0D, y + ((currentData[character]).height / 2.0F), 1.0F, matrix);
/*     */         }
/* 328 */         if (underline) {
/* 329 */           drawLine(x, y + (currentData[character]).height - 2.0D, x + (currentData[character]).width - 8.0D, y + (currentData[character]).height - 2.0D, 1.0F, matrix);
/*     */         }
/* 331 */         x += ((currentData[character]).width - 8 + this.charOffset);
/*     */       } 
/*     */     } 
/* 334 */     RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
/* 335 */     return (float)x / 2.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStringWidth(String text) {
/* 340 */     int width = 0;
/* 341 */     CFont.CharData[] currentData = this.charData;
/* 342 */     boolean bold = false;
/* 343 */     boolean italic = false;
/* 344 */     int size = text.length();
/* 345 */     for (int i = 0; i < size; i++) {
/* 346 */       char character = text.charAt(i);
/* 347 */       if (String.valueOf(character).equals("§") && i < size - 1) {
/* 348 */         int colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i + 1));
/* 349 */         if (colorIndex < 16) {
/* 350 */           bold = false;
/* 351 */           italic = false;
/* 352 */         } else if (colorIndex == 17) {
/* 353 */           bold = true;
/* 354 */           currentData = italic ? this.boldItalicChars : this.boldChars;
/* 355 */         } else if (colorIndex == 20) {
/* 356 */           italic = true;
/* 357 */           currentData = bold ? this.boldItalicChars : this.italicChars;
/* 358 */         } else if (colorIndex == 21) {
/* 359 */           bold = false;
/* 360 */           italic = false;
/* 361 */           currentData = this.charData;
/*     */         } 
/* 363 */         i++;
/*     */       
/*     */       }
/* 366 */       else if (character < currentData.length && currentData[character] != null) {
/* 367 */         width += (currentData[character]).width - 8 + this.charOffset;
/*     */       } 
/* 369 */     }  return width / 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFont(Font font) {
/* 374 */     super.setFont(font);
/* 375 */     setupBoldItalicIDs();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAntiAlias(boolean antiAlias) {
/* 380 */     super.setAntiAlias(antiAlias);
/* 381 */     setupBoldItalicIDs();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFractionalMetrics(boolean fractionalMetrics) {
/* 386 */     super.setFractionalMetrics(fractionalMetrics);
/* 387 */     setupBoldItalicIDs();
/*     */   }
/*     */   
/*     */   private void setupBoldItalicIDs() {
/* 391 */     CFont boldFont = new CFont(this.font.deriveFont(1), this.antiAlias, this.fractionalMetrics);
/* 392 */     this.texBold = boldFont.getGlTextureId();
/* 393 */     this.boldChars = boldFont.charData;
/*     */     
/* 395 */     CFont italicFont = new CFont(this.font.deriveFont(2), this.antiAlias, this.fractionalMetrics);
/* 396 */     this.texItalic = italicFont.getGlTextureId();
/* 397 */     this.italicChars = italicFont.charData;
/*     */     
/* 399 */     CFont boldItalicFont = new CFont(this.font.deriveFont(3), this.antiAlias, this.fractionalMetrics);
/* 400 */     this.texItalicBold = boldItalicFont.getGlTextureId();
/* 401 */     this.boldItalicChars = boldItalicFont.charData;
/*     */   }
/*     */   
/*     */   private void drawLine(double x, double y, double x1, double y1, float width, Matrix4f matrix) {
/* 405 */     RenderSystem.setShader(class_10142.field_53875);
/* 406 */     RenderSystem.lineWidth(width);
/* 407 */     class_289 tessellator = class_289.method_1348();
/* 408 */     class_287 buffer = tessellator.method_60827(class_293.class_5596.field_27377, class_290.field_1592);
/* 409 */     buffer.method_22918(matrix, (float)x, (float)y, 0.0F);
/* 410 */     buffer.method_22918(matrix, (float)x1, (float)y1, 0.0F);
/* 411 */     class_286.method_43433(buffer.method_60800());
/*     */   }
/*     */   
/*     */   public void drawStringWithOutline(String text, double x, double y, int color) {
/* 415 */     drawString(text, x - 0.5D, y, Color.BLACK.getRGB(), false);
/* 416 */     drawString(text, x + 0.5D, y, Color.BLACK.getRGB(), false);
/* 417 */     drawString(text, x, y - 0.5D, Color.BLACK.getRGB(), false);
/* 418 */     drawString(text, x, y + 0.5D, Color.BLACK.getRGB(), false);
/* 419 */     drawString(text, x, y, color, false);
/*     */   }
/*     */   
/*     */   public void drawCenteredStringWithOutline(String text, float x, float y, int color) {
/* 423 */     drawCenteredString(text, x - 0.5F, y, Color.BLACK.getRGB());
/* 424 */     drawCenteredString(text, x + 0.5F, y, Color.BLACK.getRGB());
/* 425 */     drawCenteredString(text, x, y - 0.5F, Color.BLACK.getRGB());
/* 426 */     drawCenteredString(text, x, y + 0.5F, Color.BLACK.getRGB());
/* 427 */     drawCenteredString(text, x, y, color);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\render\fonts\ttf\MCFontRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */