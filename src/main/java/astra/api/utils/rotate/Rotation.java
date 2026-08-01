/*    */ package shame.astra.api.utils.rotate;
/*    */ 
/*    */ import lombok.Generated;
/*    */ import net.minecraft.class_1297;
/*    */ import net.minecraft.class_243;
/*    */ import net.minecraft.class_3532;
/*    */ 
/*    */ public class Rotation implements QClient {
/*    */   private float yaw;
/*    */   private float pitch;
/*    */   
/*    */   @Generated
/* 13 */   public void setYaw(float yaw) { this.yaw = yaw; } @Generated public void setPitch(float pitch) { this.pitch = pitch; } @Generated public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof Rotation)) return false;  Rotation other = (Rotation)o; return !other.canEqual(this) ? false : ((Float.compare(getYaw(), other.getYaw()) != 0) ? false : (!(Float.compare(getPitch(), other.getPitch()) != 0))); } @Generated protected boolean canEqual(Object other) { return other instanceof Rotation; } @Generated public int hashCode() { int PRIME = 59; result = 1; result = result * 59 + Float.floatToIntBits(getYaw()); return result * 59 + Float.floatToIntBits(getPitch()); } @Generated public String toString() { return "Rotation(yaw=" + getYaw() + ", pitch=" + getPitch() + ")"; }
/*    */   @Generated
/* 15 */   public Rotation(float yaw, float pitch) { this.yaw = yaw; this.pitch = pitch; } @Generated
/*    */   public Rotation() {} @Generated
/* 17 */   public float getYaw() { return this.yaw; } @Generated public float getPitch() { return this.pitch; }
/*    */   
/*    */   public Rotation(class_1297 entity) {
/* 20 */     this.yaw = entity.method_36454();
/* 21 */     this.pitch = entity.method_36455();
/*    */   }
/*    */   
/*    */   public float getDelta(Rotation target) {
/* 25 */     float yawDelta = class_3532.method_15393(target.getYaw() - this.yaw);
/* 26 */     float pitchDelta = target.getPitch() - this.pitch;
/* 27 */     return (float)Math.hypot(Math.abs(yawDelta), Math.abs(pitchDelta));
/*    */   }
/*    */   
/*    */   public double getDeltaDouble(Rotation target) {
/* 31 */     double yawDelta = class_3532.method_15393(target.getYaw() - this.yaw);
/* 32 */     double pitchDelta = class_3532.method_15393(target.getPitch() - this.pitch);
/* 33 */     return Math.hypot(yawDelta, pitchDelta);
/*    */   }
/*    */   
/*    */   public static class_5611 camera() {
/* 37 */     return new class_5611(cameraYaw(), cameraPitch());
/*    */   }
/*    */   
/*    */   public static float cameraYaw() {
/* 41 */     return class_3532.method_15393(mc.field_1773.method_19418().method_19330() + (mc.field_1773.method_19418().method_19333() ? '´' : false));
/*    */   }
/*    */   
/*    */   public static float cameraPitch() {
/* 45 */     return (mc.field_1773.method_19418().method_19333() ? -1 : true) * mc.field_1773.method_19418().method_19329();
/*    */   }
/*    */   
/*    */   public static Rotation from(class_1657 player, class_1297 target) {
/* 49 */     class_243 playerPos = player.method_5836(0.0F);
/* 50 */     class_243 targetPos = target.method_19538().method_1031(0.0D, target.method_17682() * 0.5D, 0.0D);
/*    */     
/* 52 */     double dx = targetPos.field_1352 - playerPos.field_1352;
/* 53 */     double dy = targetPos.field_1351 - playerPos.field_1351;
/* 54 */     double dz = targetPos.field_1350 - playerPos.field_1350;
/*    */     
/* 56 */     double distanceXZ = Math.sqrt(dx * dx + dz * dz);
/*    */     
/* 58 */     float yaw = (float)Math.toDegrees(Math.atan2(dz, dx)) - 90.0F;
/* 59 */     float pitch = (float)-Math.toDegrees(Math.atan2(dy, distanceXZ));
/*    */     
/* 61 */     return new Rotation(yaw, pitch);
/*    */   }
/*    */   
/*    */   public final class_243 toVector() {
/* 65 */     float f = this.pitch * 0.017453292F;
/* 66 */     float g = -this.yaw * 0.017453292F;
/* 67 */     float h = class_3532.method_15362(g);
/* 68 */     float i = class_3532.method_15374(g);
/* 69 */     float j = class_3532.method_15362(f);
/* 70 */     float k = class_3532.method_15374(f);
/* 71 */     return new class_243((i * j), -k, (h * j));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\rotate\Rotation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */