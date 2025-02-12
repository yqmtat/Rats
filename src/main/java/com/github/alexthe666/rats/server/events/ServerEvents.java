package com.github.alexthe666.rats.server.events;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.entity.EntityIllagerPiper;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatUtils;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.github.alexthe666.rats.server.message.MessageRatDismount;
import com.google.common.base.Predicate;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.audio.Sound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.GetCollisionBoxesEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.Random;

@Mod.EventBusSubscriber
public class ServerEvents {

    Predicate<Entity> UNTAMED_RAT_SELECTOR = new Predicate<Entity>() {
        public boolean apply(@Nullable Entity p_apply_1_) {
            return p_apply_1_ instanceof EntityRat && !((EntityRat) p_apply_1_).isTamed();
        }
    };

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
        if (event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND).getItem() == RatsItemRegistry.CHEESE_STICK || event.getEntityPlayer().getHeldItem(EnumHand.OFF_HAND).getItem() == RatsItemRegistry.CHEESE_STICK) {
            event.setUseBlock(Event.Result.DENY);
        }
        if (event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND).getItem() == RatsItemRegistry.CHUNKY_CHEESE_TOKEN || event.getEntityPlayer().getHeldItem(EnumHand.OFF_HAND).getItem() == RatsItemRegistry.CHUNKY_CHEESE_TOKEN) {
            if (!RatsMod.CONFIG_OPTIONS.disableRatlantis) {
                if (!event.getEntityPlayer().isCreative()) {
                    event.getItemStack().shrink(1);
                }
                boolean canBuild = true;
                BlockPos pos = event.getPos().offset(event.getFace());
                for (int i = 0; i < 4; i++) {
                    IBlockState state = event.getWorld().getBlockState(pos.up(i));
                    if (state.getBlockHardness(event.getWorld(), pos.up(i)) == -1.0F) {
                        canBuild = false;
                    }
                }
                if (canBuild) {
                    event.getEntityPlayer().playSound(SoundEvents.BLOCK_END_PORTAL_SPAWN, 1, 1);
                    event.getWorld().setBlockState(pos, RatsBlockRegistry.MARBLED_CHEESE_RAW.getDefaultState());
                    event.getWorld().setBlockState(pos.up(), RatsBlockRegistry.RATLANTIS_PORTAL.getDefaultState());
                    event.getWorld().setBlockState(pos.up(2), RatsBlockRegistry.RATLANTIS_PORTAL.getDefaultState());
                    event.getWorld().setBlockState(pos.up(3), RatsBlockRegistry.MARBLED_CHEESE_RAW.getDefaultState());
                }
            }
        }
        if (RatsMod.CONFIG_OPTIONS.cheesemaking && event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.CAULDRON && isMilk(event.getItemStack())) {
            if (event.getWorld().getBlockState(event.getPos()).getValue(BlockCauldron.LEVEL) == 0) {
                event.getWorld().setBlockState(event.getPos(), RatsBlockRegistry.MILK_CAULDRON.getDefaultState());
                if (!event.getWorld().isRemote) {
                    CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) event.getEntityPlayer(), event.getPos(), new ItemStack(RatsBlockRegistry.MILK_CAULDRON));
                }
                event.getEntityPlayer().playSound(SoundEvents.ITEM_BUCKET_EMPTY, 1, 1);
                if (!event.getEntityPlayer().isCreative()) {
                    if (event.getItemStack().getItem() == Items.MILK_BUCKET) {
                        event.getItemStack().shrink(1);
                        event.getEntityPlayer().addItemStackToInventory(new ItemStack(Items.BUCKET));
                    } else if (isMilk(event.getItemStack())) {
                        IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(event.getItemStack());
                        fluidHandler.drain(1000, true);
                    }
                }
                event.setCanceled(true);
            }
        }
    }

    private boolean isMilk(ItemStack stack) {
        if (stack.getItem() == Items.MILK_BUCKET) {
            return true;
        }
        FluidStack fluidStack = FluidUtil.getFluidContained(stack);
        return fluidStack != null && fluidStack.amount >= 1000 && (fluidStack.getFluid().getUnlocalizedName().contains("milk") || fluidStack.getFluid().getUnlocalizedName().contains("Milk"));
    }

    @SubscribeEvent
    public void onPlayerInteractWithEntity(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof EntityOcelot) {
            EntityOcelot ocelot = (EntityOcelot) event.getTarget();
            Item heldItem = event.getEntityPlayer().getHeldItem(event.getHand()).getItem();
            Random random = event.getWorld().rand;
            if (ocelot.getHealth() < ocelot.getMaxHealth()) {
                if (heldItem == RatsItemRegistry.RAW_RAT) {
                    ocelot.heal(4);
                    event.getWorld().playSound(null, ocelot.posX, ocelot.posY, ocelot.posZ, SoundEvents.ENTITY_LLAMA_EAT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                    event.getWorld().playSound(null, ocelot.posX, ocelot.posY, ocelot.posZ, SoundEvents.ENTITY_CAT_AMBIENT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                    for (int i = 0; i < 3; i++) {
                        event.getWorld().spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, ocelot.posX + random.nextDouble() - random.nextDouble(), ocelot.posY + 0.5 + random.nextDouble() - random.nextDouble(), ocelot.posZ + random.nextDouble() - random.nextDouble(), 0, 0, 0);
                    }
                }
                if (heldItem == RatsItemRegistry.COOKED_RAT) {
                    ocelot.heal(8);
                    event.getWorld().playSound(null, ocelot.posX, ocelot.posY, ocelot.posZ, SoundEvents.ENTITY_LLAMA_EAT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                    event.getWorld().playSound(null, ocelot.posX, ocelot.posY, ocelot.posZ, SoundEvents.ENTITY_CAT_AMBIENT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                    for (int i = 0; i < 3; i++) {
                        event.getWorld().spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, ocelot.posX + random.nextDouble() - random.nextDouble(), ocelot.posY + 0.5 + random.nextDouble() - random.nextDouble(), ocelot.posZ + random.nextDouble() - random.nextDouble(), 0, 0, 0);
                    }
                }
            }
        }
        if(event.getEntityPlayer().isPotionActive(RatsMod.PLAGUE_POTION) && RatsMod.CONFIG_OPTIONS.plagueSpread && !(event.getTarget() instanceof EntityRat)){
            if(event.getTarget() instanceof EntityLivingBase && !((EntityLivingBase) event.getTarget()).isPotionActive(RatsMod.PLAGUE_POTION)){
                ((EntityLivingBase) event.getTarget()).addPotionEffect(new PotionEffect(RatsMod.PLAGUE_POTION, 6000));
                event.getTarget().playSound(SoundEvents.ENTITY_ZOMBIE_INFECT, 1.0F, 1.0F);
            }
        }
    }

    @SubscribeEvent
    public void onHitEntity(LivingAttackEvent event) {
        if(event.getSource().getImmediateSource() instanceof EntityLivingBase && RatsMod.CONFIG_OPTIONS.plagueSpread){
            EntityLivingBase attacker = (EntityLivingBase)event.getSource().getImmediateSource();
            if(attacker.isPotionActive(RatsMod.PLAGUE_POTION) && !(event.getEntityLiving() instanceof EntityRat)){
                if(!event.getEntityLiving().isPotionActive(RatsMod.PLAGUE_POTION)){
                    event.getEntityLiving().addPotionEffect(new PotionEffect(RatsMod.PLAGUE_POTION, 6000));
                    event.getEntityLiving().playSound(SoundEvents.ENTITY_ZOMBIE_INFECT, 1.0F, 1.0F);
                }
            }
        }
    }

        @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() != null && event.getEntity() instanceof EntityIronGolem && RatsMod.CONFIG_OPTIONS.golemsTargetRats) {
            EntityIronGolem golem = (EntityIronGolem) event.getEntity();
            golem.targetTasks.addTask(4, new EntityAINearestAttackableTarget(golem, EntityRat.class, 10, false, false, UNTAMED_RAT_SELECTOR));
        }
        if (event.getEntity() != null && RatUtils.isPredator(event.getEntity()) && event.getEntity() instanceof EntityAnimal) {
            EntityAnimal animal = (EntityAnimal) event.getEntity();
            animal.targetTasks.addTask(5, new EntityAINearestAttackableTarget(animal, EntityRat.class, true));
        }
        if (event.getEntity() != null && event.getEntity() instanceof EntityHusk) {
            if (((EntityHusk) event.getEntity()).getRNG().nextFloat() < RatsMod.CONFIG_OPTIONS.archeologistHatSpawnRate) {
                event.getEntity().setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(RatsItemRegistry.ARCHEOLOGIST_HAT));
                ((EntityLiving) event.getEntity()).setDropChance(EntityEquipmentSlot.HEAD, 0.5F);
            }
        }
        if (event.getEntity() != null && (event.getEntity() instanceof AbstractSkeleton || event.getEntity() instanceof EntityZombie) && BiomeDictionary.hasType(event.getWorld().getBiome(event.getEntity().getPosition()), BiomeDictionary.Type.JUNGLE)) {
            if (((EntityLiving) event.getEntity()).getRNG().nextFloat() < RatsMod.CONFIG_OPTIONS.archeologistHatSpawnRate) {
                event.getEntity().setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(RatsItemRegistry.ARCHEOLOGIST_HAT));
                ((EntityLiving) event.getEntity()).setDropChance(EntityEquipmentSlot.HEAD, 0.5F);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
        if (event.getEntityPlayer().isSneaking() && !event.getEntityPlayer().getPassengers().isEmpty()) {
            for (Entity passenger : event.getEntityPlayer().getPassengers()) {
                if (passenger instanceof EntityRat) {
                    passenger.dismountRidingEntity();
                    passenger.setPosition(event.getEntityPlayer().posX, event.getEntityPlayer().posY, event.getEntityPlayer().posZ);
                    RatsMod.NETWORK_WRAPPER.sendToServer(new MessageRatDismount(passenger.getEntityId()));
                }
            }
        }
    }

    @SubscribeEvent
    public void onGatherCollisionBoxes(GetCollisionBoxesEvent event) {
        if (event.getEntity() instanceof EntityRat) {
            event.getCollisionBoxesList().removeIf(aabb -> ((EntityRat) event.getEntity()).canPhaseThroughBlock(event.getWorld(), new BlockPos(aabb.minX, aabb.minY, aabb.minZ)));
        }
    }

    @SubscribeEvent
    public void onDrops(LivingDropsEvent event) {
        if (event.getEntityLiving() instanceof EntityIllagerPiper && event.getSource().getTrueSource() instanceof EntityPlayer && event.getEntityLiving().world.rand.nextFloat() < RatsMod.CONFIG_OPTIONS.piperHatDropRate + (RatsMod.CONFIG_OPTIONS.piperHatDropRate / 2) * event.getLootingLevel()) {
            event.getDrops().add(new EntityItem(event.getEntity().world, event.getEntityLiving().posX, event.getEntityLiving().posY, event.getEntityLiving().posZ, new ItemStack(RatsItemRegistry.PIPER_HAT)));
        }
    }

    @SubscribeEvent
    public void onLivingHeal(LivingHealEvent event) {
        if (event.getEntityLiving().getActivePotionEffect(RatsMod.PLAGUE_POTION) != null) {
            PotionEffect effect = event.getEntityLiving().getActivePotionEffect(RatsMod.PLAGUE_POTION);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving().world.isRemote && (event.getEntityLiving().isPotionActive(RatsMod.PLAGUE_POTION) || event.getEntityLiving() instanceof EntityRat && ((EntityRat) event.getEntityLiving()).hasPlague())) {
            Random rand = event.getEntityLiving().getRNG();
            if(rand.nextInt(4) == 0) {
                int entitySize = 1;
                if (event.getEntityLiving().getEntityBoundingBox().getAverageEdgeLength() > 0) {
                    entitySize = Math.max(1, (int) event.getEntityLiving().getEntityBoundingBox().getAverageEdgeLength());
                }
                for (int i = 0; i < entitySize; i++) {
                    float motionX = rand.nextFloat() * 0.2F - 0.1F;
                    float motionZ = rand.nextFloat() * 0.2F - 0.1F;
                    RatsMod.PROXY.spawnParticle("flea", event.getEntityLiving().posX + (double) (rand.nextFloat() * event.getEntityLiving().width * 2F) - (double) event.getEntityLiving().width,
                            event.getEntityLiving().posY + (double) (rand.nextFloat() * event.getEntityLiving().height),
                            event.getEntityLiving().posZ + (double) (rand.nextFloat() * event.getEntityLiving().width * 2F) - (double) event.getEntityLiving().width,
                            motionX, 0.0F, motionZ);
                }
            }
        }
    }
}