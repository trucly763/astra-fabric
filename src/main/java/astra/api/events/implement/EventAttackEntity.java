/*    */ package shame.astra.api.events.implement;
/*    */ 
/*    */ import net.minecraft.class_1657;
/*    */ 
/*    */ public class EventAttackEntity extends Event {
/*    */   private final class_1657 player;
/*    */   
/*    */   @Generated
/*  9 */   public EventAttackEntity(class_1657 player, class_1297 target) { this.player = player; this.target = target; } private final class_1297 target; @Generated
/*    */   public class_1657 getPlayer() {
/* 11 */     return this.player; } @Generated
/* 12 */   public class_1297 getTarget() { return this.target; }
/*    */ 
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\api\events\implement\EventAttackEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */