package shame.astra.hud;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.text.Text;

import shame.astra.Javelin;
import shame.astra.base.animations.base.Animation;
import shame.astra.base.animations.base.Easing;
import shame.astra.base.font.Font;
import shame.astra.base.font.Fonts;
import shame.astra.base.theme.Theme;
import shame.astra.client.hud.elements.draggable.DraggableHudElement;
import shame.astra.client.modules.api.Module;
import shame.astra.client.modules.impl.render.Menu;
import shame.astra.utility.render.display.base.BorderRadius;
import shame.astra.utility.render.display.base.CustomDrawContext;
import shame.astra.utility.render.display.base.color.ColorRGBA;
import shame.astra.utility.render.display.shader.DrawUtil;

public class NotifyComponent extends DraggableHudElement {
   private final Animation toggleAnimation;
   private final List<NotifyComponent.BaseNotification> notifications;

   // ОСТОРОЖНО ПО КОДУ ГУЛЯЕТ ГДЕ ТО 20 ЛИШНИХ ХРОМОСОМ BY TRFXCVII
   private static final float blurStrength = 15.0f;
   private static final float cornerRadius = 2.25f;

   public NotifyComponent(String name, float initialX, float initialY, float windowWidth, float windowHeight, float offsetX, float offsetY, DraggableHudElement.Align align) {
      super(name, initialX, initialY, windowWidth, windowHeight, offsetX, offsetY, align);
      this.toggleAnimation = new Animation(200L, Easing.CUBIC_OUT);
      this.notifications = new ArrayList();
   }

   public void addNotification(Module module, boolean enabled) {
      // Не показываем уведомление при включении/выключении Menu
      if (module instanceof Menu) {
         return;
      }
      this.notifications.addLast(new NotifyComponent.ModuleNotification(module, enabled));
   }

   public void addTextNotification(String icon, Text text) {
      this.notifications.addLast(new NotifyComponent.TextNotification(icon, text));
   }


   public static void drawBlurBackground(CustomDrawContext ctx, float x, float y, float width, float height, Theme theme, float animation) {
      DrawUtil.drawBlur(
              ctx.getMatrices(), x, y, width, height,
              blurStrength,
              BorderRadius.all(cornerRadius),
              new ColorRGBA(255, 255, 255, (int)(animation * 255))
      );

      ColorRGBA themeColor = theme.getColor();

      ColorRGBA backgroundColor = new ColorRGBA(
              (int) (Math.min(255, Math.max(0, themeColor.getRed() * 0.15f))),
              (int) (Math.min(255, Math.max(0, themeColor.getGreen() * 0.15f))),
              (int) (Math.min(255, Math.max(0, themeColor.getBlue() * 0.15f))),
              (int)(64 * animation)
      );

      DrawUtil.drawRoundedRect(
              ctx.getMatrices(), x, y, width, height,
              BorderRadius.all(cornerRadius),
              backgroundColor
      );
   }

   
   public void render(CustomDrawContext ctx) {
      Iterator<NotifyComponent.BaseNotification> iterator = this.notifications.iterator();
      Theme theme = Javelin.getInstance().getThemeManager().getCurrentTheme();
      Font textFont = Fonts.MEDIUM.getFont(7.25F);
      float notificationHeight = 14.5F;
      float x = (float)mc.getWindow().getScaledWidth() / 2.0F - 44.0F;
      float y = (float)mc.getWindow().getScaledHeight() / 2.0F + 16.0F;

      NotifyComponent.BaseNotification n;
      float gap = 1.5F;

      for(Iterator var11 = Lists.reverse(this.notifications).iterator(); var11.hasNext(); ) {
         n = (NotifyComponent.BaseNotification)var11.next();
         n.render(ctx, (float)mc.getWindow().getScaledWidth() / 2.0F, y, textFont, theme, notificationHeight, this);
         y += (notificationHeight + gap) * n.alphaAnimation.getValue();
      }

      while(true) {
         while(iterator.hasNext()) {
            NotifyComponent.BaseNotification notification = (NotifyComponent.BaseNotification)iterator.next();
            if (!notification.fadingOut && System.currentTimeMillis() - notification.timestamp > 1500L) {
               notification.fadingOut = true;
               notification.alphaAnimation.update(0.0F);
            }

            if (notification.fadingOut && notification.alphaAnimation.getValue() < 0.01F) {
               iterator.remove();
            } else {
               notification.alphaAnimation.update(notification.fadingOut ? 0.0F : 1.0F);
            }
         }

         return;
      }
   }

   private static class ModuleNotification extends NotifyComponent.BaseNotification {
      final Module module;
      final boolean enabled;

      ModuleNotification(Module module, boolean enabled) {
         this.module = module;
         this.enabled = enabled;
      }

      
      void render(CustomDrawContext ctx, float x, float y, Font textFont, Theme theme, float notificationHeight, NotifyComponent parent) {
         if (this.timestamp == 0L) {
            this.timestamp = System.currentTimeMillis();
         }

         float iconBgWidth = 12.0F;
         ColorRGBA textColor = ColorRGBA.WHITE.withAlpha(this.alphaAnimation.getValue() * 255.0F);

         String moduleName = "\"" + this.module.getName() + "\"";
         String statusText = this.enabled ? " Включен" : " Выключен";

         float moduleNameWidth = Fonts.SEMIBOLD.getWidth(moduleName, 7.25F);
         float statusTextWidth = textFont.width(statusText);
         float width = iconBgWidth + 8.0F + moduleNameWidth + statusTextWidth;

         Font iconFont = Fonts.NURIKI.getFont(11F);
         String icon = this.enabled ? "J" : "K";
         x -= width / 2.0F;


         drawBlurBackground(ctx, x, y, width, notificationHeight, theme, this.alphaAnimation.getValue());

         float iconX = x + (16.8F - iconFont.width(icon)) / 2.0F;
         float iconY = y + 1.6F + (notificationHeight - iconFont.height()) / 2.0F;
         ctx.drawText(iconFont, icon, iconX, iconY, theme.getColor().withAlpha(this.alphaAnimation.getValue() * 255.0F));

         float textX = x + iconBgWidth + 2.5F;
         float textY = y + (notificationHeight - textFont.height()) / 2.0F;

         ctx.drawText(textFont, moduleName, textX + 2.35F, textY, ColorRGBA.WHITE.withAlpha(this.alphaAnimation.getValue() * 255.0F));
         ctx.drawText(textFont, statusText, textX + 2.35F + moduleNameWidth, textY, textColor);
      }
   }

   private static class TextNotification extends NotifyComponent.BaseNotification {
      final String icon;
      final Text text;

      TextNotification(String icon, Text text) {
         this.icon = icon;
         this.text = text;
      }

      void render(CustomDrawContext ctx, float x, float y, Font textFont, Theme theme, float notificationHeight, NotifyComponent parent) {
         if (this.timestamp == 0L) {
            this.timestamp = System.currentTimeMillis();
         }

         float iconBgWidth = 14.0F;
         Text moduleName = this.text;
         float moduleNameWidth = textFont.width(moduleName);
         float width = iconBgWidth + 6.0F + moduleNameWidth;

         Font iconFont = Fonts.ICONS2.getFont(6.75F);

         x -= width / 2.0F;

         drawBlurBackground(ctx, x, y, width, notificationHeight, theme, this.alphaAnimation.getValue());

         float iconX = x + (17F - iconFont.width(this.icon)) / 2.0F;
         float iconY = y + 1F + (notificationHeight - iconFont.height()) / 2.0F;
         ctx.drawText(iconFont, this.icon, iconX, iconY, theme.getColor().withAlpha(this.alphaAnimation.getValue() * 255.0F));

         float textX = x + iconBgWidth + 1.65F;
         float textY = y + (notificationHeight - textFont.height()) / 2.0F;
         ctx.drawText(textFont, moduleName, textX + 3.5F, textY, this.alphaAnimation.getValue() * 255.0F);
      }
   }

   private abstract static class BaseNotification {
      long timestamp;
      boolean fadingOut = false;
      final Animation alphaAnimation;

      private BaseNotification() {
         this.alphaAnimation = new Animation(300L, Easing.CUBIC_OUT);
      }

      abstract void render(CustomDrawContext var1, float var2, float var3, Font var4, Theme var5, float var6, NotifyComponent var7);
   }
}
