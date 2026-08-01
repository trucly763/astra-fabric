/*    */ package shame.astra.client.modules.impl.render.base;
/*    */ import lombok.Generated;
/*    */ import shame.astra.api.QClient;
/*    */ import shame.astra.api.events.implement.EventRender;
/*    */ import shame.astra.api.events.implement.EventUpdate;
/*    */ import shame.astra.api.utils.draggable.Draggable;
/*    */ 
/*    */ public class InterfaceProcessing implements QClient {
/*    */   public final Draggable draggable;
/*    */   
/*    */   @Generated
/*    */   public InterfaceProcessing(Draggable draggable) {
/* 13 */     this.unusualRectType = true;
/*    */     this.draggable = draggable;
/*    */   } private boolean unusualRectType; public boolean isUnusualRectType() {
/* 16 */     return this.unusualRectType;
/*    */   }
/*    */   
/*    */   public void setUnusualRectType(boolean unusualRectType) {
/* 20 */     this.unusualRectType = unusualRectType;
/*    */   }
/*    */   
/*    */   public void onUpdate(EventUpdate eventUpdate) {}
/*    */   
/*    */   public void onRender(EventRender.Default eventRender) {}
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\base\InterfaceProcessing.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */