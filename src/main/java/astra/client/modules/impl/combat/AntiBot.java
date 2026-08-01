/*    */ package shame.astra.client.modules.impl.combat;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.class_1297;
/*    */ import net.minecraft.class_1309;
/*    */ import net.minecraft.class_1657;
/*    */ import net.minecraft.class_1799;
/*    */ import net.minecraft.class_1802;
/*    */ import shame.astra.api.events.EventLink;
/*    */ import shame.astra.api.events.implement.EventUpdate;
/*    */ import shame.astra.client.modules.Module;
/*    */ 
/*    */ public class AntiBot extends Module {
/* 15 */   public static AntiBot INSTANCE = new AntiBot();
/*    */   
/* 17 */   public static final List<class_1297> isBot = new ArrayList<>();
/*    */   
/*    */   public AntiBot() {
/* 20 */     super("AntiBot", "Определяет ботов на сервере", Module.ModuleCategory.COMBAT);
/*    */   }
/*    */   
/*    */   @EventLink
/*    */   public void onUpdate(EventUpdate event) {
/* 25 */     newMatrix();
/*    */   }
/*    */   
/*    */   public void newMatrix() {
/* 29 */     if (mc.field_1687 == null)
/*    */       return; 
/* 31 */     for (class_1657 player : mc.field_1687.method_18456()) {
/* 32 */       if (mc.field_1724 != player && (
/* 33 */         (class_1799)(player.method_31548()).field_7548.get(0)).method_7909() != class_1802.field_8162 && (
/* 34 */         (class_1799)(player.method_31548()).field_7548.get(1)).method_7909() != class_1802.field_8162 && (
/* 35 */         (class_1799)(player.method_31548()).field_7548.get(2)).method_7909() != class_1802.field_8162 && (
/* 36 */         (class_1799)(player.method_31548()).field_7548.get(3)).method_7909() != class_1802.field_8162 && (
/* 37 */         (class_1799)(player.method_31548()).field_7548.get(0)).method_7923() && (
/* 38 */         (class_1799)(player.method_31548()).field_7548.get(1)).method_7923() && (
/* 39 */         (class_1799)(player.method_31548()).field_7548.get(2)).method_7923() && (
/* 40 */         (class_1799)(player.method_31548()).field_7548.get(3)).method_7923() && player
/* 41 */         .method_6079().method_7909() == class_1802.field_8162 && ((
/* 42 */         (class_1799)(player.method_31548()).field_7548.get(0)).method_7909() == class_1802.field_8370 || (
/* 43 */         (class_1799)(player.method_31548()).field_7548.get(1)).method_7909() == class_1802.field_8570 || (
/* 44 */         (class_1799)(player.method_31548()).field_7548.get(2)).method_7909() == class_1802.field_8577 || (
/* 45 */         (class_1799)(player.method_31548()).field_7548.get(3)).method_7909() == class_1802.field_8267 || (
/* 46 */         (class_1799)(player.method_31548()).field_7548.get(0)).method_7909() == class_1802.field_8660 || (
/* 47 */         (class_1799)(player.method_31548()).field_7548.get(1)).method_7909() == class_1802.field_8396 || (
/* 48 */         (class_1799)(player.method_31548()).field_7548.get(2)).method_7909() == class_1802.field_8523 || (
/* 49 */         (class_1799)(player.method_31548()).field_7548.get(3)).method_7909() == class_1802.field_8743) && player
/* 50 */         .method_6047().method_7909() != class_1802.field_8162 && 
/* 51 */         !((class_1799)(player.method_31548()).field_7548.get(0)).method_7986() && 
/* 52 */         !((class_1799)(player.method_31548()).field_7548.get(1)).method_7986() && 
/* 53 */         !((class_1799)(player.method_31548()).field_7548.get(2)).method_7986() && 
/* 54 */         !((class_1799)(player.method_31548()).field_7548.get(3)).method_7986() && player
/* 55 */         .method_7344().method_7586() == 20) {
/* 56 */         if (!isBot.contains(player)) {
/* 57 */           isBot.add(player);
/*    */         }
/*    */         return;
/*    */       } 
/* 61 */       isBot.remove(player);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static boolean checkBot(class_1309 entity) {
/* 66 */     return (entity instanceof class_1657 && isBot.contains(entity));
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 71 */     super.onDisable();
/* 72 */     isBot.clear();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\combat\AntiBot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */