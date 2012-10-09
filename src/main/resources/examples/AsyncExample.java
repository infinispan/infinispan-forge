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
import org.infinispan.util.concurrent.NotifyingFuture;

import java.util.Arrays;

/**
 * Sample application code. For more examples visit
 * https://docs.jboss.org/author/display/ISPN/Getting+Started+Guide#GettingStartedGuide-5minutetutorial
 */
public class AsyncExample {

	/**
	* TIP: For more examples on using the asynchronous API, visit
	* http://community.jboss.org/wiki/AsynchronousAPI
	*/
	public void asyncOperations() {
		System.out
				.println("\n\n3.  Demonstrating asynchronous operations - where writes can be done in a non-blocking fashion.");
		Cache<String, Integer> wineCache = SampleCacheContainer.getCache("wine cache");

		System.out.println("  Put #1");
		NotifyingFuture<Integer> f1 = wineCache.putAsync("Pinot Noir", 300);
		System.out.println("  Put #1");
		NotifyingFuture<Integer> f2 = wineCache.putAsync("Merlot", 120);
		System.out.println("  Put #1");
		NotifyingFuture<Integer> f3 = wineCache.putAsync("Chardonnay", 180);

		// now poll the futures to make sure any remote calls have completed!
		for (NotifyingFuture<Integer> f : Arrays.asList(f1, f2, f3)) {
			try {
				System.out.println("  Checking future... ");
				f.get();
			} catch (Exception e) {
				throw new RuntimeException("Operation failed!", e);
			}
		}
		System.out.println("  Everything stored!");
	}

	public static void main(String[] args) throws Exception {
		System.out.println("\n\n\n   ********************************  \n\n\n");
		System.out.println(
				"Hello. This is a sample application that demoes the usage of Infinispan async operations.");
		AsyncExample a = new AsyncExample();
		a.asyncOperations();
		System.out.println("Sample complete.");
		System.out.println("\n\n\n   ********************************  \n\n\n");
	}
}
