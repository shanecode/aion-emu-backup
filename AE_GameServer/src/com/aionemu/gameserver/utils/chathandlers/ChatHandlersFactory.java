/*
 * This file is part of aion-emu <aion-emu.com>.
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
package com.aionemu.gameserver.utils.chathandlers;

import com.aionemu.gameserver.utils.chathandlers.admincommands.AdminCommand;
import com.aionemu.gameserver.utils.chathandlers.admincommands.DeleteSpawn;
import com.aionemu.gameserver.utils.chathandlers.admincommands.MoveTo;
import com.aionemu.gameserver.utils.chathandlers.admincommands.ReloadSpawns;
import com.aionemu.gameserver.utils.chathandlers.admincommands.SaveSpawnData;
import com.aionemu.gameserver.utils.chathandlers.admincommands.SendFakeServerPacket;
import com.aionemu.gameserver.utils.chathandlers.admincommands.SpawnNpc;
import com.aionemu.gameserver.utils.chathandlers.admincommands.UnloadSpawn;
import com.aionemu.gameserver.utils.chathandlers.admincommands.AdvSendFakeServerPacket;
import com.google.inject.Injector;

/**
 * This factory is responsible for creating class tree starting with {@link ChatHandlers}
 * 
 * @author Luno
 * 
 */
public class ChatHandlersFactory
{
	private Injector	injector;

	/**
	 * @param injector
	 */
	public ChatHandlersFactory(Injector injector)
	{
		this.injector = injector;
	}

	/**
	 * Creates and return object of {@link ChatHandlers} class
	 * 
	 * @return
	 */
	public ChatHandlers createChatHandlers()
	{
		ChatHandlers result = new ChatHandlers();

		AdminCommandChatHandler adminCCH = new AdminCommandChatHandler();
		result.addChatHandler(adminCCH);

		// Inits admin command handlers (TODO: those admin command handlers may be loaded as scripts)
		addAdminCommand(adminCCH, new SendFakeServerPacket());
		addAdminCommand(adminCCH, new AdvSendFakeServerPacket());
		addAdminCommand(adminCCH, new SpawnNpc());
		addAdminCommand(adminCCH, new SaveSpawnData());
		addAdminCommand(adminCCH, new DeleteSpawn());
		addAdminCommand(adminCCH, new MoveTo());
		addAdminCommand(adminCCH, new UnloadSpawn());
		addAdminCommand(adminCCH, new ReloadSpawns());

		return result;
	}

	private void addAdminCommand(AdminCommandChatHandler adm, AdminCommand comm)
	{
		injector.injectMembers(comm);
		adm.registerAdminCommand(comm);
	}
}