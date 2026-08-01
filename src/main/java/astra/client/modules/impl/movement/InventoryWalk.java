/*     */ package shame.astra.client.modules.impl.movement;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.class_2596;
/*     */ import net.minecraft.class_2813;
/*     */ import net.minecraft.class_2815;
/*     */ import net.minecraft.class_304;
/*     */ import net.minecraft.class_3675;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.EventCloseInv;
/*     */ import shame.astra.api.events.implement.EventPacket;
/*     */ import shame.astra.api.events.implement.EventUpdate;
/*     */ import shame.astra.api.storages.implement.helpertstorages.enumvar.ModuleClass;
/*     */ import shame.astra.api.utils.player.MoveUtils;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.ModeSetting;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InventoryWalk
/*     */   extends Module
/*     */ {
/*  25 */   public static InventoryWalk INSTANCE = new InventoryWalk();
/*     */   
/*  27 */   public ModeSetting mode = new ModeSetting("Обход", "Обычный", new String[] { "Обычный", "Grim" });
/*  28 */   public ModeSetting grimVersion = (new ModeSetting("Версия свапа", "1.21.4", new String[] { "1.21.4", "1.16.5"
/*  29 */       })).visible(() -> Boolean.valueOf(this.mode.is("Grim")));
/*     */   
/*  31 */   public int tick = 0;
/*  32 */   private final List<class_2813> pendingPackets = new ArrayList<>();
/*  33 */   private class_2815 pendingClosePacket = null;
/*     */   private boolean sprintPaused = false;
/*     */   private boolean waitingToClose = false;
/*  36 */   private int delayedFlushTicks = -1;
/*     */   private boolean flushingPackets = false;
/*     */   
/*     */   public InventoryWalk() {
/*  40 */     super("InventoryWalk", "Ходьба с открытым инвентарём", Module.ModuleCategory.MOVEMENT);
/*  41 */     addSettings(new Setting[] { (Setting)this.mode, (Setting)this.grimVersion });
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onUpdate(EventUpdate event) {
/*  46 */     if (mc.field_1724 == null)
/*     */       return; 
/*  48 */     class_304[] pressedKeys = { mc.field_1690.field_1894, mc.field_1690.field_1881, mc.field_1690.field_1913, mc.field_1690.field_1849, mc.field_1690.field_1903, mc.field_1690.field_1867 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  57 */     if (this.mode.is("Grim") && this.grimVersion.is("1.21.4") && this.waitingToClose && !MoveUtils.isMoving()) {
/*  58 */       flushQueuedPackets(true);
/*  59 */       this.waitingToClose = false;
/*  60 */       this.tick = 3;
/*     */     } 
/*     */     
/*  63 */     if (this.mode.is("Grim") && this.grimVersion.is("1.16.5") && this.delayedFlushTicks >= 0) {
/*  64 */       if (this.delayedFlushTicks == 0) {
/*  65 */         flushQueuedPackets(true);
/*  66 */         this.delayedFlushTicks = -1;
/*  67 */         this.tick = 1;
/*     */       } else {
/*  69 */         this.delayedFlushTicks--;
/*     */       } 
/*     */     }
/*     */     
/*  73 */     if (this.tick == 0 && !this.pendingPackets.isEmpty() && mc.field_1755 == null && !this.waitingToClose) {
/*  74 */       sendPendingPackets();
/*     */     }
/*     */     
/*  77 */     if (this.tick != 0) {
/*  78 */       for (class_304 keyBinding : pressedKeys) {
/*  79 */         keyBinding.method_23481(false);
/*     */       }
/*  81 */       this.tick--;
/*     */       
/*  83 */       if (this.tick == 0 && this.sprintPaused) {
/*  84 */         this.sprintPaused = false;
/*  85 */         Sprint.popPause();
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/*  90 */     if (mc.field_1755 instanceof net.minecraft.class_408 || mc.field_1755 instanceof net.minecraft.class_498) {
/*     */       return;
/*     */     }
/*     */     
/*  94 */     if (this.mode.is("Grim") && mc.field_1755 instanceof net.minecraft.class_465 && !(mc.field_1755 instanceof net.minecraft.class_490)) {
/*     */       return;
/*     */     }
/*     */     
/*  98 */     if (this.waitingToClose) {
/*  99 */       for (class_304 keyBinding : pressedKeys) {
/* 100 */         keyBinding.method_23481(false);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 105 */     for (class_304 keyBinding : pressedKeys) {
/* 106 */       boolean isKeyPressed = class_3675.method_15987(mc.method_22683().method_4490(), keyBinding.method_1429().method_1444());
/* 107 */       keyBinding.method_23481(isKeyPressed);
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onPacket(EventPacket event) {
/* 113 */     if (event.getType() != EventPacket.Type.SEND || this.flushingPackets) {
/*     */       return;
/*     */     }
/*     */     
/* 117 */     Object packet = event.getPacket();
/* 118 */     if (!this.mode.is("Grim") || !MoveUtils.isMoving() || !(mc.field_1755 instanceof net.minecraft.class_490)) {
/*     */       return;
/*     */     }
/*     */     
/* 122 */     if (packet instanceof class_2813) { class_2813 clickPacket = (class_2813)packet;
/* 123 */       this.pendingPackets.add(clickPacket);
/* 124 */       event.cancel();
/*     */       
/*     */       return; }
/*     */     
/* 128 */     if (packet instanceof class_2815) { class_2815 closePacket = (class_2815)packet;
/* 129 */       this.pendingClosePacket = closePacket;
/* 130 */       if (this.grimVersion.is("1.16.5")) {
/* 131 */         this.delayedFlushTicks = 1;
/* 132 */         this.waitingToClose = false;
/*     */       } else {
/* 134 */         this.waitingToClose = true;
/*     */       } 
/* 136 */       pauseSprint();
/* 137 */       event.cancel(); }
/*     */   
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onCloseInv(EventCloseInv eventCloseInv) {
/* 143 */     if (this.mode.is("Grim") && this.grimVersion.is("1.16.5") && MoveUtils.isMoving() && mc.field_1755 instanceof net.minecraft.class_490) {
/* 144 */       this.pendingClosePacket = new class_2815(eventCloseInv.windowId);
/* 145 */       this.delayedFlushTicks = 1;
/* 146 */       pauseSprint();
/* 147 */       this.tick = 1;
/* 148 */       eventCloseInv.cancel();
/*     */       
/*     */       return;
/*     */     } 
/* 152 */     if (this.mode.is("Grim") && !this.waitingToClose) {
/* 153 */       pauseSprint();
/* 154 */       this.tick = 1;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void pauseSprint() {
/* 159 */     if (this.sprintPaused) {
/*     */       return;
/*     */     }
/*     */     
/* 163 */     Sprint.pushPause(0L);
/* 164 */     this.sprintPaused = true;
/*     */   }
/*     */   
/*     */   private void sendPendingPackets() {
/* 168 */     if (mc.field_1724 == null || mc.method_1562() == null) {
/* 169 */       this.pendingPackets.clear();
/*     */       
/*     */       return;
/*     */     } 
/* 173 */     this.flushingPackets = true;
/*     */     try {
/* 175 */       for (class_2813 packet : this.pendingPackets) {
/* 176 */         mc.method_1562().method_52787((class_2596)packet);
/*     */       }
/*     */     } finally {
/* 179 */       this.flushingPackets = false;
/*     */     } 
/* 181 */     this.pendingPackets.clear();
/*     */   }
/*     */   
/*     */   private void flushQueuedPackets(boolean includeClose) {
/* 185 */     if (mc.field_1724 == null || mc.method_1562() == null) {
/* 186 */       this.pendingPackets.clear();
/* 187 */       this.pendingClosePacket = null;
/*     */       
/*     */       return;
/*     */     } 
/* 191 */     sendPendingPackets();
/*     */     
/* 193 */     if (includeClose && this.pendingClosePacket != null) {
/* 194 */       this.flushingPackets = true;
/*     */       try {
/* 196 */         mc.method_1562().method_52787((class_2596)this.pendingClosePacket);
/*     */       } finally {
/* 198 */         this.flushingPackets = false;
/*     */       } 
/* 200 */       this.pendingClosePacket = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void stopTick(int ticks) {
/* 205 */     InventoryWalk inventoryWalk = ModuleClass.inventoryWalk;
/* 206 */     if (inventoryWalk != null && inventoryWalk.isEnable()) {
/* 207 */       inventoryWalk.tick = Math.max(inventoryWalk.tick, ticks);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 213 */     super.onDisable();
/* 214 */     flushQueuedPackets(true);
/* 215 */     if (this.sprintPaused) {
/* 216 */       this.sprintPaused = false;
/* 217 */       Sprint.popPause();
/*     */     } 
/* 219 */     this.waitingToClose = false;
/* 220 */     this.delayedFlushTicks = -1;
/* 221 */     this.flushingPackets = false;
/* 222 */     this.tick = 0;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\movement\InventoryWalk.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */