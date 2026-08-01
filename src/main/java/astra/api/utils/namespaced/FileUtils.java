/*    */ package shame.astra.api.utils.namespaced;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.Paths;
/*    */ import lombok.Generated;
/*    */ 
/*    */ public final class FileUtils {
/*    */   @Generated
/*    */   private FileUtils() {
/* 12 */     throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
/*    */   }
/*    */   public static void reset(String str) throws IOException {
/* 15 */     Path path = Paths.get(str, new String[0]);
/* 16 */     if (Files.exists(path, new java.nio.file.LinkOption[0])) (new File(str)).delete(); 
/* 17 */     Files.createFile(path, (FileAttribute<?>[])new FileAttribute[0]);
/*    */   }
/*    */   
/*    */   public static boolean exists(String str) {
/* 21 */     return Files.exists(Paths.get(str, new String[0]), new java.nio.file.LinkOption[0]);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\namespaced\FileUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */