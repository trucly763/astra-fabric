/*    */ package shame.astra.api.utils.notification;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import shame.astra.api.utils.client.ClientSoundPlayer;
/*    */ import shame.astra.client.modules.impl.misc.ClientSounds;
/*    */ 
/*    */ public class NotificationManager
/*    */ {
/*    */   public static final long DURATION_MS = 2500L;
/*    */   private static final long MODULE_SOUND_STARTUP_MUTE_MS = 4000L;
/* 13 */   private static final long INIT_TIME_MS = System.currentTimeMillis();
/* 14 */   private static final List<Entry> entries = new ArrayList<>();
/*    */   
/*    */   public static void push(String moduleName, String categoryIcon, boolean enabled) {
/* 17 */     if (moduleName == null || moduleName.isEmpty())
/* 18 */       return;  entries.add(new Entry(moduleName, categoryIcon, enabled, null, System.currentTimeMillis()));
/* 19 */     playModuleSound(enabled);
/*    */   }
/*    */   
/*    */   public static void pushCustom(String text, String categoryIcon) {
/* 23 */     if (text == null || text.isEmpty())
/* 24 */       return;  entries.add(new Entry(text, categoryIcon, false, text, System.currentTimeMillis()));
/*    */   }
/*    */   
/*    */   public static List<Entry> getActive() {
/* 28 */     long now = System.currentTimeMillis();
/* 29 */     Iterator<Entry> it = entries.iterator();
/* 30 */     while (it.hasNext()) {
/* 31 */       Entry e = it.next();
/* 32 */       if (now - e.startTime > 2500L) {
/* 33 */         it.remove();
/*    */       }
/*    */     } 
/* 36 */     return entries;
/*    */   }
/*    */   
/*    */   public static class Entry {
/*    */     public final String moduleName;
/*    */     public final String categoryIcon;
/*    */     public final boolean enabled;
/*    */     public final String customText;
/*    */     public final long startTime;
/*    */     
/*    */     public Entry(String moduleName, String categoryIcon, boolean enabled, String customText, long startTime) {
/* 47 */       this.moduleName = moduleName;
/* 48 */       this.categoryIcon = categoryIcon;
/* 49 */       this.enabled = enabled;
/* 50 */       this.customText = customText;
/* 51 */       this.startTime = startTime;
/*    */     }
/*    */     
/*    */     public boolean isCustom() {
/* 55 */       return (this.customText != null && !this.customText.isEmpty());
/*    */     }
/*    */   }
/*    */   
/*    */   private static void playModuleSound(boolean enabled) {
/* 60 */     if (System.currentTimeMillis() - INIT_TIME_MS < 4000L) {
/*    */       return;
/*    */     }
/*    */     
/* 64 */     ClientSounds clientSounds = ClientSounds.INSTANCE;
/* 65 */     if (clientSounds == null || !clientSounds.isEnable()) {
/*    */       return;
/*    */     }
/*    */     
/* 69 */     String soundName = clientSounds.stateSounds.getCurrent();
/* 70 */     if ("Нет".equals(soundName)) {
/*    */       return;
/*    */     }
/*    */     
/* 74 */     float pitch = enabled ? 1.0F : 0.95F;
/* 75 */     ClientSoundPlayer.playSound(soundName + ".wav", (clientSounds.volume.get() / clientSounds.volume.getMax()), pitch);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\notification\NotificationManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */