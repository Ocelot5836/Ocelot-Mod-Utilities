package io.github.ocelot.sonar.common.valuecontainer;

import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * <p>Specifies a {@link ValueContainerEntry} as being for a {@link Vec3i} type</p>
 *
 * @author Ocelot
 * @since 3.1.0
 * @deprecated TODO remove in 7.0.0
 */
public class Vector3iValueContainerEntry extends AbstractVectorValueContainerEntry<Vec3i>
{
    public Vector3iValueContainerEntry(Component displayName, String name, Vec3i value)
    {
        this(displayName, name, value, null, null);
    }

    public Vector3iValueContainerEntry(Component displayName, String name, Vec3i value, Vec3i minValue, Vec3i maxValue)
    {
        super(displayName, name, value, minValue, maxValue, false);
    }

    @Override
    protected Vec3i create(Number x, Number y, Number z)
    {
        return new Vec3i(x.intValue(), y.intValue(), z.intValue());
    }

    @Override
    protected Number getX(Vec3i value)
    {
        return value.getX();
    }

    @Override
    protected Number getY(Vec3i value)
    {
        return value.getY();
    }

    @Override
    protected Number getZ(Vec3i value)
    {
        return value.getZ();
    }

    @Override
    public void write(CompoundTag nbt)
    {
        CompoundTag valueNbt = new CompoundTag();
        valueNbt.putInt("x", this.value.getX());
        valueNbt.putInt("y", this.value.getY());
        valueNbt.putInt("z", this.value.getZ());
        nbt.put(this.getName(), valueNbt);
    }

    @Override
    public void read(CompoundTag nbt)
    {
        if (nbt.contains(this.getName(), Constants.NBT.TAG_COMPOUND))
        {
            CompoundTag valueNbt = nbt.getCompound(this.getName());
            this.value = new Vec3i(valueNbt.getInt("x"), valueNbt.getInt("y"), valueNbt.getInt("z"));
        }
        else
        {
            this.value = new Vec3i(0, 0, 0);
        }
    }

    @Override
    public void parse(String data)
    {
        String[] tokens = String.valueOf(data).split(",");
        Number x = NumberUtils.createNumber(tokens[0].trim());
        Number y = NumberUtils.createNumber(tokens[1].trim());
        Number z = NumberUtils.createNumber(tokens[2].trim());
        this.value = this.clamp(new Vec3i(x.intValue(), y.intValue(), z.intValue()), this.getMinValue(), this.getMaxValue());
    }
}
