/*    */ package shame.astra.mixin;
/*    */ 
/*    */ import net.minecraft.class_10042;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.Unique;
/*    */ import shame.astra.client.modules.impl.render.SeeInvisiblesRenderState;
/*    */ 
/*    */ @Mixin({class_10042.class})
/*    */ public class LivingEntityRenderStateMixin
/*    */   implements SeeInvisiblesRenderState
/*    */ {
/*    */   @Unique
/*    */   private boolean astra$seeInvisiblesTarget;
/*    */   
/*    */   public boolean astra$isSeeInvisiblesTarget() {
/* 16 */     return this.astra$seeInvisiblesTarget;
/*    */   }
/*    */ 
/*    */   
/*    */   public void astra$setSeeInvisiblesTarget(boolean value) {
/* 21 */     this.astra$seeInvisiblesTarget = value;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\mixin\LivingEntityRenderStateMixin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */