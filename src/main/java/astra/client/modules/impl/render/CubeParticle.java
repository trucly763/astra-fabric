/*      */ package shame.astra.client.modules.impl.render;
/*      */ 
/*      */ import net.minecraft.class_1309;
/*      */ import net.minecraft.class_243;
/*      */ import net.minecraft.class_287;
/*      */ import net.minecraft.class_3532;
/*      */ import net.minecraft.class_4587;
/*      */ import net.minecraft.class_7833;
/*      */ import org.joml.Matrix4f;
/*      */ import shame.astra.api.QClient;
/*      */ import shame.astra.api.utils.color.ColorUtils;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class CubeParticle
/*      */   implements QClient
/*      */ {
/*      */   double x;
/*      */   double y;
/*      */   double z;
/*      */   double worldX;
/*      */   double worldY;
/*      */   double worldZ;
/*      */   long time;
/*      */   class_1309 entity;
/*      */   boolean fading;
/*      */   long fadeStartTime;
/*      */   float vx;
/*      */   float vy;
/*      */   float vz;
/*      */   float rotX;
/*      */   float rotY;
/*      */   float rotZ;
/*      */   float rotSpeedX;
/*      */   float rotSpeedY;
/*      */   float rotSpeedZ;
/*      */   
/*      */   public CubeParticle(class_1309 entity, double x, double y, double z) {
/* 1050 */     this.entity = entity;
/* 1051 */     this.x = x;
/* 1052 */     this.y = y;
/* 1053 */     this.z = z;
/* 1054 */     this.time = System.currentTimeMillis();
/* 1055 */     this.rotX = (float)(Math.random() * 360.0D);
/* 1056 */     this.rotY = (float)(Math.random() * 360.0D);
/* 1057 */     this.rotZ = (float)(Math.random() * 360.0D);
/* 1058 */     this.rotSpeedX = 1.4F + (float)Math.random() * 3.4F;
/* 1059 */     this.rotSpeedY = 1.4F + (float)Math.random() * 3.4F;
/* 1060 */     this.rotSpeedZ = 1.4F + (float)Math.random() * 3.4F;
/* 1061 */     this.vx = (float)((Math.random() - 0.5D) * 0.0022D);
/* 1062 */     this.vy = 0.031F + (float)Math.random() * 0.02F;
/* 1063 */     this.vz = (float)((Math.random() - 0.5D) * 0.0022D);
/*      */   }
/*      */   
/*      */   public void update(float dt, long now, class_1309 currentTarget) {
/* 1067 */     float step = dt * 60.0F;
/* 1068 */     this.rotX += this.rotSpeedX * step;
/* 1069 */     this.rotY += this.rotSpeedY * step;
/* 1070 */     this.rotZ += this.rotSpeedZ * step;
/*      */     
/* 1072 */     if (!this.fading) {
/* 1073 */       this.x += (this.vx * step);
/* 1074 */       this.y += (this.vy * step);
/* 1075 */       this.z += (this.vz * step);
/* 1076 */       this.vx *= 0.992F;
/* 1077 */       this.vz *= 0.992F;
/* 1078 */       this.vy *= 0.989F;
/*      */       
/* 1080 */       if (this.entity != null) {
/* 1081 */         double shoulderHeight = Math.max(2.2D, this.entity.method_17682() * 1.85D);
/* 1082 */         if (this.y >= shoulderHeight) {
/* 1083 */           this.y = shoulderHeight;
/* 1084 */           beginFade(now);
/*      */           
/*      */           return;
/*      */         } 
/*      */       } 
/* 1089 */       boolean targetLost = (currentTarget == null || this.entity == null || !this.entity.method_5805() || this.entity != currentTarget);
/* 1090 */       if (targetLost || now - this.time >= 560L) {
/* 1091 */         beginFade(now);
/*      */       }
/*      */       return;
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean shouldRemove(long now) {
/* 1098 */     return (this.fading && now - this.fadeStartTime >= 320L);
/*      */   }
/*      */   
/*      */   public int getRenderColor(int baseColor, int redColor, float hurtPC, long now) {
/* 1102 */     float alpha = getAlpha(now);
/* 1103 */     if (alpha <= 0.001F) {
/* 1104 */       return 0;
/*      */     }
/* 1106 */     int color = ColorUtils.replAlpha(baseColor, (int)(alpha * 255.0F));
/* 1107 */     int hurt = ColorUtils.replAlpha(redColor, (int)(alpha * 255.0F));
/* 1108 */     return TargetESP.INSTANCE.overCol(color, hurt, hurtPC);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean appendCubeFaces(class_287 faceBuilder, class_4587 ms, class_243 cam, float partialTicks, int color) {
/* 1113 */     float alpha = (color >> 24 & 0xFF) / 255.0F;
/* 1114 */     if (alpha <= 0.001F) return false;
/*      */     
/* 1116 */     class_243 renderPos = getRenderPos(partialTicks);
/* 1117 */     if (renderPos == null) return false;
/*      */ 
/*      */ 
/*      */     
/* 1121 */     float fadeScale = this.fading ? class_3532.method_16439(class_3532.method_15363((float)(System.currentTimeMillis() - this.fadeStartTime) / 320.0F, 0.0F, 1.0F), 1.0F, 0.45F) : 1.0F;
/* 1122 */     float scale = 0.12F * fadeScale;
/*      */     
/* 1124 */     ms.method_22903();
/* 1125 */     ms.method_22904(renderPos.field_1352 - cam.field_1352, renderPos.field_1351 - cam.field_1351, renderPos.field_1350 - cam.field_1350);
/* 1126 */     ms.method_22907(class_7833.field_40714.rotationDegrees(this.rotX));
/* 1127 */     ms.method_22907(class_7833.field_40716.rotationDegrees(this.rotY));
/* 1128 */     ms.method_22907(class_7833.field_40718.rotationDegrees(this.rotZ));
/* 1129 */     ms.method_22905(scale, scale, scale);
/* 1130 */     Matrix4f m = ms.method_23760().method_23761();
/*      */     
/* 1132 */     appendFaces(faceBuilder, m, color);
/* 1133 */     ms.method_22909();
/* 1134 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean appendCubeLines(class_287 lineBuilder, class_4587 ms, class_243 cam, float partialTicks, int color) {
/* 1139 */     float alpha = (color >> 24 & 0xFF) / 255.0F;
/* 1140 */     if (alpha <= 0.001F) return false;
/*      */     
/* 1142 */     class_243 renderPos = getRenderPos(partialTicks);
/* 1143 */     if (renderPos == null) return false;
/*      */ 
/*      */ 
/*      */     
/* 1147 */     float fadeScale = this.fading ? class_3532.method_16439(class_3532.method_15363((float)(System.currentTimeMillis() - this.fadeStartTime) / 320.0F, 0.0F, 1.0F), 1.0F, 0.45F) : 1.0F;
/* 1148 */     float scale = 0.12F * fadeScale;
/*      */     
/* 1150 */     ms.method_22903();
/* 1151 */     ms.method_22904(renderPos.field_1352 - cam.field_1352, renderPos.field_1351 - cam.field_1351, renderPos.field_1350 - cam.field_1350);
/* 1152 */     ms.method_22907(class_7833.field_40714.rotationDegrees(this.rotX));
/* 1153 */     ms.method_22907(class_7833.field_40716.rotationDegrees(this.rotY));
/* 1154 */     ms.method_22907(class_7833.field_40718.rotationDegrees(this.rotZ));
/* 1155 */     ms.method_22905(scale, scale, scale);
/* 1156 */     Matrix4f m = ms.method_23760().method_23761();
/*      */     
/* 1158 */     appendEdges(lineBuilder, m, ColorUtils.replAlpha(color, Math.max(1, (int)((color >> 24 & 0xFF) * 0.7F))));
/* 1159 */     ms.method_22909();
/* 1160 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean appendBloom(class_287 builder, class_4587 ms, class_243 camPos, float camYaw, float camPitch, float partialTicks, int colorInt, long now) {
/* 1165 */     float alpha = getAlpha(now);
/* 1166 */     if (alpha <= 0.001F) return false;
/*      */     
/* 1168 */     class_243 renderPos = getRenderPos(partialTicks);
/* 1169 */     if (renderPos == null) return false;
/*      */ 
/*      */ 
/*      */     
/* 1173 */     float fadeScale = this.fading ? class_3532.method_16439(class_3532.method_15363((float)(now - this.fadeStartTime) / 320.0F, 0.0F, 1.0F), 1.0F, 0.55F) : 1.0F;
/* 1174 */     float glowScale = 0.95F * fadeScale;
/* 1175 */     int ai = (int)(alpha * 0.15F * 255.0F);
/* 1176 */     if (ai <= 0) return false;
/*      */     
/* 1178 */     int r = colorInt >> 16 & 0xFF;
/* 1179 */     int g = colorInt >> 8 & 0xFF;
/* 1180 */     int b = colorInt & 0xFF;
/*      */     
/* 1182 */     ms.method_22903();
/* 1183 */     ms.method_22904(renderPos.field_1352 - camPos.field_1352, renderPos.field_1351 - camPos.field_1351, renderPos.field_1350 - camPos.field_1350);
/* 1184 */     ms.method_22907(class_7833.field_40716.rotationDegrees(-camYaw));
/* 1185 */     ms.method_22907(class_7833.field_40714.rotationDegrees(camPitch));
/* 1186 */     ms.method_22905(glowScale, glowScale, glowScale);
/* 1187 */     Matrix4f m = ms.method_23760().method_23761();
/*      */     
/* 1189 */     builder.method_22918(m, -0.5F, 0.5F, 0.0F).method_22913(0.0F, 1.0F).method_1336(r, g, b, ai);
/* 1190 */     builder.method_22918(m, 0.5F, 0.5F, 0.0F).method_22913(1.0F, 1.0F).method_1336(r, g, b, ai);
/* 1191 */     builder.method_22918(m, 0.5F, -0.5F, 0.0F).method_22913(1.0F, 0.0F).method_1336(r, g, b, ai);
/* 1192 */     builder.method_22918(m, -0.5F, -0.5F, 0.0F).method_22913(0.0F, 0.0F).method_1336(r, g, b, ai);
/* 1193 */     ms.method_22909();
/* 1194 */     return true;
/*      */   }
/*      */   
/*      */   private void beginFade(long now) {
/* 1198 */     if (this.fading)
/* 1199 */       return;  class_243 renderPos = getRenderPos(1.0F);
/* 1200 */     if (renderPos != null) {
/* 1201 */       this.worldX = renderPos.field_1352;
/* 1202 */       this.worldY = renderPos.field_1351;
/* 1203 */       this.worldZ = renderPos.field_1350;
/*      */     } 
/* 1205 */     this.fadeStartTime = now;
/* 1206 */     this.fading = true;
/* 1207 */     this.entity = null;
/*      */   }
/*      */   
/*      */   private float getAlpha(long now) {
/* 1211 */     if (!this.fading) {
/* 1212 */       float fadeIn = class_3532.method_15363((float)(now - this.time) / 140.0F, 0.0F, 1.0F);
/* 1213 */       float preFade = 1.0F - class_3532.method_15363((float)(now - this.time - 440L) / 120.0F, 0.0F, 0.35F);
/* 1214 */       return fadeIn * preFade;
/*      */     } 
/* 1216 */     return 1.0F - class_3532.method_15363((float)(now - this.fadeStartTime) / 320.0F, 0.0F, 1.0F);
/*      */   }
/*      */   
/*      */   private class_243 getRenderPos(float partialTicks) {
/* 1220 */     if (this.fading || this.entity == null) {
/* 1221 */       return new class_243(this.worldX, this.worldY, this.worldZ);
/*      */     }
/* 1223 */     return new class_243(
/* 1224 */         class_3532.method_16436(partialTicks, this.entity.field_6038, this.entity.method_23317()) + this.x, 
/* 1225 */         class_3532.method_16436(partialTicks, this.entity.field_5971, this.entity.method_23318()) + this.y, 
/* 1226 */         class_3532.method_16436(partialTicks, this.entity.field_5989, this.entity.method_23321()) + this.z);
/*      */   }
/*      */ 
/*      */   
/*      */   private void appendFaces(class_287 fb, Matrix4f m, int color) {
/* 1231 */     float min = -0.5F, max = 0.5F;
/* 1232 */     int fillColor = ColorUtils.replAlpha(color, Math.max(1, (int)((color >> 24 & 0xFF) * 0.16F)));
/* 1233 */     addFace(fb, m, min, min, min, max, max, max, fillColor);
/*      */   }
/*      */   
/*      */   private void appendEdges(class_287 buf, Matrix4f m, int color) {
/* 1237 */     for (byte[] edge : TargetESP.CUBE_EDGES) {
/* 1238 */       buf.method_22918(m, edge[0] * 0.5F, edge[1] * 0.5F, edge[2] * 0.5F).method_39415(color);
/* 1239 */       buf.method_22918(m, edge[3] * 0.5F, edge[4] * 0.5F, edge[5] * 0.5F).method_39415(color);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void addFace(class_287 buf, Matrix4f m, float x1, float y1, float z1, float x2, float y2, float z2, int color) {
/* 1246 */     buf.method_22918(m, x1, y1, z1).method_39415(color);
/* 1247 */     buf.method_22918(m, x2, y1, z1).method_39415(color);
/* 1248 */     buf.method_22918(m, x2, y1, z2).method_39415(color);
/* 1249 */     buf.method_22918(m, x1, y1, z2).method_39415(color);
/*      */     
/* 1251 */     buf.method_22918(m, x1, y2, z1).method_39415(color);
/* 1252 */     buf.method_22918(m, x1, y2, z2).method_39415(color);
/* 1253 */     buf.method_22918(m, x2, y2, z2).method_39415(color);
/* 1254 */     buf.method_22918(m, x2, y2, z1).method_39415(color);
/*      */     
/* 1256 */     buf.method_22918(m, x1, y1, z1).method_39415(color);
/* 1257 */     buf.method_22918(m, x1, y2, z1).method_39415(color);
/* 1258 */     buf.method_22918(m, x2, y2, z1).method_39415(color);
/* 1259 */     buf.method_22918(m, x2, y1, z1).method_39415(color);
/*      */     
/* 1261 */     buf.method_22918(m, x1, y1, z2).method_39415(color);
/* 1262 */     buf.method_22918(m, x2, y1, z2).method_39415(color);
/* 1263 */     buf.method_22918(m, x2, y2, z2).method_39415(color);
/* 1264 */     buf.method_22918(m, x1, y2, z2).method_39415(color);
/*      */     
/* 1266 */     buf.method_22918(m, x1, y1, z1).method_39415(color);
/* 1267 */     buf.method_22918(m, x1, y1, z2).method_39415(color);
/* 1268 */     buf.method_22918(m, x1, y2, z2).method_39415(color);
/* 1269 */     buf.method_22918(m, x1, y2, z1).method_39415(color);
/*      */     
/* 1271 */     buf.method_22918(m, x2, y1, z1).method_39415(color);
/* 1272 */     buf.method_22918(m, x2, y2, z1).method_39415(color);
/* 1273 */     buf.method_22918(m, x2, y2, z2).method_39415(color);
/* 1274 */     buf.method_22918(m, x2, y1, z2).method_39415(color);
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\CubeParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */