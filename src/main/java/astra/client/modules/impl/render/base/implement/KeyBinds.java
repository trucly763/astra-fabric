/*     */ package shame.astra.client.modules.impl.render.base.implement;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ import shame.astra.api.utils.scissor.ScissorUtils;
/*     */ import shame.astra.astra;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.impl.render.base.InterfaceProcessing;
/*     */ 
/*     */ public class KeyBinds extends InterfaceProcessing {
/*  25 */   private final Map<Module, AnimationUtils> animations = new HashMap<>();
/*  26 */   private final AnimationUtils widthAnimation = new AnimationUtils(60.0F, 10.5F, Easings.QUAD_OUT);
/*     */   
/*  28 */   private static final Map<Character, Character> RU_TO_EN = new HashMap<>();
/*     */   static {
/*  30 */     String ru = "йцукенгшщзхъфывапролджэячсмитьбюЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ";
/*  31 */     String en = "qwertyuiop[]asdfghjkl;'zxcvbnm,.QWERTYUIOP[]ASDFGHJKL;'ZXCVBNM,.";
/*  32 */     int length = Math.min(ru.length(), en.length());
/*  33 */     for (int i = 0; i < length; i++) {
/*  34 */       RU_TO_EN.put(Character.valueOf(ru.charAt(i)), Character.valueOf(en.charAt(i)));
/*     */     }
/*     */   }
/*     */   
/*     */   public KeyBinds(Draggable draggable) {
/*  39 */     super(draggable);
/*     */   }
/*     */   
/*  42 */   private Font issue(int size) { return Fonts.getFont("suisse", size); } private Font icon(int size) {
/*  43 */     return Fonts.getFont("icon1", size);
/*     */   }
/*     */   private AnimationUtils getAnimation(Module module) {
/*  46 */     return this.animations.computeIfAbsent(module, m -> new AnimationUtils(0.0F, 10.5F, Easings.QUAD_OUT));
/*     */   }
/*     */   
/*     */   private String toEnglish(String text) {
/*  50 */     StringBuilder result = new StringBuilder();
/*  51 */     for (char c : text.toCharArray()) {
/*  52 */       result.append(RU_TO_EN.getOrDefault(Character.valueOf(c), Character.valueOf(c)));
/*     */     }
/*  54 */     return result.toString();
/*     */   }
/*     */   
/*     */   private int getStaticThemeColor() {
/*  58 */     int[] colors = astra.INSTANCE.themeStorage.getThemes().getTheme().getColor();
/*  59 */     if (colors == null || colors.length == 0) {
/*  60 */       return -1;
/*     */     }
/*     */     
/*  63 */     int color = colors[0];
/*  64 */     if ((color >> 24 & 0xFF) == 0) {
/*  65 */       color = color & 0xFFFFFF | 0xFF000000;
/*     */     }
/*  67 */     return color;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRender(EventRender.Default eventRender) {
/*  72 */     if (ModuleClass.interfaceModule.style.is("Обычный")) { DefaultStyle(eventRender); }
/*  73 */     else { WaveStyle(eventRender); }
/*  74 */      super.onRender(eventRender);
/*     */   }
/*     */   public void DefaultStyle(EventRender.Default eventRender) {
/*     */     int colorTheme;
/*  78 */     float baseX = this.draggable.getX(), y = this.draggable.getY();
/*     */     
/*  80 */     if (!astra.INSTANCE.themeStorage.getThemes().getTheme().getName().equals("Rainbow")) {
/*  81 */       colorTheme = (astra.INSTANCE.themeStorage.getThemes().getTheme()).color[0];
/*     */     } else {
/*  83 */       colorTheme = ColorUtils.getThemeColor();
/*     */     } 
/*  85 */     int staticAccentColor = getStaticThemeColor();
/*     */     
/*  87 */     float targetWidth = 64.0F;
/*  88 */     float targetHeight = 16.0F;
/*  89 */     int visibleCount = 0;
/*     */     ObjectListIterator<Module> objectListIterator1;
/*  91 */     for (objectListIterator1 = ModuleClass.INSTANCE.getObject().iterator(); objectListIterator1.hasNext(); ) { Module module = objectListIterator1.next();
/*  92 */       if (module.getKey() != -1) {
/*  93 */         AnimationUtils anim = getAnimation(module);
/*  94 */         anim.update(module.isEnable() ? 1.0F : 0.0F);
/*     */       }  }
/*     */ 
/*     */     
/*  98 */     for (objectListIterator1 = ModuleClass.INSTANCE.getObject().iterator(); objectListIterator1.hasNext(); ) { Module module = objectListIterator1.next();
/*  99 */       if (module.getKey() != -1) {
/* 100 */         AnimationUtils anim = getAnimation(module);
/* 101 */         float animValue = anim.getValue();
/*     */         
/* 103 */         if (animValue > 0.01F) {
/* 104 */           visibleCount++;
/* 105 */           String keyName = toEnglish(KeyBoardUtils.getKeyName(module.getKey()));
/* 106 */           float keyWidth = issue(10).getWidth(keyName);
/* 107 */           float moduleWidth = issue(12).getWidth(module.getDisplayName()) + keyWidth + 25.0F;
/* 108 */           if (moduleWidth > targetWidth) targetWidth = moduleWidth; 
/* 109 */           targetHeight += 12.0F * animValue;
/*     */         } 
/*     */       }  }
/*     */ 
/*     */     
/* 114 */     if (visibleCount > 0) targetHeight += 2.0F;
/*     */     
/* 116 */     this.widthAnimation.update(targetWidth);
/* 117 */     float width = this.widthAnimation.getValue() + 7.0F;
/* 118 */     float height = targetHeight;
/* 119 */     float rightEdge = baseX + 60.0F;
/* 120 */     float x = rightEdge - width;
/*     */     
/* 122 */     RenderUtils.drawDefaultHudElementRects(eventRender.getContext().method_51448(), x, y, width, height, colorTheme, isUnusualRectType());
/* 123 */     issue(14).draw(eventRender.getContext().method_51448(), "Binds", x + 5.0F, y + 6.0F, -1);
/* 124 */     icon(13).draw(eventRender.getContext().method_51448(), "f", rightEdge - 13.0F, y + 7.5F, colorTheme);
/*     */     
/* 126 */     float offsetY = 18.0F;
/* 127 */     for (ObjectListIterator<Module> objectListIterator2 = ModuleClass.INSTANCE.getObject().iterator(); objectListIterator2.hasNext(); ) { Module module = objectListIterator2.next();
/* 128 */       if (module.getKey() != -1) {
/* 129 */         AnimationUtils anim = getAnimation(module);
/* 130 */         float animValue = anim.getValue();
/*     */         
/* 132 */         if (animValue > 0.01F) {
/* 133 */           ScissorUtils.push();
/* 134 */           ScissorUtils.setFromComponentCoordinates(x, y, width, height);
/* 135 */           String keyName = toEnglish(KeyBoardUtils.getBindName(module.getKey()));
/* 136 */           float keyBoxWidth = Math.max(issue(10).getWidth(keyName) + 4.0F, 9.0F);
/*     */           
/* 138 */           int alpha = (int)(255.0F * animValue);
/* 139 */           int textColor = ColorUtils.rgba(255, 255, 255, alpha);
/* 140 */           int accentColor = ColorUtils.setAlphaColor(getStableThemeColor(), alpha);
/* 141 */           int grayColor = ColorUtils.rgba(55, 55, 55, alpha);
/* 142 */           int darkColor = ColorUtils.rgba(35, 35, 35, alpha);
/*     */           
/* 144 */           issue(12).draw(eventRender.getContext().method_51448(), module.getDisplayName(), x + 12.0F, y + 2.0F + offsetY, textColor);
/* 145 */           RenderUtils.drawRoundedRect(eventRender.getContext().method_51448(), x + 5.2F, y + offsetY + 0.3F, 2.55F, 5.7F, 0.15F, accentColor);
/*     */           
/* 147 */           float keyBoxX = rightEdge - keyBoxWidth - 5.0F;
/* 148 */           RenderUtils.drawDefaultHudInfoBox(eventRender.getContext().method_51448(), keyBoxX, y + offsetY, keyBoxWidth, grayColor, darkColor);
/* 149 */           issue(10).drawCenteredString(eventRender.getContext().method_51448(), keyName, keyBoxX + keyBoxWidth / 2.0F, y + offsetY + 2.8F, textColor);
/*     */           
/* 151 */           offsetY += 12.0F * animValue;
/* 152 */           ScissorUtils.pop();
/* 153 */           ScissorUtils.unset();
/*     */         } 
/*     */       }  }
/*     */ 
/*     */     
/* 158 */     this.draggable.setWidth(60.0F);
/* 159 */     this.draggable.setHeight(height);
/*     */   }
/*     */   private int getStableThemeColor() {
/* 162 */     if (!astra.INSTANCE.themeStorage.getThemes().getTheme().getName().equals("Rainbow")) {
/* 163 */       return (astra.INSTANCE.themeStorage.getThemes().getTheme()).color[0];
/*     */     }
/* 165 */     return ColorUtils.getThemeColor();
/*     */   }
/*     */   
/*     */   public void WaveStyle(EventRender.Default eventRender) {
/* 169 */     class_4587 context = eventRender.getContext().method_51448();
/* 170 */     float x = this.draggable.getX(), y = this.draggable.getY();
/*     */     
/* 172 */     int time = (int)((float)(System.currentTimeMillis() % 2000L) / 2000.0F * 360.0F);
/*     */     
/* 174 */     int leftTop = ColorUtils.getThemeColor(time);
/* 175 */     int leftBottom = ColorUtils.getThemeColor(time + 30);
/* 176 */     int centerTop = ColorUtils.getThemeColor(time + 90);
/* 177 */     int centerBottom = ColorUtils.getThemeColor(time + 120);
/* 178 */     int rightTop = ColorUtils.getThemeColor(time + 180);
/* 179 */     int rightBottom = ColorUtils.getThemeColor(time + 210);
/*     */     
/* 181 */     List<Module> activeModules = new ArrayList<>();
/* 182 */     for (ObjectListIterator<Module> objectListIterator = ModuleClass.INSTANCE.getObject().iterator(); objectListIterator.hasNext(); ) { Module module = objectListIterator.next();
/* 183 */       if (module.getKey() <= 0) {
/* 184 */         module.getAnimka().update(0.0F);
/*     */         continue;
/*     */       } 
/* 187 */       module.getAnimka().update(module.isEnable() ? 1.0F : 0.0F);
/* 188 */       if (module.getAnimka().getValue() > 0.01F) {
/* 189 */         activeModules.add(module);
/*     */       } }
/*     */ 
/*     */     
/* 193 */     float targetWidth = 84.0F;
/* 194 */     float height = 18.0F;
/* 195 */     int visibleModules = 0;
/*     */     
/* 197 */     for (Module module : activeModules) {
/* 198 */       float animValue = module.getAnimka().getValue();
/* 199 */       if (animValue <= 0.01F)
/* 200 */         continue;  visibleModules++;
/*     */       
/* 202 */       String line = module.getDisplayName().toLowerCase() + " >> toggle";
/* 203 */       targetWidth = Math.max(targetWidth, issue(14).getWidth(line) + 7.0F);
/* 204 */       height += 12.0F * animValue;
/*     */     } 
/*     */     
/* 207 */     this.widthAnimation.update(targetWidth);
/* 208 */     float animatedWidth = this.widthAnimation.getValue();
/*     */     
/* 210 */     if (visibleModules == 0) {
/* 211 */       float headerHeight = 18.0F;
/* 212 */       RenderUtils.drawWaveHudHeader(context, x, y, animatedWidth, 15.0F, 0.0F, 10.0F, 10.0F, leftTop, leftBottom, centerTop, centerBottom, rightTop, rightBottom);
/*     */ 
/*     */       
/* 215 */       String str = "keybinds";
/* 216 */       float f1 = x + (animatedWidth - issue(15).getWidth(str)) / 2.0F;
/* 217 */       issue(15).drawStringWithShadow(eventRender.getContext().method_51448(), str, f1, y + 5.0F, -1);
/*     */       
/* 219 */       this.draggable.setWidth(animatedWidth);
/* 220 */       this.draggable.setHeight(headerHeight);
/*     */       
/*     */       return;
/*     */     } 
/* 224 */     RenderUtils.drawWaveHudPanel(context, x, y, animatedWidth, height, ColorUtils.rgba(25, 25, 25, 150), 15.0F, 0.0F, 10.0F, 10.0F, leftTop, leftBottom, centerTop, centerBottom, rightTop, rightBottom);
/*     */ 
/*     */ 
/*     */     
/* 228 */     String title = "keybinds";
/* 229 */     float titleX = x + (animatedWidth - issue(15).getWidth(title)) / 2.0F;
/* 230 */     issue(15).drawStringWithShadow(eventRender.getContext().method_51448(), title, titleX, y + 5.0F, -1);
/*     */     
/* 232 */     float yOffset = 18.0F;
/* 233 */     for (Module module : activeModules) {
/* 234 */       float animValue = module.getAnimka().getValue();
/* 235 */       if (animValue <= 0.01F)
/*     */         continue; 
/* 237 */       ScissorUtils.push();
/* 238 */       ScissorUtils.setFromComponentCoordinates(x, y, animatedWidth, height);
/*     */       
/* 240 */       int alpha = (int)(255.0F * animValue);
/* 241 */       int textColor = ColorUtils.rgba(255, 255, 255, alpha);
/*     */       
/* 243 */       String text = module.getDisplayName().toLowerCase() + " >> toggle";
/* 244 */       float textX = x + 5.5F;
/*     */       
/* 246 */       issue(14).draw(context, text, textX, y + yOffset + 2.0F, textColor);
/*     */       
/* 248 */       yOffset += 12.0F * animValue;
/*     */       
/* 250 */       ScissorUtils.unset();
/* 251 */       ScissorUtils.pop();
/*     */     } 
/*     */     
/* 254 */     this.draggable.setWidth(animatedWidth);
/* 255 */     this.draggable.setHeight(height);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\base\implement\KeyBinds.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */