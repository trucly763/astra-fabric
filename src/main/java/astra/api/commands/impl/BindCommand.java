/*     */ package shame.astra.api.commands.impl;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.StringArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.class_2172;
/*     */ import org.lwjgl.glfw.GLFW;
/*     */ import shame.astra.api.storages.implement.helpertstorages.enumvar.ModuleClass;
/*     */ import shame.astra.api.utils.chat.ChatUtils;
/*     */ import shame.astra.client.modules.Module;
/*     */ 
/*     */ public class BindCommand extends Command {
/*     */   public BindCommand() {
/*  20 */     super("bind");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(LiteralArgumentBuilder<class_2172> builder) {
/*  26 */     builder.then(literal("add")
/*  27 */         .then(arg("module", (ArgumentType)StringArgumentType.word())
/*  28 */           .suggests((context, suggestionsBuilder) -> {
/*     */               String remaining = suggestionsBuilder.getRemaining().toLowerCase();
/*     */               
/*     */               Objects.requireNonNull(suggestionsBuilder);
/*     */               
/*     */               ModuleClass.INSTANCE.getObject().stream().map(Module::getName).filter(()).forEach(suggestionsBuilder::suggest);
/*     */               
/*     */               return suggestionsBuilder.buildFuture();
/*  36 */             }).then(arg("key", (ArgumentType)StringArgumentType.word())
/*  37 */             .suggests((context, suggestionsBuilder) -> {
/*     */                 String remaining = suggestionsBuilder.getRemaining().toUpperCase();
/*     */                 
/*     */                 for (Field field : GLFW.class.getDeclaredFields()) {
/*     */                   String fieldName = field.getName();
/*     */                   
/*     */                   if (fieldName.startsWith("GLFW_KEY_")) {
/*     */                     String keyName = fieldName.replace("GLFW_KEY_", "");
/*     */                     
/*     */                     if (keyName.startsWith(remaining)) {
/*     */                       suggestionsBuilder.suggest(keyName);
/*     */                     }
/*     */                   } 
/*     */                 } 
/*     */                 
/*     */                 if ("NONE".startsWith(remaining)) {
/*     */                   suggestionsBuilder.suggest("NONE");
/*     */                 }
/*     */                 return suggestionsBuilder.buildFuture();
/*  56 */               }).executes(ctx -> {
/*     */                 String moduleName = (String)ctx.getArgument("module", String.class);
/*     */                 
/*     */                 Optional<Module> optionalModule = findModuleByName(moduleName);
/*     */                 
/*     */                 if (optionalModule.isEmpty()) {
/*     */                   ChatUtils.sendMessage("Модуль " + moduleName + " не найден");
/*     */                   
/*     */                   return 1;
/*     */                 } 
/*     */                 Module module = optionalModule.get();
/*     */                 String keyName = ((String)ctx.getArgument("key", String.class)).toUpperCase();
/*     */                 int keyCode = getKeyCode(keyName);
/*     */                 if (keyCode == -1) {
/*     */                   ChatUtils.sendMessage("Клавиша " + keyName + " не найдена");
/*     */                 } else {
/*     */                   module.setKey(keyCode);
/*     */                   ChatUtils.sendMessage("Модуль " + module.getName() + " привязан к клавише " + keyName);
/*     */                 } 
/*     */                 return 1;
/*     */               }))));
/*  77 */     builder.then(literal("remove").then(arg("module", (ArgumentType)StringArgumentType.word()).executes(ctx -> {
/*     */               String moduleName = (String)ctx.getArgument("module", String.class);
/*     */               
/*     */               Optional<Module> optionalModule = findModuleByName(moduleName);
/*     */               
/*     */               if (optionalModule.isEmpty()) {
/*     */                 ChatUtils.sendMessage("Модуль " + moduleName + " не найден");
/*     */                 return 1;
/*     */               } 
/*     */               Module module = optionalModule.get();
/*     */               module.setKey(-1);
/*     */               ChatUtils.sendMessage("Привязка клавиши для модуля " + module.getName() + " удалена");
/*     */               return 1;
/*     */             })));
/*  91 */     builder.then(literal("clear").executes(ctx -> {
/*     */             ModuleClass.INSTANCE.getObject().forEach(());
/*     */             
/*     */             ChatUtils.sendMessage("Все привязки клавиш удалены");
/*     */             return 1;
/*     */           }));
/*  97 */     builder.then(literal("list").executes(ctx -> {
/*     */             StringBuilder bindingsList = new StringBuilder("Список привязанных модулей: ");
/*     */             boolean hasBinds = ModuleClass.INSTANCE.getObject().stream().filter(()).peek(()).findAny().isPresent();
/*     */             if (!hasBinds) {
/*     */               ChatUtils.sendMessage("Нет привязанных модулей");
/*     */             } else {
/*     */               ChatUtils.sendMessage(bindingsList.toString());
/*     */             } 
/*     */             return 1;
/*     */           }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Optional<Module> findModuleByName(String moduleName) {
/* 120 */     return ModuleClass.INSTANCE.getObject().stream()
/* 121 */       .filter(module -> module.getName().equalsIgnoreCase(moduleName))
/* 122 */       .findFirst();
/*     */   }
/*     */   
/*     */   private int getKeyCode(String keyName) {
/* 126 */     if ("NONE".equalsIgnoreCase(keyName)) {
/* 127 */       return -1;
/*     */     }
/*     */     
/*     */     try {
/* 131 */       return GLFW.class.getField("GLFW_KEY_" + keyName).getInt(null);
/* 132 */     } catch (NoSuchFieldException|IllegalAccessException ignored) {
/* 133 */       return -1;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\api\commands\impl\BindCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */