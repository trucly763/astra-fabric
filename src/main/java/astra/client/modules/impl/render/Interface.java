/*     */ package shame.astra.client.modules.impl.render;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import shame.astra.api.events.implement.EventRender;
/*     */ import shame.astra.astra;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.impl.render.base.InterfaceProcessing;
/*     */ import shame.astra.client.modules.impl.render.base.implement.HelperBinds;
/*     */ import shame.astra.client.modules.impl.render.base.implement.Information;
/*     */ import shame.astra.client.modules.impl.render.base.implement.KeyBinds;
/*     */ import shame.astra.client.modules.impl.render.base.implement.KeyStrokes;
/*     */ import shame.astra.client.modules.impl.render.base.implement.Notifications;
/*     */ import shame.astra.client.modules.impl.render.base.implement.Potions;
/*     */ import shame.astra.client.modules.impl.render.base.implement.StaffList;
/*     */ import shame.astra.client.modules.impl.render.base.implement.WaterMark;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*     */ import shame.astra.client.modules.settings.implement.ListSetting;
/*     */ 
/*     */ public class Interface extends Module {
/*  22 */   public static Interface INSTANCE = new Interface();
/*  23 */   public final BooleanSetting youGameWatermark = new BooleanSetting("YouGame HUD", true);
/*  24 */   private static final ConcurrentHashMap<String, Long> PERF_WARNINGS = new ConcurrentHashMap<>();
/*  25 */   private static final boolean PERF_DEBUG = Boolean.parseBoolean(System.getProperty("astra.perf.debug", "false"));
/*  26 */   private static final long SLOW_HUD_ELEMENT_NANOS = Long.getLong("astra.perf.hudMs", 5L).longValue() * 1000000L;
/*  27 */   private static final long PERF_WARN_COOLDOWN_NANOS = Long.getLong("astra.perf.cooldownMs", 1000L).longValue() * 1000000L;
/*     */   
/*     */   private final WaterMark waterMark;
/*     */   
/*     */   private final ArrayListHud arrayListHud;
/*     */   private final KeyBinds keyBinds;
/*     */   private final HelperBinds helperBinds;
/*     */   private final Potions potions;
/*     */   private final KeyStrokes keyStrokes;
/*     */   private final Notifications notifications;
/*     */   private final TargetHud targetHud;
/*     */   private final Session session;
/*     */   private final Information information;
/*     */   private final StaffList staffList;
/*  41 */   public ModeSetting style = new ModeSetting("Стиль", "Обычный", new String[] { "Обычный" });
/*     */   
/*  43 */   private final ListSetting hudModules = new ListSetting("Элементы", new BooleanSetting[] { new BooleanSetting("Ватермарка", true), new BooleanSetting("Аррай лист", true), new BooleanSetting("Горячие клавиши", true), new BooleanSetting("Серверные бинды", true), new BooleanSetting("Зелья", true), new BooleanSetting("Таргет худ", true), new BooleanSetting("Уведомления", true), new BooleanSetting("Стафф", true), (new BooleanSetting("Сессия", true))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  52 */         .visible(() -> Boolean.valueOf(this.style.is("Wave"))), (new BooleanSetting("КейСтроки", true))
/*  53 */         .visible(() -> Boolean.valueOf(this.style.is("Wave"))), new BooleanSetting("Информация", true) });
/*     */ 
/*     */   
/*     */   public Interface() {
/*  57 */     super("Interface", "Интерфейс клиента", Module.ModuleCategory.RENDER);
/*  58 */     addSettings(new Setting[] { (Setting)this.hudModules, (Setting)this.style, (Setting)this.youGameWatermark });
/*  59 */     this.waterMark = new WaterMark(astra.draggable(this, "WaterMark", 10.0F, 10.0F));
/*  60 */     this.arrayListHud = new ArrayListHud(astra.draggable(this, "ArrayList", 5.0F, 24.0F));
/*  61 */     this.keyBinds = new KeyBinds(astra.draggable(this, "KeyBinds", 30.0F, 30.0F));
/*  62 */     this.helperBinds = new HelperBinds(astra.draggable(this, "HelperBinds", 90.0F, 30.0F));
/*  63 */     this.potions = new Potions(astra.draggable(this, "Potions", 30.0F, 60.0F));
/*  64 */     this.staffList = new StaffList(astra.draggable(this, "StaffList", 60.0F, 100.0F));
/*  65 */     this.session = new Session(astra.draggable(this, "Session", 70.0F, 30.0F));
/*  66 */     this.keyStrokes = new KeyStrokes(astra.draggable(this, "KeyStrokes", 150.0F, 120.0F));
/*  67 */     this.information = new Information(astra.draggable(this, "Information", 50.0F, 100.0F));
/*  68 */     this.notifications = new Notifications(astra.draggable(this, "Notifications", 0.0F, 0.0F));
/*  69 */     this.targetHud = new TargetHud(astra.draggable(this, "TargetHud", 30.0F, 90.0F));
/*     */   }
/*     */   
/*     */   private void renderHudElement(InterfaceProcessing element, EventRender.Default event) {
/*  73 */     long start = PERF_DEBUG ? System.nanoTime() : 0L;
/*  74 */     element.draggable.beginRenderTilt(event.getContext().method_51448());
/*     */     try {
/*  76 */       element.onRender(event);
/*     */     } finally {
/*  78 */       element.draggable.endRenderTilt(event.getContext().method_51448());
/*  79 */       if (PERF_DEBUG) {
/*  80 */         long elapsed = System.nanoTime() - start;
/*  81 */         if (elapsed >= SLOW_HUD_ELEMENT_NANOS) {
/*  82 */           logSlowHudElement(element, elapsed);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void logSlowHudElement(InterfaceProcessing element, long elapsedNanos) {
/*  89 */     String name = element.getClass().getSimpleName();
/*  90 */     long now = System.nanoTime();
/*  91 */     Long lastWarn = PERF_WARNINGS.get(name);
/*  92 */     if (lastWarn != null && now - lastWarn.longValue() < PERF_WARN_COOLDOWN_NANOS) {
/*     */       return;
/*     */     }
/*  95 */     PERF_WARNINGS.put(name, Long.valueOf(now));
/*  96 */     System.out.println(String.format(Locale.ROOT, "[PerfDebug] Slow HUD element: Interface -> %s took %.2f ms", new Object[] { name, 
/*     */ 
/*     */             
/*  99 */             Double.valueOf(elapsedNanos / 1000000.0D) }));
/*     */   }
/*     */   
/*     */   public Map<String, InterfaceProcessing> getConfigurableHudElements() {
/* 103 */     Map<String, InterfaceProcessing> elements = new LinkedHashMap<>();
/* 104 */     elements.put("waterMark", this.waterMark);
/* 105 */     elements.put("arrayList", this.arrayListHud);
/* 106 */     elements.put("keyBinds", this.keyBinds);
/* 107 */     elements.put("helperBinds", this.helperBinds);
/* 108 */     elements.put("potions", this.potions);
/* 109 */     elements.put("keyStrokes", this.keyStrokes);
/* 110 */     elements.put("notifications", this.notifications);
/* 111 */     elements.put("targetHud", this.targetHud);
/* 112 */     elements.put("session", this.session);
/* 113 */     elements.put("information", this.information);
/* 114 */     elements.put("staffList", this.staffList);
/* 115 */     return elements;
/*     */   }
/*     */   
/*     */   @EventLink(priority = -200)
/*     */   public void onEvent(EventRender.Default event) {
/* 120 */     boolean waveStyle = this.style.is("Wave");
/* 121 */     boolean showWaterMark = this.hudModules.is("Ватермарка");
/* 122 */     boolean showArrayList = this.hudModules.is("Аррай лист");
/* 123 */     boolean showKeyBinds = this.hudModules.is("Горячие клавиши");
/* 124 */     boolean showHelperBinds = this.hudModules.is("Серверные бинды");
/* 125 */     boolean showPotions = this.hudModules.is("Зелья");
/* 126 */     boolean showKeyStrokes = this.hudModules.is("КейСтроки");
/* 127 */     boolean showInformation = this.hudModules.is("Информация");
/* 128 */     boolean showStaff = this.hudModules.is("Стафф");
/* 129 */     boolean showSession = this.hudModules.is("Сессия");
/* 130 */     boolean showNotifications = this.hudModules.is("Уведомления");
/* 131 */     boolean showTargetHud = this.hudModules.is("Таргет худ");
/*     */     
/* 133 */     RenderSystem.disableDepthTest();
/* 134 */     RenderSystem.depthMask(false);
/*     */     try {
/* 136 */       if (showWaterMark) renderHudElement((InterfaceProcessing)this.waterMark, event); 
/* 137 */       if (showArrayList && waveStyle) renderHudElement((InterfaceProcessing)this.arrayListHud, event); 
/* 138 */       if (showKeyBinds) renderHudElement((InterfaceProcessing)this.keyBinds, event); 
/* 139 */       if (showHelperBinds) renderHudElement((InterfaceProcessing)this.helperBinds, event); 
/* 140 */       if (showPotions) renderHudElement((InterfaceProcessing)this.potions, event); 
/* 141 */       if (showKeyStrokes && waveStyle) renderHudElement((InterfaceProcessing)this.keyStrokes, event); 
/* 142 */       if (showInformation) renderHudElement((InterfaceProcessing)this.information, event); 
/* 143 */       if (showStaff) renderHudElement((InterfaceProcessing)this.staffList, event); 
/* 144 */       if (showSession && waveStyle) renderHudElement((InterfaceProcessing)this.session, event); 
/* 145 */       if (showNotifications) renderHudElement((InterfaceProcessing)this.notifications, event); 
/* 146 */       if (showTargetHud) renderHudElement((InterfaceProcessing)this.targetHud, event); 
/*     */     } finally {
/* 148 */       RenderSystem.depthMask(true);
/* 149 */       RenderSystem.enableDepthTest();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\Interface.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */