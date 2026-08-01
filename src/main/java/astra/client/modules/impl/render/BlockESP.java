/*     */ package shame.astra.client.modules.impl.render;
/*     */ 
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import net.minecraft.class_10142;
/*     */ import net.minecraft.class_1923;
/*     */ import net.minecraft.class_2338;
/*     */ import net.minecraft.class_2374;
/*     */ import net.minecraft.class_2382;
/*     */ import net.minecraft.class_243;
/*     */ import net.minecraft.class_2680;
/*     */ import net.minecraft.class_2818;
/*     */ import net.minecraft.class_286;
/*     */ import net.minecraft.class_287;
/*     */ import net.minecraft.class_289;
/*     */ import net.minecraft.class_290;
/*     */ import net.minecraft.class_293;
/*     */ import net.minecraft.class_4587;
/*     */ import net.minecraft.class_7923;
/*     */ import org.joml.Matrix4f;
/*     */ import shame.astra.api.events.EventLink;
/*     */ import shame.astra.api.events.implement.Event3DRender;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*     */ 
/*     */ public class BlockESP extends Module {
/*  33 */   public static BlockESP INSTANCE = new BlockESP();
/*     */   
/*     */   private static final float BOX_LINE_WIDTH = 2.0F;
/*     */   
/*     */   private static final float FILL_ALPHA = 0.18F;
/*     */   private static final float GREEN_R = 0.1F;
/*     */   private static final float GREEN_G = 1.0F;
/*     */   private static final float GREEN_B = 0.15F;
/*     */   private static final long SCAN_INTERVAL_MS = 50L;
/*     */   private static final int MAX_CHUNKS_PER_PASS = 2;
/*  43 */   private final FloatSetting distance = new FloatSetting("Дистанция", 60.0F, 10.0F, 120.0F, 1.0F);
/*     */   
/*  45 */   private final Set<String> trackedBlocks = ConcurrentHashMap.newKeySet();
/*  46 */   private final Map<class_2338, String> foundBlocks = new ConcurrentHashMap<>();
/*  47 */   private final Set<class_1923> scannedChunks = ConcurrentHashMap.newKeySet();
/*     */   
/*     */   private class_1923 lastPlayerChunk;
/*  50 */   private int lastScanRadius = -1;
/*     */   private long lastScanTime;
/*     */   
/*     */   public BlockESP() {
/*  54 */     super("BlockESP", "Показывает выбранные блоки через стену", Module.ModuleCategory.RENDER);
/*  55 */     addSettings(new Setting[] { (Setting)this.distance });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  60 */     resetScanState();
/*  61 */     super.onEnable();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  66 */     resetScanState();
/*  67 */     super.onDisable();
/*     */   }
/*     */   
/*     */   @EventLink(priority = 100)
/*     */   public void onRender3D(Event3DRender event) {
/*  72 */     if (mc.field_1687 == null || mc.field_1724 == null || this.trackedBlocks.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/*  76 */     int scanRadius = getDistance();
/*  77 */     class_1923 currentChunk = new class_1923(mc.field_1724.method_24515());
/*     */     
/*  79 */     if (scanRadius != this.lastScanRadius) {
/*  80 */       resetScanState();
/*  81 */       this.lastScanRadius = scanRadius;
/*     */     } 
/*     */     
/*  84 */     if (this.lastPlayerChunk == null || !this.lastPlayerChunk.equals(currentChunk)) {
/*  85 */       this.scannedChunks.clear();
/*  86 */       this.lastPlayerChunk = currentChunk;
/*     */     } 
/*     */     
/*  89 */     long now = System.currentTimeMillis();
/*  90 */     if (now - this.lastScanTime >= 50L) {
/*  91 */       scanNearbyBlocks(scanRadius);
/*  92 */       this.lastScanTime = now;
/*     */     } 
/*     */     
/*  95 */     cleanupInvalidAndDistantBlocks(mc.field_1724.method_19538(), scanRadius);
/*  96 */     renderFoundBlocks(event.getMatrices());
/*     */   }
/*     */   
/*     */   private void scanNearbyBlocks(int scanRadius) {
/* 100 */     if (mc.field_1687 == null || mc.field_1724 == null) {
/*     */       return;
/*     */     }
/*     */     
/* 104 */     class_2338 playerPos = mc.field_1724.method_24515();
/* 105 */     int playerChunkX = playerPos.method_10263() >> 4;
/* 106 */     int playerChunkZ = playerPos.method_10260() >> 4;
/* 107 */     int chunkRange = (scanRadius >> 4) + 2;
/*     */     
/* 109 */     List<class_1923> candidates = new ArrayList<>();
/* 110 */     for (int cx = -chunkRange; cx <= chunkRange; cx++) {
/* 111 */       for (int cz = -chunkRange; cz <= chunkRange; cz++) {
/* 112 */         class_1923 chunkPos = new class_1923(playerChunkX + cx, playerChunkZ + cz);
/* 113 */         if (!this.scannedChunks.contains(chunkPos)) {
/* 114 */           candidates.add(chunkPos);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 119 */     candidates.sort((a, b) -> {
/*     */           long da = chunkDistanceSq(a, playerChunkX, playerChunkZ);
/*     */           
/*     */           long db = chunkDistanceSq(b, playerChunkX, playerChunkZ);
/*     */           return Long.compare(da, db);
/*     */         });
/* 125 */     int scannedThisPass = 0;
/* 126 */     for (class_1923 chunkPos : candidates) {
/* 127 */       if (scannedThisPass >= 2) {
/*     */         break;
/*     */       }
/*     */       
/* 131 */       class_2818 chunk = mc.field_1687.method_8497(chunkPos.field_9181, chunkPos.field_9180);
/* 132 */       if (chunk == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 136 */       scanChunk(chunk, playerPos, scanRadius);
/* 137 */       this.scannedChunks.add(chunkPos);
/* 138 */       scannedThisPass++;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void scanChunk(class_2818 chunk, class_2338 playerPos, int scanRadius) {
/* 143 */     int minX = chunk.method_12004().method_8326();
/* 144 */     int minZ = chunk.method_12004().method_8328();
/* 145 */     int maxX = minX + 15;
/* 146 */     int maxZ = minZ + 15;
/*     */     
/* 148 */     int minY = Math.max(mc.field_1687.method_31607(), playerPos.method_10264() - scanRadius);
/* 149 */     int maxY = Math.min(mc.field_1687.method_31600(), playerPos.method_10264() + scanRadius);
/* 150 */     int radiusSq = scanRadius * scanRadius;
/*     */     
/* 152 */     class_2338.class_2339 mutable = new class_2338.class_2339();
/*     */     
/* 154 */     for (int x = minX; x <= maxX; x++) {
/* 155 */       for (int z = minZ; z <= maxZ; z++) {
/* 156 */         for (int y = minY; y <= maxY; y++) {
/* 157 */           mutable.method_10103(x, y, z);
/*     */           
/* 159 */           if (mutable.method_10262((class_2382)playerPos) <= radiusSq) {
/*     */ 
/*     */ 
/*     */             
/* 163 */             class_2680 state = chunk.method_8320((class_2338)mutable);
/* 164 */             if (!state.method_26215()) {
/*     */ 
/*     */ 
/*     */               
/* 168 */               String blockName = class_7923.field_41175.method_10221(state.method_26204()).method_12832().toLowerCase();
/* 169 */               if (this.trackedBlocks.contains(blockName))
/* 170 */                 this.foundBlocks.put(mutable.method_10062(), blockName); 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private void cleanupInvalidAndDistantBlocks(class_243 playerPos, int renderDistance) {
/* 178 */     if (mc.field_1687 == null) {
/* 179 */       this.foundBlocks.clear();
/*     */       
/*     */       return;
/*     */     } 
/* 183 */     int renderDistanceSq = renderDistance * renderDistance;
/* 184 */     this.foundBlocks.entrySet().removeIf(entry -> {
/*     */           class_2338 pos = (class_2338)entry.getKey();
/*     */           class_2680 currentState = mc.field_1687.method_8320(pos);
/*     */           if (currentState.method_26215()) {
/*     */             return true;
/*     */           }
/*     */           String currentBlockName = class_7923.field_41175.method_10221(currentState.method_26204()).method_12832().toLowerCase();
/*     */           return !this.trackedBlocks.contains(currentBlockName) ? true : ((pos.method_19770((class_2374)playerPos) > renderDistanceSq));
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void renderFoundBlocks(class_4587 matrices) {
/* 201 */     if (this.foundBlocks.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 205 */     class_243 camera = mc.field_1773.method_19418().method_19326();
/*     */     
/* 207 */     matrices.method_22903();
/* 208 */     matrices.method_22904(-camera.field_1352, -camera.field_1351, -camera.field_1350);
/* 209 */     Matrix4f matrix = matrices.method_23760().method_23761();
/*     */     
/* 211 */     RenderSystem.enableBlend();
/* 212 */     RenderSystem.defaultBlendFunc();
/* 213 */     RenderSystem.disableCull();
/* 214 */     RenderSystem.disableDepthTest();
/* 215 */     RenderSystem.depthMask(false);
/* 216 */     RenderSystem.setShader(class_10142.field_53876);
/*     */     
/* 218 */     class_289 tessellator = class_289.method_1348();
/* 219 */     class_287 fillBuffer = tessellator.method_60827(class_293.class_5596.field_27382, class_290.field_1576);
/* 220 */     for (class_2338 pos : this.foundBlocks.keySet()) {
/* 221 */       addFilledBox(fillBuffer, matrix, pos, 0.1F, 1.0F, 0.15F, 0.18F);
/*     */     }
/* 223 */     class_286.method_43433(fillBuffer.method_60800());
/*     */     
/* 225 */     RenderSystem.lineWidth(2.0F);
/* 226 */     class_287 lineBuffer = tessellator.method_60827(class_293.class_5596.field_29344, class_290.field_1576);
/* 227 */     for (class_2338 pos : this.foundBlocks.keySet()) {
/* 228 */       addOutlinedBox(lineBuffer, matrix, pos, 0.1F, 1.0F, 0.15F, 1.0F);
/*     */     }
/* 230 */     class_286.method_43433(lineBuffer.method_60800());
/*     */     
/* 232 */     RenderSystem.enableCull();
/* 233 */     RenderSystem.enableDepthTest();
/* 234 */     RenderSystem.depthMask(true);
/* 235 */     RenderSystem.disableBlend();
/* 236 */     matrices.method_22909();
/*     */   }
/*     */   
/*     */   private void addFilledBox(class_287 buffer, Matrix4f matrix, class_2338 pos, float r, float g, float b, float a) {
/* 240 */     float minX = pos.method_10263();
/* 241 */     float minY = pos.method_10264();
/* 242 */     float minZ = pos.method_10260();
/* 243 */     float maxX = minX + 1.0F;
/* 244 */     float maxY = minY + 1.0F;
/* 245 */     float maxZ = minZ + 1.0F;
/*     */     
/* 247 */     buffer.method_22918(matrix, minX, minY, minZ).method_22915(r, g, b, a);
/* 248 */     buffer.method_22918(matrix, maxX, minY, minZ).method_22915(r, g, b, a);
/* 249 */     buffer.method_22918(matrix, maxX, minY, maxZ).method_22915(r, g, b, a);
/* 250 */     buffer.method_22918(matrix, minX, minY, maxZ).method_22915(r, g, b, a);
/*     */     
/* 252 */     buffer.method_22918(matrix, minX, maxY, minZ).method_22915(r, g, b, a);
/* 253 */     buffer.method_22918(matrix, minX, maxY, maxZ).method_22915(r, g, b, a);
/* 254 */     buffer.method_22918(matrix, maxX, maxY, maxZ).method_22915(r, g, b, a);
/* 255 */     buffer.method_22918(matrix, maxX, maxY, minZ).method_22915(r, g, b, a);
/*     */     
/* 257 */     buffer.method_22918(matrix, minX, minY, minZ).method_22915(r, g, b, a);
/* 258 */     buffer.method_22918(matrix, minX, maxY, minZ).method_22915(r, g, b, a);
/* 259 */     buffer.method_22918(matrix, maxX, maxY, minZ).method_22915(r, g, b, a);
/* 260 */     buffer.method_22918(matrix, maxX, minY, minZ).method_22915(r, g, b, a);
/*     */     
/* 262 */     buffer.method_22918(matrix, minX, minY, maxZ).method_22915(r, g, b, a);
/* 263 */     buffer.method_22918(matrix, maxX, minY, maxZ).method_22915(r, g, b, a);
/* 264 */     buffer.method_22918(matrix, maxX, maxY, maxZ).method_22915(r, g, b, a);
/* 265 */     buffer.method_22918(matrix, minX, maxY, maxZ).method_22915(r, g, b, a);
/*     */     
/* 267 */     buffer.method_22918(matrix, minX, minY, minZ).method_22915(r, g, b, a);
/* 268 */     buffer.method_22918(matrix, minX, minY, maxZ).method_22915(r, g, b, a);
/* 269 */     buffer.method_22918(matrix, minX, maxY, maxZ).method_22915(r, g, b, a);
/* 270 */     buffer.method_22918(matrix, minX, maxY, minZ).method_22915(r, g, b, a);
/*     */     
/* 272 */     buffer.method_22918(matrix, maxX, minY, minZ).method_22915(r, g, b, a);
/* 273 */     buffer.method_22918(matrix, maxX, maxY, minZ).method_22915(r, g, b, a);
/* 274 */     buffer.method_22918(matrix, maxX, maxY, maxZ).method_22915(r, g, b, a);
/* 275 */     buffer.method_22918(matrix, maxX, minY, maxZ).method_22915(r, g, b, a);
/*     */   }
/*     */   
/*     */   private void addOutlinedBox(class_287 buffer, Matrix4f matrix, class_2338 pos, float r, float g, float b, float a) {
/* 279 */     float minX = pos.method_10263();
/* 280 */     float minY = pos.method_10264();
/* 281 */     float minZ = pos.method_10260();
/* 282 */     float maxX = minX + 1.0F;
/* 283 */     float maxY = minY + 1.0F;
/* 284 */     float maxZ = minZ + 1.0F;
/*     */     
/* 286 */     buffer.method_22918(matrix, minX, minY, minZ).method_22915(r, g, b, a);
/* 287 */     buffer.method_22918(matrix, maxX, minY, minZ).method_22915(r, g, b, a);
/* 288 */     buffer.method_22918(matrix, maxX, minY, minZ).method_22915(r, g, b, a);
/* 289 */     buffer.method_22918(matrix, maxX, minY, maxZ).method_22915(r, g, b, a);
/* 290 */     buffer.method_22918(matrix, maxX, minY, maxZ).method_22915(r, g, b, a);
/* 291 */     buffer.method_22918(matrix, minX, minY, maxZ).method_22915(r, g, b, a);
/* 292 */     buffer.method_22918(matrix, minX, minY, maxZ).method_22915(r, g, b, a);
/* 293 */     buffer.method_22918(matrix, minX, minY, minZ).method_22915(r, g, b, a);
/*     */     
/* 295 */     buffer.method_22918(matrix, minX, maxY, minZ).method_22915(r, g, b, a);
/* 296 */     buffer.method_22918(matrix, maxX, maxY, minZ).method_22915(r, g, b, a);
/* 297 */     buffer.method_22918(matrix, maxX, maxY, minZ).method_22915(r, g, b, a);
/* 298 */     buffer.method_22918(matrix, maxX, maxY, maxZ).method_22915(r, g, b, a);
/* 299 */     buffer.method_22918(matrix, maxX, maxY, maxZ).method_22915(r, g, b, a);
/* 300 */     buffer.method_22918(matrix, minX, maxY, maxZ).method_22915(r, g, b, a);
/* 301 */     buffer.method_22918(matrix, minX, maxY, maxZ).method_22915(r, g, b, a);
/* 302 */     buffer.method_22918(matrix, minX, maxY, minZ).method_22915(r, g, b, a);
/*     */     
/* 304 */     buffer.method_22918(matrix, minX, minY, minZ).method_22915(r, g, b, a);
/* 305 */     buffer.method_22918(matrix, minX, maxY, minZ).method_22915(r, g, b, a);
/* 306 */     buffer.method_22918(matrix, maxX, minY, minZ).method_22915(r, g, b, a);
/* 307 */     buffer.method_22918(matrix, maxX, maxY, minZ).method_22915(r, g, b, a);
/* 308 */     buffer.method_22918(matrix, maxX, minY, maxZ).method_22915(r, g, b, a);
/* 309 */     buffer.method_22918(matrix, maxX, maxY, maxZ).method_22915(r, g, b, a);
/* 310 */     buffer.method_22918(matrix, minX, minY, maxZ).method_22915(r, g, b, a);
/* 311 */     buffer.method_22918(matrix, minX, maxY, maxZ).method_22915(r, g, b, a);
/*     */   }
/*     */   
/*     */   public void addBlock(String blockName) {
/* 315 */     this.trackedBlocks.add(blockName.toLowerCase());
/* 316 */     this.scannedChunks.clear();
/* 317 */     this.foundBlocks.clear();
/*     */   }
/*     */   
/*     */   public void removeBlock(String blockName) {
/* 321 */     this.trackedBlocks.remove(blockName.toLowerCase());
/* 322 */     this.foundBlocks.entrySet().removeIf(entry -> ((String)entry.getValue()).equalsIgnoreCase(blockName));
/*     */   }
/*     */   
/*     */   public void clearBlocks() {
/* 326 */     this.trackedBlocks.clear();
/* 327 */     resetScanState();
/*     */   }
/*     */   
/*     */   public Set<String> getTrackedBlocks() {
/* 331 */     return new HashSet<>(this.trackedBlocks);
/*     */   }
/*     */   
/*     */   public boolean isTracking(String blockName) {
/* 335 */     return this.trackedBlocks.contains(blockName.toLowerCase());
/*     */   }
/*     */   
/*     */   private int getDistance() {
/* 339 */     return Math.round(this.distance.get());
/*     */   }
/*     */   
/*     */   private long chunkDistanceSq(class_1923 chunkPos, int playerChunkX, int playerChunkZ) {
/* 343 */     long dx = (chunkPos.field_9181 - playerChunkX);
/* 344 */     long dz = (chunkPos.field_9180 - playerChunkZ);
/* 345 */     return dx * dx + dz * dz;
/*     */   }
/*     */   
/*     */   private void resetScanState() {
/* 349 */     this.foundBlocks.clear();
/* 350 */     this.scannedChunks.clear();
/* 351 */     this.lastPlayerChunk = null;
/* 352 */     this.lastScanTime = 0L;
/* 353 */     this.lastScanRadius = -1;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\render\BlockESP.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */