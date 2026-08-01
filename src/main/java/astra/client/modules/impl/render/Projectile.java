/*     */ package shame.astra.client.modules.impl.render;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.class_1297;
/*     */ import net.minecraft.class_1684;
/*     */ import net.minecraft.class_2246;
/*     */ import net.minecraft.class_2374;
/*     */ import net.minecraft.class_238;
/*     */ import net.minecraft.class_239;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_286;
/*     */ import net.minecraft.class_287;
/*     */ import net.minecraft.class_289;
/*     */ import net.minecraft.class_290;
/*     */ import net.minecraft.class_293;
/*     */ import net.minecraft.class_2960;
/*     */ import net.minecraft.class_3532;
/*     */ import net.minecraft.class_3959;
/*     */ import net.minecraft.class_3965;
/*     */ import net.minecraft.class_4184;
/*     */ import net.minecraft.class_4587;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector4f;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.Event3DRender;
/*     */ import shame.astra.api.events.implement.EventRender;
/*     */ import shame.astra.api.utils.color.ColorUtils;
/*     */ import shame.astra.api.utils.render.RenderUtils;
/*     */ import shame.astra.api.utils.render.fonts.msdf.Font;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*     */ 
/*     */ public class Projectile extends Module {
/*  39 */   private final Font impactFont = Fonts.getFont("sf_regular", 14);
/*     */   
/*  41 */   public static Projectile INSTANCE = new Projectile();
/*     */   private static final class ImpactPoint extends Record { private final class_243 pos; private final float seconds;
/*  43 */     private ImpactPoint(class_243 pos, float seconds) { this.pos = pos; this.seconds = seconds; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lshame/astra/client/modules/impl/render/Projectile$ImpactPoint;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #43	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  43 */       //   0	7	0	this	Lshame/astra/client/modules/impl/render/Projectile$ImpactPoint; } public class_243 pos() { return this.pos; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lshame/astra/client/modules/impl/render/Projectile$ImpactPoint;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #43	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lshame/astra/client/modules/impl/render/Projectile$ImpactPoint; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lshame/astra/client/modules/impl/render/Projectile$ImpactPoint;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #43	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lshame/astra/client/modules/impl/render/Projectile$ImpactPoint;
/*  43 */       //   0	8	1	o	Ljava/lang/Object; } public float seconds() { return this.seconds; }
/*     */      }
/*     */   
/*  46 */   private static final class_2960 BLOOM_TEXTURE = class_2960.method_60655("astra", "textures/particle/bloom.png");
/*     */   
/*  48 */   private final FloatSetting size = new FloatSetting("Размер", 1.2F, 0.6F, 2.4F, 0.1F);
/*     */   
/*  50 */   private final List<ImpactPoint> impactPoints = new ArrayList<>();
/*  51 */   private final Matrix4f lastProjectionMatrix = new Matrix4f();
/*  52 */   private final Quaternionf lastCameraRotation = new Quaternionf();
/*  53 */   private class_243 lastCameraPos = class_243.field_1353;
/*     */   private boolean hasMatrices;
/*     */   
/*     */   public Projectile() {
/*  57 */     super("Projectile", "Траектория жемчуга эндера", Module.ModuleCategory.RENDER);
/*  58 */     addSettings(new Setting[] { (Setting)this.size });
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onRender3D(Event3DRender event) {
/*  63 */     if (mc.field_1687 == null || mc.field_1724 == null)
/*     */       return; 
/*  65 */     this.impactPoints.clear();
/*  66 */     this.hasMatrices = true;
/*  67 */     this.lastProjectionMatrix.set((Matrix4fc)event.getProjectionMatrix());
/*  68 */     this.lastCameraPos = event.getCamera().method_19326();
/*  69 */     this.lastCameraRotation.set((Quaternionfc)event.getCamera().method_23767());
/*     */     
/*  71 */     class_4587 matrices = event.getMatrices();
/*  72 */     class_4184 camera = event.getCamera();
/*  73 */     class_243 cameraPos = camera.method_19326();
/*  74 */     Quaternionf cameraRotation = camera.method_23767();
/*  75 */     float tickDelta = event.getTickDelta();
/*     */     
/*  77 */     RenderSystem.enableBlend();
/*  78 */     RenderSystem.blendFunc(770, 1);
/*  79 */     RenderSystem.disableCull();
/*  80 */     RenderSystem.disableDepthTest();
/*  81 */     RenderSystem.depthMask(false);
/*  82 */     RenderSystem.setShader(class_10142.field_53880);
/*  83 */     RenderSystem.setShaderTexture(0, BLOOM_TEXTURE);
/*     */     
/*  85 */     class_238 searchBox = mc.field_1724.method_5829().method_1014(128.0D);
/*  86 */     for (class_1684 pearl : mc.field_1687.method_8390(class_1684.class, searchBox, class_1297::method_5805)) {
/*     */       
/*  88 */       List<class_243> points = simulate(pearl, tickDelta);
/*  89 */       if (points.size() < 2)
/*     */         continue; 
/*  91 */       float seconds = (points.size() - 1) / 20.0F;
/*  92 */       class_243 impactPos = points.get(points.size() - 1);
/*  93 */       this.impactPoints.add(new ImpactPoint(impactPos, seconds));
/*     */       
/*  95 */       float quadSize = this.size.get() * 0.2F;
/*  96 */       int color = ColorUtils.setAlphaColor(ColorUtils.getThemeColor(), 40);
/*  97 */       int r = color >> 16 & 0xFF;
/*  98 */       int g = color >> 8 & 0xFF;
/*  99 */       int b = color & 0xFF;
/* 100 */       int a = color >> 24 & 0xFF;
/*     */       
/* 102 */       matrices.method_22903();
/* 103 */       matrices.method_22904(-cameraPos.field_1352, -cameraPos.field_1351, -cameraPos.field_1350);
/*     */       
/* 105 */       class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
/* 106 */       for (int i = 0; i < points.size() - 1; i++) {
/* 107 */         class_243 start = points.get(i);
/* 108 */         class_243 end = points.get(i + 1);
/*     */         
/* 110 */         int samples = Math.max(2, Math.min(12, (int)Math.ceil(start.method_1022(end) / Math.max(quadSize * 1.75F, 0.08F))));
/* 111 */         for (int j = 0; j < samples; j++) {
/* 112 */           class_243 interp = start.method_35590(end, j / samples);
/*     */           
/* 114 */           matrices.method_22903();
/* 115 */           matrices.method_22904(interp.field_1352, interp.field_1351, interp.field_1350);
/* 116 */           matrices.method_22907(cameraRotation);
/* 117 */           Matrix4f matrix = matrices.method_23760().method_23761();
/*     */           
/* 119 */           buffer.method_22918(matrix, -quadSize, -quadSize, 0.0F).method_22913(0.0F, 0.0F).method_1336(r, g, b, a);
/* 120 */           buffer.method_22918(matrix, -quadSize, quadSize, 0.0F).method_22913(0.0F, 1.0F).method_1336(r, g, b, a);
/* 121 */           buffer.method_22918(matrix, quadSize, quadSize, 0.0F).method_22913(1.0F, 1.0F).method_1336(r, g, b, a);
/* 122 */           buffer.method_22918(matrix, quadSize, -quadSize, 0.0F).method_22913(1.0F, 0.0F).method_1336(r, g, b, a);
/* 123 */           matrices.method_22909();
/*     */         } 
/*     */       } 
/* 126 */       class_286.method_43433(buffer.method_60800());
/* 127 */       matrices.method_22909();
/*     */       
/* 129 */       float markerSize = quadSize * 1.6F;
/* 130 */       int markerColor = ColorUtils.setAlphaColor(ColorUtils.getThemeColor(), 170);
/* 131 */       int mr = markerColor >> 16 & 0xFF;
/* 132 */       int mg = markerColor >> 8 & 0xFF;
/* 133 */       int mb = markerColor & 0xFF;
/* 134 */       int ma = markerColor >> 24 & 0xFF;
/* 135 */       float mx = (float)(impactPos.field_1352 - cameraPos.field_1352);
/* 136 */       float my = (float)(impactPos.field_1351 - cameraPos.field_1351 + 0.029999999329447746D);
/* 137 */       float mz = (float)(impactPos.field_1350 - cameraPos.field_1350);
/*     */       
/* 139 */       matrices.method_22903();
/* 140 */       matrices.method_46416(mx, my, mz);
/* 141 */       matrices.method_22907(cameraRotation);
/* 142 */       Matrix4f markerMatrix = matrices.method_23760().method_23761();
/* 143 */       class_287 marker = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
/* 144 */       marker.method_22918(markerMatrix, -markerSize, -markerSize, 0.0F).method_22913(0.0F, 0.0F).method_1336(mr, mg, mb, ma);
/* 145 */       marker.method_22918(markerMatrix, -markerSize, markerSize, 0.0F).method_22913(0.0F, 1.0F).method_1336(mr, mg, mb, ma);
/* 146 */       marker.method_22918(markerMatrix, markerSize, markerSize, 0.0F).method_22913(1.0F, 1.0F).method_1336(mr, mg, mb, ma);
/* 147 */       marker.method_22918(markerMatrix, markerSize, -markerSize, 0.0F).method_22913(1.0F, 0.0F).method_1336(mr, mg, mb, ma);
/* 148 */       class_286.method_43433(marker.method_60800());
/* 149 */       matrices.method_22909();
/*     */     } 
/*     */     
/* 152 */     RenderSystem.depthMask(true);
/* 153 */     RenderSystem.enableDepthTest();
/* 154 */     RenderSystem.enableCull();
/* 155 */     RenderSystem.defaultBlendFunc();
/* 156 */     RenderSystem.disableBlend();
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onRender2D(EventRender.Default event) {
/* 161 */     if (!this.hasMatrices || this.impactPoints.isEmpty() || mc.field_1724 == null)
/*     */       return; 
/* 163 */     class_4587 matrices = event.getContext().method_51448();
/* 164 */     Font font = this.impactFont;
/* 165 */     if (font == null)
/*     */       return; 
/* 167 */     int themeColor = ColorUtils.getThemeColor();
/*     */     
/* 169 */     for (ImpactPoint impact : this.impactPoints) {
/* 170 */       class_243 screen = worldToScreen(impact.pos());
/* 171 */       if (screen == null)
/*     */         continue; 
/* 173 */       String text = formatOneDecimal(impact.seconds()) + " сек";
/* 174 */       float textWidth = font.getStringWidth(text);
/* 175 */       float boxWidth = textWidth + 10.0F;
/* 176 */       float boxHeight = 12.5F;
/* 177 */       float x = (float)screen.field_1352 - boxWidth / 2.0F;
/* 178 */       float y = (float)screen.field_1351 - 6.0F;
/*     */       
/* 180 */       RenderUtils.drawDefaultHudThemedPanel(matrices, x, y, boxWidth, boxHeight, 3.0F, 3.5F, themeColor);
/* 181 */       font.drawString(matrices, text, x + 5.5F, y + 4.55F, -1);
/*     */     } 
/*     */   }
/*     */   
/*     */   private class_243 worldToScreen(class_243 worldPos) {
/* 186 */     if (mc == null || mc.method_22683() == null) return null;
/*     */     
/* 188 */     Vector3f relative = new Vector3f((float)(worldPos.field_1352 - this.lastCameraPos.field_1352), (float)(worldPos.field_1351 - this.lastCameraPos.field_1351), (float)(worldPos.field_1350 - this.lastCameraPos.field_1350));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 194 */     Quaternionf invCameraRot = (new Quaternionf((Quaternionfc)this.lastCameraRotation)).conjugate();
/* 195 */     relative.rotate((Quaternionfc)invCameraRot);
/*     */     
/* 197 */     Vector4f clip = new Vector4f(relative.x, relative.y, relative.z, 1.0F);
/* 198 */     this.lastProjectionMatrix.transform(clip);
/*     */     
/* 200 */     float w = clip.w;
/* 201 */     if (w <= 1.0E-5F) return null;
/*     */     
/* 203 */     float ndcX = clip.x / w;
/* 204 */     float ndcY = clip.y / w;
/* 205 */     float ndcZ = clip.z / w;
/*     */     
/* 207 */     float screenX = (ndcX * 0.5F + 0.5F) * mc.method_22683().method_4486();
/* 208 */     float screenY = (1.0F - ndcY * 0.5F + 0.5F) * mc.method_22683().method_4502();
/*     */     
/* 210 */     if (Float.isNaN(screenX) || Float.isNaN(screenY) || Float.isInfinite(screenX) || Float.isInfinite(screenY)) {
/* 211 */       return null;
/*     */     }
/*     */     
/* 214 */     if (screenX < -400.0F || screenY < -400.0F || screenX > (mc.method_22683().method_4486() + 400) || screenY > (mc.method_22683().method_4502() + 400)) {
/* 215 */       return null;
/*     */     }
/*     */     
/* 218 */     return new class_243(screenX, screenY, ndcZ);
/*     */   }
/*     */   
/*     */   private String formatOneDecimal(float value) {
/* 222 */     int scaled = Math.round(value * 10.0F);
/* 223 */     return "" + scaled / 10 + "." + scaled / 10;
/*     */   }
/*     */   
/*     */   private List<class_243> simulate(class_1684 pearl, float tickDelta) {
/* 227 */     List<class_243> points = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 232 */     class_243 pos = new class_243(class_3532.method_16436(tickDelta, pearl.field_6014, pearl.method_23317()), class_3532.method_16436(tickDelta, pearl.field_6036, pearl.method_23318()), class_3532.method_16436(tickDelta, pearl.field_5969, pearl.method_23321()));
/*     */     
/* 234 */     class_243 motion = pearl.method_18798();
/* 235 */     points.add(pos);
/*     */     
/* 237 */     for (int i = 0; i < 300; i++) {
/* 238 */       class_243 lastPos = pos;
/* 239 */       class_243 nextPos = pos.method_1019(motion);
/*     */       
/* 241 */       class_3965 hit = mc.field_1687.method_17742(new class_3959(lastPos, nextPos, class_3959.class_3960.field_17558, class_3959.class_242.field_1348, (class_1297)mc.field_1724));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 249 */       if (hit.method_17783() == class_239.class_240.field_1332) {
/* 250 */         points.add(hit.method_17784());
/*     */         
/*     */         break;
/*     */       } 
/* 254 */       points.add(nextPos);
/* 255 */       pos = nextPos;
/*     */       
/* 257 */       boolean inWater = mc.field_1687.method_8320(class_2338.method_49638((class_2374)pos)).method_27852(class_2246.field_10382);
/* 258 */       double drag = inWater ? 0.8D : 0.99D;
/* 259 */       motion = motion.method_1021(drag).method_1023(0.0D, 0.03D, 0.0D);
/*     */       
/* 261 */       if (pos.field_1351 <= mc.field_1687.method_31607())
/*     */         break; 
/*     */     } 
/* 264 */     return points;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\Projectile.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */