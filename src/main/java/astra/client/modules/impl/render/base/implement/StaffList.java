/*     */ package shame.astra.client.modules.impl.render.base.implement;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Pattern;
/*     */ import net.minecraft.class_1934;
/*     */ import net.minecraft.class_2561;
/*     */ import net.minecraft.class_2583;
/*     */ import net.minecraft.class_268;
/*     */ import net.minecraft.class_310;
/*     */ import net.minecraft.class_4587;
/*     */ import net.minecraft.class_640;
/*     */ import shame.astra.api.events.implement.EventRender;
/*     */ import shame.astra.api.utils.animation.AnimationUtils;
/*     */ import shame.astra.api.utils.color.ColorUtils;
/*     */ import shame.astra.api.utils.draggable.Draggable;
/*     */ import shame.astra.api.utils.render.RenderUtils;
/*     */ import shame.astra.api.utils.render.font.ReplaceSymbols;
/*     */ import shame.astra.api.utils.render.fonts.msdf.Font;
/*     */ import shame.astra.api.utils.render.fonts.msdf.Fonts;
/*     */ import shame.astra.api.utils.scissor.ScissorUtils;
/*     */ import shame.astra.astra;
/*     */ 
/*     */ public class StaffList extends InterfaceProcessing {
/*     */   private static final int STATUS_VANISH_COLOR = -47526;
/*     */   private static final int STATUS_GM3_COLOR = -9146;
/*     */   private static final int STATUS_ONLINE_COLOR = -10158216;
/*  33 */   private final class_310 mc = class_310.method_1551();
/*     */   
/*  35 */   private final Map<String, StaffData> staffDataCache = new LinkedHashMap<>();
/*  36 */   private final Map<String, Float> staffAnimations = new HashMap<>();
/*  37 */   private final Set<String> activeStaff = new HashSet<>();
/*     */   
/*  39 */   private final Pattern namePattern = Pattern.compile("^\\w{3,16}$");
/*  40 */   private final Set<String> validStaffPrefixes = new HashSet<>();
/*     */   
/*  42 */   private final AnimationUtils widthAnimation = new AnimationUtils(60.0F, 10.5F, Easings.QUAD_OUT);
/*  43 */   private float staffAnimatedHeight = 18.0F;
/*     */   
/*  45 */   private long lastStaffUpdate = 0L;
/*     */   
/*  47 */   private final List<String> visiblePlayers = new ArrayList<>();
/*  48 */   private final Set<String> animationScratch = new HashSet<>();
/*     */   
/*     */   private Font font10;
/*     */   private Font font12;
/*     */   private Font font14;
/*     */   private Font font16;
/*     */   private Font iconFont;
/*     */   
/*     */   public StaffList(Draggable draggable) {
/*  57 */     super(draggable);
/*  58 */     this.validStaffPrefixes.addAll(Arrays.asList(new String[] { "supp", "ꜱupp", "mod", "der", "adm", "wne", "мод", "помо", "адм", "владе", "отри", "таф", "taf", "curat", "курато", "dev", "раз", "сапп", "yt", "ютуб", "стажер", "сотрудник" }));
/*     */   }
/*     */ 
/*     */   
/*     */   private static class PrefixSegment
/*     */   {
/*     */     final String text;
/*     */     
/*     */     final int color;
/*     */     
/*     */     float width12;
/*     */     float width14;
/*     */     
/*     */     PrefixSegment(String text, int color) {
/*  72 */       this.text = text;
/*  73 */       this.color = color;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class StaffData {
/*     */     String status;
/*     */     List<StaffList.PrefixSegment> segments;
/*     */     float prefixWidth12;
/*     */     float prefixWidth14;
/*     */     float nameWidth12;
/*     */     float nameWidth14;
/*     */     
/*     */     StaffData(String status) {
/*  86 */       this.status = status;
/*  87 */       this.segments = new ArrayList<>();
/*     */     }
/*     */   }
/*     */   
/*     */   private void initFonts() {
/*  92 */     if (this.font10 == null) {
/*  93 */       this.font10 = Fonts.getFont("suisse", 10);
/*  94 */       this.font12 = Fonts.getFont("suisse", 12);
/*  95 */       this.font14 = Fonts.getFont("suisse", 14);
/*  96 */       this.font16 = Fonts.getFont("suisse", 16);
/*  97 */       this.iconFont = Fonts.getFont("icon", 13);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRender(EventRender.Default eventRender) {
/* 103 */     if (this.mc.field_1724 == null || this.mc.field_1687 == null)
/*     */       return; 
/* 105 */     initFonts();
/*     */     
/* 107 */     long currentTime = System.currentTimeMillis();
/* 108 */     if (currentTime - this.lastStaffUpdate > 500L) {
/* 109 */       updateStaffCache();
/* 110 */       this.lastStaffUpdate = currentTime;
/*     */     } 
/*     */     
/* 113 */     updateAnimations();
/*     */     
/* 115 */     if (ModuleClass.interfaceModule.style.is("Обычный")) {
/* 116 */       renderDefaultStyle(eventRender);
/*     */     } else {
/* 118 */       renderWaveStyle(eventRender);
/*     */     } 
/*     */     
/* 121 */     super.onRender(eventRender);
/*     */   }
/*     */   
/*     */   private boolean matchesStaffPrefix(String prefix) {
/* 125 */     String lower = prefix.toLowerCase(Locale.ROOT);
/* 126 */     for (String p : this.validStaffPrefixes) {
/* 127 */       if (lower.contains(p)) return true; 
/*     */     } 
/* 129 */     return false;
/*     */   }
/*     */   
/*     */   private List<PrefixSegment> parsePrefix(class_2561 prefix) {
/* 133 */     List<PrefixSegment> segments = new ArrayList<>();
/*     */     
/* 135 */     prefix.method_27658((style, string) -> { if (string == null || string.isEmpty()) return Optional.empty();  appendPrefixSegments(segments, string, (style.method_10973() != null) ? style.method_10973().method_27716() : 16777215); return Optional.empty(); }class_2583.field_24360);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 143 */     return segments;
/*     */   }
/*     */   
/*     */   private void appendPrefixSegments(List<PrefixSegment> segments, String text, int baseColor) {
/* 147 */     int currentColor = baseColor;
/* 148 */     StringBuilder chunk = new StringBuilder();
/* 149 */     int chunkColor = currentColor;
/*     */     int offset;
/* 151 */     for (offset = 0; offset < text.length(); ) {
/* 152 */       int codePoint = text.codePointAt(offset);
/* 153 */       int charCount = Character.charCount(codePoint);
/*     */       
/* 155 */       if (codePoint == 167 && offset + charCount < text.length()) {
/* 156 */         flushPrefixSegment(segments, chunk, chunkColor);
/* 157 */         char code = Character.toLowerCase(text.charAt(offset + charCount));
/* 158 */         Integer mappedColor = sectionColorToRgb(code);
/* 159 */         currentColor = (mappedColor != null) ? mappedColor.intValue() : ((code == 'r') ? baseColor : currentColor);
/* 160 */         chunkColor = currentColor;
/* 161 */         offset += charCount + 1;
/*     */         
/*     */         continue;
/*     */       } 
/* 165 */       String replacement = ReplaceSymbols.replaceCodePoint(codePoint);
/* 166 */       if (replacement != null) {
/* 167 */         flushPrefixSegment(segments, chunk, chunkColor);
/* 168 */         int totalChars = Math.max(1, replacement.length());
/* 169 */         for (int i = 0; i < replacement.length(); i++) {
/* 170 */           int replacementColor = ReplaceSymbols.getGradientColorForReplacement(codePoint, i, totalChars, 1.0F, currentColor);
/* 171 */           if (chunk.length() > 0 && chunkColor != replacementColor) {
/* 172 */             flushPrefixSegment(segments, chunk, chunkColor);
/*     */           }
/* 174 */           chunkColor = replacementColor;
/* 175 */           chunk.append(replacement.charAt(i));
/*     */         } 
/* 177 */         offset += charCount;
/*     */         
/*     */         continue;
/*     */       } 
/* 181 */       if (chunk.length() > 0 && chunkColor != currentColor) {
/* 182 */         flushPrefixSegment(segments, chunk, chunkColor);
/*     */       }
/* 184 */       chunkColor = currentColor;
/* 185 */       chunk.appendCodePoint(codePoint);
/* 186 */       offset += charCount;
/*     */     } 
/*     */     
/* 189 */     flushPrefixSegment(segments, chunk, chunkColor);
/*     */   }
/*     */   
/*     */   private void flushPrefixSegment(List<PrefixSegment> segments, StringBuilder chunk, int color) {
/* 193 */     if (chunk.isEmpty())
/* 194 */       return;  String text = chunk.toString();
/* 195 */     PrefixSegment seg = new PrefixSegment(text, color);
/* 196 */     seg.width12 = this.font12.getWidth(text);
/* 197 */     seg.width14 = this.font14.getWidth(text);
/* 198 */     segments.add(seg);
/* 199 */     chunk.setLength(0);
/*     */   }
/*     */   
/*     */   private Integer sectionColorToRgb(char code) {
/* 203 */     switch (code) { case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9': case 'a': case 'b': case 'c': case 'd': case 'e': case 'f':  }  return 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 220 */       null;
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateStaffCache() {
/* 225 */     this.activeStaff.clear();
/* 226 */     String selfName = this.mc.field_1724.method_5477().getString();
/*     */     
/* 228 */     for (class_268 team : this.mc.field_1687.method_8428().method_1159()) {
/* 229 */       Collection<String> players = team.method_1204();
/* 230 */       if (players.size() != 1)
/*     */         continue; 
/* 232 */       String name = players.iterator().next();
/* 233 */       if (!this.namePattern.matcher(name).matches() || 
/* 234 */         name.equals(selfName))
/*     */         continue; 
/* 236 */       class_640 info = this.mc.method_1562().method_2874(name);
/* 237 */       boolean vanish = (info == null);
/* 238 */       boolean isGM3 = (info != null && info.method_2958() == class_1934.field_9219);
/*     */       
/* 240 */       class_2561 prefixText = team.method_1144();
/* 241 */       String prefixStr = prefixText.getString();
/* 242 */       boolean matchesPrefix = matchesStaffPrefix(prefixStr);
/* 243 */       boolean isInStaffList = astra.INSTANCE.staffStorage.isStaff(name);
/*     */       
/* 245 */       if (matchesPrefix || vanish || isGM3 || isInStaffList) {
/* 246 */         String status; this.activeStaff.add(name);
/*     */ 
/*     */         
/* 249 */         if (vanish) {
/* 250 */           status = "VANISH";
/* 251 */         } else if (isGM3) {
/* 252 */           status = "GM3";
/*     */         } else {
/* 254 */           status = "ONLINE";
/*     */         } 
/*     */         
/* 257 */         StaffData existing = this.staffDataCache.get(name);
/* 258 */         if (existing == null) {
/* 259 */           existing = new StaffData(status);
/* 260 */           this.staffDataCache.put(name, existing);
/*     */         } 
/* 262 */         existing.status = status;
/* 263 */         existing.segments = parsePrefix(prefixText);
/* 264 */         calculateWidths(existing, name);
/*     */       } 
/*     */     } 
/*     */     
/* 268 */     for (String staffName : astra.INSTANCE.staffStorage.getStaffs()) {
/* 269 */       String status; if (staffName.equals(selfName) || 
/* 270 */         !this.namePattern.matcher(staffName).matches() || 
/* 271 */         this.activeStaff.contains(staffName))
/*     */         continue; 
/* 273 */       this.activeStaff.add(staffName);
/*     */       
/* 275 */       class_640 info = this.mc.method_1562().method_2874(staffName);
/*     */       
/* 277 */       if (info == null) {
/* 278 */         status = "VANISH";
/* 279 */       } else if (info.method_2958() == class_1934.field_9219) {
/* 280 */         status = "GM3";
/*     */       } else {
/* 282 */         status = "ONLINE";
/*     */       } 
/*     */       
/* 285 */       StaffData existing = this.staffDataCache.get(staffName);
/* 286 */       if (existing == null) {
/* 287 */         existing = new StaffData(status);
/* 288 */         existing.segments = new ArrayList<>();
/* 289 */         calculateWidths(existing, staffName);
/* 290 */         this.staffDataCache.put(staffName, existing); continue;
/*     */       } 
/* 292 */       existing.status = status;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void calculateWidths(StaffData data, String name) {
/* 298 */     data.prefixWidth12 = 0.0F;
/* 299 */     data.prefixWidth14 = 0.0F;
/* 300 */     for (PrefixSegment seg : data.segments) {
/* 301 */       data.prefixWidth12 += seg.width12;
/* 302 */       data.prefixWidth14 += seg.width14;
/*     */     } 
/* 304 */     data.nameWidth12 = this.font12.getWidth(name);
/* 305 */     data.nameWidth14 = this.font14.getWidth(name + " >> ");
/*     */   }
/*     */   
/*     */   private void updateAnimations() {
/* 309 */     float lerpSpeed = 0.1F;
/*     */     
/* 311 */     this.animationScratch.clear();
/* 312 */     this.animationScratch.addAll(this.staffAnimations.keySet());
/* 313 */     this.animationScratch.addAll(this.activeStaff);
/*     */     
/* 315 */     for (String playerName : this.animationScratch) {
/* 316 */       boolean isActive = this.activeStaff.contains(playerName);
/* 317 */       float targetAnim = isActive ? 1.0F : 0.0F;
/* 318 */       float currentAnim = ((Float)this.staffAnimations.getOrDefault(playerName, Float.valueOf(0.0F))).floatValue();
/* 319 */       currentAnim += (targetAnim - currentAnim) * lerpSpeed;
/* 320 */       this.staffAnimations.put(playerName, Float.valueOf(currentAnim));
/*     */     } 
/*     */     
/* 323 */     Iterator<Map.Entry<String, Float>> animIt = this.staffAnimations.entrySet().iterator();
/* 324 */     while (animIt.hasNext()) {
/* 325 */       Map.Entry<String, Float> entry = animIt.next();
/* 326 */       if (((Float)entry.getValue()).floatValue() < 0.01F && !this.activeStaff.contains(entry.getKey())) {
/* 327 */         animIt.remove();
/* 328 */         this.staffDataCache.remove(entry.getKey());
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private List<String> getVisiblePlayers() {
/* 334 */     this.visiblePlayers.clear();
/* 335 */     for (Map.Entry<String, Float> entry : this.staffAnimations.entrySet()) {
/* 336 */       if (((Float)entry.getValue()).floatValue() > 0.01F) {
/* 337 */         this.visiblePlayers.add(entry.getKey());
/*     */       }
/*     */     } 
/* 340 */     Collections.sort(this.visiblePlayers);
/* 341 */     return this.visiblePlayers;
/*     */   }
/*     */   
/*     */   private int getStatusColor(String status) {
/* 345 */     switch (status) { case "VANISH": case "GM3":  }  return 
/*     */ 
/*     */       
/* 348 */       -10158216;
/*     */   }
/*     */ 
/*     */   
/*     */   private float getStatusBoxWidth(String status) {
/* 353 */     return 12.0F;
/*     */   }
/*     */   private void renderDefaultStyle(EventRender.Default eventRender) {
/*     */     int colorTheme;
/* 357 */     float x = this.draggable.getX();
/* 358 */     float y = this.draggable.getY();
/* 359 */     class_4587 matrices = eventRender.getContext().method_51448();
/*     */ 
/*     */     
/* 362 */     if (!astra.INSTANCE.themeStorage.getThemes().getTheme().getName().equals("Rainbow")) {
/* 363 */       colorTheme = (astra.INSTANCE.themeStorage.getThemes().getTheme()).color[0];
/*     */     } else {
/* 365 */       colorTheme = ColorUtils.getThemeColor();
/*     */     } 
/*     */     
/* 368 */     List<String> visiblePlayers = getVisiblePlayers();
/*     */     
/* 370 */     float maxWidth = 60.0F;
/* 371 */     float headerHeight = 16.0F;
/* 372 */     float itemHeight = 12.0F;
/* 373 */     float padding = 5.0F;
/* 374 */     float statusPadding = 4.0F;
/*     */     
/* 376 */     for (String playerName : visiblePlayers) {
/* 377 */       StaffData data = this.staffDataCache.get(playerName);
/* 378 */       if (data != null) {
/* 379 */         float statusBoxW = getStatusBoxWidth(data.status);
/* 380 */         float totalW = padding + data.prefixWidth12 + data.nameWidth12 + statusPadding + statusBoxW + padding;
/* 381 */         if (totalW > maxWidth) {
/* 382 */           maxWidth = totalW;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 387 */     this.widthAnimation.update(maxWidth);
/* 388 */     float width = this.widthAnimation.getValue();
/*     */     
/* 390 */     float contentHeight = 0.0F;
/* 391 */     for (String playerName : visiblePlayers) {
/* 392 */       contentHeight += itemHeight * ((Float)this.staffAnimations.getOrDefault(playerName, Float.valueOf(0.0F))).floatValue();
/*     */     }
/*     */     
/* 395 */     float targetHeight = visiblePlayers.isEmpty() ? headerHeight : (headerHeight + contentHeight + 2.0F);
/* 396 */     this.staffAnimatedHeight += (targetHeight - this.staffAnimatedHeight) * 0.12F;
/* 397 */     float height = this.staffAnimatedHeight;
/*     */     
/* 399 */     RenderUtils.drawDefaultHudElementRects(matrices, x, y, width, height, colorTheme, isUnusualRectType());
/*     */     
/* 401 */     this.font14.draw(matrices, "Staff", x + 5.0F, y + 6.0F, -1);
/* 402 */     this.iconFont.draw(matrices, "y", x + width - 13.0F, y + 7.5F, colorTheme);
/*     */     
/* 404 */     float offsetY = 18.0F;
/* 405 */     ScissorUtils.push();
/* 406 */     ScissorUtils.setFromComponentCoordinates(x, y, width, height);
/* 407 */     for (String playerName : visiblePlayers) {
/* 408 */       float anim = ((Float)this.staffAnimations.getOrDefault(playerName, Float.valueOf(0.0F))).floatValue();
/* 409 */       if (anim <= 0.01F)
/*     */         continue; 
/* 411 */       StaffData data = this.staffDataCache.get(playerName);
/* 412 */       if (data == null)
/*     */         continue; 
/* 414 */       int alpha = (int)(255.0F * anim);
/* 415 */       float yOffset = -5.0F * (1.0F - anim);
/*     */       
/* 417 */       float currentX = x + padding;
/* 418 */       for (int i = 0; i < data.segments.size(); i++) {
/* 419 */         PrefixSegment seg = data.segments.get(i);
/* 420 */         int color = ColorUtils.setAlphaColor(seg.color, alpha);
/* 421 */         this.font12.draw(matrices, seg.text, currentX, y + offsetY + 2.0F + yOffset, color);
/* 422 */         currentX += seg.width12;
/*     */       } 
/*     */       
/* 425 */       this.font12.draw(matrices, playerName, currentX, y + offsetY + 2.0F + yOffset, ColorUtils.rgba(255, 255, 255, alpha));
/*     */       
/* 427 */       float statusBoxWidth = getStatusBoxWidth(data.status);
/* 428 */       float statusBoxX = x + width - statusBoxWidth - padding;
/* 429 */       float statusBoxY = y + offsetY + 1.0F + yOffset;
/* 430 */       int statusRectColor = ColorUtils.setAlphaColor(getStatusColor(data.status), alpha);
/* 431 */       RenderUtils.drawRoundedRect(matrices, statusBoxX + 4.0F, statusBoxY + 1.5F, statusBoxWidth - 4.5F, 3.45F, 0.55F, statusRectColor);
/*     */       
/* 433 */       offsetY += itemHeight * anim;
/*     */     } 
/* 435 */     ScissorUtils.pop();
/* 436 */     ScissorUtils.unset();
/*     */     
/* 438 */     this.draggable.setWidth(width);
/* 439 */     this.draggable.setHeight(height);
/*     */   }
/*     */   
/*     */   private void renderWaveStyle(EventRender.Default eventRender) {
/* 443 */     float x = this.draggable.getX();
/* 444 */     float y = this.draggable.getY();
/* 445 */     class_4587 matrices = eventRender.getContext().method_51448();
/*     */     
/* 447 */     int time = (int)((float)(System.currentTimeMillis() % 2000L) / 2000.0F * 360.0F);
/*     */     
/* 449 */     int leftTop = ColorUtils.getThemeColor(time);
/* 450 */     int leftBottom = ColorUtils.getThemeColor(time + 30);
/* 451 */     int centerTop = ColorUtils.getThemeColor(time + 90);
/* 452 */     int centerBottom = ColorUtils.getThemeColor(time + 120);
/* 453 */     int rightTop = ColorUtils.getThemeColor(time + 180);
/* 454 */     int rightBottom = ColorUtils.getThemeColor(time + 210);
/*     */     
/* 456 */     List<String> visiblePlayers = getVisiblePlayers();
/*     */     
/* 458 */     float maxWidth = 80.0F;
/* 459 */     float headerHeight = 18.0F;
/* 460 */     float itemHeight = 10.0F;
/* 461 */     float padding = 5.0F;
/*     */     
/* 463 */     for (String playerName : visiblePlayers) {
/* 464 */       StaffData data = this.staffDataCache.get(playerName);
/* 465 */       if (data != null) {
/* 466 */         float statusW = this.font12.getWidth(data.status);
/* 467 */         float totalW = padding + data.prefixWidth14 + data.nameWidth14 + statusW + padding;
/* 468 */         if (totalW > maxWidth) {
/* 469 */           maxWidth = totalW;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 474 */     float width = maxWidth;
/*     */     
/* 476 */     float contentHeight = 0.0F;
/* 477 */     for (String playerName : visiblePlayers) {
/* 478 */       contentHeight += itemHeight * ((Float)this.staffAnimations.getOrDefault(playerName, Float.valueOf(0.0F))).floatValue();
/*     */     }
/*     */     
/* 481 */     float height = visiblePlayers.isEmpty() ? headerHeight : (headerHeight + contentHeight + 4.0F);
/*     */     
/* 483 */     if (visiblePlayers.isEmpty()) {
/* 484 */       RenderUtils.drawWaveHudHeader(matrices, x, y, width, 15.0F, 0.0F, 10.0F, 10.0F, leftTop, leftBottom, centerTop, centerBottom, rightTop, rightBottom);
/*     */       
/* 486 */       float f = x + (width - this.font16.getWidth("stafflist")) / 2.0F;
/* 487 */       this.font16.drawStringWithShadow(matrices, "stafflist", f, y + 5.0F, -1);
/* 488 */       this.draggable.setWidth(width);
/* 489 */       this.draggable.setHeight(headerHeight);
/*     */       
/*     */       return;
/*     */     } 
/* 493 */     RenderUtils.drawWaveHudPanel(matrices, x, y, width, height, ColorUtils.rgba(25, 25, 25, 150), 15.0F, 0.0F, 10.0F, 10.0F, leftTop, leftBottom, centerTop, centerBottom, rightTop, rightBottom);
/*     */ 
/*     */ 
/*     */     
/* 497 */     float titleX = x + (width - this.font16.getWidth("stafflist")) / 1.9F;
/* 498 */     this.font16.drawStringWithShadow(matrices, "stafflist", titleX, y + 5.0F, -1);
/*     */     
/* 500 */     float yOffsetPos = 20.0F;
/* 501 */     ScissorUtils.push();
/* 502 */     ScissorUtils.setFromComponentCoordinates(x, y, width, height);
/* 503 */     for (String playerName : visiblePlayers) {
/* 504 */       float anim = ((Float)this.staffAnimations.getOrDefault(playerName, Float.valueOf(0.0F))).floatValue();
/* 505 */       if (anim <= 0.01F)
/*     */         continue; 
/* 507 */       StaffData data = this.staffDataCache.get(playerName);
/* 508 */       if (data == null)
/*     */         continue; 
/* 510 */       int alpha = (int)(255.0F * anim);
/* 511 */       float yOffset = -5.0F * (1.0F - anim);
/*     */       
/* 513 */       float textX = x + padding;
/* 514 */       for (int i = 0; i < data.segments.size(); i++) {
/* 515 */         PrefixSegment seg = data.segments.get(i);
/* 516 */         int color = ColorUtils.setAlphaColor(seg.color, alpha);
/* 517 */         this.font14.draw(matrices, seg.text, textX, y + yOffsetPos + 1.5F + yOffset, color);
/* 518 */         textX += seg.width14;
/*     */       } 
/*     */       
/* 521 */       this.font14.draw(matrices, playerName + " >> ", textX, y + yOffsetPos + 1.5F + yOffset, ColorUtils.rgba(255, 255, 255, alpha));
/* 522 */       float nameArrowWidth = this.font14.getWidth(playerName + " >> ");
/* 523 */       this.font12.draw(matrices, data.status, textX + nameArrowWidth, y + yOffsetPos + 2.5F + yOffset, ColorUtils.setAlphaColor(getStatusColor(data.status), alpha));
/*     */       
/* 525 */       yOffsetPos += itemHeight * anim;
/*     */     } 
/* 527 */     ScissorUtils.pop();
/* 528 */     ScissorUtils.unset();
/*     */     
/* 530 */     this.draggable.setWidth(width);
/* 531 */     this.draggable.setHeight(height);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\base\implement\StaffList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */