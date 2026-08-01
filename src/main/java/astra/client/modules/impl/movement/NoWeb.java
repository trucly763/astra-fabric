/*     */ package shame.astra.client.modules.impl.movement;
/*     */ import java.util.Iterator;
/*     */ import net.minecraft.class_2246;
/*     */ import net.minecraft.class_2338;
/*     */ import net.minecraft.class_2350;
/*     */ import net.minecraft.class_238;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_2846;
/*     */ import net.minecraft.class_3532;
/*     */ import shame.astra.api.utils.player.MoveUtils;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.ModeSetting;
/*     */ 
/*     */ public class NoWeb extends Module {
/*  16 */   public static NoWeb INSTANCE = new NoWeb();
/*  17 */   public ModeSetting web = new ModeSetting("Мод", "Коллизия", new String[] { "Коллизия", "Обычный", "Тест" });
/*     */   
/*     */   public NoWeb() {
/*  20 */     super("NoWeb", "Убирает замедление от паутины", Module.ModuleCategory.MOVEMENT);
/*  21 */     addSettings(new Setting[] { (Setting)this.web });
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onUpdate(EventUpdate eventUpdate) {
/*  26 */     if (mc.field_1724 == null || mc.field_1687 == null)
/*     */       return; 
/*  28 */     if (this.web.is("Коллизия")) {
/*  29 */       class_2338 playerPos = mc.field_1724.method_24515();
/*     */       
/*  31 */       for (int x = -1; x <= 1; x++) {
/*  32 */         for (int y = 0; y <= 2; y++) {
/*  33 */           for (int z = -1; z <= 1; z++) {
/*  34 */             class_2338 pos = playerPos.method_10069(x, y, z);
/*     */             
/*  36 */             if (mc.field_1687.method_8320(pos).method_26204() == class_2246.field_10343) {
/*  37 */               mc.field_1724.field_3944.method_52787((class_2596)new class_2846(class_2846.class_2847.field_12973, pos, class_2350.field_11036));
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  45 */     if (this.web.is("Обычный") && (
/*  46 */       !mc.field_1724.method_5715() || !mc.field_1724.method_24828())) {
/*     */ 
/*     */ 
/*     */       
/*  50 */       boolean headInWeb = false;
/*  51 */       boolean feetInWeb = false;
/*     */       double x;
/*  53 */       for (x = -0.295D; x <= 0.295D; x += 0.05D) {
/*     */         
/*  55 */         for (double z = -0.295D; z <= 0.295D; z += 0.05D) {
/*  56 */           double y; for (y = mc.field_1724.method_5751(); y >= 0.0D; ) {
/*  57 */             class_2338 headPos = class_2338.method_49637(mc.field_1724.method_23317() + x, mc.field_1724.method_23318() + y, mc.field_1724.method_23321() + z);
/*  58 */             if (mc.field_1687.method_8320(headPos).method_26204() != class_2246.field_10343) { y -= 0.1D; continue; }
/*  59 */              headInWeb = true;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/*  65 */       if (!headInWeb)
/*     */       {
/*  67 */         for (x = -0.295D; x <= 0.295D; x += 0.05D) {
/*  68 */           for (double z = -0.295D; z <= 0.295D; ) {
/*  69 */             class_2338 pos = class_2338.method_49637(mc.field_1724.method_23317() + x, mc.field_1724.method_23318(), mc.field_1724.method_23321() + z);
/*  70 */             if (mc.field_1687.method_8320(pos).method_26204() != class_2246.field_10343) { z += 0.05D; continue; }
/*  71 */              feetInWeb = true;
/*     */           } 
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/*  77 */       class_2338 aboveHeadPos = class_2338.method_49637(mc.field_1724.method_23317(), mc.field_1724.method_23318() + mc.field_1724.method_5751() + 0.20000000298023224D, mc.field_1724.method_23321());
/*  78 */       if (!headInWeb && !feetInWeb && mc.field_1687.method_8320(aboveHeadPos).method_26204() == class_2246.field_10343) {
/*  79 */         headInWeb = true;
/*     */       }
/*     */       
/*  82 */       if (headInWeb || feetInWeb) {
/*  83 */         if (mc.field_1690.field_1903.method_1434()) {
/*  84 */           mc.field_1724.method_18800(0.0D, 0.8D, 0.0D);
/*  85 */         } else if (mc.field_1690.field_1832.method_1434()) {
/*  86 */           mc.field_1724.method_18800(0.0D, -0.8D, 0.0D);
/*     */         } else {
/*  88 */           mc.field_1724.method_18800(0.0D, 0.0D, 0.0D);
/*     */         } 
/*  90 */         MoveUtils.setMotion(0.21D);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  95 */     if (this.web.is("Тест") && 
/*  96 */       mc.field_1724 != null) {
/*  97 */       boolean cobweb = false;
/*  98 */       class_238 box = mc.field_1724.method_5829();
/*  99 */       Iterator<class_2338> it = class_2338.method_10094(class_3532.method_15357(box.field_1323), class_3532.method_15357(box.field_1322), class_3532.method_15357(box.field_1321), class_3532.method_15357(box.field_1320), class_3532.method_15357(box.field_1325), class_3532.method_15357(box.field_1324)).iterator();
/*     */       
/* 101 */       while (it.hasNext()) {
/* 102 */         class_2338 pos = it.next();
/* 103 */         if (mc.field_1687.method_8320(pos).method_27852(class_2246.field_10343)) {
/* 104 */           cobweb = true;
/*     */         }
/*     */       } 
/*     */       
/* 108 */       if (cobweb) {
/* 109 */         class_243 velocity = mc.field_1724.method_18798();
/* 110 */         float yaw = mc.field_1724.method_36454();
/* 111 */         double forward = 0.0D;
/* 112 */         double strafe = 0.0D;
/* 113 */         if (mc.field_1724.field_3913.field_54155.comp_3159()) {
/* 114 */           forward++;
/*     */         }
/*     */         
/* 117 */         if (mc.field_1724.field_3913.field_54155.comp_3160()) {
/* 118 */           forward--;
/*     */         }
/*     */         
/* 121 */         if (mc.field_1724.field_3913.field_54155.comp_3161()) {
/* 122 */           strafe++;
/*     */         }
/*     */         
/* 125 */         if (mc.field_1724.field_3913.field_54155.comp_3162()) {
/* 126 */           strafe--;
/*     */         }
/*     */         
/* 129 */         if (forward != 0.0D || strafe != 0.0D) {
/* 130 */           if (forward != 0.0D) {
/* 131 */             if (strafe > 0.0D) {
/* 132 */               yaw += ((forward > 0.0D) ? -45 : 45);
/* 133 */             } else if (strafe < 0.0D) {
/* 134 */               yaw += ((forward > 0.0D) ? 45 : -45);
/*     */             } 
/*     */             
/* 137 */             strafe = 0.0D;
/* 138 */             if (forward > 0.0D) {
/* 139 */               forward = 1.0D;
/*     */             } else {
/* 141 */               forward = -1.0D;
/*     */             } 
/*     */           } 
/*     */           
/* 145 */           double movementYaw = Math.toDegrees(Math.atan2(strafe, forward)) + yaw;
/* 146 */           yaw = (float)((movementYaw % 360.0D + 360.0D) % 360.0D);
/*     */         } 
/*     */         
/* 149 */         float result = 0.63F;
/* 150 */         if ((yaw < 313.0F || yaw > 317.0F) && (yaw < 223.0F || yaw > 227.0F) && (yaw < 133.0F || yaw > 137.0F) && (yaw < 43.0F || yaw > 47.0F)) {
/* 151 */           if ((yaw < 311.0F || yaw > 319.0F) && (yaw < 221.0F || yaw > 229.0F) && (yaw < 131.0F || yaw > 139.0F) && (yaw < 41.0F || yaw > 49.0F)) {
/* 152 */             if ((yaw < 310.8F || yaw > 320.8F) && (yaw < 220.8F || yaw > 230.8F) && (yaw < 130.8F || yaw > 140.8F) && (yaw < 40.8F || yaw > 50.8F)) {
/* 153 */               if ((yaw < 308.7F || yaw > 322.7F) && (yaw < 218.7F || yaw > 232.7F) && (yaw < 128.7F || yaw > 142.7F) && (yaw < 38.7F || yaw > 52.7F)) {
/* 154 */                 if ((yaw < 306.5F || yaw > 324.5F) && (yaw < 216.5F || yaw > 234.5F) && (yaw < 126.5F || yaw > 144.5F) && (yaw < 36.5F || yaw > 54.5F)) {
/* 155 */                   if ((yaw >= 304.0F && yaw <= 327.0F) || (yaw >= 214.0F && yaw <= 237.0F) || (yaw >= 124.0F && yaw <= 147.0F) || (yaw >= 34.0F && yaw <= 57.0F)) {
/* 156 */                     result = 0.75F;
/*     */                   }
/*     */                 } else {
/* 159 */                   result = 0.79F;
/*     */                 } 
/*     */               } else {
/* 162 */                 result = 0.81F;
/*     */               } 
/*     */             } else {
/* 165 */               result = 0.83F;
/*     */             } 
/*     */           } else {
/* 168 */             result = 0.85F;
/*     */           } 
/*     */         } else {
/* 171 */           result = 0.88F;
/*     */         } 
/*     */         
/* 174 */         if (!mc.field_1690.field_1903.method_1434()) {
/* 175 */           if (mc.field_1690.field_1832.method_1434()) {
/* 176 */             mc.field_1724.method_18800(velocity.field_1352, -3.6D, velocity.field_1350);
/*     */           } else {
/* 178 */             mc.field_1724.method_18800(velocity.field_1352, 0.0D, velocity.field_1350);
/*     */           } 
/*     */         } else {
/* 181 */           mc.field_1724.method_18800(velocity.field_1352, (forward == 0.0D && strafe == 0.0D) ? 1.4D : 1.2D, velocity.field_1350);
/*     */         } 
/*     */         
/* 184 */         MoveUtils.setVelocity(result);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\movement\NoWeb.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */