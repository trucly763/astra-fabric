/*    */ package shame.astra.api.utils.client;
/*    */ import java.io.BufferedInputStream;
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.concurrent.ExecutorService;
/*    */ import java.util.concurrent.Executors;
/*    */ import javax.sound.sampled.AudioFormat;
/*    */ import javax.sound.sampled.AudioInputStream;
/*    */ import javax.sound.sampled.AudioSystem;
/*    */ import javax.sound.sampled.Clip;
/*    */ import javax.sound.sampled.FloatControl;
/*    */ import javax.sound.sampled.LineEvent;
/*    */ import lombok.Generated;
/*    */ 
/*    */ public final class ClientSoundPlayer {
/*    */   private static final ExecutorService EXECUTOR;
/*    */   
/*    */   @Generated
/*    */   private ClientSoundPlayer() {
/* 21 */     throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
/*    */   } static {
/* 23 */     EXECUTOR = Executors.newSingleThreadExecutor(r -> {
/*    */           Thread thread = new Thread(r, "astra-ClientSounds");
/*    */           thread.setDaemon(true);
/*    */           return thread;
/*    */         });
/*    */   }
/*    */   public static void playSound(String fileName, double volume, float pitch) {
/* 30 */     EXECUTOR.execute(() -> playInternal(fileName, volume, pitch));
/*    */   }
/*    */   
/*    */   private static void playInternal(String fileName, double volume, float pitch) {
/* 34 */     String resourcePath = "/assets/astra/sounds/" + fileName;
/*    */     
/* 36 */     try { InputStream inputStream = ClientSoundPlayer.class.getResourceAsStream(resourcePath); 
/* 37 */       try { if (inputStream == null)
/*    */         
/*    */         { 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */           
/* 55 */           if (inputStream != null) inputStream.close();  return; }  BufferedInputStream bufferedIn = new BufferedInputStream(inputStream); try { AudioInputStream baseStream = AudioSystem.getAudioInputStream(bufferedIn); try { AudioInputStream pitchedStream = resampleStream(baseStream, pitch); try { Clip clip = AudioSystem.getClip(); clip.addLineListener(event -> { if (event.getType() == LineEvent.Type.STOP) clip.close();  }); clip.open(pitchedStream); setVolume(clip, volume); clip.start(); if (pitchedStream != null) pitchedStream.close();  } catch (Throwable throwable) { if (pitchedStream != null) try { pitchedStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  if (baseStream != null) baseStream.close();  } catch (Throwable throwable) { if (baseStream != null) try { baseStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  bufferedIn.close(); } catch (Throwable throwable) { try { bufferedIn.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  if (inputStream != null) inputStream.close();  } catch (Throwable throwable) { if (inputStream != null) try { inputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (UnsupportedAudioFileException|IOException|javax.sound.sampled.LineUnavailableException unsupportedAudioFileException) {}
/*    */   }
/*    */ 
/*    */   
/*    */   private static AudioInputStream resampleStream(AudioInputStream originalStream, float pitch) throws IOException {
/* 60 */     AudioFormat originalFormat = originalStream.getFormat();
/* 61 */     byte[] audioBytes = originalStream.readAllBytes();
/* 62 */     float newSampleRate = originalFormat.getSampleRate() * Math.max(0.5F, Math.min(2.0F, pitch));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 69 */     AudioFormat newFormat = new AudioFormat(newSampleRate, originalFormat.getSampleSizeInBits(), originalFormat.getChannels(), true, originalFormat.isBigEndian());
/*    */ 
/*    */     
/* 72 */     return new AudioInputStream(new ByteArrayInputStream(audioBytes), newFormat, (audioBytes.length / newFormat
/*    */ 
/*    */         
/* 75 */         .getFrameSize()));
/*    */   }
/*    */ 
/*    */   
/*    */   private static void setVolume(Clip clip, double volume) {
/* 80 */     if (!clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
/*    */       return;
/*    */     }
/*    */     
/* 84 */     double clampedVolume = Math.max(0.0D, Math.min(1.0D, volume));
/* 85 */     FloatControl volumeControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
/* 86 */     float dB = (float)(Math.log10((clampedVolume <= 0.0D) ? 1.0E-4D : clampedVolume) * 20.0D);
/* 87 */     volumeControl.setValue(dB);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\client\ClientSoundPlayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */