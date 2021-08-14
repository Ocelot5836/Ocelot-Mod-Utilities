package io.github.ocelot.sonar.common.network.message;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

/**
 * <p>A message intended for the specified message handler.</p>
 *
 * @param <T> The interface that should handle this message
 * @author Ocelot
 * @since 3.2.0
 */
public interface SonarMessage<T>
{
    /**
     * Reads the raw message data from the data stream.
     *
     * @param buf The buffer to read from
     */
    void readPacketData(FriendlyByteBuf buf);

    /**
     * Writes the raw message data to the data stream.
     *
     * @param buf The buffer to write to
     */
    void writePacketData(FriendlyByteBuf buf);

    /**
     * Passes this message into the specified handler to process the message.
     *
     * @param handler The handler to process the message
     * @param ctx     The context of the message
     */
    void processPacket(T handler, NetworkEvent.Context ctx);
}
