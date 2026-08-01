/*     */ package shame.astra.api.commands.impl;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.FloatArgumentType;
/*     */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.class_2172;
/*     */ import shame.astra.api.utils.chat.ChatUtils;
/*     */ import shame.astra.client.modules.impl.player.AutoForest;
/*     */ 
/*     */ public class AutoLesCommand extends Command {
/*     */   public AutoLesCommand() {
/*  17 */     super("autoles");
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute(LiteralArgumentBuilder<class_2172> builder) {
/*  22 */     builder.executes(ctx -> {
/*     */           sendStatus();
/*     */           
/*     */           return 1;
/*     */         });
/*  27 */     builder.then(literal("enable").executes(ctx -> {
/*     */             if (!module().isCurrentSessionEnabled()) {
/*     */               module().enableForCurrentSession();
/*     */             }
/*     */             
/*     */             ChatUtils.sendMessage("АвтоЛес включён");
/*     */             return 1;
/*     */           }));
/*  35 */     builder.then(literal("disable").executes(ctx -> {
/*     */             if (module().isCurrentSessionEnabled()) {
/*     */               module().disableForCurrentSession();
/*     */             }
/*     */             
/*     */             ChatUtils.sendMessage("АвтоЛес выключен");
/*     */             return 1;
/*     */           }));
/*  43 */     builder.then(literal("mode")
/*  44 */         .then(arg("value", (ArgumentType)StringArgumentType.word())
/*  45 */           .suggests((ctx, suggestions) -> {
/*     */               Objects.requireNonNull(suggestions);
/*     */               module().getModeSuggestions().forEach(suggestions::suggest);
/*     */               return suggestions.buildFuture();
/*  49 */             }).executes(ctx -> {
/*     */               String value = (String)ctx.getArgument("value", String.class);
/*     */               
/*     */               if (!module().setModeAlias(value)) {
/*     */                 ChatUtils.sendMessage("Неизвестный режим. Доступно: normal, fast");
/*     */                 return 1;
/*     */               } 
/*     */               ChatUtils.sendMessage("Режим: " + module().getModeAlias());
/*     */               return 1;
/*     */             })));
/*  59 */     builder.then((ArgumentBuilder)booleanSetting("swing", value -> module().setSwingEnabled(value.booleanValue()), () -> Boolean.valueOf(module().isSwingEnabled())));
/*  60 */     builder.then((ArgumentBuilder)booleanSetting("autosell", value -> module().setAutoSellEnabled(value.booleanValue()), () -> Boolean.valueOf(module().isAutoSellEnabled())));
/*  61 */     builder.then((ArgumentBuilder)booleanSetting("autopay", value -> module().setAutoPayEnabled(value.booleanValue()), () -> Boolean.valueOf(module().isAutoPayEnabled())));
/*  62 */     builder.then((ArgumentBuilder)booleanSetting("visuals", value -> module().setPreserveVisualsEnabled(value.booleanValue()), () -> Boolean.valueOf(module().isPreserveVisualsEnabled())));
/*     */     
/*  64 */     builder.then((ArgumentBuilder)floatSetting("pps", value -> module().setPacketsPerSecond(value.floatValue()), () -> Float.valueOf(module().getPacketsPerSecond())));
/*  65 */     builder.then((ArgumentBuilder)floatSetting("radius", value -> module().setBreakRadius(value.floatValue()), () -> Float.valueOf(module().getBreakRadius())));
/*  66 */     builder.then((ArgumentBuilder)floatSetting("payamount", value -> module().setPayAmount(value.floatValue()), () -> Float.valueOf(module().getPayAmount())));
/*  67 */     builder.then((ArgumentBuilder)floatSetting("interval", value -> module().setIntervalSeconds(value.floatValue()), () -> Float.valueOf(module().getIntervalSeconds())));
/*     */     
/*  69 */     builder.then(((LiteralArgumentBuilder)literal("pay")
/*  70 */         .then(literal("clear").executes(ctx -> {
/*     */               module().clearPayTarget();
/*     */               
/*     */               ChatUtils.sendMessage("Ник для перевода очищен");
/*     */               return 1;
/*  75 */             }))).then(arg("nick", (ArgumentType)StringArgumentType.word()).executes(ctx -> {
/*     */               String nick = (String)ctx.getArgument("nick", String.class);
/*     */               
/*     */               if (!module().setPayTarget(nick)) {
/*     */                 ChatUtils.sendMessage("Ник не может быть пустым");
/*     */                 return 1;
/*     */               } 
/*     */               ChatUtils.sendMessage("Ник для перевода: " + module().getPayTarget());
/*     */               return 1;
/*     */             })));
/*  85 */     builder.then(literal("status").executes(ctx -> {
/*     */             sendStatus();
/*     */             return 1;
/*     */           }));
/*     */   }
/*     */   
/*     */   private LiteralArgumentBuilder<class_2172> booleanSetting(String name, Consumer<Boolean> setter, Supplier<Boolean> getter) {
/*  92 */     return (LiteralArgumentBuilder<class_2172>)literal(name).then(arg("value", (ArgumentType)BoolArgumentType.bool())
/*  93 */         .suggests((ctx, suggestions) -> {
/*     */             suggestions.suggest("true");
/*     */             
/*     */             suggestions.suggest("false");
/*     */             return suggestions.buildFuture();
/*  98 */           }).executes(ctx -> {
/*     */             boolean value = BoolArgumentType.getBool(ctx, "value");
/*     */             setter.accept(Boolean.valueOf(value));
/*     */             ChatUtils.sendMessage(settingLabel(name) + ": " + settingLabel(name));
/*     */             return 1;
/*     */           }));
/*     */   }
/*     */   
/*     */   private LiteralArgumentBuilder<class_2172> floatSetting(String name, Consumer<Float> setter, Supplier<Float> getter) {
/* 107 */     return (LiteralArgumentBuilder<class_2172>)literal(name).then(arg("value", (ArgumentType)FloatArgumentType.floatArg())
/* 108 */         .executes(ctx -> {
/*     */             float value = FloatArgumentType.getFloat(ctx, "value");
/*     */             setter.accept(Float.valueOf(value));
/*     */             ChatUtils.sendMessage(settingLabel(name) + ": " + settingLabel(name));
/*     */             return 1;
/*     */           }));
/*     */   }
/*     */   
/*     */   private void sendStatus() {
/* 117 */     AutoForest module = module();
/* 118 */     ChatUtils.sendMessage("АвтоЛес: " + (module.isCurrentSessionEnabled() ? "включён" : "выключен"));
/* 119 */     ChatUtils.sendMessage("Режим=" + module.getModeAlias() + ", Мах рукой=" + 
/* 120 */         booleanText(module.isSwingEnabled()) + ", Автопродажа=" + 
/* 121 */         booleanText(module.isAutoSellEnabled()) + ", AutoPay=" + 
/* 122 */         booleanText(module.isAutoPayEnabled()) + ", Визуализация=" + 
/* 123 */         booleanText(module.isPreserveVisualsEnabled()));
/* 124 */     ChatUtils.sendMessage("Пакетов в секунду=" + module.getPacketsPerSecond() + ", Радиус=" + module
/* 125 */         .getBreakRadius() + ", Сумма перевода=" + module
/* 126 */         .getPayAmount() + ", Задержка=" + module
/* 127 */         .getIntervalSeconds() + ", Ник перевода=" + (
/* 128 */         module.getPayTarget().isBlank() ? "<пусто>" : module.getPayTarget()));
/*     */   }
/*     */   
/*     */   private AutoForest module() {
/* 132 */     return ModuleClass.autoForest;
/*     */   }
/*     */   
/*     */   private String booleanText(boolean value) {
/* 136 */     return value ? "включено" : "выключено";
/*     */   }
/*     */   
/*     */   private String settingLabel(String name) {
/* 140 */     switch (name) { case "swing": case "autosell": case "autopay": case "visuals": case "pps": case "radius": case "payamount": case "interval":  }  return 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 149 */       name;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\api\commands\impl\AutoLesCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */