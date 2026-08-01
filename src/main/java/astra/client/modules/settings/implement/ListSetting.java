/*    */ package shame.astra.client.modules.settings.implement;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.Supplier;
/*    */ import lombok.Generated;
/*    */ import shame.astra.client.modules.settings.Setting;
/*    */ 
/*    */ public class ListSetting extends Setting {
/*    */   @Generated
/*    */   public void setSettings(List<BooleanSetting> settings) {
/* 11 */     this.settings = settings;
/*    */   } public List<BooleanSetting> settings; @Generated
/*    */   public List<BooleanSetting> getSettings() {
/* 14 */     return this.settings;
/*    */   }
/*    */   public ListSetting(String name, BooleanSetting... settings) {
/* 17 */     super(name);
/* 18 */     this.settings = List.of(settings);
/*    */   }
/*    */   
/*    */   public ListSetting of(String name, BooleanSetting... settings) {
/* 22 */     return new ListSetting(name, settings);
/*    */   }
/*    */   
/*    */   public boolean is(String name) {
/* 26 */     return requireSetting(name).isState();
/*    */   }
/*    */   
/*    */   public void set(String name, boolean value) {
/* 30 */     requireSetting(name).setState(value);
/*    */   }
/*    */   
/*    */   public ListSetting visible(Supplier<Boolean> state) {
/* 34 */     this.visible = state;
/* 35 */     return this;
/*    */   }
/*    */   
/*    */   private BooleanSetting requireSetting(String name) {
/* 39 */     for (BooleanSetting option : this.settings) {
/* 40 */       if (option.name().equalsIgnoreCase(name)) {
/* 41 */         return option;
/*    */       }
/*    */     } 
/* 44 */     throw new NullPointerException("Unknown list setting entry: " + name);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\settings\implement\ListSetting.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */