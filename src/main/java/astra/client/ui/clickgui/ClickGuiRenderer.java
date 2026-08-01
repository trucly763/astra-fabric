/*     */ package shame.astra.client.ui.clickgui;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.class_1041;
/*     */ import net.minecraft.class_332;
/*     */ import net.minecraft.class_3532;
/*     */ import shame.astra.api.utils.color.ColorUtils;
/*     */ import shame.astra.api.utils.input.KeyBoardUtils;
/*     */ import shame.astra.api.utils.math.HoveringUtils;
/*     */ import shame.astra.api.utils.render.RenderUtils;
/*     */ import shame.astra.api.utils.render.fonts.msdf.Font;
/*     */ import shame.astra.api.utils.render.fonts.msdf.Fonts;
/*     */ import shame.astra.api.utils.scissor.ScissorUtils;
/*     */ import shame.astra.astra;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ 
/*     */ public class ClickGuiRenderer
/*     */ {
/*     */   private final ClickGuiState state;
/*     */   private final ClickGuiSettingRenderer settingRenderer;
/*     */   private final ClickGuiThemeSelector themeSelector;
/*     */   
/*     */   public ClickGuiRenderer(ClickGuiState state, ClickGuiSettingRenderer settingRenderer, ClickGuiThemeSelector themeSelector) {
/*  26 */     this.state = state;
/*  27 */     this.settingRenderer = settingRenderer;
/*  28 */     this.themeSelector = themeSelector;
/*     */   }
/*     */   
/*     */   public void render(class_332 context, int mouseX, int mouseY, class_1041 window, float animationProgress) {
/*  32 */     if (window == null) {
/*     */       return;
/*     */     }
/*     */     
/*  36 */     float alphaMul = class_3532.method_15363(animationProgress, 0.0F, 1.0F);
/*  37 */     int shadeColor = getFadeShadeColor(alphaMul, 120);
/*  38 */     int colorTheme = getThemeColor();
/*  39 */     Module hoveredModule = null;
/*     */     
/*  41 */     Module.ModuleCategory[] categories = Module.ModuleCategory.values();
/*  42 */     for (int i = 0; i < categories.length; i++) {
/*  43 */       Module.ModuleCategory category = categories[i];
/*  44 */       float panelX = ClickGuiLayout.getCategoryPanelX(this.state.getX(), i);
/*  45 */       Module categoryHoveredModule = renderCategoryPanel(context, mouseX, mouseY, panelX, category, colorTheme, alphaMul, shadeColor);
/*  46 */       if (categoryHoveredModule != null) {
/*  47 */         hoveredModule = categoryHoveredModule;
/*     */       }
/*     */     } 
/*     */     
/*  51 */     renderSearch(context, categories.length, colorTheme, alphaMul, getFadeShadeColor(alphaMul, 95));
/*  52 */     this.themeSelector.render(context, window, this.state.getRenderOffsetY(), alphaMul, getFadeShadeColor(alphaMul, 95));
/*  53 */     renderDescription(context, window, hoveredModule, colorTheme, animationProgress);
/*     */   }
/*     */   
/*     */   private Module renderCategoryPanel(class_332 context, int mouseX, int mouseY, float panelX, Module.ModuleCategory category, int colorTheme, float alphaMul, int shadeColor) {
/*  57 */     float panelY = this.state.getY() + this.state.getRenderOffsetY();
/*  58 */     RenderUtils.drawRoundedRect(context.method_51448(), panelX, panelY, 100.0F, 275.0F, 8.0F, ColorUtils.darken(colorTheme, 0.07F));
/*  59 */     RenderUtils.drawRoundedRect(context.method_51448(), panelX, panelY + 23.0F, 100.0F, 0.5F, 0.0F, ColorUtils.rgb(19, 18, 24));
/*  60 */     if ((shadeColor >> 24 & 0xFF) > 0) {
/*  61 */       RenderUtils.drawRoundedRect(context.method_51448(), panelX, panelY, 100.0F, 275.0F, 8.0F, shadeColor);
/*     */     }
/*     */     
/*  64 */     icons(14).drawCenteredString(context.method_51448(), category.getIcons(), panelX + 50.0F - issue(15).getWidth(category.getName()) / 2.0F - 4.0F, panelY + 10.0F, alpha(colorTheme, alphaMul));
/*  65 */     issue(15).drawCenteredString(context.method_51448(), category.getName(), panelX + 52.0F, panelY + 9.0F, alpha(-1, alphaMul));
/*     */     
/*  67 */     float contentY = ClickGuiLayout.getContentY(panelY);
/*  68 */     float contentHeight = ClickGuiLayout.getContentHeight();
/*  69 */     this.state.clampScroll(category, contentHeight);
/*  70 */     float moduleY = contentY + this.state.getScroll(category);
/*  71 */     Module hoveredModule = null;
/*     */     
/*  73 */     ScissorUtils.push();
/*  74 */     ScissorUtils.setFromComponentCoordinates(panelX, contentY, 100.0D, contentHeight);
/*     */     
/*  76 */     for (Module module : this.state.getModules(category)) {
/*  77 */       float openProgress = this.state.getOpenProgress(module);
/*  78 */       float moduleHeight = ClickGuiLayout.getModuleHeight(module, openProgress);
/*     */       
/*  80 */       if (moduleY + moduleHeight + 4.0F >= contentY && moduleY <= contentY + contentHeight) {
/*  81 */         Module moduleHover = renderModule(context, mouseX, mouseY, panelX, moduleY, module, openProgress, moduleHeight, colorTheme, alphaMul, shadeColor);
/*  82 */         if (moduleHover != null) {
/*  83 */           hoveredModule = moduleHover;
/*     */         }
/*     */       } 
/*     */       
/*  87 */       moduleY += 4.0F + moduleHeight;
/*     */     } 
/*     */     
/*  90 */     ScissorUtils.pop();
/*  91 */     return hoveredModule;
/*     */   }
/*     */   
/*     */   private Module renderModule(class_332 context, int mouseX, int mouseY, float panelX, float moduleY, Module module, float openProgress, float moduleHeight, int colorTheme, float alphaMul, int shadeColor) {
/*  95 */     List<Setting> settings = module.getSettings();
/*  96 */     renderModuleBackground(context, panelX, moduleY, moduleHeight, module.isEnable(), colorTheme, shadeColor);
/*     */     
/*  98 */     String moduleName = module.getName();
/*  99 */     String bindText = "";
/* 100 */     if (this.state.getBindingModule() == module) {
/* 101 */       bindText = " [...]";
/* 102 */     } else if (module.getKey() != -1) {
/* 103 */       bindText = " [" + this.state.toEnglish(KeyBoardUtils.getBindName(module.getKey())) + "]";
/*     */     } 
/*     */     
/* 106 */     int nameColor = module.isEnable() ? alpha(-1, alphaMul) : alpha(ColorUtils.rgba(255, 255, 255, 170), alphaMul);
/* 107 */     int bindColor = module.isEnable() ? alpha(ColorUtils.rgba(255, 255, 255, 150), alphaMul) : alpha(ColorUtils.rgba(255, 255, 255, 100), alphaMul);
/*     */     
/* 109 */     issue(14).draw(context.method_51448(), moduleName, panelX + 10.0F, moduleY + 8.0F, nameColor);
/* 110 */     if (!bindText.isEmpty()) {
/* 111 */       float nameWidth = issue(14).getWidth(moduleName);
/* 112 */       issue(11).draw(context.method_51448(), bindText, panelX + 10.0F + nameWidth, moduleY + 9.0F, bindColor);
/*     */     } 
/*     */     
/* 115 */     if (settings != null && !settings.isEmpty() && ClickGuiLayout.hasVisibleSettings(settings)) {
/* 116 */       renderModuleDots(context, panelX, moduleY, module, module.isEnable(), alphaMul);
/*     */     }
/*     */     
/* 119 */     if (settings != null && !settings.isEmpty()) {
/* 120 */       this.settingRenderer.render(context, module, panelX, moduleY, openProgress, colorTheme, mouseX, mouseY, this.state);
/*     */     }
/*     */     
/* 123 */     if (HoveringUtils.isHovered(mouseX, mouseY, (panelX + 3.0F), moduleY, 93.5D, moduleHeight)) {
/* 124 */       return module;
/*     */     }
/* 126 */     return null;
/*     */   }
/*     */   
/*     */   private void renderModuleBackground(class_332 context, float panelX, float moduleY, float moduleHeight, boolean enabled, int colorTheme, int shadeColor) {
/* 130 */     if (enabled) {
/* 131 */       RenderUtils.drawRoundedRect(context.method_51448(), panelX + 3.0F, moduleY - 0.5F, 93.5F, moduleHeight + 1.0F, 5.0F, ColorUtils.darken(colorTheme, 0.17F));
/* 132 */       RenderUtils.drawGradientRect(context.method_51448(), panelX + 3.0F + 0.5F, moduleY, 92.5F, moduleHeight, 4.0F, ColorUtils.darken(colorTheme, 0.15F), ColorUtils.darken(colorTheme, 0.1F), false);
/* 133 */       if ((shadeColor >> 24 & 0xFF) > 0) {
/* 134 */         RenderUtils.drawRoundedRect(context.method_51448(), panelX + 3.0F + 0.5F, moduleY, 92.5F, moduleHeight, 4.0F, shadeColor);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 139 */     RenderUtils.drawRoundedRect(context.method_51448(), panelX + 3.0F, moduleY - 0.5F, 93.5F, moduleHeight + 1.0F, 5.0F, ColorUtils.darken(colorTheme, 0.1F));
/* 140 */     RenderUtils.drawGradientRect(context.method_51448(), panelX + 3.0F + 0.5F, moduleY, 92.5F, moduleHeight, 4.0F, ColorUtils.darken(colorTheme, 0.09F), ColorUtils.darken(colorTheme, 0.08F), false);
/* 141 */     if ((shadeColor >> 24 & 0xFF) > 0) {
/* 142 */       RenderUtils.drawRoundedRect(context.method_51448(), panelX + 3.0F + 0.5F, moduleY, 92.5F, moduleHeight, 4.0F, shadeColor);
/*     */     }
/*     */   }
/*     */   
/*     */   private void renderModuleDots(class_332 context, float panelX, float moduleY, Module module, boolean enabled, float alphaMul) {
/* 147 */     int dotsColor = enabled ? alpha(ColorUtils.rgba(255, 255, 255, 220), alphaMul) : alpha(ColorUtils.rgba(255, 255, 255, 100), alphaMul);
/* 148 */     float dotsX = panelX + 87.5F;
/* 149 */     float baseY = moduleY + 10.0F;
/* 150 */     float spacing = 2.0F;
/* 151 */     float radius = 2.1F;
/* 152 */     float bottomXOffset = 2.1F;
/* 153 */     float angle = this.state.updateDotsRotation(module, module.isOpen() ? 1.5707964F : 0.0F);
/* 154 */     float cos = (float)Math.cos(angle);
/* 155 */     float sin = (float)Math.sin(angle);
/* 156 */     float[][] offsets = { { 0.0F, -spacing }, { -bottomXOffset, spacing }, { bottomXOffset, spacing } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 162 */     for (float[] offset : offsets) {
/* 163 */       float rx = offset[0] * cos - offset[1] * sin;
/* 164 */       float ry = offset[0] * sin + offset[1] * cos;
/* 165 */       RenderUtils.drawRoundCircle(context.method_51448(), dotsX + rx, baseY + ry, radius, dotsColor);
/*     */     } 
/*     */   }
/*     */   
/*     */   private int getThemeColor() {
/* 170 */     if (!astra.INSTANCE.themeStorage.getThemes().getTheme().getName().equals("Rainbow")) {
/* 171 */       return (astra.INSTANCE.themeStorage.getThemes().getTheme()).color[0];
/*     */     }
/* 173 */     return ColorUtils.getThemeColor();
/*     */   }
/*     */   
/*     */   private void renderSearch(class_332 context, int categoryCount, int colorTheme, float alphaMul, int shadeColor) {
/* 177 */     float searchY = ClickGuiLayout.getSearchY(this.state.getY() + this.state.getRenderOffsetY());
/* 178 */     float searchW = getSearchWidth();
/* 179 */     float searchX = ClickGuiLayout.getSearchX(this.state.getX(), categoryCount, searchW);
/* 180 */     float searchH = 18.0F;
/* 181 */     float selectionPaddingLeft = 3.0F;
/* 182 */     float selectionPaddingRight = 1.5F;
/* 183 */     int borderColor = ColorUtils.darken(colorTheme, 0.12F);
/*     */     
/* 185 */     RenderUtils.drawRoundedRect(context.method_51448(), searchX - 0.5F, searchY - 0.5F, searchW + 1.0F, searchH + 1.0F, 5.5F, borderColor);
/* 186 */     RenderUtils.drawGradientRect(context.method_51448(), searchX, searchY, searchW, searchH, 5.0F, 
/* 187 */         ColorUtils.darken(colorTheme, 0.12F), 
/* 188 */         ColorUtils.darken(colorTheme, 0.08F), false);
/* 189 */     if ((shadeColor >> 24 & 0xFF) > 0) {
/* 190 */       RenderUtils.drawRoundedRect(context.method_51448(), searchX, searchY, searchW, searchH, 5.0F, shadeColor);
/*     */     }
/*     */     
/* 193 */     String query = this.state.getSearchText();
/* 194 */     String text = query.isEmpty() ? "Search..." : query;
/*     */ 
/*     */     
/* 197 */     int textColor = query.isEmpty() ? alpha(ColorUtils.rgba(255, 255, 255, 110), alphaMul) : alpha(ColorUtils.rgba(255, 255, 255, 230), alphaMul);
/*     */     
/* 199 */     float iconX = searchX + 3.5F;
/* 200 */     float textX = searchX + 19.0F;
/* 201 */     float textY = searchY + 6.2F;
/* 202 */     iconsNew(18).drawGradientStringHorizontal(context.method_51448(), "l", iconX + 2.0F, searchY + 6.5F, alpha(colorTheme, alphaMul), alpha(colorTheme, alphaMul));
/*     */     
/* 204 */     ScissorUtils.push();
/* 205 */     ScissorUtils.setFromComponentCoordinates((textX - selectionPaddingLeft), searchY, (searchW - 19.0F - 8.0F + selectionPaddingLeft), searchH);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 211 */     if (!query.isEmpty() && this.state.hasSearchSelection()) {
/* 212 */       int selectionStart = this.state.getSearchSelectionStart();
/* 213 */       int selectionEnd = this.state.getSearchSelectionEnd();
/* 214 */       float selectedX = textX + issue(14).getWidth(query.substring(0, selectionStart)) - selectionPaddingLeft;
/* 215 */       float selectedW = issue(14).getWidth(query.substring(selectionStart, selectionEnd)) + selectionPaddingLeft + selectionPaddingRight;
/* 216 */       RenderUtils.drawRoundedRect(context.method_51448(), selectedX, searchY + 3.8F, selectedW, 10.5F, 1.5F, alpha(ColorUtils.rgba(42, 115, 255, 155), alphaMul));
/*     */     } 
/*     */     
/* 219 */     issue(14).draw(context.method_51448(), text, textX, textY + 1.0F, textColor);
/* 220 */     if (this.state.isSearchActive() && System.currentTimeMillis() / 500L % 2L == 0L) {
/* 221 */       float cursorX = textX + issue(14).getWidth(query.substring(0, Math.min(this.state.getSearchCursor(), query.length())));
/* 222 */       RenderUtils.drawRoundedRect(context.method_51448(), cursorX + 1.0F, searchY + 4.5F, 0.8F, 9.0F, 0.0F, alpha(ColorUtils.applyAlpha(colorTheme, 0.9F), alphaMul));
/*     */     } 
/* 224 */     ScissorUtils.pop();
/*     */   }
/*     */   
/*     */   private void renderDescription(class_332 context, class_1041 window, Module hoveredModule, int colorTheme, float alphaMul) {
/* 228 */     if (hoveredModule == null) {
/*     */       return;
/*     */     }
/*     */     
/* 232 */     String description = hoveredModule.getDisplayDescription();
/* 233 */     if (description == null || description.isBlank() || "NULLABLE".equalsIgnoreCase(description) || "desc".equalsIgnoreCase(description)) {
/*     */       return;
/*     */     }
/*     */     
/* 237 */     Font descriptionFont = issue(16);
/* 238 */     float maxWidth = window.method_4486() - 40.0F;
/* 239 */     List<String> lines = wrapDescription(descriptionFont, description, maxWidth);
/* 240 */     if (lines.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 244 */     float lineHeight = descriptionFont.getHeight() - 2.0F;
/* 245 */     float boxHeight = lines.size() * lineHeight;
/* 246 */     float centerX = window.method_4486() * 0.5F;
/* 247 */     float startY = 100.0F - boxHeight - 6.0F;
/*     */     
/* 249 */     for (int i = 0; i < lines.size(); i++) {
/* 250 */       descriptionFont.drawCenteredString(context.method_51448(), lines.get(i), centerX, startY + i * lineHeight, ColorUtils.applyAlpha(-1, alphaMul));
/*     */     }
/*     */   }
/*     */   
/*     */   private List<String> wrapDescription(Font font, String text, float maxWidth) {
/* 255 */     List<String> lines = new ArrayList<>();
/* 256 */     String[] words = text.trim().split("\\s+");
/* 257 */     if (words.length == 0) {
/* 258 */       return lines;
/*     */     }
/*     */     
/* 261 */     StringBuilder currentLine = new StringBuilder();
/* 262 */     for (String word : words) {
/* 263 */       String candidate = currentLine.isEmpty() ? word : (String.valueOf(currentLine) + " " + String.valueOf(currentLine));
/* 264 */       if (font.getWidth(candidate) <= maxWidth || currentLine.isEmpty()) {
/* 265 */         currentLine.setLength(0);
/* 266 */         currentLine.append(candidate);
/*     */       }
/*     */       else {
/*     */         
/* 270 */         lines.add(currentLine.toString());
/* 271 */         currentLine.setLength(0);
/* 272 */         currentLine.append(word);
/*     */       } 
/*     */     } 
/* 275 */     if (!currentLine.isEmpty()) {
/* 276 */       lines.add(currentLine.toString());
/*     */     }
/*     */     
/* 279 */     return lines;
/*     */   }
/*     */   
/*     */   private float getSearchWidth() {
/* 283 */     String query = this.state.getSearchText();
/* 284 */     String text = query.isEmpty() ? "Search..." : query;
/* 285 */     float contentWidth = 19.0F + issue(14).getWidth(text) + 8.0F;
/* 286 */     return Math.max(75.0F, contentWidth);
/*     */   }
/*     */   
/*     */   private Font issue(int size) {
/* 290 */     return Fonts.getFont("suisse", size);
/*     */   }
/*     */   
/*     */   private Font icons(int size) {
/* 294 */     return Fonts.getFont("icon", size);
/*     */   }
/*     */   
/*     */   private Font iconsNew(int size) {
/* 298 */     return Fonts.getFont("icon1", size);
/*     */   }
/*     */   
/*     */   private int alpha(int color, float alphaMul) {
/* 302 */     return ColorUtils.applyAlpha(color, alphaMul);
/*     */   }
/*     */   
/*     */   private int getFadeShadeColor(float alphaMul, int maxAlpha) {
/* 306 */     int alpha = class_3532.method_15340((int)((1.0F - alphaMul) * maxAlpha), 0, 255);
/* 307 */     return ColorUtils.rgba(0, 0, 0, alpha);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\clien\\ui\clickgui\ClickGuiRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */