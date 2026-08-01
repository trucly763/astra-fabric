/*     */ package shame.astra.client.modules.impl.render;
/*     */ import com.mojang.blaze3d.platform.GlStateManager;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import net.minecraft.class_1297;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_286;
/*     */ import net.minecraft.class_287;
/*     */ import net.minecraft.class_289;
/*     */ import net.minecraft.class_293;
/*     */ import net.minecraft.class_2960;
/*     */ import net.minecraft.class_3532;
/*     */ import net.minecraft.class_4587;
/*     */ import net.minecraft.class_7833;
/*     */ import org.joml.Matrix4f;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.Event3DRender;
/*     */ import shame.astra.api.events.implement.EventAttackEntity;
/*     */ import shame.astra.api.utils.color.ColorUtils;
/*     */ import shame.astra.client.modules.Module;
/*     */ 
/*     */ public class KillEffect extends Module {
/*  25 */   public static KillEffect INSTANCE = new KillEffect();
/*  26 */   private static final class_2960 GLOW_TEX = class_2960.method_60655("astra", "textures/particle/bloom.png");
/*     */   private static final float DURATION = 1.5F;
/*     */   private static final float HEIGHT = 4.0F;
/*     */   private static final float MAX_RADIUS = 1.0F;
/*     */   private static final int SLICES = 40;
/*  31 */   private final Map<class_1297, class_243> trackedEntities = new IdentityHashMap<>();
/*  32 */   private final List<ActiveEffect> effects = new ArrayList<>();
/*     */   
/*     */   public KillEffect() {
/*  35 */     super("KillEffect", "Эффект при исчезновении цели", Module.ModuleCategory.RENDER);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  40 */     this.trackedEntities.clear();
/*  41 */     this.effects.clear();
/*  42 */     super.onDisable();
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onAttack(EventAttackEntity event) {
/*  47 */     if (mc.field_1724 == null || mc.field_1687 == null)
/*  48 */       return;  class_1297 target = event.getTarget();
/*  49 */     if (target instanceof net.minecraft.class_1309 && target != mc.field_1724) {
/*  50 */       this.trackedEntities.put(target, target.method_19538());
/*     */     }
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onRender3D(Event3DRender event) {
/*  56 */     if (mc.field_1687 == null || mc.field_1724 == null)
/*  57 */       return;  long currentTime = System.currentTimeMillis();
/*  58 */     Iterator<Map.Entry<class_1297, class_243>> trackIterator = this.trackedEntities.entrySet().iterator();
/*  59 */     while (trackIterator.hasNext()) {
/*  60 */       Map.Entry<class_1297, class_243> entry = trackIterator.next();
/*  61 */       class_1297 entity = entry.getKey();
/*  62 */       if (entity.method_31481() || !entity.method_5805()) {
/*  63 */         this.effects.add(new ActiveEffect(entry.getValue(), currentTime));
/*  64 */         trackIterator.remove(); continue;
/*     */       } 
/*  66 */       entry.setValue(entity.method_19538());
/*     */     } 
/*     */     
/*  69 */     RenderSystem.enableBlend();
/*  70 */     RenderSystem.disableDepthTest();
/*  71 */     RenderSystem.depthMask(false);
/*  72 */     RenderSystem.disableCull();
/*  73 */     RenderSystem.blendFuncSeparate(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE, GlStateManager.class_4535.ZERO, GlStateManager.class_4534.ONE);
/*  74 */     RenderSystem.setShader(class_10142.field_53880);
/*  75 */     RenderSystem.setShaderTexture(0, GLOW_TEX);
/*  76 */     Iterator<ActiveEffect> effectIterator = this.effects.iterator();
/*  77 */     while (effectIterator.hasNext()) {
/*  78 */       ActiveEffect effect = effectIterator.next();
/*  79 */       float progress = (float)(currentTime - effect.startTime) / 1500.0F;
/*  80 */       if (progress >= 1.0F) {
/*  81 */         effectIterator.remove();
/*     */         continue;
/*     */       } 
/*  84 */       renderEffect(event.getMatrices(), effect, mc.field_1773.method_19418().method_19326(), progress);
/*     */     } 
/*  86 */     RenderSystem.enableCull();
/*  87 */     RenderSystem.depthMask(true);
/*  88 */     RenderSystem.enableDepthTest();
/*  89 */     RenderSystem.defaultBlendFunc();
/*  90 */     RenderSystem.disableBlend();
/*     */   }
/*     */   
/*     */   private void renderEffect(class_4587 matrices, ActiveEffect effect, class_243 cameraPos, float progress) {
/*  94 */     int color = ColorUtils.getThemeColor();
/*  95 */     float r = (color >> 16 & 0xFF) / 255.0F;
/*  96 */     float g = (color >> 8 & 0xFF) / 255.0F;
/*  97 */     float b = (color & 0xFF) / 255.0F;
/*  98 */     float globalAlpha = (progress < 0.15F) ? (progress / 0.15F) : ((progress > 0.75F) ? ((1.0F - progress) / 0.25F) : 1.0F);
/*  99 */     float sliceHeight = 0.1F; int i;
/* 100 */     for (i = 0; i < 40; i++) {
/* 101 */       float t = i / 40.0F;
/* 102 */       float y = t * 4.0F;
/* 103 */       float radius = 1.0F * class_3532.method_15374((float)(Math.PI * t));
/* 104 */       float sliceAlpha = (1.0F - Math.abs(2.0F * t - 1.0F) * 0.25F) * globalAlpha;
/* 105 */       class_243 pos = effect.position.method_1031(0.0D, y, 0.0D);
/* 106 */       renderGlow(matrices, cameraPos, pos, radius * 2.1F, r, g, b, sliceAlpha * 0.22F);
/* 107 */       renderGlow(matrices, cameraPos, pos, radius * 1.15F, r, g, b, sliceAlpha * 0.48F);
/* 108 */       renderGlow(matrices, cameraPos, pos, radius * 0.55F, r, g, b, sliceAlpha * 0.85F);
/*     */     } 
/* 110 */     for (i = 0; i < 10; i++) {
/* 111 */       float t = i / 10.0F;
/* 112 */       float spread = 1.0F - t;
/* 113 */       float bottomRadius = 3.6F * spread;
/* 114 */       float bottomAlpha = spread * spread * globalAlpha * 0.38F;
/* 115 */       class_243 bPos = effect.position.method_1031(0.0D, (t * 0.45F), 0.0D);
/* 116 */       renderGlow(matrices, cameraPos, bPos, bottomRadius, r, g, b, bottomAlpha);
/* 117 */       renderGlow(matrices, cameraPos, bPos, bottomRadius * 0.35F, r, g, b, bottomAlpha * 1.7F);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void renderGlow(class_4587 matrices, class_243 cameraPos, class_243 position, float size, float r, float g, float b, float a) {
/* 122 */     if (a <= 0.01F)
/* 123 */       return;  matrices.method_22903();
/* 124 */     matrices.method_22904(position.field_1352 - cameraPos.field_1352, position.field_1351 - cameraPos.field_1351, position.field_1350 - cameraPos.field_1350);
/* 125 */     matrices.method_22907(class_7833.field_40716.rotationDegrees(-mc.field_1773.method_19418().method_19330()));
/* 126 */     matrices.method_22907(class_7833.field_40714.rotationDegrees(mc.field_1773.method_19418().method_19329()));
/* 127 */     Matrix4f matrix = matrices.method_23760().method_23761();
/* 128 */     float half = size * 0.5F;
/* 129 */     int rInt = Math.min(255, (int)(r * 255.0F));
/* 130 */     int gInt = Math.min(255, (int)(g * 255.0F));
/* 131 */     int bInt = Math.min(255, (int)(b * 255.0F));
/* 132 */     int aInt = Math.min(255, (int)(a * 255.0F));
/* 133 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
/* 134 */     buffer.method_22918(matrix, -half, -half, 0.0F).method_22913(0.0F, 1.0F).method_1336(rInt, gInt, bInt, aInt);
/* 135 */     buffer.method_22918(matrix, -half, half, 0.0F).method_22913(0.0F, 0.0F).method_1336(rInt, gInt, bInt, aInt);
/* 136 */     buffer.method_22918(matrix, half, half, 0.0F).method_22913(1.0F, 0.0F).method_1336(rInt, gInt, bInt, aInt);
/* 137 */     buffer.method_22918(matrix, half, -half, 0.0F).method_22913(1.0F, 1.0F).method_1336(rInt, gInt, bInt, aInt);
/* 138 */     class_286.method_43433(buffer.method_60800());
/* 139 */     matrices.method_22909();
/*     */   }
/*     */   
/*     */   private static class ActiveEffect { final class_243 position;
/*     */     final long startTime;
/*     */     
/*     */     ActiveEffect(class_243 position, long startTime) {
/* 146 */       this.position = position;
/* 147 */       this.startTime = startTime;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\KillEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */