/*     */ package shame.astra.client.modules.impl.render;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.class_1297;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_286;
/*     */ import net.minecraft.class_287;
/*     */ import net.minecraft.class_289;
/*     */ import net.minecraft.class_290;
/*     */ import net.minecraft.class_293;
/*     */ import net.minecraft.class_2960;
/*     */ import net.minecraft.class_4587;
/*     */ import net.minecraft.class_7833;
/*     */ import org.joml.Matrix4f;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.Event3DRender;
/*     */ import shame.astra.api.events.implement.EventAttackEntity;
/*     */ import shame.astra.api.utils.color.ColorUtils;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*     */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*     */ 
/*     */ public class HitMarker extends Module {
/*  26 */   public static HitMarker INSTANCE = new HitMarker();
/*     */   
/*  28 */   private final FloatSetting size = new FloatSetting("Размер", 0.5F, 0.1F, 2.0F, 0.05F);
/*  29 */   private final FloatSetting fadeInTime = new FloatSetting("Время появления", 100.0F, 50.0F, 500.0F, 10.0F);
/*  30 */   private final FloatSetting displayTime = new FloatSetting("Время показа", 300.0F, 100.0F, 1000.0F, 50.0F);
/*  31 */   private final FloatSetting fadeOutTime = new FloatSetting("Время исчезновения", 200.0F, 50.0F, 500.0F, 10.0F);
/*  32 */   private final BooleanSetting glow = new BooleanSetting("Свечение", true);
/*  33 */   private final BooleanSetting scale = new BooleanSetting("Анимация масштаба", true);
/*     */   
/*  35 */   private final ArrayList<HitMarkerData> hitMarkers = new ArrayList<>();
/*     */   
/*     */   public HitMarker() {
/*  38 */     super("HitMarker", "Показывает маркер при ударе", Module.ModuleCategory.RENDER);
/*  39 */     addSettings(new Setting[] { (Setting)this.size, (Setting)this.fadeInTime, (Setting)this.displayTime, (Setting)this.fadeOutTime, (Setting)this.glow, (Setting)this.scale });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  44 */     this.hitMarkers.clear();
/*  45 */     super.onDisable();
/*     */   }
/*     */   
/*     */   private class_2960 getTexture() {
/*  49 */     return class_2960.method_60655("astra", "textures/cross/cross.png");
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onAttack(EventAttackEntity event) {
/*  54 */     if (mc.field_1724 == null || mc.field_1687 == null)
/*     */       return; 
/*  56 */     class_1297 target = event.getTarget();
/*  57 */     if (target != null) {
/*  58 */       synchronized (this.hitMarkers) {
/*  59 */         this.hitMarkers.add(new HitMarkerData(
/*  60 */               resolveHitPosition((class_1297)event.getPlayer(), target), 
/*  61 */               System.currentTimeMillis(), 
/*  62 */               (long)this.fadeInTime.get(), 
/*  63 */               (long)this.displayTime.get(), 
/*  64 */               (long)this.fadeOutTime.get()));
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class_243 resolveHitPosition(class_1297 attacker, class_1297 target) {
/*  74 */     class_243 fallback = new class_243(target.method_23317(), target.method_23318() + target.method_17682() / 2.0D, target.method_23321());
/*     */     
/*  76 */     if (attacker == null) return fallback;
/*     */     
/*  78 */     class_243 eyePos = attacker.method_5836(1.0F);
/*  79 */     class_243 lookVec = attacker.method_5828(1.0F);
/*  80 */     class_243 targetCenter = target.method_5829().method_1005();
/*  81 */     double distance = Math.max(eyePos.method_1022(targetCenter) + 1.0D, 6.0D);
/*  82 */     class_243 reachPos = eyePos.method_1019(lookVec.method_1021(distance));
/*     */     
/*  84 */     Optional<class_243> hitPos = target.method_5829().method_992(eyePos, reachPos);
/*  85 */     if (hitPos.isPresent()) {
/*  86 */       return hitPos.get();
/*     */     }
/*     */     
/*  89 */     return eyePos.method_1019(lookVec.method_1021(eyePos.method_1022(targetCenter)));
/*     */   }
/*     */   @EventLink
/*     */   public void onRender3D(Event3DRender e) {
/*     */     ArrayList<HitMarkerData> renderList;
/*  94 */     if (mc.field_1724 == null || mc.field_1687 == null)
/*     */       return; 
/*  96 */     synchronized (this.hitMarkers) {
/*  97 */       this.hitMarkers.removeIf(HitMarkerData::isDead);
/*     */     } 
/*     */     
/* 100 */     if (this.hitMarkers.isEmpty())
/*     */       return; 
/* 102 */     class_4587 matrices = e.getMatrices();
/* 103 */     class_243 camera = mc.field_1773.method_19418().method_19326();
/* 104 */     class_2960 texture = getTexture();
/*     */     
/* 106 */     RenderSystem.enableBlend();
/* 107 */     RenderSystem.disableDepthTest();
/* 108 */     RenderSystem.depthMask(false);
/* 109 */     RenderSystem.disableCull();
/*     */     
/* 111 */     if (this.glow.isState()) {
/* 112 */       RenderSystem.blendFunc(770, 1);
/*     */     } else {
/* 114 */       RenderSystem.defaultBlendFunc();
/*     */     } 
/*     */     
/* 117 */     RenderSystem.setShaderTexture(0, texture);
/* 118 */     RenderSystem.setShader(class_10142.field_53880);
/*     */ 
/*     */     
/* 121 */     synchronized (this.hitMarkers) {
/* 122 */       renderList = new ArrayList<>(this.hitMarkers);
/*     */     } 
/*     */     
/* 125 */     int color = ColorUtils.getThemeColor();
/* 126 */     int r = color >> 16 & 0xFF;
/* 127 */     int g = color >> 8 & 0xFF;
/* 128 */     int b = color & 0xFF;
/*     */     
/* 130 */     for (HitMarkerData marker : renderList) {
/* 131 */       float alpha = marker.getAlpha();
/* 132 */       if (alpha <= 0.0F)
/*     */         continue; 
/* 134 */       double x = marker.position.field_1352 - camera.field_1352;
/* 135 */       double y = marker.position.field_1351 - camera.field_1351;
/* 136 */       double z = marker.position.field_1350 - camera.field_1350;
/*     */       
/* 138 */       matrices.method_22903();
/* 139 */       matrices.method_46416((float)x, (float)y, (float)z);
/* 140 */       matrices.method_22907(class_7833.field_40716.rotationDegrees(-mc.field_1773.method_19418().method_19330()));
/* 141 */       matrices.method_22907(class_7833.field_40714.rotationDegrees(mc.field_1773.method_19418().method_19329()));
/*     */       
/* 143 */       float currentSize = this.size.get();
/* 144 */       if (this.scale.isState()) {
/* 145 */         float scaleMultiplier = marker.getScaleMultiplier();
/* 146 */         currentSize *= scaleMultiplier;
/*     */       } 
/*     */       
/* 149 */       Matrix4f matrix = matrices.method_23760().method_23761();
/*     */       
/* 151 */       float half = currentSize / 2.0F;
/* 152 */       int alphaInt = (int)(alpha * 255.0F);
/*     */       
/* 154 */       class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
/*     */       
/* 156 */       buffer.method_22918(matrix, -half, -half, 0.0F).method_22913(0.0F, 1.0F).method_1336(r, g, b, alphaInt);
/* 157 */       buffer.method_22918(matrix, -half, half, 0.0F).method_22913(0.0F, 0.0F).method_1336(r, g, b, alphaInt);
/* 158 */       buffer.method_22918(matrix, half, half, 0.0F).method_22913(1.0F, 0.0F).method_1336(r, g, b, alphaInt);
/* 159 */       buffer.method_22918(matrix, half, -half, 0.0F).method_22913(1.0F, 1.0F).method_1336(r, g, b, alphaInt);
/*     */       
/* 161 */       class_286.method_43433(buffer.method_60800());
/*     */       
/* 163 */       matrices.method_22909();
/*     */     } 
/*     */     
/* 166 */     RenderSystem.enableCull();
/* 167 */     RenderSystem.depthMask(true);
/* 168 */     RenderSystem.enableDepthTest();
/* 169 */     RenderSystem.defaultBlendFunc();
/* 170 */     RenderSystem.disableBlend();
/*     */   }
/*     */   
/*     */   static class HitMarkerData {
/*     */     class_243 position;
/*     */     long birthTime;
/*     */     long fadeInTime;
/*     */     long displayTime;
/*     */     long fadeOutTime;
/*     */     
/*     */     HitMarkerData(class_243 position, long birthTime, long fadeInTime, long displayTime, long fadeOutTime) {
/* 181 */       this.position = position;
/* 182 */       this.birthTime = birthTime;
/* 183 */       this.fadeInTime = fadeInTime;
/* 184 */       this.displayTime = displayTime;
/* 185 */       this.fadeOutTime = fadeOutTime;
/*     */     }
/*     */     
/*     */     boolean isDead() {
/* 189 */       return (System.currentTimeMillis() - this.birthTime >= this.fadeInTime + this.displayTime + this.fadeOutTime);
/*     */     }
/*     */     
/*     */     float getAlpha() {
/* 193 */       long elapsed = System.currentTimeMillis() - this.birthTime;
/*     */       
/* 195 */       if (elapsed < this.fadeInTime) {
/* 196 */         float f = (float)elapsed / (float)this.fadeInTime;
/* 197 */         return easeOutCubic(f);
/* 198 */       }  if (elapsed < this.fadeInTime + this.displayTime) {
/* 199 */         return 1.0F;
/*     */       }
/* 201 */       long fadeOutElapsed = elapsed - this.fadeInTime - this.displayTime;
/* 202 */       float progress = Math.min(1.0F, (float)fadeOutElapsed / (float)this.fadeOutTime);
/* 203 */       return 1.0F - easeInCubic(progress);
/*     */     }
/*     */ 
/*     */     
/*     */     float getScaleMultiplier() {
/* 208 */       long elapsed = System.currentTimeMillis() - this.birthTime;
/*     */       
/* 210 */       if (elapsed < this.fadeInTime) {
/* 211 */         float f = (float)elapsed / (float)this.fadeInTime;
/* 212 */         return 0.5F + 0.5F * easeOutBack(f);
/* 213 */       }  if (elapsed < this.fadeInTime + this.displayTime) {
/* 214 */         return 1.0F;
/*     */       }
/* 216 */       long fadeOutElapsed = elapsed - this.fadeInTime - this.displayTime;
/* 217 */       float progress = Math.min(1.0F, (float)fadeOutElapsed / (float)this.fadeOutTime);
/* 218 */       return 1.0F - 0.3F * easeInCubic(progress);
/*     */     }
/*     */ 
/*     */     
/*     */     private float easeOutCubic(float x) {
/* 223 */       return 1.0F - (float)Math.pow(1.0D - x, 3.0D);
/*     */     }
/*     */     
/*     */     private float easeInCubic(float x) {
/* 227 */       return x * x * x;
/*     */     }
/*     */     
/*     */     private float easeOutBack(float x) {
/* 231 */       float c1 = 1.70158F;
/* 232 */       float c3 = c1 + 1.0F;
/* 233 */       return 1.0F + c3 * (float)Math.pow(x - 1.0D, 3.0D) + c1 * (float)Math.pow(x - 1.0D, 2.0D);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\HitMarker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */