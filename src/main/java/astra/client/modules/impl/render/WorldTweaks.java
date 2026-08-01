/*    */ package shame.astra.client.modules.impl.render;
/*    */ 
/*    */ import shame.astra.api.utils.color.ColorUtils;
/*    */ import shame.astra.astra;
/*    */ import shame.astra.client.modules.Module;
/*    */ import shame.astra.client.modules.settings.Setting;
/*    */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*    */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*    */ import shame.astra.client.modules.settings.implement.ListSetting;
/*    */ 
/*    */ public class WorldTweaks extends Module {
/* 12 */   public static WorldTweaks INSTANCE = new WorldTweaks();
/*    */   
/* 14 */   private final ListSetting worldSettings = new ListSetting("Настройки мира", new BooleanSetting[] { new BooleanSetting("Время", true), new BooleanSetting("Фог", true) });
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   private final FloatSetting timeSetting = (new FloatSetting("Время", 12.0F, 0.0F, 24.0F, 1.0F))
/* 20 */     .visible(() -> Boolean.valueOf(this.worldSettings.is("Время")));
/*    */   
/* 22 */   private final FloatSetting fogDistanceSetting = (new FloatSetting("Дистанция фога", 100.0F, 20.0F, 200.0F, 1.0F))
/* 23 */     .visible(() -> Boolean.valueOf(this.worldSettings.is("Фог")));
/*    */   
/*    */   public WorldTweaks() {
/* 26 */     super("CustomWorld", "Настройки мира", Module.ModuleCategory.RENDER);
/* 27 */     addSettings(new Setting[] { (Setting)this.worldSettings, (Setting)this.timeSetting, (Setting)this.fogDistanceSetting });
/*    */   }
/*    */   
/*    */   public boolean isTimeEnabled() {
/* 31 */     return (isEnable() && this.worldSettings.is("Время"));
/*    */   }
/*    */   
/*    */   public boolean isFogEnabled() {
/* 35 */     return (isEnable() && this.worldSettings.is("Фог"));
/*    */   }
/*    */   
/*    */   public long getForcedTime() {
/* 39 */     return (long)this.timeSetting.get() * 1000L;
/*    */   }
/*    */   
/*    */   public float getFogDistance() {
/* 43 */     return this.fogDistanceSetting.get();
/*    */   }
/*    */   
/*    */   public int getFogColor() {
/* 47 */     if (!astra.INSTANCE.themeStorage.getThemes().getTheme().getName().equals("Rainbow")) {
/* 48 */       return (astra.INSTANCE.themeStorage.getThemes().getTheme()).color[0];
/*    */     }
/* 50 */     return ColorUtils.getThemeColor();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\WorldTweaks.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */