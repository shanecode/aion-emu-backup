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
package com.aionemu.loginserver.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.aionemu.commons.network.DisconnectionTask;
import com.aionemu.commons.network.DisconnectionThreadPool;

/**
 * @author -Nemesiss-
 */
public class ThreadPoolManager implements DisconnectionThreadPool
{
	/**
	 * Logger for this class
	 */
	private static final Logger			log			= Logger.getLogger(ThreadPoolManager.class);

	private static ThreadPoolManager	instance	= new ThreadPoolManager();

	private ScheduledThreadPoolExecutor	scheduledThreadPool;
	private ScheduledThreadPoolExecutor	disconnectionScheduledThreadPool;

	private ThreadPoolExecutor			aionPacketsThreadPool;
	private ThreadPoolExecutor			gameServerPacketsThreadPool;

	/**
	 * @return ThreadPoolManager instance.
	 */
	public static ThreadPoolManager getInstance()
	{
		return instance;
	}

	/**
	 * Constructor.
	 */
	private ThreadPoolManager()
	{
		scheduledThreadPool = new ScheduledThreadPoolExecutor(4, new PriorityThreadFactory("ScheduledThreadPool",
			Thread.NORM_PRIORITY));
		scheduledThreadPool.setRemoveOnCancelPolicy(true);

		disconnectionScheduledThreadPool = new ScheduledThreadPoolExecutor(4, new PriorityThreadFactory("ScheduledThreadPool",
			Thread.NORM_PRIORITY));
		disconnectionScheduledThreadPool.setRemoveOnCancelPolicy(true);

		aionPacketsThreadPool = new ThreadPoolExecutor(6, 8, 15L, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>(),
			new PriorityThreadFactory("Aion Packet Pool", Thread.NORM_PRIORITY + 1));

		gameServerPacketsThreadPool = new ThreadPoolExecutor(4, Integer.MAX_VALUE, 5L, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>(), new PriorityThreadFactory("Game Server Packet Pool",
				Thread.NORM_PRIORITY + 3));

	}

	@SuppressWarnings("unchecked")
	public <T extends Runnable> ScheduledFuture<T> schedule(T r, long delay)
	{
		try
		{
			if (delay < 0)
				delay = 0;
			return (ScheduledFuture<T>) scheduledThreadPool.schedule(r, delay, TimeUnit.MILLISECONDS);
		}
		catch (RejectedExecutionException e)
		{
			return null; /* shutdown, ignore */
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends Runnable> ScheduledFuture<T> scheduleAtFixedRate(T r, long initial, long delay)
	{
		try
		{
			if (delay < 0)
				delay = 0;
			if (initial < 0)
				initial = 0;
			return (ScheduledFuture<T>) scheduledThreadPool.scheduleAtFixedRate(r, initial, delay,
				TimeUnit.MILLISECONDS);
		}
		catch (RejectedExecutionException e)
		{
			return null;
		}
	}

	public void executeAionPacket(Runnable pkt)
	{
		aionPacketsThreadPool.execute(pkt);
	}

	public void executeGsPacket(Runnable pkt)
	{
		gameServerPacketsThreadPool.execute(pkt);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void scheduleDisconnection(DisconnectionTask dt, long delay)
	{
		if (delay < 0)
			delay = 0;
		scheduledThreadPool.schedule(dt, delay, TimeUnit.MILLISECONDS);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void waitForDisconnectionTasks()
	{
		try
		{
			disconnectionScheduledThreadPool.shutdown();
			disconnectionScheduledThreadPool.awaitTermination(6,TimeUnit.MINUTES);
		}
		catch(Exception e){}
	}

	private class PriorityThreadFactory implements ThreadFactory
	{
		private int				prio;
		private String			name;
		private AtomicInteger	threadNumber	= new AtomicInteger(1);
		private ThreadGroup		group;

		public PriorityThreadFactory(String name, int prio)
		{
			this.prio = prio;
			this.name = name;
			group = new ThreadGroup(this.name);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
		 */
		public Thread newThread(Runnable r)
		{
			Thread t = new Thread(group, r);
			t.setName(name + "-" + threadNumber.getAndIncrement());
			t.setPriority(prio);
			t.setUncaughtExceptionHandler(new ThreadUncaughtExceptionHandler());
			return t;
		}

		public ThreadGroup getGroup()
		{
			return group;
		}
	}

	/**
	 * Shutdown all thread pools.
	 */
	public void shutdown()
	{
		try
		{
			scheduledThreadPool.shutdown();
			aionPacketsThreadPool.shutdown();
			gameServerPacketsThreadPool.shutdown();
			scheduledThreadPool.awaitTermination(2, TimeUnit.SECONDS);
			aionPacketsThreadPool.awaitTermination(2, TimeUnit.SECONDS);
			gameServerPacketsThreadPool.awaitTermination(2, TimeUnit.SECONDS);
			log.info("All ThreadPools are now stopped");
		}
		catch (InterruptedException e)
		{
			log.error("Can't shutdown ThreadPoolManager", e);
		}
	}
}