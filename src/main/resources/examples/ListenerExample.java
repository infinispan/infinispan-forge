/**
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA
 */
package org.infinispan.examples.lifespan;

import org.infinispan.Cache;
import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryModified;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryRemoved;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryVisited;
import org.infinispan.notifications.cachelistener.event.CacheEntryEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryVisitedEvent;

/**
 * Sample application code. For more examples visit
 * https://docs.jboss.org/author/display/ISPN/Getting+Started+Guide#GettingStartedGuide-5minutetutorial
 */
public class ListenerExample {

	/**
	* TIP: For more examples on using listeners visit
	* http://community.jboss.org/wiki/ListenersandNotifications
	*/
	public void registeringListeners() {
		System.out.println("\n\n4.  Demonstrating use of listeners.");
		Cache<Integer, String> anotherCache = SampleCacheContainer.getCache("another");
		System.out.println("  Attaching listener");
		MyListener l = new MyListener();
		anotherCache.addListener(l);

		System.out.println("  Put #1");
		anotherCache.put(1, "One");
		System.out.println("  Put #2");
		anotherCache.put(2, "Two");
		System.out.println("  Put #3");
		anotherCache.put(3, "Three");
	}

	public static void main(String[] args) throws Exception {
		System.out.println("\n\n\n   ********************************  \n\n\n");
		System.out.println(
				"Hello. This is a sample application that demoes the usage of Infinispan listeners.");
		ListenerExample a = new ListenerExample();
		a.registeringListeners();
		System.out.println("Sample complete.");
		System.out.println("\n\n\n   ********************************  \n\n\n");
	}

	@Listener
	public class MyListener {

		@CacheEntryCreated
		@CacheEntryModified
		@CacheEntryRemoved
		public void printDetailsOnChange(CacheEntryEvent e) {
			System.out.printf("Thread %s has modified an entry in the cache named %s under key %s!\n",
					Thread.currentThread().getName(), e.getCache().getName(), e.getKey());
		}

		@CacheEntryVisited
		public void pribtDetailsOnVisit(CacheEntryVisitedEvent e) {
			System.out.printf("Thread %s has visited an entry in the cache named %s under key %s!\n",
					Thread.currentThread().getName(), e.getCache().getName(), e.getKey());
		}
	}

}
