/*    */ package shame.astra.client.modules.impl.render;
/*    */ 
/*    */ import shame.astra.client.modules.Module;
/*    */ import shame.astra.client.modules.settings.Setting;
/*    */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*    */ import shame.astra.client.modules.settings.implement.ListSetting;
/*    */ 
/*    */ public class Removals extends Module {
/*  9 */   public static Removals INSTANCE = new Removals();
/*    */   
/* 11 */   private final ListSetting elements = new ListSetting("Элементы", new BooleanSetting[] { new BooleanSetting("Огонь", false), new BooleanSetting("Плохие эффекты", false), new BooleanSetting("Оверлей в блоке", false), new BooleanSetting("Частицы", false), new BooleanSetting("Погода", false), new BooleanSetting("Облака", false), new BooleanSetting("Блок-сущности", false), new BooleanSetting("Тени", false), new BooleanSetting("Анимацию тотема", false) });
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Removals() {
/* 24 */     super("Removals", "Убирает выбранные элементы рендера", Module.ModuleCategory.RENDER);
/* 25 */     addSettings(new Setting[] { (Setting)this.elements });
/*    */   }
/*    */   
/*    */   public boolean isEnabled(String element) {
/* 29 */     return (isEnable() && this.elements.is(element));
/*    */   }
/*    */   
/*    */   public boolean isTotemAnimationDisabled() {
/* 33 */     return isEnabled("Анимацию тотема");
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\Removals.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */