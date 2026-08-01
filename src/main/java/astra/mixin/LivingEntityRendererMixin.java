/*    */ package shame.astra.mixin;
/*    */ 
/*    */ import net.minecraft.class_10042;
/*    */ import net.minecraft.class_10055;
/*    */ import net.minecraft.class_1297;
/*    */ import net.minecraft.class_1309;
/*    */ import net.minecraft.class_1657;
/*    */ import net.minecraft.class_4587;
/*    */ import net.minecraft.class_4597;
/*    */ import net.minecraft.class_583;
/*    */ import net.minecraft.class_922;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.Unique;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Constant;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.ModifyConstant;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ import shame.astra.api.QClient;
/*    */ import shame.astra.api.storages.implement.helpertstorages.enumvar.ModuleClass;
/*    */ import shame.astra.client.modules.impl.render.SeeInvisibles;
/*    */ import shame.astra.client.modules.impl.render.SeeInvisiblesRenderState;
/*    */ 
/*    */ 
/*    */ 
/*    */ @Mixin({class_922.class})
/*    */ public abstract class LivingEntityRendererMixin<T extends class_1309, S extends class_10042, M extends class_583<? super S>>
/*    */   implements QClient
/*    */ {
/*    */   @Inject(method = {"method_62355"}, at = {@At("TAIL")})
/*    */   private void astra$updateSeeInvisiblesState(T entity, S state, float tickDelta, CallbackInfo ci) {
/* 32 */     boolean shouldRenderInvisible = astra$shouldRenderInvisible(entity);
/* 33 */     ((SeeInvisiblesRenderState)state).astra$setSeeInvisiblesTarget(shouldRenderInvisible);
/* 34 */     if (shouldRenderInvisible) {
/* 35 */       ((class_10042)state).field_53333 = true;
/* 36 */       ((class_10042)state).field_53461 = false;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @ModifyConstant(method = {"method_4054"}, constant = {@Constant(intValue = 654311423)})
/*    */   private int astra$changeInvisibleAlpha(int original, S state, class_4587 matrices, class_4597 vertexConsumers, int light) {
/* 45 */     return ((SeeInvisiblesRenderState)state).astra$isSeeInvisiblesTarget() ? 
/* 46 */       SeeInvisibles.INVISIBLE_COLOR : 
/* 47 */       original;
/*    */   }
/*    */   
/*    */   @Unique
/*    */   private boolean astra$shouldRenderInvisible(T entity) {
/* 52 */     if (entity instanceof class_1657) { class_1657 player = (class_1657)entity; if (ModuleClass.INSTANCE != null) {
/*    */ 
/*    */ 
/*    */         
/* 56 */         SeeInvisibles seeInvisibles = ModuleClass.seeInvisibles;
/* 57 */         return (seeInvisibles != null && seeInvisibles.shouldRenderInvisible(player));
/*    */       }  }
/*    */     
/*    */     return false; } @Unique
/*    */   private class_1657 astra$resolvePlayer(S state) {
/* 62 */     if (state instanceof class_10055) { class_10055 playerState = (class_10055)state; if (mc.field_1687 != null) {
/*    */ 
/*    */ 
/*    */         
/* 66 */         class_1297 entity = mc.field_1687.method_8469(playerState.field_53528);
/* 67 */         class_1657 player = (class_1657)entity; return (entity instanceof class_1657) ? player : null;
/*    */       }  }
/*    */     
/*    */     return null;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\mixin\LivingEntityRendererMixin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */