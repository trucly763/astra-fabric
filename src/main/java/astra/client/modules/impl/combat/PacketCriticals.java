/*    */ package shame.astra.client.modules.impl.combat;
/*    */ 
/*    */ import net.minecraft.class_2596;
/*    */ import net.minecraft.class_2828;
/*    */ import shame.astra.api.events.EventLink;
/*    */ import shame.astra.api.events.implement.EventAttackEntity;
/*    */ import shame.astra.api.utils.combat.IdealHitUtils;
/*    */ import shame.astra.client.modules.Module;
/*    */ 
/*    */ public class PacketCriticals
/*    */   extends Module {
/* 12 */   public static PacketCriticals INSTANCE = new PacketCriticals();
/*    */   
/*    */   public PacketCriticals() {
/* 15 */     super("PacketCriticals", "Бьет критами под эффект плавного падения / в паутине", Module.ModuleCategory.COMBAT);
/*    */   }
/*    */ 
/*    */   
/*    */   @EventLink
/*    */   public void onAttack(EventAttackEntity event) {
/* 21 */     if (mc.field_1724 == null || mc.field_1687 == null)
/*    */       return; 
/* 23 */     boolean inWeb = IdealHitUtils.isInCobweb();
/*    */     
/* 25 */     double x = mc.field_1724.method_23317();
/* 26 */     double y = mc.field_1724.method_23318();
/* 27 */     double z = mc.field_1724.method_23321();
/* 28 */     if (inWeb) {
/* 29 */       mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(x, y + 0.003D, z, false, false));
/* 30 */       mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(x, y, z, false, false));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\combat\PacketCriticals.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */