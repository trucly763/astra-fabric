/*    */ package shame.astra.mixin;
/*    */ 
/*    */ import net.minecraft.class_1799;
/*    */ import net.minecraft.class_1802;
/*    */ import net.minecraft.class_757;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ import shame.astra.api.storages.implement.helpertstorages.enumvar.ModuleClass;
/*    */ import shame.astra.client.modules.impl.render.Removals;
/*    */ 
/*    */ @Mixin({class_757.class})
/*    */ public class GameRendererMixin
/*    */ {
/*    */   @Inject(method = {"method_3189"}, at = {@At("HEAD")}, cancellable = true)
/*    */   private void astra$hideTotemAnimation(class_1799 stack, CallbackInfo ci) {
/* 18 */     if (ModuleClass.INSTANCE == null || stack == null || !stack.method_31574(class_1802.field_8288)) {
/*    */       return;
/*    */     }
/*    */     
/* 22 */     Removals removals = ModuleClass.removals;
/* 23 */     if (removals != null && removals.isTotemAnimationDisabled())
/* 24 */       ci.cancel(); 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\mixin\GameRendererMixin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */