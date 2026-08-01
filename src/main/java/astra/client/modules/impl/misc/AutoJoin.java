/*     */ package shame.astra.client.modules.impl.misc;
/*     */ import net.minecraft.class_1268;
/*     */ import net.minecraft.class_1657;
/*     */ import net.minecraft.class_1703;
/*     */ import net.minecraft.class_1713;
/*     */ import net.minecraft.class_1735;
/*     */ import net.minecraft.class_2596;
/*     */ import net.minecraft.class_2868;
/*     */ import net.minecraft.class_437;
/*     */ import net.minecraft.class_476;
/*     */ import net.minecraft.class_7439;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.EventPacket;
/*     */ import shame.astra.api.events.implement.EventUpdate;
/*     */ import shame.astra.api.utils.chat.ChatUtils;
/*     */ import shame.astra.api.utils.math.TimerUtils;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*     */ 
/*     */ public final class AutoJoin extends Module {
/*  22 */   public static AutoJoin INSTANCE = new AutoJoin();
/*     */   
/*     */   private static final long CLICK_DELAY_MS = 30L;
/*     */   private static final int NEXT_PAGE_SLOT = 44;
/*     */   private static final int MAX_PAGE_SWITCHES = 5;
/*  27 */   private final FloatSetting grief = new FloatSetting("Гриф", 5.0F, 1.0F, 64.0F, 1.0F);
/*     */   
/*  29 */   private final TimerUtils clickTimer = new TimerUtils();
/*  30 */   private final TimerUtils compassTimer = new TimerUtils();
/*     */   
/*     */   private boolean joining;
/*     */   private int pageSwitches;
/*     */   private int targetGrief;
/*     */   
/*     */   public AutoJoin() {
/*  37 */     super("AutoJoin", "Автоматически заходит на выбранный гриф", Module.ModuleCategory.MISC);
/*  38 */     addSettings(new Setting[] { (Setting)this.grief });
/*     */   }
/*     */   
/*     */   public void startJoinTo(int griefId) {
/*  42 */     this.grief.setValue(griefId);
/*  43 */     if (!isEnable()) {
/*  44 */       toggle();
/*     */       return;
/*     */     } 
/*  47 */     startJoin();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  52 */     super.onEnable();
/*  53 */     startJoin();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  58 */     this.joining = false;
/*  59 */     this.pageSwitches = 0;
/*  60 */     super.onDisable();
/*     */   }
/*     */ 
/*     */   
/*     */   @EventLink
/*     */   public void onUpdate(EventUpdate event) {
/*  66 */     if (!this.joining || mc.field_1724 == null || mc.field_1687 == null || mc.field_1761 == null)
/*     */       return; 
/*  68 */     if (!(mc.field_1755 instanceof class_476)) {
/*  69 */       openServerSelector(false);
/*     */       
/*     */       return;
/*     */     } 
/*  73 */     handleServerMenu();
/*     */   }
/*     */   @EventLink
/*     */   public void onPacket(EventPacket event) {
/*     */     class_7439 packet;
/*  78 */     if (!this.joining || mc.field_1724 == null || mc.field_1687 == null || event.getType() != EventPacket.Type.RECEIVE)
/*     */       return; 
/*  80 */     if (event.getPacket() instanceof net.minecraft.class_2678) {
/*  81 */       ChatUtils.sendMessage("Вход на гриф #" + this.targetGrief + ": успешно");
/*  82 */       this.joining = false;
/*  83 */       this.pageSwitches = 0;
/*     */       
/*     */       return;
/*     */     } 
/*  87 */     class_2596 class_2596 = event.getPacket(); if (class_2596 instanceof class_7439) { packet = (class_7439)class_2596; }
/*     */     else { return; }
/*  89 */      String message = packet.comp_763().getString();
/*  90 */     if (message.contains("Подождите несколько секунд перед повторным подключением")) {
/*  91 */       event.cancel();
/*     */       
/*     */       return;
/*     */     } 
/*  95 */     if (message.contains("К сожалению сервер переполнен")) {
/*  96 */       event.cancel();
/*  97 */       ChatUtils.sendMessage("Вход на гриф #" + this.targetGrief + ": неудачно");
/*     */       
/*     */       return;
/*     */     } 
/* 101 */     openServerSelector(false);
/*     */   }
/*     */   
/*     */   private void startJoin() {
/* 105 */     this.joining = true;
/* 106 */     this.pageSwitches = 0;
/* 107 */     this.targetGrief = Math.round(this.grief.get());
/* 108 */     this.clickTimer.reset();
/* 109 */     this.compassTimer.reset();
/*     */     
/* 111 */     if (mc.field_1724 != null && mc.field_1687 != null) {
/* 112 */       openServerSelector(true);
/*     */     }
/*     */   }
/*     */   
/*     */   private void openServerSelector(boolean force) {
/* 117 */     if (!force && !this.compassTimer.finished(30L))
/*     */       return; 
/* 119 */     if (mc.field_1724 == null || mc.field_1761 == null || mc.method_1562() == null)
/*     */       return; 
/* 121 */     int previousSlot = (mc.field_1724.method_31548()).field_7545;
/* 122 */     int slot = findCompassSlot();
/* 123 */     if (slot == -1) {
/*     */       return;
/*     */     }
/*     */     
/* 127 */     this.pageSwitches = 0;
/* 128 */     (mc.field_1724.method_31548()).field_7545 = slot;
/* 129 */     mc.method_1562().method_52787((class_2596)new class_2868(slot));
/* 130 */     mc.field_1761.method_2919((class_1657)mc.field_1724, class_1268.field_5808);
/* 131 */     (mc.field_1724.method_31548()).field_7545 = previousSlot;
/* 132 */     mc.method_1562().method_52787((class_2596)new class_2868(previousSlot));
/* 133 */     this.compassTimer.reset();
/*     */   }
/*     */   
/*     */   private int findCompassSlot() {
/* 137 */     if (mc.field_1724 == null) return -1;
/*     */     
/* 139 */     for (int i = 0; i < 9; i++) {
/* 140 */       if (mc.field_1724.method_31548().method_5438(i).method_7909() == class_1802.field_8251) {
/* 141 */         return i;
/*     */       }
/*     */     } 
/*     */     
/* 145 */     return -1;
/*     */   }
/*     */   private void handleServerMenu() {
/*     */     class_476 screen;
/* 149 */     class_437 class_437 = mc.field_1755; if (class_437 instanceof class_476) { screen = (class_476)class_437; } else { return; }
/* 150 */      if (!this.clickTimer.finished(30L))
/*     */       return; 
/* 152 */     String title = screen.method_25440().getString();
/* 153 */     class_1703 handler = screen.method_17577();
/*     */     
/* 155 */     if (title.contains("Выбор сервера")) {
/* 156 */       clickSlot(handler, 21);
/* 157 */       this.pageSwitches = 0;
/* 158 */       this.clickTimer.reset();
/*     */       
/*     */       return;
/*     */     } 
/* 162 */     if (clickTargetGriefIfVisible(handler)) {
/*     */       return;
/*     */     }
/*     */     
/* 166 */     if (this.targetGrief > 36 && this.pageSwitches < 5) {
/* 167 */       class_1735 nextPageSlot = getSlot(handler, 44);
/* 168 */       if (nextPageSlot != null && nextPageSlot.method_7681()) {
/* 169 */         clickSlot(handler, 44);
/* 170 */         this.pageSwitches++;
/* 171 */         this.clickTimer.reset();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean clickTargetGriefIfVisible(class_1703 handler) {
/* 177 */     String targetName = "ГРИФ #" + this.targetGrief + " (1.16.5+)";
/* 178 */     String targetPrefix = "ГРИФ #" + this.targetGrief;
/*     */     
/* 180 */     for (int slot = 0; slot < handler.field_7761.size(); slot++) {
/* 181 */       class_1735 containerSlot = handler.method_7611(slot);
/* 182 */       if (containerSlot != null && containerSlot.method_7681()) {
/*     */         
/* 184 */         String itemName = containerSlot.method_7677().method_7964().getString();
/* 185 */         if (itemName.equalsIgnoreCase(targetName) || itemName.toUpperCase().contains(targetPrefix)) {
/* 186 */           clickSlot(handler, slot);
/* 187 */           this.pageSwitches = 0;
/* 188 */           this.clickTimer.reset();
/* 189 */           return true;
/*     */         } 
/*     */       } 
/*     */     } 
/* 193 */     return false;
/*     */   }
/*     */   
/*     */   private void clickSlot(class_1703 handler, int slot) {
/* 197 */     if (mc.field_1724 == null || mc.field_1761 == null)
/* 198 */       return;  if (slot < 0 || slot >= handler.field_7761.size())
/*     */       return; 
/* 200 */     mc.field_1761.method_2906(handler.field_7763, slot, 0, class_1713.field_7790, (class_1657)mc.field_1724);
/*     */   }
/*     */   
/*     */   private class_1735 getSlot(class_1703 handler, int slot) {
/* 204 */     if (slot < 0 || slot >= handler.field_7761.size()) return null; 
/* 205 */     return handler.method_7611(slot);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\misc\AutoJoin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */