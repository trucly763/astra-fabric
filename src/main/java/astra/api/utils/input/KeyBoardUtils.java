/*     */ package shame.astra.api.utils.input;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import org.lwjgl.glfw.GLFW;
/*     */ import shame.astra.api.events.implement.EventBinding;
/*     */ import shame.astra.api.storages.implement.helpertstorages.enumvar.ModuleClass;
/*     */ import shame.astra.client.modules.Module;
/*     */ 
/*     */ public final class KeyBoardUtils implements QClient {
/*     */   public static final int MOUSE_BUTTON_OFFSET = 1000;
/*     */   
/*     */   @Generated
/*     */   private KeyBoardUtils() {
/*  13 */     throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
/*     */   }
/*     */ 
/*     */   
/*     */   public static void call(int key, int action) {
/*  18 */     if (key <= -1) {
/*     */       return;
/*     */     }
/*  21 */     if (action == 1) {
/*  22 */       if (key == 344) {
/*  23 */         ClientSoundPlayer.playSound("opengui.wav", 0.6D, 1.0F);
/*  24 */         mc.method_1507((class_437)new MenuPanel());
/*     */       } 
/*  26 */       if (key == ModuleClass.autoBuy.openKey.getKey()) {
/*  27 */         mc.method_1507((class_437)new AutoBuy());
/*     */       }
/*     */       
/*  30 */       (new EventBinding(key, EventBinding.BindType.KEYBOARD)).call();
/*     */       
/*  32 */       ObjectArrayList<Module> modules = ModuleClass.INSTANCE.getObject();
/*  33 */       for (int i = 0, size = modules.size(); i < size; i++) {
/*  34 */         Module module = (Module)modules.get(i);
/*  35 */         if (module.getKey() == key) {
/*  36 */           module.toggle();
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String getKeyName(int keyCode) {
/*  43 */     if (keyCode == -1) return "None"; 
/*  44 */     String name = GLFW.glfwGetKeyName(keyCode, 0);
/*  45 */     if (name != null) return name.toUpperCase(); 
/*  46 */     switch (keyCode) { case 256: 
/*     */       case 32: 
/*     */       case 340: 
/*     */       case 344: 
/*     */       case 341:
/*     */       
/*     */       case 345:
/*  53 */        }  return "KEY" + keyCode;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void callMouse(int button, int action) {
/*  59 */     if (mc.field_1755 != null) {
/*     */       return;
/*     */     }
/*     */     
/*  63 */     if (button < 0) {
/*     */       return;
/*     */     }
/*     */     
/*  67 */     if (action == 1) {
/*  68 */       int mouseKey = 1000 + button;
/*     */       
/*  70 */       (new EventBinding(mouseKey, EventBinding.BindType.MOUSE)).call();
/*     */       
/*  72 */       ObjectArrayList<Module> modules = ModuleClass.INSTANCE.getObject();
/*  73 */       for (int i = 0, size = modules.size(); i < size; i++) {
/*  74 */         Module module = (Module)modules.get(i);
/*  75 */         if (module.getKey() == mouseKey) {
/*  76 */           module.toggle();
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean isBindHeld(int key) {
/*  83 */     if (key == -1) return false;
/*     */     
/*  85 */     long window = mc.method_22683().method_4490();
/*     */     
/*  87 */     if (key >= 1000) {
/*  88 */       int mouseButton = key - 1000;
/*  89 */       return (GLFW.glfwGetMouseButton(window, mouseButton) == 1);
/*     */     } 
/*  91 */     return (GLFW.glfwGetKey(window, key) == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isBindPressed(int key) {
/*  96 */     return isBindHeld(key);
/*     */   }
/*     */   
/*     */   public static String getBindName(int key) {
/* 100 */     if (key == -1)
/* 101 */       return "n/a"; 
/* 102 */     if (key >= 1000) {
/* 103 */       int mouseButton = key - 1000;
/* 104 */       switch (mouseButton) { case 0: 
/*     */         case 1: 
/*     */         case 2: 
/*     */         case 3:
/*     */         
/*     */         case 4:
/* 110 */          }  return "MOUSE" + mouseButton + 1;
/*     */     } 
/*     */     
/* 113 */     if (key >= 65 && key <= 90) {
/* 114 */       return String.valueOf((char)(65 + key - 65));
/*     */     }
/*     */     
/* 117 */     if (key >= 48 && key <= 57) {
/* 118 */       return String.valueOf((char)(48 + key - 48));
/*     */     }
/*     */     
/* 121 */     switch (key) { case 96: 
/*     */       case 45: 
/*     */       case 61: 
/*     */       case 91: 
/*     */       case 93: 
/*     */       case 92: 
/*     */       case 59: 
/*     */       case 39: 
/*     */       case 44: 
/*     */       case 46: 
/*     */       case 47: 
/*     */       default:
/* 133 */         break; }  String symbol = null;
/*     */     
/* 135 */     if (symbol != null) {
/* 136 */       return symbol;
/*     */     }
/* 138 */     switch (key) { case 32: 
/*     */       case 340: 
/*     */       case 344: 
/*     */       case 341: 
/*     */       case 345: 
/*     */       case 342: 
/*     */       case 346: 
/*     */       case 258: 
/*     */       case 257: 
/*     */       case 256: 
/*     */       case 259: 
/*     */       case 261: 
/*     */       case 260: 
/*     */       case 268: 
/*     */       case 269: 
/*     */       case 266: 
/*     */       case 267: 
/*     */       case 265: 
/*     */       case 264: 
/*     */       case 263: 
/*     */       case 262: 
/*     */       case 280: 
/*     */       case 290: 
/*     */       case 291: 
/*     */       case 292: 
/*     */       case 293: 
/*     */       case 294: 
/*     */       case 295: 
/*     */       case 296: 
/*     */       case 297: 
/*     */       case 298: 
/*     */       case 299: 
/*     */       case 300: 
/*     */       case 301: 
/*     */       case 320: 
/*     */       case 321: 
/*     */       case 322: 
/*     */       case 323: 
/*     */       case 324: 
/*     */       case 325: 
/*     */       case 326: 
/*     */       case 327: 
/*     */       case 328: 
/*     */       case 329: 
/*     */       case 330: 
/*     */       case 331: 
/*     */       case 332: 
/*     */       case 333: 
/*     */       case 334:
/*     */       
/*     */       case 335:
/* 189 */        }  return "KEY" + key;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isMouseButton(int key) {
/* 195 */     return (key >= 1000);
/*     */   }
/*     */   
/*     */   public static int getMouseButtonFromKey(int key) {
/* 199 */     if (isMouseButton(key)) {
/* 200 */       return key - 1000;
/*     */     }
/* 202 */     return -1;
/*     */   }
/*     */   
/*     */   public static int createMouseBind(int mouseButton) {
/* 206 */     return 1000 + mouseButton;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\astra-1.0.0.jar!\shame\astra\ap\\utils\input\KeyBoardUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */