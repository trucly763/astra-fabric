/*    */ package shame.astra.mixin;
/*    */ 
/*    */ import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
/*    */ import net.minecraft.class_1297;
/*    */ import net.minecraft.class_1657;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*    */ import shame.astra.api.QClient;
/*    */ import shame.astra.api.storages.implement.helpertstorages.enumvar.ModuleClass;
/*    */ import shame.astra.client.modules.impl.player.NoPush;
/*    */ import shame.astra.client.modules.impl.render.SeeInvisibles;
/*    */ import shame.astra.client.modules.impl.render.ShaderEsp;
/*    */ 
/*    */ @Mixin({class_1297.class})
/*    */ public abstract class EntityMixin
/*    */   implements QClient {
/*    */   @ModifyExpressionValue(method = {"method_5784"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_1297;method_65038()Z")})
/*    */   private boolean fixFallDistanceCalculation(boolean original) {
/* 22 */     if (this == mc.field_1724) {
/* 23 */       return false;
/*    */     }
/* 25 */     return original;
/*    */   }
/*    */   
/*    */   @Inject(method = {"method_5697"}, at = {@At("HEAD")}, cancellable = true)
/*    */   public void pushAwayFrom(CallbackInfo ci) {
/* 30 */     if (this != mc.field_1724 || ModuleClass.INSTANCE == null)
/* 31 */       return;  NoPush noPush = ModuleClass.noPush;
/* 32 */     if (noPush != null && noPush.isEnable() && noPush.getCollisionList().is("Игроки")) {
/* 33 */       ci.cancel();
/*    */     }
/*    */   }
/*    */   
/*    */   @Inject(method = {"method_5675"}, at = {@At("RETURN")}, cancellable = true)
/*    */   public void isPushedByFluids(CallbackInfoReturnable<Boolean> ci) {
/* 39 */     if (this != mc.field_1724 || ModuleClass.INSTANCE == null)
/* 40 */       return;  NoPush noPush = ModuleClass.noPush;
/* 41 */     if (noPush != null && noPush.isEnable() && noPush.getCollisionList().is("Вода")) {
/* 42 */       ci.setReturnValue(Boolean.valueOf(false));
/*    */     }
/*    */   }
/*    */   
/*    */   @Inject(method = {"method_22861"}, at = {@At("HEAD")}, cancellable = true)
/*    */   private void astra$getTeamColorValue(CallbackInfoReturnable<Integer> cir) {
/* 48 */     if (ModuleClass.INSTANCE == null)
/*    */       return; 
/* 50 */     ShaderEsp shaderEsp = ModuleClass.shaderEsp;
/* 51 */     if (shaderEsp != null && shaderEsp.shouldOutline((class_1297)this)) {
/* 52 */       cir.setReturnValue(Integer.valueOf(shaderEsp.getOutlineColor()));
/*    */     }
/*    */   }
/*    */   
/*    */   @Inject(method = {"method_5756"}, at = {@At("HEAD")}, cancellable = true)
/*    */   private void astra$allowSeeInvisibles(class_1657 player, CallbackInfoReturnable<Boolean> cir) {
/* 58 */     EntityMixin entityMixin = this; if (entityMixin instanceof class_1657) { class_1657 target = (class_1657)entityMixin; if (ModuleClass.INSTANCE != null) {
/*    */ 
/*    */ 
/*    */         
/* 62 */         SeeInvisibles seeInvisibles = ModuleClass.seeInvisibles;
/* 63 */         if (seeInvisibles != null && seeInvisibles.shouldRenderInvisible(target))
/* 64 */           cir.setReturnValue(Boolean.valueOf(false)); 
/*    */         return;
/*    */       }  }
/*    */   
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\mixin\EntityMixin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */