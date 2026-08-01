package shame.astra.features.modules.movement;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.ChatFormatting;
import shame.astra.control.Manager;
import shame.astra.control.events.Event;
import shame.astra.control.events.impl.game.EventKey;
import shame.astra.control.events.impl.player.EventUpdate;
import shame.astra.module.TypeList;
import shame.astra.module.api.Annotation;
import shame.astra.module.api.Module;
import shame.astra.module.impl.player.GuiMove;
import shame.astra.module.settings.imp.BindSetting;
import shame.astra.module.settings.imp.BooleanSetting;
import shame.astra.utils.ClientUtils;
import shame.astra.utils.misc.TimerUtil;
import shame.astra.utils.move.MoveUtil;
import shame.astra.utils.world.InventoryUtils;

// @Annotation(name = "ElytraHelper", type = TypeList.Movement, desc = "Помогает свапать элитры с инвентаря")
public class ElytraHelper extends Module {
    private final BindSetting swapChestKey = new BindSetting("Элитры", 0);
    private final BindSetting fireWorkKey = new BindSetting("Фейерверк", 0);
    private final BooleanSetting autoFly = new BooleanSetting("Авто взлёт", true);
    private final BooleanSetting autofireWork = new BooleanSetting("Авто фейерверк", false);
    public final BooleanSetting swap = new BooleanSetting("Фейр в левую руку", true);
    private final BooleanSetting autofireWorkstart = new BooleanSetting("Только при взлёте", false).setVisible(() -> autofireWork.get());
    private final BooleanSetting cakeWorld = new BooleanSetting("Обход CakeWorld", false);
    ItemStack currentStack = ItemStack.EMPTY;
    private final TimerUtil stopWatch = new TimerUtil();
    boolean fireworkUsed;
    private final TimerUtil timerUtil = new TimerUtil();
    private boolean recentlySwapped = false;
    private final TimerUtil swapCooldownTimer = new TimerUtil();
    private boolean hasFiredOnStart = false;

    public ElytraHelper() {
        addSettings(swapChestKey, fireWorkKey, autoFly, autofireWork, autofireWorkstart, swap, cakeWorld);
    }

    public boolean onEvent(Event event) {
        if (event instanceof EventUpdate) {
            if (this.autoFly.get() && !mc.player.getAbilities().flying && mc.player.isOnGround() && mc.player.getInventory().getArmor(2).getItem() == Items.ELYTRA && !mc.options.keyJump.isDown() && !mc.player.isInWaterOrBubble() && !mc.player.isInLava()) {
                mc.player.jump();
            }

            if (this.autoFly.get() && !mc.player.getAbilities().flying && !mc.player.isInWaterOrBubble() && !mc.player.isOnGround() && !mc.player.isFallFlying() && mc.player.getInventory().getArmor(2).getItem() == Items.ELYTRA) {
                mc.player.startFallFlying();
                mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.START_FALL_FLYING));
                if (autofireWork.get() && autofireWorkstart.get() && !hasFiredOnStart) {
                    if (InventoryUtils.getItemSlot(Items.FIREWORK_ROCKET) != -1) {
                        InventoryUtils.inventorySwapClickFF(Items.FIREWORK_ROCKET, false);
                        hasFiredOnStart = true;
                    } else {
                        ClientUtils.sendMessage(ChatFormatting.WHITE + "У вас не были найдены" + ChatFormatting.RED + " фейерверки");
                    }
                }
            }

            if (mc.player.isOnGround() || mc.player.isInWaterOrBubble() || mc.player.isInLava()) {
                hasFiredOnStart = false;
            }

            if (mc.player.isFallFlying() && autofireWork.get() && !autofireWorkstart.get() && timerUtil.hasTimeElapsed(570L)) {
                if (InventoryUtils.getItemSlot(Items.FIREWORK_ROCKET) != -1) {
                    InventoryUtils.inventorySwapClickFF(Items.FIREWORK_ROCKET, false);
                } else {
                    ClientUtils.sendMessage(ChatFormatting.WHITE + "У вас не были найдены" + ChatFormatting.RED + " фейерверки");
                }
                timerUtil.reset();
            }

            this.currentStack = mc.player.getItemBySlot(EquipmentSlot.CHEST);

            if (recentlySwapped && swapCooldownTimer.hasTimeElapsed(2000L)) {
                recentlySwapped = false;
            }

            if (fireworkUsed) {
                if (mc.player.isFallFlying()) {
                    useFirework();
                    fireworkUsed = false;
                }
            }
        }

        if (event instanceof EventKey e) {
            if (e.key == swapChestKey.getKey() && stopWatch.hasTimeElapsed(150L)) {
                boolean wasSprinting = mc.player.isSprinting();
                boolean wasSprintKeyPressed = mc.options.keySprint.isDown();
                if (GuiMove.syncSwap.get() && Manager.FUNCTION_MANAGER.guiMove.state && !GuiMove.mode.is("Vanila")) {
                    GuiMove.stopMovementTemporarily(0.06f);
                }

                if (cakeWorld.get() && wasSprinting) {
                    mc.options.keySprint.setDown(false);
                    mc.player.setSprinting(false);
                    mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.STOP_SPRINTING));
                }

                if (getItemSlot(Items.ELYTRA) == -1) {
                    changeChestPlate(currentStack);
                    InventoryUtils.syncInventory();
                    stopWatch.reset();
                } else {
                    changeChestPlate(currentStack);
                    InventoryUtils.syncInventory();
                    stopWatch.reset();
                }

                if (cakeWorld.get() && wasSprinting) {
                    if (wasSprintKeyPressed) {
                        mc.options.keySprint.setDown(true);
                    }
                    mc.player.setSprinting(true);
                    mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.START_SPRINTING));
                }

                recentlySwapped = true;
                swapCooldownTimer.reset();
            }

            if (e.key == fireWorkKey.getKey()) {
                fireworkUsed = true;
            }
        }
        return false;
    }

    private void changeChestPlate(ItemStack stack) {
        if (stack.getItem() != Items.ELYTRA) {
            int elytraSlot = getItemSlot(Items.ELYTRA);
            int freeSlot = findFreeInventorySlot();
            if (elytraSlot >= 0) {
                InventoryUtils.moveItem(elytraSlot, 6);
            } else if (freeSlot >= 0) {
            }
            return;
        }
        int armorSlot = getChestPlateSlot();
        int freeSlot = findFreeInventorySlot();
        if (armorSlot >= 0) {
            InventoryUtils.moveItem(armorSlot, 6);
        } else if (freeSlot >= 0) {
            InventoryUtils.moveItem(6, freeSlot);
        }
    }

    private int findFreeInventorySlot() {
        for (int i = 10; i < 36; i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            if (stack.isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    private int getChestPlateSlot() {
        Item[] items = {
                Items.NETHERITE_CHESTPLATE, Items.DIAMOND_CHESTPLATE, Items.GOLD_CHESTPLATE,
                Items.IRON_CHESTPLATE, Items.LEATHER_CHESTPLATE, Items.CHAINMAIL_CHESTPLATE
        };

        for (Item item : items) {
            for (int i = 0; i < 36; ++i) {
                Item stack = mc.player.getInventory().getItem(i).getItem();
                if (stack == item) {
                    if (i < 9) {
                        i += 36;
                    }
                    return i;
                }
            }
        }
        return -1;
    }

    private int getItemSlot(Item item) {
        int finalSlot = -1;

        for (int i = 0; i < 36; ++i) {
            if (mc.player.getInventory().getItem(i).getItem() == item) {
                finalSlot = i;
                break;
            }
        }

        if (finalSlot < 9 && finalSlot != -1) {
            finalSlot += 36;
        }

        return finalSlot;
    }

    private void useFirework() {
        if (InventoryUtils.getItemSlot(Items.FIREWORK_ROCKET) == -1) {
            if (mc.player.isFallFlying()) {
                ClientUtils.sendMessage(ChatFormatting.WHITE + "У вас не были найдены" + ChatFormatting.RED + " фейерверки");
            }
        } else {
            if (Manager.FUNCTION_MANAGER.middleClickPearlFunction.legit.get()) {
                InventoryUtils.holySwapClick(Items.ENDER_PEARL, false);
            } else {
                InventoryUtils.inventorySwapClickFF(Items.FIREWORK_ROCKET, false);
            }
        }
    }

    @Override
    public void onDisable() {
        stopWatch.reset();
        timerUtil.reset();
        hasFiredOnStart = false;
        super.onDisable();
    }
}
