/*
 * This file is part of aion-unique <aionunique.smfnew.com>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.world.World;
import com.google.inject.Inject;

/**
 * 
 * @author alexa026
 * 
 */
public class CM_DUEL_REQUEST extends AionClientPacket
{
	/**
	* Target object id that client wants to start duel with
	*/
	private int	objectId;
	@Inject
	private World world;

	/**
	* Constructs new instance of <tt>CM_DUAL_REQUEST</tt> packet
	* @param opcode
	*/
	public CM_DUEL_REQUEST(int opcode)
	{
		super(opcode);
	}

	/**
	* {@inheritDoc}
	*/
	@Override
	protected void readImpl()
	{
		objectId = readD();

	}

	@Override
	protected void runImpl()
	{
		final Player activePlayer = getConnection().getActivePlayer();
		final Player targetPlayer = world.findPlayer(objectId);

		RequestResponseHandler responseHandler = new RequestResponseHandler(activePlayer) {
			@Override
			public void acceptRequest(Player requester, Player responder)
			{
				activePlayer.getController().startDuelWith(targetPlayer);
				targetPlayer.getController().startDuelWith(activePlayer);
			}

			public void denyRequest(Player requester, Player responder)
			{
				// TODO find code for STR_DUEL_HE_REJECTED_DUEL
				// activePlayer.getClientConnection().sendPacket(new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_DUEL_HE_REJECTED_DUEL, targetPlayer.getName()));
			}
		};
		
		boolean requested = targetPlayer.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_DUEL_DO_YOU_ACCEPT_DUEL,responseHandler);
		if (!requested){
			// Can't trade with player.
			// TODO: Need to check why and send a error.
		}
		else {
			targetPlayer.getClientConnection().sendPacket(new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_DUEL_DO_YOU_ACCEPT_DUEL, activePlayer.getName()));
		}
	}
}