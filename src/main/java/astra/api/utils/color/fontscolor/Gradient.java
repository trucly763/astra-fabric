/*    */ package shame.astra.api.utils.color.fontscolor;
/*    */ 
/*    */ import java.util.List;
/*    */ import lombok.Generated;
/*    */ 
/*    */ public class Gradient {
/*    */   protected final ColorRGBA topLeftColor;
/*    */   protected final ColorRGBA bottomLeftColor;
/*    */   protected final ColorRGBA topRightColor;
/*    */   protected final ColorRGBA bottomRightColor;
/*    */   
/*    */   protected Gradient(ColorRGBA topLeftColor, ColorRGBA bottomLeftColor, ColorRGBA topRightColor, ColorRGBA bottomRightColor) {
/* 13 */     this.topLeftColor = topLeftColor;
/* 14 */     this.bottomLeftColor = bottomLeftColor;
/* 15 */     this.topRightColor = topRightColor;
/* 16 */     this.bottomRightColor = bottomRightColor;
/*    */   }
/*    */   
/*    */   public static Gradient of(ColorRGBA topLeftColor, ColorRGBA bottomLeftColor, ColorRGBA topRightColor, ColorRGBA bottomRightColor) {
/* 20 */     return new Gradient(topLeftColor, bottomLeftColor, topRightColor, bottomRightColor);
/*    */   }
/*    */   
/*    */   public static Gradient of(List<ColorRGBA> colors) {
/* 24 */     return new Gradient(colors.get(0), colors.get(1), colors.get(2), colors.get(3));
/*    */   }
/*    */   
/*    */   public Gradient rotate() {
/* 28 */     return this;
/*    */   }
/*    */   
/*    */   public Gradient mulAlpha(float alphaMultiplier) {
/* 32 */     return new Gradient(this.topLeftColor.mulAlpha(alphaMultiplier), this.bottomLeftColor.mulAlpha(alphaMultiplier), this.topRightColor.mulAlpha(alphaMultiplier), this.bottomRightColor.mulAlpha(alphaMultiplier));
/*    */   }
/*    */   
/*    */   @Generated
/*    */   public ColorRGBA getTopLeftColor() {
/* 37 */     return this.topLeftColor;
/*    */   }
/*    */   
/*    */   @Generated
/*    */   public ColorRGBA getBottomLeftColor() {
/* 42 */     return this.bottomLeftColor;
/*    */   }
/*    */   
/*    */   @Generated
/*    */   public ColorRGBA getTopRightColor() {
/* 47 */     return this.topRightColor;
/*    */   }
/*    */   
/*    */   @Generated
/*    */   public ColorRGBA getBottomRightColor() {
/* 52 */     return this.bottomRightColor;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\color\fontscolor\Gradient.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */