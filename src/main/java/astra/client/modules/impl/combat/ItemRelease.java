/*    */ package shame.astra.client.modules.impl.combat;
/*    */ 
/*    */ import net.minecraft.class_1309;
/*    */ import net.minecraft.class_1657;
/*    */ import net.minecraft.class_1764;
/*    */ import shame.astra.api.events.EventLink;
/*    */ import shame.astra.api.events.implement.EventUpdate;
/*    */ import shame.astra.client.modules.Module;
/*    */ import shame.astra.client.modules.settings.Setting;
/*    */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*    */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*    */ import shame.astra.client.modules.settings.implement.ListSetting;
/*    */ 
/*    */ public class ItemRelease
/*    */   extends Module {
/* 16 */   public static ItemRelease INSTANCE = new ItemRelease();
/*    */   
/* 18 */   private final ListSetting items = new ListSetting("Предметы", new BooleanSetting[] { new BooleanSetting("Лук", true), new BooleanSetting("Трезубец", false), new BooleanSetting("Арбалет", true) });
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   private final FloatSetting tickBow = (new FloatSetting("Задержка выстрела", 2.5F, 2.0F, 5.0F, 0.05F))
/* 25 */     .visible(() -> Boolean.valueOf(this.items.is("Лук")));
/*    */   
/*    */   public ItemRelease() {
/* 28 */     super("ItemRelease", "Автоматически выпускает предмет когда он полностью натянут", Module.ModuleCategory.COMBAT);
/* 29 */     addSettings(new Setting[] { (Setting)this.items, (Setting)this.tickBow });
/*    */   }
/*    */ 
/*    */   
/*    */   @EventLink
/*    */   public void onUpdate(EventUpdate e) {
/* 35 */     if (mc.field_1724 == null || mc.field_1687 == null)
/*    */       return; 
/* 37 */     if (this.items.is("Лук") && 
/* 38 */       mc.field_1724.method_6047().method_7909() instanceof net.minecraft.class_1753 && mc.field_1724.method_6115() && mc.field_1724.method_6048() >= this.tickBow.getValue().floatValue()) {
/* 39 */       mc.field_1761.method_2897((class_1657)mc.field_1724);
/*    */     }
/*    */ 
/*    */     
/* 43 */     if (this.items.is("Трезубец") && 
/* 44 */       mc.field_1724.method_6047().method_7909() instanceof net.minecraft.class_1835 && mc.field_1724.method_6115() && mc.field_1724.method_6048() >= 10) {
/* 45 */       mc.field_1761.method_2897((class_1657)mc.field_1724);
/*    */     }
/*    */ 
/*    */     
/* 49 */     if (this.items.is("Арбалет") && 
/* 50 */       mc.field_1724.method_6047().method_7909() instanceof class_1764 && mc.field_1724.method_6115() && mc.field_1724.method_6048() >= class_1764.method_7775(mc.field_1724.method_6047(), (class_1309)mc.field_1724))
/* 51 */       mc.field_1761.method_2897((class_1657)mc.field_1724); 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\combat\ItemRelease.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */