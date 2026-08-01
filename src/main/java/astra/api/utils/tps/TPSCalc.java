/*    */ package shame.astra.api.utils.tps;
/*    */ 
/*    */ import lombok.Generated;
/*    */ import net.minecraft.class_310;
/*    */ import net.minecraft.class_3532;
/*    */ import shame.astra.api.events.EventLink;
/*    */ import shame.astra.api.events.implement.EventPacket;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TPSCalc
/*    */ {
/* 13 */   private float TPS = 20.0F;
/* 14 */   private float adjustTicks = 0.0F; private long timestamp; private long lastPacketTime; private static final int SAMPLE_SIZE = 20; private final float[] tpsSamples; private int sampleIndex; @Generated public float getAdjustTicks() { return this.adjustTicks; }
/*    */   @Generated
/* 16 */   public long getTimestamp() { return this.timestamp; } @Generated
/* 17 */   public long getLastPacketTime() { return this.lastPacketTime; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @EventLink
/*    */   public void onPacket(EventPacket e) {
/* 24 */     if (e.getType() == EventPacket.Type.RECEIVE && e.getPacket() instanceof net.minecraft.class_2761) {
/* 25 */       updateTPS();
/*    */     }
/*    */   }
/*    */   
/*    */   public float getTPS() {
/* 30 */     if (this.lastPacketTime == 0L) {
/* 31 */       return this.TPS;
/*    */     }
/*    */     
/* 34 */     class_310 mc = class_310.method_1551();
/* 35 */     if (mc == null || mc.method_1562() == null || System.currentTimeMillis() - this.lastPacketTime > 3500L) {
/* 36 */       return 20.0F;
/*    */     }
/*    */     
/* 39 */     return this.TPS;
/*    */   }
/*    */   
/*    */   public TPSCalc() {
/* 43 */     this.tpsSamples = new float[20];
/* 44 */     this.sampleIndex = 0; } @Generated public int getSampleIndex() { return this.sampleIndex; }
/*    */   @Generated
/*    */   public float[] getTpsSamples() { return this.tpsSamples; } private void updateTPS() {
/* 47 */     long now = System.nanoTime();
/* 48 */     this.lastPacketTime = System.currentTimeMillis();
/* 49 */     if (this.timestamp == 0L) {
/* 50 */       this.timestamp = now;
/*    */       
/*    */       return;
/*    */     } 
/* 54 */     long delay = now - this.timestamp;
/* 55 */     this.timestamp = now;
/* 56 */     if (delay <= 0L) {
/*    */       return;
/*    */     }
/*    */     
/* 60 */     float maxTPS = 20.0F;
/* 61 */     float rawTPS = maxTPS * 1.0E9F / (float)delay;
/* 62 */     float boundedTPS = class_3532.method_15363(rawTPS, 0.0F, maxTPS);
/*    */     
/* 64 */     this.tpsSamples[this.sampleIndex % 20] = boundedTPS;
/* 65 */     this.sampleIndex++;
/*    */     
/* 67 */     int sampleCount = Math.min(this.sampleIndex, 20);
/* 68 */     float sum = 0.0F;
/* 69 */     for (int i = 0; i < sampleCount; i++) {
/* 70 */       float sample = this.tpsSamples[i];
/* 71 */       sum += sample;
/*    */     } 
/*    */     
/* 74 */     this.TPS = (float)round((sum / sampleCount));
/* 75 */     this.adjustTicks = this.TPS - maxTPS;
/*    */   }
/*    */   
/*    */   public double round(double input) {
/* 79 */     return Math.round(input * 10.0D) / 10.0D;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\tps\TPSCalc.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */