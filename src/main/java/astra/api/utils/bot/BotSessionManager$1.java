/*     */ package shame.astra.api.utils.bot;
/*     */ 
/*     */ import io.netty.channel.ChannelDuplexHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import net.minecraft.class_10264;
/*     */ import net.minecraft.class_2596;
/*     */ import net.minecraft.class_2670;
/*     */ import net.minecraft.class_2708;
/*     */ import net.minecraft.class_2720;
/*     */ import net.minecraft.class_2749;
/*     */ import net.minecraft.class_2793;
/*     */ import net.minecraft.class_2827;
/*     */ import net.minecraft.class_2828;
/*     */ import net.minecraft.class_2856;
/*     */ import net.minecraft.class_634;
/*     */ import net.minecraft.class_6373;
/*     */ import net.minecraft.class_6374;
/*     */ import net.minecraft.class_746;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class null
/*     */   extends ChannelDuplexHandler
/*     */ {
/*     */   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
/* 328 */     if (msg instanceof class_2670) { class_2670 packet = (class_2670)msg;
/* 329 */       handler.method_48296().method_10743((class_2596)new class_2827(packet.method_11517()));
/* 330 */       if (botPlayer != null) {
/* 331 */         handler.method_52787((class_2596)new class_2828.class_5911(botPlayer.method_24828(), botPlayer.field_5976));
/*     */       }
/* 333 */       ReferenceCountUtil.release(msg);
/*     */       
/*     */       return; }
/*     */     
/* 337 */     if (msg instanceof class_6373) { class_6373 packet = (class_6373)msg;
/* 338 */       handler.method_48296().method_10743((class_2596)new class_6374(packet.method_36950()));
/* 339 */       ReferenceCountUtil.release(msg);
/*     */       
/*     */       return; }
/*     */     
/* 343 */     if (msg instanceof class_2720) { class_2720 packet = (class_2720)msg;
/* 344 */       handler.method_52787((class_2596)new class_2856(packet.comp_2158(), class_2856.class_2857.field_13016));
/* 345 */       handler.method_52787((class_2596)new class_2856(packet.comp_2158(), class_2856.class_2857.field_13017));
/* 346 */       ReferenceCountUtil.release(msg);
/*     */       
/*     */       return; }
/*     */     
/* 350 */     if (msg instanceof class_2708) { class_2708 packet = (class_2708)msg;
/* 351 */       BotSessionManager.applyFrozenPositionLook(botPlayer, packet);
/* 352 */       handler.method_52787((class_2596)new class_2793(packet.comp_3133()));
/* 353 */       if (botPlayer != null) {
/* 354 */         handler.method_52787((class_2596)new class_2828.class_2830(botPlayer
/* 355 */               .method_23317(), botPlayer
/* 356 */               .method_23318(), botPlayer
/* 357 */               .method_23321(), botPlayer
/* 358 */               .method_36454(), botPlayer
/* 359 */               .method_36455(), botPlayer
/* 360 */               .method_24828(), botPlayer.field_5976));
/*     */       }
/*     */ 
/*     */       
/* 364 */       ReferenceCountUtil.release(msg);
/*     */       
/*     */       return; }
/*     */     
/* 368 */     if (msg instanceof class_10264) { class_10264 packet = (class_10264)msg;
/* 369 */       BotSessionManager.applyFrozenEntityPositionSync(botPlayer, packet);
/* 370 */       ReferenceCountUtil.release(msg);
/*     */       
/*     */       return; }
/*     */     
/* 374 */     if (msg instanceof class_2749) { class_2749 packet = (class_2749)msg;
/* 375 */       if (botPlayer != null) {
/* 376 */         botPlayer.method_6033(packet.method_11833());
/*     */       }
/* 378 */       ReferenceCountUtil.release(msg);
/*     */       
/*     */       return; }
/*     */     
/* 382 */     if (msg instanceof net.minecraft.class_2661) {
/* 383 */       BotSessionManager.connections.removeIf(bot -> BotSessionManager.matchesName(bot.name(), name));
/* 384 */       ctx.close();
/* 385 */       ReferenceCountUtil.release(msg);
/*     */       
/*     */       return;
/*     */     } 
/* 389 */     String packetName = msg.getClass().getSimpleName();
/* 390 */     if (packetName.contains("Sound") || packetName
/* 391 */       .contains("Particle") || packetName
/* 392 */       .contains("Screen") || (BotSessionManager.ignoreBotMessages && 
/* 393 */       BotSessionManager.isBotMessagePacket(packetName)) || packetName
/* 394 */       .contains("Explosion") || packetName
/* 395 */       .contains("BossBar") || packetName
/* 396 */       .contains("Scoreboard") || packetName
/* 397 */       .contains("OverlayMessage")) {
/* 398 */       ReferenceCountUtil.release(msg);
/*     */       return;
/*     */     } 
/* 401 */     super.channelRead(ctx, msg);
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelInactive(ChannelHandlerContext ctx) throws Exception {
/* 406 */     BotSessionManager.connections.removeIf(bot -> BotSessionManager.matchesName(bot.name(), name));
/* 407 */     super.channelInactive(ctx);
/*     */   }
/*     */ 
/*     */   
/*     */   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
/* 412 */     BotSessionManager.connections.removeIf(bot -> BotSessionManager.matchesName(bot.name(), name));
/* 413 */     ctx.close();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\bot\BotSessionManager$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */