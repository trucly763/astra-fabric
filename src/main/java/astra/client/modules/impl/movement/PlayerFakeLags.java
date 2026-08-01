/*    */ package shame.astra.client.modules.impl.movement;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*    */ import lombok.Generated;
/*    */ import net.minecraft.class_2596;
/*    */ import shame.astra.api.events.EventLink;
/*    */ import shame.astra.api.events.implement.EventPacket;
/*    */ import shame.astra.api.events.implement.EventUpdate;
/*    */ import shame.astra.api.utils.math.TimerUtils;
/*    */ import shame.astra.client.modules.Module;
/*    */ import shame.astra.client.modules.settings.Setting;
/*    */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*    */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*    */ import shame.astra.client.modules.settings.implement.ModeSetting;
/*    */ 
/*    */ public class PlayerFakeLags
/*    */   extends Module {
/*    */   @Generated
/*    */   public void setReleasing(boolean releasing) {
/* 21 */     this.releasing = releasing;
/*    */   }
/*    */ 
/*    */   
/* 25 */   public static PlayerFakeLags INSTANCE = new PlayerFakeLags();
/*    */   
/* 27 */   public final ModeSetting mode = new ModeSetting("Режим", "Blink", new String[] { "Blink", "Pulse" }); @Generated public ModeSetting getMode() { return this.mode; }
/* 28 */    public final FloatSetting delay = new FloatSetting("Задержка (MS)", 500.0F, 50.0F, 2000.0F, 50.0F); @Generated public FloatSetting getDelay() { return this.delay; }
/* 29 */    public final BooleanSetting onlyMovement = new BooleanSetting("Только движение", true); @Generated public BooleanSetting getOnlyMovement() { return this.onlyMovement; }
/*    */   
/* 31 */   public final ObjectArrayList<class_2596<?>> packets = new ObjectArrayList(); @Generated public ObjectArrayList<class_2596<?>> getPackets() { return this.packets; }
/* 32 */    public final TimerUtils timer = new TimerUtils(); @Generated public TimerUtils getTimer() { return this.timer; } @Generated
/* 33 */   public boolean isReleasing() { return this.releasing; }
/*    */    public boolean releasing = false;
/*    */   public PlayerFakeLags() {
/* 36 */     super("PlayerFakeLags", "Фейковые лаги", Module.ModuleCategory.MOVEMENT);
/* 37 */     addSettings(new Setting[] { (Setting)this.mode, (Setting)this.delay, (Setting)this.onlyMovement });
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 42 */     super.onEnable();
/* 43 */     this.packets.clear();
/* 44 */     this.timer.reset();
/* 45 */     this.releasing = false;
/*    */   }
/*    */   
/*    */   @EventLink
/*    */   void onEvent(EventUpdate ignored) {
/* 50 */     if (mc.field_1724 == null)
/*    */       return; 
/* 52 */     if (this.mode.is("Pulse") && this.timer.finished(this.delay.getValue().longValue())) {
/* 53 */       releasePackets();
/* 54 */       this.timer.reset();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   @EventLink
/*    */   void onEvent(EventPacket event) {
/* 61 */     if (mc.field_1724 == null || this.releasing)
/*    */       return; 
/* 63 */     if (event.getType() == EventPacket.Type.SEND) {
/* 64 */       if (this.onlyMovement.isState()) {
/* 65 */         if (event.getPacket() instanceof net.minecraft.class_2828) {
/* 66 */           event.cancel();
/* 67 */           this.packets.add(event.getPacket());
/*    */         } 
/*    */       } else {
/* 70 */         event.cancel();
/* 71 */         this.packets.add(event.getPacket());
/*    */       } 
/*    */     }
/*    */   }
/*    */   
/*    */   private void releasePackets() {
/* 77 */     if (this.packets.isEmpty())
/*    */       return; 
/* 79 */     this.releasing = true;
/* 80 */     for (ObjectListIterator<class_2596> objectListIterator = this.packets.iterator(); objectListIterator.hasNext(); ) { class_2596<?> packet = objectListIterator.next();
/* 81 */       mc.field_1724.field_3944.method_52787(packet); }
/*    */     
/* 83 */     this.packets.clear();
/* 84 */     this.releasing = false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 89 */     super.onDisable();
/* 90 */     releasePackets();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\movement\PlayerFakeLags.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */