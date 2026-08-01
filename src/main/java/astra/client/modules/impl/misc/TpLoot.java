/*     */ package shame.astra.client.modules.impl.misc;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.class_1297;
/*     */ import net.minecraft.class_1542;
/*     */ import net.minecraft.class_1792;
/*     */ import net.minecraft.class_1799;
/*     */ import net.minecraft.class_1802;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_2596;
/*     */ import net.minecraft.class_2828;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.EventUpdate;
/*     */ import shame.astra.api.utils.chat.ChatUtils;
/*     */ import shame.astra.api.utils.math.TimerUtils;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*     */ import shame.astra.client.modules.settings.implement.ModeSetting;
/*     */ 
/*     */ public final class TpLoot
/*     */   extends Module {
/*  23 */   public static TpLoot INSTANCE = new TpLoot();
/*     */   
/*  25 */   private final FloatSetting range = new FloatSetting("Дистанция", 10.0F, 3.0F, 50.0F, 1.0F);
/*  26 */   private final FloatSetting lootDelay = new FloatSetting("Задержка лута", 500.0F, 100.0F, 5000.0F, 50.0F);
/*  27 */   private final ModeSetting afterLoot = new ModeSetting("После лута", "Возвращаться", new String[] { "Возвращаться", "Тепаться на спавн" });
/*  28 */   private final FloatSetting actionDelay = new FloatSetting("Задержка действия", 1000.0F, 200.0F, 10000.0F, 100.0F);
/*     */   
/*  30 */   private final TimerUtils lootTimer = new TimerUtils();
/*  31 */   private final TimerUtils actionTimer = new TimerUtils();
/*  32 */   private class_243 originalPos = null;
/*     */   
/*     */   private boolean waitingAction = false;
/*  35 */   private static final List<class_1792> TARGET_ITEMS = List.of(new class_1792[] { class_1802.field_22022, class_1802.field_22027, class_1802.field_22028, class_1802.field_22029, class_1802.field_22030, class_1802.field_8575, class_1802.field_8463, class_1802.field_8367, class_1802.field_8301, class_1802.field_8288, class_1802.field_8833 });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TpLoot() {
/*  50 */     super("TPLoot", "Телепортирует к ресурсам", Module.ModuleCategory.MISC);
/*  51 */     addSettings(new Setting[] { (Setting)this.range, (Setting)this.lootDelay, (Setting)this.afterLoot, (Setting)this.actionDelay });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @EventLink
/*     */   public void onTick(EventUpdate e) {
/*  58 */     if (mc.field_1724 == null || mc.field_1687 == null)
/*     */       return; 
/*  60 */     if (this.waitingAction) {
/*  61 */       if (this.actionTimer.finished((long)this.actionDelay.getValue().floatValue())) {
/*  62 */         if (this.afterLoot.is("Возвращаться") && this.originalPos != null) {
/*  63 */           teleportTo(this.originalPos);
/*  64 */           ChatUtils.sendMessage("TpLoot: возврат на исходную позицию");
/*     */         } 
/*  66 */         if (this.afterLoot.is("Тепаться на спавн")) {
/*  67 */           mc.field_1724.field_3944.method_45730("spawn");
/*  68 */           ChatUtils.sendMessage("TpLoot: выполнен /spawn");
/*     */         } 
/*  70 */         this.waitingAction = false;
/*  71 */         this.originalPos = null;
/*  72 */         this.lootTimer.reset();
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/*  77 */     if (!this.lootTimer.finished((long)this.lootDelay.getValue().floatValue()))
/*     */       return; 
/*  79 */     class_1542 targetItem = findTargetItem();
/*  80 */     if (targetItem == null)
/*     */       return; 
/*  82 */     this.originalPos = mc.field_1724.method_19538();
/*     */     
/*  84 */     class_243 itemPos = targetItem.method_19538();
/*  85 */     teleportTo(itemPos);
/*     */     
/*  87 */     class_1799 stack = targetItem.method_6983();
/*  88 */     ChatUtils.sendMessage("TpLoot: подобран " + stack.method_7964().getString());
/*     */     
/*  90 */     this.lootTimer.reset();
/*  91 */     this.waitingAction = true;
/*  92 */     this.actionTimer.reset();
/*     */   }
/*     */   
/*     */   private class_1542 findTargetItem() {
/*  96 */     double maxRange = this.range.getValue().doubleValue();
/*  97 */     class_1542 closest = null;
/*  98 */     double closestDist = Double.MAX_VALUE;
/*     */     
/* 100 */     for (class_1297 entity : mc.field_1687.method_18112()) {
/* 101 */       if (entity instanceof class_1542) { class_1542 itemEntity = (class_1542)entity;
/*     */         
/* 103 */         class_1799 stack = itemEntity.method_6983();
/* 104 */         if (!isTargetItem(stack.method_7909()))
/*     */           continue; 
/* 106 */         double dist = mc.field_1724.method_5858(entity);
/* 107 */         if (dist > maxRange * maxRange)
/*     */           continue; 
/* 109 */         if (dist < closestDist) {
/* 110 */           closestDist = dist;
/* 111 */           closest = itemEntity;
/*     */         }  }
/*     */     
/*     */     } 
/* 115 */     return closest;
/*     */   }
/*     */   
/*     */   private boolean isTargetItem(class_1792 item) {
/* 119 */     return TARGET_ITEMS.contains(item);
/*     */   }
/*     */ 
/*     */   
/*     */   private void teleportTo(class_243 pos) {
/* 124 */     int packets = (int)Math.ceil(mc.field_1724.method_19538().method_1022(pos) / 10.0D);
/* 125 */     packets = Math.max(packets, 3);
/*     */     
/* 127 */     for (int i = 0; i < packets; i++) {
/* 128 */       mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_5911(mc.field_1724.method_24828(), mc.field_1724.field_5976));
/*     */     }
/*     */     
/* 131 */     mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(pos.field_1352, pos.field_1351, pos.field_1350, false, mc.field_1724.field_5976));
/* 132 */     mc.field_1724.method_5814(pos.field_1352, pos.field_1351, pos.field_1350);
/*     */   }
/*     */   
/*     */   public void onEnable() {
/* 136 */     this.originalPos = null;
/* 137 */     this.waitingAction = false;
/* 138 */     this.lootTimer.reset();
/* 139 */     this.actionTimer.reset();
/* 140 */     super.onEnable();
/*     */   }
/*     */   
/*     */   public void onDisable() {
/* 144 */     this.originalPos = null;
/* 145 */     this.waitingAction = false;
/* 146 */     super.onDisable();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\misc\TpLoot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */