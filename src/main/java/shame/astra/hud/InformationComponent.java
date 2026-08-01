package shame.astra.hud;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import java.util.Locale;
import net.minecraft.client.gui.screen.ChatScreen;

import shame.astra.Javelin;
import shame.astra.base.animations.base.Animation;
import shame.astra.base.animations.base.Easing;
import shame.astra.base.events.impl.other.EventWindowResize;
import shame.astra.base.font.Fonts;
import shame.astra.base.theme.Theme;
import shame.astra.client.hud.elements.draggable.DraggableHudElement;
import shame.astra.utility.render.display.base.BorderRadius;
import shame.astra.utility.render.display.base.CustomDrawContext;
import shame.astra.utility.render.display.base.color.ColorRGBA;
import shame.astra.utility.render.display.shader.DrawUtil;

public class InformationComponent extends DraggableHudElement {
   private final Animation yAnimation;

   public InformationComponent(String name, float initialX, float initialY, float windowWidth, float windowHeight, float offsetX, float offsetY, DraggableHudElement.Align align) {
      super(name, initialX, initialY, windowWidth, windowHeight, offsetX, offsetY, align);
      this.yAnimation = new Animation(200L, Easing.CUBIC_OUT);
      EventManager.register(this);
   }

   @EventTarget
   private void onWindowResized(EventWindowResize e) {
      if (mc.currentScreen instanceof ChatScreen) {
         this.yAnimation.setValue((float)(mc.getWindow().getScaledHeight() - 15));
         this.yAnimation.setStartValue((float)(mc.getWindow().getScaledHeight() - 15));
      } else {
         this.yAnimation.setStartValue((float)mc.getWindow().getScaledHeight());
         this.yAnimation.setValue((float)mc.getWindow().getScaledHeight());
      }

   }

   
   public void render(CustomDrawContext ctx) {
      Theme theme = Javelin.getInstance().getThemeManager().getCurrentTheme();
      if (mc.currentScreen instanceof ChatScreen) {
         this.yAnimation.update((float)(mc.getWindow().getScaledHeight() - 15));
      } else {
         this.yAnimation.update((float)mc.getWindow().getScaledHeight());
      }

      int px = (int)Math.floor(mc.player.getX());
      int py = (int)Math.floor(mc.player.getY());
      int pz = (int)Math.floor(mc.player.getZ());
      double speed = Math.hypot(mc.player.getX() - mc.player.prevX, mc.player.getZ() - mc.player.prevZ);
      String coordsText = String.format(Locale.US, "%d %d %d", px, py, pz);
      String speedText = String.format("%.2f", speed * 20.0D).replace(",", ".");
      DrawUtil.drawBlur(ctx.getMatrices(), 4.0F, this.yAnimation.getValue() - 17.0F, Fonts.MEDIUM.getWidth(coordsText, 7.75F) + Fonts.MEDIUM.getWidth(speedText, 7.75F) + 49.5F, 14.0F, 11.0F, BorderRadius.all(2.0F), new ColorRGBA(80, 80, 80, 255));
      ctx.drawText(Fonts.ICONS2.getFont(7.5F), "\uf57d", 7.75F, this.yAnimation.getValue() - 12.5F, theme.getColor());
      ctx.drawText(Fonts.MEDIUM.getFont(7.5F), coordsText, 18.0F, this.yAnimation.getValue() - 12.5F, ColorRGBA.WHITE);
      DrawUtil.drawRoundedRect(ctx.getMatrices(), 18.5F + Fonts.MEDIUM.getWidth(coordsText, 7.75F) + 3.0F, this.yAnimation.getValue() - 11.0F, 2.0F, 2.0F, BorderRadius.all(0.5F), theme.getColor());
      ctx.drawText(Fonts.ICONS2.getFont(7.5F), "\uf70c", 24.0F + Fonts.MEDIUM.getWidth(coordsText, 7.75F) + 3.5F, this.yAnimation.getValue() - 12.5F, theme.getColor());
      ctx.drawText(Fonts.MEDIUM.getFont(7.5F), speedText + " Б/С", 24.0F + Fonts.MEDIUM.getWidth(coordsText, 7.75F) + 12.0F, this.yAnimation.getValue() - 12.5F, ColorRGBA.WHITE);
   }
}
