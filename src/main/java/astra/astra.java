/*     */ package shame.astra;
/*     */ import java.io.File;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
/*     */ import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
/*     */ import net.minecraft.class_156;
/*     */ import org.lwjgl.glfw.GLFW;
/*     */ import shame.astra.api.QClient;
/*     */ import shame.astra.api.storages.InitializeStorage;
/*     */ import shame.astra.api.storages.implement.ConfigStorage;
/*     */ import shame.astra.api.storages.implement.DragStorage;
/*     */ import shame.astra.api.storages.implement.FreeLookStorage;
/*     */ import shame.astra.api.storages.implement.FriendStorage;
/*     */ import shame.astra.api.storages.implement.LocalizationStorage;
/*     */ import shame.astra.api.storages.implement.MacroStorage;
/*     */ import shame.astra.api.storages.implement.ModuleStorage;
/*     */ import shame.astra.api.storages.implement.StaffStorage;
/*     */ import shame.astra.api.utils.client.UserInfo;
/*     */ import shame.astra.api.utils.draggable.Draggable;
/*     */ import shame.astra.api.utils.tps.TPSCalc;
/*     */ import shame.astra.client.modules.Module;
/*     */ 
/*     */ public enum astra implements ModInitializer, QClient {
/*  24 */   INSTANCE;
/*     */   public File abItemsDir;
/*     */   public File configsDir;
/*     */   public File globalsDir;
/*     */   public UserInfo userInfo;
/*     */   public WaypointStorage waypointStorage;
/*     */   public StaffStorage staffStorage;
/*     */   public MacroStorage macroStorage;
/*     */   public FriendStorage friendStorage;
/*     */   public ConfigStorage configStorage;
/*     */   public LocalizationStorage localizationStorage;
/*     */   public CommandStorage commandStorage;
/*     */   public FreeLookStorage freeLookStorage;
/*     */   public RotationStorage rotationStorage;
/*     */   public ServerStorage serverStorage;
/*     */   public TPSCalc tpsCalc;
/*     */   public ThemeStorage themeStorage;
/*     */   public ModuleStorage moduleStorage;
/*     */   public InitializeStorage initializer;
/*     */   public static double deltaTime;
/*     */   private static double prevTime;
/*     */   public boolean isServer;
/*     */   private static final String[] STARTUP_LINKS;
/*     */   
/*     */   astra()
/*     */   {
/*  50 */     this.userInfo = UserInfo.empty(); } @Generated public UserInfo getUserInfo() { return this.userInfo; }
/*     */   
/*     */   static {
/*     */     STARTUP_LINKS = new String[] { "https://yougame.biz/userok/", "https://t.me/richpaster" };
/*     */     prevTime = 0.0D;
/*     */     deltaTime = 0.0D;
/*     */   }
/*     */   public void onInitialize() {
/*  58 */     initStorage();
/*  59 */     openStartupLinks();
/*  60 */     WorldRenderEvents.START.register(client -> {
/*     */           currentTime = GLFW.glfwGetTime();
/*     */           deltaTime = currentTime - prevTime;
/*     */           prevTime = currentTime;
/*     */           deltaTime = mc.method_1493() ? 0.0D : Math.min(0.05D, deltaTime);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private void initStorage() {
/*  70 */     this.globalsDir = new File("AstraBETA", "astra");
/*  71 */     this.configsDir = new File(this.globalsDir, "configs");
/*  72 */     this.abItemsDir = new File(this.globalsDir, "abitems");
/*     */     
/*  74 */     EventInvoker.register(this);
/*  75 */     createDirs(new File[] { this.globalsDir, this.configsDir, this.abItemsDir });
/*  76 */     this.initializer = new InitializeStorage();
/*  77 */     this.initializer.onInitialize();
/*     */   }
/*     */   
/*     */   private void openStartupLinks() {
/*  81 */     CompletableFuture.runAsync(() -> {
/*     */           v0 = STARTUP_LINKS; i1 = v0.length; for (i2 = 0; i2 < i1; i2++) {
/*     */             link = v0[i2]; try {
/*     */               class_156.method_668().method_670((String)link);
/*     */               Thread.sleep(150L);
/*  86 */             } catch (Exception exception) {}
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private void createDirs(File... file) {
/*  93 */     for (v2 = file, i3 = v2.length, i4 = 0; i4 < i3; ) { f = v2[i4]; f.mkdirs(); i4++; }
/*     */   
/*     */   }
/*     */   public void closeMinecraft() {
/*     */     try {
/*  98 */       this.configStorage.saveConfig(this.configStorage.currentConfig);
/*  99 */     } catch (Exception exception) {
/* 100 */       exception.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Draggable draggable(Module module, String name, float x, float y) {
/* 107 */     DragStorage.draggables.put(name, new Draggable(module, name, x, y));
/* 108 */     return (Draggable)DragStorage.draggables.get(name);
/*     */   }
/*     */   
/*     */   public void setUserInfo(UserInfo userInfo) {
/* 112 */     this.userInfo = (userInfo == null) ? UserInfo.empty() : userInfo;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\astra.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */