/*     */ package shame.astra.api.utils.combat;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import lombok.Generated;
/*     */ import net.minecraft.class_1309;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_3532;
/*     */ import shame.astra.api.QClient;
/*     */ import shame.astra.api.storages.implement.helpertstorages.enumvar.ModuleClass;
/*     */ 
/*     */ 
/*     */ public class PredictUtils
/*     */   implements QClient
/*     */ {
/*  17 */   private static final Map<UUID, PositionData> positionCache = new ConcurrentHashMap<>();
/*     */   public static class PositionData {
/*     */     private double serverX; private double serverY; private double serverZ; private double prevServerX; private double prevServerY; private double prevServerZ;
/*     */     @Generated
/*  21 */     public double getServerX() { return this.serverX; } private double backUpX; private double backUpY; private double backUpZ; private double lastSpeed; private double prevSpeed; private long lastUpdate; @Generated public double getServerY() { return this.serverY; } @Generated public double getServerZ() { return this.serverZ; } @Generated
/*  22 */     public double getPrevServerX() { return this.prevServerX; } @Generated public double getPrevServerY() { return this.prevServerY; } @Generated public double getPrevServerZ() { return this.prevServerZ; } @Generated
/*  23 */     public double getBackUpX() { return this.backUpX; } @Generated public double getBackUpY() { return this.backUpY; } @Generated public double getBackUpZ() { return this.backUpZ; } @Generated
/*  24 */     public double getLastSpeed() { return this.lastSpeed; } @Generated public double getPrevSpeed() { return this.prevSpeed; } @Generated
/*  25 */     public long getLastUpdate() { return this.lastUpdate; }
/*     */     
/*     */     public class_243 getResolvedPos() {
/*  28 */       return new class_243(this.serverX, this.serverY, this.serverZ);
/*     */     }
/*     */     
/*     */     public class_243 getResolvedForward() {
/*  32 */       return new class_243(this.serverX - this.prevServerX, this.serverY - this.prevServerY, this.serverZ - this.prevServerZ);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void update(double x, double y, double z) {
/*  40 */       this.backUpX = this.prevServerX;
/*  41 */       this.backUpY = this.prevServerY;
/*  42 */       this.backUpZ = this.prevServerZ;
/*     */       
/*  44 */       this.prevServerX = this.serverX;
/*  45 */       this.prevServerY = this.serverY;
/*  46 */       this.prevServerZ = this.serverZ;
/*  47 */       this.serverX = x;
/*  48 */       this.serverY = y;
/*  49 */       this.serverZ = z;
/*     */       
/*  51 */       this.prevSpeed = this.lastSpeed;
/*  52 */       this.lastSpeed = getResolvedForward().method_1033() * 20.0D;
/*  53 */       this.lastUpdate = System.currentTimeMillis();
/*     */     }
/*     */     
/*     */     public boolean isSpeedChanged() {
/*  57 */       return (this.lastSpeed >= 20.0D || (this.lastSpeed != this.prevSpeed && this.lastSpeed == 0.0D));
/*     */     }
/*     */   }
/*     */   
/*     */   public static void updateEntity(class_1309 entity) {
/*  62 */     PositionData data = positionCache.computeIfAbsent(entity.method_5667(), k -> new PositionData());
/*  63 */     data.update(entity.method_23317(), entity.method_23318(), entity.method_23321());
/*     */   }
/*     */   
/*     */   public static PositionData getData(class_1309 entity) {
/*  67 */     return positionCache.get(entity.method_5667());
/*     */   }
/*     */   
/*     */   public static class_243 predict(class_1309 entity, int ticks, float extraForward, boolean isMeFlying) {
/*  71 */     PositionData data = getData(entity);
/*  72 */     class_243 pos = new class_243(entity.method_23317(), entity.method_23318() + (entity.method_5751() / 2.0F), entity.method_23321());
/*     */     
/*  74 */     if (data == null) {
/*  75 */       return predictElytraPhysics(entity, pos, ticks);
/*     */     }
/*     */     
/*  78 */     class_243 forward = data.getResolvedForward();
/*  79 */     double speed = data.getLastSpeed();
/*  80 */     boolean isHighSpeed = data.isSpeedChanged();
/*     */     
/*  82 */     if (entity.method_6128()) {
/*  83 */       double horizontalSpeed = Math.hypot(forward.field_1352, forward.field_1350) * 20.0D;
/*  84 */       double verticalSpeed = Math.abs(forward.field_1351) * 20.0D;
/*     */       
/*  86 */       if (horizontalSpeed <= 5.0D && verticalSpeed <= 5.0D) {
/*  87 */         return pos;
/*     */       }
/*     */       
/*  90 */       boolean shouldPredict = (isMeFlying && entity.method_6128() && isHighSpeed);
/*  91 */       float predictMultiplier = shouldPredict ? ((ticks + 2) + extraForward) : ticks;
/*     */       
/*  93 */       class_243 linearPredict = pos.method_1019(forward.method_18805(predictMultiplier, predictMultiplier, predictMultiplier));
/*  94 */       class_243 physicsPredict = predictElytraPhysics(entity, pos, ticks);
/*     */       
/*  96 */       double weight = class_3532.method_15350(speed / 50.0D, 0.3D, 0.9D);
/*     */       
/*  98 */       return new class_243(
/*  99 */           class_3532.method_16436(weight, physicsPredict.field_1352, linearPredict.field_1352), 
/* 100 */           class_3532.method_16436(weight, physicsPredict.field_1351, linearPredict.field_1351), 
/* 101 */           class_3532.method_16436(weight, physicsPredict.field_1350, linearPredict.field_1350));
/*     */     } 
/*     */ 
/*     */     
/* 105 */     if (speed > 1.0D) {
/* 106 */       return pos.method_1019(forward.method_18805(ticks, ticks, ticks));
/*     */     }
/*     */     
/* 109 */     return pos;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class_243 predict(class_1309 entity, class_243 pos, int ticks) {
/* 114 */     PositionData data = getData(entity);
/*     */     
/* 116 */     if (data != null && entity.method_6128()) {
/* 117 */       class_243 forward = data.getResolvedForward();
/* 118 */       double horizontalSpeed = Math.hypot(forward.field_1352, forward.field_1350) * 20.0D;
/* 119 */       double verticalSpeed = Math.abs(forward.field_1351) * 20.0D;
/*     */       
/* 121 */       if (horizontalSpeed <= 5.0D && verticalSpeed <= 5.0D) {
/* 122 */         return pos;
/*     */       }
/*     */       
/* 125 */       return pos.method_1019(forward.method_18805(ticks, ticks, ticks));
/*     */     } 
/*     */     
/* 128 */     return predictElytraPhysics(entity, pos, ticks);
/*     */   }
/*     */   
/*     */   public static class_243 predictElytraPhysics(class_1309 entity, class_243 pos, int ticks) {
/* 132 */     class_243 velocity = entity.method_18798();
/*     */     
/* 134 */     if (!entity.method_6128()) {
/* 135 */       return pos.method_1019(velocity.method_18805(ticks, ticks, ticks));
/*     */     }
/*     */     
/* 138 */     double horizontalDelta = Math.hypot(entity.field_6014 - entity.method_23317(), entity.field_5969 - entity.method_23321()) * 20.0D;
/* 139 */     double verticalDelta = Math.abs(entity.method_23318() - entity.field_6036) * 20.0D;
/*     */     
/* 141 */     if (horizontalDelta <= 5.0D && verticalDelta <= 5.0D) {
/* 142 */       return pos;
/*     */     }
/*     */     
/* 145 */     for (int i = 0; i < ticks; i++) {
/* 146 */       class_243 rotation = entity.method_5720();
/* 147 */       float pitchRad = (float)Math.toRadians(entity.method_36455());
/* 148 */       double horizontalSpeed = Math.sqrt(velocity.field_1352 * velocity.field_1352 + velocity.field_1350 * velocity.field_1350);
/* 149 */       double velocityLength = velocity.method_1033();
/* 150 */       float cos = class_3532.method_15362(pitchRad);
/* 151 */       cos = (float)((cos * cos) * Math.min(1.0D, rotation.method_1033() / 0.4D));
/*     */       
/* 153 */       velocity = velocity.method_1031(0.0D, -0.08D * (-1.0D + cos * 0.75D), 0.0D);
/*     */       
/* 155 */       if (velocity.field_1351 < 0.0D && horizontalSpeed > 0.0D) {
/* 156 */         double d5 = velocity.field_1351 * -0.1D * cos;
/* 157 */         velocity = velocity.method_1031(rotation.field_1352 * d5 / horizontalSpeed, d5, rotation.field_1350 * d5 / horizontalSpeed);
/*     */       } 
/*     */       
/* 160 */       if (pitchRad < 0.0F && horizontalSpeed > 0.0D) {
/* 161 */         double lift = velocityLength * -class_3532.method_15374(pitchRad) * 0.04D;
/* 162 */         velocity = velocity.method_1031(-rotation.field_1352 * lift / horizontalSpeed, lift * 3.2D, -rotation.field_1350 * lift / horizontalSpeed);
/*     */       } 
/*     */       
/* 165 */       if (horizontalSpeed > 0.0D) {
/* 166 */         velocity = velocity.method_1031((rotation.field_1352 / horizontalSpeed * velocityLength - velocity.field_1352) * 0.1D, 0.0D, (rotation.field_1350 / horizontalSpeed * velocityLength - velocity.field_1350) * 0.1D);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 173 */       velocity = velocity.method_18805(0.99D, 0.98D, 0.99D);
/* 174 */       pos = pos.method_1019(velocity);
/*     */     } 
/*     */     
/* 177 */     return pos;
/*     */   }
/*     */   
/*     */   public static class_243 bypasselytrahacking(class_1309 target) {
/* 181 */     class_243 interpolatedRotation = class_243.method_1030(target.method_53829(), target.method_53831());
/* 182 */     class_243 rotationVector = target.method_5720();
/* 183 */     class_243 relativePos = target.method_19538().method_1031(0.0D, (target.method_17682() * 0.6F), 0.0D).method_1020(mc.field_1724.method_33571());
/* 184 */     class_243 blendedDirection = interpolatedRotation.method_1029().method_35590(rotationVector, interpolatedRotation.method_1033());
/* 185 */     return relativePos.method_1019(blendedDirection.method_1029().method_1021(ModuleClass.elytraTarget.forward.getValue().floatValue()));
/*     */   }
/*     */   
/*     */   public static void cleanup() {
/* 189 */     long now = System.currentTimeMillis();
/* 190 */     positionCache.entrySet().removeIf(e -> (now - ((PositionData)e.getValue()).getLastUpdate() > 10000L));
/*     */   }
/*     */   
/*     */   public static void clear() {
/* 194 */     positionCache.clear();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\combat\PredictUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */