/*     */ package shame.astra.client.modules.impl.render;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.class_1703;
/*     */ import net.minecraft.class_1735;
/*     */ import net.minecraft.class_1799;
/*     */ import net.minecraft.class_1802;
/*     */ import net.minecraft.class_332;
/*     */ import net.minecraft.class_437;
/*     */ import net.minecraft.class_4587;
/*     */ import net.minecraft.class_465;
/*     */ import net.minecraft.class_9288;
/*     */ import net.minecraft.class_9334;
/*     */ import org.lwjgl.glfw.GLFW;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import shame.astra.api.utils.color.ColorUtils;
/*     */ import shame.astra.api.utils.render.RenderUtils;
/*     */ import shame.astra.api.utils.render.fonts.msdf.Font;
/*     */ import shame.astra.client.modules.Module;
/*     */ 
/*     */ public class ShulkerPreview extends Module {
/*  24 */   public static ShulkerPreview INSTANCE = new ShulkerPreview();
/*     */   
/*     */   private static final float RECT_RADIUS = 5.0F;
/*     */   
/*     */   private static final int SLOT_SIZE = 18;
/*     */   
/*     */   private static final int PADDING = 7;
/*     */   private static final int ROWS = 3;
/*     */   private static final int COLS = 9;
/*     */   private static final int TITLE_HEIGHT = 14;
/*     */   private static final int SLOT_BG_COLOR = -7631989;
/*     */   private Field guiLeftField;
/*     */   private Field guiTopField;
/*     */   private static ShulkerPreview instance;
/*     */   
/*     */   public ShulkerPreview() {
/*  40 */     super("ShulkerPreview", "Показывает содержимое шалкера при наведении + CTRL", Module.ModuleCategory.RENDER);
/*  41 */     initReflection();
/*  42 */     instance = this;
/*     */   }
/*     */   
/*     */   public static ShulkerPreview getInstance() {
/*  46 */     return instance;
/*     */   }
/*     */   
/*     */   private void initReflection() {
/*     */     try {
/*  51 */       for (Field field : class_465.class.getDeclaredFields()) {
/*  52 */         if (field.getType() == int.class) {
/*  53 */           field.setAccessible(true);
/*  54 */           String name = field.getName();
/*  55 */           if (name.equals("x") || name.equals("field_2776") || name.contains("Left") || name.contains("guiLeft")) {
/*  56 */             this.guiLeftField = field;
/*  57 */           } else if (name.equals("y") || name.equals("field_2800") || name.contains("Top") || name.contains("guiTop")) {
/*  58 */             this.guiTopField = field;
/*     */           } 
/*     */         } 
/*     */       } 
/*  62 */     } catch (Exception exception) {}
/*     */   }
/*     */   
/*     */   private int getGuiLeft(class_465<?> screen) {
/*     */     try {
/*  67 */       if (this.guiLeftField != null) {
/*  68 */         return this.guiLeftField.getInt(screen);
/*     */       }
/*  70 */     } catch (Exception exception) {}
/*  71 */     return (mc.method_22683().method_4486() - 176) / 2;
/*     */   }
/*     */   
/*     */   private int getGuiTop(class_465<?> screen) {
/*     */     try {
/*  76 */       if (this.guiTopField != null) {
/*  77 */         return this.guiTopField.getInt(screen);
/*     */       }
/*  79 */     } catch (Exception exception) {}
/*  80 */     return (mc.method_22683().method_4502() - 166) / 2;
/*     */   }
/*     */   public void renderFromMixin(class_332 context, int mouseX, int mouseY) {
/*     */     class_465<?> handledScreen;
/*  84 */     if (!isEnable())
/*  85 */       return;  if (mc == null || mc.field_1724 == null || mc.field_1755 == null)
/*     */       return; 
/*  87 */     class_437 class_437 = mc.field_1755; if (class_437 instanceof class_465) { handledScreen = (class_465)class_437; }
/*     */     else { return; }
/*  89 */      long handle = mc.method_22683().method_4490();
/*  90 */     boolean isCtrlPressed = (GLFW.glfwGetKey(handle, 341) == 1);
/*     */     
/*  92 */     if (!isCtrlPressed)
/*     */       return; 
/*  94 */     class_1735 hoveredSlot = getHoveredSlot(handledScreen);
/*  95 */     if (hoveredSlot == null)
/*     */       return; 
/*  97 */     class_1799 stack = hoveredSlot.method_7677();
/*  98 */     if (!isShulkerBox(stack))
/*     */       return; 
/* 100 */     class_9288 container = (class_9288)stack.method_57824(class_9334.field_49622);
/* 101 */     if (container == null)
/*     */       return; 
/* 103 */     renderShulkerPreview(context, stack, container, mouseX, mouseY);
/*     */   }
/*     */   
/*     */   private class_1735 getHoveredSlot(class_465<?> screen) {
/*     */     try {
/* 108 */       class_1703 handler = screen.method_17577();
/* 109 */       if (handler == null || handler.field_7761 == null) return null;
/*     */       
/* 111 */       double mouseX = mc.field_1729.method_1603() * mc.method_22683().method_4486() / mc.method_22683().method_4480();
/* 112 */       double mouseY = mc.field_1729.method_1604() * mc.method_22683().method_4502() / mc.method_22683().method_4507();
/*     */       
/* 114 */       int guiLeft = getGuiLeft(screen);
/* 115 */       int guiTop = getGuiTop(screen);
/*     */       
/* 117 */       for (class_1735 slot : handler.field_7761) {
/* 118 */         int slotX = guiLeft + slot.field_7873;
/* 119 */         int slotY = guiTop + slot.field_7872;
/*     */         
/* 121 */         if (mouseX >= slotX && mouseX < (slotX + 16) && mouseY >= slotY && mouseY < (slotY + 16)) {
/* 122 */           return slot;
/*     */         }
/*     */       } 
/* 125 */     } catch (Exception exception) {}
/* 126 */     return null;
/*     */   }
/*     */   
/*     */   private boolean isShulkerBox(class_1799 stack) {
/* 130 */     if (stack == null || stack.method_7960()) return false; 
/* 131 */     return (stack.method_7909() == class_1802.field_8545 || stack
/* 132 */       .method_7909() == class_1802.field_8722 || stack
/* 133 */       .method_7909() == class_1802.field_8380 || stack
/* 134 */       .method_7909() == class_1802.field_8050 || stack
/* 135 */       .method_7909() == class_1802.field_8829 || stack
/* 136 */       .method_7909() == class_1802.field_8271 || stack
/* 137 */       .method_7909() == class_1802.field_8548 || stack
/* 138 */       .method_7909() == class_1802.field_8520 || stack
/* 139 */       .method_7909() == class_1802.field_8627 || stack
/* 140 */       .method_7909() == class_1802.field_8451 || stack
/* 141 */       .method_7909() == class_1802.field_8213 || stack
/* 142 */       .method_7909() == class_1802.field_8816 || stack
/* 143 */       .method_7909() == class_1802.field_8350 || stack
/* 144 */       .method_7909() == class_1802.field_8584 || stack
/* 145 */       .method_7909() == class_1802.field_8461 || stack
/* 146 */       .method_7909() == class_1802.field_8676 || stack
/* 147 */       .method_7909() == class_1802.field_8268);
/*     */   }
/*     */   
/*     */   private int getShulkerColor(class_1799 stack) {
/* 151 */     if (stack.method_7909() == class_1802.field_8545) return -6394435; 
/* 152 */     if (stack.method_7909() == class_1802.field_8722) return -1; 
/* 153 */     if (stack.method_7909() == class_1802.field_8380) return -425955; 
/* 154 */     if (stack.method_7909() == class_1802.field_8050) return -3715395; 
/* 155 */     if (stack.method_7909() == class_1802.field_8829) return -12930086; 
/* 156 */     if (stack.method_7909() == class_1802.field_8271) return -75715; 
/* 157 */     if (stack.method_7909() == class_1802.field_8548) return -8337633; 
/* 158 */     if (stack.method_7909() == class_1802.field_8520) return -816214; 
/* 159 */     if (stack.method_7909() == class_1802.field_8627) return -12103854; 
/* 160 */     if (stack.method_7909() == class_1802.field_8451) return -6447721; 
/* 161 */     if (stack.method_7909() == class_1802.field_8213) return -15295332; 
/* 162 */     if (stack.method_7909() == class_1802.field_8816) return -7785800; 
/* 163 */     if (stack.method_7909() == class_1802.field_8350) return -12827478; 
/* 164 */     if (stack.method_7909() == class_1802.field_8584) return -8170446; 
/* 165 */     if (stack.method_7909() == class_1802.field_8461) return -10585066; 
/* 166 */     if (stack.method_7909() == class_1802.field_8676) return -5231066; 
/* 167 */     if (stack.method_7909() == class_1802.field_8268) return -14869215; 
/* 168 */     return -6394435;
/*     */   }
/*     */   
/*     */   private void renderShulkerPreview(class_332 context, class_1799 shulkerItem, class_9288 container, float mouseX, float mouseY) {
/* 172 */     class_4587 matrices = context.method_51448();
/* 173 */     int screenWidth = context.method_51421();
/* 174 */     int screenHeight = context.method_51443();
/*     */     
/* 176 */     float contentWidth = 162.0F;
/* 177 */     float contentHeight = 54.0F;
/* 178 */     float totalWidth = contentWidth + 14.0F;
/* 179 */     float totalHeight = contentHeight + 14.0F + 14.0F;
/*     */     
/* 181 */     float x = mouseX + 12.0F;
/* 182 */     float y = mouseY - 12.0F;
/*     */     
/* 184 */     if (x + totalWidth > screenWidth) {
/* 185 */       x = mouseX - totalWidth - 4.0F;
/*     */     }
/* 187 */     if (y + totalHeight > screenHeight) {
/* 188 */       y = screenHeight - totalHeight - 4.0F;
/*     */     }
/* 190 */     if (y < 4.0F) {
/* 191 */       y = 4.0F;
/*     */     }
/* 193 */     if (x < 4.0F) {
/* 194 */       x = 4.0F;
/*     */     }
/*     */     
/* 197 */     int shulkerColor = getShulkerColor(shulkerItem);
/* 198 */     int bgColor = ColorUtils.applyAlpha(shulkerColor, 0.85F);
/* 199 */     int darkerColor = darkenColor(shulkerColor, 0.6F);
/* 200 */     int lighterColor = lightenColor(shulkerColor, 1.3F);
/*     */     
/* 202 */     matrices.method_22903();
/*     */     
/* 204 */     GL11.glClear(256);
/* 205 */     RenderSystem.disableDepthTest();
/* 206 */     RenderSystem.enableBlend();
/* 207 */     RenderSystem.defaultBlendFunc();
/*     */     
/* 209 */     matrices.method_46416(0.0F, 0.0F, 500.0F);
/*     */     
/* 211 */     RenderUtils.drawBlur(matrices, x - 2.0F, y - 2.0F, totalWidth + 4.0F, totalHeight + 4.0F, 7.0F, 8.0F, -1);
/*     */     
/* 213 */     context.method_25294((int)x, (int)y, (int)(x + totalWidth), (int)(y + totalHeight), bgColor);
/*     */     
/* 215 */     context.method_25294((int)x, (int)y, (int)(x + totalWidth), (int)(y + 2.0F), lighterColor);
/* 216 */     context.method_25294((int)x, (int)(y + totalHeight - 2.0F), (int)(x + totalWidth), (int)(y + totalHeight), darkerColor);
/* 217 */     context.method_25294((int)x, (int)y, (int)(x + 2.0F), (int)(y + totalHeight), lighterColor);
/* 218 */     context.method_25294((int)(x + totalWidth - 2.0F), (int)y, (int)(x + totalWidth), (int)(y + totalHeight), darkerColor);
/*     */     
/* 220 */     Font font = Fonts.getFont("sf_regular", 12);
/* 221 */     if (font != null) {
/* 222 */       String title = shulkerItem.method_7964().getString();
/* 223 */       float titleX = x + 7.0F;
/* 224 */       float titleY = y + 7.0F - 1.0F;
/*     */       
/* 226 */       int textColor = isColorDark(shulkerColor) ? -1 : -15066598;
/* 227 */       font.drawString(matrices, title, titleX, titleY, textColor);
/*     */     } 
/*     */     
/* 230 */     float slotsX = x + 7.0F;
/* 231 */     float slotsY = y + 7.0F + 14.0F - 2.0F;
/*     */     
/* 233 */     int slotAreaBg = darkenColor(shulkerColor, 0.5F);
/* 234 */     context.method_25294((int)(slotsX - 1.0F), (int)(slotsY - 1.0F), (int)(slotsX + contentWidth + 1.0F), (int)(slotsY + contentHeight + 1.0F), slotAreaBg);
/*     */ 
/*     */ 
/*     */     
/* 238 */     List<class_1799> items = new ArrayList<>();
/* 239 */     Objects.requireNonNull(items); container.method_57489().forEach(items::add);
/*     */     
/* 241 */     for (int i = 0; i < 27; i++) {
/* 242 */       int row = i / 9;
/* 243 */       int col = i % 9;
/*     */       
/* 245 */       int slotX = (int)(slotsX + (col * 18));
/* 246 */       int slotY = (int)(slotsY + (row * 18));
/*     */       
/* 248 */       context.method_25294(slotX, slotY, slotX + 18 - 2, slotY + 18 - 2, -7631989);
/*     */       
/* 250 */       context.method_25294(slotX, slotY, slotX + 18 - 2, slotY + 1, -11184811);
/* 251 */       context.method_25294(slotX, slotY, slotX + 1, slotY + 18 - 2, -11184811);
/* 252 */       context.method_25294(slotX, slotY + 18 - 3, slotX + 18 - 2, slotY + 18 - 2, -1);
/* 253 */       context.method_25294(slotX + 18 - 3, slotY, slotX + 18 - 2, slotY + 18 - 2, -1);
/*     */       
/* 255 */       if (i < items.size()) {
/* 256 */         class_1799 itemStack = items.get(i);
/* 257 */         if (!itemStack.method_7960()) {
/* 258 */           context.method_51427(itemStack, slotX, slotY);
/* 259 */           context.method_51431(mc.field_1772, itemStack, slotX, slotY);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 264 */     RenderSystem.enableDepthTest();
/* 265 */     RenderSystem.disableBlend();
/*     */     
/* 267 */     matrices.method_22909();
/*     */   }
/*     */   
/*     */   private int darkenColor(int color, float factor) {
/* 271 */     int a = color >> 24 & 0xFF;
/* 272 */     int r = (int)((color >> 16 & 0xFF) * factor);
/* 273 */     int g = (int)((color >> 8 & 0xFF) * factor);
/* 274 */     int b = (int)((color & 0xFF) * factor);
/* 275 */     return a << 24 | Math.min(255, r) << 16 | Math.min(255, g) << 8 | Math.min(255, b);
/*     */   }
/*     */   
/*     */   private int lightenColor(int color, float factor) {
/* 279 */     int a = color >> 24 & 0xFF;
/* 280 */     int r = (int)Math.min(255.0F, (color >> 16 & 0xFF) * factor);
/* 281 */     int g = (int)Math.min(255.0F, (color >> 8 & 0xFF) * factor);
/* 282 */     int b = (int)Math.min(255.0F, (color & 0xFF) * factor);
/* 283 */     return a << 24 | r << 16 | g << 8 | b;
/*     */   }
/*     */   
/*     */   private boolean isColorDark(int color) {
/* 287 */     int r = color >> 16 & 0xFF;
/* 288 */     int g = color >> 8 & 0xFF;
/* 289 */     int b = color & 0xFF;
/* 290 */     double luminance = (0.299D * r + 0.587D * g + 0.114D * b) / 255.0D;
/* 291 */     return (luminance < 0.5D);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\ShulkerPreview.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */