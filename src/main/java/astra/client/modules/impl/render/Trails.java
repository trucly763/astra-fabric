/*     */ package shame.astra.client.modules.impl.render;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.class_1657;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_286;
/*     */ import net.minecraft.class_287;
/*     */ import net.minecraft.class_289;
/*     */ import net.minecraft.class_290;
/*     */ import net.minecraft.class_293;
/*     */ import net.minecraft.class_3532;
/*     */ import net.minecraft.class_4587;
/*     */ import net.minecraft.class_5498;
/*     */ import org.joml.Matrix4f;
/*     */ import shame.astra.api.events.implement.Event3DRender;
/*     */ import shame.astra.api.utils.color.ColorUtils;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*     */ 
/*     */ public class Trails extends Module {
/*  23 */   public static Trails INSTANCE = new Trails();
/*     */   
/*  25 */   private final FloatSetting duration = new FloatSetting("Длительность", 300.0F, 100.0F, 1000.0F, 10.0F);
/*     */   
/*  27 */   private final List<Point> points = new ArrayList<>();
/*     */   
/*     */   public Trails() {
/*  30 */     super("Trails", "Красивый след за игроком", Module.ModuleCategory.RENDER);
/*  31 */     addSettings(new Setting[] { (Setting)this.duration });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  36 */     this.points.clear();
/*  37 */     super.onDisable();
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onRender(Event3DRender event) {
/*  42 */     if (mc.field_1690.method_31044() == class_5498.field_26664) {
/*     */       return;
/*     */     }
/*     */     
/*  46 */     if (mc.field_1724 == null || mc.field_1687 == null)
/*     */       return; 
/*  48 */     long currentTime = System.currentTimeMillis();
/*     */     
/*  50 */     this.points.removeIf(p -> ((float)(currentTime - p.time) > this.duration.get()));
/*     */     
/*  52 */     class_243 playerPos = interpolatePlayerPosition(event.getTickDelta());
/*     */     
/*  54 */     this.points.add(new Point(playerPos));
/*     */     
/*  56 */     render3DPoints(event.getMatrices());
/*     */   }
/*     */   
/*     */   private class_243 interpolatePlayerPosition(float partialTicks) {
/*  60 */     return new class_243(
/*  61 */         class_3532.method_16436(partialTicks, mc.field_1724.field_6014, mc.field_1724.method_23317()), 
/*  62 */         class_3532.method_16436(partialTicks, mc.field_1724.field_6036, mc.field_1724.method_23318()), 
/*  63 */         class_3532.method_16436(partialTicks, mc.field_1724.field_5969, mc.field_1724.method_23321()));
/*     */   }
/*     */ 
/*     */   
/*     */   private class_243 interpolatePlayerPosition(class_1657 playerEntity, float partialTicks) {
/*  68 */     return new class_243(
/*  69 */         class_3532.method_16436(partialTicks, playerEntity.field_6014, playerEntity.method_23317()), 
/*  70 */         class_3532.method_16436(partialTicks, playerEntity.field_6036, playerEntity.method_23318()), 
/*  71 */         class_3532.method_16436(partialTicks, playerEntity.field_5969, playerEntity.method_23321()));
/*     */   }
/*     */ 
/*     */   
/*     */   private void render3DPoints(class_4587 matrixStack) {
/*  76 */     if (this.points.size() < 2)
/*     */       return; 
/*  78 */     startRendering();
/*     */     
/*  80 */     matrixStack.method_22903();
/*     */     
/*  82 */     class_243 view = mc.field_1773.method_19418().method_19326();
/*  83 */     matrixStack.method_22904(-view.field_1352, -view.field_1351, -view.field_1350);
/*     */     
/*  85 */     Matrix4f matrix = matrixStack.method_23760().method_23761();
/*     */     
/*  87 */     int themeColor = ColorUtils.getThemeColor();
/*  88 */     float red = ColorUtils.redf(themeColor);
/*  89 */     float green = ColorUtils.greenf(themeColor);
/*  90 */     float blue = ColorUtils.bluef(themeColor);
/*     */     
/*  92 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27380, class_290.field_1576);
/*     */     
/*  94 */     int index = 0;
/*  95 */     for (Point p : this.points) {
/*  96 */       float alpha = index / this.points.size() * 0.7F;
/*  97 */       int alphaInt = (int)(alpha * 255.0F);
/*     */       
/*  99 */       buffer.method_22918(matrix, (float)p.pos.field_1352, (float)(p.pos.field_1351 + mc.field_1724.method_17682()), (float)p.pos.field_1350)
/* 100 */         .method_1336((int)(red * 255.0F), (int)(green * 255.0F), (int)(blue * 255.0F), alphaInt);
/* 101 */       buffer.method_22918(matrix, (float)p.pos.field_1352, (float)p.pos.field_1351, (float)p.pos.field_1350)
/* 102 */         .method_1336((int)(red * 255.0F), (int)(green * 255.0F), (int)(blue * 255.0F), alphaInt);
/* 103 */       index++;
/*     */     } 
/*     */     
/* 106 */     class_286.method_43433(buffer.method_60800());
/*     */     
/* 108 */     RenderSystem.lineWidth(2.0F);
/*     */     
/* 110 */     renderLineStrip(matrix, this.points, true, red, green, blue);
/* 111 */     renderLineStrip(matrix, this.points, false, red, green, blue);
/*     */     
/* 113 */     matrixStack.method_22909();
/* 114 */     stopRendering();
/*     */   }
/*     */   
/*     */   private void renderLineStrip(Matrix4f matrix, List<Point> points, boolean withHeight, float red, float green, float blue) {
/* 118 */     class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_29345, class_290.field_1576);
/*     */     
/* 120 */     int index = 0;
/* 121 */     for (Point p : points) {
/* 122 */       float alpha = Math.min(index / points.size() * 1.5F, 1.0F);
/* 123 */       int alphaInt = (int)(alpha * 255.0F);
/*     */       
/* 125 */       float y = withHeight ? (float)(p.pos.field_1351 + mc.field_1724.method_17682()) : (float)p.pos.field_1351;
/*     */       
/* 127 */       buffer.method_22918(matrix, (float)p.pos.field_1352, y, (float)p.pos.field_1350)
/* 128 */         .method_1336((int)(red * 255.0F), (int)(green * 255.0F), (int)(blue * 255.0F), alphaInt);
/* 129 */       index++;
/*     */     } 
/*     */     
/* 132 */     class_286.method_43433(buffer.method_60800());
/*     */   }
/*     */   
/*     */   private void startRendering() {
/* 136 */     RenderSystem.enableBlend();
/* 137 */     RenderSystem.disableCull();
/* 138 */     RenderSystem.enableDepthTest();
/* 139 */     RenderSystem.depthMask(false);
/* 140 */     RenderSystem.defaultBlendFunc();
/* 141 */     RenderSystem.setShader(class_10142.field_53876);
/*     */   }
/*     */   
/*     */   private void stopRendering() {
/* 145 */     RenderSystem.depthMask(true);
/* 146 */     RenderSystem.enableCull();
/* 147 */     RenderSystem.disableBlend();
/*     */   }
/*     */   
/*     */   private static class Point {
/*     */     public class_243 pos;
/*     */     public long time;
/*     */     
/*     */     public Point(class_243 pos) {
/* 155 */       this.pos = pos;
/* 156 */       this.time = System.currentTimeMillis();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\Trails.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */