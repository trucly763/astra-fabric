/*     */ package shame.astra.client.ui.clickgui;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.class_1041;
/*     */ import shame.astra.api.QClient;
/*     */ import shame.astra.api.utils.input.KeyBoardUtils;
/*     */ import shame.astra.api.utils.math.HoveringUtils;
/*     */ import shame.astra.api.utils.render.fonts.msdf.Font;
/*     */ import shame.astra.api.utils.render.fonts.msdf.Fonts;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.BindSetting;
/*     */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*     */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*     */ import shame.astra.client.modules.settings.implement.ListSetting;
/*     */ import shame.astra.client.modules.settings.implement.ModeSetting;
/*     */ import shame.astra.client.modules.settings.implement.TextSetting;
/*     */ 
/*     */ public class ClickGuiInputHandler
/*     */   implements QClient
/*     */ {
/*     */   private final ClickGuiState state;
/*     */   private final ClickGuiThemeSelector themeSelector;
/*     */   
/*     */   public ClickGuiInputHandler(ClickGuiState state, ClickGuiThemeSelector themeSelector) {
/*  26 */     this.state = state;
/*  27 */     this.themeSelector = themeSelector;
/*     */   }
/*     */   
/*     */   public boolean mouseClicked(double mouseX, double mouseY, int button, class_1041 window) {
/*  31 */     if (window != null && button == 0) {
/*  32 */       int categoryCount = (Module.ModuleCategory.values()).length;
/*  33 */       float searchW = getSearchWidth();
/*  34 */       float searchX = ClickGuiLayout.getSearchX(this.state.getX(), categoryCount, searchW);
/*  35 */       float searchY = ClickGuiLayout.getSearchY(this.state.getY() + this.state.getRenderOffsetY());
/*  36 */       boolean searchHovered = HoveringUtils.isHovered(mouseX, mouseY, searchX, searchY, searchW, 18.0D);
/*  37 */       this.state.setSearchActive(searchHovered);
/*  38 */       if (searchHovered) {
/*  39 */         this.state.setEditingTextSetting(null);
/*  40 */         this.state.startSearchSelection(getSearchIndexAt(mouseX, searchX));
/*  41 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/*  45 */     if (this.state.getBindingModule() != null && button >= 2) {
/*  46 */       this.state.getBindingModule().setKey(KeyBoardUtils.createMouseBind(button));
/*  47 */       this.state.setBindingModule(null);
/*  48 */       return true;
/*     */     } 
/*     */     
/*  51 */     if (this.state.getBindingSetting() != null && button >= 2) {
/*  52 */       this.state.getBindingSetting().setKey(KeyBoardUtils.createMouseBind(button));
/*  53 */       this.state.setBindingSetting(null);
/*  54 */       return true;
/*     */     } 
/*     */     
/*  57 */     this.state.setEditingTextSetting(null);
/*     */     
/*  59 */     if (this.themeSelector.handleClick(window, mouseX, mouseY, button, this.state.getRenderOffsetY())) {
/*  60 */       return true;
/*     */     }
/*     */     
/*  63 */     Module.ModuleCategory[] categories = Module.ModuleCategory.values();
/*  64 */     for (int i = 0; i < categories.length; i++) {
/*  65 */       Module.ModuleCategory category = categories[i];
/*  66 */       float panelX = ClickGuiLayout.getCategoryPanelX(this.state.getX(), i);
/*  67 */       float contentY = ClickGuiLayout.getContentY(this.state.getY() + this.state.getRenderOffsetY());
/*  68 */       float contentHeight = ClickGuiLayout.getContentHeight();
/*     */       
/*  70 */       if (HoveringUtils.isHovered(mouseX, mouseY, panelX, contentY, 100.0D, contentHeight)) {
/*     */ 
/*     */ 
/*     */         
/*  74 */         float moduleY = contentY + this.state.getScroll(category);
/*  75 */         for (Module module : this.state.getModules(category)) {
/*  76 */           float openProgress = this.state.getOpenProgress(module);
/*  77 */           float moduleHeight = ClickGuiLayout.getModuleHeight(module, openProgress);
/*  78 */           if (HoveringUtils.isHovered(mouseX, mouseY, (panelX + 3.0F), moduleY, 93.5D, 20.0D)) {
/*  79 */             if (button == 0) {
/*  80 */               module.toggle();
/*  81 */               return true;
/*     */             } 
/*  83 */             if (button == 1) {
/*  84 */               module.setOpen(!module.isOpen());
/*  85 */               this.state.clampScroll(category, contentHeight);
/*  86 */               return true;
/*     */             } 
/*  88 */             if (button == 2) {
/*  89 */               this.state.setBindingModule(module);
/*  90 */               return true;
/*     */             } 
/*  92 */             return true;
/*     */           } 
/*     */           
/*  95 */           if (module.isOpen() && openProgress > 0.1F) {
/*  96 */             List<Setting> settings = module.getSettings();
/*  97 */             if (settings != null && handleSettingClick(mouseX, mouseY, button, panelX, moduleY, settings)) {
/*  98 */               return true;
/*     */             }
/*     */           } 
/*     */           
/* 102 */           moduleY += 4.0F + moduleHeight;
/*     */         } 
/*     */       } 
/*     */     } 
/* 106 */     return false;
/*     */   }
/*     */   
/*     */   public boolean mouseReleased(int button) {
/* 110 */     this.state.stopSearchSelection();
/* 111 */     if (button == 0) {
/* 112 */       for (Module module : this.state.getAllModules()) {
/* 113 */         List<Setting> settings = module.getSettings();
/* 114 */         if (settings == null) {
/*     */           continue;
/*     */         }
/* 117 */         for (Setting setting : settings) {
/* 118 */           if (setting instanceof FloatSetting) { FloatSetting floatSetting = (FloatSetting)setting;
/* 119 */             floatSetting.setActive(false);
/* 120 */             this.state.endSliderDrag(floatSetting); }
/*     */         
/*     */         } 
/*     */       } 
/*     */     }
/* 125 */     return false;
/*     */   }
/*     */   
/*     */   public boolean mouseDragged(double mouseX, double mouseY, int button) {
/* 129 */     if (button != 0 || !this.state.isSearchActive() || !this.state.isSearchDragging()) {
/* 130 */       return false;
/*     */     }
/*     */     
/* 133 */     int categoryCount = (Module.ModuleCategory.values()).length;
/* 134 */     float searchX = ClickGuiLayout.getSearchX(this.state.getX(), categoryCount, getSearchWidth());
/* 135 */     this.state.updateSearchSelection(getSearchIndexAt(mouseX, searchX));
/* 136 */     return true;
/*     */   }
/*     */   
/*     */   public boolean mouseScrolled(double mouseX, double mouseY, double verticalAmount) {
/* 140 */     Module.ModuleCategory[] categories = Module.ModuleCategory.values();
/* 141 */     for (int i = 0; i < categories.length; i++) {
/* 142 */       Module.ModuleCategory category = categories[i];
/* 143 */       float panelX = ClickGuiLayout.getCategoryPanelX(this.state.getX(), i);
/* 144 */       float contentY = ClickGuiLayout.getContentY(this.state.getY() + this.state.getRenderOffsetY());
/* 145 */       float contentHeight = ClickGuiLayout.getContentHeight();
/* 146 */       if (HoveringUtils.isHovered(mouseX, mouseY, panelX, contentY, 100.0D, contentHeight)) {
/* 147 */         this.state.addScroll(category, verticalAmount, contentHeight);
/* 148 */         return true;
/*     */       } 
/*     */     } 
/* 151 */     return false;
/*     */   }
/*     */   
/*     */   public boolean keyPressed(int keyCode, int modifiers) {
/* 155 */     if (this.state.getEditingTextSetting() != null) {
/* 156 */       TextSetting textSetting = this.state.getEditingTextSetting();
/* 157 */       if (keyCode == 256 || keyCode == 257 || keyCode == 335) {
/* 158 */         this.state.setEditingTextSetting(null);
/* 159 */         return true;
/*     */       } 
/* 161 */       if (keyCode == 259) {
/* 162 */         String current = textSetting.get();
/* 163 */         if (current != null && !current.isEmpty()) {
/* 164 */           textSetting.setText(current.substring(0, current.length() - 1));
/*     */         }
/* 166 */         return true;
/*     */       } 
/* 168 */       return true;
/*     */     } 
/*     */     
/* 171 */     if (this.state.isSearchActive()) {
/* 172 */       if ((modifiers & 0x2) != 0) {
/* 173 */         if (keyCode == 65) {
/* 174 */           this.state.selectAllSearchText();
/* 175 */           return true;
/*     */         } 
/* 177 */         if (keyCode == 67) {
/* 178 */           if (this.state.hasSearchSelection() && mc != null && mc.field_1774 != null) {
/* 179 */             mc.field_1774.method_1455(this.state.getSelectedSearchText());
/*     */           }
/* 181 */           return true;
/*     */         } 
/* 183 */         if (keyCode == 86) {
/* 184 */           if (mc != null && mc.field_1774 != null) {
/* 185 */             this.state.replaceSearchSelection(mc.field_1774.method_1460());
/*     */           }
/* 187 */           return true;
/*     */         } 
/* 189 */         if (keyCode == 90) {
/* 190 */           this.state.restoreSearchUndo();
/* 191 */           return true;
/*     */         } 
/*     */       } 
/* 194 */       if (keyCode == 256 || keyCode == 257 || keyCode == 335) {
/* 195 */         this.state.setSearchActive(false);
/* 196 */         return true;
/*     */       } 
/* 198 */       if (keyCode == 259) {
/* 199 */         this.state.removeLastSearchChar();
/* 200 */         return true;
/*     */       } 
/* 202 */       if (keyCode == 261) {
/* 203 */         this.state.clearSearchText();
/* 204 */         return true;
/*     */       } 
/* 206 */       if (keyCode == 263) {
/* 207 */         this.state.setSearchCursor(this.state.getSearchCursor() - 1, ((modifiers & 0x1) != 0));
/* 208 */         return true;
/*     */       } 
/* 210 */       if (keyCode == 262) {
/* 211 */         this.state.setSearchCursor(this.state.getSearchCursor() + 1, ((modifiers & 0x1) != 0));
/* 212 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 216 */     if (this.state.getBindingModule() != null) {
/* 217 */       if (keyCode == 256) {
/* 218 */         this.state.setBindingModule(null);
/* 219 */       } else if (keyCode == 261 || keyCode == 259) {
/* 220 */         this.state.getBindingModule().setKey(-1);
/* 221 */         this.state.setBindingModule(null);
/*     */       } else {
/* 223 */         this.state.getBindingModule().setKey(keyCode);
/* 224 */         this.state.setBindingModule(null);
/*     */       } 
/* 226 */       return true;
/*     */     } 
/*     */     
/* 229 */     if (this.state.getBindingSetting() != null) {
/* 230 */       if (keyCode == 256) {
/* 231 */         this.state.setBindingSetting(null);
/* 232 */       } else if (keyCode == 261 || keyCode == 259) {
/* 233 */         this.state.getBindingSetting().setKey(-1);
/* 234 */         this.state.setBindingSetting(null);
/*     */       } else {
/* 236 */         this.state.getBindingSetting().setKey(keyCode);
/* 237 */         this.state.setBindingSetting(null);
/*     */       } 
/* 239 */       return true;
/*     */     } 
/*     */     
/* 242 */     return false;
/*     */   }
/*     */   
/*     */   public boolean charTyped(char chr) {
/* 246 */     if (this.state.getEditingTextSetting() != null) {
/* 247 */       if (!Character.isISOControl(chr)) {
/* 248 */         TextSetting textSetting = this.state.getEditingTextSetting();
/* 249 */         textSetting.setText(textSetting.get() + textSetting.get());
/*     */       } 
/* 251 */       return true;
/*     */     } 
/*     */     
/* 254 */     if (!this.state.isSearchActive()) {
/* 255 */       return false;
/*     */     }
/* 257 */     this.state.appendSearchChar(chr);
/* 258 */     return true;
/*     */   }
/*     */   
/*     */   private int getSearchIndexAt(double mouseX, float searchX) {
/* 262 */     String text = this.state.getSearchText();
/* 263 */     float textX = searchX + 19.0F;
/* 264 */     float localX = (float)mouseX - textX;
/* 265 */     if (localX <= 0.0F || text.isEmpty()) {
/* 266 */       return 0;
/*     */     }
/*     */     
/* 269 */     for (int i = 1; i <= text.length(); i++) {
/* 270 */       float previousWidth = issue(14).getWidth(text.substring(0, i - 1));
/* 271 */       float currentWidth = issue(14).getWidth(text.substring(0, i));
/* 272 */       float midpoint = previousWidth + (currentWidth - previousWidth) * 0.5F;
/* 273 */       if (localX < midpoint) {
/* 274 */         return i - 1;
/*     */       }
/*     */     } 
/* 277 */     return text.length();
/*     */   }
/*     */   
/*     */   private float getSearchWidth() {
/* 281 */     String query = this.state.getSearchText();
/* 282 */     String text = query.isEmpty() ? "Search..." : query;
/* 283 */     float contentWidth = 19.0F + issue(14).getWidth(text) + 8.0F;
/* 284 */     return Math.max(75.0F, contentWidth);
/*     */   }
/*     */   
/*     */   private boolean handleSettingClick(double mouseX, double mouseY, int button, float panelX, float moduleY, List<Setting> settings) {
/* 288 */     float settingYoffset = 20.0F;
/* 289 */     for (Setting setting : settings) {
/* 290 */       if (setting == null || !setting.visible().booleanValue()) {
/*     */         continue;
/*     */       }
/*     */       
/* 294 */       float settingY = moduleY + settingYoffset + 4.0F;
/* 295 */       if (setting instanceof BooleanSetting) { BooleanSetting booleanSetting = (BooleanSetting)setting;
/* 296 */         if (button == 0 && HoveringUtils.isHovered(mouseX, mouseY, (panelX + 75.0F), (settingY - 2.0F), 16.0D, 10.0D)) {
/* 297 */           booleanSetting.setState(!booleanSetting.isState());
/* 298 */           return true;
/*     */         } 
/* 300 */         settingYoffset += 12.0F; continue; }
/* 301 */        if (setting instanceof TextSetting) { TextSetting textSetting = (TextSetting)setting;
/* 302 */         float boxWidth = 42.0F;
/* 303 */         float boxX = panelX + 49.0F;
/* 304 */         if (button == 0 && HoveringUtils.isHovered(mouseX, mouseY, boxX, (settingY - 2.5F), boxWidth, 9.0D)) {
/* 305 */           this.state.setSearchActive(false);
/* 306 */           this.state.stopSearchSelection();
/* 307 */           this.state.setEditingTextSetting(textSetting);
/* 308 */           return true;
/*     */         } 
/* 310 */         settingYoffset += 12.0F; continue; }
/* 311 */        if (setting instanceof FloatSetting) { FloatSetting floatSetting = (FloatSetting)setting;
/* 312 */         if (button == 0 && HoveringUtils.isHovered(mouseX, mouseY, (panelX + 10.0F), (settingY + 9.0F), 79.0D, 6.0D)) {
/* 313 */           floatSetting.setActive(true);
/* 314 */           floatSetting.setValue(this.state.getSliderValue(floatSetting, panelX + 10.0F, mouseX));
/* 315 */           this.state.beginSliderDrag(floatSetting, mouseX);
/* 316 */           return true;
/*     */         } 
/* 318 */         settingYoffset += 22.0F; continue; }
/* 319 */        if (setting instanceof ModeSetting) { ModeSetting modeSetting = (ModeSetting)setting;
/* 320 */         float modeY = settingY + 10.0F;
/* 321 */         for (String mode : modeSetting.getMods()) {
/* 322 */           if (button == 0 && HoveringUtils.isHovered(mouseX, mouseY, (panelX + 10.0F), (modeY - 2.0F), 79.0D, 10.0D)) {
/* 323 */             modeSetting.set(mode);
/* 324 */             return true;
/*     */           } 
/* 326 */           modeY += 10.0F;
/*     */         } 
/* 328 */         settingYoffset += ClickGuiLayout.calculateModeSettingHeight(modeSetting); continue; }
/* 329 */        if (setting instanceof ListSetting) { ListSetting listSetting = (ListSetting)setting;
/* 330 */         float listY = settingY + 10.0F;
/* 331 */         for (BooleanSetting entry : listSetting.getSettings()) {
/* 332 */           if (!entry.visible().booleanValue()) {
/*     */             continue;
/*     */           }
/* 335 */           if (button == 0 && HoveringUtils.isHovered(mouseX, mouseY, (panelX + 10.0F), (listY - 2.0F), 79.0D, 10.0D)) {
/* 336 */             entry.setState(!entry.isState());
/* 337 */             return true;
/*     */           } 
/* 339 */           listY += 10.0F;
/*     */         } 
/* 341 */         settingYoffset += ClickGuiLayout.calculateListSettingHeight(listSetting); continue; }
/* 342 */        if (setting instanceof BindSetting) { BindSetting bindSetting = (BindSetting)setting;
/* 343 */         String bindString = (this.state.getBindingSetting() == bindSetting) ? "..." : this.state.toEnglish(KeyBoardUtils.getBindName(bindSetting.getKey()));
/* 344 */         float bindWidth = issue(12).getWidth(bindString) + 6.0F;
/* 345 */         float bindX = panelX + 89.0F - bindWidth;
/* 346 */         if (button == 0 && HoveringUtils.isHovered(mouseX, mouseY, bindX, (settingY - 2.5F), bindWidth, 9.0D)) {
/* 347 */           this.state.setBindingSetting(bindSetting);
/* 348 */           return true;
/*     */         } 
/* 350 */         settingYoffset += 12.0F; }
/*     */     
/*     */     } 
/* 353 */     return false;
/*     */   }
/*     */   
/*     */   private Font issue(int size) {
/* 357 */     return Fonts.getFont("suisse", size);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\clien\\ui\clickgui\ClickGuiInputHandler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */