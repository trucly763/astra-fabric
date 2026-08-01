/*     */ package shame.astra.client.ui.clickgui;
/*     */ 
/*     */ import java.util.List;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*     */ import shame.astra.client.modules.settings.implement.ListSetting;
/*     */ import shame.astra.client.modules.settings.implement.ModeSetting;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ClickGuiLayout
/*     */ {
/*     */   public static final float WIDTH = 100.0F;
/*     */   public static final float HEIGHT = 275.0F;
/*     */   public static final float CATEGORY_PANEL_STEP = 108.0F;
/*     */   public static final float THEME_PANEL_Y = 100.0F;
/*     */   public static final float THEME_PANEL_H = 15.0F;
/*     */   public static final float THEME_BOX_SIZE = 8.0F;
/*     */   public static final float THEME_BOX_GAP = 4.0F;
/*     */   public static final float THEME_BOX_RADIUS = 2.0F;
/*     */   public static final float THEME_SIDE_PADDING = 4.0F;
/*     */   public static final float MODULE_PADDING = 3.0F;
/*     */   public static final float MODULE_GAP = 4.0F;
/*     */   public static final float MODULE_HEADER_HEIGHT = 20.0F;
/*     */   public static final float MODULE_INNER_WIDTH = 93.5F;
/*     */   public static final float SETTING_START_Y = 20.0F;
/*     */   public static final float SETTING_PADDING = 4.0F;
/*     */   public static final float SETTING_BOTTOM_PADDING = 3.0F;
/*     */   public static final float SETTING_LEFT = 10.0F;
/*     */   public static final float SETTING_RIGHT = 89.0F;
/*     */   public static final float SLIDER_WIDTH = 79.0F;
/*     */   public static final float TEXT_SETTING_WIDTH = 42.0F;
/*     */   public static final float CLICKABLE_WIDTH = 79.0F;
/*     */   public static final int SEARCH_MAX_CHARS = 24;
/*     */   public static final float SEARCH_WIDTH = 75.0F;
/*     */   public static final float SEARCH_HEIGHT = 18.0F;
/*     */   public static final float SEARCH_GAP = 8.0F;
/*     */   public static final float SEARCH_ICON_X = 3.5F;
/*     */   public static final float SEARCH_TEXT_X = 19.0F;
/*     */   public static final float SEARCH_RIGHT_PADDING = 8.0F;
/*     */   
/*     */   public static float getTotalCategoriesWidth(int categoryCount) {
/*  50 */     return 100.0F * categoryCount + 8.0F * (categoryCount - 1);
/*     */   }
/*     */   
/*     */   public static float getCategoryPanelX(float x, int index) {
/*  54 */     return x + index * 108.0F;
/*     */   }
/*     */   
/*     */   public static float getContentY(float y) {
/*  58 */     return y + 25.0F;
/*     */   }
/*     */   
/*     */   public static float getContentHeight() {
/*  62 */     return 245.0F;
/*     */   }
/*     */   
/*     */   public static float getSearchX(float x, int categoryCount) {
/*  66 */     return x + getTotalCategoriesWidth(categoryCount) / 2.0F - 37.5F;
/*     */   }
/*     */   
/*     */   public static float getSearchX(float x, int categoryCount, float searchWidth) {
/*  70 */     return x + getTotalCategoriesWidth(categoryCount) / 2.0F - searchWidth / 2.0F;
/*     */   }
/*     */   
/*     */   public static float getSearchY(float y) {
/*  74 */     return y + 275.0F + 8.0F;
/*     */   }
/*     */   
/*     */   public static boolean hasVisibleSettings(List<Setting> settings) {
/*  78 */     for (Setting setting : settings) {
/*  79 */       if (setting != null && setting.visible().booleanValue()) {
/*  80 */         return true;
/*     */       }
/*     */     } 
/*  83 */     return false;
/*     */   }
/*     */   
/*     */   public static float calculateModeSettingHeight(ModeSetting modeSetting) {
/*  87 */     return (modeSetting.getMods().size() * 10 + 12);
/*     */   }
/*     */   
/*     */   public static float calculateListSettingHeight(ListSetting listSetting) {
/*  91 */     int visibleCount = 0;
/*  92 */     for (BooleanSetting entry : listSetting.getSettings()) {
/*  93 */       if (entry.visible().booleanValue()) {
/*  94 */         visibleCount++;
/*     */       }
/*     */     } 
/*  97 */     return (visibleCount * 10 + 12);
/*     */   }
/*     */   
/*     */   public static float calculateSettingsHeight(Module module) {
/* 101 */     float height = 0.0F;
/* 102 */     List<Setting> settings = module.getSettings();
/* 103 */     if (settings == null || settings.isEmpty()) {
/* 104 */       return 0.0F;
/*     */     }
/*     */     
/* 107 */     boolean hasVisibleSetting = false;
/* 108 */     for (Setting setting : settings) {
/* 109 */       if (setting == null || !setting.visible().booleanValue()) {
/*     */         continue;
/*     */       }
/*     */       
/* 113 */       hasVisibleSetting = true;
/* 114 */       if (setting instanceof BooleanSetting || setting instanceof shame.astra.client.modules.settings.implement.BindSetting) {
/* 115 */         height += 12.0F; continue;
/* 116 */       }  if (setting instanceof shame.astra.client.modules.settings.implement.TextSetting) {
/* 117 */         height += 12.0F; continue;
/* 118 */       }  if (setting instanceof shame.astra.client.modules.settings.implement.FloatSetting) {
/* 119 */         height += 22.0F; continue;
/* 120 */       }  if (setting instanceof ModeSetting) { ModeSetting modeSetting = (ModeSetting)setting;
/* 121 */         height += calculateModeSettingHeight(modeSetting); continue; }
/* 122 */        if (setting instanceof ListSetting) { ListSetting listSetting = (ListSetting)setting;
/* 123 */         height += calculateListSettingHeight(listSetting); }
/*     */     
/*     */     } 
/*     */     
/* 127 */     if (hasVisibleSetting) {
/* 128 */       height += 3.0F;
/*     */     }
/* 130 */     return height;
/*     */   }
/*     */   
/*     */   public static float getModuleHeight(Module module, float openProgress) {
/* 134 */     return 20.0F + calculateSettingsHeight(module) * openProgress;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\clien\\ui\clickgui\ClickGuiLayout.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */