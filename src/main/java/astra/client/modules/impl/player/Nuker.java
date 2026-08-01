/*     */ package shame.astra.client.modules.impl.player;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import net.minecraft.class_1268;
/*     */ import net.minecraft.class_1922;
/*     */ import net.minecraft.class_2338;
/*     */ import net.minecraft.class_2350;
/*     */ import net.minecraft.class_2382;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_2680;
/*     */ import net.minecraft.class_7923;
/*     */ import shame.astra.api.events.implement.EventUpdate;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*     */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*     */ 
/*     */ public class Nuker extends Module {
/*  20 */   public static Nuker INSTANCE = new Nuker();
/*     */   
/*  22 */   private final FloatSetting radius = new FloatSetting("Дистанция", 3.0F, 1.0F, 5.0F, 1.0F);
/*  23 */   private final BooleanSetting breakAll = new BooleanSetting("Ломать все блоки", false);
/*  24 */   private final BooleanSetting swing = new BooleanSetting("Анимация руки", true);
/*     */   
/*  26 */   private final Set<String> targetBlocks = new HashSet<>();
/*     */   private class_2338 currentTargetBlock;
/*     */   
/*     */   public Nuker() {
/*  30 */     super("Nuker", "Автоматически ломает блоки в радиусе", Module.ModuleCategory.PLAYER);
/*  31 */     addSettings(new Setting[] { (Setting)this.radius, (Setting)this.breakAll, (Setting)this.swing });
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onUpdate(EventUpdate event) {
/*  36 */     if (mc.field_1724 == null || mc.field_1687 == null || mc.field_1761 == null) {
/*  37 */       resetBreaking();
/*     */       
/*     */       return;
/*     */     } 
/*  41 */     if (!this.breakAll.isState() && this.targetBlocks.isEmpty()) {
/*  42 */       resetBreaking();
/*     */       
/*     */       return;
/*     */     } 
/*  46 */     if (!isCurrentTargetValid()) {
/*  47 */       this.currentTargetBlock = findNewTarget();
/*     */     }
/*     */     
/*  50 */     if (this.currentTargetBlock != null) {
/*  51 */       breakCurrentTarget();
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean isCurrentTargetValid() {
/*  56 */     return (this.currentTargetBlock != null && 
/*  57 */       isInRange(this.currentTargetBlock) && 
/*  58 */       shouldBreak(this.currentTargetBlock));
/*     */   }
/*     */   
/*     */   private class_2338 findNewTarget() {
/*  62 */     int range = Math.round(this.radius.get());
/*  63 */     class_2338 playerPos = mc.field_1724.method_24515();
/*     */     
/*  65 */     return class_2338.method_20437(playerPos
/*  66 */         .method_10069(-range, 0, -range), playerPos
/*  67 */         .method_10069(range, range, range))
/*     */       
/*  69 */       .map(class_2338::method_10062)
/*  70 */       .filter(this::isInRange)
/*  71 */       .filter(this::shouldBreak)
/*  72 */       .min(Comparator.comparingDouble(pos -> mc.field_1724.method_5707(class_243.method_24953((class_2382)pos))))
/*  73 */       .orElse(null);
/*     */   }
/*     */   
/*     */   private boolean isInRange(class_2338 pos) {
/*  77 */     double maxDistance = this.radius.get();
/*  78 */     return (mc.field_1724.method_5707(class_243.method_24953((class_2382)pos)) <= maxDistance * maxDistance);
/*     */   }
/*     */   
/*     */   private boolean shouldBreak(class_2338 pos) {
/*  82 */     class_2680 state = mc.field_1687.method_8320(pos);
/*  83 */     if (state == null || state.method_26215() || state.method_26214((class_1922)mc.field_1687, pos) < 0.0F) {
/*  84 */       return false;
/*     */     }
/*     */     
/*  87 */     if (this.breakAll.isState()) {
/*  88 */       return true;
/*     */     }
/*     */     
/*  91 */     String blockName = class_7923.field_41175.method_10221(state.method_26204()).method_12832().toLowerCase();
/*  92 */     return this.targetBlocks.contains(blockName);
/*     */   }
/*     */   
/*     */   private void breakCurrentTarget() {
/*  96 */     if (this.currentTargetBlock == null || mc.field_1724 == null || mc.field_1761 == null) {
/*     */       return;
/*     */     }
/*     */     
/* 100 */     mc.field_1761.method_2910(this.currentTargetBlock, class_2350.field_11036);
/* 101 */     mc.field_1761.method_2902(this.currentTargetBlock, class_2350.field_11036);
/*     */     
/* 103 */     if (this.swing.isState()) {
/* 104 */       mc.field_1724.method_6104(class_1268.field_5808);
/*     */     }
/*     */     
/* 107 */     if (mc.field_1687.method_8320(this.currentTargetBlock).method_26215()) {
/* 108 */       resetBreaking();
/*     */     }
/*     */   }
/*     */   
/*     */   private void resetBreaking() {
/* 113 */     this.currentTargetBlock = null;
/* 114 */     if (mc.field_1761 != null) {
/* 115 */       mc.field_1761.method_2925();
/*     */     }
/*     */   }
/*     */   
/*     */   public void addBlock(String blockName) {
/* 120 */     this.targetBlocks.add(normalizeBlockName(blockName));
/*     */   }
/*     */   
/*     */   public void removeBlock(String blockName) {
/* 124 */     this.targetBlocks.remove(normalizeBlockName(blockName));
/*     */   }
/*     */   
/*     */   public void clearBlocks() {
/* 128 */     this.targetBlocks.clear();
/* 129 */     resetBreaking();
/*     */   }
/*     */   
/*     */   public boolean isTargetBlock(String blockName) {
/* 133 */     return this.targetBlocks.contains(normalizeBlockName(blockName));
/*     */   }
/*     */   
/*     */   public Set<String> getTargetBlocks() {
/* 137 */     return new HashSet<>(this.targetBlocks);
/*     */   }
/*     */   
/*     */   public static String normalizeBlockName(String blockName) {
/* 141 */     if (blockName == null) {
/* 142 */       return "";
/*     */     }
/* 144 */     String normalized = blockName.toLowerCase().trim();
/* 145 */     int namespaceSeparator = normalized.indexOf(':');
/* 146 */     return (namespaceSeparator >= 0) ? normalized.substring(namespaceSeparator + 1) : normalized;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 151 */     resetBreaking();
/* 152 */     super.onDisable();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\player\Nuker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */