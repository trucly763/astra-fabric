/*    */ package shame.astra.api.storages.implement.helpertstorages;
/*    */ public class NeuroPattern implements Serializable { private static final long serialVersionUID = 1L; private final float yaw; private final float pitch; private final float deltaYaw;
/*    */   private final float deltaPitch;
/*    */   
/*    */   @Generated
/*  6 */   public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof NeuroPattern)) return false;  NeuroPattern other = (NeuroPattern)o; if (!other.canEqual(this)) return false;  if (Float.compare(getYaw(), other.getYaw()) != 0) return false;  if (Float.compare(getPitch(), other.getPitch()) != 0) return false;  if (Float.compare(getDeltaYaw(), other.getDeltaYaw()) != 0) return false;  if (Float.compare(getDeltaPitch(), other.getDeltaPitch()) != 0) return false;  if (Double.compare(getDistance(), other.getDistance()) != 0) return false;  if (getTimestamp() != other.getTimestamp()) return false;  if (isCritical() != other.isCritical()) return false;  if (Double.compare(getTargetSpeed(), other.getTargetSpeed()) != 0) return false;  if (Float.compare(getSmoothness(), other.getSmoothness()) != 0) return false;  Object this$targetType = getTargetType(), other$targetType = other.getTargetType(); return !((this$targetType == null) ? (other$targetType != null) : !this$targetType.equals(other$targetType)); } private final double distance; private final long timestamp; private final boolean isCritical; private final double targetSpeed; private final String targetType; private final float smoothness; @Generated protected boolean canEqual(Object other) { return other instanceof NeuroPattern; } @Generated public int hashCode() { int PRIME = 59; result = 1; result = result * 59 + Float.floatToIntBits(getYaw()); result = result * 59 + Float.floatToIntBits(getPitch()); result = result * 59 + Float.floatToIntBits(getDeltaYaw()); result = result * 59 + Float.floatToIntBits(getDeltaPitch()); long $distance = Double.doubleToLongBits(getDistance()); result = result * 59 + (int)($distance >>> 32L ^ $distance); long $timestamp = getTimestamp(); result = result * 59 + (int)($timestamp >>> 32L ^ $timestamp); result = result * 59 + (isCritical() ? 79 : 97); long $targetSpeed = Double.doubleToLongBits(getTargetSpeed()); result = result * 59 + (int)($targetSpeed >>> 32L ^ $targetSpeed); result = result * 59 + Float.floatToIntBits(getSmoothness()); Object $targetType = getTargetType(); return result * 59 + (($targetType == null) ? 43 : $targetType.hashCode()); } @Generated public String toString() { return "NeuroPattern(yaw=" + getYaw() + ", pitch=" + getPitch() + ", deltaYaw=" + getDeltaYaw() + ", deltaPitch=" + getDeltaPitch() + ", distance=" + getDistance() + ", timestamp=" + getTimestamp() + ", isCritical=" + isCritical() + ", targetSpeed=" + getTargetSpeed() + ", targetType=" + getTargetType() + ", smoothness=" + getSmoothness() + ")"; }
/*    */   
/*    */   @Generated
/*  9 */   public float getYaw() { return this.yaw; } @Generated
/* 10 */   public float getPitch() { return this.pitch; } @Generated
/* 11 */   public float getDeltaYaw() { return this.deltaYaw; } @Generated
/* 12 */   public float getDeltaPitch() { return this.deltaPitch; } @Generated
/* 13 */   public double getDistance() { return this.distance; } @Generated
/* 14 */   public long getTimestamp() { return this.timestamp; } @Generated
/* 15 */   public boolean isCritical() { return this.isCritical; } @Generated
/* 16 */   public double getTargetSpeed() { return this.targetSpeed; } @Generated
/* 17 */   public String getTargetType() { return this.targetType; } @Generated
/* 18 */   public float getSmoothness() { return this.smoothness; }
/*    */ 
/*    */   
/*    */   public NeuroPattern(float yaw, float pitch, float deltaYaw, float deltaPitch, double distance, boolean isCritical, double targetSpeed, String targetType, float smoothness) {
/* 22 */     this.yaw = yaw;
/* 23 */     this.pitch = pitch;
/* 24 */     this.deltaYaw = deltaYaw;
/* 25 */     this.deltaPitch = deltaPitch;
/* 26 */     this.distance = distance;
/* 27 */     this.timestamp = System.currentTimeMillis();
/* 28 */     this.isCritical = isCritical;
/* 29 */     this.targetSpeed = targetSpeed;
/* 30 */     this.targetType = targetType;
/* 31 */     this.smoothness = smoothness;
/*    */   } }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\api\storages\implement\helpertstorages\NeuroPattern.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */