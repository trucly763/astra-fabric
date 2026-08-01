package shame.astra.features.modules.movement;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import shame.astra.Vcore;
import shame.astra.core.Managers;
import shame.astra.events.impl.EventMove;
import shame.astra.events.impl.EventPlayerTravel;
import shame.astra.events.impl.EventPostTick;
import shame.astra.events.impl.EventSync;
import shame.astra.events.impl.EventTick;
import shame.astra.events.impl.PostPlayerUpdateEvent;
import shame.astra.features.modules.Module;
import shame.astra.injection.accesors.IInteractionManager;
import shame.astra.setting.Setting;
import shame.astra.utility.Timer;
import shame.astra.utility.interfaces.IEntity;
import shame.astra.utility.player.InventoryUtility;
import shame.astra.utility.player.MovementUtility;
import shame.astra.utility.player.SearchInvResult;

public class Speed extends Module {
   public final Setting<Speed.Mode> mode = new Setting<>("Mode", Speed.Mode.NCP);
   public Setting<Boolean> useTimer = new Setting<>("Use Timer", false);
   public Setting<Boolean> pauseInLiquids = new Setting<>("PauseInLiquids", false);
   public Setting<Boolean> pauseWhileSneaking = new Setting<>("PauseWhileSneaking", false);
   public final Setting<Integer> hurttime = new Setting<>("HurtTime", 0, 0, 10, v -> this.mode.is(Speed.Mode.MatrixDamage));
   public final Setting<Float> boostFactor = new Setting<>(
      "BoostFactor", 2.0F, 0.0F, 10.0F, v -> this.mode.is(Speed.Mode.MatrixDamage) || this.mode.is(Speed.Mode.Vanilla)
   );
   public final Setting<Boolean> allowOffGround = new Setting<>("AllowOffGround", true, v -> this.mode.is(Speed.Mode.MatrixDamage));
   public final Setting<Integer> shiftTicks = new Setting<>("ShiftTicks", 0, 0, 10, v -> this.mode.is(Speed.Mode.MatrixDamage));
   public final Setting<Integer> fireWorkSlot = new Setting<>("FireSlot", 1, 1, 9, v -> this.mode.getValue() == Speed.Mode.FireWork);
   public final Setting<Integer> delay = new Setting<>("Delay", 8, 1, 20, v -> this.mode.getValue() == Speed.Mode.FireWork);
   public final Setting<Boolean> strict = new Setting<>("Strict", false, v -> this.mode.is(Speed.Mode.GrimIce));
   public final Setting<Float> matrixJBSpeed = new Setting<>("TimerSpeed", 1.088F, 1.0F, 2.0F, v -> this.mode.is(Speed.Mode.MatrixJB));
   public final Setting<Boolean> armorStands = new Setting<>(
      "ArmorStands", false, v -> this.mode.is(Speed.Mode.GrimCombo) || this.mode.is(Speed.Mode.GrimEntity2)
   );
   public double baseSpeed;
   private int stage;
   private int ticks;
   private int prevSlot;
   private float prevForward = 0.0F;
   private Timer elytraDelay = new Timer();
   private Timer startDelay = new Timer();

   public Speed() {
      super("Speed", "Increases movement speed.", Module.Category.MOVEMENT);
   }

   @Override
   public void onDisable() {
      Vcore.TICK_TIMER = 1.0F;
   }

   @Override
   public void onEnable() {
      this.stage = 1;
      this.ticks = 0;
      this.baseSpeed = 0.2873;
      this.startDelay.reset();
      this.prevSlot = -1;
   }

   @EventHandler
   public void onSync(EventSync e) {
      if ((!mc.player.FIXED_METHOD() || !this.pauseInLiquids.getValue()) && (!mc.player.FIXED_METHOD() || !this.pauseWhileSneaking.getValue())) {
         if (this.mode.getValue() == Speed.Mode.MatrixJB) {
            boolean closeToGround = false;

            for (VoxelShape a : mc.world.FIXED_METHOD(mc.player, mc.player.FIXED_METHOD().expand(0.5, 0.0, 0.5).offset(0.0, -1.0, 0.0))) {
               if (a != VoxelShapes.empty()) {
                  closeToGround = true;
                  break;
               }
            }

            if (MovementUtility.isMoving() && closeToGround && mc.player.field_6017 <= 0.0F) {
               Vcore.TICK_TIMER = 1.0F;
               mc.player.FIXED_METHOD(true);
               mc.player.FIXED_METHOD();
            } else if (mc.player.field_6017 > 0.0F && this.useTimer.getValue()) {
               Vcore.TICK_TIMER = this.matrixJBSpeed.getValue();
               mc.player.FIXED_METHOD(0.0, -0.003F, 0.0);
            }
         }
      }
   }

   @EventHandler
   public void modifyVelocity(EventPlayerTravel e) {
      if (this.mode.getValue() == Speed.Mode.GrimEntity && !e.isPre() && Vcore.core.getSetBackTime() > 1000L) {
         for (PlayerEntity ent : Managers.ASYNC.getAsyncPlayers()) {
            if (ent != mc.player && mc.player.FIXED_METHOD(ent) <= 2.25) {
               float p = mc.world.FIXED_METHOD(((IEntity)mc.player).Vcore_Recode$getVelocityBP()).FIXED_METHOD().getSlipperiness();
               float f = mc.player.FIXED_METHOD() ? p * 0.91F : 0.91F;
               float f2 = mc.player.FIXED_METHOD() ? p : 0.99F;
               mc.player
                  .FIXED_METHOD(
                     mc.player.FIXED_METHOD().FIXED_METHOD() / f * f2,
                     mc.player.FIXED_METHOD().FIXED_METHOD(),
                     mc.player.FIXED_METHOD().FIXED_METHOD() / f * f2
                  );
               break;
            }
         }
      }

      if ((this.mode.is(Speed.Mode.GrimEntity2) || this.mode.is(Speed.Mode.GrimCombo))
         && !e.isPre()
         && Vcore.core.getSetBackTime() > 1000L
         && MovementUtility.isMoving()) {
         int collisions = 0;

         for (Entity ent : mc.world.getEntities()) {
            if (ent != mc.player
               && (!(ent instanceof ArmorStandEntity) || this.armorStands.getValue())
               && (ent instanceof LivingEntity || ent instanceof BoatEntity)
               && mc.player.FIXED_METHOD().expand(1.0).intersects(ent.FIXED_METHOD())) {
               collisions++;
            }
         }

         double[] motion = MovementUtility.forward(0.08 * collisions);
         mc.player.FIXED_METHOD(motion[0], 0.0, motion[1]);
      }
   }

   @EventHandler
   public void onTick(EventTick e) {
      if ((this.mode.is(Speed.Mode.GrimIce) || this.mode.is(Speed.Mode.GrimCombo)) && mc.player.FIXED_METHOD()) {
         BlockPos pos = ((IEntity)mc.player).Vcore_Recode$getVelocityBP();
         SearchInvResult result = InventoryUtility.findBlockInHotBar(Blocks.ICE, Blocks.PACKED_ICE, Blocks.BLUE_ICE);
         if (mc.world.FIXED_METHOD(pos) || !result.found() || !mc.options.jumpKey.isPressed()) {
            return;
         }

         this.prevSlot = mc.player.FIXED_METHOD().selectedSlot;
         result.switchTo();
         this.sendPacket(
            new Full(mc.player.FIXED_METHOD(), mc.player.FIXED_METHOD(), mc.player.FIXED_METHOD(), mc.player.FIXED_METHOD(), 90.0F, mc.player.FIXED_METHOD())
         );
         if (this.strict.getValue()) {
            this.sendPacket(new PlayerActionC2SPacket(Action.START_DESTROY_BLOCK, pos, Direction.UP));
            this.sendPacket(new PlayerActionC2SPacket(Action.ABORT_DESTROY_BLOCK, pos, Direction.UP));
         }

         this.sendPacket(new PlayerActionC2SPacket(Action.STOP_DESTROY_BLOCK, pos, Direction.UP));
         this.sendSequencedPacket(
            id -> new PlayerInteractBlockC2SPacket(
               Hand.MAIN_HAND, new BlockHitResult(pos.down().toCenterPos().add(0.0, 0.5, 0.0), Direction.UP, pos.down(), false), id
            )
         );
         mc.world.FIXED_METHOD(pos, Blocks.ICE.getDefaultState());
      }
   }

   @EventHandler
   public void onPostTick(EventPostTick e) {
      if ((this.mode.is(Speed.Mode.GrimIce) || this.mode.is(Speed.Mode.GrimCombo)) && this.prevSlot != -1) {
         mc.player.FIXED_METHOD().selectedSlot = this.prevSlot;
         ((IInteractionManager)mc.interactionManager).syncSlot();
         this.prevSlot = -1;
      }
   }

   @Override
   public void onUpdate() {
      if ((!mc.player.FIXED_METHOD() || !this.pauseInLiquids.getValue()) && (!mc.player.FIXED_METHOD() || !this.pauseWhileSneaking.getValue())) {
         if (this.mode.getValue() == Speed.Mode.FireWork) {
            this.ticks--;
            int ellySlot = InventoryUtility.getElytra();
            int fireSlot = InventoryUtility.findItemInHotBar(Items.FIREWORK_ROCKET).slot();
            boolean inOffHand = mc.player.FIXED_METHOD().getItem() == Items.FIREWORK_ROCKET;
            if (fireSlot == -1) {
               int fireInInv = InventoryUtility.findItemInInventory(Items.FIREWORK_ROCKET).slot();
               if (fireInInv != -1) {
                  mc.interactionManager.clickSlot(mc.player.field_7512.syncId, fireInInv, this.fireWorkSlot.getValue() - 1, SlotActionType.SWAP, mc.player);
               }
            }

            if (ellySlot != -1 && (fireSlot != -1 || inOffHand) && !mc.player.FIXED_METHOD() && mc.player.field_6017 > 0.0F && this.ticks <= 0) {
               if (ellySlot != -2) {
                  mc.interactionManager.clickSlot(0, ellySlot, 1, SlotActionType.PICKUP, mc.player);
                  mc.interactionManager.clickSlot(0, 6, 1, SlotActionType.PICKUP, mc.player);
               }

               mc.player
                  .networkHandler
                  .FIXED_METHOD(new ClientCommandC2SPacket(mc.player, net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode.START_FALL_FLYING));
               int prevSlot = mc.player.FIXED_METHOD().selectedSlot;
               if (prevSlot != fireSlot && !inOffHand) {
                  this.sendPacket(new UpdateSelectedSlotC2SPacket(fireSlot));
               }

               mc.interactionManager.interactItem(mc.player, inOffHand ? Hand.OFF_HAND : Hand.MAIN_HAND);
               if (prevSlot != fireSlot && !inOffHand) {
                  this.sendPacket(new UpdateSelectedSlotC2SPacket(prevSlot));
               }

               if (ellySlot != -2) {
                  mc.interactionManager.clickSlot(0, 6, 1, SlotActionType.PICKUP, mc.player);
                  mc.interactionManager.clickSlot(0, ellySlot, 1, SlotActionType.PICKUP, mc.player);
               }

               mc.player.networkHandler.FIXED_METHOD(new CloseHandledScreenC2SPacket(mc.player.field_7512.syncId));
               this.ticks = this.delay.getValue();
            }
         }

         if (this.mode.getValue() == Speed.Mode.ElytraLowHop) {
            if (mc.player.FIXED_METHOD()) {
               mc.player.FIXED_METHOD();
               return;
            }

            if (mc.world.FIXED_METHOD(mc.player, mc.player.FIXED_METHOD().expand(-0.29, 0.0, -0.29).offset(0.0, -3.0, 0.0)).iterator().hasNext()
               && this.elytraDelay.passedMs(150L)
               && this.startDelay.passedMs(500L)) {
               int elytra = InventoryUtility.getElytra();
               if (elytra == -1) {
                  this.disable("You need elytra for this mode!");
               }

               mc.player.FIXED_METHOD(mc.player.FIXED_METHOD().FIXED_METHOD(), 0.0, mc.player.FIXED_METHOD().FIXED_METHOD());
               if (MovementUtility.isMoving()) {
                  MovementUtility.setMotion(0.85);
               }

               this.elytraDelay.reset();
            }
         }
      }
   }

   @EventHandler
   public void onPostPlayerUpdate(PostPlayerUpdateEvent event) {
      if (this.mode.getValue() == Speed.Mode.MatrixDamage && MovementUtility.isMoving() && mc.player.field_6235 > this.hurttime.getValue()) {
         if (mc.player.FIXED_METHOD()) {
            MovementUtility.setMotion(0.387F * this.boostFactor.getValue());
         } else if (mc.player.FIXED_METHOD()) {
            MovementUtility.setMotion(0.346F * this.boostFactor.getValue());
         } else if (!mc.player.FIXED_METHOD() && this.allowOffGround.getValue()) {
            MovementUtility.setMotion(0.448F * this.boostFactor.getValue());
         }

         if (this.shiftTicks.getValue() > 0) {
            event.cancel();
            event.setIterations(this.shiftTicks.getValue());
         }
      }
   }

   @EventHandler
   public void onMove(EventMove event) {
      if ((!mc.player.FIXED_METHOD() || !this.pauseInLiquids.getValue()) && (!mc.player.FIXED_METHOD() || !this.pauseWhileSneaking.getValue())) {
         if (this.mode.getValue() == Speed.Mode.NCP || this.mode.getValue() == Speed.Mode.StrictStrafe) {
            if (!mc.player.FIXED_METHOD().flying) {
               if (!mc.player.FIXED_METHOD()) {
                  if (mc.player.FIXED_METHOD().getFoodLevel() > 6) {
                     if (!event.isCancelled()) {
                        event.cancel();
                        if (MovementUtility.isMoving()) {
                           Vcore.TICK_TIMER = this.useTimer.getValue() ? 1.088F : 1.0F;
                           float currentSpeed = this.mode.getValue() == Speed.Mode.NCP && mc.player.input.movementForward <= 0.0F && this.prevForward > 0.0F
                              ? Managers.PLAYER.currentPlayerSpeed * 0.66F
                              : Managers.PLAYER.currentPlayerSpeed;
                           boolean canJump = !mc.player.field_5976;
                           if (this.stage == 1 && mc.player.FIXED_METHOD() && canJump) {
                              mc.player.FIXED_METHOD(mc.player.FIXED_METHOD().x, MovementUtility.getJumpSpeed(), mc.player.FIXED_METHOD().z);
                              event.setY(MovementUtility.getJumpSpeed());
                              this.baseSpeed *= 2.149;
                              this.stage = 2;
                           } else if (this.stage == 2) {
                              this.baseSpeed = currentSpeed - 0.66 * (currentSpeed - MovementUtility.getBaseMoveSpeed());
                              this.stage = 3;
                           } else {
                              if (mc.world
                                    .FIXED_METHOD(mc.player, mc.player.FIXED_METHOD().offset(0.0, mc.player.FIXED_METHOD().FIXED_METHOD(), 0.0))
                                    .iterator()
                                    .hasNext()
                                 || mc.player.field_5992) {
                                 this.stage = 1;
                              }

                              this.baseSpeed = currentSpeed - currentSpeed / 159.0;
                           }

                           this.baseSpeed = Math.max(this.baseSpeed, MovementUtility.getBaseMoveSpeed());
                           double ncpSpeed = this.mode.getValue() != Speed.Mode.StrictStrafe && !(mc.player.input.movementForward < 1.0F) ? 0.576 : 0.465;
                           double ncpBypassSpeed = this.mode.getValue() != Speed.Mode.StrictStrafe && !(mc.player.input.movementForward < 1.0F) ? 0.57 : 0.44;
                           if (mc.player.hasStatusEffect(StatusEffects.SPEED)) {
                              double amplifier = mc.player.getStatusEffect(StatusEffects.SPEED).getAmplifier();
                              ncpSpeed *= 1.0 + 0.2 * (amplifier + 1.0);
                              ncpBypassSpeed *= 1.0 + 0.2 * (amplifier + 1.0);
                           }

                           if (mc.player.hasStatusEffect(StatusEffects.SLOWNESS)) {
                              double amplifier = mc.player.getStatusEffect(StatusEffects.SLOWNESS).getAmplifier();
                              ncpSpeed /= 1.0 + 0.2 * (amplifier + 1.0);
                              ncpBypassSpeed /= 1.0 + 0.2 * (amplifier + 1.0);
                           }

                           this.baseSpeed = Math.min(this.baseSpeed, this.ticks > 25 ? ncpSpeed : ncpBypassSpeed);
                           if (this.ticks++ > 50) {
                              this.ticks = 0;
                           }

                           MovementUtility.modifyEventSpeed(event, this.baseSpeed);
                           this.prevForward = mc.player.input.movementForward;
                        } else {
                           Vcore.TICK_TIMER = 1.0F;
                           event.setX(0.0);
                           event.setZ(0.0);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public enum Mode {
      ElytraLowHop,
      FireWork,
      GrimCombo,
      GrimEntity,
      GrimEntity2,
      GrimIce,
      MatrixDamage,
      MatrixJB,
      NCP,
      StrictStrafe,
      Vanilla;
   }
}
