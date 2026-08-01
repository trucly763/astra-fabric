/*    */ package shame.astra.client.modules.impl.combat;
/*    */ 
/*    */ import net.minecraft.class_2596;
/*    */ import net.minecraft.class_2743;
/*    */ import shame.astra.api.events.EventLink;
/*    */ import shame.astra.api.events.implement.EventPacket;
/*    */ import shame.astra.api.events.implement.EventUpdate;
/*    */ import shame.astra.client.modules.Module;
/*    */ import shame.astra.client.modules.settings.Setting;
/*    */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*    */ import shame.astra.client.modules.settings.implement.ModeSetting;
/*    */ 
/*    */ public class NoVelocity
/*    */   extends Module {
/* 15 */   public static NoVelocity INSTANCE = new NoVelocity();
/*    */   
/* 17 */   private final ModeSetting mode = new ModeSetting("Мод", "Vanilla", new String[] { "Vanilla", "Grim", "Jump Reset" });
/* 18 */   private final BooleanSetting explosions = new BooleanSetting("Взрывы", true);
/*    */   
/*    */   private boolean needJump;
/*    */   private int hurtTicks;
/*    */   
/*    */   public NoVelocity() {
/* 24 */     super("NoVelocity", "Отключает отдачу от урона", Module.ModuleCategory.MOVEMENT);
/* 25 */     addSettings(new Setting[] { (Setting)this.mode, (Setting)this.explosions });
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 30 */     super.onEnable();
/* 31 */     this.needJump = false;
/* 32 */     this.hurtTicks = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 37 */     super.onDisable();
/* 38 */     this.needJump = false;
/* 39 */     this.hurtTicks = 0;
/*    */   }
/*    */   
/*    */   @EventLink
/*    */   public void onPacket(EventPacket event) {
/* 44 */     if (mc.field_1724 == null || mc.field_1687 == null)
/* 45 */       return;  if (event.getType() != EventPacket.Type.RECEIVE)
/*    */       return; 
/* 47 */     class_2596 class_2596 = event.getPacket(); if (class_2596 instanceof class_2743) { class_2743 packet = (class_2743)class_2596;
/* 48 */       if (packet.method_11818() != mc.field_1724.method_5628())
/*    */         return; 
/* 50 */       if (this.mode.is("Vanilla")) {
/* 51 */         event.cancel();
/*    */       }
/*    */       
/* 54 */       if (this.mode.is("Grim")) {
/* 55 */         event.cancel();
/* 56 */         double velY = packet.method_11816() / 8000.0D;
/*    */         
/* 58 */         if (mc.field_1724.method_24828() && velY > 0.0D) {
/* 59 */           mc.field_1724.method_18800((mc.field_1724.method_18798()).field_1352, 0.0D, (mc.field_1724.method_18798()).field_1350);
/* 60 */         } else if (velY > 0.0D) {
/* 61 */           mc.field_1724.method_18800((mc.field_1724.method_18798()).field_1352, 0.0D, (mc.field_1724.method_18798()).field_1350);
/*    */         } 
/*    */       } 
/*    */       
/* 65 */       if (this.mode.is("Jump Reset")) {
/* 66 */         double velY = packet.method_11816() / 8000.0D;
/* 67 */         if (velY > 0.1D) {
/* 68 */           this.needJump = true;
/* 69 */           this.hurtTicks = 0;
/*    */         } 
/*    */       }  }
/*    */ 
/*    */     
/* 74 */     if (this.explosions.isState() && event.getPacket() instanceof net.minecraft.class_2664 && (
/* 75 */       this.mode.is("Vanilla") || this.mode.is("Grim"))) {
/* 76 */       event.cancel();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @EventLink
/*    */   public void onUpdate(EventUpdate event) {
/* 84 */     if (mc.field_1724 == null)
/*    */       return; 
/* 86 */     if (this.mode.is("Jump Reset") && this.needJump) {
/* 87 */       this.hurtTicks++;
/*    */       
/* 89 */       if (mc.field_1724.method_24828()) {
/* 90 */         mc.field_1724.method_6043();
/* 91 */         this.needJump = false;
/* 92 */         this.hurtTicks = 0;
/*    */       } 
/*    */       
/* 95 */       if (this.hurtTicks > 5) {
/* 96 */         this.needJump = false;
/* 97 */         this.hurtTicks = 0;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\combat\NoVelocity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */