/*    */ package shame.astra.api.events.implement;
/*    */ 
/*    */ import lombok.Generated;
/*    */ import net.minecraft.class_4184;
/*    */ import net.minecraft.class_4587;
/*    */ import org.joml.Matrix4f;
/*    */ import shame.astra.api.events.Event;
/*    */ 
/*    */ public class Event3DRender extends Event {
/*    */   private final class_4587 matrices;
/*    */   private final Matrix4f positionMatrix;
/*    */   private final Matrix4f projectionMatrix;
/*    */   private final class_4184 camera;
/*    */   private final float tickDelta;
/*    */   
/*    */   @Generated
/*    */   public class_4587 getMatrices() {
/* 18 */     return this.matrices;
/*    */   }
/*    */   
/*    */   @Generated
/*    */   public Matrix4f getPositionMatrix() {
/* 23 */     return this.positionMatrix;
/*    */   }
/*    */   
/*    */   @Generated
/*    */   public Matrix4f getProjectionMatrix() {
/* 28 */     return this.projectionMatrix;
/*    */   }
/*    */   
/*    */   @Generated
/*    */   public class_4184 getCamera() {
/* 33 */     return this.camera;
/*    */   }
/*    */   
/*    */   @Generated
/*    */   public float getTickDelta() {
/* 38 */     return this.tickDelta;
/*    */   }
/*    */   
/*    */   @Generated
/*    */   public Event3DRender(class_4587 matrices, Matrix4f positionMatrix, Matrix4f projectionMatrix, class_4184 camera, float tickDelta) {
/* 43 */     this.matrices = matrices;
/* 44 */     this.positionMatrix = positionMatrix;
/* 45 */     this.projectionMatrix = projectionMatrix;
/* 46 */     this.camera = camera;
/* 47 */     this.tickDelta = tickDelta;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\api\events\implement\Event3DRender.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */