/*     */ package shame.astra.mixin;
/*     */ 
/*     */ import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
/*     */ import net.minecraft.class_1299;
/*     */ import net.minecraft.class_1309;
/*     */ import net.minecraft.class_1671;
/*     */ import net.minecraft.class_1676;
/*     */ import net.minecraft.class_1937;
/*     */ import net.minecraft.class_241;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_310;
/*     */ import org.spongepowered.asm.mixin.Mixin;
/*     */ import org.spongepowered.asm.mixin.Shadow;
/*     */ import org.spongepowered.asm.mixin.Unique;
/*     */ import org.spongepowered.asm.mixin.injection.At;
/*     */ import org.spongepowered.asm.mixin.injection.Inject;
/*     */ import org.spongepowered.asm.mixin.injection.Redirect;
/*     */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*     */ import shame.astra.api.events.implement.EventFireWork;
/*     */ import shame.astra.api.utils.player.BoostUtils;
/*     */ import shame.astra.client.modules.impl.movement.ElytraBoost;
/*     */ 
/*     */ @Mixin({class_1671.class})
/*     */ public abstract class FireWorkRocketEntityMixin
/*     */   extends class_1676
/*     */ {
/*     */   @Unique
/*     */   private class_243 rotation;
/*     */   @Shadow
/*     */   private class_1309 field_7616;
/*     */   
/*     */   public FireWorkRocketEntityMixin(class_1299<? extends class_1676> entityType, class_1937 world) {
/*  33 */     super(entityType, world);
/*     */   }
/*     */   
/*     */   @Inject(method = {"method_5773"}, at = {@At("HEAD")})
/*     */   public void tick(CallbackInfo ci) {
/*  38 */     (new EventFireWork((class_1671)this)).call();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ModifyExpressionValue(method = {"method_5773"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_1309;method_5720()Lnet/minecraft/class_243;")})
/*     */   public class_243 captureRotation(class_243 original) {
/*  49 */     this.rotation = original;
/*  50 */     return this.rotation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Redirect(method = {"method_5773"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/class_243;method_1031(DDD)Lnet/minecraft/class_243;", ordinal = 0))
/*     */   public class_243 modifyBoost(class_243 velocity, double x, double y, double z) {
/*  62 */     class_310 mc = class_310.method_1551();
/*  63 */     ElytraBoost elytraBoost = ElytraBoost.INSTANCE;
/*     */     
/*  65 */     if (mc.field_1724 == null || !mc.field_1724.method_6128()) {
/*  66 */       return defaultBoost(velocity);
/*     */     }
/*     */     
/*  69 */     if (elytraBoost == null || !elytraBoost.isEnable()) {
/*  70 */       return defaultBoost(velocity);
/*     */     }
/*     */     
/*  73 */     return handleElytraBoost(mc, elytraBoost, velocity);
/*     */   }
/*     */   
/*     */   @Unique
/*     */   private class_243 handleElytraBoost(class_310 mc, ElytraBoost elytraBoost, class_243 velocity) {
/*  78 */     String modeName = elytraBoost.getMode().getCurrent();
/*     */ 
/*     */     
/*  81 */     switch (modeName)
/*     */     { case "LonyGrief":
/*  83 */         boost = BoostUtils.getBoost((class_1309)mc.field_1724);
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
/* 101 */         return velocity.method_1031(this.rotation.field_1352 * 0.1D + (this.rotation.field_1352 * boost.field_1352 - velocity.field_1352) * 0.5D, this.rotation.field_1351 * 0.1D + (this.rotation.field_1351 * boost.field_1351 - velocity.field_1351) * 0.5D, this.rotation.field_1350 * 0.1D + (this.rotation.field_1350 * boost.field_1350 - velocity.field_1350) * 0.5D);case "SlimeWorld": boost = BoostUtils.getBoostslime((class_1309)mc.field_1724); return velocity.method_1031(this.rotation.field_1352 * 0.1D + (this.rotation.field_1352 * boost.field_1352 - velocity.field_1352) * 0.5D, this.rotation.field_1351 * 0.1D + (this.rotation.field_1351 * boost.field_1351 - velocity.field_1351) * 0.5D, this.rotation.field_1350 * 0.1D + (this.rotation.field_1350 * boost.field_1350 - velocity.field_1350) * 0.5D);case "BravoHVH": boost = BoostUtils.getBoostbravo((class_1309)mc.field_1724); return velocity.method_1031(this.rotation.field_1352 * 0.1D + (this.rotation.field_1352 * boost.field_1352 - velocity.field_1352) * 0.5D, this.rotation.field_1351 * 0.1D + (this.rotation.field_1351 * boost.field_1351 - velocity.field_1351) * 0.5D, this.rotation.field_1350 * 0.1D + (this.rotation.field_1350 * boost.field_1350 - velocity.field_1350) * 0.5D);case "ReallyWorld": boost = BoostUtils.getBoostrw((class_1309)mc.field_1724); return velocity.method_1031(this.rotation.field_1352 * 0.1D + (this.rotation.field_1352 * boost.field_1352 - velocity.field_1352) * 0.5D, this.rotation.field_1351 * 0.1D + (this.rotation.field_1351 * boost.field_1351 - velocity.field_1351) * 0.5D, this.rotation.field_1350 * 0.1D + (this.rotation.field_1350 * boost.field_1350 - velocity.field_1350) * 0.5D); }  class_241 customBoost = elytraBoost.getBoostV2(); class_243 boost = new class_243(customBoost.field_1343, customBoost.field_1342, customBoost.field_1343); return velocity.method_1031(this.rotation.field_1352 * 0.1D + (this.rotation.field_1352 * boost.field_1352 - velocity.field_1352) * 0.5D, this.rotation.field_1351 * 0.1D + (this.rotation.field_1351 * boost.field_1351 - velocity.field_1351) * 0.5D, this.rotation.field_1350 * 0.1D + (this.rotation.field_1350 * boost.field_1350 - velocity.field_1350) * 0.5D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Unique
/*     */   private class_243 defaultBoost(class_243 velocity) {
/* 110 */     return velocity.method_1031(this.rotation.field_1352 * 0.1D + (this.rotation.field_1352 * 1.5D - velocity.field_1352) * 0.5D, this.rotation.field_1351 * 0.1D + (this.rotation.field_1351 * 1.5D - velocity.field_1351) * 0.5D, this.rotation.field_1350 * 0.1D + (this.rotation.field_1350 * 1.5D - velocity.field_1350) * 0.5D);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\mixin\FireWorkRocketEntityMixin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */