/*    */ package shame.astra.api.storages.implement;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import lombok.Generated;
/*    */ 
/*    */ 
/*    */ public class FriendStorage
/*    */ {
/* 10 */   private final List<String> friends = new ArrayList<>(); @Generated public List<String> getFriends() { return this.friends; }
/*    */   
/*    */   public void add(String friend) {
/* 13 */     if (!friend.isEmpty()) this.friends.add(friend); 
/*    */   }
/*    */   
/*    */   public void remove(String friend) {
/* 17 */     this.friends.remove(friend);
/*    */   }
/*    */   
/*    */   public void clear() {
/* 21 */     this.friends.clear();
/*    */   }
/*    */   
/*    */   public boolean isFriend(String friend) {
/* 25 */     return this.friends.contains(friend);
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 29 */     return this.friends.isEmpty();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\api\storages\implement\FriendStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */