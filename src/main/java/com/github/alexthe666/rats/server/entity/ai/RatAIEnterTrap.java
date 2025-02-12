package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatUtils;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatTrap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RatAIEnterTrap extends RatAIMoveToBlock {
    private final EntityRat entity;

    public RatAIEnterTrap(EntityRat entity) {
        super(entity, 1.0F, 20);
        this.entity = entity;
        this.distanceCheck = 2.5D;
    }

    public static boolean isTrap(World world, BlockPos pos) {
        IBlockState block = world.getBlockState(pos.up());
        if (block.getBlock() == RatsBlockRegistry.RAT_TRAP) {
            TileEntity entity = world.getTileEntity(pos.up());
            return entity != null && !((TileEntityRatTrap) entity).isShut && !((TileEntityRatTrap) entity).getBait().isEmpty();
        }
        return false;
    }

    @Override
    public boolean shouldExecute() {
        if (!this.entity.canMove() || this.entity.isTamed() || this.entity.isInCage()) {
            return false;
        }
        if (!this.entity.getHeldItem(EnumHand.MAIN_HAND).isEmpty()) {
            return false;
        }
        if (this.entity.isTamed()) {
            return false;
        }
        if (this.runDelay <= 0) {
            if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.entity.world, this.entity)) {
                return false;
            }
        }
        return super.shouldExecute();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return super.shouldContinueExecuting() && this.entity.getHeldItem(EnumHand.MAIN_HAND).isEmpty();
    }

    public boolean canSeeChest() {
        RayTraceResult rayTrace = RatUtils.rayTraceBlocksIgnoreRatholes(entity.world, entity.getPositionVector(), new Vec3d(destinationBlock.getX() + 0.5, destinationBlock.getY() + 0.5, destinationBlock.getZ() + 0.5), false);
        if (rayTrace != null && rayTrace.hitVec != null) {
            BlockPos sidePos = rayTrace.getBlockPos();
            BlockPos pos = new BlockPos(rayTrace.hitVec);
            return entity.world.isAirBlock(sidePos) || entity.world.isAirBlock(pos) || this.entity.world.getTileEntity(pos) == this.entity.world.getTileEntity(destinationBlock);
        }
        return true;
    }

    @Override
    public void updateTask() {
        super.updateTask();
        if (this.getIsAboveDestination() && this.destinationBlock != null) {
            BlockPos trapPos = this.destinationBlock.up();
            TileEntity entity = this.entity.world.getTileEntity(trapPos);
            if (entity instanceof TileEntityRatTrap && !((TileEntityRatTrap) entity).isShut && !((TileEntityRatTrap) entity).getBait().isEmpty()) {
                double distance = this.entity.getDistance(trapPos.getX(), trapPos.getY(), trapPos.getZ());
                if (distance < 0.5F && canSeeChest()) {
                    ItemStack duplicate = ((TileEntityRatTrap) entity).getBait().copy();
                    duplicate.setCount(1);
                    if (!this.entity.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && !this.entity.world.isRemote) {
                        this.entity.entityDropItem(this.entity.getHeldItem(EnumHand.MAIN_HAND), 0.0F);
                    }
                    this.entity.setHeldItem(EnumHand.MAIN_HAND, duplicate);
                    ((TileEntityRatTrap) entity).getBait().shrink(1);
                    this.entity.fleePos = this.destinationBlock;
                }
            }

        }
    }

    @Override
    protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
        return isTrap(worldIn, pos);
    }
}
