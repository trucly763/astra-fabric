/*     */ package shame.astra.client.modules.impl.combat;
/*     */ 
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import net.minecraft.class_10142;
/*     */ import net.minecraft.class_1309;
/*     */ import net.minecraft.class_238;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_286;
/*     */ import net.minecraft.class_287;
/*     */ import net.minecraft.class_289;
/*     */ import net.minecraft.class_290;
/*     */ import net.minecraft.class_293;
/*     */ import net.minecraft.class_2960;
/*     */ import net.minecraft.class_3532;
/*     */ import net.minecraft.class_4184;
/*     */ import net.minecraft.class_4587;
/*     */ import org.joml.Matrix4f;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.Event3DRender;
/*     */ import shame.astra.api.storages.implement.helpertstorages.enumvar.ModuleClass;
/*     */ import shame.astra.api.utils.color.ColorUtils;
/*     */ import shame.astra.api.utils.combat.PredictUtils;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*     */ 
/*     */ public class ElytraTarget
/*     */   extends Module {
/*  29 */   public static ElytraTarget INSTANCE = new ElytraTarget();
/*     */   
/*  31 */   private static final class_2960 GLOW_TEXTURE = class_2960.method_60655("astra", "textures/trajectories/glow.png");
/*     */   private static final float BOX_GLOW_OUTER_THICKNESS = 0.17F;
/*     */   private static final float BOX_GLOW_MID_THICKNESS = 0.13F;
/*     */   private static final float BOX_GLOW_CORE_THICKNESS = 0.11F;
/*     */   private static final float BOX_GLOW_LINE_U = 0.4F;
/*  36 */   private static final int[][] BOX_EDGES = new int[][] { { 0, 2 }, { 2, 6 }, { 6, 4 }, { 4, 0 }, { 1, 3 }, { 3, 7 }, { 7, 5 }, { 5, 1 }, { 0, 1 }, { 2, 3 }, { 6, 7 }, { 4, 5 } };
/*     */ 
/*     */   
/*     */   private class_238 smoothedPredictionBox;
/*     */ 
/*     */   
/*     */   private class_1309 smoothedTarget;
/*     */   
/*  44 */   public final FloatSetting forward = new FloatSetting("Сила предикта", 3.0F, 1.0F, 6.0F, 1.0F);
/*     */   
/*     */   public ElytraTarget() {
/*  47 */     super("ElytraSample", "Таргетит игрока на элитрах", Module.ModuleCategory.COMBAT);
/*  48 */     addSettings(new Setting[] { (Setting)this.forward });
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onRender3D(Event3DRender event) {
/*  53 */     if (mc.field_1724 == null || mc.field_1687 == null) {
/*  54 */       resetPredictionSmoothing();
/*     */       return;
/*     */     } 
/*  57 */     if (!mc.field_1724.method_6128()) {
/*  58 */       resetPredictionSmoothing();
/*     */       
/*     */       return;
/*     */     } 
/*  62 */     Aura aura = ModuleClass.aura;
/*  63 */     if (aura == null || !aura.isEnable()) {
/*  64 */       resetPredictionSmoothing();
/*     */       
/*     */       return;
/*     */     } 
/*  68 */     class_1309 target = aura.getTarget();
/*  69 */     if (target == null || !target.method_5805() || !target.method_6128()) {
/*  70 */       resetPredictionSmoothing();
/*     */       
/*     */       return;
/*     */     } 
/*  74 */     class_238 predictedBox = buildPredictedBox(target);
/*  75 */     renderPredictionBox(event, smoothPredictionBox(target, predictedBox));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  80 */     resetPredictionSmoothing();
/*  81 */     super.onDisable();
/*     */   }
/*     */   
/*     */   private class_238 smoothPredictionBox(class_1309 target, class_238 predictedBox) {
/*  85 */     if (this.smoothedPredictionBox == null || this.smoothedTarget != target || this.smoothedPredictionBox.method_1005().method_1025(predictedBox.method_1005()) > 144.0D) {
/*  86 */       this.smoothedPredictionBox = predictedBox;
/*  87 */       this.smoothedTarget = target;
/*  88 */       return predictedBox;
/*     */     } 
/*     */     
/*  91 */     double distance = Math.sqrt(this.smoothedPredictionBox.method_1005().method_1025(predictedBox.method_1005()));
/*  92 */     double smoothFactor = class_3532.method_15350(0.08D + distance * 0.035D, 0.08D, 0.18D);
/*  93 */     this.smoothedPredictionBox = lerpBox(this.smoothedPredictionBox, predictedBox, smoothFactor);
/*  94 */     return this.smoothedPredictionBox;
/*     */   }
/*     */   
/*     */   private void resetPredictionSmoothing() {
/*  98 */     this.smoothedPredictionBox = null;
/*  99 */     this.smoothedTarget = null;
/*     */   }
/*     */   
/*     */   private class_238 buildPredictedBox(class_1309 target) {
/* 103 */     class_238 currentBox = target.method_5829();
/* 104 */     class_243 predictedCenter = PredictUtils.predict(target, currentBox.method_1005(), Math.max(0, this.forward.getValue().intValue()));
/* 105 */     class_243 offset = predictedCenter.method_1020(currentBox.method_1005());
/* 106 */     return currentBox.method_997(offset);
/*     */   }
/*     */   
/*     */   private class_238 lerpBox(class_238 from, class_238 to, double factor) {
/* 110 */     return new class_238(
/* 111 */         class_3532.method_16436(factor, from.field_1323, to.field_1323), 
/* 112 */         class_3532.method_16436(factor, from.field_1322, to.field_1322), 
/* 113 */         class_3532.method_16436(factor, from.field_1321, to.field_1321), 
/* 114 */         class_3532.method_16436(factor, from.field_1320, to.field_1320), 
/* 115 */         class_3532.method_16436(factor, from.field_1325, to.field_1325), 
/* 116 */         class_3532.method_16436(factor, from.field_1324, to.field_1324));
/*     */   }
/*     */ 
/*     */   
/*     */   private void renderPredictionBox(Event3DRender event, class_238 box) {
/* 121 */     class_4587 matrices = event.getMatrices();
/* 122 */     class_4184 camera = event.getCamera();
/* 123 */     class_243 cameraPos = camera.method_19326();
/*     */     
/* 125 */     int themeColor = ColorUtils.getThemeColor();
/* 126 */     int outerColor = ColorUtils.setAlphaColor(themeColor, 118);
/* 127 */     int midColor = ColorUtils.setAlphaColor(ColorUtils.interpolateColor(themeColor, -1, 0.24F), 210);
/* 128 */     int coreColor = ColorUtils.setAlphaColor(ColorUtils.interpolateColor(themeColor, -1, 0.6F), 255);
/*     */     
/* 130 */     RenderSystem.enableBlend();
/* 131 */     RenderSystem.blendFunc(770, 1);
/* 132 */     RenderSystem.disableCull();
/* 133 */     RenderSystem.disableDepthTest();
/* 134 */     RenderSystem.depthMask(false);
/* 135 */     RenderSystem.setShaderTexture(0, GLOW_TEXTURE);
/* 136 */     RenderSystem.setShader(class_10142.field_53880);
/*     */     
/* 138 */     Matrix4f matrix = matrices.method_23760().method_23761();
/* 139 */     class_287 quads = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
/* 140 */     addGlowBox(quads, matrix, cameraPos, box, outerColor, 0.17F);
/* 141 */     addGlowBox(quads, matrix, cameraPos, box, midColor, 0.13F);
/* 142 */     addGlowBox(quads, matrix, cameraPos, box, coreColor, 0.11F);
/* 143 */     class_286.method_43433(quads.method_60800());
/*     */     
/* 145 */     RenderSystem.setShaderTexture(0, 0);
/* 146 */     RenderSystem.defaultBlendFunc();
/* 147 */     RenderSystem.depthMask(true);
/* 148 */     RenderSystem.enableDepthTest();
/* 149 */     RenderSystem.enableCull();
/* 150 */     RenderSystem.disableBlend();
/*     */   }
/*     */   
/*     */   private void addGlowBox(class_287 buffer, Matrix4f matrix, class_243 camera, class_238 box, int color, float thickness) {
/* 154 */     class_243[] corners = getBoxVectors(box);
/* 155 */     for (int[] edge : BOX_EDGES) {
/* 156 */       addGlowEdge(buffer, matrix, camera, corners[edge[0]], corners[edge[1]], color, thickness);
/*     */     }
/*     */   }
/*     */   
/*     */   private void addGlowEdge(class_287 buffer, Matrix4f matrix, class_243 camera, class_243 start, class_243 end, int color, float thickness) {
/* 161 */     class_243 edge = end.method_1020(start);
/* 162 */     if (edge.method_1027() <= 1.0E-6D)
/*     */       return; 
/* 164 */     class_243 direction = edge.method_1029();
/* 165 */     double overlap = (thickness * 0.22F);
/* 166 */     start = start.method_1020(direction.method_1021(overlap));
/* 167 */     end = end.method_1019(direction.method_1021(overlap));
/* 168 */     edge = end.method_1020(start);
/*     */     
/* 170 */     class_243 center = start.method_1019(end).method_1021(0.5D);
/* 171 */     class_243 toCamera = camera.method_1020(center);
/* 172 */     if (toCamera.method_1027() <= 1.0E-6D) {
/* 173 */       toCamera = new class_243(0.0D, 1.0D, 0.0D);
/*     */     }
/*     */     
/* 176 */     class_243 side = edge.method_1036(toCamera);
/* 177 */     if (side.method_1027() <= 1.0E-6D) {
/* 178 */       side = edge.method_1036(new class_243(0.0D, 1.0D, 0.0D));
/* 179 */       if (side.method_1027() <= 1.0E-6D) {
/* 180 */         side = edge.method_1036(new class_243(1.0D, 0.0D, 0.0D));
/*     */       }
/*     */     } 
/*     */     
/* 184 */     side = side.method_1029().method_1021((thickness * 0.48F));
/*     */     
/* 186 */     class_243 p1 = start.method_1019(side).method_1020(camera);
/* 187 */     class_243 p2 = start.method_1020(side).method_1020(camera);
/* 188 */     class_243 p3 = end.method_1020(side).method_1020(camera);
/* 189 */     class_243 p4 = end.method_1019(side).method_1020(camera);
/*     */     
/* 191 */     float[] rgba = ColorUtils.rgba(color);
/* 192 */     buffer.method_22918(matrix, (float)p1.field_1352, (float)p1.field_1351, (float)p1.field_1350).method_22913(0.4F, 0.0F).method_22915(rgba[0], rgba[1], rgba[2], rgba[3]);
/* 193 */     buffer.method_22918(matrix, (float)p2.field_1352, (float)p2.field_1351, (float)p2.field_1350).method_22913(0.4F, 1.0F).method_22915(rgba[0], rgba[1], rgba[2], rgba[3]);
/* 194 */     buffer.method_22918(matrix, (float)p3.field_1352, (float)p3.field_1351, (float)p3.field_1350).method_22913(0.4F, 1.0F).method_22915(rgba[0], rgba[1], rgba[2], rgba[3]);
/* 195 */     buffer.method_22918(matrix, (float)p4.field_1352, (float)p4.field_1351, (float)p4.field_1350).method_22913(0.4F, 0.0F).method_22915(rgba[0], rgba[1], rgba[2], rgba[3]);
/*     */   }
/*     */   
/*     */   private class_243[] getBoxVectors(class_238 box) {
/* 199 */     return new class_243[] { new class_243(box.field_1323, box.field_1322, box.field_1321), new class_243(box.field_1323, box.field_1325, box.field_1321), new class_243(box.field_1320, box.field_1322, box.field_1321), new class_243(box.field_1320, box.field_1325, box.field_1321), new class_243(box.field_1323, box.field_1322, box.field_1324), new class_243(box.field_1323, box.field_1325, box.field_1324), new class_243(box.field_1320, box.field_1322, box.field_1324), new class_243(box.field_1320, box.field_1325, box.field_1324) };
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\combat\ElytraTarget.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */