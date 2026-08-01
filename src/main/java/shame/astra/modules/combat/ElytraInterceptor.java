package shame.astra.modules.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.Component;

public class ElytraInterceptor {
    private static final Minecraft mc = Minecraft.getInstance();
    
    private static final float ELYTRA_DRAG = 0.99f;
    private static final float GRAVITY = 0.05f;
    private static final float MAX_ELYTRA_SPEED = 2.0f;

    /**
     * Calculate intercept point ahead of target's elytra flight path
     */
    public static Vec3 calculateInterceptPoint(LivingEntity target, int ticksAhead) {
        if (!(target instanceof Player)) return target.position();
        
        Player player = (Player) target;
        if (!player.isFallFlying()) return target.position();

        Vec3 pos = target.position();
        Vec3 vel = target.getDeltaMovement();

        // Simulate elytra physics
        for (int i = 0; i < ticksAhead; i++) {
            // Apply drag
            vel = new Vec3(
                    vel.x * ELYTRA_DRAG,
                    vel.y * ELYTRA_DRAG,
                    vel.z * ELYTRA_DRAG
            );

            // Apply gravity (reduced due to elytra)
            vel = vel.add(0, -GRAVITY, 0);

            // Clamp speed
            double speed = vel.length();
            if (speed > MAX_ELYTRA_SPEED) {
                vel = vel.normalize().scale(MAX_ELYTRA_SPEED);
            }

            pos = pos.add(vel);
        }

        return pos;
    }

    /**
     * Get flight direction to intercept target
     */
    public static Vec3 getInterceptDirection(LivingEntity attacker, LivingEntity target) {
        if (!(target instanceof Player)) return Vec3.ZERO;

        Player player = (Player) target;
        if (!player.isFallFlying()) return Vec3.ZERO;

        // Predict where target will be in 20 ticks
        Vec3 interceptPoint = calculateInterceptPoint(target, 20);
        Vec3 currentPos = attacker.position();
        Vec3 toIntercept = interceptPoint.subtract(currentPos);

        return toIntercept.normalize();
    }

    /**
     * Calculate best position to intercept (ahead of enemy flight)
     */
    public static Vec3 calculateBestInterceptPosition(LivingEntity attacker, LivingEntity target) {
        if (!(target instanceof Player)) return target.position();

        Player player = (Player) target;
        if (!player.isFallFlying()) return target.position();

        // Predict 40 ticks ahead
        Vec3 futurePos = calculateInterceptPoint(target, 40);
        
        // Get offset perpendicular to flight direction
        Vec3 targetVel = player.getDeltaMovement();
        Vec3 side = new Vec3(-targetVel.z, 0, targetVel.x).normalize().scale(5);

        // Position to intercept from the side
        return futurePos.add(side);
    }

    /**
     * Get movement input for reaching intercept point
     */
    public static float[] getMovementInput(LivingEntity attacker, Vec3 targetPoint) {
        Vec3 currentPos = attacker.position();
        Vec3 diff = targetPoint.subtract(currentPos);

        double distance = diff.length();
        if (distance < 0.1) {
            return new float[]{0, 0, 0, 0};
        }

        // Normalize direction
        Vec3 direction = diff.normalize();

        // Convert to movement inputs
        float forward = 0;
        float strafe = 0;
        float up = 0;

        // Forward movement
        if (direction.z > 0) {
            forward = (float) Math.min(1.0, direction.z);
        } else if (direction.z < 0) {
            forward = (float) Math.max(-1.0, direction.z);
        }

        // Strafe movement
        if (direction.x > 0) {
            strafe = (float) Math.min(1.0, direction.x);
        } else if (direction.x < 0) {
            strafe = (float) Math.max(-1.0, direction.x);
        }

        // Up movement
        if (direction.y > 0.2) {
            up = 1.0f;
        } else if (direction.y < -0.2) {
            up = -1.0f;
        }

        // Return [forward, strafe, up, down]
        return new float[]{forward, strafe, up, 0};
    }

    /**
     * Calculate rotation to look at intercept point
     */
    public static float[] getInterceptRotation(LivingEntity attacker, Vec3 targetPoint) {
        Vec3 eye = attacker.getEyePosition();
        Vec3 diff = targetPoint.subtract(eye);

        float yaw = (float) Math.atan2(diff.z, diff.x) * 180 / (float) Math.PI - 90;
        float pitch = -(float) Math.asin(diff.y / diff.length()) * 180 / (float) Math.PI;

        return new float[]{yaw, pitch};
    }

    /**
     * Check if we're on intercept course
     */
    public static boolean isOnInterceptCourse(LivingEntity attacker, LivingEntity target, float tolerance) {
        if (!(target instanceof Player)) return false;

        Player player = (Player) target;
        if (!player.isFallFlying()) return false;

        Vec3 interceptPos = calculateInterceptPoint(target, 30);
        Vec3 currentPos = attacker.position();
        
        double distanceToIntercept = currentPos.distanceTo(interceptPos);
        
        return distanceToIntercept < tolerance;
    }

    /**
     * Get altitude difference to target
     */
    public static double getAltitudeDifference(LivingEntity attacker, LivingEntity target) {
        return target.getY() - attacker.getY();
    }

    /**
     * Predict if collision will occur
     */
    public static boolean willCollide(LivingEntity attacker, LivingEntity target, int ticksAhead) {
        if (!(target instanceof Player)) return false;

        Player player = (Player) target;
        if (!player.isFallFlying()) return false;

        Vec3 targetFuturePos = calculateInterceptPoint(target, ticksAhead);
        Vec3 currentPos = attacker.position();

        double distance = currentPos.distanceTo(targetFuturePos);
        
        // Consider collision if within 3 blocks
        return distance < 3.0;
    }

    /**
     * Get optimal flight speed for interception
     */
    public static float getOptimalSpeed(LivingEntity attacker, LivingEntity target) {
        if (!(target instanceof Player)) return 0.5f;

        Player player = (Player) target;
        if (!player.isFallFlying()) return 0.5f;

        Vec3 targetVel = player.getDeltaMovement();
        double targetSpeed = targetVel.length();

        // Fly slightly faster than target to intercept
        return Math.min(MAX_ELYTRA_SPEED, (float) (targetSpeed + 0.3));
    }

    /**
     * Analyze target flight pattern
     */
    public static FlightPattern analyzeFlightPattern(LivingEntity target) {
        if (!(target instanceof Player)) return FlightPattern.UNKNOWN;

        Player player = (Player) target;
        if (!player.isFallFlying()) return FlightPattern.UNKNOWN;

        Vec3 velocity = player.getDeltaMovement();
        double speed = velocity.length();
        double yVel = velocity.y;

        if (yVel < -0.3) {
            return FlightPattern.DIVING; // Spiking down
        } else if (yVel > 0.1) {
            return FlightPattern.CLIMBING; // Going up
        } else if (speed < 0.5) {
            return FlightPattern.GLIDING; // Slow glide
        } else {
            return FlightPattern.NORMAL; // Normal flight
        }
    }

    /**
     * Flight pattern enum
     */
    public enum FlightPattern {
        DIVING("Diving"), // Negative Y
        CLIMBING("Climbing"), // Positive Y
        GLIDING("Gliding"), // Low speed
        NORMAL("Normal"), // Steady flight
        UNKNOWN("Unknown");

        public final String name;

        FlightPattern(String name) {
            this.name = name;
        }
    }

    /**
     * Get interception ETA in ticks
     */
    public static int getInterceptionETA(LivingEntity attacker, LivingEntity target) {
        if (!(target instanceof Player)) return -1;

        Player player = (Player) target;
        if (!player.isFallFlying()) return -1;

        Vec3 currentPos = attacker.position();
        
        for (int ticks = 1; ticks <= 200; ticks++) {
            Vec3 interceptPoint = calculateInterceptPoint(target, ticks);
            double distance = currentPos.distanceTo(interceptPoint);

            if (distance < 2.0) {
                return ticks;
            }
        }

        return -1; // Cannot intercept
    }
}
