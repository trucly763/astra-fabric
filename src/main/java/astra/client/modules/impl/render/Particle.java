/*     */ package shame.astra.client.modules.impl.render;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.class_1297;
/*     */ import net.minecraft.class_1684;
/*     */ import net.minecraft.class_1685;
/*     */ import net.minecraft.class_1922;
/*     */ import net.minecraft.class_2338;
/*     */ import net.minecraft.class_2374;
/*     */ import net.minecraft.class_238;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_2596;
/*     */ import net.minecraft.class_2663;
/*     */ import net.minecraft.class_2680;
/*     */ import net.minecraft.class_286;
/*     */ import net.minecraft.class_287;
/*     */ import net.minecraft.class_289;
/*     */ import net.minecraft.class_2960;
/*     */ import net.minecraft.class_310;
/*     */ import net.minecraft.class_3532;
/*     */ import net.minecraft.class_3959;
/*     */ import net.minecraft.class_3965;
/*     */ import net.minecraft.class_4587;
/*     */ import net.minecraft.class_7833;
/*     */ import org.joml.Matrix4f;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.Event3DRender;
/*     */ import shame.astra.api.events.implement.EventAttackEntity;
/*     */ import shame.astra.api.events.implement.EventPacket;
/*     */ import shame.astra.api.utils.color.ColorUtils;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*     */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*     */ import shame.astra.client.modules.settings.implement.ListSetting;
/*     */ import shame.astra.client.modules.settings.implement.ModeSetting;
/*     */ 
/*     */ public class Particle extends Module {
/*  41 */   public static Particle INSTANCE = new Particle();
/*  42 */   private static final class_2960 STAR_TEXTURE = class_2960.method_60655("astra", "textures/particle/star.png");
/*  43 */   private static final class_2960 HEART_TEXTURE = class_2960.method_60655("astra", "textures/particle/heart.png");
/*  44 */   private static final class_2960 DOLLAR_TEXTURE = class_2960.method_60655("astra", "textures/particle/dollar.png");
/*  45 */   private static final class_2960 BLOOM_TEXTURE = class_2960.method_60655("astra", "textures/particle/bloom.png");
/*  46 */   private static final class_2960 SPARKLE_TEXTURE = class_2960.method_60655("astra", "textures/particle/sparkle.png");
/*     */   
/*  48 */   private final ModeSetting type = new ModeSetting("Тип частиц", "Звездочки", new String[] { "Звездочки", "Сердечки", "Доллары", "Блум", "Сияние" });
/*     */ 
/*     */   
/*  51 */   private final ListSetting reason = new ListSetting("Добавлять при", new BooleanSetting[] { new BooleanSetting("Бездействии", false), new BooleanSetting("Беге", false), new BooleanSetting("Ударе", true), new BooleanSetting("Падении перла", false), new BooleanSetting("Падении трезубца", false), new BooleanSetting("Сносе тотема", true) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   private final FloatSetting count = new FloatSetting("Количество", 10.0F, 2.0F, 40.0F, 1.0F);
/*     */   
/*  61 */   private final BooleanSetting glow = new BooleanSetting("Свечение", true);
/*     */   
/*  63 */   private final ArrayList<ParticleData> particles = new ArrayList<>();
/*  64 */   private final Random rnd = new Random();
/*     */   
/*     */   public Particle() {
/*  67 */     super("Particles", "Красивые партиклы при разных действиях", Module.ModuleCategory.RENDER);
/*  68 */     addSettings(new Setting[] { (Setting)this.type, (Setting)this.reason, (Setting)this.count, (Setting)this.glow });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  73 */     this.particles.clear();
/*  74 */     super.onDisable();
/*     */   }
/*     */   
/*     */   private class_2960 getTexture() {
/*  78 */     switch (this.type.getIndex()) { case 1: case 2: case 3: case 4:  }  return 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  83 */       STAR_TEXTURE;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isPositionInBlock(class_243 position) {
/*  88 */     if (mc.field_1687 == null || mc.field_1724 == null) return true; 
/*  89 */     class_2338 blockPos = class_2338.method_49638((class_2374)position);
/*  90 */     if (mc.field_1687.method_8320(blockPos).method_26212((class_1922)mc.field_1687, blockPos)) {
/*  91 */       return true;
/*     */     }
/*     */     
/*  94 */     class_3959 context = new class_3959(new class_243(mc.field_1724.method_23317(), mc.field_1724.method_23318() + mc.field_1724.method_5751(), mc.field_1724.method_23321()), position, class_3959.class_3960.field_17558, class_3959.class_242.field_1348, (class_1297)mc.field_1724);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 100 */     class_3965 result = mc.field_1687.method_17742(context);
/* 101 */     return (result.method_17783() == class_239.class_240.field_1332);
/*     */   }
/*     */   
/*     */   private float random(float min, float max) {
/* 105 */     return min + this.rnd.nextFloat() * (max - min);
/*     */   }
/*     */   
/*     */   private boolean isMoving() {
/* 109 */     return (mc.field_1724 != null && (mc.field_1724.field_3913.field_3905 != 0.0F || mc.field_1724.field_3913.field_3907 != 0.0F));
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onAttack(EventAttackEntity event) {
/* 114 */     if (mc.field_1724 == null || mc.field_1687 == null)
/* 115 */       return;  if (this.reason.is("Ударе")) {
/*     */       
/* 117 */       class_1297 target = event.getTarget();
/* 118 */       if (target != null)
/* 119 */         for (int i = 0; i < 35; i++) {
/* 120 */           double targetX = target.method_23317() + random(-0.4F, 0.4F);
/* 121 */           double targetY = target.method_23318() + random(-0.4F, target.method_17682() + 0.4F);
/* 122 */           double targetZ = target.method_23321() + random(-0.4F, 0.4F);
/*     */           
/* 124 */           if (!isPositionInBlock(new class_243(targetX, targetY, targetZ))) {
/*     */             
/* 126 */             float baseMx = random(-0.8F, 0.8F) * 2.0F;
/* 127 */             float baseMy = random(-0.25F, 1.4F);
/* 128 */             float baseMz = random(-0.8F, 0.8F) * 2.0F;
/*     */             
/* 130 */             class_243 velocity = new class_243((baseMx * 0.075F), (baseMy * 0.075F), (baseMz * 0.075F));
/* 131 */             long life = (long)random(1000.0F, 1200.0F);
/*     */             
/* 133 */             addParticle(targetX, targetY, targetZ, velocity, ColorUtils.getThemeColor(), 0.3F, life, 0.5F, 6.99999975040555E-4D);
/*     */           } 
/*     */         }  
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onPacket(EventPacket e) {
/* 141 */     if (mc.field_1687 == null || mc.field_1724 == null)
/* 142 */       return;  if (!this.reason.is("Сносе тотема"))
/*     */       return; 
/* 144 */     class_2596 class_2596 = e.getPacket(); if (class_2596 instanceof class_2663) { class_2663 packet = (class_2663)class_2596;
/* 145 */       if (packet.method_11470() == 35) {
/* 146 */         class_1297 entity = packet.method_11469((class_1937)mc.field_1687);
/* 147 */         if (entity != null) {
/* 148 */           double centerX = entity.method_23317();
/* 149 */           double centerY = entity.method_23318() + entity.method_17682() / 2.0D;
/* 150 */           double centerZ = entity.method_23321();
/*     */           
/* 152 */           for (int i = 0; i < 50; i++) {
/* 153 */             double theta = this.rnd.nextDouble() * 2.0D * Math.PI;
/* 154 */             double phi = this.rnd.nextDouble() * Math.PI;
/* 155 */             double speed = (this.rnd.nextDouble() * 0.5D + 0.5D) * 0.1D;
/*     */             
/* 157 */             double vx = Math.sin(phi) * Math.cos(theta) * speed;
/* 158 */             double vy = Math.sin(phi) * Math.sin(theta) * speed;
/* 159 */             double vz = Math.cos(phi) * speed;
/*     */             
/* 161 */             double spawnX = centerX + random(-0.3F, 0.3F);
/* 162 */             double spawnY = centerY + random(-0.3F, 0.3F);
/* 163 */             double spawnZ = centerZ + random(-0.3F, 0.3F);
/*     */             
/* 165 */             if (!isPositionInBlock(new class_243(spawnX, spawnY, spawnZ))) {
/*     */               
/* 167 */               int color = (this.rnd.nextDouble() < 0.7D) ? -16711936 : -256;
/* 168 */               long life = (long)random(1500.0F, 2000.0F);
/*     */               
/* 170 */               addParticle(spawnX, spawnY, spawnZ, new class_243(vx, vy, vz), color, 0.3F, life, 2.0F, 4.999999873689376E-5D);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       }  }
/*     */   
/*     */   }
/*     */   @EventLink
/*     */   public void onUpdate(EventUpdate e) {
/* 179 */     if (mc.field_1724 == null || mc.field_1687 == null)
/*     */       return; 
/* 181 */     int particleCount = (int)this.count.get();
/*     */     
/* 183 */     if (this.reason.is("Бездействии")) {
/* 184 */       class_243 base = new class_243(mc.field_1724.method_23317(), mc.field_1724.method_23318() + mc.field_1724.method_17682() / 2.0D, mc.field_1724.method_23321());
/*     */       
/* 186 */       for (int i = 0; i < particleCount; i++) {
/* 187 */         double distance = random(7.0F, 35.0F);
/* 188 */         double angle = Math.toRadians(random(0.0F, 360.0F));
/* 189 */         double height = random(-7.0F, 25.0F);
/*     */         
/* 191 */         double spawnX = base.field_1352 + Math.cos(angle) * distance;
/* 192 */         double spawnY = base.field_1351 + height;
/* 193 */         double spawnZ = base.field_1350 + Math.sin(angle) * distance;
/*     */         
/* 195 */         class_243 spawnPos = new class_243(spawnX, spawnY, spawnZ);
/* 196 */         if (!isPositionInBlock(spawnPos)) {
/*     */           
/* 198 */           long life = (long)random(1500.0F, 2000.0F);
/* 199 */           double speed = (this.rnd.nextDouble() < 0.8D) ? random(0.015F, 0.03F) : 0.125D;
/* 200 */           double phi = Math.toRadians(random(0.0F, 360.0F));
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 205 */           class_243 velocity = new class_243(Math.cos(phi) * speed, random((float)(-speed * 0.10000000149011612D), (float)(speed * 0.10000000149011612D)), Math.sin(phi) * speed);
/*     */ 
/*     */           
/* 208 */           addParticle(spawnX, spawnY, spawnZ, velocity, ColorUtils.getThemeColor(), 0.3F, life, 3.0F, 4.999999873689376E-5D);
/*     */         } 
/*     */       } 
/*     */     } 
/* 212 */     if (this.reason.is("Беге") && isMoving()) {
/* 213 */       class_243 direction, motion = mc.field_1724.method_18798();
/* 214 */       double speed = Math.sqrt(motion.field_1352 * motion.field_1352 + motion.field_1350 * motion.field_1350);
/*     */ 
/*     */       
/* 217 */       if (speed < 0.01D) {
/* 218 */         direction = mc.field_1724.method_5720().method_1021(-1.0D);
/* 219 */       } else if (mc.field_1724.method_6128()) {
/* 220 */         direction = motion.method_1029().method_1021(-1.0D);
/*     */       } else {
/* 222 */         direction = new class_243(-motion.field_1352 / speed, 0.0D, -motion.field_1350 / speed);
/*     */       } 
/*     */       
/* 225 */       double distanceBehind = (mc.field_1724.method_6128() ? 1.2D : 0.5D) + ((speed > 0.1D) ? (speed * 1.5D) : 0.0D);
/* 226 */       double offsetX = random(-0.35F, 0.35F);
/* 227 */       double offsetZ = random(-0.35F, 0.35F);
/*     */       
/* 229 */       double posX = mc.field_1724.method_23317() + direction.field_1352 * distanceBehind + offsetX;
/*     */ 
/*     */       
/* 232 */       double posY = mc.field_1724.method_6128() ? (mc.field_1724.method_23318() + mc.field_1724.method_17682() / 2.0D + direction.field_1351 * distanceBehind + random(-0.35F, 0.35F)) : (mc.field_1724.method_23318() + random(0.2F, mc.field_1724.method_17682() + 0.1F));
/* 233 */       double posZ = mc.field_1724.method_23321() + direction.field_1350 * distanceBehind + offsetZ;
/*     */       
/* 235 */       if (!isPositionInBlock(new class_243(posX, posY, posZ))) {
/* 236 */         double baseSpeed = 0.075D;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 241 */         class_243 velocity = direction.method_1021(baseSpeed).method_1031(random(-0.01F, 0.01F), random(-0.05F, 0.01F), random(-0.01F, 0.01F)).method_1021(0.1D);
/*     */         
/* 243 */         long life = (long)random(1500.0F, 2000.0F);
/* 244 */         addParticle(posX, posY, posZ, velocity, ColorUtils.getThemeColor(), 0.3F, life, 3.0F, 4.999999873689376E-5D);
/*     */       } 
/*     */     } 
/*     */     
/* 248 */     boolean trackPearls = this.reason.is("Падении перла");
/* 249 */     boolean trackTridents = this.reason.is("Падении трезубца");
/* 250 */     if (trackPearls || trackTridents) {
/* 251 */       class_238 searchBox = mc.field_1724.method_5829().method_1014(100.0D);
/* 252 */       List<class_1297> entities = mc.field_1687.method_8333(null, searchBox, e2 -> true);
/*     */       
/* 254 */       for (class_1297 entity : entities) {
/* 255 */         if (trackPearls && entity instanceof class_1684) { class_1684 pearl = (class_1684)entity;
/* 256 */           if (!pearl.method_24828()) {
/* 257 */             createProjectileParticles(pearl.method_19538(), 1);
/*     */           } }
/*     */ 
/*     */         
/* 261 */         if (trackTridents && entity instanceof class_1685) { class_1685 trident = (class_1685)entity;
/* 262 */           if (trident.method_18798().method_1027() > 0.01D) {
/* 263 */             createProjectileParticles(trident.method_19538(), 1);
/*     */           } }
/*     */       
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void createProjectileParticles(class_243 position, int cnt) {
/* 271 */     int particleColor = ColorUtils.getThemeColor();
/*     */     
/* 273 */     for (int i = 0; i < cnt * 2.5D; i++) {
/* 274 */       double dy = random(0.1F, 0.35F);
/* 275 */       class_243 particlePos = new class_243(position.field_1352, position.field_1351 + dy, position.field_1350);
/*     */       
/* 277 */       if (!isPositionInBlock(particlePos)) {
/*     */         
/* 279 */         float speedMin = random(0.015F, 0.0375F);
/* 280 */         float speedMax = random(0.05F, 0.075F);
/* 281 */         double speedFinal = random(speedMin, speedMax);
/* 282 */         double speedFinalY = speedFinal * 0.4D;
/*     */         
/* 284 */         double angleVel = Math.toRadians(random(0.0F, 360.0F));
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 289 */         class_243 velocity = new class_243(Math.cos(angleVel) * speedFinal, random((float)-speedFinalY, (float)speedFinalY), Math.sin(angleVel) * speedFinal);
/*     */ 
/*     */         
/* 292 */         long life = (long)random(2400.0F, 2800.0F);
/* 293 */         addParticle(particlePos.field_1352, particlePos.field_1351, particlePos.field_1350, velocity, particleColor, 0.25F, life, 2.0F, 4.999999873689376E-5D);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private void addParticle(double x, double y, double z, class_243 velocity, int color, float size, long lifeTime, float smooth, double gravity) {
/* 298 */     if (ParticleData.checkCollision(x, y, z, size, mc))
/* 299 */       synchronized (this.particles) {
/* 300 */         this.particles.add(new ParticleData(new class_243(x, y, z), velocity, color, size, lifeTime, smooth, gravity));
/*     */       }  
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onRender3D(Event3DRender e) {
/*     */     ArrayList<ParticleData> renderList;
/* 307 */     if (mc.field_1724 == null || mc.field_1687 == null)
/*     */       return; 
/* 309 */     synchronized (this.particles) {
/* 310 */       this.particles.removeIf(ParticleData::isDead);
/*     */     } 
/*     */     
/* 313 */     if (this.particles.isEmpty())
/*     */       return; 
/* 315 */     class_4587 matrices = e.getMatrices();
/* 316 */     class_243 camera = mc.field_1773.method_19418().method_19326();
/* 317 */     class_2960 texture = getTexture();
/*     */     
/* 319 */     RenderSystem.enableBlend();
/* 320 */     RenderSystem.disableDepthTest();
/* 321 */     RenderSystem.depthMask(false);
/* 322 */     RenderSystem.disableCull();
/*     */     
/* 324 */     if (this.glow.isState()) {
/* 325 */       RenderSystem.blendFunc(770, 1);
/*     */     } else {
/* 327 */       RenderSystem.defaultBlendFunc();
/*     */     } 
/*     */     
/* 330 */     RenderSystem.setShaderTexture(0, texture);
/* 331 */     RenderSystem.setShader(class_10142.field_53880);
/*     */ 
/*     */     
/* 334 */     synchronized (this.particles) {
/* 335 */       renderList = new ArrayList<>(this.particles);
/*     */     } 
/*     */     
/* 338 */     for (ParticleData particle : renderList) {
/* 339 */       particle.update(mc);
/*     */       
/* 341 */       double x = particle.position.field_1352 - camera.field_1352;
/* 342 */       double y = particle.position.field_1351 - camera.field_1351;
/* 343 */       double z = particle.position.field_1350 - camera.field_1350;
/*     */       
/* 345 */       matrices.method_22903();
/* 346 */       matrices.method_46416((float)x, (float)y, (float)z);
/* 347 */       matrices.method_22907(class_7833.field_40716.rotationDegrees(-mc.field_1773.method_19418().method_19330()));
/* 348 */       matrices.method_22907(class_7833.field_40714.rotationDegrees(mc.field_1773.method_19418().method_19329()));
/*     */       
/* 350 */       Matrix4f matrix = matrices.method_23760().method_23761();
/*     */       
/* 352 */       float half = particle.size / 2.0F;
/* 353 */       int alpha = (int)(particle.alpha * 255.0F);
/* 354 */       int r = particle.color >> 16 & 0xFF;
/* 355 */       int g = particle.color >> 8 & 0xFF;
/* 356 */       int b = particle.color & 0xFF;
/*     */       
/* 358 */       class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
/*     */       
/* 360 */       buffer.method_22918(matrix, -half, -half, 0.0F).method_22913(0.0F, 1.0F).method_1336(r, g, b, alpha);
/* 361 */       buffer.method_22918(matrix, -half, half, 0.0F).method_22913(0.0F, 0.0F).method_1336(r, g, b, alpha);
/* 362 */       buffer.method_22918(matrix, half, half, 0.0F).method_22913(1.0F, 0.0F).method_1336(r, g, b, alpha);
/* 363 */       buffer.method_22918(matrix, half, -half, 0.0F).method_22913(1.0F, 1.0F).method_1336(r, g, b, alpha);
/*     */       
/* 365 */       class_286.method_43433(buffer.method_60800());
/*     */       
/* 367 */       matrices.method_22909();
/*     */     } 
/*     */     
/* 370 */     RenderSystem.enableCull();
/* 371 */     RenderSystem.depthMask(true);
/* 372 */     RenderSystem.enableDepthTest();
/* 373 */     RenderSystem.defaultBlendFunc();
/* 374 */     RenderSystem.disableBlend();
/*     */   }
/*     */   
/*     */   static class ParticleData {
/*     */     class_243 position;
/*     */     class_243 velocity;
/*     */     int color;
/*     */     float size;
/*     */     long lifeTime;
/*     */     long birthTime;
/* 384 */     float alpha = 1.0F;
/*     */     float smoothFactor;
/*     */     long lastUpdateNs;
/*     */     double gravity;
/*     */     
/*     */     ParticleData(class_243 position, class_243 velocity, int color, float size, long lifeTime, float smooth, double gravity) {
/* 390 */       this.position = position;
/* 391 */       this.velocity = velocity;
/* 392 */       this.color = color;
/* 393 */       this.size = size;
/* 394 */       this.lifeTime = lifeTime;
/* 395 */       this.birthTime = System.currentTimeMillis();
/* 396 */       this.lastUpdateNs = System.nanoTime();
/* 397 */       this.smoothFactor = smooth;
/* 398 */       this.gravity = gravity;
/*     */     }
/*     */     
/*     */     boolean isDead() {
/* 402 */       return (System.currentTimeMillis() - this.birthTime >= this.lifeTime);
/*     */     }
/*     */     
/*     */     void update(class_310 mc) {
/* 406 */       long nowNs = System.nanoTime();
/* 407 */       double deltaSec = (nowNs - this.lastUpdateNs) / 1.0E9D;
/* 408 */       this.lastUpdateNs = nowNs;
/*     */       
/* 410 */       float progress = Math.min(1.0F, (float)(System.currentTimeMillis() - this.birthTime) / (float)this.lifeTime);
/* 411 */       double factor = Math.pow(1.0D - progress, this.smoothFactor);
/*     */       
/* 413 */       double vx = this.velocity.field_1352;
/* 414 */       double vy = this.velocity.field_1351;
/* 415 */       double vz = this.velocity.field_1350;
/*     */       
/* 417 */       double newX = this.position.field_1352;
/* 418 */       double newY = this.position.field_1351;
/* 419 */       double newZ = this.position.field_1350;
/*     */       
/* 421 */       newX += vx * factor * deltaSec * 60.0D;
/* 422 */       if (!checkCollision(newX, this.position.field_1351, this.position.field_1350, this.size, mc)) {
/* 423 */         vx = -vx * 0.8D;
/* 424 */         newX = this.position.field_1352;
/*     */       } 
/*     */       
/* 427 */       newY += vy * factor * deltaSec * 60.0D;
/* 428 */       if (!checkCollision(newX, newY, this.position.field_1350, this.size, mc)) {
/* 429 */         vy = -vy * 1.5D;
/* 430 */         newY = this.position.field_1351;
/*     */       } 
/*     */       
/* 433 */       newZ += vz * factor * deltaSec * 60.0D;
/* 434 */       if (!checkCollision(newX, newY, newZ, this.size, mc)) {
/* 435 */         vz = -vz * 0.8D;
/* 436 */         newZ = this.position.field_1350;
/*     */       } 
/*     */       
/* 439 */       this.position = new class_243(newX, newY, newZ);
/* 440 */       this.velocity = new class_243(vx * 0.9999D, vy * 0.9999D - this.gravity, vz * 0.9999D);
/* 441 */       this.alpha = 1.0F - progress;
/*     */     }
/*     */     
/*     */     static boolean checkCollision(double x, double y, double z, float size, class_310 mc) {
/* 445 */       if (mc.field_1687 == null) return false; 
/* 446 */       double half = size * 0.5D;
/* 447 */       int minX = class_3532.method_15357(x - half);
/* 448 */       int maxX = class_3532.method_15357(x + half);
/* 449 */       int minY = class_3532.method_15357(y - half);
/* 450 */       int maxY = class_3532.method_15357(y + half);
/* 451 */       int minZ = class_3532.method_15357(z - half);
/* 452 */       int maxZ = class_3532.method_15357(z + half);
/*     */       
/* 454 */       class_2338.class_2339 pos = new class_2338.class_2339();
/* 455 */       for (int bx = minX; bx <= maxX; bx++) {
/* 456 */         for (int by = minY; by <= maxY; by++) {
/* 457 */           for (int bz = minZ; bz <= maxZ; bz++) {
/* 458 */             pos.method_10103(bx, by, bz);
/* 459 */             class_2680 state = mc.field_1687.method_8320((class_2338)pos);
/* 460 */             if (!state.method_26215() && state.method_26212((class_1922)mc.field_1687, (class_2338)pos)) {
/* 461 */               return false;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/* 466 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\Particle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */