/*    */ package shame.astra.client.modules.settings.implement;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import lombok.Generated;
/*    */ import shame.astra.client.modules.settings.Setting;
/*    */ 
/*    */ public class BindSetting extends Setting {
/*    */   @Generated
/*    */   public void setKey(int key) {
/* 10 */     this.key = key;
/*    */   } private int key; @Generated
/*    */   public int getKey() {
/* 13 */     return this.key;
/*    */   }
/*    */   public BindSetting(String name, int keyDefault) {
/* 16 */     super(name);
/* 17 */     this.key = keyDefault;
/*    */   }
/*    */   
/*    */   public BindSetting visible(Supplier<Boolean> state) {
/* 21 */     this.visible = state;
/* 22 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\settings\implement\BindSetting.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */