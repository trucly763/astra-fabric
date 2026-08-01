/*    */ package shame.astra.client.modules.settings.implement;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import lombok.Generated;
/*    */ import shame.astra.client.modules.settings.Setting;
/*    */ 
/*    */ public class TextSetting extends Setting {
/*    */   private String text;
/*    */   private final int maxLength;
/*    */   
/*    */   @Generated
/*    */   public String getText() {
/* 13 */     return this.text; } @Generated
/* 14 */   public int getMaxLength() { return this.maxLength; }
/*    */   
/*    */   public TextSetting(String name, String text) {
/* 17 */     this(name, text, 32);
/*    */   }
/*    */   
/*    */   public TextSetting(String name, String text, int maxLength) {
/* 21 */     super(name);
/* 22 */     this.maxLength = Math.max(1, maxLength);
/* 23 */     setText(text);
/*    */   }
/*    */   
/*    */   public void setText(String text) {
/* 27 */     if (text == null) {
/* 28 */       this.text = "";
/*    */       
/*    */       return;
/*    */     } 
/* 32 */     StringBuilder builder = new StringBuilder();
/* 33 */     for (int i = 0; i < text.length() && builder.length() < this.maxLength; i++) {
/* 34 */       char chr = text.charAt(i);
/* 35 */       if (!Character.isISOControl(chr)) {
/* 36 */         builder.append(chr);
/*    */       }
/*    */     } 
/* 39 */     this.text = builder.toString();
/*    */   }
/*    */   
/*    */   public String get() {
/* 43 */     return this.text;
/*    */   }
/*    */   
/*    */   public TextSetting visible(Supplier<Boolean> state) {
/* 47 */     this.visible = state;
/* 48 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\settings\implement\TextSetting.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */