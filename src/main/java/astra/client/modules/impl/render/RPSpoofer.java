/*    */ package shame.astra.client.modules.impl.render;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import net.minecraft.class_2596;
/*    */ import net.minecraft.class_2720;
/*    */ import net.minecraft.class_2856;
/*    */ import shame.astra.api.events.EventLink;
/*    */ import shame.astra.api.events.implement.EventPacket;
/*    */ import shame.astra.api.utils.bot.BotSessionManager;
/*    */ import shame.astra.client.modules.Module;
/*    */ 
/*    */ public class RPSpoofer
/*    */   extends Module {
/* 14 */   public static RPSpoofer INSTANCE = new RPSpoofer();
/*    */   public RPSpoofer() {
/* 16 */     super("RPSpoofer", "Убирает ресурс-пак сервера", Module.ModuleCategory.PLAYER);
/*    */   }
/*    */   
/*    */   @EventLink
/*    */   public void onReceivePacket(EventPacket e) {
/* 21 */     class_2596 class_2596 = e.getPacket(); if (class_2596 instanceof class_2720) { class_2720 packet = (class_2720)class_2596; if (isEnable() || BotSessionManager.shouldBypassResourcePacks()) {
/* 22 */         UUID packId = packet.comp_2158();
/* 23 */         mc.method_1562().method_52787((class_2596)new class_2856(packId, class_2856.class_2857.field_13016));
/* 24 */         mc.method_1562().method_52787((class_2596)new class_2856(packId, class_2856.class_2857.field_13017));
/* 25 */         e.setCancelled(true);
/*    */       }  }
/*    */   
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\RPSpoofer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */