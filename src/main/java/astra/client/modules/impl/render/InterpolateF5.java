/*     */ package shame.astra.client.modules.impl.render;
/*     */ 
/*     */ import net.minecraft.class_3532;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.EventRotation;
/*     */ import shame.astra.api.events.implement.EventUpdate;
/*     */ import shame.astra.api.storages.implement.FreeLookStorage;
/*     */ import shame.astra.client.modules.Module;
/*     */ 
/*     */ public class InterpolateF5
/*     */   extends Module
/*     */ {
/*  13 */   public static InterpolateF5 INSTANCE = new InterpolateF5();
/*     */   
/*     */   private static final float SWITCH_ANIM_SPEED = 0.26F;
/*     */   
/*     */   private static final float DISTANCE_SPEED = 0.13F;
/*     */   
/*     */   private static final float ROTATION_SMOOTH = 0.28F;
/*     */   private static final float CAMERA_DISTANCE = 4.1F;
/*     */   private static final float SNEAK_OFFSET = 0.5F;
/*     */   private static final float JUMP_MULTIPLIER = 2.0F;
/*     */   private static final float ANIM_SPEED = 0.13F;
/*     */   private float currentDistance;
/*     */   private float prevDistance;
/*     */   private float currentYaw;
/*     */   private float prevYaw;
/*     */   private float currentPitch;
/*     */   private float prevPitch;
/*     */   private float heightOffset;
/*     */   private float prevHeightOffset;
/*     */   private boolean switchAnimating;
/*     */   private boolean wasThirdPerson;
/*     */   private boolean needsInit = true;
/*     */   
/*     */   public InterpolateF5() {
/*  37 */     super("Cinematic Camera", "Плавная камера от ф5", Module.ModuleCategory.RENDER);
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onUpdate(EventUpdate event) {
/*  42 */     if (mc.field_1724 == null || mc.field_1687 == null)
/*     */       return; 
/*  44 */     boolean isThirdPerson = !mc.field_1690.method_31044().method_31034();
/*     */     
/*  46 */     if (isThirdPerson && !this.wasThirdPerson) initCamera(true); 
/*  47 */     if (!isThirdPerson && this.wasThirdPerson) {
/*  48 */       this.needsInit = true;
/*  49 */       this.switchAnimating = false;
/*     */     } 
/*     */     
/*  52 */     this.wasThirdPerson = isThirdPerson;
/*  53 */     if (isThirdPerson) updateCamera(); 
/*     */   }
/*     */   
/*     */   @EventLink(priority = 100)
/*     */   public void onRotation(EventRotation event) {
/*  58 */     if (mc.field_1724 == null || mc.field_1687 == null)
/*  59 */       return;  if (mc.field_1690.method_31044().method_31034())
/*     */       return; 
/*  61 */     event.setYaw(getInterpolatedYaw(event.getPartialTicks()));
/*  62 */     event.setPitch(getInterpolatedPitch(event.getPartialTicks()));
/*     */   }
/*     */   
/*     */   private void initCamera(boolean animateSwitch) {
/*  66 */     if (mc.field_1724 == null)
/*     */       return; 
/*  68 */     this.currentYaw = this.prevYaw = getReferenceYaw();
/*  69 */     this.currentPitch = this.prevPitch = getReferencePitch();
/*  70 */     this.currentDistance = this.prevDistance = animateSwitch ? 0.0F : 4.1F;
/*  71 */     this.heightOffset = this.prevHeightOffset = 0.0F;
/*  72 */     this.switchAnimating = animateSwitch;
/*  73 */     this.needsInit = false;
/*     */   }
/*     */   
/*     */   private void updateCamera() {
/*  77 */     if (mc.field_1724 == null)
/*  78 */       return;  if (this.needsInit) {
/*  79 */       initCamera(true);
/*     */       
/*     */       return;
/*     */     } 
/*  83 */     this.prevYaw = this.currentYaw;
/*  84 */     this.prevPitch = this.currentPitch;
/*  85 */     this.prevDistance = this.currentDistance;
/*  86 */     this.prevHeightOffset = this.heightOffset;
/*     */     
/*  88 */     float rotationSpeed = 0.28F;
/*     */     
/*  90 */     this.currentYaw += class_3532.method_15393(getReferenceYaw() - this.currentYaw) * rotationSpeed;
/*  91 */     this.currentPitch = class_3532.method_15363(this.currentPitch + (getReferencePitch() - this.currentPitch) * rotationSpeed, -90.0F, 90.0F);
/*     */     
/*  93 */     float distanceSpeed = this.switchAnimating ? 0.26F : 0.13F;
/*  94 */     this.currentDistance += (4.1F - this.currentDistance) * distanceSpeed;
/*  95 */     if (this.switchAnimating && Math.abs(4.1F - this.currentDistance) <= 0.02F) {
/*  96 */       this.currentDistance = 4.1F;
/*  97 */       this.switchAnimating = false;
/*     */     } 
/*     */     
/* 100 */     float targetOffset = 0.0F;
/* 101 */     if (mc.field_1724.method_5715()) {
/* 102 */       targetOffset = -0.5F;
/*     */     }
/* 104 */     if (!mc.field_1724.method_24828()) {
/* 105 */       targetOffset += (float)(-(mc.field_1724.method_18798()).field_1351 * 2.0D);
/*     */     }
/*     */     
/* 108 */     this.heightOffset += (targetOffset - this.heightOffset) * 0.13F;
/*     */   }
/*     */   
/*     */   public float getInterpolatedYaw(float partialTicks) {
/* 112 */     if (mc.field_1724 == null) return 0.0F; 
/* 113 */     return this.prevYaw + (this.currentYaw - this.prevYaw) * partialTicks;
/*     */   }
/*     */   
/*     */   public float getInterpolatedPitch(float partialTicks) {
/* 117 */     if (mc.field_1724 == null) return 0.0F; 
/* 118 */     return class_3532.method_15363(this.prevPitch + (this.currentPitch - this.prevPitch) * partialTicks, -90.0F, 90.0F);
/*     */   }
/*     */   
/*     */   public float getInterpolatedDistance(float partialTicks) {
/* 122 */     return this.prevDistance + (this.currentDistance - this.prevDistance) * partialTicks;
/*     */   }
/*     */   
/*     */   public float getInterpolatedHeightOffset(float partialTicks) {
/* 126 */     return this.prevHeightOffset + (this.heightOffset - this.prevHeightOffset) * partialTicks;
/*     */   }
/*     */   
/*     */   private float getReferenceYaw() {
/* 130 */     if (FreeLookStorage.isActive()) {
/* 131 */       return FreeLookStorage.getFreeYaw();
/*     */     }
/* 133 */     return (mc.field_1724 != null) ? mc.field_1724.method_36454() : 0.0F;
/*     */   }
/*     */   
/*     */   private float getReferencePitch() {
/* 137 */     if (FreeLookStorage.isActive()) {
/* 138 */       return FreeLookStorage.getFreePitch();
/*     */     }
/* 140 */     return (mc.field_1724 != null) ? mc.field_1724.method_36455() : 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/* 145 */     super.onEnable();
/* 146 */     this.needsInit = true;
/* 147 */     this.wasThirdPerson = false;
/*     */     
/* 149 */     if (mc.field_1724 != null && !mc.field_1690.method_31044().method_31034()) {
/* 150 */       initCamera(true);
/* 151 */       this.wasThirdPerson = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 157 */     super.onDisable();
/* 158 */     this.needsInit = true;
/* 159 */     this.heightOffset = 0.0F;
/* 160 */     this.prevHeightOffset = 0.0F;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\InterpolateF5.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */