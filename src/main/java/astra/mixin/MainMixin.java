/*    */ package shame.astra.mixin;
/*    */ 
/*    */ import net.minecraft.client.main.Main;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ import shame.astra.astra;
/*    */ 
/*    */ @Mixin({Main.class})
/*    */ public class MainMixin
/*    */ {
/*    */   @Inject(method = {"main"}, at = {@At("HEAD")})
/*    */   private static void onMain(String[] args, CallbackInfo ci) {
/* 15 */     if (astra.INSTANCE.isServer) {
/*    */       try {
/* 17 */         astra.INSTANCE.closeMinecraft();
/* 18 */       } catch (Exception e) {
/* 19 */         e.printStackTrace();
/*    */       } 
/* 21 */       astra.INSTANCE.isServer = false;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\mixin\MainMixin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */