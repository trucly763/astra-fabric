/*    */ package shame.astra.mixin;
/*    */ 
/*    */ import com.llamalad7.mixinextras.sugar.Local;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.ParseResults;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.class_2172;
/*    */ import net.minecraft.class_342;
/*    */ import net.minecraft.class_4717;
/*    */ import org.spongepowered.asm.mixin.Final;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.Shadow;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ import shame.astra.astra;
/*    */ 
/*    */ @Mixin({class_4717.class})
/*    */ public abstract class ChatInputSuggestorMixin
/*    */ {
/*    */   @Final
/*    */   @Shadow
/*    */   class_342 field_21599;
/*    */   @Shadow
/*    */   boolean field_21614;
/*    */   @Shadow
/*    */   private ParseResults<class_2172> field_21610;
/*    */   
/*    */   @Inject(method = {"method_23934"}, at = {@At(value = "INVOKE", target = "Lcom/mojang/brigadier/StringReader;canRead()Z", remap = false)}, cancellable = true)
/*    */   public void refresh(CallbackInfo ci, @Local StringReader reader) {
/* 33 */     String prefix = astra.INSTANCE.commandStorage.getPrefix();
/*    */     
/* 35 */     if (reader.canRead(prefix.length()) && reader.getString().startsWith(prefix, reader.getCursor())) {
/*    */       
/* 37 */       reader.setCursor(reader.getCursor() + prefix.length());
/* 38 */       CommandDispatcher<class_2172> dispatcher = astra.INSTANCE.commandStorage.getDispatcher();
/* 39 */       if (this.field_21610 == null) this.field_21610 = dispatcher.parse(reader, astra.INSTANCE.commandStorage.getSource());  int cursor;
/* 40 */       if ((cursor = this.field_21599.method_1881()) >= 1 && (this.field_21612 == null || !this.field_21614)) {
/* 41 */         this.field_21611 = dispatcher.getCompletionSuggestions(this.field_21610, cursor);
/* 42 */         this.field_21611.thenRun(() -> {
/*    */               if (this.field_21611.isDone())
/*    */                 method_23920(false); 
/*    */             });
/*    */       } 
/* 47 */       ci.cancel();
/*    */     } 
/*    */   }
/*    */   
/*    */   @Shadow
/*    */   private CompletableFuture<Suggestions> field_21611;
/*    */   @Shadow
/*    */   private class_4717.class_464 field_21612;
/*    */   
/*    */   @Shadow
/*    */   public abstract void method_23920(boolean paramBoolean);
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\mixin\ChatInputSuggestorMixin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */