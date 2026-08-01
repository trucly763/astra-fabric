/*     */ package shame.astra.client.modules.impl.player;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.ThreadLocalRandom;
/*     */ import net.minecraft.class_1263;
/*     */ import net.minecraft.class_1657;
/*     */ import net.minecraft.class_1703;
/*     */ import net.minecraft.class_1707;
/*     */ import net.minecraft.class_1713;
/*     */ import net.minecraft.class_1735;
/*     */ import net.minecraft.class_2371;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.EventUpdate;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*     */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*     */ 
/*     */ public class ChestStealer extends Module {
/*  22 */   public static ChestStealer INSTANCE = new ChestStealer();
/*     */   
/*  24 */   private final FloatSetting stealDelay = new FloatSetting("Задержка", 100.0F, 0.0F, 1000.0F, 1.0F);
/*  25 */   private final BooleanSetting randomize = new BooleanSetting("Рандомизация", false);
/*     */   
/*  27 */   private long lastStealTime = 0L;
/*     */   
/*     */   public ChestStealer() {
/*  30 */     super("ChestStealer", "Автоматически открывает сундуки и забирает из них предметы", Module.ModuleCategory.PLAYER);
/*  31 */     addSettings(new Setting[] { (Setting)this.stealDelay, (Setting)this.randomize });
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   private void onUpdate(EventUpdate event) {
/*  36 */     if (mc.field_1724 == null || mc.field_1761 == null) {
/*     */       return;
/*     */     }
/*     */     
/*  40 */     class_1703 openContainer = mc.field_1724.field_7512;
/*     */     
/*  42 */     if (openContainer == null || openContainer == mc.field_1724.field_7498) {
/*     */       return;
/*     */     }
/*     */     
/*  46 */     if (!(openContainer instanceof class_1707) && !(openContainer instanceof net.minecraft.class_1722)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  51 */     long currentTime = System.currentTimeMillis();
/*  52 */     long delay = (long)this.stealDelay.get();
/*     */     
/*  54 */     if (currentTime - this.lastStealTime < delay) {
/*     */       return;
/*     */     }
/*     */     
/*  58 */     class_2371 class_2371 = openContainer.field_7761;
/*  59 */     findValidItem((List<class_1735>)class_2371, openContainer).ifPresent(slot -> {
/*     */           if (mc.field_1724.field_7512 == openContainer) {
/*     */             mc.field_1761.method_2906(openContainer.field_7763, slot.field_7874, 0, class_1713.field_7794, (class_1657)mc.field_1724);
/*     */             this.lastStealTime = currentTime;
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Optional<class_1735> findValidItem(List<class_1735> slots, class_1703 handler) {
/*  74 */     int containerSlotCount = getContainerSlotCount(handler);
/*     */     
/*  76 */     if (containerSlotCount <= 0 || containerSlotCount > slots.size()) {
/*  77 */       return Optional.empty();
/*     */     }
/*     */     
/*  80 */     List<class_1735> containerSlots = slots.subList(0, containerSlotCount);
/*  81 */     List<class_1735> validSlots = new ArrayList<>();
/*     */     
/*  83 */     for (class_1735 slot : containerSlots) {
/*  84 */       if (slot.method_7681() && !slot.method_7677().method_7960() && 
/*  85 */         !mc.field_1724.method_7357().method_7904(slot.method_7677())) {
/*  86 */         validSlots.add(slot);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  91 */     if (validSlots.isEmpty()) {
/*  92 */       return Optional.empty();
/*     */     }
/*     */     
/*  95 */     if (this.randomize.isState()) {
/*  96 */       int randomIndex = ThreadLocalRandom.current().nextInt(validSlots.size());
/*  97 */       return Optional.of(validSlots.get(randomIndex));
/*     */     } 
/*  99 */     return Optional.of(validSlots.get(0));
/*     */   }
/*     */ 
/*     */   
/*     */   private int getContainerSlotCount(class_1703 handler) {
/* 104 */     if (handler instanceof class_1707) { class_1707 container = (class_1707)handler;
/* 105 */       class_1263 inventory = container.method_7629();
/* 106 */       return inventory.method_5439(); }
/* 107 */      if (handler instanceof net.minecraft.class_1722) {
/* 108 */       return 5;
/*     */     }
/* 110 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 115 */     this.lastStealTime = 0L;
/* 116 */     super.onDisable();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\player\ChestStealer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */