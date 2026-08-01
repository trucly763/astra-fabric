/*     */ package shame.astra.api.commands.impl;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.StringArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.io.File;
/*     */ import java.util.Arrays;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.class_2172;
/*     */ import shame.astra.api.commands.Command;
/*     */ import shame.astra.api.utils.chat.ChatUtils;
/*     */ import shame.astra.astra;
/*     */ 
/*     */ public class ConfigCommand extends Command {
/*     */   public ConfigCommand() {
/*  19 */     super("config");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(LiteralArgumentBuilder<class_2172> builder) {
/*  25 */     ((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)builder
/*  26 */       .then(literal("save")
/*  27 */         .then(arg("config", (ArgumentType)StringArgumentType.word())
/*  28 */           .suggests((context, builder1) -> {
/*     */               if (astra.INSTANCE.configsDir.exists() && astra.INSTANCE.configsDir.isDirectory()) {
/*     */                 File[] files = astra.INSTANCE.configsDir.listFiles(());
/*     */ 
/*     */                 
/*     */                 if (files != null) {
/*     */                   Objects.requireNonNull(builder1);
/*     */ 
/*     */                   
/*     */                   Arrays.<File>stream(files).map(File::getName).map(()).forEach(builder1::suggest);
/*     */                 } 
/*     */               } 
/*     */               
/*     */               return builder1.buildFuture();
/*  42 */             }).executes(context -> {
/*     */               String config = (String)context.getArgument("config", String.class);
/*     */               try {
/*     */                 astra.INSTANCE.configStorage.saveConfig(config);
/*     */                 ChatUtils.sendMessage("Конфиг " + config + " успешно сохранён!");
/*  47 */               } catch (Exception e) {
/*     */                 ChatUtils.sendMessage("Ошибка при сохранении конфига " + config + "!");
/*     */ 
/*     */                 
/*     */                 e.printStackTrace();
/*     */               } 
/*     */ 
/*     */               
/*     */               return 1;
/*  56 */             })))).then(literal("load")
/*  57 */         .then(arg("config", (ArgumentType)StringArgumentType.word())
/*  58 */           .suggests((context, builder1) -> {
/*     */               if (astra.INSTANCE.configsDir.exists() && astra.INSTANCE.configsDir.isDirectory()) {
/*     */                 File[] files = astra.INSTANCE.configsDir.listFiles(());
/*     */ 
/*     */                 
/*     */                 if (files != null) {
/*     */                   Objects.requireNonNull(builder1);
/*     */ 
/*     */                   
/*     */                   Arrays.<File>stream(files).map(File::getName).map(()).forEach(builder1::suggest);
/*     */                 } 
/*     */               } 
/*     */               
/*     */               return builder1.buildFuture();
/*  72 */             }).executes(context -> {
/*     */               String config = (String)context.getArgument("config", String.class);
/*     */               try {
/*     */                 astra.INSTANCE.configStorage.loadConfig(config);
/*     */                 ChatUtils.sendMessage("Конфиг " + config + " успешно загружен!");
/*  77 */               } catch (Exception e) {
/*     */                 ChatUtils.sendMessage("Ошибка при загрузке конфига " + config + "!");
/*     */ 
/*     */                 
/*     */                 e.printStackTrace();
/*     */               } 
/*     */ 
/*     */               
/*     */               return 1;
/*  86 */             })))).then(literal("list")
/*  87 */         .executes(context -> {
/*     */             File[] files = astra.INSTANCE.configsDir.listFiles(());
/*     */             
/*     */             if (files == null || files.length == 0) {
/*     */               ChatUtils.sendMessage("Список конфигов пуст!");
/*     */             } else {
/*     */               StringBuilder builder1 = new StringBuilder();
/*     */               
/*     */               for (int i = 0; i < files.length; i++) {
/*     */                 String fileName = files[i].getName().replace(".wonder", "");
/*     */                 builder1.append(fileName);
/*     */                 if (i < files.length - 1) {
/*     */                   builder1.append(", ");
/*     */                 }
/*     */               } 
/*     */               ChatUtils.sendMessage("Конфиги: " + String.valueOf(builder1));
/*     */             } 
/*     */             return 1;
/* 105 */           }))).then(literal("dir")
/* 106 */         .executes(context -> {
/*     */             try {
/*     */               File configsDir = new File(astra.INSTANCE.globalsDir, "configs");
/*     */               
/*     */               if (!configsDir.exists()) {
/*     */                 configsDir.mkdirs();
/*     */               }
/*     */               (new ProcessBuilder(new String[] { "explorer.exe", configsDir.getAbsolutePath() })).start();
/*     */               ChatUtils.sendMessage("Папка с конфигами открыта!");
/* 115 */             } catch (Exception e) {
/*     */               ChatUtils.sendMessage("Ошибка при открытии папки с конфигами!");
/*     */               e.printStackTrace();
/*     */             } 
/*     */             return 1;
/*     */           }));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\api\commands\impl\ConfigCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */