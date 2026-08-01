/*     */ package shame.astra.api.utils.player;
/*     */ import net.minecraft.class_10192;
/*     */ import net.minecraft.class_1268;
/*     */ import net.minecraft.class_1657;
/*     */ import net.minecraft.class_1713;
/*     */ import net.minecraft.class_1738;
/*     */ import net.minecraft.class_1792;
/*     */ import net.minecraft.class_1799;
/*     */ import net.minecraft.class_1802;
/*     */ import net.minecraft.class_1839;
/*     */ import net.minecraft.class_1887;
/*     */ import net.minecraft.class_1893;
/*     */ import net.minecraft.class_2596;
/*     */ import net.minecraft.class_2815;
/*     */ import net.minecraft.class_2848;
/*     */ import net.minecraft.class_2851;
/*     */ import net.minecraft.class_2868;
/*     */ import net.minecraft.class_5321;
/*     */ import net.minecraft.class_6880;
/*     */ import net.minecraft.class_9304;
/*     */ import net.minecraft.class_9334;
/*     */ 
/*     */ public final class InventoryUtils implements QClient {
/*     */   @Generated
/*     */   private InventoryUtils() {
/*  26 */     throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
/*     */   }
/*     */   public static int getItemSlot(class_1792 input) {
/*  29 */     for (class_1799 stack : mc.field_1724.method_5661()) {
/*  30 */       if (stack.method_7909() == input) {
/*  31 */         return -2;
/*     */       }
/*     */     } 
/*  34 */     int slot = -1;
/*  35 */     for (int i = 0; i < 36; i++) {
/*  36 */       class_1799 s = mc.field_1724.method_31548().method_5438(i);
/*  37 */       if (s.method_7909() == input) {
/*  38 */         slot = i;
/*     */         break;
/*     */       } 
/*     */     } 
/*  42 */     if (slot < 9 && slot != -1) {
/*  43 */       slot += 36;
/*     */     }
/*  45 */     return slot;
/*     */   }
/*     */   
/*     */   public static int getEnchantmentLevel(class_1799 stack, class_5321<class_1887> enchantmentKey) {
/*  49 */     class_9304 enchantments = (class_9304)stack.method_57825(class_9334.field_49633, class_9304.field_49385);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  54 */     for (class_6880<class_1887> enchantment : (Iterable<class_6880<class_1887>>)enchantments.method_57534()) {
/*  55 */       if (enchantment.method_40225(enchantmentKey)) {
/*  56 */         return enchantments.method_57536(enchantment);
/*     */       }
/*     */     } 
/*  59 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int findBestElytraSlot() {
/*  64 */     if (mc.field_1724 == null) return -1;
/*     */     
/*  66 */     int bestSlot = -1;
/*  67 */     double bestScore = -1.0D;
/*     */     
/*  69 */     for (int slot = 0; slot < 36; slot++) {
/*  70 */       class_1799 stack = mc.field_1724.method_31548().method_5438(slot);
/*     */       
/*  72 */       if (stack.method_7909() == class_1802.field_8833) {
/*     */         
/*  74 */         int protection = getEnchantmentLevel(stack, class_1893.field_9111);
/*  75 */         int unbreaking = getEnchantmentLevel(stack, class_1893.field_9119);
/*  76 */         int mending = getEnchantmentLevel(stack, class_1893.field_9101);
/*     */         
/*  78 */         int maxDurability = stack.method_7936();
/*  79 */         int currentDamage = stack.method_7919();
/*  80 */         double durabilityRatio = (maxDurability - currentDamage) / maxDurability;
/*     */         
/*  82 */         double score = (protection * 100 + unbreaking * 10 + ((mending > 0) ? 1 : 0)) + durabilityRatio * 10.0D;
/*     */ 
/*     */         
/*  85 */         if (score > bestScore) {
/*  86 */           bestScore = score;
/*  87 */           bestSlot = slot;
/*     */         } 
/*     */       } 
/*  90 */     }  return bestSlot;
/*     */   }
/*     */   
/*     */   public static int findBestChestplateSlot() {
/*  94 */     if (mc.field_1724 == null) return -1;
/*     */     
/*  96 */     int bestSlot = -1;
/*  97 */     double bestScore = -1.0D;
/*     */     
/*  99 */     for (int slot = 0; slot < 36; slot++) {
/* 100 */       class_1799 stack = mc.field_1724.method_31548().method_5438(slot);
/* 101 */       class_1792 class_1792 = stack.method_7909(); if (class_1792 instanceof class_1738) { class_1738 armor = (class_1738)class_1792;
/*     */         
/* 103 */         class_10192 equippable = (class_10192)stack.method_57824(class_9334.field_54196);
/* 104 */         if (equippable != null && equippable.comp_3174() == class_1304.field_6174) {
/*     */           
/* 106 */           int protection = getEnchantmentLevel(stack, class_1893.field_9111);
/* 107 */           int unbreaking = getEnchantmentLevel(stack, class_1893.field_9119);
/* 108 */           int mending = getEnchantmentLevel(stack, class_1893.field_9101);
/* 109 */           int priority = getChestplatePriority((class_1792)armor);
/*     */           
/* 111 */           int maxDamage = stack.method_7936();
/* 112 */           int damage = stack.method_7919();
/* 113 */           double durabilityRatio = (maxDamage == 0) ? 1.0D : ((maxDamage - damage) / maxDamage);
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 118 */           double score = priority * 10000.0D + protection * 100.0D + unbreaking * 10.0D + ((mending > 0) ? true : false) + durabilityRatio * 10.0D;
/*     */ 
/*     */           
/* 121 */           if (score > bestScore)
/* 122 */           { bestScore = score;
/* 123 */             bestSlot = slot; } 
/*     */         }  }
/*     */     
/* 126 */     }  return bestSlot;
/*     */   }
/*     */   
/*     */   public static int getChestplatePriority(class_1792 item) {
/* 130 */     if (item == class_1802.field_22028) return 5; 
/* 131 */     if (item == class_1802.field_8058) return 4; 
/* 132 */     if (item == class_1802.field_8523) return 3; 
/* 133 */     if (item == class_1802.field_8678) return 2; 
/* 134 */     if (item == class_1802.field_8873) return 2; 
/* 135 */     if (item == class_1802.field_8577) return 1; 
/* 136 */     return 0;
/*     */   }
/*     */   
/*     */   public static int find(class_1792 item, int start, int end) {
/* 140 */     if (mc.field_1724 != null) {
/* 141 */       for (int i = end; i >= start; i--) {
/* 142 */         if (mc.field_1724.field_7512.field_7763 != 0 && mc.field_1724.field_7512.method_7611(i).method_7677().method_7909() == item) {
/* 143 */           return i;
/*     */         }
/*     */         
/* 146 */         if (mc.field_1724.field_7512.field_7763 == 0 && mc.field_1724.method_31548().method_5438(i).method_7909() == item) {
/* 147 */           return i;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 152 */     return -1;
/*     */   }
/*     */   
/*     */   public static void swapAndUseHvH(class_1792 item) {
/* 156 */     int slot = find(item, 9, 45);
/* 157 */     int slotHotbar = find(item, 0, 8);
/* 158 */     int previousSlot = (mc.field_1724.method_31548()).field_7545;
/*     */     
/* 160 */     boolean isUsingItem = mc.field_1724.method_6115();
/*     */     
/* 162 */     if (mc.field_1724.method_6047().method_7909() == item) {
/* 163 */       if (!isUsingItem) {
/* 164 */         mc.field_1761.method_2919((class_1657)mc.field_1724, class_1268.field_5808);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 169 */     if (mc.field_1724.method_6079().method_7909() == item) {
/* 170 */       mc.field_1761.method_2919((class_1657)mc.field_1724, class_1268.field_5810);
/*     */       
/*     */       return;
/*     */     } 
/* 174 */     if (isUsingItem) {
/* 175 */       if (slotHotbar != -1) {
/* 176 */         mc.field_1761.method_2906(0, 36 + slotHotbar, 40, class_1713.field_7791, (class_1657)mc.field_1724);
/* 177 */         mc.field_1724.field_3944.method_52787((class_2596)new class_2815(0));
/* 178 */         mc.field_1761.method_2919((class_1657)mc.field_1724, class_1268.field_5810);
/* 179 */         mc.field_1761.method_2906(0, 36 + slotHotbar, 40, class_1713.field_7791, (class_1657)mc.field_1724);
/* 180 */         mc.field_1724.field_3944.method_52787((class_2596)new class_2815(0));
/* 181 */       } else if (slot != -1) {
/* 182 */         mc.field_1761.method_2906(0, slot, 40, class_1713.field_7791, (class_1657)mc.field_1724);
/* 183 */         mc.field_1724.field_3944.method_52787((class_2596)new class_2815(0));
/* 184 */         mc.field_1761.method_2919((class_1657)mc.field_1724, class_1268.field_5810);
/* 185 */         mc.field_1761.method_2906(0, slot, 40, class_1713.field_7791, (class_1657)mc.field_1724);
/* 186 */         mc.field_1724.field_3944.method_52787((class_2596)new class_2815(0));
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 191 */     if (slotHotbar != -1) {
/* 192 */       mc.field_1724.field_3944.method_52787((class_2596)new class_2868(slotHotbar));
/* 193 */       mc.field_1761.method_2919((class_1657)mc.field_1724, class_1268.field_5808);
/* 194 */       mc.field_1724.field_3944.method_52787((class_2596)new class_2868(previousSlot));
/*     */       
/*     */       return;
/*     */     } 
/* 198 */     if (slot != -1) {
/* 199 */       int slotCorrectable = -1;
/*     */       
/* 201 */       for (int slotNone = 0; slotNone < 8; slotNone++) {
/* 202 */         class_1799 stack = mc.field_1724.method_31548().method_5438(slotNone);
/* 203 */         if (stack.method_7960()) {
/* 204 */           slotCorrectable = slotNone;
/*     */           
/*     */           break;
/*     */         } 
/* 208 */         class_1839 action = stack.method_7976();
/* 209 */         if (action == class_1839.field_8952) {
/* 210 */           slotCorrectable = slotNone;
/*     */         }
/*     */       } 
/*     */       
/* 214 */       boolean wasSprinting = false;
/*     */       
/* 216 */       if (mc.field_1724.method_5624()) {
/* 217 */         mc.field_1724.field_3944.method_52787((class_2596)new class_2851(new class_10185(false, false, false, false, false, false, false)));
/* 218 */         mc.field_1724.method_5728(false);
/* 219 */         mc.field_1724.field_3944.method_52787((class_2596)new class_2848((class_1297)mc.field_1724, class_2848.class_2849.field_12985));
/* 220 */         if (!ModuleClass.sprint.isEnable()) {
/* 221 */           mc.field_1690.field_1867.method_23481(false);
/*     */         }
/* 223 */         wasSprinting = true;
/*     */       } 
/*     */       
/* 226 */       if (slotCorrectable == -1) {
/* 227 */         mc.field_1761.method_2906(0, slot, 8, class_1713.field_7791, (class_1657)mc.field_1724);
/* 228 */         mc.field_1724.field_3944.method_52787((class_2596)new class_2815(0));
/* 229 */         mc.field_1724.field_3944.method_52787((class_2596)new class_2868(8));
/* 230 */         mc.field_1761.method_2919((class_1657)mc.field_1724, class_1268.field_5808);
/* 231 */         mc.field_1724.field_3944.method_52787((class_2596)new class_2868(previousSlot));
/*     */       } else {
/* 233 */         mc.field_1761.method_2906(0, slot, slotCorrectable, class_1713.field_7791, (class_1657)mc.field_1724);
/* 234 */         mc.field_1724.field_3944.method_52787((class_2596)new class_2815(0));
/* 235 */         mc.field_1724.field_3944.method_52787((class_2596)new class_2868(slotCorrectable));
/* 236 */         mc.field_1761.method_2919((class_1657)mc.field_1724, class_1268.field_5808);
/* 237 */         mc.field_1724.field_3944.method_52787((class_2596)new class_2868(previousSlot));
/* 238 */         mc.field_1761.method_2906(0, slot, slotCorrectable, class_1713.field_7791, (class_1657)mc.field_1724);
/* 239 */         mc.field_1724.field_3944.method_52787((class_2596)new class_2815(0));
/*     */       } 
/*     */       
/* 242 */       if (wasSprinting)
/* 243 */         mc.field_1724.field_3944.method_52787((class_2596)new class_2851(mc.field_1724.field_3913.field_54155)); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\player\InventoryUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */