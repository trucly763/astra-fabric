/*     */ package shame.astra.api.utils.render.font;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import shame.astra.api.utils.color.ColorUtils;
/*     */ 
/*     */ public class ReplaceSymbols
/*     */ {
/*   9 */   private static final Map<Integer, String> REPLACEMENTS = new HashMap<>();
/*  10 */   private static final Map<Integer, Integer> RANK_COLORS = new HashMap<>();
/*     */   
/*  12 */   private static final int[] RANKS = new int[] { 42240, 42244, 42248, 42258, 42262, 42272, 42276, 42280, 42336, 42290, 42294, 42308, 42326, 42312, 42304, 42322, 42249, 42259, 42263, 42273, 42277, 42281, 42291, 42295, 42241, 42245, 42313, 4144, 4138, 4132, 4134, 4140, 4139, 4151, 4130, 4148, 4141, 4152, 4133, 4131, 4149, 4150, 4137, 4129, 4145, 4143, 4146, 4135, 4153, 4126 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  22 */     REPLACEMENTS.put(Integer.valueOf(9889), "");
/*  23 */     REPLACEMENTS.put(Integer.valueOf(9733), "");
/*     */     
/*  25 */     REPLACEMENTS.put(Integer.valueOf(42240), "PLAYER");
/*  26 */     REPLACEMENTS.put(Integer.valueOf(42244), "HERO");
/*  27 */     REPLACEMENTS.put(Integer.valueOf(42248), "TITAN");
/*  28 */     REPLACEMENTS.put(Integer.valueOf(42258), "AVENGER");
/*  29 */     REPLACEMENTS.put(Integer.valueOf(42262), "OVERLORD");
/*  30 */     REPLACEMENTS.put(Integer.valueOf(42272), "MAGISTER");
/*  31 */     REPLACEMENTS.put(Integer.valueOf(42276), "IMPERATOR");
/*  32 */     REPLACEMENTS.put(Integer.valueOf(42280), "DRAGON");
/*  33 */     REPLACEMENTS.put(Integer.valueOf(42336), "D.HELPER");
/*  34 */     REPLACEMENTS.put(Integer.valueOf(42290), "BULL");
/*  35 */     REPLACEMENTS.put(Integer.valueOf(42294), "TIGER");
/*  36 */     REPLACEMENTS.put(Integer.valueOf(42308), "VAMPIRE");
/*  37 */     REPLACEMENTS.put(Integer.valueOf(42326), "BUNNY");
/*  38 */     REPLACEMENTS.put(Integer.valueOf(42312), "COBRA");
/*  39 */     REPLACEMENTS.put(Integer.valueOf(42304), "HYDRA");
/*  40 */     REPLACEMENTS.put(Integer.valueOf(42322), "RABBIT");
/*  41 */     REPLACEMENTS.put(Integer.valueOf(42249), "HELPER");
/*  42 */     REPLACEMENTS.put(Integer.valueOf(42259), "ML.MODER");
/*  43 */     REPLACEMENTS.put(Integer.valueOf(42263), "MODER");
/*  44 */     REPLACEMENTS.put(Integer.valueOf(42273), "MODER+");
/*  45 */     REPLACEMENTS.put(Integer.valueOf(42277), "ST.MODER");
/*  46 */     REPLACEMENTS.put(Integer.valueOf(42281), "GL.MODER");
/*  47 */     REPLACEMENTS.put(Integer.valueOf(42291), "ML.ADMIN");
/*  48 */     REPLACEMENTS.put(Integer.valueOf(42295), "ADMIN");
/*  49 */     REPLACEMENTS.put(Integer.valueOf(42241), "MEDIA");
/*  50 */     REPLACEMENTS.put(Integer.valueOf(42245), "YT");
/*  51 */     REPLACEMENTS.put(Integer.valueOf(42305), "GOD");
/*  52 */     REPLACEMENTS.put(Integer.valueOf(4144), "HERO");
/*  53 */     REPLACEMENTS.put(Integer.valueOf(4138), "TITAN");
/*  54 */     REPLACEMENTS.put(Integer.valueOf(4132), "PRINCE");
/*  55 */     REPLACEMENTS.put(Integer.valueOf(4134), "PHOENIX");
/*  56 */     REPLACEMENTS.put(Integer.valueOf(4140), "OVERLORD");
/*  57 */     REPLACEMENTS.put(Integer.valueOf(4139), "GUARDIAN");
/*  58 */     REPLACEMENTS.put(Integer.valueOf(4151), "KRATOS");
/*  59 */     REPLACEMENTS.put(Integer.valueOf(4130), "PHANTOM");
/*  60 */     REPLACEMENTS.put(Integer.valueOf(4148), "CUSTOM");
/*  61 */     REPLACEMENTS.put(Integer.valueOf(4141), "WINTER");
/*  62 */     REPLACEMENTS.put(Integer.valueOf(4152), "SAKURA");
/*  63 */     REPLACEMENTS.put(Integer.valueOf(4133), "SUMMER");
/*  64 */     REPLACEMENTS.put(Integer.valueOf(4131), "HALLOWEEN");
/*  65 */     REPLACEMENTS.put(Integer.valueOf(4149), "TIKTOK");
/*  66 */     REPLACEMENTS.put(Integer.valueOf(4150), "TIKTOK+");
/*  67 */     REPLACEMENTS.put(Integer.valueOf(4137), "MEDIA");
/*  68 */     REPLACEMENTS.put(Integer.valueOf(4129), "YOUTUBE");
/*  69 */     REPLACEMENTS.put(Integer.valueOf(4145), "HELPER");
/*  70 */     REPLACEMENTS.put(Integer.valueOf(4143), "ML.ADMIN");
/*  71 */     REPLACEMENTS.put(Integer.valueOf(4146), "MODER");
/*  72 */     REPLACEMENTS.put(Integer.valueOf(4135), "CURATOR");
/*  73 */     REPLACEMENTS.put(Integer.valueOf(4153), "SPECTATOR");
/*  74 */     REPLACEMENTS.put(Integer.valueOf(4126), "DEVELOPER");
/*     */     
/*  76 */     REPLACEMENTS.put(Integer.valueOf(7424), "A");
/*  77 */     REPLACEMENTS.put(Integer.valueOf(665), "B");
/*  78 */     REPLACEMENTS.put(Integer.valueOf(7428), "C");
/*  79 */     REPLACEMENTS.put(Integer.valueOf(7429), "D");
/*  80 */     REPLACEMENTS.put(Integer.valueOf(7431), "E");
/*  81 */     REPLACEMENTS.put(Integer.valueOf(42800), "F");
/*  82 */     REPLACEMENTS.put(Integer.valueOf(610), "G");
/*  83 */     REPLACEMENTS.put(Integer.valueOf(668), "H");
/*  84 */     REPLACEMENTS.put(Integer.valueOf(618), "I");
/*  85 */     REPLACEMENTS.put(Integer.valueOf(7434), "J");
/*  86 */     REPLACEMENTS.put(Integer.valueOf(7435), "K");
/*  87 */     REPLACEMENTS.put(Integer.valueOf(671), "L");
/*  88 */     REPLACEMENTS.put(Integer.valueOf(7437), "M");
/*  89 */     REPLACEMENTS.put(Integer.valueOf(628), "N");
/*  90 */     REPLACEMENTS.put(Integer.valueOf(7439), "O");
/*  91 */     REPLACEMENTS.put(Integer.valueOf(7448), "P");
/*  92 */     REPLACEMENTS.put(Integer.valueOf(491), "Q");
/*  93 */     REPLACEMENTS.put(Integer.valueOf(640), "R");
/*  94 */     REPLACEMENTS.put(Integer.valueOf(7451), "T");
/*  95 */     REPLACEMENTS.put(Integer.valueOf(7452), "U");
/*  96 */     REPLACEMENTS.put(Integer.valueOf(42801), "S");
/*  97 */     REPLACEMENTS.put(Integer.valueOf(7456), "V");
/*  98 */     REPLACEMENTS.put(Integer.valueOf(7457), "W");
/*  99 */     REPLACEMENTS.put(Integer.valueOf(7521), "X");
/* 100 */     REPLACEMENTS.put(Integer.valueOf(655), "Y");
/* 101 */     REPLACEMENTS.put(Integer.valueOf(7458), "Z");
/*     */     
/* 103 */     RANK_COLORS.put(Integer.valueOf(42240), Integer.valueOf(ColorUtils.rgb(141, 143, 141)));
/* 104 */     RANK_COLORS.put(Integer.valueOf(42244), Integer.valueOf(ColorUtils.rgb(100, 113, 251)));
/* 105 */     RANK_COLORS.put(Integer.valueOf(42248), Integer.valueOf(ColorUtils.rgb(245, 220, 29)));
/* 106 */     RANK_COLORS.put(Integer.valueOf(42258), Integer.valueOf(ColorUtils.rgb(79, 201, 83)));
/* 107 */     RANK_COLORS.put(Integer.valueOf(42262), Integer.valueOf(ColorUtils.rgb(85, 255, 255)));
/* 108 */     RANK_COLORS.put(Integer.valueOf(42272), Integer.valueOf(ColorUtils.rgb(224, 138, 52)));
/* 109 */     RANK_COLORS.put(Integer.valueOf(42276), Integer.valueOf(ColorUtils.rgb(202, 60, 60)));
/* 110 */     RANK_COLORS.put(Integer.valueOf(42280), Integer.valueOf(ColorUtils.rgb(245, 51, 238)));
/* 111 */     RANK_COLORS.put(Integer.valueOf(42336), Integer.valueOf(ColorUtils.rgb(214, 200, 42)));
/* 112 */     RANK_COLORS.put(Integer.valueOf(42290), Integer.valueOf(ColorUtils.rgb(121, 81, 202)));
/* 113 */     RANK_COLORS.put(Integer.valueOf(42294), Integer.valueOf(ColorUtils.rgb(202, 130, 60)));
/* 114 */     RANK_COLORS.put(Integer.valueOf(42308), Integer.valueOf(ColorUtils.rgb(202, 60, 60)));
/* 115 */     RANK_COLORS.put(Integer.valueOf(42326), Integer.valueOf(ColorUtils.rgb(68, 65, 66)));
/* 116 */     RANK_COLORS.put(Integer.valueOf(42312), Integer.valueOf(ColorUtils.rgb(127, 214, 86)));
/* 117 */     RANK_COLORS.put(Integer.valueOf(42304), Integer.valueOf(ColorUtils.rgb(92, 120, 7)));
/* 118 */     RANK_COLORS.put(Integer.valueOf(42322), Integer.valueOf(ColorUtils.rgb(230, 232, 230)));
/* 119 */     RANK_COLORS.put(Integer.valueOf(42249), Integer.valueOf(ColorUtils.rgb(214, 200, 42)));
/* 120 */     RANK_COLORS.put(Integer.valueOf(42259), Integer.valueOf(ColorUtils.rgb(100, 113, 251)));
/* 121 */     RANK_COLORS.put(Integer.valueOf(42263), Integer.valueOf(ColorUtils.rgb(100, 113, 251)));
/* 122 */     RANK_COLORS.put(Integer.valueOf(42273), Integer.valueOf(ColorUtils.rgb(121, 81, 202)));
/* 123 */     RANK_COLORS.put(Integer.valueOf(42277), Integer.valueOf(ColorUtils.rgb(100, 113, 251)));
/* 124 */     RANK_COLORS.put(Integer.valueOf(42281), Integer.valueOf(ColorUtils.rgb(121, 81, 202)));
/* 125 */     RANK_COLORS.put(Integer.valueOf(42291), Integer.valueOf(ColorUtils.rgb(64, 151, 214)));
/* 126 */     RANK_COLORS.put(Integer.valueOf(42295), Integer.valueOf(ColorUtils.rgb(202, 60, 60)));
/* 127 */     RANK_COLORS.put(Integer.valueOf(42241), Integer.valueOf(ColorUtils.rgb(121, 81, 202)));
/* 128 */     RANK_COLORS.put(Integer.valueOf(42245), Integer.valueOf(ColorUtils.rgb(255, 255, 255)));
/* 129 */     RANK_COLORS.put(Integer.valueOf(42305), Integer.valueOf(ColorUtils.rgb(245, 198, 29)));
/* 130 */     RANK_COLORS.put(Integer.valueOf(42313), Integer.valueOf(ColorUtils.rgb(202, 130, 60)));
/* 131 */     RANK_COLORS.put(Integer.valueOf(4144), Integer.valueOf(ColorUtils.rgb(13, 176, 209)));
/* 132 */     RANK_COLORS.put(Integer.valueOf(4138), Integer.valueOf(ColorUtils.rgb(21, 232, 24)));
/* 133 */     RANK_COLORS.put(Integer.valueOf(4132), Integer.valueOf(ColorUtils.rgb(232, 169, 21)));
/* 134 */     RANK_COLORS.put(Integer.valueOf(4134), Integer.valueOf(ColorUtils.rgb(237, 215, 19)));
/* 135 */     RANK_COLORS.put(Integer.valueOf(4140), Integer.valueOf(ColorUtils.rgb(64, 163, 152)));
/* 136 */     RANK_COLORS.put(Integer.valueOf(4139), Integer.valueOf(ColorUtils.rgb(86, 196, 99)));
/* 137 */     RANK_COLORS.put(Integer.valueOf(4151), Integer.valueOf(ColorUtils.rgb(147, 46, 230)));
/* 138 */     RANK_COLORS.put(Integer.valueOf(4130), Integer.valueOf(ColorUtils.rgb(230, 46, 46)));
/* 139 */     RANK_COLORS.put(Integer.valueOf(4148), Integer.valueOf(ColorUtils.rgb(16, 35, 179)));
/* 140 */     RANK_COLORS.put(Integer.valueOf(4141), Integer.valueOf(ColorUtils.rgb(55, 154, 184)));
/* 141 */     RANK_COLORS.put(Integer.valueOf(4152), Integer.valueOf(ColorUtils.rgb(184, 39, 159)));
/* 142 */     RANK_COLORS.put(Integer.valueOf(4133), Integer.valueOf(ColorUtils.rgb(255, 182, 56)));
/* 143 */     RANK_COLORS.put(Integer.valueOf(4131), Integer.valueOf(ColorUtils.rgb(232, 60, 30)));
/* 144 */     RANK_COLORS.put(Integer.valueOf(4149), Integer.valueOf(ColorUtils.rgb(0, 0, 0)));
/* 145 */     RANK_COLORS.put(Integer.valueOf(4150), Integer.valueOf(ColorUtils.rgb(0, 0, 0)));
/* 146 */     RANK_COLORS.put(Integer.valueOf(4137), Integer.valueOf(ColorUtils.rgb(37, 232, 30)));
/* 147 */     RANK_COLORS.put(Integer.valueOf(4129), Integer.valueOf(ColorUtils.rgb(232, 30, 30)));
/* 148 */     RANK_COLORS.put(Integer.valueOf(4145), Integer.valueOf(ColorUtils.rgb(30, 134, 232)));
/* 149 */     RANK_COLORS.put(Integer.valueOf(4143), Integer.valueOf(ColorUtils.rgb(89, 167, 227)));
/* 150 */     RANK_COLORS.put(Integer.valueOf(4146), Integer.valueOf(ColorUtils.rgb(62, 137, 194)));
/* 151 */     RANK_COLORS.put(Integer.valueOf(4135), Integer.valueOf(ColorUtils.rgb(56, 235, 74)));
/* 152 */     RANK_COLORS.put(Integer.valueOf(4153), Integer.valueOf(ColorUtils.rgb(173, 184, 174)));
/* 153 */     RANK_COLORS.put(Integer.valueOf(4126), Integer.valueOf(ColorUtils.rgb(255, 0, 25)));
/*     */   }
/*     */ 
/*     */   
/*     */   public static String replaceCodePoint(int codePoint) {
/* 158 */     return REPLACEMENTS.get(Integer.valueOf(codePoint));
/*     */   }
/*     */   
/*     */   public static int getGradientColorForReplacement(int codePoint, int charIndex, int totalChars, float alpha, int currentColor) {
/* 162 */     if (isRank(codePoint)) {
/* 163 */       Integer baseColor = RANK_COLORS.get(Integer.valueOf(codePoint));
/* 164 */       if (baseColor == null) {
/* 165 */         return withOpacity(currentColor, alpha);
/*     */       }
/* 167 */       int endColor = ColorUtils.darken(baseColor.intValue(), 0.8F);
/* 168 */       float ratio = (totalChars <= 1) ? 1.0F : (charIndex / (totalChars - 1));
/* 169 */       int interpolatedColor = ColorUtils.interpolateColor(endColor, baseColor.intValue(), ratio);
/* 170 */       return withOpacity(interpolatedColor, alpha);
/*     */     } 
/* 172 */     return withOpacity(currentColor, alpha);
/*     */   }
/*     */   
/*     */   private static boolean isRank(int codePoint) {
/* 176 */     for (int rank : RANKS) {
/* 177 */       if (rank == codePoint) return true; 
/*     */     } 
/* 179 */     return false;
/*     */   }
/*     */   
/*     */   private static int withOpacity(int color, float alpha) {
/* 183 */     int a = Math.max(0, Math.min(255, (int)(alpha * 255.0F)));
/* 184 */     return ColorUtils.setAlphaColor(color, a);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\render\font\ReplaceSymbols.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */