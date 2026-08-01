/*     */ package shame.astra.client.modules.impl.movement;
/*     */ 
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import net.minecraft.class_10142;
/*     */ import net.minecraft.class_238;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_2596;
/*     */ import net.minecraft.class_286;
/*     */ import net.minecraft.class_287;
/*     */ import net.minecraft.class_289;
/*     */ import net.minecraft.class_290;
/*     */ import net.minecraft.class_293;
/*     */ import net.minecraft.class_4050;
/*     */ import net.minecraft.class_4587;
/*     */ import org.joml.Matrix4f;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.Event3DRender;
/*     */ import shame.astra.api.events.implement.EventMove;
/*     */ import shame.astra.api.events.implement.EventMoveInput;
/*     */ import shame.astra.api.events.implement.EventPacket;
/*     */ import shame.astra.client.modules.Module;
/*     */ 
/*     */ public class FreeCam extends Module {
/*  24 */   public static FreeCam INSTANCE = new FreeCam();
/*     */   
/*     */   public class_243 pos;
/*     */   
/*     */   public FreeCam() {
/*  29 */     super("FreeCam", "Обзор местности за фейк игрока", Module.ModuleCategory.MOVEMENT);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  34 */     super.onEnable();
/*  35 */     if (mc.field_1724 != null) {
/*  36 */       this.pos = mc.field_1724.method_19538();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  42 */     super.onDisable();
/*  43 */     if (mc.field_1724 != null && this.pos != null) {
/*  44 */       mc.field_1724.method_33574(this.pos);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onEvent(EventPacket event) {
/*  50 */     class_2596<?> packet = event.getPacket();
/*     */     
/*  52 */     if (packet instanceof net.minecraft.class_2828) {
/*  53 */       event.cancel();
/*  54 */     } else if (packet instanceof net.minecraft.class_2724 || packet instanceof net.minecraft.class_2678) {
/*  55 */       toggle();
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onEvent(Event3DRender event) {
/*  61 */     if (this.pos == null || mc.field_1724 == null)
/*     */       return; 
/*  63 */     float width = mc.field_1724.method_17681() / 2.0F;
/*  64 */     float height = mc.field_1724.method_17682();
/*     */     
/*  66 */     class_238 box = new class_238(this.pos.field_1352 - width, this.pos.field_1351, this.pos.field_1350 - width, this.pos.field_1352 + width, this.pos.field_1351 + height, this.pos.field_1350 + width);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  75 */     drawHitbox(event.getMatrices(), box, event.getCamera().method_19326());
/*     */   }
/*     */   
/*     */   private void drawHitbox(class_4587 matrices, class_238 box, class_243 camera) {
/*  79 */     double x1 = box.field_1323 - camera.field_1352;
/*  80 */     double y1 = box.field_1322 - camera.field_1351;
/*  81 */     double z1 = box.field_1321 - camera.field_1350;
/*  82 */     double x2 = box.field_1320 - camera.field_1352;
/*  83 */     double y2 = box.field_1325 - camera.field_1351;
/*  84 */     double z2 = box.field_1324 - camera.field_1350;
/*     */     
/*  86 */     Matrix4f matrix = matrices.method_23760().method_23761();
/*     */     
/*  88 */     class_289 tessellator = class_289.method_1348();
/*     */     
/*  90 */     RenderSystem.enableBlend();
/*  91 */     RenderSystem.defaultBlendFunc();
/*  92 */     RenderSystem.disableCull();
/*  93 */     RenderSystem.disableDepthTest();
/*  94 */     RenderSystem.setShader(class_10142.field_53876);
/*  95 */     RenderSystem.lineWidth(1.5F);
/*     */     
/*  97 */     class_287 buffer = tessellator.method_60827(class_293.class_5596.field_29344, class_290.field_1576);
/*     */     
/*  99 */     float r = 1.0F;
/* 100 */     float g = 1.0F;
/* 101 */     float b = 1.0F;
/* 102 */     float a = 1.0F;
/*     */     
/* 104 */     buffer.method_22918(matrix, (float)x1, (float)y1, (float)z1).method_22915(r, g, b, a);
/* 105 */     buffer.method_22918(matrix, (float)x2, (float)y1, (float)z1).method_22915(r, g, b, a);
/*     */     
/* 107 */     buffer.method_22918(matrix, (float)x2, (float)y1, (float)z1).method_22915(r, g, b, a);
/* 108 */     buffer.method_22918(matrix, (float)x2, (float)y1, (float)z2).method_22915(r, g, b, a);
/*     */     
/* 110 */     buffer.method_22918(matrix, (float)x2, (float)y1, (float)z2).method_22915(r, g, b, a);
/* 111 */     buffer.method_22918(matrix, (float)x1, (float)y1, (float)z2).method_22915(r, g, b, a);
/*     */     
/* 113 */     buffer.method_22918(matrix, (float)x1, (float)y1, (float)z2).method_22915(r, g, b, a);
/* 114 */     buffer.method_22918(matrix, (float)x1, (float)y1, (float)z1).method_22915(r, g, b, a);
/*     */     
/* 116 */     buffer.method_22918(matrix, (float)x1, (float)y2, (float)z1).method_22915(r, g, b, a);
/* 117 */     buffer.method_22918(matrix, (float)x2, (float)y2, (float)z1).method_22915(r, g, b, a);
/*     */     
/* 119 */     buffer.method_22918(matrix, (float)x2, (float)y2, (float)z1).method_22915(r, g, b, a);
/* 120 */     buffer.method_22918(matrix, (float)x2, (float)y2, (float)z2).method_22915(r, g, b, a);
/*     */     
/* 122 */     buffer.method_22918(matrix, (float)x2, (float)y2, (float)z2).method_22915(r, g, b, a);
/* 123 */     buffer.method_22918(matrix, (float)x1, (float)y2, (float)z2).method_22915(r, g, b, a);
/*     */     
/* 125 */     buffer.method_22918(matrix, (float)x1, (float)y2, (float)z2).method_22915(r, g, b, a);
/* 126 */     buffer.method_22918(matrix, (float)x1, (float)y2, (float)z1).method_22915(r, g, b, a);
/*     */     
/* 128 */     buffer.method_22918(matrix, (float)x1, (float)y1, (float)z1).method_22915(r, g, b, a);
/* 129 */     buffer.method_22918(matrix, (float)x1, (float)y2, (float)z1).method_22915(r, g, b, a);
/*     */     
/* 131 */     buffer.method_22918(matrix, (float)x2, (float)y1, (float)z1).method_22915(r, g, b, a);
/* 132 */     buffer.method_22918(matrix, (float)x2, (float)y2, (float)z1).method_22915(r, g, b, a);
/*     */     
/* 134 */     buffer.method_22918(matrix, (float)x2, (float)y1, (float)z2).method_22915(r, g, b, a);
/* 135 */     buffer.method_22918(matrix, (float)x2, (float)y2, (float)z2).method_22915(r, g, b, a);
/*     */     
/* 137 */     buffer.method_22918(matrix, (float)x1, (float)y1, (float)z2).method_22915(r, g, b, a);
/* 138 */     buffer.method_22918(matrix, (float)x1, (float)y2, (float)z2).method_22915(r, g, b, a);
/*     */     
/* 140 */     class_286.method_43433(buffer.method_60800());
/*     */     
/* 142 */     RenderSystem.enableDepthTest();
/* 143 */     RenderSystem.enableCull();
/* 144 */     RenderSystem.disableBlend();
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onEvent(EventMove event) {
/* 149 */     if (mc.field_1724 == null)
/*     */       return; 
/* 151 */     mc.field_1724.field_5960 = true;
/*     */     
/* 153 */     double speed = 1.0D;
/* 154 */     double forward = mc.field_1724.field_3913.field_3905;
/* 155 */     double strafe = mc.field_1724.field_3913.field_3907;
/*     */     
/* 157 */     double yaw = Math.toRadians(mc.field_1724.method_36454());
/*     */     
/* 159 */     double motionX = 0.0D;
/* 160 */     double motionZ = 0.0D;
/*     */     
/* 162 */     if (forward != 0.0D || strafe != 0.0D) {
/* 163 */       double angle = yaw + Math.atan2(-strafe, forward);
/* 164 */       motionX = -Math.sin(angle) * speed;
/* 165 */       motionZ = Math.cos(angle) * speed;
/*     */     } 
/*     */     
/* 168 */     double motionY = 0.0D;
/* 169 */     if (mc.field_1690.field_1903.method_1434()) {
/* 170 */       motionY = speed;
/* 171 */     } else if (mc.field_1690.field_1832.method_1434()) {
/* 172 */       motionY = -speed;
/*     */     } 
/*     */     
/* 175 */     event.setMovePos(new class_243(motionX, motionY, motionZ));
/*     */   }
/*     */   
/*     */   @EventLink
/*     */   public void onEvent(EventMoveInput event) {
/* 180 */     if (mc.field_1724 == null)
/*     */       return; 
/* 182 */     if (mc.field_1724.method_18376() == class_4050.field_18081 || mc.field_1724.method_18376() == class_4050.field_18079)
/* 183 */       event.setStrafe(event.getStrafe() * 5.0F); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\movement\FreeCam.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */