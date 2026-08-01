/*     */ package shame.astra.client.modules.impl.misc;
/*     */ import java.util.Set;
/*     */ import net.minecraft.class_124;
/*     */ import net.minecraft.class_1268;
/*     */ import net.minecraft.class_1304;
/*     */ import net.minecraft.class_1657;
/*     */ import net.minecraft.class_1713;
/*     */ import net.minecraft.class_1792;
/*     */ import net.minecraft.class_1799;
/*     */ import net.minecraft.class_1802;
/*     */ import net.minecraft.class_2596;
/*     */ import net.minecraft.class_2815;
/*     */ import net.minecraft.class_2848;
/*     */ import net.minecraft.class_2868;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.EventBinding;
/*     */ import shame.astra.api.events.implement.EventMoveInput;
/*     */ import shame.astra.api.utils.chat.ChatUtils;
/*     */ import shame.astra.api.utils.player.InventoryUtils;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.impl.movement.Sprint;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.BindSetting;
/*     */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*     */ 
/*     */ public class ElytraSwap extends Module {
/*  27 */   public static ElytraSwap INSTANCE = new ElytraSwap();
/*     */   
/*  29 */   private final BindSetting elytraBind = new BindSetting("Бинд элитры", -1);
/*  30 */   private final BindSetting fireworkBind = new BindSetting("Бинд фейерверка", -1);
/*  31 */   private final BooleanSetting autofly = new BooleanSetting("Авто-взлёт", true);
/*  32 */   private final BooleanSetting bypassgrim = new BooleanSetting("Обходить Grim", true);
/*  33 */   private final BooleanSetting bypassGround = new BooleanSetting("Обходить Граунд", true);
/*     */   
/*     */   private boolean swapElytraQueued;
/*     */   private boolean useFirework;
/*     */   private int bypassTicks;
/*     */   private boolean sprintPaused;
/*     */   private int swapCooldown;
/*  40 */   private int fireworkReturnSlot = -1;
/*  41 */   private int fireworkReturnTicks = -1;
/*     */   private boolean packetSwapActive;
/*     */   private int packetSwapStage;
/*     */   private int packetSwapSlot;
/*     */   
/*     */   public ElytraSwap() {
/*  47 */     super("ElytraSwap", "Автоматический свап элитр", Module.ModuleCategory.MISC);
/*  48 */     addSettings(new Setting[] { (Setting)this.elytraBind, (Setting)this.fireworkBind, (Setting)this.autofly, (Setting)this.bypassgrim, (Setting)this.bypassGround });
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onInput(EventMoveInput e) {
/*  53 */     if (this.bypassgrim.isState() && this.bypassTicks > 0) {
/*  54 */       if (mc.field_1724 == null)
/*  55 */         return;  mc.field_1724.method_5728(false);
/*  56 */       e.setForward(0.0F);
/*  57 */       e.setStrafe(0.0F);
/*  58 */       e.setJump(false);
/*  59 */       e.setSneak(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onEvent(EventUpdate ignored) {
/*  65 */     if (mc.field_1724 == null)
/*     */       return; 
/*  67 */     if (this.swapCooldown > 0) this.swapCooldown--; 
/*  68 */     handleFireworkReturn();
/*  69 */     handlePacketSwap();
/*     */     
/*  71 */     if (this.bypassTicks > 0) {
/*  72 */       mc.field_1724.method_5728(false);
/*  73 */       this.bypassTicks--;
/*  74 */       if (this.bypassTicks == 1) performSwap(); 
/*  75 */       if (this.bypassTicks == 0) restoreSprint();
/*     */       
/*     */       return;
/*     */     } 
/*  79 */     if (this.swapElytraQueued) {
/*  80 */       if (this.swapCooldown > 0) { this.swapElytraQueued = false; return; }
/*  81 */        if (this.bypassgrim.isState()) { disableSprint(); this.bypassTicks = 3; this.swapCooldown = 1; }
/*  82 */       else { performSwap(); this.swapCooldown = 1; }
/*  83 */        this.swapElytraQueued = false;
/*     */     } 
/*     */     
/*  86 */     if (this.useFirework) {
/*  87 */       int slotFirework = InventoryUtils.getItemSlot(class_1802.field_8639);
/*  88 */       if (mc.field_1724.method_6128()) {
/*  89 */         if (slotFirework != -1) {
/*  90 */           if (this.bypassGround.isState()) {
/*  91 */             executePacketFireworkSwap(slotFirework);
/*     */           } else {
/*  93 */             InventoryUtils.swapAndUseHvH(class_1802.field_8639);
/*     */           } 
/*     */         } else {
/*  96 */           ChatUtils.sendMessage(String.valueOf(class_124.field_1061) + String.valueOf(class_124.field_1061) + "Нет Фейерверков!");
/*     */         } 
/*     */       }
/*  99 */       this.useFirework = false;
/*     */     } 
/*     */     
/* 102 */     if (this.autofly.isState() && this.bypassTicks == 0) {
/* 103 */       class_1799 chestStack = mc.field_1724.method_6118(class_1304.field_6174);
/* 104 */       if (chestStack.method_31574(class_1802.field_8833) && !mc.field_1724.method_5799() && !mc.field_1724.method_5771() && mc.field_1724
/* 105 */         .method_24828() && !mc.field_1690.field_1903.method_1434()) {
/* 106 */         mc.field_1724.method_6043();
/* 107 */       } else if (chestStack.method_31574(class_1802.field_8833) && isElytraUsable(chestStack) && 
/* 108 */         !mc.field_1724.method_6128() && !mc.field_1724.method_24828()) {
/* 109 */         mc.field_1724.method_23669();
/* 110 */         mc.field_1724.field_3944.method_52787((class_2596)new class_2848((class_1297)mc.field_1724, class_2848.class_2849.field_12982));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handlePacketSwap() {
/* 116 */     if (!this.packetSwapActive || mc.field_1724 == null)
/*     */       return; 
/* 118 */     if (this.packetSwapStage == 0) {
/* 119 */       int currentSlot = (mc.field_1724.method_31548()).field_7545;
/* 120 */       int nextSlot = (currentSlot + 1) % 9;
/* 121 */       mc.field_1724.field_3944.method_52787((class_2596)new class_2868(nextSlot));
/* 122 */       this.packetSwapStage = 1;
/* 123 */     } else if (this.packetSwapStage == 1) {
/* 124 */       mc.field_1724.field_3944.method_52787((class_2596)new class_2868(this.packetSwapSlot));
/* 125 */       this.packetSwapActive = false;
/* 126 */       this.packetSwapStage = 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void executePacketFireworkSwap(int fireworkSlot) {
/* 131 */     int currentSlot = (mc.field_1724.method_31548()).field_7545;
/* 132 */     this.packetSwapSlot = currentSlot;
/*     */     
/* 134 */     if (fireworkSlot < 9) {
/* 135 */       mc.field_1724.field_3944.method_52787((class_2596)new class_2868(fireworkSlot));
/* 136 */       mc.field_1761.method_2919((class_1657)mc.field_1724, class_1268.field_5808);
/* 137 */       mc.field_1724.field_3944.method_52787((class_2596)new class_2868(currentSlot));
/*     */     } else {
/* 139 */       int targetSlot = (fireworkSlot >= 36) ? (fireworkSlot - 36) : fireworkSlot;
/* 140 */       mc.field_1761.method_2906(0, fireworkSlot, 0, class_1713.field_7791, (class_1657)mc.field_1724);
/* 141 */       mc.field_1761.method_2906(0, 36 + currentSlot, 0, class_1713.field_7791, (class_1657)mc.field_1724);
/* 142 */       mc.field_1761.method_2919((class_1657)mc.field_1724, class_1268.field_5808);
/* 143 */       mc.field_1761.method_2906(0, 36 + currentSlot, 0, class_1713.field_7791, (class_1657)mc.field_1724);
/* 144 */       mc.field_1761.method_2906(0, fireworkSlot, 0, class_1713.field_7791, (class_1657)mc.field_1724);
/* 145 */       mc.field_1724.field_3944.method_52787((class_2596)new class_2815(0));
/*     */     } 
/* 147 */     this.packetSwapActive = true;
/* 148 */     this.packetSwapStage = 0;
/*     */   }
/*     */   
/*     */   private void performSwap() {
/* 152 */     int slotElytra = InventoryUtils.findBestElytraSlot();
/* 153 */     int chestSlot = InventoryUtils.findBestChestplateSlot();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 159 */     boolean needChestplate = (mc.field_1724.method_6118(class_1304.field_6174).method_31574(class_1802.field_8833) || mc.field_1724.method_6118(class_1304.field_6174).method_7960() || !Set.<class_1792>of(class_1802.field_22028, class_1802.field_8058, class_1802.field_8523, class_1802.field_8678, class_1802.field_8873, class_1802.field_8577).contains(mc.field_1724.method_6118(class_1304.field_6174).method_7909()));
/*     */     
/* 161 */     if (needChestplate) {
/* 162 */       if (chestSlot == -1) { ChatUtils.sendMessage(String.valueOf(class_124.field_1061) + String.valueOf(class_124.field_1061) + "Нет нагрудника!"); this.bypassTicks = 0; restoreSprint(); return; }
/* 163 */        class_1799 chestItem = mc.field_1724.field_7498.method_7611(chestSlot).method_7677();
/* 164 */       doSwap(chestSlot);
/*     */     } else {
/* 166 */       if (slotElytra == -1) { ChatUtils.sendMessage(String.valueOf(class_124.field_1061) + String.valueOf(class_124.field_1061) + "Нет элитры!"); this.bypassTicks = 0; restoreSprint(); return; }
/* 167 */        class_1799 elytraItem = mc.field_1724.field_7498.method_7611(slotElytra).method_7677();
/* 168 */       doSwap(slotElytra);
/*     */     } 
/* 170 */     mc.field_1724.field_3944.method_52787((class_2596)new class_2815(0));
/*     */   }
/*     */   
/*     */   private void doSwap(int slot) {
/* 174 */     if (slot >= 0 && slot < 9) {
/* 175 */       mc.field_1761.method_2906(0, 6, slot, class_1713.field_7791, (class_1657)mc.field_1724);
/*     */     } else {
/* 177 */       mc.field_1761.method_2906(0, slot, 0, class_1713.field_7791, (class_1657)mc.field_1724);
/* 178 */       mc.field_1761.method_2906(0, 6, 0, class_1713.field_7791, (class_1657)mc.field_1724);
/* 179 */       mc.field_1761.method_2906(0, slot, 0, class_1713.field_7791, (class_1657)mc.field_1724);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleFireworkReturn() {
/* 184 */     if (this.fireworkReturnTicks < 0)
/* 185 */       return;  if (this.fireworkReturnTicks > 0) { this.fireworkReturnTicks--; return; }
/* 186 */      if (this.fireworkReturnSlot != -1) {
/* 187 */       swapSlotToOffhand(this.fireworkReturnSlot);
/* 188 */       mc.field_1724.field_3944.method_52787((class_2596)new class_2815(0));
/*     */     } 
/* 190 */     this.fireworkReturnSlot = -1;
/* 191 */     this.fireworkReturnTicks = -1;
/*     */   }
/*     */   
/*     */   private int findScreenSlot(class_1792 item) {
/* 195 */     for (int slot = 9; slot < 45; slot++) {
/* 196 */       class_1799 stack = mc.field_1724.field_7498.method_7611(slot).method_7677();
/* 197 */       if (stack.method_31574(item)) return slot; 
/*     */     } 
/* 199 */     return -1;
/*     */   }
/*     */   
/*     */   private void swapSlotToOffhand(int slot) {
/* 203 */     if (slot >= 36 && slot <= 44) {
/* 204 */       mc.field_1761.method_2906(0, 45, slot - 36, class_1713.field_7791, (class_1657)mc.field_1724);
/*     */       return;
/*     */     } 
/* 207 */     mc.field_1761.method_2906(0, slot, 0, class_1713.field_7791, (class_1657)mc.field_1724);
/* 208 */     mc.field_1761.method_2906(0, 45, 0, class_1713.field_7791, (class_1657)mc.field_1724);
/* 209 */     mc.field_1761.method_2906(0, slot, 0, class_1713.field_7791, (class_1657)mc.field_1724);
/*     */   }
/*     */   
/*     */   private void disableSprint() {
/* 213 */     if (this.sprintPaused)
/* 214 */       return;  Sprint.pushPause(1000L);
/* 215 */     this.sprintPaused = true;
/*     */   }
/*     */   
/*     */   private void restoreSprint() {
/* 219 */     if (!this.sprintPaused)
/* 220 */       return;  this.sprintPaused = false;
/* 221 */     Sprint.popPause();
/*     */   }
/*     */   
/*     */   private boolean isElytraUsable(class_1799 stack) {
/* 225 */     return (stack.method_7919() < stack.method_7936() - 1);
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onEvent(EventBinding event) {
/* 230 */     if (event.getKey() == this.elytraBind.getKey()) this.swapElytraQueued = true; 
/* 231 */     if (event.getKey() == this.fireworkBind.getKey()) this.useFirework = true;
/*     */   
/*     */   }
/*     */   
/*     */   public void onDisable() {
/* 236 */     this.bypassTicks = 0; this.swapCooldown = 0; this.fireworkReturnSlot = -1;
/* 237 */     this.fireworkReturnTicks = -1; this.packetSwapActive = false; this.packetSwapStage = 0;
/* 238 */     restoreSprint(); super.onDisable();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\misc\ElytraSwap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */