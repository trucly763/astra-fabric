/*    */ package shame.astra.mixin;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import net.minecraft.class_1297;
/*    */ import net.minecraft.class_156;
/*    */ import net.minecraft.class_310;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.Unique;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*    */ import shame.astra.api.events.Event;
/*    */ import shame.astra.api.events.EventInvoker;
/*    */ import shame.astra.api.events.implement.EventGameUpdate;
/*    */ import shame.astra.api.events.implement.EventTickPost;
/*    */ import shame.astra.api.events.implement.EventTickPre;
/*    */ import shame.astra.api.storages.implement.helpertstorages.enumvar.ModuleClass;
/*    */ import shame.astra.api.utils.baritone.BaritoneAntiStuck;
/*    */ import shame.astra.api.utils.player.Counter;
/*    */ import shame.astra.client.modules.impl.render.ShaderEsp;
/*    */ 
/*    */ @Mixin({class_310.class})
/*    */ public abstract class MinecraftClientMixin
/*    */ {
/*    */   @Unique
/* 27 */   private long lastHookTime = class_156.method_648(); @Unique
/* 28 */   private int accumulatedCalls = 0;
/*    */ 
/*    */   
/*    */   @Inject(method = {"method_1574"}, at = {@At("HEAD")})
/*    */   public void tick(CallbackInfo ci) throws InvocationTargetException, IllegalAccessException, InstantiationException {
/* 33 */     if (EventInvoker.hasListeners(EventTickPre.class)) {
/* 34 */       EventTickPre event = new EventTickPre();
/* 35 */       EventInvoker.invoke((Event)event);
/*    */     } 
/* 37 */     Counter.updateFPS();
/*    */   }
/*    */   
/*    */   @Inject(method = {"method_1574"}, at = {@At("RETURN")})
/*    */   public void tickEnd(CallbackInfo ci) throws InvocationTargetException, IllegalAccessException, InstantiationException {
/* 42 */     if (EventInvoker.hasListeners(EventTickPost.class)) {
/* 43 */       EventTickPost event = new EventTickPost();
/* 44 */       EventInvoker.invoke((Event)event);
/*    */     } 
/* 46 */     BaritoneAntiStuck.tick();
/*    */   }
/*    */   
/*    */   @Inject(method = {"method_1523"}, at = {@At("HEAD")})
/*    */   private void render(boolean tick, CallbackInfo ci) throws InvocationTargetException, IllegalAccessException, InstantiationException {
/* 51 */     if (!EventInvoker.hasListeners(EventGameUpdate.class)) {
/* 52 */       this.lastHookTime = class_156.method_648();
/* 53 */       this.accumulatedCalls = 0;
/*    */       
/*    */       return;
/*    */     } 
/* 57 */     long now = class_156.method_648();
/* 58 */     long delta = now - this.lastHookTime;
/* 59 */     this.accumulatedCalls += (int)(delta / 4166666L);
/* 60 */     this.lastHookTime += this.accumulatedCalls * 4166666L;
/*    */     
/* 62 */     for (this.accumulatedCalls = Math.min(this.accumulatedCalls, 240); this.accumulatedCalls > 0; this.accumulatedCalls--) {
/* 63 */       EventInvoker.invoke((Event)new EventGameUpdate());
/*    */     }
/*    */   }
/*    */   
/*    */   @Inject(method = {"method_27022"}, at = {@At("HEAD")}, cancellable = true)
/*    */   private void astra$hasOutline(class_1297 entity, CallbackInfoReturnable<Boolean> cir) {
/* 69 */     if (ModuleClass.INSTANCE == null)
/*    */       return; 
/* 71 */     ShaderEsp shaderEsp = ModuleClass.shaderEsp;
/* 72 */     if (shaderEsp != null && shaderEsp.shouldOutline(entity)) {
/* 73 */       cir.setReturnValue(Boolean.valueOf(true));
/*    */       return;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\mixin\MinecraftClientMixin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */