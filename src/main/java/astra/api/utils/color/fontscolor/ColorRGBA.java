/*     */ package shame.astra.api.utils.color.fontscolor;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Objects;
/*     */ import lombok.Generated;
/*     */ import net.minecraft.class_3532;
/*     */ import shame.astra.api.utils.color.ColorUtils;
/*     */ import shame.astra.api.utils.math.MathUtils;
/*     */ 
/*     */ public class ColorRGBA {
/*  12 */   public static final ColorRGBA WHITE = new ColorRGBA(255, 255, 255);
/*  13 */   public static final ColorRGBA BLACK = new ColorRGBA(0, 0, 0);
/*  14 */   public static final ColorRGBA GREEN = new ColorRGBA(0, 255, 0);
/*  15 */   public static final ColorRGBA RED = new ColorRGBA(255, 0, 0);
/*  16 */   public static final ColorRGBA BLUE = new ColorRGBA(0, 0, 255);
/*  17 */   public static final ColorRGBA YELLOW = new ColorRGBA(255, 255, 0);
/*  18 */   public static final ColorRGBA GRAY = new ColorRGBA(88, 87, 93);
/*  19 */   public static final ColorRGBA TRANSPARENT = new ColorRGBA(0, 0, 0, 0);
/*     */   private transient float[] hsbValues;
/*     */   private final int red;
/*     */   private final int green;
/*     */   private final int blue;
/*     */   private final int alpha;
/*  25 */   private static final ByteBuffer PIXEL_BUFFER = ByteBuffer.allocateDirect(4);
/*     */   
/*     */   public ColorRGBA(int color) {
/*  28 */     this(ColorUtils.red(color), ColorUtils.green(color), ColorUtils.blue(color), ColorUtils.alpha(color));
/*     */   }
/*     */   
/*     */   public ColorRGBA(Color color) {
/*  32 */     this(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
/*     */   }
/*     */   
/*     */   public ColorRGBA(int red, int green, int blue) {
/*  36 */     this(red, green, blue, 255);
/*     */   }
/*     */   
/*     */   public ColorRGBA(int red, int green, int blue, int alpha) {
/*  40 */     red = class_3532.method_15340(red, 0, 255);
/*  41 */     green = class_3532.method_15340(green, 0, 255);
/*  42 */     blue = class_3532.method_15340(blue, 0, 255);
/*  43 */     alpha = class_3532.method_15340(alpha, 0, 255);
/*  44 */     this.red = red;
/*  45 */     this.green = green;
/*  46 */     this.blue = blue;
/*  47 */     this.alpha = alpha;
/*     */   }
/*     */   
/*     */   public ColorRGBA(int red, int green, int blue, float alpha) {
/*  51 */     red = class_3532.method_15340(red, 0, 255);
/*  52 */     green = class_3532.method_15340(green, 0, 255);
/*  53 */     blue = class_3532.method_15340(blue, 0, 255);
/*  54 */     alpha = class_3532.method_15363(alpha, 0.0F, 255.0F);
/*  55 */     this.red = red;
/*  56 */     this.green = green;
/*  57 */     this.blue = blue;
/*  58 */     this.alpha = (int)alpha;
/*     */   }
/*     */   
/*     */   public int getRGB() {
/*  62 */     int a = Math.round(clamp(this.alpha));
/*  63 */     int r = Math.round(clamp(this.red));
/*  64 */     int g = Math.round(clamp(this.green));
/*  65 */     int b = Math.round(clamp(this.blue));
/*  66 */     return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
/*     */   }
/*     */   
/*     */   private int clamp(float value) {
/*  70 */     return (int)Math.max(0.0F, Math.min(255.0F, value));
/*     */   }
/*     */   
/*     */   public static ColorRGBA fromHex(String hex) {
/*  74 */     String sanitized = hex.startsWith("#") ? hex.substring(1) : hex;
/*  75 */     if (sanitized.length() != 6 && sanitized.length() != 8) {
/*  76 */       throw new IllegalArgumentException("Hex color must be in the format #RRGGBB or #RRGGBBAA");
/*     */     }
/*  78 */     int red = Integer.parseInt(sanitized.substring(0, 2), 16);
/*  79 */     int green = Integer.parseInt(sanitized.substring(2, 4), 16);
/*  80 */     int blue = Integer.parseInt(sanitized.substring(4, 6), 16);
/*  81 */     int alpha = (sanitized.length() == 8) ? Integer.parseInt(sanitized.substring(6, 8), 16) : 255;
/*  82 */     return new ColorRGBA(red, green, blue, alpha);
/*     */   }
/*     */ 
/*     */   
/*     */   public static ColorRGBA lerp(ColorRGBA startColor, ColorRGBA endColor, float delta) {
/*  87 */     float clampedDelta = Math.max(0.0F, Math.min(1.0F, delta));
/*  88 */     int r = (int)(startColor.getRed() + (endColor.getRed() - startColor.getRed()) * clampedDelta);
/*  89 */     int g = (int)(startColor.getGreen() + (endColor.getGreen() - startColor.getGreen()) * clampedDelta);
/*  90 */     int b = (int)(startColor.getBlue() + (endColor.getBlue() - startColor.getBlue()) * clampedDelta);
/*  91 */     int a = (int)(startColor.getAlpha() + (endColor.getAlpha() - startColor.getAlpha()) * clampedDelta);
/*  92 */     return new ColorRGBA(r, g, b, a);
/*     */   }
/*     */   
/*     */   public static ColorRGBA fromInt(int colorInt) {
/*  96 */     int alpha = colorInt >> 24 & 0xFF;
/*  97 */     int red = colorInt >> 16 & 0xFF;
/*  98 */     int green = colorInt >> 8 & 0xFF;
/*  99 */     int blue = colorInt & 0xFF;
/* 100 */     return new ColorRGBA(red, green, blue, alpha);
/*     */   }
/*     */   
/*     */   public ColorRGBA withAlpha(float newAlpha) {
/* 104 */     return new ColorRGBA(this.red, this.green, this.blue, (int)newAlpha);
/*     */   }
/*     */   
/*     */   public ColorRGBA withAlpha(int newAlpha) {
/* 108 */     return new ColorRGBA(this.red, this.green, this.blue, newAlpha);
/*     */   }
/*     */   
/*     */   public ColorRGBA mulAlpha(float percent) {
/* 112 */     return withAlpha((int)(this.alpha * percent));
/*     */   }
/*     */   
/*     */   public ColorRGBA mix(ColorRGBA color2, float amount) {
/* 116 */     amount = Math.min(1.0F, Math.max(0.0F, amount));
/* 117 */     return new ColorRGBA((int)MathUtils.interpolate(getRed(), color2.getRed(), amount), (int)MathUtils.interpolate(getGreen(), color2.getGreen(), amount), (int)MathUtils.interpolate(getBlue(), color2.getBlue(), amount), (int)MathUtils.interpolate(getAlpha(), color2.getAlpha(), amount));
/*     */   }
/*     */   
/*     */   public ColorRGBA darker(float amount) {
/* 121 */     amount = class_3532.method_15363(amount, 0.0F, 1.0F);
/* 122 */     return new ColorRGBA((int)(this.red * (1.0F - amount)), (int)(this.green * (1.0F - amount)), (int)(this.blue * (1.0F - amount)), this.alpha);
/*     */   }
/*     */   
/*     */   public static ColorRGBA fromHSB(float hue, float saturation, float brightness) {
/* 126 */     if (saturation == 0.0F) {
/* 127 */       int grayValue = (int)(brightness * 255.0F + 0.5F);
/* 128 */       return new ColorRGBA(grayValue, grayValue, grayValue);
/*     */     } 
/* 130 */     float h = (hue - (float)Math.floor(hue)) * 6.0F;
/* 131 */     float f = h - (float)Math.floor(h);
/* 132 */     float p = brightness * (1.0F - saturation);
/* 133 */     float q = brightness * (1.0F - saturation * f);
/* 134 */     float t = brightness * (1.0F - saturation * (1.0F - f));
/* 135 */     float r = 0.0F;
/* 136 */     float g = 0.0F;
/* 137 */     float b = 0.0F;
/* 138 */     switch ((int)h) {
/*     */       case 0:
/* 140 */         r = brightness;
/* 141 */         g = t;
/* 142 */         b = p;
/*     */         break;
/*     */       case 1:
/* 145 */         r = q;
/* 146 */         g = brightness;
/* 147 */         b = p;
/*     */         break;
/*     */       case 2:
/* 150 */         r = p;
/* 151 */         g = brightness;
/* 152 */         b = t;
/*     */         break;
/*     */       case 3:
/* 155 */         r = p;
/* 156 */         g = q;
/* 157 */         b = brightness;
/*     */         break;
/*     */       case 4:
/* 160 */         r = t;
/* 161 */         g = p;
/* 162 */         b = brightness;
/*     */         break;
/*     */       case 5:
/* 165 */         r = brightness;
/* 166 */         g = p;
/* 167 */         b = q;
/*     */         break;
/*     */     } 
/* 170 */     return new ColorRGBA((int)(r * 255.0F), (int)(g * 255.0F), (int)(b * 255.0F));
/*     */   }
/*     */ 
/*     */   
/*     */   public float getHue() {
/* 175 */     return getHSBValues()[0];
/*     */   }
/*     */   
/*     */   public float getSaturation() {
/* 179 */     return getHSBValues()[2];
/*     */   }
/*     */   
/*     */   public float getBrightness() {
/* 183 */     return getHSBValues()[1];
/*     */   }
/*     */   
/*     */   private float[] getHSBValues() {
/* 187 */     if (this.hsbValues == null) {
/* 188 */       this.hsbValues = calculateHSB();
/*     */     }
/*     */     
/* 191 */     return this.hsbValues;
/*     */   }
/*     */   
/*     */   private float[] calculateHSB() {
/* 195 */     float r = this.red / 255.0F;
/* 196 */     float g = this.green / 255.0F;
/* 197 */     float b = this.blue / 255.0F;
/* 198 */     float maxC = Math.max(r, Math.max(g, b));
/* 199 */     float minC = Math.min(r, Math.min(g, b));
/* 200 */     float delta = maxC - minC;
/* 201 */     float hue = 0.0F;
/* 202 */     if (delta != 0.0F) {
/* 203 */       if (maxC == r) {
/* 204 */         hue = (g - b) / delta;
/* 205 */       } else if (maxC == g) {
/* 206 */         hue = (b - r) / delta + 2.0F;
/*     */       } else {
/* 208 */         hue = (r - g) / delta + 4.0F;
/*     */       } 
/*     */       
/* 211 */       hue /= 6.0F;
/* 212 */       if (hue < 0.0F) {
/* 213 */         hue++;
/*     */       }
/*     */     } 
/*     */     
/* 217 */     float saturation = (maxC == 0.0F) ? 0.0F : (delta / maxC);
/* 218 */     return new float[] { hue, saturation, maxC };
/*     */   }
/*     */   
/*     */   public ColorRGBA brighter(float amount) {
/* 222 */     amount = class_3532.method_15363(amount, 0.0F, 1.0F);
/* 223 */     return new ColorRGBA((int)(this.red + (255.0F - this.red) * amount), (int)(this.green + (255.0F - this.green) * amount), (int)(this.blue + (255.0F - this.blue) * amount), this.alpha);
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 227 */     if (this == o)
/* 228 */       return true; 
/* 229 */     if (o != null && getClass() == o.getClass()) {
/* 230 */       ColorRGBA colorRGBA = (ColorRGBA)o;
/* 231 */       return (Float.compare(this.red, colorRGBA.red) == 0 && Float.compare(this.green, colorRGBA.green) == 0 && Float.compare(this.blue, colorRGBA.blue) == 0 && Float.compare(this.alpha, colorRGBA.alpha) == 0);
/*     */     } 
/* 233 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public float difference(ColorRGBA colorRGBA) {
/* 238 */     return Math.abs(getHue() - colorRGBA.getHue()) + Math.abs(getBrightness() - colorRGBA.getBrightness()) + Math.abs(getSaturation() - colorRGBA.getSaturation());
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 242 */     return Objects.hash(new Object[] { Integer.valueOf(this.red), Integer.valueOf(this.green), Integer.valueOf(this.blue), Integer.valueOf(this.alpha) });
/*     */   }
/*     */   
/*     */   @Generated
/*     */   public int getRed() {
/* 247 */     return this.red;
/*     */   }
/*     */   
/*     */   @Generated
/*     */   public int getGreen() {
/* 252 */     return this.green;
/*     */   }
/*     */   
/*     */   @Generated
/*     */   public int getBlue() {
/* 257 */     return this.blue;
/*     */   }
/*     */   
/*     */   @Generated
/*     */   public int getAlpha() {
/* 262 */     return this.alpha;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\color\fontscolor\ColorRGBA.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */