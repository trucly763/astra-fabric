/*    */ package shame.astra.api.storages.implement;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import lombok.Generated;
/*    */ import net.minecraft.class_2172;
/*    */ import net.minecraft.class_637;
/*    */ import shame.astra.api.commands.Command;
/*    */ import shame.astra.api.commands.impl.AutoLesCommand;
/*    */ import shame.astra.api.commands.impl.BindCommand;
/*    */ import shame.astra.api.commands.impl.BlockESPCommand;
/*    */ import shame.astra.api.commands.impl.BotCommand;
/*    */ import shame.astra.api.commands.impl.ConfigCommand;
/*    */ import shame.astra.api.commands.impl.MacroCommand;
/*    */ import shame.astra.api.commands.impl.NukerCommand;
/*    */ import shame.astra.api.commands.impl.VClipCommand;
/*    */ 
/*    */ public class CommandStorage {
/* 19 */   private final CommandDispatcher<class_2172> dispatcher = new CommandDispatcher(); @Generated public CommandDispatcher<class_2172> getDispatcher() { return this.dispatcher; }
/* 20 */    private final List<Command> commands = new ArrayList<>(); @Generated public List<Command> getCommands() { return this.commands; }
/* 21 */    private String prefix = "."; @Generated public String getPrefix() { return this.prefix; } @Generated public void setPrefix(String prefix) { this.prefix = prefix; }
/*    */   
/*    */   public CommandStorage() {
/* 24 */     registry();
/*    */   }
/*    */   
/*    */   private void registry() {
/* 28 */     addCommands(new Command[] { (Command)new AutoLesCommand(), (Command)new FriendCommand(), (Command)new ConfigCommand(), (Command)new MacroCommand(), (Command)new BotCommand(), (Command)new BlockESPCommand(), (Command)new NukerCommand(), (Command)new NukerCommand("nuk"), (Command)new GPSCommand(), (Command)new BindCommand(), (Command)new StaffCommand(), (Command)new VClipCommand(), (Command)new DataCommand() });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public class_2172 getSource() {
/* 46 */     return (class_2172)new class_637(null, class_310.method_1551());
/*    */   }
/*    */   
/*    */   private void addCommands(Command... command) {
/* 50 */     for (Command cmd : command) {
/* 51 */       cmd.register(this.dispatcher);
/* 52 */       this.commands.add(cmd);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\api\storages\implement\CommandStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */