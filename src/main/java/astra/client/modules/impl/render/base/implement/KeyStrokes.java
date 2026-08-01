/*     */ package shame.astra.client.modules.impl.render.base.implement;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.class_310;
/*     */ import shame.astra.api.events.implement.EventRender;
/*     */ import shame.astra.api.utils.color.ColorUtils;
/*     */ import shame.astra.api.utils.draggable.Draggable;
/*     */ import shame.astra.api.utils.render.RenderUtils;
/*     */ import shame.astra.api.utils.render.fonts.msdf.Font;
/*     */ import shame.astra.api.utils.render.fonts.msdf.Fonts;
/*     */ import shame.astra.client.modules.impl.render.base.InterfaceProcessing;
/*     */ 
/*     */ public class KeyStrokes extends InterfaceProcessing {
/*  15 */   private final class_310 mc = class_310.method_1551();
/*     */   
/*  17 */   private final List<Long> leftClicks = new ArrayList<>();
/*     */   
/*     */   private boolean wasLmbPressed = false;
/*     */   
/*  21 */   private float animationProgress = 0.0F;
/*  22 */   private long lastUpdateTime = System.currentTimeMillis();
/*  23 */   private float pulseAnimation = 0.0F;
/*  24 */   private float breathingAnimation = 0.0F;
/*     */   
/*     */   public KeyStrokes(Draggable draggable) {
/*  27 */     super(draggable);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRender(EventRender.Default eventRender) {
/*  32 */     float x = this.draggable.getX(), y = this.draggable.getY();
/*     */ 
/*     */     
/*  35 */     long currentTime = System.currentTimeMillis();
/*  36 */     float deltaTime = (float)(currentTime - this.lastUpdateTime) / 1000.0F;
/*  37 */     this.lastUpdateTime = currentTime;
/*  38 */     this.animationProgress += deltaTime * 2.0F;
/*  39 */     if (this.animationProgress > 360.0F) this.animationProgress -= 360.0F;
/*     */ 
/*     */     
/*  42 */     this.pulseAnimation += deltaTime * 3.0F;
/*  43 */     if (this.pulseAnimation > 360.0F) this.pulseAnimation -= 360.0F; 
/*  44 */     float pulseScale = 1.0F + (float)Math.sin(this.pulseAnimation) * 0.1F;
/*     */ 
/*     */     
/*  47 */     this.breathingAnimation += deltaTime * 2.5F;
/*  48 */     if (this.breathingAnimation > 360.0F) this.breathingAnimation -= 360.0F; 
/*  49 */     float breathingAlpha = 0.8F + (float)Math.sin(this.breathingAnimation) * 0.2F;
/*     */     
/*  51 */     Font font = Fonts.getFont("suisse", 15);
/*  52 */     Font smallFont = Fonts.getFont("suisse", 10);
/*     */     
/*  54 */     float keySize = 20.0F;
/*  55 */     float gap = 2.0F;
/*     */     
/*  57 */     boolean wPressed = this.mc.field_1690.field_1894.method_1434();
/*  58 */     boolean aPressed = this.mc.field_1690.field_1913.method_1434();
/*  59 */     boolean sPressed = this.mc.field_1690.field_1881.method_1434();
/*  60 */     boolean dPressed = this.mc.field_1690.field_1849.method_1434();
/*  61 */     boolean spacePressed = this.mc.field_1690.field_1903.method_1434();
/*  62 */     boolean lmbPressed = this.mc.field_1690.field_1886.method_1434();
/*  63 */     boolean rmbPressed = this.mc.field_1690.field_1904.method_1434();
/*     */     
/*  65 */     if (lmbPressed && !this.wasLmbPressed) {
/*  66 */       this.leftClicks.add(Long.valueOf(currentTime));
/*     */     }
/*  68 */     this.wasLmbPressed = lmbPressed;
/*     */     
/*  70 */     this.leftClicks.removeIf(time -> (currentTime - time.longValue() > 1000L));
/*     */     
/*  72 */     int lmbCps = this.leftClicks.size();
/*     */     
/*  74 */     int themeColor = ColorUtils.getThemeColor((int)this.animationProgress);
/*  75 */     int themeColor2 = ColorUtils.getThemeColor((int)(this.animationProgress + 90.0F) % 360);
/*     */     
/*  77 */     float wX = x + keySize + gap;
/*  78 */     float wY = y;
/*  79 */     drawKey(eventRender, wX, wY, keySize, keySize, "W", wPressed, font, pulseScale, breathingAlpha, themeColor);
/*     */     
/*  81 */     float aX = x;
/*  82 */     float aY = y + keySize + gap;
/*  83 */     drawKey(eventRender, aX, aY, keySize, keySize, "A", aPressed, font, pulseScale, breathingAlpha, themeColor);
/*     */     
/*  85 */     float sX = x + keySize + gap;
/*  86 */     float sY = y + keySize + gap;
/*  87 */     drawKey(eventRender, sX, sY, keySize, keySize, "S", sPressed, font, pulseScale, breathingAlpha, themeColor);
/*     */     
/*  89 */     float dX = x + (keySize + gap) * 2.0F;
/*  90 */     float dY = y + keySize + gap;
/*  91 */     drawKey(eventRender, dX, dY, keySize, keySize, "D", dPressed, font, pulseScale, breathingAlpha, themeColor);
/*     */     
/*  93 */     float spaceWidth = keySize * 3.0F + gap * 2.0F;
/*  94 */     float spaceHeight = 20.0F;
/*  95 */     float spaceX = x;
/*  96 */     float spaceY = y + (keySize + gap) * 2.0F;
/*  97 */     drawKey(eventRender, spaceX, spaceY, spaceWidth, spaceHeight, "Space", spacePressed, font, pulseScale, breathingAlpha, themeColor);
/*     */     
/*  99 */     float mouseWidth = (spaceWidth - gap) / 2.0F;
/* 100 */     float mouseHeight = 20.0F;
/* 101 */     float lmbX = x;
/* 102 */     float lmbY = y + (keySize + gap) * 2.0F + spaceHeight + gap;
/*     */     
/* 104 */     drawKeyWithCps(eventRender, lmbX, lmbY, mouseWidth, mouseHeight, "LMB", lmbPressed, font, smallFont, lmbCps, themeColor, themeColor2, pulseScale, breathingAlpha);
/*     */     
/* 106 */     float rmbX = x + mouseWidth + gap;
/* 107 */     float rmbY = y + (keySize + gap) * 2.0F + spaceHeight + gap;
/* 108 */     drawKey(eventRender, rmbX, rmbY, mouseWidth, mouseHeight, "RMB", rmbPressed, font, pulseScale, breathingAlpha, themeColor);
/*     */     
/* 110 */     float totalWidth = keySize * 3.0F + gap * 2.0F;
/* 111 */     float totalHeight = keySize * 2.0F + gap + spaceHeight + gap + mouseHeight + gap;
/*     */     
/* 113 */     this.draggable.setWidth(totalWidth);
/* 114 */     this.draggable.setHeight(totalHeight);
/*     */     
/* 116 */     super.onRender(eventRender);
/*     */   }
/*     */   
/*     */   private void drawKey(EventRender.Default eventRender, float x, float y, float width, float height, String text, boolean pressed, Object font, float pulseScale, float breathingAlpha, int themeColor) {
/* 120 */     int bgColor = pressed ? ColorUtils.rgba(180, 180, 180, 200) : ColorUtils.rgba(25, 25, 25, 150);
/* 121 */     int textColor = pressed ? ColorUtils.rgba(0, 0, 0, 255) : ColorUtils.rgba(255, 255, 255, 255);
/*     */ 
/*     */     
/* 124 */     if (pressed) {
/* 125 */       int glowColor = ColorUtils.applyAlpha(themeColor, breathingAlpha * 0.4F);
/* 126 */       RenderUtils.drawShadow(eventRender.getContext().method_51448(), x - 1.0F, y - 1.0F, width + 2.0F, height + 2.0F, 2.0F, 5.0F * pulseScale, glowColor);
/*     */     } 
/*     */     
/* 129 */     RenderUtils.drawKeyStrokeRect(eventRender.getContext().method_51448(), x, y, width, height, 3.0F, bgColor);
/*     */     
/* 131 */     Font f = Fonts.getFont("suisse", 15);
/* 132 */     float textWidth = f.getWidth(text);
/* 133 */     float textHeight = 8.0F;
/*     */     
/* 135 */     float textX = x + (width - textWidth) / 2.0F;
/* 136 */     float textY = y + (height - textHeight) / 2.0F;
/*     */     
/* 138 */     f.draw(eventRender.getContext().method_51448(), text, textX - 0.5F, textY + 2.0F, textColor);
/*     */   }
/*     */   
/*     */   private void drawKeyWithCps(EventRender.Default eventRender, float x, float y, float width, float height, String text, boolean pressed, Object font, Object smallFont, int cps, int themeColor, int themeColor2, float pulseScale, float breathingAlpha) {
/* 142 */     int bgColor = pressed ? ColorUtils.rgba(180, 180, 180, 200) : ColorUtils.rgba(25, 25, 25, 150);
/* 143 */     int textColor = pressed ? ColorUtils.rgba(0, 0, 0, 255) : ColorUtils.rgba(255, 255, 255, 255);
/*     */ 
/*     */     
/* 146 */     if (pressed) {
/* 147 */       int glowColor = ColorUtils.applyAlpha(themeColor, breathingAlpha * 0.5F);
/* 148 */       RenderUtils.drawShadow(eventRender.getContext().method_51448(), x - 1.0F, y - 1.0F, width + 2.0F, height + 2.0F, 2.0F, 6.0F * pulseScale, glowColor);
/*     */     } 
/*     */     
/* 151 */     RenderUtils.drawKeyStrokeRect(eventRender.getContext().method_51448(), x, y, width, height, 3.0F, bgColor);
/*     */     
/* 153 */     Font f = Fonts.getFont("suisse", 15);
/* 154 */     Font sf = Fonts.getFont("suisse", 12);
/*     */     
/* 156 */     float textWidth = f.getWidth(text);
/* 157 */     float textX = x + (width - textWidth) / 2.0F;
/* 158 */     float textHeight = 8.0F;
/* 159 */     float textY = y + (height - textHeight) / 2.0F;
/* 160 */     f.draw(eventRender.getContext().method_51448(), text, textX - 0.5F, textY + 2.0F, textColor);
/*     */     
/* 162 */     String cpsText = "cps: " + cps;
/* 163 */     float cpsWidth = sf.getWidth(cpsText);
/* 164 */     float cpsX = x + (width - cpsWidth) / 2.0F;
/* 165 */     float cpsY = textY + 12.0F;
/*     */ 
/*     */     
/* 168 */     sf.drawGradientStringHorizontal(eventRender.getContext().method_51448(), cpsText, cpsX, cpsY - 3.0F, themeColor, themeColor2);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\base\implement\KeyStrokes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */