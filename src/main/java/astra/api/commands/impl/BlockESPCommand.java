/*     */ package shame.astra.api.commands.impl;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.StringArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.class_2172;
/*     */ import net.minecraft.class_2248;
/*     */ import net.minecraft.class_2960;
/*     */ import net.minecraft.class_7923;
/*     */ import shame.astra.api.utils.chat.ChatUtils;
/*     */ import shame.astra.client.modules.impl.render.BlockESP;
/*     */ 
/*     */ public class BlockESPCommand extends Command {
/*     */   public BlockESPCommand() {
/*  21 */     super("blockesp");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(LiteralArgumentBuilder<class_2172> builder) {
/*  27 */     ((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)builder
/*  28 */       .then(literal("add")
/*  29 */         .then(arg("block", (ArgumentType)StringArgumentType.word())
/*  30 */           .suggests((context, builder1) -> {
/*     */               String input = builder1.getRemaining().toLowerCase();
/*     */               
/*     */               Objects.requireNonNull(class_7923.field_41175);
/*     */               
/*     */               Objects.requireNonNull(builder1);
/*     */               
/*     */               class_7923.field_41175.method_10220().map(class_7923.field_41175::method_10221).map(class_2960::method_12832).filter(()).limit(20L).forEach(builder1::suggest);
/*     */               
/*     */               return builder1.buildFuture();
/*  40 */             }).executes(context -> {
/*     */               String blockName = (String)context.getArgument("block", String.class);
/*     */ 
/*     */               
/*     */               if (BlockESP.INSTANCE.isTracking(blockName)) {
/*     */                 ChatUtils.sendMessage("§cБлок §e" + blockName + "§c уже отслеживается!");
/*     */ 
/*     */                 
/*     */                 return 1;
/*     */               } 
/*     */ 
/*     */               
/*     */               boolean exists = class_7923.field_41175.method_10220().anyMatch(());
/*     */               
/*     */               if (!exists) {
/*     */                 ChatUtils.sendMessage("§cБлок §e" + blockName + "§c не найден!");
/*     */                 
/*     */                 return 1;
/*     */               } 
/*     */               
/*     */               BlockESP.INSTANCE.addBlock(blockName);
/*     */               
/*     */               ChatUtils.sendMessage("§aБлок §e" + blockName + "§a добавлен в отслеживание!");
/*     */               
/*     */               return 1;
/*  65 */             })))).then(literal("remove")
/*  66 */         .then(arg("block", (ArgumentType)StringArgumentType.word())
/*  67 */           .suggests((context, builder1) -> {
/*     */               Objects.requireNonNull(builder1);
/*     */ 
/*     */               
/*     */               BlockESP.INSTANCE.getTrackedBlocks().stream().sorted(String::compareTo).filter(()).forEach(builder1::suggest);
/*     */               
/*     */               return builder1.buildFuture();
/*  74 */             }).executes(context -> {
/*     */               String blockName = (String)context.getArgument("block", String.class);
/*     */               
/*     */               if (!BlockESP.INSTANCE.isTracking(blockName)) {
/*     */                 ChatUtils.sendMessage("§cБлок §e" + blockName + "§c не отслеживается!");
/*     */                 
/*     */                 return 1;
/*     */               } 
/*     */               
/*     */               BlockESP.INSTANCE.removeBlock(blockName);
/*     */               
/*     */               ChatUtils.sendMessage("§aБлок §e" + blockName + "§a удалён из отслеживания!");
/*     */               
/*     */               return 1;
/*  88 */             })))).then(literal("list")
/*  89 */         .executes(context -> {
/*     */             Set<String> blocks = BlockESP.INSTANCE.getTrackedBlocks();
/*     */ 
/*     */             
/*     */             if (blocks.isEmpty()) {
/*     */               ChatUtils.sendMessage("§cСписок отслеживаемых блоков пуст!");
/*     */ 
/*     */               
/*     */               return 1;
/*     */             } 
/*     */             
/*     */             String blockList = blocks.stream().sorted().collect(Collectors.joining("§7, §e"));
/*     */             
/*     */             ChatUtils.sendMessage("§aОтслеживаемые блоки §7(§e" + blocks.size() + "§7)§a: §e" + blockList);
/*     */             
/*     */             return 1;
/* 105 */           }))).then(literal("clear")
/* 106 */         .executes(context -> {
/*     */             if (BlockESP.INSTANCE.getTrackedBlocks().isEmpty()) {
/*     */               ChatUtils.sendMessage("§cСписок отслеживаемых блоков уже пуст!");
/*     */               return 1;
/*     */             } 
/*     */             BlockESP.INSTANCE.clearBlocks();
/*     */             ChatUtils.sendMessage("§aСписок отслеживаемых блоков очищен!");
/*     */             return 1;
/*     */           }));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\api\commands\impl\BlockESPCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */