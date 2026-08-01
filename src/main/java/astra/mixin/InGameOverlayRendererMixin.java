/*    */ package shame.astra.mixin;
/*    */ 
/*    */ import net.minecraft.class_1058;
/*    */ import net.minecraft.class_4587;
/*    */ import net.minecraft.class_4597;
/*    */ import net.minecraft.class_4603;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ import shame.astra.api.storages.implement.helpertstorages.enumvar.ModuleClass;
/*    */ import shame.astra.client.modules.impl.render.Removals;
/*    */ 
/*    */ @Mixin({class_4603.class})
/*    */ public class InGameOverlayRendererMixin
/*    */ {
/*    */   @Inject(method = {"method_23070"}, at = {@At("HEAD")}, cancellable = true)
/*    */   private static void astra$renderFireOverlay(class_4587 matrices, class_4597 vertexConsumers, CallbackInfo ci) {
/* 19 */     if (ModuleClass.INSTANCE == null)
/*    */       return; 
/* 21 */     Removals removals = ModuleClass.removals;
/* 22 */     if (removals != null && removals.isEnabled("Огонь")) {
/* 23 */       ci.cancel();
/*    */     }
/*    */   }
/*    */   
/*    */   @Inject(method = {"method_23068"}, at = {@At("HEAD")}, cancellable = true)
/*    */   private static void astra$renderInWallOverlay(class_1058 sprite, class_4587 matrices, class_4597 vertexConsumers, CallbackInfo ci) {
/* 29 */     if (ModuleClass.INSTANCE == null)
/*    */       return; 
/* 31 */     Removals removals = ModuleClass.removals;
/* 32 */     if (removals != null && removals.isEnabled("Оверлей в блоке"))
/* 33 */       ci.cancel(); 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\mixin\InGameOverlayRendererMixin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */