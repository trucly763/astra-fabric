/*     */ package shame.astra.client.modules.impl.render;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.class_1297;
/*     */ import net.minecraft.class_1799;
/*     */ import net.minecraft.class_1802;
/*     */ import net.minecraft.class_1935;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_3532;
/*     */ import net.minecraft.class_4587;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector4f;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.Event3DRender;
/*     */ import shame.astra.api.events.implement.EventRender;
/*     */ import shame.astra.api.utils.render.RenderUtils;
/*     */ import shame.astra.api.utils.render.fonts.msdf.Font;
/*     */ import shame.astra.api.utils.render.fonts.msdf.Fonts;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*     */ 
/*     */ public class FireworkESP extends Module {
/*  30 */   public static FireworkESP INSTANCE = new FireworkESP();
/*     */   
/*  32 */   private final FloatSetting interval = new FloatSetting("Интервал (мс)", 100.0F, 10.0F, 1000.0F, 10.0F);
/*  33 */   private final FloatSetting lifetime = new FloatSetting("Время жизни (мс)", 1000.0F, 100.0F, 5000.0F, 100.0F);
/*     */   
/*  35 */   private final Matrix4f lastProjectionMatrix = new Matrix4f();
/*  36 */   private final Quaternionf lastCameraRotation = new Quaternionf();
/*  37 */   private class_243 lastCameraPos = class_243.field_1353;
/*     */   private float lastTickDelta;
/*  39 */   private final Map<Integer, FireworkData> fireworks = new HashMap<>();
/*     */   
/*     */   public FireworkESP() {
/*  42 */     super("FireworkESP", "Показывает теги и трейлы фейерверков", Module.ModuleCategory.RENDER);
/*  43 */     addSettings(new Setting[] { (Setting)this.interval, (Setting)this.lifetime });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  48 */     super.onDisable();
/*  49 */     this.fireworks.clear();
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onRender3D(Event3DRender event) {
/*  54 */     this.lastProjectionMatrix.set((Matrix4fc)event.getProjectionMatrix());
/*  55 */     this.lastCameraPos = event.getCamera().method_19326();
/*  56 */     this.lastCameraRotation.set((Quaternionfc)event.getCamera().method_23767());
/*  57 */     this.lastTickDelta = event.getTickDelta();
/*     */     
/*  59 */     if (mc.field_1687 == null)
/*     */       return; 
/*  61 */     long currentTime = System.currentTimeMillis();
/*     */     
/*  63 */     this.fireworks.entrySet().removeIf(entry -> {
/*     */           class_1297 entity = mc.field_1687.method_8469(((Integer)entry.getKey()).intValue());
/*  65 */           boolean isDead = (entity == null || !entity.method_5805());
/*     */           ((FireworkData)entry.getValue()).points.removeIf(());
/*  67 */           return (isDead && ((FireworkData)entry.getValue()).points.isEmpty());
/*     */         });
/*     */     
/*  70 */     for (class_1297 entity : mc.field_1687.method_18112()) {
/*  71 */       if (entity instanceof net.minecraft.class_1671 && entity.method_5805()) {
/*  72 */         FireworkData data = this.fireworks.computeIfAbsent(Integer.valueOf(entity.method_5628()), k -> new FireworkData());
/*     */         
/*  74 */         if ((float)(currentTime - data.lastSpawnTime) >= this.interval.get()) {
/*     */ 
/*     */ 
/*     */           
/*  78 */           class_243 pos = new class_243(class_3532.method_16436(this.lastTickDelta, entity.field_6038, entity.method_23317()), class_3532.method_16436(this.lastTickDelta, entity.field_5971, entity.method_23318()) + 0.5D, class_3532.method_16436(this.lastTickDelta, entity.field_5989, entity.method_23321()));
/*     */           
/*  80 */           float ageInSeconds = entity.field_6012 / 20.0F;
/*  81 */           data.points.add(new TrailPoint(pos, currentTime, ageInSeconds));
/*  82 */           data.lastSpawnTime = currentTime;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onRender2D(EventRender.Default event) {
/*  90 */     if (mc.field_1724 == null || mc.field_1687 == null)
/*     */       return; 
/*  92 */     class_4587 matrices = event.getContext().method_51448();
/*  93 */     class_1799 icon = new class_1799((class_1935)class_1802.field_8639);
/*  94 */     Font font = Fonts.getFont("sf_regular", 14);
/*  95 */     long currentTime = System.currentTimeMillis();
/*     */     
/*  97 */     for (Map.Entry<Integer, FireworkData> entry : this.fireworks.entrySet()) {
/*  98 */       FireworkData data = entry.getValue();
/*     */       
/* 100 */       for (TrailPoint p : data.points) {
/* 101 */         class_243 screen = worldToScreen(p.pos);
/* 102 */         if (screen == null)
/*     */           continue; 
/* 104 */         float progress = 1.0F - (float)(currentTime - p.timestamp) / this.lifetime.get();
/* 105 */         progress = class_3532.method_15363(progress, 0.0F, 1.0F);
/* 106 */         String text = String.format("%.1fs", new Object[] { Float.valueOf(p.ageSec) });
/*     */         
/* 108 */         renderIconRect(event, matrices, font, icon, screen, progress, text);
/*     */       } 
/*     */       
/* 111 */       class_1297 entity = mc.field_1687.method_8469(((Integer)entry.getKey()).intValue());
/* 112 */       if (entity instanceof net.minecraft.class_1671 && entity.method_5805()) {
/*     */ 
/*     */ 
/*     */         
/* 116 */         class_243 currentPos = new class_243(class_3532.method_16436(this.lastTickDelta, entity.field_6038, entity.method_23317()), class_3532.method_16436(this.lastTickDelta, entity.field_5971, entity.method_23318()) + 0.5D, class_3532.method_16436(this.lastTickDelta, entity.field_5989, entity.method_23321()));
/*     */         
/* 118 */         class_243 screen = worldToScreen(currentPos);
/* 119 */         if (screen != null) {
/* 120 */           String text = String.format("%.1fs", new Object[] { Float.valueOf(entity.field_6012 / 20.0F) });
/* 121 */           renderIconRect(event, matrices, font, icon, screen, 1.0F, text);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void renderIconRect(EventRender.Default event, class_4587 matrices, Font font, class_1799 icon, class_243 screen, float progress, String text) {
/* 128 */     float iconScale = 0.6F;
/* 129 */     float rectHeight = 12.0F;
/* 130 */     float padding = 2.5F;
/* 131 */     float gap = 2.0F;
/* 132 */     float textYOffset = 3.5F;
/*     */     
/* 134 */     float animScale = 0.35F + 0.65F * progress;
/* 135 */     int alpha = (int)(200.0F * progress);
/* 136 */     if (alpha <= 5)
/*     */       return; 
/* 138 */     int bgColor = alpha << 24 | 0xA0A0A;
/* 139 */     int textColor = alpha << 24 | 0xFFFFFF;
/*     */     
/* 141 */     float textWidth = (font != null) ? font.getStringWidth(text) : 0.0F;
/* 142 */     float iconWidth = 16.0F * iconScale;
/* 143 */     float totalWidth = padding + iconWidth + gap + textWidth + padding;
/*     */     
/* 145 */     matrices.method_22903();
/* 146 */     matrices.method_22904(screen.field_1352, screen.field_1351, 0.0D);
/* 147 */     matrices.method_22905(animScale, animScale, 1.0F);
/*     */     
/* 149 */     RenderUtils.drawRoundedRect(matrices, -totalWidth / 2.0F, -rectHeight / 2.0F, totalWidth, rectHeight, 0.0F, bgColor);
/*     */     
/* 151 */     float currentX = -totalWidth / 2.0F + padding;
/*     */     
/* 153 */     matrices.method_22903();
/* 154 */     matrices.method_46416(currentX, -(16.0F * iconScale) / 2.0F, 0.0F);
/* 155 */     matrices.method_22905(iconScale, iconScale, 1.0F);
/* 156 */     event.getContext().method_51427(icon, 0, 0);
/* 157 */     matrices.method_22909();
/*     */     
/* 159 */     currentX += iconWidth + gap;
/*     */     
/* 161 */     if (font != null) {
/* 162 */       font.drawString(matrices, text, currentX, -rectHeight / 2.0F + textYOffset + 0.5F, textColor);
/*     */     }
/*     */     
/* 165 */     matrices.method_22909();
/*     */   }
/*     */   
/*     */   private class_243 worldToScreen(class_243 worldPos) {
/* 169 */     Vector3f relative = new Vector3f((float)(worldPos.field_1352 - this.lastCameraPos.field_1352), (float)(worldPos.field_1351 - this.lastCameraPos.field_1351), (float)(worldPos.field_1350 - this.lastCameraPos.field_1350));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 175 */     relative.rotate((Quaternionfc)(new Quaternionf((Quaternionfc)this.lastCameraRotation)).conjugate());
/*     */     
/* 177 */     Vector4f clip = new Vector4f(relative.x, relative.y, relative.z, 1.0F);
/* 178 */     this.lastProjectionMatrix.transform(clip);
/*     */     
/* 180 */     float w = clip.w;
/* 181 */     if (w <= 1.0E-5F) return null;
/*     */     
/* 183 */     float ndcX = clip.x / w;
/* 184 */     float ndcY = clip.y / w;
/* 185 */     float ndcZ = clip.z / w;
/*     */     
/* 187 */     float screenX = (ndcX * 0.5F + 0.5F) * mc.method_22683().method_4486();
/* 188 */     float screenY = (1.0F - ndcY * 0.5F + 0.5F) * mc.method_22683().method_4502();
/*     */     
/* 190 */     if (Float.isNaN(screenX) || Float.isNaN(screenY) || Float.isInfinite(screenX) || Float.isInfinite(screenY)) {
/* 191 */       return null;
/*     */     }
/*     */     
/* 194 */     return new class_243(screenX, screenY, ndcZ);
/*     */   }
/*     */   
/*     */   private static class FireworkData {
/*     */     long lastSpawnTime;
/* 199 */     final List<FireworkESP.TrailPoint> points = new ArrayList<>();
/*     */   }
/*     */   
/*     */   private static class TrailPoint {
/*     */     final class_243 pos;
/*     */     final long timestamp;
/*     */     final float ageSec;
/*     */     
/*     */     TrailPoint(class_243 pos, long timestamp, float ageSec) {
/* 208 */       this.pos = pos;
/* 209 */       this.timestamp = timestamp;
/* 210 */       this.ageSec = ageSec;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\FireworkESP.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */