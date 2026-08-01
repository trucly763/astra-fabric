/*     */ package shame.astra.mixin;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import net.minecraft.class_2960;
/*     */ import org.spongepowered.asm.mixin.Mixin;
/*     */ import org.spongepowered.asm.mixin.injection.At;
/*     */ import org.spongepowered.asm.mixin.injection.Inject;
/*     */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*     */ 
/*     */ @Mixin({class_2960.class})
/*     */ public abstract class IdentifierMixin
/*     */ {
/*     */   private static final String SAFE_JOIN_PATH = "invalid_join_id";
/*     */   
/*     */   @Inject(method = {"method_45137"}, at = {@At("HEAD")}, cancellable = true)
/*     */   private static void astra$sanitizeJoinPath(String namespace, String path, CallbackInfoReturnable<String> cir) {
/*  17 */     if (!shouldSanitizePath(path)) {
/*     */       return;
/*     */     }
/*     */     
/*  21 */     cir.setReturnValue(sanitizePath(path));
/*     */   }
/*     */   
/*     */   @Inject(method = {"method_45135"}, at = {@At("HEAD")}, cancellable = true)
/*     */   private static void astra$sanitizeJoinNamespace(String namespace, String path, CallbackInfoReturnable<String> cir) {
/*  26 */     if (!shouldSanitizeNamespace(namespace)) {
/*     */       return;
/*     */     }
/*     */     
/*  30 */     cir.setReturnValue(sanitizeNamespace(namespace));
/*     */   }
/*     */   
/*     */   private static boolean shouldSanitizeNamespace(String value) {
/*  34 */     if (value == null || value.isEmpty()) {
/*  35 */       return true;
/*     */     }
/*     */     
/*  38 */     for (int i = 0; i < value.length(); i++) {
/*  39 */       char c = value.charAt(i);
/*  40 */       if (!isAllowedNamespaceChar(c)) {
/*  41 */         return true;
/*     */       }
/*     */     } 
/*     */     
/*  45 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean shouldSanitizePath(String value) {
/*  49 */     if (value == null || value.isEmpty()) {
/*  50 */       return true;
/*     */     }
/*     */     
/*  53 */     for (int i = 0; i < value.length(); i++) {
/*  54 */       char c = value.charAt(i);
/*  55 */       if (!isAllowedPathChar(c)) {
/*  56 */         return true;
/*     */       }
/*     */     } 
/*     */     
/*  60 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean isAllowedNamespaceChar(char c) {
/*  64 */     return ((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '_' || c == '.' || c == '-');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isAllowedPathChar(char c) {
/*  72 */     return (isAllowedNamespaceChar(c) || c == '/');
/*     */   }
/*     */   
/*     */   private static String sanitizeNamespace(String namespace) {
/*  76 */     StringBuilder builder = new StringBuilder(namespace.length());
/*  77 */     String lower = namespace.toLowerCase(Locale.ROOT);
/*     */     
/*  79 */     for (int i = 0; i < lower.length(); i++) {
/*  80 */       char c = lower.charAt(i);
/*  81 */       if (isAllowedNamespaceChar(c)) {
/*  82 */         builder.append(c);
/*     */       } else {
/*  84 */         builder.append('_');
/*     */       } 
/*     */     } 
/*     */     
/*  88 */     String sanitized = builder.toString();
/*  89 */     return sanitized.isBlank() ? "minecraft" : sanitized;
/*     */   }
/*     */   
/*     */   private static String sanitizePath(String path) {
/*  93 */     StringBuilder builder = new StringBuilder(path.length());
/*  94 */     String lower = path.toLowerCase(Locale.ROOT);
/*     */     
/*  96 */     for (int i = 0; i < lower.length(); i++) {
/*  97 */       char c = lower.charAt(i);
/*  98 */       if (isAllowedPathChar(c)) {
/*  99 */         builder.append(c);
/*     */       } else {
/* 101 */         builder.append('_');
/*     */       } 
/*     */     } 
/*     */     
/* 105 */     String sanitized = builder.toString();
/* 106 */     return sanitized.isBlank() ? "invalid_join_id" : sanitized;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\mixin\IdentifierMixin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */