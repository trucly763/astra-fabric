/*    */ package shame.astra.mixin;
/*    */ 
/*    */ import net.minecraft.class_10185;
/*    */ import net.minecraft.class_310;
/*    */ import net.minecraft.class_743;
/*    */ import net.minecraft.class_744;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ import shame.astra.api.events.EventInvoker;
/*    */ import shame.astra.api.events.implement.EventMoveInput;
/*    */ 
/*    */ @Mixin({class_743.class})
/*    */ public abstract class KeyboardInputMixin extends class_744 {
/* 16 */   private static final class_310 mc = class_310.method_1551();
/*    */   
/*    */   @Inject(method = {"method_3129"}, at = {@At("TAIL")})
/*    */   private void onTickTail(CallbackInfo ci) {
/* 20 */     if (!EventInvoker.hasListeners(EventMoveInput.class)) {
/*    */       return;
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 28 */     EventMoveInput eventInput = new EventMoveInput(this.field_3905, this.field_3907, this.field_54155.comp_3163(), this.field_54155.comp_3164());
/*    */     
/* 30 */     eventInput.call();
/*    */     
/* 32 */     float forward = eventInput.getForward();
/* 33 */     float strafe = eventInput.getStrafe();
/*    */     
/* 35 */     this
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 42 */       .field_54155 = new class_10185((forward > 0.0F), (forward < 0.0F), (strafe > 0.0F), (strafe < 0.0F), eventInput.isJump(), eventInput.isSneak(), this.field_54155.comp_3165());
/*    */     
/* 44 */     this.field_3905 = forward;
/* 45 */     this.field_3907 = strafe;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\mixin\KeyboardInputMixin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */