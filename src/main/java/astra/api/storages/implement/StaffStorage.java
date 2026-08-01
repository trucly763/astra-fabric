/*    */ package shame.astra.api.storages.implement;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import lombok.Generated;
/*    */ 
/*    */ 
/*    */ public class StaffStorage
/*    */ {
/* 10 */   private final List<String> staffs = new ArrayList<>(); @Generated public List<String> getStaffs() { return this.staffs; }
/*    */   
/*    */   public void add(String friend) {
/* 13 */     if (!friend.isEmpty()) this.staffs.add(friend); 
/*    */   }
/*    */   
/*    */   public void remove(String friend) {
/* 17 */     this.staffs.remove(friend);
/*    */   }
/*    */   
/*    */   public void clear() {
/* 21 */     this.staffs.clear();
/*    */   }
/*    */   
/*    */   public boolean isStaff(String friend) {
/* 25 */     return this.staffs.contains(friend);
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 29 */     return this.staffs.isEmpty();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\api\storages\implement\StaffStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */