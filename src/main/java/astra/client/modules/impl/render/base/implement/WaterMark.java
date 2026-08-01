/*     */ package shame.astra.client.modules.impl.render.base.implement;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.class_4587;
/*     */ import net.minecraft.class_640;
/*     */ import net.minecraft.class_642;
/*     */ import shame.astra.api.events.implement.EventRender;
/*     */ import shame.astra.api.storages.implement.helpertstorages.enumvar.ModuleClass;
/*     */ import shame.astra.api.utils.color.ColorUtils;
/*     */ import shame.astra.api.utils.draggable.Draggable;
/*     */ import shame.astra.api.utils.render.RenderUtils;
/*     */ import shame.astra.api.utils.render.fonts.msdf.Font;
/*     */ import shame.astra.api.utils.render.fonts.msdf.Fonts;
/*     */ import shame.astra.astra;
/*     */ import shame.astra.client.modules.impl.render.base.InterfaceProcessing;
/*     */ 
/*     */ public class WaterMark extends InterfaceProcessing {
/*     */   private boolean showFps = true;
/*     */   private boolean showMs = true;
/*  24 */   private float animationProgress = 0.0F; private boolean showServer = true; private boolean showTps = true;
/*  25 */   private long lastUpdateTime = System.currentTimeMillis();
/*  26 */   private float pulseAnimation = 0.0F;
/*  27 */   private float breathingAnimation = 0.0F;
/*     */ 
/*     */   
/*  30 */   private List<Particle> particles = new ArrayList<>();
/*  31 */   private Random random = new Random();
/*     */   private static class Particle { float x;
/*     */     float y;
/*     */     float velX;
/*     */     float velY;
/*     */     float life;
/*     */     float maxLife;
/*     */     float size;
/*     */     
/*     */     Particle(float x, float y) {
/*  41 */       this.x = x;
/*  42 */       this.y = y;
/*  43 */       Random r = new Random();
/*  44 */       this.velX = (r.nextFloat() - 0.5F) * 0.5F;
/*  45 */       this.velY = -r.nextFloat() * 1.5F - 0.5F;
/*  46 */       this.maxLife = r.nextFloat() * 2.0F + 1.0F;
/*  47 */       this.life = this.maxLife;
/*  48 */       this.size = r.nextFloat() * 1.5F + 0.5F;
/*     */     }
/*     */     
/*     */     void update(float deltaTime) {
/*  52 */       this.x += this.velX * deltaTime * 10.0F;
/*  53 */       this.y += this.velY * deltaTime * 10.0F;
/*  54 */       this.life -= deltaTime;
/*     */     }
/*     */     
/*     */     boolean isDead() {
/*  58 */       return (this.life <= 0.0F);
/*     */     }
/*     */     
/*     */     float getAlpha() {
/*  62 */       return Math.min(1.0F, this.life / this.maxLife);
/*     */     } }
/*     */ 
/*     */   
/*     */   public static String getUsername() {
/*  67 */     return "Shame";
/*     */   }
/*     */   
/*     */   public static String getUID() {
/*  71 */     return "-1";
/*     */   }
/*     */   
/*     */   public WaterMark(Draggable draggable) {
/*  75 */     super(draggable);
/*     */   }
/*     */   
/*     */   public boolean isShowFps() {
/*  79 */     return this.showFps;
/*     */   }
/*     */   
/*     */   public void setShowFps(boolean showFps) {
/*  83 */     this.showFps = showFps;
/*     */   }
/*     */   
/*     */   public boolean isShowMs() {
/*  87 */     return this.showMs;
/*     */   }
/*     */   
/*     */   public void setShowMs(boolean showMs) {
/*  91 */     this.showMs = showMs;
/*     */   }
/*     */   
/*     */   public boolean isShowServer() {
/*  95 */     return this.showServer;
/*     */   }
/*     */   
/*     */   public void setShowServer(boolean showServer) {
/*  99 */     this.showServer = showServer;
/*     */   }
/*     */   
/*     */   public boolean isShowTps() {
/* 103 */     return this.showTps;
/*     */   }
/*     */   
/*     */   public void setShowTps(boolean showTps) {
/* 107 */     this.showTps = showTps;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRender(EventRender.Default eventRender) {
/* 112 */     if (shouldUseYouGameStyle()) { YouGameStyle(eventRender); }
/* 113 */     else if (ModuleClass.interfaceModule.style.is("Wave")) { WaveStyle(eventRender); }
/* 114 */     else { DefaultStyle(eventRender); }
/* 115 */      super.onRender(eventRender);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean shouldUseYouGameStyle() {
/* 122 */     return (!ModuleClass.interfaceModule.style.is("Wave") || ModuleClass.interfaceModule.youGameWatermark
/* 123 */       .isState());
/*     */   }
/*     */ 
/*     */   
/*     */   public void YouGameStyle(EventRender.Default eventRender) {
/* 128 */     class_4587 matrices = eventRender.getContext().method_51448();
/* 129 */     float x = this.draggable.getX();
/* 130 */     float y = this.draggable.getY();
/* 131 */     Font text = Fonts.getFont("suisse", 12);
/* 132 */     Font icon = Fonts.getFont("iconnew", 12);
/*     */     
/* 134 */     int themeColor = astra.INSTANCE.themeStorage.getThemes().getTheme().getName().equals("Rainbow") ? ColorUtils.getThemeColor() : (astra.INSTANCE.themeStorage.getThemes().getTheme()).color[0];
/* 135 */     int panel = ColorUtils.rgba(23, 24, 29, 218);
/* 136 */     int cell = ColorUtils.rgba(35, 36, 43, 228);
/* 137 */     int white = ColorUtils.rgba(245, 246, 250, 255);
/* 138 */     int muted = ColorUtils.rgba(185, 187, 195, 255);
/*     */     
/* 140 */     int fps = (mc != null) ? mc.method_47599() : 0;
/* 141 */     int ping = 0;
/* 142 */     if (mc != null && mc.field_1724 != null && mc.method_1562() != null) {
/* 143 */       class_640 entry = mc.method_1562().method_2871(mc.field_1724.method_5667());
/* 144 */       if (entry != null) ping = entry.method_2959();
/*     */     
/*     */     } 
/* 147 */     float bps = (mc != null && mc.field_1724 != null) ? (float)(Math.hypot(mc.field_1724.method_23317() - mc.field_1724.field_6014, mc.field_1724.method_23321() - mc.field_1724.field_5969) * 20.0D) : 0.0F;
/* 148 */     String fpsText = "" + fps + " fps";
/* 149 */     String pingText = "" + ping + " ms";
/*     */ 
/*     */     
/* 152 */     String coordinates = (mc != null && mc.field_1724 != null) ? ("x: " + (int)Math.floor(mc.field_1724.method_23317()) + " y: " + (int)Math.floor(mc.field_1724.method_23318()) + " z: " + (int)Math.floor(mc.field_1724.method_23321())) : "x: 0 y: 0 z: 0";
/* 153 */     String tpsText = formatOneDecimal(getServerTps()) + " tps";
/* 154 */     String bpsText = formatOneDecimal(bps) + " bps";
/*     */     
/* 156 */     float h = 16.0F, gap = 2.0F, logoW = 18.0F;
/* 157 */     float titleW = Math.max(46.0F, text.getStringWidth("astra") + 18.0F);
/* 158 */     float fpsW = Math.max(37.0F, text.getStringWidth(fpsText) + 13.0F);
/* 159 */     float pingW = Math.max(35.0F, text.getStringWidth(pingText) + 13.0F);
/* 160 */     float topW = logoW + gap + titleW + gap + fpsW + gap + pingW;
/* 161 */     drawYouGameCell(matrices, x, y, logoW, h, panel, themeColor);
/* 162 */     icon.draw(matrices, "A", x + 5.4F, y + 5.3F, themeColor);
/* 163 */     drawYouGameCell(matrices, x + logoW + gap, y, titleW, h, cell, themeColor);
/* 164 */     text.draw(matrices, "astra", x + logoW + gap + 7.0F, y + 5.2F, white);
/* 165 */     drawYouGameCell(matrices, x + logoW + gap + titleW + gap, y, fpsW, h, cell, themeColor);
/* 166 */     text.draw(matrices, fpsText, x + logoW + gap + titleW + gap + 6.0F, y + 5.2F, white);
/* 167 */     drawYouGameCell(matrices, x + topW - pingW, y, pingW, h, cell, themeColor);
/* 168 */     text.draw(matrices, pingText, x + topW - pingW + 6.0F, y + 5.2F, white);
/*     */     
/* 170 */     float bottomY = y + h + gap;
/* 171 */     float coordinatesW = Math.max(94.0F, text.getStringWidth(coordinates) + 12.0F);
/* 172 */     float tpsW = Math.max(42.0F, text.getStringWidth(tpsText) + 12.0F);
/* 173 */     float bpsW = Math.max(42.0F, text.getStringWidth(bpsText) + 12.0F);
/* 174 */     float bottomW = coordinatesW + gap + tpsW + gap + bpsW;
/* 175 */     drawYouGameCell(matrices, x, bottomY, coordinatesW, h, cell, themeColor);
/* 176 */     text.draw(matrices, coordinates, x + 6.0F, bottomY + 5.2F, muted);
/* 177 */     drawYouGameCell(matrices, x + coordinatesW + gap, bottomY, tpsW, h, cell, themeColor);
/* 178 */     text.draw(matrices, tpsText, x + coordinatesW + gap + 6.0F, bottomY + 5.2F, white);
/* 179 */     drawYouGameCell(matrices, x + coordinatesW + gap + tpsW + gap, bottomY, bpsW, h, cell, themeColor);
/* 180 */     text.draw(matrices, bpsText, x + coordinatesW + gap + tpsW + gap + 6.0F, bottomY + 5.2F, white);
/* 181 */     this.draggable.setWidth(Math.max(topW, bottomW));
/* 182 */     this.draggable.setHeight(h * 2.0F + gap);
/*     */   }
/*     */   
/*     */   private void drawYouGameCell(class_4587 matrices, float x, float y, float width, float height, int fill, int accent) {
/* 186 */     RenderUtils.drawShadow(matrices, x, y, width, height, 5.0F, 7.0F, ColorUtils.applyAlpha(accent, 0.18F));
/* 187 */     RenderUtils.drawRoundedRect(matrices, x, y, width, height, 3.0F, fill);
/* 188 */     RenderUtils.drawRoundedRect(matrices, x, y, width, 0.8F, 3.0F, ColorUtils.applyAlpha(accent, 0.78F));
/*     */   }
/*     */   public void DefaultStyle(EventRender.Default eventRender) {
/*     */     int iconTop, iconBottom, iconMid;
/* 192 */     class_4587 matrices = eventRender.getContext().method_51448();
/* 193 */     float x = this.draggable.getX();
/* 194 */     float y = this.draggable.getY();
/*     */ 
/*     */     
/* 197 */     long currentTime = System.currentTimeMillis();
/* 198 */     float deltaTime = (float)(currentTime - this.lastUpdateTime) / 1000.0F;
/* 199 */     this.lastUpdateTime = currentTime;
/* 200 */     this.animationProgress += deltaTime * 2.0F;
/* 201 */     if (this.animationProgress > 360.0F) this.animationProgress -= 360.0F;
/*     */ 
/*     */     
/* 204 */     this.pulseAnimation += deltaTime * 3.0F;
/* 205 */     if (this.pulseAnimation > 360.0F) this.pulseAnimation -= 360.0F; 
/* 206 */     float pulseScale = 1.0F + (float)Math.sin(this.pulseAnimation) * 0.15F;
/*     */ 
/*     */     
/* 209 */     this.breathingAnimation += deltaTime * 2.5F;
/* 210 */     if (this.breathingAnimation > 360.0F) this.breathingAnimation -= 360.0F; 
/* 211 */     float breathingAlpha = 0.7F + (float)Math.sin(this.breathingAnimation) * 0.3F;
/*     */ 
/*     */     
/* 214 */     this.particles.removeIf(Particle::isDead);
/* 215 */     for (Particle p : this.particles) {
/* 216 */       p.update(deltaTime);
/*     */     }
/*     */ 
/*     */     
/* 220 */     if (this.random.nextFloat() < 0.3F) {
/* 221 */       this.particles.add(new Particle(x + this.random.nextFloat() * 120.0F, y + 15.0F));
/*     */     }
/*     */     
/* 224 */     Font logoFont = Fonts.getFont("logo", 17);
/* 225 */     Font iconNew14 = Fonts.getFont("iconnew", 14);
/* 226 */     Font iconNew15 = Fonts.getFont("iconnew", 15);
/* 227 */     Font icon14 = Fonts.getFont("icon", 14);
/* 228 */     Font statsIconFont = Fonts.getFont("astra", 14);
/* 229 */     if (statsIconFont == null) statsIconFont = (iconNew14 != null) ? iconNew14 : icon14; 
/* 230 */     Font suisse13 = Fonts.getFont("suisse", 13);
/*     */     
/* 232 */     float astraRectH = 16.0F;
/* 233 */     int iconSize = 17;
/* 234 */     String iconGlyph = "A";
/* 235 */     float iconW = logoFont.getStringWidth(iconGlyph);
/* 236 */     float iconX = x + (17.0F - iconW) / 2.0F;
/* 237 */     float iconY = y + 5.5F;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 243 */     if (!astra.INSTANCE.themeStorage.getThemes().getTheme().getName().equals("Rainbow")) {
/* 244 */       iconTop = (astra.INSTANCE.themeStorage.getThemes().getTheme()).color[0];
/* 245 */       iconBottom = ColorUtils.gradient(iconTop, iconTop, (float)Math.sin((this.animationProgress / 180.0F) * Math.PI) * 0.5F + 0.5F);
/* 246 */       iconMid = ColorUtils.gradient(iconTop, iconTop, (float)Math.cos((this.animationProgress / 180.0F) * Math.PI) * 0.5F + 0.5F);
/*     */     } else {
/* 248 */       iconTop = ColorUtils.getThemeColor((int)this.animationProgress);
/* 249 */       iconBottom = ColorUtils.getThemeColor((int)(this.animationProgress + 180.0F) % 360);
/* 250 */       iconMid = ColorUtils.getThemeColor((int)(this.animationProgress + 90.0F) % 360);
/*     */     } 
/*     */     
/* 253 */     boolean drawSquares = isUnusualRectType();
/* 254 */     float rect2Pad = 3.0F;
/* 255 */     String username = getUsername();
/* 256 */     String UID = getUID();
/* 257 */     int whiteColor = (new Color(255, 255, 255, 255)).getRGB();
/* 258 */     float textY = y + 6.8F;
/*     */     
/* 260 */     String brandText = "";
/* 261 */     float brandTextX = iconX + iconW + 2.5F;
/* 262 */     float brandTextW = suisse13.getStringWidth(brandText);
/*     */     
/* 264 */     float astraRectX = x;
/* 265 */     float astraRectY = y;
/* 266 */     float astraRectW = brandTextX + brandTextW + 1.5F - x;
/*     */ 
/*     */     
/* 269 */     RenderUtils.drawDefaultHudThemedPanel(matrices, astraRectX, astraRectY, astraRectW, astraRectH, 2.8F, 3.3F, iconTop);
/*     */ 
/*     */     
/* 272 */     float borderAlpha = 0.3F + (float)Math.sin((this.pulseAnimation * 2.0F)) * 0.2F;
/* 273 */     int borderColor = ColorUtils.applyAlpha(iconTop, borderAlpha);
/* 274 */     RenderUtils.drawShadow(matrices, astraRectX, astraRectY - 0.5F, astraRectW, 1.0F, 1.0F, 1.5F, borderColor);
/*     */     
/* 276 */     if (drawSquares);
/*     */ 
/*     */ 
/*     */     
/* 280 */     int logoShadow = ColorUtils.applyAlpha(iconTop, breathingAlpha * 0.6F);
/* 281 */     int logoShadow2 = ColorUtils.applyAlpha(iconBottom, breathingAlpha * 0.4F);
/* 282 */     int logoShadow3 = ColorUtils.applyAlpha(iconMid, breathingAlpha * 0.5F);
/*     */ 
/*     */     
/* 285 */     RenderUtils.drawShadow(matrices, iconX - 1.5F, iconY - 3.0F, iconW + 1.0F, (iconSize - 7), 5.0F, 12.0F * pulseScale, logoShadow);
/*     */     
/* 287 */     RenderUtils.drawShadow(matrices, iconX - 0.5F, iconY - 2.0F, iconW, (iconSize - 9), 4.0F, 8.0F, logoShadow3);
/*     */     
/* 289 */     RenderUtils.drawShadow(matrices, iconX + 0.3F, iconY - 1.25F, iconW - 1.0F, (iconSize - 11), 3.0F, 5.0F, logoShadow2);
/*     */ 
/*     */     
/* 292 */     logoFont.drawGradientStringHorizontal(matrices, iconGlyph, iconX - 0.25F, iconY, iconTop, iconBottom);
/* 293 */     suisse13.drawString(matrices, brandText, brandTextX, textY, whiteColor);
/*     */     
/* 295 */     float rect2X = astraRectX + astraRectW + 2.5F;
/*     */     
/* 297 */     float rect2H = 15.85F;
/* 298 */     int icon2Size = 14;
/* 299 */     String iconGlyph2 = "e";
/* 300 */     float icon2Y = y + 7.45F;
/*     */     
/* 302 */     int icon3Size = 14;
/* 303 */     String fpsIconGlyph = "j";
/* 304 */     String pingIconGlyph = "f";
/* 305 */     float icon3Y = y + 7.25F;
/*     */     
/* 307 */     int fps = (mc != null) ? mc.method_47599() : 0;
/* 308 */     String fpsValue = String.valueOf(fps);
/* 309 */     String fpsSuffix = "fps";
/* 310 */     String fpsText = fpsValue + fpsValue;
/*     */     
/* 312 */     int ping = 0;
/* 313 */     if (mc != null && mc.field_1724 != null && mc.method_1562() != null) {
/* 314 */       class_640 entry = mc.method_1562().method_2871(mc.field_1724.method_5667());
/* 315 */       if (entry != null) ping = entry.method_2959(); 
/*     */     } 
/* 317 */     String pingValue = String.valueOf(ping);
/* 318 */     String pingSuffix = "ms";
/* 319 */     String pingText = pingValue + pingValue;
/*     */     
/* 321 */     float contentW = rect2Pad;
/* 322 */     contentW += iconNew14.getStringWidth(iconGlyph2) + 1.0F;
/* 323 */     if (!username.isEmpty()) {
/* 324 */       contentW += suisse13.getStringWidth(username) + 2.0F;
/*     */     }
/* 326 */     if (this.showFps) {
/* 327 */       contentW += (statsIconFont != null) ? (statsIconFont.getStringWidth(fpsIconGlyph) + 2.0F) : 0.0F;
/* 328 */       contentW += suisse13.getStringWidth(fpsText) + 2.0F;
/*     */     } 
/* 330 */     if (this.showMs) {
/* 331 */       contentW += (statsIconFont != null) ? (statsIconFont.getStringWidth(pingIconGlyph) + 2.0F) : 0.0F;
/* 332 */       contentW += suisse13.getStringWidth(pingText) + 2.0F;
/*     */     } 
/* 334 */     contentW += rect2Pad;
/*     */     
/* 336 */     float rect2W = contentW - 1.05F;
/*     */ 
/*     */     
/* 339 */     RenderUtils.drawDefaultHudThemedPanel(matrices, rect2X, astraRectY, rect2W, rect2H, 2.8F, 3.3F, iconTop);
/*     */ 
/*     */     
/* 342 */     float border2Alpha = 0.3F + (float)Math.sin((this.pulseAnimation * 2.0F + 1.0F)) * 0.2F;
/* 343 */     int border2Color = ColorUtils.applyAlpha(iconBottom, border2Alpha);
/* 344 */     RenderUtils.drawShadow(matrices, rect2X, astraRectY - 0.5F, rect2W, 1.0F, 1.0F, 1.5F, border2Color);
/*     */ 
/*     */     
/* 347 */     int panelGlow = ColorUtils.applyAlpha(iconTop, breathingAlpha * 0.15F);
/* 348 */     RenderUtils.drawShadow(matrices, rect2X - 1.0F, astraRectY - 1.0F, rect2W + 2.0F, rect2H + 2.0F, 2.0F, 4.0F, panelGlow);
/*     */     
/* 350 */     if (drawSquares) {
/* 351 */       RenderUtils.drawHudSquarePattern(matrices, rect2X, astraRectY, rect2W, rect2H, iconTop);
/*     */     }
/*     */     
/* 354 */     float drawX = rect2X + rect2Pad + 1.5F;
/*     */ 
/*     */     
/* 357 */     int iconGlow = ColorUtils.applyAlpha(iconTop, breathingAlpha * 0.4F);
/* 358 */     RenderUtils.drawShadow(matrices, drawX - 2.0F, icon2Y - 1.0F, iconNew14.getStringWidth(iconGlyph2) + 2.0F, 8.0F, 2.0F, 3.0F, iconGlow);
/* 359 */     iconNew14.drawGradientStringHorizontal(matrices, iconGlyph2, drawX - 1.0F, icon2Y, iconTop, iconBottom);
/* 360 */     drawX += iconNew14.getStringWidth(iconGlyph2) + 1.0F;
/*     */     
/* 362 */     if (!username.isEmpty()) {
/*     */       
/* 364 */       int nameGlow = ColorUtils.applyAlpha(iconMid, breathingAlpha * 0.2F);
/* 365 */       RenderUtils.drawShadow(matrices, drawX - 1.0F, textY - 1.0F, suisse13.getStringWidth(username) + 2.0F, 10.0F, 1.0F, 2.0F, nameGlow);
/* 366 */       suisse13.drawString(matrices, username, drawX, textY, whiteColor);
/* 367 */       drawX += suisse13.getStringWidth(username) + 2.0F;
/*     */     } 
/*     */     
/* 370 */     if (this.showFps) {
/* 371 */       if (statsIconFont != null) {
/*     */         
/* 373 */         int fpsIconGlow = ColorUtils.applyAlpha(iconTop, breathingAlpha * 0.35F);
/* 374 */         RenderUtils.drawShadow(matrices, drawX - 1.0F, icon3Y - 1.0F, statsIconFont.getStringWidth(fpsIconGlyph) + 2.0F, 8.0F, 2.0F, 3.0F, fpsIconGlow);
/* 375 */         statsIconFont.drawGradientStringHorizontal(matrices, fpsIconGlyph, drawX, icon3Y, iconTop, iconBottom);
/* 376 */         drawX += statsIconFont.getStringWidth(fpsIconGlyph) + 2.0F;
/*     */       } 
/*     */       
/* 379 */       suisse13.drawString(matrices, fpsValue, drawX, textY, whiteColor);
/* 380 */       suisse13.drawGradientStringHorizontal(matrices, fpsSuffix, drawX + suisse13.getStringWidth(fpsValue) - 1.0F, textY, iconTop, iconBottom);
/* 381 */       drawX += suisse13.getStringWidth(fpsText) + 2.0F;
/*     */     } 
/*     */     
/* 384 */     if (this.showMs) {
/* 385 */       if (statsIconFont != null) {
/*     */         
/* 387 */         int pingIconGlow = ColorUtils.applyAlpha(iconBottom, breathingAlpha * 0.35F);
/* 388 */         RenderUtils.drawShadow(matrices, drawX - 1.0F, icon3Y - 1.0F, statsIconFont.getStringWidth(pingIconGlyph) + 2.0F, 8.0F, 2.0F, 3.0F, pingIconGlow);
/* 389 */         statsIconFont.drawGradientStringHorizontal(matrices, pingIconGlyph, drawX, icon3Y, iconTop, iconBottom);
/* 390 */         drawX += statsIconFont.getStringWidth(pingIconGlyph) + 2.0F;
/*     */       } 
/*     */       
/* 393 */       suisse13.drawString(matrices, pingValue, drawX, textY, whiteColor);
/* 394 */       suisse13.drawGradientStringHorizontal(matrices, pingSuffix, drawX + suisse13.getStringWidth(pingValue) - 0.5F, textY, iconTop, iconBottom);
/*     */     } 
/*     */     
/* 397 */     String serverName = "Singleplayer";
/* 398 */     if (mc != null) {
/* 399 */       class_642 info = mc.method_1558();
/* 400 */       if (info != null && info.field_3761 != null && !info.field_3761.isEmpty()) {
/* 401 */         serverName = info.field_3761;
/*     */       }
/*     */     } 
/*     */     
/* 405 */     boolean showBottom = (this.showServer || this.showTps);
/* 406 */     float rectBtmY = astraRectY + astraRectH + 2.0F;
/* 407 */     float rectBtmH = 15.85F;
/*     */     
/* 409 */     int iconSmallSize = 15;
/* 410 */     float iconSmallW = iconNew15.getStringWidth(iconGlyph);
/* 411 */     float iconSmallY = rectBtmY + (rectBtmH - iconSmallSize) / 2.0F + 6.5F;
/* 412 */     float serverTextY = rectBtmY + (rectBtmH - 12.0F) / 2.0F + 4.8F;
/* 413 */     String serverDisplayName = formatServerNameForDisplay(serverName);
/* 414 */     float serverTextW = suisse13.getStringWidth(serverDisplayName);
/* 415 */     int extraIconSize = 15;
/* 416 */     String extraIconGlyph = "y";
/* 417 */     float extraIconW = iconNew15.getStringWidth(extraIconGlyph);
/* 418 */     float extraIconY = rectBtmY + (rectBtmH - extraIconSize) / 2.0F + 6.4F;
/* 419 */     String tpsValue = formatOneDecimal(getServerTps());
/* 420 */     String tpsSuffix = "tps";
/* 421 */     String tpsText = tpsValue + tpsValue;
/* 422 */     float tpsTextW = suisse13.getStringWidth(tpsText);
/* 423 */     float rectBtmW = 0.0F;
/* 424 */     if (showBottom) {
/* 425 */       float bottomX = x + rect2Pad + 8.5F;
/* 426 */       if (this.showServer) {
/* 427 */         bottomX += iconSmallW + 3.0F + serverTextW;
/*     */       }
/* 429 */       if (this.showTps) {
/* 430 */         if (this.showServer) bottomX += 3.0F; 
/* 431 */         bottomX += extraIconW + 3.0F + tpsTextW;
/*     */       } 
/* 433 */       rectBtmW = Math.max(40.0F, bottomX + rect2Pad - x);
/*     */ 
/*     */       
/* 436 */       RenderUtils.drawDefaultHudThemedPanel(matrices, x, rectBtmY, rectBtmW - 2.85F, rectBtmH, 2.8F, 3.3F, iconTop);
/*     */ 
/*     */       
/* 439 */       float borderBtmAlpha = 0.3F + (float)Math.sin((this.pulseAnimation * 2.0F + 2.0F)) * 0.2F;
/* 440 */       int borderBtmColor = ColorUtils.applyAlpha(iconMid, borderBtmAlpha);
/* 441 */       RenderUtils.drawShadow(matrices, x, rectBtmY - 0.5F, rectBtmW - 2.85F, 1.0F, 1.0F, 1.5F, borderBtmColor);
/*     */ 
/*     */       
/* 444 */       int bottomPanelGlow = ColorUtils.applyAlpha(iconBottom, breathingAlpha * 0.15F);
/* 445 */       RenderUtils.drawShadow(matrices, x - 1.0F, rectBtmY - 1.0F, rectBtmW - 1.85F, rectBtmH + 2.0F, 2.0F, 4.0F, bottomPanelGlow);
/*     */       
/* 447 */       if (drawSquares) {
/* 448 */         RenderUtils.drawHudSquarePattern(matrices, x, rectBtmY, rectBtmW, rectBtmH, iconTop);
/*     */       }
/*     */       
/* 451 */       float drawBottomX = x + rect2Pad + 7.0F;
/* 452 */       if (this.showServer) {
/*     */         
/* 454 */         int serverIconGlow = ColorUtils.applyAlpha(iconMid, breathingAlpha * 0.4F);
/* 455 */         RenderUtils.drawShadow(matrices, drawBottomX - 7.5F, iconSmallY - 1.0F, iconSmallW + 2.0F, 8.0F, 2.0F, 3.0F, serverIconGlow);
/* 456 */         iconNew15.drawGradientStringHorizontal(matrices, "n", drawBottomX - 6.5F, iconSmallY, iconTop, iconBottom);
/* 457 */         drawBottomX += iconSmallW + 3.0F;
/* 458 */         drawServerNameWithThemeParts(matrices, serverDisplayName, drawBottomX, serverTextY, iconTop, iconBottom, whiteColor);
/* 459 */         drawBottomX += serverTextW;
/*     */       } 
/* 461 */       if (this.showTps) {
/* 462 */         if (this.showServer) drawBottomX += 3.0F;
/*     */         
/* 464 */         int tpsIconGlow = ColorUtils.applyAlpha(iconBottom, breathingAlpha * 0.4F);
/* 465 */         RenderUtils.drawShadow(matrices, drawBottomX - 2.5F, extraIconY - 1.0F, extraIconW + 2.0F, 8.0F, 2.0F, 3.0F, tpsIconGlow);
/* 466 */         iconNew15.drawGradientStringHorizontal(matrices, extraIconGlyph, drawBottomX - 1.5F, extraIconY, iconTop, iconBottom);
/* 467 */         drawBottomX += extraIconW + 3.0F;
/* 468 */         suisse13.drawString(matrices, tpsValue, drawBottomX - 1.75F, serverTextY, whiteColor);
/* 469 */         suisse13.drawGradientStringHorizontal(matrices, tpsSuffix, drawBottomX + suisse13.getStringWidth(tpsValue) - 2.5F, serverTextY, iconTop, iconBottom);
/*     */       } 
/*     */     } 
/*     */     
/* 473 */     float totalW = Math.max(astraRectW + 2.0F + rect2W, rectBtmW);
/* 474 */     this.draggable.setWidth(totalW);
/* 475 */     this.draggable.setHeight(showBottom ? (astraRectH + 1.0F + rectBtmH) : astraRectH);
/*     */ 
/*     */     
/* 478 */     for (Particle particle : this.particles) {
/* 479 */       float particleAlpha = particle.getAlpha() * breathingAlpha;
/* 480 */       int particleColor = ColorUtils.applyAlpha(iconMid, particleAlpha * 0.6F);
/* 481 */       RenderUtils.drawShadow(matrices, particle.x, particle.y, particle.size, particle.size, 1.0F, particle.size * 2.0F, particleColor);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void WaveStyle(EventRender.Default eventRender) {
/* 486 */     float x = this.draggable.getX(), y = this.draggable.getY();
/* 487 */     class_4587 matrices = eventRender.getContext().method_51448();
/* 488 */     Font waveFont = Fonts.getFont("wave", 30);
/* 489 */     String watermarkText = "astra";
/*     */ 
/*     */     
/* 492 */     long currentTime = System.currentTimeMillis();
/* 493 */     float deltaTime = (float)(currentTime - this.lastUpdateTime) / 1000.0F;
/* 494 */     this.lastUpdateTime = currentTime;
/* 495 */     this.animationProgress += deltaTime * 2.0F;
/* 496 */     if (this.animationProgress > 360.0F) this.animationProgress -= 360.0F;
/*     */ 
/*     */     
/* 499 */     this.pulseAnimation += deltaTime * 3.0F;
/* 500 */     if (this.pulseAnimation > 360.0F) this.pulseAnimation -= 360.0F; 
/* 501 */     float pulseScale = 1.0F + (float)Math.sin(this.pulseAnimation) * 0.2F;
/*     */ 
/*     */     
/* 504 */     this.breathingAnimation += deltaTime * 2.5F;
/* 505 */     if (this.breathingAnimation > 360.0F) this.breathingAnimation -= 360.0F; 
/* 506 */     float breathingAlpha = 0.8F + (float)Math.sin(this.breathingAnimation) * 0.2F;
/*     */ 
/*     */     
/* 509 */     int indexColor = ColorUtils.getThemeColor((int)(90.0F + this.animationProgress) % 360);
/* 510 */     int indexColor2 = ColorUtils.getThemeColor((int)(180.0F + this.animationProgress) % 360);
/* 511 */     int indexColor3 = ColorUtils.getThemeColor((int)(270.0F + this.animationProgress) % 360);
/* 512 */     int indexColor4 = ColorUtils.getColor((int)(360.0F + this.animationProgress) % 360);
/* 513 */     int indexColor5 = ColorUtils.getThemeColor((int)(45.0F + this.animationProgress) % 360);
/* 514 */     float glowWidth = 95.0F + waveFont.getStringWidth("ful");
/*     */ 
/*     */     
/* 517 */     int shadow1 = ColorUtils.applyAlpha(indexColor4, breathingAlpha * 0.6F);
/* 518 */     int shadow2 = ColorUtils.applyAlpha(indexColor2, breathingAlpha * 0.5F);
/* 519 */     int shadow3 = ColorUtils.applyAlpha(indexColor, breathingAlpha * 0.4F);
/* 520 */     int shadow4 = ColorUtils.applyAlpha(indexColor3, breathingAlpha * 0.7F);
/* 521 */     int shadow5 = ColorUtils.applyAlpha(indexColor5, breathingAlpha * 0.3F);
/*     */ 
/*     */     
/* 524 */     RenderUtils.drawShadow(matrices, x - 4.0F, y - 4.0F, glowWidth + 8.0F, 20.0F, 14.0F, 25.0F * pulseScale, shadow1, shadow2, shadow3, shadow4);
/*     */     
/* 526 */     RenderUtils.drawShadow(matrices, x - 2.0F, y - 2.0F, glowWidth + 4.0F, 16.0F, 12.0F, 20.0F, shadow4, shadow2, shadow5, shadow3);
/*     */     
/* 528 */     RenderUtils.drawShadow(matrices, x, y, glowWidth, 12.0F, 10.0F, 15.0F, shadow4, shadow2, shadow1, shadow3);
/*     */ 
/*     */     
/* 531 */     waveFont.drawGradientStringHorizontal(matrices, watermarkText, x, y, indexColor, indexColor2);
/*     */     
/* 533 */     this.draggable.setWidth(Math.max(glowWidth, waveFont.getStringWidth(watermarkText)));
/* 534 */     this.draggable.setHeight(12.0F);
/*     */ 
/*     */     
/* 537 */     this.particles.removeIf(Particle::isDead);
/* 538 */     for (Particle p : this.particles) {
/* 539 */       p.update(deltaTime);
/*     */     }
/*     */ 
/*     */     
/* 543 */     if (this.random.nextFloat() < 0.4F) {
/* 544 */       this.particles.add(new Particle(x + this.random.nextFloat() * glowWidth, y + 10.0F));
/*     */     }
/*     */ 
/*     */     
/* 548 */     for (Particle particle : this.particles) {
/* 549 */       float particleAlpha = particle.getAlpha() * breathingAlpha;
/* 550 */       int particleColor = ColorUtils.applyAlpha(indexColor5, particleAlpha * 0.8F);
/* 551 */       RenderUtils.drawShadow(matrices, particle.x, particle.y, particle.size, particle.size, 1.0F, particle.size * 3.0F, particleColor);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void drawServerNameWithThemeParts(class_4587 matrices, String serverName, float x, float y, int themeColor, int themeColor2, int whiteColor) {
/* 556 */     Font font = Fonts.getFont("suisse", 13);
/* 557 */     String[] parts = serverName.split("\\.");
/* 558 */     if (parts.length < 2) {
/* 559 */       font.drawString(matrices, serverName, x, y, whiteColor);
/*     */       
/*     */       return;
/*     */     } 
/* 563 */     String mainPart = String.join(".", Arrays.<CharSequence>copyOf((CharSequence[])parts, parts.length - 1));
/* 564 */     String suffixPart = "." + parts[parts.length - 1];
/*     */     
/* 566 */     font.drawString(matrices, mainPart, x, y, whiteColor);
/* 567 */     float suffixX = x + font.getStringWidth(mainPart) - 2.0F;
/*     */     
/* 569 */     font.drawGradientStringHorizontal(matrices, suffixPart, suffixX, y, themeColor, themeColor2);
/*     */   }
/*     */   
/*     */   private String formatServerNameForDisplay(String serverName) {
/* 573 */     if (serverName == null || serverName.isEmpty()) {
/* 574 */       return "";
/*     */     }
/*     */     
/* 577 */     String host = serverName;
/* 578 */     int portIndex = host.indexOf(':');
/* 579 */     if (portIndex > 0) {
/* 580 */       host = host.substring(0, portIndex);
/*     */     }
/*     */     
/* 583 */     String[] parts = host.split("\\.");
/* 584 */     if (parts.length >= 3) {
/* 585 */       return String.join(".", Arrays.<CharSequence>copyOfRange((CharSequence[])parts, 1, parts.length));
/*     */     }
/* 587 */     return host;
/*     */   }
/*     */   
/*     */   private float getServerTps() {
/* 591 */     if (astra.INSTANCE == null || astra.INSTANCE.tpsCalc == null) {
/* 592 */       return 20.0F;
/*     */     }
/* 594 */     return Math.max(0.0F, Math.min(20.0F, astra.INSTANCE.tpsCalc.getTPS()));
/*     */   }
/*     */   
/*     */   private String formatOneDecimal(float value) {
/* 598 */     int scaled = Math.round(value * 10.0F);
/* 599 */     return "" + scaled / 10 + "." + scaled / 10;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\base\implement\WaterMark.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */