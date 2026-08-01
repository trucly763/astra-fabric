/*    */ package shame.astra.mixin;
/*    */ 
/*    */ import net.minecraft.class_1297;
/*    */ import net.minecraft.class_1657;
/*    */ import net.minecraft.class_636;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ import shame.astra.api.events.Event;
/*    */ import shame.astra.api.events.EventInvoker;
/*    */ import shame.astra.api.events.implement.EventAttackEntity;
/*    */ 
/*    */ @Mixin({class_636.class})
/*    */ public abstract class ClientPlayerInteractionManagerMixin {
/*    */   @Inject(method = {"method_2918"}, at = {@At("HEAD")}, cancellable = true)
/*    */   public void attackEntity(class_1657 player, class_1297 target, CallbackInfo ci) {
/*    */     try {
/* 19 */       if (player != null && target != null) {
/* 20 */         EventAttackEntity event = new EventAttackEntity(player, target);
/* 21 */         EventInvoker.invoke((Event)event);
/* 22 */         if (event.isCancelled()) ci.cancel(); 
/*    */       } 
/* 24 */     } catch (Exception exception) {}
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\mixin\ClientPlayerInteractionManagerMixin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */