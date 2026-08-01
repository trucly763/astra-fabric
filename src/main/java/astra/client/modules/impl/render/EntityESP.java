/*      */ package shame.astra.client.modules.impl.render;
/*      */ import com.mojang.blaze3d.platform.GlStateManager;
/*      */ import com.mojang.blaze3d.systems.RenderSystem;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Optional;
/*      */ import java.util.UUID;
/*      */ import net.minecraft.class_10142;
/*      */ import net.minecraft.class_10366;
/*      */ import net.minecraft.class_1297;
/*      */ import net.minecraft.class_1309;
/*      */ import net.minecraft.class_1542;
/*      */ import net.minecraft.class_1657;
/*      */ import net.minecraft.class_1792;
/*      */ import net.minecraft.class_1799;
/*      */ import net.minecraft.class_1814;
/*      */ import net.minecraft.class_238;
/*      */ import net.minecraft.class_243;
/*      */ import net.minecraft.class_2561;
/*      */ import net.minecraft.class_2583;
/*      */ import net.minecraft.class_268;
/*      */ import net.minecraft.class_276;
/*      */ import net.minecraft.class_284;
/*      */ import net.minecraft.class_286;
/*      */ import net.minecraft.class_287;
/*      */ import net.minecraft.class_289;
/*      */ import net.minecraft.class_290;
/*      */ import net.minecraft.class_293;
/*      */ import net.minecraft.class_3532;
/*      */ import net.minecraft.class_4587;
/*      */ import net.minecraft.class_5944;
/*      */ import net.minecraft.class_6367;
/*      */ import net.minecraft.class_640;
/*      */ import net.minecraft.class_7923;
/*      */ import org.joml.Matrix4f;
/*      */ import org.joml.Matrix4fc;
/*      */ import org.joml.Quaternionf;
/*      */ import org.joml.Quaternionfc;
/*      */ import org.joml.Vector3f;
/*      */ import org.joml.Vector4f;
/*      */ import org.lwjgl.opengl.GL11;
/*      */ import org.lwjgl.opengl.GL30;
/*      */ import shame.astra.api.events.EventLink;
/*      */ import shame.astra.api.events.implement.Event3DRender;
/*      */ import shame.astra.api.events.implement.EventRender;
/*      */ import shame.astra.api.storages.implement.helpertstorages.Theme;
/*      */ import shame.astra.api.storages.implement.helpertstorages.enumvar.ModuleClass;
/*      */ import shame.astra.api.utils.color.ColorUtils;
/*      */ import shame.astra.api.utils.render.RenderUtils;
/*      */ import shame.astra.api.utils.render.ShaderUtils;
/*      */ import shame.astra.api.utils.render.font.ReplaceSymbols;
/*      */ import shame.astra.api.utils.render.fonts.msdf.Font;
/*      */ import shame.astra.astra;
/*      */ import shame.astra.client.modules.Module;
/*      */ import shame.astra.client.modules.impl.misc.NameProtect;
/*      */ import shame.astra.client.modules.impl.misc.ScoreboardHP;
/*      */ import shame.astra.client.modules.settings.Setting;
/*      */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*      */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*      */ import shame.astra.client.modules.settings.implement.ListSetting;
/*      */ import shame.astra.client.modules.settings.implement.ModeSetting;
/*      */ 
/*      */ public class EntityESP extends Module {
/*   67 */   public static EntityESP INSTANCE = new EntityESP();
/*      */   
/*      */   private static final float TAG_FROM_ENTITY_GAP = 0.0F;
/*      */   private static final int TAG_FONT_SIZE = 13;
/*      */   private static final int TAG_TEXT_COLOR = -1;
/*      */   private static final int TAG_HEALTH_COLOR = -43691;
/*      */   private static final int TAG_FRIEND_COLOR = -11141291;
/*      */   private static final float TAG_HUD_RADIUS = 1.1F;
/*      */   private static final int TAG_HUD_ALPHA = 204;
/*      */   private static final float ARMOR_CELL_SIZE = 8.4F;
/*      */   private static final float ARMOR_ITEM_SCALE = 0.46F;
/*      */   private static final float ARMOR_CELL_GAP = 1.0F;
/*      */   private static final float PLAYER_HEAD_SIZE = 7.5F;
/*      */   private static final float PLAYER_HEAD_GAP = 3.0F;
/*      */   private static final float BOX_LINE_WIDTH = 1.5F;
/*      */   private static final float FILL_ALPHA = 0.23F;
/*      */   private static final float EPSILON = 0.001F;
/*      */   private static final long DONATE_CACHE_TTL_MS = 1000L;
/*      */   private static final long DONATE_CACHE_CLEANUP_MS = 2000L;
/*      */   private static final int MAX_ITEM_TAGS_PER_FRAME = 48;
/*   87 */   private final ListSetting elements = new ListSetting("Элементы", new BooleanSetting[] { new BooleanSetting("Теги", true), new BooleanSetting("Броня", true) });
/*      */ 
/*      */ 
/*      */   
/*   91 */   private final BooleanSetting show3DBox = new BooleanSetting("Боксы", true);
/*   92 */   private final BooleanSetting boxFilled = new BooleanSetting("Заполнить бокс", true);
/*   93 */   private final ModeSetting boxFillMode = new ModeSetting("Мод заливки", "Обычный", new String[] { "Обычный", "Волны", "Нитки" });
/*   94 */   private final FloatSetting waveSpeed = (new FloatSetting("Скорость волн", 1.2F, 0.1F, 5.0F, 0.1F))
/*   95 */     .visible(() -> Boolean.valueOf(this.boxFillMode.is("Волны")));
/*   96 */   private final FloatSetting waveScale = (new FloatSetting("Размер волн", 1.0F, 1.0F, 3.0F, 0.1F))
/*   97 */     .visible(() -> Boolean.valueOf(this.boxFillMode.is("Волны")));
/*   98 */   private final FloatSetting lineSpeed = (new FloatSetting("Скорость линий", 1.4F, 0.1F, 5.0F, 0.1F))
/*   99 */     .visible(() -> Boolean.valueOf((this.boxFillMode.getIndex() == 2)));
/*  100 */   private final FloatSetting lineJitter = (new FloatSetting("Прыжки линий", 0.55F, 0.0F, 1.5F, 0.01F))
/*  101 */     .visible(() -> Boolean.valueOf((this.boxFillMode.getIndex() == 2)));
/*  102 */   private final FloatSetting outline = (new FloatSetting("Обводка", 1.1F, 0.1F, 5.0F, 0.1F))
/*  103 */     .visible(this::isPostBoxMode);
/*  104 */   private final FloatSetting glow = (new FloatSetting("Свечение", 1.0F, 0.0F, 5.0F, 0.1F))
/*  105 */     .visible(this::isPostBoxMode);
/*  106 */   private final FloatSetting fill = (new FloatSetting("Сила заливки", 0.6F, 0.0F, 1.0F, 0.01F))
/*  107 */     .visible(this::isPostBoxMode);
/*  108 */   private final FloatSetting alpha = (new FloatSetting("Прозрачность", 1.0F, 0.0F, 4.0F, 0.01F))
/*  109 */     .visible(this::isPostBoxMode);
/*      */   
/*  111 */   private final BooleanSetting hurtTint = new BooleanSetting("Краснеть при ударе", true);
/*  112 */   private final Matrix4f lastProjectionMatrix = new Matrix4f();
/*  113 */   private final Quaternionf lastCameraRotation = new Quaternionf();
/*  114 */   private final Quaternionf lastInverseCameraRotation = new Quaternionf();
/*  115 */   private class_243 lastCameraPos = class_243.field_1353;
/*      */   private float lastTickDelta;
/*      */   private int lastScaledWidth;
/*      */   private int lastScaledHeight;
/*      */   private boolean hasProjection;
/*      */   private class_276 maskBuffer;
/*  121 */   private final List<class_276> bloomBuffers = new ArrayList<>();
/*  122 */   private final Map<UUID, DonateCache> donateCache = new HashMap<>();
/*  123 */   private final Map<Integer, Float> entityHurtTintProgress = new HashMap<>();
/*      */   private long nextDonateCacheCleanupAt;
/*  125 */   private int maskWidth = -1;
/*  126 */   private int maskHeight = -1;
/*      */   private boolean hasShaderMask;
/*  128 */   private final Vector3f projectionScratch = new Vector3f();
/*  129 */   private final Vector4f clipScratch = new Vector4f();
/*  130 */   private final ProjectedPoint projectedPoint = new ProjectedPoint();
/*  131 */   private final class_1799[] armorStacksScratch = new class_1799[6];
/*  132 */   private final boolean[] armorHandScratch = new boolean[6];
/*  133 */   private int frameThemeColor = -1;
/*  134 */   private final BooleanSetting targetPlayers = new BooleanSetting("Игроки", true);
/*  135 */   private final BooleanSetting targetMobs = new BooleanSetting("Мобы", true);
/*  136 */   private final BooleanSetting targetAnimals = new BooleanSetting("Животные", true);
/*  137 */   private final BooleanSetting targetItems = new BooleanSetting("Предметы", true);
/*  138 */   private final ListSetting targets = new ListSetting("Отображать", new BooleanSetting[] { this.targetPlayers, this.targetMobs, this.targetAnimals, this.targetItems });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public EntityESP() {
/*  146 */     super("EntityESP", "Показывает игроков через стену", Module.ModuleCategory.RENDER);
/*  147 */     addSettings(new Setting[] { (Setting)this.targets, (Setting)this.elements });
/*  148 */     addSettings(new Setting[] { (Setting)this.show3DBox, (Setting)this.boxFilled, (Setting)this.hurtTint });
/*      */   }
/*      */ 
/*      */   
/*      */   public void onDisable() {
/*  153 */     this.hasProjection = false;
/*  154 */     this.hasShaderMask = false;
/*  155 */     this.donateCache.clear();
/*  156 */     this.entityHurtTintProgress.clear();
/*  157 */     this.nextDonateCacheCleanupAt = 0L;
/*  158 */     if (this.maskBuffer != null) {
/*  159 */       this.maskBuffer.method_1238();
/*  160 */       this.maskBuffer = null;
/*      */     } 
/*  162 */     for (class_276 fb : this.bloomBuffers) {
/*  163 */       fb.method_1238();
/*      */     }
/*  165 */     this.bloomBuffers.clear();
/*  166 */     super.onDisable();
/*      */   }
/*      */   
/*      */   @EventLink(priority = 100)
/*      */   public void onRender3D(Event3DRender event) {
/*  171 */     this.hasProjection = true;
/*  172 */     this.lastProjectionMatrix.set((Matrix4fc)event.getProjectionMatrix());
/*  173 */     this.lastCameraPos = event.getCamera().method_19326();
/*  174 */     this.lastCameraRotation.set((Quaternionfc)event.getCamera().method_23767());
/*  175 */     this.lastInverseCameraRotation.set((Quaternionfc)this.lastCameraRotation).conjugate();
/*  176 */     this.lastTickDelta = event.getTickDelta();
/*  177 */     this.lastScaledWidth = mc.method_22683().method_4486();
/*  178 */     this.lastScaledHeight = mc.method_22683().method_4502();
/*  179 */     this.frameThemeColor = getStableThemeColor();
/*      */     
/*  181 */     this.hasShaderMask = false;
/*  182 */     if (!this.show3DBox.isState() || mc.field_1687 == null || mc.field_1724 == null)
/*  183 */       return;  class_4587 matrices = event.getMatrices();
/*  184 */     float tickDelta = event.getTickDelta();
/*  185 */     boolean postMode = isPostBoxMode();
/*  186 */     boolean threadMode = isThreadMode();
/*      */     
/*  188 */     if (postMode) {
/*  189 */       ensureMaskBuffer();
/*  190 */       if (this.maskBuffer != null) {
/*  191 */         this.maskBuffer.method_1236(0.0F, 0.0F, 0.0F, 0.0F);
/*  192 */         this.maskBuffer.method_1230();
/*  193 */         copyMainDepthToMask();
/*  194 */         this.maskBuffer.method_1235(false);
/*  195 */         RenderSystem.disableBlend();
/*  196 */         RenderSystem.enableDepthTest();
/*  197 */         RenderSystem.depthMask(false);
/*  198 */         RenderSystem.disableCull();
/*  199 */         RenderSystem.setShader(class_10142.field_53876);
/*      */       } 
/*      */     } 
/*      */     
/*  203 */     for (class_1297 entity : mc.field_1687.method_18112()) {
/*  204 */       if (!shouldProcess3DEntity(entity))
/*  205 */         continue;  if (postMode && this.maskBuffer != null) {
/*  206 */         drawPlayerMaskBox(matrices, entity, tickDelta);
/*  207 */         this.hasShaderMask = true; continue;
/*      */       } 
/*  209 */       render3DBox(matrices, entity, tickDelta);
/*      */     } 
/*      */ 
/*      */     
/*  213 */     if (postMode && this.maskBuffer != null) {
/*  214 */       RenderSystem.disableBlend();
/*  215 */       RenderSystem.depthMask(true);
/*  216 */       RenderSystem.enableDepthTest();
/*  217 */       RenderSystem.enableCull();
/*  218 */       mc.method_1522().method_1235(true);
/*  219 */       if (this.show3DBox.isState()) {
/*  220 */         renderShaderBoxesWorldPass();
/*      */       }
/*      */     } 
/*      */     
/*  224 */     if (threadMode) {
/*  225 */       for (class_1297 entity : mc.field_1687.method_18112()) {
/*  226 */         if (!shouldProcess3DEntity(entity))
/*  227 */           continue;  renderThreadWeb(matrices, entity, tickDelta);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   @EventLink(priority = 100)
/*      */   public void onRender2D(EventRender.Default event) {
/*  234 */     if (!this.hasProjection || mc.field_1687 == null || mc.field_1724 == null)
/*  235 */       return;  this.frameThemeColor = getStableThemeColor();
/*  236 */     boolean tagsEnabled = (!this.elements.getSettings().isEmpty() && ((BooleanSetting)this.elements.getSettings().get(0)).isState());
/*  237 */     boolean armorEnabled = (this.elements.getSettings().size() > 1 && ((BooleanSetting)this.elements.getSettings().get(1)).isState());
/*  238 */     if (!tagsEnabled && !armorEnabled)
/*      */       return; 
/*  240 */     Font font = tagsEnabled ? Fonts.getFont("sf_regular", 13) : null;
/*  241 */     int renderedItemTags = 0;
/*      */     
/*  243 */     for (class_1297 entity : mc.field_1687.method_18112()) {
/*  244 */       if (entity instanceof class_1657) { class_1657 player = (class_1657)entity;
/*  245 */         if (!shouldProcess2DPlayer(player))
/*      */           continue; 
/*  247 */         class_238 interpolatedBox = getInterpolatedBox((class_1297)player, this.lastTickDelta);
/*  248 */         ScreenRect rect = projectBox(interpolatedBox);
/*  249 */         if (rect == null)
/*      */           continue; 
/*  251 */         if (tagsEnabled && font != null) {
/*  252 */           drawTag(event, player, rect, font);
/*      */         }
/*  254 */         if (armorEnabled) {
/*  255 */           drawArmor(event, player, rect, tagsEnabled);
/*      */         }
/*      */         
/*      */         continue; }
/*      */       
/*  260 */       if (!tagsEnabled || font == null) {
/*      */         continue;
/*      */       }
/*      */       
/*  264 */       if (entity instanceof class_1542) { class_1542 itemEntity = (class_1542)entity;
/*  265 */         if (!shouldProcessItem2D(itemEntity) || renderedItemTags >= 48) {
/*      */           continue;
/*      */         }
/*      */         
/*  269 */         if (projectEntityAnchor((class_1297)itemEntity, itemEntity.method_17682() + 0.25D, this.projectedPoint)) {
/*  270 */           drawDroppedItemTag(event, itemEntity, this.projectedPoint.x, this.projectedPoint.y, font);
/*  271 */           renderedItemTags++;
/*      */         } 
/*      */         
/*      */         continue; }
/*      */       
/*  276 */       if (entity instanceof class_1309) { class_1309 livingEntity = (class_1309)entity; if (!shouldProcessLiving2D(livingEntity)) {
/*      */           continue;
/*      */         }
/*      */         
/*  280 */         class_238 interpolatedBox = getInterpolatedBox((class_1297)livingEntity, this.lastTickDelta);
/*  281 */         ScreenRect rect = projectBox(interpolatedBox);
/*  282 */         if (rect == null)
/*      */           continue; 
/*  284 */         drawLivingTag(event, livingEntity, rect, font); }
/*      */     
/*      */     } 
/*      */   }
/*      */   private void drawTag(EventRender.Default event, class_1657 player, ScreenRect rect, Font font) {
/*  289 */     class_4587 matrices = event.getContext().method_51448();
/*  290 */     List<DonateSegment> donateSegments = getDonateSegmentsFromTab(player);
/*  291 */     String nameText = getProtectedName(player.method_5820());
/*  292 */     float hp = ScoreboardHP.getHealthWithAbsorption((class_1309)player);
/*  293 */     String leftBracket = "";
/*  294 */     String hpText = "" + Math.round(hp) + " hp";
/*  295 */     String rightBracket = "";
/*      */     
/*  297 */     boolean isFriend = (astra.INSTANCE.friendStorage != null && astra.INSTANCE.friendStorage.isFriend(player.method_5477().getString()));
/*  298 */     String friendSuffix = isFriend ? " [F]" : "";
/*      */     
/*  300 */     float donateWidth = 0.0F;
/*  301 */     for (DonateSegment segment : donateSegments) {
/*  302 */       donateWidth += font.getStringWidth(segment.text());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  309 */     float totalWidth = donateWidth + font.getStringWidth(nameText) + font.getStringWidth(leftBracket) + font.getStringWidth(hpText) + font.getStringWidth(rightBracket) + font.getStringWidth(friendSuffix) + 7.5F + 3.0F + 2.0F;
/*      */ 
/*      */     
/*  312 */     float boxHeight = 16.0F;
/*  313 */     float x = rect.centerX() - totalWidth * 0.5F;
/*  314 */     float y = getTagTopY(rect, boxHeight);
/*      */     
/*  316 */     drawDefaultTagPanel(matrices, x - 1.0F, y - 0.5F, totalWidth + 2.0F, boxHeight - 4.0F);
/*      */     
/*  318 */     float headY = y + 1.7F;
/*  319 */     RenderUtils.drawPlayerHead(matrices, player.method_5667(), x + 1.0F, headY, 7.5F, 1.0F, 1.0F, 0.0F);
/*      */     
/*  321 */     float drawX = x + 1.5F + 7.5F + 3.0F;
/*  322 */     for (DonateSegment segment : donateSegments) {
/*  323 */       font.drawString(matrices, segment.text(), drawX, y + 4.0F, segment.color());
/*  324 */       drawX += font.getStringWidth(segment.text());
/*      */     } 
/*  326 */     font.drawString(matrices, nameText, drawX, y + 4.0F, -1);
/*  327 */     drawX += font.getStringWidth(nameText);
/*  328 */     font.drawString(matrices, leftBracket, drawX, y + 4.0F, -1);
/*  329 */     drawX += font.getStringWidth(leftBracket);
/*  330 */     font.drawString(matrices, hpText, drawX, y + 4.0F, -43691);
/*  331 */     drawX += font.getStringWidth(hpText);
/*  332 */     font.drawString(matrices, rightBracket, drawX, y + 4.0F, -1);
/*  333 */     drawX += font.getStringWidth(rightBracket);
/*  334 */     if (isFriend) font.drawString(matrices, friendSuffix, drawX, y + 4.0F, -11141291); 
/*      */   }
/*      */   
/*      */   private void drawArmor(EventRender.Default event, class_1657 player, ScreenRect rect, boolean tagsEnabled) {
/*  338 */     class_4587 matrices = event.getContext().method_51448();
/*  339 */     int count = 0;
/*  340 */     class_1799 offHand = player.method_6079();
/*  341 */     if (!offHand.method_7960()) {
/*  342 */       this.armorStacksScratch[count] = offHand;
/*  343 */       this.armorHandScratch[count++] = true;
/*      */     } 
/*  345 */     for (class_1799 stack : player.method_5661()) {
/*  346 */       if (!stack.method_7960()) {
/*  347 */         this.armorStacksScratch[count] = stack;
/*  348 */         this.armorHandScratch[count++] = false;
/*      */       } 
/*      */     } 
/*  351 */     class_1799 mainHand = player.method_6047();
/*  352 */     if (!mainHand.method_7960()) {
/*  353 */       this.armorStacksScratch[count] = mainHand;
/*  354 */       this.armorHandScratch[count++] = true;
/*      */     } 
/*  356 */     if (count == 0)
/*      */       return; 
/*  358 */     float step = 9.4F;
/*  359 */     float rowWidth = count * 8.4F + Math.max(0, count - 1) * 1.0F;
/*  360 */     float x = rect.centerX() - rowWidth * 0.5F;
/*      */ 
/*      */     
/*  363 */     float y = tagsEnabled ? (getTagTopY(rect, 14.0F) - 13.0F) : (rect.minY() - 13.0F);
/*      */     int i;
/*  365 */     for (i = 0; i < count; i++) {
/*  366 */       float drawX = x + i * step;
/*  367 */       float drawY = y;
/*  368 */       drawDefaultTagPanel(matrices, drawX, drawY, 8.4F, 8.4F);
/*      */     } 
/*      */     
/*  371 */     RenderSystem.enableBlend();
/*  372 */     RenderSystem.defaultBlendFunc();
/*  373 */     RenderSystem.disableDepthTest();
/*  374 */     RenderSystem.depthMask(false);
/*  375 */     for (i = 0; i < count; i++) {
/*  376 */       float drawX = x + i * step;
/*  377 */       float drawY = y;
/*  378 */       int stackIndex = count - 1 - i;
/*  379 */       class_1799 stack = this.armorStacksScratch[stackIndex];
/*  380 */       boolean handStack = this.armorHandScratch[stackIndex];
/*      */       
/*  382 */       matrices.method_22903();
/*  383 */       float itemSize = 7.36F;
/*  384 */       float itemX = drawX + (8.4F - itemSize) * 0.5F;
/*  385 */       float itemY = drawY + (8.4F - itemSize) * 0.5F;
/*  386 */       matrices.method_46416(itemX, itemY, 0.0F);
/*  387 */       matrices.method_22905(0.46F, 0.46F, 1.0F);
/*  388 */       event.getContext().method_51427(stack, 0, 0);
/*  389 */       if (!handStack) {
/*  390 */         event.getContext().method_51432(mc.field_1772, stack, 0, 0, null);
/*      */       }
/*  392 */       matrices.method_22909();
/*      */     } 
/*  394 */     RenderSystem.depthMask(true);
/*  395 */     RenderSystem.enableDepthTest();
/*  396 */     RenderSystem.disableBlend();
/*      */     
/*  398 */     for (i = 0; i < count; i++) {
/*  399 */       this.armorStacksScratch[i] = class_1799.field_8037;
/*  400 */       this.armorHandScratch[i] = false;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void drawLivingTag(EventRender.Default event, class_1309 entity, ScreenRect rect, Font font) {
/*  405 */     class_4587 matrices = event.getContext().method_51448();
/*  406 */     class_1657 player = (class_1657)entity;
/*      */     
/*  408 */     String nameText = (entity instanceof class_1657) ? getProtectedName(player.method_5476().getString()) : entity.method_5476().getString();
/*  409 */     String hpText = "" + Math.round(ScoreboardHP.getHealthWithAbsorption(entity)) + " hp";
/*  410 */     float totalWidth = font.getStringWidth(nameText) + font.getStringWidth(" ") + font.getStringWidth(hpText);
/*  411 */     float boxHeight = 14.0F;
/*  412 */     float x = rect.centerX() - totalWidth * 0.5F;
/*  413 */     float y = getTagTopY(rect, boxHeight);
/*      */     
/*  415 */     drawDefaultTagPanel(matrices, x - 1.0F, y - 0.5F, totalWidth + 2.0F, boxHeight - 4.0F);
/*  416 */     font.drawString(matrices, nameText, x, y + 3.0F, -1);
/*  417 */     font.drawString(matrices, hpText, x + font.getStringWidth(nameText) + font.getStringWidth(" "), y + 3.0F, -43691);
/*      */   }
/*      */   
/*      */   private void drawDroppedItemTag(EventRender.Default event, class_1542 itemEntity, float anchorX, float anchorY, Font font) {
/*  421 */     class_4587 matrices = event.getContext().method_51448();
/*  422 */     class_1799 stack = itemEntity.method_6983();
/*  423 */     String countText = "" + stack.method_7947() + "x";
/*  424 */     List<DonateSegment> itemSegments = getStyledTextSegments(stack.method_7964(), getDroppedItemTextColor(stack));
/*  425 */     int countColor = ColorUtils.rgba(155, 155, 155, 255);
/*  426 */     float itemNameWidth = 0.0F;
/*  427 */     for (DonateSegment segment : itemSegments) {
/*  428 */       itemNameWidth += font.getStringWidth(segment.text());
/*      */     }
/*  430 */     float spaceWidth = font.getStringWidth(" ");
/*  431 */     float totalWidth = itemNameWidth + spaceWidth + font.getStringWidth(countText);
/*  432 */     float boxHeight = 14.0F;
/*  433 */     float x = anchorX - totalWidth * 0.5F;
/*  434 */     float y = anchorY - boxHeight - 2.0F;
/*      */     
/*  436 */     drawDefaultTagPanel(matrices, x - 2.0F, y - 0.5F, totalWidth + 4.0F, boxHeight - 3.0F);
/*  437 */     float drawX = x;
/*  438 */     for (DonateSegment segment : itemSegments) {
/*  439 */       font.drawString(matrices, segment.text(), drawX, y + 3.5F, segment.color());
/*  440 */       drawX += font.getStringWidth(segment.text());
/*      */     } 
/*  442 */     font.drawString(matrices, countText, drawX + spaceWidth, y + 3.5F, countColor);
/*      */   }
/*      */   
/*      */   private int getMinecraftItemNameColor(class_1799 stack) {
/*  446 */     class_2561 name = stack.method_7964();
/*  447 */     if (name != null) {
/*  448 */       int[] discoveredColor = { 0 };
/*  449 */       boolean[] found = { false };
/*  450 */       name.method_27658((style, string) -> { if (!found[0] && style != null && style.method_10973() != null) { discoveredColor[0] = 0xFF000000 | style.method_10973().method_27716(); found[0] = true; }  return found[0] ? Optional.<String>of(string) : Optional.empty(); }class_2583.field_24360);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  457 */       if (found[0]) return discoveredColor[0];
/*      */     
/*      */     } 
/*  460 */     switch (stack.method_7932()) { default: throw new MatchException(null, null);case field_8907: case field_8903: case field_8904: case field_8906: break; }  return 
/*      */ 
/*      */ 
/*      */       
/*  464 */       -1;
/*      */   }
/*      */ 
/*      */   
/*      */   private int getDroppedItemTextColor(class_1799 stack) {
/*  469 */     return getMinecraftItemNameColor(stack);
/*      */   }
/*      */   
/*      */   private boolean isNetheriteItem(class_1792 item) {
/*  473 */     return class_7923.field_41178.method_10221(item).method_12832().contains("netherite");
/*      */   }
/*      */   
/*      */   private void drawDefaultTagPanel(class_4587 matrices, float x, float y, float width, float height) {
/*  477 */     int themeColor = this.frameThemeColor;
/*  478 */     RenderUtils.drawDefaultHudPanel(matrices, x, y, width, height, 1.1F, 1.1F, 
/*      */ 
/*      */         
/*  481 */         ColorUtils.rgba(50, 50, 50, 204), 
/*  482 */         ColorUtils.setAlphaColor(ColorUtils.darken(themeColor, 0.15F), 204), 
/*  483 */         ColorUtils.setAlphaColor(ColorUtils.darken(themeColor, 0.05F), 204));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean shouldHideVanillaTags() {
/*  488 */     return (isEnable() && !this.elements.getSettings().isEmpty() && ((BooleanSetting)this.elements.getSettings().get(0)).isState());
/*      */   }
/*      */   
/*      */   private float getTagTopY(ScreenRect rect, float tagHeight) {
/*  492 */     return rect.minY() - tagHeight - 0.0F;
/*      */   }
/*      */   
/*      */   private String[] getNameVariants(class_1657 player) {
/*  496 */     String profileName = (player.method_7334() != null) ? player.method_7334().getName() : "";
/*  497 */     String scoreboardName = player.method_5820();
/*  498 */     String protectedScoreboardName = getProtectedName(scoreboardName);
/*  499 */     String protectedProfileName = getProtectedName(profileName);
/*  500 */     String protectedPlainName = getProtectedName(player.method_5477().getString());
/*  501 */     return new String[] { player
/*  502 */         .method_5477().getString(), protectedPlainName, scoreboardName, protectedScoreboardName, profileName, protectedProfileName };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String getProtectedName(String input) {
/*  512 */     NameProtect nameProtect = (ModuleClass.INSTANCE != null) ? ModuleClass.nameProtect : null;
/*  513 */     if (nameProtect == null || !nameProtect.isEnable()) {
/*  514 */       return input;
/*      */     }
/*  516 */     return nameProtect.patch(input);
/*      */   }
/*      */   
/*      */   private int findAnyNameIndex(String text, String[] names) {
/*  520 */     if (text == null || text.isEmpty() || names == null) return -1; 
/*  521 */     int best = -1;
/*  522 */     for (String name : names) {
/*  523 */       if (name != null && !name.isEmpty()) {
/*  524 */         int idx = indexOfIgnoreCase(text, name);
/*  525 */         if (idx >= 0 && (best == -1 || idx < best))
/*  526 */           best = idx; 
/*      */       } 
/*      */     } 
/*  529 */     return best;
/*      */   }
/*      */   
/*      */   private int indexOfIgnoreCase(String text, String search) {
/*  533 */     if (text == null || search == null || search.isEmpty()) return -1; 
/*  534 */     int limit = text.length() - search.length();
/*  535 */     for (int i = 0; i <= limit; i++) {
/*  536 */       if (text.regionMatches(true, i, search, 0, search.length())) {
/*  537 */         return i;
/*      */       }
/*      */     } 
/*  540 */     return -1;
/*      */   }
/*      */   
/*      */   private void trimSegmentsToLength(List<DonateSegment> segments, int maxLength) {
/*  544 */     int remaining = Math.max(0, maxLength);
/*  545 */     List<DonateSegment> trimmed = new ArrayList<>();
/*  546 */     for (DonateSegment seg : segments) {
/*  547 */       if (remaining <= 0)
/*  548 */         break;  String text = seg.text();
/*  549 */       if (text.length() <= remaining) {
/*  550 */         trimmed.add(seg);
/*  551 */         remaining -= text.length(); continue;
/*      */       } 
/*  553 */       trimmed.add(new DonateSegment(text.substring(0, remaining), seg.color()));
/*  554 */       remaining = 0;
/*      */     } 
/*      */     
/*  557 */     segments.clear();
/*  558 */     segments.addAll(trimmed);
/*      */   }
/*      */   
/*      */   private List<DonateSegment> getDonateSegmentsFromTab(class_1657 player) {
/*  562 */     long now = System.currentTimeMillis();
/*  563 */     DonateCache cache = this.donateCache.computeIfAbsent(player.method_5667(), uuid -> new DonateCache());
/*  564 */     if (now < cache.nextUpdateAt) {
/*  565 */       return cache.segments;
/*      */     }
/*      */     
/*  568 */     List<DonateSegment> segments = new ArrayList<>();
/*  569 */     if (mc.method_1562() == null) {
/*  570 */       cache.segments = Collections.emptyList();
/*  571 */       cache.nextUpdateAt = now + 1000L;
/*  572 */       return cache.segments;
/*      */     } 
/*      */     
/*  575 */     class_640 entry = mc.method_1562().method_2871(player.method_5667());
/*  576 */     if (entry == null) {
/*  577 */       cache.segments = Collections.emptyList();
/*  578 */       cache.nextUpdateAt = now + 1000L;
/*  579 */       return cache.segments;
/*      */     } 
/*      */     
/*  582 */     class_2561 displayName = entry.method_2971();
/*  583 */     if (displayName == null) displayName = player.method_5476(); 
/*  584 */     if (displayName == null) {
/*  585 */       cache.segments = Collections.emptyList();
/*  586 */       cache.nextUpdateAt = now + 1000L;
/*  587 */       return cache.segments;
/*      */     } 
/*      */     
/*  590 */     String[] nameVariants = getNameVariants(player);
/*  591 */     boolean[] foundName = { false };
/*      */     
/*  593 */     displayName.method_27658((style, string) -> { if (foundName[0] || string == null || string.isEmpty()) return Optional.empty();  String part = string.replace('\n', ' ').replace('\r', ' '); int nameIndex = findAnyNameIndex(part, nameVariants); String donatePart = (nameIndex >= 0) ? part.substring(0, nameIndex) : part; if (!donatePart.isEmpty()) { int baseColor = (style.method_10973() != null) ? style.method_10973().method_27716() : 16777215; appendColoredSegments(segments, donatePart, baseColor); }  if (nameIndex >= 0) foundName[0] = true;  return Optional.empty(); }class_2583.field_24360);
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
/*      */ 
/*      */     
/*  613 */     if (!foundName[0]) {
/*  614 */       segments.clear();
/*  615 */       class_268 team = player.method_5781();
/*  616 */       if (team != null && team.method_1144() != null) {
/*  617 */         appendTextSegments(segments, team.method_1144());
/*      */       }
/*      */     } 
/*      */     
/*  621 */     if (segments.isEmpty()) {
/*  622 */       cache.segments = Collections.emptyList();
/*  623 */       cache.nextUpdateAt = now + 1000L;
/*  624 */       cleanupDonateCache(now);
/*  625 */       return cache.segments;
/*      */     } 
/*      */     
/*  628 */     StringBuilder combined = new StringBuilder();
/*  629 */     for (DonateSegment seg : segments) {
/*  630 */       combined.append(seg.text());
/*      */     }
/*  632 */     int donateNameIndex = findAnyNameIndex(combined.toString(), nameVariants);
/*  633 */     if (donateNameIndex >= 0) {
/*  634 */       if (donateNameIndex == 0) {
/*  635 */         cache.segments = Collections.emptyList();
/*  636 */         cache.nextUpdateAt = now + 1000L;
/*  637 */         cleanupDonateCache(now);
/*  638 */         return cache.segments;
/*      */       } 
/*  640 */       trimSegmentsToLength(segments, donateNameIndex);
/*      */     } 
/*      */     
/*  643 */     if (segments.isEmpty()) {
/*  644 */       cache.segments = Collections.emptyList();
/*  645 */       cache.nextUpdateAt = now + 1000L;
/*  646 */       cleanupDonateCache(now);
/*  647 */       return cache.segments;
/*      */     } 
/*      */     
/*  650 */     StringBuilder textCheck = new StringBuilder();
/*  651 */     for (DonateSegment seg : segments) {
/*  652 */       textCheck.append(seg.text());
/*      */     }
/*  654 */     if (textCheck.toString().trim().isEmpty()) {
/*  655 */       cache.segments = Collections.emptyList();
/*  656 */       cache.nextUpdateAt = now + 1000L;
/*  657 */       cleanupDonateCache(now);
/*  658 */       return cache.segments;
/*      */     } 
/*      */     
/*  661 */     DonateSegment last = segments.get(segments.size() - 1);
/*  662 */     if (!last.text().endsWith(" ")) {
/*  663 */       segments.set(segments.size() - 1, new DonateSegment(last.text() + " ", last.color()));
/*      */     }
/*  665 */     cache.segments = List.copyOf(segments);
/*  666 */     cache.nextUpdateAt = now + 1000L;
/*  667 */     cleanupDonateCache(now);
/*  668 */     return cache.segments;
/*      */   }
/*      */   
/*      */   private void appendTextSegments(List<DonateSegment> out, class_2561 text) {
/*  672 */     text.method_27658((style, string) -> { if (string == null || string.isEmpty()) return Optional.empty();  int baseColor = (style.method_10973() != null) ? style.method_10973().method_27716() : 16777215; appendColoredSegments(out, string.replace('\n', ' ').replace('\r', ' '), baseColor); return Optional.empty(); }class_2583.field_24360);
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
/*      */   private List<DonateSegment> getStyledTextSegments(class_2561 text, int fallbackColor) {
/*  684 */     List<DonateSegment> segments = new ArrayList<>();
/*  685 */     if (text != null) {
/*  686 */       appendTextSegments(segments, text);
/*      */     }
/*  688 */     if (segments.isEmpty() && text != null && !text.getString().isEmpty()) {
/*  689 */       segments.add(new DonateSegment(text.getString(), fallbackColor));
/*      */     }
/*  691 */     return segments;
/*      */   }
/*      */   
/*      */   private void appendColoredSegments(List<DonateSegment> out, String text, int baseColor) {
/*  695 */     if (text == null || text.isEmpty())
/*  696 */       return;  int currentColor = baseColor;
/*  697 */     StringBuilder chunk = new StringBuilder();
/*      */     
/*  699 */     int chunkColor = currentColor;
/*      */     int offset;
/*  701 */     for (offset = 0; offset < text.length(); ) {
/*  702 */       int codePoint = text.codePointAt(offset);
/*  703 */       int charCount = Character.charCount(codePoint);
/*      */       
/*  705 */       if (codePoint == 167 && offset + charCount < text.length()) {
/*  706 */         flushSegment(out, chunk, chunkColor);
/*  707 */         char code = Character.toLowerCase(text.charAt(offset + charCount));
/*  708 */         Integer mappedColor = sectionColorToRgb(code);
/*  709 */         if (mappedColor != null) {
/*  710 */           currentColor = mappedColor.intValue();
/*  711 */         } else if (code == 'r') {
/*  712 */           currentColor = baseColor;
/*      */         } 
/*  714 */         chunkColor = currentColor;
/*  715 */         offset += charCount + 1;
/*      */         
/*      */         continue;
/*      */       } 
/*  719 */       String replacement = ReplaceSymbols.replaceCodePoint(codePoint);
/*  720 */       if (replacement != null) {
/*  721 */         flushSegment(out, chunk, chunkColor);
/*  722 */         int totalChars = Math.max(1, replacement.length());
/*  723 */         for (int i = 0; i < replacement.length(); i++) {
/*  724 */           int gradientColor = ReplaceSymbols.getGradientColorForReplacement(codePoint, i, totalChars, 1.0F, currentColor);
/*  725 */           if (chunk.length() > 0 && chunkColor != gradientColor) {
/*  726 */             flushSegment(out, chunk, chunkColor);
/*      */           }
/*  728 */           chunkColor = gradientColor;
/*  729 */           chunk.append(replacement.charAt(i));
/*      */         } 
/*  731 */         offset += charCount;
/*      */         
/*      */         continue;
/*      */       } 
/*  735 */       if (chunk.length() > 0 && chunkColor != currentColor) {
/*  736 */         flushSegment(out, chunk, chunkColor);
/*      */       }
/*  738 */       chunkColor = currentColor;
/*  739 */       chunk.appendCodePoint(codePoint);
/*  740 */       offset += charCount;
/*      */     } 
/*      */     
/*  743 */     flushSegment(out, chunk, chunkColor);
/*      */   }
/*      */   
/*      */   private void flushSegment(List<DonateSegment> out, StringBuilder chunk, int color) {
/*  747 */     if (chunk.isEmpty())
/*  748 */       return;  out.add(new DonateSegment(chunk.toString(), color));
/*  749 */     chunk.setLength(0);
/*      */   }
/*      */   
/*      */   private Integer sectionColorToRgb(char code) {
/*  753 */     switch (code) { case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9': case 'a': case 'b': case 'c': case 'd': case 'e': case 'f':  }  return 
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
/*  770 */       null;
/*      */   }
/*      */ 
/*      */   
/*      */   private void cleanupDonateCache(long now) {
/*  775 */     if (now < this.nextDonateCacheCleanupAt || mc.field_1687 == null) {
/*      */       return;
/*      */     }
/*  778 */     this.nextDonateCacheCleanupAt = now + 2000L;
/*  779 */     this.donateCache.entrySet().removeIf(entry -> (mc.field_1687.method_18470((UUID)entry.getKey()) == null));
/*      */   }
/*      */   
/*      */   private class_238 getInterpolatedBox(class_1297 entity, float tickDelta) {
/*  783 */     double x = class_3532.method_16436(tickDelta, entity.field_6038, entity.method_23317());
/*  784 */     double y = class_3532.method_16436(tickDelta, entity.field_5971, entity.method_23318());
/*  785 */     double z = class_3532.method_16436(tickDelta, entity.field_5989, entity.method_23321());
/*      */     
/*  787 */     double ox = x - entity.method_23317();
/*  788 */     double oy = y - entity.method_23318();
/*  789 */     double oz = z - entity.method_23321();
/*      */     
/*  791 */     return entity.method_5829().method_989(ox, oy, oz).method_1014(0.05D);
/*      */   }
/*      */   
/*      */   private ScreenRect projectBox(class_238 box) {
/*  795 */     double minX = Double.POSITIVE_INFINITY;
/*  796 */     double minY = Double.POSITIVE_INFINITY;
/*  797 */     double maxX = Double.NEGATIVE_INFINITY;
/*  798 */     double maxY = Double.NEGATIVE_INFINITY;
/*  799 */     boolean projectedAny = false;
/*      */     
/*  801 */     for (int xi = 0; xi < 2; xi++) {
/*  802 */       for (int yi = 0; yi < 2; yi++) {
/*  803 */         for (int zi = 0; zi < 2; zi++) {
/*  804 */           if (projectToScreen(
/*  805 */               (xi == 0) ? box.field_1323 : box.field_1320, 
/*  806 */               (yi == 0) ? box.field_1322 : box.field_1325, 
/*  807 */               (zi == 0) ? box.field_1321 : box.field_1324, this.projectedPoint)) {
/*      */ 
/*      */             
/*  810 */             projectedAny = true;
/*  811 */             minX = Math.min(minX, this.projectedPoint.x);
/*  812 */             minY = Math.min(minY, this.projectedPoint.y);
/*  813 */             maxX = Math.max(maxX, this.projectedPoint.x);
/*  814 */             maxY = Math.max(maxY, this.projectedPoint.y);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*  819 */     if (!projectedAny) return null; 
/*  820 */     if (minX > (mc.method_22683().method_4486() + 300) || maxX < -300.0D) return null; 
/*  821 */     if (minY > (mc.method_22683().method_4502() + 300) || maxY < -300.0D) return null; 
/*  822 */     if (maxX - minX < 2.0D || maxY - minY < 2.0D) return null;
/*      */     
/*  824 */     return new ScreenRect((float)minX, (float)minY, (float)maxX, (float)maxY);
/*      */   }
/*      */   
/*      */   private boolean projectToScreen(double worldX, double worldY, double worldZ, ProjectedPoint out) {
/*  828 */     this.projectionScratch.set((float)(worldX - this.lastCameraPos.field_1352), (float)(worldY - this.lastCameraPos.field_1351), (float)(worldZ - this.lastCameraPos.field_1350));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  833 */     this.projectionScratch.rotate((Quaternionfc)this.lastInverseCameraRotation);
/*      */     
/*  835 */     this.clipScratch.set(this.projectionScratch.x, this.projectionScratch.y, this.projectionScratch.z, 1.0F);
/*  836 */     this.lastProjectionMatrix.transform(this.clipScratch);
/*      */     
/*  838 */     float w = this.clipScratch.w;
/*  839 */     if (w <= 1.0E-5F) return false;
/*      */     
/*  841 */     float ndcX = this.clipScratch.x / w;
/*  842 */     float ndcY = this.clipScratch.y / w;
/*  843 */     float ndcZ = this.clipScratch.z / w;
/*      */     
/*  845 */     float screenX = (ndcX * 0.5F + 0.5F) * this.lastScaledWidth;
/*  846 */     float screenY = (1.0F - ndcY * 0.5F + 0.5F) * this.lastScaledHeight;
/*      */     
/*  848 */     if (Float.isNaN(screenX) || Float.isNaN(screenY)) return false; 
/*  849 */     if (Float.isInfinite(screenX) || Float.isInfinite(screenY)) return false;
/*      */     
/*  851 */     out.x = screenX;
/*  852 */     out.y = screenY;
/*  853 */     out.z = ndcZ;
/*  854 */     return true;
/*      */   }
/*      */   
/*      */   private boolean projectEntityAnchor(class_1297 entity, double yOffset, ProjectedPoint out) {
/*  858 */     double x = class_3532.method_16436(this.lastTickDelta, entity.field_6038, entity.method_23317());
/*  859 */     double y = class_3532.method_16436(this.lastTickDelta, entity.field_5971, entity.method_23318()) + yOffset;
/*  860 */     double z = class_3532.method_16436(this.lastTickDelta, entity.field_5989, entity.method_23321());
/*  861 */     return projectToScreen(x, y, z, out);
/*      */   }
/*      */   
/*      */   private boolean isInFirstPerson() {
/*  865 */     return (mc != null && mc.field_1773 != null && !mc.field_1773.method_19418().method_19333());
/*      */   }
/*      */   
/*      */   private boolean shouldProcess3DEntity(class_1297 entity) {
/*  869 */     if (entity == null || entity.method_31481() || entity instanceof net.minecraft.class_1531) {
/*  870 */       return false;
/*      */     }
/*  872 */     if (entity instanceof class_1657) { class_1657 player = (class_1657)entity;
/*  873 */       return shouldProcessPlayer(player, false); }
/*      */     
/*  875 */     if (entity instanceof class_1542) { class_1542 itemEntity = (class_1542)entity;
/*  876 */       return (this.targetItems.isState() && itemEntity.method_5805()); }
/*      */     
/*  878 */     if (entity instanceof class_1309) { class_1309 livingEntity = (class_1309)entity; if (livingEntity.method_5805()) {
/*      */ 
/*      */         
/*  881 */         if (isAnimalEntity(entity)) {
/*  882 */           return this.targetAnimals.isState();
/*      */         }
/*  884 */         if (isMobEntity(entity)) {
/*  885 */           return this.targetMobs.isState();
/*      */         }
/*  887 */         return false;
/*      */       }  }
/*      */     
/*  890 */     return false; } private boolean shouldProcess2DPlayer(class_1657 player) { return shouldProcessPlayer(player, true); }
/*      */ 
/*      */   
/*      */   private boolean shouldProcessLiving2D(class_1309 entity) {
/*  894 */     return shouldProcess3DEntity((class_1297)entity);
/*      */   }
/*      */   
/*      */   private boolean shouldProcessItem2D(class_1542 itemEntity) {
/*  898 */     return (this.targetItems.isState() && itemEntity.method_5805());
/*      */   }
/*      */   
/*      */   private boolean shouldProcessPlayer(class_1657 player, boolean skipInvisible) {
/*  902 */     if (!this.targetPlayers.isState()) {
/*  903 */       return false;
/*      */     }
/*  905 */     if (player == null || !player.method_5805()) {
/*  906 */       return false;
/*      */     }
/*  908 */     if (player == mc.field_1724 && isInFirstPerson()) {
/*  909 */       return false;
/*      */     }
/*  911 */     if (skipInvisible && player.method_5767() && !canRenderInvisiblePlayer(player)) {
/*  912 */       return false;
/*      */     }
/*  914 */     return true;
/*      */   }
/*      */   
/*      */   private boolean isTargetEnabled(int index) {
/*  918 */     return (this.targets.getSettings().size() > index && ((BooleanSetting)this.targets.getSettings().get(index)).isState());
/*      */   }
/*      */   
/*      */   private boolean isAnimalEntity(class_1297 entity) {
/*  922 */     return (entity instanceof net.minecraft.class_1429 || entity instanceof net.minecraft.class_9866 || entity instanceof net.minecraft.class_1421);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isMobEntity(class_1297 entity) {
/*  928 */     return (entity instanceof net.minecraft.class_1308 && !isAnimalEntity(entity) && !(entity instanceof class_1657));
/*      */   }
/*      */   
/*      */   private boolean canRenderInvisiblePlayer(class_1657 player) {
/*  932 */     SeeInvisibles seeInvisibles = ModuleClass.seeInvisibles;
/*  933 */     return (seeInvisibles != null && seeInvisibles.shouldRenderInvisible(player));
/*      */   }
/*      */   
/*      */   private boolean isOutsideRenderDistance(class_1297 entity) {
/*  937 */     int viewDistanceChunks = ((Integer)mc.field_1690.method_42503().method_41753()).intValue();
/*  938 */     double maxDistance = Math.max(48.0D, viewDistanceChunks * 16.0D + 16.0D);
/*  939 */     return (entity.method_5707(this.lastCameraPos) > maxDistance * maxDistance);
/*      */   }
/*      */   private static final class ScreenRect extends Record {
/*      */     private final float minX; private final float minY; private final float maxX; private final float maxY;
/*  943 */     private ScreenRect(float minX, float minY, float maxX, float maxY) { this.minX = minX; this.minY = minY; this.maxX = maxX; this.maxY = maxY; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lshame/astra/client/modules/impl/render/EntityESP$ScreenRect;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #943	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lshame/astra/client/modules/impl/render/EntityESP$ScreenRect; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lshame/astra/client/modules/impl/render/EntityESP$ScreenRect;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #943	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lshame/astra/client/modules/impl/render/EntityESP$ScreenRect; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lshame/astra/client/modules/impl/render/EntityESP$ScreenRect;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #943	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lshame/astra/client/modules/impl/render/EntityESP$ScreenRect;
/*  943 */       //   0	8	1	o	Ljava/lang/Object; } public float minX() { return this.minX; } public float minY() { return this.minY; } public float maxX() { return this.maxX; } public float maxY() { return this.maxY; }
/*      */      float centerX() {
/*  945 */       return (this.minX + this.maxX) * 0.5F;
/*      */     }
/*      */     
/*      */     float centerY() {
/*  949 */       return (this.minY + this.maxY) * 0.5F;
/*      */     }
/*      */   }
/*      */   private static final class DonateSegment extends Record { private final String text; private final int color;
/*  953 */     private DonateSegment(String text, int color) { this.text = text; this.color = color; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lshame/astra/client/modules/impl/render/EntityESP$DonateSegment;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #953	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lshame/astra/client/modules/impl/render/EntityESP$DonateSegment; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lshame/astra/client/modules/impl/render/EntityESP$DonateSegment;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #953	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lshame/astra/client/modules/impl/render/EntityESP$DonateSegment; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lshame/astra/client/modules/impl/render/EntityESP$DonateSegment;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #953	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lshame/astra/client/modules/impl/render/EntityESP$DonateSegment;
/*  953 */       //   0	8	1	o	Ljava/lang/Object; } public String text() { return this.text; } public int color() { return this.color; }
/*      */      }
/*      */   private static class DonateCache { private List<EntityESP.DonateSegment> segments; private long nextUpdateAt;
/*      */     private DonateCache() {
/*  957 */       this.segments = Collections.emptyList();
/*      */     } }
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
/*      */   private static class ProjectedPoint
/*      */   {
/*      */     private float x;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private float y;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private float z;
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
/*      */   private void render3DBox(class_4587 matrices, class_1297 entity, float tickDelta) {
/*      */     // Byte code:
/*      */     //   0: getstatic shame/astra/client/modules/impl/render/EntityESP.mc : Lnet/minecraft/class_310;
/*      */     //   3: getfield field_1773 : Lnet/minecraft/class_757;
/*      */     //   6: invokevirtual method_19418 : ()Lnet/minecraft/class_4184;
/*      */     //   9: invokevirtual method_19326 : ()Lnet/minecraft/class_243;
/*      */     //   12: astore #4
/*      */     //   14: fload_3
/*      */     //   15: f2d
/*      */     //   16: aload_2
/*      */     //   17: getfield field_6038 : D
/*      */     //   20: aload_2
/*      */     //   21: invokevirtual method_23317 : ()D
/*      */     //   24: invokestatic method_16436 : (DDD)D
/*      */     //   27: aload #4
/*      */     //   29: getfield field_1352 : D
/*      */     //   32: dsub
/*      */     //   33: dstore #5
/*      */     //   35: fload_3
/*      */     //   36: f2d
/*      */     //   37: aload_2
/*      */     //   38: getfield field_5971 : D
/*      */     //   41: aload_2
/*      */     //   42: invokevirtual method_23318 : ()D
/*      */     //   45: invokestatic method_16436 : (DDD)D
/*      */     //   48: aload #4
/*      */     //   50: getfield field_1351 : D
/*      */     //   53: dsub
/*      */     //   54: dstore #7
/*      */     //   56: fload_3
/*      */     //   57: f2d
/*      */     //   58: aload_2
/*      */     //   59: getfield field_5989 : D
/*      */     //   62: aload_2
/*      */     //   63: invokevirtual method_23321 : ()D
/*      */     //   66: invokestatic method_16436 : (DDD)D
/*      */     //   69: aload #4
/*      */     //   71: getfield field_1350 : D
/*      */     //   74: dsub
/*      */     //   75: dstore #9
/*      */     //   77: aload_2
/*      */     //   78: invokevirtual method_5829 : ()Lnet/minecraft/class_238;
/*      */     //   81: aload_2
/*      */     //   82: invokevirtual method_23317 : ()D
/*      */     //   85: dneg
/*      */     //   86: aload_2
/*      */     //   87: invokevirtual method_23318 : ()D
/*      */     //   90: dneg
/*      */     //   91: aload_2
/*      */     //   92: invokevirtual method_23321 : ()D
/*      */     //   95: dneg
/*      */     //   96: invokevirtual method_989 : (DDD)Lnet/minecraft/class_238;
/*      */     //   99: astore #11
/*      */     //   101: aload_1
/*      */     //   102: invokevirtual method_22903 : ()V
/*      */     //   105: aload_1
/*      */     //   106: dload #5
/*      */     //   108: dload #7
/*      */     //   110: dload #9
/*      */     //   112: invokevirtual method_22904 : (DDD)V
/*      */     //   115: aload_2
/*      */     //   116: instanceof net/minecraft/class_1657
/*      */     //   119: ifeq -> 163
/*      */     //   122: aload_2
/*      */     //   123: checkcast net/minecraft/class_1657
/*      */     //   126: astore #13
/*      */     //   128: getstatic shame/astra/astra.INSTANCE : Lshame/astra/astra;
/*      */     //   131: getfield friendStorage : Lshame/astra/api/storages/implement/FriendStorage;
/*      */     //   134: ifnull -> 163
/*      */     //   137: getstatic shame/astra/astra.INSTANCE : Lshame/astra/astra;
/*      */     //   140: getfield friendStorage : Lshame/astra/api/storages/implement/FriendStorage;
/*      */     //   143: aload #13
/*      */     //   145: invokevirtual method_5477 : ()Lnet/minecraft/class_2561;
/*      */     //   148: invokeinterface getString : ()Ljava/lang/String;
/*      */     //   153: invokevirtual isFriend : (Ljava/lang/String;)Z
/*      */     //   156: ifeq -> 163
/*      */     //   159: iconst_1
/*      */     //   160: goto -> 164
/*      */     //   163: iconst_0
/*      */     //   164: istore #12
/*      */     //   166: iload #12
/*      */     //   168: ifeq -> 189
/*      */     //   171: bipush #84
/*      */     //   173: sipush #255
/*      */     //   176: bipush #84
/*      */     //   178: sipush #255
/*      */     //   181: invokestatic rgba : (IIII)I
/*      */     //   184: istore #13
/*      */     //   186: goto -> 195
/*      */     //   189: aload_0
/*      */     //   190: invokevirtual getStableThemeColor : ()I
/*      */     //   193: istore #13
/*      */     //   195: aload_0
/*      */     //   196: aload_2
/*      */     //   197: iload #13
/*      */     //   199: invokevirtual applyEntityHurtTint : (Lnet/minecraft/class_1297;I)I
/*      */     //   202: istore #13
/*      */     //   204: iload #13
/*      */     //   206: invokestatic redf : (I)F
/*      */     //   209: fstore #14
/*      */     //   211: iload #13
/*      */     //   213: invokestatic greenf : (I)F
/*      */     //   216: fstore #15
/*      */     //   218: iload #13
/*      */     //   220: invokestatic bluef : (I)F
/*      */     //   223: fstore #16
/*      */     //   225: invokestatic enableBlend : ()V
/*      */     //   228: invokestatic defaultBlendFunc : ()V
/*      */     //   231: invokestatic disableCull : ()V
/*      */     //   234: invokestatic enableDepthTest : ()V
/*      */     //   237: iconst_0
/*      */     //   238: invokestatic depthMask : (Z)V
/*      */     //   241: getstatic net/minecraft/class_10142.field_53876 : Lnet/minecraft/class_10156;
/*      */     //   244: invokestatic setShader : (Lnet/minecraft/class_10156;)Lnet/minecraft/class_5944;
/*      */     //   247: pop
/*      */     //   248: ldc 1.5
/*      */     //   250: invokestatic lineWidth : (F)V
/*      */     //   253: aload_1
/*      */     //   254: invokevirtual method_23760 : ()Lnet/minecraft/class_4587$class_4665;
/*      */     //   257: invokevirtual method_23761 : ()Lorg/joml/Matrix4f;
/*      */     //   260: astore #17
/*      */     //   262: invokestatic method_1348 : ()Lnet/minecraft/class_289;
/*      */     //   265: astore #18
/*      */     //   267: aload_0
/*      */     //   268: getfield boxFilled : Lshame/astra/client/modules/settings/implement/BooleanSetting;
/*      */     //   271: invokevirtual isState : ()Z
/*      */     //   274: ifeq -> 295
/*      */     //   277: aload_0
/*      */     //   278: aload #18
/*      */     //   280: aload #17
/*      */     //   282: aload #11
/*      */     //   284: fload #14
/*      */     //   286: fload #15
/*      */     //   288: fload #16
/*      */     //   290: ldc 0.23
/*      */     //   292: invokevirtual drawFilledBox : (Lnet/minecraft/class_289;Lorg/joml/Matrix4f;Lnet/minecraft/class_238;FFFF)V
/*      */     //   295: aload_0
/*      */     //   296: aload #18
/*      */     //   298: aload #17
/*      */     //   300: aload #11
/*      */     //   302: fload #14
/*      */     //   304: fload #15
/*      */     //   306: fload #16
/*      */     //   308: fconst_1
/*      */     //   309: invokevirtual drawBoxOutline : (Lnet/minecraft/class_289;Lorg/joml/Matrix4f;Lnet/minecraft/class_238;FFFF)V
/*      */     //   312: invokestatic enableCull : ()V
/*      */     //   315: invokestatic enableDepthTest : ()V
/*      */     //   318: iconst_1
/*      */     //   319: invokestatic depthMask : (Z)V
/*      */     //   322: invokestatic disableBlend : ()V
/*      */     //   325: aload_1
/*      */     //   326: invokevirtual method_22909 : ()V
/*      */     //   329: return
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #968	-> 0
/*      */     //   #969	-> 14
/*      */     //   #970	-> 35
/*      */     //   #971	-> 56
/*      */     //   #973	-> 77
/*      */     //   #975	-> 101
/*      */     //   #976	-> 105
/*      */     //   #980	-> 115
/*      */     //   #978	-> 122
/*      */     //   #980	-> 145
/*      */     //   #982	-> 166
/*      */     //   #983	-> 171
/*      */     //   #985	-> 189
/*      */     //   #987	-> 195
/*      */     //   #988	-> 204
/*      */     //   #989	-> 211
/*      */     //   #990	-> 218
/*      */     //   #992	-> 225
/*      */     //   #993	-> 228
/*      */     //   #994	-> 231
/*      */     //   #995	-> 234
/*      */     //   #996	-> 237
/*      */     //   #997	-> 241
/*      */     //   #998	-> 248
/*      */     //   #1000	-> 253
/*      */     //   #1001	-> 262
/*      */     //   #1003	-> 267
/*      */     //   #1004	-> 277
/*      */     //   #1007	-> 295
/*      */     //   #1009	-> 312
/*      */     //   #1010	-> 315
/*      */     //   #1011	-> 318
/*      */     //   #1012	-> 322
/*      */     //   #1013	-> 325
/*      */     //   #1014	-> 329
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   128	35	13	player	Lnet/minecraft/class_1657;
/*      */     //   186	3	13	boxColor	I
/*      */     //   0	330	0	this	Lshame/astra/client/modules/impl/render/EntityESP;
/*      */     //   0	330	1	matrices	Lnet/minecraft/class_4587;
/*      */     //   0	330	2	entity	Lnet/minecraft/class_1297;
/*      */     //   0	330	3	tickDelta	F
/*      */     //   14	316	4	camera	Lnet/minecraft/class_243;
/*      */     //   35	295	5	x	D
/*      */     //   56	274	7	y	D
/*      */     //   77	253	9	z	D
/*      */     //   101	229	11	box	Lnet/minecraft/class_238;
/*      */     //   166	164	12	isFriend	Z
/*      */     //   195	135	13	boxColor	I
/*      */     //   211	119	14	r	F
/*      */     //   218	112	15	g	F
/*      */     //   225	105	16	b	F
/*      */     //   262	68	17	matrix	Lorg/joml/Matrix4f;
/*      */     //   267	63	18	tessellator	Lnet/minecraft/class_289;
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
/*      */   private int applyEntityHurtTint(class_1297 entity, int baseColor) {
/* 1017 */     if (entity instanceof class_1309) { class_1309 livingEntity = (class_1309)entity; if (this.hurtTint.isState()) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1022 */         float target = class_3532.method_15363(livingEntity.field_6235 / 10.0F, 0.0F, 1.0F);
/* 1023 */         float current = ((Float)this.entityHurtTintProgress.getOrDefault(Integer.valueOf(entity.method_5628()), Float.valueOf(0.0F))).floatValue();
/* 1024 */         float speed = (target > current) ? 0.38F : 0.16F;
/* 1025 */         current += (target - current) * speed;
/*      */         
/* 1027 */         if (current <= 0.003F && target <= 0.0F) {
/* 1028 */           this.entityHurtTintProgress.remove(Integer.valueOf(entity.method_5628()));
/* 1029 */           return baseColor;
/*      */         } 
/*      */         
/* 1032 */         this.entityHurtTintProgress.put(Integer.valueOf(entity.method_5628()), Float.valueOf(current));
/* 1033 */         int hitColor = ColorUtils.rgba(255, 70, 70, 255);
/* 1034 */         return ColorUtils.interpolateColor(baseColor, hitColor, current);
/*      */       }  }
/*      */     
/*      */     this.entityHurtTintProgress.remove(Integer.valueOf(entity.method_5628()));
/* 1038 */     return baseColor; } private void drawPlayerMaskBox(class_4587 matrices, class_1297 entity, float tickDelta) { class_243 camera = mc.field_1773.method_19418().method_19326();
/* 1039 */     double x = class_3532.method_16436(tickDelta, entity.field_6038, entity.method_23317()) - camera.field_1352;
/* 1040 */     double y = class_3532.method_16436(tickDelta, entity.field_5971, entity.method_23318()) - camera.field_1351;
/* 1041 */     double z = class_3532.method_16436(tickDelta, entity.field_5989, entity.method_23321()) - camera.field_1350;
/* 1042 */     class_238 box = entity.method_5829().method_989(-entity.method_23317(), -entity.method_23318(), -entity.method_23321());
/*      */     
/* 1044 */     matrices.method_22903();
/* 1045 */     matrices.method_22904(x, y, z);
/* 1046 */     drawMaskBox(class_289.method_1348(), matrices.method_23760().method_23761(), box);
/* 1047 */     matrices.method_22909(); }
/*      */ 
/*      */   
/*      */   private void drawMaskBox(class_289 tessellator, Matrix4f matrix, class_238 box) {
/* 1051 */     class_287 b = tessellator.method_60827(class_293.class_5596.field_27382, class_290.field_1576);
/* 1052 */     float minX = (float)box.field_1323;
/* 1053 */     float minY = (float)box.field_1322;
/* 1054 */     float minZ = (float)box.field_1321;
/* 1055 */     float maxX = (float)box.field_1320;
/* 1056 */     float maxY = (float)box.field_1325;
/* 1057 */     float maxZ = (float)box.field_1324;
/* 1058 */     int white = -1;
/*      */     
/* 1060 */     b.method_22918(matrix, minX, minY, minZ).method_39415(white);
/* 1061 */     b.method_22918(matrix, maxX, minY, minZ).method_39415(white);
/* 1062 */     b.method_22918(matrix, maxX, minY, maxZ).method_39415(white);
/* 1063 */     b.method_22918(matrix, minX, minY, maxZ).method_39415(white);
/*      */     
/* 1065 */     b.method_22918(matrix, minX, maxY, minZ).method_39415(white);
/* 1066 */     b.method_22918(matrix, minX, maxY, maxZ).method_39415(white);
/* 1067 */     b.method_22918(matrix, maxX, maxY, maxZ).method_39415(white);
/* 1068 */     b.method_22918(matrix, maxX, maxY, minZ).method_39415(white);
/*      */     
/* 1070 */     b.method_22918(matrix, minX, minY, minZ).method_39415(white);
/* 1071 */     b.method_22918(matrix, minX, maxY, minZ).method_39415(white);
/* 1072 */     b.method_22918(matrix, maxX, maxY, minZ).method_39415(white);
/* 1073 */     b.method_22918(matrix, maxX, minY, minZ).method_39415(white);
/*      */     
/* 1075 */     b.method_22918(matrix, minX, minY, maxZ).method_39415(white);
/* 1076 */     b.method_22918(matrix, maxX, minY, maxZ).method_39415(white);
/* 1077 */     b.method_22918(matrix, maxX, maxY, maxZ).method_39415(white);
/* 1078 */     b.method_22918(matrix, minX, maxY, maxZ).method_39415(white);
/*      */     
/* 1080 */     b.method_22918(matrix, minX, minY, minZ).method_39415(white);
/* 1081 */     b.method_22918(matrix, minX, minY, maxZ).method_39415(white);
/* 1082 */     b.method_22918(matrix, minX, maxY, maxZ).method_39415(white);
/* 1083 */     b.method_22918(matrix, minX, maxY, minZ).method_39415(white);
/*      */     
/* 1085 */     b.method_22918(matrix, maxX, minY, minZ).method_39415(white);
/* 1086 */     b.method_22918(matrix, maxX, maxY, minZ).method_39415(white);
/* 1087 */     b.method_22918(matrix, maxX, maxY, maxZ).method_39415(white);
/* 1088 */     b.method_22918(matrix, maxX, minY, maxZ).method_39415(white);
/*      */     
/* 1090 */     class_286.method_43433(b.method_60800());
/*      */   }
/*      */   
/*      */   private void renderShaderBoxes() {
/* 1094 */     if (!this.hasShaderMask || this.maskBuffer == null)
/* 1095 */       return;  boolean lineMode = isThreadMode();
/*      */     
/* 1097 */     class_5944 shader = mc.method_62887().method_62947(ShaderUtils.blockOverlay);
/* 1098 */     if (shader == null)
/*      */       return; 
/* 1100 */     int color1 = getStableThemeColor();
/* 1101 */     int color2 = isRainbowTheme() ? ColorUtils.getThemeColor(180) : color1;
/*      */     
/* 1103 */     mc.method_1522().method_1235(false);
/* 1104 */     RenderSystem.enableBlend();
/* 1105 */     RenderSystem.defaultBlendFunc();
/* 1106 */     RenderSystem.disableDepthTest();
/* 1107 */     RenderSystem.setShader(ShaderUtils.blockOverlay);
/* 1108 */     RenderSystem.setShaderTexture(0, this.maskBuffer.method_30277());
/* 1109 */     setUniform(shader, "texelSize", 1.0F / Math.max(1, mc.method_22683().method_4489()), 1.0F / Math.max(1, mc.method_22683().method_4506()));
/* 1110 */     setUniform(shader, "color", ColorUtils.redf(color1), ColorUtils.greenf(color1), ColorUtils.bluef(color1));
/* 1111 */     setUniform(shader, "color2", ColorUtils.redf(color2), ColorUtils.greenf(color2), ColorUtils.bluef(color2));
/* 1112 */     setUniform(shader, "time", (float)(System.currentTimeMillis() % 100000L) / 1000.0F);
/* 1113 */     setUniform(shader, "speed", this.waveSpeed.get());
/* 1114 */     setUniform(shader, "scale", this.waveScale.get());
/* 1115 */     setUniform(shader, "outline", this.outline.get());
/* 1116 */     setUniform(shader, "glow", lineMode ? 0.0F : this.glow.get());
/* 1117 */     setUniform(shader, "fill", lineMode ? 0.0F : this.fill.get());
/* 1118 */     setUniform(shader, "alpha", lineMode ? 1.0F : this.alpha.get());
/* 1119 */     setUniform(shader, "outlineOnly", lineMode ? 1.0F : 0.0F);
/* 1120 */     drawFullscreenQuad();
/*      */     
/* 1122 */     if (this.glow.get() > 0.001F) {
/* 1123 */       int blurredMask = runKawaseBloom(Math.max(3, Math.min(8, 4 + Math.round(this.outline.get() * 0.7F))));
/* 1124 */       class_5944 glowShader = mc.method_62887().method_62947(ShaderUtils.shaderHandsGlow);
/* 1125 */       if (glowShader != null) {
/* 1126 */         RenderSystem.blendFuncSeparate(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE, GlStateManager.class_4535.ZERO, GlStateManager.class_4534.ONE);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1132 */         RenderSystem.setShader(ShaderUtils.shaderHandsGlow);
/* 1133 */         RenderSystem.setShaderTexture(0, blurredMask);
/* 1134 */         RenderSystem.setShaderTexture(1, this.maskBuffer.method_30277());
/* 1135 */         setUniform(glowShader, "color", ColorUtils.redf(color1), ColorUtils.greenf(color1), ColorUtils.bluef(color1));
/* 1136 */         setUniform(glowShader, "color2", ColorUtils.redf(color2), ColorUtils.greenf(color2), ColorUtils.bluef(color2));
/* 1137 */         setUniform(glowShader, "exposure", 1.0F + this.glow.get() * 1.8F);
/* 1138 */         drawFullscreenQuad();
/*      */       } 
/*      */     } 
/*      */     
/* 1142 */     RenderSystem.enableDepthTest();
/* 1143 */     RenderSystem.disableBlend();
/* 1144 */     RenderSystem.defaultBlendFunc();
/* 1145 */     RenderSystem.setShaderTexture(0, 0);
/* 1146 */     RenderSystem.setShaderTexture(1, 0);
/* 1147 */     mc.method_1522().method_1235(true);
/*      */   }
/*      */   
/*      */   private void renderShaderBoxesWorldPass() {
/* 1151 */     if (!isPostBoxMode())
/*      */       return; 
/* 1153 */     Matrix4f savedProjection = new Matrix4f((Matrix4fc)RenderSystem.getProjectionMatrix());
/* 1154 */     float width = Math.max(mc.method_22683().method_4486(), 1);
/* 1155 */     float height = Math.max(mc.method_22683().method_4502(), 1);
/* 1156 */     Matrix4f ortho = (new Matrix4f()).setOrtho(0.0F, width, height, 0.0F, -1000.0F, 1000.0F);
/* 1157 */     RenderSystem.setProjectionMatrix(ortho, class_10366.field_54954);
/*      */     
/*      */     try {
/* 1160 */       renderShaderBoxes();
/*      */     } finally {
/* 1162 */       RenderSystem.setProjectionMatrix(savedProjection, class_10366.field_54954);
/*      */     } 
/*      */   }
/*      */   
/*      */   private int runKawaseBloom(int iterations) {
/* 1167 */     ensureBloomBuffers(iterations);
/* 1168 */     if (this.bloomBuffers.isEmpty()) return this.maskBuffer.method_30277();
/*      */     
/* 1170 */     int currentTexture = this.maskBuffer.method_30277();
/* 1171 */     class_5944 downShader = mc.method_62887().method_62947(ShaderUtils.shaderHandsKawaseDown);
/* 1172 */     class_5944 upShader = mc.method_62887().method_62947(ShaderUtils.shaderHandsKawaseUp);
/* 1173 */     if (downShader == null || upShader == null) return currentTexture; 
/*      */     int i;
/* 1175 */     for (i = 0; i < iterations; i++) {
/* 1176 */       class_276 dst = this.bloomBuffers.get(i);
/* 1177 */       dst.method_1236(0.0F, 0.0F, 0.0F, 0.0F);
/* 1178 */       dst.method_1230();
/* 1179 */       dst.method_1235(true);
/*      */       
/* 1181 */       RenderSystem.setShader(ShaderUtils.shaderHandsKawaseDown);
/* 1182 */       RenderSystem.setShaderTexture(0, currentTexture);
/* 1183 */       setHandsKawaseUniforms(downShader, dst.field_1482, dst.field_1481, 1.0F + i);
/* 1184 */       drawFullscreenQuad();
/*      */       
/* 1186 */       currentTexture = dst.method_30277();
/*      */     } 
/*      */     
/* 1189 */     for (i = iterations - 1; i >= 1; i--) {
/* 1190 */       class_276 dst = this.bloomBuffers.get(i - 1);
/* 1191 */       dst.method_1236(0.0F, 0.0F, 0.0F, 0.0F);
/* 1192 */       dst.method_1230();
/* 1193 */       dst.method_1235(true);
/*      */       
/* 1195 */       RenderSystem.setShader(ShaderUtils.shaderHandsKawaseUp);
/* 1196 */       RenderSystem.setShaderTexture(0, currentTexture);
/* 1197 */       setHandsKawaseUniforms(upShader, dst.field_1482, dst.field_1481, 1.0F + i);
/* 1198 */       setUniform(upShader, "color", 1.0F, 1.0F, 1.0F);
/* 1199 */       drawFullscreenQuad();
/*      */       
/* 1201 */       currentTexture = dst.method_30277();
/*      */     } 
/*      */     
/* 1204 */     mc.method_1522().method_1235(true);
/* 1205 */     return currentTexture;
/*      */   }
/*      */   
/*      */   private void ensureMaskBuffer() {
/* 1209 */     int w = mc.method_22683().method_4489();
/* 1210 */     int h = mc.method_22683().method_4506();
/* 1211 */     if (this.maskBuffer == null || this.maskWidth != w || this.maskHeight != h) {
/* 1212 */       if (this.maskBuffer != null) {
/* 1213 */         this.maskBuffer.method_1238();
/*      */       }
/* 1215 */       this.maskBuffer = (class_276)new class_6367(w, h, true);
/* 1216 */       this.maskWidth = w;
/* 1217 */       this.maskHeight = h;
/* 1218 */       for (class_276 fb : this.bloomBuffers) {
/* 1219 */         fb.method_1238();
/*      */       }
/* 1221 */       this.bloomBuffers.clear();
/*      */     } 
/*      */   }
/*      */   
/*      */   private void ensureBloomBuffers(int iterations) {
/* 1226 */     while (this.bloomBuffers.size() > iterations) {
/* 1227 */       int last = this.bloomBuffers.size() - 1;
/* 1228 */       ((class_276)this.bloomBuffers.get(last)).method_1238();
/* 1229 */       this.bloomBuffers.remove(last);
/*      */     } 
/*      */     
/* 1232 */     for (int i = 0; i < iterations; i++) {
/* 1233 */       int w = Math.max(2, this.maskWidth >> i + 1);
/* 1234 */       int h = Math.max(2, this.maskHeight >> i + 1);
/* 1235 */       if (i >= this.bloomBuffers.size()) {
/* 1236 */         this.bloomBuffers.add(new class_6367(w, h, false));
/*      */       } else {
/*      */         
/* 1239 */         class_276 fb = this.bloomBuffers.get(i);
/* 1240 */         if (fb.field_1482 != w || fb.field_1481 != h) {
/* 1241 */           fb.method_1238();
/* 1242 */           this.bloomBuffers.set(i, new class_6367(w, h, false));
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   private void copyMainDepthToMask() {
/* 1248 */     if (this.maskBuffer == null)
/* 1249 */       return;  int readFbo = GL11.glGetInteger(36010);
/* 1250 */     int drawFbo = GL11.glGetInteger(36006);
/* 1251 */     int w = mc.method_22683().method_4489();
/* 1252 */     int h = mc.method_22683().method_4506();
/*      */     
/* 1254 */     GL30.glBindFramebuffer(36008, (mc.method_1522()).field_1476);
/* 1255 */     GL30.glBindFramebuffer(36009, this.maskBuffer.field_1476);
/* 1256 */     GL30.glBlitFramebuffer(0, 0, w, h, 0, 0, w, h, 256, 9728);
/* 1257 */     GL30.glBindFramebuffer(36008, readFbo);
/* 1258 */     GL30.glBindFramebuffer(36009, drawFbo);
/*      */   }
/*      */   
/*      */   private void setUniform(class_5944 shader, String name, float value) {
/* 1262 */     class_284 uniform = shader.method_34582(name);
/* 1263 */     if (uniform != null) uniform.method_1251(value); 
/*      */   }
/*      */   
/*      */   private void setUniform(class_5944 shader, String name, float x, float y) {
/* 1267 */     class_284 uniform = shader.method_34582(name);
/* 1268 */     if (uniform != null) uniform.method_1255(x, y); 
/*      */   }
/*      */   
/*      */   private void setUniform(class_5944 shader, String name, float x, float y, float z) {
/* 1272 */     class_284 uniform = shader.method_34582(name);
/* 1273 */     if (uniform != null) uniform.method_1249(x, y, z); 
/*      */   }
/*      */   
/*      */   private void setHandsKawaseUniforms(class_5944 shader, int texWidth, int texHeight, float offset) {
/* 1277 */     setUniform(shader, "uSize", Math.max(1, texWidth), Math.max(1, texHeight));
/* 1278 */     setUniform(shader, "uOffset", offset, offset);
/* 1279 */     setUniform(shader, "uHalfPixel", 0.5F / Math.max(1, texWidth), 0.5F / Math.max(1, texHeight));
/*      */   }
/*      */   
/*      */   private void drawFullscreenQuad() {
/* 1283 */     float width = Math.max(mc.method_22683().method_4486(), 1);
/* 1284 */     float height = Math.max(mc.method_22683().method_4502(), 1);
/* 1285 */     class_287 b = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
/* 1286 */     b.method_22912(0.0F, 0.0F, 0.0F).method_22913(0.0F, 1.0F).method_22915(1.0F, 1.0F, 1.0F, 1.0F);
/* 1287 */     b.method_22912(0.0F, height, 0.0F).method_22913(0.0F, 0.0F).method_22915(1.0F, 1.0F, 1.0F, 1.0F);
/* 1288 */     b.method_22912(width, height, 0.0F).method_22913(1.0F, 0.0F).method_22915(1.0F, 1.0F, 1.0F, 1.0F);
/* 1289 */     b.method_22912(width, 0.0F, 0.0F).method_22913(1.0F, 1.0F).method_22915(1.0F, 1.0F, 1.0F, 1.0F);
/* 1290 */     class_286.method_43433(b.method_60800());
/*      */   }
/*      */   
/*      */   private boolean isPostBoxMode() {
/* 1294 */     return false;
/*      */   }
/*      */   
/*      */   private boolean isThreadMode() {
/* 1298 */     return false;
/*      */   }
/*      */   
/*      */   private boolean isRainbowTheme() {
/* 1302 */     if (astra.INSTANCE == null || astra.INSTANCE.themeStorage == null || astra.INSTANCE.themeStorage.getThemes() == null) {
/* 1303 */       return false;
/*      */     }
/* 1305 */     Theme theme = astra.INSTANCE.themeStorage.getThemes().getTheme();
/* 1306 */     return (theme != null && "Rainbow".equals(theme.getName()));
/*      */   }
/*      */   
/*      */   private int getStableThemeColor() {
/* 1310 */     if (astra.INSTANCE == null || astra.INSTANCE.themeStorage == null || astra.INSTANCE.themeStorage.getThemes() == null) {
/* 1311 */       return ColorUtils.getThemeColor(0);
/*      */     }
/* 1313 */     Theme theme = astra.INSTANCE.themeStorage.getThemes().getTheme();
/* 1314 */     if (theme == null || theme.color == null || theme.color.length == 0) {
/* 1315 */       return ColorUtils.getThemeColor(0);
/*      */     }
/* 1317 */     return theme.color[0];
/*      */   }
/*      */   
/*      */   private void renderThreadWeb(class_4587 matrices, class_1297 entity, float tickDelta) {
/* 1321 */     class_243 camera = mc.field_1773.method_19418().method_19326();
/* 1322 */     double x = class_3532.method_16436(tickDelta, entity.field_6038, entity.method_23317()) - camera.field_1352;
/* 1323 */     double y = class_3532.method_16436(tickDelta, entity.field_5971, entity.method_23318()) - camera.field_1351;
/* 1324 */     double z = class_3532.method_16436(tickDelta, entity.field_5989, entity.method_23321()) - camera.field_1350;
/* 1325 */     class_238 box = entity.method_5829().method_989(-entity.method_23317(), -entity.method_23318(), -entity.method_23321());
/*      */     
/* 1327 */     matrices.method_22903();
/* 1328 */     matrices.method_22904(x, y, z);
/* 1329 */     drawAnimatedWeb(matrices.method_23760().method_23761(), box, entity.method_5628());
/* 1330 */     matrices.method_22909();
/*      */   }
/*      */   
/*      */   private void drawAnimatedWeb(Matrix4f matrix, class_238 box, long seedBase) {
/* 1334 */     int strandsPerFace = 5;
/* 1335 */     int samples = 18;
/* 1336 */     float t = (float)(System.currentTimeMillis() % 100000L) / 1000.0F * this.lineSpeed.get();
/* 1337 */     float lineWidth = 0.0025F;
/* 1338 */     float bendBase = 0.06F + this.lineJitter.get() * 0.2F;
/* 1339 */     int baseAlpha = Math.max(20, Math.min(255, (int)(this.alpha.get() * 210.0F)));
/* 1340 */     int themeColor = getStableThemeColor();
/*      */     
/* 1342 */     RenderSystem.enableBlend();
/* 1343 */     RenderSystem.defaultBlendFunc();
/* 1344 */     RenderSystem.disableCull();
/* 1345 */     RenderSystem.enableDepthTest();
/* 1346 */     RenderSystem.depthMask(false);
/* 1347 */     RenderSystem.setShader(class_10142.field_53876);
/* 1348 */     drawFilledBoxInt(matrix, box, ColorUtils.setAlphaColor(themeColor, (int)(this.alpha.get() * this.fill.get() * 170.0F)));
/*      */     
/* 1350 */     for (int face = 0; face < 6; face++) {
/* 1351 */       int[] neighbors = faceNeighbors(face);
/* 1352 */       for (int strand = 0; strand < strandsPerFace; strand++) {
/* 1353 */         int key = face * 1000 + strand * 53;
/* 1354 */         int adj = neighbors[strand % neighbors.length];
/* 1355 */         double phase = t * (0.95D + rand01(seedBase, key + 1) * 0.55D) + strand * 0.83D + face * 1.11D;
/* 1356 */         double edgeT = clamp01(0.5D + Math.sin(phase * 1.37D + rand01(seedBase, key + 2) * 6.2831853D) * 0.38D);
/*      */         
/* 1358 */         class_243 pivot = edgePoint(box, face, adj, edgeT, 0.0015D);
/* 1359 */         class_243 start = facePoint(box, face, 
/* 1360 */             clamp01(0.5D + (rand01(seedBase, key + 3) - 0.5D) * 0.46D), 
/* 1361 */             clamp01(0.5D + (rand01(seedBase, key + 4) - 0.5D) * 0.46D), 0.0015D);
/*      */         
/* 1363 */         class_243 end = facePoint(box, adj, 
/* 1364 */             clamp01(0.5D + (rand01(seedBase, key + 5) - 0.5D) * 0.46D), 
/* 1365 */             clamp01(0.5D + (rand01(seedBase, key + 6) - 0.5D) * 0.46D), 0.0015D);
/*      */ 
/*      */         
/* 1368 */         class_243[] basisA = faceBasis(face);
/* 1369 */         class_243[] basisB = faceBasis(adj);
/* 1370 */         class_243 normalA = faceNormal(face);
/* 1371 */         class_243 normalB = faceNormal(adj);
/*      */         
/* 1373 */         double bendA = bendBase * (0.7D + rand01(seedBase, key + 7)) * Math.sin(phase * 1.9D + rand01(seedBase, key + 8) * 6.2831853D);
/*      */         
/* 1375 */         double bendB = bendBase * (0.7D + rand01(seedBase, key + 9)) * Math.cos(phase * 1.7D + rand01(seedBase, key + 10) * 6.2831853D);
/*      */         
/* 1377 */         class_243 dirA = pivot.method_1020(start);
/* 1378 */         class_243 c1a = start.method_1019(dirA.method_1021(0.38D)).method_1019(basisA[0].method_1021(bendA)).method_1019(basisA[1].method_1021(-bendA * 0.55D));
/* 1379 */         class_243 c2a = start.method_1019(dirA.method_1021(0.76D)).method_1019(basisA[0].method_1021(-bendA * 0.65D)).method_1019(basisA[1].method_1021(bendA * 0.4D));
/*      */         
/* 1381 */         class_243 dirB = end.method_1020(pivot);
/* 1382 */         class_243 c1b = pivot.method_1019(dirB.method_1021(0.24D)).method_1019(basisB[0].method_1021(bendB)).method_1019(basisB[1].method_1021(bendB * 0.45D));
/* 1383 */         class_243 c2b = pivot.method_1019(dirB.method_1021(0.62D)).method_1019(basisB[0].method_1021(-bendB * 0.7D)).method_1019(basisB[1].method_1021(-bendB * 0.35D));
/*      */         
/* 1385 */         int alphaLine = Math.max(18, Math.min(255, (int)(baseAlpha * (0.74D + 0.26D * Math.sin(phase * 2.6D)))));
/* 1386 */         int color = ColorUtils.setAlphaColor(themeColor, alphaLine);
/* 1387 */         drawBezierRibbon(matrix, start, c1a, c2a, pivot, normalA, samples, color, lineWidth);
/* 1388 */         drawBezierRibbon(matrix, pivot, c1b, c2b, end, normalB, samples, color, lineWidth);
/*      */       } 
/*      */     } 
/*      */     
/* 1392 */     RenderSystem.depthMask(true);
/* 1393 */     RenderSystem.enableCull();
/* 1394 */     RenderSystem.disableBlend();
/*      */   }
/*      */   
/*      */   private class_243 cubicBezier(class_243 p0, class_243 p1, class_243 p2, class_243 p3, float t) {
/* 1398 */     double it = 1.0D - t;
/* 1399 */     double it2 = it * it;
/* 1400 */     double t2 = (t * t);
/* 1401 */     return p0.method_1021(it2 * it)
/* 1402 */       .method_1019(p1.method_1021(3.0D * it2 * t))
/* 1403 */       .method_1019(p2.method_1021(3.0D * it * t2))
/* 1404 */       .method_1019(p3.method_1021(t2 * t));
/*      */   }
/*      */   
/*      */   private void drawBezierRibbon(Matrix4f matrix, class_243 p0, class_243 p1, class_243 p2, class_243 p3, class_243 faceNormal, int samples, int color, float halfWidth) {
/* 1408 */     class_243[] points = new class_243[samples + 1];
/* 1409 */     for (int s = 0; s <= samples; s++) {
/* 1410 */       float u = s / samples;
/* 1411 */       points[s] = cubicBezier(p0, p1, p2, p3, u);
/*      */     } 
/*      */     
/* 1414 */     class_287 quads = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1576);
/* 1415 */     for (int i = 0; i < samples; i++) {
/* 1416 */       class_243 a = points[i];
/* 1417 */       class_243 b = points[i + 1];
/* 1418 */       class_243 dir = b.method_1020(a);
/* 1419 */       if (dir.method_1027() >= 1.0E-6D) {
/*      */         
/* 1421 */         class_243 perp = faceNormal.method_1036(dir).method_1029().method_1021(halfWidth);
/* 1422 */         class_243 aL = a.method_1019(perp);
/* 1423 */         class_243 aR = a.method_1020(perp);
/* 1424 */         class_243 bL = b.method_1019(perp);
/* 1425 */         class_243 bR = b.method_1020(perp);
/*      */         
/* 1427 */         quads.method_22918(matrix, (float)aL.field_1352, (float)aL.field_1351, (float)aL.field_1350).method_39415(color);
/* 1428 */         quads.method_22918(matrix, (float)aR.field_1352, (float)aR.field_1351, (float)aR.field_1350).method_39415(color);
/* 1429 */         quads.method_22918(matrix, (float)bR.field_1352, (float)bR.field_1351, (float)bR.field_1350).method_39415(color);
/* 1430 */         quads.method_22918(matrix, (float)bL.field_1352, (float)bL.field_1351, (float)bL.field_1350).method_39415(color);
/*      */       } 
/* 1432 */     }  class_286.method_43433(quads.method_60800());
/*      */   }
/*      */   
/*      */   private int[] faceNeighbors(int face) {
/* 1436 */     switch (face) { case 0: case 1:
/* 1437 */         (new int[4])[0] = 2; (new int[4])[1] = 3; (new int[4])[2] = 4; (new int[4])[3] = 5;
/* 1438 */       case 2: case 3: (new int[4])[0] = 0; (new int[4])[1] = 1; (new int[4])[2] = 4; (new int[4])[3] = 5; }
/* 1439 */      return new int[] { 0, 1, 2, 3 };
/*      */   }
/*      */ 
/*      */   
/*      */   private class_243[] faceBasis(int face) {
/* 1444 */     switch (face) { case 0: case 1:
/* 1445 */         (new class_243[2])[0] = new class_243(1.0D, 0.0D, 0.0D); (new class_243[2])[1] = new class_243(0.0D, 0.0D, 1.0D);
/* 1446 */       case 2: case 3: (new class_243[2])[0] = new class_243(1.0D, 0.0D, 0.0D); (new class_243[2])[1] = new class_243(0.0D, 1.0D, 0.0D); }
/* 1447 */      return new class_243[] { new class_243(0.0D, 0.0D, 1.0D), new class_243(0.0D, 1.0D, 0.0D) };
/*      */   }
/*      */ 
/*      */   
/*      */   private class_243 faceNormal(int face) {
/* 1452 */     switch (face) { case 0: case 1: case 2: case 3: case 4:  }  return 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1458 */       new class_243(1.0D, 0.0D, 0.0D);
/*      */   }
/*      */ 
/*      */   
/*      */   private class_243 edgePoint(class_238 box, int faceA, int faceB, double t, double inset) {
/* 1463 */     double x = Double.NaN;
/* 1464 */     double y = Double.NaN;
/* 1465 */     double z = Double.NaN;
/*      */     
/* 1467 */     double[] fixedA = faceFixedCoords(box, faceA, inset);
/* 1468 */     if (!Double.isNaN(fixedA[0])) x = fixedA[0]; 
/* 1469 */     if (!Double.isNaN(fixedA[1])) y = fixedA[1]; 
/* 1470 */     if (!Double.isNaN(fixedA[2])) z = fixedA[2];
/*      */     
/* 1472 */     double[] fixedB = faceFixedCoords(box, faceB, inset);
/* 1473 */     if (!Double.isNaN(fixedB[0])) x = fixedB[0]; 
/* 1474 */     if (!Double.isNaN(fixedB[1])) y = fixedB[1]; 
/* 1475 */     if (!Double.isNaN(fixedB[2])) z = fixedB[2];
/*      */     
/* 1477 */     double tt = clamp01(t);
/* 1478 */     if (Double.isNaN(x)) x = lerp(box.field_1323, box.field_1320, tt); 
/* 1479 */     if (Double.isNaN(y)) y = lerp(box.field_1322, box.field_1325, tt); 
/* 1480 */     if (Double.isNaN(z)) z = lerp(box.field_1321, box.field_1324, tt); 
/* 1481 */     return new class_243(x, y, z);
/*      */   }
/*      */   
/*      */   private double[] faceFixedCoords(class_238 box, int face, double inset) {
/* 1485 */     switch (face) { case 0:
/* 1486 */         (new double[3])[0] = Double.NaN; (new double[3])[1] = box.field_1325 - inset; (new double[3])[2] = Double.NaN;
/* 1487 */       case 1: (new double[3])[0] = Double.NaN; (new double[3])[1] = box.field_1322 + inset; (new double[3])[2] = Double.NaN;
/* 1488 */       case 2: (new double[3])[0] = Double.NaN; (new double[3])[1] = Double.NaN; (new double[3])[2] = box.field_1321 + inset;
/* 1489 */       case 3: (new double[3])[0] = Double.NaN; (new double[3])[1] = Double.NaN; (new double[3])[2] = box.field_1324 - inset;
/* 1490 */       case 4: (new double[3])[0] = box.field_1323 + inset; (new double[3])[1] = Double.NaN; (new double[3])[2] = Double.NaN; }
/* 1491 */      return new double[] { box.field_1320 - inset, Double.NaN, Double.NaN };
/*      */   }
/*      */ 
/*      */   
/*      */   private class_243 facePoint(class_238 box, int face, double u, double v, double inset) {
/* 1496 */     u = clamp01(u);
/* 1497 */     v = clamp01(v);
/* 1498 */     switch (face) { case 0: case 1: case 2: case 3: case 4:  }  return 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1504 */       new class_243(box.field_1320 - inset, lerp(box.field_1322, box.field_1325, v), lerp(box.field_1321, box.field_1324, u));
/*      */   }
/*      */ 
/*      */   
/*      */   private double rand01(long seed, int salt) {
/* 1509 */     long x = seed + -7046029254386353131L * (salt + 1L);
/* 1510 */     x ^= x >>> 30L;
/* 1511 */     x *= -4658895280553007687L;
/* 1512 */     x ^= x >>> 27L;
/* 1513 */     x *= -7723592293110705685L;
/* 1514 */     x ^= x >>> 31L;
/* 1515 */     return (x & 0xFFFFFFL) / 1.6777216E7D;
/*      */   }
/*      */   
/*      */   private double lerp(double a, double b, double t) {
/* 1519 */     return a + (b - a) * t;
/*      */   }
/*      */   
/*      */   private double clamp01(double v) {
/* 1523 */     return Math.max(0.0D, Math.min(1.0D, v));
/*      */   }
/*      */   
/*      */   private void drawFilledBoxInt(Matrix4f matrix, class_238 box, int color) {
/* 1527 */     class_287 b = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1576);
/*      */     
/* 1529 */     b.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_39415(color);
/* 1530 */     b.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_39415(color);
/* 1531 */     b.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_39415(color);
/* 1532 */     b.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_39415(color);
/*      */     
/* 1534 */     b.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1321).method_39415(color);
/* 1535 */     b.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1321).method_39415(color);
/* 1536 */     b.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324).method_39415(color);
/* 1537 */     b.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1324).method_39415(color);
/*      */     
/* 1539 */     b.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_39415(color);
/* 1540 */     b.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_39415(color);
/* 1541 */     b.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1321).method_39415(color);
/* 1542 */     b.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1321).method_39415(color);
/*      */     
/* 1544 */     b.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_39415(color);
/* 1545 */     b.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1324).method_39415(color);
/* 1546 */     b.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324).method_39415(color);
/* 1547 */     b.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_39415(color);
/*      */     
/* 1549 */     b.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_39415(color);
/* 1550 */     b.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1321).method_39415(color);
/* 1551 */     b.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1324).method_39415(color);
/* 1552 */     b.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_39415(color);
/*      */     
/* 1554 */     b.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_39415(color);
/* 1555 */     b.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_39415(color);
/* 1556 */     b.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324).method_39415(color);
/* 1557 */     b.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1321).method_39415(color);
/*      */     
/* 1559 */     class_286.method_43433(b.method_60800());
/*      */   }
/*      */   
/*      */   private void drawFilledBox(class_289 tessellator, Matrix4f matrix, class_238 box, float r, float g, float b, float a) {
/* 1563 */     RenderSystem.setShader(class_10142.field_53876);
/* 1564 */     class_287 buffer = tessellator.method_60827(class_293.class_5596.field_27382, class_290.field_1576);
/*      */     
/* 1566 */     float minX = (float)box.field_1323;
/* 1567 */     float minY = (float)box.field_1322;
/* 1568 */     float minZ = (float)box.field_1321;
/* 1569 */     float maxX = (float)box.field_1320;
/* 1570 */     float maxY = (float)box.field_1325;
/* 1571 */     float maxZ = (float)box.field_1324;
/*      */     
/* 1573 */     buffer.method_22918(matrix, minX, minY, minZ).method_22915(r, g, b, a);
/* 1574 */     buffer.method_22918(matrix, maxX, minY, minZ).method_22915(r, g, b, a);
/* 1575 */     buffer.method_22918(matrix, maxX, minY, maxZ).method_22915(r, g, b, a);
/* 1576 */     buffer.method_22918(matrix, minX, minY, maxZ).method_22915(r, g, b, a);
/*      */     
/* 1578 */     buffer.method_22918(matrix, minX, maxY, minZ).method_22915(r, g, b, a);
/* 1579 */     buffer.method_22918(matrix, minX, maxY, maxZ).method_22915(r, g, b, a);
/* 1580 */     buffer.method_22918(matrix, maxX, maxY, maxZ).method_22915(r, g, b, a);
/* 1581 */     buffer.method_22918(matrix, maxX, maxY, minZ).method_22915(r, g, b, a);
/*      */     
/* 1583 */     buffer.method_22918(matrix, minX, minY, minZ).method_22915(r, g, b, a);
/* 1584 */     buffer.method_22918(matrix, minX, maxY, minZ).method_22915(r, g, b, a);
/* 1585 */     buffer.method_22918(matrix, maxX, maxY, minZ).method_22915(r, g, b, a);
/* 1586 */     buffer.method_22918(matrix, maxX, minY, minZ).method_22915(r, g, b, a);
/*      */     
/* 1588 */     buffer.method_22918(matrix, minX, minY, maxZ).method_22915(r, g, b, a);
/* 1589 */     buffer.method_22918(matrix, maxX, minY, maxZ).method_22915(r, g, b, a);
/* 1590 */     buffer.method_22918(matrix, maxX, maxY, maxZ).method_22915(r, g, b, a);
/* 1591 */     buffer.method_22918(matrix, minX, maxY, maxZ).method_22915(r, g, b, a);
/*      */     
/* 1593 */     buffer.method_22918(matrix, minX, minY, minZ).method_22915(r, g, b, a);
/* 1594 */     buffer.method_22918(matrix, minX, minY, maxZ).method_22915(r, g, b, a);
/* 1595 */     buffer.method_22918(matrix, minX, maxY, maxZ).method_22915(r, g, b, a);
/* 1596 */     buffer.method_22918(matrix, minX, maxY, minZ).method_22915(r, g, b, a);
/*      */     
/* 1598 */     buffer.method_22918(matrix, maxX, minY, minZ).method_22915(r, g, b, a);
/* 1599 */     buffer.method_22918(matrix, maxX, maxY, minZ).method_22915(r, g, b, a);
/* 1600 */     buffer.method_22918(matrix, maxX, maxY, maxZ).method_22915(r, g, b, a);
/* 1601 */     buffer.method_22918(matrix, maxX, minY, maxZ).method_22915(r, g, b, a);
/*      */     
/* 1603 */     class_286.method_43433(buffer.method_60800());
/*      */   }
/*      */   
/*      */   private void drawBoxOutline(class_289 tessellator, Matrix4f matrix, class_238 box, float r, float g, float b, float a) {
/* 1607 */     RenderSystem.setShader(class_10142.field_53876);
/* 1608 */     RenderSystem.lineWidth(1.5F);
/* 1609 */     class_287 buffer = tessellator.method_60827(class_293.class_5596.field_29344, class_290.field_1576);
/*      */     
/* 1611 */     float minX = (float)box.field_1323;
/* 1612 */     float minY = (float)box.field_1322;
/* 1613 */     float minZ = (float)box.field_1321;
/* 1614 */     float maxX = (float)box.field_1320;
/* 1615 */     float maxY = (float)box.field_1325;
/* 1616 */     float maxZ = (float)box.field_1324;
/*      */     
/* 1618 */     buffer.method_22918(matrix, minX, minY, minZ).method_22915(r, g, b, a);
/* 1619 */     buffer.method_22918(matrix, maxX, minY, minZ).method_22915(r, g, b, a);
/* 1620 */     buffer.method_22918(matrix, maxX, minY, minZ).method_22915(r, g, b, a);
/* 1621 */     buffer.method_22918(matrix, maxX, minY, maxZ).method_22915(r, g, b, a);
/* 1622 */     buffer.method_22918(matrix, maxX, minY, maxZ).method_22915(r, g, b, a);
/* 1623 */     buffer.method_22918(matrix, minX, minY, maxZ).method_22915(r, g, b, a);
/* 1624 */     buffer.method_22918(matrix, minX, minY, maxZ).method_22915(r, g, b, a);
/* 1625 */     buffer.method_22918(matrix, minX, minY, minZ).method_22915(r, g, b, a);
/*      */     
/* 1627 */     buffer.method_22918(matrix, minX, maxY, minZ).method_22915(r, g, b, a);
/* 1628 */     buffer.method_22918(matrix, maxX, maxY, minZ).method_22915(r, g, b, a);
/* 1629 */     buffer.method_22918(matrix, maxX, maxY, minZ).method_22915(r, g, b, a);
/* 1630 */     buffer.method_22918(matrix, maxX, maxY, maxZ).method_22915(r, g, b, a);
/* 1631 */     buffer.method_22918(matrix, maxX, maxY, maxZ).method_22915(r, g, b, a);
/* 1632 */     buffer.method_22918(matrix, minX, maxY, maxZ).method_22915(r, g, b, a);
/* 1633 */     buffer.method_22918(matrix, minX, maxY, maxZ).method_22915(r, g, b, a);
/* 1634 */     buffer.method_22918(matrix, minX, maxY, minZ).method_22915(r, g, b, a);
/*      */     
/* 1636 */     buffer.method_22918(matrix, minX, minY, minZ).method_22915(r, g, b, a);
/* 1637 */     buffer.method_22918(matrix, minX, maxY, minZ).method_22915(r, g, b, a);
/* 1638 */     buffer.method_22918(matrix, maxX, minY, minZ).method_22915(r, g, b, a);
/* 1639 */     buffer.method_22918(matrix, maxX, maxY, minZ).method_22915(r, g, b, a);
/* 1640 */     buffer.method_22918(matrix, maxX, minY, maxZ).method_22915(r, g, b, a);
/* 1641 */     buffer.method_22918(matrix, maxX, maxY, maxZ).method_22915(r, g, b, a);
/* 1642 */     buffer.method_22918(matrix, minX, minY, maxZ).method_22915(r, g, b, a);
/* 1643 */     buffer.method_22918(matrix, minX, maxY, maxZ).method_22915(r, g, b, a);
/*      */     
/* 1645 */     class_286.method_43433(buffer.method_60800());
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\EntityESP.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */