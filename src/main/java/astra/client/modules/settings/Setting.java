/*    */ package shame.astra.client.modules.settings;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.util.function.Supplier;
/*    */ import lombok.Generated;
/*    */ import shame.astra.api.QClient;
/*    */ import shame.astra.astra;
/*    */ 
/*    */ public abstract class Setting
/*    */   implements QClient {
/*    */   private final String name;
/*    */   
/*    */   @Generated
/*    */   public String name() {
/* 15 */     return this.name;
/*    */   }
/* 17 */   public Supplier<Boolean> visible = () -> Boolean.valueOf(true); public Color color = Color.WHITE; @Generated public Color color() { return this.color; }
/*    */   
/*    */   public Setting(String name) {
/* 20 */     this.name = name;
/*    */   }
/*    */   
/*    */   public Boolean visible() {
/* 24 */     return this.visible.get();
/*    */   }
/*    */   
/*    */   public String displayName() {
/* 28 */     return (astra.INSTANCE.localizationStorage == null) ? this.name : astra.INSTANCE.localizationStorage.translate(this.name);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\settings\Setting.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */