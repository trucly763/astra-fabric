/*     */ package shame.astra.client.modules.impl.render.base.implement;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import net.minecraft.class_4587;
/*     */ import shame.astra.api.events.implement.EventRender;
/*     */ import shame.astra.api.storages.implement.helpertstorages.enumvar.ModuleClass;
/*     */ import shame.astra.api.utils.color.ColorUtils;
/*     */ import shame.astra.api.utils.draggable.Draggable;
/*     */ import shame.astra.api.utils.render.RenderUtils;
/*     */ import shame.astra.api.utils.render.fonts.msdf.Font;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.impl.render.base.InterfaceProcessing;
/*     */ 
/*     */ public class ArrayListHud extends InterfaceProcessing {
/*     */   private static final float LINE_HEIGHT = 9.5F;
/*     */   private static final float FLOW_SPEED = 1000.0F;
/*     */   private static final Comparator<ModuleEntry> MODULE_WIDTH_COMPARATOR;
/*     */   
/*     */   static {
/*  22 */     MODULE_WIDTH_COMPARATOR = Comparator.<ModuleEntry>comparingDouble(entry -> entry.width).reversed();
/*     */   }
/*  24 */   private final List<ModuleEntry> visibleModules = new ArrayList<>();
/*     */ 
/*     */   
/*  27 */   private float animationProgress = 0.0F;
/*  28 */   private long lastUpdateTime = System.currentTimeMillis();
/*  29 */   private float pulseAnimation = 0.0F;
/*  30 */   private float breathingAnimation = 0.0F;
/*     */   
/*     */   public ArrayListHud(Draggable draggable) {
/*  33 */     super(draggable);
/*     */   }
/*     */   
/*     */   private Font font() {
/*  37 */     return Fonts.getFont("suisse", 14);
/*     */   }
/*     */   
/*     */   private void drawFlowingText(class_4587 matrices, Font font, String text, float x, float y, int color, float alphaMul) {
/*  41 */     int textColor = ColorUtils.setAlphaColor(color, (int)(255.0F * alphaMul));
/*  42 */     font.draw(matrices, text, x, y, textColor);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRender(EventRender.Default eventRender) {
/*  47 */     class_4587 matrices = eventRender.getContext().method_51448();
/*  48 */     Font font = font();
/*  49 */     ObjectArrayList objectArrayList = ModuleClass.INSTANCE.getObject();
/*     */ 
/*     */     
/*  52 */     long currentTime = System.currentTimeMillis();
/*  53 */     float deltaTime = (float)(currentTime - this.lastUpdateTime) / 1000.0F;
/*  54 */     this.lastUpdateTime = currentTime;
/*  55 */     this.animationProgress += deltaTime * 2.0F;
/*  56 */     if (this.animationProgress > 360.0F) this.animationProgress -= 360.0F;
/*     */ 
/*     */     
/*  59 */     this.pulseAnimation += deltaTime * 3.0F;
/*  60 */     if (this.pulseAnimation > 360.0F) this.pulseAnimation -= 360.0F; 
/*  61 */     float pulseScale = 1.0F + (float)Math.sin(this.pulseAnimation) * 0.1F;
/*     */ 
/*     */     
/*  64 */     this.breathingAnimation += deltaTime * 2.5F;
/*  65 */     if (this.breathingAnimation > 360.0F) this.breathingAnimation -= 360.0F; 
/*  66 */     float breathingAlpha = 0.8F + (float)Math.sin(this.breathingAnimation) * 0.2F;
/*     */     
/*  68 */     this.visibleModules.clear();
/*  69 */     for (Module module : objectArrayList) {
/*  70 */       module.getArrayAnimka().update(module.isEnable() ? 1.0F : 0.0F);
/*  71 */       float anim = module.getArrayAnimka().getValue();
/*  72 */       if (anim <= 0.03F)
/*     */         continue; 
/*  74 */       String displayName = module.getDisplayName();
/*  75 */       this.visibleModules.add(new ModuleEntry(displayName.toLowerCase(), font.getWidth(displayName), anim));
/*     */     } 
/*  77 */     this.visibleModules.sort(MODULE_WIDTH_COMPARATOR);
/*  78 */     long now = System.currentTimeMillis();
/*     */     
/*  80 */     float x = this.draggable.getX();
/*  81 */     float y = this.draggable.getY();
/*  82 */     float maxWidth = 0.0F;
/*  83 */     boolean leftHalf = (x <= mc.method_22683().method_4486() * 0.5F);
/*     */     
/*  85 */     for (ModuleEntry entry : this.visibleModules) {
/*  86 */       maxWidth = Math.max(maxWidth, entry.width);
/*     */     }
/*     */     
/*  89 */     float yOffset = 0.0F;
/*  90 */     for (int i = 0; i < this.visibleModules.size(); i++) {
/*  91 */       float drawX; ModuleEntry entry = this.visibleModules.get(i);
/*  92 */       float anim = entry.anim;
/*  93 */       float lineStep = 9.5F * anim;
/*     */       
/*  95 */       int indexShift = (int)((float)now * 1000.0F / 10.0F) + i * 42;
/*  96 */       int rowColor = ColorUtils.getThemeColor(indexShift);
/*  97 */       int rowColor2 = ColorUtils.getThemeColor(indexShift + 90);
/*  98 */       int rowColor3 = ColorUtils.getThemeColor(indexShift + 45);
/*     */ 
/*     */       
/* 101 */       int glowAlpha = (int)((leftHalf ? '' : 'ª') * anim * breathingAlpha);
/* 102 */       int glow1 = ColorUtils.setAlphaColor(rowColor, glowAlpha);
/* 103 */       int glow2 = ColorUtils.setAlphaColor(rowColor2, glowAlpha);
/* 104 */       int glow3 = ColorUtils.setAlphaColor(rowColor3, (int)(glowAlpha * 0.7F));
/*     */       
/* 106 */       float textWidth = entry.width;
/*     */       
/* 108 */       if (leftHalf) {
/* 109 */         drawX = x - 3.0F;
/*     */       } else {
/* 111 */         drawX = x + maxWidth - textWidth - 3.0F;
/*     */       } 
/* 113 */       float drawY = y + yOffset + (1.0F - anim) * 7.0F;
/*     */ 
/*     */       
/* 116 */       float shadowX = leftHalf ? (drawX - 0.6F) : (drawX - 1.5F);
/* 117 */       float shadowW = leftHalf ? (textWidth - 4.0F) : textWidth;
/*     */ 
/*     */       
/* 120 */       RenderUtils.drawShadow(matrices, shadowX - 1.0F, drawY - 1.0F, shadowW + 2.0F, 8.0F, 6.0F, 14.0F * pulseScale, glow3, glow3, glow1, glow1);
/*     */       
/* 122 */       RenderUtils.drawShadow(matrices, shadowX, drawY, shadowW, 6.0F, 5.0F, 11.0F, glow2, glow2, glow1, glow1);
/*     */ 
/*     */       
/* 125 */       float textX = leftHalf ? (drawX - 0.8F) : (drawX - 2.0F);
/* 126 */       int textColor = ColorUtils.setAlphaColor(rowColor, (int)(255.0F * anim));
/* 127 */       int textColor2 = ColorUtils.setAlphaColor(rowColor2, (int)(255.0F * anim * 0.8F));
/*     */ 
/*     */       
/* 130 */       font.drawGradientStringHorizontal(matrices, entry.lowerName, textX, drawY + 1.5F, textColor, textColor2);
/*     */       
/* 132 */       yOffset += lineStep;
/*     */     } 
/*     */     
/* 135 */     if (yOffset > 0.5F) {
/* 136 */       float lineX = leftHalf ? (x - 6.5F) : (x + maxWidth - 7.0F);
/* 137 */       float lineWidth = 2.5F;
/*     */ 
/*     */       
/* 140 */       int topLineColor = ColorUtils.setAlphaColor(ColorUtils.getThemeColor((int)this.animationProgress), (int)(220.0F * breathingAlpha));
/* 141 */       int bottomLineColor = ColorUtils.setAlphaColor(ColorUtils.getThemeColor((int)(this.animationProgress + 180.0F) % 360), (int)(220.0F * breathingAlpha));
/* 142 */       int midLineColor = ColorUtils.setAlphaColor(ColorUtils.getThemeColor((int)(this.animationProgress + 90.0F) % 360), (int)(200.0F * breathingAlpha));
/*     */ 
/*     */       
/* 145 */       int lineGlow = ColorUtils.setAlphaColor(topLineColor, (int)(100.0F * breathingAlpha));
/* 146 */       RenderUtils.drawShadow(matrices, lineX - 1.0F, y, lineWidth + 2.0F, yOffset - 2.0F, 2.0F, 5.0F * pulseScale, lineGlow);
/*     */ 
/*     */       
/* 149 */       RenderUtils.drawGradientRect(matrices, lineX, y, lineWidth, yOffset - 2.0F, 0.0F, topLineColor, bottomLineColor);
/*     */     } 
/*     */     
/* 152 */     this.draggable.setWidth(maxWidth + 4.0F);
/* 153 */     this.draggable.setHeight(yOffset);
/*     */     
/* 155 */     super.onRender(eventRender);
/*     */   }
/*     */   private static final class ModuleEntry extends Record { private final String lowerName; private final float width; private final float anim;
/* 158 */     private ModuleEntry(String lowerName, float width, float anim) { this.lowerName = lowerName; this.width = width; this.anim = anim; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lshame/astra/client/modules/impl/render/base/implement/ArrayListHud$ModuleEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #158	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 158 */       //   0	7	0	this	Lshame/astra/client/modules/impl/render/base/implement/ArrayListHud$ModuleEntry; } public String lowerName() { return this.lowerName; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lshame/astra/client/modules/impl/render/base/implement/ArrayListHud$ModuleEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #158	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lshame/astra/client/modules/impl/render/base/implement/ArrayListHud$ModuleEntry; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lshame/astra/client/modules/impl/render/base/implement/ArrayListHud$ModuleEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #158	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lshame/astra/client/modules/impl/render/base/implement/ArrayListHud$ModuleEntry;
/* 158 */       //   0	8	1	o	Ljava/lang/Object; } public float width() { return this.width; } public float anim() { return this.anim; }
/*     */      }
/*     */ 
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\base\implement\ArrayListHud.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */