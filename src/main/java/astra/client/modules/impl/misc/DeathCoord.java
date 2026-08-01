/*    */ package shame.astra.client.modules.impl.misc;
/*    */ 
/*    */ import net.minecraft.class_2338;
/*    */ import net.minecraft.class_2561;
/*    */ import shame.astra.api.events.EventLink;
/*    */ import shame.astra.api.events.implement.EventUpdate;
/*    */ import shame.astra.client.modules.Module;
/*    */ import shame.astra.client.modules.settings.Setting;
/*    */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*    */ 
/*    */ public class DeathCoord extends Module {
/* 12 */   public static DeathCoord INSTANCE = new DeathCoord();
/*    */   
/* 14 */   private final BooleanSetting copyToClipboard = new BooleanSetting("Копировать в буфер", true);
/*    */   
/* 16 */   private class_2338 deathPos = null;
/*    */   private boolean isDead = false;
/*    */   
/*    */   public DeathCoord() {
/* 20 */     super("DeathCoord", "Показывает координаты смерти", Module.ModuleCategory.MISC);
/* 21 */     addSettings(new Setting[] { (Setting)this.copyToClipboard });
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 26 */     super.onEnable();
/* 27 */     this.isDead = false;
/* 28 */     this.deathPos = null;
/*    */   }
/*    */   
/*    */   @EventLink
/*    */   public void onUpdate(EventUpdate event) {
/* 33 */     if (mc.field_1724 == null || mc.field_1687 == null)
/*    */       return; 
/* 35 */     if (mc.field_1724.method_6032() <= 0.0F && !this.isDead) {
/* 36 */       this.isDead = true;
/* 37 */       this.deathPos = mc.field_1724.method_24515();
/*    */       
/* 39 */       String coords = "X: " + this.deathPos.method_10263() + " Y: " + this.deathPos.method_10264() + " Z: " + this.deathPos.method_10260();
/* 40 */       String dimension = getDimension();
/* 41 */       String message = "§cВы умерли! §f" + coords + " §7(" + dimension + ")";
/*    */       
/* 43 */       mc.field_1724.method_7353((class_2561)class_2561.method_43470(message), false);
/*    */       
/* 45 */       if (this.copyToClipboard.isState()) {
/* 46 */         mc.field_1774.method_1455("" + this.deathPos.method_10263() + " " + this.deathPos.method_10263() + " " + this.deathPos.method_10264());
/*    */       }
/*    */     } 
/*    */     
/* 50 */     if (mc.field_1724.method_6032() > 0.0F && this.isDead) {
/* 51 */       this.isDead = false;
/*    */     }
/*    */   }
/*    */   
/*    */   private String getDimension() {
/* 56 */     if (mc.field_1687 == null) return "Unknown";
/*    */     
/* 58 */     String dimension = mc.field_1687.method_27983().method_29177().toString();
/*    */     
/* 60 */     if (dimension.contains("overworld")) return "Overworld"; 
/* 61 */     if (dimension.contains("nether")) return "Nether"; 
/* 62 */     if (dimension.contains("end")) return "End";
/*    */     
/* 64 */     return dimension;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 69 */     super.onDisable();
/* 70 */     this.isDead = false;
/* 71 */     this.deathPos = null;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\misc\DeathCoord.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */