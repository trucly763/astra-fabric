/*    */ package shame.astra.api.events.implement;
/*    */ 
/*    */ import lombok.Generated;
/*    */ import net.minecraft.class_1041;
/*    */ import net.minecraft.class_332;
/*    */ import net.minecraft.class_4184;
/*    */ import net.minecraft.class_4587;
/*    */ import net.minecraft.class_761;
/*    */ import org.joml.Matrix4f;
/*    */ import shame.astra.api.events.Event;
/*    */ 
/*    */ public class EventRender extends Event {
/*    */   public static class Default extends Event { private final class_332 context;
/*    */     private final float partialTicks;
/*    */     
/*    */     @Generated
/* 17 */     public Default(class_332 context, float partialTicks) { this.context = context; this.partialTicks = partialTicks; }
/*    */     @Generated
/* 19 */     public class_332 getContext() { return this.context; } @Generated
/* 20 */     public float getPartialTicks() { return this.partialTicks; }
/*    */      }
/*    */   public static class World extends Event { private final class_1041 scaledResolution; private final float partialTicks; private final Matrix4f matrix; private final class_4587 matrixStack;
/*    */     
/*    */     @Generated
/* 25 */     public World(class_1041 scaledResolution, float partialTicks, Matrix4f matrix, class_4587 matrixStack) { this.scaledResolution = scaledResolution; this.partialTicks = partialTicks; this.matrix = matrix; this.matrixStack = matrixStack; }
/*    */     @Generated
/* 27 */     public class_1041 getScaledResolution() { return this.scaledResolution; } @Generated
/* 28 */     public float getPartialTicks() { return this.partialTicks; } @Generated
/* 29 */     public Matrix4f getMatrix() { return this.matrix; } @Generated
/* 30 */     public class_4587 getMatrixStack() { return this.matrixStack; }
/*    */      }
/*    */   
/*    */   public static class Game extends Event { private final class_761 context; private final class_4587 matrix; private final Matrix4f projectionMatrix;
/*    */     @Generated
/* 35 */     public Game(class_761 context, class_4587 matrix, Matrix4f projectionMatrix, class_4184 camera, float partialTicks, long finishTimeNano) { this.context = context; this.matrix = matrix; this.projectionMatrix = projectionMatrix; this.camera = camera; this.partialTicks = partialTicks; this.finishTimeNano = finishTimeNano; } private final class_4184 camera; private final float partialTicks; private final long finishTimeNano; @Generated
/*    */     public class_761 getContext() {
/* 37 */       return this.context; } @Generated
/* 38 */     public class_4587 getMatrix() { return this.matrix; } @Generated
/* 39 */     public Matrix4f getProjectionMatrix() { return this.projectionMatrix; } @Generated
/* 40 */     public class_4184 getCamera() { return this.camera; } @Generated
/* 41 */     public float getPartialTicks() { return this.partialTicks; } @Generated
/* 42 */     public long getFinishTimeNano() { return this.finishTimeNano; }
/*    */      }
/*    */ 
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\api\events\implement\EventRender.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */