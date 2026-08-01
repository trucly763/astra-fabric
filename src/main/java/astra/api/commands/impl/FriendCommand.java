/*    */ package shame.astra.api.commands.impl;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.StringArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.Objects;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.class_2172;
/*    */ import net.minecraft.class_640;
/*    */ import shame.astra.api.utils.chat.ChatUtils;
/*    */ import shame.astra.astra;
/*    */ 
/*    */ public class FriendCommand extends Command {
/*    */   public FriendCommand() {
/* 17 */     super("friend");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(LiteralArgumentBuilder<class_2172> builder) {
/* 23 */     ((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)builder
/* 24 */       .then(literal("add")
/* 25 */         .then(arg("player", (ArgumentType)StringArgumentType.word())
/* 26 */           .suggests((context, builder1) -> {
/*    */               for (class_640 entry : mc.method_1562().method_2880()) {
/*    */                 String name = entry.method_2966().getName();
/*    */                 if (name.toLowerCase().startsWith(builder1.getRemaining().toLowerCase())) {
/*    */                   builder1.suggest(name);
/*    */                 }
/*    */               } 
/*    */               return builder1.buildFuture();
/* 34 */             }).executes(context -> {
/*    */               String player = (String)context.getArgument("player", String.class);
/*    */               
/*    */               if (!astra.INSTANCE.friendStorage.isFriend(player)) {
/*    */                 astra.INSTANCE.friendStorage.add(player);
/*    */                 
/*    */                 ChatUtils.sendMessage("Игрок " + player + " добавлен в друзья!");
/*    */               } else {
/*    */                 ChatUtils.sendMessage("Игрок " + player + " уже в списке друзей!");
/*    */               } 
/*    */               
/*    */               return 1;
/* 46 */             })))).then(literal("remove")
/* 47 */         .then(arg("player", (ArgumentType)StringArgumentType.word())
/* 48 */           .suggests((context, builder1) -> {
/*    */               Objects.requireNonNull(builder1);
/*    */ 
/*    */               
/*    */               astra.INSTANCE.friendStorage.getFriends().stream().sorted(String::compareTo).filter(()).forEach(builder1::suggest);
/*    */               
/*    */               return builder1.buildFuture();
/* 55 */             }).executes(context -> {
/*    */               String player = (String)context.getArgument("player", String.class);
/*    */               
/*    */               if (astra.INSTANCE.friendStorage.isFriend(player)) {
/*    */                 astra.INSTANCE.friendStorage.remove(player);
/*    */                 
/*    */                 ChatUtils.sendMessage("Игрок " + player + " удалён из друзей!");
/*    */               } else {
/*    */                 ChatUtils.sendMessage("Игрок " + player + " не найден в списке друзей!");
/*    */               } 
/*    */               
/*    */               return 1;
/* 67 */             })))).then(literal("list")
/* 68 */         .executes(context -> {
/*    */             if (astra.INSTANCE.friendStorage.getFriends().isEmpty()) {
/*    */               ChatUtils.sendMessage("Список друзей пуст!");
/*    */             } else {
/*    */               StringBuilder builder1 = new StringBuilder();
/*    */               
/*    */               for (int i = 0; i < astra.INSTANCE.friendStorage.getFriends().size(); i++) {
/*    */                 builder1.append(astra.INSTANCE.friendStorage.getFriends().get(i));
/*    */                 if (i < astra.INSTANCE.friendStorage.getFriends().size() - 1) {
/*    */                   builder1.append(", ");
/*    */                 }
/*    */               } 
/*    */               ChatUtils.sendMessage("Друзья: " + String.valueOf(builder1));
/*    */             } 
/*    */             return 1;
/* 83 */           }))).then(literal("clear")
/* 84 */         .executes(context -> {
/*    */             if (!astra.INSTANCE.friendStorage.isEmpty()) {
/*    */               astra.INSTANCE.friendStorage.clear();
/*    */               ChatUtils.sendMessage("Список друзей очищен!");
/*    */             } else {
/*    */               ChatUtils.sendMessage("Список друзей пуст!");
/*    */             } 
/*    */             return 1;
/*    */           }));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\api\commands\impl\FriendCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */