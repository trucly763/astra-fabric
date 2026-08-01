/*     */ package shame.astra.api.utils.player;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import net.minecraft.class_1268;
/*     */ import net.minecraft.class_1293;
/*     */ import net.minecraft.class_1294;
/*     */ import net.minecraft.class_1657;
/*     */ import net.minecraft.class_1792;
/*     */ import net.minecraft.class_1799;
/*     */ import net.minecraft.class_1802;
/*     */ import net.minecraft.class_2248;
/*     */ import net.minecraft.class_2596;
/*     */ import net.minecraft.class_2868;
/*     */ import net.minecraft.class_5134;
/*     */ import net.minecraft.class_7923;
/*     */ import net.minecraft.class_9285;
/*     */ import net.minecraft.class_9334;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ 
/*     */ public final class HotbarUtil implements QClient {
/*     */   @Generated
/*     */   private HotbarUtil() {
/*  23 */     throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
/*  24 */   } private static int cachedSlot = -1;
/*     */   
/*     */   public static int getItemCount(class_1792 item) {
/*  27 */     if (mc.field_1724 == null) return 0;
/*     */     
/*  29 */     int counter = 0;
/*  30 */     for (int i = 0; i < mc.field_1724.method_31548().method_5439(); i++) {
/*  31 */       class_1799 stack = mc.field_1724.method_31548().method_5438(i);
/*  32 */       if (stack.method_31574(item)) counter += stack.method_7947(); 
/*     */     } 
/*  34 */     return counter;
/*     */   }
/*     */   
/*     */   public static SlotSearchResult getAxe() {
/*  38 */     return findBest(itemStack -> itemStack.method_7909() instanceof net.minecraft.class_1743, false);
/*     */   }
/*     */   
/*     */   public static SlotSearchResult getAxeHotBar() {
/*  42 */     return findBest(itemStack -> itemStack.method_7909() instanceof net.minecraft.class_1743, true);
/*     */   }
/*     */   
/*     */   public static SlotSearchResult getPickAxe() {
/*  46 */     return findBest(itemStack -> itemStack.method_7909() instanceof net.minecraft.class_1810, false);
/*     */   }
/*     */   
/*     */   public static SlotSearchResult getPickAxeHotbar() {
/*  50 */     return getPickAxeHotBar();
/*     */   }
/*     */   
/*     */   public static SlotSearchResult getPickAxeHotBar() {
/*  54 */     return findBest(itemStack -> itemStack.method_7909() instanceof net.minecraft.class_1810, true);
/*     */   }
/*     */   
/*     */   public static SlotSearchResult getSword() {
/*  58 */     return findBest(itemStack -> itemStack.method_7909() instanceof net.minecraft.class_1829, false);
/*     */   }
/*     */   
/*     */   public static SlotSearchResult getSwordHotBar() {
/*  62 */     return findBest(itemStack -> itemStack.method_7909() instanceof net.minecraft.class_1829, true);
/*     */   }
/*     */   
/*     */   public static SlotSearchResult getSkull() {
/*  66 */     return findInHotBar(stack -> (stack.method_31574(class_1802.field_8398) || stack.method_31574(class_1802.field_8791) || stack.method_31574(class_1802.field_8681) || stack.method_31574(class_1802.field_8575) || stack.method_31574(class_1802.field_8470)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getElytra() {
/*  74 */     if (mc.field_1724 == null) return -1;
/*     */     
/*  76 */     for (class_1799 stack : (mc.field_1724.method_31548()).field_7548) {
/*  77 */       if (stack.method_31574(class_1802.field_8833) && stack.method_7919() < stack.method_7936() - 1) {
/*  78 */         return -2;
/*     */       }
/*     */     } 
/*     */     
/*  82 */     for (int i = 0; i < 36; i++) {
/*  83 */       class_1799 stack = mc.field_1724.method_31548().method_5438(i);
/*  84 */       if (stack.method_31574(class_1802.field_8833) && stack.method_7919() < stack.method_7936() - 1) {
/*  85 */         return (i < 9) ? (i + 36) : i;
/*     */       }
/*     */     } 
/*  88 */     return -1;
/*     */   }
/*     */   
/*     */   public static SlotSearchResult findInHotBar(Searcher searcher) {
/*  92 */     if (mc.field_1724 != null) {
/*  93 */       if (searcher.isValid(mc.field_1724.method_6079())) {
/*  94 */         return SlotSearchResult.inOffhand(mc.field_1724.method_6079());
/*     */       }
/*     */       
/*  97 */       for (int i = 0; i < 9; i++) {
/*  98 */         class_1799 stack = mc.field_1724.method_31548().method_5438(i);
/*  99 */         if (searcher.isValid(stack)) return new SlotSearchResult(i, true, stack); 
/*     */       } 
/*     */     } 
/* 102 */     return SlotSearchResult.notFound();
/*     */   }
/*     */   
/*     */   public static SlotSearchResult findItemInHotBar(List<class_1792> items) {
/* 106 */     return findInHotBar(stack -> items.contains(stack.method_7909()));
/*     */   }
/*     */   
/*     */   public static SlotSearchResult findItemInHotBar(class_1792... items) {
/* 110 */     return findItemInHotBar(Arrays.asList(items));
/*     */   }
/*     */   
/*     */   public static SlotSearchResult findInInventory(Searcher searcher) {
/* 114 */     if (mc.field_1724 != null)
/* 115 */       for (int i = 35; i >= 0; i--) {
/* 116 */         class_1799 stack = mc.field_1724.method_31548().method_5438(i);
/* 117 */         if (searcher.isValid(stack)) return new SlotSearchResult(i, true, stack);
/*     */       
/*     */       }  
/* 120 */     return SlotSearchResult.notFound();
/*     */   }
/*     */   
/*     */   public static SlotSearchResult findItemInInventory(List<class_1792> items) {
/* 124 */     return findInInventory(stack -> items.contains(stack.method_7909()));
/*     */   }
/*     */   
/*     */   public static SlotSearchResult findItemInInventory(class_1792... items) {
/* 128 */     return findItemInInventory(Arrays.asList(items));
/*     */   }
/*     */   
/*     */   public static SlotSearchResult findBlockInHotBar(@NotNull List<class_2248> blocks) {
/* 132 */     return findItemInHotBar(blocks.stream().map(class_2248::method_8389).toList());
/*     */   }
/*     */   
/*     */   public static SlotSearchResult findBlockInHotBar(class_2248... blocks) {
/* 136 */     return findItemInHotBar(Arrays.<class_2248>stream(blocks).map(class_2248::method_8389).toList());
/*     */   }
/*     */   
/*     */   public static SlotSearchResult findBlockInInventory(@NotNull List<class_2248> blocks) {
/* 140 */     return findItemInInventory(blocks.stream().map(class_2248::method_8389).toList());
/*     */   }
/*     */   
/*     */   public static SlotSearchResult findBlockInInventory(class_2248... blocks) {
/* 144 */     return findItemInInventory(Arrays.<class_2248>stream(blocks).map(class_2248::method_8389).toList());
/*     */   }
/*     */   
/*     */   public static void saveSlot() {
/* 148 */     if (mc.field_1724 != null) cachedSlot = (mc.field_1724.method_31548()).field_7545; 
/*     */   }
/*     */   
/*     */   public static void returnSlot() {
/* 152 */     if (cachedSlot != -1) switchTo(cachedSlot); 
/* 153 */     cachedSlot = -1;
/*     */   }
/*     */   
/*     */   public static void saveAndSwitchTo(int slot) {
/* 157 */     saveSlot();
/* 158 */     switchTo(slot);
/*     */   }
/*     */   
/*     */   public static void switchTo(int slot) {
/* 162 */     if (mc.field_1724 == null || mc.method_1562() == null || slot < 0 || slot > 8)
/* 163 */       return;  if ((mc.field_1724.method_31548()).field_7545 == slot)
/* 164 */       return;  (mc.field_1724.method_31548()).field_7545 = slot;
/* 165 */     mc.method_1562().method_52787((class_2596)new class_2868(slot));
/*     */   }
/*     */   
/*     */   public static void switchToSilent(int slot) {
/* 169 */     if (mc.field_1724 == null || mc.method_1562() == null || slot < 0 || slot > 8)
/* 170 */       return;  mc.method_1562().method_52787((class_2596)new class_2868(slot));
/*     */   }
/*     */   
/*     */   public static SlotSearchResult getAntiWeaknessItem() {
/* 174 */     if (mc.field_1724 == null) return SlotSearchResult.notFound();
/*     */     
/* 176 */     class_1792 mainHand = mc.field_1724.method_6047().method_7909();
/* 177 */     if (mainHand instanceof net.minecraft.class_1829 || mainHand instanceof net.minecraft.class_1810 || mainHand instanceof net.minecraft.class_1743 || mainHand instanceof net.minecraft.class_1821)
/*     */     {
/*     */ 
/*     */       
/* 181 */       return new SlotSearchResult((mc.field_1724.method_31548()).field_7545, true, mc.field_1724.method_6047());
/*     */     }
/*     */     
/* 184 */     return findInHotBar(stack -> (stack.method_7909() instanceof net.minecraft.class_1829 || stack.method_7909() instanceof net.minecraft.class_1810 || stack.method_7909() instanceof net.minecraft.class_1743 || stack.method_7909() instanceof net.minecraft.class_1821));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float getHitDamage(@NotNull class_1799 weapon, class_1657 entity) {
/* 191 */     if (mc.field_1724 == null || mc.field_1687 == null) return 0.0F;
/*     */     
/* 193 */     float baseDamage = getBaseAttackDamage(weapon);
/*     */     
/* 195 */     if (mc.field_1724.field_6017 > 0.0F) baseDamage += baseDamage / 2.0F;
/*     */     
/* 197 */     if (mc.field_1724.method_6059(class_1294.field_5910)) {
/* 198 */       int strength = ((class_1293)Objects.<class_1293>requireNonNull(mc.field_1724.method_6112(class_1294.field_5910))).method_5578() + 1;
/* 199 */       baseDamage += 3.0F * strength;
/*     */     } 
/*     */     
/* 202 */     return class_1280.method_5496((class_1309)entity, baseDamage, mc.field_1687.method_48963().method_48830(), entity.method_6096(), 
/* 203 */         (float)entity.method_45325(class_5134.field_23725));
/*     */   }
/*     */   
/*     */   public static SlotSearchResult findBedInHotBar() {
/* 207 */     return findInHotBar(stack -> stack.method_7909() instanceof net.minecraft.class_1748);
/*     */   }
/*     */   
/*     */   public static SlotSearchResult findBed() {
/* 211 */     return findInInventory(stack -> stack.method_7909() instanceof net.minecraft.class_1748);
/*     */   }
/*     */   
/*     */   public static class_1792 getItem(String name) {
/* 215 */     if (name == null) return class_1802.field_8162; 
/* 216 */     String normalized = name.toLowerCase();
/*     */     
/* 218 */     for (class_2248 block : class_7923.field_41175) {
/* 219 */       if (block.method_63499().replace("block.minecraft.", "").equals(normalized)) {
/* 220 */         return class_1792.method_7867(block);
/*     */       }
/*     */     } 
/*     */     
/* 224 */     for (class_1792 item : class_7923.field_41178) {
/* 225 */       if (item.method_7876().replace("item.minecraft.", "").equals(normalized)) {
/* 226 */         return item;
/*     */       }
/*     */     } 
/*     */     
/* 230 */     return class_1802.field_8831;
/*     */   }
/*     */   
/*     */   public static int getBedsCount() {
/* 234 */     if (mc.field_1724 == null) return 0;
/*     */     
/* 236 */     int counter = 0;
/* 237 */     for (int i = 0; i < mc.field_1724.method_31548().method_5439(); i++) {
/* 238 */       class_1799 stack = mc.field_1724.method_31548().method_5438(i);
/* 239 */       if (stack.method_7909() instanceof net.minecraft.class_1748) counter += stack.method_7947(); 
/*     */     } 
/* 241 */     return counter;
/*     */   }
/*     */   
/*     */   private static SlotSearchResult findBest(Searcher searcher, boolean hotbarOnly) {
/* 245 */     if (mc.field_1724 == null) return SlotSearchResult.notFound();
/*     */     
/* 247 */     int bestSlot = -1;
/* 248 */     float bestDamage = 0.0F;
/* 249 */     int end = hotbarOnly ? 8 : 35;
/*     */     
/* 251 */     for (int i = 0; i <= end; i++) {
/* 252 */       class_1799 stack = mc.field_1724.method_31548().method_5438(i);
/* 253 */       if (searcher.isValid(stack)) {
/*     */         
/* 255 */         float damage = getBaseAttackDamage(stack);
/* 256 */         if (damage > bestDamage) {
/* 257 */           bestDamage = damage;
/* 258 */           bestSlot = i;
/*     */         } 
/*     */       } 
/*     */     } 
/* 262 */     return (bestSlot == -1) ? 
/* 263 */       SlotSearchResult.notFound() : 
/* 264 */       new SlotSearchResult(bestSlot, true, mc.field_1724.method_31548().method_5438(bestSlot));
/*     */   }
/*     */   
/*     */   private static float getBaseAttackDamage(class_1799 stack) {
/* 268 */     class_9285 component = (class_9285)stack.method_57825(class_9334.field_49636, class_9285.field_49326);
/* 269 */     double damage = 1.0D;
/*     */     
/* 271 */     for (class_9285.class_9287 entry : component.comp_2393()) {
/* 272 */       if (entry.comp_2395().equals(class_5134.field_23721)) {
/* 273 */         damage += entry.comp_2396().comp_2449();
/*     */       }
/*     */     } 
/*     */     
/* 277 */     return (float)damage;
/*     */   }
/*     */   
/*     */   public static boolean isHolding(class_1792 item) {
/* 281 */     return (mc.field_1724 != null && (mc.field_1724.method_6047().method_31574(item) || mc.field_1724.method_6079().method_31574(item)));
/*     */   }
/*     */   
/*     */   public static class_1268 getHand(class_1792 item) {
/* 285 */     if (mc.field_1724 == null) return null; 
/* 286 */     if (mc.field_1724.method_6079().method_31574(item)) return class_1268.field_5810; 
/* 287 */     if (mc.field_1724.method_6047().method_31574(item)) return class_1268.field_5808; 
/* 288 */     return null;
/*     */   }
/*     */   
/*     */   public static interface Searcher {
/*     */     boolean isValid(class_1799 param1class_1799);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\player\HotbarUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */