package com.github.alexthe666.rats.server.world.gen;

import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockVine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.Random;

public class WorldGenRatRuin extends WorldGenerator {

    public EnumFacing facing;

    public WorldGenRatRuin(EnumFacing facing) {
        super(false);

        this.facing = facing;

    }

    public static boolean isPartOfRuin(IBlockState state) {
        return state.getBlock() == RatsBlockRegistry.MARBLED_CHEESE || state.getBlock() == RatsBlockRegistry.MARBLED_CHEESE_RAW || state.getBlock() == RatsBlockRegistry.MARBLED_CHEESE_PILLAR
                || state.getBlock() == RatsBlockRegistry.MARBLED_CHEESE_CHISELED || state.getBlock() == RatsBlockRegistry.MARBLED_CHEESE_STAIRS || state.getBlock() == RatsBlockRegistry.MARBLED_CHEESE_SLAB
                || state.getBlock() == RatsBlockRegistry.MARBLED_CHEESE_DOUBLESLAB || state.getBlock() == RatsBlockRegistry.MARBLED_CHEESE_BRICK || state.getBlock() == RatsBlockRegistry.MARBLED_CHEESE_BRICK_CHISELED
                || state.getBlock() == RatsBlockRegistry.MARBLED_CHEESE_BRICK_CRACKED || state.getBlock() == RatsBlockRegistry.MARBLED_CHEESE_BRICK_MOSSY || state.getBlock() == RatsBlockRegistry.MARBLED_CHEESE_STAIRS
                || state.getBlock() == RatsBlockRegistry.MARBLED_CHEESE_BRICK_SLAB || state.getBlock() == RatsBlockRegistry.MARBLED_CHEESE_DOUBLESLAB || state.getBlock() == RatsBlockRegistry.MARBLED_CHEESE_TILE;
    }

    public static Rotation getRotationFromFacing(EnumFacing facing) {
        switch (facing) {
            case EAST:
                return Rotation.CLOCKWISE_90;
            case SOUTH:
                return Rotation.CLOCKWISE_180;
            case WEST:
                return Rotation.COUNTERCLOCKWISE_90;
            default:
                return Rotation.NONE;
        }
    }

    public static BlockPos getGround(BlockPos pos, World world) {
        return getGround(pos.getX(), pos.getZ(), world);
    }

    public static BlockPos getGround(int x, int z, World world) {
        BlockPos skyPos = new BlockPos(x, world.getHeight(), z);
        while ((!world.getBlockState(skyPos).isOpaqueCube() || canHeightSkipBlock(skyPos, world)) && skyPos.getY() > 1) {
            skyPos = skyPos.down();
        }
        return skyPos;
    }

    private static boolean canHeightSkipBlock(BlockPos pos, World world) {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock() instanceof BlockLog || state.getBlock() instanceof BlockLiquid;
    }

    public boolean generate(World worldIn, Random rand, BlockPos position) {
        RatStructure model = RatStructure.HUT;
        int chance = rand.nextInt(99) + 1;
        BlockPos offsetPos = BlockPos.ORIGIN;
        if (chance < 10) {
            model = rand.nextBoolean() ? RatStructure.CHEESE_STATUETTE : RatStructure.GIANT_CHEESE;
        } else if (chance < 50) {
            switch (rand.nextInt(5)) {
                case 1:
                    model = RatStructure.PILLAR;
                    break;
                case 2:
                    model = RatStructure.PILLAR_LEANING;
                    offsetPos = new BlockPos(0, -2, 0);
                    break;
                case 3:
                    model = RatStructure.PILLAR_COLLECTION;
                    break;
                case 4:
                    model = RatStructure.PILLAR_THIN;
                    break;
                default:
                    model = RatStructure.PILLAR_LEANING;
                    offsetPos = new BlockPos(0, -2, 0);
                    break;
            }
        } else if (chance < 70) {
            switch (rand.nextInt(5)) {
                case 1:
                    model = RatStructure.TOWER;
                    break;
                case 2:
                    model = RatStructure.FORUM;
                    break;
                case 3:
                    model = RatStructure.HUT;
                    break;
                case 4:
                    model = RatStructure.PALACE;
                    break;
                default:
                    model = RatStructure.TEMPLE;
                    break;
            }
        } else if (chance < 96) {
            switch (rand.nextInt(4)) {
                case 1:
                    model = RatStructure.SPHINX;
                    break;
                case 2:
                    model = RatStructure.LINCOLN;
                    break;
                case 3:
                    model = RatStructure.CHEESE_STATUETTE;
                    break;
                default:
                    model = RatStructure.HEAD;
                    break;
            }
        } else {
            model = RatStructure.COLOSSUS;
        }
        position = position.add(rand.nextInt(8) - 4, 1, rand.nextInt(8) - 4);
        MinecraftServer server = worldIn.getMinecraftServer();
        BlockPos height = getGround(position, worldIn);
        IBlockState dirt = worldIn.getBlockState(height.down(2));
        TemplateManager templateManager = worldIn.getSaveHandler().getStructureTemplateManager();
        Template template = templateManager.getTemplate(server, model.structureLoc);
        PlacementSettings settings = new PlacementSettings().setRotation(getRotationFromFacing(facing));
        BlockPos pos = height.offset(facing, template.getSize().getZ() / 2).offset(facing.rotateYCCW(), template.getSize().getX() / 2);
        settings.setReplacedBlock(Blocks.AIR);
        if (checkIfCanGenAt(worldIn, pos, template.getSize().getX(), template.getSize().getZ(), facing)) {
            template.addBlocksToWorld(worldIn, pos, new RatsStructureProcessor(0.75F * rand.nextFloat() + 0.75F), settings, 2);
            for (BlockPos underPos : BlockPos.getAllInBox(
                    height.down().offset(facing, -template.getSize().getZ() / 2).offset(facing.rotateYCCW(), -template.getSize().getX() / 2),
                    height.down(3).offset(facing, template.getSize().getZ() / 2).offset(facing.rotateYCCW(), template.getSize().getX() / 2)
            )) {
                if (!worldIn.getBlockState(underPos).isOpaqueCube() && !worldIn.isAirBlock(underPos.up())) {
                    worldIn.setBlockState(underPos, dirt);
                    worldIn.setBlockState(underPos.down(), dirt);
                    worldIn.setBlockState(underPos.down(2), dirt);
                }
            }

            for (BlockPos vinePos : BlockPos.getAllInBox(
                    height.offset(facing, -template.getSize().getZ() / 2).offset(facing.rotateYCCW(), -template.getSize().getX() / 2),
                    height.up(template.getSize().getY()).offset(facing, template.getSize().getZ() / 2).offset(facing.rotateYCCW(), template.getSize().getX() / 2)
            )) {
                if (worldIn.getBlockState(vinePos).isOpaqueCube()) {
                    for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL.facings()) {
                        if (!worldIn.getBlockState(vinePos.offset(enumfacing)).isOpaqueCube() && worldIn.rand.nextInt(8) == 0) {
                            EnumFacing opposFacing = enumfacing.getOpposite();
                            IBlockState iblockstate = Blocks.VINE.getDefaultState().withProperty(BlockVine.NORTH, Boolean.valueOf(opposFacing == EnumFacing.NORTH)).withProperty(BlockVine.EAST, Boolean.valueOf(opposFacing == EnumFacing.EAST)).withProperty(BlockVine.SOUTH, Boolean.valueOf(opposFacing == EnumFacing.SOUTH)).withProperty(BlockVine.WEST, Boolean.valueOf(opposFacing == EnumFacing.WEST));
                            worldIn.setBlockState(vinePos.offset(enumfacing), iblockstate, 2);
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean checkIfCanGenAt(World world, BlockPos middle, int x, int z, EnumFacing facing) {
        return !isPartOfRuin(world.getBlockState(middle.offset(facing, z / 2))) && !isPartOfRuin(world.getBlockState(middle.offset(facing.getOpposite(), z / 2))) &&
                !isPartOfRuin(world.getBlockState(middle.offset(facing.rotateY(), x / 2))) && !isPartOfRuin(world.getBlockState(middle.offset(facing.rotateYCCW(), x / 2)));
    }
}