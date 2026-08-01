/*    */ package shame.astra.client.modules.impl.player;
/*    */ import net.minecraft.class_1713;
/*    */ import net.minecraft.class_1735;
/*    */ import net.minecraft.class_2596;
/*    */ import net.minecraft.class_2813;
/*    */ import net.minecraft.class_2846;
/*    */ import shame.astra.api.events.EventLink;
/*    */ import shame.astra.api.events.implement.EventPacket;
/*    */ import shame.astra.api.utils.chat.ChatUtils;
/*    */ import shame.astra.client.modules.Module;
/*    */ import shame.astra.client.modules.settings.Setting;
/*    */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*    */ import shame.astra.client.modules.settings.implement.ListSetting;
/*    */ import shame.astra.mixin.SlotAccessor;
/*    */ 
/*    */ public class LockSlot extends Module {
/* 17 */   public static LockSlot INSTANCE = new LockSlot();
/*    */   
/* 19 */   private final ListSetting slots = new ListSetting("Слоты", new BooleanSetting[] { new BooleanSetting("1", false), new BooleanSetting("2", false), new BooleanSetting("3", false), new BooleanSetting("4", false), new BooleanSetting("5", false), new BooleanSetting("6", false), new BooleanSetting("7", false), new BooleanSetting("8", false), new BooleanSetting("9", false) });
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
/*    */   public LockSlot() {
/* 32 */     super("LockSlot", "Блокирует выброс предметов из выбранных слотов", Module.ModuleCategory.PLAYER);
/* 33 */     addSettings(new Setting[] { (Setting)this.slots });
/*    */   }
/*    */   
/*    */   @EventLink
/*    */   public void onPacket(EventPacket event) {
/* 38 */     if (mc.field_1724 == null || event.getType() != EventPacket.Type.SEND)
/* 39 */       return;  if (mc.field_1755 instanceof net.minecraft.class_465)
/*    */       return; 
/* 41 */     class_2596 class_2596 = event.getPacket(); if (class_2596 instanceof class_2846) { class_2846 packet = (class_2846)class_2596;
/* 42 */       if (packet.method_12363() != class_2846.class_2847.field_12975 && packet
/* 43 */         .method_12363() != class_2846.class_2847.field_12970) {
/*    */         return;
/*    */       }
/* 46 */       if (isCurrentSlotLockedForDrop()) {
/* 47 */         event.cancel();
/* 48 */         sendLockedMessage((mc.field_1724.method_31548()).field_7545);
/*    */       } 
/*    */       
/*    */       return; }
/*    */     
/* 53 */     class_2596 = event.getPacket(); if (class_2596 instanceof class_2813) { class_2813 packet = (class_2813)class_2596; if (packet.method_12195() == class_1713.field_7795) {
/* 54 */         int hotbarSlot = getHotbarSlotFromClick(packet.method_12192());
/* 55 */         if (hotbarSlot >= 0 && isHotbarSlotLocked(hotbarSlot)) {
/* 56 */           event.cancel();
/* 57 */           sendLockedMessage(hotbarSlot);
/*    */         } 
/*    */       }  }
/*    */   
/*    */   }
/*    */   public boolean isCurrentSlotLockedForDrop() {
/* 63 */     if (!isEnable() || mc.field_1724 == null || mc.field_1724.method_6047().method_7960()) return false; 
/* 64 */     if (mc.field_1755 instanceof net.minecraft.class_465) return false; 
/* 65 */     return isHotbarSlotLocked((mc.field_1724.method_31548()).field_7545);
/*    */   }
/*    */   
/*    */   private boolean isHotbarSlotLocked(int slot) {
/* 69 */     if (slot < 0 || slot >= this.slots.getSettings().size()) return false; 
/* 70 */     return ((BooleanSetting)this.slots.getSettings().get(slot)).isState();
/*    */   }
/*    */   
/*    */   private int getHotbarSlotFromClick(int slotId) {
/* 74 */     if (mc.field_1724 == null || slotId < 0 || slotId >= mc.field_1724.field_7512.field_7761.size()) {
/* 75 */       return -1;
/*    */     }
/*    */     
/* 78 */     class_1735 slot = mc.field_1724.field_7512.method_7611(slotId);
/* 79 */     SlotAccessor accessor = (SlotAccessor)slot;
/* 80 */     int inventoryIndex = accessor.astra$getIndex();
/* 81 */     if (accessor.astra$getInventory() == mc.field_1724.method_31548() && inventoryIndex >= 0 && inventoryIndex <= 8) {
/* 82 */       return inventoryIndex;
/*    */     }
/* 84 */     return -1;
/*    */   }
/*    */   
/*    */   private void sendLockedMessage(int slot) {
/* 88 */     ChatUtils.sendMessage("Выброс предмета из слота " + slot + 1 + " заблокирован");
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\player\LockSlot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */