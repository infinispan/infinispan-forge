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
package org.jboss.infinispan.forge;

import static org.junit.Assert.assertTrue;

import org.infinispan.forge.util.ResourceUtils;
import org.jboss.forge.project.facets.ResourceFacet;
import org.jboss.forge.resources.DirectoryResource;
import org.jboss.forge.resources.FileResource;
import org.jboss.forge.resources.ResourceException;
import org.jboss.forge.test.AbstractShellTest;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Andre Dietisheim
 */
public class ResourceUtilsTest extends AbstractShellTest {

	private DirectoryResource sourceFolder;
	private DirectoryResource destinationFolder;

	@Before
	public void setUp() {
		ResourceFacet resourceFacet = getProject().getFacet(ResourceFacet.class);
		this.sourceFolder = resourceFacet.getResourceFolder();
		this.destinationFolder = resourceFacet.getTestResourceFolder();

	}

	@Test
	public void shouldCopyDirectory() {
		// precondition
		DirectoryResource directory1 = sourceFolder.getChildDirectory("1");
		directory1.mkdir();

		// operation
		ResourceUtils.copy(directory1, destinationFolder);

		// verification
		FileResource<?> testResource1 = (FileResource<?>) destinationFolder.getChild("1");
		assertTrue(testResource1.exists());
		assertTrue(testResource1.isDirectory());
	}

	@Test
	public void shouldCopyFile() {
		// precondition
		FileResource<?> child1 = (FileResource<?>) sourceFolder.getChild("2");
		child1.createNewFile();

		// operation
		ResourceUtils.copy(child1, destinationFolder);

		// verification
		FileResource<?> testResource1 = (FileResource<?>) destinationFolder.getChild("2");
		assertTrue(testResource1.exists());
		assertTrue(!testResource1.isDirectory());
	}

	@Test
	public void shouldCopyNestedFile() {
		// precondition
		((FileResource<?>) sourceFolder.getChildDirectory("3").getChild("3")).createNewFile();

		// operation
		ResourceUtils.copy(sourceFolder.getChildDirectory("3"), destinationFolder);

		// verification
		DirectoryResource directory3 = destinationFolder.getChildDirectory("3");
		assertTrue(directory3.exists());
		assertTrue(directory3.isDirectory());
		FileResource<?> file3 = (FileResource<?>) directory3.getChild("3");
		assertTrue(file3.exists());
		assertTrue(!file3.isDirectory());
	}

	@Test(expected=ResourceException.class)
	public void shouldFailBecauseOfFileWithSameName() {
		// precondition
		((FileResource<?>) sourceFolder.getChildDirectory("4").getChild("4")).createNewFile();
		((FileResource<?>) destinationFolder.getChild("4")).createNewFile();
		
		
		// operation
		ResourceUtils.copy(sourceFolder.getChildDirectory("4"), destinationFolder);
	}

}
