/*    */ package shame.astra.mixin;
/*    */ 
/*    */ import net.minecraft.class_310;
/*    */ import net.minecraft.class_312;
/*    */ import net.minecraft.class_3540;
/*    */ import org.spongepowered.asm.mixin.Final;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.Shadow;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ import shame.astra.api.events.Event;
/*    */ import shame.astra.api.events.EventInvoker;
/*    */ import shame.astra.api.events.implement.EventLook;
/*    */ import shame.astra.api.utils.input.KeyBoardUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ @Mixin({class_312.class})
/*    */ public abstract class MouseMixin
/*    */ {
/*    */   @Shadow
/*    */   @Final
/*    */   private class_310 field_1779;
/*    */   @Shadow
/*    */   private double field_1789;
/*    */   
/*    */   @Inject(method = {"method_1601"}, at = {@At("HEAD")}, cancellable = false)
/*    */   private void onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
/*    */     try {
/* 31 */       if (this.field_1779.field_1724 == null)
/*    */         return; 
/* 33 */       int buttonId = button;
/* 34 */       int actionId = (action == 1) ? 1 : 0;
/* 35 */       KeyBoardUtils.callMouse(buttonId, actionId);
/*    */ 
/*    */     
/*    */     }
/* 39 */     catch (Exception exception) {} } @Shadow
/*    */   private double field_1787; @Shadow
/*    */   private class_3540 field_1793; @Shadow
/*    */   private class_3540 field_1782; @Inject(method = {"method_1606"}, at = {@At("HEAD")}, cancellable = true)
/*    */   private void onUpdateMouse(double timeDelta, CallbackInfo ci) {
/*    */     try {
/*    */       double i, j;
/* 46 */       if (this.field_1779.field_1724 == null)
/*    */         return; 
/* 48 */       double sensitivity = ((Double)this.field_1779.field_1690.method_42495().method_41753()).doubleValue() * 0.6D + 0.2D;
/* 49 */       double scaled = sensitivity * sensitivity * sensitivity * 8.0D;
/*    */ 
/*    */       
/* 52 */       if (this.field_1779.field_1690.field_1914) {
/* 53 */         i = this.field_1793.method_15429(this.field_1789 * scaled, timeDelta * scaled);
/* 54 */         j = this.field_1782.method_15429(this.field_1787 * scaled, timeDelta * scaled);
/* 55 */       } else if (this.field_1779.field_1690.method_31044().method_31034() && this.field_1779.field_1724.method_31550()) {
/* 56 */         this.field_1793.method_15428();
/* 57 */         this.field_1782.method_15428();
/* 58 */         i = this.field_1789 * sensitivity * sensitivity * sensitivity;
/* 59 */         j = this.field_1787 * sensitivity * sensitivity * sensitivity;
/*    */       } else {
/* 61 */         this.field_1793.method_15428();
/* 62 */         this.field_1782.method_15428();
/* 63 */         i = this.field_1789 * scaled;
/* 64 */         j = this.field_1787 * scaled;
/*    */       } 
/*    */       
/* 67 */       int invert = ((Boolean)this.field_1779.field_1690.method_42438().method_41753()).booleanValue() ? -1 : 1;
/*    */       
/* 69 */       EventLook event = new EventLook(i, j * invert);
/* 70 */       EventInvoker.invoke((Event)event);
/*    */       
/* 72 */       if (!event.isCancelled()) {
/* 73 */         this.field_1779.method_1577().method_4908(event.getYaw(), event.getPitch());
/* 74 */         this.field_1779.field_1724.method_5872(event.getYaw(), event.getPitch());
/*    */       } 
/*    */       
/* 77 */       this.field_1789 = 0.0D;
/* 78 */       this.field_1787 = 0.0D;
/*    */       
/* 80 */       ci.cancel();
/* 81 */     } catch (Exception exception) {}
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\mixin\MouseMixin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */