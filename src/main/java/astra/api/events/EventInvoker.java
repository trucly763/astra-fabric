/*     */ package shame.astra.api.events;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ 
/*     */ public class EventInvoker
/*     */ {
/*  13 */   private static final ConcurrentHashMap<Class<?>, Object> classRegistry = new ConcurrentHashMap<>();
/*  14 */   private static final ConcurrentHashMap<Class<? extends Event>, List<Invocation>> invocationCache = new ConcurrentHashMap<>();
/*  15 */   private static final ConcurrentHashMap<String, Long> slowHandlerWarnings = new ConcurrentHashMap<>();
/*  16 */   private static final ConcurrentHashMap<String, Long> slowEventWarnings = new ConcurrentHashMap<>();
/*  17 */   private static final boolean PERF_DEBUG = Boolean.parseBoolean(System.getProperty("astra.perf.debug", "false"));
/*  18 */   private static final long SLOW_HANDLER_NANOS = Long.getLong("astra.perf.handlerMs", 8L).longValue() * 1000000L;
/*  19 */   private static final long SLOW_EVENT_NANOS = Long.getLong("astra.perf.eventMs", 18L).longValue() * 1000000L;
/*  20 */   private static final long WARN_COOLDOWN_NANOS = Long.getLong("astra.perf.cooldownMs", 1000L).longValue() * 1000000L;
/*     */   
/*     */   private static volatile boolean cacheDirty = true;
/*     */   
/*     */   public static void register(Object obj) {
/*  25 */     classRegistry.putIfAbsent(obj.getClass(), obj);
/*  26 */     cacheDirty = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void unregister(Object obj) {
/*  31 */     classRegistry.remove(obj.getClass());
/*  32 */     cacheDirty = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void clean() {
/*  37 */     classRegistry.clear();
/*  38 */     invocationCache.clear();
/*  39 */     cacheDirty = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void invoke(Event event) throws IllegalAccessException, InvocationTargetException, InstantiationException {
/*  44 */     long eventStart = PERF_DEBUG ? System.nanoTime() : 0L;
/*  45 */     if (cacheDirty)
/*     */     {
/*  47 */       rebuildCache();
/*     */     }
/*     */     
/*  50 */     List<Invocation> invocations = invocationCache.get(event.getClass());
/*  51 */     if (invocations == null || invocations.isEmpty()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  56 */     for (Invocation invocation : invocations) {
/*     */       
/*  58 */       if (!classRegistry.containsKey(invocation.listener().getClass())) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */       
/*  63 */       Method method = invocation.method();
/*  64 */       method.setAccessible(true);
/*  65 */       long handlerStart = PERF_DEBUG ? System.nanoTime() : 0L;
/*     */       
/*     */       try {
/*  68 */         method.invoke(invocation.listener(), new Object[] { event });
/*     */       }
/*     */       finally {
/*     */         
/*  72 */         if (PERF_DEBUG) {
/*     */           
/*  74 */           long elapsed = System.nanoTime() - handlerStart;
/*  75 */           if (elapsed >= SLOW_HANDLER_NANOS)
/*     */           {
/*  77 */             logSlowHandler(event, invocation, elapsed);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  83 */     if (PERF_DEBUG) {
/*     */       
/*  85 */       long elapsed = System.nanoTime() - eventStart;
/*  86 */       if (elapsed >= SLOW_EVENT_NANOS)
/*     */       {
/*  88 */         logSlowEvent(event, elapsed, invocations.size());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean hasListeners(Class<? extends Event> eventClass) {
/*  95 */     if (cacheDirty)
/*     */     {
/*  97 */       rebuildCache();
/*     */     }
/*     */     
/* 100 */     List<Invocation> invocations = invocationCache.get(eventClass);
/* 101 */     return (invocations != null && !invocations.isEmpty());
/*     */   }
/*     */ 
/*     */   
/*     */   private static synchronized void rebuildCache() {
/* 106 */     if (!cacheDirty) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 111 */     ConcurrentHashMap<Class<? extends Event>, List<Invocation>> rebuilt = new ConcurrentHashMap<>();
/* 112 */     for (Object listener : classRegistry.values()) {
/*     */       
/* 114 */       for (Method method : listener.getClass().getDeclaredMethods()) {
/*     */         
/* 116 */         if (method.isAnnotationPresent((Class)EventLink.class)) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 121 */           Class<?>[] parameters = method.getParameterTypes();
/* 122 */           if (parameters.length == 1 && Event.class.isAssignableFrom(parameters[0])) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 128 */             Class<? extends Event> eventClass = (Class)parameters[0];
/* 129 */             method.setAccessible(true);
/* 130 */             ((List<Invocation>)rebuilt.computeIfAbsent(eventClass, key -> new ArrayList()))
/* 131 */               .add(new Invocation(listener, method, ((EventLink)method.<EventLink>getAnnotation(EventLink.class)).priority()));
/*     */           } 
/*     */         } 
/*     */       } 
/* 135 */     }  for (Iterator<List<Invocation>> iterator = rebuilt.values().iterator(); iterator.hasNext(); ) { List<Invocation> invocations = iterator.next();
/*     */       
/* 137 */       invocations.sort((a, b) -> {
/*     */             int priorityCompare = Integer.compare(b.priority(), a.priority());
/*     */             
/*     */             if (priorityCompare != 0) {
/*     */               return priorityCompare;
/*     */             }
/*     */             
/*     */             int classCompare = a.listener().getClass().getName().compareTo(b.listener().getClass().getName());
/*     */             return (classCompare != 0) ? classCompare : a.method().getName().compareTo(b.method().getName());
/*     */           }); }
/*     */     
/* 148 */     invocationCache.clear();
/* 149 */     invocationCache.putAll(rebuilt);
/* 150 */     cacheDirty = false;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void logSlowHandler(Event event, Invocation invocation, long elapsedNanos) {
/* 155 */     String listenerName = invocation.listener().getClass().getSimpleName();
/* 156 */     String methodName = invocation.method().getName();
/* 157 */     String eventName = event.getClass().getSimpleName();
/* 158 */     String key = "handler:" + eventName + ":" + listenerName + "#" + methodName;
/* 159 */     if (!canWarn(slowHandlerWarnings, key)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 164 */     System.out.println(String.format(Locale.ROOT, "[PerfDebug] Slow handler: %s -> %s#%s took %.2f ms", new Object[] { eventName, listenerName, methodName, 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 169 */             Double.valueOf(elapsedNanos / 1000000.0D) }));
/*     */   }
/*     */ 
/*     */   
/*     */   private static void logSlowEvent(Event event, long elapsedNanos, int invocationCount) {
/* 174 */     String eventName = event.getClass().getSimpleName();
/* 175 */     String key = "event:" + eventName;
/* 176 */     if (!canWarn(slowEventWarnings, key)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 181 */     System.out.println(String.format(Locale.ROOT, "[PerfDebug] Slow event: %s took %.2f ms for %d handlers", new Object[] { eventName, 
/*     */ 
/*     */             
/* 184 */             Double.valueOf(elapsedNanos / 1000000.0D), 
/* 185 */             Integer.valueOf(invocationCount) }));
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean canWarn(ConcurrentHashMap<String, Long> warnings, String key) {
/* 190 */     long now = System.nanoTime();
/* 191 */     Long lastWarn = warnings.get(key);
/* 192 */     if (lastWarn != null && now - lastWarn.longValue() < WARN_COOLDOWN_NANOS)
/*     */     {
/* 194 */       return false;
/*     */     }
/*     */     
/* 197 */     warnings.put(key, Long.valueOf(now));
/* 198 */     return true;
/*     */   }
/*     */   private static final class Invocation extends Record { private final Object listener; private final Method method; private final int priority;
/* 201 */     private Invocation(Object listener, Method method, int priority) { this.listener = listener; this.method = method; this.priority = priority; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lshame/astra/api/events/EventInvoker$Invocation;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #201	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 201 */       //   0	7	0	this	Lshame/astra/api/events/EventInvoker$Invocation; } public Object listener() { return this.listener; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lshame/astra/api/events/EventInvoker$Invocation;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #201	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lshame/astra/api/events/EventInvoker$Invocation; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lshame/astra/api/events/EventInvoker$Invocation;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #201	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lshame/astra/api/events/EventInvoker$Invocation;
/* 201 */       //   0	8	1	o	Ljava/lang/Object; } public Method method() { return this.method; } public int priority() { return this.priority; }
/*     */      }
/*     */ 
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\api\events\EventInvoker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */