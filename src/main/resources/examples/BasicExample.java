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

/**
 * Sample application code. For more examples visit
 * https://docs.jboss.org/author/display/ISPN/Getting+Started+Guide#GettingStartedGuide-5minutetutorial
 */
public class BasicExample {

	public void basicUse() {
		System.out.println("\n\n1.  Demonstrating basic usage of Infinispan.  This cache stores arbitrary Strings.");
		Cache<String, String> cache = SampleCacheContainer.getCache();

		System.out.println("  Storing value 'World' under key 'Hello'");
		String oldValue = cache.put("Hello", "World");
		System.out.printf("  Done.  Saw old value as '%s'\n", oldValue);

		System.out.println("  Replacing 'World' with 'Mars'.");
		boolean worked = cache.replace("Hello", "World", "Mars");
		System.out.printf("  Successful? %s\n", worked);

		assert oldValue == null;
		assert worked == true;
	}

	public static void main(String[] args) throws Exception {
		System.out.println("\n\n\n   ********************************  \n\n\n");
		System.out.println(
				"Hello. This is a sample application that demoes basic usage of Infinispan.");
		BasicExample a = new BasicExample();
		a.basicUse();
		System.out.println("Sample complete.");
		System.out.println("\n\n\n   ********************************  \n\n\n");
	}
}
