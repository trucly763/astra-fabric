/*     */ package shame.astra.api.utils.rpc.utils;
/*     */ 
/*     */ import java.time.OffsetDateTime;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ public class Builder
/*     */ {
/*  41 */   private final DiscordRichPresence richPresence = new DiscordRichPresence();
/*     */   
/*     */   public Builder setSmallImage(String var1) {
/*  44 */     return setSmallImage(var1, "");
/*     */   }
/*     */   
/*     */   public Builder setDetails(String var1) {
/*  48 */     if (var1 != null && !var1.isEmpty()) {
/*  49 */       this.richPresence.details = var1.substring(0, Math.min(var1.length(), 128));
/*     */     }
/*     */     
/*  52 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setLargeImage(String var1, String var2) {
/*  56 */     this.richPresence.largeImageKey = var1;
/*  57 */     this.richPresence.largeImageText = var2;
/*  58 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setState(String var1) {
/*  62 */     if (var1 != null && !var1.isEmpty()) {
/*  63 */       this.richPresence.state = var1.substring(0, Math.min(var1.length(), 128));
/*     */     }
/*     */     
/*  66 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setInstance(boolean var1) {
/*  70 */     if ((this.richPresence.button_label_1 == null || !this.richPresence.button_label_1.isEmpty()) && (this.richPresence.button_label_2 == null || !this.richPresence.button_label_2.isEmpty())) {
/*  71 */       this.richPresence.instance = var1 ? 1 : 0;
/*     */     }
/*  73 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setButtons(RPCButton var1) {
/*  77 */     return setButtons(Collections.singletonList(var1));
/*     */   }
/*     */   
/*     */   public Builder setSmallImage(String var1, String var2) {
/*  81 */     this.richPresence.smallImageKey = var1;
/*  82 */     this.richPresence.smallImageText = var2;
/*  83 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setParty(String var1, int var2, int var3) {
/*  87 */     if ((this.richPresence.button_label_1 == null || !this.richPresence.button_label_1.isEmpty()) && (this.richPresence.button_label_2 == null || !this.richPresence.button_label_2.isEmpty())) {
/*  88 */       this.richPresence.partyId = var1;
/*  89 */       this.richPresence.partySize = var2;
/*  90 */       this.richPresence.partyMax = var3;
/*     */     } 
/*  92 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setButtons(List<RPCButton> var1) {
/*  96 */     if (var1 != null && !var1.isEmpty()) {
/*  97 */       int var2 = Math.min(var1.size(), 2);
/*  98 */       this.richPresence.button_label_1 = ((RPCButton)var1.get(0)).getLabel();
/*  99 */       this.richPresence.button_url_1 = ((RPCButton)var1.get(0)).getUrl();
/* 100 */       if (var2 == 2) {
/* 101 */         this.richPresence.button_label_2 = ((RPCButton)var1.get(1)).getLabel();
/* 102 */         this.richPresence.button_url_2 = ((RPCButton)var1.get(1)).getUrl();
/*     */       } 
/*     */     } 
/*     */     
/* 106 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setStartTimestamp(OffsetDateTime var1) {
/* 110 */     this.richPresence.startTimestamp = var1.toEpochSecond();
/* 111 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setSecrets(String var1, String var2, String var3) {
/* 115 */     if ((this.richPresence.button_label_1 == null || !this.richPresence.button_label_1.isEmpty()) && (this.richPresence.button_label_2 == null || !this.richPresence.button_label_2.isEmpty())) {
/* 116 */       this.richPresence.matchSecret = var1;
/* 117 */       this.richPresence.joinSecret = var2;
/* 118 */       this.richPresence.spectateSecret = var3;
/*     */     } 
/* 120 */     return this;
/*     */   }
/*     */   
/*     */   public void setButtons(RPCButton var1, RPCButton var2) {
/* 124 */     setButtons(Arrays.asList(new RPCButton[] { var1, var2 }));
/*     */   }
/*     */   
/*     */   public void setStartTimestamp(long var1) {
/* 128 */     this.richPresence.startTimestamp = var1;
/*     */   }
/*     */   
/*     */   public Builder setSecrets(String var1, String var2) {
/* 132 */     if ((this.richPresence.button_label_1 == null || !this.richPresence.button_label_1.isEmpty()) && (this.richPresence.button_label_2 == null || !this.richPresence.button_label_2.isEmpty())) {
/* 133 */       this.richPresence.joinSecret = var1;
/* 134 */       this.richPresence.spectateSecret = var2;
/*     */     } 
/* 136 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setEndTimestamp(long var1) {
/* 140 */     this.richPresence.endTimestamp = var1;
/* 141 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setEndTimestamp(OffsetDateTime var1) {
/* 145 */     this.richPresence.endTimestamp = var1.toEpochSecond();
/* 146 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setLargeImage(String var1) {
/* 150 */     return setLargeImage(var1, "");
/*     */   }
/*     */   
/*     */   public DiscordRichPresence build() {
/* 154 */     return this.richPresence;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\rp\\utils\DiscordRichPresence$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */