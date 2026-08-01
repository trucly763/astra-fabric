/*    */ package shame.astra.client.modules.settings.implement;
/*    */ 
/*    */ import java.util.List;
/*    */ import lombok.Generated;
/*    */ import shame.astra.astra;
/*    */ 
/*    */ public class ModeSetting extends Setting {
/*    */   private List<String> mods;
/*    */   private String current;
/*    */   private int index;
/*    */   
/*    */   @Generated
/* 13 */   public void setMods(List<String> mods) { this.mods = mods; } @Generated public void setCurrent(String current) { this.current = current; } @Generated public void setIndex(int index) { this.index = index; }
/*    */   
/*    */   @Generated
/* 16 */   public List<String> getMods() { return this.mods; }
/*    */   @Generated
/* 18 */   public String getCurrent() { return this.current; } @Generated
/*    */   public int getIndex() {
/* 20 */     return this.index;
/*    */   }
/*    */   public ModeSetting(String name, String current, String... modes) {
/* 23 */     super(name);
/* 24 */     this.mods = Arrays.asList(modes);
/* 25 */     this.index = this.mods.indexOf(current);
/* 26 */     if (this.index < 0) {
/* 27 */       this.index = 0;
/*    */     }
/* 29 */     this.current = this.mods.get(this.index);
/*    */   }
/*    */   
/*    */   public void set(String selected) {
/* 33 */     int newIndex = this.mods.indexOf(selected);
/* 34 */     if (newIndex < 0) {
/*    */       return;
/*    */     }
/* 37 */     this.current = selected;
/* 38 */     this.index = newIndex;
/*    */   }
/*    */   
/*    */   public boolean is(String mode) {
/* 42 */     return this.current.equals(mode);
/*    */   }
/*    */   
/*    */   public String displayMode(String mode) {
/* 46 */     return (astra.INSTANCE.localizationStorage == null) ? mode : astra.INSTANCE.localizationStorage.translate(mode);
/*    */   }
/*    */   
/*    */   public String displayCurrent() {
/* 50 */     return displayMode(this.current);
/*    */   }
/*    */   
/*    */   public ModeSetting visible(Supplier<Boolean> state) {
/* 54 */     this.visible = state;
/* 55 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\settings\implement\ModeSetting.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */