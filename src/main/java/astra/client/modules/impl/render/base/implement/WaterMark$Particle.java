/*    */ package shame.astra.client.modules.impl.render.base.implement;
/*    */ 
/*    */ import java.util.Random;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class Particle
/*    */ {
/*    */   float x;
/*    */   float y;
/*    */   float velX;
/*    */   float velY;
/*    */   float life;
/*    */   float maxLife;
/*    */   float size;
/*    */   
/*    */   Particle(float x, float y) {
/* 41 */     this.x = x;
/* 42 */     this.y = y;
/* 43 */     Random r = new Random();
/* 44 */     this.velX = (r.nextFloat() - 0.5F) * 0.5F;
/* 45 */     this.velY = -r.nextFloat() * 1.5F - 0.5F;
/* 46 */     this.maxLife = r.nextFloat() * 2.0F + 1.0F;
/* 47 */     this.life = this.maxLife;
/* 48 */     this.size = r.nextFloat() * 1.5F + 0.5F;
/*    */   }
/*    */   
/*    */   void update(float deltaTime) {
/* 52 */     this.x += this.velX * deltaTime * 10.0F;
/* 53 */     this.y += this.velY * deltaTime * 10.0F;
/* 54 */     this.life -= deltaTime;
/*    */   }
/*    */   
/*    */   boolean isDead() {
/* 58 */     return (this.life <= 0.0F);
/*    */   }
/*    */   
/*    */   float getAlpha() {
/* 62 */     return Math.min(1.0F, this.life / this.maxLife);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\base\implement\WaterMark$Particle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */