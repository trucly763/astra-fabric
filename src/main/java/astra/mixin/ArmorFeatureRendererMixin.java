/*    */ package shame.astra.mixin;
/*    */ 
/*    */ import net.minecraft.class_10017;
/*    */ import net.minecraft.class_10055;
/*    */ import net.minecraft.class_1297;
/*    */ import net.minecraft.class_1657;
/*    */ import net.minecraft.class_4587;
/*    */ import net.minecraft.class_4597;
/*    */ import net.minecraft.class_970;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ import shame.astra.api.QClient;
/*    */ import shame.astra.api.storages.implement.helpertstorages.enumvar.ModuleClass;
/*    */ import shame.astra.client.modules.impl.render.Chams;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Mixin({class_970.class})
/*    */ public class ArmorFeatureRendererMixin
/*    */   implements QClient
/*    */ {
/*    */   @Inject(method = {"method_4199"}, at = {@At("HEAD")}, cancellable = true)
/*    */   private void astra$hideArmor(class_4587 matrices, class_4597 vertexConsumers, int light, class_10017 state, float limbAngle, float limbDistance, CallbackInfo ci) {
/* 27 */     if (state instanceof class_10055) { class_10055 playerState = (class_10055)state; if (ModuleClass.INSTANCE != null && mc.field_1687 != null) {
/*    */ 
/*    */ 
/*    */         
/* 31 */         Chams chams = ModuleClass.chams;
/* 32 */         if (chams == null || !chams.isEnable()) {
/*    */           return;
/*    */         }
/*    */         
/* 36 */         class_1297 entity = mc.field_1687.method_8469(playerState.field_53528);
/* 37 */         if (entity instanceof class_1657) { class_1657 player = (class_1657)entity; if (chams.shouldHideItemsAndCape(player))
/* 38 */             ci.cancel();  }
/*    */         
/*    */         return;
/*    */       }  }
/*    */   
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\mixin\ArmorFeatureRendererMixin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */