/*    */ package shame.astra.api.storages.implement.helpertstorages;
/*    */ 
/*    */ 
/*    */ public class Theme implements QClient {
/*    */   private String name;
/*    */   public int[] color;
/*    */   
/*    */   @Generated
/*  9 */   public void setName(String name) { this.name = name; } @Generated public void setColor(int[] color) { this.color = color; }
/*    */   @Generated
/* 11 */   public String getName() { return this.name; } @Generated
/* 12 */   public int[] getColor() { return this.color; }
/*    */   
/*    */   public Theme(String name, int... color) {
/* 15 */     this.name = name;
/* 16 */     this.color = color;
/*    */   }
/*    */   
/*    */   public int getColor(int index) {
/* 20 */     if (this.name.equals("Rainbow")) {
/* 21 */       return ColorUtils.rainbow(10, index, 0.6F, 1.0F, 1.0F);
/*    */     }
/* 23 */     return ColorUtils.gradient(5, index, this.color);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\api\storages\implement\helpertstorages\Theme.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */