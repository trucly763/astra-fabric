/*    */ package shame.astra.client.modules.impl.misc;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.class_124;
/*    */ import net.minecraft.class_1657;
/*    */ import net.minecraft.class_2338;
/*    */ import shame.astra.api.events.EventLink;
/*    */ import shame.astra.api.events.implement.EventUpdate;
/*    */ import shame.astra.api.utils.chat.ChatUtils;
/*    */ import shame.astra.client.modules.Module;
/*    */ 
/*    */ public class LeaveTracker extends Module {
/* 16 */   public static LeaveTracker INSTANCE = new LeaveTracker();
/*    */   
/* 18 */   private final Map<UUID, TrackedPlayer> trackedPlayers = new HashMap<>();
/*    */   private class_638 lastWorld;
/*    */   private boolean initialized;
/*    */   
/*    */   public LeaveTracker() {
/* 23 */     super("LeaveTracker", "Пишет координаты ливнутых игроков из прогрузки", Module.ModuleCategory.MISC);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 28 */     this.trackedPlayers.clear();
/* 29 */     this.initialized = false;
/* 30 */     super.onDisable();
/*    */   }
/*    */   
/*    */   @EventLink
/*    */   public void onUpdate(EventUpdate event) {
/* 35 */     if (mc.field_1724 == null || mc.field_1687 == null)
/*    */       return; 
/* 37 */     if (mc.field_1687 != this.lastWorld) {
/* 38 */       this.lastWorld = mc.field_1687;
/* 39 */       this.trackedPlayers.clear();
/* 40 */       this.initialized = false;
/*    */     } 
/*    */     
/* 43 */     if (!this.initialized) {
/* 44 */       snapshotPlayers();
/* 45 */       this.initialized = true;
/*    */       
/*    */       return;
/*    */     } 
/* 49 */     Set<UUID> seenPlayers = new HashSet<>();
/*    */     
/* 51 */     for (class_1657 player : mc.field_1687.method_18456()) {
/* 52 */       if (player == mc.field_1724 || !player.method_5805())
/*    */         continue; 
/* 54 */       UUID uuid = player.method_5667();
/* 55 */       seenPlayers.add(uuid);
/* 56 */       this.trackedPlayers.put(uuid, new TrackedPlayer(player.method_5477().getString(), player.method_24515()));
/*    */     } 
/*    */     
/* 59 */     Iterator<Map.Entry<UUID, TrackedPlayer>> iterator = this.trackedPlayers.entrySet().iterator();
/* 60 */     while (iterator.hasNext()) {
/* 61 */       Map.Entry<UUID, TrackedPlayer> entry = iterator.next();
/*    */       
/* 63 */       if (seenPlayers.contains(entry.getKey()))
/*    */         continue; 
/* 65 */       TrackedPlayer tracked = entry.getValue();
/* 66 */       double distSq = mc.field_1724.method_5649(tracked.pos
/* 67 */           .method_10263(), tracked.pos
/* 68 */           .method_10264(), tracked.pos
/* 69 */           .method_10260());
/*    */ 
/*    */       
/* 72 */       if (distSq < 65536.0D) {
/* 73 */         ChatUtils.sendMessage(String.valueOf(class_124.field_1080) + String.valueOf(class_124.field_1080) + tracked.name + " ливнул на " + String.valueOf(class_124.field_1068) + String.valueOf(class_124.field_1080) + " " + tracked.pos
/*    */ 
/*    */             
/* 76 */             .method_10263() + " " + tracked.pos
/* 77 */             .method_10264());
/*    */       }
/*    */ 
/*    */       
/* 81 */       iterator.remove();
/*    */     } 
/*    */   }
/*    */   
/*    */   private void snapshotPlayers() {
/* 86 */     this.trackedPlayers.clear();
/* 87 */     for (class_1657 player : mc.field_1687.method_18456()) {
/* 88 */       if (player == mc.field_1724 || !player.method_5805())
/* 89 */         continue;  this.trackedPlayers.put(player
/* 90 */           .method_5667(), new TrackedPlayer(player
/* 91 */             .method_5477().getString(), player.method_24515()));
/*    */     } 
/*    */   }
/*    */   private static final class TrackedPlayer extends Record { private final String name; private final class_2338 pos;
/*    */     
/* 96 */     private TrackedPlayer(String name, class_2338 pos) { this.name = name; this.pos = pos; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lshame/astra/client/modules/impl/misc/LeaveTracker$TrackedPlayer;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #96	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 96 */       //   0	7	0	this	Lshame/astra/client/modules/impl/misc/LeaveTracker$TrackedPlayer; } public String name() { return this.name; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lshame/astra/client/modules/impl/misc/LeaveTracker$TrackedPlayer;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #96	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lshame/astra/client/modules/impl/misc/LeaveTracker$TrackedPlayer; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lshame/astra/client/modules/impl/misc/LeaveTracker$TrackedPlayer;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #96	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lshame/astra/client/modules/impl/misc/LeaveTracker$TrackedPlayer;
/* 96 */       //   0	8	1	o	Ljava/lang/Object; } public class_2338 pos() { return this.pos; }
/*    */      }
/*    */ 
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\misc\LeaveTracker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */