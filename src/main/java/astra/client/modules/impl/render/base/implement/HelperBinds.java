/*     */ package shame.astra.client.modules.impl.render.base.implement;
/*     */ 
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.class_1799;
/*     */ import net.minecraft.class_1935;
/*     */ import net.minecraft.class_332;
/*     */ import net.minecraft.class_4587;
/*     */ import shame.astra.api.events.implement.EventRender;
/*     */ import shame.astra.api.storages.implement.helpertstorages.enumvar.ModuleClass;
/*     */ import shame.astra.api.utils.animation.AnimationUtils;
/*     */ import shame.astra.api.utils.animation.Easings;
/*     */ import shame.astra.api.utils.color.ColorUtils;
/*     */ import shame.astra.api.utils.draggable.Draggable;
/*     */ import shame.astra.api.utils.input.KeyBoardUtils;
/*     */ import shame.astra.api.utils.render.RenderUtils;
/*     */ import shame.astra.api.utils.render.fonts.msdf.Font;
/*     */ import shame.astra.api.utils.render.fonts.msdf.Fonts;
/*     */ import shame.astra.astra;
/*     */ import shame.astra.client.modules.impl.misc.ServerHelper;
/*     */ import shame.astra.client.modules.impl.render.base.InterfaceProcessing;
/*     */ 
/*     */ public class HelperBinds extends InterfaceProcessing {
/*  25 */   private final AnimationUtils widthAnimation = new AnimationUtils(80.0F, 10.5F, Easings.QUAD_OUT);
/*     */   
/*     */   public HelperBinds(Draggable draggable) {
/*  28 */     super(draggable);
/*     */   }
/*     */   
/*     */   private Font issue(int size) {
/*  32 */     return Fonts.getFont("suisse", size);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRender(EventRender.Default eventRender) {
/*  37 */     List<ServerHelper.HelperBind> binds = getVisibleBinds();
/*  38 */     if (binds.isEmpty()) {
/*  39 */       this.widthAnimation.update(0.0F);
/*  40 */       this.draggable.setWidth(0.0F);
/*  41 */       this.draggable.setHeight(0.0F);
/*     */       
/*     */       return;
/*     */     } 
/*  45 */     if (ModuleClass.interfaceModule.style.is("Обычный")) {
/*  46 */       DefaultStyle(eventRender, binds);
/*     */     } else {
/*  48 */       WaveStyle(eventRender, binds);
/*     */     } 
/*     */     
/*  51 */     super.onRender(eventRender);
/*     */   }
/*     */   
/*     */   private List<ServerHelper.HelperBind> getVisibleBinds() {
/*  55 */     ServerHelper serverHelper = ServerHelper.INSTANCE;
/*  56 */     List<ServerHelper.HelperBind> binds = new ArrayList<>();
/*  57 */     if (serverHelper == null) return binds;
/*     */ 
/*     */ 
/*     */     
/*  61 */     List<ServerHelper.HelperBind> helperBinds = serverHelper.isLonyMode() ? serverHelper.getLonyHelperBinds() : serverHelper.getSpookyHelperBinds();
/*     */     
/*  63 */     for (ServerHelper.HelperBind bind : helperBinds) {
/*  64 */       if (bind.bind().getKey() != -1) {
/*  65 */         binds.add(bind);
/*     */       }
/*     */     } 
/*     */     
/*  69 */     return binds;
/*     */   }
/*     */   
/*     */   private void DefaultStyle(EventRender.Default eventRender, List<ServerHelper.HelperBind> binds) {
/*  73 */     class_4587 matrices = eventRender.getContext().method_51448();
/*  74 */     float x = this.draggable.getX();
/*  75 */     float y = this.draggable.getY();
/*  76 */     int colorTheme = getThemeColor();
/*     */     
/*  78 */     int fontSize = 13;
/*  79 */     Font keyFont = issue(fontSize);
/*  80 */     float height = 19.0F;
/*  81 */     float itemSize = 9.8F;
/*  82 */     float itemScale = 0.61F;
/*  83 */     float fontGap = 2.8F;
/*  84 */     float cellGap = 5.0F;
/*  85 */     float sidePadding = 6.0F;
/*  86 */     float width = getCompactWidth(binds, keyFont, itemSize, fontGap, cellGap, sidePadding, 60.0F);
/*     */     
/*  88 */     this.widthAnimation.update(width);
/*  89 */     float animatedWidth = this.widthAnimation.getValue();
/*     */     
/*  91 */     drawDefaultPanel(matrices, x, y, animatedWidth, height, colorTheme);
/*     */     
/*  93 */     if (binds.isEmpty()) {
/*  94 */       issue(12).draw(matrices, "Helper", x + 5.0F, y + 6.0F, ColorUtils.rgba(255, 255, 255, 230));
/*  95 */       this.draggable.setWidth(animatedWidth);
/*  96 */       this.draggable.setHeight(height);
/*     */       
/*     */       return;
/*     */     } 
/* 100 */     drawCompactBinds(eventRender.getContext(), binds, keyFont, x, y, height, itemSize, itemScale, fontGap, cellGap, sidePadding, 8.2F);
/*     */     
/* 102 */     this.draggable.setWidth(animatedWidth);
/* 103 */     this.draggable.setHeight(height);
/*     */   }
/*     */   
/*     */   private void WaveStyle(EventRender.Default eventRender, List<ServerHelper.HelperBind> binds) {
/* 107 */     class_4587 matrices = eventRender.getContext().method_51448();
/* 108 */     float x = this.draggable.getX();
/* 109 */     float y = this.draggable.getY();
/*     */     
/* 111 */     int time = (int)((float)(System.currentTimeMillis() % 2000L) / 2000.0F * 360.0F);
/* 112 */     int leftTop = ColorUtils.getThemeColor(time);
/* 113 */     int leftBottom = ColorUtils.getThemeColor(time + 30);
/* 114 */     int centerTop = ColorUtils.getThemeColor(time + 90);
/* 115 */     int centerBottom = ColorUtils.getThemeColor(time + 120);
/* 116 */     int rightTop = ColorUtils.getThemeColor(time + 180);
/* 117 */     int rightBottom = ColorUtils.getThemeColor(time + 210);
/*     */     
/* 119 */     Font keyFont = issue(14);
/* 120 */     float height = 22.0F;
/* 121 */     float itemSize = 11.0F;
/* 122 */     float itemScale = 0.69F;
/* 123 */     float fontGap = 3.5F;
/* 124 */     float cellGap = 6.0F;
/* 125 */     float sidePadding = 7.0F;
/* 126 */     float width = getCompactWidth(binds, keyFont, itemSize, fontGap, cellGap, sidePadding, 72.0F);
/*     */     
/* 128 */     this.widthAnimation.update(width);
/* 129 */     float animatedWidth = this.widthAnimation.getValue();
/*     */     
/* 131 */     if (binds.isEmpty()) {
/* 132 */       RenderUtils.drawWaveHudHeader(matrices, x, y, animatedWidth, 15.0F, 0.0F, 10.0F, 10.0F, leftTop, leftBottom, centerTop, centerBottom, rightTop, rightBottom);
/*     */       
/* 134 */       String title = "helper";
/* 135 */       float titleX = x + (animatedWidth - issue(15).getWidth(title)) / 2.0F;
/* 136 */       issue(15).drawStringWithShadow(matrices, title, titleX, y + 5.0F, -1);
/* 137 */       this.draggable.setWidth(animatedWidth);
/* 138 */       this.draggable.setHeight(18.0F);
/*     */       
/*     */       return;
/*     */     } 
/* 142 */     RenderUtils.drawWaveHudPanel(matrices, x, y, animatedWidth, height, ColorUtils.rgba(25, 25, 25, 150), 3.5F, 0.0F, 10.0F, 10.0F, leftTop, leftBottom, centerTop, centerBottom, rightTop, rightBottom);
/*     */ 
/*     */ 
/*     */     
/* 146 */     drawCompactBinds(eventRender.getContext(), binds, keyFont, x, y, height, itemSize, itemScale, fontGap, cellGap, sidePadding, 9.5F);
/*     */     
/* 148 */     this.draggable.setWidth(animatedWidth);
/* 149 */     this.draggable.setHeight(height);
/*     */   }
/*     */   
/*     */   private float getCompactWidth(List<ServerHelper.HelperBind> binds, Font keyFont, float itemSize, float fontGap, float cellGap, float sidePadding, float emptyWidth) {
/* 153 */     if (binds.isEmpty()) {
/* 154 */       return emptyWidth;
/*     */     }
/*     */     
/* 157 */     float width = sidePadding * 2.0F;
/* 158 */     for (int i = 0; i < binds.size(); i++) {
/* 159 */       String keyName = KeyBoardUtils.getBindName(((ServerHelper.HelperBind)binds.get(i)).bind().getKey());
/* 160 */       width += itemSize + fontGap + keyFont.getWidth(keyName);
/* 161 */       if (i < binds.size() - 1) {
/* 162 */         width += cellGap;
/*     */       }
/*     */     } 
/* 165 */     return width;
/*     */   }
/*     */ 
/*     */   
/*     */   private void drawCompactBinds(class_332 context, List<ServerHelper.HelperBind> binds, Font keyFont, float x, float y, float height, float itemSize, float itemScale, float fontGap, float cellGap, float sidePadding, float textOffsetY) {
/* 170 */     class_4587 matrices = context.method_51448();
/* 171 */     float offsetX = x + sidePadding;
/* 172 */     float itemY = y + (height - itemSize) * 0.5F;
/* 173 */     float textY = y + textOffsetY;
/*     */     
/* 175 */     for (int i = 0; i < binds.size(); i++) {
/* 176 */       ServerHelper.HelperBind bind = binds.get(i);
/* 177 */       String keyName = KeyBoardUtils.getBindName(bind.bind().getKey());
/* 178 */       drawItemIcon(context, new class_1799((class_1935)bind.item()), offsetX, itemY, itemScale);
/* 179 */       keyFont.draw(matrices, keyName, offsetX + itemSize + fontGap, textY, ColorUtils.rgba(255, 255, 255, 240));
/*     */       
/* 181 */       offsetX += itemSize + fontGap + keyFont.getWidth(keyName);
/* 182 */       if (i < binds.size() - 1) {
/* 183 */         offsetX += cellGap;
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void drawItemIcon(class_332 context, class_1799 stack, float x, float y, float scale) {
/* 189 */     class_4587 matrices = context.method_51448();
/* 190 */     RenderSystem.enableBlend();
/* 191 */     RenderSystem.defaultBlendFunc();
/* 192 */     RenderSystem.disableDepthTest();
/* 193 */     RenderSystem.depthMask(false);
/* 194 */     matrices.method_22903();
/* 195 */     matrices.method_46416(x, y, 0.0F);
/* 196 */     matrices.method_22905(scale, scale, 1.0F);
/* 197 */     context.method_51427(stack, 0, 0);
/* 198 */     matrices.method_22909();
/* 199 */     RenderSystem.depthMask(false);
/* 200 */     RenderSystem.disableDepthTest();
/*     */   }
/*     */   
/*     */   private void drawDefaultPanel(class_4587 matrices, float x, float y, float width, float height, int colorTheme) {
/* 204 */     RenderUtils.drawDefaultHudThemedPanel(matrices, x, y, width, height, 3.0F, 3.5F, colorTheme);
/* 205 */     if (isUnusualRectType()) {
/* 206 */       RenderUtils.drawHudSquarePattern(matrices, x, y, width, height, colorTheme);
/*     */     }
/*     */   }
/*     */   
/*     */   private int getThemeColor() {
/* 211 */     if (!astra.INSTANCE.themeStorage.getThemes().getTheme().getName().equals("Rainbow")) {
/* 212 */       return (astra.INSTANCE.themeStorage.getThemes().getTheme()).color[0];
/*     */     }
/* 214 */     return ColorUtils.getThemeColor();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\base\implement\HelperBinds.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */