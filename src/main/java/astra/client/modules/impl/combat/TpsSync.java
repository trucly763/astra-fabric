/*    */ package shame.astra.client.modules.impl.combat;
/*    */ 
/*    */ import net.minecraft.class_3532;
/*    */ import shame.astra.astra;
/*    */ import shame.astra.client.modules.Module;
/*    */ 
/*    */ public class TpsSync
/*    */   extends Module {
/*  9 */   public static TpsSync INSTANCE = new TpsSync();
/*    */   
/*    */   public TpsSync() {
/* 12 */     super("TpsSync", "Синхронизация с TPS сервера", Module.ModuleCategory.COMBAT);
/*    */   }
/*    */   
/*    */   public float getCurrentTPS() {
/* 16 */     if (astra.INSTANCE == null || astra.INSTANCE.tpsCalc == null) {
/* 17 */       return 20.0F;
/*    */     }
/* 19 */     float tps = astra.INSTANCE.tpsCalc.getTPS();
/* 20 */     return class_3532.method_15363(tps, 0.1F, 20.0F);
/*    */   }
/*    */   
/*    */   public long getAdjustedCooldown(long baseCooldown) {
/* 24 */     if (!isEnable()) {
/* 25 */       return baseCooldown;
/*    */     }
/*    */     
/* 28 */     float tps = getCurrentTPS();
/* 29 */     if (tps >= 20.0F) {
/* 30 */       return baseCooldown;
/*    */     }
/*    */     
/* 33 */     float multiplier = 20.0F / tps;
/* 34 */     float additionalFactor = 1.0F + (20.0F - tps) * 0.05F;
/* 35 */     long adjusted = (long)((float)baseCooldown * multiplier * additionalFactor);
/*    */     
/* 37 */     return Math.min(adjusted, 3000L);
/*    */   }
/*    */   
/*    */   public boolean canAttack(long lastAttackTime, long baseCooldown, long currentTime) {
/* 41 */     if (!isEnable()) {
/* 42 */       return (currentTime >= lastAttackTime + baseCooldown);
/*    */     }
/*    */     
/* 45 */     long adjustedCooldown = getAdjustedCooldown(baseCooldown);
/* 46 */     return (currentTime >= lastAttackTime + adjustedCooldown);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\combat\TpsSync.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */