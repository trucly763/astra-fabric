/*     */ package shame.astra.api.utils.render.fonts.msdf;
/*     */ 
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParser;
/*     */ import java.util.HashMap;
/*     */ import net.minecraft.class_1044;
/*     */ import net.minecraft.class_2960;
/*     */ import shame.astra.api.QClient;
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
/*     */ public class Builder
/*     */ {
/* 154 */   private String name = "?";
/*     */   private class_2960 dataIdentifier;
/*     */   private class_2960 atlasIdentifier;
/*     */   
/*     */   public Builder name(String name) {
/* 159 */     this.name = name;
/* 160 */     return this;
/*     */   }
/*     */   
/*     */   public Builder data(String dataFileName) {
/* 164 */     this.dataIdentifier = class_2960.method_60655("astra", "fonts/msdf/" + dataFileName + "/font.json");
/* 165 */     return this;
/*     */   }
/*     */   
/*     */   public Builder atlas(String atlasFileName) {
/* 169 */     this.atlasIdentifier = class_2960.method_60655("astra", "fonts/msdf/" + atlasFileName + "/font.png");
/* 170 */     return this;
/*     */   }
/*     */   
/*     */   public MsdfFont build() {
/* 174 */     String json = MsdfFont.readResource(this.dataIdentifier);
/* 175 */     JsonObject root = JsonParser.parseString(json).getAsJsonObject();
/*     */     
/* 177 */     JsonObject atlasObj = root.getAsJsonObject("atlas");
/* 178 */     float atlasWidth = atlasObj.get("width").getAsFloat();
/* 179 */     float atlasHeight = atlasObj.get("height").getAsFloat();
/* 180 */     float range = atlasObj.get("distanceRange").getAsFloat();
/*     */     
/* 182 */     JsonObject metricsObj = root.getAsJsonObject("metrics");
/* 183 */     float lineHeight = metricsObj.get("lineHeight").getAsFloat();
/* 184 */     float ascender = metricsObj.get("ascender").getAsFloat();
/* 185 */     float descender = metricsObj.get("descender").getAsFloat();
/*     */     
/* 187 */     HashMap<Integer, MsdfGlyph> glyphs = new HashMap<>();
/* 188 */     JsonArray glyphsArray = root.getAsJsonArray("glyphs");
/*     */     
/* 190 */     for (JsonElement element : glyphsArray) {
/* 191 */       JsonObject glyphObj = element.getAsJsonObject();
/*     */       
/* 193 */       int unicode = glyphObj.get("unicode").getAsInt();
/* 194 */       float advance = glyphObj.get("advance").getAsFloat();
/*     */       
/* 196 */       float planeLeft = 0.0F, planeTop = 0.0F, planeRight = 0.0F, planeBottom = 0.0F;
/* 197 */       if (glyphObj.has("planeBounds") && !glyphObj.get("planeBounds").isJsonNull()) {
/* 198 */         JsonObject plane = glyphObj.getAsJsonObject("planeBounds");
/* 199 */         planeLeft = plane.get("left").getAsFloat();
/* 200 */         planeTop = plane.get("top").getAsFloat();
/* 201 */         planeRight = plane.get("right").getAsFloat();
/* 202 */         planeBottom = plane.get("bottom").getAsFloat();
/*     */       } 
/*     */       
/* 205 */       float atlasLeft = 0.0F, atlasTop = 0.0F, atlasRight = 0.0F, atlasBottom = 0.0F;
/* 206 */       if (glyphObj.has("atlasBounds") && !glyphObj.get("atlasBounds").isJsonNull()) {
/* 207 */         JsonObject atlas = glyphObj.getAsJsonObject("atlasBounds");
/* 208 */         atlasLeft = atlas.get("left").getAsFloat();
/* 209 */         atlasTop = atlas.get("top").getAsFloat();
/* 210 */         atlasRight = atlas.get("right").getAsFloat();
/* 211 */         atlasBottom = atlas.get("bottom").getAsFloat();
/*     */       } 
/*     */       
/* 214 */       MsdfGlyph glyph = new MsdfGlyph(unicode, advance, planeLeft, planeTop, planeRight, planeBottom, atlasLeft, atlasTop, atlasRight, atlasBottom, atlasWidth, atlasHeight);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 221 */       glyphs.put(Integer.valueOf(unicode), glyph);
/*     */     } 
/*     */     
/* 224 */     class_1044 texture = QClient.mc.method_1531().method_4619(this.atlasIdentifier);
/*     */     
/* 226 */     return new MsdfFont(this.name, texture, atlasWidth, atlasHeight, range, lineHeight, ascender, descender, glyphs);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\render\fonts\msdf\MsdfFont$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */