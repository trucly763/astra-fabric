/*    */ package shame.astra.client.modules.settings.implement;
/*    */ import java.util.function.Supplier;
/*    */ import lombok.Generated;
/*    */ 
/*    */ public class FloatSetting extends Setting {
/*    */   private float value;
/*    */   private final float min;
/*    */   
/*    */   @Generated
/*    */   public void setActive(boolean active) {
/* 11 */     this.active = active;
/*    */   } private final float max; private final float increment; private boolean active;
/*    */   @Generated
/*    */   public float getMin() {
/* 15 */     return this.min; } @Generated
/* 16 */   public float getMax() { return this.max; } @Generated
/* 17 */   public float getIncrement() { return this.increment; } @Generated
/* 18 */   public boolean isActive() { return this.active; }
/*    */   
/*    */   public FloatSetting(String name, float value, float min, float max, float increment) {
/* 21 */     super(name);
/* 22 */     this.value = value;
/* 23 */     this.min = min;
/* 24 */     this.max = max;
/* 25 */     this.increment = increment;
/*    */   }
/*    */   
/*    */   public Number getValue() {
/* 29 */     return Float.valueOf(class_3532.method_15363(this.value, getMin(), getMax()));
/*    */   }
/*    */   
/*    */   public void setValue(float value) {
/* 33 */     this.value = class_3532.method_15363(value, getMin(), getMax());
/*    */   }
/*    */   
/*    */   public float get() {
/* 37 */     return getValue().floatValue();
/*    */   }
/*    */   
/*    */   public FloatSetting visible(Supplier<Boolean> state) {
/* 41 */     this.visible = state;
/* 42 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\settings\implement\FloatSetting.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */