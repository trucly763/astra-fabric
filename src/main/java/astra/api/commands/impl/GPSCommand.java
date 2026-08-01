/*    */ package shame.astra.api.commands.impl;
/*    */ 
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.class_1074;
/*    */ import net.minecraft.class_2172;
/*    */ import shame.astra.api.commands.Command;
/*    */ import shame.astra.api.utils.chat.ChatUtils;
/*    */ import shame.astra.api.utils.cmd.waypoint.Waypoint;
/*    */ import shame.astra.astra;
/*    */ 
/*    */ public class GPSCommand
/*    */   extends Command {
/*    */   public GPSCommand() {
/* 18 */     super("gps");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(LiteralArgumentBuilder<class_2172> builder) {
/* 24 */     ((LiteralArgumentBuilder)builder
/* 25 */       .then(arg("X", (ArgumentType)IntegerArgumentType.integer())
/* 26 */         .then(arg("Z", (ArgumentType)IntegerArgumentType.integer())
/* 27 */           .executes(context -> {
/*    */               int x = ((Integer)context.getArgument("X", Integer.class)).intValue();
/*    */               
/*    */               int z = ((Integer)context.getArgument("Z", Integer.class)).intValue();
/*    */               
/*    */               Waypoint waypoint = new Waypoint(x, z);
/*    */               
/*    */               astra.INSTANCE.waypointStorage.set(waypoint);
/*    */               
/*    */               ChatUtils.sendMessage(class_1074.method_4662("Метка поставлена: ", new Object[] { Integer.valueOf(x), Integer.valueOf(z) }));
/*    */               
/*    */               return 1;
/* 39 */             })))).then(literal("remove")
/* 40 */         .executes(context -> {
/*    */             if (!astra.INSTANCE.waypointStorage.isEmpty()) {
/*    */               astra.INSTANCE.waypointStorage.clear();
/*    */               ChatUtils.sendMessage(class_1074.method_4662("Метка удалена!", new Object[0]));
/*    */             } else {
/*    */               ChatUtils.sendMessage(class_1074.method_4662("Метки не было", new Object[0]));
/*    */             } 
/*    */             return 1;
/*    */           }));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\api\commands\impl\GPSCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */