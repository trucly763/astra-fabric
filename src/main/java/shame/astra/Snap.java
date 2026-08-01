package shame.astra.features.modules.combat.aura.rotation;

import shame.astra.features.modules.combat.Aura;

public class Snap implements RotationModeHandler {
   @Override
   public void rotate(Aura aura, boolean ready) {
      Track.rotateClassic(aura, ready);
   }
}
