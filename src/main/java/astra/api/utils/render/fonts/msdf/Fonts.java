/*    */ package shame.astra.api.utils.render.fonts.msdf;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import net.minecraft.class_4587;
/*    */ 
/*    */ public class Fonts
/*    */ {
/*  8 */   private static final HashMap<String, MsdfFont> loadedFonts = new HashMap<>();
/*  9 */   private static final HashMap<String, Font[]> fontCache = (HashMap)new HashMap<>();
/*    */   private static boolean initialized = false;
/*    */   
/*    */   public static void init() {
/* 13 */     if (initialized)
/* 14 */       return;  initialized = true;
/*    */     
/* 16 */     loadFont("sf_regular");
/* 17 */     loadFont("wave");
/* 18 */     loadFont("icon");
/* 19 */     loadFont("icon1");
/* 20 */     loadFont("iconnew");
/* 21 */     loadFont("suisse");
/*    */   }
/*    */   
/*    */   private static void loadFont(String name) {
/*    */     try {
/* 26 */       MsdfFont msdfFont = MsdfFont.builder().atlas(name).data(name).build();
/* 27 */       loadedFonts.put(name, msdfFont);
/*    */       
/* 29 */       Font[] fonts = new Font[100];
/* 30 */       for (int i = 8; i < 100; i++) {
/* 31 */         fonts[i] = new Font(msdfFont, i);
/*    */       }
/* 33 */       fontCache.put(name, fonts);
/* 34 */     } catch (Exception e) {
/* 35 */       System.err.println("[Fonts] Failed to load " + name + ": " + e.getMessage());
/*    */     } 
/*    */   }
/*    */   
/*    */   public static Font getFont(String name, int size) {
/* 40 */     if (!initialized) init();
/*    */     
/* 42 */     String cleanName = name.replace(".ttf", "");
/*    */     
/* 44 */     if (size < 8) size = 8; 
/* 45 */     if (size >= 100) size = 99;
/*    */     
/* 47 */     Font[] fonts = fontCache.get(cleanName);
/* 48 */     if (fonts != null && fonts[size] != null) {
/* 49 */       return fonts[size];
/*    */     }
/*    */     
/* 52 */     if (!loadedFonts.containsKey(cleanName)) {
/* 53 */       loadFont(cleanName);
/*    */     }
/*    */     
/* 56 */     fonts = fontCache.get(cleanName);
/* 57 */     if (fonts != null && fonts[size] != null) {
/* 58 */       return fonts[size];
/*    */     }
/*    */     
/* 61 */     return null;
/*    */   }
/*    */   
/*    */   public static void drawStringWithFade(Font font, String text, float x, float y, float maxWidth, int color) {
/* 65 */     if (font == null)
/* 66 */       return;  class_4587 stack = new class_4587();
/* 67 */     font.drawStringWithFade(stack, text, x, y, maxWidth, color);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\render\fonts\msdf\Fonts.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */