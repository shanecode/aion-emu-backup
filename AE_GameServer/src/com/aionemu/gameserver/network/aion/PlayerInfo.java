/**
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
package com.aionemu.gameserver.network.aion;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import com.aionemu.gameserver.model.account.PlayerAccountData;
import com.aionemu.gameserver.model.gameobjects.player.PlayerAppearance;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.gameobjects.player.PlayerItems;

/**
 * 
 * @author AEJTester
 * @author Nemesiss
 * @author Niato
 */
public abstract class PlayerInfo extends AionServerPacket
{
	private static Logger log= Logger.getLogger(PlayerInfo.class);

	protected PlayerInfo()
	{
		
	}


	protected void writePlayerInfo(ByteBuffer buf, PlayerAccountData accPlData, PlayerItems playerItems)
	{
		PlayerCommonData pbd = accPlData.getPlayerCommonData();
		final int raceId = pbd.getRace().getRaceId();
		final int genderId = pbd.getGender().getGenderId();
		final PlayerAppearance playerAppearance = accPlData.getAppereance();
		writeD(buf, pbd.getPlayerObjId());
		writeS(buf, pbd.getName());
		/**
		 * Stupid NC...
		 */
		int size = 44 - (pbd.getName().length() * 2 + 2);
		byte[] stupidNc = new byte[size];
		writeB(buf, stupidNc);
		writeD(buf, genderId);
		writeD(buf, raceId);
		writeD(buf, pbd.getPlayerClass().getClassId());
		writeD(buf, playerAppearance.getVoice());
		writeD(buf, playerAppearance.getSkinRGB());
		writeD(buf, playerAppearance.getHairRGB());
		writeD(buf, playerAppearance.getEyeRGB());
		writeD(buf, playerAppearance.getLipRGB());
		writeC(buf, playerAppearance.getFace());
		writeC(buf, playerAppearance.getHair());
		writeC(buf, playerAppearance.getDeco());
		writeC(buf, playerAppearance.getTattoo());
		writeC(buf, 4);// always 4 o0
		writeC(buf, playerAppearance.getFaceShape());
		writeC(buf, playerAppearance.getForehead());
		writeC(buf, playerAppearance.getEyeHeight());
		writeC(buf, playerAppearance.getEyeSpace());
		writeC(buf, playerAppearance.getEyeWidth());
		writeC(buf, playerAppearance.getEyeSize());
		writeC(buf, playerAppearance.getEyeShape());
		writeC(buf, playerAppearance.getEyeAngle());
		writeC(buf, playerAppearance.getBrowHeight());
		writeC(buf, playerAppearance.getBrowAngle());
		writeC(buf, playerAppearance.getBrowShape());
		writeC(buf, playerAppearance.getNose());
		writeC(buf, playerAppearance.getNoseBridge());
		writeC(buf, playerAppearance.getNoseWidth());
		writeC(buf, playerAppearance.getNoseTip());
		writeC(buf, playerAppearance.getCheek());
		writeC(buf, playerAppearance.getLipHeight());
		writeC(buf, playerAppearance.getMouthSize());
		writeC(buf, playerAppearance.getLipSize());
		writeC(buf, playerAppearance.getSmile());
		writeC(buf, playerAppearance.getLipShape());
		writeC(buf, playerAppearance.getJawHeigh());
		writeC(buf, playerAppearance.getChinJut());
		writeC(buf, playerAppearance.getEarShape());
		writeC(buf, playerAppearance.getHeadSize());
		// 1.5.x 0x00, shoulderSize, armLength, legLength (BYTE) after HeadSize
		writeC(buf, 0x00); // 0x00
		writeC(buf, 0x01); // shoulderSize
		writeC(buf, 0x01); // armLength
		writeC(buf, 0x01); // legLength
		writeC(buf, playerAppearance.getNeck());
		writeC(buf, playerAppearance.getNeckLength());
		writeC(buf, playerAppearance.getShoulders());
		writeC(buf, playerAppearance.getTorso());
		writeC(buf, playerAppearance.getChest());
		writeC(buf, playerAppearance.getWaist());
		writeC(buf, playerAppearance.getHips());
		writeC(buf, playerAppearance.getArmThickness());
		writeC(buf, playerAppearance.getHandSize());
		writeC(buf, playerAppearance.getLegThicnkess());
		writeC(buf, playerAppearance.getFootSize());
		writeC(buf, playerAppearance.getFacialRate());
		writeC(buf, 0x00); // always 0 may be acessLevel
		writeC(buf, 0x00); // always 0 - unk
		writeF(buf, playerAppearance.getHeight());
		int raceSex = 100000 + raceId * 2 + genderId;
		writeD(buf, raceSex);
		writeD(buf, pbd.getPosition().getMapId());//mapid for preloading map
		writeF(buf, pbd.getPosition().getX());
		writeF(buf, pbd.getPosition().getY());
		writeF(buf, pbd.getPosition().getZ());
		writeD(buf, 0);// unk 0x6f
		writeD(buf, pbd.getLevel());// lvl confirmed
		writeD(buf, -1);// unk -1 -1 = played 0 = neverplayed
		writeD(buf, 0);// unk 0
		writeD(buf, 0);// unk 41549824 can be 0
		writeD(buf, 0);// unk 1 can be 0
		writeD(buf, 0);// unk 25118405 elyos : 25642769
		writeD(buf, 0);// unk 1
		writeD(buf, 0);// unk 0
		writeD(buf, 0);// unk 2 can be 0
		writeD(buf, 0);// unk 0
		writeD(buf, 0);// unk 0
		writeD(buf, 0);// unk 1242826833
		writeD(buf, 0);// unk 0
		writeD(buf, 0);// unk 0
		writeD(buf, 0);// unk 0
		writeD(buf, 0);// unk 73138176
		writeD(buf, 0);// unk 0
		writeD(buf, 0);// unk 73182320
		writeD(buf, 0);// unk 0
		writeD(buf, 0);// unk 50379392
		writeD(buf, 0);// unk 1242638636

		int itemsSize = 0;
		int i=0;
		
		if (playerItems.getWarmer()!=0)
		{
			 writeC(buf, i);
		     writeD(buf, playerItems.getWarmer());
		     writeD(buf, 0);
		     writeD(buf, 0);
		     i++;
		     itemsSize = itemsSize + 13;
		}
    
		if (playerItems.getShield()!=0)
		{
			 writeC(buf, i);
		     writeD(buf, playerItems.getShield());
		     writeD(buf, 0);
		     writeD(buf, 0);
		     i++;
		     itemsSize = itemsSize + 13;
		}

	    
		if (playerItems.getHelmet()!=0)
		{
			  writeC(buf, i);
			  writeD(buf, playerItems.getHelmet());
			  writeD(buf, 0);
			  writeD(buf, 0);
			  i++;
			  itemsSize = itemsSize + 13;
		}

		if (playerItems.getArmor()!=0)
		{
			writeC(buf, i);
		    writeD(buf, playerItems.getArmor());
		    writeD(buf, 0);
		    writeD(buf, 0);
		    i++;
		    itemsSize = itemsSize + 13;
		}

		if (playerItems.getBoots()!=0)
		{
			  writeC(buf, i);
			  writeD(buf, playerItems.getBoots());
			  writeD(buf, 0);
			  writeD(buf, 0);
			  i++;
			  itemsSize = itemsSize + 13;
		}

		if (playerItems.getLearrings()!=0)
		{
			 writeC(buf, i);
		     writeD(buf, playerItems.getLearrings());
		     writeD(buf, 0);
		     writeD(buf, 0);
		     i++;
		     itemsSize = itemsSize + 13;
		}

		
		if (playerItems.getRearrings()!=0)
		{
			 writeC(buf, i);
		     writeD(buf, playerItems.getRearrings());
		     writeD(buf, 0);
		     writeD(buf, 0);
		     i++;
		     itemsSize = itemsSize + 13;
		}

		
		if (playerItems.getLring()!=0)
		{
			 writeC(buf, i);
		     writeD(buf, playerItems.getLring());
		     writeD(buf, 0);
		     writeD(buf, 0);
		     i++;
		     itemsSize = itemsSize + 13;
		}

		
		
		if (playerItems.getRring()!=0)
		{
			 writeC(buf, i);
		     writeD(buf, playerItems.getRring());
		     writeD(buf, 0);
		     writeD(buf, 0);
		     i++;
		     itemsSize = itemsSize + 13;
		}

	    
		if (playerItems.getNecklace()!=0)
		{
			 writeC(buf, i);
			 writeD(buf, playerItems.getNecklace());
		     writeD(buf, 0);
		     writeD(buf, 0);
		     i++;
		     itemsSize = itemsSize + 13;
		}

		
		if (playerItems.getPauldron()!=0)
		{
			  writeC(buf, i);
			  writeD(buf, playerItems.getPauldron());
			  writeD(buf, 0);
			  writeD(buf, 0);
			  i++;
			  itemsSize = itemsSize + 13;
		}

		if (playerItems.getPants()!=0)
		{
			 writeC(buf, i);
		     writeD(buf, playerItems.getPants());
		     writeD(buf, 0);
		     writeD(buf, 0);
		     i++;
		     itemsSize = itemsSize + 13;
		}

		if (playerItems.getRshard()!=0)
		{
			 writeC(buf, i);
			 writeD(buf, playerItems.getRshard());
			 writeD(buf, 0);
			 writeD(buf, 0);
			 i++;
			 itemsSize = itemsSize + 13;
		}

		if (playerItems.getLshard()!=0)
		{
			 writeC(buf, i);
		     writeD(buf, playerItems.getLshard());
		     writeD(buf, 0);
		     writeD(buf, 0);
		     i++;
			 itemsSize = itemsSize + 13;
		}

		if (playerItems.getWing()!=0)
		{
			 writeC(buf, i);
		     writeD(buf, playerItems.getWing());
		     writeD(buf, 0);
		     writeD(buf, 0);
		     i++;
			 itemsSize = itemsSize + 13;
		}

		
		stupidNc = new byte[208-itemsSize];
		writeB(buf, stupidNc);
		writeD(buf, accPlData.getDeletionTimeInSeconds());
		writeD(buf, 0x00);// unk
		
	}
}
