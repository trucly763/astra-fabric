/*    */ package shame.astra.api.utils.combat;
/*    */ 
/*    */ import lombok.Generated;
/*    */ import net.minecraft.class_243;
/*    */ 
/*    */ public class PositionData {
/*    */   private double serverX;
/*    */   private double serverY;
/*    */   private double serverZ;
/*    */   private double prevServerX;
/*    */   private double prevServerY;
/*    */   private double prevServerZ;
/*    */   private double backUpX;
/*    */   private double backUpY;
/*    */   private double backUpZ;
/*    */   private double lastSpeed;
/*    */   private double prevSpeed;
/*    */   private long lastUpdate;
/*    */   
/*    */   @Generated
/* 21 */   public double getServerX() { return this.serverX; } @Generated public double getServerY() { return this.serverY; } @Generated public double getServerZ() { return this.serverZ; } @Generated
/* 22 */   public double getPrevServerX() { return this.prevServerX; } @Generated public double getPrevServerY() { return this.prevServerY; } @Generated public double getPrevServerZ() { return this.prevServerZ; } @Generated
/* 23 */   public double getBackUpX() { return this.backUpX; } @Generated public double getBackUpY() { return this.backUpY; } @Generated public double getBackUpZ() { return this.backUpZ; } @Generated
/* 24 */   public double getLastSpeed() { return this.lastSpeed; } @Generated public double getPrevSpeed() { return this.prevSpeed; } @Generated
/* 25 */   public long getLastUpdate() { return this.lastUpdate; }
/*    */   
/*    */   public class_243 getResolvedPos() {
/* 28 */     return new class_243(this.serverX, this.serverY, this.serverZ);
/*    */   }
/*    */   
/*    */   public class_243 getResolvedForward() {
/* 32 */     return new class_243(this.serverX - this.prevServerX, this.serverY - this.prevServerY, this.serverZ - this.prevServerZ);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void update(double x, double y, double z) {
/* 40 */     this.backUpX = this.prevServerX;
/* 41 */     this.backUpY = this.prevServerY;
/* 42 */     this.backUpZ = this.prevServerZ;
/*    */     
/* 44 */     this.prevServerX = this.serverX;
/* 45 */     this.prevServerY = this.serverY;
/* 46 */     this.prevServerZ = this.serverZ;
/* 47 */     this.serverX = x;
/* 48 */     this.serverY = y;
/* 49 */     this.serverZ = z;
/*    */     
/* 51 */     this.prevSpeed = this.lastSpeed;
/* 52 */     this.lastSpeed = getResolvedForward().method_1033() * 20.0D;
/* 53 */     this.lastUpdate = System.currentTimeMillis();
/*    */   }
/*    */   
/*    */   public boolean isSpeedChanged() {
/* 57 */     return (this.lastSpeed >= 20.0D || (this.lastSpeed != this.prevSpeed && this.lastSpeed == 0.0D));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\combat\PredictUtils$PositionData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */