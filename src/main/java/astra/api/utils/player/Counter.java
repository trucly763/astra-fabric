/*    */ package shame.astra.api.utils.player;
/*    */ 
/*    */ import lombok.Generated;
/*    */ import net.minecraft.class_3532;
/*    */ import shame.astra.api.QClient;
/*    */ 
/*    */ public final class Counter implements QClient {
/*    */   @Generated
/*  9 */   private Counter() { throw new UnsupportedOperationException("This is a utility class and cannot be instantiated"); } private static int currentFPS; @Generated
/*    */   public static int getCurrentFPS() {
/* 11 */     return currentFPS;
/*    */   }
/*    */   public static void updateFPS() {
/* 14 */     int prevFPS = mc.method_47599();
/* 15 */     currentFPS = class_3532.method_48781(0.5F, prevFPS, currentFPS);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\player\Counter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */