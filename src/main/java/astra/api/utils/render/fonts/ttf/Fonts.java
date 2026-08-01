/*     */ package shame.astra.api.utils.render.fonts.ttf;
/*     */ 
/*     */ import java.awt.Font;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import lombok.Generated;
/*     */ import net.minecraft.class_2960;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Fonts
/*     */ {
/*     */   private static final String MOD_ID = "astra";
/*  14 */   private static final Map<String, Map<Float, MCFontRenderer>> regularFonts = new HashMap<>();
/*  15 */   private static final Map<String, Map<Float, GradientFontRenderer>> gradientFonts = new HashMap<>();
/*     */   
/*     */   public static MCFontRenderer comfortaa16;
/*     */   public static MCFontRenderer comfortaa18;
/*     */   public static MCFontRenderer comfortaa20;
/*     */   public static GradientFontRenderer comfortaaGradient18;
/*     */   public static MCFontRenderer roboto16;
/*     */   public static MCFontRenderer roboto18;
/*     */   public static MCFontRenderer roboto20;
/*     */   public static GradientFontRenderer robotoGradient18;
/*     */   public static MCFontRenderer montserrat16;
/*     */   public static MCFontRenderer montserrat18;
/*     */   public static MCFontRenderer montserrat20;
/*     */   public static GradientFontRenderer montserratGradient18;
/*     */   
/*     */   @Generated
/*     */   public static boolean isInitialized() {
/*  32 */     return initialized;
/*     */   }
/*     */   
/*     */   public static void init() {
/*  36 */     if (initialized)
/*     */       return; 
/*  38 */     comfortaa16 = getFont("comfortaa.ttf", 16.0F);
/*  39 */     comfortaa18 = getFont("comfortaa.ttf", 18.0F);
/*  40 */     comfortaa20 = getFont("comfortaa.ttf", 20.0F);
/*  41 */     comfortaaGradient18 = getGradientFont("comfortaa.ttf", 18.0F);
/*     */     
/*  43 */     roboto16 = getFont("roboto.ttf", 16.0F);
/*  44 */     roboto18 = getFont("roboto.ttf", 18.0F);
/*  45 */     roboto20 = getFont("roboto.ttf", 20.0F);
/*  46 */     robotoGradient18 = getGradientFont("roboto.ttf", 18.0F);
/*     */     
/*  48 */     montserrat16 = getFont("montserrat.ttf", 16.0F);
/*  49 */     montserrat18 = getFont("montserrat.ttf", 18.0F);
/*  50 */     montserrat20 = getFont("montserrat.ttf", 20.0F);
/*  51 */     montserratGradient18 = getGradientFont("montserrat.ttf", 18.0F);
/*     */     
/*  53 */     initialized = true;
/*     */   }
/*     */   private static boolean initialized = false;
/*     */   public static MCFontRenderer getFont(String fontName, float size) {
/*  57 */     regularFonts.computeIfAbsent(fontName, k -> new HashMap<>());
/*     */     
/*  59 */     Map<Float, MCFontRenderer> fontSizes = regularFonts.get(fontName);
/*     */     
/*  61 */     if (fontSizes.containsKey(Float.valueOf(size))) {
/*  62 */       return fontSizes.get(Float.valueOf(size));
/*     */     }
/*     */     
/*  65 */     Font font = FontUtil.getFontFromTTF(
/*  66 */         class_2960.method_60655("astra", "fonts/ttf/" + fontName), size, 0);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  71 */     if (font == null) {
/*  72 */       font = new Font("Arial", 0, (int)size);
/*     */     }
/*     */     
/*  75 */     MCFontRenderer renderer = new MCFontRenderer(font, true, true);
/*  76 */     fontSizes.put(Float.valueOf(size), renderer);
/*  77 */     return renderer;
/*     */   }
/*     */   
/*     */   public static GradientFontRenderer getGradientFont(String fontName, float size) {
/*  81 */     gradientFonts.computeIfAbsent(fontName, k -> new HashMap<>());
/*     */     
/*  83 */     Map<Float, GradientFontRenderer> fontSizes = gradientFonts.get(fontName);
/*     */     
/*  85 */     if (fontSizes.containsKey(Float.valueOf(size))) {
/*  86 */       return fontSizes.get(Float.valueOf(size));
/*     */     }
/*     */     
/*  89 */     Font font = FontUtil.getFontFromTTF(
/*  90 */         class_2960.method_60655("astra", "fonts/" + fontName), size, 0);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  95 */     if (font == null) {
/*  96 */       font = new Font("Arial", 0, (int)size);
/*     */     }
/*     */     
/*  99 */     GradientFontRenderer renderer = new GradientFontRenderer(font, true, true);
/* 100 */     fontSizes.put(Float.valueOf(size), renderer);
/* 101 */     return renderer;
/*     */   }
/*     */   
/*     */   public static void drawStringWithFade(MCFontRenderer font, String text, float x, float y, float maxWidth, int color) {
/* 105 */     if (text == null || text.isEmpty() || maxWidth <= 0.0F)
/* 106 */       return;  float currentX = x;
/* 107 */     float fadeZoneWidth = Math.min(22.0F, Math.max(8.0F, maxWidth * 0.35F));
/* 108 */     float fadeStartX = x + maxWidth - fadeZoneWidth;
/* 109 */     int originalAlpha = color >> 24 & 0xFF;
/*     */     
/* 111 */     for (int i = 0; i < text.length(); i++) {
/* 112 */       String ch = String.valueOf(text.charAt(i));
/* 113 */       float charWidth = font.getStringWidth(ch);
/* 114 */       if (currentX > x + maxWidth && i > 0)
/*     */         break; 
/* 116 */       int finalColor = color;
/* 117 */       if (currentX > fadeStartX) {
/* 118 */         float progress = (currentX - fadeStartX) / fadeZoneWidth;
/* 119 */         progress = Math.max(0.0F, Math.min(1.0F, progress));
/* 120 */         float fadeFactor = (float)Math.cos(progress * Math.PI / 2.0D);
/* 121 */         int newAlpha = (int)(originalAlpha * fadeFactor);
/* 122 */         finalColor = color & 0xFFFFFF | newAlpha << 24;
/*     */       } 
/*     */       
/* 125 */       if ((finalColor >> 24 & 0xFF) > 4) {
/* 126 */         font.drawString(ch, currentX, y, finalColor);
/*     */       }
/* 128 */       currentX += charWidth;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static MCFontRenderer getSystemFont(String fontName, float size) {
/* 133 */     String key = "system_" + fontName;
/* 134 */     regularFonts.computeIfAbsent(key, k -> new HashMap<>());
/*     */     
/* 136 */     Map<Float, MCFontRenderer> fontSizes = regularFonts.get(key);
/*     */     
/* 138 */     if (fontSizes.containsKey(Float.valueOf(size))) {
/* 139 */       return fontSizes.get(Float.valueOf(size));
/*     */     }
/*     */     
/* 142 */     Font font = new Font(fontName, 0, (int)size);
/* 143 */     MCFontRenderer renderer = new MCFontRenderer(font, true, true);
/* 144 */     fontSizes.put(Float.valueOf(size), renderer);
/* 145 */     return renderer;
/*     */   }
/*     */   
/*     */   public static MCFontRenderer getSystemFont(String fontName, float size, int style) {
/* 149 */     String key = "system_" + fontName + "_" + style;
/* 150 */     regularFonts.computeIfAbsent(key, k -> new HashMap<>());
/*     */     
/* 152 */     Map<Float, MCFontRenderer> fontSizes = regularFonts.get(key);
/*     */     
/* 154 */     if (fontSizes.containsKey(Float.valueOf(size))) {
/* 155 */       return fontSizes.get(Float.valueOf(size));
/*     */     }
/*     */     
/* 158 */     Font font = new Font(fontName, style, (int)size);
/* 159 */     MCFontRenderer renderer = new MCFontRenderer(font, true, true);
/* 160 */     fontSizes.put(Float.valueOf(size), renderer);
/* 161 */     return renderer;
/*     */   }
/*     */   
/*     */   public static void clearCache() {
/* 165 */     regularFonts.clear();
/* 166 */     gradientFonts.clear();
/* 167 */     initialized = false;
/*     */   }
/*     */   
/*     */   public static void clearCache(String fontName) {
/* 171 */     regularFonts.remove(fontName);
/* 172 */     gradientFonts.remove(fontName);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\render\fonts\ttf\Fonts.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */