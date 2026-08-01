/*     */ package shame.astra.client.modules.impl.render.base.implement;
/*     */ 
/*     */ import shame.astra.api.events.implement.EventRender;
/*     */ import shame.astra.api.storages.implement.helpertstorages.enumvar.ModuleClass;
/*     */ import shame.astra.api.utils.color.ColorUtils;
/*     */ import shame.astra.api.utils.draggable.Draggable;
/*     */ import shame.astra.api.utils.math.MathUtils;
/*     */ import shame.astra.api.utils.render.RenderUtils;
/*     */ import shame.astra.api.utils.render.fonts.msdf.Font;
/*     */ import shame.astra.api.utils.render.fonts.msdf.Fonts;
/*     */ import shame.astra.astra;
/*     */ import shame.astra.client.modules.impl.render.base.InterfaceProcessing;
/*     */ 
/*     */ public class Information
/*     */   extends InterfaceProcessing {
/*     */   public Information(Draggable draggable) {
/*  17 */     super(draggable);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRender(EventRender.Default eventRender) {
/*  22 */     if (!ModuleClass.interfaceModule.style.is("Wave")) { DefaultStyle(eventRender); }
/*  23 */     else { WaveStyle(eventRender); }
/*  24 */      super.onRender(eventRender);
/*     */   }
/*     */   public void DefaultStyle(EventRender.Default eventRender) {
/*     */     int colorTheme;
/*  28 */     float x = this.draggable.getX(), y = this.draggable.getY();
/*  29 */     Font font = Fonts.getFont("suisse", 13);
/*  30 */     Font iconFont = Fonts.getFont("icon", 16);
/*  31 */     Font smallIconFont = Fonts.getFont("icon", 15);
/*     */ 
/*     */     
/*  34 */     if (!astra.INSTANCE.themeStorage.getThemes().getTheme().getName().equals("Rainbow")) {
/*  35 */       colorTheme = (astra.INSTANCE.themeStorage.getThemes().getTheme()).color[0];
/*     */     } else {
/*  37 */       colorTheme = ColorUtils.getThemeColor();
/*     */     } 
/*  39 */     boolean drawSquares = isUnusualRectType();
/*     */     
/*  41 */     int px = (int)Math.floor(mc.field_1724.method_23317());
/*  42 */     int py = (int)Math.floor(mc.field_1724.method_23318());
/*  43 */     int pz = (int)Math.floor(mc.field_1724.method_23321());
/*     */     
/*  45 */     float height = 16.0F;
/*  46 */     double bps = MathUtils.calculateBPS();
/*  47 */     String xValue = String.valueOf(px);
/*  48 */     String yValue = String.valueOf(py);
/*  49 */     String zValue = String.valueOf(pz);
/*  50 */     String coordsText = xValue + "x " + xValue + "y " + yValue + "z";
/*  51 */     String bpsValue = formatTwoDecimals(bps);
/*  52 */     String bpsSuffix = " b/s";
/*  53 */     float widthbps = font.getWidth(bpsValue + bpsValue);
/*  54 */     float xbps = x + 17.0F + widthbps;
/*  55 */     float widthCords = font.getWidth(coordsText);
/*  56 */     float totalWidth = 13.0F + widthCords + widthbps + 2.0F + 13.8F;
/*     */     
/*  58 */     RenderUtils.drawDefaultHudThemedPanel(eventRender.getContext().method_51448(), x, y, totalWidth, height, 3.0F, 3.5F, colorTheme);
/*  59 */     if (drawSquares) {
/*  60 */       RenderUtils.drawHudSquarePattern(eventRender.getContext().method_51448(), x, y, totalWidth, height, colorTheme);
/*     */     }
/*     */     
/*  63 */     float speedTextX = x + 13.5F;
/*  64 */     float bpsValueWidth = font.getWidth(bpsValue);
/*  65 */     font.draw(eventRender.getContext().method_51448(), bpsValue, speedTextX, y + 6.6D, -1);
/*  66 */     font.draw(eventRender.getContext().method_51448(), bpsSuffix, (speedTextX + bpsValueWidth - 2.0F), y + 6.6D, colorTheme);
/*  67 */     float coordsX = xbps + 9.0F;
/*  68 */     font.draw(eventRender.getContext().method_51448(), xValue, coordsX, y + 6.6D, -1);
/*  69 */     coordsX += font.getWidth(xValue);
/*  70 */     font.draw(eventRender.getContext().method_51448(), "x", (coordsX - 1.0F), y + 6.6D, colorTheme);
/*  71 */     coordsX += font.getWidth("x ");
/*  72 */     font.draw(eventRender.getContext().method_51448(), yValue, coordsX, y + 6.6D, -1);
/*  73 */     coordsX += font.getWidth(yValue);
/*  74 */     font.draw(eventRender.getContext().method_51448(), "y", (coordsX - 1.0F), y + 6.6D, colorTheme);
/*  75 */     coordsX += font.getWidth("y ");
/*  76 */     font.draw(eventRender.getContext().method_51448(), zValue, coordsX, y + 6.6D, -1);
/*  77 */     coordsX += font.getWidth(zValue);
/*  78 */     font.draw(eventRender.getContext().method_51448(), "z", (coordsX - 1.0F), y + 6.6D, colorTheme);
/*  79 */     iconFont.draw(eventRender.getContext().method_51448(), "c", x + 3.25D, y + 6.6D, colorTheme);
/*  80 */     smallIconFont.draw(eventRender.getContext().method_51448(), "x", (xbps - 1.0F), y + 6.85D, colorTheme);
/*     */ 
/*     */     
/*  83 */     this.draggable.setHeight(height);
/*  84 */     this.draggable.setWidth(totalWidth);
/*     */   }
/*     */   
/*     */   public void WaveStyle(EventRender.Default eventRender) {
/*  88 */     float x = this.draggable.getX(), y = this.draggable.getY();
/*     */     
/*  90 */     float time = (float)(System.currentTimeMillis() % 2000L) / 2000.0F * 360.0F;
/*     */     
/*  92 */     int leftTop1 = ColorUtils.getThemeColor((int)time);
/*  93 */     int leftBottom1 = ColorUtils.getThemeColor((int)(time + 30.0F));
/*  94 */     int centerTop1 = ColorUtils.getThemeColor((int)(time + 90.0F));
/*  95 */     int centerBottom1 = ColorUtils.getThemeColor((int)(time + 120.0F));
/*  96 */     int rightTop1 = ColorUtils.getThemeColor((int)(time + 180.0F));
/*  97 */     int rightBottom1 = ColorUtils.getThemeColor((int)(time + 210.0F));
/*     */     
/*  99 */     String title = "coords";
/* 100 */     String xText = "x: " + (int)mc.field_1724.method_19538().method_10216();
/* 101 */     String yText = "y: " + (int)mc.field_1724.method_19538().method_10214();
/* 102 */     String zText = "z: " + (int)mc.field_1724.method_19538().method_10215();
/*     */     
/* 104 */     Font font = Fonts.getFont("suisse", 15);
/*     */     
/* 106 */     float xWidth = font.getWidth(xText);
/* 107 */     float yWidth = font.getWidth(yText);
/* 108 */     float zWidth = font.getWidth(zText);
/* 109 */     float titleWidth = font.getWidth(title);
/*     */     
/* 111 */     float maxCoordWidth = Math.max(xWidth, Math.max(yWidth, zWidth));
/*     */     
/* 113 */     float padding = 9.0F;
/* 114 */     float rectWidth = maxCoordWidth + padding;
/* 115 */     float rectHeight = 40.0F;
/*     */     
/* 117 */     rectWidth = Math.max(rectWidth, 35.0F);
/*     */     
/* 119 */     float centerX = x + rectWidth / 2.0F;
/*     */     
/* 121 */     RenderUtils.drawWaveHudPanel(eventRender.getContext().method_51448(), x, y, rectWidth, rectHeight, ColorUtils.rgba(25, 25, 25, 150), 3.5F, 0.0F, 10.0F, 10.0F, leftTop1, leftBottom1, centerTop1, centerBottom1, rightTop1, rightBottom1);
/*     */ 
/*     */ 
/*     */     
/* 125 */     float barPadding = 5.0F;
/* 126 */     RenderUtils.drawWaveHudHeader(eventRender.getContext().method_51448(), x + barPadding, y + 12.0F, rectWidth - barPadding * 2.0F, 2.5F, 0.0F, 10.0F, 10.0F, leftTop1, leftBottom1, centerTop1, centerBottom1, rightTop1, rightBottom1);
/*     */ 
/*     */     
/* 129 */     font.drawStringWithShadow(eventRender.getContext().method_51448(), title, centerX - titleWidth / 2.0F, y + 5.0F, -1);
/* 130 */     font.drawStringWithShadow(eventRender.getContext().method_51448(), xText, x + 4.5F, y + 17.0F, -1);
/* 131 */     font.drawStringWithShadow(eventRender.getContext().method_51448(), yText, x + 4.5F, y + 24.0F, -1);
/* 132 */     font.drawStringWithShadow(eventRender.getContext().method_51448(), zText, x + 4.5F, y + 31.0F, -1);
/*     */     
/* 134 */     float bpsX = x + rectWidth + 5.0F;
/* 135 */     float bpsY = y;
/*     */     
/* 137 */     double bps = MathUtils.calculateBPS();
/*     */     
/* 139 */     String bpsTitle = "bps";
/* 140 */     String bpsText = String.valueOf((int)bps);
/*     */     
/* 142 */     float bpsTitleWidth = font.getWidth(bpsTitle);
/* 143 */     float bpsTextWidth = font.getWidth(bpsText);
/*     */     
/* 145 */     float bpsRectWidth = Math.max(bpsTitleWidth, bpsTextWidth) + 10.0F;
/* 146 */     float bpsRectHeight = 25.0F;
/*     */     
/* 148 */     bpsRectWidth = Math.max(bpsRectWidth, 30.0F);
/*     */     
/* 150 */     float bpsCenterX = bpsX + bpsRectWidth / 2.0F;
/*     */     
/* 152 */     RenderUtils.drawWaveHudPanel(eventRender.getContext().method_51448(), bpsX, bpsY, bpsRectWidth, bpsRectHeight, ColorUtils.rgba(25, 25, 25, 150), 3.5F, 0.0F, 10.0F, 10.0F, leftTop1, leftBottom1, centerTop1, centerBottom1, rightTop1, rightBottom1);
/*     */ 
/*     */ 
/*     */     
/* 156 */     RenderUtils.drawWaveHudHeader(eventRender.getContext().method_51448(), bpsX + barPadding, bpsY + 12.0F, bpsRectWidth - barPadding * 2.0F, 2.5F, 0.0F, 10.0F, 10.0F, leftTop1, leftBottom1, centerTop1, centerBottom1, rightTop1, rightBottom1);
/*     */ 
/*     */     
/* 159 */     font.drawStringWithShadow(eventRender.getContext().method_51448(), bpsTitle, bpsCenterX - bpsTitleWidth / 2.0F, bpsY + 5.0F, -1);
/* 160 */     font.drawStringWithShadow(eventRender.getContext().method_51448(), bpsText, bpsCenterX - bpsTextWidth / 2.0F, bpsY + 17.0F, -1);
/*     */     
/* 162 */     float totalWidth = rectWidth + 5.0F + bpsRectWidth;
/* 163 */     this.draggable.setWidth(totalWidth);
/* 164 */     this.draggable.setHeight(rectHeight);
/*     */   }
/*     */   
/*     */   private String formatTwoDecimals(double value) {
/* 168 */     int scaled = (int)Math.round(value * 100.0D);
/* 169 */     int fraction = Math.abs(scaled % 100);
/* 170 */     return "" + scaled / 100 + "." + scaled / 100 + ((fraction < 10) ? "0" : "");
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\base\implement\Information.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */