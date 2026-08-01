/*    */ package shame.astra.api.utils.network;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import lombok.Generated;
/*    */ import net.minecraft.class_2596;
/*    */ import shame.astra.api.QClient;
/*    */ 
/*    */ public final class NetworkUtils implements QClient {
/*    */   @Generated
/*    */   private NetworkUtils() {
/* 12 */     throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
/*    */   }
/* 14 */   private static final List<class_2596<?>> silentPackets = new ArrayList<>(); @Generated public static List<class_2596<?>> getSilentPackets() { return silentPackets; }
/*    */   
/*    */   public static void sendSilentPacket(class_2596<?> packet) {
/* 17 */     silentPackets.add(packet);
/* 18 */     mc.method_1562().method_52787(packet);
/*    */   }
/*    */   
/*    */   public static void sendPacket(class_2596<?> packet) {
/* 22 */     mc.method_1562().method_52787(packet);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\network\NetworkUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */