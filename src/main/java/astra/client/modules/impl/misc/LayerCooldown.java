/*     */ package shame.astra.client.modules.impl.misc;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import net.minecraft.class_1799;
/*     */ import net.minecraft.class_1935;
/*     */ import net.minecraft.class_2246;
/*     */ import net.minecraft.class_2248;
/*     */ import net.minecraft.class_2338;
/*     */ import net.minecraft.class_2374;
/*     */ import net.minecraft.class_2382;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_2596;
/*     */ import net.minecraft.class_2680;
/*     */ import net.minecraft.class_2767;
/*     */ import net.minecraft.class_332;
/*     */ import net.minecraft.class_4587;
/*     */ import net.minecraft.class_7923;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector4f;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.Event3DRender;
/*     */ import shame.astra.api.events.implement.EventPacket;
/*     */ import shame.astra.api.events.implement.EventRender;
/*     */ import shame.astra.api.utils.color.ColorUtils;
/*     */ import shame.astra.api.utils.render.RenderUtils;
/*     */ import shame.astra.api.utils.render.fonts.msdf.Font;
/*     */ import shame.astra.client.modules.Module;
/*     */ 
/*     */ public class LayerCooldown extends Module {
/*  36 */   public static LayerCooldown INSTANCE = new LayerCooldown();
/*     */   
/*     */   private static final long DELAYED_SCAN_MS = 250L;
/*     */   private static final int SEARCH_RADIUS = 4;
/*     */   private static final int SEARCH_HEIGHT = 4;
/*     */   private static final int MAX_TIMERS = 100;
/*     */   private static final float TIMER_SECONDS = 19.5F;
/*     */   private static final float MAX_DISTANCE = 96.0F;
/*     */   private static final double TIMER_Y_OFFSET = 0.6D;
/*  45 */   private static final class_1799 LAYER_ICON = new class_1799((class_1935)class_1802.field_8551);
/*     */   
/*  47 */   private final Matrix4f lastProjectionMatrix = new Matrix4f();
/*  48 */   private final Quaternionf lastCameraRotation = new Quaternionf();
/*  49 */   private class_243 lastCameraPos = class_243.field_1353;
/*     */   
/*     */   private boolean hasProjection;
/*  52 */   private final List<LayerTimer> timers = new ArrayList<>();
/*  53 */   private final List<PendingScan> pendingScans = new ArrayList<>();
/*     */   
/*     */   public LayerCooldown() {
/*  56 */     super("LayerCooldown", "Показывает таймер возле поставленного пласта", Module.ModuleCategory.MISC);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  61 */     this.timers.clear();
/*  62 */     this.pendingScans.clear();
/*  63 */     this.hasProjection = false;
/*  64 */     super.onDisable();
/*     */   }
/*     */   @EventLink
/*     */   public void onPacket(EventPacket event) {
/*     */     class_2767 packet;
/*  69 */     if (event.getType() != EventPacket.Type.RECEIVE || mc.field_1687 == null || mc.field_1724 == null)
/*  70 */       return;  class_2596 class_2596 = event.getPacket(); if (class_2596 instanceof class_2767) { packet = (class_2767)class_2596; }
/*     */     else { return; }
/*  72 */      String sound = getSoundPath(packet);
/*  73 */     if (sound == null)
/*     */       return; 
/*  75 */     class_243 soundPos = new class_243(packet.method_11890(), packet.method_11889(), packet.method_11893());
/*  76 */     class_2338 blockPos = class_2338.method_49638((class_2374)soundPos);
/*     */     
/*  78 */     if ("block.piston.extend".equals(sound)) {
/*  79 */       addTimer(blockPos, soundPos);
/*     */       
/*     */       return;
/*     */     } 
/*  83 */     if (isDelayedTrapSound(sound)) {
/*  84 */       this.pendingScans.add(new PendingScan(blockPos, System.currentTimeMillis() + 250L));
/*     */     }
/*     */   }
/*     */   
/*     */   @EventLink(priority = 100)
/*     */   public void onRender3D(Event3DRender event) {
/*  90 */     if (mc.field_1687 == null || mc.field_1724 == null)
/*     */       return; 
/*  92 */     this.hasProjection = true;
/*  93 */     this.lastProjectionMatrix.set((Matrix4fc)event.getProjectionMatrix());
/*  94 */     this.lastCameraRotation.set((Quaternionfc)event.getCamera().method_23767());
/*  95 */     this.lastCameraPos = event.getCamera().method_19326();
/*     */     
/*  97 */     processPendingScans();
/*     */   }
/*     */   
/*     */   @EventLink(priority = 100)
/*     */   public void onRender2D(EventRender.Default event) {
/* 102 */     if (!this.hasProjection || mc.field_1687 == null || mc.field_1724 == null)
/*     */       return; 
/* 104 */     long now = System.currentTimeMillis();
/* 105 */     this.timers.removeIf(timer -> (timer.endTime <= now));
/* 106 */     while (this.timers.size() > 100) {
/* 107 */       this.timers.remove(0);
/*     */     }
/*     */     
/* 110 */     if (this.timers.isEmpty())
/*     */       return; 
/* 112 */     class_4587 matrices = event.getContext().method_51448();
/* 113 */     Font font = Fonts.getFont("sf_regular", 13);
/* 114 */     if (font == null)
/*     */       return; 
/* 116 */     float maxDistSq = 9216.0F;
/* 117 */     for (int i = 0; i < this.timers.size(); i++) {
/* 118 */       LayerTimer timer = this.timers.get(i);
/* 119 */       if (mc.field_1724.method_5707(timer.pos) <= maxDistSq) {
/*     */         
/* 121 */         class_243 screen = worldToScreen(timer.pos);
/* 122 */         if (screen != null) {
/*     */           
/* 124 */           float seconds = Math.max(0.0F, (float)(timer.endTime - now) / 1000.0F);
/* 125 */           drawTimer(event.getContext(), matrices, font, (float)screen.field_1352, (float)screen.field_1351, seconds);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   } private void processPendingScans() {
/* 130 */     if (this.pendingScans.isEmpty() || mc.field_1687 == null)
/*     */       return; 
/* 132 */     long now = System.currentTimeMillis();
/* 133 */     Iterator<PendingScan> iterator = this.pendingScans.iterator();
/* 134 */     while (iterator.hasNext()) {
/* 135 */       PendingScan scan = iterator.next();
/* 136 */       if (scan.runAt > now)
/*     */         continue; 
/* 138 */       class_2338 found = findLayerLikeBlock(scan.center);
/*     */ 
/*     */       
/* 141 */       class_243 pos = (found == null) ? class_243.method_24953((class_2382)scan.center) : new class_243(found.method_10263() + 0.5D, found.method_10264() + 0.65D, found.method_10260() + 0.5D);
/* 142 */       addTimer((found == null) ? scan.center : found, pos);
/* 143 */       iterator.remove();
/*     */     } 
/*     */   }
/*     */   
/*     */   private class_2338 findLayerLikeBlock(class_2338 center) {
/* 148 */     class_2338 best = null;
/* 149 */     double bestDistance = Double.MAX_VALUE;
/*     */     
/* 151 */     for (int x = -4; x <= 4; x++) {
/* 152 */       for (int y = -4; y <= 4; y++) {
/* 153 */         for (int z = -4; z <= 4; z++) {
/* 154 */           class_2338 pos = center.method_10069(x, y, z);
/* 155 */           class_2680 state = mc.field_1687.method_8320(pos);
/* 156 */           if (isLayerLikeBlock(state)) {
/*     */             
/* 158 */             double distance = pos.method_10262((class_2382)center);
/* 159 */             if (distance < bestDistance) {
/* 160 */               bestDistance = distance;
/* 161 */               best = pos;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 167 */     return best;
/*     */   }
/*     */   
/*     */   private boolean isLayerLikeBlock(class_2680 state) {
/* 171 */     if (state == null || state.method_26215()) return false;
/*     */     
/* 173 */     class_2248 block = state.method_26204();
/* 174 */     return (block == class_2246.field_10560 || block == class_2246.field_10615 || block == class_2246.field_10008 || block == class_2246.field_10342 || block == class_2246.field_10535 || block == class_2246.field_10105 || block == class_2246.field_10414);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addTimer(class_2338 blockPos, class_243 renderPos) {
/* 184 */     long endTime = System.currentTimeMillis() + 19500L;
/*     */     
/* 186 */     for (int i = 0; i < this.timers.size(); i++) {
/* 187 */       LayerTimer timer = this.timers.get(i);
/* 188 */       if (timer.blockPos.method_10262((class_2382)blockPos) <= 2.25D) {
/* 189 */         this.timers.set(i, new LayerTimer(blockPos, renderPos.method_1031(0.0D, 0.6D, 0.0D), endTime));
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 194 */     this.timers.add(new LayerTimer(blockPos, renderPos.method_1031(0.0D, 0.6D, 0.0D), endTime));
/*     */   }
/*     */   
/*     */   private boolean isDelayedTrapSound(String sound) {
/* 198 */     return ("block.anvil.place".equals(sound) || "entity.zombie_horse.death"
/* 199 */       .equals(sound) || "entity.ender_dragon.growl"
/* 200 */       .equals(sound));
/*     */   }
/*     */   
/*     */   private String getSoundPath(class_2767 packet) {
/*     */     try {
/* 205 */       return class_7923.field_41172.method_10221(packet.method_11894().comp_349()).method_12832();
/* 206 */     } catch (Exception ignored) {
/* 207 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void drawTimer(class_332 context, class_4587 matrices, Font font, float x, float y, float seconds) {
/* 212 */     String text = formatOneDecimal(seconds) + "с";
/* 213 */     float textWidth = font.getStringWidth(text);
/* 214 */     float iconSize = 10.0F;
/* 215 */     float iconScale = 0.62F;
/* 216 */     float gap = 3.0F;
/* 217 */     float boxWidth = iconSize + gap + textWidth + 8.0F;
/* 218 */     float boxHeight = 12.5F;
/* 219 */     float boxX = x - boxWidth * 0.5F;
/* 220 */     float boxY = y - boxHeight * 0.5F;
/* 221 */     int themeColor = ColorUtils.getThemeColor();
/*     */     
/* 223 */     RenderSystem.enableBlend();
/* 224 */     RenderSystem.defaultBlendFunc();
/* 225 */     RenderUtils.drawDefaultHudThemedPanel(matrices, boxX, boxY, boxWidth, boxHeight, 2.0F, 3.0F, themeColor);
/* 226 */     drawItemIcon(context, matrices, boxX + 4.0F, boxY + 1.25F, iconScale);
/* 227 */     font.drawString(matrices, text, boxX + 4.0F + iconSize + gap, boxY + 4.55F, -1);
/* 228 */     RenderSystem.disableBlend();
/*     */   }
/*     */   
/*     */   private String formatOneDecimal(float value) {
/* 232 */     int scaled = Math.round(value * 10.0F);
/* 233 */     return "" + scaled / 10 + "." + scaled / 10;
/*     */   }
/*     */   
/*     */   private void drawItemIcon(class_332 context, class_4587 matrices, float x, float y, float scale) {
/* 237 */     if (context == null)
/*     */       return; 
/* 239 */     RenderSystem.enableBlend();
/* 240 */     RenderSystem.defaultBlendFunc();
/* 241 */     RenderSystem.disableDepthTest();
/* 242 */     RenderSystem.depthMask(false);
/* 243 */     matrices.method_22903();
/* 244 */     matrices.method_46416(x, y, 0.0F);
/* 245 */     matrices.method_22905(scale, scale, 1.0F);
/* 246 */     context.method_51427(LAYER_ICON, 0, 0);
/* 247 */     matrices.method_22909();
/* 248 */     RenderSystem.depthMask(true);
/* 249 */     RenderSystem.enableDepthTest();
/*     */   }
/*     */   
/*     */   private class_243 worldToScreen(class_243 worldPos) {
/* 253 */     if (mc == null || mc.method_22683() == null) return null;
/*     */     
/* 255 */     Vector3f relative = new Vector3f((float)(worldPos.field_1352 - this.lastCameraPos.field_1352), (float)(worldPos.field_1351 - this.lastCameraPos.field_1351), (float)(worldPos.field_1350 - this.lastCameraPos.field_1350));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 261 */     Quaternionf invCameraRot = (new Quaternionf((Quaternionfc)this.lastCameraRotation)).conjugate();
/* 262 */     relative.rotate((Quaternionfc)invCameraRot);
/*     */     
/* 264 */     Vector4f clip = new Vector4f(relative.x, relative.y, relative.z, 1.0F);
/* 265 */     this.lastProjectionMatrix.transform(clip);
/*     */     
/* 267 */     float w = clip.w;
/* 268 */     if (w <= 1.0E-5F) return null;
/*     */     
/* 270 */     float ndcX = clip.x / w;
/* 271 */     float ndcY = clip.y / w;
/* 272 */     float ndcZ = clip.z / w;
/*     */     
/* 274 */     float screenX = (ndcX * 0.5F + 0.5F) * mc.method_22683().method_4486();
/* 275 */     float screenY = (1.0F - ndcY * 0.5F + 0.5F) * mc.method_22683().method_4502();
/*     */     
/* 277 */     if (Float.isNaN(screenX) || Float.isNaN(screenY) || Float.isInfinite(screenX) || Float.isInfinite(screenY)) {
/* 278 */       return null;
/*     */     }
/* 280 */     if (screenX < -400.0F || screenY < -400.0F || screenX > (mc
/* 281 */       .method_22683().method_4486() + 400) || screenY > (mc
/* 282 */       .method_22683().method_4502() + 400)) {
/* 283 */       return null;
/*     */     }
/*     */     
/* 286 */     return new class_243(screenX, screenY, ndcZ);
/*     */   }
/*     */   private static final class LayerTimer extends Record { private final class_2338 blockPos; private final class_243 pos; private final long endTime;
/* 289 */     private LayerTimer(class_2338 blockPos, class_243 pos, long endTime) { this.blockPos = blockPos; this.pos = pos; this.endTime = endTime; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lshame/astra/client/modules/impl/misc/LayerCooldown$LayerTimer;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #289	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 289 */       //   0	7	0	this	Lshame/astra/client/modules/impl/misc/LayerCooldown$LayerTimer; } public class_2338 blockPos() { return this.blockPos; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lshame/astra/client/modules/impl/misc/LayerCooldown$LayerTimer;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #289	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lshame/astra/client/modules/impl/misc/LayerCooldown$LayerTimer; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lshame/astra/client/modules/impl/misc/LayerCooldown$LayerTimer;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #289	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lshame/astra/client/modules/impl/misc/LayerCooldown$LayerTimer;
/* 289 */       //   0	8	1	o	Ljava/lang/Object; } public class_243 pos() { return this.pos; } public long endTime() { return this.endTime; }
/*     */      }
/*     */   private static final class PendingScan extends Record { private final class_2338 center; private final long runAt;
/* 292 */     private PendingScan(class_2338 center, long runAt) { this.center = center; this.runAt = runAt; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lshame/astra/client/modules/impl/misc/LayerCooldown$PendingScan;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #292	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lshame/astra/client/modules/impl/misc/LayerCooldown$PendingScan; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lshame/astra/client/modules/impl/misc/LayerCooldown$PendingScan;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #292	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lshame/astra/client/modules/impl/misc/LayerCooldown$PendingScan; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lshame/astra/client/modules/impl/misc/LayerCooldown$PendingScan;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #292	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lshame/astra/client/modules/impl/misc/LayerCooldown$PendingScan;
/* 292 */       //   0	8	1	o	Ljava/lang/Object; } public class_2338 center() { return this.center; } public long runAt() { return this.runAt; }
/*     */      }
/*     */ 
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\misc\LayerCooldown.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */