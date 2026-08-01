/*    */ package shame.astra.client.modules.impl.misc;
/*    */ 
/*    */ import shame.astra.client.modules.Module;
/*    */ import shame.astra.client.modules.settings.Setting;
/*    */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*    */ import shame.astra.client.modules.settings.implement.ModeSetting;
/*    */ 
/*    */ public class ClientSounds extends Module {
/*  9 */   public static ClientSounds INSTANCE = new ClientSounds();
/*    */   
/* 11 */   public final ModeSetting stateSounds = new ModeSetting("Режим", "Нет", new String[] { "Первый", "Второй", "Третий", "Четвертый", "Пятый", "Шестой" });
/*    */   
/* 13 */   public final FloatSetting volume = new FloatSetting("Громкость", 50.0F, 1.0F, 100.0F, 0.5F);
/*    */   
/*    */   public ClientSounds() {
/* 16 */     super("ClientSounds", "Добавляет звуки клиента", Module.ModuleCategory.MISC);
/* 17 */     addSettings(new Setting[] { (Setting)this.stateSounds, (Setting)this.volume });
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\misc\ClientSounds.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */