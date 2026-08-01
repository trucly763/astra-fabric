/*     */ package shame.astra.api.utils.color;
/*     */ 
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import java.awt.Color;
/*     */ import net.minecraft.class_2561;
/*     */ import net.minecraft.class_2583;
/*     */ import net.minecraft.class_3532;
/*     */ import net.minecraft.class_5250;
/*     */ import net.minecraft.class_5251;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import shame.astra.api.utils.math.MathUtils;
/*     */ import shame.astra.astra;
/*     */ 
/*     */ public class ColorUtils
/*     */ {
/*  16 */   public static final Color green = new Color(36, 218, 118);
/*  17 */   public static final Color yellow = new Color(255, 196, 67);
/*  18 */   public static final Color orange = new Color(255, 134, 0);
/*  19 */   public static final Color red = new Color(239, 72, 54);
/*  20 */   public static final Color Blues = new Color(125, 217, 250);
/*     */   
/*     */   public static int red(int c) {
/*  23 */     return c >> 16 & 0xFF;
/*     */   }
/*     */   public static int green(int c) {
/*  26 */     return c >> 8 & 0xFF;
/*     */   }
/*     */   public static float redf(int c) {
/*  29 */     return red(c) / 255.0F;
/*     */   }
/*     */   
/*     */   public static float greenf(int c) {
/*  33 */     return green(c) / 255.0F;
/*     */   }
/*     */   
/*     */   public static float bluef(int c) {
/*  37 */     return blue(c) / 255.0F;
/*     */   }
/*     */   
/*     */   public static float alphaf(int c) {
/*  41 */     return alpha(c) / 255.0F;
/*     */   }
/*     */   public static int getColor(int brightness, int alpha) {
/*  44 */     return getColor(brightness, brightness, brightness, alpha);
/*     */   }
/*     */   
/*     */   public static int gradient(int color1, int color2, float amount) {
/*  48 */     amount = class_3532.method_15363(amount, 0.0F, 1.0F);
/*  49 */     int r = class_3532.method_48781(amount, red(color1), red(color2));
/*  50 */     int g = class_3532.method_48781(amount, green(color1), green(color2));
/*  51 */     int b = class_3532.method_48781(amount, blue(color1), blue(color2));
/*  52 */     int a = class_3532.method_48781(amount, alpha(color1), alpha(color2));
/*     */     
/*  54 */     return rgba(r, g, b, a);
/*     */   }
/*     */   
/*     */   public static int toColor(String hexColor) {
/*  58 */     if (hexColor == null || hexColor.length() != 7 || !hexColor.startsWith("#")) {
/*  59 */       return -16777216;
/*     */     }
/*     */     try {
/*  62 */       int rgb = Integer.parseInt(hexColor.substring(1), 16);
/*  63 */       return 0xFF000000 | rgb;
/*  64 */     } catch (NumberFormatException e) {
/*  65 */       return -16777216;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static int applyAlpha(int color, float alphaMul) {
/*  70 */     int a = color >> 24 & 0xFF;
/*  71 */     int na = (int)(a * Math.max(0.0F, Math.min(1.0F, alphaMul)));
/*  72 */     return color & 0xFFFFFF | na << 24;
/*     */   }
/*     */   
/*     */   public static int r(int color) {
/*  76 */     return color >> 16 & 0xFF;
/*     */   }
/*     */   public static int g(int color) {
/*  79 */     return color >> 8 & 0xFF;
/*     */   }
/*     */   
/*     */   public static int b(int color) {
/*  83 */     return color & 0xFF;
/*     */   }
/*     */   
/*     */   public static int a(int color) {
/*  87 */     return color >> 24 & 0xFF;
/*     */   }
/*     */   
/*     */   public static int hexToRgb(String hex) {
/*  91 */     if (hex.startsWith("#")) {
/*  92 */       hex = hex.substring(1);
/*     */     }
/*     */     
/*  95 */     if (hex.length() != 6) {
/*  96 */       throw new IllegalArgumentException("Недопустимый формат HEX: " + hex);
/*     */     }
/*     */     
/*  99 */     int r = Integer.parseInt(hex.substring(0, 2), 16);
/* 100 */     int g = Integer.parseInt(hex.substring(2, 4), 16);
/* 101 */     int b = Integer.parseInt(hex.substring(4, 6), 16);
/*     */     
/* 103 */     return rgb(r, g, b);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getThemeColor() {
/* 109 */     if (!astra.INSTANCE.themeStorage.getThemes().getTheme().getName().equals("Rainbow")) {
/* 110 */       return (astra.INSTANCE.themeStorage.getThemes().getTheme()).color[0];
/*     */     }
/* 112 */     return getThemeColor(0);
/*     */   }
/*     */   
/*     */   public static int getThemeColor(int index) {
/* 116 */     return astra.INSTANCE.themeStorage.getThemes().getTheme().getColor(index);
/*     */   }
/*     */   public static int getThemeStaticColor() {
/* 119 */     return (astra.INSTANCE.themeStorage.getThemes().getTheme()).color[0];
/*     */   }
/*     */   public static int rainbow(int speed, int index, float saturation, float brightness, float opacity) {
/* 122 */     int angle = (int)((System.currentTimeMillis() / speed + index) % 360L);
/* 123 */     float hue = angle / 360.0F;
/* 124 */     int color = Color.HSBtoRGB(hue, saturation, brightness);
/* 125 */     return getColor(
/* 126 */         red(color), 
/* 127 */         green(color), 
/* 128 */         blue(color), 
/* 129 */         Math.max(0, Math.min(255, (int)(opacity * 255.0F))));
/*     */   }
/*     */ 
/*     */   
/*     */   public static int interpolate(int color1, int color2, double amount) {
/* 134 */     amount = (float)MathUtils.clamp(0.0D, 1.0D, amount);
/* 135 */     return getColor((
/* 136 */         (Integer)MathUtils.ler1p(Integer.valueOf(red(color1)), Integer.valueOf(red(color2)), amount)).intValue(), (
/* 137 */         (Integer)MathUtils.ler1p(Integer.valueOf(green(color1)), Integer.valueOf(green(color2)), amount)).intValue(), (
/* 138 */         (Integer)MathUtils.ler1p(Integer.valueOf(blue(color1)), Integer.valueOf(blue(color2)), amount)).intValue(), (
/* 139 */         (Integer)MathUtils.ler1p(Integer.valueOf(alpha(color1)), Integer.valueOf(alpha(color2)), amount)).intValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public static int[] genGradientForText(int color1, int color2, int length) {
/* 144 */     int[] gradient = new int[length];
/* 145 */     for (int i = 0; i < length; i++) {
/* 146 */       double pc = i / (length - 1);
/* 147 */       gradient[i] = interpolate(color1, color2, pc);
/*     */     } 
/* 149 */     return gradient;
/*     */   }
/*     */   
/*     */   public static int blue(int c) {
/* 153 */     return c & 0xFF;
/*     */   }
/*     */   
/*     */   public static int overCol(int c1, int c2, float pc01) {
/* 157 */     return getColor(red(c1) * (1.0F - pc01) + red(c2) * pc01, green(c1) * (1.0F - pc01) + green(c2) * pc01, blue(c1) * (1.0F - pc01) + blue(c2) * pc01, alpha(c1) * (1.0F - pc01) + alpha(c2) * pc01);
/*     */   }
/*     */   
/*     */   public static int darken(int color, float factor) {
/* 161 */     float[] rgb = getColorT(color);
/* 162 */     float[] hsb = Color.RGBtoHSB((int)(rgb[0] * 255.0F), (int)(rgb[1] * 255.0F), (int)(rgb[2] * 255.0F), null);
/*     */     
/* 164 */     hsb[2] = hsb[2] * factor;
/* 165 */     hsb[2] = Math.max(0.0F, Math.min(1.0F, hsb[2]));
/*     */     
/* 167 */     int darkenedRGB = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
/* 168 */     return applyOpacity(darkenedRGB, (int)(rgb[3] * 255.0F));
/*     */   }
/*     */   
/*     */   public static int multDark(int c, float brpc) {
/* 172 */     return getColor(red(c) * brpc, green(c) * brpc, blue(c) * brpc, alpha(c));
/*     */   }
/*     */   
/*     */   public static int overCol(int c1, int c2) {
/* 176 */     return overCol(c1, c2, 0.5F);
/*     */   }
/*     */   
/*     */   public static int alpha(int c) {
/* 180 */     return c >> 24 & 0xFF;
/*     */   }
/*     */   
/*     */   public static int multAlpha(int c, float apc) {
/* 184 */     return getColor(red(c), green(c), blue(c), alpha(c) * apc);
/*     */   }
/*     */   
/*     */   public static int replAlpha(int color, int alpha) {
/* 188 */     alpha = Math.max(0, Math.min(255, alpha));
/* 189 */     return alpha << 24 | color & 0xFFFFFF;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Color random() {
/* 194 */     return new Color(Color.HSBtoRGB((float)Math.random(), (float)(0.75D + Math.random() / 4.0D), (float)(0.75D + Math.random() / 4.0D)));
/*     */   }
/*     */   public static int getColor(float r, float g, float b, float a) {
/* 197 */     return (new Color((int)r, (int)g, (int)b, (int)a)).getRGB();
/*     */   }
/*     */   
/*     */   public static float[] getRGBAf(int c) {
/* 201 */     return new float[] { redf(c), greenf(c), bluef(c), alphaf(c) };
/*     */   }
/*     */   
/*     */   public static float[] getRGBAf1(int c) {
/* 205 */     return new float[] { red(c) / 255.0F, green(c) / 255.0F, blue(c) / 255.0F, alpha(c) / 255.0F };
/*     */   }
/*     */   public static Color interpolateTwoColors(int speed, int index, Color start, Color end, boolean trueColor) {
/* 208 */     int angle = 0;
/* 209 */     if (speed == 0) {
/* 210 */       angle = index % 360;
/*     */     } else {
/* 212 */       angle = (int)((System.currentTimeMillis() / speed + index) % 360L);
/*     */     } 
/* 214 */     angle = ((angle >= 180) ? (360 - angle) : angle) * 2;
/* 215 */     boolean tur = trueColor;
/* 216 */     return tur ? interpolateColorHue(start, end, angle / 360.0F) : interpolateColorC(start, end, angle / 360.0F);
/*     */   }
/*     */   public static Color interpolateTwoColors(int speed, int index, Color start, Color end) {
/* 219 */     return interpolateTwoColors(speed, index, start, end, false);
/*     */   }
/*     */   public static Color astolfo(float yDist, float yTotal, float saturation, float speedt) {
/* 222 */     float speed = 1800.0F;
/* 223 */     float hue = (float)(System.currentTimeMillis() % (int)speed) + (yTotal - yDist) * speedt;
/* 224 */     while (hue > speed) {
/* 225 */       hue -= speed;
/*     */     }
/* 227 */     hue /= speed;
/* 228 */     if (hue > 1.0F) {
/* 229 */       hue = 1.0F - hue - 1.0F;
/*     */     }
/* 231 */     hue++;
/* 232 */     return Color.getHSBColor(hue, saturation, 1.0F);
/*     */   }
/*     */   
/*     */   private static int calculateHueDegrees(int divisor, int offset) {
/* 236 */     long currentTime = System.currentTimeMillis();
/* 237 */     long calculatedValue = (currentTime / divisor + offset) % 360L;
/* 238 */     return (int)calculatedValue;
/*     */   }
/*     */   
/*     */   public static void setColor(Color color, float alpha) {
/* 242 */     float red = color.getRed() / 255.0F;
/* 243 */     float green = color.getGreen() / 255.0F;
/* 244 */     float blue = color.getBlue() / 255.0F;
/*     */     
/* 246 */     RenderSystem.setShaderColor(red, green, blue, alpha);
/*     */   }
/*     */   
/*     */   public static int rgb(int r, int g, int b) {
/* 250 */     return 0xFF000000 | r << 16 | g << 8 | b;
/*     */   }
/*     */   public static int rgba(int r, int g, int b, int a) {
/* 253 */     return a << 24 | r << 16 | g << 8 | b;
/*     */   }
/*     */   public static float[] rgba(int color) {
/* 256 */     return new float[] { (color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F, (color & 0xFF) / 255.0F, (color >> 24 & 0xFF) / 255.0F };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int rgba(double r, double g, double b, double a) {
/* 265 */     return rgba((int)r, (int)g, (int)b, (int)a);
/*     */   }
/*     */   public static int getRed(int hex) {
/* 268 */     return hex >> 16 & 0xFF;
/*     */   }
/*     */   
/*     */   public static int getGreen(int hex) {
/* 272 */     return hex >> 8 & 0xFF;
/*     */   }
/*     */   public static int interpolate(int start, int end, float value) {
/* 275 */     float[] startColor = rgba(start);
/* 276 */     float[] endColor = rgba(end);
/*     */     
/* 278 */     return rgba((int)MathUtils.interpolate(startColor[0] * 255.0F, endColor[0] * 255.0F, value), 
/* 279 */         (int)MathUtils.interpolate(startColor[1] * 255.0F, endColor[1] * 255.0F, value), 
/* 280 */         (int)MathUtils.interpolate(startColor[2] * 255.0F, endColor[2] * 255.0F, value), 
/* 281 */         (int)MathUtils.interpolate(startColor[3] * 255.0F, endColor[3] * 255.0F, value));
/*     */   }
/*     */   public static int interpolateColor(int color1, int color2, float amount) {
/* 284 */     amount = Math.min(1.0F, Math.max(0.0F, amount));
/*     */     
/* 286 */     int red1 = getRed(color1);
/* 287 */     int green1 = getGreen(color1);
/* 288 */     int blue1 = getBlue(color1);
/* 289 */     int alpha1 = getAlpha(color1);
/*     */     
/* 291 */     int red2 = getRed(color2);
/* 292 */     int green2 = getGreen(color2);
/* 293 */     int blue2 = getBlue(color2);
/* 294 */     int alpha2 = getAlpha(color2);
/*     */     
/* 296 */     int interpolatedRed = interpolateInt(red1, red2, amount);
/* 297 */     int interpolatedGreen = interpolateInt(green1, green2, amount);
/* 298 */     int interpolatedBlue = interpolateInt(blue1, blue2, amount);
/* 299 */     int interpolatedAlpha = interpolateInt(alpha1, alpha2, amount);
/*     */     
/* 301 */     return interpolatedAlpha << 24 | interpolatedRed << 16 | interpolatedGreen << 8 | interpolatedBlue;
/*     */   }
/*     */   
/*     */   public static class_5250 gradient(String message, int first, int end) {
/* 305 */     class_5250 text = class_2561.method_43473();
/*     */     
/* 307 */     for (int i = 0; i < message.length(); i++) {
/* 308 */       int color = interpolateColor(first, end, i / message.length());
/*     */       
/* 310 */       class_5250 charText = class_2561.method_43470(String.valueOf(message.charAt(i))).method_10862(class_2583.field_24360.method_27703(class_5251.method_27717(color)));
/* 311 */       text.method_10852((class_2561)charText);
/*     */     } 
/*     */     
/* 314 */     return text;
/*     */   }
/*     */   
/*     */   public static class_2561 replace(class_2561 original, String find, String replaceWith) {
/* 318 */     if (original == null || find == null || replaceWith == null) {
/* 319 */       return original;
/*     */     }
/*     */     
/* 322 */     String originalText = original.getString();
/* 323 */     String replacedText = originalText.replace(find, replaceWith);
/* 324 */     return (class_2561)class_2561.method_43470(replacedText);
/*     */   }
/*     */   
/*     */   public static int gradient(int speed, int index, int... colors) {
/* 328 */     int angle = (int)((System.currentTimeMillis() / speed + index) % 360L);
/* 329 */     angle = ((angle > 180) ? (360 - angle) : angle) + 180;
/* 330 */     int colorIndex = (int)(angle / 360.0F * colors.length);
/* 331 */     if (colorIndex == colors.length) {
/* 332 */       colorIndex--;
/*     */     }
/* 334 */     int color1 = colors[colorIndex];
/* 335 */     int color2 = colors[(colorIndex == colors.length - 1) ? 0 : (colorIndex + 1)];
/* 336 */     return interpolateColor(color1, color2, angle / 360.0F * colors.length - colorIndex);
/*     */   }
/*     */   
/*     */   public static int themeGradient(int speed, int index, float darkenFactor) {
/* 340 */     int theme = getThemeColor();
/* 341 */     return gradient(speed, index, new int[] { theme, darken(theme, darkenFactor) });
/*     */   }
/*     */   
/*     */   public static int getBlue(int hex) {
/* 345 */     return hex & 0xFF;
/*     */   }
/*     */   
/*     */   public static int getAlpha(int hex) {
/* 349 */     return hex >> 24 & 0xFF;
/*     */   }
/*     */   public static int getColor(int red, int green, int blue, int alpha) {
/* 352 */     int color = 0;
/* 353 */     color |= alpha << 24;
/* 354 */     color |= red << 16;
/* 355 */     color |= green << 8;
/* 356 */     return color | blue;
/*     */   }
/*     */   
/*     */   public static int getColor(int bright) {
/* 360 */     return getColor(bright, bright, bright, 255);
/*     */   }
/*     */   public static float[] getColorA(int color) {
/* 363 */     return new float[] { red(color) / 255.0F, green(color) / 255.0F, blue(color) / 255.0F, alphaf(color) };
/*     */   }
/*     */   
/*     */   public static float[] getColorT(int color) {
/* 367 */     return new float[] { red(color) / 255.0F, green(color) / 255.0F, blue(color) / 255.0F, alphaf(color) };
/*     */   }
/*     */   
/*     */   public static void setColor(double red, double green, double blue, double alpha) {
/* 371 */     GL11.glColor4d(red, green, blue, alpha);
/*     */   }
/*     */   
/*     */   public static int setAlphaColor(int color, int alpha) {
/* 375 */     return color & 0xFFFFFF | alpha << 24;
/*     */   }
/*     */ 
/*     */   
/*     */   public static float lerp(float a, float b, float f) {
/* 380 */     return a + f * (b - a);
/*     */   }
/*     */   
/*     */   public static Color interpolateColorC(Color color1, Color color2, float amount) {
/* 384 */     amount = Math.min(1.0F, Math.max(0.0F, amount));
/* 385 */     return new Color(interpolateInt(color1.getRed(), color2.getRed(), amount), 
/* 386 */         interpolateInt(color1.getGreen(), color2.getGreen(), amount), 
/* 387 */         interpolateInt(color1.getBlue(), color2.getBlue(), amount), 
/* 388 */         interpolateInt(color1.getAlpha(), color2.getAlpha(), amount));
/*     */   }
/*     */   
/*     */   public static Double interpolate(double oldValue, double newValue, double interpolationValue) {
/* 392 */     return Double.valueOf(oldValue + (newValue - oldValue) * interpolationValue);
/*     */   }
/*     */   
/*     */   public static float interpolateFloat(float oldValue, float newValue, double interpolationValue) {
/* 396 */     return interpolate(oldValue, newValue, (float)interpolationValue).floatValue();
/*     */   }
/*     */   
/*     */   public static int interpolateInt(int oldValue, int newValue, double interpolationValue) {
/* 400 */     return interpolate(oldValue, newValue, (float)interpolationValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Color interpolateColorHue(Color color1, Color color2, float amount) {
/* 405 */     amount = Math.min(1.0F, Math.max(0.0F, amount));
/*     */     
/* 407 */     float[] color1HSB = Color.RGBtoHSB(color1.getRed(), color1.getGreen(), color1.getBlue(), null);
/* 408 */     float[] color2HSB = Color.RGBtoHSB(color2.getRed(), color2.getGreen(), color2.getBlue(), null);
/*     */     
/* 410 */     Color resultColor = Color.getHSBColor(interpolateFloat(color1HSB[0], color2HSB[0], amount), 
/* 411 */         interpolateFloat(color1HSB[1], color2HSB[1], amount), interpolateFloat(color1HSB[2], color2HSB[2], amount));
/*     */     
/* 413 */     return new Color(resultColor.getRed(), resultColor.getGreen(), resultColor.getBlue(), 
/* 414 */         interpolateInt(color1.getAlpha(), color2.getAlpha(), amount));
/*     */   }
/*     */   
/*     */   public static void setColor(Color color) {
/* 418 */     if (color == null)
/* 419 */       color = Color.white; 
/* 420 */     setColor((color.getRed() / 255.0F), (color.getGreen() / 255.0F), (color.getBlue() / 255.0F), (color.getAlpha() / 255.0F));
/*     */   }
/*     */   
/*     */   public static void setColor(int color) {
/* 424 */     setColor(color, (color >> 24 & 0xFF) / 255.0F);
/*     */   }
/*     */   
/*     */   public static void setColor(int color, float alpha) {
/* 428 */     float r = (color >> 16 & 0xFF) / 255.0F;
/* 429 */     float g = (color >> 8 & 0xFF) / 255.0F;
/* 430 */     float b = (color & 0xFF) / 255.0F;
/* 431 */     RenderSystem.setShaderColor(r, g, b, alpha);
/*     */   }
/*     */   
/*     */   public static int applyOpacity(int color, float alpha) {
/* 435 */     return rgba(getRed(color), getGreen(color), getBlue(color), (getAlpha(color) * alpha / 255.0F));
/*     */   }
/*     */   
/*     */   public static int reFactorColor(int color, float factor) {
/* 439 */     return rgba((extractRedf(color) * factor), (extractGreenf(color) * factor), (extractBluef(color) * factor), extractAlphaf(color));
/*     */   }
/*     */   
/*     */   public static float extractRedf(int color) {
/* 443 */     return (color >> 16 & 0xFF) / 255.0F;
/*     */   }
/*     */   
/*     */   public static int extractRed(int color) {
/* 447 */     return color >> 16 & 0xFF;
/*     */   }
/*     */   
/*     */   public static float extractBluef(int color) {
/* 451 */     return (color & 0xFF) / 255.0F;
/*     */   }
/*     */   
/*     */   public static int extractBlue(int color) {
/* 455 */     return color & 0xFF;
/*     */   }
/*     */   
/*     */   public static float extractGreenf(int color) {
/* 459 */     return (color >> 8 & 0xFF) / 255.0F;
/*     */   }
/*     */   
/*     */   public static int extractGreen(int color) {
/* 463 */     return color >> 8 & 0xFF;
/*     */   }
/*     */   
/*     */   public static float extractAlphaf(int color) {
/* 467 */     return (color >> 24 & 0xFF) / 255.0F;
/*     */   }
/*     */   
/*     */   public static int extractAlpha(int color) {
/* 471 */     return color >> 24 & 0xFF;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\color\ColorUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */