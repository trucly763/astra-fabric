/*     */ package shame.astra.client.modules.impl.combat;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import net.minecraft.class_1297;
/*     */ import net.minecraft.class_1802;
/*     */ import net.minecraft.class_239;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_3959;
/*     */ import net.minecraft.class_3965;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.EventUpdate;
/*     */ import shame.astra.api.utils.player.InventoryUtils;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*     */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*     */ 
/*     */ public class ElytraResolver extends Module {
/*  21 */   public static ElytraResolver INSTANCE = new ElytraResolver();
/*     */   
/*  23 */   private final FloatSetting distance = new FloatSetting("Дистанция отлета", 6.0F, 4.0F, 8.0F, 0.1F);
/*  24 */   private final BooleanSetting autoFirework = new BooleanSetting("Авто-Фейерверк", true);
/*     */   
/*     */   private static final float MIN_HEIGHT = 4.0F;
/*     */   
/*     */   private boolean escaping;
/*     */   private class_243 escapePos;
/*     */   private long escapeStartTime;
/*  31 */   private int returnFireworkTicks = -1;
/*     */   private class_243 lastEscapeDirection;
/*     */   
/*     */   public ElytraResolver() {
/*  35 */     super("ElytraResolver", "Отлет на элитрах", Module.ModuleCategory.COMBAT);
/*  36 */     addSettings(new Setting[] { (Setting)this.distance, (Setting)this.autoFirework });
/*  37 */     INSTANCE = this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  42 */     super.onDisable();
/*  43 */     this.escaping = false;
/*  44 */     this.escapePos = null;
/*  45 */     this.returnFireworkTicks = -1;
/*  46 */     this.lastEscapeDirection = null;
/*     */   }
/*     */   
/*     */   public void onAuraAttack() {
/*  50 */     if (!isEnable() || mc.field_1724 == null || !mc.field_1724.method_6128())
/*     */       return; 
/*  52 */     class_243 bestPos = calculateSmartEscape(mc.field_1724.method_19538(), this.distance.get());
/*  53 */     if (bestPos != null) {
/*  54 */       this.escapePos = bestPos;
/*  55 */       this.escaping = true;
/*  56 */       this.escapeStartTime = System.currentTimeMillis();
/*     */       
/*  58 */       if (this.autoFirework.isState()) {
/*  59 */         useFirework();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onUpdate(EventUpdate event) {
/*  66 */     if (mc.field_1724 == null || mc.field_1687 == null || !mc.field_1724.method_6128()) {
/*  67 */       this.escaping = false;
/*  68 */       this.returnFireworkTicks = -1;
/*     */       
/*     */       return;
/*     */     } 
/*  72 */     if (this.returnFireworkTicks > 0) {
/*  73 */       this.returnFireworkTicks--;
/*  74 */     } else if (this.returnFireworkTicks == 0) {
/*  75 */       if (this.autoFirework.isState()) {
/*  76 */         useFirework();
/*     */       }
/*  78 */       this.returnFireworkTicks = -1;
/*     */     } 
/*     */     
/*  81 */     if (this.escaping && this.escapePos != null) {
/*  82 */       double dist = mc.field_1724.method_19538().method_1022(this.escapePos);
/*  83 */       if (dist < 2.0D || System.currentTimeMillis() - this.escapeStartTime > 1000L) {
/*  84 */         this.escaping = false;
/*  85 */         if (this.autoFirework.isState()) {
/*  86 */           this.returnFireworkTicks = 2;
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isEscaping() {
/*  93 */     return (isEnable() && this.escaping && this.escapePos != null && mc.field_1724 != null && mc.field_1724.method_6128());
/*     */   }
/*     */   
/*     */   public class_243 getEscapePos() {
/*  97 */     return this.escapePos;
/*     */   }
/*     */   
/*     */   private class_243 calculateSmartEscape(class_243 pPos, float d) {
/* 101 */     class_243 playerLook = mc.field_1724.method_5720();
/* 102 */     class_243 playerVelocity = mc.field_1724.method_18798();
/*     */     
/* 104 */     class_243[] directions = generateSmartDirections(playerLook, playerVelocity);
/* 105 */     List<EscapePoint> validPoints = new ArrayList<>();
/*     */     
/* 107 */     for (class_243 dir : directions) {
/* 108 */       class_243 target = pPos.method_1019(dir.method_1021(d));
/*     */       
/* 110 */       if (target.field_1351 < pPos.field_1351 + 4.0D) {
/*     */         continue;
/*     */       }
/*     */       
/* 114 */       class_3959 context = new class_3959(pPos, target, class_3959.class_3960.field_17558, class_3959.class_242.field_1348, (class_1297)mc.field_1724);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 121 */       class_3965 hit = mc.field_1687.method_17742(context);
/* 122 */       double actualDistance = d;
/* 123 */       class_243 finalPos = target;
/*     */       
/* 125 */       if (hit.method_17783() != class_239.class_240.field_1333) {
/* 126 */         double hitDist = hit.method_17784().method_1022(pPos);
/* 127 */         if (hitDist > 2.0D) {
/* 128 */           actualDistance = hitDist;
/* 129 */           finalPos = hit.method_17784().method_1019(dir.method_1021(-1.0D));
/*     */         } else {
/*     */           continue;
/*     */         } 
/*     */       } 
/*     */       
/* 135 */       double score = calculateEscapeScore(dir, playerLook, playerVelocity, actualDistance, finalPos);
/* 136 */       validPoints.add(new EscapePoint(finalPos, actualDistance, score));
/*     */       continue;
/*     */     } 
/* 139 */     if (validPoints.isEmpty()) return null;
/*     */     
/* 141 */     validPoints.sort(Comparator.comparingDouble(p -> -p.score));
/* 142 */     this.lastEscapeDirection = ((EscapePoint)validPoints.get(0)).pos.method_1020(pPos).method_1029();
/* 143 */     return ((EscapePoint)validPoints.get(0)).pos;
/*     */   }
/*     */   
/*     */   private class_243[] generateSmartDirections(class_243 playerLook, class_243 velocity) {
/* 147 */     class_243 back = (new class_243(-playerLook.field_1352, 0.0D, -playerLook.field_1350)).method_1029();
/* 148 */     class_243 right = (new class_243(-playerLook.field_1350, 0.0D, playerLook.field_1352)).method_1029();
/* 149 */     class_243 left = right.method_1021(-1.0D);
/* 150 */     class_243 up = new class_243(0.0D, 1.0D, 0.0D);
/*     */     
/* 152 */     List<class_243> dirs = new ArrayList<>();
/*     */     
/* 154 */     dirs.add(back.method_1019(up).method_1029());
/* 155 */     dirs.add(back.method_1019(right).method_1019(up).method_1029());
/* 156 */     dirs.add(back.method_1019(left).method_1019(up).method_1029());
/* 157 */     dirs.add(right.method_1019(up).method_1029());
/* 158 */     dirs.add(left.method_1019(up).method_1029());
/* 159 */     dirs.add(back.method_1019(right.method_1021(0.5D)).method_1019(up.method_1021(1.5D)).method_1029());
/* 160 */     dirs.add(back.method_1019(left.method_1021(0.5D)).method_1019(up.method_1021(1.5D)).method_1029());
/* 161 */     dirs.add(back.method_1019(up.method_1021(2.0D)).method_1029());
/* 162 */     dirs.add(right.method_1019(up.method_1021(1.5D)).method_1029());
/* 163 */     dirs.add(left.method_1019(up.method_1021(1.5D)).method_1029());
/* 164 */     dirs.add(back.method_1021(0.7D).method_1019(right.method_1021(0.3D)).method_1019(up.method_1021(1.2D)).method_1029());
/* 165 */     dirs.add(back.method_1021(0.7D).method_1019(left.method_1021(0.3D)).method_1019(up.method_1021(1.2D)).method_1029());
/* 166 */     dirs.add(back.method_1021(0.5D).method_1019(up.method_1021(1.8D)).method_1029());
/* 167 */     dirs.add(right.method_1021(0.8D).method_1019(up.method_1021(1.3D)).method_1029());
/* 168 */     dirs.add(left.method_1021(0.8D).method_1019(up.method_1021(1.3D)).method_1029());
/*     */     
/* 170 */     if (velocity.method_1027() > 0.01D) {
/* 171 */       class_243 perpendicular = (new class_243(-velocity.field_1350, 0.0D, velocity.field_1352)).method_1029();
/* 172 */       dirs.add(perpendicular.method_1019(up).method_1029());
/* 173 */       dirs.add(perpendicular.method_1021(-1.0D).method_1019(up).method_1029());
/* 174 */       dirs.add(perpendicular.method_1019(up.method_1021(1.5D)).method_1029());
/* 175 */       dirs.add(perpendicular.method_1021(-1.0D).method_1019(up.method_1021(1.5D)).method_1029());
/*     */     } 
/*     */     
/* 178 */     return dirs.<class_243>toArray(new class_243[0]);
/*     */   }
/*     */   
/*     */   private double calculateEscapeScore(class_243 direction, class_243 playerLook, class_243 velocity, double distance, class_243 finalPos) {
/* 182 */     double score = 0.0D;
/*     */     
/* 184 */     double backwardBonus = -direction.method_1026((new class_243(playerLook.field_1352, 0.0D, playerLook.field_1350)).method_1029());
/* 185 */     score += backwardBonus * 30.0D;
/*     */     
/* 187 */     score += direction.field_1351 * 25.0D;
/*     */     
/* 189 */     score += distance * 2.0D;
/*     */     
/* 191 */     if (velocity.method_1027() > 0.01D) {
/* 192 */       class_243 velNorm = velocity.method_1029();
/* 193 */       double perpendicular = Math.abs(direction.method_1026(new class_243(-velNorm.field_1350, 0.0D, velNorm.field_1352)));
/* 194 */       score += perpendicular * 15.0D;
/*     */     } 
/*     */     
/* 197 */     if (this.lastEscapeDirection != null) {
/* 198 */       double similarity = direction.method_1026(this.lastEscapeDirection);
/* 199 */       if (similarity > 0.7D) {
/* 200 */         score -= 20.0D;
/*     */       }
/*     */     } 
/*     */     
/* 204 */     double groundDistance = finalPos.field_1351 - mc.field_1687.method_31607();
/* 205 */     if (groundDistance < 10.0D) {
/* 206 */       score -= (10.0D - groundDistance) * 5.0D;
/*     */     }
/*     */     
/* 209 */     return score;
/*     */   }
/*     */   
/*     */   private void useFirework() {
/* 213 */     if (mc.field_1724 == null)
/* 214 */       return;  int slotFirework = InventoryUtils.getItemSlot(class_1802.field_8639);
/* 215 */     if (slotFirework != -1)
/* 216 */       InventoryUtils.swapAndUseHvH(class_1802.field_8639); 
/*     */   }
/*     */   
/*     */   private static class EscapePoint
/*     */   {
/*     */     class_243 pos;
/*     */     double distance;
/*     */     double score;
/*     */     
/*     */     EscapePoint(class_243 pos, double distance, double score) {
/* 226 */       this.pos = pos;
/* 227 */       this.distance = distance;
/* 228 */       this.score = score;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\combat\ElytraResolver.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */