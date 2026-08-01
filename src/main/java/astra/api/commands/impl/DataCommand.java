/*     */ package shame.astra.api.commands.impl;
/*     */ 
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.StringArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.class_2172;
/*     */ import shame.astra.api.commands.Command;
/*     */ import shame.astra.api.storages.implement.helpertstorages.enumvar.ModuleClass;
/*     */ import shame.astra.api.utils.chat.ChatUtils;
/*     */ import shame.astra.client.modules.impl.combat.Aura;
/*     */ 
/*     */ public class DataCommand extends Command {
/*     */   public DataCommand() {
/*  20 */     super("data");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(LiteralArgumentBuilder<class_2172> builder) {
/*  26 */     ((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)builder
/*  27 */       .executes(context -> {
/*     */           sendStatus();
/*     */           
/*     */           return 1;
/*  31 */         })).then(literal("record")
/*  32 */         .executes(context -> {
/*     */             Aura aura = getAura();
/*     */             
/*     */             if (aura == null) {
/*     */               return 1;
/*     */             }
/*     */             
/*     */             aura.getDataSystem().startRecording();
/*     */             ChatUtils.sendMessage("Data: запись начата, старые паттерны в памяти очищены");
/*     */             return 1;
/*  42 */           }))).then(((LiteralArgumentBuilder)literal("stop")
/*  43 */         .executes(context -> {
/*     */             stopRecording("data_" + System.currentTimeMillis());
/*     */             
/*     */             return 1;
/*  47 */           })).then(arg("name", (ArgumentType)StringArgumentType.greedyString())
/*  48 */           .executes(context -> {
/*     */               stopRecording((String)context.getArgument("name", String.class));
/*     */               
/*     */               return 1;
/*  52 */             })))).then(literal("play")
/*  53 */         .then(arg("name", (ArgumentType)StringArgumentType.word())
/*  54 */           .suggests((context, suggestions) -> {
/*     */               Objects.requireNonNull(suggestions);
/*     */               
/*     */               getAuraPatterns().stream().filter(()).forEach(suggestions::suggest);
/*     */               
/*     */               return suggestions.buildFuture();
/*  60 */             }).executes(context -> {
/*     */               playProfile((String)context.getArgument("name", String.class));
/*     */               
/*     */               return 1;
/*  64 */             })))).then(literal("delete")
/*  65 */         .then(arg("name", (ArgumentType)StringArgumentType.word())
/*  66 */           .suggests((context, suggestions) -> {
/*     */               Objects.requireNonNull(suggestions);
/*     */               
/*     */               getAuraPatterns().stream().filter(()).forEach(suggestions::suggest);
/*     */               
/*     */               return suggestions.buildFuture();
/*  72 */             }).executes(context -> {
/*     */               deleteProfile((String)context.getArgument("name", String.class));
/*     */               
/*     */               return 1;
/*  76 */             })))).then(literal("list")
/*  77 */         .executes(context -> {
/*     */             listProfiles();
/*     */             
/*     */             return 1;
/*  81 */           }))).then(literal("clear")
/*  82 */         .executes(context -> {
/*     */             Aura aura = getAura();
/*     */             
/*     */             if (aura == null) {
/*     */               return 1;
/*     */             }
/*     */             
/*     */             aura.getDataSystem().clearPatterns();
/*     */             ChatUtils.sendMessage("Data: паттерны очищены");
/*     */             return 1;
/*  92 */           }))).then(literal("status")
/*  93 */         .executes(context -> {
/*     */             sendStatus();
/*     */             return 1;
/*     */           }));
/*     */   }
/*     */   
/*     */   private void stopRecording(String name) {
/* 100 */     Aura aura = getAura();
/* 101 */     if (aura == null) {
/*     */       return;
/*     */     }
/*     */     
/* 105 */     if (!aura.getDataSystem().isRecording()) {
/* 106 */       ChatUtils.sendMessage("Data: запись не запущена");
/*     */       
/*     */       return;
/*     */     } 
/* 110 */     if (!aura.getDataSystem().savePatterns(name)) {
/* 111 */       ChatUtils.sendMessage("Data: нечего сохранять");
/*     */       
/*     */       return;
/*     */     } 
/* 115 */     aura.getDataSystem().stopRecording();
/* 116 */     ChatUtils.sendMessage("Data: запись остановлена и сохранена как " + name);
/*     */   }
/*     */   
/*     */   private void playProfile(String name) {
/* 120 */     Aura aura = getAura();
/* 121 */     if (aura == null) {
/*     */       return;
/*     */     }
/*     */     
/* 125 */     if (!aura.getDataSystem().loadPatterns(name)) {
/* 126 */       ChatUtils.sendMessage("Data: профиль " + name + " не найден или поврежден");
/*     */       
/*     */       return;
/*     */     } 
/* 130 */     aura.getDataSystem().setRecording(false);
/* 131 */     aura.getDataSystem().setUsingNeuro(true);
/* 132 */     aura.getDataSystem().resetState();
/* 133 */     ChatUtils.sendMessage("Data: загружен профиль " + name + " (" + aura.getDataSystem().getPatternCount() + " паттернов)");
/* 134 */     ChatUtils.sendMessage("Data: выбери режим ротации Data в Aura");
/*     */   }
/*     */   
/*     */   private void deleteProfile(String name) {
/* 138 */     Aura aura = getAura();
/* 139 */     if (aura == null) {
/*     */       return;
/*     */     }
/*     */     
/* 143 */     if (aura.getDataSystem().deletePatterns(name)) {
/* 144 */       ChatUtils.sendMessage("Data: профиль " + name + " удален");
/*     */     } else {
/* 146 */       ChatUtils.sendMessage("Data: профиль " + name + " не найден");
/*     */     } 
/*     */   }
/*     */   
/*     */   private void listProfiles() {
/* 151 */     List<String> patterns = getAuraPatterns();
/* 152 */     if (patterns.isEmpty()) {
/* 153 */       ChatUtils.sendMessage("Data: нет сохраненных профилей");
/*     */       return;
/*     */     } 
/* 156 */     ChatUtils.sendMessage("Data: сохраненные профили (" + patterns.size() + "):");
/* 157 */     for (String name : patterns) {
/* 158 */       ChatUtils.sendMessage("  - " + name);
/*     */     }
/*     */   }
/*     */   
/*     */   private void sendStatus() {
/* 163 */     Aura aura = getAura();
/* 164 */     if (aura == null) {
/*     */       return;
/*     */     }
/*     */     
/* 168 */     ChatUtils.sendMessage(aura.getDataSystem().getStatusString());
/* 169 */     if (aura.getDataSystem().getPatternCount() > 0) {
/* 170 */       ChatUtils.sendMessage("Новых в сессии: " + aura.getDataSystem().getRecordedThisSession());
/*     */     }
/*     */   }
/*     */   
/*     */   private Aura getAura() {
/* 175 */     Aura aura = (ModuleClass.INSTANCE == null) ? null : ModuleClass.aura;
/* 176 */     if (aura == null) {
/* 177 */       ChatUtils.sendMessage("Data: модуль Aura не найден");
/*     */     }
/* 179 */     return aura;
/*     */   }
/*     */   
/*     */   private List<String> getAuraPatterns() {
/* 183 */     Aura aura = (ModuleClass.INSTANCE == null) ? null : ModuleClass.aura;
/* 184 */     return (aura == null) ? List.<String>of() : aura.getDataSystem().getPatternNames();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\api\commands\impl\DataCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */