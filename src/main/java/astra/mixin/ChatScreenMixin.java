/*    */ package shame.astra.mixin;
/*    */ 
/*    */ import net.minecraft.class_1041;
/*    */ import net.minecraft.class_310;
/*    */ import net.minecraft.class_332;
/*    */ import net.minecraft.class_408;
/*    */ import org.lwjgl.glfw.GLFW;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.Unique;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*    */ import shame.astra.api.storages.implement.DragStorage;
/*    */ import shame.astra.api.utils.draggable.Draggable;
/*    */ import shame.astra.astra;
/*    */ 
/*    */ @Mixin({class_408.class})
/*    */ public class ChatScreenMixin
/*    */ {
/*    */   @Unique
/*    */   private boolean astra$leftPressed;
/*    */   
/*    */   @Inject(method = {"method_25402"}, at = {@At("HEAD")}, cancellable = true)
/*    */   private void onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
/* 26 */     for (Draggable draggable : DragStorage.draggables.values()) {
/* 27 */       if (draggable.getModule().isEnable() && draggable.onClick(mouseX, mouseY, button)) {
/* 28 */         cir.setReturnValue(Boolean.valueOf(true));
/*    */         return;
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   @Inject(method = {"method_25394"}, at = {@At("HEAD")})
/*    */   private void onRender(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
/* 36 */     class_310 mc = class_310.method_1551();
/* 37 */     class_1041 window = mc.method_22683();
/*    */     
/* 39 */     boolean leftPressed = (GLFW.glfwGetMouseButton(mc.method_22683().method_4490(), 0) == 1);
/* 40 */     if (this.astra$leftPressed && !leftPressed) {
/* 41 */       for (Draggable draggable : DragStorage.draggables.values()) {
/* 42 */         draggable.onRelease(0);
/*    */       }
/*    */     }
/* 45 */     this.astra$leftPressed = leftPressed;
/*    */     
/* 47 */     for (Draggable draggable : DragStorage.draggables.values()) {
/* 48 */       if (draggable.getModule().isEnable()) {
/* 49 */         draggable.onDraw(mouseX, mouseY, window, context.method_51448());
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   @Inject(method = {"method_25432"}, at = {@At("HEAD")})
/*    */   private void onRemoved(CallbackInfo ci) {
/* 57 */     this.astra$leftPressed = false;
/* 58 */     for (Draggable draggable : DragStorage.draggables.values()) {
/* 59 */       draggable.onRelease(0);
/*    */     }
/*    */     try {
/* 62 */       astra.INSTANCE.configStorage.saveConfig(astra.INSTANCE.configStorage.currentConfig);
/* 63 */     } catch (Exception e) {
/* 64 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\mixin\ChatScreenMixin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */