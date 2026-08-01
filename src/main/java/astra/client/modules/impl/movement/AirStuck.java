/*     */ package shame.astra.client.modules.impl.movement;
/*     */ import net.minecraft.class_1304;
/*     */ import net.minecraft.class_1657;
/*     */ import net.minecraft.class_1713;
/*     */ import net.minecraft.class_1799;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_2596;
/*     */ import net.minecraft.class_2815;
/*     */ import net.minecraft.class_2828;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.EventMove;
/*     */ import shame.astra.api.events.implement.EventPacket;
/*     */ import shame.astra.api.utils.network.NetworkUtils;
/*     */ import shame.astra.api.utils.player.InventoryUtils;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*     */ import shame.astra.client.modules.settings.implement.ModeSetting;
/*     */ 
/*     */ public class AirStuck extends Module {
/*  21 */   public static AirStuck INSTANCE = new AirStuck();
/*     */   
/*  23 */   private final ModeSetting mode = new ModeSetting("Мод", "Обычный", new String[] { "Обычный", "LonyGrief" });
/*  24 */   private final BooleanSetting cancelPackets = new BooleanSetting("Отменять пакеты", true);
/*  25 */   private final BooleanSetting swapElytra = new BooleanSetting("Свапать элитру", true);
/*     */   
/*  27 */   private class_243 freezePosition = class_243.field_1353;
/*     */   private boolean frozen = false;
/*     */   
/*     */   public AirStuck() {
/*  31 */     super("AirStuck", "Зависает в воздухе", Module.ModuleCategory.MOVEMENT);
/*  32 */     addSettings(new Setting[] { (Setting)this.mode, (Setting)this.cancelPackets, (Setting)this.swapElytra });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  37 */     this.frozen = false;
/*     */     
/*  39 */     if (mc.field_1724 != null && this.swapElytra.isState()) {
/*  40 */       swapChestEquipment();
/*     */     }
/*     */     
/*  43 */     if (mc.field_1724 != null && this.mode.is("Обычный")) {
/*  44 */       this.freezePosition = mc.field_1724.method_19538();
/*  45 */       this.frozen = true;
/*     */     } 
/*     */     
/*  48 */     super.onEnable();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  53 */     this.frozen = false;
/*  54 */     super.onDisable();
/*     */   }
/*     */   
/*     */   private void swapChestEquipment() {
/*  58 */     class_1799 chestStack = mc.field_1724.method_6118(class_1304.field_6174);
/*     */     
/*  60 */     if (!chestStack.method_31574(class_1802.field_8833)) {
/*     */       return;
/*     */     }
/*     */     
/*  64 */     int chestplateSlot = InventoryUtils.findBestChestplateSlot();
/*  65 */     if (chestplateSlot != -1) {
/*  66 */       doSwap(chestplateSlot);
/*     */     }
/*     */   }
/*     */   
/*     */   private void doSwap(int slot) {
/*  71 */     if (slot >= 0 && slot < 9) {
/*  72 */       mc.field_1761.method_2906(0, 6, slot, class_1713.field_7791, (class_1657)mc.field_1724);
/*     */     } else {
/*  74 */       mc.field_1761.method_2906(0, slot, 0, class_1713.field_7791, (class_1657)mc.field_1724);
/*  75 */       mc.field_1761.method_2906(0, 6, 0, class_1713.field_7791, (class_1657)mc.field_1724);
/*  76 */       mc.field_1761.method_2906(0, slot, 0, class_1713.field_7791, (class_1657)mc.field_1724);
/*     */     } 
/*     */     
/*  79 */     mc.field_1724.field_3944.method_52787((class_2596)new class_2815(0));
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onMove(EventMove e) {
/*  84 */     if (mc.field_1724 == null)
/*     */       return; 
/*  86 */     if (this.mode.is("LonyGrief") && !this.frozen && 
/*  87 */       mc.field_1724.field_6017 > 0.0F && (mc.field_1724.method_18798()).field_1351 < 0.0D) {
/*  88 */       this.freezePosition = mc.field_1724.method_19538();
/*  89 */       this.frozen = true;
/*     */     } 
/*     */ 
/*     */     
/*  93 */     if (this.frozen) {
/*  94 */       e.setMovePos(class_243.field_1353);
/*  95 */       mc.field_1724.method_5814(this.freezePosition.field_1352, this.freezePosition.field_1351, this.freezePosition.field_1350);
/*  96 */       mc.field_1724.method_18800(0.0D, 0.0D, 0.0D);
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onPacket(EventPacket e) {
/* 102 */     if (!this.frozen || e.getType() != EventPacket.Type.SEND)
/*     */       return; 
/* 104 */     class_2596 class_2596 = e.getPacket(); if (class_2596 instanceof class_2828) { class_2828 packet = (class_2828)class_2596;
/* 105 */       if (this.cancelPackets.isState()) {
/* 106 */         e.cancel();
/*     */       } else {
/* 108 */         e.cancel();
/* 109 */         NetworkUtils.sendSilentPacket((class_2596)createFrozenPacket(packet));
/*     */       }  }
/*     */   
/*     */   }
/*     */   
/*     */   private class_2828 createFrozenPacket(class_2828 packet) {
/* 115 */     boolean onGround = packet.method_12273();
/* 116 */     boolean horizontalCollision = packet.method_61225();
/*     */     
/* 118 */     if (packet.method_36171() && packet.method_36172()) {
/* 119 */       return (class_2828)new class_2828.class_2830(this.freezePosition.field_1352, this.freezePosition.field_1351, this.freezePosition.field_1350, packet
/*     */ 
/*     */ 
/*     */           
/* 123 */           .method_12271(mc.field_1724.method_36454()), packet
/* 124 */           .method_12270(mc.field_1724.method_36455()), onGround, horizontalCollision);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 130 */     if (packet.method_36171()) {
/* 131 */       return (class_2828)new class_2828.class_2829(this.freezePosition.field_1352, this.freezePosition.field_1351, this.freezePosition.field_1350, onGround, horizontalCollision);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 140 */     if (packet.method_36172()) {
/* 141 */       return (class_2828)new class_2828.class_2831(packet
/* 142 */           .method_12271(mc.field_1724.method_36454()), packet
/* 143 */           .method_12270(mc.field_1724.method_36455()), onGround, horizontalCollision);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 149 */     return (class_2828)new class_2828.class_5911(onGround, horizontalCollision);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\movement\AirStuck.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */