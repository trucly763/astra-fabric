/*     */ package shame.astra.client.modules.impl.combat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import net.minecraft.class_1268;
/*     */ import net.minecraft.class_1294;
/*     */ import net.minecraft.class_1297;
/*     */ import net.minecraft.class_1309;
/*     */ import net.minecraft.class_1657;
/*     */ import net.minecraft.class_1713;
/*     */ import net.minecraft.class_1792;
/*     */ import net.minecraft.class_1802;
/*     */ import net.minecraft.class_1922;
/*     */ import net.minecraft.class_2338;
/*     */ import net.minecraft.class_2350;
/*     */ import net.minecraft.class_2382;
/*     */ import net.minecraft.class_241;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_2680;
/*     */ import net.minecraft.class_3532;
/*     */ import net.minecraft.class_3965;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.EventBinding;
/*     */ import shame.astra.api.events.implement.EventGameUpdate;
/*     */ import shame.astra.api.events.implement.EventUpdate;
/*     */ import shame.astra.api.storages.implement.RotationStorage;
/*     */ import shame.astra.api.utils.rotate.Rotation;
/*     */ import shame.astra.api.utils.rotate.RotationUtils;
/*     */ import shame.astra.astra;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.BindSetting;
/*     */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*     */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*     */ import shame.astra.client.modules.settings.implement.ListSetting;
/*     */ import shame.astra.client.modules.settings.implement.ModeSetting;
/*     */ 
/*     */ public class AutoTrap extends Module {
/*  39 */   public static AutoTrap INSTANCE = new AutoTrap();
/*     */   
/*  41 */   private final ModeSetting mode = new ModeSetting("Мод", "Obsidian", new String[] { "Obsidian", "CobWeb" });
/*  42 */   private final FloatSetting distance = new FloatSetting("Дистанция", 3.0F, 1.0F, 5.0F, 0.1F);
/*  43 */   private final BindSetting bind = new BindSetting("Бинд", -1);
/*  44 */   private final BooleanSetting fromInventory = new BooleanSetting("Из инвентаря", false);
/*  45 */   private final BooleanSetting rotation = new BooleanSetting("Ротация", true);
/*     */   
/*  47 */   private final ListSetting targets = new ListSetting("Таргеты", new BooleanSetting[] { new BooleanSetting("Игроки", true), new BooleanSetting("Невидимые", true), new BooleanSetting("Себя", false) });
/*     */   
/*     */   private final BooleanSetting reverseRotate;
/*     */   
/*     */   private class_1657 target;
/*     */   
/*     */   private int oldSlot;
/*     */   
/*     */   private int inventorySlot;
/*     */   
/*     */   private boolean placing;
/*     */   
/*     */   private boolean use;
/*     */   private final List<class_2338> blocksToPlace;
/*     */   private int placeIndex;
/*     */   private class_2338 currentBlock;
/*     */   private boolean waitingForRotation;
/*     */   private int rotationTicks;
/*     */   private float restoreYaw;
/*     */   private float restorePitch;
/*     */   
/*     */   public AutoTrap() {
/*  69 */     super("AutoTrap", "Автоматически ставит ловушку вокруг игрока", Module.ModuleCategory.COMBAT); Objects.requireNonNull(this.rotation); this.reverseRotate = (new BooleanSetting("Реверс ротейт", true)).visible(this.rotation::isState); this.oldSlot = -1; this.inventorySlot = -1; this.placing = false; this.use = false; this.blocksToPlace = new ArrayList<>(); this.placeIndex = 0; this.currentBlock = null; this.waitingForRotation = false; this.rotationTicks = 0;
/*  70 */     addSettings(new Setting[] { (Setting)this.mode, (Setting)this.distance, (Setting)this.bind, (Setting)this.fromInventory, (Setting)this.rotation, (Setting)this.reverseRotate, (Setting)this.targets });
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onBinding(EventBinding event) {
/*  75 */     if (mc.field_1755 != null)
/*     */       return; 
/*  77 */     if (event.getKey() == this.bind.getKey()) {
/*  78 */       this.use = true;
/*     */     }
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onGameUpdate(EventGameUpdate e) {
/*  84 */     if (mc.field_1724 == null || mc.field_1687 == null)
/*  85 */       return;  if (!this.placing || this.currentBlock == null || !this.rotation.isState())
/*     */       return; 
/*  87 */     rotateToBlock(this.currentBlock);
/*     */   }
/*     */ 
/*     */   
/*     */   @EventLink
/*     */   public void onUpdate(EventUpdate e) {
/*  93 */     if (mc.field_1724 == null || mc.field_1687 == null) {
/*  94 */       this.use = false;
/*     */       
/*     */       return;
/*     */     } 
/*  98 */     if (this.use && !this.placing) {
/*  99 */       this.target = findTarget();
/* 100 */       if (this.target != null) {
/* 101 */         startPlacing();
/*     */       }
/* 103 */       this.use = false;
/*     */     } 
/*     */     
/* 106 */     if (this.placing) {
/* 107 */       processPlacing();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void rotateToBlock(class_2338 pos) {
/* 113 */     class_2350 side = getPlaceSide(pos);
/* 114 */     if (side == null)
/*     */       return; 
/* 116 */     class_2338 neighbor = pos.method_10093(side);
/* 117 */     class_2350 opposite = side.method_10153();
/* 118 */     class_243 hitVec = getHitVec(neighbor, opposite);
/*     */     
/* 120 */     class_241 targetRot = RotationUtils.getRotations(hitVec);
/*     */     
/* 122 */     RotationStorage.update(new Rotation(targetRot.field_1343, targetRot.field_1342), 360.0F, 360.0F, 360.0F, 360.0F, 5, 1, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class_243 getHitVec(class_2338 neighbor, class_2350 face) {
/* 131 */     class_243 center = class_243.method_24953((class_2382)neighbor);
/* 132 */     return center.method_1031(face
/* 133 */         .method_10148() * 0.5D, face
/* 134 */         .method_10164() * 0.5D, face
/* 135 */         .method_10165() * 0.5D);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isRotatedToBlock(class_2338 pos) {
/* 140 */     if (!this.rotation.isState()) return true;
/*     */     
/* 142 */     class_2350 side = getPlaceSide(pos);
/* 143 */     if (side == null) return false;
/*     */     
/* 145 */     class_2338 neighbor = pos.method_10093(side);
/* 146 */     class_2350 opposite = side.method_10153();
/* 147 */     class_243 hitVec = getHitVec(neighbor, opposite);
/*     */     
/* 149 */     class_241 targetRot = RotationUtils.getRotations(hitVec);
/* 150 */     float yawDiff = Math.abs(class_3532.method_15393(targetRot.field_1343 - mc.field_1724.method_36454()));
/* 151 */     float pitchDiff = Math.abs(class_3532.method_15393(targetRot.field_1342 - mc.field_1724.method_36455()));
/*     */     
/* 153 */     return (yawDiff < 5.0F && pitchDiff < 5.0F);
/*     */   }
/*     */   
/*     */   private void startPlacing() {
/* 157 */     this.blocksToPlace.clear();
/* 158 */     this.placeIndex = 0;
/* 159 */     this.waitingForRotation = false;
/* 160 */     this.rotationTicks = 0;
/*     */     
/* 162 */     class_2338 targetPos = this.target.method_24515();
/*     */     
/* 164 */     if (this.mode.is("Obsidian")) {
/* 165 */       this.blocksToPlace.add(targetPos.method_10069(1, 0, 0));
/* 166 */       this.blocksToPlace.add(targetPos.method_10069(-1, 0, 0));
/* 167 */       this.blocksToPlace.add(targetPos.method_10069(0, 0, 1));
/* 168 */       this.blocksToPlace.add(targetPos.method_10069(0, 0, -1));
/* 169 */       this.blocksToPlace.add(targetPos.method_10069(1, 1, 0));
/* 170 */       this.blocksToPlace.add(targetPos.method_10069(-1, 1, 0));
/* 171 */       this.blocksToPlace.add(targetPos.method_10069(0, 1, 1));
/* 172 */       this.blocksToPlace.add(targetPos.method_10069(0, 1, -1));
/* 173 */       this.blocksToPlace.add(targetPos.method_10069(0, 2, 0));
/* 174 */       this.blocksToPlace.add(targetPos.method_10069(1, 2, 0));
/* 175 */       this.blocksToPlace.add(targetPos.method_10069(-1, 2, 0));
/* 176 */       this.blocksToPlace.add(targetPos.method_10069(0, 2, 1));
/* 177 */       this.blocksToPlace.add(targetPos.method_10069(0, 2, -1));
/*     */     } else {
/* 179 */       this.blocksToPlace.add(targetPos);
/* 180 */       this.blocksToPlace.add(targetPos.method_10084());
/*     */     } 
/*     */     
/* 183 */     if (this.fromInventory.isState()) {
/* 184 */       this.oldSlot = (mc.field_1724.method_31548()).field_7545;
/* 185 */       int slot = findItemSlot();
/* 186 */       if (slot == -1) {
/* 187 */         this.placing = false;
/*     */         
/*     */         return;
/*     */       } 
/* 191 */       if (slot < 9) {
/* 192 */         (mc.field_1724.method_31548()).field_7545 = slot;
/* 193 */         this.inventorySlot = -1;
/*     */       } else {
/* 195 */         this.inventorySlot = slot;
/* 196 */         mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, slot, this.oldSlot, class_1713.field_7791, (class_1657)mc.field_1724);
/*     */       } 
/*     */     } 
/*     */     
/* 200 */     this.placing = true;
/*     */   }
/*     */   
/*     */   private void processPlacing() {
/* 204 */     if (this.target != null && (!this.target.method_5805() || AntiBot.checkBot((class_1309)this.target) || mc.field_1724.method_5739((class_1297)this.target) > this.distance.getValue().floatValue())) {
/* 205 */       finishPlacing();
/*     */       
/*     */       return;
/*     */     } 
/* 209 */     if (this.placeIndex >= this.blocksToPlace.size()) {
/* 210 */       finishPlacing();
/*     */       
/*     */       return;
/*     */     } 
/* 214 */     class_2338 pos = this.blocksToPlace.get(this.placeIndex);
/* 215 */     this.currentBlock = pos;
/*     */     
/* 217 */     if (!mc.field_1687.method_8320(pos).method_45474()) {
/* 218 */       this.placeIndex++;
/* 219 */       this.waitingForRotation = false;
/* 220 */       this.rotationTicks = 0;
/*     */       
/*     */       return;
/*     */     } 
/* 224 */     class_2350 side = getPlaceSide(pos);
/* 225 */     if (side == null) {
/* 226 */       this.placeIndex++;
/* 227 */       this.waitingForRotation = false;
/* 228 */       this.rotationTicks = 0;
/*     */       
/*     */       return;
/*     */     } 
/* 232 */     if (this.rotation.isState()) {
/* 233 */       if (!this.waitingForRotation) {
/* 234 */         rotateToBlock(pos);
/* 235 */         this.waitingForRotation = true;
/* 236 */         this.rotationTicks = 0;
/*     */         
/*     */         return;
/*     */       } 
/* 240 */       this.rotationTicks++;
/*     */       
/* 242 */       if (!isRotatedToBlock(pos) || this.rotationTicks < 2) {
/* 243 */         rotateToBlock(pos);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 248 */     placeBlock(pos);
/* 249 */     this.placeIndex++;
/* 250 */     this.waitingForRotation = false;
/* 251 */     this.rotationTicks = 0;
/*     */   }
/*     */   
/*     */   private void finishPlacing() {
/* 255 */     if (this.fromInventory.isState()) {
/* 256 */       if (this.inventorySlot != -1) {
/* 257 */         mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, this.inventorySlot, this.oldSlot, class_1713.field_7791, (class_1657)mc.field_1724);
/* 258 */         this.inventorySlot = -1;
/* 259 */       } else if (this.oldSlot != -1) {
/* 260 */         (mc.field_1724.method_31548()).field_7545 = this.oldSlot;
/*     */       } 
/* 262 */       this.oldSlot = -1;
/*     */     } 
/* 264 */     this.placing = false;
/* 265 */     this.target = null;
/* 266 */     this.currentBlock = null;
/* 267 */     this.blocksToPlace.clear();
/* 268 */     this.placeIndex = 0;
/* 269 */     this.waitingForRotation = false;
/* 270 */     this.rotationTicks = 0;
/*     */   }
/*     */   
/*     */   private class_2350 getPlaceSide(class_2338 pos) {
/* 274 */     class_2350[] priority = { class_2350.field_11033, class_2350.field_11036, class_2350.field_11043, class_2350.field_11035, class_2350.field_11039, class_2350.field_11034 };
/* 275 */     for (class_2350 dir : priority) {
/* 276 */       class_2338 neighbor = pos.method_10093(dir);
/* 277 */       class_2680 state = mc.field_1687.method_8320(neighbor);
/* 278 */       if (!state.method_45474() && !state.method_51176() && state.method_26212((class_1922)mc.field_1687, neighbor)) {
/* 279 */         return dir;
/*     */       }
/*     */     } 
/* 282 */     for (class_2350 dir : priority) {
/* 283 */       class_2338 neighbor = pos.method_10093(dir);
/* 284 */       class_2680 state = mc.field_1687.method_8320(neighbor);
/* 285 */       if (!state.method_45474() && !state.method_51176()) {
/* 286 */         return dir;
/*     */       }
/*     */     } 
/* 289 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private void placeBlock(class_2338 pos) {
/* 294 */     class_2350 side = getPlaceSide(pos);
/* 295 */     if (side == null)
/*     */       return; 
/* 297 */     class_2338 neighbor = pos.method_10093(side);
/* 298 */     class_2350 opposite = side.method_10153();
/* 299 */     class_243 hitVec = getHitVec(neighbor, opposite);
/*     */     
/* 301 */     class_3965 result = new class_3965(hitVec, opposite, neighbor, false);
/* 302 */     mc.field_1761.method_2896(mc.field_1724, class_1268.field_5808, result);
/* 303 */     mc.field_1724.method_6104(class_1268.field_5808);
/*     */   }
/*     */   
/*     */   private int findItemSlot() {
/* 307 */     class_1792 item = this.mode.is("Obsidian") ? class_1802.field_8281 : class_1802.field_8786;
/* 308 */     for (int i = 0; i < 36; i++) {
/* 309 */       if (mc.field_1724.method_31548().method_5438(i).method_7909() == item) {
/* 310 */         return i;
/*     */       }
/*     */     } 
/* 313 */     return -1;
/*     */   }
/*     */   
/*     */   private class_1657 findTarget() {
/* 317 */     if (this.targets.is("Себя")) {
/* 318 */       return (class_1657)mc.field_1724;
/*     */     }
/*     */     
/* 321 */     List<class_1657> playerTargets = new ArrayList<>();
/* 322 */     for (class_1297 entity : mc.field_1687.method_18112()) {
/* 323 */       if (entity instanceof class_1657) { class_1657 player = (class_1657)entity;
/* 324 */         if (player == mc.field_1724 || 
/* 325 */           !player.method_5805() || 
/* 326 */           AntiBot.checkBot((class_1309)player) || 
/* 327 */           !this.targets.is("Игроки") || (
/* 328 */           player.method_6059(class_1294.field_5905) && !this.targets.is("Невидимые")) || 
/* 329 */           astra.INSTANCE.friendStorage.isFriend(player.method_5477().getString()) || 
/* 330 */           mc.field_1724.method_5739((class_1297)player) > this.distance.getValue().floatValue())
/* 331 */           continue;  playerTargets.add(player); }
/*     */     
/* 333 */     }  if (playerTargets.isEmpty()) return null; 
/* 334 */     playerTargets.sort(Comparator.comparingDouble(p -> mc.field_1724.method_5739((class_1297)p)));
/* 335 */     return playerTargets.get(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 340 */     super.onDisable();
/* 341 */     if (this.placing) {
/* 342 */       finishPlacing();
/*     */     }
/* 344 */     this.target = null;
/* 345 */     this.placing = false;
/* 346 */     this.use = false;
/* 347 */     this.currentBlock = null;
/* 348 */     this.blocksToPlace.clear();
/* 349 */     this.placeIndex = 0;
/* 350 */     this.oldSlot = -1;
/* 351 */     this.inventorySlot = -1;
/* 352 */     this.waitingForRotation = false;
/* 353 */     this.rotationTicks = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/* 358 */     super.onEnable();
/* 359 */     this.placing = false;
/* 360 */     this.use = false;
/* 361 */     this.currentBlock = null;
/* 362 */     this.blocksToPlace.clear();
/* 363 */     this.placeIndex = 0;
/* 364 */     this.oldSlot = -1;
/* 365 */     this.inventorySlot = -1;
/* 366 */     this.waitingForRotation = false;
/* 367 */     this.rotationTicks = 0;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\combat\AutoTrap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */