package shame.astra.modules.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.opengl.GL11;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NursultanSoulsRenderer {
    private final Minecraft mc = Minecraft.getInstance();
    private int lastHurtTime = 0;
    private long lastHurtTimeMs = 0;
    
    private final Map<LivingEntity, List<Vec3>> phantomTrail1 = new HashMap<>();
    private final Map<LivingEntity, List<Vec3>> phantomTrail2 = new HashMap<>();
    private final Map<LivingEntity, List<Vec3>> phantomTrail3 = new HashMap<>();

    public void renderNursultanSouls(LivingEntity target, PoseStack ms, float partialTicks, float alpha) {
        if (target == null || !target.isAlive()) return;

        final float baseSizePx = 0.19f;
        final float radius = 0.7f;
        final int trailLength = 55;

        // Get camera - 1.21.4 way
        var camera = mc.gameRenderer.getMainCamera();
        final double camX = camera.getPosition().x;
        final double camY = camera.getPosition().y;
        final double camZ = camera.getPosition().z;

        // Lerp position - 1.21.4 way
        final double bx = Mth.lerp(partialTicks, target.xOld, target.getX()) - camX;
        final double by = Mth.lerp(partialTicks, target.yOld, target.getY()) - camY;
        final double bz = Mth.lerp(partialTicks, target.zOld, target.getZ()) - camZ;

        // Hurt state tracking
        boolean isHurt = target.hurtTime > 0;
        float baseSpeed = 1.0f;
        float hurtSpeedBonus = 0f;

        if (isHurt) {
            hurtSpeedBonus = 0.2f;
            lastHurtTime = target.hurtTime;
            lastHurtTimeMs = System.currentTimeMillis();
        } else if (lastHurtTime > 0) {
            long timeSinceHurt = System.currentTimeMillis() - lastHurtTimeMs;
            if (timeSinceHurt < 300) {
                hurtSpeedBonus = 0.2f * (1f - timeSinceHurt / 300f);
            } else {
                lastHurtTime = 0;
                hurtSpeedBonus = 0f;
            }
        }

        float currentSpeedMultiplier = baseSpeed + hurtSpeedBonus;
        final double t = System.currentTimeMillis() / (350.0 / currentSpeedMultiplier);
        final float aPC = alpha;

        float hurtFactor = Mth.clamp(target.hurtTime / 10f, 0f, 1f);
        long timeSinceHurt = System.currentTimeMillis() - lastHurtTimeMs;
        float afterHurtRed = 0f;

        if (timeSinceHurt < 200 && lastHurtTime > 0) {
            afterHurtRed = 1f - (timeSinceHurt / 200f);
        }
        hurtFactor = Math.max(hurtFactor, afterHurtRed);

        final int red = ColorUtil.getColor(255, 80, 80, (int) (255 * aPC));
        final float hurtPC = (float) Math.sin(target.hurtTime * (18F * Math.PI / 180F));

        final float[] baseHeights = {
                target.getBbHeight() * 0.85f,
                target.getBbHeight() * 0.55f,
                target.getBbHeight() * 0.25f
        };

        final double[] angles = {
                Math.toRadians(0),
                Math.toRadians(120),
                Math.toRadians(240)
        };

        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        @SuppressWarnings("unchecked")
        List<Vec3>[] trails = new List[]{
                phantomTrail1.computeIfAbsent(target, k -> new ArrayList<>()),
                phantomTrail2.computeIfAbsent(target, k -> new ArrayList<>()),
                phantomTrail3.computeIfAbsent(target, k -> new ArrayList<>())
        };

        for (int k = 0; k < 3; k++) {
            float verticalAmp = 0.16f + (hurtFactor * 0.08f);
            double verticalMove = Math.sin(t * 1.5 + k * 2.0) * verticalAmp;
            double height = baseHeights[k] + verticalMove;

            float rotBaseSpeed = 1.3f;
            float rotBonus = hurtSpeedBonus * 0.5f;
            double rotSpeed = t * (rotBaseSpeed + rotBonus);
            double angle = angles[k] + rotSpeed;

            double baseX = Math.cos(angle) * radius;
            double baseZ = Math.sin(angle) * radius;

            Vec3 currentWorldPos = new Vec3(
                    bx + baseX + camX,
                    by + height + camY,
                    bz + baseZ + camZ
            );

            List<Vec3> trail = trails[k];
            trail.add(0, currentWorldPos);

            while (trail.size() > trailLength) {
                trail.remove(trail.size() - 1);
            }

            for (int j = 0; j < trail.size(); j++) {
                Vec3 pos = trail.get(j);
                float progress = j / (float) trailLength;
                float alphaMul = (float) Math.pow(1.0f - progress, 1.5);
                float sizeFactor = 1.0f - progress * 0.5f;
                float dynSize = baseSizePx * sizeFactor;
                int dynAlpha = (int) (255 * aPC * alphaMul);

                int baseColor;
                if (hurtFactor > 0.05f) {
                    int r = 255;
                    int g = 80 + (int) (80 * (1 - hurtFactor));
                    int b = 80 + (int) (80 * (1 - hurtFactor));
                    baseColor = ColorUtil.getColor(r, g, b, dynAlpha);
                } else {
                    baseColor = ColorUtil.fade(j * 3 + k * 40);
                    baseColor = ColorUtil.replAlpha(baseColor, dynAlpha);
                }

                int color = ColorUtil.replAlpha(
                        ColorUtil.overCol(baseColor, red, hurtPC * hurtFactor),
                        Mth.clamp(dynAlpha, 0, 255)
                );

                ms.pushPose();
                ms.translate(pos.x - camX, pos.y - camY, pos.z - camZ);
                
                // 1.21.4 camera rotation
                ms.mulPose(camera.rotation());

                // Glow effect
                RenderUtil.bindTexture(new net.minecraft.resources.ResourceLocation("astra", "texture/glow.png"));
                float glowSize = dynSize * (1.3f + hurtFactor * 0.3f);
                int haloAlpha = (int) (dynAlpha * (0.4f + hurtFactor * 0.2f));
                int haloColor = ColorUtil.replAlpha(color, haloAlpha);

                RectUtil.drawRect(ms,
                        -glowSize, -glowSize,
                        glowSize * 2, glowSize * 2,
                        haloColor, haloColor, haloColor, haloColor,
                        true, true);

                // Main sprite
                RectUtil.drawRect(ms,
                        -dynSize, -dynSize,
                        dynSize * 2, dynSize * 2,
                        color, color, color, color,
                        true, true);

                ms.popPose();
            }
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
    }

    public void updateHurtState(LivingEntity target) {
        if (target.hurtTime > lastHurtTime) {
            lastHurtTime = target.hurtTime;
            lastHurtTimeMs = System.currentTimeMillis();
        }
    }

    public void clearTrails(LivingEntity target) {
        phantomTrail1.remove(target);
        phantomTrail2.remove(target);
        phantomTrail3.remove(target);
    }

    public void clearAllTrails() {
        phantomTrail1.clear();
        phantomTrail2.clear();
        phantomTrail3.clear();
    }
}
