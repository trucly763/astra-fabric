/*     */ package shame.astra.client.modules.impl.player;
/*     */ import net.minecraft.class_1268;
/*     */ import net.minecraft.class_1657;
/*     */ import net.minecraft.class_1799;
/*     */ import net.minecraft.class_1890;
/*     */ import net.minecraft.class_2596;
/*     */ import net.minecraft.class_2767;
/*     */ import net.minecraft.class_2868;
/*     */ import net.minecraft.class_3417;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.EventPacket;
/*     */ import shame.astra.api.events.implement.EventUpdate;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*     */ 
/*     */ public class AutoFish extends Module {
/*  18 */   public static AutoFish INSTANCE = new AutoFish();
/*     */   
/*  20 */   private final BooleanSetting takeRod = new BooleanSetting("Автоматически брать удочку", true);
/*     */   
/*     */   private boolean isCached = false;
/*     */   private boolean needCached = false;
/*  24 */   private int rodHotbarSlot = -1;
/*  25 */   private long lastActionTime = 0L;
/*  26 */   private long catchTime = 0L;
/*     */   
/*     */   public AutoFish() {
/*  29 */     super("AutoFish", "Автоматизирует процесс рыбалки", Module.ModuleCategory.PLAYER);
/*  30 */     addSettings(new Setting[] { (Setting)this.takeRod });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  35 */     this.isCached = false;
/*  36 */     this.needCached = false;
/*  37 */     this.rodHotbarSlot = -1;
/*  38 */     this.lastActionTime = 0L;
/*  39 */     this.catchTime = 0L;
/*  40 */     super.onDisable();
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onUpdate(EventUpdate event) {
/*  45 */     if (mc.field_1724 == null || mc.field_1687 == null) {
/*     */       return;
/*     */     }
/*     */     
/*  49 */     if (this.takeRod.isState() && this.rodHotbarSlot == -1) {
/*  50 */       findBestFishingRodInHotbar();
/*     */     }
/*     */     
/*  53 */     if (this.rodHotbarSlot != -1 && (mc.field_1724.method_31548()).field_7545 != this.rodHotbarSlot) {
/*  54 */       (mc.field_1724.method_31548()).field_7545 = this.rodHotbarSlot;
/*  55 */       mc.field_1724.field_3944.method_52787((class_2596)new class_2868(this.rodHotbarSlot));
/*     */     } 
/*     */     
/*  58 */     long currentTime = System.currentTimeMillis();
/*     */     
/*  60 */     if (this.isCached && currentTime - this.catchTime >= 600L) {
/*  61 */       useFishingRod();
/*  62 */       this.isCached = false;
/*  63 */       this.needCached = true;
/*  64 */       this.lastActionTime = currentTime;
/*     */     } 
/*     */     
/*  67 */     if (this.needCached && currentTime - this.lastActionTime >= 300L) {
/*  68 */       useFishingRod();
/*  69 */       this.needCached = false;
/*  70 */       this.lastActionTime = currentTime;
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onPacket(EventPacket event) {
/*  76 */     if (mc.field_1724 == null || mc.field_1687 == null) {
/*     */       return;
/*     */     }
/*     */     
/*  80 */     class_2596 class_2596 = event.getPacket(); if (class_2596 instanceof class_2767) { class_2767 packet = (class_2767)class_2596;
/*  81 */       if (packet.method_11894().comp_349() == class_3417.field_14660) {
/*  82 */         this.isCached = true;
/*  83 */         this.catchTime = System.currentTimeMillis();
/*     */       }  }
/*     */   
/*     */   }
/*     */   
/*     */   private void useFishingRod() {
/*  89 */     if (mc.field_1724 == null || mc.field_1761 == null) {
/*     */       return;
/*     */     }
/*     */     
/*  93 */     if (this.rodHotbarSlot != -1 && this.rodHotbarSlot < 9) {
/*  94 */       class_1799 stack = mc.field_1724.method_31548().method_5438(this.rodHotbarSlot);
/*     */       
/*  96 */       if (stack.method_7909() instanceof net.minecraft.class_1787) {
/*  97 */         if ((mc.field_1724.method_31548()).field_7545 != this.rodHotbarSlot) {
/*  98 */           (mc.field_1724.method_31548()).field_7545 = this.rodHotbarSlot;
/*  99 */           mc.field_1724.field_3944.method_52787((class_2596)new class_2868(this.rodHotbarSlot));
/*     */         } 
/*     */         
/* 102 */         mc.field_1761.method_2919((class_1657)mc.field_1724, class_1268.field_5808);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void findBestFishingRodInHotbar() {
/* 108 */     if (mc.field_1724 == null) {
/*     */       return;
/*     */     }
/*     */     
/* 112 */     int bestRodSlot = -1;
/* 113 */     int maxEnchantments = -1;
/*     */     
/* 115 */     for (int i = 0; i < 9; i++) {
/* 116 */       class_1799 stack = mc.field_1724.method_31548().method_5438(i);
/*     */       
/* 118 */       if (stack.method_7909() instanceof net.minecraft.class_1787) {
/* 119 */         int enchantmentCount = class_1890.method_57532(stack).method_57541();
/*     */         
/* 121 */         if (enchantmentCount > maxEnchantments) {
/* 122 */           maxEnchantments = enchantmentCount;
/* 123 */           bestRodSlot = i;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 128 */     if (bestRodSlot != -1)
/* 129 */       this.rodHotbarSlot = bestRodSlot; 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\player\AutoFish.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */