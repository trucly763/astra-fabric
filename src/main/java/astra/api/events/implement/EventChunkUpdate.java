/*    */ package shame.astra.api.events.implement;
/*    */ 
/*    */ 
/*    */ public class EventChunkUpdate extends Event {
/*    */   private final int chunkX;
/*    */   
/*    */   @Generated
/*  8 */   public EventChunkUpdate(int chunkX, int chunkZ) { this.chunkX = chunkX; this.chunkZ = chunkZ; } private final int chunkZ; @Generated
/*    */   public int getChunkX() {
/* 10 */     return this.chunkX; } @Generated
/* 11 */   public int getChunkZ() { return this.chunkZ; }
/*    */ 
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\api\events\implement\EventChunkUpdate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */