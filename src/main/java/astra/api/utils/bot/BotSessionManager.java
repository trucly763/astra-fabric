/*     */ package shame.astra.api.utils.bot;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelDuplexHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.RecordComponent;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import net.minecraft.class_10264;
/*     */ import net.minecraft.class_1268;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_2535;
/*     */ import net.minecraft.class_2561;
/*     */ import net.minecraft.class_2596;
/*     */ import net.minecraft.class_2670;
/*     */ import net.minecraft.class_2708;
/*     */ import net.minecraft.class_2720;
/*     */ import net.minecraft.class_2749;
/*     */ import net.minecraft.class_2793;
/*     */ import net.minecraft.class_2827;
/*     */ import net.minecraft.class_2828;
/*     */ import net.minecraft.class_2856;
/*     */ import net.minecraft.class_2879;
/*     */ import net.minecraft.class_2886;
/*     */ import net.minecraft.class_310;
/*     */ import net.minecraft.class_320;
/*     */ import net.minecraft.class_3532;
/*     */ import net.minecraft.class_412;
/*     */ import net.minecraft.class_437;
/*     */ import net.minecraft.class_442;
/*     */ import net.minecraft.class_500;
/*     */ import net.minecraft.class_634;
/*     */ import net.minecraft.class_636;
/*     */ import net.minecraft.class_6373;
/*     */ import net.minecraft.class_6374;
/*     */ import net.minecraft.class_638;
/*     */ import net.minecraft.class_639;
/*     */ import net.minecraft.class_642;
/*     */ import net.minecraft.class_746;
/*     */ import shame.astra.api.storages.implement.helpertstorages.enumvar.ModuleClass;
/*     */ import shame.astra.client.modules.impl.player.AutoForest;
/*     */ import shame.astra.mixin.IMinecraftClientAccessor;
/*     */ 
/*     */ public final class BotSessionManager {
/*  53 */   private static final List<BotConnection> connections = new CopyOnWriteArrayList<>();
/*     */   private static volatile boolean ignoreBotMessages;
/*     */   private static volatile boolean bypassResourcePacksDuringBotConnect;
/*     */   
/*     */   public static List<BotConnection> getConnections() {
/*  58 */     pruneDeadConnections();
/*  59 */     return new ArrayList<>(connections);
/*     */   }
/*     */   
/*     */   public static boolean shouldBypassResourcePacks() {
/*  63 */     return bypassResourcePacksDuringBotConnect;
/*     */   }
/*     */   
/*     */   public static void finishBotConnectStage() {
/*  67 */     bypassResourcePacksDuringBotConnect = false;
/*     */   }
/*     */   
/*     */   public static String getCurrentSessionName() {
/*  71 */     class_310 mc = class_310.method_1551();
/*  72 */     return (mc.method_1548() == null) ? "" : mc.method_1548().method_1676();
/*     */   }
/*     */   
/*     */   public static List<String> getSessionNames(boolean includeCurrent) {
/*  76 */     pruneDeadConnections();
/*  77 */     Set<String> names = new LinkedHashSet<>();
/*  78 */     if (includeCurrent) {
/*  79 */       String currentName = getCurrentSessionName();
/*  80 */       if (!currentName.isBlank()) {
/*  81 */         names.add(currentName);
/*     */       }
/*     */     } 
/*     */     
/*  85 */     for (BotConnection bot : connections) {
/*  86 */       if (bot.name() != null && !bot.name().isBlank()) {
/*  87 */         names.add(bot.name());
/*     */       }
/*     */     } 
/*  90 */     return new ArrayList<>(names);
/*     */   }
/*     */   
/*     */   public static boolean toggleIgnoreBotMessages() {
/*  94 */     ignoreBotMessages = !ignoreBotMessages;
/*  95 */     return ignoreBotMessages;
/*     */   }
/*     */   
/*     */   public static boolean isIgnoreBotMessages() {
/*  99 */     return ignoreBotMessages;
/*     */   }
/*     */   
/*     */   public static void connect(String name, String address) {
/* 103 */     class_310 mc = class_310.method_1551();
/* 104 */     if (mc.method_1548() == null || name == null || name.isBlank() || address == null || address.isBlank()) {
/*     */       return;
/*     */     }
/*     */     
/* 108 */     class_320 originalSession = mc.method_1548();
/* 109 */     class_642 originalServerInfo = mc.method_1558();
/* 110 */     pruneDeadConnections();
/* 111 */     disconnectSessionsByName(name, (class_2561)class_2561.method_43470("Replaced"));
/* 112 */     BotConnection previous = freezeCurrentSession();
/* 113 */     ModuleClass.autoForest.resetToDefaults();
/* 114 */     ((IMinecraftClientAccessor)mc).setSession(createSessionWithName(mc.method_1548(), name));
/* 115 */     bypassResourcePacksDuringBotConnect = true;
/* 116 */     mc.execute(() -> {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           try {
/*     */ 
/*     */             
/*     */             class_412.method_36877((class_437)new class_500((class_437)new class_442()), mc, class_639.method_2950(address), new class_642(address, address, class_642.class_8678.field_45611), false, null);
/* 126 */           } catch (Exception ignored) {
/*     */             bypassResourcePacksDuringBotConnect = false;
/*     */             restoreAfterConnectFailure(mc, previous, originalSession, originalServerInfo);
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   public static void pulseBots(boolean rightClick) {
/* 134 */     for (BotConnection bot : connections) {
/* 135 */       if (!isConnectionUsable(bot))
/* 136 */         continue;  if (rightClick) {
/* 137 */         bot.handler().method_52787((class_2596)new class_2886(class_1268.field_5808, 0, bot.player().method_36454(), bot.player().method_36455())); continue;
/*     */       } 
/* 139 */       bot.handler().method_52787((class_2596)new class_2879(class_1268.field_5808));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void sayAll(String message) {
/* 145 */     for (BotConnection bot : connections) {
/* 146 */       if (!isConnectionUsable(bot))
/* 147 */         continue;  if (message.startsWith("/")) {
/* 148 */         bot.handler().method_45730(message.substring(1)); continue;
/*     */       } 
/* 150 */       bot.handler().method_45729(message);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean control(String name) {
/* 156 */     if (name == null || name.isBlank()) {
/* 157 */       return false;
/*     */     }
/*     */     
/* 160 */     pruneDeadConnections();
/* 161 */     class_310 mc = class_310.method_1551();
/* 162 */     if (mc.field_1724 != null && mc.field_1687 != null && name.equalsIgnoreCase(getCurrentSessionName())) {
/* 163 */       return true;
/*     */     }
/*     */     
/* 166 */     return ((Boolean)connections.stream()
/* 167 */       .filter(bot -> matchesName(bot.name(), name))
/* 168 */       .findFirst()
/* 169 */       .map(bot -> {
/*     */           if (!isConnectionUsable(bot)) {
/*     */             connections.remove(bot);
/*     */             
/*     */             return Boolean.valueOf(false);
/*     */           } 
/*     */           BotConnection previous = freezeCurrentSession();
/*     */           if (!activateSession(bot)) {
/*     */             if (previous != null && activateSession(previous)) {
/*     */               connections.remove(previous);
/*     */             }
/*     */             return Boolean.valueOf(false);
/*     */           } 
/*     */           connections.remove(bot);
/*     */           return Boolean.valueOf(true);
/* 184 */         }).orElse(Boolean.valueOf(false))).booleanValue();
/*     */   }
/*     */   
/*     */   public static boolean say(String name, String message) {
/* 188 */     pruneDeadConnections();
/* 189 */     return ((Boolean)connections.stream()
/* 190 */       .filter(bot -> matchesName(bot.name(), name))
/* 191 */       .findFirst()
/* 192 */       .map(bot -> {
/*     */           if (!isConnectionUsable(bot)) {
/*     */             connections.remove(bot);
/*     */             
/*     */             return Boolean.valueOf(false);
/*     */           } 
/*     */           if (message.startsWith("/")) {
/*     */             bot.handler().method_45730(message.substring(1));
/*     */           } else {
/*     */             bot.handler().method_45729(message);
/*     */           } 
/*     */           return Boolean.valueOf(true);
/* 204 */         }).orElse(Boolean.valueOf(false))).booleanValue();
/*     */   }
/*     */   
/*     */   public static boolean remove(String name) {
/* 208 */     if (name == null || name.isBlank()) {
/* 209 */       return false;
/*     */     }
/*     */     
/* 212 */     return (disconnectSessionsByName(name, (class_2561)class_2561.method_43470("Removed")) > 0);
/*     */   }
/*     */   
/*     */   public static boolean restore() {
/* 216 */     return restore(null);
/*     */   }
/*     */   
/*     */   public static boolean restore(String name) {
/* 220 */     pruneDeadConnections();
/*     */ 
/*     */     
/* 223 */     String targetName = (name == null || name.isBlank()) ? (connections.isEmpty() ? "" : ((BotConnection)connections.get(connections.size() - 1)).name()) : name;
/* 224 */     return (!targetName.isBlank() && control(targetName));
/*     */   }
/*     */   
/*     */   private static BotConnection freezeCurrentSession() {
/* 228 */     class_310 mc = class_310.method_1551();
/* 229 */     if (mc.method_1562() == null || mc.field_1687 == null || mc.field_1724 == null) {
/* 230 */       return null;
/*     */     }
/*     */     
/* 233 */     class_634 handler = mc.method_1562();
/* 234 */     makeNettyBot(handler, mc.method_1548().method_1676(), mc.field_1724);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 245 */     BotConnection connection = new BotConnection(mc.method_1548().method_1676(), (mc.method_1558() != null) ? (mc.method_1558()).field_3761 : "", handler.method_48296(), handler, mc.field_1687, mc.field_1724, mc.field_1761, mc.method_1548(), mc.method_1558(), ModuleClass.autoForest.captureState());
/*     */     
/* 247 */     replaceConnection(connection);
/* 248 */     clearActiveSession(mc);
/* 249 */     return connection;
/*     */   }
/*     */   
/*     */   private static boolean activateSession(BotConnection bot) {
/* 253 */     if (!isConnectionUsable(bot)) {
/* 254 */       return false;
/*     */     }
/*     */     
/* 257 */     class_310 mc = class_310.method_1551();
/* 258 */     IMinecraftClientAccessor accessor = (IMinecraftClientAccessor)mc;
/*     */     
/* 260 */     Channel channel = getChannel(bot.connection());
/* 261 */     if (channel != null && channel.pipeline().get("bot_filter") != null) {
/* 262 */       channel.pipeline().remove("bot_filter");
/*     */     }
/*     */     
/*     */     try {
/* 266 */       setMinecraftClientField(mc, class_634.class, bot.handler());
/* 267 */       accessor.setSession((bot.session() != null) ? bot.session() : createSessionWithName(mc.method_1548(), bot.name()));
/* 268 */       setMinecraftClientField(mc, class_642.class, (bot.serverInfo() != null) ? bot.serverInfo() : createServerInfo(bot.name(), bot.address()));
/* 269 */       accessor.setItemUseCooldown(0);
/*     */       
/* 271 */       mc.field_1687 = bot.world();
/* 272 */       mc.field_1724 = bot.player();
/* 273 */       mc.field_1719 = (class_1297)bot.player();
/* 274 */       mc.field_1761 = bot.interactionManager();
/*     */       
/* 276 */       if (mc.field_1769 != null) {
/* 277 */         mc.field_1769.method_3244(bot.world());
/*     */       }
/*     */       
/* 280 */       ModuleClass.autoForest.applyState(bot.autoForestState());
/* 281 */       bot.handler().method_52787((class_2596)new class_2828.class_2830(bot
/* 282 */             .player().method_23317(), bot
/* 283 */             .player().method_23318(), bot
/* 284 */             .player().method_23321(), bot
/* 285 */             .player().method_36454(), bot
/* 286 */             .player().method_36455(), bot
/* 287 */             .player().method_24828(), 
/* 288 */             (bot.player()).field_5976));
/*     */       
/* 290 */       mc.method_1507(null);
/* 291 */       return true;
/* 292 */     } catch (Exception ignored) {
/* 293 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void clearActiveSession(class_310 mc) {
/* 298 */     IMinecraftClientAccessor accessor = (IMinecraftClientAccessor)mc;
/* 299 */     setMinecraftClientField(mc, class_634.class, null);
/* 300 */     accessor.setItemUseCooldown(0);
/* 301 */     mc.field_1687 = null;
/* 302 */     mc.field_1724 = null;
/* 303 */     mc.field_1719 = null;
/* 304 */     mc.field_1761 = null;
/* 305 */     if (mc.field_1769 != null) {
/* 306 */       mc.field_1769.method_3244(null);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void replaceConnection(BotConnection connection) {
/* 311 */     disconnectSessionsByName(connection.name(), (class_2561)class_2561.method_43470("Replaced"));
/* 312 */     connections.add(connection);
/*     */   }
/*     */   
/*     */   private static void makeNettyBot(final class_634 handler, final String name, final class_746 botPlayer) {
/* 316 */     Channel channel = getChannel(handler.method_48296());
/* 317 */     if (channel == null)
/* 318 */       return;  if (channel.pipeline().get("bot_filter") != null) {
/* 319 */       channel.pipeline().remove("bot_filter");
/*     */     }
/* 321 */     if (channel.pipeline().get("packet_handler") == null) {
/*     */       return;
/*     */     }
/*     */     
/* 325 */     channel.pipeline().addBefore("packet_handler", "bot_filter", (ChannelHandler)new ChannelDuplexHandler()
/*     */         {
/*     */           public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
/* 328 */             if (msg instanceof class_2670) { class_2670 packet = (class_2670)msg;
/* 329 */               handler.method_48296().method_10743((class_2596)new class_2827(packet.method_11517()));
/* 330 */               if (botPlayer != null) {
/* 331 */                 handler.method_52787((class_2596)new class_2828.class_5911(botPlayer.method_24828(), botPlayer.field_5976));
/*     */               }
/* 333 */               ReferenceCountUtil.release(msg);
/*     */               
/*     */               return; }
/*     */             
/* 337 */             if (msg instanceof class_6373) { class_6373 packet = (class_6373)msg;
/* 338 */               handler.method_48296().method_10743((class_2596)new class_6374(packet.method_36950()));
/* 339 */               ReferenceCountUtil.release(msg);
/*     */               
/*     */               return; }
/*     */             
/* 343 */             if (msg instanceof class_2720) { class_2720 packet = (class_2720)msg;
/* 344 */               handler.method_52787((class_2596)new class_2856(packet.comp_2158(), class_2856.class_2857.field_13016));
/* 345 */               handler.method_52787((class_2596)new class_2856(packet.comp_2158(), class_2856.class_2857.field_13017));
/* 346 */               ReferenceCountUtil.release(msg);
/*     */               
/*     */               return; }
/*     */             
/* 350 */             if (msg instanceof class_2708) { class_2708 packet = (class_2708)msg;
/* 351 */               BotSessionManager.applyFrozenPositionLook(botPlayer, packet);
/* 352 */               handler.method_52787((class_2596)new class_2793(packet.comp_3133()));
/* 353 */               if (botPlayer != null) {
/* 354 */                 handler.method_52787((class_2596)new class_2828.class_2830(botPlayer
/* 355 */                       .method_23317(), botPlayer
/* 356 */                       .method_23318(), botPlayer
/* 357 */                       .method_23321(), botPlayer
/* 358 */                       .method_36454(), botPlayer
/* 359 */                       .method_36455(), botPlayer
/* 360 */                       .method_24828(), botPlayer.field_5976));
/*     */               }
/*     */ 
/*     */               
/* 364 */               ReferenceCountUtil.release(msg);
/*     */               
/*     */               return; }
/*     */             
/* 368 */             if (msg instanceof class_10264) { class_10264 packet = (class_10264)msg;
/* 369 */               BotSessionManager.applyFrozenEntityPositionSync(botPlayer, packet);
/* 370 */               ReferenceCountUtil.release(msg);
/*     */               
/*     */               return; }
/*     */             
/* 374 */             if (msg instanceof class_2749) { class_2749 packet = (class_2749)msg;
/* 375 */               if (botPlayer != null) {
/* 376 */                 botPlayer.method_6033(packet.method_11833());
/*     */               }
/* 378 */               ReferenceCountUtil.release(msg);
/*     */               
/*     */               return; }
/*     */             
/* 382 */             if (msg instanceof net.minecraft.class_2661) {
/* 383 */               BotSessionManager.connections.removeIf(bot -> BotSessionManager.matchesName(bot.name(), name));
/* 384 */               ctx.close();
/* 385 */               ReferenceCountUtil.release(msg);
/*     */               
/*     */               return;
/*     */             } 
/* 389 */             String packetName = msg.getClass().getSimpleName();
/* 390 */             if (packetName.contains("Sound") || packetName
/* 391 */               .contains("Particle") || packetName
/* 392 */               .contains("Screen") || (BotSessionManager.ignoreBotMessages && 
/* 393 */               BotSessionManager.isBotMessagePacket(packetName)) || packetName
/* 394 */               .contains("Explosion") || packetName
/* 395 */               .contains("BossBar") || packetName
/* 396 */               .contains("Scoreboard") || packetName
/* 397 */               .contains("OverlayMessage")) {
/* 398 */               ReferenceCountUtil.release(msg);
/*     */               return;
/*     */             } 
/* 401 */             super.channelRead(ctx, msg);
/*     */           }
/*     */ 
/*     */           
/*     */           public void channelInactive(ChannelHandlerContext ctx) throws Exception {
/* 406 */             BotSessionManager.connections.removeIf(bot -> BotSessionManager.matchesName(bot.name(), name));
/* 407 */             super.channelInactive(ctx);
/*     */           }
/*     */ 
/*     */           
/*     */           public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
/* 412 */             BotSessionManager.connections.removeIf(bot -> BotSessionManager.matchesName(bot.name(), name));
/* 413 */             ctx.close();
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private static boolean isBotMessagePacket(String packetName) {
/* 419 */     return (packetName.contains("Chat") || packetName
/* 420 */       .contains("Message") || packetName
/* 421 */       .contains("Title") || packetName
/* 422 */       .contains("Overlay"));
/*     */   }
/*     */   
/*     */   private static Channel getChannel(class_2535 connection) {
/*     */     try {
/* 427 */       for (Field field : class_2535.class.getDeclaredFields()) {
/* 428 */         if (Channel.class.isAssignableFrom(field.getType())) {
/* 429 */           field.setAccessible(true);
/* 430 */           return (Channel)field.get(connection);
/*     */         } 
/*     */       } 
/* 433 */     } catch (Exception exception) {}
/*     */     
/* 435 */     return null;
/*     */   }
/*     */   
/*     */   private static void applyFrozenPositionLook(class_746 botPlayer, class_2708 packet) {
/* 439 */     if (botPlayer == null || packet == null) {
/*     */       return;
/*     */     }
/*     */     
/* 443 */     double x = readPacketDouble(packet, "x", botPlayer.method_23317());
/* 444 */     double y = readPacketDouble(packet, "y", botPlayer.method_23318());
/* 445 */     double z = readPacketDouble(packet, "z", botPlayer.method_23321());
/* 446 */     float yaw = (float)readPacketDouble(packet, "yaw", botPlayer.method_36454());
/* 447 */     float pitch = (float)readPacketDouble(packet, "pitch", botPlayer.method_36455());
/* 448 */     Object change = readPacketComponent(packet, "change");
/* 449 */     if (change == null) {
/* 450 */       change = readPacketComponent(packet, "flags");
/*     */     }
/*     */     
/* 453 */     if (hasRelativeFlag(change, "X")) {
/* 454 */       x += botPlayer.method_23317();
/*     */     }
/* 456 */     if (hasRelativeFlag(change, "Y")) {
/* 457 */       y += botPlayer.method_23318();
/*     */     }
/* 459 */     if (hasRelativeFlag(change, "Z")) {
/* 460 */       z += botPlayer.method_23321();
/*     */     }
/* 462 */     if (hasRelativeFlag(change, "Y_ROT")) {
/* 463 */       yaw += botPlayer.method_36454();
/*     */     }
/* 465 */     if (hasRelativeFlag(change, "X_ROT")) {
/* 466 */       pitch += botPlayer.method_36455();
/*     */     }
/*     */     
/* 469 */     pitch = class_3532.method_15363(pitch, -90.0F, 90.0F);
/* 470 */     botPlayer.method_5808(x, y, z, yaw, pitch);
/* 471 */     botPlayer.method_36456(yaw);
/* 472 */     botPlayer.method_36457(pitch);
/*     */   }
/*     */   
/*     */   private static void applyFrozenEntityPositionSync(class_746 botPlayer, class_10264 packet) {
/* 476 */     if (botPlayer == null || packet == null || packet.comp_3223() != botPlayer.method_5628() || packet.comp_3224() == null) {
/*     */       return;
/*     */     }
/*     */     
/* 480 */     class_243 position = packet.comp_3224().comp_3148();
/* 481 */     if (position == null) {
/*     */       return;
/*     */     }
/*     */     
/* 485 */     float yaw = packet.comp_3224().comp_3150();
/* 486 */     float pitch = class_3532.method_15363(packet.comp_3224().comp_3151(), -90.0F, 90.0F);
/* 487 */     botPlayer.method_5808(position.field_1352, position.field_1351, position.field_1350, yaw, pitch);
/* 488 */     botPlayer.method_36456(yaw);
/* 489 */     botPlayer.method_36457(pitch);
/* 490 */     if (packet.comp_3224().comp_3149() != null) {
/* 491 */       botPlayer.method_18799(packet.comp_3224().comp_3149());
/*     */     }
/* 493 */     botPlayer.method_24830(packet.comp_3225());
/*     */   }
/*     */   
/*     */   private static double readPacketDouble(Object packet, String name, double fallback) {
/* 497 */     Object value = readPacketComponent(packet, name);
/* 498 */     Number number = (Number)value; return (value instanceof Number) ? number.doubleValue() : fallback;
/*     */   }
/*     */   
/*     */   private static Object readPacketComponent(Object packet, String name) {
/* 502 */     if (packet == null || name == null || name.isBlank()) {
/* 503 */       return null;
/*     */     }
/*     */     
/*     */     try {
/* 507 */       Method method = packet.getClass().getMethod(name, new Class[0]);
/* 508 */       method.setAccessible(true);
/* 509 */       return method.invoke(packet, new Object[0]);
/* 510 */     } catch (Exception exception) {
/*     */ 
/*     */       
/*     */       try {
/* 514 */         Method method = packet.getClass().getMethod("get" + Character.toUpperCase(name.charAt(0)) + name.substring(1), new Class[0]);
/* 515 */         method.setAccessible(true);
/* 516 */         return method.invoke(packet, new Object[0]);
/* 517 */       } catch (Exception exception1) {
/*     */ 
/*     */         
/*     */         try {
/* 521 */           RecordComponent[] components = packet.getClass().getRecordComponents();
/* 522 */           if (components != null) {
/* 523 */             for (RecordComponent component : components) {
/* 524 */               if (name.equals(component.getName())) {
/* 525 */                 return component.getAccessor().invoke(packet, new Object[0]);
/*     */               }
/*     */             } 
/*     */           }
/* 529 */         } catch (Exception exception2) {}
/*     */ 
/*     */         
/*     */         try {
/* 533 */           for (Field field : packet.getClass().getDeclaredFields()) {
/* 534 */             if (name.equalsIgnoreCase(field.getName())) {
/* 535 */               field.setAccessible(true);
/* 536 */               return field.get(packet);
/*     */             } 
/*     */           } 
/* 539 */         } catch (Exception exception2) {}
/*     */ 
/*     */         
/* 542 */         return null;
/*     */       } 
/*     */     } 
/*     */   }
/* 546 */   private static boolean hasRelativeFlag(Object flags, String flagName) { if (flags instanceof Iterable) { Iterable<?> iterable = (Iterable)flags; if (flagName != null) {
/*     */ 
/*     */ 
/*     */         
/* 550 */         for (Object flag : iterable) {
/* 551 */           if (flag instanceof Enum) { Enum<?> enumFlag = (Enum)flag; if (flagName.equals(enumFlag.name()))
/* 552 */               return true;  }
/*     */         
/*     */         } 
/* 555 */         return false;
/*     */       }  }
/*     */     
/*     */     return false; } private static class_320 createSessionWithName(class_320 current, String name) {
/*     */     try {
/* 560 */       Constructor<class_320> constructor = class_320.class.getDeclaredConstructor(new Class[] { String.class, UUID.class, String.class, Optional.class, Optional.class, class_320.class_321.class });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 568 */       constructor.setAccessible(true);
/* 569 */       return constructor.newInstance(new Object[] { name, 
/*     */             
/* 571 */             UUID.randomUUID(), 
/* 572 */             (current == null) ? "" : current.method_1674(), 
/* 573 */             Optional.empty(), 
/* 574 */             Optional.empty(), class_320.class_321.field_1988 });
/*     */     
/*     */     }
/* 577 */     catch (Exception e) {
/* 578 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void setMinecraftClientField(class_310 mc, Class<?> fieldType, Object value) {
/*     */     try {
/* 584 */       for (Field field : class_310.class.getDeclaredFields()) {
/* 585 */         if (field.getType() == fieldType) {
/* 586 */           field.setAccessible(true);
/* 587 */           field.set(mc, value);
/*     */           return;
/*     */         } 
/*     */       } 
/* 591 */     } catch (Exception exception) {}
/*     */   }
/*     */ 
/*     */   
/*     */   private static class_642 createServerInfo(String name, String address) {
/* 596 */     String safeAddress = (address == null) ? "" : address;
/* 597 */     String safeName = (name == null || name.isBlank()) ? safeAddress : name;
/* 598 */     return new class_642(safeName, safeAddress, class_642.class_8678.field_45611);
/*     */   }
/*     */   
/*     */   private static void restoreAfterConnectFailure(class_310 mc, BotConnection previous, class_320 originalSession, class_642 originalServerInfo) {
/*     */     try {
/* 603 */       bypassResourcePacksDuringBotConnect = false;
/* 604 */       if (previous != null && activateSession(previous)) {
/* 605 */         connections.remove(previous);
/*     */         
/*     */         return;
/*     */       } 
/* 609 */       IMinecraftClientAccessor accessor = (IMinecraftClientAccessor)mc;
/* 610 */       accessor.setSession(originalSession);
/* 611 */       setMinecraftClientField(mc, class_642.class, originalServerInfo);
/* 612 */     } catch (Exception exception) {}
/*     */   }
/*     */ 
/*     */   
/*     */   private static int disconnectSessionsByName(String name, class_2561 reason) {
/* 617 */     if (name == null || name.isBlank()) {
/* 618 */       return 0;
/*     */     }
/*     */     
/* 621 */     int removed = 0;
/* 622 */     for (BotConnection bot : new ArrayList(connections)) {
/* 623 */       if (!matchesName(bot.name(), name)) {
/*     */         continue;
/*     */       }
/*     */       
/* 627 */       connections.remove(bot);
/* 628 */       removed++;
/*     */       try {
/* 630 */         if (bot.connection() != null) {
/* 631 */           bot.connection().method_10747(reason);
/*     */         }
/* 633 */       } catch (Exception exception) {}
/*     */     } 
/*     */     
/* 636 */     return removed;
/*     */   }
/*     */   
/*     */   private static void pruneDeadConnections() {
/* 640 */     connections.removeIf(bot -> !isConnectionUsable(bot));
/*     */   }
/*     */   
/*     */   private static boolean isConnectionUsable(BotConnection bot) {
/* 644 */     if (bot == null || bot.name() == null || bot.name().isBlank()) {
/* 645 */       return false;
/*     */     }
/* 647 */     if (bot.connection() == null || bot.handler() == null || bot.world() == null || bot.player() == null) {
/* 648 */       return false;
/*     */     }
/* 650 */     if ((bot.player()).field_3944 != bot.handler()) {
/* 651 */       return false;
/*     */     }
/*     */     
/* 654 */     Channel channel = getChannel(bot.connection());
/* 655 */     return (channel == null || channel.isOpen());
/*     */   }
/*     */   
/*     */   private static boolean matchesName(String left, String right) {
/* 659 */     return (left != null && right != null && left.equalsIgnoreCase(right));
/*     */   }
/*     */   
/*     */   public static final class BotConnection {
/*     */     private final String name;
/*     */     private final String address;
/*     */     private final class_2535 connection;
/*     */     private final class_634 handler;
/*     */     private final class_638 world;
/*     */     private final class_746 player;
/*     */     private final class_636 interactionManager;
/*     */     private final class_320 session;
/*     */     private final class_642 serverInfo;
/*     */     private final AutoForest.SessionState autoForestState;
/*     */     
/*     */     public BotConnection(String name, String address, class_2535 connection, class_634 handler, class_638 world, class_746 player, class_636 interactionManager, class_320 session, class_642 serverInfo, AutoForest.SessionState autoForestState) {
/* 675 */       this.name = name;
/* 676 */       this.address = address;
/* 677 */       this.connection = connection;
/* 678 */       this.handler = handler;
/* 679 */       this.world = world;
/* 680 */       this.player = player;
/* 681 */       this.interactionManager = interactionManager;
/* 682 */       this.session = session;
/* 683 */       this.serverInfo = serverInfo;
/* 684 */       this.autoForestState = autoForestState;
/*     */     }
/*     */     
/* 687 */     public String name() { return this.name; }
/* 688 */     public String address() { return this.address; }
/* 689 */     public class_2535 connection() { return this.connection; }
/* 690 */     public class_634 handler() { return this.handler; }
/* 691 */     public class_638 world() { return this.world; }
/* 692 */     public class_746 player() { return this.player; }
/* 693 */     public class_636 interactionManager() { return this.interactionManager; }
/* 694 */     public class_320 session() { return this.session; }
/* 695 */     public class_642 serverInfo() { return this.serverInfo; } public AutoForest.SessionState autoForestState() {
/* 696 */       return this.autoForestState;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\bot\BotSessionManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */