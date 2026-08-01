/*    */ package shame.astra.client.modules.impl.render;
/*    */ 
/*    */ import net.minecraft.class_1294;
/*    */ import net.minecraft.class_1657;
/*    */ import shame.astra.api.events.EventLink;
/*    */ import shame.astra.api.events.implement.EventUpdate;
/*    */ import shame.astra.client.modules.Module;
/*    */ 
/*    */ public class SeeInvisibles
/*    */   extends Module {
/*    */   public static final float INVISIBLE_ALPHA = 0.7F;
/* 12 */   public static final int INVISIBLE_COLOR = Math.round(178.5F) << 24 | 0xFFFFFF;
/* 13 */   public static SeeInvisibles INSTANCE = new SeeInvisibles();
/*    */   
/*    */   public SeeInvisibles() {
/* 16 */     super("SeeInvisibles", "Показывает невидимых игроков", Module.ModuleCategory.RENDER);
/*    */   }
/*    */   
/*    */   @EventLink
/*    */   public void onUpdate(EventUpdate event) {
/* 21 */     if (mc.field_1724 == null || mc.field_1687 == null) {
/*    */       return;
/*    */     }
/*    */     
/* 25 */     for (class_1657 player : mc.field_1687.method_18456()) {
/* 26 */       if (shouldRenderInvisible(player)) {
/* 27 */         player.method_5648(false);
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean shouldRenderInvisible(class_1657 player) {
/* 33 */     return (isEnable() && mc.field_1724 != null && player != null && player != mc.field_1724 && (player
/*    */ 
/*    */ 
/*    */       
/* 37 */       .method_5767() || player.method_6059(class_1294.field_5905)));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\SeeInvisibles.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */