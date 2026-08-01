/*    */ package shame.astra.client.modules.impl.player;
/*    */ import net.minecraft.class_1268;
/*    */ import net.minecraft.class_2338;
/*    */ import net.minecraft.class_2350;
/*    */ import net.minecraft.class_239;
/*    */ import net.minecraft.class_2596;
/*    */ import net.minecraft.class_2680;
/*    */ import net.minecraft.class_2846;
/*    */ import net.minecraft.class_3965;
/*    */ import net.minecraft.class_634;
/*    */ import net.minecraft.class_636;
/*    */ import net.minecraft.class_638;
/*    */ import net.minecraft.class_746;
/*    */ import shame.astra.client.modules.Module;
/*    */ import shame.astra.client.modules.settings.Setting;
/*    */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*    */ 
/*    */ public class FastBreak extends Module {
/* 19 */   public static FastBreak INSTANCE = new FastBreak();
/*    */   
/* 21 */   private final FloatSetting speed = new FloatSetting("Ускорение", 0.5F, 0.3F, 1.0F, 0.1F);
/*    */   
/*    */   public FastBreak() {
/* 24 */     super("FastBreak", "Ускоряет ломание блоков", Module.ModuleCategory.PLAYER);
/* 25 */     addSettings(new Setting[] { (Setting)this.speed });
/*    */   }
/*    */   @EventLink
/*    */   public void onUpdate(EventUpdate event) {
/*    */     class_3965 hit;
/* 30 */     if (mc.field_1724 == null || mc.field_1687 == null || mc.field_1761 == null) {
/*    */       return;
/*    */     }
/*    */     
/* 34 */     class_239 class_239 = mc.field_1765; if (class_239 instanceof class_3965) { hit = (class_3965)class_239; }
/*    */     else
/*    */     { return; }
/*    */     
/* 38 */     if (!mc.field_1690.field_1886.method_1434()) {
/*    */       return;
/*    */     }
/*    */     
/* 42 */     accelerateClientBreak(mc.field_1761, mc.field_1724, mc.field_1687, hit.method_17777(), hit.method_17780(), this.speed.get(), true);
/*    */   }
/*    */   
/*    */   public float getSpeed() {
/* 46 */     return this.speed.get();
/*    */   }
/*    */   
/*    */   public static int getExtraTicks(float speed) {
/* 50 */     return Math.max(1, Math.round(Math.max(0.3F, speed) / 0.35F));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean accelerateClientBreak(class_636 interactionManager, class_746 player, class_638 world, class_2338 pos, class_2350 side, float speed, boolean swing) {
/* 60 */     if (interactionManager == null || player == null || world == null || pos == null) {
/* 61 */       return false;
/*    */     }
/*    */     
/* 64 */     class_2680 state = world.method_8320(pos);
/* 65 */     if (state == null || state.method_26215()) {
/* 66 */       return false;
/*    */     }
/*    */     
/* 69 */     class_2350 breakSide = (side == null) ? class_2350.field_11036 : side;
/* 70 */     int extraTicks = getExtraTicks(speed);
/* 71 */     for (int i = 0; i < extraTicks; i++) {
/* 72 */       interactionManager.method_2902(pos, breakSide);
/*    */     }
/*    */     
/* 75 */     if (swing) {
/* 76 */       player.method_6104(class_1268.field_5808);
/*    */     }
/*    */     
/* 79 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean packetBreak(class_634 handler, class_746 player, class_2338 pos, class_2350 side, boolean swing) {
/* 87 */     if (handler == null || player == null || pos == null) {
/* 88 */       return false;
/*    */     }
/*    */     
/* 91 */     class_2350 breakSide = (side == null) ? class_2350.field_11036 : side;
/* 92 */     handler.method_52787((class_2596)new class_2846(class_2846.class_2847.field_12968, pos, breakSide));
/* 93 */     handler.method_52787((class_2596)new class_2846(class_2846.class_2847.field_12973, pos, breakSide));
/*    */     
/* 95 */     if (swing) {
/* 96 */       handler.method_52787((class_2596)new class_2879(class_1268.field_5808));
/*    */     }
/*    */     
/* 99 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\player\FastBreak.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */