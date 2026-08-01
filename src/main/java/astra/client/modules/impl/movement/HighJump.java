/*     */ package shame.astra.client.modules.impl.movement;
/*     */ 
/*     */ import net.minecraft.class_2246;
/*     */ import net.minecraft.class_2338;
/*     */ import net.minecraft.class_2680;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.EventUpdate;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*     */ import shame.astra.client.modules.settings.implement.ModeSetting;
/*     */ 
/*     */ 
/*     */ public class HighJump
/*     */   extends Module
/*     */ {
/*  17 */   public static HighJump INSTANCE = new HighJump();
/*     */   
/*  19 */   private final ModeSetting mode = new ModeSetting("Режим", "Shulker", new String[] { "Shulker", "Slime", "Boat" });
/*  20 */   private final FloatSetting slimeMultiplier = new FloatSetting("Множитель", 2.0F, 1.1F, 5.0F, 0.1F);
/*     */   
/*     */   private boolean wasInBoat;
/*     */   private double lastVelY;
/*     */   private int cooldown;
/*     */   
/*     */   public HighJump() {
/*  27 */     super("HighJump", "Высокий прыжок от различных источников", Module.ModuleCategory.MOVEMENT);
/*  28 */     addSettings(new Setting[] { (Setting)this.mode, (Setting)this.slimeMultiplier });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  33 */     super.onEnable();
/*  34 */     this.wasInBoat = false;
/*  35 */     this.lastVelY = 0.0D;
/*  36 */     this.cooldown = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  41 */     super.onDisable();
/*  42 */     this.wasInBoat = false;
/*  43 */     this.lastVelY = 0.0D;
/*  44 */     this.cooldown = 0;
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onUpdate(EventUpdate event) {
/*  49 */     if (mc.field_1724 == null || mc.field_1687 == null)
/*     */       return; 
/*  51 */     if (this.cooldown > 0) this.cooldown--;
/*     */     
/*  53 */     if (this.mode.is("Shulker")) {
/*  54 */       handleShulker();
/*     */     }
/*     */     
/*  57 */     if (this.mode.is("Slime")) {
/*  58 */       handleSlime();
/*     */     }
/*     */     
/*  61 */     if (this.mode.is("Boat")) {
/*  62 */       handleBoat();
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleShulker() {
/*  67 */     if (!(mc.field_1755 instanceof net.minecraft.class_495))
/*     */       return; 
/*  69 */     class_2338 playerPos = mc.field_1724.method_24515();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  77 */     class_2338[] checkPositions = { playerPos.method_10074(), playerPos, playerPos.method_10095(), playerPos.method_10072(), playerPos.method_10078(), playerPos.method_10067() };
/*     */ 
/*     */     
/*  80 */     boolean onShulker = false;
/*  81 */     for (class_2338 pos : checkPositions) {
/*  82 */       class_2680 state = mc.field_1687.method_8320(pos);
/*  83 */       if (state.method_26204() instanceof net.minecraft.class_2480) {
/*  84 */         onShulker = true;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*  89 */     if (onShulker) {
/*  90 */       mc.field_1724.method_18800((mc.field_1724.method_18798()).field_1352, 2.0D, (mc.field_1724.method_18798()).field_1350);
/*  91 */       mc.field_1724.method_7346();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleSlime() {
/*  96 */     double velY = (mc.field_1724.method_18798()).field_1351;
/*     */     
/*  98 */     class_2338 below = mc.field_1724.method_24515().method_10074();
/*  99 */     class_2338 belowTwo = mc.field_1724.method_24515().method_10087(2);
/*     */ 
/*     */     
/* 102 */     boolean onSlime = (mc.field_1687.method_8320(below).method_27852(class_2246.field_10030) || mc.field_1687.method_8320(belowTwo).method_27852(class_2246.field_10030));
/*     */     
/* 104 */     if (this.lastVelY < -0.1D && velY > 0.1D && onSlime && this.cooldown == 0) {
/* 105 */       double boostedVel = velY * this.slimeMultiplier.get();
/* 106 */       mc.field_1724.method_18800((mc.field_1724.method_18798()).field_1352, boostedVel, (mc.field_1724.method_18798()).field_1350);
/* 107 */       this.cooldown = 5;
/*     */     } 
/*     */     
/* 110 */     this.lastVelY = velY;
/*     */   }
/*     */   
/*     */   private void handleBoat() {
/* 114 */     boolean inBoat = mc.field_1724.method_5854() instanceof net.minecraft.class_1690;
/*     */     
/* 116 */     if (this.wasInBoat && !inBoat && this.cooldown == 0) {
/* 117 */       mc.field_1724.method_18800((mc.field_1724.method_18798()).field_1352, 1.5D, (mc.field_1724.method_18798()).field_1350);
/* 118 */       this.cooldown = 20;
/*     */     } 
/*     */     
/* 121 */     this.wasInBoat = inBoat;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\movement\HighJump.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */