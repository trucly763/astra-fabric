/*    */ package shame.astra.api.storages.implement;
/*    */ 
/*    */ import lombok.Generated;
/*    */ import net.minecraft.class_1297;
/*    */ import net.minecraft.class_1657;
/*    */ import net.minecraft.class_2596;
/*    */ import net.minecraft.class_2663;
/*    */ import net.minecraft.class_2828;
/*    */ import net.minecraft.class_2848;
/*    */ import net.minecraft.class_2868;
/*    */ import shame.astra.api.events.implement.EventPacket;
/*    */ 
/*    */ public class ServerStorage implements QClient {
/*    */   private int serverSlot;
/*    */   private float serverYaw;
/*    */   private float serverPitch;
/*    */   private float fallDistance;
/*    */   private double serverX;
/*    */   
/*    */   @Generated
/* 21 */   public int getServerSlot() { return this.serverSlot; } private double serverY; private double serverZ; private boolean serverOnGround; private boolean serverSprinting; private boolean serverSneaking; private boolean serverHorizontalCollision; @Generated
/* 22 */   public float getServerYaw() { return this.serverYaw; } @Generated public float getServerPitch() { return this.serverPitch; } @Generated public float getFallDistance() { return this.fallDistance; } @Generated
/* 23 */   public double getServerX() { return this.serverX; } @Generated public double getServerY() { return this.serverY; } @Generated public double getServerZ() { return this.serverZ; } @Generated
/* 24 */   public boolean isServerOnGround() { return this.serverOnGround; } @Generated public boolean isServerSprinting() { return this.serverSprinting; } @Generated public boolean isServerSneaking() { return this.serverSneaking; } @Generated public boolean isServerHorizontalCollision() { return this.serverHorizontalCollision; }
/*    */   
/*    */   public void ServerManager() {
/* 27 */     EventInvoker.register(this);
/*    */   }
/*    */   
/*    */   @EventLink
/*    */   public void onTick(EventTickPre e) {
/* 32 */     if (mc.field_1724 == null || mc.field_1687 == null)
/*    */       return; 
/* 34 */     double y = mc.field_1724.field_6036 - mc.field_1724.method_23318();
/* 35 */     if (mc.field_1724.method_24828()) { this.fallDistance = 0.0F; }
/* 36 */     else if (y > 0.0D) { this.fallDistance += (float)y; }
/*    */   
/*    */   }
/*    */   @EventLink
/*    */   public void onPacketSend(EventPacket e) {
/* 41 */     if (mc.field_1724 == null || mc.field_1687 == null)
/*    */       return; 
/* 43 */     class_2596 class_2596 = e.getPacket(); if (class_2596 instanceof class_2828) { class_2828 packet = (class_2828)class_2596;
/* 44 */       if (packet.method_36171()) {
/* 45 */         this.serverX = packet.method_12269(mc.field_1724.method_23317());
/* 46 */         this.serverY = packet.method_12268(mc.field_1724.method_23318());
/* 47 */         this.serverZ = packet.method_12274(mc.field_1724.method_23321());
/*    */       } 
/*    */       
/* 50 */       if (packet.method_36172()) {
/* 51 */         this.serverYaw = packet.method_12271(mc.field_1724.method_36454());
/* 52 */         this.serverPitch = packet.method_12270(mc.field_1724.method_36455());
/*    */       } 
/*    */       
/* 55 */       this.serverOnGround = packet.method_12273();
/* 56 */       this.serverHorizontalCollision = packet.method_61225(); }
/*    */ 
/*    */     
/* 59 */     class_2596 = e.getPacket(); if (class_2596 instanceof class_2868) { class_2868 packet = (class_2868)class_2596; this.serverSlot = packet.method_12442(); }
/*    */     
/* 61 */     class_2596 = e.getPacket(); if (class_2596 instanceof class_2848) { class_2848 packet = (class_2848)class_2596;
/* 62 */       switch (packet.method_12365()) {
/*    */         case field_12981:
/* 64 */           e.setCancelled(this.serverSprinting);
/* 65 */           if (!e.isCancelled()) {
/* 66 */             this.serverSprinting = true;
/*    */           }
/*    */           break;
/*    */         case field_12985:
/* 70 */           e.setCancelled(!this.serverSprinting);
/* 71 */           if (!e.isCancelled())
/* 72 */             this.serverSprinting = false; 
/*    */           break;
/*    */         case field_12979:
/* 75 */           this.serverSneaking = true; break;
/* 76 */         case field_12984: this.serverSneaking = false;
/*    */           break;
/*    */       }  }
/*    */   
/*    */   }
/*    */   @EventLink
/*    */   public void onPacketReceive(EventPacket e) throws InvocationTargetException, IllegalAccessException, InstantiationException {
/* 83 */     if (mc.field_1724 == null || mc.field_1687 == null)
/*    */       return; 
/* 85 */     class_2596 class_2596 = e.getPacket(); if (class_2596 instanceof class_2663) { class_2663 packet = (class_2663)class_2596; if (packet.method_11470() == 35) {
/* 86 */         class_1657 player; class_1297 class_1297 = packet.method_11469((class_1937)mc.field_1687); if (class_1297 instanceof class_1657) { player = (class_1657)class_1297; } else { return; }
/* 87 */          EventInvoker.invoke((Event)new EventPopTotem(player));
/*    */       }  }
/*    */   
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\api\storages\implement\ServerStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */