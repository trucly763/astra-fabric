/*    */ package shame.astra.client.modules.impl.misc;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import net.minecraft.class_2596;
/*    */ import net.minecraft.class_7439;
/*    */ import shame.astra.api.events.EventLink;
/*    */ import shame.astra.api.events.implement.EventPacket;
/*    */ import shame.astra.astra;
/*    */ import shame.astra.client.modules.Module;
/*    */ import shame.astra.client.modules.settings.Setting;
/*    */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*    */ 
/*    */ public class AutoAccept
/*    */   extends Module {
/* 15 */   public static AutoAccept INSTANCE = new AutoAccept();
/*    */   
/* 17 */   private final BooleanSetting onlyFriend = new BooleanSetting("Только друзья", false);
/*    */   
/*    */   public AutoAccept() {
/* 20 */     super("AutoAccept", "Автоматически принимает телепорт", Module.ModuleCategory.MISC);
/* 21 */     addSettings(new Setting[] { (Setting)this.onlyFriend });
/*    */   }
/*    */   
/*    */   @EventLink
/*    */   public void onEvent(EventPacket event) {
/* 26 */     if (mc.field_1724 == null || mc.field_1687 == null)
/* 27 */       return;  if (event.getType() != EventPacket.Type.RECEIVE)
/*    */       return; 
/* 29 */     class_2596<?> packet = event.getPacket();
/* 30 */     if (packet instanceof class_7439) { class_7439 messagePacket = (class_7439)packet;
/* 31 */       String raw = messagePacket.comp_763().getString().toLowerCase(Locale.ROOT);
/*    */       
/* 33 */       if (raw.contains("телепортироваться") || raw.contains("has requested teleport") || raw.contains("просит к вам телепортироваться")) {
/* 34 */         if (this.onlyFriend.isState()) {
/* 35 */           boolean isFriend = false;
/*    */           
/* 37 */           if (astra.INSTANCE.friendStorage != null) {
/* 38 */             for (String friend : astra.INSTANCE.friendStorage.getFriends()) {
/* 39 */               if (raw.contains(friend.toLowerCase(Locale.ROOT))) {
/* 40 */                 isFriend = true;
/*    */                 
/*    */                 break;
/*    */               } 
/*    */             } 
/*    */           }
/* 46 */           if (!isFriend) {
/*    */             return;
/*    */           }
/*    */         } 
/*    */         
/* 51 */         mc.field_1724.field_3944.method_45730("tpaccept");
/*    */       }  }
/*    */   
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\misc\AutoAccept.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */