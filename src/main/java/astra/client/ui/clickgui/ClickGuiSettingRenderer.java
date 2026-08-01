/*     */ package shame.astra.client.ui.clickgui;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.class_332;
/*     */ import net.minecraft.class_4587;
/*     */ import shame.astra.api.utils.animation.AnimationUtils;
/*     */ import shame.astra.api.utils.color.ColorUtils;
/*     */ import shame.astra.api.utils.input.KeyBoardUtils;
/*     */ import shame.astra.api.utils.render.RenderUtils;
/*     */ import shame.astra.api.utils.render.fonts.msdf.Font;
/*     */ import shame.astra.api.utils.render.fonts.msdf.Fonts;
/*     */ import shame.astra.api.utils.scissor.ScissorUtils;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.BindSetting;
/*     */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*     */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*     */ import shame.astra.client.modules.settings.implement.ListSetting;
/*     */ import shame.astra.client.modules.settings.implement.ModeSetting;
/*     */ import shame.astra.client.modules.settings.implement.TextSetting;
/*     */ 
/*     */ public class ClickGuiSettingRenderer
/*     */ {
/*     */   private static final float HOVER_SCROLL_OVERFLOW_THRESHOLD = 6.0F;
/*     */   
/*     */   public void render(class_332 context, Module module, float panelX, float moduleY, float openProgress, int colorTheme, double mouseX, double mouseY, ClickGuiState state) {
/*  27 */     List<Setting> settings = module.getSettings();
/*  28 */     if (settings == null || settings.isEmpty() || openProgress <= 0.01F) {
/*     */       return;
/*     */     }
/*     */     
/*  32 */     float maxSettingHeight = ClickGuiLayout.calculateSettingsHeight(module);
/*  33 */     float settingsClipY = moduleY + 20.0F;
/*  34 */     float settingsClipHeight = maxSettingHeight * openProgress;
/*     */     
/*  36 */     ScissorUtils.push();
/*  37 */     ScissorUtils.setFromComponentCoordinates((panelX + 3.0F), settingsClipY, 93.5D, settingsClipHeight);
/*     */     
/*  39 */     float settingYoffset = 20.0F;
/*  40 */     for (Setting setting : settings) {
/*  41 */       if (setting == null || !setting.visible().booleanValue()) {
/*     */         continue;
/*     */       }
/*     */       
/*  45 */       float settingY = moduleY + settingYoffset + 4.0F;
/*  46 */       int alpha = (int)(255.0F * openProgress);
/*     */       
/*  48 */       if (setting instanceof BooleanSetting) { BooleanSetting booleanSetting = (BooleanSetting)setting;
/*  49 */         renderBooleanSetting(context, panelX, settingY, alpha, colorTheme, mouseX, mouseY, booleanSetting, state);
/*  50 */         settingYoffset += 12.0F; continue; }
/*  51 */        if (setting instanceof TextSetting) { TextSetting textSetting = (TextSetting)setting;
/*  52 */         renderTextSetting(context, panelX, settingY, alpha, colorTheme, mouseX, mouseY, textSetting, state);
/*  53 */         settingYoffset += 22.0F; continue; }
/*  54 */        if (setting instanceof FloatSetting) { FloatSetting floatSetting = (FloatSetting)setting;
/*  55 */         renderFloatSetting(context, panelX, settingY, alpha, colorTheme, mouseX, mouseY, floatSetting, state);
/*  56 */         settingYoffset += 22.0F; continue; }
/*  57 */        if (setting instanceof ModeSetting) { ModeSetting modeSetting = (ModeSetting)setting;
/*  58 */         renderModeSetting(context, panelX, settingY, alpha, colorTheme, mouseX, mouseY, modeSetting, state);
/*  59 */         settingYoffset += ClickGuiLayout.calculateModeSettingHeight(modeSetting); continue; }
/*  60 */        if (setting instanceof ListSetting) { ListSetting listSetting = (ListSetting)setting;
/*  61 */         renderListSetting(context, panelX, settingY, alpha, colorTheme, mouseX, mouseY, listSetting, state);
/*  62 */         settingYoffset += ClickGuiLayout.calculateListSettingHeight(listSetting); continue; }
/*  63 */        if (setting instanceof BindSetting) { BindSetting bindSetting = (BindSetting)setting;
/*  64 */         renderBindSetting(context, panelX, settingY, alpha, colorTheme, mouseX, mouseY, bindSetting, state);
/*  65 */         settingYoffset += 12.0F; }
/*     */     
/*     */     } 
/*     */     
/*  69 */     ScissorUtils.pop();
/*     */   }
/*     */   
/*     */   private void renderBooleanSetting(class_332 context, float panelX, float settingY, int alpha, int colorTheme, double mouseX, double mouseY, BooleanSetting booleanSetting, ClickGuiState state) {
/*  73 */     AnimationUtils backgroundAnimation = state.getBooleanBackgroundAnimation(booleanSetting);
/*  74 */     AnimationUtils circleAnimation = state.getBooleanCircleAnimation(booleanSetting);
/*  75 */     backgroundAnimation.update(booleanSetting.isState() ? 1.0F : 0.0F);
/*  76 */     circleAnimation.update(booleanSetting.isState() ? 1.0F : 0.0F);
/*     */     
/*  78 */     float backgroundProgress = backgroundAnimation.getValue();
/*  79 */     float circleProgress = circleAnimation.getValue();
/*     */     
/*  81 */     int offColor = ColorUtils.darken(colorTheme, 0.05F);
/*  82 */     int onColor = colorTheme;
/*     */     
/*  84 */     int r = (int)((offColor >> 16 & 0xFF) + ((onColor >> 16 & 0xFF) - (offColor >> 16 & 0xFF)) * backgroundProgress);
/*  85 */     int g = (int)((offColor >> 8 & 0xFF) + ((onColor >> 8 & 0xFF) - (offColor >> 8 & 0xFF)) * backgroundProgress);
/*  86 */     int b = (int)((offColor & 0xFF) + ((onColor & 0xFF) - (offColor & 0xFF)) * backgroundProgress);
/*  87 */     int a = (int)((offColor >> 24 & 0xFF) + ((onColor >> 24 & 0xFF) - (offColor >> 24 & 0xFF)) * backgroundProgress);
/*  88 */     int interpolatedColor = a << 24 | r << 16 | g << 8 | b;
/*     */     
/*  90 */     float maxWidth = panelX + 73.0F - panelX + 10.0F;
/*  91 */     drawStringWithHoverScroll(
/*  92 */         issue(13), context
/*  93 */         .method_51448(), booleanSetting
/*  94 */         .name(), panelX + 10.0F, settingY, maxWidth, 
/*     */ 
/*     */ 
/*     */         
/*  98 */         getPrimarySettingColor(alpha), mouseX, mouseY, state, 
/*     */ 
/*     */ 
/*     */         
/* 102 */         getSettingTextKey((Setting)booleanSetting));
/*     */ 
/*     */     
/* 105 */     RenderUtils.drawRoundedRect(context
/* 106 */         .method_51448(), panelX + 75.0F, settingY - 2.0F, 16.0F, 9.0F, 3.5F, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 112 */         ColorUtils.rgba(interpolatedColor >> 16 & 0xFF, interpolatedColor >> 8 & 0xFF, interpolatedColor & 0xFF, alpha));
/*     */ 
/*     */     
/* 115 */     float circleX = panelX + 79.5F + circleProgress * 6.2F;
/* 116 */     RenderUtils.drawRoundCircle(context.method_51448(), circleX + 0.5F, settingY + 2.5F, 7.0F, ColorUtils.rgba(255, 255, 255, alpha));
/*     */   }
/*     */   
/*     */   private void renderFloatSetting(class_332 context, float panelX, float settingY, int alpha, int colorTheme, double mouseX, double mouseY, FloatSetting floatSetting, ClickGuiState state) {
/* 120 */     if (floatSetting.isActive()) {
/* 121 */       floatSetting.setValue(state.updateActiveSliderValue(floatSetting, mouseX));
/*     */     }
/*     */     
/* 124 */     AnimationUtils sliderAnimation = state.getSliderAnimation(floatSetting);
/* 125 */     sliderAnimation.update(state.getSliderPos(floatSetting));
/* 126 */     float animatedPos = sliderAnimation.getValue();
/*     */     
/* 128 */     String valueString = formatSliderValue(floatSetting);
/* 129 */     float valueX = panelX + 89.0F - issue(12).getWidth(valueString);
/* 130 */     float nameMaxWidth = valueX - 4.0F - panelX + 10.0F;
/*     */     
/* 132 */     drawStringWithHoverScroll(
/* 133 */         issue(12), context
/* 134 */         .method_51448(), floatSetting
/* 135 */         .name(), panelX + 10.0F, settingY + 1.0F, nameMaxWidth, 
/*     */ 
/*     */ 
/*     */         
/* 139 */         getPrimarySettingColor(alpha), mouseX, mouseY, state, 
/*     */ 
/*     */ 
/*     */         
/* 143 */         getSettingTextKey((Setting)floatSetting));
/*     */ 
/*     */     
/* 146 */     issue(12).drawString(context.method_51448(), valueString, valueX, settingY + 1.0F, ColorUtils.setAlphaColor(colorTheme, alpha));
/*     */     
/* 148 */     int sliderBackgroundColor = ColorUtils.setAlphaColor(ColorUtils.darken(colorTheme, 0.2F), alpha);
/* 149 */     RenderUtils.drawRoundedRect(context.method_51448(), panelX + 10.0F, settingY + 9.0F, 79.0F, 4.5F, 1.25F, sliderBackgroundColor);
/*     */     
/* 151 */     int sliderFillColor = ColorUtils.setAlphaColor(colorTheme, alpha);
/* 152 */     RenderUtils.drawRoundedRect(context.method_51448(), panelX + 10.0F, settingY + 9.0F, animatedPos * 79.0F, 4.5F, 1.25F, sliderFillColor);
/* 153 */     RenderUtils.drawRoundCircle(context.method_51448(), panelX + 10.0F + animatedPos * 79.0F, settingY + 11.25F, 6.0F, ColorUtils.setAlphaColor(-1, alpha));
/*     */   }
/*     */   
/*     */   private void renderTextSetting(class_332 context, float panelX, float settingY, int alpha, int colorTheme, double mouseX, double mouseY, TextSetting textSetting, ClickGuiState state) {
/* 157 */     String value = textSetting.get();
/* 158 */     boolean editing = (state.getEditingTextSetting() == textSetting);
/* 159 */     String preview = (value == null || value.isEmpty()) ? "..." : value;
/* 160 */     String boxText = editing ? (preview + "_") : preview;
/* 161 */     float boxWidth = 42.0F;
/* 162 */     float boxX = panelX + 49.0F;
/*     */     
/* 164 */     drawStringWithHoverScroll(
/* 165 */         issue(13), context
/* 166 */         .method_51448(), textSetting
/* 167 */         .name(), panelX + 10.0F, settingY, boxX - 1.0F - panelX + 10.0F, 
/*     */ 
/*     */ 
/*     */         
/* 171 */         getPrimarySettingColor(alpha), mouseX, mouseY, state, 
/*     */ 
/*     */ 
/*     */         
/* 175 */         getSettingTextKey((Setting)textSetting));
/*     */ 
/*     */     
/* 178 */     int background = ColorUtils.setAlphaColor(editing ? colorTheme : ColorUtils.darken(colorTheme, 0.15F), alpha);
/* 179 */     int textColor = ColorUtils.setAlphaColor(-1, alpha);
/* 180 */     float boxY = settingY - 2.5F;
/* 181 */     RenderUtils.drawRoundedRect(context.method_51448(), boxX, boxY, boxWidth, 9.0F, 1.5F, background);
/* 182 */     ScissorUtils.push();
/* 183 */     ScissorUtils.setFromComponentCoordinates((boxX + 2.0F), (boxY + 1.0F), (boxWidth - 4.0F), 7.0D);
/* 184 */     issue(12).drawString(context.method_51448(), boxText, boxX + 3.0F, settingY + 1.0F, textColor);
/* 185 */     ScissorUtils.pop();
/*     */   }
/*     */   
/*     */   private void renderModeSetting(class_332 context, float panelX, float settingY, int alpha, int colorTheme, double mouseX, double mouseY, ModeSetting modeSetting, ClickGuiState state) {
/* 189 */     drawStringWithHoverScroll(
/* 190 */         issue(12), context
/* 191 */         .method_51448(), modeSetting
/* 192 */         .name(), panelX + 10.0F, settingY + 1.0F, 79.0F, 
/*     */ 
/*     */ 
/*     */         
/* 196 */         getPrimarySettingColor(alpha), mouseX, mouseY, state, 
/*     */ 
/*     */ 
/*     */         
/* 200 */         getSettingTextKey((Setting)modeSetting));
/*     */ 
/*     */     
/* 203 */     float modeY = settingY + 10.0F;
/* 204 */     for (String mode : modeSetting.getMods()) {
/* 205 */       boolean selected = modeSetting.getCurrent().equals(mode);
/* 206 */       AnimationUtils animation = state.getModeAnimation(getModeKey(modeSetting, mode), selected);
/* 207 */       animation.update(selected ? 1.0F : 0.0F);
/* 208 */       float progress = animation.getValue();
/*     */       
/* 210 */       int outerColor = ColorUtils.setAlphaColor(colorTheme, (int)(alpha * (0.3F + 0.7F * progress)));
/* 211 */       int innerColor = selected ? ColorUtils.setAlphaColor(ColorUtils.darken(colorTheme, 0.4F), alpha) : ColorUtils.rgba(255, 255, 255, alpha);
/*     */       
/* 213 */       issue(13).draw(context.method_51448(), mode, panelX + 10.0F, modeY, getSecondarySettingColor(alpha));
/* 214 */       RenderUtils.drawRoundCircle(context.method_51448(), panelX + 86.0F, modeY + 2.0F, 9.0F, outerColor);
/* 215 */       RenderUtils.drawRoundCircle(context.method_51448(), panelX + 86.0F, modeY + 2.0F, 6.0F - progress * 2.0F + 3.0F, innerColor);
/*     */       
/* 217 */       modeY += 10.0F;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void renderListSetting(class_332 context, float panelX, float settingY, int alpha, int colorTheme, double mouseX, double mouseY, ListSetting listSetting, ClickGuiState state) {
/* 222 */     drawStringWithHoverScroll(
/* 223 */         issue(12), context
/* 224 */         .method_51448(), listSetting
/* 225 */         .name(), panelX + 10.0F, settingY + 1.0F, 79.0F, 
/*     */ 
/*     */ 
/*     */         
/* 229 */         getPrimarySettingColor(alpha), mouseX, mouseY, state, 
/*     */ 
/*     */ 
/*     */         
/* 233 */         getSettingTextKey((Setting)listSetting));
/*     */ 
/*     */     
/* 236 */     float listY = settingY + 10.0F;
/* 237 */     for (BooleanSetting entry : listSetting.getSettings()) {
/* 238 */       if (!entry.visible().booleanValue()) {
/*     */         continue;
/*     */       }
/*     */       
/* 242 */       boolean selected = entry.isState();
/* 243 */       AnimationUtils animation = state.getListAnimation(getListKey(listSetting, entry), selected);
/* 244 */       animation.update(selected ? 1.0F : 0.0F);
/* 245 */       float progress = animation.getValue();
/*     */       
/* 247 */       int outerColor = ColorUtils.setAlphaColor(colorTheme, (int)(alpha * (0.3F + 0.7F * progress)));
/* 248 */       int innerColor = selected ? ColorUtils.setAlphaColor(ColorUtils.darken(colorTheme, 0.4F), alpha) : ColorUtils.rgba(255, 255, 255, alpha);
/*     */       
/* 250 */       drawStringWithHoverScroll(
/* 251 */           issue(13), context
/* 252 */           .method_51448(), entry
/* 253 */           .name(), panelX + 10.0F, listY, panelX + 73.0F - panelX + 10.0F, 
/*     */ 
/*     */ 
/*     */           
/* 257 */           getSecondarySettingColor(alpha), mouseX, mouseY, state, 
/*     */ 
/*     */ 
/*     */           
/* 261 */           getListKey(listSetting, entry) + "_text");
/*     */       
/* 263 */       RenderUtils.drawRoundCircle(context.method_51448(), panelX + 86.0F, listY + 2.0F, 9.0F, outerColor);
/* 264 */       RenderUtils.drawRoundCircle(context.method_51448(), panelX + 86.0F, listY + 2.0F, 6.0F - progress * 2.0F + 3.0F, innerColor);
/*     */       
/* 266 */       listY += 10.0F;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void renderBindSetting(class_332 context, float panelX, float settingY, int alpha, int colorTheme, double mouseX, double mouseY, BindSetting bindSetting, ClickGuiState state) {
/* 271 */     boolean binding = (state.getBindingSetting() == bindSetting);
/* 272 */     AnimationUtils bindAnimation = state.getBindAnimation(getBindKey(bindSetting), binding);
/* 273 */     bindAnimation.update(binding ? 1.0F : 0.0F);
/* 274 */     float progress = bindAnimation.getValue();
/*     */     
/* 276 */     String bindString = binding ? "..." : state.toEnglish(KeyBoardUtils.getBindName(bindSetting.getKey()));
/* 277 */     float bindTextWidth = issue(12).getWidth(bindString);
/* 278 */     float bindWidth = bindTextWidth + 6.0F;
/* 279 */     float bindX = panelX + 89.0F - bindWidth;
/*     */     
/* 281 */     int bindBackgroundColor = ColorUtils.setAlphaColor(
/* 282 */         ColorUtils.interpolateColor(ColorUtils.darken(colorTheme, 0.15F), colorTheme, progress), alpha);
/*     */ 
/*     */     
/* 285 */     int bindTextColor = ColorUtils.setAlphaColor(ColorUtils.interpolateColor(ColorUtils.rgb(140, 139, 145), -1, progress), alpha);
/*     */     
/* 287 */     RenderUtils.drawRoundedRect(context.method_51448(), bindX, settingY - 2.5F, bindWidth, 9.0F, 1.5F, bindBackgroundColor);
/* 288 */     issue(12).drawString(context.method_51448(), bindString, bindX + 3.0F, settingY + 1.0F, bindTextColor);
/* 289 */     drawStringWithHoverScroll(
/* 290 */         issue(12), context
/* 291 */         .method_51448(), bindSetting
/* 292 */         .name(), panelX + 10.0F, settingY + 1.0F, bindX - 4.0F - panelX + 10.0F, 
/*     */ 
/*     */ 
/*     */         
/* 296 */         getPrimarySettingColor(alpha), mouseX, mouseY, state, 
/*     */ 
/*     */ 
/*     */         
/* 300 */         getSettingTextKey((Setting)bindSetting));
/*     */   }
/*     */ 
/*     */   
/*     */   private String getModeKey(ModeSetting setting, String mode) {
/* 305 */     return "" + System.identityHashCode(setting) + "_mode_" + System.identityHashCode(setting);
/*     */   }
/*     */   
/*     */   private String getListKey(ListSetting setting, BooleanSetting entry) {
/* 309 */     return "" + setting.hashCode() + "_list_" + setting.hashCode();
/*     */   }
/*     */   
/*     */   private String getBindKey(BindSetting setting) {
/* 313 */     return "" + setting.hashCode() + "_bind";
/*     */   }
/*     */   
/*     */   private String formatSliderValue(FloatSetting setting) {
/* 317 */     float value = setting.get();
/* 318 */     float increment = setting.getIncrement();
/* 319 */     if (increment >= 1.0F) {
/* 320 */       return String.valueOf((int)value);
/*     */     }
/* 322 */     if (increment >= 0.1F) {
/* 323 */       return String.format("%.1f", new Object[] { Float.valueOf(value) });
/*     */     }
/* 325 */     return String.format("%.2f", new Object[] { Float.valueOf(value) });
/*     */   }
/*     */   
/*     */   private void drawStringWithHoverScroll(Font font, class_4587 matrix, String text, float x, float y, float maxWidth, int color, double mouseX, double mouseY, ClickGuiState state, String animationKey) {
/* 329 */     if (text == null || text.isEmpty() || maxWidth <= 0.0F) {
/*     */       return;
/*     */     }
/*     */     
/* 333 */     float totalWidth = font.getWidth(text);
/* 334 */     float overflow = totalWidth - maxWidth;
/* 335 */     if (overflow <= 6.0F) {
/* 336 */       font.draw(matrix, text, x, y, color);
/*     */       
/*     */       return;
/*     */     } 
/* 340 */     boolean hovered = isTextHovered(x, y, maxWidth, font.getHeight(), mouseX, mouseY);
/* 341 */     float scrollPhase = state.advanceTextScrollPhase(animationKey, hovered);
/* 342 */     boolean scrollActive = state.isTextScrollActive(animationKey, hovered);
/* 343 */     AnimationUtils hoverAnimation = state.getTextHoverAnimation(animationKey, scrollActive);
/* 344 */     hoverAnimation.update(scrollActive ? 1.0F : 0.0F);
/* 345 */     float hoverProgress = hoverAnimation.getValue();
/* 346 */     float scrollOffset = getHoverScrollOffset(overflow, scrollPhase) * hoverProgress;
/*     */     
/* 348 */     ScissorUtils.push();
/* 349 */     ScissorUtils.setFromComponentCoordinates(x, (y - 2.0F), maxWidth, (font.getHeight() + 4.0F));
/* 350 */     font.draw(matrix, text, x - scrollOffset, y, color);
/* 351 */     ScissorUtils.pop();
/*     */   }
/*     */   
/*     */   private int getPrimarySettingColor(int alpha) {
/* 355 */     return ColorUtils.rgba(245, 245, 248, alpha);
/*     */   }
/*     */   
/*     */   private int getSecondarySettingColor(int alpha) {
/* 359 */     return ColorUtils.rgba(186, 186, 194, alpha);
/*     */   }
/*     */   
/*     */   private boolean isTextHovered(float x, float y, float width, float height, double mouseX, double mouseY) {
/* 363 */     return (mouseX >= x && mouseX <= (x + width) && mouseY >= (y - 2.0F) && mouseY <= (y + height + 2.0F));
/*     */   }
/*     */   
/*     */   private float getHoverScrollOffset(float maxOffset, float phase) {
/* 367 */     if (maxOffset <= 0.0F) {
/* 368 */       return 0.0F;
/*     */     }
/*     */     
/* 371 */     float pingPong = (phase < 0.5F) ? (phase * 2.0F) : (2.0F - phase * 2.0F);
/* 372 */     float eased = pingPong * pingPong * (3.0F - 2.0F * pingPong);
/* 373 */     return maxOffset * eased;
/*     */   }
/*     */   
/*     */   private String getSettingTextKey(Setting setting) {
/* 377 */     return "setting_text_" + System.identityHashCode(setting);
/*     */   }
/*     */   
/*     */   private Font issue(int size) {
/* 381 */     return Fonts.getFont("suisse", size);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\clien\\ui\clickgui\ClickGuiSettingRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */