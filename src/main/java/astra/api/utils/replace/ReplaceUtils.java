/*     */ package shame.astra.api.utils.replace;
/*     */ import net.minecraft.class_124;
/*     */ import net.minecraft.class_2561;
/*     */ import net.minecraft.class_5250;
/*     */ 
/*     */ public class ReplaceUtils {
/*     */   public static class_2561 replace(class_2561 input, String target, String replacement) {
/*   8 */     if (input == null || target == null || replacement == null) return input; 
/*   9 */     class_5250 result = class_2561.method_43473().method_10862(input.method_10866());
/*  10 */     appendReplaced(result, input, target, replacement);
/*  11 */     return (class_2561)result;
/*     */   }
/*     */   
/*     */   private static void appendReplaced(class_5250 result, class_2561 current, String target, String replacement) {
/*  15 */     class_7417 content = current.method_10851();
/*  16 */     class_2583 style = current.method_10866();
/*     */     
/*  18 */     if (content instanceof class_8828.class_2585) { class_8828.class_2585 literal = (class_8828.class_2585)content;
/*  19 */       Pattern pattern = Pattern.compile(Pattern.quote(target), 2);
/*  20 */       String replaced = pattern.matcher(literal.comp_737()).replaceAll(replacement);
/*  21 */       result.method_10852((class_2561)class_2561.method_43470(replaced).method_10862(style)); }
/*     */ 
/*     */     
/*  24 */     for (class_2561 sibling : current.method_10855()) {
/*  25 */       appendReplaced(result, sibling, target, replacement);
/*     */     }
/*     */   }
/*     */   
/*     */   public static String replaceSymbols(String string) {
/*  30 */     return string
/*  31 */       .replaceAll("ꔗ", String.valueOf(class_124.field_1078) + "MODER")
/*  32 */       .replaceAll("ꔥ", String.valueOf(class_124.field_1078) + "ST.MODER")
/*  33 */       .replaceAll("ꔡ", String.valueOf(class_124.field_1076) + "MODER+")
/*  34 */       .replaceAll("ꔀ", String.valueOf(class_124.field_1080) + "PLAYER")
/*  35 */       .replaceAll("ꔉ", String.valueOf(class_124.field_1054) + "HELPER")
/*  36 */       .replaceAll("◆", "@")
/*  37 */       .replaceAll("┃", "|")
/*  38 */       .replaceAll("ꕆ", String.valueOf(class_124.field_1054) + "PEGAS")
/*  39 */       .replaceAll("ꔸ", String.valueOf(class_124.field_1054) + "GOD")
/*  40 */       .replaceAll("ꔳ", String.valueOf(class_124.field_1075) + "Ml.admin")
/*  41 */       .replaceAll("ꔅ", String.valueOf(class_124.field_1061) + "Y" + String.valueOf(class_124.field_1061) + "T")
/*  42 */       .replaceAll("ꔂ", String.valueOf(class_124.field_1078) + "D.MODER")
/*  43 */       .replaceAll("ꕠ", String.valueOf(class_124.field_1054) + "D.HELPER")
/*  44 */       .replaceAll("ꕄ", String.valueOf(class_124.field_1061) + "VAMPIRE")
/*  45 */       .replaceAll("ꔖ", String.valueOf(class_124.field_1075) + "OVERLORD")
/*  46 */       .replaceAll("ꕈ", String.valueOf(class_124.field_1060) + "COBRA")
/*  47 */       .replaceAll("ꔨ", String.valueOf(class_124.field_1076) + "DRAGON")
/*  48 */       .replaceAll("ꔤ", String.valueOf(class_124.field_1061) + "IMPERATOR")
/*  49 */       .replaceAll("ꔠ", String.valueOf(class_124.field_1065) + "MAGISTER")
/*  50 */       .replaceAll("ꔄ", String.valueOf(class_124.field_1078) + "HERO")
/*  51 */       .replaceAll("ꔒ", String.valueOf(class_124.field_1060) + "AVENGER")
/*  52 */       .replaceAll("ꕒ", String.valueOf(class_124.field_1068) + "RABBIT")
/*  53 */       .replaceAll("ꔈ", String.valueOf(class_124.field_1054) + "TITAN")
/*  54 */       .replaceAll("ꕀ", String.valueOf(class_124.field_1077) + "HYDRA")
/*  55 */       .replaceAll("ꔶ", String.valueOf(class_124.field_1065) + "TIGER")
/*  56 */       .replaceAll("ꔲ", String.valueOf(class_124.field_1064) + "BULL")
/*  57 */       .replaceAll("ꕖ", String.valueOf(class_124.field_1074) + "BUNNY")
/*  58 */       .replaceAll("ꕗꕘ", String.valueOf(class_124.field_1054) + "SPONSOR")
/*  59 */       .replaceAll("🔥", "@")
/*  60 */       .replaceAll("ᴀ", "A")
/*  61 */       .replaceAll("ʙ", "B")
/*  62 */       .replaceAll("ᴄ", "C")
/*  63 */       .replaceAll("ᴅ", "D")
/*  64 */       .replaceAll("ᴇ", "E")
/*  65 */       .replaceAll("ғ", "F")
/*  66 */       .replaceAll("ɢ", "G")
/*  67 */       .replaceAll("ʜ", "H")
/*  68 */       .replaceAll("ɪ", "I")
/*  69 */       .replaceAll("ᴊ", "J")
/*  70 */       .replaceAll("ᴋ", "K")
/*  71 */       .replaceAll("ʟ", "L")
/*  72 */       .replaceAll("ᴍ", "M")
/*  73 */       .replaceAll("ɴ", "N")
/*  74 */       .replaceAll("ꜱ", "S")
/*  75 */       .replaceAll("s", "S")
/*  76 */       .replaceAll("ᴏ", "O")
/*  77 */       .replaceAll("ᴘ", "P")
/*  78 */       .replaceAll("ǫ", "Q")
/*  79 */       .replaceAll("ʀ", "R")
/*  80 */       .replaceAll("ᴛ", "T")
/*  81 */       .replaceAll("ᴜ", "U")
/*  82 */       .replaceAll("ᴠ", "V")
/*  83 */       .replaceAll("ᴡ", "W")
/*  84 */       .replaceAll("ꜰ", "F")
/*  85 */       .replaceAll("x", "X")
/*  86 */       .replaceAll("ʏ", "Y")
/*  87 */       .replaceAll("ᴢ", "Z");
/*     */   }
/*     */   
/*     */   public static class_2561 replaceSymbols(class_2561 text) {
/*  91 */     if (text.getString().contains("ꔗ")) text = replace(text, "ꔗ", String.valueOf(class_124.field_1078) + "MODER"); 
/*  92 */     if (text.getString().contains("ꔥ")) text = replace(text, "ꔥ", String.valueOf(class_124.field_1078) + "ST.MODER"); 
/*  93 */     if (text.getString().contains("ꔡ")) text = replace(text, "ꔡ", String.valueOf(class_124.field_1076) + "MODER+"); 
/*  94 */     if (text.getString().contains("ꔀ")) text = replace(text, "ꔀ", String.valueOf(class_124.field_1080) + "PLAYER"); 
/*  95 */     if (text.getString().contains("ꔉ")) text = replace(text, "ꔉ", String.valueOf(class_124.field_1054) + "HELPER"); 
/*  96 */     if (text.getString().contains("◆")) text = replace(text, "◆", "@"); 
/*  97 */     if (text.getString().contains("┃")) text = replace(text, "┃", "|"); 
/*  98 */     if (text.getString().contains("ꔳ")) text = replace(text, "ꔳ", String.valueOf(class_124.field_1075) + "Ml.admin"); 
/*  99 */     if (text.getString().contains("ꔅ")) text = replace(text, "ꔅ", String.valueOf(class_124.field_1061) + "Y" + String.valueOf(class_124.field_1061) + "T"); 
/* 100 */     if (text.getString().contains("ꔂ")) text = replace(text, "ꔂ", String.valueOf(class_124.field_1078) + "D.MODER"); 
/* 101 */     if (text.getString().contains("ꕠ")) text = replace(text, "ꕠ", String.valueOf(class_124.field_1054) + "D.HELPER"); 
/* 102 */     if (text.getString().contains("ꕄ")) text = replace(text, "ꕄ", String.valueOf(class_124.field_1061) + "DRACULA"); 
/* 103 */     if (text.getString().contains("ꔖ")) text = replace(text, "ꔖ", String.valueOf(class_124.field_1075) + "OVERLORD"); 
/* 104 */     if (text.getString().contains("ꕈ")) text = replace(text, "ꕈ", String.valueOf(class_124.field_1060) + "COBRA"); 
/* 105 */     if (text.getString().contains("ꔨ")) text = replace(text, "ꔨ", String.valueOf(class_124.field_1076) + "DRAGON"); 
/* 106 */     if (text.getString().contains("ꔤ")) text = replace(text, "ꔤ", String.valueOf(class_124.field_1061) + "IMPERATOR"); 
/* 107 */     if (text.getString().contains("ꔠ")) text = replace(text, "ꔠ", String.valueOf(class_124.field_1065) + "MAGISTER"); 
/* 108 */     if (text.getString().contains("ꔄ")) text = replace(text, "ꔄ", String.valueOf(class_124.field_1078) + "HERO"); 
/* 109 */     if (text.getString().contains("ꔒ")) text = replace(text, "ꔒ", String.valueOf(class_124.field_1060) + "AVENGER"); 
/* 110 */     if (text.getString().contains("ꕒ")) text = replace(text, "ꕒ", String.valueOf(class_124.field_1068) + "RABBIT"); 
/* 111 */     if (text.getString().contains("ꔈ")) text = replace(text, "ꔈ", String.valueOf(class_124.field_1054) + "TITAN"); 
/* 112 */     if (text.getString().contains("ꕀ")) text = replace(text, "ꕀ", String.valueOf(class_124.field_1077) + "HYDRA"); 
/* 113 */     if (text.getString().contains("ꔶ")) text = replace(text, "ꔶ", String.valueOf(class_124.field_1065) + "TIGER"); 
/* 114 */     if (text.getString().contains("ꔲ")) text = replace(text, "ꔲ", String.valueOf(class_124.field_1064) + "BULL"); 
/* 115 */     if (text.getString().contains("ꕖ")) text = replace(text, "ꕖ", String.valueOf(class_124.field_1074) + "BUNNY"); 
/* 116 */     if (text.getString().contains("ꕗꕘ")) text = replace(text, "ꕗꕘ", String.valueOf(class_124.field_1054) + "SPONSOR"); 
/* 117 */     if (text.getString().contains("🔥")) text = replace(text, "🔥", "@");
/*     */     
/* 119 */     if (text.getString().contains("ᴀ")) text = replace(text, "ᴀ", "A"); 
/* 120 */     if (text.getString().contains("ʙ")) text = replace(text, "ʙ", "B"); 
/* 121 */     if (text.getString().contains("ᴄ")) text = replace(text, "ᴄ", "C"); 
/* 122 */     if (text.getString().contains("ᴅ")) text = replace(text, "ᴅ", "D"); 
/* 123 */     if (text.getString().contains("ᴇ")) text = replace(text, "ᴇ", "E"); 
/* 124 */     if (text.getString().contains("ғ")) text = replace(text, "ғ", "F"); 
/* 125 */     if (text.getString().contains("ɢ")) text = replace(text, "ɢ", "G"); 
/* 126 */     if (text.getString().contains("ʜ")) text = replace(text, "ʜ", "H"); 
/* 127 */     if (text.getString().contains("ɪ")) text = replace(text, "ɪ", "I"); 
/* 128 */     if (text.getString().contains("ᴊ")) text = replace(text, "ᴊ", "J"); 
/* 129 */     if (text.getString().contains("ᴋ")) text = replace(text, "ᴋ", "K"); 
/* 130 */     if (text.getString().contains("ʟ")) text = replace(text, "ʟ", "L"); 
/* 131 */     if (text.getString().contains("ᴍ")) text = replace(text, "ᴍ", "M"); 
/* 132 */     if (text.getString().contains("ɴ")) text = replace(text, "ɴ", "N"); 
/* 133 */     if (text.getString().contains("ꜱ")) text = replace(text, "ꜱ", "S"); 
/* 134 */     if (text.getString().contains("s")) text = replace(text, "s", "S"); 
/* 135 */     if (text.getString().contains("ᴏ")) text = replace(text, "ᴏ", "O"); 
/* 136 */     if (text.getString().contains("ᴘ")) text = replace(text, "ᴘ", "P"); 
/* 137 */     if (text.getString().contains("ǫ")) text = replace(text, "ǫ", "Q"); 
/* 138 */     if (text.getString().contains("ʀ")) text = replace(text, "ʀ", "R"); 
/* 139 */     if (text.getString().contains("ᴛ")) text = replace(text, "ᴛ", "T"); 
/* 140 */     if (text.getString().contains("ᴜ")) text = replace(text, "ᴜ", "U"); 
/* 141 */     if (text.getString().contains("ᴠ")) text = replace(text, "ᴠ", "V"); 
/* 142 */     if (text.getString().contains("ᴡ")) text = replace(text, "ᴡ", "W"); 
/* 143 */     if (text.getString().contains("ꜰ")) text = replace(text, "ꜰ", "F"); 
/* 144 */     if (text.getString().contains("x")) text = replace(text, "x", "X"); 
/* 145 */     if (text.getString().contains("ʏ")) text = replace(text, "ʏ", "Y"); 
/* 146 */     if (text.getString().contains("ᴢ")) text = replace(text, "ᴢ", "Z");
/*     */     
/* 148 */     return text;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\replace\ReplaceUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */