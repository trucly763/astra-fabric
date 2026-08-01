/*     */ package shame.astra.client.modules.impl.render;
/*     */ 
/*     */ import net.minecraft.class_243;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class HitMarkerData
/*     */ {
/*     */   class_243 position;
/*     */   long birthTime;
/*     */   long fadeInTime;
/*     */   long displayTime;
/*     */   long fadeOutTime;
/*     */   
/*     */   HitMarkerData(class_243 position, long birthTime, long fadeInTime, long displayTime, long fadeOutTime) {
/* 181 */     this.position = position;
/* 182 */     this.birthTime = birthTime;
/* 183 */     this.fadeInTime = fadeInTime;
/* 184 */     this.displayTime = displayTime;
/* 185 */     this.fadeOutTime = fadeOutTime;
/*     */   }
/*     */   
/*     */   boolean isDead() {
/* 189 */     return (System.currentTimeMillis() - this.birthTime >= this.fadeInTime + this.displayTime + this.fadeOutTime);
/*     */   }
/*     */   
/*     */   float getAlpha() {
/* 193 */     long elapsed = System.currentTimeMillis() - this.birthTime;
/*     */     
/* 195 */     if (elapsed < this.fadeInTime) {
/* 196 */       float f = (float)elapsed / (float)this.fadeInTime;
/* 197 */       return easeOutCubic(f);
/* 198 */     }  if (elapsed < this.fadeInTime + this.displayTime) {
/* 199 */       return 1.0F;
/*     */     }
/* 201 */     long fadeOutElapsed = elapsed - this.fadeInTime - this.displayTime;
/* 202 */     float progress = Math.min(1.0F, (float)fadeOutElapsed / (float)this.fadeOutTime);
/* 203 */     return 1.0F - easeInCubic(progress);
/*     */   }
/*     */ 
/*     */   
/*     */   float getScaleMultiplier() {
/* 208 */     long elapsed = System.currentTimeMillis() - this.birthTime;
/*     */     
/* 210 */     if (elapsed < this.fadeInTime) {
/* 211 */       float f = (float)elapsed / (float)this.fadeInTime;
/* 212 */       return 0.5F + 0.5F * easeOutBack(f);
/* 213 */     }  if (elapsed < this.fadeInTime + this.displayTime) {
/* 214 */       return 1.0F;
/*     */     }
/* 216 */     long fadeOutElapsed = elapsed - this.fadeInTime - this.displayTime;
/* 217 */     float progress = Math.min(1.0F, (float)fadeOutElapsed / (float)this.fadeOutTime);
/* 218 */     return 1.0F - 0.3F * easeInCubic(progress);
/*     */   }
/*     */ 
/*     */   
/*     */   private float easeOutCubic(float x) {
/* 223 */     return 1.0F - (float)Math.pow(1.0D - x, 3.0D);
/*     */   }
/*     */   
/*     */   private float easeInCubic(float x) {
/* 227 */     return x * x * x;
/*     */   }
/*     */   
/*     */   private float easeOutBack(float x) {
/* 231 */     float c1 = 1.70158F;
/* 232 */     float c3 = c1 + 1.0F;
/* 233 */     return 1.0F + c3 * (float)Math.pow(x - 1.0D, 3.0D) + c1 * (float)Math.pow(x - 1.0D, 2.0D);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\HitMarker$HitMarkerData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */