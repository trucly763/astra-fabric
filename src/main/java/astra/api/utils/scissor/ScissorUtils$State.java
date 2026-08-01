/*    */ package shame.astra.api.utils.scissor;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class State
/*    */   implements Cloneable
/*    */ {
/*    */   public boolean enabled;
/*    */   public int transX;
/*    */   public int transY;
/*    */   public int x;
/*    */   public int y;
/*    */   public int width;
/*    */   public int height;
/*    */   
/*    */   public State clone() {
/*    */     try {
/* 24 */       return (State)super.clone();
/* 25 */     } catch (CloneNotSupportedException e) {
/* 26 */       throw new AssertionError(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\scissor\ScissorUtils$State.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */