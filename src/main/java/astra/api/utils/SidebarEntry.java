/*    */ package shame.astra.api.utils;
/*    */ 
/*    */ import net.minecraft.class_2561;
/*    */ 
/*    */ public class SidebarEntry
/*    */ {
/*    */   public final class_2561 name;
/*    */   public final class_2561 score;
/*    */   public final int scoreWidth;
/*    */   
/*    */   public SidebarEntry(class_2561 name, class_2561 score, int scoreWidth) {
/* 12 */     this.name = name;
/* 13 */     this.score = score;
/* 14 */     this.scoreWidth = scoreWidth;
/*    */   }
/*    */   
/*    */   public class_2561 name() {
/* 18 */     return this.name;
/*    */   }
/*    */   
/*    */   public class_2561 score() {
/* 22 */     return this.score;
/*    */   }
/*    */   
/*    */   public int scoreWidth() {
/* 26 */     return this.scoreWidth;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\SidebarEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */