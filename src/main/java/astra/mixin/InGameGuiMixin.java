/*     */ package shame.astra.mixin;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import net.minecraft.class_1297;
/*     */ import net.minecraft.class_1657;
/*     */ import net.minecraft.class_2561;
/*     */ import net.minecraft.class_266;
/*     */ import net.minecraft.class_268;
/*     */ import net.minecraft.class_269;
/*     */ import net.minecraft.class_270;
/*     */ import net.minecraft.class_329;
/*     */ import net.minecraft.class_332;
/*     */ import net.minecraft.class_5250;
/*     */ import net.minecraft.class_5251;
/*     */ import net.minecraft.class_5348;
/*     */ import net.minecraft.class_9011;
/*     */ import net.minecraft.class_9022;
/*     */ import net.minecraft.class_9025;
/*     */ import net.minecraft.class_9779;
/*     */ import org.spongepowered.asm.mixin.Final;
/*     */ import org.spongepowered.asm.mixin.Mixin;
/*     */ import org.spongepowered.asm.mixin.Shadow;
/*     */ import org.spongepowered.asm.mixin.injection.At;
/*     */ import org.spongepowered.asm.mixin.injection.Inject;
/*     */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*     */ import shame.astra.api.QClient;
/*     */ import shame.astra.api.events.EventInvoker;
/*     */ import shame.astra.api.events.implement.EventRender;
/*     */ import shame.astra.api.storages.implement.helpertstorages.enumvar.ModuleClass;
/*     */ import shame.astra.api.utils.SidebarEntry;
/*     */ import shame.astra.client.modules.impl.misc.NameProtect;
/*     */ 
/*     */ @Mixin({class_329.class})
/*     */ public class InGameGuiMixin implements QClient {
/*     */   private static final int DOMAIN_COLOR = 15557921;
/*     */   
/*     */   @Inject(method = {"method_1735"}, at = {@At("HEAD")}, cancellable = true)
/*     */   private void renderVignetteOverlay(class_332 context, class_1297 entity, CallbackInfo ci) {
/*  39 */     if (ModuleClass.noVignette.isEnable())
/*  40 */       ci.cancel(); 
/*     */   } @Shadow
/*     */   @Final
/*     */   private class_310 field_2035;
/*     */   @Inject(method = {"method_1753"}, at = {@At("HEAD")})
/*     */   private void render(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
/*  46 */     BlurProgram.getInstance().beginFrame();
/*  47 */     if (EventInvoker.hasListeners(EventRender.Default.class)) {
/*  48 */       (new EventRender.Default(context, tickCounter.method_60637(true))).call();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Shadow
/*     */   private class_1657 method_1737() {
/*  62 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Inject(method = {"method_1757"}, at = {@At("HEAD")}, cancellable = true)
/*     */   private void astra$renderPatchedScoreboard(class_332 drawContext, class_266 objective, CallbackInfo ci) {
/*  72 */     if (!astra$shouldPatchScoreboard()) {
/*     */       return;
/*     */     }
/*     */     
/*  76 */     class_269 scoreboard = objective.method_1117();
/*  77 */     class_9022 numberFormat = objective.method_55380((class_9022)class_9025.field_47567);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  91 */     List<SidebarEntry> lines = scoreboard.method_1184(objective).stream().filter(entry -> !entry.method_55385()).sorted(Comparator.comparing(class_9011::comp_2128).reversed().thenComparing(class_9011::comp_2127, String.CASE_INSENSITIVE_ORDER)).limit(15L).map(entry -> { class_268 team = scoreboard.method_1164(entry.comp_2127()); class_2561 name = astra$patchText((class_2561)class_268.method_1142((class_270)team, entry.method_55387())); class_5250 class_5250 = entry.method_55386(numberFormat); int scoreWidth = this.field_2035.field_1772.method_27525((class_5348)class_5250); return new SidebarEntry(name, (class_2561)class_5250, scoreWidth); }).toList();
/*     */     
/*  93 */     class_2561 title = astra$patchText(objective.method_1114());
/*  94 */     int titleWidth = this.field_2035.field_1772.method_27525((class_5348)title);
/*  95 */     int maxWidth = titleWidth;
/*  96 */     int separatorWidth = this.field_2035.field_1772.method_1727(": ");
/*     */     
/*  98 */     for (SidebarEntry line : lines) {
/*  99 */       maxWidth = Math.max(maxWidth, this.field_2035.field_1772.method_27525((class_5348)line.name) + ((line.scoreWidth > 0) ? (separatorWidth + line.scoreWidth) : 0));
/*     */     }
/*     */     
/* 102 */     int lineCount = lines.size();
/* 103 */     int totalHeight = lineCount * 9;
/* 104 */     int bottom = drawContext.method_51443() / 2 + totalHeight / 3;
/* 105 */     int left = drawContext.method_51421() - maxWidth - 3;
/* 106 */     int right = drawContext.method_51421() - 1;
/* 107 */     int bodyColor = this.field_2035.field_1690.method_19345(0.3F);
/* 108 */     int headerColor = this.field_2035.field_1690.method_19345(0.4F);
/* 109 */     int top = bottom - lineCount * 9;
/*     */     
/* 111 */     drawContext.method_25294(left - 2, top - 10, right, top - 1, headerColor);
/* 112 */     drawContext.method_25294(left - 2, top - 1, right, bottom, bodyColor);
/* 113 */     drawContext.method_51439(this.field_2035.field_1772, title, left + maxWidth / 2 - titleWidth / 2, top - 9, -1, false);
/*     */     
/* 115 */     for (int index = 0; index < lineCount; index++) {
/* 116 */       SidebarEntry line = lines.get(index);
/* 117 */       int y = bottom - (lineCount - index) * 9;
/* 118 */       drawContext.method_51439(this.field_2035.field_1772, line.name, left, y, -1, false);
/* 119 */       drawContext.method_51439(this.field_2035.field_1772, line.score, right - line.scoreWidth, y, -1, false);
/*     */     } 
/*     */     
/* 122 */     ci.cancel();
/*     */   }
/*     */   
/*     */   private boolean astra$shouldPatchScoreboard() {
/* 126 */     if (ModuleClass.INSTANCE != null) if (ModuleClass.nameProtect != null) if (ModuleClass.nameProtect
/*     */           
/* 128 */           .isEnable());  
/*     */     return false;
/*     */   }
/*     */   private class_2561 astra$patchText(class_2561 text) {
/* 132 */     NameProtect nameProtect = ModuleClass.nameProtect;
/* 133 */     class_2561 patched = nameProtect.patchText(text);
/* 134 */     String patchedString = patched.getString();
/*     */     
/* 136 */     if (nameProtect.shouldHideGrief()) {
/* 137 */       if (patchedString.contains("Анархия-")) {
/* 138 */         patchedString = patchedString.replaceAll("Анархия-\\d+", "AstraBETA.fun");
/*     */       }
/* 140 */       if (patchedString.contains("ГРИФ #")) {
/* 141 */         patchedString = patchedString.replaceAll("ГРИФ #\\d+", "AstraBETA.fun");
/*     */       }
/*     */     } 
/*     */     
/* 145 */     if (patchedString.equals(patched.getString())) {
/* 146 */       return patched;
/*     */     }
/* 148 */     return (class_2561)class_2561.method_43470(patchedString).method_10862(patched.method_10866().method_27703(class_5251.method_27717(15557921)));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\mixin\InGameGuiMixin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */