/*     */ package shame.astra.client.modules.impl.combat;
/*     */ 
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import net.minecraft.class_10142;
/*     */ import net.minecraft.class_1297;
/*     */ import net.minecraft.class_1309;
/*     */ import net.minecraft.class_1657;
/*     */ import net.minecraft.class_1799;
/*     */ import net.minecraft.class_238;
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
/*     */ import shame.astra.api.events.implement.EventGameUpdate;
/*     */ import shame.astra.api.storages.implement.RotationStorage;
/*     */ import shame.astra.api.utils.color.ColorUtils;
/*     */ import shame.astra.api.utils.rotate.Rotation;
/*     */ import shame.astra.astra;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.impl.combat.components.gcd.GCDUtil;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*     */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*     */ import shame.astra.client.modules.settings.implement.ListSetting;
/*     */ 
/*     */ public class AimBot
/*     */   extends Module
/*     */ {
/*  41 */   public static AimBot INSTANCE = new AimBot();
/*     */   
/*  43 */   private final ListSetting targetTypes = new ListSetting("Типы целей", new BooleanSetting[] { new BooleanSetting("Игроки", true), new BooleanSetting("В броне", true), new BooleanSetting("Без брони", false), new BooleanSetting("Мобы", false), new BooleanSetting("Зомби", false) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  50 */   private final FloatSetting range = new FloatSetting("Дистанция", 40.0F, 10.0F, 100.0F, 1.0F);
/*  51 */   private final FloatSetting aimTime = new FloatSetting("Время наводки (тики)", 10.0F, 0.0F, 40.0F, 1.0F);
/*  52 */   private final BooleanSetting silentRotations = new BooleanSetting("Тихие повороты", true);
/*  53 */   private final BooleanSetting showCrosshair = new BooleanSetting("Показать прицел", true);
/*  54 */   private final FloatSetting crosshairSize = new FloatSetting("Размер прицела", 1.0F, 0.3F, 3.0F, 0.1F);
/*     */   
/*  56 */   private class_1309 target = null;
/*     */   private boolean isAiming = false;
/*  58 */   private float aimProgress = 0.0F;
/*  59 */   private Rotation targetRotation = null;
/*     */   
/*     */   public AimBot() {
/*  62 */     super("AimBot", "Авто-наведение для лука и арбалета", Module.ModuleCategory.COMBAT);
/*  63 */     addSettings(new Setting[] { (Setting)this.targetTypes, (Setting)this.range, (Setting)this.aimTime, (Setting)this.silentRotations, (Setting)this.showCrosshair, (Setting)this.crosshairSize });
/*     */   }
/*     */   
/*     */   private class_2960 getCrosshairTexture() {
/*  67 */     return class_2960.method_60655("astra", "textures/cross/hit.png");
/*     */   }
/*     */   
/*     */   private boolean isHoldingBowOrCrossbow() {
/*  71 */     class_1799 mainHand = mc.field_1724.method_6047();
/*  72 */     class_1799 offHand = mc.field_1724.method_6079();
/*  73 */     return (mainHand.method_7909() instanceof net.minecraft.class_1753 || mainHand
/*  74 */       .method_7909() instanceof net.minecraft.class_1764 || offHand
/*  75 */       .method_7909() instanceof net.minecraft.class_1753 || offHand
/*  76 */       .method_7909() instanceof net.minecraft.class_1764);
/*     */   }
/*     */   
/*     */   private boolean isUsingBowOrCrossbow() {
/*  80 */     return (mc.field_1724.method_6115() && isHoldingBowOrCrossbow());
/*     */   }
/*     */   
/*     */   private boolean isValidTarget(class_1309 entity) {
/*  84 */     if (entity == mc.field_1724) return false; 
/*  85 */     if (!entity.method_5805() || entity.method_6032() <= 0.0F) return false;
/*     */     
/*  87 */     if (entity instanceof class_1657) {
/*  88 */       if (!this.targetTypes.is("Игроки")) return false; 
/*  89 */       if (astra.INSTANCE.friendStorage.isFriend(entity.method_5477().getString())) return false;
/*     */       
/*  91 */       boolean hasArmor = false;
/*  92 */       class_1657 player = (class_1657)entity;
/*  93 */       for (class_1799 armor : player.method_5661()) {
/*  94 */         if (!armor.method_7960()) {
/*  95 */           hasArmor = true;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 100 */       if (this.targetTypes.is("В броне") && hasArmor) return true; 
/* 101 */       if (this.targetTypes.is("Без брони") && !hasArmor) return true; 
/* 102 */       if (!this.targetTypes.is("В броне") && !this.targetTypes.is("Без брони")) return true;
/*     */       
/* 104 */       return false;
/*     */     } 
/*     */     
/* 107 */     if (entity instanceof net.minecraft.class_1642) {
/* 108 */       return this.targetTypes.is("Зомби");
/*     */     }
/*     */     
/* 111 */     if (entity instanceof net.minecraft.class_1588) {
/* 112 */       return this.targetTypes.is("Мобы");
/*     */     }
/*     */     
/* 115 */     return false;
/*     */   }
/*     */   
/*     */   private class_1309 findBestTarget() {
/* 119 */     List<class_1309> targets = new ArrayList<>();
/*     */     
/* 121 */     class_238 searchBox = mc.field_1724.method_5829().method_1014(this.range.getValue().floatValue());
/*     */     
/* 123 */     for (class_1309 entity : mc.field_1687.method_8390(class_1309.class, searchBox, e -> true)) {
/* 124 */       if (!isValidTarget(entity))
/*     */         continue; 
/* 126 */       double dist = mc.field_1724.method_5739((class_1297)entity);
/* 127 */       if (dist > this.range.getValue().floatValue())
/*     */         continue; 
/* 129 */       targets.add(entity);
/*     */     } 
/*     */     
/* 132 */     if (targets.isEmpty()) return null;
/*     */     
/* 134 */     targets.sort(Comparator.comparingDouble(entity -> mc.field_1724.method_5739((class_1297)entity)));
/* 135 */     return targets.get(0);
/*     */   }
/*     */ 
/*     */   
/*     */   private Rotation calculateBowRotation(class_1309 target) {
/* 140 */     class_243 eyes = mc.field_1724.method_33571();
/* 141 */     class_243 targetPos = target.method_5829().method_1005();
/*     */     
/* 143 */     double dx = targetPos.field_1352 - eyes.field_1352;
/* 144 */     double dy = targetPos.field_1351 - eyes.field_1351;
/* 145 */     double dz = targetPos.field_1350 - eyes.field_1350;
/*     */     
/* 147 */     double distance = Math.sqrt(dx * dx + dz * dz);
/*     */     
/* 149 */     float yaw = (float)Math.toDegrees(Math.atan2(dz, dx)) - 90.0F;
/* 150 */     float pitch = (float)-Math.toDegrees(Math.atan2(dy, distance));
/*     */     
/* 152 */     return new Rotation(yaw, pitch);
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onRender3D(Event3DRender event) {
/* 157 */     if (!this.showCrosshair.isState() || this.target == null || !this.isAiming)
/*     */       return; 
/* 159 */     float partialTicks = event.getTickDelta();
/*     */ 
/*     */ 
/*     */     
/* 163 */     class_243 targetPos = new class_243(class_3532.method_16436(partialTicks, this.target.field_6038, this.target.method_23317()), class_3532.method_16436(partialTicks, this.target.field_5971, this.target.method_23318()) + this.target.method_17682() / 2.0D, class_3532.method_16436(partialTicks, this.target.field_5989, this.target.method_23321()));
/*     */ 
/*     */     
/* 166 */     class_243 cameraPos = mc.field_1773.method_19418().method_19326();
/* 167 */     class_4587 matrices = event.getMatrices();
/*     */     
/* 169 */     double renderX = targetPos.field_1352 - cameraPos.field_1352;
/* 170 */     double renderY = targetPos.field_1351 - cameraPos.field_1351;
/* 171 */     double renderZ = targetPos.field_1350 - cameraPos.field_1350;
/*     */     
/* 173 */     RenderSystem.enableBlend();
/* 174 */     RenderSystem.blendFunc(770, 1);
/* 175 */     RenderSystem.disableDepthTest();
/* 176 */     RenderSystem.depthMask(false);
/* 177 */     RenderSystem.disableCull();
/* 178 */     RenderSystem.setShaderTexture(0, getCrosshairTexture());
/* 179 */     RenderSystem.setShader(class_10142.field_53880);
/*     */     
/* 181 */     matrices.method_22903();
/* 182 */     matrices.method_22904(renderX, renderY, renderZ);
/* 183 */     matrices.method_22907(class_7833.field_40716.rotationDegrees(-mc.field_1773.method_19418().method_19330()));
/* 184 */     matrices.method_22907(class_7833.field_40714.rotationDegrees(mc.field_1773.method_19418().method_19329()));
/*     */     
/* 186 */     float size = this.crosshairSize.get() * 0.5F;
/* 187 */     int alpha = (int)(255.0F * this.aimProgress);
/* 188 */     int color = ColorUtils.getThemeColor();
/* 189 */     int r = color >> 16 & 0xFF;
/* 190 */     int g = color >> 8 & 0xFF;
/* 191 */     int b = color & 0xFF;
/*     */     
/* 193 */     Matrix4f matrix = matrices.method_23760().method_23761();
/* 194 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
/*     */     
/* 196 */     buffer.method_22918(matrix, -size, -size, 0.0F).method_22913(0.0F, 1.0F).method_1336(r, g, b, alpha);
/* 197 */     buffer.method_22918(matrix, -size, size, 0.0F).method_22913(0.0F, 0.0F).method_1336(r, g, b, alpha);
/* 198 */     buffer.method_22918(matrix, size, size, 0.0F).method_22913(1.0F, 0.0F).method_1336(r, g, b, alpha);
/* 199 */     buffer.method_22918(matrix, size, -size, 0.0F).method_22913(1.0F, 1.0F).method_1336(r, g, b, alpha);
/*     */     
/* 201 */     class_286.method_43433(buffer.method_60800());
/*     */     
/* 203 */     matrices.method_22909();
/*     */     
/* 205 */     RenderSystem.enableCull();
/* 206 */     RenderSystem.enableDepthTest();
/* 207 */     RenderSystem.depthMask(true);
/* 208 */     RenderSystem.defaultBlendFunc();
/* 209 */     RenderSystem.disableBlend();
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onGameUpdate(EventGameUpdate e) {
/* 214 */     if (mc.field_1724 == null || mc.field_1687 == null)
/*     */       return; 
/* 216 */     this.isAiming = isUsingBowOrCrossbow();
/*     */     
/* 218 */     if (this.isAiming) {
/* 219 */       class_1309 newTarget = findBestTarget();
/*     */       
/* 221 */       if (newTarget != null) {
/* 222 */         if (this.target != newTarget) {
/* 223 */           this.target = newTarget;
/* 224 */           this.aimProgress = 0.0F;
/*     */         } 
/*     */         
/* 227 */         Rotation newRotation = calculateBowRotation(this.target);
/*     */         
/* 229 */         float maxStep = 1.0F / Math.max(1.0F, this.aimTime.getValue().floatValue());
/* 230 */         this.aimProgress = Math.min(this.aimProgress + maxStep, 1.0F);
/*     */         
/* 232 */         float currentYaw = mc.field_1724.method_36454();
/* 233 */         float currentPitch = mc.field_1724.method_36455();
/* 234 */         float targetYaw = newRotation.getYaw();
/* 235 */         float targetPitch = newRotation.getPitch();
/*     */         
/* 237 */         float yawDiff = class_3532.method_15393(targetYaw - currentYaw);
/* 238 */         float pitchDiff = targetPitch - currentPitch;
/*     */         
/* 240 */         float stepYaw = yawDiff * this.aimProgress;
/* 241 */         float stepPitch = pitchDiff * this.aimProgress;
/*     */         
/* 243 */         this.targetRotation = new Rotation(currentYaw + stepYaw, currentPitch + stepPitch);
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 249 */       this.target = null;
/* 250 */       this.targetRotation = null;
/* 251 */       this.aimProgress = 0.0F;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @EventLink
/*     */   public void onUpdate(EventGameUpdate ignoredghj) {
/* 258 */     if (this.target != null && this.isAiming && this.targetRotation != null) {
/* 259 */       if (this.silentRotations.isState()) {
/* 260 */         float gcd = GCDUtil.getGCD();
/* 261 */         float yaw = this.targetRotation.getYaw();
/* 262 */         float pitch = this.targetRotation.getPitch();
/*     */         
/* 264 */         yaw -= (yaw - mc.field_1724.method_36454()) % gcd;
/* 265 */         pitch -= (pitch - mc.field_1724.method_36455()) % gcd;
/*     */         
/* 267 */         RotationStorage.update(new Rotation(yaw, pitch), 180.0F, 180.0F, 45.0F, 45.0F, 0, 2, false);
/*     */       } else {
/* 269 */         mc.field_1724.method_36456(this.targetRotation.getYaw());
/* 270 */         mc.field_1724.method_36457(this.targetRotation.getPitch());
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public class_1309 getTarget() {
/* 276 */     return this.target;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/* 281 */     super.onEnable();
/* 282 */     this.target = null;
/* 283 */     this.isAiming = false;
/* 284 */     this.aimProgress = 0.0F;
/* 285 */     this.targetRotation = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 290 */     super.onDisable();
/* 291 */     this.target = null;
/* 292 */     this.isAiming = false;
/* 293 */     this.aimProgress = 0.0F;
/* 294 */     this.targetRotation = null;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\combat\AimBot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */