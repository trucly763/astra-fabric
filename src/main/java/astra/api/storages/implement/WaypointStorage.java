/*     */ package shame.astra.api.storages.implement;
/*     */ 
/*     */ import lombok.Generated;
/*     */ import net.minecraft.class_2960;
/*     */ import net.minecraft.class_3532;
/*     */ import net.minecraft.class_7833;
/*     */ import shame.astra.api.QClient;
/*     */ import shame.astra.api.events.EventInvoker;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.EventRender;
/*     */ import shame.astra.api.utils.animation.AnimationUtils;
/*     */ import shame.astra.api.utils.animation.Easings;
/*     */ import shame.astra.api.utils.cmd.waypoint.Waypoint;
/*     */ import shame.astra.api.utils.color.ColorUtils;
/*     */ import shame.astra.api.utils.math.MathUtils;
/*     */ import shame.astra.api.utils.render.RenderUtils;
/*     */ import shame.astra.api.utils.render.fonts.msdf.Font;
/*     */ import shame.astra.api.utils.render.fonts.msdf.Fonts;
/*     */ 
/*     */ public class WaypointStorage
/*     */   implements QClient {
/*  22 */   private static final class_2960 ARROW_TEXTURE = class_2960.method_60655("astra", "textures/arrows/gps.png");
/*     */   
/*  24 */   private final AnimationUtils alphaAnimation = new AnimationUtils(0.0F, 8.5F, Easings.CUBIC_OUT);
/*     */   
/*     */   private float animatedYaw;
/*     */   
/*     */   private Waypoint activeWaypoint;
/*     */   
/*     */   public WaypointStorage() {
/*  31 */     this.activeWaypoint = null; EventInvoker.register(this); } @Generated public Waypoint getActiveWaypoint() { return this.activeWaypoint; }
/*     */ 
/*     */   
/*     */   public void set(Waypoint waypoint) {
/*  35 */     this.activeWaypoint = waypoint;
/*     */   }
/*     */   
/*     */   public void remove(Waypoint waypoint) {
/*  39 */     if (this.activeWaypoint != null && this.activeWaypoint.equals(waypoint)) {
/*  40 */       this.activeWaypoint = null;
/*     */     }
/*     */   }
/*     */   
/*     */   public void clear() {
/*  45 */     this.activeWaypoint = null;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  49 */     return (this.activeWaypoint == null);
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onRender2D(EventRender.Default event) {
/*  54 */     if (mc.field_1724 == null || mc.field_1687 == null) {
/*     */       return;
/*     */     }
/*     */     
/*  58 */     this.alphaAnimation.update((this.activeWaypoint == null) ? 0.0F : 1.0F);
/*  59 */     float alpha = class_3532.method_15363(this.alphaAnimation.getValue(), 0.0F, 1.0F);
/*  60 */     if (this.activeWaypoint == null || alpha <= 0.02F) {
/*     */       return;
/*     */     }
/*     */     
/*  64 */     float centerX = mc.method_22683().method_4486() * 0.5F;
/*  65 */     float centerY = mc.method_22683().method_4502() * 0.25F;
/*  66 */     float size = 40.0F;
/*     */     
/*  68 */     double deltaX = this.activeWaypoint.getX() - mc.field_1724.method_23317();
/*  69 */     double deltaZ = this.activeWaypoint.getZ() - mc.field_1724.method_23321();
/*  70 */     int distance = (int)MathUtils.round(class_3532.method_15355((float)(deltaX * deltaX + deltaZ * deltaZ)));
/*     */     
/*  72 */     float targetYaw = (float)-Math.toDegrees(Math.atan2(deltaX, deltaZ)) - mc.field_1773.method_19418().method_19330();
/*  73 */     this.animatedYaw = interpolateAngle(this.animatedYaw, targetYaw, 0.18F);
/*     */     
/*  75 */     int color = ColorUtils.applyAlpha(ColorUtils.getThemeColor(), alpha);
/*     */     
/*  77 */     Font font = Fonts.getFont("sf_regular", 12);
/*  78 */     if (font != null) {
/*  79 */       String distanceText = "" + distance + "m.";
/*  80 */       font.draw(event.getContext().method_51448(), distanceText, centerX - font
/*  81 */           .getWidth(distanceText) * 0.5F + 1.5F, centerY + 7.5F, 
/*     */           
/*  83 */           ColorUtils.applyAlpha(-1, alpha));
/*     */     } 
/*     */     
/*  86 */     event.getContext().method_51448().method_22903();
/*  87 */     event.getContext().method_51448().method_46416(centerX, centerY, 0.0F);
/*  88 */     event.getContext().method_51448().method_22907(class_7833.field_40718.rotationDegrees(this.animatedYaw));
/*  89 */     event.getContext().method_51448().method_46416(-centerX, -centerY, 0.0F);
/*     */     
/*  91 */     float drawX = centerX - size * 0.5F;
/*  92 */     float drawY = centerY - size * 0.5F;
/*     */     
/*  94 */     RenderUtils.drawImage(event.getContext().method_51448(), ARROW_TEXTURE, drawX, drawY, size, size, color);
/*  95 */     event.getContext().method_51448().method_22909();
/*     */   }
/*     */   
/*     */   private float interpolateAngle(float current, float target, float factor) {
/*  99 */     float delta = class_3532.method_15393(target - current);
/* 100 */     return current + delta * factor;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\api\storages\implement\WaypointStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */