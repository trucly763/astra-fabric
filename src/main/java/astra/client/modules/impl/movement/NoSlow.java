/*    */ package shame.astra.client.modules.impl.movement;
/*    */ import net.minecraft.class_1268;
/*    */ import net.minecraft.class_2596;
/*    */ import net.minecraft.class_2886;
/*    */ import shame.astra.api.events.EventLink;
/*    */ import shame.astra.api.events.implement.EventSlowWalking;
/*    */ import shame.astra.api.storages.implement.helpertstorages.enumvar.ModuleClass;
/*    */ import shame.astra.api.utils.player.ViaProtocolUtils;
/*    */ import shame.astra.client.modules.Module;
/*    */ import shame.astra.client.modules.settings.Setting;
/*    */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*    */ import shame.astra.client.modules.settings.implement.ModeSetting;
/*    */ 
/*    */ public class NoSlow extends Module {
/* 15 */   public static NoSlow INSTANCE = new NoSlow();
/*    */   
/* 17 */   private final ModeSetting mode = new ModeSetting("Мод", "Grim Old", new String[] { "Grim Old", "Grim Last" });
/* 18 */   private final BooleanSetting sprint = new BooleanSetting("Спринт", true);
/*    */   
/*    */   public NoSlow() {
/* 21 */     super("NoSlow", "Убирает замедление во время еды", Module.ModuleCategory.MOVEMENT);
/* 22 */     addSettings(new Setting[] { (Setting)this.mode, (Setting)this.sprint });
/*    */   }
/*    */   
/*    */   @EventLink
/*    */   public void onSlowDown(EventSlowWalking event) {
/* 27 */     if (mc.field_1724 == null || !mc.field_1724.method_6115())
/*    */       return; 
/* 29 */     if (this.mode.is("Grim Last") && 
/* 30 */       mc.field_1724.method_6048() % 2 == 0) {
/* 31 */       event.setCancelled(true);
/*    */     }
/*    */ 
/*    */     
/* 35 */     if (this.mode.is("Grim Old")) {
/* 36 */       class_1268 activeHand = mc.field_1724.method_6058();
/* 37 */       boolean legacyProtocol = ViaProtocolUtils.isTargetProtocolBelowOneNineteen();
/*    */       
/* 39 */       if (this.sprint.isState()) {
/* 40 */         mc.field_1724.method_5728((((ModuleClass.sprint
/* 41 */             .isEnable() && Sprint.isSprinting()) || mc.field_1690.field_1867.method_1434()) && mc.field_1724.field_3913.field_3905 > 0.0F && (!legacyProtocol || (!mc.field_1724.field_5976 && !mc.field_1724.field_34927)) && 
/*    */ 
/*    */             
/* 44 */             !mc.field_1724.method_6128()));
/*    */       }
/*    */ 
/*    */       
/* 48 */       class_1268 otherHand = (activeHand == class_1268.field_5808) ? class_1268.field_5810 : class_1268.field_5808;
/* 49 */       mc.method_1562().method_52787((class_2596)new class_2886(otherHand, 0, mc.field_1724.method_36454(), mc.field_1724.method_36455()));
/*    */       
/* 51 */       event.setCancelled(true);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\movement\NoSlow.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */