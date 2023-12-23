package me.blackfur.itemspawner;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

public class Config {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ITEM_ENTITY_PAIRS;

    static {
        BUILDER.push("Base Config");
        ITEM_ENTITY_PAIRS = BUILDER.comment("A list of items and their entities").defineList("item_entity_pairs", List.of("minecraft:iron_ingot->minecraft:iron_golem"), entry -> true);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
