/*    */ package shame.astra.api.commands.impl;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.class_1922;
/*    */ import net.minecraft.class_2172;
/*    */ import net.minecraft.class_2338;
/*    */ import net.minecraft.class_2350;
/*    */ import net.minecraft.class_265;
/*    */ import net.minecraft.class_2680;
/*    */ import shame.astra.api.commands.Command;
/*    */ 
/*    */ public class TPCommand extends Command {
/*    */   public TPCommand() {
/* 17 */     super("tp");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(LiteralArgumentBuilder<class_2172> builder) {
/* 23 */     builder.then(arg("Y", (ArgumentType)IntegerArgumentType.integer())
/* 24 */         .executes(context -> {
/*    */             int y = ((Integer)context.getArgument("Y", Integer.class)).intValue();
/*    */             
/*    */             mc.field_1724.method_5814(mc.field_1724.method_23317(), mc.field_1724.method_23318() + y, mc.field_1724.method_23321());
/*    */             
/*    */             return 1;
/*    */           }));
/* 31 */     builder.then(literal("up")
/* 32 */         .executes(context -> {
/*    */             clipToSafeBlock(true);
/*    */             
/*    */             return 1;
/*    */           }));
/*    */     
/* 38 */     builder.then(literal("down")
/* 39 */         .executes(context -> {
/*    */             clipToSafeBlock(false);
/*    */             return 1;
/*    */           }));
/*    */   }
/*    */ 
/*    */   
/*    */   private void clipToSafeBlock(boolean up) {
/* 47 */     if (mc.field_1724 == null || mc.field_1687 == null) {
/*    */       return;
/*    */     }
/*    */     
/* 51 */     int startY = mc.field_1724.method_31478();
/* 52 */     int minY = mc.field_1687.method_31607();
/* 53 */     int maxY = mc.field_1687.method_31600() - 2;
/* 54 */     int step = up ? 1 : -1;
/* 55 */     int from = up ? (startY + 1) : (startY - 1);
/* 56 */     int to = up ? maxY : minY;
/*    */     int y;
/* 58 */     for (y = from; up ? (y <= to) : (y >= to); ) {
/* 59 */       if (!isSafeStandPosition(y)) {
/*    */         y += step;
/*    */         continue;
/*    */       } 
/* 63 */       class_265 shape = mc.field_1687.method_8320(new class_2338(mc.field_1724.method_31477(), y - 1, mc.field_1724.method_31479())).method_26220((class_1922)mc.field_1687, new class_2338(mc.field_1724.method_31477(), y - 1, mc.field_1724.method_31479()));
/* 64 */       double offsetY = shape.method_1110() ? 0.0D : shape.method_1105(class_2350.class_2351.field_11052);
/* 65 */       mc.field_1724.method_5814(mc.field_1724.method_23317(), y + offsetY, mc.field_1724.method_23321());
/*    */       return;
/*    */     } 
/*    */   }
/*    */   
/*    */   private boolean isSafeStandPosition(int y) {
/* 71 */     class_2338 floorPos = new class_2338(mc.field_1724.method_31477(), y - 1, mc.field_1724.method_31479());
/* 72 */     class_2338 feetPos = floorPos.method_10084();
/* 73 */     class_2338 headPos = feetPos.method_10084();
/*    */     
/* 75 */     class_2680 floorState = mc.field_1687.method_8320(floorPos);
/* 76 */     if (floorState.method_26220((class_1922)mc.field_1687, floorPos).method_1110()) {
/* 77 */       return false;
/*    */     }
/*    */     
/* 80 */     return (mc.field_1687.method_8320(feetPos).method_26220((class_1922)mc.field_1687, feetPos).method_1110() && mc.field_1687
/* 81 */       .method_8320(headPos).method_26220((class_1922)mc.field_1687, headPos).method_1110());
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\api\commands\impl\TPCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */