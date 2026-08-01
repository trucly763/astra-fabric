/*    */ package shame.astra.api.utils.render.fonts.msdf;
/*    */ 
/*    */ import net.minecraft.class_4588;
/*    */ import org.joml.Matrix4f;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class MsdfGlyph
/*    */ {
/*    */   private final int code;
/*    */   private final float minU;
/*    */   private final float maxU;
/*    */   private final float minV;
/*    */   private final float maxV;
/*    */   private final float advance;
/*    */   private final float topPosition;
/*    */   private final float width;
/*    */   private final float height;
/*    */   
/*    */   public MsdfGlyph(int unicode, float advance, float planeLeft, float planeTop, float planeRight, float planeBottom, float atlasLeft, float atlasTop, float atlasRight, float atlasBottom, float atlasWidth, float atlasHeight) {
/* 22 */     this.code = unicode;
/* 23 */     this.advance = advance;
/*    */     
/* 25 */     if (atlasLeft != 0.0F || atlasRight != 0.0F || atlasTop != 0.0F || atlasBottom != 0.0F) {
/* 26 */       this.minU = atlasLeft / atlasWidth;
/* 27 */       this.maxU = atlasRight / atlasWidth;
/* 28 */       this.minV = 1.0F - atlasTop / atlasHeight;
/* 29 */       this.maxV = 1.0F - atlasBottom / atlasHeight;
/*    */     } else {
/* 31 */       this.minU = 0.0F;
/* 32 */       this.maxU = 0.0F;
/* 33 */       this.minV = 0.0F;
/* 34 */       this.maxV = 0.0F;
/*    */     } 
/*    */     
/* 37 */     if (planeLeft != 0.0F || planeRight != 0.0F || planeTop != 0.0F || planeBottom != 0.0F) {
/* 38 */       this.width = planeRight - planeLeft;
/* 39 */       this.height = planeTop - planeBottom;
/* 40 */       this.topPosition = planeTop;
/*    */     } else {
/* 42 */       this.width = 0.0F;
/* 43 */       this.height = 0.0F;
/* 44 */       this.topPosition = 0.0F;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public float apply(Matrix4f matrix, class_4588 consumer, float size, float x, float y, float z, int red, int green, int blue, int alpha) {
/* 51 */     y -= this.topPosition * size;
/* 52 */     y--;
/*    */     
/* 54 */     float w = this.width * size;
/* 55 */     float h = this.height * size;
/*    */     
/* 57 */     consumer.method_22918(matrix, x, y, z)
/* 58 */       .method_1336(red, green, blue, alpha)
/* 59 */       .method_22913(this.minU, this.minV);
/*    */     
/* 61 */     consumer.method_22918(matrix, x, y + h, z)
/* 62 */       .method_1336(red, green, blue, alpha)
/* 63 */       .method_22913(this.minU, this.maxV);
/*    */     
/* 65 */     consumer.method_22918(matrix, x + w, y + h, z)
/* 66 */       .method_1336(red, green, blue, alpha)
/* 67 */       .method_22913(this.maxU, this.maxV);
/*    */     
/* 69 */     consumer.method_22918(matrix, x + w, y, z)
/* 70 */       .method_1336(red, green, blue, alpha)
/* 71 */       .method_22913(this.maxU, this.minV);
/*    */     
/* 73 */     return this.width * (size - 1.0F) + (Character.isSpaceChar(this.code) ? (this.advance * size) : 0.0F);
/*    */   }
/*    */   
/*    */   public float getWidth(float size) {
/* 77 */     return this.width * (size - 1.0F) + (Character.isSpaceChar(this.code) ? (this.advance * size) : 0.0F);
/*    */   }
/*    */   
/*    */   public int getCharCode() {
/* 81 */     return this.code;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\render\fonts\msdf\MsdfGlyph.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */