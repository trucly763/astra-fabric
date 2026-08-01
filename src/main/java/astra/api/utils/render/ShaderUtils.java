/*    */ package shame.astra.api.utils.render;
/*    */ import lombok.Generated;
/*    */ import net.minecraft.class_10156;
/*    */ import net.minecraft.class_290;
/*    */ import net.minecraft.class_293;
/*    */ import net.minecraft.class_2960;
/*    */ import shame.astra.api.QClient;
/*    */ 
/*    */ public final class ShaderUtils implements QClient {
/*    */   @Generated
/*    */   private ShaderUtils() {
/* 12 */     throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
/*    */   }
/* 14 */   public static final class_10156 roundedRect = register("rect", "rounded_rect", class_290.field_1576);
/* 15 */   public static final class_10156 roundedRectOutline = register("rect", "rounded_rect_outline", class_290.field_1576);
/* 16 */   public static final class_10156 ringArc = register("ring_arc", "ring_arc", class_290.field_1576);
/* 17 */   public static final class_10156 roundedTexture = register("texture", "texture_rect", class_290.field_1575);
/* 18 */   public static final class_10156 liquidGlass = register("liquidglass", "liquid", class_290.field_1575);
/* 19 */   public static final class_10156 kawaseDown = register("kawase_down", "kawase_down", class_290.field_1575);
/* 20 */   public static final class_10156 kawaseUp = register("kawase_up", "kawase_up", class_290.field_1575);
/* 21 */   public static final class_10156 gradientRect = register("gradient_rect", "gradient", class_290.field_1575);
/* 22 */   public static final class_10156 shadowRect = register("shadow_rect", "shadow", class_290.field_1576);
/* 23 */   public static final class_10156 shadow6Rect = register("shadow6", "shadow", class_290.field_1576);
/* 24 */   public static final class_10156 fontsMsdf = register("fonts", "fonts", class_290.field_1575);
/* 25 */   public static final class_10156 face = register("face", "face", class_290.field_1575);
/* 26 */   public static final class_10156 gradient6Rect = register("gradient6", "gradient", class_290.field_1576);
/* 27 */   public static final class_10156 sonar = register("sonar", "sonar", class_290.field_1576);
/* 28 */   public static final class_10156 scanEffect = register("sonar", "scan_effect", class_290.field_1585);
/* 29 */   public static final class_10156 blockOverlay = register("blockoverlay", "block_overlay", class_290.field_1575);
/* 30 */   public static final class_10156 chamsFill = register("chams", "chams_fill", class_290.field_1575);
/* 31 */   public static final class_10156 shaderHandsMaskDiff = register("hands", "hands_mask_diff", class_290.field_1575);
/* 32 */   public static final class_10156 shaderHandsOverlay = register("hands", "hands_overlay", class_290.field_1575);
/* 33 */   public static final class_10156 shaderHandsGlow = register("hands", "hands_glow", class_290.field_1575);
/* 34 */   public static final class_10156 shaderHandsKawaseDown = register("hands", "hands_kawase_down", class_290.field_1575);
/* 35 */   public static final class_10156 shaderHandsKawaseUp = register("hands", "hands_kawase_up", class_290.field_1575);
/* 36 */   public static final class_10156 shaderEspGlow = register("shaderesp", "glow", class_290.field_1575);
/* 37 */   public static final class_10156 shaderEspFill = register("shaderesp", "fill", class_290.field_1575);
/*    */   
/*    */   private static class_10156 register(String shaderNamePackage, String shaderName, class_293 vertexFormat) {
/* 40 */     return new class_10156(class_2960.method_60655("astra", "core/" + shaderNamePackage + "/" + shaderName), vertexFormat, class_10149.field_53930);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\render\ShaderUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */