/**
 * This file is part of aion-emu.
 *
 *  aion-emu is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-emu is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package aionemu.network.aion;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import aionemu.network.IOServer;
import aionemu.network.aion.serverpackets.Init;
import aionemu_commons.network.IAcceptor;
import aionemu_commons.network.nio.Dispatcher;
import aionemu_commons.network.nio.NioServer;

/**
 * @author -Nemesiss-
 */
public class AionAcceptor implements IAcceptor
{
	@Override public void accept(SelectionKey key) throws IOException
	{
		// For an accept to be pending the channel must be a server socket
		// channel.
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
		// Accept the connection and make it non-blocking
		SocketChannel socketChannel = serverSocketChannel.accept();
		socketChannel.configureBlocking(false);

		// Register the new SocketChannel with our Selector, indicating
		// we'd like to be notified when there's data waiting to be read
		AionConnection con = new AionConnection(socketChannel);

		Dispatcher readDispatcher = IOServer.getInstance().getReadDispatcher();
		SelectionKey readKey = readDispatcher.register(socketChannel, SelectionKey.OP_READ, con);

		Dispatcher writeDispatcher = IOServer.getInstance().getWriteDispatcher();
		if (writeDispatcher != readDispatcher)
			con.setWriteKey(writeDispatcher.register(socketChannel, 0, con));
		else
			con.setWriteKey(readKey);

		con.sendPacket(new Init(con));
	}

	@Override public String getName()
	{
		return "Aion Connections";
	}
}