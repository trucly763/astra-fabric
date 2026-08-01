/*    */ package shame.astra.api.events.implement;
/*    */ 
/*    */ import lombok.Generated;
/*    */ import net.minecraft.class_332;
/*    */ import shame.astra.api.events.Event;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Default
/*    */   extends Event
/*    */ {
/*    */   private final class_332 context;
/*    */   private final float partialTicks;
/*    */   
/*    */   @Generated
/*    */   public Default(class_332 context, float partialTicks) {
/* 17 */     this.context = context; this.partialTicks = partialTicks;
/*    */   } @Generated
/* 19 */   public class_332 getContext() { return this.context; } @Generated
/* 20 */   public float getPartialTicks() { return this.partialTicks; }
/*    */ 
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\api\events\implement\EventRender$Default.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */