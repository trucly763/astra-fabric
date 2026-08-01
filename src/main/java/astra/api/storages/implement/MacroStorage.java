/*    */ package shame.astra.api.storages.implement;
/*    */ 
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import lombok.Generated;
/*    */ import net.minecraft.class_124;
/*    */ import shame.astra.api.QClient;
/*    */ import shame.astra.api.events.EventInvoker;
/*    */ import shame.astra.api.events.EventLink;
/*    */ import shame.astra.api.events.implement.EventBinding;
/*    */ import shame.astra.api.utils.chat.ChatUtils;
/*    */ import shame.astra.api.utils.cmd.macro.Macro;
/*    */ import shame.astra.astra;
/*    */ 
/*    */ public class MacroStorage
/*    */   implements QClient
/*    */ {
/*    */   private final List<Macro> macros;
/*    */   private final List<String> names;
/*    */   
/*    */   public MacroStorage() {
/* 23 */     this.macros = new ArrayList<>();
/* 24 */     this.names = new ArrayList<>(); EventInvoker.register(this); } @Generated public List<String> getNames() { return this.names; }
/*    */   @Generated
/*    */   public List<Macro> getMacros() { return this.macros; } public void add(Macro macro) {
/* 27 */     if (macro == null || macro.getName() == null || macro.getName().isBlank() || getMacro(macro.getName()) != null) {
/*    */       return;
/*    */     }
/* 30 */     this.macros.add(macro);
/* 31 */     this.names.add(macro.getName());
/*    */   }
/*    */   
/*    */   public void remove(Macro macro) {
/* 35 */     if (macro == null) {
/*    */       return;
/*    */     }
/* 38 */     this.macros.remove(macro);
/* 39 */     this.names.remove(macro.getName());
/*    */   }
/*    */   
/*    */   public void clear() {
/* 43 */     if (!this.macros.isEmpty()) this.macros.clear(); 
/* 44 */     if (!this.names.isEmpty()) this.names.clear(); 
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 48 */     return this.macros.isEmpty();
/*    */   }
/*    */   
/*    */   public Macro getMacro(String name) {
/* 52 */     for (Macro macro : this.macros) {
/* 53 */       if (!macro.getName().equalsIgnoreCase(name))
/* 54 */         continue;  return macro;
/*    */     } 
/*    */     
/* 57 */     return null;
/*    */   }
/*    */   
/*    */   @EventLink
/*    */   public void onKey(EventBinding e) {
/* 62 */     if (mc.field_1724 == null || mc.field_1687 == null || mc.field_1755 != null || mc.field_1724.field_3944 == null || this.macros.isEmpty())
/*    */       return; 
/* 64 */     for (Macro macro : this.macros) {
/* 65 */       if (macro == null || macro.getBind() == null || macro.getBind().getKey() != e.getKey()) {
/*    */         continue;
/*    */       }
/* 68 */       executeMacro(macro);
/*    */     } 
/*    */   }
/*    */   
/*    */   private void executeMacro(Macro macro) {
/* 73 */     String command = macro.getCommand();
/* 74 */     if (command == null || command.isBlank()) {
/*    */       return;
/*    */     }
/*    */     
/* 78 */     if (command.startsWith("/")) {
/* 79 */       mc.field_1724.field_3944.method_45730(command.substring(1));
/*    */       
/*    */       return;
/*    */     } 
/* 83 */     String prefix = astra.INSTANCE.commandStorage.getPrefix();
/* 84 */     if (prefix != null && !prefix.isEmpty() && command.startsWith(prefix)) {
/*    */       try {
/* 86 */         astra.INSTANCE.commandStorage.getDispatcher().execute(command
/* 87 */             .substring(prefix.length()), astra.INSTANCE.commandStorage
/* 88 */             .getSource());
/*    */       }
/* 90 */       catch (CommandSyntaxException ignored) {
/* 91 */         ChatUtils.sendMessage(String.valueOf(class_124.field_1061) + "Ошибка в использовании макроса " + String.valueOf(class_124.field_1061) + "!");
/*    */       } 
/*    */       
/*    */       return;
/*    */     } 
/* 96 */     mc.field_1724.field_3944.method_45729(command);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\api\storages\implement\MacroStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */