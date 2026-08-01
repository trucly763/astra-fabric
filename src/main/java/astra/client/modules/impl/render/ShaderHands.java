/*    */ package shame.astra.client.modules.impl.render;
/*    */ 
/*    */ import shame.astra.api.events.EventLink;
/*    */ import shame.astra.api.events.implement.EventRender;
/*    */ import shame.astra.api.utils.render.hands.ShaderHandsRenderer;
/*    */ import shame.astra.client.modules.Module;
/*    */ import shame.astra.client.modules.settings.Setting;
/*    */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*    */ import shame.astra.client.modules.settings.implement.ModeSetting;
/*    */ 
/*    */ public class ShaderHands
/*    */   extends Module {
/* 13 */   public static ShaderHands INSTANCE = new ShaderHands();
/* 14 */   private static final ShaderHandsRenderer RENDERER = ShaderHandsRenderer.getInstance();
/* 15 */   public final ModeSetting mode = new ModeSetting("Режим", "Свечение", new String[] { "Свечение", "Красивый" });
/*    */   
/* 17 */   public final FloatSetting waveSpeed = (new FloatSetting("Скорость волн", 1.2F, 0.1F, 5.0F, 0.1F))
/* 18 */     .visible(() -> Boolean.valueOf(this.mode.is("Красивый")));
/* 19 */   public final FloatSetting waveScale = (new FloatSetting("Частота волн", 1.0F, 1.0F, 3.0F, 0.1F))
/* 20 */     .visible(() -> Boolean.valueOf(this.mode.is("Красивый")));
/*    */   
/* 22 */   public final FloatSetting outline = new FloatSetting("Ширина обводки", 1.2F, 0.1F, 5.0F, 0.1F);
/* 23 */   public final FloatSetting glow = new FloatSetting("Сила свечения", 1.0F, 0.0F, 5.0F, 0.1F);
/* 24 */   public final FloatSetting fill = new FloatSetting("Заливка", 0.6F, 0.0F, 1.0F, 0.01F);
/* 25 */   public final FloatSetting alpha = new FloatSetting("Прозрачность", 1.0F, 0.0F, 1.0F, 0.05F);
/*    */   
/*    */   public ShaderHands() {
/* 28 */     super("ShaderHands", "Красивый Шейдер на руки и предметы", Module.ModuleCategory.RENDER);
/* 29 */     addSettings(new Setting[] { (Setting)this.mode, (Setting)this.waveSpeed, (Setting)this.waveScale, (Setting)this.outline, (Setting)this.glow, (Setting)this.fill, (Setting)this.alpha });
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 34 */     RENDERER.invalidateState();
/* 35 */     super.onDisable();
/*    */   }
/*    */   
/*    */   @EventLink(priority = 0)
/*    */   public void onRender2D(EventRender.Default event) {
/* 40 */     if (!isEnable())
/* 41 */       return;  RENDERER.renderOverlayIfPending();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\ShaderHands.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */