/*    */ package shame.astra.api.storages.implement.helpertstorages;
/*    */ public final class SituationKey { private final String targetType; private final String distanceBucket;
/*    */   
/*    */   @Generated
/*  5 */   public SituationKey(String targetType, String distanceBucket, String movementState, String critState, String healthState) { this.targetType = targetType; this.distanceBucket = distanceBucket; this.movementState = movementState; this.critState = critState; this.healthState = healthState; } private final String movementState; private final String critState; private final String healthState; @Generated public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof SituationKey)) return false;  SituationKey other = (SituationKey)o; Object this$targetType = getTargetType(), other$targetType = other.getTargetType(); if ((this$targetType == null) ? (other$targetType != null) : !this$targetType.equals(other$targetType)) return false;  Object this$distanceBucket = getDistanceBucket(), other$distanceBucket = other.getDistanceBucket(); if ((this$distanceBucket == null) ? (other$distanceBucket != null) : !this$distanceBucket.equals(other$distanceBucket)) return false;  Object this$movementState = getMovementState(), other$movementState = other.getMovementState(); if ((this$movementState == null) ? (other$movementState != null) : !this$movementState.equals(other$movementState)) return false;  Object this$critState = getCritState(), other$critState = other.getCritState(); if ((this$critState == null) ? (other$critState != null) : !this$critState.equals(other$critState)) return false;  Object this$healthState = getHealthState(), other$healthState = other.getHealthState(); return !((this$healthState == null) ? (other$healthState != null) : !this$healthState.equals(other$healthState)); } @Generated public int hashCode() { int PRIME = 59; result = 1; Object $targetType = getTargetType(); result = result * 59 + (($targetType == null) ? 43 : $targetType.hashCode()); Object $distanceBucket = getDistanceBucket(); result = result * 59 + (($distanceBucket == null) ? 43 : $distanceBucket.hashCode()); Object $movementState = getMovementState(); result = result * 59 + (($movementState == null) ? 43 : $movementState.hashCode()); Object $critState = getCritState(); result = result * 59 + (($critState == null) ? 43 : $critState.hashCode()); Object $healthState = getHealthState(); return result * 59 + (($healthState == null) ? 43 : $healthState.hashCode()); }
/*    */   @Generated
/*  7 */   public String getTargetType() { return this.targetType; } @Generated
/*  8 */   public String getDistanceBucket() { return this.distanceBucket; } @Generated
/*  9 */   public String getMovementState() { return this.movementState; } @Generated
/* 10 */   public String getCritState() { return this.critState; } @Generated
/* 11 */   public String getHealthState() { return this.healthState; }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 15 */     return this.targetType + "_" + this.targetType + "_" + this.distanceBucket + "_" + this.movementState + "_" + this.critState;
/*    */   } }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\api\storages\implement\helpertstorages\SituationKey.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */