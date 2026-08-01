/*     */ package shame.astra.client.modules.impl.render.base.implement;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.class_1058;
/*     */ import net.minecraft.class_1074;
/*     */ import net.minecraft.class_1291;
/*     */ import net.minecraft.class_1293;
/*     */ import net.minecraft.class_3532;
/*     */ import net.minecraft.class_6880;
/*     */ import shame.astra.api.events.implement.EventRender;
/*     */ import shame.astra.api.storages.implement.helpertstorages.enumvar.ModuleClass;
/*     */ import shame.astra.api.utils.animation.AnimationUtils;
/*     */ import shame.astra.api.utils.animation.Easings;
/*     */ import shame.astra.api.utils.color.ColorUtils;
/*     */ import shame.astra.api.utils.draggable.Draggable;
/*     */ import shame.astra.api.utils.render.RenderUtils;
/*     */ import shame.astra.api.utils.render.fonts.msdf.Font;
/*     */ import shame.astra.api.utils.render.fonts.msdf.Fonts;
/*     */ import shame.astra.api.utils.scissor.ScissorUtils;
/*     */ import shame.astra.astra;
/*     */ import shame.astra.client.modules.impl.render.base.InterfaceProcessing;
/*     */ 
/*     */ public class Potions
/*     */   extends InterfaceProcessing
/*     */ {
/*     */   private static final class PotionSnapshot {
/*     */     class_6880<class_1291> entry;
/*     */     String baseName;
/*     */     int amplifier;
/*     */     int duration;
/*     */     boolean infinite;
/*     */   }
/*  40 */   private final Map<class_1291, AnimationUtils> animations = new LinkedHashMap<>();
/*  41 */   private final Map<class_1291, PotionSnapshot> snapshots = new HashMap<>();
/*  42 */   private final Map<class_1291, Integer> maxDurations = new HashMap<>();
/*  43 */   private final Set<class_1291> renderOrderSeen = new HashSet<>();
/*  44 */   private final AnimationUtils widthAnimation = new AnimationUtils(70.0F, 10.5F, Easings.QUAD_OUT);
/*     */ 
/*     */   
/*  47 */   private float animationProgress = 0.0F;
/*  48 */   private long lastUpdateTime = System.currentTimeMillis();
/*  49 */   private float pulseAnimation = 0.0F;
/*  50 */   private float breathingAnimation = 0.0F;
/*     */   
/*     */   public Potions(Draggable draggable) {
/*  53 */     super(draggable);
/*     */   }
/*     */   
/*  56 */   private Font issue(int size) { return Fonts.getFont("suisse", size); } private Font icon(int size) {
/*  57 */     return Fonts.getFont("icon", size);
/*     */   }
/*     */   private AnimationUtils getAnimation(class_1291 effect) {
/*  60 */     return this.animations.computeIfAbsent(effect, e -> new AnimationUtils(0.0F, 10.5F, Easings.QUAD_OUT));
/*     */   }
/*     */   
/*     */   private static String getLevelSuffix(int level) {
/*  64 */     return String.valueOf(Math.max(1, level));
/*     */   }
/*     */   
/*     */   private static String formatDuration(class_1293 effect) {
/*  68 */     return formatDuration(effect.method_5584(), effect.method_48559());
/*     */   }
/*     */   
/*     */   private static String formatDuration(int duration, boolean infinite) {
/*  72 */     if (infinite) {
/*  73 */       return "inf";
/*     */     }
/*  75 */     int seconds = Math.max(0, duration / 20);
/*  76 */     int minutes = seconds / 60;
/*  77 */     int secs = seconds % 60;
/*  78 */     return "" + minutes + ":" + minutes;
/*     */   }
/*     */   
/*     */   private void updateSnapshot(class_1293 effect) {
/*  82 */     class_1291 type = (class_1291)effect.method_5579().comp_349();
/*  83 */     PotionSnapshot snapshot = this.snapshots.computeIfAbsent(type, e -> new PotionSnapshot());
/*  84 */     snapshot.entry = effect.method_5579();
/*  85 */     snapshot.baseName = class_1074.method_4662(effect.method_5586(), new Object[0]);
/*  86 */     snapshot.amplifier = effect.method_5578() + 1;
/*  87 */     snapshot.duration = effect.method_5584();
/*  88 */     snapshot.infinite = effect.method_48559();
/*     */   }
/*     */   
/*     */   private List<class_1291> buildRenderOrder(Collection<class_1293> effects, Set<class_1291> active) {
/*  92 */     List<class_1291> order = new ArrayList<>();
/*  93 */     this.renderOrderSeen.clear();
/*  94 */     for (class_1293 effect : effects) {
/*  95 */       class_1291 type = (class_1291)effect.method_5579().comp_349();
/*  96 */       if (this.renderOrderSeen.add(type)) {
/*  97 */         order.add(type);
/*     */       }
/*     */     } 
/* 100 */     for (class_1291 type : this.animations.keySet()) {
/* 101 */       if (!active.contains(type)) {
/* 102 */         order.add(type);
/*     */       }
/*     */     } 
/* 105 */     return order;
/*     */   }
/*     */   
/*     */   private void drawEffectIcon(EventRender.Default eventRender, class_6880<class_1291> effect, float x, float y, int size, int alpha) {
/* 109 */     class_1058 sprite = mc.method_18505().method_18663(effect);
/* 110 */     int color = ColorUtils.rgba(255, 255, 255, alpha);
/* 111 */     RenderUtils.drawSprite(eventRender.getContext().method_51448(), sprite, x, y, size, color);
/*     */   }
/*     */   
/*     */   private void drawTextWithShadow(EventRender.Default eventRender, Font font, String text, float x, float y, int color) {
/* 115 */     int shadow = ColorUtils.rgba(20, 20, 20, 145);
/* 116 */     font.draw(eventRender.getContext().method_51448(), text, x + 0.8F, y + 0.8F, shadow);
/* 117 */     font.draw(eventRender.getContext().method_51448(), text, x, y, color);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRender(EventRender.Default eventRender) {
/* 122 */     if (ModuleClass.interfaceModule.style.is("Обычный")) {
/* 123 */       DefaultStyle(eventRender);
/*     */     } else {
/* 125 */       WaveStyle(eventRender);
/*     */     } 
/* 127 */     super.onRender(eventRender);
/*     */   }
/*     */   public void DefaultStyle(EventRender.Default eventRender) {
/*     */     int colorTheme;
/* 131 */     float x = this.draggable.getX();
/* 132 */     float y = this.draggable.getY();
/*     */ 
/*     */     
/* 135 */     long currentTime = System.currentTimeMillis();
/* 136 */     float deltaTime = (float)(currentTime - this.lastUpdateTime) / 1000.0F;
/* 137 */     this.lastUpdateTime = currentTime;
/* 138 */     this.animationProgress += deltaTime * 2.0F;
/* 139 */     if (this.animationProgress > 360.0F) this.animationProgress -= 360.0F;
/*     */ 
/*     */     
/* 142 */     this.pulseAnimation += deltaTime * 3.0F;
/* 143 */     if (this.pulseAnimation > 360.0F) this.pulseAnimation -= 360.0F; 
/* 144 */     float pulseScale = 1.0F + (float)Math.sin(this.pulseAnimation) * 0.1F;
/*     */ 
/*     */     
/* 147 */     this.breathingAnimation += deltaTime * 2.5F;
/* 148 */     if (this.breathingAnimation > 360.0F) this.breathingAnimation -= 360.0F; 
/* 149 */     float breathingAlpha = 0.8F + (float)Math.sin(this.breathingAnimation) * 0.2F;
/*     */ 
/*     */     
/* 152 */     if (!astra.INSTANCE.themeStorage.getThemes().getTheme().getName().equals("Rainbow")) {
/* 153 */       colorTheme = (astra.INSTANCE.themeStorage.getThemes().getTheme()).color[0];
/*     */     } else {
/* 155 */       colorTheme = ColorUtils.getThemeColor((int)this.animationProgress);
/*     */     } 
/* 157 */     int colorTheme2 = ColorUtils.getThemeColor((int)(this.animationProgress + 90.0F) % 360);
/* 158 */     float targetWidth = 70.0F;
/* 159 */     float targetHeight = 16.0F;
/* 160 */     int visibleCount = 0;
/*     */ 
/*     */ 
/*     */     
/* 164 */     Collection<class_1293> effects = (mc != null && mc.field_1724 != null) ? mc.field_1724.method_6026() : List.<class_1293>of();
/*     */     
/* 166 */     Set<class_1291> active = new HashSet<>();
/* 167 */     for (class_1293 effect : effects) {
/* 168 */       class_1291 type = (class_1291)effect.method_5579().comp_349();
/* 169 */       active.add(type);
/* 170 */       getAnimation(type).update(1.0F);
/* 171 */       updateSnapshot(effect);
/*     */       
/* 173 */       int duration = effect.method_5584();
/* 174 */       Integer prevMax = this.maxDurations.get(type);
/*     */       
/* 176 */       if (prevMax == null || duration > prevMax.intValue()) {
/* 177 */         this.maxDurations.put(type, Integer.valueOf(duration));
/*     */       }
/*     */     } 
/*     */     
/* 181 */     for (Map.Entry<class_1291, AnimationUtils> entry : this.animations.entrySet()) {
/* 182 */       if (!active.contains(entry.getKey())) {
/* 183 */         ((AnimationUtils)entry.getValue()).update(0.0F);
/*     */       }
/*     */     } 
/*     */     
/* 187 */     List<class_1291> renderOrder = buildRenderOrder(effects, active);
/*     */     
/* 189 */     for (class_1291 type : renderOrder) {
/* 190 */       AnimationUtils anim = getAnimation(type);
/* 191 */       float animValue = anim.getValue();
/* 192 */       PotionSnapshot snapshot = this.snapshots.get(type);
/* 193 */       if (animValue <= 0.01F || 
/* 194 */         snapshot == null) {
/*     */         continue;
/*     */       }
/* 197 */       visibleCount++;
/* 198 */       String baseName = (snapshot.baseName != null) ? snapshot.baseName : class_1074.method_4662(type.method_5567(), new Object[0]);
/* 199 */       String levelSuffix = getLevelSuffix(snapshot.amplifier);
/* 200 */       String time = formatDuration(snapshot.duration, snapshot.infinite);
/* 201 */       float nameWidth = issue(12).getWidth(baseName);
/* 202 */       if (!levelSuffix.isEmpty()) {
/* 203 */         nameWidth += issue(11).getWidth(" LVL") + issue(12).getWidth(levelSuffix);
/*     */       }
/* 205 */       float timeWidth = issue(10).getWidth(time) + 6.0F;
/* 206 */       float rowWidth = nameWidth + timeWidth + 25.0F + 9.0F + 9.0F;
/* 207 */       if (rowWidth > targetWidth) targetWidth = rowWidth; 
/* 208 */       targetHeight += 12.0F * animValue;
/*     */     } 
/*     */ 
/*     */     
/* 212 */     if (visibleCount > 0) targetHeight += 2.0F;
/*     */     
/* 214 */     this.widthAnimation.update(targetWidth);
/* 215 */     float width = this.widthAnimation.getValue();
/* 216 */     float height = targetHeight;
/*     */ 
/*     */     
/* 219 */     int glowColor = ColorUtils.applyAlpha(colorTheme, breathingAlpha * 0.25F);
/* 220 */     RenderUtils.drawShadow(eventRender.getContext().method_51448(), x - 2.0F, y - 2.0F, width + 4.0F, height + 4.0F, 3.0F, 7.0F * pulseScale, glowColor);
/*     */     
/* 222 */     RenderUtils.drawDefaultHudElementRects(eventRender.getContext().method_51448(), x, y, width, height, colorTheme, isUnusualRectType());
/*     */ 
/*     */     
/* 225 */     issue(14).drawGradientStringHorizontal(eventRender.getContext().method_51448(), "Effects", x + 5.0F, y + 6.0F, colorTheme, colorTheme2);
/* 226 */     icon(13).drawGradientStringHorizontal(eventRender.getContext().method_51448(), "d", x + width - 12.5F, y + 7.5F, colorTheme, colorTheme2);
/*     */     
/* 228 */     float offsetY = 18.0F;
/* 229 */     for (class_1291 type : renderOrder) {
/* 230 */       AnimationUtils anim = getAnimation(type);
/* 231 */       float animValue = anim.getValue();
/* 232 */       PotionSnapshot snapshot = this.snapshots.get(type);
/*     */       
/* 234 */       if (animValue <= 0.01F || 
/* 235 */         snapshot == null) {
/*     */         continue;
/*     */       }
/* 238 */       ScissorUtils.push();
/* 239 */       ScissorUtils.setFromComponentCoordinates(x, y, width, height);
/*     */       
/* 241 */       int alpha = (int)(255.0F * animValue);
/* 242 */       int textColor = ColorUtils.rgba(255, 255, 255, alpha);
/* 243 */       int grayColor = ColorUtils.rgba(55, 55, 55, alpha);
/* 244 */       int darkColor = ColorUtils.rgba(35, 35, 35, alpha);
/*     */       
/* 246 */       float iconSize = 7.0F;
/* 247 */       float iconX = x + 5.0F;
/* 248 */       float iconY = y + offsetY;
/* 249 */       if (snapshot.entry != null) {
/* 250 */         drawEffectIcon(eventRender, snapshot.entry, iconX, iconY, (int)iconSize, alpha);
/*     */       }
/*     */       
/* 253 */       String baseName = (snapshot.baseName != null) ? snapshot.baseName : class_1074.method_4662(type.method_5567(), new Object[0]);
/* 254 */       String levelSuffix = getLevelSuffix(snapshot.amplifier);
/* 255 */       float textX = iconX + iconSize + 3.0F;
/* 256 */       float textY = y + 2.0F + offsetY;
/* 257 */       issue(12).draw(eventRender.getContext().method_51448(), baseName, textX, textY, textColor);
/* 258 */       if (!levelSuffix.isEmpty()) {
/* 259 */         float baseWidth = issue(12).getWidth(baseName);
/* 260 */         int levelThemeColor = ColorUtils.setAlphaColor(colorTheme, alpha);
/* 261 */         float lvlX = textX + baseWidth;
/* 262 */         issue(10).draw(eventRender.getContext().method_51448(), " LVL", lvlX, textY + 1.0F, levelThemeColor);
/* 263 */         issue(11).draw(eventRender.getContext().method_51448(), levelSuffix, (lvlX + issue(11).getWidth(" LVL")), textY + 0.5D, levelThemeColor);
/*     */       } 
/*     */       
/* 266 */       String time = formatDuration(snapshot.duration, snapshot.infinite);
/* 267 */       float timeBoxWidth = Math.max(issue(10).getWidth(time) + 4.0F, 12.0F);
/* 268 */       float ringSize = 6.0F;
/* 269 */       float ringGap = 3.0F;
/* 270 */       float timeBoxX = x + width - timeBoxWidth - 5.0F;
/* 271 */       float ringX = timeBoxX - ringGap - ringSize;
/* 272 */       float ringY = y + offsetY + 0.3F;
/* 273 */       RenderUtils.drawDefaultHudInfoBox(eventRender.getContext().method_51448(), timeBoxX, y + offsetY, timeBoxWidth, grayColor, darkColor);
/* 274 */       issue(10).drawCenteredString(eventRender.getContext().method_51448(), time, timeBoxX + timeBoxWidth / 2.0F, y + offsetY + 3.0F, textColor);
/*     */       
/* 276 */       float progress = 1.0F;
/* 277 */       if (!snapshot.infinite) {
/* 278 */         int currentDuration = snapshot.duration;
/* 279 */         int maxDuration = ((Integer)this.maxDurations.getOrDefault(type, Integer.valueOf(currentDuration))).intValue();
/*     */         
/* 281 */         if (maxDuration > 0) {
/* 282 */           progress = class_3532.method_15363(currentDuration / maxDuration, 0.0F, 1.0F);
/*     */         } else {
/* 284 */           progress = 0.0F;
/*     */         } 
/*     */       } 
/*     */       
/* 288 */       int ringColor = ColorUtils.setAlphaColor(colorTheme, alpha);
/* 289 */       float thickness = 1.75F;
/* 290 */       RenderUtils.drawRingArc(eventRender.getContext().method_51448(), ringX, ringY, ringSize, thickness, -90.0F, 270.0F, grayColor);
/* 291 */       if (progress > 0.0F) {
/* 292 */         float endAngle = -90.0F + 360.0F * progress;
/* 293 */         RenderUtils.drawRingArc(eventRender.getContext().method_51448(), ringX, ringY, ringSize, thickness, -90.0F, endAngle, ringColor);
/*     */       } 
/*     */       
/* 296 */       offsetY += 12.0F * animValue;
/* 297 */       ScissorUtils.pop();
/* 298 */       ScissorUtils.unset();
/*     */     } 
/*     */ 
/*     */     
/* 302 */     this.animations.entrySet().removeIf(entry -> (!active.contains(entry.getKey()) && ((AnimationUtils)entry.getValue()).getValue() <= 0.01F));
/* 303 */     this.snapshots.keySet().removeIf(type -> !this.animations.containsKey(type));
/* 304 */     this.maxDurations.keySet().removeIf(type -> !this.animations.containsKey(type));
/*     */     
/* 306 */     this.draggable.setWidth(width);
/* 307 */     this.draggable.setHeight(height);
/*     */   }
/*     */   
/*     */   public void WaveStyle(EventRender.Default eventRender) {
/* 311 */     float x = this.draggable.getX();
/* 312 */     float y = this.draggable.getY();
/*     */     
/* 314 */     int time = (int)((float)(System.currentTimeMillis() % 2000L) / 2000.0F * 360.0F);
/*     */     
/* 316 */     int leftTop = ColorUtils.getThemeColor(time);
/* 317 */     int leftBottom = ColorUtils.getThemeColor(time + 30);
/* 318 */     int centerTop = ColorUtils.getThemeColor(time + 90);
/* 319 */     int centerBottom = ColorUtils.getThemeColor(time + 120);
/* 320 */     int rightTop = ColorUtils.getThemeColor(time + 180);
/* 321 */     int rightBottom = ColorUtils.getThemeColor(time + 210);
/*     */ 
/*     */ 
/*     */     
/* 325 */     Collection<class_1293> effects = (mc != null && mc.field_1724 != null) ? mc.field_1724.method_6026() : List.<class_1293>of();
/*     */     
/* 327 */     Set<class_1291> active = new HashSet<>();
/* 328 */     for (class_1293 effect : effects) {
/* 329 */       class_1291 type = (class_1291)effect.method_5579().comp_349();
/* 330 */       active.add(type);
/* 331 */       getAnimation(type).update(1.0F);
/*     */     } 
/* 333 */     for (Map.Entry<class_1291, AnimationUtils> entry : this.animations.entrySet()) {
/* 334 */       if (!active.contains(entry.getKey())) {
/* 335 */         ((AnimationUtils)entry.getValue()).update(0.0F);
/*     */       }
/*     */     } 
/*     */     
/* 339 */     float width = 84.0F;
/* 340 */     float height = 18.0F;
/* 341 */     int visibleEffects = 0;
/*     */     
/* 343 */     for (class_1293 effect : effects) {
/* 344 */       AnimationUtils anim = getAnimation((class_1291)effect.method_5579().comp_349());
/* 345 */       float animValue = anim.getValue();
/* 346 */       if (animValue <= 0.01F)
/* 347 */         continue;  visibleEffects++;
/*     */       
/* 349 */       String baseName = class_1074.method_4662(effect.method_5586(), new Object[0]);
/* 350 */       String levelSuffix = getLevelSuffix(effect.method_5578() + 1);
/* 351 */       String line = baseName + baseName;
/* 352 */       width = Math.max(width, issue(16).getWidth(line) + 38.0F);
/* 353 */       width = Math.max(width, issue(15).getWidth(formatDuration(effect)) + 38.0F);
/* 354 */       height += 18.0F * animValue;
/*     */     } 
/*     */     
/* 357 */     if (visibleEffects == 0) {
/* 358 */       float headerHeight = 18.0F;
/* 359 */       RenderUtils.drawWaveHudHeader(eventRender.getContext().method_51448(), x, y, width, 15.0F, 0.0F, 10.0F, 10.0F, leftTop, leftBottom, centerTop, centerBottom, rightTop, rightBottom);
/*     */       
/* 361 */       String str = "potions";
/* 362 */       float f1 = x + (width - issue(16).getWidth(str)) / 2.0F;
/* 363 */       drawTextWithShadow(eventRender, issue(16), str, f1, y + 5.0F, -1);
/* 364 */       this.draggable.setWidth(width);
/* 365 */       this.draggable.setHeight(headerHeight);
/*     */       
/*     */       return;
/*     */     } 
/* 369 */     RenderUtils.drawWaveHudPanel(eventRender.getContext().method_51448(), x, y, width, height, ColorUtils.rgba(25, 25, 25, 150), 15.0F, 0.0F, 10.0F, 10.0F, leftTop, leftBottom, centerTop, centerBottom, rightTop, rightBottom);
/*     */ 
/*     */ 
/*     */     
/* 373 */     String title = "potions";
/* 374 */     float titleX = x + (width - issue(16).getWidth(title)) / 1.9F;
/* 375 */     drawTextWithShadow(eventRender, issue(16), title, titleX, y + 5.0F, -1);
/*     */     
/* 377 */     float yOffset = 20.0F;
/* 378 */     for (class_1293 effect : effects) {
/* 379 */       AnimationUtils anim = getAnimation((class_1291)effect.method_5579().comp_349());
/* 380 */       float animValue = anim.getValue();
/* 381 */       if (animValue <= 0.01F)
/*     */         continue; 
/* 383 */       ScissorUtils.push();
/* 384 */       ScissorUtils.setFromComponentCoordinates(x, y, width, height);
/*     */       
/* 386 */       int alpha = (int)(255.0F * animValue);
/* 387 */       int textColor = ColorUtils.rgba(255, 255, 255, alpha);
/* 388 */       int levelColor = ColorUtils.rgba(20, 185, 45, alpha);
/*     */       
/* 390 */       float iconX = x + 5.0F;
/* 391 */       float iconY = y + yOffset;
/* 392 */       drawEffectIcon(eventRender, effect.method_5579(), iconX, iconY, 11, alpha);
/*     */       
/* 394 */       String baseName = class_1074.method_4662(effect.method_5586(), new Object[0]).toLowerCase();
/* 395 */       String levelSuffix = getLevelSuffix(effect.method_5578() + 1);
/* 396 */       float textX = iconX + 14.0F;
/*     */       
/* 398 */       issue(15).draw(eventRender.getContext().method_51448(), baseName + " >", textX, y + yOffset - 1.0F, textColor);
/* 399 */       if (!levelSuffix.isEmpty()) {
/* 400 */         float nameW = issue(14).getWidth(baseName + " >");
/* 401 */         issue(14).draw(eventRender.getContext().method_51448(), " " + levelSuffix, (textX + nameW + 2.0F), (y + yOffset) - 0.5D, levelColor);
/*     */       } 
/*     */       
/* 404 */       issue(14).draw(eventRender.getContext().method_51448(), formatDuration(effect), textX, (y + yOffset) + 7.5D, textColor);
/*     */       
/* 406 */       yOffset += 18.0F * animValue;
/* 407 */       ScissorUtils.pop();
/* 408 */       ScissorUtils.unset();
/*     */     } 
/*     */     
/* 411 */     this.draggable.setWidth(width);
/* 412 */     this.draggable.setHeight(height);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\base\implement\Potions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */