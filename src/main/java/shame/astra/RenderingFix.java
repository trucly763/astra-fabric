package shame.astra.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.lwjgl.opengl.GL11;
import com.mojang.math.Axis;

public class RenderingFix {

    /**
     * Fixed rendering code for 1.21.4 - Celka Skeed 3D effect
     */
    public static void renderCelkaSkeed3D2(
            LivingEntity target,
            PoseStack ms,
            float partialTicks,
            boolean fadeEnabled,
            float alpha,
            int baseColor) {
        
        final float speed = 4;
        final float verticalSpeed = 2;
        final float baseSizePx = 0.5F;
        final int brightness = 1;
        final int trailLength = 35;
        final float radiusConst = 0.65F;
        final float upperPosition = 1.6F;
        final float lowerPosition = 0.5F;

        // Get camera position - 1.21.4 way
        var camera = net.minecraft.client.Camera.getInstance();
        final double camX = camera.getPosition().x;
        final double camY = camera.getPosition().y;
        final double camZ = camera.getPosition().z;

        // Lerp entity position - 1.21.4 way
        var prevPos = target.position().lerp(target.getPosition(), partialTicks);
        final double bx = prevPos.x - camX;
        final double by = prevPos.y - camY;
        final double bz = prevPos.z - camZ;

        // Animation timing
        final double t = System.currentTimeMillis() / (700.0 / speed);
        final double tv = System.currentTimeMillis() / (950.0 / speed);

        final float aPC = alpha;
        final float hurtPC = (float) Math.sin(target.hurtTime * (18F * Math.PI / 180F));
        final int red = ColorUtil.getColor(190, 100, 100, (int) (255 * aPC));

        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        float radius = radiusConst;

        final float midPosition = (upperPosition + lowerPosition) / 2.0f;
        final float[] fixedY = new float[]{upperPosition, midPosition, lowerPosition};

        for (int k = 0; k < 3; k++) {
            for (int j = 0; j < trailLength; j++) {
                float kf = j / (float) trailLength;
                float ease = 1.0f - kf;
                ease *= ease;
                ease *= ease;

                double tj = t - j * 0.05;
                double tvj = tv + j * 0.2;

                double tiltAngle = Math.toRadians(5 + k);

                float fo4s = 0.1f;
                double cyc = (Math.sin(tvj) + 1.0) * 0.6F + Math.sin(tiltAngle) + Math.sin(-j * 0.06F) * 0.3F;

                double baseAngle = Math.toRadians(k * 120.0 + (tj * 50.0 % 360.0));
                double offX = Math.cos(baseAngle) * radius;
                double offZ = Math.sin(baseAngle) * radius;

                double breathRange = 0.5F;
                double slowDriftAmp = 0.08;
                
                double drift = Math.sin(t * 0.18 + k * (2.0) + j * 0.12) * slowDriftAmp * (1.0 - kf);
                double offY = fixedY[k] + (cyc - 0.5) * breathRange;

                kf = j / (float) trailLength;
                float sizeFactor = 1.0f - (kf * 0.7f);
                float dynSize = baseSizePx * sizeFactor;
                int dynAlpha = (int) (255 * aPC);

                int color = ColorUtil.replAlpha(ColorUtil.overCol(
                        ColorUtil.multAlpha(ColorUtil.fade(0), aPC),
                        red,
                        hurtPC
                ), Mth.clamp(dynAlpha, 0, 255));
                
                int color2 = ColorUtil.replAlpha(ColorUtil.overCol(
                        ColorUtil.multAlpha(ColorUtil.fade(90), aPC),
                        red,
                        hurtPC
                ), Mth.clamp(dynAlpha, 0, 255));
                
                int color3 = ColorUtil.replAlpha(ColorUtil.overCol(
                        ColorUtil.multAlpha(ColorUtil.fade(180), aPC),
                        red,
                        hurtPC
                ), Mth.clamp(dynAlpha, 0, 255));
                
                int color4 = ColorUtil.replAlpha(ColorUtil.overCol(
                        ColorUtil.multAlpha(ColorUtil.fade(270), aPC),
                        red,
                        hurtPC
                ), Mth.clamp(dynAlpha, 0, 255));

                ms.pushPose();
                ms.translate(bx + offX, by + offY, bz + offZ);
                ms.translate(-dynSize / 12f, -dynSize / 12f, 0f);
                
                // 1.21.4 rotation method
                ms.mulPose(Axis.ZP.rotationDegrees(camera.getYRot()));
                ms.mulPose(Axis.XP.rotationDegrees(camera.getXRot()));
                
                ms.translate(dynSize / 12f, dynSize / 12f, 0f);
                
                RenderUtil.bindTexture(new net.minecraft.resources.ResourceLocation("astra", "texture/glow.png"));
                RectUtil.drawRect(ms, -dynSize / 2f, -dynSize / 12f,
                        dynSize, dynSize,
                        color, color2, color3, color4, true, true);

                ms.popPose();
            }
        }

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
    }

    /**
     * Render Nursultan souls effect
     */
    public static void renderNursultanSouls(
            LivingEntity target,
            PoseStack ms,
            float partialTicks,
            float alpha) {
        
        var camera = net.minecraft.client.Camera.getInstance();
        final double camX = camera.getPosition().x;
        final double camY = camera.getPosition().y;
        final double camZ = camera.getPosition().z;

        var pos = target.position().lerp(target.getPosition(), partialTicks);
        final double bx = pos.x - camX;
        final double by = pos.y - camY;
        final double bz = pos.z - camZ;

        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        for (int i = 0; i < 8; i++) {
            double angle = (System.currentTimeMillis() + i * 45) / 500.0 % 360.0;
            double rad = Math.toRadians(angle);
            
            double px = bx + Math.cos(rad) * 2.0;
            double pz = bz + Math.sin(rad) * 2.0;
            double py = by + Math.sin(System.currentTimeMillis() / 400.0 + i) * 0.5 + 1.6;

            ms.pushPose();
            ms.translate(px, py, pz);
            
            int col = ColorUtil.getColor(200, 50, 50, (int) (255 * alpha));
            RectUtil.drawRect(ms, -0.25f, -0.25f, 0.5f, 0.5f, col, col, col, col, true, true);
            
            ms.popPose();
        }

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
    }
}
