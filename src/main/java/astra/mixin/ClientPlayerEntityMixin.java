/*     */ package shame.astra.mixin;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import net.minecraft.class_1313;
/*     */ import net.minecraft.class_1657;
/*     */ import net.minecraft.class_1937;
/*     */ import net.minecraft.class_2338;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_2596;
/*     */ import net.minecraft.class_304;
/*     */ import net.minecraft.class_746;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.spongepowered.asm.mixin.Final;
/*     */ import org.spongepowered.asm.mixin.Shadow;
/*     */ import org.spongepowered.asm.mixin.injection.At;
/*     */ import org.spongepowered.asm.mixin.injection.Inject;
/*     */ import org.spongepowered.asm.mixin.injection.Redirect;
/*     */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*     */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*     */ import shame.astra.api.QClient;
/*     */ import shame.astra.api.events.EventInvoker;
/*     */ import shame.astra.api.events.implement.EventCloseInv;
/*     */ import shame.astra.api.events.implement.EventMove;
/*     */ import shame.astra.api.events.implement.EventSlowWalking;
/*     */ import shame.astra.api.events.implement.EventSprint;
/*     */ import shame.astra.api.events.implement.EventUpdate;
/*     */ import shame.astra.api.events.implement.EventUpdatePost;
/*     */ import shame.astra.api.storages.implement.RotationStorage;
/*     */ import shame.astra.api.storages.implement.helpertstorages.enumvar.ModuleClass;
/*     */ import shame.astra.api.utils.player.ViaProtocolUtils;
/*     */ import shame.astra.client.modules.impl.combat.Aura;
/*     */ 
/*     */ @Mixin({class_746.class})
/*     */ public abstract class ClientPlayerEntityMixin extends class_1657 implements QClient {
/*     */   public ClientPlayerEntityMixin(class_1937 world, class_2338 pos, float yaw, GameProfile gameProfile) {
/*  35 */     super(world, pos, yaw, gameProfile);
/*     */   }
/*     */   
/*     */   @Shadow
/*     */   @Final
/*     */   public class_634 field_3944;
/*     */   
/*     */   @Shadow
/*     */   public abstract void method_3137();
/*     */   
/*     */   @Inject(method = {"method_5773"}, at = {@At(value = "HEAD", target = "Lnet/minecraft/class_742;method_5773()V")})
/*     */   private void onTick(CallbackInfo ci) {
/*  47 */     if (EventInvoker.hasListeners(EventUpdate.class)) {
/*  48 */       (new EventUpdate()).call();
/*     */     }
/*     */   }
/*     */   
/*     */   @Inject(method = {"method_5773"}, at = {@At(value = "TAIL", target = "Lnet/minecraft/class_742;method_5773()V")})
/*     */   private void onTickPost(CallbackInfo ci) {
/*  54 */     if (EventInvoker.hasListeners(EventUpdatePost.class)) {
/*  55 */       (new EventUpdatePost()).call();
/*     */     }
/*     */     
/*  58 */     if (shouldSyncRotation()) {
/*  59 */       this.field_6241 = method_36454();
/*  60 */       this.field_6259 = method_36454();
/*  61 */       this.field_6283 = method_36454();
/*  62 */       this.field_6220 = method_36454();
/*     */     } 
/*     */   }
/*     */   
/*     */   @Unique
/*     */   private boolean shouldSyncRotation() {
/*  68 */     return (ModuleClass.aura.isEnable() && Aura.clientLook
/*  69 */       .isState() && RotationStorage.instance
/*  70 */       .isRotating());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Redirect(method = {"method_6007"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/class_304;method_1434()Z", ordinal = 1), require = 0)
/*     */   private boolean onSprintKeyPressed(class_304 instance) {
/*  79 */     if (ViaProtocolUtils.isTargetProtocolBelowOneNineteen() && (this.field_5976 || this.field_34927)) {
/*  80 */       return false;
/*     */     }
/*     */     
/*  83 */     EventSprint event = new EventSprint();
/*  84 */     event.call();
/*  85 */     if (event.isCancelled()) {
/*  86 */       return false;
/*     */     }
/*  88 */     return instance.method_1434();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Redirect(method = {"method_6007"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/class_746;method_6115()Z"), require = 0)
/*     */   private boolean onSlowDownRedirect(class_746 player) {
/* 100 */     if (player.method_6115()) {
/* 101 */       EventSlowWalking event = new EventSlowWalking();
/* 102 */       event.call();
/* 103 */       return (player.method_6115() && player.method_5854() == null && !event.isCancelled());
/*     */     } 
/* 105 */     return (player.method_6115() && player.method_5854() == null);
/*     */   }
/*     */   
/*     */   @Inject(method = {"method_30673"}, at = {@At("HEAD")}, cancellable = true)
/*     */   public void pushOutOfBlocks(double x, double z, CallbackInfo ci) {
/* 110 */     if (ModuleClass.noPush.isEnable() && ModuleClass.noPush.getCollisionList().is("Блоки")) {
/* 111 */       ci.cancel();
/*     */     }
/*     */   }
/*     */   
/*     */   @Inject(method = {"method_5784"}, at = {@At("HEAD")}, cancellable = true)
/*     */   private void onMoveHook(class_1313 movementType, class_243 movement, @NotNull CallbackInfo ci) {
/* 117 */     EventMove event = new EventMove(movement);
/* 118 */     event.call();
/*     */     
/* 120 */     if (!event.isCancelled() && event.getMovePos().equals(movement)) {
/*     */       return;
/*     */     }
/*     */     
/* 124 */     if (event.isCancelled()) {
/* 125 */       ci.cancel();
/*     */       
/*     */       return;
/*     */     } 
/* 129 */     double d = method_23317();
/* 130 */     double e = method_23321();
/* 131 */     method_5784(movementType, event.getMovePos());
/* 132 */     float f = (float)Math.sqrt(Math.pow(method_23317() - d, 2.0D) + Math.pow(method_23321() - e, 2.0D));
/* 133 */     method_48565(f);
/* 134 */     ci.cancel();
/*     */   }
/*     */   
/*     */   @Inject(method = {"method_7346"}, at = {@At("HEAD")}, cancellable = true)
/*     */   private void onCloseHandledScreen(CallbackInfo ci) {
/* 139 */     int syncId = this.field_7512.field_7763;
/* 140 */     EventCloseInv event = new EventCloseInv(syncId);
/* 141 */     event.call();
/* 142 */     if (!event.isCancelled()) {
/* 143 */       this.field_3944.method_52787((class_2596)new class_2815(syncId));
/*     */     }
/* 145 */     method_3137();
/* 146 */     ci.cancel();
/*     */   }
/*     */   
/*     */   @Inject(method = {"method_7290"}, at = {@At("HEAD")}, cancellable = true)
/*     */   private void onDropSelectedItem(boolean entireStack, CallbackInfoReturnable<Boolean> cir) {
/* 151 */     if (ModuleClass.lockSlot != null && ModuleClass.lockSlot.isCurrentSlotLockedForDrop())
/* 152 */       cir.setReturnValue(Boolean.valueOf(false)); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\mixin\ClientPlayerEntityMixin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */