/*    */ package shame.astra.api.utils.rpc.utils;
/*    */ import com.sun.jna.Structure;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import shame.astra.api.utils.rpc.callbacks.DisconnectedCallback;
/*    */ import shame.astra.api.utils.rpc.callbacks.ErroredCallback;
/*    */ import shame.astra.api.utils.rpc.callbacks.JoinGameCallback;
/*    */ import shame.astra.api.utils.rpc.callbacks.JoinRequestCallback;
/*    */ import shame.astra.api.utils.rpc.callbacks.ReadyCallback;
/*    */ import shame.astra.api.utils.rpc.callbacks.SpectateGameCallback;
/*    */ 
/*    */ public class DiscordEventHandlers extends Structure {
/*    */   public DisconnectedCallback disconnected;
/*    */   public JoinRequestCallback joinRequest;
/*    */   public SpectateGameCallback spectateGame;
/*    */   
/*    */   protected List<String> getFieldOrder() {
/* 18 */     return Arrays.asList(new String[] { "ready", "disconnected", "errored", "joinGame", "spectateGame", "joinRequest" });
/*    */   }
/*    */   public ReadyCallback ready; public ErroredCallback errored; public JoinGameCallback joinGame;
/*    */   
/* 22 */   public static class Builder { private final DiscordEventHandlers handlers = new DiscordEventHandlers();
/*    */     
/*    */     public DiscordEventHandlers build() {
/* 25 */       return this.handlers;
/*    */     }
/*    */     
/*    */     public Builder disconnected(DisconnectedCallback var1) {
/* 29 */       this.handlers.disconnected = var1;
/* 30 */       return this;
/*    */     }
/*    */     
/*    */     public Builder errored(ErroredCallback var1) {
/* 34 */       this.handlers.errored = var1;
/* 35 */       return this;
/*    */     }
/*    */     
/*    */     public Builder ready(ReadyCallback var1) {
/* 39 */       this.handlers.ready = var1;
/* 40 */       return this;
/*    */     }
/*    */     
/*    */     public Builder joinRequest(JoinRequestCallback var1) {
/* 44 */       this.handlers.joinRequest = var1;
/* 45 */       return this;
/*    */     }
/*    */     
/*    */     public Builder joinGame(JoinGameCallback var1) {
/* 49 */       this.handlers.joinGame = var1;
/* 50 */       return this;
/*    */     }
/*    */     
/*    */     public Builder spectateGame(SpectateGameCallback var1) {
/* 54 */       this.handlers.spectateGame = var1;
/* 55 */       return this;
/*    */     } }
/*    */ 
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\rp\\utils\DiscordEventHandlers.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */