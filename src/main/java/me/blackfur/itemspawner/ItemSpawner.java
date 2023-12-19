package me.blackfur.itemspawner;

import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ItemSpawner.MODID)
public class ItemSpawner {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "itemspawner";

    public ItemSpawner() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC, "itemspawner-config.toml");

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
    }

    public static HashMap<Item, EntityType<?>> CUSTOM_SPAWN_EGGS = new HashMap<>();

    DefaultDispenseItemBehavior customeggdispensebehaviour = new DefaultDispenseItemBehavior() {
        public @NotNull ItemStack execute(BlockSource p_123523_, ItemStack p_123524_) {
            Direction direction = p_123523_.getBlockState().getValue(DispenserBlock.FACING);
            EntityType<?> entitytype = CUSTOM_SPAWN_EGGS.get(p_123524_.getItem());

            try {
                entitytype.spawn(p_123523_.getLevel(), p_123524_, null, p_123523_.getPos().relative(direction), MobSpawnType.DISPENSER, direction != Direction.UP, false);
            } catch (Exception var6) {
                LOGGER.error("Error while dispensing spawn egg from dispenser at {}", p_123523_.getPos(), var6);
                return ItemStack.EMPTY;
            }

            p_123524_.shrink(1);
            p_123523_.getLevel().gameEvent(null, GameEvent.ENTITY_PLACE, p_123523_.getPos());
            return p_123524_;
        }
    };

    private void commonSetup(final FMLCommonSetupEvent event) {
        for (String pair : Config.ITEM_ENTITY_PAIRS.get()) {
             String[] pairArray = pair.split("->");
             Item item = Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(pairArray[0])));
             EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(pairArray[1]));
             CUSTOM_SPAWN_EGGS.put(item, entityType);
             DispenserBlock.registerBehavior(item, customeggdispensebehaviour);
        }
    }
}
