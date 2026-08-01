/*    */ package shame.astra.api.utils.chat;
/*    */ import java.awt.Color;
/*    */ import net.minecraft.class_2561;
/*    */ import net.minecraft.class_2583;
/*    */ import net.minecraft.class_310;
/*    */ import net.minecraft.class_5250;
/*    */ import net.minecraft.class_5251;
/*    */ import shame.astra.api.utils.color.ColorUtils;
/*    */ 
/*    */ public final class ChatUtils {
/*    */   @Generated
/*    */   private ChatUtils() {
/* 13 */     throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
/*    */   }
/*    */   public static void sendMessage(Object message) {
/* 16 */     class_310 mc = class_310.method_1551();
/*    */     
/* 18 */     if (mc.field_1724 == null) {
/* 19 */       System.out.println("[astra] " + String.valueOf(message));
/*    */       
/*    */       return;
/*    */     } 
/* 23 */     class_5250 text = class_2561.method_43470("");
/* 24 */     String prefix = "astra";
/* 25 */     for (int i = 0; i < prefix.length(); i++) {
/* 26 */       text.method_10852((class_2561)class_2561.method_43470(String.valueOf(prefix.charAt(i)))
/* 27 */           .method_10862(class_2583.field_24360
/* 28 */             .method_10982(Boolean.valueOf(true))
/* 29 */             .method_27703(class_5251.method_27717(ColorUtils.gradient(ColorUtils.getThemeColor(0), ColorUtils.getThemeColor(90), i / prefix.length())))));
/*    */     }
/*    */ 
/*    */     
/* 33 */     text.method_10852((class_2561)class_2561.method_43470(" ⇨ ")
/* 34 */         .method_10862(class_2583.field_24360
/* 35 */           .method_10982(Boolean.valueOf(false))
/* 36 */           .method_27703(class_5251.method_27717((new Color(200, 200, 200)).getRGB()))));
/*    */ 
/*    */     
/* 39 */     text.method_10852((class_2561)class_2561.method_43470(String.valueOf(message))
/* 40 */         .method_10862(class_2583.field_24360
/* 41 */           .method_10982(Boolean.valueOf(false))
/* 42 */           .method_27703(class_5251.method_27717((new Color(200, 200, 200)).getRGB()))));
/*    */ 
/*    */     
/* 45 */     mc.field_1724.method_7353((class_2561)text, false);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\chat\ChatUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */