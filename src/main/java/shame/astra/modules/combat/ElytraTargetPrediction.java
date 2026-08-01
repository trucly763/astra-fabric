package shame.astra.modules.combat;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class ElytraTargetPrediction {

    /**
     * Predict future position of flying player
     */
    public static Vec3 predictElytraPosition(LivingEntity target, float ticks) {
        if (!(target instanceof Player)) {
            return target.position();
        }

        Player player = (Player) target;
        if (!player.isFallFlying()) {
            return target.position();
        }

        Vec3 currentPos = target.position();
        Vec3 velocity = target.getDeltaMovement();

        // Elytra physics - velocity decreases over time
        float drag = 0.99f;
        float gravityEffect = 0.05f;

        Vec3 predictedVel = velocity.scale(Math.pow(drag, ticks));
        predictedVel = predictedVel.add(0, -gravityEffect * ticks, 0);

        Vec3 predictedPos = currentPos.add(velocity.scale(ticks))
                .add(0, -gravityEffect * ticks * ticks / 2.0, 0);

        return predictedPos;
    }

    /**
     * Calculate interception point for projectiles
     */
    public static Vec3 calculateInterceptPoint(LivingEntity playerPos, LivingEntity targetEntity, 
                                              float projectileSpeed) {
        if (!(targetEntity instanceof Player)) {
            return targetEntity.getEyePosition();
        }

        Player target = (Player) targetEntity;
        if (!target.isFallFlying()) {
            return target.getEyePosition();
        }

        Vec3 targetPos = target.position();
        Vec3 targetVel = target.getDeltaMovement();
        Vec3 relPos = targetPos.subtract(playerPos.position());

        // Solve: |relPos + targetVel*t| = projectileSpeed * t
        double a = targetVel.lengthSqr() - projectileSpeed * projectileSpeed;
        double b = 2 * relPos.dot(targetVel);
        double c = relPos.lengthSqr();

        double discriminant = b * b - 4 * a * c;
        if (discriminant < 0) {
            return targetPos.add(targetVel.scale(5));
        }

        double t = (-b + Math.sqrt(discriminant)) / (2 * a);
        if (t < 0) t = (-b - Math.sqrt(discriminant)) / (2 * a);

        return targetPos.add(targetVel.scale(Math.max(0, t)));
    }

    /**
     * Check if target is in optimal elytra combat range
     */
    public static boolean isOptimalElytraRange(LivingEntity attacker, LivingEntity target, 
                                              float minRange, float maxRange) {
        if (!(target instanceof Player)) return false;

        Player player = (Player) target;
        if (!player.isFallFlying()) return false;

        double distance = attacker.distanceTo(target);
        return distance >= minRange && distance <= maxRange;
    }

    /**
     * Calculate rotation angles to intercept point
     */
    public static float[] calculateInterceptRotation(LivingEntity attacker, Vec3 interceptPoint) {
        Vec3 attackerEye = attacker.getEyePosition();
        Vec3 diff = interceptPoint.subtract(attackerEye);

        float yaw = (float) Math.atan2(diff.z, diff.x) * 180 / (float) Math.PI - 90;
        float pitch = -(float) Math.asin(diff.y / diff.length()) * 180 / (float) Math.PI;

        return new float[]{yaw, pitch};
    }

    /**
     * Smooth rotation update
     */
    public static float[] smoothRotation(float[] currentRotation, float[] targetRotation, 
                                        float rotationSpeed) {
        float yaw = smoothAngle(currentRotation[0], targetRotation[0], rotationSpeed);
        float pitch = Mth.clamp(
                smoothAngle(currentRotation[1], targetRotation[1], rotationSpeed),
                -90, 90
        );

        return new float[]{yaw, pitch};
    }

    private static float smoothAngle(float current, float target, float speed) {
        float diff = target - current;

        // Normalize angle difference to -180 to 180
        while (diff > 180) diff -= 360;
        while (diff < -180) diff += 360;

        if (Math.abs(diff) < speed) {
            return target;
        }

        return current + (diff > 0 ? speed : -speed);
    }

    /**
     * Estimate hit chance based on distance and target movement
     */
    public static float estimateHitChance(LivingEntity attacker, LivingEntity target,
                                         float projectileSpeed) {
        if (!(target instanceof Player)) return 0.5f;

        Player player = (Player) target;
        if (!player.isFallFlying()) return 0.7f;

        double distance = attacker.distanceTo(target);
        Vec3 targetVel = target.getDeltaMovement();
        
        // Base hit chance decreases with distance
        float distanceFactor = Math.max(0, 1.0f - (float) distance / 50.0f);
        
        // Velocity factor - faster targets are harder to hit
        float velocityFactor = Math.max(0, 1.0f - (float) targetVel.length() / 2.0f);
        
        // Projectile speed factor - faster projectiles are more accurate
        float speedFactor = Math.min(1.0f, projectileSpeed / 2.0f);

        return distanceFactor * 0.4f + velocityFactor * 0.3f + speedFactor * 0.3f;
    }

    /**
     * Check if player is elytra spiking (falling with elytra)
     */
    public static boolean isElytraSpiking(Player player) {
        if (!player.isFallFlying()) return false;

        Vec3 velocity = player.getDeltaMovement();
        // Negative Y velocity indicates falling/diving
        return velocity.y < -0.5;
    }

    /**
     * Predict elytra landing spot
     */
    public static Vec3 predictLandingSpot(Player player) {
        if (!player.isFallFlying()) {
            return player.position();
        }

        Vec3 pos = player.position();
        Vec3 vel = player.getDeltaMovement();

        // Simulate until Y velocity makes it negative (landing)
        float drag = 0.99f;
        float gravity = 0.05f;
        int maxTicks = 200;

        for (int i = 0; i < maxTicks; i++) {
            vel = vel.scale(drag).add(0, -gravity, 0);
            pos = pos.add(vel);

            if (vel.y < 0 && pos.y < player.getY()) {
                return pos;
            }
        }

        return pos;
    }
}
