/*     */ package shame.astra.api.utils.scissor;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.awt.Rectangle;
/*     */ import java.util.List;
/*     */ import net.minecraft.class_1041;
/*     */ import net.minecraft.class_310;
/*     */ import org.lwjgl.opengl.GL30;
/*     */ 
/*     */ public class ScissorUtils
/*     */ {
/*     */   private static class State
/*     */     implements Cloneable {
/*     */     public boolean enabled;
/*     */     public int transX;
/*     */     public int transY;
/*     */     public int x;
/*     */     public int y;
/*     */     public int width;
/*     */     public int height;
/*     */     
/*     */     public State clone() {
/*     */       try {
/*  24 */         return (State)super.clone();
/*  25 */       } catch (CloneNotSupportedException e) {
/*  26 */         throw new AssertionError(e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*  31 */   private static State state = new State();
/*     */   
/*  33 */   private static final List<State> stateStack = Lists.newArrayList();
/*     */   
/*     */   public static void push() {
/*  36 */     stateStack.add(state.clone());
/*     */   }
/*     */   
/*     */   public static void pop() {
/*  40 */     if (stateStack.isEmpty()) {
/*     */       return;
/*     */     }
/*  43 */     state = stateStack.remove(stateStack.size() - 1);
/*  44 */     if (state.enabled) {
/*  45 */       GL30.glEnable(3089);
/*  46 */       GL30.glScissor(state.x, state.y, state.width, state.height);
/*     */     } else {
/*  48 */       GL30.glDisable(3089);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void unset() {
/*  53 */     GL30.glDisable(3089);
/*  54 */     state.enabled = false;
/*     */   }
/*     */   
/*     */   private static class_1041 getWindow() {
/*  58 */     class_310 client = class_310.method_1551();
/*  59 */     return (client == null) ? null : client.method_22683();
/*     */   }
/*     */   
/*     */   private static double getScaleFactor() {
/*  63 */     class_1041 window = getWindow();
/*  64 */     return (window == null) ? 1.0D : window.method_4495();
/*     */   }
/*     */   
/*     */   public static void setFromComponentCoordinates(int x, int y, int width, int height) {
/*  68 */     class_1041 window = getWindow();
/*  69 */     if (window == null) {
/*     */       return;
/*     */     }
/*  72 */     double scaleFactor = getScaleFactor();
/*     */     
/*  74 */     int screenX = (int)(x * scaleFactor);
/*  75 */     int screenY = (int)(y * scaleFactor);
/*  76 */     int screenWidth = (int)(width * scaleFactor);
/*  77 */     int screenHeight = (int)(height * scaleFactor);
/*  78 */     screenY = window.method_4507() - screenY - screenHeight;
/*  79 */     set(screenX, screenY, screenWidth, screenHeight);
/*     */   }
/*     */   
/*     */   public static void setFromComponentCoordinates(double x, double y, double width, double height) {
/*  83 */     class_1041 window = getWindow();
/*  84 */     if (window == null) {
/*     */       return;
/*     */     }
/*  87 */     double scaleFactor = getScaleFactor();
/*     */     
/*  89 */     int screenX = (int)(x * scaleFactor);
/*  90 */     int screenY = (int)(y * scaleFactor);
/*  91 */     int screenWidth = (int)(width * scaleFactor);
/*  92 */     int screenHeight = (int)(height * scaleFactor);
/*  93 */     screenY = window.method_4507() - screenY - screenHeight;
/*  94 */     set(screenX, screenY, screenWidth, screenHeight);
/*     */   }
/*     */   
/*     */   public static void setFromComponentCoordinates(double x, double y, double width, double height, float scale) {
/*  98 */     class_1041 window = getWindow();
/*  99 */     if (window == null) {
/*     */       return;
/*     */     }
/* 102 */     double scaleFactor = getScaleFactor();
/*     */     
/* 104 */     float animationValue = scale;
/*     */     
/* 106 */     float halfAnimationValueRest = (1.0F - animationValue) / 2.0F;
/* 107 */     double testX = x + width * halfAnimationValueRest;
/* 108 */     double testY = y + height * halfAnimationValueRest;
/* 109 */     double testW = width * animationValue;
/* 110 */     double testH = height * animationValue;
/*     */     
/* 112 */     testX = testX * animationValue + (window.method_4486() - testW) * halfAnimationValueRest;
/*     */     
/* 114 */     int screenX = (int)(testX * scaleFactor);
/* 115 */     int screenY = (int)(testY * scaleFactor);
/* 116 */     int screenWidth = (int)(testW * scaleFactor);
/* 117 */     int screenHeight = (int)(testH * scaleFactor);
/* 118 */     screenY = window.method_4507() - screenY - screenHeight;
/* 119 */     set(screenX, screenY, screenWidth, screenHeight);
/*     */   }
/*     */   public static void set(int x, int y, int width, int height) {
/*     */     Rectangle current;
/* 123 */     class_1041 window = getWindow();
/* 124 */     if (window == null) {
/*     */       return;
/*     */     }
/* 127 */     Rectangle screen = new Rectangle(0, 0, window.method_4480(), window.method_4507());
/*     */     
/* 129 */     if (state.enabled) {
/* 130 */       current = new Rectangle(state.x, state.y, state.width, state.height);
/*     */     } else {
/* 132 */       current = screen;
/*     */     } 
/* 134 */     Rectangle target = new Rectangle(x + state.transX, y + state.transY, width, height);
/* 135 */     Rectangle result = current.intersection(target);
/* 136 */     result = result.intersection(screen);
/* 137 */     if (result.width < 0)
/* 138 */       result.width = 0; 
/* 139 */     if (result.height < 0)
/* 140 */       result.height = 0; 
/* 141 */     state.enabled = true;
/* 142 */     state.x = result.x;
/* 143 */     state.y = result.y;
/* 144 */     state.width = result.width;
/* 145 */     state.height = result.height;
/* 146 */     GL30.glEnable(3089);
/* 147 */     GL30.glScissor(result.x, result.y, result.width, result.height);
/*     */   }
/*     */   
/*     */   public static void translate(int x, int y) {
/* 151 */     state.transX = x;
/* 152 */     state.transY = y;
/*     */   }
/*     */   
/*     */   public static void translateFromComponentCoordinates(int x, int y) {
/* 156 */     class_1041 window = getWindow();
/* 157 */     if (window == null) {
/*     */       return;
/*     */     }
/* 160 */     int totalHeight = window.method_4502();
/* 161 */     double scaleFactor = getScaleFactor();
/*     */     
/* 163 */     int screenX = (int)(x * scaleFactor);
/* 164 */     int screenY = (int)(y * scaleFactor);
/* 165 */     screenY = (int)(totalHeight * scaleFactor) - screenY;
/* 166 */     translate(screenX, screenY);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\scissor\ScissorUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */