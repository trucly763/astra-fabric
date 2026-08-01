/*     */ package shame.astra.client.modules.impl.misc;
/*     */ 
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.class_2561;
/*     */ import net.minecraft.class_342;
/*     */ import net.minecraft.class_408;
/*     */ import net.minecraft.class_437;
/*     */ import shame.astra.api.utils.replace.ReplaceUtils;
/*     */ import shame.astra.astra;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*     */ import shame.astra.client.modules.settings.implement.TextSetting;
/*     */ import shame.astra.mixin.ChatScreenAccessor;
/*     */ 
/*     */ public class NameProtect extends Module {
/*  19 */   public static final NameProtect INSTANCE = new NameProtect();
/*  20 */   private final BooleanSetting friends = new BooleanSetting("Скрывать друзей", true);
/*  21 */   private final BooleanSetting grief = new BooleanSetting("Скрывать гриф", false);
/*  22 */   private final TextSetting nickname = new TextSetting("Никнейм", "astra", 32);
/*     */   private static final int PATCH_CACHE_LIMIT = 512;
/*     */   
/*  25 */   private final Map<String, String> patchCache = new LinkedHashMap<String, String>(512, 0.75F, true)
/*     */     {
/*     */       protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
/*  28 */         return (size() > 512);
/*     */       }
/*     */     };
/*     */   
/*     */   private NameProtect() {
/*  33 */     super("NameProtect", "Скрывает никнеймы", Module.ModuleCategory.MISC);
/*  34 */     addSettings(new Setting[] { (Setting)this.friends, (Setting)this.grief, (Setting)this.nickname });
/*     */   }
/*     */   
/*     */   public String patch(String text) {
/*  38 */     if (text == null) {
/*  39 */       return null;
/*     */     }
/*  41 */     if (!shouldPatch()) {
/*  42 */       return text;
/*     */     }
/*     */     
/*  45 */     String cacheKey = getPatchCacheKey(text);
/*  46 */     String cached = this.patchCache.get(cacheKey);
/*  47 */     if (cached != null) {
/*  48 */       return cached;
/*     */     }
/*     */     
/*  51 */     String out = text;
/*  52 */     String replacement = getReplacementName();
/*  53 */     out = replaceIgnoreCase(out, mc.method_1548().method_1676(), replacement);
/*  54 */     if (this.friends.isState() && astra.INSTANCE != null && astra.INSTANCE.friendStorage != null) {
/*  55 */       for (String friend : astra.INSTANCE.friendStorage.getFriends()) {
/*  56 */         out = replaceIgnoreCase(out, friend, replacement);
/*     */       }
/*     */     }
/*  59 */     out = patchGrief(out);
/*  60 */     this.patchCache.put(cacheKey, out);
/*  61 */     return out;
/*     */   }
/*     */   
/*     */   public String patchIncomingText(String text) {
/*  65 */     return patch(text);
/*     */   }
/*     */   
/*     */   public class_2561 patchText(class_2561 text) {
/*  69 */     if (text == null) {
/*  70 */       return null;
/*     */     }
/*     */     
/*  73 */     if (!shouldPatch()) {
/*  74 */       return text;
/*     */     }
/*     */     
/*  77 */     class_2561 output = text;
/*  78 */     String replacement = getReplacementName();
/*  79 */     output = ReplaceUtils.replace(output, mc.method_1548().method_1676(), replacement);
/*  80 */     if (this.friends.isState() && astra.INSTANCE != null && astra.INSTANCE.friendStorage != null) {
/*  81 */       for (String friend : astra.INSTANCE.friendStorage.getFriends()) {
/*  82 */         output = ReplaceUtils.replace(output, friend, replacement);
/*     */       }
/*     */     }
/*  85 */     return output;
/*     */   }
/*     */   
/*     */   public String getReplacementName() {
/*  89 */     String value = this.nickname.get();
/*  90 */     return (value == null || value.isBlank()) ? "astra" : value;
/*     */   }
/*     */   
/*     */   public boolean shouldHideGrief() {
/*  94 */     return this.grief.isState();
/*     */   }
/*     */   
/*     */   private String replaceIgnoreCase(String text, String target, String replacement) {
/*  98 */     if (text == null || target == null || target.isEmpty()) {
/*  99 */       return text;
/*     */     }
/* 101 */     int firstIndex = indexOfIgnoreCase(text, target, 0);
/* 102 */     if (firstIndex < 0) {
/* 103 */       return text;
/*     */     }
/*     */     
/* 106 */     StringBuilder out = new StringBuilder(text.length() + replacement.length());
/* 107 */     int from = 0;
/* 108 */     int index = firstIndex;
/* 109 */     while (index >= 0) {
/* 110 */       out.append(text, from, index).append(replacement);
/* 111 */       from = index + target.length();
/* 112 */       index = indexOfIgnoreCase(text, target, from);
/*     */     } 
/* 114 */     out.append(text, from, text.length());
/* 115 */     return out.toString();
/*     */   }
/*     */   
/*     */   private int indexOfIgnoreCase(String text, String target, int from) {
/* 119 */     int max = text.length() - target.length();
/* 120 */     for (int i = Math.max(0, from); i <= max; i++) {
/* 121 */       if (text.regionMatches(true, i, target, 0, target.length())) {
/* 122 */         return i;
/*     */       }
/*     */     } 
/* 125 */     return -1;
/*     */   }
/*     */   
/*     */   private String patchGrief(String text) {
/* 129 */     if (text == null || !this.grief.isState()) {
/* 130 */       return text;
/*     */     }
/*     */     
/* 133 */     String out = text.replaceAll("Анархия-\\d+", "AstraBETA.fun");
/* 134 */     out = out.replaceAll("ГРИФ #\\d+", "AstraBETA.fun");
/* 135 */     return out;
/*     */   }
/*     */   
/*     */   private String getPatchCacheKey(String text) {
/* 139 */     String username = (mc != null && mc.method_1548() != null) ? mc.method_1548().method_1676() : "";
/* 140 */     int friendsHash = 0;
/* 141 */     if (this.friends.isState() && astra.INSTANCE != null && astra.INSTANCE.friendStorage != null) {
/* 142 */       List<String> friendList = astra.INSTANCE.friendStorage.getFriends();
/* 143 */       friendsHash = friendList.hashCode();
/*     */     } 
/* 145 */     return username + "\002" + username + "\002" + 
/* 146 */       getReplacementName() + "\002" + this.friends
/* 147 */       .isState() + "\002" + this.grief
/* 148 */       .isState() + "\002" + friendsHash;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean shouldPatch() {
/* 154 */     return (isEnable() && mc != null && mc.field_1724 != null && mc.field_1687 != null && !isFriendRemoveInputActive());
/*     */   }
/*     */   private boolean isFriendRemoveInputActive() {
/*     */     class_408 chatScreen;
/* 158 */     class_437 class_437 = mc.field_1755; if (class_437 instanceof class_408) { chatScreen = (class_408)class_437; }
/* 159 */     else { return false; }
/*     */ 
/*     */     
/* 162 */     class_342 chatField = ((ChatScreenAccessor)chatScreen).astra$getChatField();
/* 163 */     if (chatField == null) {
/* 164 */       return false;
/*     */     }
/*     */     
/* 167 */     String input = chatField.method_1882();
/* 168 */     if (input == null) {
/* 169 */       return false;
/*     */     }
/*     */     
/* 172 */     String normalized = input.trim().toLowerCase();
/*     */ 
/*     */     
/* 175 */     String prefix = (astra.INSTANCE != null && astra.INSTANCE.commandStorage != null) ? astra.INSTANCE.commandStorage.getPrefix().toLowerCase() : ".";
/* 176 */     return normalized.startsWith(prefix + "friend remove");
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\misc\NameProtect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */