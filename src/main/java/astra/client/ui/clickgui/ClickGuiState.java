/*     */ package shame.astra.client.ui.clickgui;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.EnumMap;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import net.minecraft.class_1041;
/*     */ import shame.astra.api.storages.implement.helpertstorages.enumvar.ModuleClass;
/*     */ import shame.astra.api.utils.animation.AnimationUtils;
/*     */ import shame.astra.api.utils.animation.Easings;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.implement.BindSetting;
/*     */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*     */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*     */ import shame.astra.client.modules.settings.implement.TextSetting;
/*     */ 
/*     */ public class ClickGuiState
/*     */ {
/*  21 */   private static final Map<Character, Character> RU_TO_EN = new HashMap<>();
/*     */   
/*     */   static {
/*  24 */     String ru = "йцукенгшщзхъфывапролджэячсмитьбюЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ";
/*     */     
/*  26 */     String en = "qwertyuiop[]asdfghjkl;'zxcvbnm,.QWERTYUIOP[]ASDFGHJKL;'ZXCVBNM,.";
/*  27 */     int length = Math.min(ru.length(), en.length());
/*  28 */     for (int i = 0; i < length; i++) {
/*  29 */       RU_TO_EN.put(Character.valueOf(ru.charAt(i)), Character.valueOf(en.charAt(i)));
/*     */     }
/*     */   }
/*     */   
/*  33 */   private final Map<Module, Float> dotsRotation = new HashMap<>();
/*  34 */   private final Map<Module, AnimationUtils> moduleOpenAnimation = new HashMap<>();
/*  35 */   private final Map<BooleanSetting, AnimationUtils> booleanBackgroundAnimation = new HashMap<>();
/*  36 */   private final Map<BooleanSetting, AnimationUtils> booleanCircleAnimation = new HashMap<>();
/*  37 */   private final Map<FloatSetting, AnimationUtils> sliderAnimation = new HashMap<>();
/*  38 */   private final Map<FloatSetting, Double> sliderDragMouseX = new HashMap<>();
/*  39 */   private final Map<FloatSetting, Double> sliderDragRemainder = new HashMap<>();
/*  40 */   private final Map<String, AnimationUtils> modeAnimation = new HashMap<>();
/*  41 */   private final Map<String, AnimationUtils> listAnimation = new HashMap<>();
/*  42 */   private final Map<String, AnimationUtils> bindAnimation = new HashMap<>();
/*  43 */   private final Map<String, AnimationUtils> textHoverAnimation = new HashMap<>();
/*  44 */   private final Map<String, Float> textScrollPhase = new HashMap<>();
/*  45 */   private final Map<String, Boolean> textScrollFinishing = new HashMap<>();
/*  46 */   private final Map<String, Boolean> textScrollHovered = new HashMap<>();
/*  47 */   private final Map<Module.ModuleCategory, Float> categoryScrollTarget = new EnumMap<>(Module.ModuleCategory.class);
/*  48 */   private final Map<Module.ModuleCategory, AnimationUtils> categoryScrollAnimation = new EnumMap<>(Module.ModuleCategory.class);
/*  49 */   private final Map<Module.ModuleCategory, List<Module>> modulesByCategory = new EnumMap<>(Module.ModuleCategory.class);
/*  50 */   private final List<Module> allModules = new ArrayList<>();
/*     */   
/*     */   private float x;
/*     */   private float y;
/*     */   private BindSetting bindingSetting;
/*     */   private TextSetting editingTextSetting;
/*     */   private Module bindingModule;
/*     */   private float renderOffsetY;
/*     */   private boolean searchActive;
/*  59 */   private String searchText = "";
/*  60 */   private String undoSearchText = "";
/*  61 */   private int searchCursor = 0;
/*  62 */   private int searchSelectionAnchor = 0;
/*  63 */   private int searchSelectionCursor = 0;
/*     */   private boolean searchDragging;
/*     */   
/*     */   public ClickGuiState() {
/*  67 */     refreshModules();
/*     */   }
/*     */   
/*     */   public void refreshModules() {
/*  71 */     this.allModules.clear();
/*  72 */     this.allModules.addAll(ModuleClass.INSTANCE.getObject().stream()
/*  73 */         .filter(module -> (!"AutoBuy".equals(module.getName()) && !"AutoForest".equals(module.getName())))
/*  74 */         .toList());
/*  75 */     for (Module.ModuleCategory category : Module.ModuleCategory.values()) {
/*  76 */       this.modulesByCategory.put(category, this.allModules.stream().filter(module -> (module.getCategory() == category)).toList());
/*  77 */       this.categoryScrollTarget.putIfAbsent(category, Float.valueOf(0.0F));
/*  78 */       this.categoryScrollAnimation.putIfAbsent(category, new AnimationUtils(0.0F, 8.0F, Easings.CUBIC_OUT));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void updatePosition(class_1041 window, int categoryCount) {
/*  83 */     float totalCategoriesWidth = ClickGuiLayout.getTotalCategoriesWidth(categoryCount);
/*  84 */     this.x = window.method_4486() / 2.0F - totalCategoriesWidth / 2.0F;
/*  85 */     this.y = window.method_4502() / 2.0F - 137.5F;
/*     */   }
/*     */   
/*     */   public float getX() {
/*  89 */     return this.x;
/*     */   }
/*     */   
/*     */   public float getY() {
/*  93 */     return this.y;
/*     */   }
/*     */   
/*     */   public float getRenderOffsetY() {
/*  97 */     return this.renderOffsetY;
/*     */   }
/*     */   
/*     */   public void setRenderOffsetY(float renderOffsetY) {
/* 101 */     this.renderOffsetY = renderOffsetY;
/*     */   }
/*     */   
/*     */   public List<Module> getModules(Module.ModuleCategory category) {
/* 105 */     List<Module> modules = this.modulesByCategory.getOrDefault(category, List.of());
/* 106 */     if (this.searchText.isBlank()) {
/* 107 */       return modules;
/*     */     }
/*     */     
/* 110 */     String query = this.searchText.toLowerCase(Locale.ROOT);
/* 111 */     return modules.stream()
/* 112 */       .filter(module -> (module.getName().toLowerCase(Locale.ROOT).contains(query) || module.getDisplayName().toLowerCase(Locale.ROOT).contains(query) || module.getDisplayDescription().toLowerCase(Locale.ROOT).contains(query)))
/*     */ 
/*     */       
/* 115 */       .toList();
/*     */   }
/*     */   
/*     */   public List<Module> getAllModules() {
/* 119 */     return this.allModules;
/*     */   }
/*     */   
/*     */   public String toEnglish(String text) {
/* 123 */     StringBuilder result = new StringBuilder();
/* 124 */     for (char c : text.toCharArray()) {
/* 125 */       result.append(RU_TO_EN.getOrDefault(Character.valueOf(c), Character.valueOf(c)));
/*     */     }
/* 127 */     return result.toString();
/*     */   }
/*     */   
/*     */   public float getSliderPos(FloatSetting setting) {
/* 131 */     float delta = setting.getMax() - setting.getMin();
/* 132 */     return (setting.get() - setting.getMin()) / delta;
/*     */   }
/*     */   
/*     */   public float getSliderValue(FloatSetting setting, float posX, double mouseX) {
/* 136 */     float delta = setting.getMax() - setting.getMin();
/* 137 */     float clickedX = (float)mouseX - posX;
/* 138 */     float value = Math.max(0.0F, Math.min(1.0F, clickedX / 79.0F));
/* 139 */     float outValue = setting.getMin() + delta * value;
/* 140 */     float increment = setting.getIncrement();
/* 141 */     outValue = Math.round(outValue / increment) * increment;
/* 142 */     return Math.max(setting.getMin(), Math.min(setting.getMax(), outValue));
/*     */   }
/*     */   
/*     */   public void beginSliderDrag(FloatSetting setting, double mouseX) {
/* 146 */     this.sliderDragMouseX.put(setting, Double.valueOf(mouseX));
/* 147 */     this.sliderDragRemainder.put(setting, Double.valueOf(0.0D));
/*     */   }
/*     */   
/*     */   public void endSliderDrag(FloatSetting setting) {
/* 151 */     this.sliderDragMouseX.remove(setting);
/* 152 */     this.sliderDragRemainder.remove(setting);
/*     */   }
/*     */   
/*     */   public float updateActiveSliderValue(FloatSetting setting, double mouseX) {
/* 156 */     double lastMouseX = ((Double)this.sliderDragMouseX.getOrDefault(setting, Double.valueOf(mouseX))).doubleValue();
/* 157 */     this.sliderDragMouseX.put(setting, Double.valueOf(mouseX));
/*     */     
/* 159 */     double deltaX = mouseX - lastMouseX;
/* 160 */     if (Math.abs(deltaX) < 1.0E-4D) {
/* 161 */       return setting.get();
/*     */     }
/*     */     
/* 164 */     float range = setting.getMax() - setting.getMin();
/* 165 */     float increment = setting.getIncrement();
/* 166 */     if (range <= 0.0F || increment <= 0.0F) {
/* 167 */       return setting.get();
/*     */     }
/*     */     
/* 170 */     double steps = (range / increment);
/* 171 */     if (steps <= 0.0D) {
/* 172 */       return setting.get();
/*     */     }
/*     */     
/* 175 */     double pixelsPerStep = 79.0D / steps;
/* 176 */     if (pixelsPerStep <= 0.0D) {
/* 177 */       return setting.get();
/*     */     }
/*     */     
/* 180 */     double accumulated = ((Double)this.sliderDragRemainder.getOrDefault(setting, Double.valueOf(0.0D))).doubleValue() + deltaX;
/* 181 */     int wholeSteps = (int)(accumulated / pixelsPerStep);
/* 182 */     if (wholeSteps == 0) {
/* 183 */       this.sliderDragRemainder.put(setting, Double.valueOf(accumulated));
/* 184 */       return setting.get();
/*     */     } 
/*     */     
/* 187 */     this.sliderDragRemainder.put(setting, Double.valueOf(accumulated - wholeSteps * pixelsPerStep));
/*     */     
/* 189 */     float value = setting.get() + wholeSteps * increment;
/* 190 */     value = Math.round(value / increment) * increment;
/* 191 */     return Math.max(setting.getMin(), Math.min(setting.getMax(), value));
/*     */   }
/*     */   
/*     */   public float getScroll(Module.ModuleCategory category) {
/* 195 */     AnimationUtils animation = this.categoryScrollAnimation.computeIfAbsent(category, key -> new AnimationUtils(0.0F, 8.0F, Easings.CUBIC_OUT));
/* 196 */     animation.update(((Float)this.categoryScrollTarget.getOrDefault(category, Float.valueOf(0.0F))).floatValue());
/* 197 */     return animation.getValue();
/*     */   }
/*     */   
/*     */   public void clampScroll(Module.ModuleCategory category, float contentHeight) {
/* 201 */     float totalHeight = getTotalModulesHeight(category);
/* 202 */     float maxScroll = Math.min(0.0F, contentHeight - totalHeight);
/* 203 */     float currentTarget = ((Float)this.categoryScrollTarget.getOrDefault(category, Float.valueOf(0.0F))).floatValue();
/* 204 */     if (currentTarget < maxScroll || currentTarget > 0.0F) {
/* 205 */       this.categoryScrollTarget.put(category, Float.valueOf(Math.max(maxScroll, Math.min(0.0F, currentTarget))));
/*     */     }
/*     */   }
/*     */   
/*     */   public void addScroll(Module.ModuleCategory category, double verticalAmount, float contentHeight) {
/* 210 */     float totalHeight = getTotalModulesHeight(category);
/* 211 */     float maxScroll = Math.min(0.0F, contentHeight - totalHeight);
/* 212 */     float currentTarget = ((Float)this.categoryScrollTarget.getOrDefault(category, Float.valueOf(0.0F))).floatValue();
/* 213 */     float newTarget = currentTarget + (float)(verticalAmount * 20.0D);
/* 214 */     this.categoryScrollTarget.put(category, Float.valueOf(Math.max(maxScroll, Math.min(0.0F, newTarget))));
/*     */   }
/*     */   
/*     */   public float getTotalModulesHeight(Module.ModuleCategory category) {
/* 218 */     float totalHeight = 0.0F;
/* 219 */     for (Module module : getModules(category)) {
/* 220 */       totalHeight += 4.0F + ClickGuiLayout.getModuleHeight(module, getOpenProgress(module));
/*     */     }
/* 222 */     return totalHeight;
/*     */   }
/*     */   
/*     */   public float getOpenProgress(Module module) {
/* 226 */     AnimationUtils animation = this.moduleOpenAnimation.computeIfAbsent(module, key -> new AnimationUtils(module.isOpen() ? 1.0F : 0.0F, 14.0F, Easings.CUBIC_OUT));
/*     */ 
/*     */ 
/*     */     
/* 230 */     animation.update(module.isOpen() ? 1.0F : 0.0F);
/* 231 */     return animation.getValue();
/*     */   }
/*     */   
/*     */   public float updateDotsRotation(Module module, float targetAngle) {
/* 235 */     float currentAngle = ((Float)this.dotsRotation.getOrDefault(module, Float.valueOf(targetAngle))).floatValue();
/* 236 */     currentAngle += (targetAngle - currentAngle) * 0.06F;
/* 237 */     if (Math.abs(targetAngle - currentAngle) < 0.001F) {
/* 238 */       currentAngle = targetAngle;
/*     */     }
/* 240 */     this.dotsRotation.put(module, Float.valueOf(currentAngle));
/* 241 */     return currentAngle;
/*     */   }
/*     */   
/*     */   public AnimationUtils getBooleanBackgroundAnimation(BooleanSetting setting) {
/* 245 */     return this.booleanBackgroundAnimation.computeIfAbsent(setting, key -> new AnimationUtils(setting.isState() ? 1.0F : 0.0F, 15.0F, Easings.CUBIC_OUT));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnimationUtils getBooleanCircleAnimation(BooleanSetting setting) {
/* 252 */     return this.booleanCircleAnimation.computeIfAbsent(setting, key -> new AnimationUtils(setting.isState() ? 1.0F : 0.0F, 8.2F, Easings.BACK_OUT));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnimationUtils getSliderAnimation(FloatSetting setting) {
/* 259 */     return this.sliderAnimation.computeIfAbsent(setting, key -> new AnimationUtils(getSliderPos(setting), 12.0F, Easings.CUBIC_OUT));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnimationUtils getModeAnimation(String key, boolean selected) {
/* 266 */     return this.modeAnimation.computeIfAbsent(key, unused -> new AnimationUtils(selected ? 1.0F : 0.0F, 10.0F, Easings.CUBIC_OUT));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnimationUtils getListAnimation(String key, boolean selected) {
/* 273 */     return this.listAnimation.computeIfAbsent(key, unused -> new AnimationUtils(selected ? 1.0F : 0.0F, 10.0F, Easings.CUBIC_OUT));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnimationUtils getBindAnimation(String key, boolean binding) {
/* 280 */     return this.bindAnimation.computeIfAbsent(key, unused -> new AnimationUtils(binding ? 1.0F : 0.0F, 10.0F, Easings.CUBIC_OUT));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnimationUtils getTextHoverAnimation(String key, boolean hovered) {
/* 287 */     return this.textHoverAnimation.computeIfAbsent(key, unused -> new AnimationUtils(hovered ? 1.0F : 0.0F, 9.0F, Easings.CUBIC_OUT));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float advanceTextScrollPhase(String key, boolean hovered) {
/* 294 */     float phase = ((Float)this.textScrollPhase.getOrDefault(key, Float.valueOf(0.0F))).floatValue();
/* 295 */     boolean wasHovered = ((Boolean)this.textScrollHovered.getOrDefault(key, Boolean.valueOf(false))).booleanValue();
/* 296 */     boolean finishing = ((Boolean)this.textScrollFinishing.getOrDefault(key, Boolean.valueOf(false))).booleanValue();
/*     */     
/* 298 */     if (hovered) {
/* 299 */       phase += 0.004F;
/* 300 */       if (phase > 1.0F) {
/* 301 */         phase--;
/*     */       }
/* 303 */       finishing = false;
/*     */     } else {
/* 305 */       if (wasHovered && phase > 0.0F) {
/* 306 */         finishing = true;
/*     */       }
/* 308 */       if (finishing) {
/* 309 */         phase += 0.004F;
/* 310 */         if (phase >= 1.0F) {
/* 311 */           phase = 0.0F;
/* 312 */           finishing = false;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 317 */     this.textScrollHovered.put(key, Boolean.valueOf(hovered));
/* 318 */     this.textScrollFinishing.put(key, Boolean.valueOf(finishing));
/* 319 */     this.textScrollPhase.put(key, Float.valueOf(phase));
/* 320 */     return phase;
/*     */   }
/*     */   
/*     */   public boolean isTextScrollActive(String key, boolean hovered) {
/* 324 */     return (hovered || ((Boolean)this.textScrollFinishing.getOrDefault(key, Boolean.valueOf(false))).booleanValue());
/*     */   }
/*     */   
/*     */   public BindSetting getBindingSetting() {
/* 328 */     return this.bindingSetting;
/*     */   }
/*     */   
/*     */   public void setBindingSetting(BindSetting bindingSetting) {
/* 332 */     this.bindingSetting = bindingSetting;
/*     */   }
/*     */   
/*     */   public Module getBindingModule() {
/* 336 */     return this.bindingModule;
/*     */   }
/*     */   
/*     */   public void setBindingModule(Module bindingModule) {
/* 340 */     this.bindingModule = bindingModule;
/*     */   }
/*     */   
/*     */   public TextSetting getEditingTextSetting() {
/* 344 */     return this.editingTextSetting;
/*     */   }
/*     */   
/*     */   public void setEditingTextSetting(TextSetting editingTextSetting) {
/* 348 */     this.editingTextSetting = editingTextSetting;
/*     */   }
/*     */   
/*     */   public boolean isSearchActive() {
/* 352 */     return this.searchActive;
/*     */   }
/*     */   
/*     */   public void setSearchActive(boolean searchActive) {
/* 356 */     this.searchActive = searchActive;
/*     */   }
/*     */   
/*     */   public String getSearchText() {
/* 360 */     return this.searchText;
/*     */   }
/*     */   
/*     */   public void appendSearchChar(char chr) {
/* 364 */     if (Character.isISOControl(chr) || (this.searchText.length() >= 24 && !hasSearchSelection())) {
/*     */       return;
/*     */     }
/* 367 */     replaceSearchSelection(String.valueOf(chr));
/*     */   }
/*     */   
/*     */   public void removeLastSearchChar() {
/* 371 */     if (hasSearchSelection()) {
/* 372 */       replaceSearchSelection("");
/*     */       return;
/*     */     } 
/* 375 */     if (this.searchCursor > 0) {
/* 376 */       rememberSearchUndo();
/* 377 */       this.searchText = this.searchText.substring(0, this.searchCursor - 1) + this.searchText.substring(0, this.searchCursor - 1);
/* 378 */       this.searchCursor--;
/* 379 */       clearSearchSelection();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void clearSearchText() {
/* 384 */     rememberSearchUndo();
/* 385 */     this.searchText = "";
/* 386 */     this.searchCursor = 0;
/* 387 */     clearSearchSelection();
/*     */   }
/*     */   
/*     */   public void setSearchText(String searchText) {
/* 391 */     rememberSearchUndo();
/* 392 */     this.searchText = sanitizeSearchText(searchText);
/* 393 */     this.searchCursor = this.searchText.length();
/* 394 */     clearSearchSelection();
/*     */   }
/*     */   
/*     */   public void restoreSearchUndo() {
/* 398 */     String current = this.searchText;
/* 399 */     this.searchText = (this.undoSearchText == null) ? "" : this.undoSearchText;
/* 400 */     this.undoSearchText = current;
/* 401 */     this.searchCursor = this.searchText.length();
/* 402 */     clearSearchSelection();
/*     */   }
/*     */   
/*     */   public int getSearchCursor() {
/* 406 */     return this.searchCursor;
/*     */   }
/*     */   
/*     */   public int getSearchSelectionStart() {
/* 410 */     return Math.min(this.searchSelectionAnchor, this.searchSelectionCursor);
/*     */   }
/*     */   
/*     */   public int getSearchSelectionEnd() {
/* 414 */     return Math.max(this.searchSelectionAnchor, this.searchSelectionCursor);
/*     */   }
/*     */   
/*     */   public boolean hasSearchSelection() {
/* 418 */     return (getSearchSelectionStart() != getSearchSelectionEnd());
/*     */   }
/*     */   
/*     */   public String getSelectedSearchText() {
/* 422 */     if (!hasSearchSelection()) {
/* 423 */       return "";
/*     */     }
/* 425 */     return this.searchText.substring(getSearchSelectionStart(), getSearchSelectionEnd());
/*     */   }
/*     */   
/*     */   public void selectAllSearchText() {
/* 429 */     this.searchSelectionAnchor = 0;
/* 430 */     this.searchSelectionCursor = this.searchText.length();
/* 431 */     this.searchCursor = this.searchText.length();
/*     */   }
/*     */   
/*     */   public void setSearchCursor(int cursor, boolean keepSelection) {
/* 435 */     this.searchCursor = clampSearchIndex(cursor);
/* 436 */     if (keepSelection) {
/* 437 */       this.searchSelectionCursor = this.searchCursor;
/*     */     } else {
/* 439 */       this.searchSelectionAnchor = this.searchCursor;
/* 440 */       this.searchSelectionCursor = this.searchCursor;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void startSearchSelection(int index) {
/* 445 */     this.searchCursor = clampSearchIndex(index);
/* 446 */     this.searchSelectionAnchor = this.searchCursor;
/* 447 */     this.searchSelectionCursor = this.searchCursor;
/* 448 */     this.searchDragging = true;
/*     */   }
/*     */   
/*     */   public void updateSearchSelection(int index) {
/* 452 */     if (!this.searchDragging) {
/*     */       return;
/*     */     }
/* 455 */     this.searchCursor = clampSearchIndex(index);
/* 456 */     this.searchSelectionCursor = this.searchCursor;
/*     */   }
/*     */   
/*     */   public void stopSearchSelection() {
/* 460 */     this.searchDragging = false;
/*     */   }
/*     */   
/*     */   public boolean isSearchDragging() {
/* 464 */     return this.searchDragging;
/*     */   }
/*     */   
/*     */   public void replaceSearchSelection(String text) {
/* 468 */     rememberSearchUndo();
/* 469 */     String insert = sanitizeSearchText(text);
/* 470 */     int selectionStart = getSearchSelectionStart();
/* 471 */     int selectionEnd = getSearchSelectionEnd();
/* 472 */     if (!hasSearchSelection()) {
/* 473 */       selectionStart = this.searchCursor;
/* 474 */       selectionEnd = this.searchCursor;
/*     */     } 
/* 476 */     int available = Math.max(0, 24 - this.searchText.length() - selectionEnd - selectionStart);
/* 477 */     if (insert.length() > available) {
/* 478 */       insert = insert.substring(0, available);
/*     */     }
/* 480 */     this.searchText = this.searchText.substring(0, selectionStart) + this.searchText.substring(0, selectionStart) + insert;
/* 481 */     this.searchCursor = selectionStart + insert.length();
/* 482 */     clearSearchSelection();
/*     */   }
/*     */   
/*     */   private void clearSearchSelection() {
/* 486 */     this.searchSelectionAnchor = this.searchCursor;
/* 487 */     this.searchSelectionCursor = this.searchCursor;
/* 488 */     this.searchDragging = false;
/*     */   }
/*     */   
/*     */   private int clampSearchIndex(int index) {
/* 492 */     return Math.max(0, Math.min(this.searchText.length(), index));
/*     */   }
/*     */   
/*     */   private void rememberSearchUndo() {
/* 496 */     this.undoSearchText = this.searchText;
/*     */   }
/*     */   
/*     */   private String sanitizeSearchText(String text) {
/* 500 */     if (text == null || text.isEmpty()) {
/* 501 */       return "";
/*     */     }
/*     */     
/* 504 */     StringBuilder builder = new StringBuilder();
/* 505 */     for (int i = 0; i < text.length() && builder.length() < 24; i++) {
/* 506 */       char chr = text.charAt(i);
/* 507 */       if (!Character.isISOControl(chr)) {
/* 508 */         builder.append(chr);
/*     */       }
/*     */     } 
/* 511 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\clien\\ui\clickgui\ClickGuiState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */