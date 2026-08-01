package shame.astra.modules.combat;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.Mth;

public class AutoElytraInterceptor {
    private final Minecraft mc = Minecraft.getInstance();
    private LivingEntity currentTarget = null;
    private int interceptTicks = 0;
    private boolean isIntercepting = false;

    // Settings
    private float interceptTolerance = 2.0f; // blocks
    private float rotationSpeed = 10.0f; // degrees per tick
    private int predictAhead = 25; // ticks to predict
    private boolean autoRotate = true;
    private boolean autoMove = true;
    private boolean showDebug = false;

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (mc.player == null || mc.level == null) return;

        // Find elytra target
        currentTarget = findElytraTarget();
        
        if (currentTarget != null && currentTarget.isAlive() && isElytraFlying(currentTarget)) {
            handleInterception();
        } else {
            isIntercepting = false;
            interceptTicks = 0;
        }
    }

    private LivingEntity findElytraTarget() {
        LivingEntity closestTarget = null;
        double closestDist = Double.MAX_VALUE;

        for (LivingEntity entity : mc.level.getEntitiesOfClass(
                LivingEntity.class,
                mc.player.getBoundingBox().inflate(120))) {
            
            if (entity == mc.player || !entity.isAlive()) continue;
            if (!(entity instanceof Player)) continue;
            if (!isElytraFlying(entity)) continue;

            double dist = mc.player.distanceTo(entity);
            if (dist < closestDist) {
                closestDist = dist;
                closestTarget = entity;
            }
        }

        return closestTarget;
    }

    private boolean isElytraFlying(LivingEntity entity) {
        if (!(entity instanceof Player)) return false;
        Player player = (Player) entity;
        return player.isFallFlying() && player.isAlive();
    }

    private void handleInterception() {
        // Calculate intercept point
        Vec3 interceptPoint = ElytraInterceptor.calculateBestInterceptPosition(mc.player, currentTarget);

        // Get flight pattern
        ElytraInterceptor.FlightPattern pattern = ElytraInterceptor.analyzeFlightPattern(currentTarget);

        // Auto rotation
        if (autoRotate) {
            float[] targetRotation = ElytraInterceptor.getInterceptRotation(mc.player, interceptPoint);
            applyRotation(targetRotation);
        }

        // Auto movement
        if (autoMove) {
            float[] movementInput = ElytraInterceptor.getMovementInput(mc.player, interceptPoint);
            applyMovement(movementInput);
        }

        // Check if on intercept course
        if (ElytraInterceptor.isOnInterceptCourse(mc.player, currentTarget, interceptTolerance)) {
            isIntercepting = true;
            interceptTicks++;
        } else {
            isIntercepting = false;
            interceptTicks = 0;
        }

        // Debug info
        if (showDebug) {
            int eta = ElytraInterceptor.getInterceptionETA(mc.player, currentTarget);
            double distance = mc.player.distanceTo(currentTarget);
            String debug = String.format("Target: %s | Distance: %.1f | ETA: %d | Pattern: %s",
                    currentTarget.getName().getString(),
                    distance,
                    eta,
                    pattern.name);
            // Log or display debug info
        }
    }

    private void applyRotation(float[] targetRotation) {
        float currentYaw = mc.player.getYRot();
        float currentPitch = mc.player.getXRot();

        float targetYaw = targetRotation[0];
        float targetPitch = targetRotation[1];

        // Smooth rotation
        float newYaw = smoothAngle(currentYaw, targetYaw, rotationSpeed);
        float newPitch = Mth.clamp(
                smoothAngle(currentPitch, targetPitch, rotationSpeed),
                -90, 90
        );

        mc.player.setYRot(newYaw);
        mc.player.setXRot(newPitch);
    }

    private void applyMovement(float[] movement) {
        if (movement == null || movement.length < 4) return;

        // Apply elytra movement
        float forward = movement[0];
        float strafe = movement[1];
        float up = movement[2];

        boolean moveForward = forward > 0.5;
        boolean moveBackward = forward < -0.5;
        boolean moveRight = strafe > 0.5;
        boolean moveLeft = strafe < -0.5;
        boolean moveUp = up > 0.5;

        // Set movement keys
        if (mc.options != null) {
            mc.options.keyUp.setPressed(moveUp || moveForward);
            mc.options.keyDown.setPressed(!moveUp && moveBackward);
            mc.options.keyLeft.setPressed(moveLeft);
            mc.options.keyRight.setPressed(moveRight);
        }
    }

    private float smoothAngle(float current, float target, float speed) {
        float diff = target - current;

        // Normalize to -180 to 180
        while (diff > 180) diff -= 360;
        while (diff < -180) diff += 360;

        if (Math.abs(diff) < speed) {
            return target;
        }

        return current + (diff > 0 ? speed : -speed);
    }

    // Getters/Setters for settings
    public void setInterceptTolerance(float tolerance) {
        this.interceptTolerance = tolerance;
    }

    public void setRotationSpeed(float speed) {
        this.rotationSpeed = speed;
    }

    public void setPredictAhead(int ticks) {
        this.predictAhead = ticks;
    }

    public void setAutoRotate(boolean auto) {
        this.autoRotate = auto;
    }

    public void setAutoMove(boolean auto) {
        this.autoMove = auto;
    }

    public void setShowDebug(boolean show) {
        this.showDebug = show;
    }

    public boolean isIntercepting() {
        return isIntercepting;
    }

    public int getInterceptTicks() {
        return interceptTicks;
    }

    public LivingEntity getCurrentTarget() {
        return currentTarget;
    }
}
