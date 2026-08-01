/*    */ package shame.astra.mixin;
/*    */ 
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import net.minecraft.class_2535;
/*    */ import net.minecraft.class_2596;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ import shame.astra.api.events.Event;
/*    */ import shame.astra.api.events.EventInvoker;
/*    */ import shame.astra.api.events.implement.EventPacket;
/*    */ import shame.astra.api.utils.network.NetworkUtils;
/*    */ 
/*    */ @Mixin({class_2535.class})
/*    */ public abstract class ClientConnectionMixin {
/*    */   @Inject(method = {"method_10770"}, at = {@At("HEAD")}, cancellable = true)
/*    */   public void channelRead0(ChannelHandlerContext channelHandlerContext, class_2596<?> packet, CallbackInfo ci) throws InvocationTargetException, IllegalAccessException, InstantiationException {
/* 20 */     EventPacket eventReceive = new EventPacket(packet, EventPacket.Type.RECEIVE);
/* 21 */     EventInvoker.invoke((Event)eventReceive);
/* 22 */     if (eventReceive.isCancelled()) ci.cancel(); 
/*    */   }
/*    */   
/*    */   @Inject(method = {"method_10743"}, at = {@At("HEAD")}, cancellable = true)
/*    */   public void send(class_2596<?> packet, CallbackInfo ci) throws InvocationTargetException, IllegalAccessException, InstantiationException {
/* 27 */     if (NetworkUtils.getSilentPackets().contains(packet)) {
/* 28 */       NetworkUtils.getSilentPackets().remove(packet);
/*    */       
/*    */       return;
/*    */     } 
/* 32 */     EventPacket eventSend = new EventPacket(packet, EventPacket.Type.SEND);
/* 33 */     EventInvoker.invoke((Event)eventSend);
/* 34 */     if (eventSend.isCancelled()) ci.cancel(); 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\mixin\ClientConnectionMixin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */