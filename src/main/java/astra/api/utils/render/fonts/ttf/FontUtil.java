/*    */ package shame.astra.api.utils.render.fonts.ttf;
/*    */ import java.awt.Font;
/*    */ import java.io.InputStream;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.class_2960;
/*    */ import net.minecraft.class_310;
/*    */ import net.minecraft.class_3298;
/*    */ 
/*    */ public class FontUtil {
/*    */   public static Font getFontFromTTF(class_2960 loc, float fontSize, int fontType) {
/*    */     try {
/* 12 */       class_310 client = class_310.method_1551();
/* 13 */       if (client == null) return null; 
/* 14 */       if (client.method_1478() == null) return null;
/*    */       
/* 16 */       Optional<class_3298> resource = client.method_1478().method_14486(loc);
/* 17 */       if (resource.isPresent()) {
/* 18 */         InputStream inputStream = ((class_3298)resource.get()).method_14482();
/* 19 */         Font output = Font.createFont(fontType, inputStream);
/* 20 */         output = output.deriveFont(fontSize);
/* 21 */         inputStream.close();
/* 22 */         return output;
/*    */       } 
/* 24 */       return null;
/* 25 */     } catch (Exception e) {
/* 26 */       e.printStackTrace();
/* 27 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\render\fonts\ttf\FontUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */