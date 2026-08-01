/*     */ package shame.astra.client.modules.impl.misc;
/*     */ 
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import net.minecraft.class_1309;
/*     */ import net.minecraft.class_1657;
/*     */ import net.minecraft.class_2561;
/*     */ import net.minecraft.class_266;
/*     */ import net.minecraft.class_268;
/*     */ import net.minecraft.class_269;
/*     */ import net.minecraft.class_270;
/*     */ import net.minecraft.class_640;
/*     */ import net.minecraft.class_8646;
/*     */ import net.minecraft.class_9011;
/*     */ import net.minecraft.class_9013;
/*     */ import net.minecraft.class_9015;
/*     */ import net.minecraft.class_9022;
/*     */ import net.minecraft.class_9025;
/*     */ import shame.astra.api.storages.implement.helpertstorages.enumvar.ModuleClass;
/*     */ import shame.astra.client.modules.Module;
/*     */ import shame.astra.client.modules.settings.Setting;
/*     */ import shame.astra.client.modules.settings.implement.BooleanSetting;
/*     */ 
/*     */ public class ScoreboardHP extends Module {
/*  26 */   public static final ScoreboardHP INSTANCE = new ScoreboardHP();
/*     */   
/*  28 */   private static final Pattern HP_NUMBER = Pattern.compile("(\\d+(?:[.,]\\d+)?)");
/*     */   
/*     */   private static final float MAX_REASONABLE_HP = 1024.0F;
/*     */   
/*     */   public static final float UNKNOWN_HP = -1.0F;
/*     */   
/*  34 */   private final BooleanSetting gulpvp = new BooleanSetting("GulpVP", false);
/*     */   
/*     */   public ScoreboardHP() {
/*  37 */     super("ScoreboardHP", "Обход показа HP для серверов", Module.ModuleCategory.MISC);
/*  38 */     addSettings(new Setting[] { (Setting)this.gulpvp });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean shouldHideHealth(class_1657 player) {
/*  45 */     if (!INSTANCE.isEnable() || !INSTANCE.gulpvp.isState()) {
/*  46 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  50 */     if (mc.method_1558() != null) {
/*  51 */       String serverAddress = (mc.method_1558()).field_3761;
/*  52 */       if (serverAddress != null && serverAddress.toLowerCase().contains("gulpvp.pw")) {
/*  53 */         return true;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  59 */     if (isHealthHiddenOnServer(player)) {
/*  60 */       return true;
/*     */     }
/*     */     
/*  63 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isHealthHiddenOnServer(class_1657 player) {
/*  70 */     if (mc.field_1687 == null) return false;
/*     */     
/*     */     try {
/*  73 */       class_269 scoreboard = mc.field_1687.method_8428();
/*     */ 
/*     */       
/*  76 */       class_266 belowName = scoreboard.method_1189(class_8646.field_45158);
/*  77 */       class_266 list = scoreboard.method_1189(class_8646.field_45156);
/*     */ 
/*     */       
/*  80 */       if (belowName == null && list == null) {
/*     */         
/*  82 */         Float sidebarHp = getGulpVpSidebarHealth(player);
/*  83 */         return (sidebarHp == null);
/*     */       } 
/*     */       
/*  86 */       return false;
/*  87 */     } catch (Exception ignored) {
/*  88 */       return true;
/*     */     } 
/*     */   }
/*     */   public static float getHealth(class_1309 entity) {
/*     */     class_1657 player;
/*  93 */     if (entity == null) {
/*  94 */       return 0.0F;
/*     */     }
/*     */     
/*  97 */     if (!INSTANCE.isEnable()) {
/*  98 */       return entity.method_6032();
/*     */     }
/*     */     
/* 101 */     if (entity instanceof net.minecraft.class_746) {
/* 102 */       return entity.method_6032();
/*     */     }
/*     */     
/* 105 */     if (entity instanceof class_1657) { player = (class_1657)entity; }
/* 106 */     else { return entity.method_6032(); }
/*     */ 
/*     */ 
/*     */     
/* 110 */     if (shouldHideHealth(player)) {
/* 111 */       return -1.0F;
/*     */     }
/*     */     
/* 114 */     if (INSTANCE.gulpvp.isState()) {
/* 115 */       Float sidebarHp = getGulpVpSidebarHealth(player);
/* 116 */       if (sidebarHp != null) {
/* 117 */         return sidebarHp.floatValue();
/*     */       }
/*     */     } 
/*     */     
/* 121 */     if (mc.method_1558() == null) {
/* 122 */       return entity.method_6032();
/*     */     }
/*     */     
/* 125 */     return getObjectiveHealth(player);
/*     */   }
/*     */   public static float getHealthWithAbsorption(class_1309 entity) {
/*     */     class_1657 player;
/* 129 */     if (entity == null) {
/* 130 */       return 0.0F;
/*     */     }
/*     */     
/* 133 */     if (!INSTANCE.isEnable()) {
/* 134 */       return Math.max(0.0F, entity.method_6032() + entity.method_6067());
/*     */     }
/*     */     
/* 137 */     if (entity instanceof net.minecraft.class_746) {
/* 138 */       return Math.max(0.0F, entity.method_6032() + entity.method_6067());
/*     */     }
/*     */     
/* 141 */     if (entity instanceof class_1657) { player = (class_1657)entity; }
/* 142 */     else { return Math.max(0.0F, getHealth(entity) + entity.method_6067()); }
/*     */ 
/*     */ 
/*     */     
/* 146 */     if (shouldHideHealth(player)) {
/* 147 */       return -1.0F;
/*     */     }
/*     */     
/* 150 */     if (INSTANCE.gulpvp.isState() && entity instanceof class_1657) {
/* 151 */       Float sidebarHp = getGulpVpSidebarHealth(player);
/* 152 */       if (sidebarHp != null) {
/* 153 */         return Math.max(0.0F, sidebarHp.floatValue());
/*     */       }
/*     */     } 
/*     */     
/* 157 */     return Math.max(0.0F, getHealth(entity) + entity.method_6067());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean shouldShowUnknownInTargetHud(class_1309 entity) {
/*     */     class_1657 player;
/* 164 */     if (entity instanceof class_1657) { player = (class_1657)entity; }
/* 165 */     else { return false; }
/*     */     
/* 167 */     return shouldHideHealth(player);
/*     */   }
/*     */   
/*     */   private static float getObjectiveHealth(class_1657 player) {
/*     */     try {
/* 172 */       class_269 scoreboard = player.method_7327();
/* 173 */       class_266 objective = scoreboard.method_1189(class_8646.field_45158);
/* 174 */       if (objective == null) {
/* 175 */         objective = scoreboard.method_1189(class_8646.field_45156);
/*     */       }
/* 177 */       if (objective == null) {
/* 178 */         return player.method_6032();
/*     */       }
/*     */       
/* 181 */       class_9013 score = scoreboard.method_55430((class_9015)player, objective);
/* 182 */       if (score == null) {
/* 183 */         return player.method_6032();
/*     */       }
/*     */       
/* 186 */       return score.method_55397();
/* 187 */     } catch (Exception ignored) {
/* 188 */       return player.method_6032();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Float getGulpVpSidebarHealth(class_1657 player) {
/* 193 */     if (mc.field_1687 == null) {
/* 194 */       return null;
/*     */     }
/*     */     
/*     */     try {
/* 198 */       class_269 scoreboard = mc.field_1687.method_8428();
/* 199 */       class_266 sidebar = scoreboard.method_1189(class_8646.field_45157);
/* 200 */       if (sidebar == null) {
/* 201 */         return null;
/*     */       }
/*     */       
/* 204 */       class_9022 numberFormat = sidebar.method_55380((class_9022)class_9025.field_47567);
/* 205 */       String[] nameVariants = collectNameVariants(player);
/* 206 */       Float bestHp = null;
/* 207 */       int bestMatchScore = -1;
/*     */       
/* 209 */       for (class_9011 entry : scoreboard.method_1184(sidebar)) {
/* 210 */         if (entry.method_55385()) {
/*     */           continue;
/*     */         }
/*     */         
/* 214 */         class_268 team = scoreboard.method_1164(entry.comp_2127());
/* 215 */         String lineText = stripFormatting(class_268.method_1142((class_270)team, entry.method_55387()).getString());
/* 216 */         String ownerText = stripFormatting(entry.comp_2127());
/* 217 */         String scoreText = stripFormatting(entry.method_55386(numberFormat).getString());
/*     */         
/* 219 */         int matchScore = getNameMatchScore(lineText, ownerText, nameVariants);
/* 220 */         if (matchScore < 0) {
/*     */           continue;
/*     */         }
/*     */         
/* 224 */         Float hp = extractSidebarHp(entry, scoreText, lineText);
/* 225 */         if (hp == null) {
/*     */           continue;
/*     */         }
/*     */         
/* 229 */         if (matchScore > bestMatchScore) {
/* 230 */           bestMatchScore = matchScore;
/* 231 */           bestHp = hp;
/*     */         } 
/*     */       } 
/*     */       
/* 235 */       return bestHp;
/* 236 */     } catch (Exception ignored) {
/* 237 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Float extractSidebarHp(class_9011 entry, String scoreText, String lineText) {
/* 242 */     Float fromScoreColumn = parseHpNumber(scoreText);
/* 243 */     if (fromScoreColumn != null) {
/* 244 */       return fromScoreColumn;
/*     */     }
/*     */     
/* 247 */     if (isReasonableHp(entry.comp_2128())) {
/* 248 */       return Float.valueOf(entry.comp_2128());
/*     */     }
/*     */     
/* 251 */     Float fromLine = parseHpFromLine(lineText);
/* 252 */     if (fromLine != null) {
/* 253 */       return fromLine;
/*     */     }
/*     */     
/* 256 */     return null;
/*     */   }
/*     */   
/*     */   private static String[] collectNameVariants(class_1657 player) {
/* 260 */     String plainName = stripFormatting(player.method_5477().getString());
/* 261 */     String scoreboardName = stripFormatting(player.method_5820());
/*     */ 
/*     */     
/* 264 */     String profileName = (player.method_7334() != null) ? stripFormatting(player.method_7334().getName()) : "";
/*     */     
/* 266 */     String tabName = "";
/* 267 */     if (mc.method_1562() != null) {
/* 268 */       class_640 entry = mc.method_1562().method_2871(player.method_5667());
/* 269 */       if (entry != null) {
/* 270 */         class_2561 displayName = entry.method_2971();
/* 271 */         if (displayName != null) {
/* 272 */           tabName = stripFormatting(displayName.getString());
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 277 */     String protectedPlain = protectName(plainName);
/* 278 */     String protectedScoreboard = protectName(scoreboardName);
/* 279 */     String protectedProfile = protectName(profileName);
/* 280 */     String protectedTab = protectName(tabName);
/*     */     
/* 282 */     return dedupeNames(new String[] { plainName, protectedPlain, scoreboardName, protectedScoreboard, profileName, protectedProfile, tabName, protectedTab });
/*     */   }
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
/*     */   private static String[] dedupeNames(String... names) {
/* 295 */     LinkedHashSet<String> unique = new LinkedHashSet<>();
/* 296 */     for (String name : names) {
/* 297 */       if (name != null) {
/*     */ 
/*     */         
/* 300 */         String trimmed = name.trim();
/* 301 */         if (!trimmed.isEmpty())
/* 302 */           unique.add(trimmed); 
/*     */       } 
/*     */     } 
/* 305 */     return (String[])unique.toArray(x$0 -> new String[x$0]);
/*     */   }
/*     */   
/*     */   private static String protectName(String input) {
/* 309 */     if (input == null || input.isEmpty()) {
/* 310 */       return "";
/*     */     }
/* 312 */     NameProtect nameProtect = (ModuleClass.INSTANCE != null) ? ModuleClass.nameProtect : null;
/* 313 */     if (nameProtect == null || !nameProtect.isEnable()) {
/* 314 */       return input;
/*     */     }
/* 316 */     return nameProtect.patch(input);
/*     */   }
/*     */   
/*     */   private static int getNameMatchScore(String lineText, String ownerText, String[] nameVariants) {
/* 320 */     int best = -1;
/*     */     
/* 322 */     for (String name : nameVariants) {
/* 323 */       if (name != null && !name.isEmpty()) {
/*     */ 
/*     */ 
/*     */         
/* 327 */         if (!ownerText.isEmpty()) {
/* 328 */           if (ownerText.equalsIgnoreCase(name)) {
/* 329 */             best = Math.max(best, 100);
/* 330 */           } else if (ownerText.contains(name)) {
/* 331 */             best = Math.max(best, 80);
/*     */           } 
/*     */         }
/*     */         
/* 335 */         if (!lineText.isEmpty())
/*     */         {
/*     */ 
/*     */           
/* 339 */           if (lineText.equalsIgnoreCase(name)) {
/* 340 */             best = Math.max(best, 95);
/*     */ 
/*     */           
/*     */           }
/* 344 */           else if (lineText.endsWith(name) || lineText.endsWith(" " + name)) {
/* 345 */             best = Math.max(best, 90);
/*     */           }
/*     */           else {
/*     */             
/* 349 */             int index = indexOfIgnoreCase(lineText, name);
/* 350 */             if (index >= 0)
/* 351 */               best = Math.max(best, 70 + Math.min(20, name.length())); 
/*     */           }  } 
/*     */       } 
/*     */     } 
/* 355 */     return best;
/*     */   }
/*     */   
/*     */   private static int indexOfIgnoreCase(String text, String search) {
/* 359 */     if (text == null || search == null || search.isEmpty()) {
/* 360 */       return -1;
/*     */     }
/* 362 */     int limit = text.length() - search.length();
/* 363 */     for (int i = 0; i <= limit; i++) {
/* 364 */       if (text.regionMatches(true, i, search, 0, search.length())) {
/* 365 */         return i;
/*     */       }
/*     */     } 
/* 368 */     return -1;
/*     */   }
/*     */   
/*     */   private static Float parseHpNumber(String text) {
/* 372 */     if (text == null || text.isEmpty()) {
/* 373 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 382 */     String cleaned = text.replace("❤", "").replace("♥", "").replace("HP", "").replace("hp", "").replace("хп", "").replace("Хп", "").trim();
/*     */     
/* 384 */     Matcher matcher = HP_NUMBER.matcher(cleaned);
/* 385 */     if (!matcher.find()) {
/* 386 */       return null;
/*     */     }
/*     */     
/*     */     try {
/* 390 */       float value = Float.parseFloat(matcher.group(1).replace(',', '.'));
/* 391 */       return isReasonableHp(value) ? Float.valueOf(value) : null;
/* 392 */     } catch (NumberFormatException ignored) {
/* 393 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Float parseHpFromLine(String line) {
/* 398 */     if (line == null || line.isEmpty()) {
/* 399 */       return null;
/*     */     }
/*     */     
/* 402 */     Matcher matcher = HP_NUMBER.matcher(line);
/* 403 */     Float last = null;
/* 404 */     while (matcher.find()) {
/*     */       try {
/* 406 */         float value = Float.parseFloat(matcher.group(1).replace(',', '.'));
/* 407 */         if (isReasonableHp(value)) {
/* 408 */           last = Float.valueOf(value);
/*     */         }
/* 410 */       } catch (NumberFormatException numberFormatException) {}
/*     */     } 
/*     */     
/* 413 */     return last;
/*     */   }
/*     */   
/*     */   private static boolean isReasonableHp(float value) {
/* 417 */     return (value >= 0.0F && value <= 1024.0F);
/*     */   }
/*     */   
/*     */   private static boolean isReasonableHp(int value) {
/* 421 */     return (value >= 0 && value <= 1024);
/*     */   }
/*     */   
/*     */   private static String stripFormatting(String text) {
/* 425 */     if (text == null) {
/* 426 */       return "";
/*     */     }
/* 428 */     return text.replaceAll("§[0-9a-fk-orx]", "")
/* 429 */       .replaceAll("(?i)§x(§[0-9a-f]){6}", "")
/* 430 */       .trim();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\client\modules\impl\misc\ScoreboardHP.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */