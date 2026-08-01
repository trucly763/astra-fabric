/*     */ package shame.astra.client.modules.impl.misc;
/*     */ import net.minecraft.class_1268;
/*     */ import net.minecraft.class_2246;
/*     */ import net.minecraft.class_2338;
/*     */ import net.minecraft.class_2350;
/*     */ import net.minecraft.class_2382;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_3532;
/*     */ import net.minecraft.class_3965;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.EventBinding;
/*     */ import shame.astra.api.events.implement.EventGameUpdate;
/*     */ import shame.astra.api.events.implement.EventUpdate;
/*     */ import shame.astra.api.storages.implement.RotationStorage;
/*     */ import shame.astra.api.utils.rotate.Rotation;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.BindSetting;
/*     */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*     */ 
/*     */ public class EcOpen extends Module {
/*  22 */   public static EcOpen INSTANCE = new EcOpen();
/*     */   
/*  24 */   private final BindSetting openKey = new BindSetting("Открыть", -1);
/*  25 */   private final FloatSetting range = new FloatSetting("Дистанция", 6.0F, 3.0F, 6.0F, 0.1F);
/*     */   
/*  27 */   private class_2338 targetChest = null;
/*     */   private boolean shouldRotate = false;
/*  29 */   private int rotationTicks = 0; private float currentYaw;
/*     */   private float currentPitch;
/*     */   
/*     */   public EcOpen() {
/*  33 */     super("EcOpen", "Открывает эндер сундук по бинду", Module.ModuleCategory.MISC);
/*  34 */     addSettings(new Setting[] { (Setting)this.openKey, (Setting)this.range });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  39 */     reset();
/*  40 */     super.onEnable();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  45 */     reset();
/*  46 */     super.onDisable();
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onBinding(EventBinding event) {
/*  51 */     if (mc.field_1755 != null || mc.field_1724 == null || mc.field_1687 == null)
/*     */       return; 
/*  53 */     if (event.getKey() == this.openKey.getKey()) {
/*  54 */       findEnderChest();
/*     */     }
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onGameUpdate(EventGameUpdate event) {
/*  60 */     if (!this.shouldRotate || this.targetChest == null || mc.field_1724 == null)
/*     */       return; 
/*  62 */     if (!mc.field_1687.method_8320(this.targetChest).method_27852(class_2246.field_10443)) {
/*  63 */       reset();
/*     */       
/*     */       return;
/*     */     } 
/*  67 */     class_243 target = class_243.method_24953((class_2382)this.targetChest);
/*  68 */     float[] rotations = calculateRotation(target);
/*     */     
/*  70 */     float deltaYaw = class_3532.method_15393(rotations[0] - this.currentYaw);
/*  71 */     float deltaPitch = rotations[1] - this.currentPitch;
/*     */     
/*  73 */     this.currentYaw += deltaYaw * 0.8F;
/*  74 */     this.currentPitch = class_3532.method_15363(this.currentPitch + deltaPitch * 0.8F, -90.0F, 90.0F);
/*     */     
/*  76 */     RotationStorage.update(new Rotation(this.currentYaw, this.currentPitch), 360.0F, 360.0F, 360.0F, 360.0F, 1, 1, false);
/*  77 */     this.rotationTicks++;
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onUpdate(EventUpdate event) {
/*  82 */     if (!this.shouldRotate || this.targetChest == null || mc.field_1724 == null)
/*     */       return; 
/*  84 */     if (this.rotationTicks >= 2) {
/*  85 */       class_243 hitVec = class_243.method_24953((class_2382)this.targetChest).method_1031(0.0D, 0.5D, 0.0D);
/*  86 */       class_3965 hitResult = new class_3965(hitVec, class_2350.field_11036, this.targetChest, false);
/*     */       
/*  88 */       mc.field_1761.method_2896(mc.field_1724, class_1268.field_5808, hitResult);
/*  89 */       mc.field_1724.method_6104(class_1268.field_5808);
/*  90 */       reset();
/*     */     } 
/*     */     
/*  93 */     if (this.rotationTicks > 20) reset(); 
/*     */   }
/*     */   
/*     */   private void findEnderChest() {
/*  97 */     class_2338 playerPos = mc.field_1724.method_24515();
/*  98 */     int r = this.range.getValue().intValue();
/*  99 */     double maxDist = (this.range.getValue().floatValue() * this.range.getValue().floatValue());
/* 100 */     double closestDist = Double.MAX_VALUE;
/* 101 */     class_2338 closest = null;
/*     */     
/* 103 */     for (int x = -r; x <= r; x++) {
/* 104 */       for (int y = -r; y <= r; y++) {
/* 105 */         for (int z = -r; z <= r; z++) {
/* 106 */           class_2338 pos = playerPos.method_10069(x, y, z);
/* 107 */           if (mc.field_1687.method_8320(pos).method_27852(class_2246.field_10443)) {
/* 108 */             double dist = mc.field_1724.method_33571().method_1025(class_243.method_24953((class_2382)pos));
/* 109 */             if (dist < closestDist && dist <= maxDist) {
/* 110 */               closestDist = dist;
/* 111 */               closest = pos;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 118 */     if (closest != null) {
/* 119 */       this.targetChest = closest;
/* 120 */       this.shouldRotate = true;
/* 121 */       this.rotationTicks = 0;
/* 122 */       this.currentYaw = mc.field_1724.method_36454();
/* 123 */       this.currentPitch = mc.field_1724.method_36455();
/*     */     } 
/*     */   }
/*     */   
/*     */   private float[] calculateRotation(class_243 target) {
/* 128 */     class_243 eye = mc.field_1724.method_33571();
/* 129 */     double dx = target.field_1352 - eye.field_1352;
/* 130 */     double dy = target.field_1351 - eye.field_1351;
/* 131 */     double dz = target.field_1350 - eye.field_1350;
/* 132 */     double dist = Math.sqrt(dx * dx + dz * dz);
/*     */     
/* 134 */     float yaw = (float)Math.toDegrees(Math.atan2(dz, dx)) - 90.0F;
/* 135 */     float pitch = (float)-Math.toDegrees(Math.atan2(dy, dist));
/*     */     
/* 137 */     return new float[] { yaw, class_3532.method_15363(pitch, -90.0F, 90.0F) };
/*     */   }
/*     */   
/*     */   private void reset() {
/* 141 */     this.targetChest = null;
/* 142 */     this.shouldRotate = false;
/* 143 */     this.rotationTicks = 0;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\misc\EcOpen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */