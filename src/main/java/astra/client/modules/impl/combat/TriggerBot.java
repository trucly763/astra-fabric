/*     */ package shame.astra.client.modules.impl.combat;
/*     */ 
/*     */ import lombok.Generated;
/*     */ import net.minecraft.class_1268;
/*     */ import net.minecraft.class_1294;
/*     */ import net.minecraft.class_1297;
/*     */ import net.minecraft.class_1309;
/*     */ import net.minecraft.class_1657;
/*     */ import net.minecraft.class_1675;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_3966;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.EventMoveInput;
/*     */ import shame.astra.api.events.implement.EventUpdate;
/*     */ import shame.astra.api.utils.combat.IdealHitUtils;
/*     */ import shame.astra.api.utils.math.TimerUtils;
/*     */ import shame.astra.astra;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.impl.movement.Sprint;
/*     */ import shame.astra.client.modules.impl.player.AutoEat;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*     */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*     */ import shame.astra.client.modules.settings.implement.ListSetting;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TriggerBot
/*     */   extends Module
/*     */ {
/*  32 */   public static TriggerBot INSTANCE = new TriggerBot();
/*     */   
/*  34 */   private final FloatSetting range = new FloatSetting("Дистанция атаки", 3.0F, 0.0F, 6.0F, 0.05F);
/*     */   
/*  36 */   private final ListSetting options = new ListSetting("Опции", new BooleanSetting[] { new BooleanSetting("Умные криты", true), new BooleanSetting("Сброс спринта", true), new BooleanSetting("Бить через стены", false), new BooleanSetting("Проверка на наведение", true), new BooleanSetting("Отжимать щит", false), new BooleanSetting("Ломать щит", true) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  45 */   private final ListSetting targets = new ListSetting("Таргеты", new BooleanSetting[] { new BooleanSetting("Игроки", true), new BooleanSetting("Невидимки", true), new BooleanSetting("Мирные", false), new BooleanSetting("Мобы", true) });
/*     */   
/*     */   private class_1309 target;
/*     */ 
/*     */   
/*     */   @Generated
/*     */   public class_1309 getTarget() {
/*  52 */     return this.target;
/*     */   }
/*     */   
/*  55 */   private final TimerUtils attackTimer = new TimerUtils();
/*     */   
/*     */   private boolean needSprintReset = false;
/*     */   private boolean sprintResetDone = false;
/*  59 */   private int sprintResetTicks = 0;
/*     */   
/*     */   public TriggerBot() {
/*  62 */     super("TriggerBot", "Автоматически атакует при наведении на цель", Module.ModuleCategory.COMBAT);
/*  63 */     addSettings(new Setting[] { (Setting)this.range, (Setting)this.options, (Setting)this.targets });
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onMoveInput(EventMoveInput event) {
/*  68 */     if (this.needSprintReset) {
/*  69 */       event.setForward(0.0F);
/*  70 */       event.setStrafe(0.0F);
/*  71 */       this.needSprintReset = false;
/*  72 */       this.sprintResetDone = true;
/*  73 */       this.sprintResetTicks = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @EventLink
/*     */   public void onUpdate(EventUpdate e) {
/*  80 */     if (mc.field_1724 == null || mc.field_1687 == null)
/*  81 */       return;  if (AutoEat.shouldSuppressCombat()) {
/*  82 */       this.target = null;
/*  83 */       resetSprintState();
/*     */       
/*     */       return;
/*     */     } 
/*  87 */     if (this.sprintResetDone) {
/*  88 */       this.sprintResetTicks++;
/*     */     }
/*     */     
/*  91 */     this.target = getTargetUnderCrosshair();
/*     */     
/*  93 */     if (this.target != null) {
/*  94 */       processAttack();
/*     */     } else {
/*  96 */       resetSprintState();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void processAttack() {
/* 101 */     if (!shouldAttack())
/*     */       return; 
/* 103 */     if (this.options.is("Сброс спринта") && mc.field_1724.method_5624() && !this.sprintResetDone && !shouldSkipSprintResetInWater()) {
/* 104 */       this.needSprintReset = true;
/*     */       
/*     */       return;
/*     */     } 
/* 108 */     if (this.options.is("Сброс спринта") && this.sprintResetDone && this.sprintResetTicks < 1) {
/*     */       return;
/*     */     }
/*     */     
/* 112 */     attack();
/* 113 */     this.sprintResetDone = false;
/* 114 */     this.sprintResetTicks = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   private class_1309 getTargetUnderCrosshair() {
/* 119 */     class_243 eyePos = mc.field_1724.method_5836(1.0F);
/* 120 */     class_243 lookVec = mc.field_1724.method_5828(1.0F);
/* 121 */     float rangeValue = this.range.getValue().floatValue();
/* 122 */     class_243 reachVec = eyePos.method_1019(lookVec.method_1021(rangeValue));
/*     */     
/* 124 */     class_3966 result = class_1675.method_18075((class_1297)mc.field_1724, eyePos, reachVec, mc.field_1724
/*     */ 
/*     */ 
/*     */         
/* 128 */         .method_5829().method_1014(rangeValue), entity -> 
/* 129 */         (entity != mc.field_1724 && entity.method_5805() && entity instanceof class_1309), (rangeValue * rangeValue));
/*     */ 
/*     */ 
/*     */     
/* 133 */     if (result != null) { class_1297 class_1297 = result.method_17782(); if (class_1297 instanceof class_1309) { class_1309 living = (class_1309)class_1297;
/* 134 */         if (isValidTarget(living)) {
/* 135 */           return living;
/*     */         } }
/*     */        }
/*     */     
/* 139 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void attack() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield options : Lshame/astra/client/modules/settings/implement/ListSetting;
/*     */     //   4: ldc 'Отжимать щит'
/*     */     //   6: invokevirtual is : (Ljava/lang/String;)Z
/*     */     //   9: ifeq -> 39
/*     */     //   12: getstatic shame/astra/client/modules/impl/combat/TriggerBot.mc : Lnet/minecraft/class_310;
/*     */     //   15: getfield field_1724 : Lnet/minecraft/class_746;
/*     */     //   18: invokevirtual method_6039 : ()Z
/*     */     //   21: ifeq -> 39
/*     */     //   24: getstatic shame/astra/client/modules/impl/combat/TriggerBot.mc : Lnet/minecraft/class_310;
/*     */     //   27: getfield field_1761 : Lnet/minecraft/class_636;
/*     */     //   30: getstatic shame/astra/client/modules/impl/combat/TriggerBot.mc : Lnet/minecraft/class_310;
/*     */     //   33: getfield field_1724 : Lnet/minecraft/class_746;
/*     */     //   36: invokevirtual method_2897 : (Lnet/minecraft/class_1657;)V
/*     */     //   39: aload_0
/*     */     //   40: getfield target : Lnet/minecraft/class_1309;
/*     */     //   43: astore_2
/*     */     //   44: aload_2
/*     */     //   45: instanceof net/minecraft/class_1657
/*     */     //   48: ifeq -> 83
/*     */     //   51: aload_2
/*     */     //   52: checkcast net/minecraft/class_1657
/*     */     //   55: astore_1
/*     */     //   56: aload_1
/*     */     //   57: invokevirtual method_6039 : ()Z
/*     */     //   60: ifeq -> 83
/*     */     //   63: aload_0
/*     */     //   64: getfield options : Lshame/astra/client/modules/settings/implement/ListSetting;
/*     */     //   67: ldc 'Ломать щит'
/*     */     //   69: invokevirtual is : (Ljava/lang/String;)Z
/*     */     //   72: ifeq -> 83
/*     */     //   75: aload_0
/*     */     //   76: aload_1
/*     */     //   77: invokevirtual shieldBreak : (Lnet/minecraft/class_1657;)V
/*     */     //   80: goto -> 102
/*     */     //   83: getstatic shame/astra/client/modules/impl/combat/TriggerBot.mc : Lnet/minecraft/class_310;
/*     */     //   86: getfield field_1761 : Lnet/minecraft/class_636;
/*     */     //   89: getstatic shame/astra/client/modules/impl/combat/TriggerBot.mc : Lnet/minecraft/class_310;
/*     */     //   92: getfield field_1724 : Lnet/minecraft/class_746;
/*     */     //   95: aload_0
/*     */     //   96: getfield target : Lnet/minecraft/class_1309;
/*     */     //   99: invokevirtual method_2918 : (Lnet/minecraft/class_1657;Lnet/minecraft/class_1297;)V
/*     */     //   102: getstatic shame/astra/client/modules/impl/combat/TriggerBot.mc : Lnet/minecraft/class_310;
/*     */     //   105: getfield field_1724 : Lnet/minecraft/class_746;
/*     */     //   108: getstatic net/minecraft/class_1268.field_5808 : Lnet/minecraft/class_1268;
/*     */     //   111: invokevirtual method_6104 : (Lnet/minecraft/class_1268;)V
/*     */     //   114: aload_0
/*     */     //   115: getfield attackTimer : Lshame/astra/api/utils/math/TimerUtils;
/*     */     //   118: invokevirtual reset : ()V
/*     */     //   121: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #144	-> 0
/*     */     //   #145	-> 24
/*     */     //   #148	-> 39
/*     */     //   #149	-> 75
/*     */     //   #151	-> 83
/*     */     //   #154	-> 102
/*     */     //   #155	-> 114
/*     */     //   #156	-> 121
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   56	27	1	player	Lnet/minecraft/class_1657;
/*     */     //   0	122	0	this	Lshame/astra/client/modules/impl/combat/TriggerBot;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void shieldBreak(class_1657 entity) {
/* 160 */     int axeSlot = findAxeSlot();
/*     */     
/* 162 */     if (axeSlot != -1) {
/* 163 */       int prevSlot = (mc.field_1724.method_31548()).field_7545;
/* 164 */       (mc.field_1724.method_31548()).field_7545 = axeSlot;
/* 165 */       mc.field_1761.method_2918((class_1657)mc.field_1724, (class_1297)entity);
/* 166 */       mc.field_1724.method_6104(class_1268.field_5808);
/* 167 */       (mc.field_1724.method_31548()).field_7545 = prevSlot;
/*     */     } else {
/* 169 */       mc.field_1761.method_2918((class_1657)mc.field_1724, (class_1297)entity);
/*     */     } 
/*     */   }
/*     */   
/*     */   private int findAxeSlot() {
/* 174 */     for (int i = 0; i < 9; i++) {
/* 175 */       if (mc.field_1724.method_31548().method_5438(i).method_7909() instanceof net.minecraft.class_1743) {
/* 176 */         return i;
/*     */       }
/*     */     } 
/* 179 */     return -1;
/*     */   }
/*     */   
/*     */   private boolean isValidTarget(class_1309 entity) {
/* 183 */     if (entity == null || entity == mc.field_1724) return false; 
/* 184 */     if (!entity.method_5805() || entity.method_6032() <= 0.0F) return false; 
/* 185 */     if (entity instanceof net.minecraft.class_1531) return false;
/*     */     
/* 187 */     if (entity instanceof class_1657) { class_1657 player = (class_1657)entity;
/* 188 */       if (!this.targets.is("Игроки")) return false; 
/* 189 */       if (player.method_6059(class_1294.field_5905) && !this.targets.is("Невидимки")) return false; 
/* 190 */       if (astra.INSTANCE.friendStorage.isFriend(entity.method_5477().getString())) return false;  }
/* 191 */     else if (entity instanceof net.minecraft.class_1296 || entity instanceof net.minecraft.class_1431)
/* 192 */     { if (!this.targets.is("Мирные")) return false;  }
/* 193 */     else if (entity instanceof net.minecraft.class_1588 && 
/* 194 */       !this.targets.is("Мобы")) { return false; }
/*     */ 
/*     */     
/* 197 */     if (mc.field_1724.method_33571().method_1022(entity.method_5829().method_1005()) > this.range.getValue().floatValue()) {
/* 198 */       return false;
/*     */     }
/*     */     
/* 201 */     if (!this.options.is("Бить через стены") && !mc.field_1724.method_6057((class_1297)entity)) {
/* 202 */       return false;
/*     */     }
/*     */     
/* 205 */     return true;
/*     */   }
/*     */   
/*     */   private boolean shouldAttack() {
/* 209 */     if (mc.field_1724.method_7261(1.5F) < IdealHitUtils.getAICooldown()) {
/* 210 */       return false;
/*     */     }
/*     */     
/* 213 */     if (this.options.is("Проверка на наведение")) {
/* 214 */       class_243 eyePos = mc.field_1724.method_5836(1.0F);
/* 215 */       class_243 lookVec = mc.field_1724.method_5828(1.0F);
/* 216 */       float rangeValue = this.range.getValue().floatValue();
/* 217 */       class_243 reachVec = eyePos.method_1019(lookVec.method_1021(rangeValue));
/*     */       
/* 219 */       class_3966 result = class_1675.method_18075((class_1297)mc.field_1724, eyePos, reachVec, mc.field_1724
/*     */ 
/*     */ 
/*     */           
/* 223 */           .method_5829().method_1014(rangeValue), ex -> 
/* 224 */           (ex != mc.field_1724 && ex.method_5805()), (rangeValue * rangeValue));
/*     */ 
/*     */ 
/*     */       
/* 228 */       if (result == null || result.method_17782() != this.target) {
/* 229 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 233 */     if (this.options.is("Умные криты") && !IdealHitUtils.canCritical(this.target)) {
/* 234 */       return false;
/*     */     }
/*     */     
/* 237 */     return true;
/*     */   }
/*     */   
/*     */   private void resetSprintState() {
/* 241 */     this.sprintResetDone = false;
/* 242 */     this.sprintResetTicks = 0;
/*     */   }
/*     */   
/*     */   private boolean shouldSkipSprintResetInWater() {
/* 246 */     return (mc.field_1724 != null && (mc.field_1724
/* 247 */       .method_5799() || mc.field_1724.method_5869()) && Sprint.INSTANCE != null && Sprint.INSTANCE
/*     */       
/* 249 */       .shouldKeepSprintInWater());
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 254 */     super.onDisable();
/* 255 */     this.target = null;
/* 256 */     this.needSprintReset = false;
/* 257 */     this.sprintResetDone = false;
/* 258 */     this.sprintResetTicks = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/* 263 */     super.onEnable();
/* 264 */     this.needSprintReset = false;
/* 265 */     this.sprintResetDone = false;
/* 266 */     this.sprintResetTicks = 0;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\combat\TriggerBot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */