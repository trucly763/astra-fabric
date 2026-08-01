package shame.astra.modules.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.util.Mth;
import org.lwjgl.opengl.GL11;
import meteordevelopment.orbit.EventHandler;

public class TargetESP {
    private final Minecraft mc = Minecraft.getInstance();
    private LivingEntity currentTarget = null;
    private float targetAlpha = 0.0f;
    private float targetBoxAlpha = 0.0f;
    private long lastTargetTime = 0;

    @EventHandler
    public void onRender(RenderEvent event) {
        if (mc.player == null || mc.level == null) return;

        LivingEntity target = getTarget();
        if (target != null && target != mc.player && target.isAlive()) {
            currentTarget = target;
            lastTargetTime = System.currentTimeMillis();
            targetAlpha = Math.min(1.0f, targetAlpha + 0.1f);
        } else {
            targetAlpha = Math.max(0.0f, targetAlpha - 0.05f);
        }

        if (targetAlpha > 0.01f && currentTarget != null && currentTarget.isAlive()) {
            renderTargetBox(currentTarget, event.matrixStack, event.partialTicks);
            renderTargetInfo(currentTarget, event.matrixStack, event.partialTicks);
        }
    }

    private LivingEntity getTarget() {
        LivingEntity closestTarget = null;
        double closestDist = Double.MAX_VALUE;

        for (LivingEntity entity : mc.level.getEntitiesOfClass(
                LivingEntity.class,
                mc.player.getBoundingBox().inflate(50))) {
            
            if (entity == mc.player || !entity.isAlive()) continue;
            if (entity instanceof Player && isTeammate((Player) entity)) continue;

            double dist = mc.player.distanceTo(entity);
            if (dist < closestDist) {
                closestDist = dist;
                closestTarget = entity;
            }
        }

        return closestTarget;
    }

    private boolean isTeammate(Player player) {
        // Team color check - implement your team logic here
        return false;
    }

    private void renderTargetBox(LivingEntity target, PoseStack ms, float partialTicks) {
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glLineWidth(2.0f);

        var camera = mc.gameRenderer.getMainCamera();
        double camX = camera.getPosition().x;
        double camY = camera.getPosition().y;
        double camZ = camera.getPosition().z;

        // Interpolate position
        double x = Mth.lerp(partialTicks, target.xOld, target.getX()) - camX;
        double y = Mth.lerp(partialTicks, target.yOld, target.getY()) - camY;
        double z = Mth.lerp(partialTicks, target.zOld, target.getZ()) - camZ;

        float width = target.getBbWidth();
        float height = target.getBbHeight();

        ms.pushPose();
        ms.translate(x - width / 2, y, z - width / 2);

        // Box colors - red for players, yellow for mobs
        int colorR = 255;
        int colorG = target instanceof Player ? 50 : 200;
        int colorB = 50;
        int alpha = (int) (255 * targetAlpha * 0.7f);

        drawBox(ms, 0, 0, 0, width, height, colorR, colorG, colorB, alpha);

        // Head box
        drawBox(ms, 0, height - 0.5f, 0, width, 0.5f, colorR, colorG, colorB, (int) (alpha * 0.8f));

        ms.popPose();

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glLineWidth(1.0f);
    }

    private void renderTargetInfo(LivingEntity target, PoseStack ms, float partialTicks) {
        var camera = mc.gameRenderer.getMainCamera();
        double camX = camera.getPosition().x;
        double camY = camera.getPosition().y;
        double camZ = camera.getPosition().z;

        double x = Mth.lerp(partialTicks, target.xOld, target.getX()) - camX;
        double y = Mth.lerp(partialTicks, target.yOld, target.getY()) - camY;
        double z = Mth.lerp(partialTicks, target.zOld, target.getZ()) - camZ;

        ms.pushPose();
        ms.translate(x, y + target.getBbHeight() + 0.5, z);

        // Face camera
        ms.mulPose(mc.gameRenderer.getMainCamera().rotation());

        // Render name
        String name = target.getDisplayName().getString();
        int textColor = 0xFF00FF00; // Green
        
        if (target instanceof Player) {
            textColor = 0xFFFF5050; // Red for players
        }

        int textWidth = mc.font.width(name);
        ms.translate(-textWidth / 2.0, 0, 0);
        
        mc.font.drawInBatch(name, 0, 0, textColor, false,
                ms.last().pose(),
                mc.renderBuffers().bufferSource(),
                net.minecraft.client.renderer.texture.TextureAtlasSprite.MISSING_PIXEL,
                15728880, 0, false);

        // Render health
        if (target instanceof LivingEntity living) {
            float health = living.getHealth();
            float maxHealth = living.getMaxHealth();
            String healthStr = String.format("%.1f/%.1f", health, maxHealth);
            
            int healthColor = (int) (health > maxHealth / 2 ? 0xFF00FF00 : 0xFFFFFF00);
            if (health < maxHealth / 4) healthColor = 0xFFFF0000;

            int healthWidth = mc.font.width(healthStr);
            ms.translate(textWidth / 2.0 - healthWidth / 2.0, 12, 0);
            
            mc.font.drawInBatch(healthStr, 0, 0, healthColor, false,
                    ms.last().pose(),
                    mc.renderBuffers().bufferSource(),
                    net.minecraft.client.renderer.texture.TextureAtlasSprite.MISSING_PIXEL,
                    15728880, 0, false);
        }

        // Render distance
        double dist = mc.player.distanceTo(target);
        String distStr = String.format("%.1fm", dist);
        int distWidth = mc.font.width(distStr);
        ms.translate(healthWidth / 2.0 - distWidth / 2.0, 12, 0);
        
        int distColor = dist < 10 ? 0xFFFF0000 : 0xFFFFFF00;
        mc.font.drawInBatch(distStr, 0, 0, distColor, false,
                ms.last().pose(),
                mc.renderBuffers().bufferSource(),
                net.minecraft.client.renderer.texture.TextureAtlasSprite.MISSING_PIXEL,
                15728880, 0, false);

        mc.renderBuffers().bufferSource().endBatch();
        ms.popPose();
    }

    private void drawBox(PoseStack ms, float x, float y, float z, float width, float height, 
                        int r, int g, int b, int alpha) {
        GL11.glBegin(GL11.GL_LINE_LOOP);
        float ar = r / 255.0f;
        float ag = g / 255.0f;
        float ab = b / 255.0f;
        float aa = alpha / 255.0f;

        GL11.glColor4f(ar, ag, ab, aa);

        // Front face
        GL11.glVertex3f(x, y, z);
        GL11.glVertex3f(x + width, y, z);
        GL11.glVertex3f(x + width, y + height, z);
        GL11.glVertex3f(x, y + height, z);

        GL11.glEnd();

        GL11.glBegin(GL11.GL_LINE_LOOP);
        // Back face
        GL11.glVertex3f(x, y, z + width);
        GL11.glVertex3f(x + width, y, z + width);
        GL11.glVertex3f(x + width, y + height, z + width);
        GL11.glVertex3f(x, y + height, z + width);
        GL11.glEnd();

        // Connect edges
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3f(x, y, z);
        GL11.glVertex3f(x, y, z + width);
        GL11.glVertex3f(x + width, y, z);
        GL11.glVertex3f(x + width, y, z + width);
        GL11.glVertex3f(x + width, y + height, z);
        GL11.glVertex3f(x + width, y + height, z + width);
        GL11.glVertex3f(x, y + height, z);
        GL11.glVertex3f(x, y + height, z + width);
        GL11.glEnd();

        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
