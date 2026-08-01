/*     */ package shame.astra.mixin;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import net.minecraft.class_1268;
/*     */ import net.minecraft.class_1306;
/*     */ import net.minecraft.class_1309;
/*     */ import net.minecraft.class_1657;
/*     */ import net.minecraft.class_1799;
/*     */ import net.minecraft.class_310;
/*     */ import net.minecraft.class_3532;
/*     */ import net.minecraft.class_4587;
/*     */ import net.minecraft.class_4597;
/*     */ import net.minecraft.class_742;
/*     */ import net.minecraft.class_746;
/*     */ import net.minecraft.class_759;
/*     */ import net.minecraft.class_7833;
/*     */ import org.spongepowered.asm.mixin.Mixin;
/*     */ import org.spongepowered.asm.mixin.Overwrite;
/*     */ import org.spongepowered.asm.mixin.Shadow;
/*     */ import org.spongepowered.asm.mixin.injection.At;
/*     */ import org.spongepowered.asm.mixin.injection.Inject;
/*     */ import org.spongepowered.asm.mixin.injection.ModifyArg;
/*     */ import org.spongepowered.asm.mixin.injection.Redirect;
/*     */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*     */ import shame.astra.api.storages.implement.helpertstorages.enumvar.ModuleClass;
/*     */ import shame.astra.api.utils.render.hands.ShaderHandsRenderer;
/*     */ import shame.astra.client.modules.impl.combat.Aura;
/*     */ import shame.astra.client.modules.impl.render.ShaderHands;
/*     */ import shame.astra.client.modules.impl.render.SwingAnimations;
/*     */ import shame.astra.client.modules.impl.render.ViewModel;
/*     */ 
/*     */ @Mixin({class_759.class})
/*     */ public abstract class HeldItemRendererMixin {
/*     */   @Shadow
/*     */   private class_1799 field_4047;
/*     */   @Shadow
/*     */   private float field_4043;
/*     */   @Shadow
/*     */   private float field_4053;
/*     */   
/*     */   @Inject(method = {"method_22976"}, at = {@At("HEAD")})
/*     */   private void onRenderItemHead(float tickProgress, class_4587 matrices, class_4597.class_4598 immediate, class_746 player, int light, CallbackInfo ci) {
/*  43 */     ShaderHands shaderHands = getShaderHands();
/*  44 */     if (shaderHands == null || !shaderHands.isEnable())
/*  45 */       return;  ShaderHandsRenderer.getInstance().captureBeforeHands(); } @Shadow
/*     */   private float field_4051; @Shadow
/*     */   private float field_4052; @Shadow
/*     */   private class_1799 field_4048; @Shadow
/*     */   protected abstract void method_3228(class_742 paramclass_742, float paramFloat1, float paramFloat2, class_1268 paramclass_1268, float paramFloat3, class_1799 paramclass_1799, float paramFloat4, class_4587 paramclass_4587, class_4597 paramclass_4597, int paramInt); @Inject(method = {"method_22976"}, at = {@At("TAIL")})
/*  50 */   private void onRenderItemTail(float tickProgress, class_4587 matrices, class_4597.class_4598 immediate, class_746 player, int light, CallbackInfo ci) { ShaderHands shaderHands = getShaderHands();
/*  51 */     if (shaderHands == null || !shaderHands.isEnable())
/*  52 */       return;  ShaderHandsRenderer.getInstance().captureAfterHands(); }
/*     */ 
/*     */ 
/*     */   
/*     */   @Redirect(method = {"method_22976"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/class_759;method_3228(Lnet/minecraft/class_742;FFLnet/minecraft/class_1268;FLnet/minecraft/class_1799;FLnet/minecraft/class_4587;Lnet/minecraft/class_4597;I)V"))
/*     */   private void onRenderFirstPersonItemCall(class_759 instance, class_742 player, float tickDelta, float pitch, class_1268 hand, float swingProgress, class_1799 stack, float equipProgress, class_4587 matrices, class_4597 vertexConsumers, int light) {
/*  58 */     class_1268 renderHand = hand;
/*  59 */     SwingAnimations tweaks = getTweaks();
/*  60 */     if (tweaks != null && tweaks.isEnable() && !tweaks.hmiEnable.isState() && tweaks.swapHands.isState()) {
/*  61 */       renderHand = (hand == class_1268.field_5808) ? class_1268.field_5810 : class_1268.field_5808;
/*     */     }
/*  63 */     ((HeldItemRendererInvoker)instance).whylol$callRenderFirstPersonItem(player, tickDelta, pitch, renderHand, swingProgress, stack, equipProgress, matrices, vertexConsumers, light);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ModifyArg(method = {"method_3228"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/class_759;method_3219(Lnet/minecraft/class_4587;Lnet/minecraft/class_4597;IFFLnet/minecraft/class_1306;)V"), index = 5)
/*     */   private class_1306 swapEmptyHandArm(class_1306 arm) {
/*  75 */     SwingAnimations tweaks = getTweaks();
/*  76 */     if (tweaks != null && tweaks.isEnable() && !tweaks.hmiEnable.isState() && tweaks.swapHands.isState()) {
/*  77 */       return (arm == class_1306.field_6183) ? class_1306.field_6182 : class_1306.field_6183;
/*     */     }
/*  79 */     return arm;
/*     */   }
/*     */   
/*     */   @Inject(method = {"method_3228"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_4587;method_22903()V", shift = At.Shift.AFTER)})
/*     */   private void onRenderFirstPersonItem(class_742 player, float tickDelta, float pitch, class_1268 hand, float swingProgress, class_1799 stack, float equipProgress, class_4587 matrices, class_4597 vertexConsumers, int light, CallbackInfo ci) {
/*  84 */     ViewModel viewModel = getViewModel();
/*  85 */     if (viewModel == null || !viewModel.isEnable()) {
/*     */       return;
/*     */     }
/*     */     
/*  89 */     if (hand == class_1268.field_5808) {
/*  90 */       matrices.method_46416(viewModel.mainHandX.get(), viewModel.mainHandY.get(), viewModel.mainHandZ.get());
/*     */     } else {
/*  92 */       matrices.method_46416(viewModel.offHandX.get(), viewModel.offHandY.get(), viewModel.offHandZ.get());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Redirect(method = {"method_3228"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/class_759;method_65816(FFLnet/minecraft/class_4587;ILnet/minecraft/class_1306;)V", ordinal = 2))
/*     */   private void onSwingArm(class_759 instance, float swingProgress, float equipProgress, class_4587 matrices, int armX, class_1306 arm) {
/*     */     float f2, g, f1, panderAnim, anim, tilt, panderF, rotate;
/* 105 */     SwingAnimations tweaks = getTweaks();
/* 106 */     if (tweaks == null || !tweaks.isEnable() || tweaks.hmiEnable.isState() || !tweaks.swingEnabled.isState()) {
/* 107 */       callSwingArm(instance, swingProgress, equipProgress, matrices, armX, arm);
/*     */       return;
/*     */     } 
/* 110 */     Aura aura = (ModuleClass.INSTANCE != null) ? ModuleClass.aura : null;
/* 111 */     if (tweaks.auraTargetOnly.isState() && (
/* 112 */       aura == null || !aura.isEnable() || aura.getTarget() == null || !aura.getTarget().method_5805())) {
/* 113 */       callSwingArm(instance, swingProgress, equipProgress, matrices, armX, arm);
/*     */       
/*     */       return;
/*     */     } 
/* 117 */     if ((class_310.method_1551()).field_1724 != null) {
/* 118 */       class_1306 expectedSwingArm = (class_310.method_1551()).field_1724.method_6068();
/* 119 */       if (tweaks.swapHands.isState()) {
/* 120 */         expectedSwingArm = (expectedSwingArm == class_1306.field_6183) ? class_1306.field_6182 : class_1306.field_6183;
/*     */       }
/* 122 */       if (arm != expectedSwingArm) {
/* 123 */         callSwingArm(instance, swingProgress, equipProgress, matrices, armX, arm);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 128 */     int i = (arm == class_1306.field_6183) ? 1 : -1;
/* 129 */     float strength = tweaks.swingStrength.get();
/* 130 */     float sin1 = class_3532.method_15374(swingProgress * swingProgress * 3.1415927F);
/* 131 */     float sin2 = class_3532.method_15374(class_3532.method_15355(swingProgress) * 3.1415927F);
/*     */     
/* 133 */     switch (tweaks.swingType.getCurrent()) {
/*     */       case "Down":
/* 135 */         matrices.method_46416(i * 0.56F, -0.32F, -0.72F);
/* 136 */         matrices.method_22907(class_7833.field_40716.rotationDegrees((76 * i)));
/* 137 */         matrices.method_22907(class_7833.field_40716.rotationDegrees(sin2 * -5.0F * strength));
/* 138 */         matrices.method_22907(class_7833.field_40713.rotationDegrees(sin2 * -100.0F * strength));
/* 139 */         matrices.method_22907(class_7833.field_40714.rotationDegrees(sin2 * -155.0F * strength));
/* 140 */         matrices.method_22907(class_7833.field_40714.rotationDegrees(-100.0F));
/*     */         return;
/*     */       case "Poke":
/* 143 */         f2 = (float)Math.sin(swingProgress * 1.5707963267948966D * 2.0D);
/* 144 */         tilt = strength / 3.0F;
/* 145 */         matrices.method_46416(i * 0.56F, -0.52F, -0.72F);
/* 146 */         matrices.method_46416(0.0F, 0.0F, tilt * -f2);
/* 147 */         matrices.method_22907(class_7833.field_40716.rotationDegrees(75.0F * i));
/* 148 */         matrices.method_22907(class_7833.field_40718.rotationDegrees((-75.0F * strength / 4.0F * f2 - 60.0F) * i));
/* 149 */         matrices.method_22907(class_7833.field_40714.rotationDegrees(-75.0F));
/*     */         return;
/*     */       case "Static":
/* 152 */         matrices.method_46416(i * 0.56F, -0.42F, -0.72F);
/* 153 */         matrices.method_22907(class_7833.field_40714.rotationDegrees(sin2 * -60.0F * strength));
/* 154 */         matrices.method_22904(0.0D, -0.1D, 0.0D);
/*     */         return;
/*     */       case "Feast":
/* 157 */         matrices.method_46416(i * 0.56F, -0.32F, -0.72F);
/* 158 */         matrices.method_22907(class_7833.field_40716.rotationDegrees((30 * i)));
/* 159 */         matrices.method_22907(class_7833.field_40716.rotationDegrees(sin2 * 75.0F * i * strength));
/* 160 */         matrices.method_22907(class_7833.field_40714.rotationDegrees(sin2 * -65.0F * strength));
/* 161 */         matrices.method_22907(class_7833.field_40716.rotationDegrees((30 * i)));
/* 162 */         matrices.method_22907(class_7833.field_40714.rotationDegrees(-80.0F));
/* 163 */         matrices.method_22907(class_7833.field_40716.rotationDegrees((35 * i)));
/*     */         return;
/*     */       case "Akrien":
/* 166 */         matrices.method_46416(i * 0.65F, -0.32F, -0.72F);
/* 167 */         matrices.method_22907(class_7833.field_40716.rotationDegrees((76 * i)));
/* 168 */         matrices.method_22907(class_7833.field_40716.rotationDegrees(sin2 * -5.0F * strength));
/* 169 */         matrices.method_22907(class_7833.field_40713.rotationDegrees(sin2 * -100.0F * strength));
/* 170 */         matrices.method_22907(class_7833.field_40714.rotationDegrees(sin2 * -155.0F * strength));
/* 171 */         matrices.method_22907(class_7833.field_40714.rotationDegrees(-100.0F));
/* 172 */         matrices.method_22907(class_7833.field_40716.rotationDegrees(sin2 * 25.0F * strength));
/* 173 */         matrices.method_22907(class_7833.field_40713.rotationDegrees(sin2 * -25.0F * strength));
/* 174 */         matrices.method_22907(class_7833.field_40713.rotationDegrees(sin1 * 15.0F * strength));
/* 175 */         matrices.method_46416(sin2 * 0.18F * strength, sin2 * 0.59F * strength, 0.0F); return;
/*     */       case "Smooth":
/* 177 */         applySwingOffset(matrices, i, swingProgress, strength); return;
/*     */       case "Block":
/* 179 */         if (swingProgress > 0.0F) {
/* 180 */           float f = class_3532.method_15374(class_3532.method_15355(swingProgress) * 3.1415927F);
/* 181 */           matrices.method_46416(0.56F * i, equipProgress * -0.2F - 0.5F, -0.7F);
/* 182 */           matrices.method_22907(class_7833.field_40716.rotationDegrees((45 * i)));
/* 183 */           matrices.method_22907(class_7833.field_40714.rotationDegrees(f * -85.0F * strength));
/* 184 */           matrices.method_46416(-0.1F * i, 0.28F, 0.2F);
/* 185 */           matrices.method_22907(class_7833.field_40714.rotationDegrees(-85.0F));
/*     */         } else {
/* 187 */           float n = -0.4F * class_3532.method_15374(class_3532.method_15355(swingProgress) * 3.1415927F);
/* 188 */           float m = 0.2F * class_3532.method_15374(class_3532.method_15355(swingProgress) * 6.2831855F);
/* 189 */           float f3 = -0.2F * class_3532.method_15374(swingProgress * 3.1415927F);
/* 190 */           matrices.method_46416(n * i * strength, m * strength, f3 * strength);
/* 191 */           applyEquipOffset(matrices, i, equipProgress);
/* 192 */           applySwingOffset(matrices, i, swingProgress, strength);
/*     */         } 
/*     */         return;
/*     */       case "ToBack":
/* 196 */         g = class_3532.method_15374(class_3532.method_15355(swingProgress) * 3.1415927F);
/* 197 */         matrices.method_46416(0.65F * i, -0.45F, -0.9F);
/* 198 */         matrices.method_22907(class_7833.field_40714.rotationDegrees(50.0F));
/* 199 */         matrices.method_22907(class_7833.field_40716.rotationDegrees((-30.0F * (1.0F - g * strength) - 30.0F) * i));
/* 200 */         matrices.method_22907(class_7833.field_40718.rotationDegrees(110.0F * i));
/*     */         return;
/*     */       case "SelfBack":
/* 203 */         f1 = (float)Math.sin(swingProgress * 1.5707963267948966D * 2.0D);
/* 204 */         matrices.method_46416(0.65F * i, -0.3F, -0.8F);
/* 205 */         matrices.method_22907(class_7833.field_40716.rotationDegrees((90 * i)));
/* 206 */         matrices.method_22907(class_7833.field_40718.rotationDegrees((-70 * i)));
/* 207 */         matrices.method_22907(class_7833.field_40714.rotationDegrees(-100.0F - 60.0F * strength * f1)); return;
/*     */       case "Break":
/*     */       case "Брик":
/* 210 */         matrices.method_46416(0.66F * i, -0.3F, -0.38F);
/* 211 */         matrices.method_22907(class_7833.field_40716.rotationDegrees((270 * i)));
/* 212 */         matrices.method_22907(class_7833.field_40714.rotationDegrees(sin2 * 10.0F * strength));
/*     */         
/* 214 */         matrices.method_22905(0.5F, 0.5F, 0.5F);
/* 215 */         matrices.method_46416(-0.1F * i, 0.2F, 0.0F);
/*     */         
/* 217 */         matrices.method_22907(class_7833.field_40716.rotationDegrees(-10.0F * i));
/* 218 */         matrices.method_22907(class_7833.field_40714.rotationDegrees(90.0F));
/* 219 */         matrices.method_22907(class_7833.field_40716.rotationDegrees(-105.0F * i));
/*     */         return;
/*     */       case "DropDown":
/* 222 */         f1 = (float)Math.sin(swingProgress * 1.5707963267948966D * 2.0D);
/* 223 */         applyEquipOffset(matrices, i, 0.0F);
/* 224 */         matrices.method_22907(class_7833.field_40716.rotationDegrees(80.0F));
/* 225 */         matrices.method_22907(class_7833.field_40714.rotationDegrees(tweaks.corner.get()));
/* 226 */         matrices.method_22907(class_7833.field_40714.rotationDegrees(-tweaks.slant.get() * f1 * strength));
/*     */         return;
/*     */       case "Pander":
/* 229 */         panderAnim = class_3532.method_15374(swingProgress * 3.1415927F);
/* 230 */         panderF = 1.0F - equipProgress;
/* 231 */         matrices.method_46416(i * 0.56F, -0.52F, -0.72F);
/* 232 */         matrices.method_46416((0.3F - panderAnim * 0.15F) * i, 0.2F - panderF * 0.12F, -0.15F - panderAnim * 0.13F);
/* 233 */         matrices.method_22907(class_7833.field_40716.rotationDegrees((76.0F - 10.0F * panderAnim) * i));
/* 234 */         matrices.method_22907(class_7833.field_40718.rotationDegrees((-16.0F - 8.0F * panderAnim) * i));
/* 235 */         matrices.method_22907(class_7833.field_40714.rotationDegrees(-83.0F - 26.0F * panderAnim));
/*     */         return;
/*     */       case "Slant":
/* 238 */         anim = (float)Math.sin(swingProgress * 1.5707963267948966D * 2.0D);
/* 239 */         rotate = 35.0F * strength;
/* 240 */         matrices.method_46416(i * 0.56F, -0.52F, -0.72F);
/* 241 */         matrices.method_46416(0.0F, 0.0F, -0.3F * anim * strength);
/* 242 */         matrices.method_22907(class_7833.field_40714.rotationDegrees(anim * -rotate));
/* 243 */         matrices.method_22907(class_7833.field_40718.rotationDegrees(anim * rotate)); return;
/*     */     } 
/* 245 */     callSwingArm(instance, swingProgress, equipProgress, matrices, armX, arm);
/*     */   }
/*     */ 
/*     */   
/*     */   @Overwrite
/*     */   public void method_22976(float tickDelta, class_4587 matrices, class_4597.class_4598 vertexConsumers, class_746 player, int light) {
/* 251 */     float f = player.method_6055(tickDelta);
/* 252 */     class_1268 hand = (class_1268)MoreObjects.firstNonNull(player.field_6266, class_1268.field_5808);
/* 253 */     float g = player.method_61414(tickDelta);
/* 254 */     (class_759)this; class_759.class_5773 handRenderType = class_759.method_33303(player);
/* 255 */     float h = class_3532.method_16439(tickDelta, player.field_3914, player.field_3916);
/* 256 */     float i = class_3532.method_16439(tickDelta, player.field_3931, player.field_3932);
/*     */ 
/*     */     
/* 259 */     if (handRenderType.field_28387) {
/* 260 */       float j = (hand == class_1268.field_5808) ? f : 0.0F;
/* 261 */       float k = 1.0F - class_3532.method_16439(tickDelta, this.field_4053, this.field_4043);
/* 262 */       method_3228((class_742)player, tickDelta, g, class_1268.field_5808, j, this.field_4047, k, matrices, (class_4597)vertexConsumers, light);
/*     */     } 
/*     */     
/* 265 */     if (handRenderType.field_28388) {
/* 266 */       float j = (hand == class_1268.field_5810) ? f : 0.0F;
/* 267 */       float k = 1.0F - class_3532.method_16439(tickDelta, this.field_4051, this.field_4052);
/* 268 */       method_3228((class_742)player, tickDelta, g, class_1268.field_5810, j, this.field_4048, k, matrices, (class_4597)vertexConsumers, light);
/*     */     } 
/*     */     
/* 271 */     vertexConsumers.method_22993();
/*     */   }
/*     */   
/*     */   @Inject(method = {"method_3218"}, at = {@At("HEAD")}, cancellable = true)
/*     */   private void onApplyEatOrDrinkTransformation(class_4587 matrices, float tickDelta, class_1306 arm, class_1799 stack, class_1657 player, CallbackInfo ci) {
/* 276 */     SwingAnimations tweaks = getTweaks();
/* 277 */     if (tweaks == null || !tweaks.isEnable() || tweaks.hmiEnable.isState() || !tweaks.eatAnim.isState() || !player.method_6115()) {
/*     */       return;
/*     */     }
/*     */     
/* 281 */     applyEatOrDrinkTransformationCustom(matrices, tickDelta, arm, stack);
/* 282 */     ci.cancel();
/*     */   }
/*     */   
/*     */   private void applyEatOrDrinkTransformationCustom(class_4587 matrices, float tickDelta, class_1306 arm, class_1799 stack) {
/* 286 */     if ((class_310.method_1551()).field_1724 == null) {
/*     */       return;
/*     */     }
/*     */     
/* 290 */     float f = (class_310.method_1551()).field_1724.method_6014() - tickDelta + 1.0F;
/* 291 */     float g = f / stack.method_7935((class_1309)(class_310.method_1551()).field_1724);
/*     */     
/* 293 */     if (g < 0.8F) {
/* 294 */       float f1 = class_3532.method_15379(class_3532.method_15362(f / 4.0F * 3.1415927F) * 0.005F);
/* 295 */       matrices.method_46416(0.0F, f1, 0.0F);
/*     */     } 
/*     */     
/* 298 */     float h = 1.0F - (float)Math.pow(g, 27.0D);
/* 299 */     int i = (arm == class_1306.field_6183) ? 1 : -1;
/*     */     
/* 301 */     float offsetX = 0.0F;
/* 302 */     float offsetY = 0.0F;
/* 303 */     float offsetZ = 0.0F;
/*     */     
/* 305 */     ViewModel viewModel = getViewModel();
/* 306 */     if (viewModel != null && viewModel.isEnable()) {
/* 307 */       if (arm == class_1306.field_6183) {
/* 308 */         offsetX = viewModel.mainHandX.get();
/* 309 */         offsetY = viewModel.mainHandY.get();
/* 310 */         offsetZ = viewModel.mainHandZ.get();
/*     */       } else {
/* 312 */         offsetX = viewModel.offHandX.get();
/* 313 */         offsetY = viewModel.offHandY.get();
/* 314 */         offsetZ = viewModel.offHandZ.get();
/*     */       } 
/*     */     }
/*     */     
/* 318 */     matrices.method_46416(h * 0.6F * i + offsetX, h * -0.5F + offsetY, offsetZ);
/* 319 */     matrices.method_22907(class_7833.field_40716.rotationDegrees(i * h * 90.0F));
/* 320 */     matrices.method_22907(class_7833.field_40714.rotationDegrees(h * 10.0F));
/* 321 */     matrices.method_22907(class_7833.field_40718.rotationDegrees(i * h * 30.0F));
/*     */   }
/*     */   
/*     */   private void applyEquipOffset(class_4587 matrices, int i, float equipProgress) {
/* 325 */     matrices.method_46416(i * 0.56F, -0.52F + equipProgress * -0.6F, -0.72F);
/*     */   }
/*     */   
/*     */   private void applySwingOffset(class_4587 matrices, int i, float swingProgress, float strength) {
/* 329 */     float f = class_3532.method_15374(swingProgress * swingProgress * 3.1415927F);
/* 330 */     matrices.method_46416(0.56F * i, -0.52F, -0.72F);
/* 331 */     matrices.method_22907(class_7833.field_40716.rotationDegrees(i * (45.0F + f * -20.0F * strength)));
/* 332 */     float g = class_3532.method_15374(class_3532.method_15355(swingProgress) * 3.1415927F);
/* 333 */     matrices.method_22907(class_7833.field_40718.rotationDegrees(i * g * -20.0F * strength));
/* 334 */     matrices.method_22907(class_7833.field_40714.rotationDegrees(g * -80.0F * strength));
/* 335 */     matrices.method_22907(class_7833.field_40716.rotationDegrees(i * -45.0F));
/*     */   }
/*     */   
/*     */   private void callSwingArm(class_759 instance, float swingProgress, float equipProgress, class_4587 matrices, int armX, class_1306 arm) {
/* 339 */     ((HeldItemRendererInvoker)instance).whylol$callSwingArm(swingProgress, equipProgress, matrices, armX, arm);
/*     */   }
/*     */   
/*     */   private SwingAnimations getTweaks() {
/* 343 */     if (ModuleClass.INSTANCE == null) {
/* 344 */       return null;
/*     */     }
/* 346 */     return ModuleClass.swingAnimations;
/*     */   }
/*     */   
/*     */   private ViewModel getViewModel() {
/* 350 */     if (ModuleClass.INSTANCE == null) {
/* 351 */       return null;
/*     */     }
/* 353 */     return ModuleClass.viewModel;
/*     */   }
/*     */   
/*     */   private ShaderHands getShaderHands() {
/* 357 */     if (ModuleClass.INSTANCE == null) {
/* 358 */       return null;
/*     */     }
/* 360 */     return ModuleClass.shaderHands;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\mixin\HeldItemRendererMixin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */