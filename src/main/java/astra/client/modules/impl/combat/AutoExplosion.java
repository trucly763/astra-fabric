/*     */ package shame.astra.client.modules.impl.combat;
/*     */ 
/*     */ import lombok.Generated;
/*     */ import net.minecraft.class_1268;
/*     */ import net.minecraft.class_1297;
/*     */ import net.minecraft.class_1511;
/*     */ import net.minecraft.class_1657;
/*     */ import net.minecraft.class_1713;
/*     */ import net.minecraft.class_1792;
/*     */ import net.minecraft.class_1799;
/*     */ import net.minecraft.class_1802;
/*     */ import net.minecraft.class_1935;
/*     */ import net.minecraft.class_2246;
/*     */ import net.minecraft.class_2248;
/*     */ import net.minecraft.class_2338;
/*     */ import net.minecraft.class_2350;
/*     */ import net.minecraft.class_238;
/*     */ import net.minecraft.class_2382;
/*     */ import net.minecraft.class_239;
/*     */ import net.minecraft.class_241;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_2596;
/*     */ import net.minecraft.class_2815;
/*     */ import net.minecraft.class_2868;
/*     */ import net.minecraft.class_2885;
/*     */ import net.minecraft.class_3965;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.EventBinding;
/*     */ import shame.astra.api.events.implement.EventPacket;
/*     */ import shame.astra.api.utils.input.KeyBoardUtils;
/*     */ import shame.astra.api.utils.rotate.RotationUtils;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.BindSetting;
/*     */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*     */ import shame.astra.client.modules.settings.implement.ModeSetting;
/*     */ 
/*     */ public final class AutoExplosion extends Module {
/*     */   @Generated
/*  40 */   public void setTargetPos(class_2338 targetPos) { this.targetPos = targetPos; } @Generated public void setTargetSlot(int targetSlot) { this.targetSlot = targetSlot; } @Generated public void setOldSlot(int oldSlot) { this.oldSlot = oldSlot; } @Generated public void setNeedSync(boolean needSync) { this.needSync = needSync; } @Generated public void setCrystalArea(class_238 crystalArea) { this.crystalArea = crystalArea; } @Generated public void setBlocked(boolean blocked) { this.blocked = blocked; } @Generated public void setInternalInteract(boolean internalInteract) { this.internalInteract = internalInteract; }
/*     */ 
/*     */   
/*  43 */   public static AutoExplosion INSTANCE = new AutoExplosion();
/*     */   
/*  45 */   private final ModeSetting modeBaxa = new ModeSetting("Режим взрыва", "Авто", new String[] { "Авто", "По бинду" }); @Generated public ModeSetting getModeBaxa() { return this.modeBaxa; } @Generated
/*  46 */   public BindSetting getBind() { return this.bind; } private final BindSetting bind = (new BindSetting("Бинд", -1))
/*  47 */     .visible(() -> Boolean.valueOf(this.modeBaxa.is("По бинду")));
/*  48 */   private final BooleanSetting explosionOnRightClick = new BooleanSetting("Взрыв по ПКМ", true); @Generated public BooleanSetting getExplosionOnRightClick() { return this.explosionOnRightClick; }
/*  49 */    private final BooleanSetting keepCrystal = new BooleanSetting("Оставлять кристалл", false); private static final double INTERACT_RANGE = 4.5D; private class_2338 targetPos; @Generated public BooleanSetting getKeepCrystal() { return this.keepCrystal; }
/*     */   
/*     */   @Generated
/*     */   public class_2338 getTargetPos() {
/*  53 */     return this.targetPos;
/*  54 */   } private int targetSlot = -1; @Generated public int getTargetSlot() { return this.targetSlot; }
/*  55 */    private boolean needSync; private class_238 crystalArea; private int oldSlot = -1; private boolean blocked; private boolean internalInteract; @Generated public int getOldSlot() { return this.oldSlot; } @Generated
/*  56 */   public boolean isNeedSync() { return this.needSync; } @Generated
/*  57 */   public class_238 getCrystalArea() { return this.crystalArea; } @Generated
/*  58 */   public boolean isBlocked() { return this.blocked; } @Generated
/*  59 */   public boolean isInternalInteract() { return this.internalInteract; }
/*     */   
/*     */   public AutoExplosion() {
/*  62 */     super("AutoExplosion", "Автоматически взрывает кристалл", Module.ModuleCategory.COMBAT);
/*  63 */     addSettings(new Setting[] { (Setting)this.modeBaxa, (Setting)this.bind, (Setting)this.explosionOnRightClick, (Setting)this.keepCrystal });
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onBinding(EventBinding event) {
/*  68 */     if (mc.field_1724 == null || mc.field_1687 == null || mc.field_1755 != null)
/*  69 */       return;  if (!this.modeBaxa.is("По бинду")) {
/*     */       return;
/*     */     }
/*     */     
/*  73 */     boolean pressed = (this.bind.getKey() == -1) ? ((event.getKey() == KeyBoardUtils.createMouseBind(2))) : ((event.getKey() == this.bind.getKey()));
/*     */     
/*  75 */     if (pressed) {
/*  76 */       placeObsidianByCrosshair();
/*     */     }
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onPacket(EventPacket event) {
/*  82 */     if (mc.field_1724 == null || mc.field_1687 == null)
/*  83 */       return;  if (event.getType() != EventPacket.Type.SEND)
/*  84 */       return;  if (this.internalInteract)
/*     */       return; 
/*  86 */     class_2596 class_2596 = event.getPacket(); if (class_2596 instanceof class_2885) { class_2885 packet = (class_2885)class_2596;
/*  87 */       class_3965 hit = packet.method_12543();
/*  88 */       class_2338 clickedPos = hit.method_17777();
/*  89 */       class_2338 placePos = clickedPos.method_10093(hit.method_17780());
/*     */       
/*  91 */       if (isHoldingObsidian() && isInRange(placePos) && !mc.field_1724.method_7357().method_7904(new class_1799((class_1935)class_1802.field_8301))) {
/*  92 */         int crystalSlot = findCrystalSlot();
/*  93 */         if (crystalSlot != -1) {
/*  94 */           this.targetPos = placePos;
/*  95 */           this.targetSlot = crystalSlot;
/*  96 */           this.blocked = true;
/*     */         } 
/*     */       } 
/*     */       
/* 100 */       if (this.explosionOnRightClick.isState() && shouldPlaceByRightClick(clickedPos) && 
/* 101 */         placeCrystalFromOffhand(hit, clickedPos)) {
/* 102 */         event.cancel();
/*     */       } }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   @EventLink
/*     */   public void onTick(EventUpdate event) {
/* 110 */     if (mc.field_1724 == null || mc.field_1687 == null) {
/* 111 */       reset();
/*     */       
/*     */       return;
/*     */     } 
/* 115 */     if (this.needSync) {
/* 116 */       this.needSync = false;
/* 117 */       restoreSelectedSlot();
/*     */     } 
/*     */     
/* 120 */     if (this.targetPos != null) {
/* 121 */       if (mc.field_1687.method_8320(this.targetPos).method_26215()) {
/* 122 */         this.targetPos = null;
/* 123 */       } else if (this.blocked) {
/* 124 */         this.blocked = false;
/*     */       } else {
/* 126 */         tryPlaceCrystalFast(this.targetPos);
/*     */       } 
/*     */     }
/*     */     
/* 130 */     processCrystalArea();
/*     */   }
/*     */   
/*     */   private void tryPlaceCrystalFast(class_2338 pos) {
/* 134 */     if (this.targetSlot < 0 || this.targetSlot > 8 || !canPlaceCrystal(pos)) {
/*     */       return;
/*     */     }
/*     */     
/* 138 */     rotateTo(class_243.method_24953((class_2382)pos));
/*     */     
/* 140 */     this.oldSlot = (mc.field_1724.method_31548()).field_7545;
/* 141 */     mc.method_1562().method_52787((class_2596)new class_2868(this.targetSlot));
/* 142 */     (mc.field_1724.method_31548()).field_7545 = this.targetSlot;
/*     */     
/* 144 */     class_243 hitVec = class_243.method_24953((class_2382)pos).method_1031(0.0D, 0.5D, 0.0D);
/* 145 */     class_3965 result = new class_3965(hitVec, class_2350.field_11036, pos, false);
/* 146 */     sendInteract(class_1268.field_5808, result);
/* 147 */     mc.field_1724.method_6104(class_1268.field_5808);
/*     */     
/* 149 */     this.needSync = true;
/* 150 */     this.crystalArea = boxFromBlock(pos.method_10084()).method_1014(0.1D);
/* 151 */     this.targetPos = null;
/*     */   }
/*     */   
/*     */   private void processCrystalArea() {
/* 155 */     if (this.crystalArea == null)
/*     */       return; 
/* 157 */     for (class_1297 entity : mc.field_1687.method_8335(null, this.crystalArea)) {
/* 158 */       if (entity instanceof class_1511) { class_1511 crystal = (class_1511)entity; if (!crystal.method_5805())
/*     */           continue; 
/* 160 */         if (!crystal.method_5829().method_1006(mc.field_1724.method_33571())) {
/* 161 */           rotateTo(crystal.method_5829().method_1005());
/*     */         }
/* 163 */         attackCrystal(crystal);
/* 164 */         this.crystalArea = null;
/* 165 */         if (!this.keepCrystal.isState())
/* 166 */           restoreSelectedSlot(); 
/*     */         return; }
/*     */     
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean shouldPlaceByRightClick(class_2338 clickedPos) {
/* 173 */     if (mc.field_1724.method_7357().method_7904(new class_1799((class_1935)class_1802.field_8301))) return false; 
/* 174 */     if (isHoldingBlockForPlace()) return false;
/*     */     
/* 176 */     class_2248 block = mc.field_1687.method_8320(clickedPos).method_26204();
/* 177 */     if (block != class_2246.field_10540 && block != class_2246.field_9987) return false;
/*     */     
/* 179 */     return mc.field_1687.method_8320(clickedPos.method_10084()).method_26215();
/*     */   }
/*     */   
/*     */   private boolean placeCrystalFromOffhand(class_3965 hit, class_2338 clickedPos) {
/* 183 */     int slot = findScreenSlot(class_1802.field_8301);
/* 184 */     if (slot == -1 && mc.field_1724.method_6079().method_7909() != class_1802.field_8301) return false;
/*     */     
/* 186 */     boolean swapped = false;
/* 187 */     if (mc.field_1724.method_6079().method_7909() != class_1802.field_8301) {
/* 188 */       swapSlotToOffhand(slot);
/* 189 */       swapped = true;
/*     */     } 
/*     */     
/* 192 */     sendInteract(class_1268.field_5810, hit);
/* 193 */     mc.field_1724.method_6104(class_1268.field_5810);
/* 194 */     this.crystalArea = boxFromBlock(clickedPos.method_10084()).method_1014(0.1D);
/*     */     
/* 196 */     if (swapped) {
/* 197 */       swapSlotToOffhand(slot);
/* 198 */       mc.field_1724.field_3944.method_52787((class_2596)new class_2815(0));
/*     */     } 
/* 200 */     return true;
/*     */   }
/*     */   private void placeObsidianByCrosshair() {
/*     */     class_3965 hit;
/* 204 */     int obsidianSlot = findScreenSlot(class_1802.field_8281);
/* 205 */     int crystalSlot = findCrystalSlot();
/* 206 */     if (obsidianSlot == -1 || crystalSlot == -1)
/* 207 */       return;  class_239 class_239 = mc.field_1765; if (class_239 instanceof class_3965) { hit = (class_3965)class_239; } else { return; }
/* 208 */      if (hit.method_17783() != class_239.class_240.field_1332)
/* 209 */       return;  if (mc.field_1687.method_8320(hit.method_17777()).method_26215())
/*     */       return; 
/* 211 */     class_2338 placePos = hit.method_17777().method_10093(hit.method_17780());
/* 212 */     this.targetPos = placePos;
/* 213 */     this.targetSlot = crystalSlot;
/* 214 */     this.blocked = true;
/*     */     
/* 216 */     swapSlotToOffhand(obsidianSlot);
/* 217 */     sendInteract(class_1268.field_5810, hit);
/* 218 */     mc.field_1724.method_6104(class_1268.field_5810);
/* 219 */     swapSlotToOffhand(obsidianSlot);
/* 220 */     mc.field_1724.field_3944.method_52787((class_2596)new class_2815(0));
/*     */   }
/*     */   
/*     */   private void attackCrystal(class_1511 crystal) {
/* 224 */     mc.method_1562().method_52787((class_2596)class_2824.method_34206((class_1297)crystal, false));
/* 225 */     mc.field_1724.method_6104(class_1268.field_5808);
/*     */   }
/*     */   
/*     */   private void sendInteract(class_1268 hand, class_3965 hitResult) {
/* 229 */     this.internalInteract = true;
/*     */     try {
/* 231 */       mc.method_1562().method_52787((class_2596)new class_2885(hand, hitResult, 0));
/*     */     } finally {
/* 233 */       this.internalInteract = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void rotateTo(class_243 vec) {
/* 238 */     class_241 rotation = RotationUtils.getRotations(vec);
/* 239 */     RotationStorage.update(new Rotation(rotation.field_1343, rotation.field_1342), 360.0F, 360.0F, 360.0F, 360.0F, 1, 2, false);
/*     */   }
/*     */   
/*     */   private boolean canPlaceCrystal(class_2338 pos) {
/* 243 */     class_2338 up1 = pos.method_10084();
/* 244 */     class_2338 up2 = pos.method_10086(2);
/*     */     
/* 246 */     if (!mc.field_1687.method_8320(up1).method_26215()) return false; 
/* 247 */     if (!mc.field_1687.method_8320(up2).method_26215()) return false;
/*     */ 
/*     */ 
/*     */     
/* 251 */     class_238 box = new class_238(up1.method_10263(), up1.method_10264(), up1.method_10260(), up1.method_10263() + 1.0D, up1.method_10264() + 2.0D, up1.method_10260() + 1.0D);
/*     */ 
/*     */     
/* 254 */     for (class_1297 entity : mc.field_1687.method_8335(null, box)) {
/* 255 */       if (!(entity instanceof class_1511)) {
/* 256 */         return false;
/*     */       }
/*     */     } 
/* 259 */     return true;
/*     */   }
/*     */   
/*     */   private int findCrystalSlot() {
/* 263 */     for (int i = 0; i < 9; i++) {
/* 264 */       if (mc.field_1724.method_31548().method_5438(i).method_7909() == class_1802.field_8301) {
/* 265 */         return i;
/*     */       }
/*     */     } 
/* 268 */     return -1;
/*     */   }
/*     */   
/*     */   private int findScreenSlot(class_1792 item) {
/* 272 */     for (int i = 9; i < 45; i++) {
/* 273 */       class_1799 stack = mc.field_1724.field_7498.method_7611(i).method_7677();
/* 274 */       if (stack.method_7909() == item) {
/* 275 */         return i;
/*     */       }
/*     */     } 
/* 278 */     return -1;
/*     */   }
/*     */   
/*     */   private void swapSlotToOffhand(int slot) {
/* 282 */     if (slot >= 36 && slot <= 44) {
/* 283 */       mc.field_1761.method_2906(0, 45, slot - 36, class_1713.field_7791, (class_1657)mc.field_1724);
/*     */       
/*     */       return;
/*     */     } 
/* 287 */     mc.field_1761.method_2906(0, slot, 0, class_1713.field_7791, (class_1657)mc.field_1724);
/* 288 */     mc.field_1761.method_2906(0, 45, 0, class_1713.field_7791, (class_1657)mc.field_1724);
/* 289 */     mc.field_1761.method_2906(0, slot, 0, class_1713.field_7791, (class_1657)mc.field_1724);
/*     */   }
/*     */   
/*     */   private void restoreSelectedSlot() {
/* 293 */     if (this.oldSlot != -1) {
/* 294 */       mc.method_1562().method_52787((class_2596)new class_2868(this.oldSlot));
/* 295 */       (mc.field_1724.method_31548()).field_7545 = this.oldSlot;
/* 296 */       this.oldSlot = -1;
/*     */     } 
/*     */   }
/*     */   
/*     */   private class_238 boxFromBlock(class_2338 pos) {
/* 301 */     return new class_238(pos
/* 302 */         .method_10263(), pos.method_10264(), pos.method_10260(), pos
/* 303 */         .method_10263() + 1.0D, pos.method_10264() + 1.0D, pos.method_10260() + 1.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isHoldingObsidian() {
/* 308 */     return (mc.field_1724.method_6047().method_7909() == class_1802.field_8281 || mc.field_1724
/* 309 */       .method_6079().method_7909() == class_1802.field_8281);
/*     */   }
/*     */   
/*     */   private boolean isHoldingBlockForPlace() {
/* 313 */     class_1792 main = mc.field_1724.method_6047().method_7909();
/* 314 */     class_1792 off = mc.field_1724.method_6079().method_7909();
/*     */     
/* 316 */     return ((main instanceof net.minecraft.class_1747 && main != class_1802.field_8575) || (off instanceof net.minecraft.class_1747 && off != class_1802.field_8575));
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isInRange(class_2338 pos) {
/* 321 */     return (mc.field_1724.method_33571().method_1022(class_243.method_24953((class_2382)pos)) <= 4.5D);
/*     */   }
/*     */   
/*     */   private void reset() {
/* 325 */     if (this.oldSlot != -1 && mc.field_1724 != null && mc.method_1562() != null) {
/* 326 */       restoreSelectedSlot();
/*     */     }
/* 328 */     this.targetPos = null;
/* 329 */     this.targetSlot = -1;
/* 330 */     this.needSync = false;
/* 331 */     this.crystalArea = null;
/* 332 */     this.blocked = false;
/* 333 */     this.internalInteract = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/* 338 */     super.onEnable();
/* 339 */     reset();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 344 */     super.onDisable();
/* 345 */     reset();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\combat\AutoExplosion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */