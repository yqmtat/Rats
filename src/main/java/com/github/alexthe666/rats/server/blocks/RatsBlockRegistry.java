package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.EnumDyeColor;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RatsBlockRegistry {

    @GameRegistry.ObjectHolder(RatsMod.MODID + ":block_of_cheese")
    public static Block BLOCK_OF_CHEESE = new BlockCheese();

    @GameRegistry.ObjectHolder(RatsMod.MODID + ":milk_cauldron")
    public static Block MILK_CAULDRON = new BlockMilkCauldron();

    @GameRegistry.ObjectHolder(RatsMod.MODID + ":cheese_cauldron")
    public static Block CHEESE_CAULDRON = new BlockCheeseCauldron();

    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_hole")
    public static Block RAT_HOLE = new BlockRatHole();

    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_trap")
    public static Block RAT_TRAP = new BlockRatTrap();

    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_cage")
    public static Block RAT_CAGE = new BlockRatCage("rat_cage");

    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_cage_decorated")
    public static Block RAT_CAGE_DECORATED = new BlockRatCageDecorated();

    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_cage_breeding_lantern")
    public static Block RAT_CAGE_BREEDING_LANTERN = new BlockRatCageBreedingLantern();


    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_crafting_table")
    public static Block RAT_CRAFTING_TABLE = new BlockRatCraftingTable();

    public static Block[] RAT_TUBE_COLOR = new Block[16];
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":garbage_pile")
    public static Block GARBAGE_PILE = new BlockGarbage();
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":ratglove_flower")
    public static Block RATGLOVE_FLOWER = new BlockRatgloveFlower();
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":marbled_cheese_raw")
    public static Block MARBLED_CHEESE_RAW = new BlockGeneric("marbled_cheese_raw", Material.ROCK, 2.0F, 10.0F, SoundType.STONE);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":marbled_cheese")
    public static Block MARBLED_CHEESE = new BlockGeneric("marbled_cheese", Material.ROCK, 2.0F, 10.0F, SoundType.STONE);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":marbled_cheese_slab")
    public static Block MARBLED_CHEESE_SLAB = new BlockMarbledCheeseSlab.Half("marbled_cheese_slab", 2.0F, 10.0F, SoundType.STONE);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":marbled_cheese_slab_double")
    public static Block MARBLED_CHEESE_DOUBLESLAB = new BlockMarbledCheeseSlab.Double("marbled_cheese_slab", 2.0F, 10.0F, SoundType.STONE);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":marbled_cheese_stairs")
    public static Block MARBLED_CHEESE_STAIRS = new BlockGenericStairs(MARBLED_CHEESE.getDefaultState(), "marbled_cheese_stairs");
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":marbled_cheese_tile")
    public static Block MARBLED_CHEESE_TILE = new BlockGeneric("marbled_cheese_tile", Material.ROCK, 2.0F, 10.0F, SoundType.STONE);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":marbled_cheese_chiseled")
    public static Block MARBLED_CHEESE_CHISELED = new BlockGeneric("marbled_cheese_chiseled", Material.ROCK, 2.0F, 10.0F, SoundType.STONE);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":marbled_cheese_pillar")
    public static Block MARBLED_CHEESE_PILLAR = new BlockGenericPillar("marbled_cheese_pillar", Material.ROCK, 2.0F, 10.0F, SoundType.STONE);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":marbled_cheese_brick")
    public static Block MARBLED_CHEESE_BRICK = new BlockGeneric("marbled_cheese_brick", Material.ROCK, 2.0F, 10.0F, SoundType.STONE);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":marbled_cheese_brick_slab")
    public static Block MARBLED_CHEESE_BRICK_SLAB = new BlockMarbledCheeseBrickSlab.Half("marbled_cheese_brick_slab", 2.0F, 10.0F, SoundType.STONE);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":marbled_cheese_brick_slab_double")
    public static Block MARBLED_CHEESE_BRICK_DOUBLESLAB = new BlockMarbledCheeseBrickSlab.Double("marbled_cheese_brick_slab", 2.0F, 10.0F, SoundType.STONE);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":marbled_cheese_brick_stairs")
    public static Block MARBLED_CHEESE_BRICK_STAIRS = new BlockGenericStairs(MARBLED_CHEESE_BRICK.getDefaultState(), "marbled_cheese_brick_stairs");
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":marbled_cheese_brick_chiseled")
    public static Block MARBLED_CHEESE_BRICK_CHISELED = new BlockGeneric("marbled_cheese_brick_chiseled", Material.ROCK, 2.0F, 10.0F, SoundType.STONE);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":marbled_cheese_brick_cracked")
    public static Block MARBLED_CHEESE_BRICK_CRACKED = new BlockGeneric("marbled_cheese_brick_cracked", Material.ROCK, 2.0F, 10.0F, SoundType.STONE);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":marbled_cheese_brick_mossy")
    public static Block MARBLED_CHEESE_BRICK_MOSSY = new BlockGeneric("marbled_cheese_brick_mossy", Material.ROCK, 2.0F, 10.0F, SoundType.STONE);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":marbled_cheese_dirt")
    public static Block MARBLED_CHEESE_DIRT = new BlockGeneric("marbled_cheese_dirt", Material.GROUND, 0.5F, 0F, SoundType.GROUND);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":marbled_cheese_grass")
    public static Block MARBLED_CHEESE_GRASS = new BlockMarbledCheeseGrass();
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":marbled_cheese_rat_head")
    public static Block MARBLED_CHEESE_RAT_HEAD = new BlockMarbledCheeseRatHead();
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":marbled_cheese_golem_core")
    public static Block MARBLED_CHEESE_GOLEM_CORE = new BlockGeneric("marbled_cheese_golem_core", Material.ROCK, 5.0F, 30.0F, SoundType.STONE, 0.7F);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":ratlantis_portal")
    public static Block RATLANTIS_PORTAL = new BlockRatlantisPortal();
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":compressed_rat")
    public static Block COMPRESSED_RAT = new BlockCompressedRat();
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":jack_o_ratern")
    public static Block JACK_O_RATERN = new BlockJackORatern();
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":upgrade_combiner")
    public static Block UPGRADE_COMBINER = new BlockUpgradeCombiner();

    static {
        for (int i = 0; i < 16; i++) {
            RAT_TUBE_COLOR[i] = new BlockRatTube(EnumDyeColor.byMetadata(i).getName());
        }
    }

}
