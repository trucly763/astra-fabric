/*     */ package shame.astra.client.modules.impl.misc;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import net.minecraft.class_1297;
/*     */ import net.minecraft.class_1657;
/*     */ import net.minecraft.class_2561;
/*     */ import net.minecraft.class_268;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.EventUpdate;
/*     */ import shame.astra.api.utils.chat.ChatUtils;
/*     */ import shame.astra.astra;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*     */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*     */ import shame.astra.client.modules.settings.implement.ListSetting;
/*     */ import shame.astra.client.modules.settings.implement.ModeSetting;
/*     */ 
/*     */ public class AutoLeave extends Module {
/*  23 */   public static final AutoLeave INSTANCE = new AutoLeave();
/*     */   
/*  25 */   private static final Set<String> STAFF_PREFIXES = new HashSet<>(Arrays.asList(new String[] { "supp", "mod", "der", "adm", "wne", "curat", "dev", "yt", "мод", "помо", "адм", "владе", "курато", "сапп", "ютуб", "стажер", "сотрудник" }));
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  30 */   private final FloatSetting leaveDistance = new FloatSetting("Дистанция срабатывания", 5.0F, 3.0F, 50.0F, 1.0F);
/*  31 */   private final ListSetting leaveIfSeen = new ListSetting("Выходить если замечен", new BooleanSetting[] { new BooleanSetting("Игрок", true), new BooleanSetting("Модератор", false) });
/*     */ 
/*     */ 
/*     */   
/*  35 */   private final ModeSetting leaveType = new ModeSetting("Тип выхода", "В мейн меню", new String[] { "В мейн меню", "/hub", "/home", "/spawn" });
/*  36 */   private final BooleanSetting stopBaritone = new BooleanSetting("Выключать баритон", false);
/*  37 */   private final BooleanSetting leaveDisable = new BooleanSetting("Выключать после выхода", true);
/*     */   
/*     */   private int cooldownTicks;
/*     */   
/*     */   public AutoLeave() {
/*  42 */     super("AutoLeave", "Выходит с сервера, когда замечает поблизости игрока", Module.ModuleCategory.MISC);
/*  43 */     addSettings(new Setting[] { (Setting)this.leaveDistance, (Setting)this.leaveIfSeen, (Setting)this.leaveType, (Setting)this.stopBaritone, (Setting)this.leaveDisable });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  48 */     this.cooldownTicks = 0;
/*  49 */     super.onEnable();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  54 */     this.cooldownTicks = 0;
/*  55 */     super.onDisable();
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onUpdate(EventUpdate event) {
/*  60 */     if (mc.field_1724 == null || mc.field_1687 == null) {
/*     */       return;
/*     */     }
/*     */     
/*  64 */     if (this.cooldownTicks > 0) {
/*  65 */       this.cooldownTicks--;
/*     */       
/*     */       return;
/*     */     } 
/*  69 */     float maxDistance = this.leaveDistance.get();
/*  70 */     for (class_1657 player : mc.field_1687.method_18456()) {
/*  71 */       if (player == null || player == mc.field_1724) {
/*     */         continue;
/*     */       }
/*     */       
/*  75 */       if (mc.field_1724.method_5739((class_1297)player) <= maxDistance && shouldLeaveFor(player)) {
/*  76 */         triggerLeave();
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean shouldLeaveFor(class_1657 player) {
/*  83 */     if (isModerator(player)) {
/*  84 */       return this.leaveIfSeen.is("Модератор");
/*     */     }
/*  86 */     return this.leaveIfSeen.is("Игрок");
/*     */   }
/*     */   
/*     */   private boolean isModerator(class_1657 player) {
/*  90 */     if (player == null) {
/*  91 */       return false;
/*     */     }
/*     */     
/*  94 */     String name = player.method_5477().getString();
/*  95 */     if (astra.INSTANCE != null && astra.INSTANCE.staffStorage != null && astra.INSTANCE.staffStorage.isStaff(name)) {
/*  96 */       return true;
/*     */     }
/*     */     
/*  99 */     class_268 team = player.method_5781();
/* 100 */     if (team == null) {
/* 101 */       return false;
/*     */     }
/*     */     
/* 104 */     String prefix = team.method_1144().getString().toLowerCase(Locale.ROOT);
/* 105 */     for (String candidate : STAFF_PREFIXES) {
/* 106 */       if (prefix.contains(candidate)) {
/* 107 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 111 */     return false;
/*     */   }
/*     */   
/*     */   private void triggerLeave() {
/* 115 */     tryStopBaritone();
/*     */     
/* 117 */     switch (this.leaveType.getCurrent()) { case "В мейн меню":
/* 118 */         disconnectLeave(); break;
/* 119 */       case "/hub": commandLeave("hub"); break;
/* 120 */       case "/home": commandLeave("home home"); break;
/* 121 */       case "/spawn": commandLeave("spawn");
/*     */         break; }
/*     */   
/*     */   }
/*     */   private void tryStopBaritone() {
/* 126 */     if (!this.stopBaritone.isState() || mc.method_1562() == null) {
/*     */       return;
/*     */     }
/* 129 */     mc.method_1562().method_45729("#stop");
/*     */   }
/*     */   
/*     */   private void disconnectLeave() {
/* 133 */     if (mc.method_1562() == null) {
/* 134 */       ChatUtils.sendMessage("Модуль не работает в одиночном мире");
/*     */       
/*     */       return;
/*     */     } 
/* 138 */     mc.method_1562().method_48296().method_10747((class_2561)class_2561.method_43470("AutoLeave"));
/* 139 */     if (this.leaveDisable.isState()) {
/* 140 */       toggle();
/*     */     }
/*     */   }
/*     */   
/*     */   private void commandLeave(String command) {
/* 145 */     if (mc.method_1562() == null) {
/* 146 */       ChatUtils.sendMessage("AutoLeave нельзя использовать в одиночной игре!");
/*     */       
/*     */       return;
/*     */     } 
/* 150 */     mc.method_1562().method_45730(command);
/* 151 */     this.cooldownTicks = this.leaveDisable.isState() ? 10 : 30;
/*     */     
/* 153 */     if (this.leaveDisable.isState())
/* 154 */       toggle(); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\misc\AutoLeave.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */