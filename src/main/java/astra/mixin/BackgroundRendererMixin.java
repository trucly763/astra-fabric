/*    */ package shame.astra.mixin;
/*    */ import net.minecraft.class_1297;
/*    */ import net.minecraft.class_4184;
/*    */ import net.minecraft.class_6854;
/*    */ import net.minecraft.class_758;
/*    */ import net.minecraft.class_9958;
/*    */ import org.joml.Vector4f;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*    */ import shame.astra.api.storages.implement.helpertstorages.enumvar.ModuleClass;
/*    */ import shame.astra.api.utils.color.ColorUtils;
/*    */ import shame.astra.client.modules.impl.render.Removals;
/*    */ import shame.astra.client.modules.impl.render.WorldTweaks;
/*    */ 
/*    */ @Mixin({class_758.class})
/*    */ public class BackgroundRendererMixin {
/*    */   @Inject(method = {"method_42588"}, at = {@At("HEAD")}, cancellable = true)
/*    */   private static void astra$getFogModifier(class_1297 entity, float tickDelta, CallbackInfoReturnable<Object> cir) {
/* 21 */     if (ModuleClass.INSTANCE == null)
/*    */       return; 
/* 23 */     Removals removals = ModuleClass.removals;
/* 24 */     if (removals != null && removals.isEnabled("Плохие эффекты")) {
/* 25 */       cir.setReturnValue(null);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Inject(method = {"method_3211"}, at = {@At("RETURN")}, cancellable = true)
/*    */   private static void astra$applyFog(class_4184 camera, class_758.class_4596 fogType, Vector4f color, float viewDistance, boolean thickenFog, float tickDelta, CallbackInfoReturnable<class_9958> cir) {
/* 39 */     if (ModuleClass.INSTANCE == null)
/*    */       return; 
/* 41 */     WorldTweaks tweaks = ModuleClass.worldTweaks;
/* 42 */     if (tweaks == null || !tweaks.isFogEnabled())
/*    */       return; 
/* 44 */     float fogDistance = Math.max(12.0F, tweaks.getFogDistance());
/* 45 */     float fogEnd = Math.min(viewDistance, fogDistance);
/* 46 */     float fogStart = Math.max(0.0F, fogEnd * 0.05F);
/* 47 */     int color1 = tweaks.getFogColor();
/*    */     
/* 49 */     cir.setReturnValue(new class_9958(fogStart, fogEnd, class_6854.field_36350, 
/*    */ 
/*    */ 
/*    */           
/* 53 */           ColorUtils.redf(color1), 
/* 54 */           ColorUtils.greenf(color1), 
/* 55 */           ColorUtils.bluef(color1), 1.0F));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\mixin\BackgroundRendererMixin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */