package shame.astra.features.modules.movement;

import meteordevelopment.orbit.EventHandler;
import shame.astra.events.impl.EventMove;
import shame.astra.features.modules.Module;
import shame.astra.setting.Setting;
import shame.astra.setting.impl.SettingGroup;

// @Annotation(name = "ElytraMotion", type = TypeList.Movement, desc = "Позволяет зависнуть возле цели на элитрах")
public class ElytraMotion extends Module {

    public final SliderSetting distancie = new SliderSetting("Дист до таргета", 2F, 1.25F, 2.5F, 0.05F);

    public ElytraMotion() {
        addSettings(distancie);
    }

    @Override
    public boolean onEvent(final Event event) {
        return false;
    }
}
