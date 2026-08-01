/*      */ package shame.astra.mixin;
/*      */ import java.util.Random;
/*      */ import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
/*      */ import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
/*      */ import net.minecraft.class_1268;
/*      */ import net.minecraft.class_1306;
/*      */ import net.minecraft.class_1309;
/*      */ import net.minecraft.class_1747;
/*      */ import net.minecraft.class_1764;
/*      */ import net.minecraft.class_1799;
/*      */ import net.minecraft.class_1802;
/*      */ import net.minecraft.class_1839;
/*      */ import net.minecraft.class_2246;
/*      */ import net.minecraft.class_2248;
/*      */ import net.minecraft.class_2394;
/*      */ import net.minecraft.class_243;
/*      */ import net.minecraft.class_2680;
/*      */ import net.minecraft.class_2769;
/*      */ import net.minecraft.class_310;
/*      */ import net.minecraft.class_3481;
/*      */ import net.minecraft.class_3489;
/*      */ import net.minecraft.class_3532;
/*      */ import net.minecraft.class_4587;
/*      */ import net.minecraft.class_4597;
/*      */ import net.minecraft.class_742;
/*      */ import net.minecraft.class_746;
/*      */ import net.minecraft.class_759;
/*      */ import net.minecraft.class_776;
/*      */ import net.minecraft.class_7833;
/*      */ import net.minecraft.class_811;
/*      */ import net.minecraft.class_9285;
/*      */ import net.minecraft.class_9334;
/*      */ import org.spongepowered.asm.mixin.Final;
/*      */ import org.spongepowered.asm.mixin.Shadow;
/*      */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*      */ import shame.astra.api.storages.implement.helpertstorages.enumvar.ModuleClass;
/*      */ import shame.astra.astra;
/*      */ import shame.astra.client.modules.impl.render.SwingAnimations;
/*      */ import shame.astra.client.modules.impl.render.ViewModel;
/*      */ 
/*      */ @Mixin({class_759.class})
/*      */ public abstract class HeldItemRendererHmiMixin {
/*      */   private boolean repPower = false;
/*   44 */   private float prevAge = 0.0F;
/*   45 */   private double previousRotation = 0.0D;
/*   46 */   private float swingAngleY = 0.0F;
/*   47 */   private float swingAngleX = 0.0F;
/*   48 */   private float swingVelocityY = 0.0F;
/*   49 */   private float swingVelocityX = 0.0F;
/*   50 */   private float swingVelocityZ = 0.0F;
/*      */   private static final float GRAVITY = 0.1F;
/*      */   private static final float DAMPING = 0.88F;
/*      */   private static final float SENSITIVITY = 0.015F;
/*   54 */   private float vertAngleY = 0.0F;
/*   55 */   private float vertVelocityY = 0.0F;
/*   56 */   private float vertVelocityYSlime = 0.0F;
/*   57 */   private float vertAngleYSlime = 0.0F;
/*   58 */   private float riptideCounter = 0.0F;
/*   59 */   private float netherCounter = 0.0F;
/*      */   @Shadow
/*      */   private class_1799 field_4047;
/*      */   @Shadow
/*      */   @Final
/*      */   private class_310 field_4050;
/*   65 */   private float fallCounter = 0.0F;
/*   66 */   private float inWaterCounter = 0.0F;
/*   67 */   private float inspect = 0.0F;
/*   68 */   private float tilt = 0.0F;
/*   69 */   private float freezeCounter = 0.0F;
/*   70 */   private float clCount = 0.0F;
/*   71 */   private float crawlCount = 0.0F;
/*   72 */   private float directionalCrawlCount = 0.0F;
/*   73 */   private float climbCount = 0.0F;
/*   74 */   private float mouseHolding = 1.0F;
/*      */   private boolean isSwinging = false;
/*   76 */   private float swingProgress = 0.0F; private boolean isForward = false; private boolean isAttacking = false; private boolean left = false; @Shadow
/*      */   private float field_4043; @Shadow
/*      */   private float field_4053; @Shadow
/*      */   private float field_4051; @Shadow
/*      */   private float field_4052; @Shadow
/*      */   private class_1799 field_4048; private float easeInOutBack(float x) {
/*   82 */     float c1 = 1.70158F;
/*   83 */     float c2 = c1 * 1.525F;
/*   84 */     return (float)((x < 0.5D) ? (Math.pow((2.0F * x), 2.0D) * ((c2 + 1.0F) * 2.0F * x - c2) / 2.0D) : ((Math.pow((2.0F * x - 2.0F), 2.0D) * ((c2 + 1.0F) * (x * 2.0F - 2.0F) + c2) + 2.0D) / 2.0D));
/*      */   }
/*      */   
/*      */   private float getAttackDamage(class_1799 stack) {
/*   88 */     class_9285 modifiers = (class_9285)stack.method_57353().method_57829(class_9334.field_49636);
/*   89 */     if (modifiers == null) {
/*   90 */       return 0.0F;
/*      */     }
/*   92 */     float totalDamage = 0.0F;
/*      */     
/*   94 */     for (class_9285.class_9287 entry : modifiers.comp_2393()) {
/*   95 */       if (entry.comp_2395().comp_349() == class_5134.field_23721.comp_349()) {
/*   96 */         totalDamage += (float)entry.comp_2396().comp_2449();
/*      */       }
/*      */     } 
/*      */     
/*  100 */     return totalDamage;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean isSharpAnimation(SwingAnimations config) {
/*  105 */     return (config != null && config.hmiAnimationType.is("Шарп"));
/*      */   }
/*      */   
/*      */   private void altSwing(class_4587 matrices, class_1306 arm, float swingProgress, class_1799 item) {
/*  109 */     int i = (arm == class_1306.field_6183) ? 1 : -1;
/*  110 */     float f = class_3532.method_15374(swingProgress * 3.14F);
/*  111 */     matrices.method_22907(class_7833.field_40716.rotationDegrees(i * (45.0F + f * 0.0F)));
/*  112 */     matrices.method_22907(class_7833.field_40716.rotationDegrees(i * -45.0F));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Inject(method = {"method_3228"}, at = {@At("HEAD")}, cancellable = true)
/*      */   private void onRenderFirstPersonItem(class_742 player, float tickDelta, float pitch, class_1268 hand, float swingProgress, class_1799 item, float equipProgress, class_4587 matrices, class_4597 vertexConsumers, int light, CallbackInfo ci) {
/*  133 */     SwingAnimations swings = ModuleClass.swingAnimations;
/*  134 */     if (!swings.isEnable() || !swings.hmiEnable.isState()) {
/*      */       return;
/*      */     }
/*      */     
/*  138 */     boolean isMainHand = (hand == class_1268.field_5808);
/*  139 */     class_1306 arm = isMainHand ? player.method_6068() : player.method_6068().method_5928();
/*  140 */     float sideFactor = isMainHand ? 1.0F : -1.0F;
/*      */     
/*  142 */     if (swings.swapHands.isState()) {
/*  143 */       arm = arm.method_5928();
/*  144 */       sideFactor *= -1.0F;
/*      */     } 
/*      */     
/*  147 */     renderCustomFirstPersonItem(player, tickDelta, pitch, hand, arm, sideFactor, swingProgress, item, equipProgress, matrices, vertexConsumers, light);
/*      */     
/*  149 */     ci.cancel();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void renderCustomFirstPersonItem(class_742 player, float tickDelta, float pitch, class_1268 hand, class_1306 arm, float sideFactor, float swingProgress, class_1799 item, float equipProgress, class_4587 matrices, class_4597 vertexConsumers, int light) {
/*  157 */     SwingAnimations swings = ModuleClass.swingAnimations;
/*  158 */     if (swings.isEnable() && swings.hmiEnable.isState() && 
/*  159 */       !player.method_31550()) {
/*  160 */       float al; SwingAnimations config = ModuleClass.swingAnimations;
/*  161 */       float yaw = player.method_36454();
/*  162 */       double radians = Math.toRadians(yaw);
/*  163 */       double forwardX = -Math.sin(radians);
/*  164 */       double forwardZ = Math.cos(radians);
/*  165 */       class_243 horizontalVelocity = player.method_18798();
/*  166 */       double dotProduct = horizontalVelocity.field_1352 * forwardX + horizontalVelocity.field_1350 * forwardZ;
/*  167 */       double crossProduct = (player.method_18798().method_61890()).field_1352 * forwardZ - horizontalVelocity.field_1350 * forwardX;
/*      */       
/*  169 */       if (player.method_36455() != 0.0F) {
/*  170 */         al = 90.0F / player.method_36455() / 10.0F;
/*      */       } else {
/*  172 */         al = 1.0F;
/*      */       } 
/*      */       
/*  175 */       if (al > 1.0F) {
/*  176 */         al = 1.0F;
/*      */       }
/*      */       
/*  179 */       if (al < 0.0F) {
/*  180 */         al = 1.0F;
/*      */       }
/*      */       
/*  183 */       boolean bl = (hand == class_1268.field_5808);
/*  184 */       matrices.method_22903();
/*  185 */       matrices.method_22903();
/*  186 */       ViewModel viewModel = (ModuleClass.INSTANCE != null) ? ModuleClass.viewModel : null;
/*  187 */       if (viewModel != null && viewModel.isEnable()) {
/*  188 */         viewModel.applyHandPosition(matrices, arm);
/*      */       }
/*  190 */       double tt = astra.deltaTime * 30.0D;
/*  191 */       float smoothness = class_3532.method_15363(config.hmiSmoothness.get(), 0.35F, 2.5F);
/*  192 */       float hmiProgress = (float)Math.pow(class_3532.method_15363(swingProgress, 0.0F, 1.0F), smoothness);
/*  193 */       float swing_rot = (hmiProgress < 0.6D) ? class_3532.method_15374(class_3532.method_15363(hmiProgress, 0.0F, 0.12506F) * 12.56F) : class_3532.method_15374(class_3532.method_15363(hmiProgress, 0.62532F, 0.75038F) * 12.56F);
/*  194 */       float swing = class_3532.method_15374(hmiProgress * 3.14F);
/*  195 */       swing = easeInOutBack(swing);
/*  196 */       boolean sharpSword = (item.method_31573(class_3489.field_42611) && isSharpAnimation(config));
/*  197 */       if ((item.method_31574(class_1802.field_8287) || item.method_31574(class_1802.field_49098) || item.method_31574(class_1802.field_8803) || item.method_31574(class_1802.field_8449) || item.method_31574(class_1802.field_8543) || item.method_7909() instanceof net.minecraft.class_1828 || item.method_7909() instanceof net.minecraft.class_1803) && player.method_6079().method_7960() && item.method_7976() != class_1839.field_8951 && !item.method_31574(class_1802.field_8814) && !player.method_5681() && !player.method_20448() && !player.method_6101()) {
/*  198 */         if (player.method_6068() == class_1306.field_6182) {
/*  199 */           bl = !bl;
/*      */         }
/*      */         
/*  202 */         matrices.method_22903();
/*  203 */         matrices.method_22907(class_7833.field_40716.rotationDegrees(-25.0F * sideFactor));
/*  204 */         matrices.method_22907(class_7833.field_40714.rotationDegrees(-10.0F));
/*  205 */         matrices.method_22907(class_7833.field_40716.rotationDegrees(25.0F * sideFactor * swing));
/*  206 */         matrices.method_22907(class_7833.field_40714.rotationDegrees(30.0F * swing));
/*  207 */         matrices.method_22904(-0.15D * sideFactor, 0.1D, 0.1D);
/*  208 */         matrices.method_22904(0.0D, -0.55D * swing, 0.4D * swing * 3.140000104904175D);
/*  209 */         HeldItemRendererAccessor acc = (HeldItemRendererAccessor)this;
/*  210 */         acc.invokeRenderArmHoldingItem(matrices, vertexConsumers, light, equipProgress, 0.0F, arm.method_5928());
/*  211 */         matrices.method_22909();
/*      */       } 
/*      */       
/*  214 */       if (this.field_4050.field_1690.field_1886.method_1434() && !this.isAttacking && swingProgress == 0.0D) {
/*  215 */         this.left = !this.left;
/*      */       }
/*      */       
/*  218 */       if (!item.method_7960()) {
/*  219 */         if (player.method_6068() == class_1306.field_6182) {
/*  220 */           bl = !bl;
/*      */         }
/*      */ 
/*      */         
/*  224 */         if ((this.left || item.method_31573(class_3489.field_42612) || item.method_7976() == class_1839.field_8951 || item.method_7976() == class_1839.field_8949) && !item.method_31573(class_3489.field_42615)) {
/*  225 */           if (sharpSword) {
/*  226 */             matrices.method_22904(0.1D * sideFactor * swing_rot, 0.1D * swing_rot, -0.5D * swing);
/*  227 */             matrices.method_22907(class_7833.field_40713.rotationDegrees(-30.0F * swing_rot));
/*  228 */             matrices.method_22907(class_7833.field_40718.rotationDegrees(-20.0F * swing_rot * sideFactor));
/*  229 */             matrices.method_22907(class_7833.field_40713.rotationDegrees(40.0F * swing));
/*  230 */           } else if (!item.method_31573(class_3489.field_42611) && !item.method_31573(class_3489.field_42612)) {
/*  231 */             if (item.method_7976() == class_1839.field_8951) {
/*  232 */               matrices.method_22904(0.0D, 0.0D, 0.45D * swing_rot);
/*  233 */               matrices.method_22904(-0.25D * sideFactor * swing, -0.35D * swing_rot, -0.6D * swing);
/*  234 */               matrices.method_22904(0.0D, 0.1D * swing, 0.0D);
/*  235 */               matrices.method_22907(class_7833.field_40716.rotationDegrees(15.0F * swing_rot * sideFactor));
/*  236 */               matrices.method_22907(class_7833.field_40718.rotationDegrees(30.0F * swing_rot * sideFactor));
/*  237 */             } else if (item.method_31573(ConventionalItemTags.TOOLS) && item.method_7976() != class_1839.field_8949 && !item.method_31573(class_3489.field_42615)) {
/*  238 */               matrices.method_22904(0.1D * sideFactor * swing_rot, 0.1D * swing_rot, -0.5D * swing);
/*  239 */               matrices.method_22907(class_7833.field_40713.rotationDegrees(-30.0F * swing_rot));
/*  240 */               matrices.method_22907(class_7833.field_40718.rotationDegrees(-20.0F * swing_rot * sideFactor));
/*  241 */               matrices.method_22907(class_7833.field_40713.rotationDegrees(40.0F * swing));
/*  242 */             } else if (item.method_7976() != class_1839.field_8949) {
/*  243 */               matrices.method_22904(0.1D * sideFactor * swing_rot, 0.1D * swing_rot, -0.1D * swing);
/*  244 */               matrices.method_22907(class_7833.field_40713.rotationDegrees(-30.0F * swing_rot));
/*  245 */               matrices.method_22907(class_7833.field_40718.rotationDegrees(-10.0F * swing_rot * sideFactor));
/*  246 */               matrices.method_22907(class_7833.field_40713.rotationDegrees(40.0F * swing));
/*  247 */               matrices.method_22907(class_7833.field_40716.rotationDegrees(10.0F * swing * sideFactor));
/*      */             } else {
/*  249 */               matrices.method_22904(0.1D * sideFactor * swing_rot, 0.1D * swing_rot, -0.2D * swing);
/*  250 */               matrices.method_22907(class_7833.field_40713.rotationDegrees(-10.0F * swing_rot));
/*  251 */               matrices.method_22907(class_7833.field_40718.rotationDegrees(-10.0F * swing_rot * sideFactor));
/*  252 */               matrices.method_22907(class_7833.field_40713.rotationDegrees(20.0F * swing));
/*      */             } 
/*      */           } else {
/*  255 */             matrices.method_22904(0.8D * sideFactor * swing_rot, 0.3D * swing_rot, -0.5D * swing);
/*  256 */             matrices.method_22907(class_7833.field_40716.rotationDegrees(15.0F * swing_rot * sideFactor));
/*  257 */             matrices.method_22907(class_7833.field_40713.rotationDegrees(-20.0F * swing_rot));
/*  258 */             matrices.method_22907(class_7833.field_40718.rotationDegrees(-70.0F * swing_rot * sideFactor));
/*  259 */             if (item.method_31573(class_3489.field_42611)) {
/*  260 */               matrices.method_22907(class_7833.field_40713.rotationDegrees(40.0F * swing));
/*      */             } else {
/*  262 */               matrices.method_22907(class_7833.field_40713.rotationDegrees(30.0F * swing));
/*      */             } 
/*      */           } 
/*  265 */         } else if (!item.method_31573(class_3489.field_42615)) {
/*  266 */           if (sharpSword) {
/*  267 */             matrices.method_22904(0.1D * sideFactor * swing_rot, 0.1D * swing_rot, -0.5D * swing);
/*  268 */             matrices.method_22907(class_7833.field_40713.rotationDegrees(-30.0F * swing_rot));
/*  269 */             matrices.method_22907(class_7833.field_40718.rotationDegrees(-20.0F * swing_rot * sideFactor));
/*  270 */             matrices.method_22907(class_7833.field_40713.rotationDegrees(40.0F * swing));
/*  271 */           } else if (item.method_31573(class_3489.field_42611)) {
/*  272 */             matrices.method_22904(-0.55D * sideFactor * swing_rot, -0.8D * swing_rot, -0.77D * swing);
/*  273 */             matrices.method_22907(class_7833.field_40716.rotationDegrees(5.0F * swing_rot * sideFactor));
/*  274 */             matrices.method_22907(class_7833.field_40713.rotationDegrees(-30.0F * swing_rot));
/*  275 */             matrices.method_22907(class_7833.field_40718.rotationDegrees(70.0F * swing_rot * sideFactor));
/*  276 */             matrices.method_22907(class_7833.field_40713.rotationDegrees(50.0F * swing));
/*  277 */           } else if (item.method_31573(ConventionalItemTags.TOOLS) && !item.method_31573(class_3489.field_42615)) {
/*  278 */             matrices.method_22904(0.1D * sideFactor * swing_rot, 0.1D * swing_rot, -0.5D * swing);
/*  279 */             matrices.method_22907(class_7833.field_40713.rotationDegrees(-30.0F * swing_rot));
/*  280 */             matrices.method_22907(class_7833.field_40718.rotationDegrees(-20.0F * swing_rot * sideFactor));
/*  281 */             matrices.method_22907(class_7833.field_40713.rotationDegrees(40.0F * swing));
/*      */           } else {
/*  283 */             matrices.method_22904(0.1D * sideFactor * swing_rot, 0.1D * swing_rot, -0.1D * swing);
/*  284 */             matrices.method_22907(class_7833.field_40713.rotationDegrees(-30.0F * swing_rot));
/*  285 */             matrices.method_22907(class_7833.field_40718.rotationDegrees(-10.0F * swing_rot * sideFactor));
/*  286 */             matrices.method_22907(class_7833.field_40713.rotationDegrees(40.0F * swing));
/*  287 */             matrices.method_22907(class_7833.field_40716.rotationDegrees(10.0F * swing * sideFactor));
/*      */           } 
/*  289 */         } else if (item.method_31573(class_3489.field_42615)) {
/*  290 */           matrices.method_22904(0.0D, 0.15D * swing_rot, -0.25D * swing_rot);
/*  291 */           matrices.method_22904(0.0D, 0.0D, -0.2D * swing);
/*  292 */           matrices.method_22907(class_7833.field_40716.rotationDegrees(15.0F * swing_rot));
/*  293 */           matrices.method_22907(class_7833.field_40714.rotationDegrees(-35.0F * swing_rot));
/*  294 */           matrices.method_22907(class_7833.field_40714.rotationDegrees(30.0F * swing));
/*      */         } 
/*  296 */       } else if (class_2248.method_9503(item.method_7909()) != class_2246.field_10124 && (!item.method_31573(ConventionalItemTags.TOOLS) || item.method_31573(class_3489.field_41890) || item.method_31573(class_3489.field_40109) || item.method_7976() == class_1839.field_8950 || !item.method_7923()) && item.method_7976() != class_1839.field_8953 && item.method_7976() != class_1839.field_27079 && getAttackDamage(item) == 0.0F && item.method_7976() != class_1839.field_8949 && !item.method_31574(class_1802.field_23254) && !item.method_31574(class_1802.field_8184) && !item.method_31574(class_1802.field_8378) && !item.method_31574(class_1802.field_8868)) {
/*  297 */         swingProgress = (float)(swingProgress * 1.2D);
/*  298 */         if (swingProgress > 1.0F) {
/*  299 */           swingProgress = 0.0F;
/*      */         }
/*  301 */       } else if (!item.method_31573(class_3489.field_42615)) {
/*  302 */         swingProgress = (float)(swingProgress * 1.5D);
/*  303 */         if (swingProgress > 1.0F) {
/*  304 */           swingProgress = 0.0F;
/*      */         }
/*      */       } 
/*      */       
/*  308 */       if (player.method_18798().method_1033() >= 0.08D) {
/*  309 */         this.crawlCount = (float)(this.crawlCount + 0.1D * player.method_18798().method_1033() * 2.0D * tt);
/*  310 */         this.directionalCrawlCount = (float)(this.directionalCrawlCount + 0.1D * dotProduct * 4.0D * tt);
/*  311 */         this.directionalCrawlCount = (float)(this.directionalCrawlCount + ((dotProduct > 0.0D) ? (0.1D * Math.abs(crossProduct) * 4.0D * tt) : (0.1D * Math.abs(crossProduct) * -1.0D * 4.0D * tt)));
/*      */       } 
/*      */       
/*  314 */       if (player.method_18798().method_10214() > 0.0D) {
/*  315 */         this.climbCount = (float)(this.climbCount + 0.1D * tt);
/*      */       }
/*      */       
/*  318 */       if (player.method_18798().method_10214() < 0.0D) {
/*  319 */         this.climbCount = (float)(this.climbCount - 0.1D * tt);
/*      */       }
/*      */       
/*  322 */       if (((player.method_20448() && config.climbAndCrawl) || (player.method_6101() && !player.method_24828() && Math.abs(player.method_18798().method_10214()) > 0.0D && config.climbAndCrawl)) && !player.method_6115() && swingProgress == 0.0F) {
/*  323 */         this.clCount = (float)(this.clCount + 0.1D * tt);
/*  324 */         if (this.clCount > 1.0F) {
/*  325 */           this.clCount = 1.0F;
/*      */         }
/*      */         
/*  328 */         if (!item.method_31574(class_1802.field_16539) && !item.method_31574(class_1802.field_22016)) {
/*  329 */           matrices.method_22907(class_7833.field_40714.rotationDegrees(-20.0F * this.clCount));
/*      */         }
/*      */       } else {
/*  332 */         this.clCount = (float)(this.clCount * Math.pow(0.8799999952316284D, tt));
/*      */       } 
/*      */       
/*  335 */       if (swingProgress == 0.0F) {
/*  336 */         matrices.method_46416(bl ? (player.method_36455() / 650.0F * this.clCount * -1.0F) : (player.method_36455() / 650.0F * this.clCount), 0.0F, 0.0F);
/*  337 */         matrices.method_22907(class_7833.field_40714.rotationDegrees(player.method_36455() * this.clCount));
/*      */       } 
/*      */       
/*  340 */       if (!item.method_31574(class_1802.field_16539) && !item.method_31574(class_1802.field_22016)) {
/*  341 */         matrices.method_46416(0.0F, 0.0F, player.method_36455() / 120.0F * this.clCount);
/*  342 */       } else if (swingProgress == 0.0F) {
/*  343 */         matrices.method_46416(0.0F, 0.0F, player.method_36455() / 80.0F * this.clCount);
/*      */       } 
/*      */       
/*  346 */       if (player.method_6101() && config.climbAndCrawl && !player.method_24828() && !item.method_31574(class_1802.field_16539) && !item.method_31574(class_1802.field_22016) && !player.method_6115()) {
/*  347 */         matrices.method_22904(0.0D, 0.1D, -0.2D);
/*      */       }
/*      */       
/*  350 */       if ((player.method_52535() || player.field_27857) && !player.method_5681() && !player.method_5869()) {
/*  351 */         this.inWaterCounter = (float)(this.inWaterCounter + 0.1D * tt);
/*  352 */         if (this.inWaterCounter >= 1.0F) {
/*  353 */           this.inWaterCounter = 1.0F;
/*      */         }
/*      */       } else {
/*  356 */         this.inWaterCounter = (float)(this.inWaterCounter * Math.pow(0.8799999952316284D, tt));
/*      */       } 
/*      */       
/*  359 */       if (player.field_27857 && player.method_32313() > 0.1D) {
/*  360 */         this.freezeCounter = (float)(this.freezeCounter + 0.1D * tt);
/*      */       } else {
/*  362 */         this.freezeCounter = (float)(this.freezeCounter * Math.pow(0.8799999952316284D, tt));
/*      */       } 
/*      */       
/*  365 */       matrices.method_22904(0.0D, 0.02D * this.inWaterCounter, 0.0D);
/*  366 */       matrices.method_22907(class_7833.field_40718.rotationDegrees(8.0F * sideFactor * this.inWaterCounter));
/*  367 */       matrices.method_22907(class_7833.field_40714.rotationDegrees(0.3F * class_3532.method_15374(this.freezeCounter * 5.0F)));
/*  368 */       if (player.method_18798().method_10214() < -0.85D && item.method_31574(class_1802.field_49814) && player.method_6047() == item) {
/*  369 */         this.fallCounter = (float)(this.fallCounter + 0.1D * tt);
/*  370 */         if (this.fallCounter >= 1.0F) {
/*  371 */           this.fallCounter = 1.0F;
/*      */         }
/*      */       } else {
/*  374 */         this.fallCounter = (float)(this.fallCounter * Math.pow(0.8799999952316284D, tt));
/*      */       } 
/*      */       
/*  377 */       if (bl) {
/*  378 */         matrices.method_22907(class_7833.field_40714.rotationDegrees(45.0F * this.fallCounter));
/*  379 */         matrices.method_22904(0.0D, -0.2D * this.fallCounter, 0.0D);
/*      */       } 
/*      */       
/*  382 */       this.vertAngleY = (float)(this.vertAngleY + player.method_18798().method_10214() * 0.014999999664723873D * tt);
/*  383 */       this.vertAngleY = (float)(this.vertAngleY - (0.1F * this.vertAngleY) * tt);
/*  384 */       this.vertAngleY = (float)(this.vertAngleY * Math.pow(0.8799999952316284D, tt));
/*  385 */       this.vertVelocityYSlime = (float)(this.vertVelocityYSlime + player.method_18798().method_10214() * 0.014999999664723873D * tt);
/*  386 */       this.vertVelocityYSlime = (float)(this.vertVelocityYSlime - (0.1F * this.vertAngleYSlime) * tt);
/*  387 */       this.vertVelocityYSlime = (float)(this.vertVelocityYSlime * Math.pow(0.8799999952316284D, tt));
/*  388 */       this.vertAngleYSlime = (float)(this.vertAngleYSlime + this.vertVelocityYSlime * tt);
/*  389 */       matrices.method_46416(0.0F, this.vertAngleY * -1.0F, 0.0F);
/*  390 */       matrices.method_22904(0.0D, Math.sin(player.field_6012 * 0.1D) * 0.007D * sideFactor, 0.0D);
/*  391 */       matrices.method_22907(class_7833.field_40716.rotationDegrees(0.15F * class_3532.method_15374(player.field_6012 * 0.15F) * sideFactor));
/*  392 */       if (!item.method_7960() || player.method_20448() || (player.method_6101() && !player.method_24828()) || player.method_5681()) {
/*  393 */         if (player.method_6068() == class_1306.field_6182) {
/*  394 */           bl = !bl;
/*      */         }
/*      */         
/*  397 */         if (item.method_7976() == class_1839.field_8949) {
/*  398 */           matrices.method_46416(0.0F, 0.0F, 0.0F);
/*      */         } else {
/*  400 */           matrices.method_22904(0.0D, -0.1D, 0.1D);
/*      */         } 
/*      */       } 
/*      */       
/*  404 */       if (item.method_31574(class_1802.field_16539) || item.method_31574(class_1802.field_22016) || item.method_31573(class_3489.field_40108)) {
/*  405 */         matrices.method_22904(0.0D, 0.1D, 0.0D);
/*  406 */         if (player.method_5681()) {
/*  407 */           matrices.method_22904(0.0D, -0.1D, 0.1D);
/*      */         }
/*      */       } 
/*      */       
/*  411 */       if (player.method_5681() && swingProgress == 0.0F && config.swimmingAnimation) {
/*  412 */         double distance = this.crawlCount;
/*  413 */         double swingAmplitude = 1.5D;
/*  414 */         double frequency = 2.0D;
/*  415 */         double s = distance * frequency;
/*  416 */         double handRotation = Math.sin(s) * swingAmplitude;
/*  417 */         double smoothRotation = handRotation * 0.8D + this.previousRotation * 0.2D;
/*  418 */         matrices.method_22907(class_7833.field_40716.rotationDegrees((float)(bl ? smoothRotation : -smoothRotation)));
/*  419 */         matrices.method_22904(0.0D, 0.0D, smoothRotation * 0.20000000298023224D);
/*  420 */         double k = (this.crawlCount * 2.0F);
/*  421 */         double a = Math.cos(k);
/*  422 */         double b = a;
/*  423 */         if (a <= 0.0D) {
/*  424 */           b = a * 0.5D;
/*      */         }
/*      */         
/*  427 */         matrices.method_22907(class_7833.field_40715.rotationDegrees((float)(bl ? (b * 30.0D) : (b * 30.0D * -1.0D))));
/*  428 */         matrices.method_22904(0.0D, 0.0D, a * 0.20000000298023224D);
/*  429 */         if (item.method_7960() && !bl && !player.method_5767()) {
/*  430 */           matrices.method_22904((1.0F * sideFactor), 0.0D - equipProgress * 0.3D, 0.3D);
/*  431 */           matrices.method_22907(class_7833.field_40716.rotationDegrees(45.0F * sideFactor));
/*  432 */           matrices.method_22907(class_7833.field_40718.rotationDegrees(-40.0F * sideFactor));
/*  433 */           matrices.method_22907(class_7833.field_40714.rotationDegrees(30.0F));
/*  434 */           altSwing(matrices, arm, swingProgress, item);
/*  435 */           float c = class_3532.method_15374(equipProgress * 3.14F);
/*  436 */           matrices.method_22905(0.9F, 0.9F, 0.9F);
/*  437 */           HeldItemRendererAccessor acc = (HeldItemRendererAccessor)this;
/*  438 */           acc.invokeRenderArmHoldingItem(matrices, vertexConsumers, light, 0.0F, 0.0F, arm);
/*      */         } 
/*      */         
/*  441 */         this.previousRotation = smoothRotation;
/*      */       } 
/*      */       
/*  444 */       if (((player.method_6101() && !player.method_24828()) || (player.method_20448() && swingProgress == 0.0F)) && !player.method_6115()) {
/*  445 */         double s = this.climbCount;
/*  446 */         float v = (float)player.method_18798().method_10214();
/*  447 */         float a = class_3532.method_15362((float)s * 2.0F);
/*  448 */         if (player.method_6101()) {
/*  449 */           if (!item.method_31574(class_1802.field_16539) && !item.method_31574(class_1802.field_22016)) {
/*  450 */             matrices.method_22907(class_7833.field_40714.rotationDegrees(20.0F * a * sideFactor));
/*      */           } else {
/*  452 */             matrices.method_22907(class_7833.field_40714.rotationDegrees(1.0F * a * sideFactor));
/*      */           } 
/*      */         }
/*      */         
/*  456 */         if (player.method_20448() && !player.method_6115() && swingProgress == 0.0F) {
/*  457 */           float crawlProgress = class_3532.method_15374(this.directionalCrawlCount * 4.0F * this.mouseHolding);
/*  458 */           float upAndDown = class_3532.method_15362(this.directionalCrawlCount * 4.0F * this.mouseHolding);
/*  459 */           if (item.method_31574(class_1802.field_16539) || item.method_31574(class_1802.field_22016)) {
/*  460 */             crawlProgress *= 0.14F;
/*  461 */             upAndDown *= 0.14F;
/*      */           } 
/*      */           
/*  464 */           matrices.method_22904(0.2D * crawlProgress, 0.3D * crawlProgress * sideFactor, -0.2D * crawlProgress * sideFactor * al);
/*  465 */           matrices.method_22907(class_7833.field_40716.rotationDegrees(25.0F * crawlProgress));
/*  466 */           matrices.method_22907(class_7833.field_40714.rotationDegrees(class_3532.method_15363(20.0F * upAndDown * sideFactor, 0.0F, 20.0F)));
/*      */         } 
/*      */         
/*  469 */         if (item.method_7960() && !bl && !player.method_5767() && ((!player.method_24828() && player.method_6101()) || player.method_20448())) {
/*  470 */           matrices.method_22904((1.0F * sideFactor), 0.0D - equipProgress * 0.3D, 0.3D);
/*  471 */           matrices.method_22907(class_7833.field_40716.rotationDegrees(45.0F * sideFactor));
/*  472 */           matrices.method_22907(class_7833.field_40718.rotationDegrees(-40.0F * sideFactor));
/*  473 */           matrices.method_22907(class_7833.field_40714.rotationDegrees(30.0F));
/*  474 */           altSwing(matrices, arm, swingProgress, item);
/*  475 */           matrices.method_22905(0.9F, 0.9F, 0.9F);
/*  476 */           HeldItemRendererAccessor acc = (HeldItemRendererAccessor)this;
/*  477 */           acc.invokeRenderArmHoldingItem(matrices, vertexConsumers, light, 0.0F, 0.0F, arm);
/*      */         } 
/*      */       } 
/*      */       
/*  481 */       if (item.method_7960()) {
/*  482 */         if (bl && !player.method_5767()) {
/*  483 */           if ((player.method_24828() || !player.method_6101()) && !player.method_5681() && !player.method_20448()) {
/*  484 */             if (player.method_6068() == class_1306.field_6182) {
/*  485 */               bl = !bl;
/*      */             }
/*      */ 
/*      */             
/*  489 */             matrices.method_22904(0.0D, 0.2D * swing_rot, 0.15D * swing_rot);
/*  490 */             matrices.method_22904(0.1D * sideFactor * swing, 0.15D * swing, -0.45D * swing);
/*  491 */             matrices.method_22907(class_7833.field_40716.rotationDegrees(35.0F * swing * sideFactor));
/*  492 */             matrices.method_22907(class_7833.field_40714.rotationDegrees(-30.0F * swing));
/*  493 */             matrices.method_22907(class_7833.field_40716.rotationDegrees(-10.0F * swing_rot * sideFactor));
/*  494 */             matrices.method_22907(class_7833.field_40714.rotationDegrees(10.0F * swing_rot));
/*  495 */             HeldItemRendererAccessor acc = (HeldItemRendererAccessor)this;
/*  496 */             acc.invokeRenderArmHoldingItem(matrices, vertexConsumers, light, 0.0F, 0.0F, arm);
/*      */           } else {
/*  498 */             matrices.method_22904((1.0F * sideFactor), 0.0D - equipProgress * 0.3D, 0.3D);
/*  499 */             matrices.method_22907(class_7833.field_40716.rotationDegrees(45.0F * sideFactor));
/*  500 */             matrices.method_22907(class_7833.field_40718.rotationDegrees(-40.0F * sideFactor));
/*  501 */             matrices.method_22907(class_7833.field_40714.rotationDegrees(30.0F));
/*  502 */             altSwing(matrices, arm, swingProgress, item);
/*  503 */             float c = class_3532.method_15374(equipProgress * 3.14F);
/*  504 */             matrices.method_22905(0.9F, 0.9F, 0.9F);
/*  505 */             HeldItemRendererAccessor acc = (HeldItemRendererAccessor)this;
/*  506 */             acc.invokeRenderArmHoldingItem(matrices, vertexConsumers, light, 0.0F, 0.0F, arm);
/*      */           } 
/*      */         }
/*  509 */       } else if (item.method_57826(class_9334.field_49646)) {
/*  510 */         if (bl && this.field_4047.method_7960()) {
/*  511 */           matrices.method_22904(0.0D, 0.1D, 0.0D);
/*  512 */           HeldItemRendererAccessor acc = (HeldItemRendererAccessor)this;
/*  513 */           acc.invokeRenderMapInBothHands(matrices, vertexConsumers, light, pitch, equipProgress, swingProgress);
/*      */         } else {
/*  515 */           matrices.method_22904(bl ? -0.1D : 0.1D, 0.1D, 0.0D);
/*  516 */           HeldItemRendererAccessor acc = (HeldItemRendererAccessor)this;
/*  517 */           acc.invokeRenderMapInOneHand(matrices, vertexConsumers, light, equipProgress, arm, swingProgress, item);
/*      */         } 
/*  519 */       } else if (item.method_7976() == class_1839.field_8947) {
/*  520 */         matrices.method_22903();
/*  521 */         boolean bl2 = class_1764.method_7781(item);
/*  522 */         boolean bl3 = (arm == class_1306.field_6183);
/*  523 */         int i = bl3 ? 1 : -1;
/*  524 */         if (player.method_6115() && player.method_6014() > 0 && player.method_6058() == hand) {
/*  525 */           HeldItemRendererAccessor heldItemRendererAccessor = (HeldItemRendererAccessor)this;
/*  526 */           heldItemRendererAccessor.invokeApplyEquipOffset(matrices, arm, equipProgress);
/*  527 */           matrices.method_46416(i * -0.4785682F, -0.24387F, 0.05731531F);
/*  528 */           matrices.method_22907(class_7833.field_40714.rotationDegrees(-11.935F));
/*  529 */           matrices.method_22907(class_7833.field_40716.rotationDegrees(i * 65.3F));
/*  530 */           matrices.method_22907(class_7833.field_40718.rotationDegrees(i * 9.785F));
/*  531 */           float f = item.method_7935((class_1309)player) - player.method_6014() - tickDelta + 1.0F;
/*  532 */           float g = f / class_1764.method_7775(item, (class_1309)player);
/*  533 */           if (g > 1.0F) {
/*  534 */             g = 1.0F;
/*      */           }
/*      */           
/*  537 */           if (g > 0.1F) {
/*  538 */             float h = class_3532.method_15374((f - 0.1F) * 1.3F);
/*  539 */             float j = g - 0.1F;
/*  540 */             float k = h * j;
/*  541 */             matrices.method_46416(k * 0.0F, k * 0.004F, k * 0.0F);
/*      */           } 
/*      */           
/*  544 */           matrices.method_46416(g * 0.0F, g * 0.0F, g * 0.04F);
/*  545 */           matrices.method_22905(1.0F, 1.0F, 1.0F);
/*  546 */           matrices.method_22907(class_7833.field_40715.rotationDegrees(i * 45.0F));
/*      */         } else {
/*  548 */           ((HeldItemRendererAccessor)this).invokeSwingArm(swingProgress, equipProgress, matrices, i, arm);
/*      */           
/*  550 */           if (bl2 && swingProgress < 0.001F && bl) {
/*  551 */             matrices.method_46416(i * -0.341864F, 0.0F, 0.0F);
/*  552 */             matrices.method_22907(class_7833.field_40716.rotationDegrees(i * 10.0F));
/*      */           } 
/*      */         } 
/*      */         
/*  556 */         matrices.method_46416(0.0F, 0.0F, -1.0F);
/*  557 */         matrices.method_22904(-0.45D * i, 0.45D, 1.7D);
/*  558 */         matrices.method_22904((1.0F * sideFactor), 0.0D - equipProgress * 0.3D, 0.3D);
/*  559 */         matrices.method_22907(class_7833.field_40716.rotationDegrees(45.0F * sideFactor));
/*  560 */         matrices.method_22907(class_7833.field_40718.rotationDegrees(-40.0F * sideFactor));
/*  561 */         matrices.method_22907(class_7833.field_40714.rotationDegrees(30.0F));
/*  562 */         altSwing(matrices, arm, swingProgress, item);
/*  563 */         float c = class_3532.method_15374(equipProgress * 3.14F);
/*  564 */         matrices.method_22905(0.9F, 0.9F, 0.9F);
/*  565 */         HeldItemRendererAccessor acc = (HeldItemRendererAccessor)this;
/*  566 */         acc.invokeRenderArmHoldingItem(matrices, vertexConsumers, light, 0.0F, 0.0F, arm);
/*  567 */         matrices.method_22904(-0.25D * i, 1.25D, 0.05D);
/*  568 */         matrices.method_22907(class_7833.field_40716.rotationDegrees((-90 * i)));
/*  569 */         matrices.method_22907(class_7833.field_40714.rotationDegrees(77.0F));
/*  570 */         matrices.method_22907(class_7833.field_40718.rotationDegrees((85 * i)));
/*  571 */         matrices.method_22905(1.2F, 1.2F, 1.2F);
/*  572 */         matrices.method_22907(class_7833.field_40714.rotationDegrees(-10.0F));
/*  573 */         matrices.method_22904(0.0D, -0.15D, 0.15D);
/*  574 */         acc.invokeRenderItem((class_1309)player, item, bl3 ? class_811.field_4322 : class_811.field_4321, !bl3, matrices, vertexConsumers, light);
/*  575 */         matrices.method_22909();
/*  576 */         if (player.method_6115() && player.method_6014() > 0 && player.method_6058() == hand) {
/*  577 */           float f = item.method_7935((class_1309)player) - player.method_6014() - tickDelta + 1.0F;
/*  578 */           float g = f / class_1764.method_7775(item, (class_1309)player);
/*  579 */           if (g > 1.0F) {
/*  580 */             g = 1.0F;
/*      */           }
/*      */           
/*  583 */           if (g > 0.1F) {
/*  584 */             float h = class_3532.method_15374((f - 0.1F) * 1.3F);
/*  585 */             float j = g - 0.1F;
/*  586 */             float k = h * j;
/*  587 */             matrices.method_46416(k * 0.0F, k * 0.004F, k * 0.0F);
/*      */           } 
/*      */           
/*  590 */           matrices.method_22907(class_7833.field_40715.rotationDegrees((g <= 0.2D) ? (75.0F * g * 5.0F * i) : (75 * i)));
/*  591 */           matrices.method_22907(class_7833.field_40713.rotationDegrees(10.0F * g * 1.5F));
/*  592 */           matrices.method_22904(-0.37D * i, 0.0D, 0.6D);
/*  593 */           matrices.method_22904(0.15D * g * i, 0.0D, 0.0D);
/*  594 */           acc.invokeRenderArmHoldingItem(matrices, vertexConsumers, light, equipProgress, swingProgress, arm.method_5928());
/*      */         } 
/*      */       } else {
/*  597 */         boolean bl2 = (arm == class_1306.field_6183);
/*  598 */         int l = bl2 ? 1 : -1;
/*  599 */         if (player.method_6115() && player.method_6014() > 0 && player.method_6058() == hand) {
/*  600 */           HeldItemRendererAccessor acc; float u; float y; float q; float c; HeldItemRendererAccessor acc4; float k; float s; float s2; HeldItemRendererAccessor acc5; float m1; float f1; float f; HeldItemRendererAccessor acc1; float m; HeldItemRendererAccessor acc0; float f5; float g5; float h5; float n; float z; float x; HeldItemRendererAccessor acc78; HeldItemRendererAccessor acc67; switch (item.method_7976()) {
/*      */             case field_8952:
/*  602 */               acc = (HeldItemRendererAccessor)this;
/*  603 */               acc.invokeApplyEquipOffset(matrices, arm, equipProgress);
/*      */               break;
/*      */             case field_8950:
/*      */             case field_8946:
/*  607 */               u = item.method_7935((class_1309)player) - player.method_6014() - tickDelta + 1.0F;
/*  608 */               y = u / 5.0F;
/*  609 */               if (y > 1.0F) {
/*  610 */                 y = 1.0F;
/*      */               }
/*      */               
/*  613 */               q = class_3532.method_15374(u / 2.0F * 3.14F);
/*  614 */               q /= 10.0F;
/*  615 */               matrices.method_22904((1 * l), 0.1D, 0.3D);
/*  616 */               matrices.method_22904(0.2D * l * y, -0.7D * y, -0.2D * y);
/*  617 */               matrices.method_22904(0.0D, -0.2D * q, -0.2D * q);
/*  618 */               matrices.method_22904(0.0D, 0.1D * easeInOutBack(class_3532.method_15374(y * 3.14F)), 0.0D);
/*  619 */               matrices.method_22907(class_7833.field_40716.rotationDegrees((45 * l)));
/*  620 */               matrices.method_22907(class_7833.field_40718.rotationDegrees((-40 * l)));
/*  621 */               matrices.method_22907(class_7833.field_40714.rotationDegrees(30.0F));
/*  622 */               altSwing(matrices, arm, swingProgress, item);
/*  623 */               c = class_3532.method_15374(equipProgress * 3.14F);
/*  624 */               matrices.method_22905(0.9F, 0.9F, 0.9F);
/*  625 */               matrices.method_22907(class_7833.field_40716.rotationDegrees(45.0F * y * l));
/*  626 */               acc4 = (HeldItemRendererAccessor)this;
/*  627 */               acc4.invokeRenderArmHoldingItem(matrices, vertexConsumers, light, 0.0F, swingProgress, arm);
/*      */               break;
/*      */             case field_8949:
/*  630 */               k = item.method_7935((class_1309)player) - player.method_6014() - tickDelta + 1.0F;
/*  631 */               s = k / 4.0F;
/*  632 */               s2 = k / 6.0F;
/*  633 */               if (s > 1.0F) {
/*  634 */                 s = 1.0F;
/*      */               }
/*      */               
/*  637 */               if (s2 > 1.0F) {
/*  638 */                 s2 = 1.0F;
/*      */               }
/*      */               
/*  641 */               matrices.method_22904(0.0D, -0.2D, 0.0D);
/*  642 */               matrices.method_22904((1 * l), 0.0D, 0.3D);
/*  643 */               matrices.method_22904(0.7D * s * l, 0.0D, -1.3D * s);
/*  644 */               matrices.method_22904(-0.2D * l * s2, 0.0D, 0.0D);
/*  645 */               matrices.method_22907(class_7833.field_40714.rotationDegrees((float)(10.0D * Math.sin(s2 * 3.14D))));
/*  646 */               matrices.method_22907(class_7833.field_40716.rotationDegrees(70.0F * s * l));
/*  647 */               matrices.method_22907(class_7833.field_40716.rotationDegrees((45 * l)));
/*  648 */               matrices.method_22907(class_7833.field_40718.rotationDegrees((-40 * l)));
/*  649 */               matrices.method_22907(class_7833.field_40714.rotationDegrees(30.0F));
/*  650 */               matrices.method_22907(class_7833.field_40716.rotationDegrees((5 * l) * s));
/*  651 */               matrices.method_22907(class_7833.field_40714.rotationDegrees(-10.0F * s));
/*  652 */               matrices.method_22904(0.0D, 0.0D, -0.2D * s);
/*  653 */               altSwing(matrices, arm, swingProgress, item);
/*  654 */               matrices.method_22905(0.9F, 0.9F, 0.9F);
/*  655 */               acc5 = (HeldItemRendererAccessor)this;
/*  656 */               acc5.invokeRenderArmHoldingItem(matrices, vertexConsumers, light, 0.0F, swingProgress, arm);
/*  657 */               matrices.method_22904(0.35D * l, -0.13D, -0.12D);
/*  658 */               matrices.method_22907(class_7833.field_40718.rotationDegrees(10.0F * l));
/*  659 */               matrices.method_22907(class_7833.field_40716.rotationDegrees(10.0F * l));
/*  660 */               matrices.method_22907(class_7833.field_40714.rotationDegrees(0.0F));
/*  661 */               matrices.method_22904(-0.2D * l, -0.04D, 0.15D);
/*  662 */               matrices.method_22905(1.0F, 1.0F, 1.0F);
/*      */               break;
/*      */             case field_8953:
/*  665 */               matrices.method_22903();
/*  666 */               if (player.method_6068() == class_1306.field_6182) {
/*  667 */                 bl = !bl;
/*      */               }
/*      */               
/*  670 */               m1 = item.method_7935((class_1309)player) - player.method_6014() - tickDelta + 1.0F;
/*  671 */               f1 = m1 / 20.0F;
/*  672 */               f = (f1 * f1 + f1 * 2.0F) / 3.0F;
/*  673 */               if (f1 > 1.0F) {
/*  674 */                 f1 = 1.0F;
/*      */               }
/*      */               
/*  677 */               if (f1 > 0.1F) {
/*  678 */                 float g1 = class_3532.method_15374((m1 - 0.1F) * 1.3F);
/*  679 */                 float j1 = g1 * f1;
/*  680 */                 matrices.method_46416(j1 * 0.0F, j1 * 0.004F, j1 * 0.0F);
/*      */               } 
/*      */               
/*  683 */               matrices.method_22904(bl ? -0.1D : 0.1D, 0.0D, f1 * 0.15D);
/*  684 */               acc1 = (HeldItemRendererAccessor)this;
/*  685 */               acc1.invokeRenderArmHoldingItem(matrices, vertexConsumers, light, equipProgress, swingProgress, arm);
/*  686 */               matrices.method_22909();
/*  687 */               matrices.method_22904(bl ? -0.5D : 0.5D, -0.45D, 0.1D);
/*  688 */               matrices.method_22907(class_7833.field_40714.rotation(0.3F));
/*  689 */               if (bl) {
/*  690 */                 matrices.method_22907(class_7833.field_40717.rotation(-0.3F));
/*  691 */                 matrices.method_22907(class_7833.field_40715.rotation(1.0F));
/*      */               } else {
/*  693 */                 matrices.method_22907(class_7833.field_40718.rotation(-0.3F));
/*  694 */                 matrices.method_22907(class_7833.field_40716.rotation(1.0F));
/*      */               } 
/*      */               
/*  697 */               acc1.invokeRenderArmHoldingItem(matrices, vertexConsumers, light, equipProgress, swingProgress, arm.method_5928());
/*  698 */               if (bl) {
/*  699 */                 matrices.method_22907(class_7833.field_40715.rotation(2.5F));
/*      */               } else {
/*  701 */                 matrices.method_22907(class_7833.field_40716.rotation(2.5F));
/*      */               } 
/*      */               
/*  704 */               matrices.method_22904(bl ? -0.65D : 0.65D, -0.35D, 0.27D);
/*  705 */               if (f1 > 1.0F) {
/*  706 */                 f1 = 1.0F;
/*      */               }
/*      */               
/*  709 */               matrices.method_22909();
/*  710 */               if (config.mb3DCompat) {
/*  711 */                 matrices.method_22907(class_7833.field_40716.rotationDegrees((10 * l)));
/*      */               }
/*      */               
/*  714 */               matrices.method_22907(class_7833.field_40713.rotationDegrees(75.0F));
/*  715 */               matrices.method_22907(class_7833.field_40717.rotationDegrees((-15 * l)));
/*  716 */               matrices.method_22904(0.8D * l, (0.0F - equipProgress * 0.3F), -0.1D);
/*  717 */               if (f > 0.1F) {
/*  718 */                 float g1 = class_3532.method_15374((m1 - 0.1F) * 1.3F);
/*  719 */                 float h1 = f1 - 0.1F;
/*  720 */                 float j1 = g1 * h1;
/*  721 */                 matrices.method_46416(j1 * 0.0F, j1 * 0.004F, j1 * 0.0F);
/*      */               } 
/*      */               
/*  724 */               matrices.method_22903();
/*      */               break;
/*      */             case field_8951:
/*  727 */               if (player.method_6079().method_7960() && !player.method_20448() && !player.method_5681() && !player.method_6101()) {
/*  728 */                 matrices.method_22903();
/*  729 */                 matrices.method_22907(class_7833.field_40716.rotationDegrees((-25 * l)));
/*  730 */                 matrices.method_22904(-0.15D * l, 0.1D, 0.1D);
/*  731 */                 HeldItemRendererAccessor acc8 = (HeldItemRendererAccessor)this;
/*  732 */                 acc8.invokeRenderArmHoldingItem(matrices, vertexConsumers, light, equipProgress, swingProgress, arm.method_5928());
/*  733 */                 matrices.method_22909();
/*      */               } 
/*      */               
/*  736 */               m = item.method_7935((class_1309)player) - player.method_6014() - tickDelta + 1.0F;
/*  737 */               f = m / 10.0F;
/*  738 */               if (f > 1.0F) {
/*  739 */                 f = 1.0F;
/*      */               }
/*      */               
/*  742 */               if (f > 0.1F) {
/*  743 */                 float g = class_3532.method_15374((m - 0.1F) * 1.3F);
/*  744 */                 float h = f - 0.1F;
/*  745 */                 float j = g * h;
/*  746 */                 matrices.method_46416(j * 0.0F, j * 0.004F, j * 0.0F);
/*      */               } 
/*      */               
/*  749 */               matrices.method_22907(class_7833.field_40714.rotationDegrees(45.0F));
/*  750 */               matrices.method_22907(class_7833.field_40716.rotationDegrees((25 * l)));
/*  751 */               matrices.method_22904(0.2D * l, 0.0D, 0.8D);
/*  752 */               acc0 = (HeldItemRendererAccessor)this;
/*  753 */               acc0.invokeRenderArmHoldingItem(matrices, vertexConsumers, light, equipProgress, swingProgress, arm);
/*  754 */               matrices.method_22907(class_7833.field_40714.rotationDegrees(135.0F));
/*  755 */               matrices.method_22907(class_7833.field_40718.rotationDegrees((-65 * l)));
/*  756 */               matrices.method_22904((0.65F * l), -1.0D, -0.6D);
/*      */               break;
/*      */             case field_42717:
/*  759 */               f5 = (player.method_6014() % 10);
/*  760 */               g5 = f5 - tickDelta + 1.0F;
/*  761 */               h5 = 1.0F - g5 / 10.0F;
/*  762 */               n = -15.0F + 75.0F * class_3532.method_15362(h5 * 2.0F * 3.1415927F);
/*  763 */               z = item.method_7935((class_1309)player) - player.method_6014() - tickDelta + 1.0F;
/*  764 */               x = z / 4.0F;
/*  765 */               if (x > 1.0F) {
/*  766 */                 x = 1.0F;
/*      */               }
/*      */               
/*  769 */               matrices.method_22907(class_7833.field_40716.rotationDegrees((25 * l) * x));
/*  770 */               matrices.method_22904((0.3F * l * x), 0.3D * x, 0.1D * x);
/*  771 */               if (x == 1.0F) {
/*  772 */                 matrices.method_22907(class_7833.field_40716.rotationDegrees(n / 20.0F));
/*      */               }
/*      */               
/*  775 */               acc78 = (HeldItemRendererAccessor)this;
/*  776 */               acc78.invokeRenderArmHoldingItem(matrices, vertexConsumers, light, equipProgress, swingProgress, arm);
/*      */               break;
/*      */             case field_55494:
/*  779 */               matrices.method_22904((1 * l), 0.0D - equipProgress * 0.3D, 0.3D);
/*  780 */               matrices.method_22907(class_7833.field_40716.rotationDegrees((45 * l)));
/*  781 */               matrices.method_22907(class_7833.field_40718.rotationDegrees((-40 * l)));
/*  782 */               matrices.method_22907(class_7833.field_40714.rotationDegrees(30.0F));
/*  783 */               altSwing(matrices, arm, swingProgress, item);
/*  784 */               matrices.method_22905(0.9F, 0.9F, 0.9F);
/*  785 */               acc67 = (HeldItemRendererAccessor)this;
/*  786 */               acc67.invokeRenderArmHoldingItem(matrices, vertexConsumers, light, 0.0F, 0.0F, arm); break;
/*      */           } 
/*  788 */         } else if (player.method_6123() && item.method_7976() == class_1839.field_8951) {
/*  789 */           this.riptideCounter = (float)(this.riptideCounter + 0.15D * tt);
/*  790 */           float m = item.method_7935((class_1309)player) - player.method_6014() - tickDelta + 1.0F;
/*  791 */           float f = m / 10.0F;
/*  792 */           if (f > 1.0F) {
/*  793 */             f = 1.0F;
/*      */           }
/*      */           
/*  796 */           if (f > 0.1F) {
/*  797 */             float g = class_3532.method_15374((m - 0.1F) * 1.3F);
/*  798 */             float h = f - 0.1F;
/*  799 */             float j = g * h;
/*  800 */             matrices.method_46416(j * 0.0F, j * 0.004F, j * 0.0F);
/*      */           } 
/*      */           
/*  803 */           matrices.method_22907(class_7833.field_40714.rotationDegrees(45.0F - this.riptideCounter * 2.0F));
/*  804 */           matrices.method_22907(class_7833.field_40716.rotationDegrees((25 * l)));
/*  805 */           matrices.method_22904(0.2D * l, 0.0D, 0.75D);
/*  806 */           matrices.method_22904(0.0D, 0.0D, 0.01D * class_3532.method_15374(this.riptideCounter * 6.28F));
/*  807 */           HeldItemRendererAccessor acc = (HeldItemRendererAccessor)this;
/*  808 */           acc.invokeRenderArmHoldingItem(matrices, vertexConsumers, light, equipProgress, swingProgress, arm);
/*  809 */           matrices.method_22907(class_7833.field_40714.rotationDegrees(135.0F));
/*  810 */           matrices.method_22907(class_7833.field_40718.rotationDegrees((-65 * l)));
/*  811 */           matrices.method_22904((0.65F * l), -1.0D, -0.6D);
/*      */         } else {
/*  813 */           this.riptideCounter = 0.0F;
/*  814 */           if (!item.method_31574(class_1802.field_16539) && !item.method_31574(class_1802.field_22016) && !item.method_31573(class_3489.field_40108)) {
/*  815 */             if (item.method_7976() == class_1839.field_8949) {
/*  816 */               matrices.method_22904(0.0D, -0.2D, 0.0D);
/*      */             }
/*      */           } else {
/*  819 */             matrices.method_22904(0.1D * l, 0.0D, -0.1D);
/*  820 */             matrices.method_22907(class_7833.field_40714.rotationDegrees(10.0F));
/*      */           } 
/*      */           
/*  823 */           matrices.method_22904((1 * l), 0.0D - equipProgress * 0.3D, 0.3D);
/*  824 */           matrices.method_22907(class_7833.field_40716.rotationDegrees((45 * l)));
/*  825 */           matrices.method_22907(class_7833.field_40718.rotationDegrees((-40 * l)));
/*  826 */           matrices.method_22907(class_7833.field_40714.rotationDegrees(30.0F));
/*  827 */           altSwing(matrices, arm, swingProgress, item);
/*  828 */           matrices.method_22905(0.9F, 0.9F, 0.9F);
/*  829 */           HeldItemRendererAccessor acc = (HeldItemRendererAccessor)this;
/*  830 */           acc.invokeRenderArmHoldingItem(matrices, vertexConsumers, light, 0.0F, 0.0F, arm);
/*      */         } 
/*      */         
/*  833 */         matrices.method_22904(-0.3D * l, 0.65D, -0.1D);
/*  834 */         matrices.method_22907(class_7833.field_40716.rotationDegrees((-65 * l)));
/*  835 */         matrices.method_22907(class_7833.field_40714.rotationDegrees(10.0F));
/*  836 */         if (item.method_31573(class_3489.field_15542)) {
/*  837 */           matrices.method_22904(0.2D * l, -0.1D, 0.0D);
/*      */         }
/*      */         
/*  840 */         if (class_2248.method_9503(item.method_7909()) != class_2246.field_10124 && item.method_7976() != class_1839.field_8950 && !item.method_31573(ConventionalItemTags.BUCKETS)) {
/*  841 */           if (item.method_7964().toString().toLowerCase().contains("TORCH".toLowerCase())) {
/*  842 */             matrices.method_22905(1.5F, 1.5F, 1.5F);
/*  843 */             matrices.method_22907(class_7833.field_40715.rotationDegrees((25 * l)));
/*  844 */             matrices.method_22907(class_7833.field_40714.rotationDegrees(5.0F));
/*  845 */             matrices.method_22907(class_7833.field_40718.rotationDegrees((75 * l)));
/*  846 */             matrices.method_22904(0.2D * l, 0.2D, 0.05D);
/*  847 */           } else if ((item.method_31574(class_1802.field_8276) || item.method_31574(class_1802.field_8725) || item.method_31574(class_1802.field_8865) || item.method_31574(class_1802.field_8366) || class_2248.method_9503(item.method_7909()).method_9564().method_26164(ConventionalBlockTags.GLASS_PANES) || class_2248.method_9503(item.method_7909()).method_9564().method_26164(class_3481.field_15463) || class_2248.method_9503(item.method_7909()).method_9564().method_26164(class_3481.field_22414) || item.method_31573(class_3489.field_15553)) && !class_2248.method_9503(item.method_7909()).method_9564().method_26164(class_3481.field_15503) && !class_2248.method_9503(item.method_7909()).method_9564().method_26164(class_3481.field_43170) && !class_2248.method_9503(item.method_7909()).method_9564().method_26164(class_3481.field_15501)) {
/*  848 */             matrices.method_22904(0.0D, 0.0D, -0.1D);
/*  849 */             matrices.method_22907(class_7833.field_40715.rotationDegrees((5 * l)));
/*  850 */             matrices.method_22907(class_7833.field_40714.rotationDegrees(15.0F));
/*  851 */             matrices.method_22907(class_7833.field_40718.rotationDegrees((75 * l)));
/*  852 */           } else if (!item.method_31574(class_1802.field_16539) && !item.method_31574(class_1802.field_22016) && !item.method_31573(class_3489.field_40108)) {
/*  853 */             matrices.method_22907(class_7833.field_40715.rotationDegrees((25 * l)));
/*  854 */             matrices.method_22907(class_7833.field_40714.rotationDegrees(5.0F));
/*  855 */             matrices.method_22907(class_7833.field_40718.rotationDegrees((75 * l)));
/*  856 */             matrices.method_22904(0.2D * l, 0.2D, 0.05D);
/*  857 */             if (class_2248.method_9503(item.method_7909()).method_9564().method_26164(class_3481.field_15501)) {
/*  858 */               matrices.method_22904(-0.2D * l, 0.0D, 0.0D);
/*  859 */               matrices.method_22905(1.1F, 1.1F, 1.1F);
/*      */             } 
/*      */           } else {
/*  862 */             float dt = (float)(astra.deltaTime * 30.0D);
/*  863 */             float yawDelta = player.field_6259 - player.method_5791();
/*  864 */             float pitchDelta = player.field_6004 - player.method_36455();
/*  865 */             this.swingVelocityY += yawDelta * 0.015F * dt;
/*  866 */             this.swingVelocityY += swingProgress * 2.0F * dt;
/*  867 */             this.swingVelocityX += pitchDelta * 0.015F * dt;
/*  868 */             this.swingVelocityY -= 0.1F * this.swingAngleY * dt;
/*  869 */             this.swingVelocityX -= 0.1F * this.swingAngleX * dt;
/*  870 */             this.swingVelocityY = (float)(this.swingVelocityY * Math.pow(0.8799999952316284D, dt));
/*  871 */             this.swingVelocityX = (float)(this.swingVelocityX * Math.pow(0.8799999952316284D, dt));
/*  872 */             this.swingAngleY += this.swingVelocityY * dt;
/*  873 */             this.swingAngleX += this.swingVelocityX * dt;
/*  874 */             double currentSpeed = player.method_18798().method_1033();
/*  875 */             this.swingVelocityZ = (float)(this.swingVelocityZ + (bl ? ((currentSpeed * -1.0D * 15.0D - this.swingVelocityZ) * 0.10000000149011612D * dt) : ((currentSpeed * 15.0D - this.swingVelocityZ) * 0.10000000149011612D * dt)));
/*  876 */             if (((currentSpeed > 0.09D && player.method_24828()) || player.method_5681() || (player.method_6101() && !player.method_24828())) && ((Boolean)this.field_4050.field_1690.method_42448().method_41753()).booleanValue()) {
/*  877 */               Random random = new Random();
/*  878 */               boolean randomBoolean = random.nextBoolean();
/*  879 */               this.swingVelocityY += (float)(randomBoolean ? (-5.5D * currentSpeed * dt) : (5.5D * currentSpeed * dt));
/*      */             } 
/*      */             
/*  882 */             matrices.method_22904(0.0D, 0.0D, -0.1D);
/*  883 */             matrices.method_22907(class_7833.field_40715.rotationDegrees((35 * l) + this.swingAngleY));
/*  884 */             matrices.method_22907(class_7833.field_40714.rotationDegrees(15.0F + this.swingAngleX));
/*  885 */             matrices.method_22907(class_7833.field_40718.rotationDegrees((75 * l) + this.swingVelocityZ));
/*  886 */             if (item.method_31573(class_3489.field_40108)) {
/*  887 */               matrices.method_22904(0.0D, -0.1D, 0.0D);
/*  888 */               matrices.method_22907(class_7833.field_40716.rotationDegrees((-45 * l)));
/*      */             } 
/*      */             
/*  891 */             matrices.method_22904(0.3D * l, -0.35D, 0.0D);
/*  892 */             matrices.method_22904(0.0D, 0.0D, 0.1D);
/*  893 */             matrices.method_22905(1.5F, 1.5F, 1.5F);
/*      */           } 
/*      */         } else {
/*  896 */           if ((!item.method_31573(ConventionalItemTags.TOOLS) || item.method_31573(class_3489.field_41890) || item.method_31573(class_3489.field_40109) || item.method_7976() == class_1839.field_8950 || !item.method_7923()) && item.method_7976() != class_1839.field_8953 && item.method_7976() != class_1839.field_27079 && getAttackDamage(item) == 0.0F && item.method_7976() != class_1839.field_8949 && !item.method_31574(class_1802.field_23254) && !item.method_31574(class_1802.field_8184) && !item.method_31574(class_1802.field_8378) && !item.method_31574(class_1802.field_8868) && !item.method_31573(class_3489.field_42613) && !config.mb3DCompat) {
/*  897 */             if (item.method_7976() == class_1839.field_42717) {
/*  898 */               matrices.method_22907(class_7833.field_40713.rotationDegrees(25.0F));
/*  899 */               matrices.method_22904(bl ? 0.0D : 0.35D, bl ? 0.0D : 0.25D, bl ? 0.0D : 0.37D);
/*  900 */               if (!bl) {
/*  901 */                 matrices.method_22905(0.75F, 0.75F, 0.75F);
/*      */               }
/*      */               
/*  904 */               matrices.method_22907(class_7833.field_40717.rotationDegrees((-75 * l)));
/*  905 */               matrices.method_22907(class_7833.field_40713.rotationDegrees(35.0F));
/*  906 */               matrices.method_22904(bl ? -0.05D : 0.85D, bl ? 0.0D : 0.05D, bl ? 0.08D : -0.2D);
/*      */             } else {
/*  908 */               matrices.method_22907(class_7833.field_40715.rotationDegrees((5 * l)));
/*  909 */               matrices.method_22907(class_7833.field_40714.rotationDegrees(15.0F));
/*  910 */               matrices.method_22907(class_7833.field_40718.rotationDegrees((75 * l)));
/*  911 */               matrices.method_22904(0.0D, -0.05D, -0.1D);
/*  912 */               matrices.method_22905(0.7F, 0.7F, 0.7F);
/*      */             } 
/*      */             
/*  915 */             if (item.method_31574(class_1802.field_8153) || item.method_31574(class_1802.field_8777) || item.method_31574(class_1802.field_8323)) {
/*  916 */               this.vertVelocityYSlime = (float)(this.vertVelocityYSlime + swingProgress * 0.03D * astra.deltaTime * 30.0D);
/*  917 */               if (((player.method_18798().method_1033() > 0.09D && player.method_24828()) || player.method_5681() || player.method_20448() || (player.method_6101() && !player.method_24828())) && ((Boolean)this.field_4050.field_1690.method_42448().method_41753()).booleanValue()) {
/*  918 */                 Random random = new Random();
/*  919 */                 boolean randomBoolean = random.nextBoolean();
/*  920 */                 this.vertVelocityYSlime += (float)(-0.05D * player.method_18798().method_1033() * astra.deltaTime * 30.0D);
/*      */               } 
/*      */               
/*  923 */               matrices.method_22905(1.0F, 1.0F + this.vertAngleYSlime * -2.0F, 1.0F);
/*      */             } 
/*  925 */           } else if (item.method_7976() == class_1839.field_8949 && item.method_7976() != class_1839.field_8951) {
/*  926 */             matrices.method_22907(class_7833.field_40718.rotationDegrees((160 * l)));
/*  927 */             matrices.method_22907(class_7833.field_40716.rotationDegrees((-60 * l)));
/*  928 */             matrices.method_22907(class_7833.field_40714.rotationDegrees(-70.0F));
/*  929 */             matrices.method_22905(0.75F, 0.75F, 0.75F);
/*  930 */             matrices.method_22904(0.15D * l, bl ? 0.35D : 0.45D, bl ? -0.15D : -0.1D);
/*  931 */             matrices.method_22904(0.17D * l, 0.0D, 0.3D);
/*  932 */             matrices.method_22907(class_7833.field_40716.rotationDegrees((-90 * l)));
/*  933 */           } else if (item.method_7976() == class_1839.field_8951) {
/*  934 */             matrices.method_22907(class_7833.field_40715.rotationDegrees((75 * l)));
/*  935 */             matrices.method_22907(class_7833.field_40714.rotationDegrees(90.0F));
/*  936 */             matrices.method_22907(class_7833.field_40718.rotationDegrees((45 * l)));
/*  937 */             matrices.method_46416(-0.3F * l, 0.0F, 0.0F);
/*  938 */           } else if (item.method_7976() != class_1839.field_8951) {
/*  939 */             matrices.method_22907(class_7833.field_40715.rotationDegrees((75 * l)));
/*  940 */             matrices.method_22907(class_7833.field_40714.rotationDegrees(70.0F));
/*  941 */             matrices.method_22907(class_7833.field_40718.rotationDegrees((45 * l)));
/*      */           } 
/*      */           
/*  944 */           if (item.method_7976() != class_1839.field_8949) {
/*  945 */             matrices.method_22905(1.2F, 1.2F, 1.2F);
/*      */           }
/*      */           
/*  948 */           if (item.method_7976() == class_1839.field_8953 && !player.method_6115()) {
/*  949 */             matrices.method_22904(-0.1D * l, -0.2D, 0.0D);
/*      */           }
/*      */           
/*  952 */           if (item.method_31574(class_1802.field_49814)) {
/*  953 */             if (config.mb3DCompat) {
/*  954 */               matrices.method_22904(-0.08D, 0.17D, 0.0D);
/*  955 */               matrices.method_22907(class_7833.field_40714.rotationDegrees(40.0F));
/*      */             } 
/*      */             
/*  958 */             matrices.method_22904(0.1D * l, 0.0D, 0.0D);
/*  959 */             matrices.method_22905(0.9F, 0.9F, 0.9F);
/*      */           } 
/*      */         } 
/*      */         
/*  963 */         if (item.method_7909() instanceof class_1747 && ((!item.method_31573(ConventionalItemTags.BUCKETS) && item.method_7976() != class_1839.field_8950 && !item.method_31573(class_3489.field_15556) && !item.method_31574(class_1802.field_8276) && !item.method_31574(class_1802.field_8725) && !item.method_31574(class_1802.field_8865) && !item.method_31574(class_1802.field_8366) && !class_2248.method_9503(item.method_7909()).method_9564().method_26164(ConventionalBlockTags.GLASS_PANES) && !class_2248.method_9503(item.method_7909()).method_9564().method_26164(class_3481.field_15463) && !class_2248.method_9503(item.method_7909()).method_9564().method_26164(class_3481.field_22414) && !item.method_31573(class_3489.field_15553)) || class_2248.method_9503(item.method_7909()).method_9564().method_26164(class_3481.field_15503)) && !class_2248.method_9503(item.method_7909()).method_9564().method_26164(class_3481.field_43170)) {
/*  964 */           class_1747 blockItem = (class_1747)item.method_7909();
/*  965 */           class_776 blockRenderManager = class_310.method_1551().method_1541();
/*  966 */           blockRenderManager.method_3349(blockItem.method_7711().method_9564());
/*  967 */           matrices.method_22903();
/*  968 */           if (!bl2) {
/*  969 */             matrices.method_46416(-0.4F, 0.0F, 0.0F);
/*      */           }
/*      */           
/*  972 */           matrices.method_22905(0.4F, 0.4F, 0.4F);
/*  973 */           matrices.method_22904(-0.9D * l, -0.45D, -0.5D);
/*  974 */           if (class_2248.method_9503(item.method_7909()).method_9564().method_26164(class_3481.field_15493)) {
/*  975 */             matrices.method_22904(0.2D * l, -0.15D, -0.2D);
/*      */           }
/*      */           
/*  978 */           if (class_2248.method_9503(item.method_7909()).method_9564().method_26164(class_3481.field_24076)) {
/*  979 */             matrices.method_22904(0.0D, 0.1D, 0.0D);
/*      */           }
/*      */           
/*  982 */           if (item.method_31574(class_1802.field_8828) || item.method_31574(class_1802.field_21086) || class_2248.method_9503(item.method_7909()).method_9564().method_26164(class_3481.field_20339) || class_2248.method_9503(item.method_7909()).method_9564().method_26164(class_3481.field_15503) || class_2248.method_9503(item.method_7909()).method_9564().method_26164(class_3481.field_15462) || class_2248.method_9503(item.method_7909()).method_9564().method_26164(class_3481.field_44469)) {
/*  983 */             this.vertVelocityYSlime = (float)(this.vertVelocityYSlime + swingProgress * 0.03D * astra.deltaTime * 30.0D);
/*  984 */             if (((player.method_18798().method_1033() > 0.09D && player.method_24828()) || player.method_5681() || player.method_20448() || (player.method_6101() && !player.method_24828())) && ((Boolean)this.field_4050.field_1690.method_42448().method_41753()).booleanValue()) {
/*  985 */               Random random = new Random();
/*  986 */               boolean randomBoolean = random.nextBoolean();
/*  987 */               this.vertVelocityYSlime += (float)(-0.05D * player.method_18798().method_1033() * astra.deltaTime * 30.0D);
/*      */             } 
/*      */             
/*  990 */             matrices.method_22905(1.0F, 1.0F + this.vertAngleYSlime * -2.0F, 1.0F);
/*      */           } 
/*      */           
/*  993 */           class_2680 blockState = blockItem.method_7711().method_9564();
/*  994 */           if (player.field_6012 - this.prevAge >= 100.0F) {
/*  995 */             this.repPower = !this.repPower;
/*  996 */             this.prevAge = player.field_6012;
/*      */           } 
/*      */           
/*  999 */           if (blockItem.method_7711() == class_2246.field_10450 && this.repPower) {
/* 1000 */             blockState = (class_2680)blockState.method_11657((class_2769)class_2462.field_10911, Boolean.valueOf(true));
/*      */           }
/*      */           
/* 1003 */           if (blockItem.method_7711() == class_2246.field_10377 && this.repPower) {
/* 1004 */             blockState = (class_2680)blockState.method_11657((class_2769)class_2286.field_10911, Boolean.valueOf(true));
/*      */           }
/*      */           
/* 1007 */           if (blockItem.method_7711() == class_2246.field_10523 && player.method_5869()) {
/* 1008 */             blockState = (class_2680)blockState.method_11657((class_2769)class_2459.field_11446, Boolean.valueOf(false));
/*      */           }
/*      */           
/* 1011 */           if ((blockItem.method_7711() == class_2246.field_17350 || blockItem.method_7711() == class_2246.field_23860) && player.method_5869()) {
/* 1012 */             blockState = (class_2680)blockState.method_11657((class_2769)class_3922.field_17352, Boolean.valueOf(false));
/*      */           }
/*      */           
/* 1015 */           if (item.method_31573(class_3489.field_16444)) {
/* 1016 */             if (bl) {
/* 1017 */               matrices.method_22904(0.9D, 0.0D, 0.8D);
/*      */             }
/*      */             
/* 1020 */             matrices.method_22907(class_7833.field_40716.rotationDegrees((90 * l)));
/*      */           } 
/*      */           
/* 1023 */           blockRenderManager.method_3353(blockState, matrices, vertexConsumers, light, class_4608.field_21444);
/* 1024 */           matrices.method_22909();
/*      */         } else {
/* 1026 */           if ((item.method_31573(ConventionalItemTags.TOOLS) && !item.method_31573(class_3489.field_41890) && !item.method_31573(class_3489.field_40109) && item.method_7976() != class_1839.field_8950 && item.method_7923()) || item.method_7976() == class_1839.field_8953 || item.method_7976() == class_1839.field_27079 || getAttackDamage(item) != 0.0F || item.method_7976() == class_1839.field_8949 || item.method_31574(class_1802.field_23254) || item.method_31574(class_1802.field_8184) || item.method_31574(class_1802.field_8378) || item.method_31574(class_1802.field_8868)) {
/* 1027 */             if (item.method_31573(class_3489.field_42611) && !sharpSword) {
/* 1028 */               matrices.method_22907(class_7833.field_40714.rotationDegrees(-60.0F * swing));
/* 1029 */               matrices.method_22904(0.0D, 0.1D * swing, -0.1D * swing);
/*      */             } 
/*      */             
/* 1032 */             if (item.method_31573(class_3489.field_42615)) {
/* 1033 */               matrices.method_22907(class_7833.field_40714.rotationDegrees(-80.0F * swing_rot));
/* 1034 */               matrices.method_22907(class_7833.field_40714.rotationDegrees(30.0F * swing));
/* 1035 */             } else if (item.method_7976() == class_1839.field_8951) {
/* 1036 */               matrices.method_22907(class_7833.field_40714.rotationDegrees(-40.0F * swing_rot));
/* 1037 */               matrices.method_22904(0.0D, 0.1D * swing_rot, -0.1D * swing_rot);
/* 1038 */             } else if (item.method_7976() != class_1839.field_8949) {
/* 1039 */               matrices.method_22907(class_7833.field_40714.rotationDegrees(-25.0F * swing));
/* 1040 */               matrices.method_22904(0.0D, 0.05D * swing, -0.05D * swing);
/*      */             } 
/*      */           } 
/*      */           
/* 1044 */           if (!item.method_31574(class_1802.field_8137) && (!item.method_31574(class_1802.field_8301) || !config.mb3DCompat)) {
/* 1045 */             this.netherCounter = 0.0F;
/*      */           } else {
/* 1047 */             this.netherCounter = (float)(this.netherCounter + 0.9D * tt);
/* 1048 */             matrices.method_22904(0.0D, 0.25D + 0.02D * class_3532.method_15374(this.netherCounter * 0.1F), 0.0D);
/* 1049 */             matrices.method_22907(class_7833.field_40714.rotationDegrees(3.0F * class_3532.method_15374(this.netherCounter * 0.2F)));
/* 1050 */             matrices.method_22905(1.0F + 0.01F * class_3532.method_15374(this.netherCounter), 1.0F + 0.01F * class_3532.method_15374(this.netherCounter), 1.0F + 0.01F * class_3532.method_15374(this.netherCounter));
/*      */           } 
/*      */           
/* 1053 */           if (config.mb3DCompat) {
/* 1054 */             if (item.method_31573(class_3489.field_42611)) {
/* 1055 */               matrices.method_22904(0.0D, 0.2D, 0.0D);
/*      */             }
/*      */             
/* 1058 */             if (item.method_31574(class_1802.field_8153) || item.method_31574(class_1802.field_8777) || item.method_31574(class_1802.field_8323)) {
/* 1059 */               this.vertVelocityYSlime = (float)(this.vertVelocityYSlime + swingProgress * 0.03D * astra.deltaTime * 30.0D);
/* 1060 */               if (((player.method_18798().method_1033() > 0.09D && player.method_24828()) || player.method_5681() || player.method_20448() || (player.method_6101() && !player.method_24828())) && ((Boolean)this.field_4050.field_1690.method_42448().method_41753()).booleanValue()) {
/* 1061 */                 Random random = new Random();
/* 1062 */                 boolean randomBoolean = random.nextBoolean();
/* 1063 */                 this.vertVelocityYSlime += (float)(-0.05D * player.method_18798().method_1033() * astra.deltaTime * 30.0D);
/*      */               } 
/*      */               
/* 1066 */               matrices.method_22905(1.0F, 1.0F + this.vertAngleYSlime * -2.0F, 1.0F);
/*      */             } 
/*      */           } 
/*      */           
/* 1070 */           if (item.method_31573(class_3489.field_42615)) {
/* 1071 */             matrices.method_22904(0.07D * l, 0.0D, 0.05D);
/* 1072 */             matrices.method_22907(class_7833.field_40716.rotationDegrees((90 * l)));
/* 1073 */             matrices.method_22907(class_7833.field_40714.rotationDegrees(-15.0F));
/*      */           } 
/*      */           
/* 1076 */           if (item.method_31574(class_1802.field_8810)) {
/* 1077 */             player.method_37908().method_8406((class_2394)class_2398.field_11246, player.method_19538().method_10216(), player.method_19538().method_10214(), player.method_19538().method_10215(), 0.1D, 0.1D, 0.1D);
/*      */           }
/*      */           
/* 1080 */           HeldItemRendererAccessor acc = (HeldItemRendererAccessor)this;
/* 1081 */           acc.invokeRenderItem((class_1309)player, item, bl2 ? class_811.field_4322 : class_811.field_4321, !bl2, matrices, vertexConsumers, light);
/*      */         } 
/*      */       } 
/*      */       
/* 1085 */       matrices.method_22909();
/* 1086 */       matrices.method_22909();
/* 1087 */       this.isAttacking = this.field_4050.field_1690.field_1886.method_1434();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   @Shadow
/*      */   protected abstract void method_3228(class_742 paramclass_742, float paramFloat1, float paramFloat2, class_1268 paramclass_1268, float paramFloat3, class_1799 paramclass_1799, float paramFloat4, class_4587 paramclass_4587, class_4597 paramclass_4597, int paramInt);
/*      */ 
/*      */   
/*      */   @Shadow
/*      */   protected abstract void method_65816(float paramFloat1, float paramFloat2, class_4587 paramclass_4587, int paramInt, class_1306 paramclass_1306);
/*      */ 
/*      */   
/*      */   @Shadow
/*      */   private static class_759.class_5773 method_33303(class_746 player) {
/* 1102 */     throw new AssertionError();
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\mixin\HeldItemRendererHmiMixin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */