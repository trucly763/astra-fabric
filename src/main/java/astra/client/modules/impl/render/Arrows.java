/*     */ package shame.astra.client.modules.impl.render;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.class_1297;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_2960;
/*     */ import net.minecraft.class_3532;
/*     */ import net.minecraft.class_4587;
/*     */ import net.minecraft.class_5498;
/*     */ import net.minecraft.class_742;
/*     */ import net.minecraft.class_7833;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.EventRender;
/*     */ import shame.astra.api.storages.implement.FreeLookStorage;
/*     */ import shame.astra.api.utils.color.ColorUtils;
/*     */ import shame.astra.api.utils.math.MathUtils;
/*     */ import shame.astra.api.utils.render.RenderUtils;
/*     */ import shame.astra.astra;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*     */ import shame.astra.client.modules.settings.implement.ModeSetting;
/*     */ 
/*     */ public class Arrows
/*     */   extends Module {
/*  30 */   public static Arrows INSTANCE = new Arrows();
/*  31 */   private static final class_2960 FIRST_ARROW_TEXTURE = class_2960.method_60655("astra", "textures/arrows/arrow.png");
/*  32 */   private static final class_2960 SECOND_ARROW_TEXTURE = class_2960.method_60655("astra", "textures/arrows/arr.png");
/*  33 */   private static final class_2960 MAMA_ARROW_TEXTURE = class_2960.method_60655("astra", "textures/arrows/arrowsnurik.png");
/*     */   
/*  35 */   private final ModeSetting type = new ModeSetting("Вид", "Первый", new String[] { "Первый", "Второй", "Третий" });
/*  36 */   private final FloatSetting radius = new FloatSetting("Радиус", 58.0F, 30.0F, 120.0F, 1.0F);
/*  37 */   private final FloatSetting size = new FloatSetting("Размер", 13.0F, 8.0F, 28.0F, 0.5F);
/*  38 */   private final FloatSetting glowRadius = new FloatSetting("Свечение", 7.5F, 0.0F, 20.0F, 0.5F);
/*     */   
/*  40 */   private final Map<UUID, ArrowState> states = new HashMap<>();
/*  41 */   private final Set<UUID> seenPlayers = new HashSet<>();
/*     */   
/*     */   public Arrows() {
/*  44 */     super("Arrows", "Красивые стрелочки на энтити", Module.ModuleCategory.RENDER);
/*  45 */     addSettings(new Setting[] { (Setting)this.type, (Setting)this.radius, (Setting)this.size, (Setting)this.glowRadius });
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onRender(EventRender.Default event) {
/*  50 */     if (mc.field_1724 == null || mc.field_1687 == null || mc.field_1690.field_1842) {
/*  51 */       this.states.clear();
/*     */       return;
/*     */     } 
/*  54 */     if (mc.field_1690.method_31044() != class_5498.field_26664) {
/*  55 */       fadeAllStates();
/*     */       
/*     */       return;
/*     */     } 
/*  59 */     float partialTicks = event.getPartialTicks();
/*  60 */     float centerX = mc.method_22683().method_4486() * 0.5F;
/*  61 */     float centerY = mc.method_22683().method_4502() * 0.5F;
/*  62 */     float arrowSize = this.size.get();
/*  63 */     float y = centerY - this.radius.get();
/*  64 */     float playerYaw = getReferenceYaw(partialTicks);
/*  65 */     class_243 selfPos = getReferencePos(partialTicks);
/*     */     
/*  67 */     this.seenPlayers.clear();
/*  68 */     for (class_742 player : mc.field_1687.method_18456()) {
/*  69 */       if (player == mc.field_1724 || !player.method_5805() || player.method_7325() || isGhostPlayer(player)) {
/*     */         continue;
/*     */       }
/*     */       
/*  73 */       UUID uuid = player.method_5667();
/*  74 */       ArrowState state = this.states.computeIfAbsent(uuid, id -> new ArrowState());
/*  75 */       this.seenPlayers.add(uuid);
/*     */       
/*  77 */       int color = getPlayerColor(player);
/*  78 */       float targetYaw = getRelativeYaw((class_1297)player, partialTicks, playerYaw, selfPos);
/*  79 */       state.rotation = interpolateAngle(state.rotation, targetYaw, 0.18F);
/*  80 */       state.alpha = approach(state.alpha, 1.0F, 0.12F);
/*  81 */       float alpha = class_3532.method_15363(state.alpha, 0.0F, 1.0F);
/*  82 */       if (alpha <= 0.01F) {
/*     */         continue;
/*     */       }
/*     */       
/*  86 */       int drawColor = ColorUtils.applyAlpha(color, alpha);
/*  87 */       int shadowColor = ColorUtils.applyAlpha(ColorUtils.darken(color, 0.55F), alpha * 0.65F);
/*  88 */       renderArrow(event.getContext().method_51448(), centerX, centerY, y, arrowSize, state.rotation, drawColor, shadowColor);
/*     */     } 
/*     */     
/*  91 */     this.states.entrySet().removeIf(entry -> {
/*     */           if (this.seenPlayers.contains(entry.getKey())) {
/*     */             return false;
/*     */           }
/*     */           ArrowState state = (ArrowState)entry.getValue();
/*     */           state.alpha = approach(state.alpha, 0.0F, 0.1F);
/*     */           return (state.alpha <= 0.02F);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private void renderArrow(class_4587 matrices, float centerX, float centerY, float y, float size, float rotation, int color, int shadowColor) {
/*     */     class_2960 ARROW;
/* 104 */     if (this.type.getIndex() == 0) {
/* 105 */       ARROW = FIRST_ARROW_TEXTURE;
/* 106 */     } else if (this.type.getIndex() == 1) {
/* 107 */       ARROW = SECOND_ARROW_TEXTURE;
/*     */     } else {
/* 109 */       ARROW = MAMA_ARROW_TEXTURE;
/*     */     } 
/*     */     
/* 112 */     class_2960 ARROW_TEXTURE = ARROW;
/* 113 */     matrices.method_22903();
/* 114 */     matrices.method_46416(centerX, centerY, 0.0F);
/* 115 */     matrices.method_22907(class_7833.field_40718.rotationDegrees(rotation));
/* 116 */     matrices.method_46416(-centerX, -centerY, 0.0F);
/*     */     
/* 118 */     float x = centerX - size * 0.5F;
/* 119 */     RenderUtils.drawImage(matrices, ARROW_TEXTURE, x, y + size * 0.08F, size, size, shadowColor);
/* 120 */     RenderUtils.drawImage(matrices, ARROW_TEXTURE, x, y, size, size, color);
/* 121 */     matrices.method_22909();
/*     */   }
/*     */   
/*     */   private void fadeAllStates() {
/* 125 */     this.states.entrySet().removeIf(entry -> {
/*     */           ArrowState state = (ArrowState)entry.getValue();
/*     */           state.alpha = approach(state.alpha, 0.0F, 0.1F);
/*     */           return (state.alpha <= 0.02F);
/*     */         });
/*     */   }
/*     */   
/*     */   private float approach(float current, float target, float factor) {
/* 133 */     factor = class_3532.method_15363(factor, 0.0F, 1.0F);
/* 134 */     return class_3532.method_16439(factor, current, target);
/*     */   }
/*     */   
/*     */   private int getPlayerColor(class_742 player) {
/* 138 */     String name = player.method_5477().getString();
/* 139 */     boolean isFriend = (astra.INSTANCE.friendStorage != null && astra.INSTANCE.friendStorage.isFriend(name));
/* 140 */     return isFriend ? ColorUtils.rgb(80, 170, 255) : ColorUtils.getThemeColor();
/*     */   }
/*     */   
/*     */   private float getRelativeYaw(class_1297 entity, float partialTicks, float playerYaw, class_243 selfPos) {
/* 144 */     class_243 entityPos = MathUtils.interpolate(entity, partialTicks);
/*     */     
/* 146 */     double dx = entityPos.field_1352 - selfPos.field_1352;
/* 147 */     double dz = entityPos.field_1350 - selfPos.field_1350;
/* 148 */     float yaw = (float)-Math.toDegrees(Math.atan2(dx, dz));
/* 149 */     return class_3532.method_15393(yaw - playerYaw);
/*     */   }
/*     */   
/*     */   private float getReferenceYaw(float partialTicks) {
/* 153 */     if (FreeLookStorage.isActive()) {
/* 154 */       return FreeLookStorage.getFreeYaw();
/*     */     }
/* 156 */     return class_3532.method_16439(partialTicks, mc.field_1724.field_5982, mc.field_1724.method_36454());
/*     */   }
/*     */   
/*     */   private class_243 getReferencePos(float partialTicks) {
/* 160 */     if (FreeLookStorage.isActive() && mc.field_1773 != null && mc.field_1773.method_19418() != null) {
/* 161 */       return mc.field_1773.method_19418().method_19326();
/*     */     }
/* 163 */     return MathUtils.interpolate((class_1297)mc.field_1724, partialTicks);
/*     */   }
/*     */   
/*     */   private float interpolateAngle(float current, float target, float factor) {
/* 167 */     float delta = class_3532.method_15393(target - current);
/* 168 */     return current + delta * factor;
/*     */   }
/*     */   
/*     */   private boolean isGhostPlayer(class_742 player) {
/* 172 */     if (player.method_5797() != null) {
/* 173 */       String name = player.method_5797().getString();
/* 174 */       if (name != null && name.startsWith("Ghost_")) {
/* 175 */         return true;
/*     */       }
/*     */     } 
/* 178 */     return ("OtherClientPlayerEntity".equals(player.getClass().getSimpleName()) && player.method_36455() == -30.0F);
/*     */   }
/*     */   
/*     */   private static final class ArrowState {
/*     */     private float alpha;
/*     */     private float rotation;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\Arrows.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */