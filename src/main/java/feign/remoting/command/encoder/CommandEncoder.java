package feign.remoting.command.encoder;

import java.io.Serializable;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * Encode command.
 * 
 * @author jiangping
 * @version $Id: CommandEncoder.java, v 0.1 Mar 10, 2016 11:33:02 AM jiangping Exp $
 */
public interface CommandEncoder {

    /**
     * Encode object into bytes.
     * 
     * @param ctx
     * @param msg
     * @param out
     * @throws Exception
     */
    void encode(ChannelHandlerContext ctx, Serializable msg, ByteBuf out) throws Exception;

}
