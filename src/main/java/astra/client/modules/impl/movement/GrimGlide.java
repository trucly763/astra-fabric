/*    */ package shame.astra.client.modules.impl.movement;
/*    */ 
/*    */ import java.util.concurrent.ThreadLocalRandom;
/*    */ import net.minecraft.class_243;
/*    */ import shame.astra.api.events.EventLink;
/*    */ import shame.astra.api.events.implement.EventUpdate;
/*    */ import shame.astra.client.modules.Module;
/*    */ 
/*    */ 
/*    */ public class GrimGlide
/*    */   extends Module
/*    */ {
/* 13 */   public static GrimGlide INSTANCE = new GrimGlide();
/*    */   
/* 15 */   private long lastTickTime = 0L;
/* 16 */   private int ticksTwo = 0;
/*    */   
/*    */   public GrimGlide() {
/* 19 */     super("GrimGlide", "Ускорение на элитре без фееров", Module.ModuleCategory.MOVEMENT);
/*    */   }
/*    */ 
/*    */   
/*    */   @EventLink
/*    */   public void onUpdate(EventUpdate event) {
/* 25 */     if (mc.field_1724 == null || mc.field_1687 == null || !mc.field_1724.method_6128())
/*    */       return; 
/* 27 */     this.ticksTwo++;
/* 28 */     class_243 pos = mc.field_1724.method_19538();
/* 29 */     float yaw = mc.field_1724.method_36454();
/* 30 */     double forward = (mc.field_1724.field_6012 % 2 == 0) ? 0.087D : 0.09D;
/*    */     
/* 32 */     double dx = -Math.sin(Math.toRadians(yaw)) * forward;
/* 33 */     double dz = Math.cos(Math.toRadians(yaw)) * forward;
/*    */     
/* 35 */     if (System.currentTimeMillis() - this.lastTickTime >= 40L) {
/* 36 */       mc.field_1724.method_5814(pos.method_10216() + dx, pos.method_10214(), pos.method_10215() + dz);
/* 37 */       this.lastTickTime = System.currentTimeMillis();
/*    */     } 
/*    */     
/* 40 */     if (this.ticksTwo % 40 == 0) {
/* 41 */       mc.field_1724.method_18800(dx * 
/* 42 */           ThreadLocalRandom.current().nextFloat(1.001F, 1.0021F), 
/* 43 */           (mc.field_1724.method_18798()).field_1351 + 0.00600000075995922D, dz * 
/* 44 */           ThreadLocalRandom.current().nextFloat(1.001F, 1.0021F));
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 51 */     super.onEnable();
/* 52 */     this.ticksTwo = 0;
/* 53 */     this.lastTickTime = System.currentTimeMillis();
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 58 */     this.ticksTwo = 0;
/* 59 */     super.onDisable();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\movement\GrimGlide.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */