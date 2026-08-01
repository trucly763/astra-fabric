package shame.astra.features.modules.movement;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import shame.astra.core.manager.client.ModuleManager;
import shame.astra.events.impl.EventFireworkMotion;
import shame.astra.features.modules.Module;
import shame.astra.features.modules.combat.Aura;
import shame.astra.setting.Setting;
import shame.astra.setting.impl.SettingGroup;

public final class ElytraBoost extends Module {
   private static final int[] YAW_VECTORS = new int[]{-45, 45, 135, -135};
   private static final int[] PITCH_VECTORS = new int[]{-45, 45};
   private final Setting<Boolean> smartSpeed = new Setting<>("SmartSpeed", true);
   private final Setting<Float> fireworkSpeed = new Setting<>("FireworkSpeed", 2.5F, 1.5F, 3.5F, v -> !this.smartSpeed.getValue());
   private final Setting<String> boostMode = new Setting<>("BoostMode", "GrimAC", "GrimAC", "Vulkan", "NoDetect");
   private final Setting<Float> speedMultiplier = new Setting<>("SpeedMult", 1.8F, 1.0F, 3.0F);
   private final Setting<Boolean> motionSpoof = new Setting<>("MotionSpoof", true);
   private final Setting<Boolean> velocityBypass = new Setting<>("VelocityBypass", true);
   private final Setting<Float> packetDelay = new Setting<>("PacketDelay", 0.5F, 0.0F, 2.0F);
   
   private final Setting<Float> fireworkSpeedMinGlobal = new Setting<>(
      "MinSpeedGlobal", 1.65F, 1.5F, 5.0F, v -> this.smartSpeed.getValue()
   );
   private final Setting<Float> fireworkSpeedMaxGlobal = new Setting<>(
      "MaxSpeedGlobal", 3.0F, 1.5F, 5.0F, v -> this.smartSpeed.getValue()
   );
   private final Setting<Float> fireworkSpeedMaxYaw = new Setting<>("MaxSpeedYaw", 2.5F, 1.5F, 5.0F, v -> this.smartSpeed.getValue());
   private final Setting<Float> fireworkSpeedMaxPitch = new Setting<>(
      "MaxSpeedPitch", 3.0F, 1.5F, 5.0F, v -> this.smartSpeed.getValue()
   );

   private final Setting<Boolean> autoBoostOnTarget = new Setting<>("Auto Boost On Target", true);
   private final Setting<Float> targetLockDistance = new Setting<>("Lock Distance", 5.0F, 1.0F, 15.0F);
   private final Setting<Float> boostIntensity = new Setting<>("Boost Intensity", 1.0F, 0.5F, 2.0F);
   private final Setting<Boolean> matrix = new Setting<>("Matrix", true);
   private final Setting<Boolean> untrusted = new Setting<>("Untrusted", false);
   
   private int boostTick = 0;
   private double lastSpeedXZ = 0;
   private LivingEntity lockedTarget = null;

   public ElytraBoost() {
      super("ElytraBoost", "Boosts firework power while elytra flying (No Flag).", Module.Category.MOVEMENT);
   }

   @EventHandler
   public void onFireworkMotion(EventFireworkMotion event) {
      if (event == null || mc.player == null) return;

      // Auto boost when ElytraTarget locked
      if (autoBoostOnTarget.getValue()) {
         updateTargetLock();
         if (lockedTarget != null) {
            double distance = mc.player.distanceTo(lockedTarget);
            if (distance < targetLockDistance.getValue()) {
               // FULL BOOST MODE - dính như keo 502
               event.setVector(new Vec3d(3.5 * boostIntensity.getValue(), 2.0, 3.5 * boostIntensity.getValue()));
               applyStickMotion(lockedTarget);
               return;
            }
         }
      }

      String mode = boostMode.getValue();
      double speedXZ = 1.6;
      double speedY = 1.6;

      switch (mode) {
         case "GrimAC" -> speedXZ = handleGrimACBoost();
         case "Vulkan" -> speedXZ = handleVulkanBoost();
         case "NoDetect" -> speedXZ = handleNoDetectBoost();
      }

      // Speed multiplier
      speedXZ *= speedMultiplier.getValue();

      if (smartSpeed.getValue()) {
         speedXZ = Math.max(fireworkSpeedMinGlobal.getValue(), speedXZ);
         speedXZ = Math.min(fireworkSpeedMaxGlobal.getValue(), speedXZ);
      }

      lastSpeedXZ = speedXZ;
      event.setVector(new Vec3d(speedXZ, speedY, speedXZ));
      
      // Motion spoof để không bị detect
      if (motionSpoof.getValue()) {
         applyMotionSpoof();
      }
   }

   private double handleGrimACBoost() {
      // GrimAC bypass mạnh: gradual acceleration + velocity spoof
      boostTick++;
      
      // Slow ramp-up để GrimAC không detect sudden velocity change
      double baseBoost = 1.6;
      double acceleration = Math.min(1.5, boostTick * 0.08);
      double boost = baseBoost + acceleration;
      
      // Add small sine wave variation (natural movement)
      boost += Math.sin(boostTick * 0.05) * 0.1;
      
      // Reset after 25 ticks
      if (boostTick > 25) boostTick = 0;
      
      return Math.min(3.0, boost);
   }

   private double handleVulkanBoost() {
      // Vulkan: smooth ramp up
      boostTick++;
      double progress = Math.min(1.0, boostTick / 10.0);
      return 1.6 + (progress * 1.2);
   }

   private double handleNoDetectBoost() {
      // No Detect: gradient increase (khó detect)
      double yaw = Math.abs(Math.sin(mc.player.getYRot() * 0.01));
      return 1.8 + (yaw * 0.8);
   }

   private void updateTargetLock() {
      // Get target từ ElytraTarget module
      if (Aura.target instanceof LivingEntity) {
         lockedTarget = (LivingEntity) Aura.target;
      } else {
         lockedTarget = null;
      }
   }

   private void applyStickMotion(LivingEntity target) {
      if (mc.player == null || target == null) return;

      // Calculate position để "dính" vào target như keo 502
      Vec3d playerPos = mc.player.getPos();
      Vec3d targetPos = target.getPos();
      Vec3d direction = targetPos.subtract(playerPos).normalize();

      // Move towards target aggressively
      double stickForce = 0.15;
      mc.player.setVelocity(
         mc.player.getVelocity().x + direction.x * stickForce,
         mc.player.getVelocity().y,
         mc.player.getVelocity().z + direction.z * stickForce
      );

      // Add micro jitter để tránh teleport detection
      double jitter = (Math.random() - 0.5) * 0.01;
      Vec3d vel = mc.player.getVelocity();
      mc.player.setVelocity(vel.x + jitter, vel.y, vel.z + jitter);
   }

   private void applyMotionSpoof() {
      if (!velocityBypass.getValue() || mc.player == null) return;

      Vec3d vel = mc.player.getVelocity();
      
      // GrimAC detection bypass: add micro-variations
      double randX = (Math.random() - 0.5) * 0.001;  // Tiny offset
      double randZ = (Math.random() - 0.5) * 0.001;
      
      // Add yaw-based velocity (natural looking)
      float yaw = mc.player.getYaw();
      double yawFactor = Math.cos(Math.toRadians(yaw)) * 0.005;
      
      mc.player.setVelocity(
         vel.x + randX + yawFactor,
         vel.y,
         vel.z + randZ
      );
   }

   public double getBoostV2() {
      LivingEntity target = Aura.target instanceof LivingEntity living ? living : null;
      float lastYaw = target != null ? ModuleManager.aura.rotationYaw : mc.player.getYaw();
      float lastPitch = target != null ? ModuleManager.aura.rotationPitch : mc.player.getPitch();
      if (Math.abs(lastPitch) > 55.0F) {
         return 1.55;
      }

      double yawRad = Math.toRadians(lastYaw);
      double pitchRad = Math.toRadians(lastPitch);
      double sinYaw = Math.sin(yawRad);
      double cosYaw = Math.cos(yawRad);
      double cosPitch = Math.cos(pitchRad);
      if (cosPitch < 1.0E-6) {
         return 1.55;
      }

      double m = Math.max(Math.abs(sinYaw), Math.abs(cosYaw));
      double pitchContrib = 1.0 / cosPitch - 1.0;
      double yawContrib = 1.0 / m - 1.0;
      double a = 0.15;
      double b = 1.45;
      double desiredYawMaxBoost = this.fireworkSpeedMaxYaw.getValue().floatValue();
      double desiredPitchMaxBoost = this.fireworkSpeedMaxPitch.getValue().floatValue();
      double yawMaxContrib = (desiredYawMaxBoost - a) / b - 1.0;
      double pitchMaxContrib = (desiredPitchMaxBoost - a) / b - 1.0;
      pitchContrib = Math.min(pitchContrib, pitchMaxContrib);
      yawContrib = Math.min(yawContrib, yawMaxContrib);
      double inv = 1.0 + pitchContrib + yawContrib;
      double a2 = 0.15;
      double b2 = 1.45;
      return a2 + b2 * inv;
   }

   public double getBoostV1() {
      LivingEntity target = Aura.target instanceof LivingEntity living ? living : null;
      float lastYaw = target != null ? ModuleManager.aura.rotationYaw : mc.player.getYaw();
      float lastPitch = target != null ? ModuleManager.aura.rotationPitch : mc.player.getPitch();
      if (Math.abs(lastPitch) > 55.0F) {
         return 1.55;
      }

      float boostYaw = this.adjustBoostForYaw(lastYaw);
      double boostPitch = this.adjustBoostForPitch(lastYaw, lastPitch);
      double boost = boostYaw + (boostPitch - 1.6F);
      boost = Math.max(1.6, boost);
      return this.matrix.getValue() ? Math.min(boost, 2.1) : boost;
   }

   private float adjustBoostForYaw(float lastYaw) {
      int closestYawIndex = findClosestVector(lastYaw, YAW_VECTORS);
      if (closestYawIndex == -1) {
         return 1.6F;
      }

      float yawDistance = Math.abs(MathHelper.wrapDegrees(lastYaw) - YAW_VECTORS[closestYawIndex]);
      float maxBoost = 2.2F;
      float minBoostValue = 1.6F;
      float maxDistance = 12.0F;
      float variableSpeedSmart = 0.0F;
      if (yawDistance <= maxDistance) {
         float ratio = yawDistance / maxDistance;
         variableSpeedSmart = maxBoost - (maxBoost - minBoostValue) * ratio;
      }

      float variableSpeed = getVariableSpeed(yawDistance);
      float finalSpeed = Math.max(variableSpeedSmart, variableSpeed);
      float max = this.untrusted.getValue() ? 1.95F : 1.8F;
      return this.matrix.getValue() ? Math.min(finalSpeed, max) : finalSpeed;
   }

   private static float getVariableSpeed(float yawDistance) {
      float[] thresholds = new float[]{4.0F, 8.0F, 11.0F, 15.0F, 21.0F, 28.0F};
      float[] speeds = new float[]{2.2F, 2.1F, 2.0F, 1.9F, 1.8F, 1.7F, 1.6F};
      int level = 0;

      while (level < thresholds.length && yawDistance >= thresholds[level]) {
         level++;
      }

      return speeds[level];
   }

   private double adjustBoostForPitch(float lastYaw, float lastPitch) {
      int closestYawIndex = findClosestVector(lastPitch, PITCH_VECTORS);
      if (closestYawIndex == -1) {
         return 1.6F;
      }

      int closestYawIndex1 = findClosestVector(lastYaw, YAW_VECTORS);
      float yawDistance1 = Math.abs(MathHelper.wrapDegrees(lastYaw) - YAW_VECTORS[closestYawIndex1]);
      float yawDistance = Math.abs(MathHelper.wrapDegrees(lastPitch) - PITCH_VECTORS[closestYawIndex]);
      float maxBoost = getVariableSpeed(yawDistance);
      float minBoostValue = 1.6F;
      float maxDistance = 45.0F;
      float variableSpeedSmart = 0.0F;
      if (yawDistance <= maxDistance) {
         float ratio = yawDistance / maxDistance;
         variableSpeedSmart = maxBoost - (maxBoost - minBoostValue) * ratio;
      }

      return variableSpeedSmart;
   }

   private static int findClosestVector(float angle, int[] vectors) {
      int minDistIndex = -1;
      float minDist = Float.MAX_VALUE;

      for (int i = 0; i < vectors.length; i++) {
         float dist = Math.abs(MathHelper.wrapDegrees(angle) - vectors[i]);
         if (dist < minDist) {
            minDist = dist;
            minDistIndex = i;
         }
      }

      return minDistIndex;
   }
}
