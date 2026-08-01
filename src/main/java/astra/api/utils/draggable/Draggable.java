/*     */ package shame.astra.api.utils.draggable;
/*     */ 
/*     */ import com.google.gson.annotations.Expose;
/*     */ import com.google.gson.annotations.SerializedName;
/*     */ import lombok.Generated;
/*     */ import net.minecraft.class_1041;
/*     */ import net.minecraft.class_310;
/*     */ import net.minecraft.class_4587;
/*     */ import net.minecraft.class_7833;
/*     */ import shame.astra.api.utils.math.HoveringUtils;
/*     */ import shame.astra.api.utils.math.MathUtils;
/*     */ import shame.astra.api.utils.render.RenderUtils;
/*     */ import shame.astra.client.modules.Module;
/*     */ 
/*     */ public class Draggable implements QClient {
/*     */   @Expose
/*     */   @SerializedName("x")
/*     */   private float xPos;
/*     */   @Expose
/*     */   @SerializedName("y")
/*     */   private float yPos;
/*     */   public float initialXVal;
/*     */   public float initialYVal;
/*     */   private float startX;
/*     */   private float startY;
/*     */   private boolean dragging;
/*     */   
/*     */   @Generated
/*  29 */   public void setWidth(float width) { this.width = width; } private float width; private float height; @Expose @SerializedName("name") private String name; private final Module module; private float targetXPos; private float targetYPos; private static final float CENTER_LINE_WIDTH = 1.0F; private static final float SNAP_THRESHOLD = 10.0F; @Generated
/*  30 */   public float getWidth() { return this.width; }
/*     */   @Generated
/*  32 */   public void setHeight(float height) { this.height = height; } @Generated
/*  33 */   public float getHeight() { return this.height; } @Generated
/*     */   public String getName() {
/*  35 */     return this.name;
/*     */   }
/*     */   @Generated
/*     */   public Module getModule() {
/*  39 */     return this.module;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  48 */   private float lineAlpha = 0.0F; private long lastUpdateTime; private boolean snapToCenter; private boolean snapToCenterx; private boolean snapToCenter2x; private boolean snapToCenter3x; private boolean snapToCenter4x;
/*     */   private boolean snapToCenter5x;
/*     */   private boolean snapToCenter2;
/*     */   private boolean snapToCenter3;
/*     */   private boolean snapToCenter4;
/*     */   private boolean snapToCenter5;
/*     */   private static final float LERP_SPEED = 0.19F;
/*     */   private static final float MAX_TILT_DEGREES = 25.0F;
/*     */   private static final float TILT_FROM_MOUSE_DELTA = 4.0F;
/*     */   private static final float DRAG_TILT_LERP = 0.14F;
/*     */   private static final float RELEASE_TILT_LERP = 0.1F;
/*     */   private static final float TILT_DELTA_SMOOTHING = 0.18F;
/*     */   private static final float TILT_TARGET_SMOOTHING = 0.22F;
/*     */   private static final float TILT_DEADZONE = 0.18F;
/*     */   private static final float DRAG_SCALE_MULTIPLIER = 1.01F;
/*     */   private static final float DRAG_SCALE_LERP = 0.1F;
/*     */   private static final float RELEASE_SCALE_LERP = 0.02F;
/*     */   private float dragTiltDegrees;
/*     */   private float targetTiltDegrees;
/*     */   private float smoothedMouseDeltaX;
/*     */   private float lastDragMouseX;
/*     */   private boolean hasLastDragMouseX;
/*     */   private boolean tiltMatrixPushed;
/*  71 */   private float dragScale = 1.0F;
/*  72 */   private float targetScale = 1.0F;
/*     */   
/*     */   public Draggable(Module module, String name, float initialXVal, float initialYVal) {
/*  75 */     this.module = module;
/*  76 */     this.name = name;
/*  77 */     this.xPos = initialXVal;
/*  78 */     this.yPos = initialYVal;
/*  79 */     this.initialXVal = initialXVal;
/*  80 */     this.initialYVal = initialYVal;
/*     */   }
/*     */   
/*     */   public float getX() {
/*  84 */     return this.xPos;
/*     */   }
/*     */   
/*     */   public void setX(float x) {
/*  88 */     this.xPos = x;
/*     */   }
/*     */   
/*     */   public float getY() {
/*  92 */     return this.yPos;
/*     */   }
/*     */   
/*     */   public void setY(float y) {
/*  96 */     this.yPos = y;
/*     */   }
/*     */   
/*     */   private Vec2i getMouse(int mouseX, int mouseY) {
/* 100 */     class_310 client = class_310.method_1551();
/* 101 */     class_1041 window = (client == null) ? null : client.method_22683();
/* 102 */     double scaleFactor = (window == null) ? 1.0D : window.method_4495();
/* 103 */     return new Vec2i((int)(mouseX * scaleFactor / 2.0D), (int)(mouseY * scaleFactor / 2.0D));
/*     */   }
/*     */   
/*     */   public final void onDraw(int mouseX, int mouseY, class_1041 res, class_4587 ms) {
/* 107 */     Vec2i fixed = getMouse(mouseX, mouseY);
/* 108 */     mouseX = fixed.getX();
/* 109 */     mouseY = fixed.getY();
/*     */     
/* 111 */     float centerX = res.method_4486() / 2.0F;
/* 112 */     float centerY = res.method_4502() / 2.0F;
/*     */     
/* 114 */     float centerX2 = res.method_4486() / 4.0F;
/* 115 */     float centerY2 = res.method_4502() / 4.0F;
/* 116 */     float centerX3 = res.method_4486() / 8.0F;
/* 117 */     float centerY3 = res.method_4502() / 8.0F;
/*     */     
/* 119 */     float centerX4 = res.method_4486() / 1.15F;
/* 120 */     float centerY4 = res.method_4502() / 1.15F;
/* 121 */     float centerX5 = res.method_4486() / 1.35F;
/* 122 */     float centerY5 = res.method_4502() / 1.35F;
/*     */     
/* 124 */     this.snapToCenter = this.snapToCenterx = this.snapToCenter2x = this.snapToCenter3x = this.snapToCenter4x = this.snapToCenter5x = this.snapToCenter2 = this.snapToCenter3 = this.snapToCenter4 = this.snapToCenter5 = false;
/*     */     
/* 126 */     if (this.dragging) {
/* 127 */       this.targetScale = 1.01F;
/* 128 */       if (this.hasLastDragMouseX) {
/* 129 */         float mouseDeltaX = mouseX - this.lastDragMouseX;
/* 130 */         if (Math.abs(mouseDeltaX) < 0.18F) {
/* 131 */           mouseDeltaX = 0.0F;
/*     */         }
/*     */         
/* 134 */         this.smoothedMouseDeltaX = MathUtils.lerp(this.smoothedMouseDeltaX, mouseDeltaX, 0.18F);
/* 135 */         float desiredTilt = Math.max(-25.0F, Math.min(25.0F, this.smoothedMouseDeltaX * 4.0F));
/* 136 */         this.targetTiltDegrees = MathUtils.lerp(this.targetTiltDegrees, desiredTilt, 0.22F);
/*     */       } 
/* 138 */       this.lastDragMouseX = mouseX;
/* 139 */       this.hasLastDragMouseX = true;
/*     */       
/* 141 */       this.targetXPos = mouseX - this.startX;
/* 142 */       this.targetYPos = mouseY - this.startY;
/*     */       
/* 144 */       boolean snapped = false;
/*     */       
/* 146 */       if (Math.abs(this.targetXPos + this.width / 2.0F - centerX) < 10.0F) {
/* 147 */         this.targetXPos = centerX - this.width / 2.0F;
/* 148 */         this.snapToCenterx = true;
/* 149 */         snapped = true;
/*     */       } 
/*     */       
/* 152 */       if (Math.abs(this.targetYPos + this.height / 2.0F - centerY) < 10.0F) {
/* 153 */         this.targetYPos = centerY - this.height / 2.0F;
/* 154 */         this.snapToCenter = true;
/* 155 */         snapped = true;
/*     */       } 
/*     */       
/* 158 */       if (Math.abs(this.targetXPos + this.width / 2.0F - centerX2) < 10.0F) {
/* 159 */         this.targetXPos = centerX2 - this.width / 2.0F;
/* 160 */         this.snapToCenter2x = true;
/* 161 */         snapped = true;
/*     */       } 
/*     */       
/* 164 */       if (Math.abs(this.targetYPos + this.height / 2.0F - centerY2) < 10.0F) {
/* 165 */         this.targetYPos = centerY2 - this.height / 2.0F;
/* 166 */         this.snapToCenter2 = true;
/* 167 */         snapped = true;
/*     */       } 
/*     */       
/* 170 */       if (Math.abs(this.targetXPos + this.width / 2.0F - centerX3) < 10.0F) {
/* 171 */         this.targetXPos = centerX3 - this.width / 2.0F;
/* 172 */         this.snapToCenter3x = true;
/* 173 */         snapped = true;
/*     */       } 
/*     */       
/* 176 */       if (Math.abs(this.targetYPos + this.height / 2.0F - centerY3) < 10.0F) {
/* 177 */         this.targetYPos = centerY3 - this.height / 2.0F;
/* 178 */         this.snapToCenter3 = true;
/* 179 */         snapped = true;
/*     */       } 
/*     */       
/* 182 */       if (Math.abs(this.targetXPos + this.width / 2.0F - centerX4) < 10.0F) {
/* 183 */         this.targetXPos = centerX4 - this.width / 2.0F;
/* 184 */         this.snapToCenter4x = true;
/* 185 */         snapped = true;
/*     */       } 
/*     */       
/* 188 */       if (Math.abs(this.targetYPos + this.height / 2.0F - centerY4) < 10.0F) {
/* 189 */         this.targetYPos = centerY4 - this.height / 2.0F;
/* 190 */         this.snapToCenter4 = true;
/* 191 */         snapped = true;
/*     */       } 
/*     */       
/* 194 */       if (Math.abs(this.targetXPos + this.width / 2.0F - centerX5) < 10.0F) {
/* 195 */         this.targetXPos = centerX5 - this.width / 2.0F;
/* 196 */         this.snapToCenter5x = true;
/* 197 */         snapped = true;
/*     */       } 
/*     */       
/* 200 */       if (Math.abs(this.targetYPos + this.height / 2.0F - centerY5) < 10.0F) {
/* 201 */         this.targetYPos = centerY5 - this.height / 2.0F;
/* 202 */         this.snapToCenter5 = true;
/* 203 */         snapped = true;
/*     */       } 
/*     */       
/* 206 */       if (this.targetXPos + this.width > res.method_4486()) {
/* 207 */         this.targetXPos = res.method_4486() - this.width;
/*     */       }
/* 209 */       if (this.targetYPos + this.height > res.method_4502()) {
/* 210 */         this.targetYPos = res.method_4502() - this.height;
/*     */       }
/* 212 */       if (this.targetXPos < 0.0F) {
/* 213 */         this.targetXPos = 0.0F;
/*     */       }
/* 215 */       if (this.targetYPos < 0.0F) {
/* 216 */         this.targetYPos = 0.0F;
/*     */       }
/*     */       
/* 219 */       this.xPos = MathUtils.lerp(this.xPos, this.targetXPos, 0.19F);
/* 220 */       this.yPos = MathUtils.lerp(this.yPos, this.targetYPos, 0.19F);
/*     */       
/* 222 */       updateLineAlpha(snapped);
/*     */     } else {
/* 224 */       this.targetScale = 1.0F;
/* 225 */       this.targetTiltDegrees = 0.0F;
/* 226 */       this.smoothedMouseDeltaX = MathUtils.lerp(this.smoothedMouseDeltaX, 0.0F, 0.18F);
/* 227 */       this.hasLastDragMouseX = false;
/* 228 */       updateLineAlpha(false);
/*     */     } 
/* 230 */     updateTilt();
/*     */     
/* 232 */     drawCenterLines(ms, res);
/*     */   }
/*     */   
/*     */   private void updateTilt() {
/* 236 */     float lerp = this.dragging ? 0.14F : 0.1F;
/* 237 */     this.dragTiltDegrees = MathUtils.lerp(this.dragTiltDegrees, this.targetTiltDegrees, lerp);
/* 238 */     if (!this.dragging && Math.abs(this.dragTiltDegrees) < 0.02F) {
/* 239 */       this.dragTiltDegrees = 0.0F;
/*     */     }
/*     */     
/* 242 */     float scaleLerp = this.dragging ? 0.1F : 0.02F;
/* 243 */     this.dragScale = MathUtils.lerp(this.dragScale, this.targetScale, scaleLerp);
/* 244 */     if (!this.dragging && Math.abs(this.dragScale - 1.0F) < 0.002F) {
/* 245 */       this.dragScale = 1.0F;
/*     */     }
/*     */   }
/*     */   
/*     */   public void beginRenderTilt(class_4587 ms) {
/* 250 */     updateTilt();
/* 251 */     this.tiltMatrixPushed = false;
/* 252 */     if (Math.abs(this.dragTiltDegrees) < 0.05F && Math.abs(this.dragScale - 1.0F) < 0.002F) {
/*     */       return;
/*     */     }
/*     */     
/* 256 */     float centerX = this.xPos + this.width / 2.0F;
/* 257 */     float centerY = this.yPos + this.height / 2.0F;
/*     */     
/* 259 */     ms.method_22903();
/* 260 */     ms.method_46416(centerX, centerY, 0.0F);
/* 261 */     ms.method_22907(class_7833.field_40718.rotationDegrees(this.dragTiltDegrees));
/* 262 */     ms.method_22905(this.dragScale, this.dragScale, 1.0F);
/* 263 */     ms.method_46416(-centerX, -centerY, 0.0F);
/* 264 */     this.tiltMatrixPushed = true;
/*     */   }
/*     */   
/*     */   public void endRenderTilt(class_4587 ms) {
/* 268 */     if (this.tiltMatrixPushed) {
/* 269 */       ms.method_22909();
/* 270 */       this.tiltMatrixPushed = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateLineAlpha(boolean active) {
/* 275 */     long currentTime = System.currentTimeMillis();
/* 276 */     float deltaTime = (float)(currentTime - this.lastUpdateTime) / 1000.0F;
/* 277 */     this.lastUpdateTime = currentTime;
/*     */     
/* 279 */     float fadeSpeed = 2.0F;
/* 280 */     float fadeOutSpeed = 2.0F;
/*     */     
/* 282 */     if (active) {
/* 283 */       this.lineAlpha += deltaTime * fadeSpeed;
/* 284 */       if (this.lineAlpha > 1.0F) {
/* 285 */         this.lineAlpha = 1.0F;
/*     */       }
/*     */     } else {
/* 288 */       this.lineAlpha -= deltaTime * fadeOutSpeed;
/* 289 */       if (this.lineAlpha < 0.0F) {
/* 290 */         this.lineAlpha = 0.0F;
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void drawCenterLines(class_4587 ms, class_1041 res) {
/* 296 */     if (this.lineAlpha > 0.0F) {
/* 297 */       float centerX = res.method_4486() / 2.0F;
/* 298 */       float centerY = res.method_4502() / 2.0F;
/* 299 */       float centerX2 = res.method_4486() / 4.0F;
/* 300 */       float centerY2 = res.method_4502() / 4.0F;
/* 301 */       float centerX3 = res.method_4486() / 8.0F;
/* 302 */       float centerY3 = res.method_4502() / 8.0F;
/* 303 */       float centerX4 = res.method_4486() / 1.15F;
/* 304 */       float centerY4 = res.method_4502() / 1.15F;
/* 305 */       float centerX5 = res.method_4486() / 1.35F;
/* 306 */       float centerY5 = res.method_4502() / 1.35F;
/*     */       
/* 308 */       int color = (int)(this.lineAlpha * 255.0F) << 24 | 0xFFFFFF;
/* 309 */       if (this.snapToCenterx) {
/* 310 */         RenderUtils.drawRoundedRect(ms, centerX - 0.33333334F, 0.0F, 1.0F, res.method_4502(), 1.0F, color);
/*     */       }
/* 312 */       if (this.snapToCenter) {
/* 313 */         RenderUtils.drawRoundedRect(ms, 0.0F, centerY - 0.33333334F, res.method_4486(), 1.0F, 1.0F, color);
/*     */       }
/* 315 */       if (this.snapToCenter2x) {
/* 316 */         RenderUtils.drawRoundedRect(ms, centerX2 - 0.33333334F, 0.0F, 1.0F, res.method_4502(), 1.0F, color);
/*     */       }
/* 318 */       if (this.snapToCenter2) {
/* 319 */         RenderUtils.drawRoundedRect(ms, 0.0F, centerY2 - 0.33333334F, res.method_4486(), 1.0F, 1.0F, color);
/*     */       }
/* 321 */       if (this.snapToCenter3x) {
/* 322 */         RenderUtils.drawRoundedRect(ms, centerX3 - 0.33333334F, 0.0F, 1.0F, res.method_4502(), 1.0F, color);
/*     */       }
/* 324 */       if (this.snapToCenter3) {
/* 325 */         RenderUtils.drawRoundedRect(ms, 0.0F, centerY3 - 0.33333334F, res.method_4486(), 1.0F, 1.0F, color);
/*     */       }
/* 327 */       if (this.snapToCenter4x) {
/* 328 */         RenderUtils.drawRoundedRect(ms, centerX4 - 0.33333334F, 0.0F, 1.0F, res.method_4502(), 1.0F, color);
/*     */       }
/* 330 */       if (this.snapToCenter4) {
/* 331 */         RenderUtils.drawRoundedRect(ms, 0.0F, centerY4 - 0.33333334F, res.method_4486(), 1.0F, 1.0F, color);
/*     */       }
/* 333 */       if (this.snapToCenter5x) {
/* 334 */         RenderUtils.drawRoundedRect(ms, centerX5 - 0.33333334F, 0.0F, 1.0F, res.method_4502(), 1.0F, color);
/*     */       }
/* 336 */       if (this.snapToCenter5)
/* 337 */         RenderUtils.drawRoundedRect(ms, 0.0F, centerY5 - 0.33333334F, res.method_4486(), 1.0F, 1.0F, color); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public final boolean onClick(double mouseX, double mouseY, int button) {
/* 342 */     if (button == 0 && HoveringUtils.isInRegion(mouseX, mouseY, this.xPos, this.yPos, this.width, this.height)) {
/* 343 */       this.dragging = true;
/* 344 */       this.targetScale = 1.01F;
/* 345 */       this.startX = (int)(mouseX - this.xPos);
/* 346 */       this.startY = (int)(mouseY - this.yPos);
/* 347 */       this.smoothedMouseDeltaX = 0.0F;
/* 348 */       this.hasLastDragMouseX = false;
/* 349 */       this.lastUpdateTime = System.currentTimeMillis();
/* 350 */       return true;
/*     */     } 
/* 352 */     return false;
/*     */   }
/*     */   
/*     */   public final void onRelease(int button) {
/* 356 */     if (button == 0) {
/* 357 */       this.dragging = false;
/* 358 */       this.targetScale = 1.0F;
/* 359 */       this.targetTiltDegrees = 0.0F;
/* 360 */       this.smoothedMouseDeltaX = 0.0F;
/* 361 */       this.hasLastDragMouseX = false;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\draggable\Draggable.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */