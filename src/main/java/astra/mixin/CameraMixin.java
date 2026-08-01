/*    */ package shame.astra.mixin;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import net.minecraft.class_1297;
/*    */ import net.minecraft.class_1922;
/*    */ import net.minecraft.class_4184;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Redirect;
/*    */ import shame.astra.api.events.Event;
/*    */ import shame.astra.api.events.EventInvoker;
/*    */ import shame.astra.api.events.implement.EventRotation;
/*    */ import shame.astra.api.storages.implement.helpertstorages.enumvar.ModuleClass;
/*    */ import shame.astra.client.modules.impl.render.InterpolateF5;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Mixin({class_4184.class})
/*    */ public abstract class CameraMixin
/*    */ {
/*    */   @Redirect(method = {"method_19321"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/class_4184;method_19325(FF)V"))
/*    */   private void redirectSetRotation(class_4184 instance, float yaw, float pitch, class_1922 area, class_1297 focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta) throws InvocationTargetException, IllegalAccessException, InstantiationException {
/* 27 */     EventRotation event = new EventRotation(yaw, pitch, tickDelta);
/* 28 */     EventInvoker.invoke((Event)event);
/*    */     
/* 30 */     float newYaw = event.getYaw();
/* 31 */     float newPitch = event.getPitch();
/*    */     
/* 33 */     if (thirdPerson && inverseView) {
/* 34 */       newYaw += 180.0F;
/* 35 */       newPitch = -newPitch;
/*    */     } 
/*    */     
/* 38 */     ((ICameraMixin)instance).setCustomRotation(newYaw, newPitch);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Redirect(method = {"method_19321"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/class_4184;method_19318(F)F"))
/*    */   private float redirectClipToSpace(class_4184 instance, float distance, class_1922 area, class_1297 focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta) {
/* 49 */     if (!thirdPerson) {
/* 50 */       return ((ICameraMixin)instance).setClipToSpace(distance);
/*    */     }
/*    */     
/* 53 */     InterpolateF5 module = (ModuleClass.INSTANCE != null) ? ModuleClass.interpolateF5 : null;
/* 54 */     if (module != null && module.isEnable()) {
/* 55 */       return ((ICameraMixin)instance).setClipToSpace(module.getInterpolatedDistance(tickDelta));
/*    */     }
/*    */     
/* 58 */     return ((ICameraMixin)instance).setClipToSpace(distance);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Redirect(method = {"method_19321"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/class_4184;method_19324(FFF)V"))
/*    */   private void redirectMoveBy(class_4184 instance, float x, float y, float z, class_1922 area, class_1297 focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta) {
/* 69 */     float newY = y;
/*    */     
/* 71 */     if (thirdPerson) {
/* 72 */       InterpolateF5 module = (ModuleClass.INSTANCE != null) ? ModuleClass.interpolateF5 : null;
/* 73 */       if (module != null && module.isEnable()) {
/* 74 */         newY += module.getInterpolatedHeightOffset(tickDelta);
/*    */       }
/*    */     } 
/*    */     
/* 78 */     ((ICameraMixin)instance).setCustomMoveBy(x, newY, z);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\mixin\CameraMixin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */