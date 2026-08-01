/*     */ package shame.astra.client.ui;
/*     */ 
/*     */ import net.minecraft.class_1041;
/*     */ import net.minecraft.class_2561;
/*     */ import net.minecraft.class_332;
/*     */ import net.minecraft.class_3532;
/*     */ import net.minecraft.class_437;
/*     */ import shame.astra.api.QClient;
/*     */ import shame.astra.api.utils.animation.AnimationUtils;
/*     */ import shame.astra.api.utils.animation.Easings;
/*     */ import shame.astra.api.utils.client.ClientSoundPlayer;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.ui.clickgui.ClickGuiInputHandler;
/*     */ import shame.astra.client.ui.clickgui.ClickGuiRenderer;
/*     */ import shame.astra.client.ui.clickgui.ClickGuiSettingRenderer;
/*     */ import shame.astra.client.ui.clickgui.ClickGuiState;
/*     */ import shame.astra.client.ui.clickgui.ClickGuiThemeSelector;
/*     */ 
/*     */ public class MenuPanel
/*     */   extends class_437 implements QClient {
/*  21 */   private static final ClickGuiState SHARED_STATE = new ClickGuiState();
/*  22 */   private final int categoryCount = (Module.ModuleCategory.values()).length;
/*  23 */   private final ClickGuiState state = SHARED_STATE;
/*  24 */   private final ClickGuiThemeSelector themeSelector = new ClickGuiThemeSelector();
/*  25 */   private final ClickGuiRenderer renderer = new ClickGuiRenderer(this.state, new ClickGuiSettingRenderer(), this.themeSelector);
/*  26 */   private final ClickGuiInputHandler inputHandler = new ClickGuiInputHandler(this.state, this.themeSelector);
/*  27 */   private final AnimationUtils openAnimation = new AnimationUtils(0.0F, 7.5F, Easings.CUBIC_OUT);
/*     */   private boolean closing;
/*     */   private boolean closeSoundPlayed;
/*     */   
/*     */   public MenuPanel() {
/*  32 */     super(class_2561.method_30163("ClickGui"));
/*  33 */     this.state.refreshModules();
/*     */   }
/*     */   
/*     */   private class_1041 getWindow() {
/*  37 */     return (mc == null) ? null : mc.method_22683();
/*     */   }
/*     */   
/*     */   private void syncLayout() {
/*  41 */     class_1041 window = getWindow();
/*  42 */     if (window != null) {
/*  43 */       this.state.updatePosition(window, this.categoryCount);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void method_25420(class_332 context, int mouseX, int mouseY, float delta) {}
/*     */ 
/*     */   
/*     */   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
/*  53 */     class_1041 window = getWindow();
/*  54 */     if (window == null) {
/*     */       return;
/*     */     }
/*     */     
/*  58 */     updateAnimation();
/*  59 */     float progress = getAnimationProgress();
/*  60 */     if (this.closing && progress <= 0.001F) {
/*  61 */       if (mc != null) {
/*  62 */         mc.method_1507(null);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/*  67 */     this.state.updatePosition(window, this.categoryCount);
/*  68 */     this.state.setRenderOffsetY(getPanelOffsetY(progress));
/*  69 */     this.renderer.render(context, mouseX, mouseY, window, progress);
/*     */     
/*  71 */     super.method_25394(context, mouseX, mouseY, delta);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean method_25402(double mouseX, double mouseY, int button) {
/*  76 */     if (this.closing) return true; 
/*  77 */     syncLayout();
/*  78 */     this.state.setRenderOffsetY(getPanelOffsetY(getAnimationProgress()));
/*  79 */     return (this.inputHandler.mouseClicked(mouseX, mouseY, button, getWindow()) || super
/*  80 */       .method_25402(mouseX, mouseY, button));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean method_25406(double mouseX, double mouseY, int button) {
/*  85 */     if (this.closing) return true; 
/*  86 */     syncLayout();
/*  87 */     return (this.inputHandler.mouseReleased(button) || super.method_25406(mouseX, mouseY, button));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean method_25403(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
/*  92 */     if (this.closing) return true; 
/*  93 */     syncLayout();
/*  94 */     this.state.setRenderOffsetY(getPanelOffsetY(getAnimationProgress()));
/*  95 */     return (this.inputHandler.mouseDragged(mouseX, mouseY, button) || super
/*  96 */       .method_25403(mouseX, mouseY, button, deltaX, deltaY));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean method_25401(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
/* 101 */     if (this.closing) return true; 
/* 102 */     syncLayout();
/* 103 */     this.state.setRenderOffsetY(getPanelOffsetY(getAnimationProgress()));
/* 104 */     return (this.inputHandler.mouseScrolled(mouseX, mouseY, verticalAmount) || super
/* 105 */       .method_25401(mouseX, mouseY, horizontalAmount, verticalAmount));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean method_25404(int keyCode, int scanCode, int modifiers) {
/* 110 */     if (this.closing) return true; 
/* 111 */     if (this.inputHandler.keyPressed(keyCode, modifiers)) {
/* 112 */       return true;
/*     */     }
/* 114 */     if (keyCode == 256) {
/* 115 */       startClosing();
/* 116 */       return true;
/*     */     } 
/* 118 */     return super.method_25404(keyCode, scanCode, modifiers);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean method_25400(char chr, int modifiers) {
/* 123 */     if (this.closing) return true; 
/* 124 */     return (this.inputHandler.charTyped(chr) || super.method_25400(chr, modifiers));
/*     */   }
/*     */ 
/*     */   
/*     */   public void method_25419() {
/* 129 */     startClosing();
/*     */   }
/*     */ 
/*     */   
/*     */   public void method_25432() {
/* 134 */     if (!this.closeSoundPlayed) {
/* 135 */       this.closeSoundPlayed = true;
/* 136 */       ClientSoundPlayer.playSound("closegui.wav", 0.6D, 1.0F);
/*     */     } 
/* 138 */     super.method_25432();
/*     */   }
/*     */   
/*     */   private void startClosing() {
/* 142 */     if (this.closing) {
/*     */       return;
/*     */     }
/*     */     
/* 146 */     this.closing = true;
/* 147 */     this.openAnimation.setEasing(Easings.CUBIC_IN);
/*     */     
/* 149 */     if (!this.closeSoundPlayed) {
/* 150 */       this.closeSoundPlayed = true;
/* 151 */       ClientSoundPlayer.playSound("closegui.wav", 0.6D, 1.0F);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateAnimation() {
/* 156 */     if (this.closing) {
/* 157 */       this.openAnimation.update(0.0F);
/*     */     } else {
/* 159 */       this.openAnimation.setEasing(Easings.CUBIC_OUT);
/* 160 */       this.openAnimation.update(1.0F);
/*     */     } 
/*     */   }
/*     */   
/*     */   private float getAnimationProgress() {
/* 165 */     return class_3532.method_15363(this.openAnimation.getValue(), 0.0F, 1.0F);
/*     */   }
/*     */   
/*     */   private float getPanelOffsetY(float progress) {
/* 169 */     return (1.0F - progress) * 22.0F;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\clien\\ui\MenuPanel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */