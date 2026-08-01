/*    */ package shame.astra.client.modules.settings.implement;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import lombok.Generated;
/*    */ import shame.astra.client.modules.settings.Setting;
/*    */ 
/*    */ public class BooleanSetting extends Setting {
/*    */   @Generated
/*    */   public void setState(boolean state) {
/* 10 */     this.state = state;
/*    */   } private boolean state; @Generated
/*    */   public boolean isState() {
/* 13 */     return this.state;
/*    */   }
/*    */   public BooleanSetting(String name, boolean state) {
/* 16 */     super(name);
/* 17 */     this.state = state;
/*    */   }
/*    */   
/*    */   public static BooleanSetting of(String name, boolean state) {
/* 21 */     return new BooleanSetting(name, state);
/*    */   }
/*    */   
/*    */   public BooleanSetting visible(Supplier<Boolean> state) {
/* 25 */     this.visible = state;
/* 26 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\settings\implement\BooleanSetting.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */