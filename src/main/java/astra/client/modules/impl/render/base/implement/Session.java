/*    */ package shame.astra.client.modules.impl.render.base.implement;
/*    */ 
/*    */ import net.minecraft.class_642;
/*    */ import shame.astra.api.events.implement.EventRender;
/*    */ import shame.astra.api.utils.color.ColorUtils;
/*    */ import shame.astra.api.utils.draggable.Draggable;
/*    */ import shame.astra.api.utils.render.RenderUtils;
/*    */ import shame.astra.api.utils.render.fonts.msdf.Font;
/*    */ import shame.astra.api.utils.render.fonts.msdf.Fonts;
/*    */ import shame.astra.client.modules.impl.render.base.InterfaceProcessing;
/*    */ 
/*    */ public class Session extends InterfaceProcessing {
/* 13 */   private long sessionStartTime = System.currentTimeMillis();
/*    */   
/*    */   public Session(Draggable draggable) {
/* 16 */     super(draggable);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onRender(EventRender.Default eventRender) {
/* 21 */     float x = this.draggable.getX(), y = this.draggable.getY();
/* 22 */     long now = System.currentTimeMillis();
/*    */     
/* 24 */     float height = 18.0F;
/*    */     
/* 26 */     String serverName = "local";
/* 27 */     if (mc != null) {
/* 28 */       class_642 info = mc.method_1558();
/* 29 */       if (info != null && info.field_3761 != null && !info.field_3761.isEmpty()) {
/* 30 */         serverName = info.field_3761;
/*    */       }
/*    */     } 
/*    */     
/* 34 */     String playerName = "unknown";
/* 35 */     if (mc != null && mc.field_1724 != null) {
/* 36 */       playerName = mc.field_1724.method_5477().getString();
/* 37 */     } else if (mc != null && mc.method_1548() != null) {
/* 38 */       playerName = mc.method_1548().method_1676();
/*    */     } 
/*    */     
/* 41 */     long elapsed = now - this.sessionStartTime;
/* 42 */     long totalSeconds = elapsed / 1000L;
/* 43 */     long hours = totalSeconds / 3600L;
/* 44 */     long minutes = totalSeconds % 3600L / 60L;
/* 45 */     long seconds = totalSeconds % 60L;
/* 46 */     String playTime = "" + hours + "h " + hours + "m " + minutes + "s";
/*    */     
/* 48 */     String titleText = "sessioninfo";
/* 49 */     String serverText = "server: " + serverName;
/* 50 */     String nameText = "name: " + playerName;
/* 51 */     String playTimeText = "playtime: " + playTime;
/*    */     
/* 53 */     Font font = Fonts.getFont("suisse", 15);
/*    */     
/* 55 */     float titleWidth = font.getWidth(titleText);
/* 56 */     float serverWidth = font.getWidth(serverText);
/* 57 */     float nameWidth = font.getWidth(nameText);
/* 58 */     float playTimeWidth = font.getWidth(playTimeText);
/*    */     
/* 60 */     float maxTextWidth = Math.max(titleWidth, Math.max(serverWidth, Math.max(nameWidth, playTimeWidth)));
/* 61 */     float width = maxTextWidth + 10.0F;
/*    */     
/* 63 */     int time = (int)((float)(now % 2000L) / 2000.0F * 360.0F);
/*    */     
/* 65 */     int leftTop = ColorUtils.getThemeColor(time);
/* 66 */     int leftBottom = ColorUtils.getThemeColor(time + 30);
/* 67 */     int centerTop = ColorUtils.getThemeColor(time + 90);
/* 68 */     int centerBottom = ColorUtils.getThemeColor(time + 120);
/* 69 */     int rightTop = ColorUtils.getThemeColor(time + 180);
/* 70 */     int rightBottom = ColorUtils.getThemeColor(time + 210);
/*    */     
/* 72 */     RenderUtils.drawWaveHudPanel(eventRender.getContext().method_51448(), x, y, width, height + 25.0F, ColorUtils.rgba(25, 25, 25, 150), height - 3.0F, 0.0F, 10.0F, 10.0F, leftTop, leftBottom, centerTop, centerBottom, rightTop, rightBottom);
/*    */ 
/*    */ 
/*    */     
/* 76 */     font.drawStringWithShadow(eventRender.getContext().method_51448(), titleText, x + 3.0F, y + 5.0F, -1);
/* 77 */     font.drawStringWithShadow(eventRender.getContext().method_51448(), serverText, x + 3.0F, y + 18.0F, -1);
/* 78 */     font.drawStringWithShadow(eventRender.getContext().method_51448(), nameText, x + 3.0F, y + 25.5F, -1);
/* 79 */     font.drawStringWithShadow(eventRender.getContext().method_51448(), playTimeText, x + 3.0F, y + 33.5F, -1);
/*    */     
/* 81 */     this.draggable.setHeight(height + 25.0F);
/* 82 */     this.draggable.setWidth(width);
/* 83 */     super.onRender(eventRender);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\base\implement\Session.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */