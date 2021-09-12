package io.github.ocelot.sonar;

import io.github.ocelot.sonar.block.TestBlock;
import io.github.ocelot.sonar.client.TestClientInit;
import io.github.ocelot.sonar.common.util.SortedItemGroup;
import io.github.ocelot.sonar.item.CrashItem;
import io.github.ocelot.sonar.network.TestMessageHandler;
import io.github.ocelot.sonar.tileentity.TestTileEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(TestMod.MOD_ID)
public class TestMod
{
    public static final String MOD_ID = "examplemod";

    public static final SortedItemGroup TEST_GROUP = new SortedItemGroup(MOD_ID)
    {
        @Override
        public ItemStack makeIcon()
        {
            return new ItemStack(TEST_BLOCK.get());
        }
    };

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<TestBlock> TEST_BLOCK = BLOCKS.register("test", () -> new TestBlock(Block.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<BlockItem> TEST_BLOCK_ITEM = ITEMS.register("test", () -> new BlockItem(TEST_BLOCK.get(), new Item.Properties().tab(TEST_GROUP)));
    public static final RegistryObject<CrashItem> TEST_CRASH_ITEM = ITEMS.register("test_crash", () -> new CrashItem(new Item.Properties().tab(TEST_GROUP)));

    public static final RegistryObject<BlockEntityType<TestTileEntity>> TEST_TILE_ENTITY = TILE_ENTITIES.register("test", () -> BlockEntityType.Builder.of(TestTileEntity::new, TEST_BLOCK.get()).build(null));

    public TestMod()
    {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        Sonar.init(modBus, SonarModule.values());
        BLOCKS.register(modBus);
        TILE_ENTITIES.register(modBus);
        ITEMS.register(modBus);
        modBus.addListener(this::init);
        modBus.addListener(this::initClient);
    }

    private void init(FMLCommonSetupEvent event)
    {
        TestMessageHandler.init();
    }

    private void initClient(FMLClientSetupEvent event)
    {
        TestClientInit.init();
    }
}
