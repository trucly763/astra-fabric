/*     */ package shame.astra.api.utils.render.fonts.msdf;
/*     */ 
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParser;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.HashMap;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.class_1044;
/*     */ import net.minecraft.class_2960;
/*     */ import net.minecraft.class_4588;
/*     */ import org.joml.Matrix4f;
/*     */ import shame.astra.api.QClient;
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MsdfFont
/*     */   implements QClient
/*     */ {
/*     */   private final String name;
/*     */   private final class_1044 texture;
/*     */   private final float atlasWidth;
/*     */   private final float atlasHeight;
/*     */   private final float range;
/*     */   private final float lineHeight;
/*     */   private final float ascender;
/*     */   private final float descender;
/*     */   private final HashMap<Integer, MsdfGlyph> glyphs;
/*     */   private boolean filtered = false;
/*     */   
/*     */   private MsdfFont(String name, class_1044 texture, float atlasWidth, float atlasHeight, float range, float lineHeight, float ascender, float descender, HashMap<Integer, MsdfGlyph> glyphs) {
/*  35 */     this.name = name;
/*  36 */     this.texture = texture;
/*  37 */     this.atlasWidth = atlasWidth;
/*  38 */     this.atlasHeight = atlasHeight;
/*  39 */     this.range = range;
/*  40 */     this.lineHeight = lineHeight;
/*  41 */     this.ascender = ascender;
/*  42 */     this.descender = descender;
/*  43 */     this.glyphs = glyphs;
/*     */   }
/*     */   
/*     */   public void setFiltered() {
/*  47 */     if (!this.filtered) {
/*  48 */       this.texture.method_4527(true, false);
/*  49 */       this.filtered = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getTextureId() {
/*  54 */     return this.texture.method_4624();
/*     */   }
/*     */   
/*     */   public float getAtlasWidth() {
/*  58 */     return this.atlasWidth;
/*     */   }
/*     */   
/*     */   public float getAtlasHeight() {
/*  62 */     return this.atlasHeight;
/*     */   }
/*     */   
/*     */   public float getRange() {
/*  66 */     return this.range;
/*     */   }
/*     */   
/*     */   public float getLineHeight() {
/*  70 */     return this.lineHeight;
/*     */   }
/*     */   
/*     */   public float getBaselineHeight() {
/*  74 */     return this.lineHeight + this.descender;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  78 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void applyGlyphs(Matrix4f matrix, class_4588 consumer, float size, String text, float thickness, float x, float y, float z, int red, int green, int blue, int alpha) {
/*  84 */     text = replaceSymbols(text);
/*     */     
/*  86 */     for (int i = 0; i < text.length(); i++) {
/*  87 */       char c = text.charAt(i);
/*     */       
/*  89 */       if (c == '§' && i + 1 < text.length()) {
/*  90 */         i++;
/*     */       }
/*     */       else {
/*     */         
/*  94 */         MsdfGlyph glyph = this.glyphs.get(Integer.valueOf(c));
/*  95 */         if (glyph != null)
/*  96 */           x += glyph.apply(matrix, consumer, size, x, y, z, red, green, blue, alpha) + thickness; 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public float getWidth(String text, float size) {
/* 102 */     text = replaceSymbols(text);
/* 103 */     float width = 0.0F;
/*     */     
/* 105 */     for (int i = 0; i < text.length(); i++) {
/* 106 */       char c = text.charAt(i);
/*     */       
/* 108 */       if (c == '§' && i + 1 < text.length()) {
/* 109 */         i++;
/*     */       }
/*     */       else {
/*     */         
/* 113 */         MsdfGlyph glyph = this.glyphs.get(Integer.valueOf(c));
/* 114 */         if (glyph != null) {
/* 115 */           width += glyph.getWidth(size);
/*     */         }
/*     */       } 
/*     */     } 
/* 119 */     return width;
/*     */   }
/*     */   
/*     */   private static String replaceSymbols(String text) {
/* 123 */     if (text == null) return ""; 
/* 124 */     return text
/* 125 */       .replace("ᴀ", "A").replace("ʙ", "B").replace("ᴄ", "C")
/* 126 */       .replace("ᴅ", "D").replace("ᴇ", "E").replace("ғ", "F")
/* 127 */       .replace("ɢ", "G").replace("ʜ", "H").replace("ɪ", "I")
/* 128 */       .replace("ᴊ", "J").replace("ᴋ", "K").replace("ʟ", "L")
/* 129 */       .replace("ᴍ", "M").replace("ɴ", "N").replace("ᴏ", "O")
/* 130 */       .replace("ᴘ", "P").replace("ǫ", "Q").replace("ʀ", "R")
/* 131 */       .replace("ꜱ", "S").replace("ᴛ", "T").replace("ᴜ", "U")
/* 132 */       .replace("ᴠ", "V").replace("ᴡ", "W").replace("ʏ", "Y")
/* 133 */       .replace("ᴢ", "Z").replace("ꜰ", "F");
/*     */   }
/*     */   
/*     */   private static String readResource(class_2960 identifier) {
/*     */     try {
/* 138 */       InputStream inputStream = mc.method_1478().open(identifier);
/* 139 */       BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
/* 140 */       String result = reader.lines().collect(Collectors.joining("\n"));
/* 141 */       reader.close();
/* 142 */       inputStream.close();
/* 143 */       return result;
/* 144 */     } catch (Exception e) {
/* 145 */       throw new RuntimeException("Failed to read resource: " + String.valueOf(identifier), e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Builder builder() {
/* 150 */     return new Builder();
/*     */   }
/*     */   
/*     */   public static class Builder {
/* 154 */     private String name = "?";
/*     */     private class_2960 dataIdentifier;
/*     */     private class_2960 atlasIdentifier;
/*     */     
/*     */     public Builder name(String name) {
/* 159 */       this.name = name;
/* 160 */       return this;
/*     */     }
/*     */     
/*     */     public Builder data(String dataFileName) {
/* 164 */       this.dataIdentifier = class_2960.method_60655("astra", "fonts/msdf/" + dataFileName + "/font.json");
/* 165 */       return this;
/*     */     }
/*     */     
/*     */     public Builder atlas(String atlasFileName) {
/* 169 */       this.atlasIdentifier = class_2960.method_60655("astra", "fonts/msdf/" + atlasFileName + "/font.png");
/* 170 */       return this;
/*     */     }
/*     */     
/*     */     public MsdfFont build() {
/* 174 */       String json = MsdfFont.readResource(this.dataIdentifier);
/* 175 */       JsonObject root = JsonParser.parseString(json).getAsJsonObject();
/*     */       
/* 177 */       JsonObject atlasObj = root.getAsJsonObject("atlas");
/* 178 */       float atlasWidth = atlasObj.get("width").getAsFloat();
/* 179 */       float atlasHeight = atlasObj.get("height").getAsFloat();
/* 180 */       float range = atlasObj.get("distanceRange").getAsFloat();
/*     */       
/* 182 */       JsonObject metricsObj = root.getAsJsonObject("metrics");
/* 183 */       float lineHeight = metricsObj.get("lineHeight").getAsFloat();
/* 184 */       float ascender = metricsObj.get("ascender").getAsFloat();
/* 185 */       float descender = metricsObj.get("descender").getAsFloat();
/*     */       
/* 187 */       HashMap<Integer, MsdfGlyph> glyphs = new HashMap<>();
/* 188 */       JsonArray glyphsArray = root.getAsJsonArray("glyphs");
/*     */       
/* 190 */       for (JsonElement element : glyphsArray) {
/* 191 */         JsonObject glyphObj = element.getAsJsonObject();
/*     */         
/* 193 */         int unicode = glyphObj.get("unicode").getAsInt();
/* 194 */         float advance = glyphObj.get("advance").getAsFloat();
/*     */         
/* 196 */         float planeLeft = 0.0F, planeTop = 0.0F, planeRight = 0.0F, planeBottom = 0.0F;
/* 197 */         if (glyphObj.has("planeBounds") && !glyphObj.get("planeBounds").isJsonNull()) {
/* 198 */           JsonObject plane = glyphObj.getAsJsonObject("planeBounds");
/* 199 */           planeLeft = plane.get("left").getAsFloat();
/* 200 */           planeTop = plane.get("top").getAsFloat();
/* 201 */           planeRight = plane.get("right").getAsFloat();
/* 202 */           planeBottom = plane.get("bottom").getAsFloat();
/*     */         } 
/*     */         
/* 205 */         float atlasLeft = 0.0F, atlasTop = 0.0F, atlasRight = 0.0F, atlasBottom = 0.0F;
/* 206 */         if (glyphObj.has("atlasBounds") && !glyphObj.get("atlasBounds").isJsonNull()) {
/* 207 */           JsonObject atlas = glyphObj.getAsJsonObject("atlasBounds");
/* 208 */           atlasLeft = atlas.get("left").getAsFloat();
/* 209 */           atlasTop = atlas.get("top").getAsFloat();
/* 210 */           atlasRight = atlas.get("right").getAsFloat();
/* 211 */           atlasBottom = atlas.get("bottom").getAsFloat();
/*     */         } 
/*     */         
/* 214 */         MsdfGlyph glyph = new MsdfGlyph(unicode, advance, planeLeft, planeTop, planeRight, planeBottom, atlasLeft, atlasTop, atlasRight, atlasBottom, atlasWidth, atlasHeight);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 221 */         glyphs.put(Integer.valueOf(unicode), glyph);
/*     */       } 
/*     */       
/* 224 */       class_1044 texture = QClient.mc.method_1531().method_4619(this.atlasIdentifier);
/*     */       
/* 226 */       return new MsdfFont(this.name, texture, atlasWidth, atlasHeight, range, lineHeight, ascender, descender, glyphs);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\render\fonts\msdf\MsdfFont.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */