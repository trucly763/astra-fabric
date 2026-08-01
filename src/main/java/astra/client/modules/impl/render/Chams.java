/*     */ package shame.astra.client.modules.impl.render;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import java.awt.Color;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.class_10042;
/*     */ import net.minecraft.class_10055;
/*     */ import net.minecraft.class_1007;
/*     */ import net.minecraft.class_10142;
/*     */ import net.minecraft.class_1297;
/*     */ import net.minecraft.class_1657;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_284;
/*     */ import net.minecraft.class_286;
/*     */ import net.minecraft.class_287;
/*     */ import net.minecraft.class_289;
/*     */ import net.minecraft.class_290;
/*     */ import net.minecraft.class_293;
/*     */ import net.minecraft.class_3532;
/*     */ import net.minecraft.class_4587;
/*     */ import net.minecraft.class_5498;
/*     */ import net.minecraft.class_572;
/*     */ import net.minecraft.class_591;
/*     */ import net.minecraft.class_5944;
/*     */ import net.minecraft.class_630;
/*     */ import net.minecraft.class_742;
/*     */ import net.minecraft.class_897;
/*     */ import org.joml.Matrix4f;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import shame.astra.api.events.implement.Event3DRender;
/*     */ import shame.astra.api.utils.color.ColorUtils;
/*     */ import shame.astra.api.utils.render.ShaderUtils;
/*     */ import shame.astra.astra;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*     */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*     */ import shame.astra.client.modules.settings.implement.ListSetting;
/*     */ import shame.astra.mixin.LivingEntityRendererAccessor;
/*     */ 
/*     */ public class Chams extends Module {
/*  41 */   public static final Chams INSTANCE = new Chams();
/*     */   
/*     */   public static final String TARGET_PLAYERS = "Игроков";
/*     */   
/*     */   public static final String TARGET_FRIENDS = "Друзей";
/*     */   public static final String TARGET_SELF = "Себя";
/*     */   private static final int DEFAULT_FILL_ALPHA = 130;
/*     */   private static final float DEFAULT_LINE_WIDTH = 0.5F;
/*     */   private static final float CLIENT_FILL_SATURATION = 1.18F;
/*     */   private static final float CLIENT_FILL_BRIGHTNESS = 1.12F;
/*     */   private static final float CLIENT_OUTLINE_SATURATION = 1.12F;
/*     */   private static final float CLIENT_OUTLINE_BRIGHTNESS = 1.08F;
/*     */   private static final float MIN_PULSE_ALPHA = 0.65F;
/*     */   private static final float PULSE_SWING = 0.35F;
/*  55 */   private static final int FRIEND_FILL_COLOR = (new Color(85, 255, 85, 60)).getRGB();
/*  56 */   private static final int FRIEND_OUTLINE_COLOR = (new Color(100, 255, 100, 255)).getRGB();
/*     */   
/*     */   private static final long OUTLINE_RETRY_DELAY_MS = 3000L;
/*  59 */   private final ListSetting rendering = new ListSetting("Отображать", new BooleanSetting[] { new BooleanSetting("Игроков", true), new BooleanSetting("Друзей", true), new BooleanSetting("Себя", false) });
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  64 */   private final BooleanSetting waves = new BooleanSetting("Волны", true);
/*     */   
/*     */   private final FloatSetting waveSpeedX;
/*     */   
/*     */   private final FloatSetting waveSpeedY;
/*     */   
/*     */   private final FloatSetting waveScale;
/*     */   
/*     */   private final FloatSetting waveDensity;
/*     */   
/*     */   private final FloatSetting waveGlow;
/*     */   
/*     */   private final BooleanSetting glow;
/*     */   
/*     */   private final FloatSetting glowIntensity;
/*     */   
/*     */   private final FloatSetting glowLayers;
/*     */   
/*     */   private final BooleanSetting pulse;
/*     */   private final FloatSetting pulseSpeed;
/*     */   private final BooleanSetting hideOriginal;
/*     */   private final BooleanSetting hideItemsAndCape;
/*     */   private final long startTime;
/*     */   private boolean outlineAssistReady;
/*     */   private long nextOutlineRetryAt;
/*     */   
/*     */   private Chams() {
/*  91 */     super("Chams", "Чамсы по модели игрока", Module.ModuleCategory.RENDER); Objects.requireNonNull(this.waves); this.waveSpeedX = (new FloatSetting("Скорость X", 0.22F, 0.0F, 1.5F, 0.01F)).visible(this.waves::isState); Objects.requireNonNull(this.waves); this.waveSpeedY = (new FloatSetting("Скорость Y", 0.15F, 0.0F, 1.5F, 0.01F)).visible(this.waves::isState); Objects.requireNonNull(this.waves); this.waveScale = (new FloatSetting("Размер волн", 1.35F, 0.2F, 4.0F, 0.05F)).visible(this.waves::isState); Objects.requireNonNull(this.waves); this.waveDensity = (new FloatSetting("Плотность волн", 1.15F, 0.5F, 3.0F, 0.05F)).visible(this.waves::isState); Objects.requireNonNull(this.waves); this.waveGlow = (new FloatSetting("Сила волн", 1.0F, 0.2F, 3.0F, 0.05F)).visible(this.waves::isState); this.glow = new BooleanSetting("Свечение", true); Objects.requireNonNull(this.glow); this.glowIntensity = (new FloatSetting("Сила свечения", 2.0F, 1.0F, 5.0F, 0.1F)).visible(this.glow::isState); Objects.requireNonNull(this.glow); this.glowLayers = (new FloatSetting("Слои свечения", 3.0F, 1.0F, 6.0F, 1.0F)).visible(this.glow::isState); this.pulse = new BooleanSetting("Пульсирование", false); Objects.requireNonNull(this.pulse); this.pulseSpeed = (new FloatSetting("Скорость пульсации", 2.0F, 0.5F, 5.0F, 0.1F)).visible(this.pulse::isState); this.hideOriginal = new BooleanSetting("Скрыть оригинал", false); this.hideItemsAndCape = new BooleanSetting("Скрывать предметы и плащ", false); this.startTime = System.currentTimeMillis();
/*  92 */     addSettings(new Setting[] { (Setting)this.rendering, (Setting)this.waves, (Setting)this.waveSpeedX, (Setting)this.waveSpeedY, (Setting)this.waveScale, (Setting)this.waveDensity, (Setting)this.waveGlow, (Setting)this.glow, (Setting)this.glowIntensity, (Setting)this.glowLayers, (Setting)this.pulse, (Setting)this.pulseSpeed, (Setting)this.hideOriginal, (Setting)this.hideItemsAndCape });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onEnable() {
/* 112 */     super.onEnable();
/* 113 */     this.outlineAssistReady = false;
/* 114 */     this.nextOutlineRetryAt = 0L;
/* 115 */     tryEnsureOutlineProcessor();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 120 */     this.outlineAssistReady = false;
/* 121 */     this.nextOutlineRetryAt = 0L;
/* 122 */     super.onDisable();
/*     */   }
/*     */   
/*     */   @EventLink(priority = 100)
/*     */   public void onRender3D(Event3DRender event) {
/* 127 */     if (!isEnable() || mc.field_1687 == null || mc.field_1724 == null) {
/*     */       return;
/*     */     }
/*     */     
/* 131 */     if (hasOutlineAssistTargets() && !this.outlineAssistReady && System.currentTimeMillis() >= this.nextOutlineRetryAt) {
/* 132 */       tryEnsureOutlineProcessor();
/*     */     }
/*     */     
/* 135 */     RenderSystem.enableBlend();
/* 136 */     RenderSystem.defaultBlendFunc();
/* 137 */     RenderSystem.disableCull();
/* 138 */     RenderSystem.disableDepthTest();
/* 139 */     RenderSystem.depthMask(false);
/*     */     
/* 141 */     for (class_1657 player : mc.field_1687.method_18456()) {
/* 142 */       if (!affects(player)) {
/*     */         continue;
/*     */       }
/* 145 */       if (player == mc.field_1724 && mc.field_1690.method_31044() == class_5498.field_26664) {
/*     */         continue;
/*     */       }
/* 148 */       renderManualPlayer(event, player);
/*     */     } 
/*     */     
/* 151 */     RenderSystem.depthMask(true);
/* 152 */     RenderSystem.enableDepthTest();
/* 153 */     RenderSystem.enableCull();
/* 154 */     RenderSystem.disableBlend();
/* 155 */     RenderSystem.lineWidth(1.0F);
/*     */   } private void renderManualPlayer(Event3DRender event, class_1657 player) {
/*     */     class_742 clientPlayer;
/*     */     class_1007 renderer;
/* 159 */     if (player instanceof class_742) { clientPlayer = (class_742)player; }
/*     */     else
/*     */     { return; }
/*     */     
/* 163 */     class_897<?, ?> rawRenderer = mc.method_1561().method_3953((class_1297)player);
/* 164 */     if (rawRenderer instanceof class_1007) { renderer = (class_1007)rawRenderer; }
/*     */     else
/*     */     { return; }
/*     */     
/* 168 */     class_10055 state = renderer.method_62608();
/* 169 */     renderer.method_62604(clientPlayer, state, event.getTickDelta());
/* 170 */     class_591 model = (class_591)renderer.method_4038();
/* 171 */     model.method_62110(state);
/*     */     
/* 173 */     class_4587 matrices = event.getMatrices();
/* 174 */     matrices.method_22903();
/* 175 */     setupModelMatrix(matrices, state, renderer, event.getCamera().method_19326(), player, event.getTickDelta());
/*     */     
/* 177 */     int fillColor = resolveFillColor(player);
/* 178 */     int outlineColor = resolveOutlineColor(player);
/* 179 */     renderShaderFillModel(matrices, (class_572<?>)model, 0.0F, fillColor);
/* 180 */     renderOutlineModel(matrices, (class_572<?>)model, 0.0F, outlineColor);
/*     */     
/* 182 */     matrices.method_22909();
/*     */   }
/*     */   
/*     */   private void setupModelMatrix(class_4587 matrices, class_10055 state, class_1007 renderer, class_243 cameraPos, class_1657 player, float tickDelta) {
/* 186 */     class_243 pos = player.method_30950(tickDelta);
/* 187 */     double x = pos.field_1352 - cameraPos.field_1352;
/* 188 */     double y = pos.field_1351 - cameraPos.field_1351;
/* 189 */     double z = pos.field_1350 - cameraPos.field_1350;
/* 190 */     matrices.method_22904(x, y, z);
/*     */     
/* 192 */     if (state.field_53463 != null) {
/* 193 */       float eyeOffset = state.field_53331 - 0.1F;
/* 194 */       matrices.method_46416(-state.field_53463.method_10148() * eyeOffset, 0.0F, -state.field_53463.method_10165() * eyeOffset);
/*     */     } 
/*     */     
/* 197 */     float baseScale = state.field_53453;
/* 198 */     matrices.method_22905(baseScale, baseScale, baseScale);
/* 199 */     LivingEntityRendererAccessor accessor = (LivingEntityRendererAccessor)renderer;
/* 200 */     accessor.astra$setupTransforms((class_10042)state, matrices, state.field_53446, baseScale);
/* 201 */     matrices.method_22905(-1.0F, -1.0F, 1.0F);
/* 202 */     accessor.astra$scale((class_10042)state, matrices);
/* 203 */     matrices.method_46416(0.0F, -1.501F, 0.0F);
/*     */   }
/*     */   
/*     */   private void renderShaderFillModel(class_4587 matrices, class_572<?> model, float expand, int color) {
/* 207 */     if (!this.waves.isState()) {
/* 208 */       renderSolidFillModel(matrices, model, expand, color);
/*     */       
/*     */       return;
/*     */     } 
/* 212 */     class_5944 shader = mc.method_62887().method_62947(ShaderUtils.chamsFill);
/* 213 */     if (shader == null) {
/*     */       return;
/*     */     }
/*     */     
/* 217 */     RenderSystem.setShader(ShaderUtils.chamsFill);
/* 218 */     setUniform(shader, "time", this.waves.isState() ? ((float)(System.currentTimeMillis() - this.startTime) / 1000.0F) : 0.0F);
/* 219 */     setUniform(shader, "speedX", this.waveSpeedX.get());
/* 220 */     setUniform(shader, "speedY", this.waveSpeedY.get());
/* 221 */     setUniform(shader, "scale", this.waveScale.get());
/* 222 */     setUniform(shader, "density", this.waveDensity.get());
/* 223 */     setUniform(shader, "glowStrength", this.waveGlow.get());
/*     */     
/* 225 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
/* 226 */     class_630 root = model.method_63512();
/* 227 */     renderFillPart(matrices, buffer, root, model.field_3398, -4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, expand, color);
/* 228 */     renderFillPart(matrices, buffer, root, model.field_3391, -4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, expand, color);
/* 229 */     renderFillPart(matrices, buffer, root, model.field_3401, -3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, expand, color);
/* 230 */     renderFillPart(matrices, buffer, root, model.field_27433, -1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, expand, color);
/* 231 */     renderFillPart(matrices, buffer, root, model.field_3392, -2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, expand, color);
/* 232 */     renderFillPart(matrices, buffer, root, model.field_3397, -2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, expand, color);
/* 233 */     class_286.method_43433(buffer.method_60800());
/*     */   }
/*     */   
/*     */   private void renderSolidFillModel(class_4587 matrices, class_572<?> model, float expand, int color) {
/* 237 */     RenderSystem.setShader(class_10142.field_53876);
/* 238 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1576);
/* 239 */     class_630 root = model.method_63512();
/* 240 */     renderSolidFillPart(matrices, buffer, root, model.field_3398, -4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, expand, color);
/* 241 */     renderSolidFillPart(matrices, buffer, root, model.field_3391, -4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, expand, color);
/* 242 */     renderSolidFillPart(matrices, buffer, root, model.field_3401, -3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, expand, color);
/* 243 */     renderSolidFillPart(matrices, buffer, root, model.field_27433, -1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, expand, color);
/* 244 */     renderSolidFillPart(matrices, buffer, root, model.field_3392, -2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, expand, color);
/* 245 */     renderSolidFillPart(matrices, buffer, root, model.field_3397, -2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, expand, color);
/* 246 */     class_286.method_43433(buffer.method_60800());
/*     */   }
/*     */ 
/*     */   
/*     */   private void renderSolidFillPart(class_4587 baseStack, class_287 buffer, class_630 root, class_630 part, float offX, float offY, float offZ, float width, float height, float depth, float expand, int color) {
/* 251 */     baseStack.method_22903();
/* 252 */     root.method_22703(baseStack);
/* 253 */     part.method_22703(baseStack);
/*     */     
/* 255 */     Matrix4f matrix = baseStack.method_23760().method_23761();
/* 256 */     float scale = 0.0625F;
/* 257 */     float expandScale = expand * scale;
/*     */     
/* 259 */     float minX = offX * scale - expandScale;
/* 260 */     float minY = offY * scale - expandScale;
/* 261 */     float minZ = offZ * scale - expandScale;
/* 262 */     float maxX = (offX + width) * scale + expandScale;
/* 263 */     float maxY = (offY + height) * scale + expandScale;
/* 264 */     float maxZ = (offZ + depth) * scale + expandScale;
/*     */     
/* 266 */     addSolidQuad(buffer, matrix, minX, maxY, minZ, minX, maxY, maxZ, maxX, maxY, maxZ, maxX, maxY, minZ, color);
/* 267 */     addSolidQuad(buffer, matrix, minX, minY, maxZ, minX, minY, minZ, maxX, minY, minZ, maxX, minY, maxZ, color);
/* 268 */     addSolidQuad(buffer, matrix, minX, minY, minZ, minX, maxY, minZ, maxX, maxY, minZ, maxX, minY, minZ, color);
/* 269 */     addSolidQuad(buffer, matrix, maxX, minY, maxZ, maxX, maxY, maxZ, minX, maxY, maxZ, minX, minY, maxZ, color);
/* 270 */     addSolidQuad(buffer, matrix, minX, minY, maxZ, minX, maxY, maxZ, minX, maxY, minZ, minX, minY, minZ, color);
/* 271 */     addSolidQuad(buffer, matrix, maxX, minY, minZ, maxX, maxY, minZ, maxX, maxY, maxZ, maxX, minY, maxZ, color);
/*     */     
/* 273 */     baseStack.method_22909();
/*     */   }
/*     */ 
/*     */   
/*     */   private void renderFillPart(class_4587 baseStack, class_287 buffer, class_630 root, class_630 part, float offX, float offY, float offZ, float width, float height, float depth, float expand, int color) {
/* 278 */     baseStack.method_22903();
/* 279 */     root.method_22703(baseStack);
/* 280 */     part.method_22703(baseStack);
/*     */     
/* 282 */     Matrix4f matrix = baseStack.method_23760().method_23761();
/* 283 */     float scale = 0.0625F;
/* 284 */     float expandScale = expand * scale;
/*     */     
/* 286 */     float minX = offX * scale - expandScale;
/* 287 */     float minY = offY * scale - expandScale;
/* 288 */     float minZ = offZ * scale - expandScale;
/* 289 */     float maxX = (offX + width) * scale + expandScale;
/* 290 */     float maxY = (offY + height) * scale + expandScale;
/* 291 */     float maxZ = (offZ + depth) * scale + expandScale;
/*     */     
/* 293 */     addQuad(buffer, matrix, minX, maxY, minZ, minX, maxY, maxZ, maxX, maxY, maxZ, maxX, maxY, minZ, color);
/* 294 */     addQuad(buffer, matrix, minX, minY, maxZ, minX, minY, minZ, maxX, minY, minZ, maxX, minY, maxZ, color);
/* 295 */     addQuad(buffer, matrix, minX, minY, minZ, minX, maxY, minZ, maxX, maxY, minZ, maxX, minY, minZ, color);
/* 296 */     addQuad(buffer, matrix, maxX, minY, maxZ, maxX, maxY, maxZ, minX, maxY, maxZ, minX, minY, maxZ, color);
/* 297 */     addQuad(buffer, matrix, minX, minY, maxZ, minX, maxY, maxZ, minX, maxY, minZ, minX, minY, minZ, color);
/* 298 */     addQuad(buffer, matrix, maxX, minY, minZ, maxX, maxY, minZ, maxX, maxY, maxZ, maxX, minY, maxZ, color);
/*     */     
/* 300 */     baseStack.method_22909();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addQuad(class_287 buffer, Matrix4f matrix, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, int color) {
/* 309 */     int r = ColorUtils.red(color);
/* 310 */     int g = ColorUtils.green(color);
/* 311 */     int b = ColorUtils.blue(color);
/* 312 */     int a = ColorUtils.alpha(color);
/*     */     
/* 314 */     float u1 = waveU(x1, y1, z1);
/* 315 */     float v1 = waveV(x1, y1, z1);
/* 316 */     float u2 = waveU(x2, y2, z2);
/* 317 */     float v2 = waveV(x2, y2, z2);
/* 318 */     float u3 = waveU(x3, y3, z3);
/* 319 */     float v3 = waveV(x3, y3, z3);
/* 320 */     float u4 = waveU(x4, y4, z4);
/* 321 */     float v4 = waveV(x4, y4, z4);
/*     */     
/* 323 */     buffer.method_22918(matrix, x1, y1, z1).method_22913(u1, v1).method_1336(r, g, b, a);
/* 324 */     buffer.method_22918(matrix, x2, y2, z2).method_22913(u2, v2).method_1336(r, g, b, a);
/* 325 */     buffer.method_22918(matrix, x3, y3, z3).method_22913(u3, v3).method_1336(r, g, b, a);
/* 326 */     buffer.method_22918(matrix, x4, y4, z4).method_22913(u4, v4).method_1336(r, g, b, a);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addSolidQuad(class_287 buffer, Matrix4f matrix, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, int color) {
/* 335 */     int r = ColorUtils.red(color);
/* 336 */     int g = ColorUtils.green(color);
/* 337 */     int b = ColorUtils.blue(color);
/* 338 */     int a = ColorUtils.alpha(color);
/*     */     
/* 340 */     buffer.method_22918(matrix, x1, y1, z1).method_1336(r, g, b, a);
/* 341 */     buffer.method_22918(matrix, x2, y2, z2).method_1336(r, g, b, a);
/* 342 */     buffer.method_22918(matrix, x3, y3, z3).method_1336(r, g, b, a);
/* 343 */     buffer.method_22918(matrix, x4, y4, z4).method_1336(r, g, b, a);
/*     */   }
/*     */   
/*     */   private float waveU(float x, float y, float z) {
/* 347 */     return x * 1.15F + z * 0.72F;
/*     */   }
/*     */   
/*     */   private float waveV(float x, float y, float z) {
/* 351 */     return y * 1.05F - z * 0.38F + x * 0.18F;
/*     */   }
/*     */   
/*     */   private void renderOutlineModel(class_4587 matrices, class_572<?> model, float expand, int color) {
/* 355 */     RenderSystem.setShader(class_10142.field_53876);
/* 356 */     GL11.glEnable(2848);
/* 357 */     GL11.glHint(3154, 4354);
/* 358 */     RenderSystem.lineWidth(0.5F);
/*     */     
/* 360 */     if (this.glow.isState()) {
/* 361 */       RenderSystem.blendFuncSeparate(770, 1, 1, 0);
/* 362 */       int layers = Math.max(1, Math.round(this.glowLayers.get()));
/* 363 */       float intensity = Math.max(1.0F, this.glowIntensity.get());
/* 364 */       for (int index = layers; index >= 1; index--) {
/* 365 */         float layerExpand = expand + index * 0.5F * intensity;
/* 366 */         float alphaMul = 1.0F / (index + 1) * 0.7F;
/* 367 */         int alpha = Math.max(1, Math.min(255, Math.round(ColorUtils.alpha(color) * alphaMul)));
/* 368 */         drawOutlineParts(matrices, model, layerExpand, withAlpha(color, alpha));
/*     */       } 
/*     */     } 
/*     */     
/* 372 */     RenderSystem.defaultBlendFunc();
/* 373 */     drawOutlineParts(matrices, model, expand, color);
/* 374 */     GL11.glDisable(2848);
/*     */   }
/*     */   
/*     */   private void drawOutlineParts(class_4587 matrices, class_572<?> model, float expand, int color) {
/* 378 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_29344, class_290.field_1576);
/* 379 */     class_630 root = model.method_63512();
/* 380 */     renderPartOutlineLines(matrices, buffer, root, model.field_3398, -4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, expand, color);
/* 381 */     renderPartOutlineLines(matrices, buffer, root, model.field_3391, -4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, expand, color);
/* 382 */     renderPartOutlineLines(matrices, buffer, root, model.field_3401, -3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, expand, color);
/* 383 */     renderPartOutlineLines(matrices, buffer, root, model.field_27433, -1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, expand, color);
/* 384 */     renderPartOutlineLines(matrices, buffer, root, model.field_3392, -2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, expand, color);
/* 385 */     renderPartOutlineLines(matrices, buffer, root, model.field_3397, -2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, expand, color);
/* 386 */     class_286.method_43433(buffer.method_60800());
/*     */   }
/*     */ 
/*     */   
/*     */   private void renderPartOutlineLines(class_4587 baseStack, class_287 buffer, class_630 root, class_630 part, float offX, float offY, float offZ, float width, float height, float depth, float expand, int color) {
/* 391 */     baseStack.method_22903();
/* 392 */     root.method_22703(baseStack);
/* 393 */     part.method_22703(baseStack);
/*     */     
/* 395 */     float scale = 0.0625F;
/* 396 */     float expandScale = expand * scale;
/* 397 */     float minX = offX * scale - expandScale;
/* 398 */     float minY = offY * scale - expandScale;
/* 399 */     float minZ = offZ * scale - expandScale;
/* 400 */     float maxX = (offX + width) * scale + expandScale;
/* 401 */     float maxY = (offY + height) * scale + expandScale;
/* 402 */     float maxZ = (offZ + depth) * scale + expandScale;
/* 403 */     Matrix4f matrix = baseStack.method_23760().method_23761();
/*     */     
/* 405 */     addLine(buffer, matrix, minX, minY, minZ, maxX, minY, minZ, color);
/* 406 */     addLine(buffer, matrix, maxX, minY, minZ, maxX, minY, maxZ, color);
/* 407 */     addLine(buffer, matrix, maxX, minY, maxZ, minX, minY, maxZ, color);
/* 408 */     addLine(buffer, matrix, minX, minY, maxZ, minX, minY, minZ, color);
/*     */     
/* 410 */     addLine(buffer, matrix, minX, maxY, minZ, maxX, maxY, minZ, color);
/* 411 */     addLine(buffer, matrix, maxX, maxY, minZ, maxX, maxY, maxZ, color);
/* 412 */     addLine(buffer, matrix, maxX, maxY, maxZ, minX, maxY, maxZ, color);
/* 413 */     addLine(buffer, matrix, minX, maxY, maxZ, minX, maxY, minZ, color);
/*     */     
/* 415 */     addLine(buffer, matrix, minX, minY, minZ, minX, maxY, minZ, color);
/* 416 */     addLine(buffer, matrix, maxX, minY, minZ, maxX, maxY, minZ, color);
/* 417 */     addLine(buffer, matrix, maxX, minY, maxZ, maxX, maxY, maxZ, color);
/* 418 */     addLine(buffer, matrix, minX, minY, maxZ, minX, maxY, maxZ, color);
/*     */     
/* 420 */     baseStack.method_22909();
/*     */   }
/*     */   
/*     */   private void addLine(class_287 buffer, Matrix4f matrix, float x1, float y1, float z1, float x2, float y2, float z2, int color) {
/* 424 */     int r = ColorUtils.red(color);
/* 425 */     int g = ColorUtils.green(color);
/* 426 */     int b = ColorUtils.blue(color);
/* 427 */     int a = ColorUtils.alpha(color);
/*     */     
/* 429 */     buffer.method_22918(matrix, x1, y1, z1).method_1336(r, g, b, a);
/* 430 */     buffer.method_22918(matrix, x2, y2, z2).method_1336(r, g, b, a);
/*     */   }
/*     */   
/*     */   private void setUniform(class_5944 shader, String name, float value) {
/* 434 */     class_284 uniform = shader.method_34582(name);
/* 435 */     if (uniform != null) {
/* 436 */       uniform.method_1251(value);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean affects(class_1657 player) {
/* 441 */     if (!isEnable() || player == null || !player.method_5805()) {
/* 442 */       return false;
/*     */     }
/* 444 */     if (player == mc.field_1724) {
/* 445 */       return (this.rendering.is("Себя") && mc.field_1690.method_31044() != class_5498.field_26664);
/*     */     }
/* 447 */     if (isFriend(player)) {
/* 448 */       return this.rendering.is("Друзей");
/*     */     }
/* 450 */     return this.rendering.is("Игроков");
/*     */   }
/*     */   
/*     */   public boolean shouldHideBaseModel(class_1657 player) {
/* 454 */     return (this.hideOriginal.isState() && affects(player));
/*     */   }
/*     */   
/*     */   public boolean shouldHideItemsAndCape(class_1657 player) {
/* 458 */     return (this.hideItemsAndCape.isState() && affects(player));
/*     */   }
/*     */   
/*     */   public boolean shouldUseOutlineAssist(class_1657 player) {
/* 462 */     return affects(player);
/*     */   }
/*     */   
/*     */   public boolean shouldHideOutlineFramebuffer() {
/* 466 */     return (isEnable() && hasOutlineAssistTargets());
/*     */   }
/*     */   
/*     */   public int resolveFillColor(class_1657 player) {
/* 470 */     return applyPulse(baseFillColor(player));
/*     */   }
/*     */   
/*     */   public int resolveOutlineColor(class_1657 player) {
/* 474 */     return applyPulse(baseOutlineColor(player));
/*     */   }
/*     */   
/*     */   private int baseFillColor(class_1657 player) {
/* 478 */     if (isFriend(player)) {
/* 479 */       return FRIEND_FILL_COLOR;
/*     */     }
/* 481 */     return vividWithAlpha(ColorUtils.getThemeColor(), 1.18F, 1.12F, 130);
/*     */   }
/*     */   
/*     */   private int baseOutlineColor(class_1657 player) {
/* 485 */     if (isFriend(player)) {
/* 486 */       return FRIEND_OUTLINE_COLOR;
/*     */     }
/* 488 */     return vividWithAlpha(ColorUtils.getThemeColor(), 1.12F, 1.08F, 255);
/*     */   }
/*     */   
/*     */   private int applyPulse(int color) {
/* 492 */     if (!this.pulse.isState()) {
/* 493 */       return color;
/*     */     }
/* 495 */     float elapsedSeconds = (float)(System.currentTimeMillis() - this.startTime) / 1000.0F;
/* 496 */     float pulseValue = (float)((Math.sin((elapsedSeconds * this.pulseSpeed.get()) * Math.PI) + 1.0D) * 0.5D);
/* 497 */     float alphaMul = 0.65F + 0.35F * pulseValue;
/* 498 */     return ColorUtils.multAlpha(color, alphaMul);
/*     */   }
/*     */   
/*     */   private int vividWithAlpha(int color, float saturationBoost, float brightnessBoost, int alpha) {
/* 502 */     float[] hsb = Color.RGBtoHSB(ColorUtils.red(color), ColorUtils.green(color), ColorUtils.blue(color), null);
/* 503 */     float saturation = class_3532.method_15363(hsb[1] * saturationBoost, 0.0F, 1.0F);
/* 504 */     float brightness = class_3532.method_15363(Math.max(hsb[2], 0.8F) * brightnessBoost, 0.0F, 1.0F);
/* 505 */     int rgb = Color.HSBtoRGB(hsb[0], saturation, brightness);
/* 506 */     return ColorUtils.rgba(ColorUtils.red(rgb), ColorUtils.green(rgb), ColorUtils.blue(rgb), alpha);
/*     */   }
/*     */   
/*     */   private int withAlpha(int color, int alpha) {
/* 510 */     return color & 0xFFFFFF | (alpha & 0xFF) << 24;
/*     */   }
/*     */   
/*     */   private boolean isFriend(class_1657 player) {
/* 514 */     return (astra.INSTANCE != null && astra.INSTANCE.friendStorage != null && astra.INSTANCE.friendStorage
/*     */       
/* 516 */       .isFriend(player.method_5477().getString()));
/*     */   }
/*     */   
/*     */   private boolean hasOutlineAssistTargets() {
/* 520 */     if (!isEnable() || mc.field_1687 == null || mc.field_1724 == null) {
/* 521 */       return false;
/*     */     }
/*     */     
/* 524 */     for (class_1657 player : mc.field_1687.method_18456()) {
/* 525 */       if (shouldUseOutlineAssist(player)) {
/* 526 */         return true;
/*     */       }
/*     */     } 
/* 529 */     return false;
/*     */   }
/*     */   
/*     */   private boolean tryEnsureOutlineProcessor() {
/* 533 */     if (mc.field_1769 == null) {
/* 534 */       this.outlineAssistReady = false;
/* 535 */       return false;
/*     */     } 
/*     */     
/* 538 */     if (mc.field_1769.method_22990() != null) {
/* 539 */       this.outlineAssistReady = true;
/* 540 */       return true;
/*     */     } 
/*     */     
/*     */     try {
/* 544 */       mc.field_1769.method_3296();
/* 545 */     } catch (Exception exception) {}
/*     */ 
/*     */     
/* 548 */     this.outlineAssistReady = (mc.field_1769.method_22990() != null);
/* 549 */     if (!this.outlineAssistReady) {
/* 550 */       this.nextOutlineRetryAt = System.currentTimeMillis() + 3000L;
/*     */     }
/* 552 */     return this.outlineAssistReady;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\Chams.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */