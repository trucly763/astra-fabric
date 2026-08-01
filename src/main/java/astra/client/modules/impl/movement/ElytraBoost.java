/*     */ package shame.astra.client.modules.impl.movement;
/*     */ import lombok.Generated;
/*     */ import net.minecraft.class_241;
/*     */ import shame.astra.api.utils.chat.ChatUtils;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.impl.combat.Aura;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*     */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*     */ import shame.astra.client.modules.settings.implement.ModeSetting;
/*     */ 
/*     */ public class ElytraBoost extends Module {
/*     */   @Generated
/*     */   public void setLastDebugMessageAt(long lastDebugMessageAt) {
/*  15 */     this.lastDebugMessageAt = lastDebugMessageAt;
/*     */   }
/*     */   
/*  18 */   private static final String[] RANGE_LABELS = new String[] { "0 - 5", "5 - 10", "10 - 15", "15 - 20", "20 - 25", "25 - 30", "30 - 35", "35 - 40", "40 - 45" };
/*     */   private static final long DEBUG_MESSAGE_INTERVAL_MS = 800L;
/*  20 */   public static ElytraBoost INSTANCE = new ElytraBoost();
/*     */   
/*  22 */   private final FloatSetting[] yawSpeeds = new FloatSetting[9]; @Generated public FloatSetting[] getYawSpeeds() { return this.yawSpeeds; }
/*  23 */    private final FloatSetting[] pitchSpeeds = new FloatSetting[9]; @Generated public FloatSetting[] getPitchSpeeds() { return this.pitchSpeeds; }
/*     */   
/*  25 */   private final ModeSetting mode = new ModeSetting("Сервер", "Custom", new String[] { "Custom", "LonyGrief", "BravoHVH", "ReallyWorld", "SlimeWorld" }); @Generated public ModeSetting getMode() { return this.mode; } @Generated
/*  26 */   public BooleanSetting getDebug() { return this.debug; } private final BooleanSetting debug = (new BooleanSetting("Дебаг", false))
/*  27 */     .visible(this::isCustomMode); private long lastDebugMessageAt; @Generated
/*  28 */   public long getLastDebugMessageAt() { return this.lastDebugMessageAt; }
/*     */   
/*     */   public ElytraBoost() {
/*  31 */     super("ElytraBoost", "Ускоряет на элитрах", Module.ModuleCategory.MOVEMENT);
/*     */     int i;
/*  33 */     for (i = 0; i < this.yawSpeeds.length; i++) {
/*  34 */       this.yawSpeeds[i] = (new FloatSetting("yaw " + RANGE_LABELS[i], 1.5F, 1.5F, 2.5F, 0.01F))
/*  35 */         .visible(this::isCustomMode);
/*     */     }
/*     */     
/*  38 */     for (i = 0; i < this.pitchSpeeds.length; i++) {
/*  39 */       this.pitchSpeeds[i] = (new FloatSetting("pitch " + RANGE_LABELS[i], 1.5F, 1.5F, 2.5F, 0.01F))
/*  40 */         .visible(this::isCustomMode);
/*     */     }
/*     */     
/*  43 */     addSettings(new Setting[] { (Setting)this.mode, (Setting)this.debug });
/*  44 */     addSettings((Setting[])this.yawSpeeds);
/*  45 */     addSettings((Setting[])this.pitchSpeeds);
/*     */   }
/*     */   
/*     */   public boolean isCustomMode() {
/*  49 */     return this.mode.is("Custom");
/*     */   }
/*     */   
/*     */   public class_241 getBoostV2() {
/*  53 */     float yaw = (mc.field_1724 != null) ? mc.field_1724.method_36454() : 0.0F;
/*  54 */     float pitch = (mc.field_1724 != null) ? mc.field_1724.method_36455() : 0.0F;
/*     */     
/*  56 */     Aura aura = Aura.INSTANCE;
/*  57 */     if (aura != null && aura.isEnable() && aura.getTarget() != null) {
/*  58 */       class_241 rotations = aura.getTargetRotations();
/*  59 */       if (rotations != null) {
/*  60 */         yaw = rotations.field_1343;
/*  61 */         pitch = rotations.field_1342;
/*     */       } 
/*     */     } 
/*     */     
/*  65 */     float normalizedYaw = convertValToRange(class_3532.method_15393(yaw));
/*  66 */     float normalizedPitch = convertValToRange(Math.abs(pitch));
/*  67 */     int yawIndex = getRangeIndex(normalizedYaw, this.yawSpeeds.length);
/*  68 */     int pitchIndex = getRangeIndex(normalizedPitch, this.pitchSpeeds.length);
/*  69 */     float yawSpeed = this.yawSpeeds[yawIndex].getValue().floatValue();
/*  70 */     float pitchSpeed = this.pitchSpeeds[pitchIndex].getValue().floatValue();
/*     */     
/*  72 */     if (pitchSpeed > yawSpeed) {
/*  73 */       yawSpeed = pitchSpeed;
/*     */     }
/*     */     
/*  76 */     logDebug(yawIndex, yawSpeed, pitchIndex, pitchSpeed);
/*  77 */     return new class_241(yawSpeed, pitchSpeed);
/*     */   }
/*     */   
/*     */   private void logDebug(int yawIndex, float yawSpeed, int pitchIndex, float pitchSpeed) {
/*  81 */     if (!this.debug.isState()) {
/*     */       return;
/*     */     }
/*     */     
/*  85 */     long now = System.currentTimeMillis();
/*  86 */     if (now - this.lastDebugMessageAt < 800L) {
/*     */       return;
/*     */     }
/*     */     
/*  90 */     this.lastDebugMessageAt = now;
/*  91 */     ChatUtils.sendMessage("yaw " + RANGE_LABELS[yawIndex] + ": " + yawSpeed + " | pitch " + RANGE_LABELS[pitchIndex] + ": " + pitchSpeed);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getRangeIndex(float value, int length) {
/*  98 */     return Math.min((int)(value / 5.0F), length - 1);
/*     */   }
/*     */   
/*     */   private float convertValToRange(float value) {
/* 102 */     float result = Math.abs(value);
/* 103 */     if (result > 90.0F) {
/* 104 */       result = 180.0F - result;
/*     */     }
/* 106 */     if (result > 45.0F) {
/* 107 */       result = 90.0F - result;
/*     */     }
/* 109 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\movement\ElytraBoost.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */