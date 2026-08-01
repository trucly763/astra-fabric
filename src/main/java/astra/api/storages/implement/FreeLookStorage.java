/*    */ package shame.astra.api.storages.implement;
/*    */ import lombok.Generated;
/*    */ import net.minecraft.class_3532;
/*    */ import shame.astra.api.QClient;
/*    */ import shame.astra.api.events.EventLink;
/*    */ import shame.astra.api.events.implement.EventLook;
/*    */ import shame.astra.api.events.implement.EventRotation;
/*    */ 
/*    */ public class FreeLookStorage implements QClient {
/*    */   private static boolean active;
/*    */   private static float freeYaw;
/*    */   private static float freePitch;
/*    */   
/*    */   public FreeLookStorage() {
/* 15 */     EventInvoker.register(this);
/*    */   }
/*    */   @Generated
/* 18 */   public static void setActive(boolean active) { FreeLookStorage.active = active; } @Generated
/* 19 */   public static float getFreeYaw() { return freeYaw; } @Generated public static float getFreePitch() { return freePitch; } @Generated public static void setFreeYaw(float freeYaw) { FreeLookStorage.freeYaw = freeYaw; } @Generated public static void setFreePitch(float freePitch) { FreeLookStorage.freePitch = freePitch; }
/*    */    public static boolean isActive() {
/* 21 */     return active;
/*    */   }
/*    */   
/*    */   @EventLink
/*    */   public void onLook(EventLook event) {
/* 26 */     if (active) {
/* 27 */       rotateTowards(event.getYaw(), event.getPitch());
/* 28 */       event.cancel();
/*    */     } 
/*    */   }
/*    */   
/*    */   @EventLink
/*    */   public void onRotation(EventRotation event) {
/* 34 */     if (active) {
/* 35 */       event.setYaw(freeYaw);
/* 36 */       event.setPitch(freePitch);
/*    */     } else {
/* 38 */       freeYaw = event.getYaw();
/* 39 */       freePitch = event.getPitch();
/*    */     } 
/*    */   }
/*    */   
/*    */   private void rotateTowards(double targetYaw, double targetPitch) {
/* 44 */     freePitch = class_3532.method_15363((float)(freePitch + targetPitch * 0.15D), -90.0F, 90.0F);
/* 45 */     freeYaw = (float)(freeYaw + targetYaw * 0.15D);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\api\storages\implement\FreeLookStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */