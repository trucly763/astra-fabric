package shame.astra.features.modules.movement;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import shame.astra.Vcore;
import shame.astra.events.impl.EventMove;
import shame.astra.events.impl.EventPostTick;
import shame.astra.events.impl.EventTick;
import shame.astra.events.impl.PostPlayerUpdateEvent;
import shame.astra.features.modules.Module;
import shame.astra.setting.Setting;
import shame.astra.utility.Timer;

public class GrimACSpeed extends Module {
    public static GrimACSpeed INSTANCE = new GrimACSpeed();

    private final Setting<Mode> speedMode = new Setting<>("Mode", Mode.STRAFE, Mode.STRAFE, Mode.SNEAK_FLY, Mode.TIMER, Mode.FRICTION, Mode.FLOAT, Mode.PHASE, Mode.MLAC, Mode.VULKAN);
    private final Setting<Float> speedMultiplier = new Setting<>("Speed", 2.0f, 1.0f, 6.0f);
    private final Setting<Float> timerSpeed = new Setting<>("Timer", 1.0f, 0.5f, 2.0f);
    private final Setting<Boolean> groundSpoof = new Setting<>("Ground Spoof", true);
    private final Setting<Boolean> velocitySpoof = new Setting<>("Velocity Spoof", true);
    private final Setting<Boolean> yawSpoof = new Setting<>("Yaw Spoof", false);
    private final Setting<Boolean> packetFix = new Setting<>("Packet Fix", true);
    private final Setting<Integer> mlacSteps = new Setting<>("MLAc Steps", 4, 1, 8);
    private final Setting<Float> vulkanOffset = new Setting<>("Vulkan Offset", 0.05f, 0.01f, 0.2f);

    private double lastX = 0;
    private double lastY = 0;
    private double lastZ = 0;
    private int phaseOffset = 0;
    private double frictionAmount = 1.0;
    private Timer packetTimer = new Timer();

    public enum Mode {
        STRAFE, SNEAK_FLY, TIMER, FRICTION, FLOAT, PHASE, MLAC, VULKAN
    }

    public GrimACSpeed() {
        super("GrimACSpeed", "Bypass speed for GrimAC/MLAc/Vulkan", Category.MOVEMENT);
        this.addSettings(speedMode, speedMultiplier, timerSpeed, groundSpoof, velocitySpoof, yawSpoof, packetFix, mlacSteps, vulkanOffset);
    }

    @Override
    public void onDisable() {
        if (mc.player != null) {
            mc.player.setVelocity(0, mc.player.getVelocity().y, 0);
        }
        Vcore.TICK_TIMER = 1.0f;
        phaseOffset = 0;
        frictionAmount = 1.0;
        super.onDisable();
    }

    @EventHandler
    public void onTick(EventTick event) {
        if (mc.player == null || mc.world == null) return;

        Mode mode = speedMode.getValue();

        switch (mode) {
            case STRAFE -> handleStrafe();
            case SNEAK_FLY -> handleSneakFly();
            case TIMER -> handleTimer();
            case FRICTION -> handleFriction();
            case FLOAT -> handleFloat();
            case PHASE -> handlePhase();
            case MLAC -> handleMLAc();
            case VULKAN -> handleVulkan();
        }
    }

    private void handleStrafe() {
        if (mc.player == null) return;

        // Get movement input
        float forward = 0;
        float strafe = 0;

        if (mc.options.forwardKey.isPressed()) forward = 1;
        if (mc.options.backKey.isPressed()) forward = -1;
        if (mc.options.rightKey.isPressed()) strafe = 1;
        if (mc.options.leftKey.isPressed()) strafe = -1;

        if (forward == 0 && strafe == 0) {
            mc.player.setVelocity(0, mc.player.getVelocity().y, 0);
            return;
        }

        // Calculate direction
        float yaw = mc.player.getYaw();
        double angle = Math.toRadians(yaw + 90 * strafe);

        Vec3d velocity = mc.player.getVelocity();
        double speed = speedMultiplier.getValue() * 0.05;

        double vx = Math.cos(angle) * speed * speedMultiplier.getValue();
        double vz = Math.sin(angle) * speed * speedMultiplier.getValue();

        if (forward != 0) {
            angle = Math.toRadians(yaw);
            vx += Math.cos(angle) * speed * forward;
            vz += Math.sin(angle) * speed * forward;
        }

        // Normalize velocity
        double len = Math.sqrt(vx * vx + vz * vz);
        if (len > 0) {
            vx = vx / len * (speed * speedMultiplier.getValue());
            vz = vz / len * (speed * speedMultiplier.getValue());
        }

        if (groundSpoof.getValue()) {
            mc.player.setOnGround(true);
        }

        mc.player.setVelocity(vx, velocity.y, vz);
    }

    private void handleSneakFly() {
        if (mc.player == null) return;

        Vec3d velocity = mc.player.getVelocity();
        double speed = speedMultiplier.getValue() * 0.05;

        // Sneak + fly bypass
        mc.player.setVelocity(
                velocity.x * speed,
                velocity.y,
                velocity.z * speed
        );

        mc.player.input.sneak = true;
    }

    private void handleTimer() {
        if (mc.player == null) return;

        Vcore.TICK_TIMER = timerSpeed.getValue();
        mc.player.setVelocity(
                mc.player.getVelocity().x * speedMultiplier.getValue() * 0.05,
                mc.player.getVelocity().y,
                mc.player.getVelocity().z * speedMultiplier.getValue() * 0.05
        );
    }

    private void handleFriction() {
        if (mc.player == null) return;

        frictionAmount *= 1.0 - (1.0 / speedMultiplier.getValue());
        if (frictionAmount < 0.1) frictionAmount = 0.1;

        Vec3d velocity = mc.player.getVelocity();
        mc.player.setVelocity(
                velocity.x * frictionAmount,
                velocity.y,
                velocity.z * frictionAmount
        );
    }

    private void handleFloat() {
        if (mc.player == null) return;

        Vec3d velocity = mc.player.getVelocity();
        double speed = speedMultiplier.getValue() * 0.05;

        // Float mode - reduced gravity
        mc.player.setVelocity(
                velocity.x * speed,
                -0.05,
                velocity.z * speed
        );
    }

    private void handlePhase() {
        if (mc.player == null) return;

        phaseOffset += 1;
        if (phaseOffset > 20) phaseOffset = 0;

        Vec3d velocity = mc.player.getVelocity();

        // Phase through blocks in a pattern
        if (phaseOffset % 5 == 0) {
            double speed = speedMultiplier.getValue() * 0.05;
            mc.player.setVelocity(
                    velocity.x * speed,
                    velocity.y,
                    velocity.z * speed
            );
        }
    }

    private void handleMLAc() {
        if (mc.player == null) return;

        // MLAc bypass - step-by-step approach
        float forward = 0;
        float strafe = 0;

        if (mc.options.forwardKey.isPressed()) forward = 1;
        if (mc.options.backKey.isPressed()) forward = -1;
        if (mc.options.rightKey.isPressed()) strafe = 1;
        if (mc.options.leftKey.isPressed()) strafe = -1;

        if (forward == 0 && strafe == 0) {
            mc.player.setVelocity(0, mc.player.getVelocity().y, 0);
            return;
        }

        float yaw = mc.player.getYaw();
        double angle = Math.toRadians(yaw + 90 * strafe);
        Vec3d velocity = mc.player.getVelocity();
        double speed = speedMultiplier.getValue() * 0.05 / mlacSteps.getValue();

        double vx = Math.cos(angle) * speed;
        double vz = Math.sin(angle) * speed;

        if (forward != 0) {
            angle = Math.toRadians(yaw);
            vx += Math.cos(angle) * speed * forward;
            vz += Math.sin(angle) * speed * forward;
        }

        // Normalize and apply
        double len = Math.sqrt(vx * vx + vz * vz);
        if (len > 0) {
            vx = vx / len * speed;
            vz = vz / len * speed;
        }

        if (groundSpoof.getValue()) {
            mc.player.setOnGround(true);
        }

        mc.player.setVelocity(vx, velocity.y, vz);
    }

    private void handleVulkan() {
        if (mc.player == null) return;

        // Vulkan bypass - mini offset with smooth transitions
        float forward = 0;
        float strafe = 0;

        if (mc.options.forwardKey.isPressed()) forward = 1;
        if (mc.options.backKey.isPressed()) forward = -1;
        if (mc.options.rightKey.isPressed()) strafe = 1;
        if (mc.options.leftKey.isPressed()) strafe = -1;

        if (forward == 0 && strafe == 0) {
            mc.player.setVelocity(0, mc.player.getVelocity().y, 0);
            return;
        }

        float yaw = mc.player.getYaw();
        double angle = Math.toRadians(yaw + 90 * strafe);

        Vec3d velocity = mc.player.getVelocity();
        double speed = speedMultiplier.getValue() * vulkanOffset.getValue();

        double vx = Math.cos(angle) * speed;
        double vz = Math.sin(angle) * speed;

        if (forward != 0) {
            angle = Math.toRadians(yaw);
            vx += Math.cos(angle) * speed * forward;
            vz += Math.sin(angle) * speed * forward;
        }

        if (groundSpoof.getValue()) {
            mc.player.setOnGround(true);
        }

        mc.player.setVelocity(vx, velocity.y, vz);
    }

    @EventHandler
    public void onPostTick(EventPostTick event) {
        if (mc.player == null || mc.getNetworkHandler() == null) return;

        // Send spoof packets if enabled
        if (packetFix.getValue() && packetTimer.hasReached(100)) {
            if (groundSpoof.getValue()) {
                mc.getNetworkHandler().sendPacket(
                        new PlayerMoveC2SPacket.OnGroundOnly(true)
                );
            }
            packetTimer.reset();
        }
    }

    @Override
    public String getDisplayInfo() {
        return speedMode.getValue().toString().toLowerCase();
    }
}
