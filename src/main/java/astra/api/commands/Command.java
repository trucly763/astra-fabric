/*    */ package shame.astra.api.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import lombok.Generated;
/*    */ import net.minecraft.class_2172;
/*    */ import shame.astra.api.QClient;
/*    */ 
/*    */ public abstract class Command implements QClient {
/*    */   @Generated
/*    */   public String getCommand() {
/* 13 */     return this.command;
/*    */   } private final String command;
/*    */   public Command(String command) {
/* 16 */     this.command = command;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void register(CommandDispatcher<class_2172> dispatcher) {
/* 22 */     LiteralArgumentBuilder<class_2172> builder = LiteralArgumentBuilder.literal(this.command);
/* 23 */     execute(builder);
/* 24 */     dispatcher.register(builder);
/*    */   }
/*    */   
/*    */   protected <T> RequiredArgumentBuilder<class_2172, T> arg(String name, ArgumentType<T> type) {
/* 28 */     return RequiredArgumentBuilder.argument(name, type);
/*    */   }
/*    */   
/*    */   protected LiteralArgumentBuilder<class_2172> literal(String name) {
/* 32 */     return LiteralArgumentBuilder.literal(name);
/*    */   }
/*    */   
/*    */   public abstract void execute(LiteralArgumentBuilder<class_2172> paramLiteralArgumentBuilder);
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\api\commands\Command.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */