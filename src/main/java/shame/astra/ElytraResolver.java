package shame.astra.features.modules.movement;

import shame.astra.control.events.Event;
import shame.astra.module.TypeList;
import shame.astra.module.api.Annotation;
import shame.astra.module.api.Module;
import shame.astra.module.settings.imp.BooleanSetting;
import shame.astra.module.settings.imp.MultiBoxSetting;
import shame.astra.module.settings.imp.SliderSetting;

// @Annotation(
        name = "ElytraResolver",
        type = TypeList.Movement,
        desc = "Меняет ваше движение так что бы по вам не могли ударить на элитрах"
)
public class ElytraResolver extends Module {

    public final MultiBoxSetting vector = new MultiBoxSetting("Векторы лива",
            new BooleanSetting("Вверх", true),
            new BooleanSetting("Вниз", false),
            new BooleanSetting("Восток", true),
            new BooleanSetting("Запад", true),
            new BooleanSetting("Юг", true),
            new BooleanSetting("Север", true));
    public final SliderSetting elytradistance = new SliderSetting("Дистанция", 4.5F, 3.0F, 8F, 0.5F);

    public final BooleanSetting skipvector = new BooleanSetting("Исключать столкновение", true);
    public final BooleanSetting autoF = new BooleanSetting("Авто фейерверк", true);
    public final BooleanSetting freezeDummy = new BooleanSetting("Замораживать игрока", true);

    public ElytraResolver() {
        this.addSettings(vector, elytradistance, skipvector, autoF, freezeDummy);
    }

    public boolean onEvent(Event event) {
        return false;
    }
}
