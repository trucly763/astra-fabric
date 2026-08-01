/*    */ package shame.astra.api.utils.client;
/*    */ 
/*    */ public class UserInfo {
/*    */   private final String username;
/*    */   private final int uid;
/*    */   private final String role;
/*    */   private final String hwid;
/*    */   private final String expireDate;
/*    */   
/*    */   public UserInfo(String username, int uid, String role, String hwid, String expireDate) {
/* 11 */     this.username = username;
/* 12 */     this.uid = uid;
/* 13 */     this.role = role;
/* 14 */     this.hwid = hwid;
/* 15 */     this.expireDate = expireDate;
/*    */   }
/*    */   
/*    */   public static UserInfo empty() {
/* 19 */     return new UserInfo("Unknown", 0, "Unknown", "", "");
/*    */   }
/*    */   
/*    */   public String getUsername() {
/* 23 */     return this.username;
/*    */   }
/*    */   
/*    */   public int getUid() {
/* 27 */     return this.uid;
/*    */   }
/*    */   
/*    */   public String getRole() {
/* 31 */     return this.role;
/*    */   }
/*    */   
/*    */   public String getHwid() {
/* 35 */     return this.hwid;
/*    */   }
/*    */   
/*    */   public String getExpireDate() {
/* 39 */     return this.expireDate;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\client\UserInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */