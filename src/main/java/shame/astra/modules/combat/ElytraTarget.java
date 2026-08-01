package shame.astra.modules.combat;

import com.mojang.blaze3d.vertex.PoseStack;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.util.Mth;
import org.lwjgl.opengl.GL11;
import java.util.*;

public class ElytraTarget {
    private final Minecraft mc = Minecraft.getInstance();
    private LivingEntity elytraTarget = null;
    private float targetAlpha = 0.0f;
    private long lastTargetTime = 0;
    private float rotationYaw = 0.0f;
    private float rotationPitch = 0.0f;
    
    private final Map<LivingEntity, List<ElytraTrailPoint>> trailMap = new HashMap<>();
    private static final int MAX_TRAIL_POINTS = 60;
    private static final float TRAIL_UPDATE_RATE = 0.05f;

    @EventHandler
    public void onRender(RenderEvent event) {
        if (mc.player == null || mc.level == null) return;

        LivingEntity target = findElytraTarget();
        if (target != null && target.isAlive() && isElytraFlying(target)) {
            elytraTarget = target;
            lastTargetTime = System.currentTimeMillis();
            targetAlpha = Math.min(1.0f, targetAlpha + 0.15f);
        } else {
            targetAlpha = Math.max(0.0f, targetAlpha - 0.08f);
        }

        if (targetAlpha > 0.01f && elytraTarget != null && elytraTarget.isAlive()) {
            updateTrail(elytraTarget);
            renderElytraTarget(elytraTarget, event.matrixStack, event.partialTicks);
            renderTrailEffect(elytraTarget, event.matrixStack, event.partialTicks);
        }
    }

    private LivingEntity findElytraTarget() {
        LivingEntity closestTarget = null;
        double closestDist = Double.MAX_VALUE;

        for (LivingEntity entity : mc.level.getEntitiesOfClass(
                LivingEntity.class,
                mc.player.getBoundingBox().inflate(100))) {
            
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

    private void updateTrail(LivingEntity target) {
        List<ElytraTrailPoint> trail = trailMap.computeIfAbsent(target, k -> new ArrayList<>());

        ElytraTrailPoint point = new ElytraTrailPoint(
                target.getX(),
                target.getY(),
                target.getZ(),
                System.currentTimeMillis()
        );

        trail.add(0, point);

        while (trail.size() > MAX_TRAIL_POINTS) {
            trail.remove(trail.size() - 1);
        }
    }

    private void renderElytraTarget(LivingEntity target, PoseStack ms, float partialTicks) {
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        var camera = mc.gameRenderer.getMainCamera();
        double camX = camera.getPosition().x;
        double camY = camera.getPosition().y;
        double camZ = camera.getPosition().z;

        double x = Mth.lerp(partialTicks, target.xOld, target.getX()) - camX;
        double y = Mth.lerp(partialTicks, target.yOld, target.getY()) - camY;
        double z = Mth.lerp(partialTicks, target.zOld, target.getZ()) - camZ;

        // Update rotation to track target
        updateTargetRotation(x, y + target.getBbHeight() / 2, z);

        ms.pushPose();
        ms.translate(x, y, z);

        // Draw rotating rings
        drawElytraRings(ms, partialTicks);

        // Draw target box
        drawTargetBox(ms, target);

        ms.popPose();

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
    }

    private void updateTargetRotation(double dx, double dy, double dz) {
        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
        
        rotationYaw = (float) Math.toDegrees(Math.atan2(dz, dx));
        rotationPitch = (float) Math.toDegrees(Math.atan2(dy, Math.sqrt(dx * dx + dz * dz)));
    }

    private void drawElytraRings(PoseStack ms, float partialTicks) {
        long time = System.currentTimeMillis();
        float timeScale = (time % 3000) / 3000.0f;

        // Ring 1 - XZ plane
        drawRotatingRing(ms, 1.5f, 0, timeScale, 0xFF4080FF);

        // Ring 2 - XY plane  
        ms.pushPose();
        ms.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(90));
        drawRotatingRing(ms, 1.2f, 45, timeScale + 0.33f, 0xFF80FFFF);
        ms.popPose();

        // Ring 3 - YZ plane
        ms.pushPose();
        ms.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90));
        drawRotatingRing(ms, 0.9f, 90, timeScale + 0.66f, 0xFFFF80FF);
        ms.popPose();
    }

    private void drawRotatingRing(PoseStack ms, float radius, float rotation, float timeProgress, int color) {
        GL11.glBegin(GL11.GL_LINE_STRIP);
        
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;
        float a = targetAlpha * 0.8f;

        int segments = 32;
        for (int i = 0; i <= segments; i++) {
            float angle = (float) (2 * Math.PI * i / segments);
            float rotatedAngle = angle + (float) Math.toRadians(rotation);
            
            float px = (float) Math.cos(rotatedAngle) * radius;
            float py = (float) Math.sin(rotatedAngle) * radius;

            GL11.glColor4f(r, g, b, a);
            GL11.glVertex3f(px, py, 0);
        }

        GL11.glEnd();
    }

    private void drawTargetBox(PoseStack ms, LivingEntity target) {
        float width = target.getBbWidth();
        float height = target.getBbHeight();

        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glColor4f(0.2f, 0.5f, 1.0f, targetAlpha);

        // Front face
        GL11.glVertex3f(-width / 2, 0, 0);
        GL11.glVertex3f(width / 2, 0, 0);
        GL11.glVertex3f(width / 2, height, 0);
        GL11.glVertex3f(-width / 2, height, 0);

        GL11.glEnd();

        GL11.glBegin(GL11.GL_LINE_LOOP);
        // Back face
        GL11.glVertex3f(-width / 2, 0, -width);
        GL11.glVertex3f(width / 2, 0, -width);
        GL11.glVertex3f(width / 2, height, -width);
        GL11.glVertex3f(-width / 2, height, -width);
        GL11.glEnd();

        // Connect edges
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3f(-width / 2, 0, 0);
        GL11.glVertex3f(-width / 2, 0, -width);
        GL11.glVertex3f(width / 2, 0, 0);
        GL11.glVertex3f(width / 2, 0, -width);
        GL11.glVertex3f(width / 2, height, 0);
        GL11.glVertex3f(width / 2, height, -width);
        GL11.glVertex3f(-width / 2, height, 0);
        GL11.glVertex3f(-width / 2, height, -width);
        GL11.glEnd();

        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private void renderTrailEffect(LivingEntity target, PoseStack ms, float partialTicks) {
        List<ElytraTrailPoint> trail = trailMap.get(target);
        if (trail == null || trail.isEmpty()) return;

        var camera = mc.gameRenderer.getMainCamera();
        double camX = camera.getPosition().x;
        double camY = camera.getPosition().y;
        double camZ = camera.getPosition().z;

        GL11.glBegin(GL11.GL_LINE_STRIP);

        for (int i = 0; i < trail.size(); i++) {
            ElytraTrailPoint point = trail.get(i);
            float progress = i / (float) trail.size();
            float alpha = targetAlpha * (1.0f - progress) * 0.6f;

            double x = point.x - camX;
            double y = point.y - camY;
            double z = point.z - camZ;

            GL11.glColor4f(0.4f, 0.8f, 1.0f, alpha);
            GL11.glVertex3d(x, y, z);
        }

        GL11.glEnd();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private static class ElytraTrailPoint {
        double x, y, z;
        long timestamp;

        ElytraTrailPoint(double x, double y, double z, long timestamp) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.timestamp = timestamp;
        }
    }

    public void clearTargets() {
        elytraTarget = null;
        trailMap.clear();
    }
}
