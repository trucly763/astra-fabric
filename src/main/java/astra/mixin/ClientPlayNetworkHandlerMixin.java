/*    */ package shame.astra.mixin;
/*    */ 
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.class_10264;
/*    */ import net.minecraft.class_124;
/*    */ import net.minecraft.class_2596;
/*    */ import net.minecraft.class_2664;
/*    */ import net.minecraft.class_2678;
/*    */ import net.minecraft.class_2743;
/*    */ import net.minecraft.class_310;
/*    */ import net.minecraft.class_634;
/*    */ import net.minecraft.class_638;
/*    */ import net.minecraft.class_7439;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.Shadow;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ import shame.astra.api.events.implement.EventPacket;
/*    */ import shame.astra.api.utils.baritone.BaritoneAntiStuck;
/*    */ import shame.astra.api.utils.bot.BotSessionManager;
/*    */ import shame.astra.api.utils.chat.ChatUtils;
/*    */ import shame.astra.astra;
/*    */ 
/*    */ @Mixin({class_634.class})
/*    */ public abstract class ClientPlayNetworkHandlerMixin {
/*    */   @Shadow
/*    */   private class_638 field_3699;
/*    */   
/*    */   @Inject(method = {"method_45729"}, at = {@At("HEAD")}, cancellable = true)
/*    */   public void sendChatMessage(@NotNull String message, CallbackInfo ci) {
/* 33 */     if (message.startsWith(astra.INSTANCE.commandStorage.getPrefix())) {
/*    */       try {
/* 35 */         astra.INSTANCE.commandStorage.getDispatcher().execute(message.substring(astra.INSTANCE.commandStorage.getPrefix().length()), astra.INSTANCE.commandStorage.getSource());
/* 36 */       } catch (CommandSyntaxException e) {
/* 37 */         ChatUtils.sendMessage(String.valueOf(class_124.field_1061) + "Ошибка в использовании!");
/*    */       } 
/* 39 */       ci.cancel();
/*    */       return;
/*    */     } 
/*    */   }
/*    */   
/*    */   @Inject(method = {"method_11132"}, at = {@At("HEAD")}, cancellable = true)
/*    */   private void onVelocityUpdate(class_2743 packet, CallbackInfo ci) {
/* 46 */     EventPacket event = new EventPacket((class_2596)packet, EventPacket.Type.RECEIVE);
/* 47 */     event.call();
/* 48 */     if (event.isCancelled()) {
/* 49 */       ci.cancel();
/*    */     }
/*    */   }
/*    */   
/*    */   @Inject(method = {"method_11124"}, at = {@At("HEAD")}, cancellable = true)
/*    */   private void onExplosion(class_2664 packet, CallbackInfo ci) {
/* 55 */     EventPacket event = new EventPacket((class_2596)packet, EventPacket.Type.RECEIVE);
/* 56 */     event.call();
/* 57 */     if (event.isCancelled()) {
/* 58 */       ci.cancel();
/*    */     }
/*    */   }
/*    */   
/*    */   @Inject(method = {"method_64553"}, at = {@At("HEAD")}, cancellable = true)
/*    */   private void onEntityPositionSync(class_10264 packet, CallbackInfo ci) {
/* 64 */     class_310 mc = class_310.method_1551();
/* 65 */     if (this.field_3699 == null || mc.field_1724 == null || mc.field_1687 == null) {
/* 66 */       ci.cancel();
/*    */     }
/*    */   }
/*    */   
/*    */   @Inject(method = {"method_43596"}, at = {@At("HEAD")})
/*    */   private void onGameMessage(class_7439 packet, CallbackInfo ci) {
/* 72 */     BaritoneAntiStuck.onGameMessage(packet.comp_763().getString());
/*    */   }
/*    */   
/*    */   @Inject(method = {"method_11120"}, at = {@At("HEAD")})
/*    */   private void onGameJoin(class_2678 packet, CallbackInfo ci) {
/* 77 */     BotSessionManager.finishBotConnectStage();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\mixin\ClientPlayNetworkHandlerMixin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */