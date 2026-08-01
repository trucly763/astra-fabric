/*     */ package shame.astra.api.commands.impl;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.StringArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.class_310;
/*     */ import shame.astra.api.utils.bot.BotSessionManager;
/*     */ import shame.astra.api.utils.chat.ChatUtils;
/*     */ 
/*     */ public class BotCommand extends Command {
/*     */   public BotCommand() {
/*  16 */     super("bot");
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute(LiteralArgumentBuilder<class_2172> builder) {
/*  21 */     ((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)builder.then(literal("connect")
/*  22 */         .then(arg("name", (ArgumentType)StringArgumentType.string())
/*  23 */           .then(arg("ip", (ArgumentType)StringArgumentType.string())
/*  24 */             .executes(context -> {
/*     */                 class_310 mc = class_310.method_1551();
/*     */                 if (mc.field_1724 == null) {
/*     */                   return 0;
/*     */                 }
/*     */                 String name = StringArgumentType.getString(context, "name");
/*     */                 String ip = StringArgumentType.getString(context, "ip");
/*     */                 BotSessionManager.connect(name, ip);
/*     */                 ChatUtils.sendMessage("§7[Bot] §fПодключение выполнено: " + name + " -> " + ip);
/*     */                 return 1;
/*  34 */               }))))).then(literal("remove")
/*  35 */         .then(arg("name", (ArgumentType)StringArgumentType.string())
/*  36 */           .suggests((context, suggestions) -> {
/*     */               Objects.requireNonNull(suggestions);
/*     */               BotSessionManager.getSessionNames(false).forEach(suggestions::suggest);
/*     */               return suggestions.buildFuture();
/*  40 */             }).executes(context -> {
/*     */               class_310 mc = class_310.method_1551();
/*     */               if (mc.field_1724 == null) {
/*     */                 return 0;
/*     */               }
/*     */               String name = StringArgumentType.getString(context, "name");
/*     */               if (BotSessionManager.remove(name)) {
/*     */                 ChatUtils.sendMessage("§7[Bot] §fСессия отключена и удалена: " + name);
/*     */               } else {
/*     */                 ChatUtils.sendMessage("§7[Bot] §fСессия не найдена: " + name);
/*     */               } 
/*     */               return 1;
/*  52 */             })))).then(literal("control")
/*  53 */         .then(arg("name", (ArgumentType)StringArgumentType.string())
/*  54 */           .suggests((context, suggestions) -> {
/*     */               Objects.requireNonNull(suggestions);
/*     */               BotSessionManager.getSessionNames(false).forEach(suggestions::suggest);
/*     */               return suggestions.buildFuture();
/*  58 */             }).executes(context -> {
/*     */               class_310 mc = class_310.method_1551();
/*     */               
/*     */               if (mc.field_1724 == null) {
/*     */                 return 0;
/*     */               }
/*     */               String name = StringArgumentType.getString(context, "name");
/*     */               if (name.equalsIgnoreCase(BotSessionManager.getCurrentSessionName())) {
/*     */                 ChatUtils.sendMessage("§7[Bot] §fТы уже управляешь этой сессией: " + name);
/*     */                 return 1;
/*     */               } 
/*     */               if (BotSessionManager.control(name)) {
/*     */                 ChatUtils.sendMessage("§7[Bot] §fПереключаю на сессию: " + name);
/*     */               } else {
/*     */                 ChatUtils.sendMessage("§7[Bot] §fСессия не найдена: " + name);
/*     */               } 
/*     */               return 1;
/*  75 */             })))).then(literal("say")
/*  76 */         .then(arg("name", (ArgumentType)StringArgumentType.string())
/*  77 */           .suggests((context, suggestions) -> {
/*     */               Objects.requireNonNull(suggestions);
/*     */               BotSessionManager.getSessionNames(false).forEach(suggestions::suggest);
/*     */               return suggestions.buildFuture();
/*  81 */             }).then(arg("message", (ArgumentType)StringArgumentType.greedyString())
/*  82 */             .executes(context -> {
/*     */                 class_310 mc = class_310.method_1551();
/*     */                 if (mc.field_1724 == null) {
/*     */                   return 0;
/*     */                 }
/*     */                 String name = StringArgumentType.getString(context, "name");
/*     */                 String message = StringArgumentType.getString(context, "message");
/*     */                 if (BotSessionManager.say(name, message)) {
/*     */                   ChatUtils.sendMessage("§7[Bot] §fСообщение отправлено от сессии " + name);
/*     */                 } else {
/*     */                   ChatUtils.sendMessage("§7[Bot] §fСессия не найдена: " + name);
/*     */                 } 
/*     */                 return 1;
/*  95 */               }))))).then(literal("sayall")
/*  96 */         .then(arg("message", (ArgumentType)StringArgumentType.greedyString())
/*  97 */           .executes(context -> {
/*     */               class_310 mc = class_310.method_1551();
/*     */               if (mc.field_1724 == null) {
/*     */                 return 0;
/*     */               }
/*     */               String message = StringArgumentType.getString(context, "message");
/*     */               BotSessionManager.sayAll(message);
/*     */               ChatUtils.sendMessage("§7[Bot] §fСообщение отправлено от всех ботов.");
/*     */               return 1;
/* 106 */             })))).then(((LiteralArgumentBuilder)literal("return")
/* 107 */         .executes(context -> {
/*     */             class_310 mc = class_310.method_1551();
/*     */             if (mc.field_1724 == null) {
/*     */               return 0;
/*     */             }
/*     */             if (BotSessionManager.restore()) {
/*     */               ChatUtils.sendMessage("§7[Bot] §fВозвращаю предыдущую сессию");
/*     */             } else {
/*     */               ChatUtils.sendMessage("§7[Bot] §fНет сохранённой сессии для возврата");
/*     */             } 
/*     */             return 1;
/* 118 */           })).then(arg("name", (ArgumentType)StringArgumentType.string())
/* 119 */           .suggests((context, suggestions) -> {
/*     */               Objects.requireNonNull(suggestions);
/*     */               BotSessionManager.getSessionNames(true).forEach(suggestions::suggest);
/*     */               return suggestions.buildFuture();
/* 123 */             }).executes(context -> {
/*     */               class_310 mc = class_310.method_1551();
/*     */               
/*     */               if (mc.field_1724 == null) {
/*     */                 return 0;
/*     */               }
/*     */               String name = StringArgumentType.getString(context, "name");
/*     */               if (name.equalsIgnoreCase(BotSessionManager.getCurrentSessionName())) {
/*     */                 ChatUtils.sendMessage("§7[Bot] §fТы уже управляешь этой сессией: " + name);
/*     */                 return 1;
/*     */               } 
/*     */               if (BotSessionManager.restore(name)) {
/*     */                 ChatUtils.sendMessage("§7[Bot] §fПереключаю на сессию: " + name);
/*     */               } else {
/*     */                 ChatUtils.sendMessage("§7[Bot] §fСессия не найдена: " + name);
/*     */               } 
/*     */               return 1;
/* 140 */             })))).then(literal("ignore")
/* 141 */         .executes(context -> {
/*     */             class_310 mc = class_310.method_1551();
/*     */             if (mc.field_1724 == null) {
/*     */               return 0;
/*     */             }
/*     */             boolean enabled = BotSessionManager.toggleIgnoreBotMessages();
/*     */             ChatUtils.sendMessage("§7[Bot] §fИгнор сообщений ботов: " + (enabled ? "§aвключен" : "§cвыключен"));
/*     */             return 1;
/* 149 */           }))).then(literal("list")
/* 150 */         .executes(context -> {
/*     */             class_310 mc = class_310.method_1551();
/*     */             if (mc.field_1724 == null)
/*     */               return 0; 
/*     */             List<BotSessionManager.BotConnection> connections = BotSessionManager.getConnections();
/*     */             ChatUtils.sendMessage("§7[Bot] §fТекущая сессия: " + BotSessionManager.getCurrentSessionName());
/*     */             if (connections.isEmpty()) {
/*     */               ChatUtils.sendMessage("§7[Bot] §fСписок сохранённых сессий пуст");
/*     */             } else {
/*     */               ChatUtils.sendMessage("§7[Bot] §fСохранённые сессии:");
/*     */               for (BotSessionManager.BotConnection bot : connections)
/*     */                 ChatUtils.sendMessage("§7- §f" + bot.name() + " (§7" + bot.address() + "§f)"); 
/*     */             } 
/*     */             return 1;
/*     */           }));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\api\commands\impl\BotCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */