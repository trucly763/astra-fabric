/*     */ package shame.astra.mixin;
/*     */ 
/*     */ import net.minecraft.class_10209;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_4063;
/*     */ import net.minecraft.class_4184;
/*     */ import net.minecraft.class_4587;
/*     */ import net.minecraft.class_4597;
/*     */ import net.minecraft.class_757;
/*     */ import net.minecraft.class_761;
/*     */ import net.minecraft.class_9779;
/*     */ import net.minecraft.class_9909;
/*     */ import net.minecraft.class_9922;
/*     */ import net.minecraft.class_9958;
/*     */ import org.joml.Matrix4f;
/*     */ import org.spongepowered.asm.mixin.Mixin;
/*     */ import org.spongepowered.asm.mixin.injection.At;
/*     */ import org.spongepowered.asm.mixin.injection.Inject;
/*     */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*     */ import shame.astra.api.QClient;
/*     */ import shame.astra.api.events.EventInvoker;
/*     */ import shame.astra.api.events.implement.Event3DRender;
/*     */ import shame.astra.api.storages.implement.helpertstorages.enumvar.ModuleClass;
/*     */ import shame.astra.client.modules.impl.render.Removals;
/*     */ import shame.astra.client.modules.impl.render.ShaderEsp;
/*     */ import shame.astra.client.modules.impl.render.Sonar;
/*     */ 
/*     */ @Mixin({class_761.class})
/*     */ public class WorldRendererMixin
/*     */   implements QClient {
/*     */   @Inject(method = {"method_62201"}, at = {@At("HEAD")}, cancellable = true)
/*     */   private void astra$renderParticles(class_9909 frameGraphBuilder, class_4184 camera, float tickDelta, class_9958 fog, CallbackInfo ci) {
/*  33 */     if (ModuleClass.INSTANCE == null)
/*     */       return; 
/*  35 */     Removals removals = ModuleClass.removals;
/*  36 */     if (removals != null && removals.isEnabled("Частицы")) {
/*  37 */       ci.cancel();
/*     */     }
/*     */   }
/*     */   
/*     */   @Inject(method = {"method_62203"}, at = {@At("HEAD")}, cancellable = true)
/*     */   private void astra$renderWeather(class_9909 frameGraphBuilder, class_243 pos, float tickDelta, class_9958 fog, CallbackInfo ci) {
/*  43 */     if (ModuleClass.INSTANCE == null)
/*     */       return; 
/*  45 */     Removals removals = ModuleClass.removals;
/*  46 */     if (removals != null && removals.isEnabled("Погода")) {
/*  47 */       ci.cancel();
/*     */     }
/*     */   }
/*     */   
/*     */   @Inject(method = {"method_62209"}, at = {@At("HEAD")}, cancellable = true)
/*     */   private void astra$addWeatherParticlesAndSound(class_4184 camera, CallbackInfo ci) {
/*  53 */     if (ModuleClass.INSTANCE == null)
/*     */       return; 
/*  55 */     Removals removals = ModuleClass.removals;
/*  56 */     if (removals != null && removals.isEnabled("Погода")) {
/*  57 */       ci.cancel();
/*     */     }
/*     */   }
/*     */   
/*     */   @Inject(method = {"method_62204"}, at = {@At("HEAD")}, cancellable = true)
/*     */   private void astra$renderClouds(class_9909 frameGraphBuilder, Matrix4f positionMatrix, Matrix4f projectionMatrix, class_4063 renderMode, class_243 cameraPos, float ticks, int color, float cloudHeight, CallbackInfo ci) {
/*  63 */     if (ModuleClass.INSTANCE == null)
/*     */       return; 
/*  65 */     Removals removals = ModuleClass.removals;
/*  66 */     if (removals != null && removals.isEnabled("Облака")) {
/*  67 */       ci.cancel();
/*     */     }
/*     */   }
/*     */   
/*     */   @Inject(method = {"method_62208"}, at = {@At("HEAD")}, cancellable = true)
/*     */   private void astra$renderBlockEntities(class_4587 matrices, class_4597.class_4598 mainConsumers, class_4597.class_4598 translucentConsumers, class_4184 camera, float tickDelta, CallbackInfo ci) {
/*  73 */     if (ModuleClass.INSTANCE == null)
/*     */       return; 
/*  75 */     Removals removals = ModuleClass.removals;
/*  76 */     if (removals != null && removals.isEnabled("Блок-сущности")) {
/*  77 */       ci.cancel();
/*     */     }
/*     */   }
/*     */   
/*     */   @Inject(method = {"method_22710"}, at = {@At("RETURN")})
/*     */   private void render(class_9922 allocator, class_9779 tickCounter, boolean renderBlockOutline, class_4184 camera, class_757 gameRenderer, Matrix4f positionMatrix, Matrix4f projectionMatrix, CallbackInfo ci) {
/*  83 */     Sonar sonar = (ModuleClass.INSTANCE != null) ? ModuleClass.sonar : null;
/*  84 */     boolean has3DListeners = EventInvoker.hasListeners(Event3DRender.class);
/*  85 */     boolean renderSonar = (sonar != null && sonar.isEnable());
/*  86 */     if (!has3DListeners && !renderSonar) {
/*     */       return;
/*     */     }
/*     */     
/*  90 */     class_10209.method_64146().method_15405("astra_renderWorld");
/*  91 */     class_4587 matrices = new class_4587();
/*  92 */     matrices.method_34425(positionMatrix);
/*  93 */     if (has3DListeners) {
/*  94 */       (new Event3DRender(matrices, positionMatrix, projectionMatrix, camera, tickCounter.method_60637(false))).call();
/*     */     }
/*  96 */     if (renderSonar) {
/*  97 */       sonar.renderFromMixin(positionMatrix, projectionMatrix, camera.method_19326());
/*     */     }
/*     */   }
/*     */   
/*     */   @Inject(method = {"method_3254"}, at = {@At("HEAD")}, cancellable = true)
/*     */   private void astra$drawEntityOutlinesFramebuffer(CallbackInfo ci) {
/* 103 */     if (ModuleClass.INSTANCE == null)
/* 104 */       return;  ShaderEsp shaderEsp = ModuleClass.shaderEsp;
/* 105 */     if (shaderEsp != null && shaderEsp.isEnable()) {
/* 106 */       ci.cancel();
/*     */       return;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Inject(method = {"method_22712"}, at = {@At("HEAD")}, cancellable = true)
/*     */   public void onDrawBlockOutline(CallbackInfo ci) {
/* 114 */     if (ModuleClass.blockOverlay.isEnable()) ci.cancel(); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\mixin\WorldRendererMixin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */