/*     */ package shame.astra.client.ui.clickgui;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import net.minecraft.class_1041;
/*     */ import net.minecraft.class_332;
/*     */ import shame.astra.api.storages.implement.ThemeStorage;
/*     */ import shame.astra.api.utils.color.ColorUtils;
/*     */ import shame.astra.api.utils.math.HoveringUtils;
/*     */ import shame.astra.api.utils.render.RenderUtils;
/*     */ import shame.astra.astra;
/*     */ 
/*     */ public class ClickGuiThemeSelector {
/*     */   public void render(class_332 context, class_1041 window, float offsetY, float alphaMul, int shadeColor) {
/*  14 */     if (context == null || window == null) {
/*     */       return;
/*     */     }
/*     */     
/*  18 */     ObjectArrayList<ThemeStorage.Themes> objectArrayList = astra.INSTANCE.themeStorage.getThemeList();
/*  19 */     if (objectArrayList == null || objectArrayList.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/*  23 */     float totalWidth = objectArrayList.size() * 8.0F + (objectArrayList.size() - 1) * 4.0F;
/*  24 */     float panelWidth = totalWidth + 8.0F;
/*  25 */     float panelX = getThemePanelX(window, panelWidth);
/*  26 */     float panelY = 100.0F + offsetY;
/*  27 */     float startX = panelX + 4.0F;
/*  28 */     float startY = panelY + 3.5F;
/*     */     
/*  30 */     RenderUtils.drawGradientRect(context
/*  31 */         .method_51448(), panelX, panelY, panelWidth, 15.0F, 3.5F, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  37 */         ColorUtils.darken(ColorUtils.getThemeColor(), 0.12F), 
/*  38 */         ColorUtils.darken(ColorUtils.getThemeColor(), 0.1F), false);
/*     */ 
/*     */     
/*  41 */     if ((shadeColor >> 24 & 0xFF) > 0) {
/*  42 */       RenderUtils.drawRoundedRect(context.method_51448(), panelX, panelY, panelWidth, 15.0F, 3.5F, shadeColor);
/*     */     }
/*     */     
/*  45 */     ThemeStorage.Themes selected = astra.INSTANCE.themeStorage.getThemes();
/*  46 */     for (int i = 0; i < objectArrayList.size(); i++) {
/*  47 */       ThemeStorage.Themes theme = objectArrayList.get(i);
/*  48 */       float boxX = startX + i * 12.0F;
/*  49 */       float boxY = startY;
/*  50 */       if (theme == selected) {
/*  51 */         RenderUtils.drawRoundedRect(context
/*  52 */             .method_51448(), boxX - 0.5F, boxY - 0.5F, 9.0F, 9.0F, 2.5F, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*  58 */             ColorUtils.setAlphaColor(-1, Math.max(1, (int)(200.0F * alphaMul))));
/*     */       }
/*     */       
/*  61 */       RenderUtils.drawRoundedRect(context
/*  62 */           .method_51448(), boxX, boxY, 8.0F, 8.0F, 2.0F, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  68 */           ColorUtils.applyAlpha(getThemeDisplayColor(theme), Math.max(0.55F, alphaMul)));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean handleClick(class_1041 window, double mouseX, double mouseY, int button, float offsetY) {
/*  74 */     if (window == null || button != 0) {
/*  75 */       return false;
/*     */     }
/*     */     
/*  78 */     ObjectArrayList<ThemeStorage.Themes> objectArrayList = astra.INSTANCE.themeStorage.getThemeList();
/*  79 */     if (objectArrayList == null || objectArrayList.isEmpty()) {
/*  80 */       return false;
/*     */     }
/*     */     
/*  83 */     float totalWidth = objectArrayList.size() * 8.0F + (objectArrayList.size() - 1) * 4.0F;
/*  84 */     float panelWidth = totalWidth + 8.0F;
/*  85 */     float panelX = getThemePanelX(window, panelWidth);
/*  86 */     float panelY = 100.0F + offsetY;
/*  87 */     float startX = panelX + 4.0F;
/*  88 */     float startY = panelY + 3.5F;
/*     */     
/*  90 */     if (!HoveringUtils.isHovered(mouseX, mouseY, panelX, panelY, panelWidth, 15.0D)) {
/*  91 */       return false;
/*     */     }
/*     */     
/*  94 */     for (int i = 0; i < objectArrayList.size(); i++) {
/*  95 */       float boxX = startX + i * 12.0F;
/*  96 */       float boxY = startY;
/*  97 */       if (HoveringUtils.isHovered(mouseX, mouseY, boxX, boxY, 8.0D, 8.0D)) {
/*  98 */         astra.INSTANCE.themeStorage.setThemes(objectArrayList.get(i));
/*  99 */         return true;
/*     */       } 
/*     */     } 
/* 102 */     return false;
/*     */   }
/*     */   
/*     */   private int getThemeDisplayColor(ThemeStorage.Themes theme) {
/* 106 */     int color = theme.getTheme().getColor(0);
/* 107 */     if (ColorUtils.alpha(color) == 0) {
/* 108 */       return ColorUtils.rgba(220, 220, 220, 180);
/*     */     }
/* 110 */     return color;
/*     */   }
/*     */   
/*     */   private float getThemePanelX(class_1041 window, float panelWidth) {
/* 114 */     return window.method_4486() / 2.0F - panelWidth / 2.0F;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\clien\\ui\clickgui\ClickGuiThemeSelector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */