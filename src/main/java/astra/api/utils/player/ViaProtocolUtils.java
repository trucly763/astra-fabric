/*     */ package shame.astra.api.utils.player;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ public final class ViaProtocolUtils {
/*     */   private static final int MC_1_19_PROTOCOL = 759;
/*     */   private static final long CACHE_TIME_MS = 1500L;
/*  11 */   private static final Pattern VERSION_PATTERN = Pattern.compile("1\\.(\\d+)");
/*     */ 
/*     */   
/*     */   private static long nextRefreshAt;
/*     */   
/*     */   private static boolean belowOneNineteen;
/*     */ 
/*     */   
/*     */   public static boolean isTargetProtocolBelowOneNineteen() {
/*  20 */     long now = System.currentTimeMillis();
/*  21 */     if (now < nextRefreshAt) {
/*  22 */       return belowOneNineteen;
/*     */     }
/*     */     
/*  25 */     belowOneNineteen = resolveBelowOneNineteen();
/*  26 */     nextRefreshAt = now + 1500L;
/*  27 */     return belowOneNineteen;
/*     */   }
/*     */   
/*     */   private static boolean resolveBelowOneNineteen() {
/*     */     try {
/*  32 */       Class<?> viaFabricPlusClass = Class.forName("com.viaversion.viafabricplus.ViaFabricPlus");
/*  33 */       Object impl = viaFabricPlusClass.getMethod("getImpl", new Class[0]).invoke(null, new Object[0]);
/*  34 */       if (impl == null) {
/*  35 */         return false;
/*     */       }
/*     */       
/*  38 */       Object targetVersion = invokeNoArg(impl, "getTargetVersion");
/*  39 */       if (targetVersion == null) {
/*  40 */         return false;
/*     */       }
/*     */       
/*  43 */       Integer protocolId = readProtocolId(targetVersion);
/*  44 */       return (protocolId != null && protocolId.intValue() < 759);
/*  45 */     } catch (Throwable ignored) {
/*  46 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Object invokeNoArg(Object instance, String methodName) {
/*     */     try {
/*  52 */       Method method = instance.getClass().getMethod(methodName, new Class[0]);
/*  53 */       if (!Modifier.isPublic(method.getModifiers()) || method.getParameterCount() != 0) {
/*  54 */         return null;
/*     */       }
/*  56 */       return method.invoke(instance, new Object[0]);
/*  57 */     } catch (Throwable ignored) {
/*  58 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Integer readProtocolId(Object targetVersion) {
/*     */     try {
/*  64 */       Method getVersion = targetVersion.getClass().getMethod("getVersion", new Class[0]);
/*  65 */       Object value = getVersion.invoke(targetVersion, new Object[0]);
/*  66 */       if (value instanceof Number) { Number number = (Number)value;
/*  67 */         return Integer.valueOf(number.intValue()); }
/*     */     
/*  69 */     } catch (Throwable throwable) {}
/*     */ 
/*     */     
/*     */     try {
/*  73 */       for (Method method : targetVersion.getClass().getMethods()) {
/*  74 */         if (Modifier.isPublic(method.getModifiers()) && method.getParameterCount() == 0) {
/*     */ 
/*     */           
/*  77 */           Class<?> returnType = method.getReturnType();
/*  78 */           if (returnType == int.class || returnType == Integer.class)
/*     */           
/*     */           { 
/*     */             
/*  82 */             String name = method.getName().toLowerCase();
/*  83 */             if (name.contains("version") || name.contains("protocol") || name.contains("id"))
/*     */             
/*     */             { 
/*     */               
/*  87 */               Object value = method.invoke(targetVersion, new Object[0]);
/*  88 */               if (value instanceof Number) { Number number = (Number)value;
/*  89 */                 return Integer.valueOf(number.intValue()); }  }  } 
/*     */         } 
/*     */       } 
/*  92 */     } catch (Throwable throwable) {}
/*     */ 
/*     */     
/*  95 */     Matcher matcher = VERSION_PATTERN.matcher(String.valueOf(targetVersion));
/*  96 */     if (matcher.find()) {
/*  97 */       int minor = Integer.parseInt(matcher.group(1));
/*  98 */       return Integer.valueOf((minor >= 19) ? 759 : 758);
/*     */     } 
/*     */     
/* 101 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\player\ViaProtocolUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */