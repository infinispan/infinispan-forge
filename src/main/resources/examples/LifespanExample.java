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
import java.util.concurrent.TimeUnit;

/**
 * Sample application code. For more examples visit
 * https://docs.jboss.org/author/display/ISPN/Getting+Started+Guide#GettingStartedGuide-5minutetutorial
 */
public class LifespanExample {

	private static final int ENTRY_LIFESPAN_SECONDS = 10;

	public void lifespans() throws InterruptedException {
		Cache<String, Float> stocksCache = SampleCacheContainer.getCache("stock tickers");
		System.out.println("  Storing key 'RHT' for " + ENTRY_LIFESPAN_SECONDS + " seconds.");
		stocksCache.put("RHT", 45.0f, ENTRY_LIFESPAN_SECONDS, TimeUnit.SECONDS);
		System.out.printf("  Checking for existence of key.  Is it there? %s\n", stocksCache.containsKey("RHT"));
		System.out.println("  Sleeping for 10 seconds...");
		Thread.sleep((ENTRY_LIFESPAN_SECONDS + 1) * 1000 * 60);
		System.out.printf("  Checking for existence of key.  Is it there? %s\n", stocksCache.containsKey("RHT"));
		assert stocksCache.get("RHT") == null;
	}

	public static void main(String[] args) throws Exception {
		System.out.println("\n\n\n   ********************************  \n\n\n");
		System.out.println(
				"Hello. This is a sample application that demoes the usage of Infinispan with expirable entries.");
		LifespanExample a = new LifespanExample();
		a.lifespans();
		System.out.println("Sample complete.");
		System.out.println("\n\n\n   ********************************  \n\n\n");
	}
}
