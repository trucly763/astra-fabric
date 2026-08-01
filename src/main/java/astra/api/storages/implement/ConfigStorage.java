/*     */ package shame.astra.api.storages.implement;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonPrimitive;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*     */ import java.io.File;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.util.Map;
/*     */ import shame.astra.api.utils.cmd.macro.Macro;
/*     */ import shame.astra.api.utils.draggable.Draggable;
/*     */ import shame.astra.astra;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.impl.render.Interface;
/*     */ import shame.astra.client.modules.impl.render.base.InterfaceProcessing;
/*     */ import shame.astra.client.modules.impl.render.base.implement.WaterMark;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.BindSetting;
/*     */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*     */ import shame.astra.client.modules.settings.implement.FloatSetting;
/*     */ import shame.astra.client.modules.settings.implement.ListSetting;
/*     */ import shame.astra.client.modules.settings.implement.TextSetting;
/*     */ 
/*     */ public class ConfigStorage {
/*  27 */   public String currentConfig = "default";
/*  28 */   private final String extension = ".wonder";
/*     */   
/*     */   public ConfigStorage() {
/*  31 */     loadAll();
/*  32 */     Runtime.getRuntime().addShutdownHook(new Thread(this::saveAll));
/*     */   }
/*     */ 
/*     */   
/*     */   private void loadAll() {
/*     */     try {
/*  38 */       loadGlobals();
/*  39 */       loadConfig(this.currentConfig);
/*  40 */     } catch (Exception e) {
/*  41 */       e.printStackTrace(System.err);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void saveAll() {
/*     */     try {
/*  48 */       saveGlobals();
/*  49 */       saveConfig(this.currentConfig);
/*  50 */     } catch (Exception e) {
/*  51 */       e.printStackTrace(System.err);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void saveConfig(String config) throws Exception {
/*  57 */     File file = new File(astra.INSTANCE.configsDir, config + ".wonder");
/*     */     
/*  59 */     JsonObject object = new JsonObject();
/*  60 */     object.add("config", (JsonElement)new JsonPrimitive(config));
/*  61 */     object.add("theme", (JsonElement)new JsonPrimitive(astra.INSTANCE.themeStorage.getThemes().name()));
/*  62 */     object.add("language", (JsonElement)new JsonPrimitive(astra.INSTANCE.localizationStorage.getLanguage().name()));
/*  63 */     object.add("modules", (JsonElement)serializeModules());
/*  64 */     object.add("draggables", (JsonElement)serializeDraggables());
/*  65 */     object.add("hud", (JsonElement)serializeHudState());
/*     */     
/*  67 */     Writer writer = new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8); 
/*  68 */     try { writer.write((new GsonBuilder())
/*  69 */           .setPrettyPrinting()
/*  70 */           .create()
/*  71 */           .toJson((JsonElement)object));
/*  72 */       writer.close(); } catch (Throwable throwable) { try { writer.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */        throw throwable; }
/*  74 */      this.currentConfig = config;
/*     */   }
/*     */   
/*     */   public void loadConfig(String config) throws Exception {
/*     */     JsonObject object;
/*  79 */     if (!FileUtils.exists(String.valueOf(astra.INSTANCE.configsDir) + "/" + String.valueOf(astra.INSTANCE.configsDir) + ".wonder"))
/*     */       return; 
/*  81 */     InputStream stream = Files.newInputStream(Paths.get(String.valueOf(astra.INSTANCE.configsDir) + "/" + String.valueOf(astra.INSTANCE.configsDir) + ".wonder", new String[0]), new java.nio.file.OpenOption[0]); 
/*  82 */     try { Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8); 
/*  83 */       try { object = JsonParser.parseReader(reader).getAsJsonObject();
/*  84 */         reader.close(); } catch (Throwable throwable) { try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  if (stream != null) stream.close();  } catch (Throwable throwable) { if (stream != null)
/*     */         try { stream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/*  86 */      if (object.has("theme")) {
/*  87 */       String themeName = object.get("theme").getAsString();
/*  88 */       for (ThemeStorage.Themes theme : ThemeStorage.Themes.values()) {
/*  89 */         if (theme.name().equals(themeName)) {
/*  90 */           astra.INSTANCE.themeStorage.setThemes(theme);
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/*  96 */     if (object.has("language")) {
/*     */       try {
/*  98 */         astra.INSTANCE.localizationStorage.setLanguage(LocalizationStorage.Language.valueOf(object.get("language").getAsString()));
/*  99 */       } catch (Exception exception) {}
/*     */     }
/*     */ 
/*     */     
/* 103 */     if (object.has("draggables")) {
/* 104 */       deserializeDraggables(object.get("draggables").getAsJsonObject());
/*     */     }
/*     */     
/* 107 */     if (object.has("modules")) {
/* 108 */       deserializeModules(object.get("modules").getAsJsonObject());
/*     */     }
/*     */     
/* 111 */     if (object.has("hud")) {
/* 112 */       deserializeHudState(object.get("hud").getAsJsonObject());
/*     */     }
/*     */     
/* 115 */     this.currentConfig = config;
/*     */   }
/*     */ 
/*     */   
/*     */   public void saveGlobals() throws Exception {
/* 120 */     File file = new File(astra.INSTANCE.globalsDir, "globals.wonder");
/* 121 */     JsonObject object = new JsonObject();
/* 122 */     object.add("config", (JsonElement)new JsonPrimitive(this.currentConfig));
/*     */     
/* 124 */     object.add("theme", (JsonElement)new JsonPrimitive(astra.INSTANCE.themeStorage.getThemes().name()));
/* 125 */     object.add("language", (JsonElement)new JsonPrimitive(astra.INSTANCE.localizationStorage.getLanguage().name()));
/*     */     
/* 127 */     object.add("draggables", (JsonElement)serializeDraggables());
/* 128 */     object.add("hud", (JsonElement)serializeHudState());
/*     */     
/* 130 */     JsonArray friendsArray = new JsonArray();
/* 131 */     Objects.requireNonNull(friendsArray); astra.INSTANCE.friendStorage.getFriends().forEach(friendsArray::add);
/* 132 */     object.add("friends", (JsonElement)friendsArray);
/*     */     
/* 134 */     JsonArray staffsArray = new JsonArray();
/* 135 */     Objects.requireNonNull(staffsArray); astra.INSTANCE.staffStorage.getStaffs().forEach(staffsArray::add);
/* 136 */     object.add("staffs", (JsonElement)staffsArray);
/*     */     
/* 138 */     JsonArray macrosArray = new JsonArray();
/* 139 */     astra.INSTANCE.macroStorage.getMacros().forEach(macro -> {
/*     */           JsonObject macroObject = new JsonObject();
/*     */           macroObject.addProperty("name", macro.getName());
/*     */           macroObject.addProperty("command", macro.getCommand());
/*     */           macroObject.addProperty("key", Integer.valueOf(macro.getBind().getKey()));
/*     */           macrosArray.add((JsonElement)macroObject);
/*     */         });
/* 146 */     object.add("macros", (JsonElement)macrosArray);
/*     */     
/* 148 */     Writer writer = new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8); 
/* 149 */     try { writer.write((new GsonBuilder()).setPrettyPrinting().create().toJson((JsonElement)object));
/* 150 */       writer.close(); }
/*     */     catch (Throwable throwable) { try { writer.close(); }
/*     */       catch (Throwable throwable1)
/*     */       { throwable.addSuppressed(throwable1); }
/*     */        throw throwable; }
/* 155 */      } public void loadGlobals() throws Exception { JsonObject object; if (!FileUtils.exists(String.valueOf(astra.INSTANCE.globalsDir) + "/globals.wonder"))
/*     */       return; 
/* 157 */     InputStream stream = Files.newInputStream(Paths.get(String.valueOf(astra.INSTANCE.globalsDir) + "/globals.wonder", new String[0]), new java.nio.file.OpenOption[0]); 
/* 158 */     try { Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8); 
/* 159 */       try { object = JsonParser.parseReader(reader).getAsJsonObject();
/* 160 */         reader.close(); } catch (Throwable throwable) { try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  if (stream != null) stream.close();  } catch (Throwable throwable) { if (stream != null)
/*     */         try { stream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/* 162 */      if (object.has("config")) this.currentConfig = object.get("config").getAsString();
/*     */     
/* 164 */     if (object.has("theme")) {
/* 165 */       String themeName = object.get("theme").getAsString();
/* 166 */       for (ThemeStorage.Themes theme : ThemeStorage.Themes.values()) {
/* 167 */         if (theme.name().equals(themeName)) {
/* 168 */           astra.INSTANCE.themeStorage.setThemes(theme);
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 174 */     if (object.has("language")) {
/*     */       try {
/* 176 */         astra.INSTANCE.localizationStorage.setLanguage(LocalizationStorage.Language.valueOf(object.get("language").getAsString()));
/* 177 */       } catch (Exception exception) {}
/*     */     }
/*     */ 
/*     */     
/* 181 */     if (object.has("draggables")) {
/* 182 */       deserializeDraggables(object.get("draggables").getAsJsonObject());
/*     */     }
/*     */     
/* 185 */     if (object.has("hud")) {
/* 186 */       deserializeHudState(object.get("hud").getAsJsonObject());
/*     */     }
/*     */     
/* 189 */     if (object.has("friends")) {
/* 190 */       for (JsonElement element : object.get("friends").getAsJsonArray()) {
/* 191 */         if (astra.INSTANCE.friendStorage.isFriend(element.getAsString()))
/* 192 */           continue;  astra.INSTANCE.friendStorage.add(element.getAsString());
/*     */       } 
/*     */     }
/*     */     
/* 196 */     if (object.has("staffs")) {
/* 197 */       for (JsonElement element : object.get("staffs").getAsJsonArray()) {
/* 198 */         if (astra.INSTANCE.staffStorage.isStaff(element.getAsString()))
/* 199 */           continue;  astra.INSTANCE.staffStorage.add(element.getAsString());
/*     */       } 
/*     */     }
/*     */     
/* 203 */     if (object.has("macros")) {
/* 204 */       for (JsonElement element : object.get("macros").getAsJsonArray()) {
/*     */         try {
/*     */           String name, command;
/*     */           
/*     */           int key;
/*     */           
/* 210 */           if (element.isJsonObject()) {
/* 211 */             JsonObject macroObject = element.getAsJsonObject();
/* 212 */             name = macroObject.has("name") ? macroObject.get("name").getAsString() : "";
/* 213 */             command = macroObject.has("command") ? macroObject.get("command").getAsString() : "";
/* 214 */             key = macroObject.has("key") ? macroObject.get("key").getAsInt() : -1;
/*     */           } else {
/* 216 */             String[] split = element.getAsString().split(":", 3);
/* 217 */             if (split.length < 3)
/* 218 */               continue;  name = split[0];
/* 219 */             command = split[1];
/* 220 */             key = Integer.parseInt(split[2]);
/*     */           } 
/*     */           
/* 223 */           if (name.isBlank() || astra.INSTANCE.macroStorage.getMacro(name) != null) {
/*     */             continue;
/*     */           }
/*     */           
/* 227 */           astra.INSTANCE.macroStorage.add(new Macro(name, command, new BindSetting("bind", key)));
/* 228 */         } catch (Exception exception) {}
/*     */       } 
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/*     */   private JsonObject serializeModules() {
/* 235 */     JsonObject modules = new JsonObject();
/* 236 */     for (ObjectListIterator<Module> objectListIterator = ModuleClass.INSTANCE.getObject().iterator(); objectListIterator.hasNext(); ) { Module module = objectListIterator.next();
/*     */       try {
/* 238 */         JsonObject object = new JsonObject();
/* 239 */         object.add("toggled", (JsonElement)new JsonPrimitive(Boolean.valueOf(module.isEnable())));
/* 240 */         object.add("bind", (JsonElement)new JsonPrimitive(Integer.valueOf(module.getKey())));
/*     */         
/* 242 */         JsonObject settings = new JsonObject();
/* 243 */         for (Setting s : module.getSettings()) {
/*     */           try {
/* 245 */             if (s instanceof BooleanSetting) { BooleanSetting bool = (BooleanSetting)s;
/* 246 */               settings.add(s.name(), (JsonElement)new JsonPrimitive(Boolean.valueOf(bool.isState()))); continue; }
/* 247 */              if (s instanceof FloatSetting) { FloatSetting num = (FloatSetting)s;
/* 248 */               settings.add(s.name(), (JsonElement)new JsonPrimitive(Float.valueOf(num.getValue().floatValue()))); continue; }
/* 249 */              if (s instanceof ModeSetting) { ModeSetting mode = (ModeSetting)s;
/* 250 */               settings.add(s.name(), (JsonElement)new JsonPrimitive(mode.getCurrent())); continue; }
/* 251 */              if (s instanceof TextSetting) { TextSetting text = (TextSetting)s;
/* 252 */               settings.add(s.name(), (JsonElement)new JsonPrimitive(text.get())); continue; }
/* 253 */              if (s instanceof BindSetting) { BindSetting bind = (BindSetting)s;
/* 254 */               settings.add(s.name(), (JsonElement)new JsonPrimitive(Integer.valueOf(bind.getKey()))); continue; }
/* 255 */              if (s instanceof ListSetting) { ListSetting list = (ListSetting)s;
/* 256 */               JsonObject listObj = new JsonObject();
/* 257 */               for (BooleanSetting setting : list.getSettings()) {
/* 258 */                 listObj.add(setting.name(), (JsonElement)new JsonPrimitive(Boolean.valueOf(setting.isState())));
/*     */               }
/* 260 */               settings.add(list.name(), (JsonElement)listObj); }
/*     */           
/* 262 */           } catch (Exception exception) {}
/*     */         } 
/*     */ 
/*     */         
/* 266 */         object.add("settings", (JsonElement)settings);
/* 267 */         modules.add(module.getName(), (JsonElement)object);
/* 268 */       } catch (Exception exception) {} }
/*     */ 
/*     */ 
/*     */     
/* 272 */     return modules;
/*     */   }
/*     */   
/*     */   private void deserializeModules(JsonObject modules) {
/* 276 */     Map<Module, Boolean> targetStates = new LinkedHashMap<>();
/*     */     ObjectListIterator<Module> objectListIterator;
/* 278 */     for (objectListIterator = ModuleClass.INSTANCE.getObject().iterator(); objectListIterator.hasNext(); ) { Module module = objectListIterator.next();
/*     */ 
/*     */       
/*     */       try {
/* 282 */         JsonObject object = modules.has(module.getName()) ? modules.get(module.getName()).getAsJsonObject() : null;
/*     */ 
/*     */ 
/*     */         
/* 286 */         boolean toggled = (object != null && object.has("toggled") && object.get("toggled").getAsBoolean());
/*     */         
/* 288 */         targetStates.put(module, Boolean.valueOf(toggled));
/*     */         
/* 290 */         if (module.isEnable()) {
/* 291 */           module.setEnabled(false);
/*     */         }
/* 293 */       } catch (Exception ignored) {
/* 294 */         targetStates.put(module, Boolean.valueOf(false));
/*     */       }  }
/*     */ 
/*     */     
/* 298 */     for (objectListIterator = ModuleClass.INSTANCE.getObject().iterator(); objectListIterator.hasNext(); ) { Module module = objectListIterator.next();
/*     */       try {
/* 300 */         if (!modules.has(module.getName())) {
/*     */           continue;
/*     */         }
/*     */         
/* 304 */         JsonObject object = modules.get(module.getName()).getAsJsonObject();
/*     */         
/* 306 */         if (object.has("bind")) {
/* 307 */           module.setKey(object.get("bind").getAsInt());
/*     */         }
/*     */         
/* 310 */         if (object.has("settings")) {
/* 311 */           JsonObject settings = object.get("settings").getAsJsonObject();
/*     */           
/* 313 */           for (Setting s : module.getSettings()) {
/*     */             try {
/* 315 */               if (!settings.has(s.name()))
/*     */                 continue; 
/* 317 */               JsonElement element = settings.get(s.name());
/*     */               
/* 319 */               if (s instanceof BooleanSetting) { BooleanSetting bool = (BooleanSetting)s;
/* 320 */                 bool.setState(element.getAsBoolean()); continue; }
/* 321 */                if (s instanceof FloatSetting) { FloatSetting num = (FloatSetting)s;
/* 322 */                 num.setValue(element.getAsFloat()); continue; }
/* 323 */                if (s instanceof ModeSetting) { ModeSetting mode = (ModeSetting)s;
/* 324 */                 mode.set(element.getAsString()); continue; }
/* 325 */                if (s instanceof TextSetting) { TextSetting text = (TextSetting)s;
/* 326 */                 text.setText(element.getAsString()); continue; }
/* 327 */                if (s instanceof BindSetting) { BindSetting bind = (BindSetting)s;
/* 328 */                 bind.setKey(element.getAsInt()); continue; }
/* 329 */                if (s instanceof ListSetting) { ListSetting list = (ListSetting)s;
/* 330 */                 JsonObject listObj = element.getAsJsonObject();
/* 331 */                 for (BooleanSetting setting : list.getSettings()) {
/* 332 */                   if (listObj.has(setting.name())) {
/* 333 */                     setting.setState(listObj.get(setting.name()).getAsBoolean());
/*     */                   }
/*     */                 }  }
/*     */             
/* 337 */             } catch (Exception exception) {}
/*     */           }
/*     */         
/*     */         }
/*     */       
/* 342 */       } catch (Exception exception) {} }
/*     */ 
/*     */ 
/*     */     
/* 346 */     for (Map.Entry<Module, Boolean> entry : targetStates.entrySet()) {
/*     */       try {
/* 348 */         ((Module)entry.getKey()).setEnabled(((Boolean)entry.getValue()).booleanValue());
/* 349 */       } catch (Exception exception) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private JsonObject serializeHudState() {
/* 355 */     JsonObject hud = new JsonObject();
/* 356 */     Interface interfaceModule = ModuleClass.interfaceModule;
/* 357 */     if (interfaceModule == null) {
/* 358 */       return hud;
/*     */     }
/*     */     
/* 361 */     for (Map.Entry<String, InterfaceProcessing> entry : (Iterable<Map.Entry<String, InterfaceProcessing>>)interfaceModule.getConfigurableHudElements().entrySet()) {
/* 362 */       InterfaceProcessing element = entry.getValue();
/* 363 */       if (element == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 367 */       JsonObject object = new JsonObject();
/* 368 */       object.add("unusualRectType", (JsonElement)new JsonPrimitive(Boolean.valueOf(element.isUnusualRectType())));
/*     */       
/* 370 */       if (element instanceof WaterMark) { WaterMark waterMark = (WaterMark)element;
/* 371 */         object.add("showFps", (JsonElement)new JsonPrimitive(Boolean.valueOf(waterMark.isShowFps())));
/* 372 */         object.add("showMs", (JsonElement)new JsonPrimitive(Boolean.valueOf(waterMark.isShowMs())));
/* 373 */         object.add("showServer", (JsonElement)new JsonPrimitive(Boolean.valueOf(waterMark.isShowServer())));
/* 374 */         object.add("showTps", (JsonElement)new JsonPrimitive(Boolean.valueOf(waterMark.isShowTps()))); }
/* 375 */       else if (element instanceof TargetHud) { TargetHud targetHud = (TargetHud)element;
/* 376 */         object.add("headParticlesEnabled", (JsonElement)new JsonPrimitive(Boolean.valueOf(targetHud.isHeadParticlesEnabled()))); }
/*     */ 
/*     */       
/* 379 */       hud.add(entry.getKey(), (JsonElement)object);
/*     */     } 
/*     */     
/* 382 */     return hud;
/*     */   }
/*     */   
/*     */   private void deserializeHudState(JsonObject hud) {
/* 386 */     Interface interfaceModule = ModuleClass.interfaceModule;
/* 387 */     if (interfaceModule == null) {
/*     */       return;
/*     */     }
/*     */     
/* 391 */     for (Map.Entry<String, InterfaceProcessing> entry : (Iterable<Map.Entry<String, InterfaceProcessing>>)interfaceModule.getConfigurableHudElements().entrySet()) {
/* 392 */       if (!hud.has(entry.getKey())) {
/*     */         continue;
/*     */       }
/*     */       
/*     */       try {
/* 397 */         JsonObject object = hud.get(entry.getKey()).getAsJsonObject();
/* 398 */         InterfaceProcessing element = entry.getValue();
/*     */         
/* 400 */         if (object.has("unusualRectType")) {
/* 401 */           element.setUnusualRectType(object.get("unusualRectType").getAsBoolean());
/*     */         }
/*     */         
/* 404 */         if (element instanceof WaterMark) { WaterMark waterMark = (WaterMark)element;
/* 405 */           if (object.has("showFps")) {
/* 406 */             waterMark.setShowFps(object.get("showFps").getAsBoolean());
/*     */           }
/* 408 */           if (object.has("showMs")) {
/* 409 */             waterMark.setShowMs(object.get("showMs").getAsBoolean());
/*     */           }
/* 411 */           if (object.has("showServer")) {
/* 412 */             waterMark.setShowServer(object.get("showServer").getAsBoolean());
/*     */           }
/* 414 */           if (object.has("showTps"))
/* 415 */             waterMark.setShowTps(object.get("showTps").getAsBoolean());  continue; }
/*     */         
/* 417 */         if (element instanceof TargetHud) { TargetHud targetHud = (TargetHud)element;
/* 418 */           if (object.has("headParticlesEnabled")) {
/* 419 */             targetHud.setHeadParticlesEnabled(object.get("headParticlesEnabled").getAsBoolean());
/*     */           } }
/*     */       
/* 422 */       } catch (Exception exception) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private JsonObject serializeDraggables() {
/* 428 */     JsonObject draggables = new JsonObject();
/* 429 */     for (Draggable drag : DragStorage.draggables.values()) {
/* 430 */       JsonObject object = new JsonObject();
/* 431 */       object.add("x", (JsonElement)new JsonPrimitive(Float.valueOf(drag.getX())));
/* 432 */       object.add("y", (JsonElement)new JsonPrimitive(Float.valueOf(drag.getY())));
/* 433 */       draggables.add(drag.getName(), (JsonElement)object);
/*     */     } 
/* 435 */     return draggables;
/*     */   }
/*     */   
/*     */   private void deserializeDraggables(JsonObject draggables) {
/* 439 */     for (String name : draggables.keySet()) {
/* 440 */       Draggable drag = DragStorage.draggables.get(name);
/* 441 */       if (drag == null)
/*     */         continue; 
/* 443 */       JsonObject object = draggables.get(name).getAsJsonObject();
/* 444 */       if (object.has("x")) {
/* 445 */         drag.setX(object.get("x").getAsFloat());
/*     */       }
/* 447 */       if (object.has("y"))
/* 448 */         drag.setY(object.get("y").getAsFloat()); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\api\storages\implement\ConfigStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */