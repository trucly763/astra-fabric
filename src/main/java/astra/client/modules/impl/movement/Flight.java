/*    */ package shame.astra.client.modules.impl.movement;
/*    */ 
/*    */ import net.minecraft.class_243;
/*    */ import shame.astra.api.events.EventLink;
/*    */ import shame.astra.api.events.implement.EventUpdate;
/*    */ import shame.astra.client.modules.Module;
/*    */ import shame.astra.client.modules.settings.Setting;
/*    */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*    */ 
/*    */ public class Flight
/*    */   extends Module {
/* 12 */   public static Flight INSTANCE = new Flight();
/*    */   
/* 14 */   private final FloatSetting speed = new FloatSetting("Скорость", 2.0F, 0.1F, 10.0F, 0.1F);
/*    */   
/*    */   public Flight() {
/* 17 */     super("Flight", "Полёт", Module.ModuleCategory.MOVEMENT);
/* 18 */     addSettings(new Setting[] { (Setting)this.speed });
/*    */   }
/*    */ 
/*    */   
/*    */   @EventLink
/*    */   public void onUpdate(EventUpdate event) {
/* 24 */     if (mc.field_1724 == null)
/*    */       return; 
/* 26 */     double spd = this.speed.get();
/* 27 */     float yaw = (float)Math.toRadians(mc.field_1724.method_36454());
/*    */     
/* 29 */     double motionX = 0.0D;
/* 30 */     double motionY = 0.0D;
/* 31 */     double motionZ = 0.0D;
/*    */     
/* 33 */     double forward = 0.0D;
/* 34 */     double strafe = 0.0D;
/*    */     
/* 36 */     if (mc.field_1690.field_1894.method_1434()) forward++; 
/* 37 */     if (mc.field_1690.field_1881.method_1434()) forward--; 
/* 38 */     if (mc.field_1690.field_1913.method_1434()) strafe++; 
/* 39 */     if (mc.field_1690.field_1849.method_1434()) strafe--;
/*    */     
/* 41 */     if (forward != 0.0D || strafe != 0.0D) {
/* 42 */       double angle = Math.atan2(forward, strafe) - 1.5707963267948966D;
/* 43 */       motionX = -Math.sin(yaw + angle) * spd;
/* 44 */       motionZ = Math.cos(yaw + angle) * spd;
/*    */     } 
/*    */     
/* 47 */     if (mc.field_1690.field_1903.method_1434()) {
/* 48 */       motionY = spd;
/* 49 */     } else if (mc.field_1690.field_1832.method_1434()) {
/* 50 */       motionY = -spd;
/*    */     } 
/*    */     
/* 53 */     mc.field_1724.method_18799(new class_243(motionX, motionY, motionZ));
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 58 */     super.onDisable();
/* 59 */     if (mc.field_1724 != null)
/* 60 */       mc.field_1724.method_18799(class_243.field_1353); 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\movement\Flight.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */