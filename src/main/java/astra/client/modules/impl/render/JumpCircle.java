/*     */ package shame.astra.client.modules.impl.render;
/*     */ 
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import net.minecraft.class_10142;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_286;
/*     */ import net.minecraft.class_287;
/*     */ import net.minecraft.class_289;
/*     */ import net.minecraft.class_290;
/*     */ import net.minecraft.class_293;
/*     */ import net.minecraft.class_2960;
/*     */ import net.minecraft.class_3532;
/*     */ import net.minecraft.class_4587;
/*     */ import net.minecraft.class_7833;
/*     */ import org.joml.Matrix4f;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.Event3DRender;
/*     */ import shame.astra.api.events.implement.EventUpdate;
/*     */ import shame.astra.api.utils.color.ColorUtils;
/*     */ import shame.astra.astra;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*     */ 
/*     */ public class JumpCircle
/*     */   extends Module
/*     */ {
/*  31 */   public static JumpCircle INSTANCE = new JumpCircle();
/*     */   
/*     */   private static final float MAX_LIFETIME_MS = 1850.0F;
/*     */   private static final float ROTATION_SPEED = 120.0F;
/*     */   private static final float PULSE_SPEED = 7.0F;
/*     */   private static final float PULSE_SCALE = 0.06F;
/*     */   private static final float PULSE_ALPHA = 0.12F;
/*     */   private static final int MAX_CIRCLES = 8;
/*  39 */   private final FloatSetting radius = new FloatSetting("Радиус", 1.85F, 0.5F, 4.0F, 0.1F);
/*  40 */   private final FloatSetting speed = new FloatSetting("Скорость", 1.2F, 1.0F, 5.0F, 0.1F);
/*  41 */   private final FloatSetting fadeSpeed = new FloatSetting("Скорость исчезновения", 1.5F, 1.0F, 5.0F, 0.5F);
/*     */   
/*  43 */   private final List<CircleData> circles = new ArrayList<>();
/*  44 */   private final class_2960 circleTexture = class_2960.method_60655("astra", "textures/jumpcircle/circle.png");
/*     */   
/*     */   private boolean wasOnGround = true;
/*     */   
/*     */   public JumpCircle() {
/*  49 */     super("JumpCircle", "Круг при прыжке", Module.ModuleCategory.RENDER);
/*  50 */     addSettings(new Setting[] { (Setting)this.radius, (Setting)this.speed, (Setting)this.fadeSpeed });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  55 */     if (mc.field_1724 != null) {
/*  56 */       this.wasOnGround = mc.field_1724.method_24828();
/*     */     }
/*  58 */     super.onEnable();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  63 */     this.circles.clear();
/*  64 */     super.onDisable();
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onUpdate(EventUpdate event) {
/*  69 */     if (mc.field_1724 == null || mc.field_1687 == null)
/*     */       return; 
/*  71 */     boolean isOnGround = mc.field_1724.method_24828();
/*  72 */     if (this.wasOnGround && !isOnGround) {
/*     */ 
/*     */ 
/*     */       
/*  76 */       class_243 pos = new class_243(mc.field_1724.method_23317(), Math.floor(mc.field_1724.method_23318()) + 0.001D, mc.field_1724.method_23321());
/*     */       
/*  78 */       this.circles.add(new CircleData(pos, System.currentTimeMillis()));
/*  79 */       while (this.circles.size() > 8) {
/*  80 */         this.circles.remove(0);
/*     */       }
/*     */     } 
/*  83 */     this.wasOnGround = isOnGround;
/*     */     
/*  85 */     long now = System.currentTimeMillis();
/*  86 */     float lifeTimeMs = getLifeTimeMs();
/*  87 */     Iterator<CircleData> iterator = this.circles.iterator();
/*  88 */     while (iterator.hasNext()) {
/*  89 */       CircleData circle = iterator.next();
/*  90 */       if (now - circle.startTimeMs > (long)lifeTimeMs) {
/*  91 */         iterator.remove();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onRender3D(Event3DRender event) {
/*  98 */     if (this.circles.isEmpty())
/*     */       return; 
/* 100 */     long now = System.currentTimeMillis();
/* 101 */     class_243 cameraPos = event.getCamera().method_19326();
/* 102 */     class_4587 matrices = event.getMatrices();
/*     */     
/* 104 */     RenderSystem.enableBlend();
/* 105 */     RenderSystem.enableDepthTest();
/* 106 */     RenderSystem.depthMask(false);
/* 107 */     RenderSystem.disableCull();
/* 108 */     RenderSystem.blendFunc(770, 1);
/* 109 */     RenderSystem.setShader(class_10142.field_53880);
/* 110 */     RenderSystem.setShaderTexture(0, this.circleTexture);
/*     */     
/* 112 */     for (CircleData circle : this.circles) {
/* 113 */       float progress = getProgress(now, circle);
/* 114 */       if (progress >= 1.0F)
/*     */         continue; 
/* 116 */       float alpha = getAlpha(progress);
/* 117 */       if (alpha <= 0.01F)
/* 118 */         continue;  renderGlowCircle(matrices, cameraPos, circle, progress, alpha, now);
/*     */     } 
/*     */     
/* 121 */     RenderSystem.enableCull();
/* 122 */     RenderSystem.depthMask(true);
/* 123 */     RenderSystem.enableDepthTest();
/* 124 */     RenderSystem.defaultBlendFunc();
/* 125 */     RenderSystem.disableBlend();
/* 126 */     RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   private float getLifeTimeMs() {
/* 130 */     return 1850.0F / Math.max(0.25F, this.speed.get());
/*     */   }
/*     */   
/*     */   private float getProgress(long now, CircleData circle) {
/* 134 */     return (float)(now - circle.startTimeMs) / getLifeTimeMs();
/*     */   }
/*     */   
/*     */   private float getAlpha(float progress) {
/* 138 */     float fade = class_3532.method_15363(progress * this.fadeSpeed.get(), 0.0F, 1.0F);
/* 139 */     return 1.0F - fade;
/*     */   }
/*     */   
/*     */   private void renderGlowCircle(class_4587 matrices, class_243 cameraPos, CircleData circle, float progress, float alpha, long now) {
/* 143 */     float lifeTimeSec = (float)(now - circle.startTimeMs) / 1000.0F;
/* 144 */     float easedProgress = easeOutCubic(progress);
/* 145 */     float scale = Math.min(easedProgress * this.radius.get(), this.radius.get());
/*     */     
/* 147 */     float rotation = lifeTimeSec * 120.0F * this.speed.get();
/* 148 */     rotation += (float)Math.sin(progress * Math.PI * 2.0D) * 30.0F;
/*     */     
/* 150 */     float pulse = (float)Math.sin((lifeTimeSec * 7.0F * this.speed.get()));
/* 151 */     float pulseScale = 1.0F + pulse * 0.06F;
/* 152 */     float pulseAlpha = class_3532.method_15363(alpha * (1.0F + pulse * 0.12F), 0.0F, 1.0F);
/* 153 */     float alphaBoost = class_3532.method_15363(pulseAlpha * 1.25F, 0.0F, 1.0F);
/* 154 */     float finalScale = scale * pulseScale;
/*     */     
/* 156 */     int baseTheme = getStableThemeColor();
/* 157 */     int secondaryTheme = getStableThemeSecondaryColor();
/* 158 */     int colorA = ColorUtils.setAlphaColor(baseTheme, (int)(255.0F * alphaBoost));
/* 159 */     int colorB = ColorUtils.setAlphaColor(secondaryTheme, (int)(255.0F * alphaBoost));
/* 160 */     int darkA = ColorUtils.setAlphaColor(ColorUtils.darken(baseTheme, 0.65F), (int)(255.0F * class_3532.method_15363(alphaBoost * 0.9F, 0.0F, 1.0F)));
/* 161 */     int darkB = ColorUtils.setAlphaColor(ColorUtils.darken(secondaryTheme, 0.65F), (int)(255.0F * class_3532.method_15363(alphaBoost * 0.9F, 0.0F, 1.0F)));
/*     */     
/* 163 */     matrices.method_22903();
/* 164 */     matrices.method_22904(circle.pos.field_1352 - cameraPos.field_1352, circle.pos.field_1351 - cameraPos.field_1351, circle.pos.field_1350 - cameraPos.field_1350);
/* 165 */     matrices.method_22907(class_7833.field_40714.rotationDegrees(90.0F));
/* 166 */     matrices.method_22907(class_7833.field_40718.rotationDegrees(rotation));
/*     */     
/* 168 */     Matrix4f matrix = matrices.method_23760().method_23761();
/* 169 */     float half = finalScale * 0.5F;
/* 170 */     float thickScale = finalScale * 1.08F;
/* 171 */     float thickHalf = thickScale * 0.5F;
/*     */     
/* 173 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
/* 174 */     addTexturedQuad(buffer, matrix, -half, -half, half, half, colorA, colorB);
/* 175 */     addTexturedQuad(buffer, matrix, -thickHalf, -thickHalf, thickHalf, thickHalf, darkA, darkB);
/* 176 */     class_286.method_43433(buffer.method_60800());
/*     */     
/* 178 */     matrices.method_22909();
/*     */   }
/*     */   
/*     */   private void addTexturedQuad(class_287 buffer, Matrix4f matrix, float x1, float y1, float x2, float y2, int colorA, int colorB) {
/* 182 */     int aR = colorA >> 16 & 0xFF;
/* 183 */     int aG = colorA >> 8 & 0xFF;
/* 184 */     int aB = colorA & 0xFF;
/* 185 */     int aA = colorA >> 24 & 0xFF;
/* 186 */     int bR = colorB >> 16 & 0xFF;
/* 187 */     int bG = colorB >> 8 & 0xFF;
/* 188 */     int bB = colorB & 0xFF;
/* 189 */     int bA = colorB >> 24 & 0xFF;
/*     */     
/* 191 */     buffer.method_22918(matrix, x1, y1, 0.0F).method_22913(0.0F, 1.0F).method_1336(aR, aG, aB, aA);
/* 192 */     buffer.method_22918(matrix, x1, y2, 0.0F).method_22913(0.0F, 0.0F).method_1336(bR, bG, bB, bA);
/* 193 */     buffer.method_22918(matrix, x2, y2, 0.0F).method_22913(1.0F, 0.0F).method_1336(bR, bG, bB, bA);
/* 194 */     buffer.method_22918(matrix, x2, y1, 0.0F).method_22913(1.0F, 1.0F).method_1336(aR, aG, aB, aA);
/*     */   }
/*     */   
/*     */   private int getStableThemeColor() {
/* 198 */     if (!astra.INSTANCE.themeStorage.getThemes().getTheme().getName().equals("Rainbow")) {
/* 199 */       return (astra.INSTANCE.themeStorage.getThemes().getTheme()).color[0];
/*     */     }
/* 201 */     return ColorUtils.getThemeColor();
/*     */   }
/*     */   
/*     */   private int getStableThemeSecondaryColor() {
/* 205 */     if (!astra.INSTANCE.themeStorage.getThemes().getTheme().getName().equals("Rainbow")) {
/* 206 */       return (astra.INSTANCE.themeStorage.getThemes().getTheme()).color[0];
/*     */     }
/* 208 */     return ColorUtils.getThemeColor(180);
/*     */   }
/*     */   
/*     */   private static float easeOutCubic(float t) {
/* 212 */     float u = 1.0F - t;
/* 213 */     return 1.0F - u * u * u;
/*     */   }
/*     */   private static final class CircleData extends Record { private final class_243 pos; private final long startTimeMs;
/* 216 */     private CircleData(class_243 pos, long startTimeMs) { this.pos = pos; this.startTimeMs = startTimeMs; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lshame/astra/client/modules/impl/render/JumpCircle$CircleData;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #216	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 216 */       //   0	7	0	this	Lshame/astra/client/modules/impl/render/JumpCircle$CircleData; } public class_243 pos() { return this.pos; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lshame/astra/client/modules/impl/render/JumpCircle$CircleData;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #216	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lshame/astra/client/modules/impl/render/JumpCircle$CircleData; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lshame/astra/client/modules/impl/render/JumpCircle$CircleData;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #216	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lshame/astra/client/modules/impl/render/JumpCircle$CircleData;
/* 216 */       //   0	8	1	o	Ljava/lang/Object; } public long startTimeMs() { return this.startTimeMs; }
/*     */      }
/*     */ 
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\JumpCircle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */