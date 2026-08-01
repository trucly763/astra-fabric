/*     */ package shame.astra.client.modules.impl.render.base.implement;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.class_4587;
/*     */ import shame.astra.api.events.implement.EventRender;
/*     */ import shame.astra.api.storages.implement.helpertstorages.enumvar.ModuleClass;
/*     */ import shame.astra.api.utils.animation.AnimationUtils;
/*     */ import shame.astra.api.utils.animation.Easings;
/*     */ import shame.astra.api.utils.color.ColorUtils;
/*     */ import shame.astra.api.utils.draggable.Draggable;
/*     */ import shame.astra.api.utils.notification.NotificationManager;
/*     */ import shame.astra.api.utils.render.RenderUtils;
/*     */ import shame.astra.api.utils.render.fonts.msdf.Font;
/*     */ import shame.astra.api.utils.render.fonts.msdf.Fonts;
/*     */ import shame.astra.astra;
/*     */ import shame.astra.client.modules.impl.render.base.InterfaceProcessing;
/*     */ 
/*     */ public class Notifications
/*     */   extends InterfaceProcessing {
/*     */   private static final float DEFAULT_PAD_X = 7.0F;
/*     */   private static final float DEFAULT_ICON_TEXT_GAP = 1.0F;
/*     */   private static final float PREVIEW_ICON_TEXT_GAP = 2.0F;
/*  27 */   private final Map<NotificationManager.Entry, AnimationUtils> appearAnimations = new HashMap<>();
/*  28 */   private final Map<NotificationManager.Entry, Float> currentYPositions = new HashMap<>();
/*  29 */   private final Set<NotificationManager.Entry> activeEntriesScratch = new HashSet<>();
/*  30 */   private long lastRenderTime = System.currentTimeMillis();
/*  31 */   private float previewAlpha = 0.0F;
/*     */   
/*     */   public Notifications(Draggable draggable) {
/*  34 */     super(draggable);
/*     */   }
/*     */   
/*  37 */   private Font issue(int size) { return Fonts.getFont("suisse", size); }
/*  38 */   private Font icons(int size) { return Fonts.getFont("icon", size); } private Font iconNew(int size) {
/*  39 */     return Fonts.getFont("icon", size);
/*     */   }
/*     */   private String getEntryText(NotificationManager.Entry entry) {
/*  42 */     if (entry.isCustom()) {
/*  43 */       return entry.customText;
/*     */     }
/*  45 */     String state = entry.enabled ? "Включен!" : "Выключен!";
/*  46 */     return entry.moduleName + " " + entry.moduleName;
/*     */   }
/*     */   
/*     */   private String getWaveBodyText(NotificationManager.Entry entry) {
/*  50 */     if (entry.isCustom()) {
/*  51 */       return entry.customText;
/*     */     }
/*  53 */     return "Module '" + entry.moduleName + "' is " + (entry.enabled ? "enabled." : "disabled.");
/*     */   }
/*     */   
/*     */   private float getDefaultEntryWidth(NotificationManager.Entry entry, float padX) {
/*  57 */     String text = getEntryText(entry);
/*  58 */     String iconGlyph = (entry.categoryIcon != null && !entry.categoryIcon.isEmpty()) ? entry.categoryIcon : "?";
/*  59 */     return issue(13).getWidth(text) + icons(14).getWidth(iconGlyph) + padX * 2.0F + 1.0F;
/*     */   }
/*     */   
/*     */   private float getPreviewWidth(String previewText, String previewIconGlyph, float padX) {
/*  63 */     return issue(13).getWidth(previewText) + icons(16).getWidth(previewIconGlyph) + padX * 2.0F + 2.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRender(EventRender.Default eventRender) {
/*  68 */     if (!ModuleClass.interfaceModule.style.is("Wave")) {
/*  69 */       DefaultStyle(eventRender);
/*     */     } else {
/*  71 */       WaveStyle(eventRender);
/*     */     } 
/*  73 */     super.onRender(eventRender);
/*     */   }
/*     */   private void DefaultStyle(EventRender.Default eventRender) {
/*     */     int colorTheme;
/*  77 */     if (mc == null)
/*     */       return; 
/*  79 */     long currentTime = System.currentTimeMillis();
/*  80 */     float deltaTime = (float)(currentTime - this.lastRenderTime) / 1000.0F;
/*  81 */     this.lastRenderTime = currentTime;
/*     */     
/*  83 */     List<NotificationManager.Entry> entries = NotificationManager.getActive();
/*  84 */     boolean isChatOpen = mc.field_1755 instanceof net.minecraft.class_408;
/*     */     
/*  86 */     boolean shouldRender = (!entries.isEmpty() || isChatOpen);
/*     */     
/*  88 */     float targetPreviewAlpha = isChatOpen ? 0.7F : 0.0F;
/*  89 */     float alphaSpeed = 8.0F;
/*  90 */     this.previewAlpha += (targetPreviewAlpha - this.previewAlpha) * Math.min(1.0F, alphaSpeed * deltaTime);
/*     */     
/*  92 */     if (!shouldRender && this.previewAlpha < 0.01F) {
/*  93 */       this.appearAnimations.clear();
/*  94 */       this.currentYPositions.clear();
/*  95 */       this.previewAlpha = 0.0F;
/*     */       
/*     */       return;
/*     */     } 
/*  99 */     float baseX = this.draggable.getX();
/* 100 */     float baseY = this.draggable.getY();
/*     */ 
/*     */     
/* 103 */     if (!astra.INSTANCE.themeStorage.getThemes().getTheme().getName().equals("Rainbow")) {
/* 104 */       colorTheme = (astra.INSTANCE.themeStorage.getThemes().getTheme()).color[0];
/*     */     } else {
/* 106 */       colorTheme = ColorUtils.getThemeColor();
/*     */     } 
/* 108 */     boolean drawSquares = isUnusualRectType();
/*     */     
/* 110 */     long now = System.currentTimeMillis();
/* 111 */     float height = 16.0F;
/* 112 */     float spacing = 3.0F;
/* 113 */     float lerpSpeed = 12.0F;
/* 114 */     float padX = 7.0F;
/*     */     
/* 116 */     String previewText = "Кликни на меня для открытия настроек!";
/* 117 */     String previewIconGlyph = "A";
/* 118 */     float previewIconW = icons(16).getWidth(previewIconGlyph);
/* 119 */     float previewWidth = getPreviewWidth(previewText, previewIconGlyph, padX);
/*     */     
/* 121 */     float maxWidth = previewWidth;
/* 122 */     for (NotificationManager.Entry entry : entries) {
/* 123 */       float width = getDefaultEntryWidth(entry, padX);
/* 124 */       if (width > maxWidth) maxWidth = width;
/*     */     
/*     */     } 
/* 127 */     float targetY = baseY;
/*     */     
/* 129 */     if (this.previewAlpha > 0.01F) {
/* 130 */       float x = baseX + (maxWidth - previewWidth) * 0.5F;
/* 131 */       float renderY = targetY;
/* 132 */       float alpha = this.previewAlpha;
/* 133 */       float scale = 0.86F + 0.14F * alpha;
/*     */       
/* 135 */       int base = ColorUtils.setAlphaColor(ColorUtils.rgba(50, 50, 50, 255), (int)(255.0F * alpha));
/* 136 */       int top = ColorUtils.setAlphaColor(ColorUtils.darken(colorTheme, 0.15F), (int)(255.0F * alpha));
/* 137 */       int bottom = ColorUtils.setAlphaColor(ColorUtils.darken(colorTheme, 0.05F), (int)(255.0F * alpha));
/*     */       
/* 139 */       float cx = x + previewWidth * 0.5F;
/* 140 */       float cy = renderY + height * 0.5F;
/* 141 */       class_4587 ms = eventRender.getContext().method_51448();
/* 142 */       ms.method_22903();
/* 143 */       ms.method_46416(cx, cy, 0.0F);
/* 144 */       ms.method_22905(scale, scale, 1.0F);
/* 145 */       ms.method_46416(-cx, -cy, 0.0F);
/*     */       
/* 147 */       RenderUtils.drawDefaultHudPanel(ms, x, renderY, previewWidth, height, 3.0F, 3.5F, base, top, bottom);
/* 148 */       float squareAlpha = alpha * alpha * (3.0F - 2.0F * alpha);
/* 149 */       if (drawSquares && squareAlpha > 0.08F) {
/* 150 */         RenderUtils.drawHudSquarePattern(ms, x, renderY, previewWidth, height, ColorUtils.setAlphaColor(colorTheme, (int)(255.0F * squareAlpha)));
/*     */       }
/*     */       
/* 153 */       int textColor = ColorUtils.setAlphaColor(-1, (int)(255.0F * alpha));
/* 154 */       int iconColor = ColorUtils.setAlphaColor(colorTheme, (int)(255.0F * alpha));
/* 155 */       icons(16).draw(ms, previewIconGlyph, x + padX - 3.5F, renderY + 6.6F, iconColor);
/* 156 */       issue(13).draw(ms, previewText, x + padX + previewIconW + 5.5F, renderY + 6.6F, textColor);
/*     */       
/* 158 */       ms.method_22909();
/*     */       
/* 160 */       targetY += height + spacing;
/*     */     } 
/*     */     
/* 163 */     for (NotificationManager.Entry entry : entries) {
/* 164 */       AnimationUtils anim = this.appearAnimations.computeIfAbsent(entry, e -> new AnimationUtils(0.0F, 12.0F, Easings.QUAD_OUT));
/* 165 */       long age = now - entry.startTime;
/* 166 */       anim.update(1.0F);
/* 167 */       float appear = anim.getValue();
/* 168 */       float alpha = appear;
/* 169 */       if (age > 2300L) {
/* 170 */         alpha = (1.0F - (float)(age - 2300L) / 200.0F) * appear;
/*     */       }
/* 172 */       if (alpha <= 0.0F) {
/* 173 */         targetY += height + spacing;
/*     */         
/*     */         continue;
/*     */       } 
/* 177 */       Float currentY = this.currentYPositions.get(entry);
/* 178 */       if (currentY == null) {
/* 179 */         currentY = Float.valueOf(targetY);
/*     */       }
/*     */       
/* 182 */       float diff = targetY - currentY.floatValue();
/* 183 */       if (Math.abs(diff) > 0.01F) {
/* 184 */         currentY = Float.valueOf(currentY.floatValue() + diff * Math.min(1.0F, lerpSpeed * deltaTime));
/*     */       } else {
/* 186 */         currentY = Float.valueOf(targetY);
/*     */       } 
/* 188 */       this.currentYPositions.put(entry, currentY);
/*     */       
/* 190 */       String text = getEntryText(entry);
/* 191 */       String iconGlyph = (entry.categoryIcon != null && !entry.categoryIcon.isEmpty()) ? entry.categoryIcon : "?";
/*     */       
/* 193 */       float iconW = icons(14).getWidth(iconGlyph);
/* 194 */       float width = getDefaultEntryWidth(entry, padX);
/* 195 */       float x = baseX + (maxWidth - width) * 0.5F;
/* 196 */       float slide = 6.0F * (1.0F - appear);
/*     */       
/* 198 */       float renderY = currentY.floatValue() + slide;
/* 199 */       float scale = 0.86F + 0.14F * alpha;
/* 200 */       boolean disabled = (!entry.isCustom() && !entry.enabled);
/* 201 */       int disabledRed = ColorUtils.rgba(200, 55, 55, 255);
/*     */       
/* 203 */       int base = ColorUtils.setAlphaColor(ColorUtils.rgba(50, 50, 50, 255), (int)(255.0F * alpha));
/* 204 */       int top = ColorUtils.setAlphaColor(ColorUtils.darken(colorTheme, 0.15F), (int)(255.0F * alpha));
/* 205 */       int bottom = ColorUtils.setAlphaColor(ColorUtils.darken(colorTheme, 0.05F), (int)(255.0F * alpha));
/*     */       
/* 207 */       float cx = x + width * 0.5F;
/* 208 */       float cy = renderY + height * 0.5F;
/* 209 */       class_4587 ms = eventRender.getContext().method_51448();
/* 210 */       ms.method_22903();
/* 211 */       ms.method_46416(cx, cy, 0.0F);
/* 212 */       ms.method_22905(scale, scale, 1.0F);
/* 213 */       ms.method_46416(-cx, -cy, 0.0F);
/*     */       
/* 215 */       RenderUtils.drawDefaultHudPanel(ms, x, renderY, width, height, 3.0F, 3.5F, base, top, bottom);
/* 216 */       float squareAlpha = alpha * alpha * (3.0F - 2.0F * alpha);
/* 217 */       if (drawSquares && squareAlpha > 0.08F) {
/* 218 */         RenderUtils.drawHudSquarePattern(ms, x, renderY, width, height, ColorUtils.setAlphaColor(colorTheme, (int)(255.0F * squareAlpha)));
/*     */       }
/*     */       
/* 221 */       int textColor = ColorUtils.setAlphaColor(-1, (int)(255.0F * alpha));
/* 222 */       int iconColor = ColorUtils.setAlphaColor(colorTheme, (int)(255.0F * alpha));
/* 223 */       icons(14).draw(ms, iconGlyph, x + padX - 1.5F, renderY + 7.3F, iconColor);
/* 224 */       float textX = x + padX + iconW + 1.0F;
/* 225 */       if (!entry.isCustom()) {
/* 226 */         String modulePart = entry.moduleName + " ";
/* 227 */         String statePart = (text.length() > modulePart.length()) ? text.substring(modulePart.length()) : "";
/* 228 */         int stateColor = disabled ? disabledRed : iconColor;
/* 229 */         issue(13).draw(ms, modulePart, textX + 2.0F, renderY + 6.8F, textColor);
/* 230 */         issue(13).draw(ms, statePart, textX + issue(13).getWidth(modulePart) - 0.5F + 2.0F, renderY + 7.0F, stateColor);
/*     */       } else {
/* 232 */         issue(13).draw(ms, text, textX, renderY + 6.8F, textColor);
/*     */       } 
/*     */       
/* 235 */       ms.method_22909();
/*     */       
/* 237 */       targetY += height + spacing;
/*     */     } 
/*     */     
/* 240 */     this.activeEntriesScratch.clear();
/* 241 */     this.activeEntriesScratch.addAll(entries);
/* 242 */     this.appearAnimations.keySet().removeIf(entry -> !this.activeEntriesScratch.contains(entry));
/* 243 */     this.currentYPositions.keySet().removeIf(entry -> !this.activeEntriesScratch.contains(entry));
/*     */     
/* 245 */     this.draggable.setWidth(maxWidth);
/* 246 */     this.draggable.setHeight(Math.max(1.0F, targetY - baseY));
/*     */   }
/*     */   
/*     */   private void WaveStyle(EventRender.Default eventRender) {
/* 250 */     if (mc == null)
/*     */       return; 
/* 252 */     long currentTime = System.currentTimeMillis();
/* 253 */     float deltaTime = (float)(currentTime - this.lastRenderTime) / 1000.0F;
/* 254 */     this.lastRenderTime = currentTime;
/*     */     
/* 256 */     List<NotificationManager.Entry> entries = NotificationManager.getActive();
/* 257 */     if (entries.isEmpty()) {
/* 258 */       this.appearAnimations.clear();
/* 259 */       this.currentYPositions.clear();
/*     */       
/*     */       return;
/*     */     } 
/* 263 */     int time = (int)((float)(System.currentTimeMillis() % 2000L) / 2000.0F * 360.0F);
/*     */     
/* 265 */     int leftTop = ColorUtils.getThemeColor(time);
/* 266 */     int leftBottom = ColorUtils.getThemeColor(time + 30);
/* 267 */     int centerTop = ColorUtils.getThemeColor(time + 90);
/* 268 */     int centerBottom = ColorUtils.getThemeColor(time + 120);
/* 269 */     int rightTop = ColorUtils.getThemeColor(time + 180);
/* 270 */     int rightBottom = ColorUtils.getThemeColor(time + 210);
/*     */     
/* 272 */     long now = System.currentTimeMillis();
/*     */     
/* 274 */     float spacing = 4.0F;
/* 275 */     float lerpSpeed = 14.0F;
/* 276 */     float screenW = mc.method_22683().method_4486();
/* 277 */     float screenH = mc.method_22683().method_4502();
/* 278 */     float rightPadding = 5.0F;
/* 279 */     float bottomPadding = 5.0F;
/*     */     
/* 281 */     float stackOffset = 0.0F;
/* 282 */     float maxWidth = 120.0F;
/*     */     
/* 284 */     for (NotificationManager.Entry entry : entries) {
/* 285 */       String title = "Notify";
/* 286 */       String body = getWaveBodyText(entry);
/* 287 */       float iconW = iconNew(14).getWidth("j");
/* 288 */       float titleW = issue(15).getWidth(title);
/* 289 */       float bodyW = issue(13).getWidth(body);
/* 290 */       float width = Math.max(120.0F, Math.max(titleW + iconW + 18.0F, bodyW + 14.0F));
/* 291 */       maxWidth = Math.max(maxWidth, width);
/*     */     } 
/*     */     
/* 294 */     for (NotificationManager.Entry entry : entries) {
/* 295 */       AnimationUtils anim = this.appearAnimations.computeIfAbsent(entry, e -> new AnimationUtils(0.0F, 12.0F, Easings.QUAD_OUT));
/* 296 */       anim.update(1.0F);
/* 297 */       float appear = anim.getValue();
/*     */       
/* 299 */       long age = now - entry.startTime;
/* 300 */       float alphaMul = appear;
/* 301 */       if (age > 2300L) {
/* 302 */         alphaMul = (1.0F - (float)(age - 2300L) / 200.0F) * appear;
/*     */       }
/* 304 */       if (alphaMul <= 0.01F)
/*     */         continue; 
/* 306 */       String title = "Notify";
/* 307 */       String body = getWaveBodyText(entry);
/* 308 */       String warningGlyph = "j";
/*     */       
/* 310 */       float iconW = iconNew(14).getWidth(warningGlyph);
/* 311 */       float titleW = issue(15).getWidth(title);
/* 312 */       float bodyW = issue(13).getWidth(body);
/* 313 */       float width = Math.max(120.0F, Math.max(titleW + iconW + 18.0F, bodyW + 14.0F));
/* 314 */       float height = 24.0F;
/*     */       
/* 316 */       float x = screenW - width - rightPadding;
/* 317 */       float targetY = screenH - bottomPadding - height - stackOffset;
/*     */       
/* 319 */       Float currentY = this.currentYPositions.get(entry);
/* 320 */       if (currentY == null) currentY = Float.valueOf(targetY); 
/* 321 */       currentY = Float.valueOf(currentY.floatValue() + (targetY - currentY.floatValue()) * Math.min(1.0F, lerpSpeed * deltaTime));
/* 322 */       this.currentYPositions.put(entry, currentY);
/*     */       
/* 324 */       float y = currentY.floatValue();
/* 325 */       float scale = 0.86F + 0.14F * alphaMul;
/*     */       
/* 327 */       int bg = ColorUtils.rgba(25, 25, 25, (int)(150.0F * alphaMul));
/* 328 */       int txt = ColorUtils.setAlphaColor(-1, (int)(255.0F * alphaMul));
/* 329 */       int iconCol = ColorUtils.setAlphaColor(ColorUtils.rgba(235, 0, 0, 255), (int)(255.0F * alphaMul));
/*     */       
/* 331 */       float cx = x + width * 0.5F;
/* 332 */       float cy = y + height * 0.5F;
/* 333 */       class_4587 ms = eventRender.getContext().method_51448();
/* 334 */       ms.method_22903();
/* 335 */       ms.method_46416(cx, cy, 0.0F);
/* 336 */       ms.method_22905(scale, scale, 1.0F);
/* 337 */       ms.method_46416(-cx, -cy, 0.0F);
/*     */       
/* 339 */       RenderUtils.drawWaveHudPanel(ms, x, y, width, height - 1.5F, bg, 3.5F, 0.0F, 10.0F, 10.0F, leftTop, leftBottom, centerTop, centerBottom, rightTop, rightBottom);
/*     */ 
/*     */ 
/*     */       
/* 343 */       iconNew(28).draw(ms, warningGlyph, x + 3.0F, y + 8.0F, iconCol);
/* 344 */       issue(15).draw(ms, title, x + 19.0F, y + 6.5F, txt);
/* 345 */       issue(13).draw(ms, body, x + 19.0F, y + 15.0F, txt);
/*     */       
/* 347 */       ms.method_22909();
/*     */       
/* 349 */       stackOffset += (height + spacing) * appear;
/*     */     } 
/*     */     
/* 352 */     this.appearAnimations.keySet().removeIf(entry -> !entries.contains(entry));
/* 353 */     this.currentYPositions.keySet().removeIf(entry -> !entries.contains(entry));
/*     */     
/* 355 */     this.draggable.setWidth(0.0F);
/* 356 */     this.draggable.setHeight(0.0F);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\base\implement\Notifications.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */