/*     */ package shame.astra.client.modules.impl.render;
/*     */ 
/*     */ import net.minecraft.class_1922;
/*     */ import net.minecraft.class_2338;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_2680;
/*     */ import net.minecraft.class_310;
/*     */ import net.minecraft.class_3532;
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
/*     */ class ParticleData
/*     */ {
/*     */   class_243 position;
/*     */   class_243 velocity;
/*     */   int color;
/*     */   float size;
/*     */   long lifeTime;
/*     */   long birthTime;
/* 384 */   float alpha = 1.0F;
/*     */   float smoothFactor;
/*     */   long lastUpdateNs;
/*     */   double gravity;
/*     */   
/*     */   ParticleData(class_243 position, class_243 velocity, int color, float size, long lifeTime, float smooth, double gravity) {
/* 390 */     this.position = position;
/* 391 */     this.velocity = velocity;
/* 392 */     this.color = color;
/* 393 */     this.size = size;
/* 394 */     this.lifeTime = lifeTime;
/* 395 */     this.birthTime = System.currentTimeMillis();
/* 396 */     this.lastUpdateNs = System.nanoTime();
/* 397 */     this.smoothFactor = smooth;
/* 398 */     this.gravity = gravity;
/*     */   }
/*     */   
/*     */   boolean isDead() {
/* 402 */     return (System.currentTimeMillis() - this.birthTime >= this.lifeTime);
/*     */   }
/*     */   
/*     */   void update(class_310 mc) {
/* 406 */     long nowNs = System.nanoTime();
/* 407 */     double deltaSec = (nowNs - this.lastUpdateNs) / 1.0E9D;
/* 408 */     this.lastUpdateNs = nowNs;
/*     */     
/* 410 */     float progress = Math.min(1.0F, (float)(System.currentTimeMillis() - this.birthTime) / (float)this.lifeTime);
/* 411 */     double factor = Math.pow(1.0D - progress, this.smoothFactor);
/*     */     
/* 413 */     double vx = this.velocity.field_1352;
/* 414 */     double vy = this.velocity.field_1351;
/* 415 */     double vz = this.velocity.field_1350;
/*     */     
/* 417 */     double newX = this.position.field_1352;
/* 418 */     double newY = this.position.field_1351;
/* 419 */     double newZ = this.position.field_1350;
/*     */     
/* 421 */     newX += vx * factor * deltaSec * 60.0D;
/* 422 */     if (!checkCollision(newX, this.position.field_1351, this.position.field_1350, this.size, mc)) {
/* 423 */       vx = -vx * 0.8D;
/* 424 */       newX = this.position.field_1352;
/*     */     } 
/*     */     
/* 427 */     newY += vy * factor * deltaSec * 60.0D;
/* 428 */     if (!checkCollision(newX, newY, this.position.field_1350, this.size, mc)) {
/* 429 */       vy = -vy * 1.5D;
/* 430 */       newY = this.position.field_1351;
/*     */     } 
/*     */     
/* 433 */     newZ += vz * factor * deltaSec * 60.0D;
/* 434 */     if (!checkCollision(newX, newY, newZ, this.size, mc)) {
/* 435 */       vz = -vz * 0.8D;
/* 436 */       newZ = this.position.field_1350;
/*     */     } 
/*     */     
/* 439 */     this.position = new class_243(newX, newY, newZ);
/* 440 */     this.velocity = new class_243(vx * 0.9999D, vy * 0.9999D - this.gravity, vz * 0.9999D);
/* 441 */     this.alpha = 1.0F - progress;
/*     */   }
/*     */   
/*     */   static boolean checkCollision(double x, double y, double z, float size, class_310 mc) {
/* 445 */     if (mc.field_1687 == null) return false; 
/* 446 */     double half = size * 0.5D;
/* 447 */     int minX = class_3532.method_15357(x - half);
/* 448 */     int maxX = class_3532.method_15357(x + half);
/* 449 */     int minY = class_3532.method_15357(y - half);
/* 450 */     int maxY = class_3532.method_15357(y + half);
/* 451 */     int minZ = class_3532.method_15357(z - half);
/* 452 */     int maxZ = class_3532.method_15357(z + half);
/*     */     
/* 454 */     class_2338.class_2339 pos = new class_2338.class_2339();
/* 455 */     for (int bx = minX; bx <= maxX; bx++) {
/* 456 */       for (int by = minY; by <= maxY; by++) {
/* 457 */         for (int bz = minZ; bz <= maxZ; bz++) {
/* 458 */           pos.method_10103(bx, by, bz);
/* 459 */           class_2680 state = mc.field_1687.method_8320((class_2338)pos);
/* 460 */           if (!state.method_26215() && state.method_26212((class_1922)mc.field_1687, (class_2338)pos)) {
/* 461 */             return false;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 466 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\Particle$ParticleData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */