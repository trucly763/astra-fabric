package shame.astra.features.modules.movement;

import net.minecraft.world.entity.LivingEntity;
import shame.astra.control.events.Event;
import shame.astra.module.TypeList;
import shame.astra.module.api.Annotation;
import shame.astra.module.api.Module;
import shame.astra.module.settings.imp.SliderSetting;
import shame.astra.utils.misc.TimerUtil;

// @Annotation(
        name = "ElytraPredict",
        type = TypeList.Movement,
        desc = "Смещает хитбокс противника во время полёта на элитрах для перегона на элитрах"
)
public class ElytraPredict extends Module {
    public final SliderSetting elytradistance = new SliderSetting("Дистанция обгона", 3.0F, 0.0F, 4.5F, 0.05F);
    public final TimerUtil timer = new TimerUtil();
    public boolean disabled = false;

    public ElytraPredict() {
        this.addSettings(elytradistance);
    }

    public boolean onEvent(Event event) {
        return false;
    }

    public double getElytraDistance(LivingEntity target) {
        return elytradistance.getValue().floatValue();
    }

    public boolean canPredict(LivingEntity target) {
        if (mc.player.getLastHurtMobTime() > 0 && !target.getLastHurtByMobTimestamp() > System.currentTimeMillis() - 500) {
            disabled = true;
            timer.reset();
        }
        if (timer.hasTimeElapsed(500)) disabled = false;
        return !disabled;
    }
}
