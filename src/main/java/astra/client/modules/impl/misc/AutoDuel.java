/*     */ package shame.astra.client.modules.impl.misc;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.regex.Pattern;
/*     */ import net.minecraft.class_1657;
/*     */ import net.minecraft.class_1713;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_2596;
/*     */ import net.minecraft.class_437;
/*     */ import net.minecraft.class_476;
/*     */ import net.minecraft.class_640;
/*     */ import net.minecraft.class_7439;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.EventPacket;
/*     */ import shame.astra.api.events.implement.EventUpdate;
/*     */ import shame.astra.api.utils.math.TimerUtils;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.ModeSetting;
/*     */ 
/*     */ public class AutoDuel extends Module {
/*  22 */   public static AutoDuel INSTANCE = new AutoDuel();
/*     */   
/*  24 */   public ModeSetting mode = new ModeSetting("Режим", "Шары", new String[] { "Щит", "Шипы", "Лук", "Тотемы", "Нодебафф", "Шары", "Классик", "Читер", "Незер" });
/*     */ 
/*     */   
/*  27 */   private static final Pattern NAME_PATTERN = Pattern.compile("^\\w{3,16}$");
/*     */   
/*  29 */   private final List<String> sent = new ArrayList<>();
/*  30 */   private final TimerUtils duelT = new TimerUtils();
/*  31 */   private final TimerUtils clrT = new TimerUtils();
/*  32 */   private final TimerUtils pickT = new TimerUtils();
/*  33 */   private final TimerUtils setT = new TimerUtils();
/*     */   
/*     */   private class_243 lastPos;
/*     */   private boolean inDuel;
/*     */   
/*     */   public AutoDuel() {
/*  39 */     super("AutoDuel", "Автоматически кидает дуель", Module.ModuleCategory.MISC);
/*  40 */     addSettings(new Setting[] { (Setting)this.mode });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  45 */     super.onEnable();
/*  46 */     this.sent.clear();
/*  47 */     this.inDuel = false;
/*  48 */     if (mc.field_1724 != null) {
/*  49 */       this.lastPos = mc.field_1724.method_19538();
/*     */     }
/*  51 */     this.duelT.reset();
/*  52 */     this.clrT.reset();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  57 */     super.onDisable();
/*  58 */     this.sent.clear();
/*  59 */     this.inDuel = false;
/*     */   }
/*     */ 
/*     */   
/*     */   @EventLink
/*     */   public void onUpdate(EventUpdate e) {
/*  65 */     if (mc.field_1724 == null || mc.field_1687 == null || this.inDuel)
/*     */       return; 
/*  67 */     if (this.lastPos != null && mc.field_1724.method_19538().method_1022(this.lastPos) > 500.0D) {
/*  68 */       toggle();
/*     */       return;
/*     */     } 
/*  71 */     this.lastPos = mc.field_1724.method_19538();
/*     */     
/*  73 */     if (this.clrT.getElapsedTime() >= 30000L) {
/*  74 */       this.sent.clear();
/*  75 */       this.clrT.reset();
/*     */     } 
/*     */     
/*  78 */     if (this.duelT.getElapsedTime() >= 1000L) {
/*  79 */       sendDuel();
/*  80 */       this.duelT.reset();
/*     */     } 
/*     */     
/*  83 */     handleGui();
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onPacket(EventPacket e) {
/*  88 */     if (mc.field_1724 == null || mc.field_1687 == null)
/*     */       return; 
/*  90 */     if (e.getType() == EventPacket.Type.RECEIVE) { class_2596 class_2596 = e.getPacket(); if (class_2596 instanceof class_7439) { class_7439 p = (class_7439)class_2596;
/*  91 */         String msg = p.comp_763().getString().toLowerCase();
/*  92 */         if ((msg.contains("начало") && msg.contains("через") && msg.contains("секунд")) || msg
/*  93 */           .contains("поединок начался") || msg
/*  94 */           .contains("во время поединка")) {
/*  95 */           this.inDuel = true;
/*  96 */           toggle();
/*     */         }  }
/*     */        }
/*     */   
/*     */   }
/*     */   
/*     */   private void sendDuel() {
/* 103 */     for (String p : getPlayers()) {
/* 104 */       if (!this.sent.contains(p) && !p.equals(mc.field_1724.method_5477().getString())) {
/* 105 */         mc.method_1562().method_45730("duel " + p);
/* 106 */         this.sent.add(p);
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private void handleGui() {
/*     */     class_476 s;
/* 113 */     class_437 class_437 = mc.field_1755; if (class_437 instanceof class_476) { s = (class_476)class_437; } else { return; }
/* 114 */      int id = ((class_1707)s.method_17577()).field_7763;
/* 115 */     String t = s.method_25440().getString();
/*     */     
/* 117 */     if (t.contains("Выбор набора") && this.pickT.getElapsedTime() >= 150L) {
/* 118 */       mc.field_1761.method_2906(id, getModeSlot(), 0, class_1713.field_7794, (class_1657)mc.field_1724);
/* 119 */       this.pickT.reset();
/* 120 */     } else if (t.contains("Настройка поединка") && this.setT.getElapsedTime() >= 150L) {
/* 121 */       mc.field_1761.method_2906(id, 0, 0, class_1713.field_7794, (class_1657)mc.field_1724);
/* 122 */       this.setT.reset();
/*     */     } 
/*     */   }
/*     */   
/*     */   private int getModeSlot() {
/* 127 */     if (this.mode.is("Щит")) return 0; 
/* 128 */     if (this.mode.is("Шипы")) return 1; 
/* 129 */     if (this.mode.is("Лук")) return 2; 
/* 130 */     if (this.mode.is("Тотемы")) return 3; 
/* 131 */     if (this.mode.is("Нодебафф")) return 4; 
/* 132 */     if (this.mode.is("Шары")) return 5; 
/* 133 */     if (this.mode.is("Классик")) return 6; 
/* 134 */     if (this.mode.is("Читер")) return 7; 
/* 135 */     if (this.mode.is("Незер")) return 8; 
/* 136 */     return 5;
/*     */   }
/*     */   
/*     */   private List<String> getPlayers() {
/* 140 */     List<String> list = new ArrayList<>();
/* 141 */     if (mc.method_1562() == null) return list; 
/* 142 */     for (class_640 e : mc.method_1562().method_2880()) {
/* 143 */       String n = e.method_2966().getName();
/* 144 */       if (NAME_PATTERN.matcher(n).matches()) {
/* 145 */         list.add(n);
/*     */       }
/*     */     } 
/* 148 */     return list;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\misc\AutoDuel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */