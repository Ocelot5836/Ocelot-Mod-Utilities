package io.github.ocelot.sonar.common.valuecontainer;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * <p>A {@link NumberValueContainerEntry} that supports Minecraft {@link ResourceLocation}.</p>
 *
 * @author Ocelot
 * @since 3.1.0
 * @deprecated TODO remove in 7.0.0
 */
public class RegistryObjectValueContainerEntry<T extends IForgeRegistryEntry<T>> implements ValueContainerEntry<ResourceLocation>
{
    private final Component displayName;
    private final String name;
    private final IForgeRegistry<T> registry;
    private final T previousValue;
    private T value;
    private Predicate<String> validator;

    public RegistryObjectValueContainerEntry(Component displayName, String name, IForgeRegistry<T> registry, T value)
    {
        this.displayName = displayName;
        this.name = name;
        this.registry = registry;
        this.previousValue = value;
        this.value = value;
        this.validator = createDefaultValidator(this);
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public Component getDisplayName()
    {
        return displayName;
    }

    @Override
    public InputType getInputType()
    {
        return InputType.TEXT_FIELD;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E> E getValue()
    {
        return (E) value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E> E getPreviousValue()
    {
        return (E) previousValue;
    }

    @Override
    public boolean isDirty()
    {
        return !this.value.equals(this.previousValue);
    }

    @Override
    public String getDisplay()
    {
        return Objects.requireNonNull(this.value.getRegistryName()).toString();
    }

    @Override
    public void write(CompoundTag nbt)
    {
        nbt.putString(this.getName(), Objects.requireNonNull(this.value.getRegistryName()).toString());
    }

    @Override
    public void read(CompoundTag nbt)
    {
        this.value = nbt.contains(this.getName(), Constants.NBT.TAG_STRING) ? this.registry.getValue(new ResourceLocation(nbt.getString(this.getName()))) : this.previousValue;
    }

    @Override
    public void parse(String data)
    {
        this.value = this.registry.getValue(new ResourceLocation(data));
    }

    @Override
    public Optional<Predicate<String>> getValidator()
    {
        return Optional.ofNullable(this.validator);
    }

    /**
     * Sets the validator to the specified value.
     *
     * @param validator The new validator value or null for no validator
     */
    public RegistryObjectValueContainerEntry<T> setValidator(@Nullable Predicate<String> validator)
    {
        this.validator = validator;
        return this;
    }

    /**
     * Generates the default validator for registry object entries.
     *
     * @param entry The type of entry to use the validator for
     * @return A new predicate that will be used for text area parsing
     */
    public static Predicate<String> createDefaultValidator(RegistryObjectValueContainerEntry<?> entry)
    {
        return s ->
        {
            if (!ResourceLocation.isValidResourceLocation(s))
                return false;
            return entry.registry.containsKey(new ResourceLocation(s));
        };
    }
}
