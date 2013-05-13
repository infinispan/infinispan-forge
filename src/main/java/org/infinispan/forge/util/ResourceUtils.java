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
package org.infinispan.forge.util;

import java.io.File;
import java.io.InputStream;

import org.jboss.forge.resources.FileResource;
import org.jboss.forge.resources.Resource;
import org.jboss.forge.resources.ResourceException;
import org.jboss.forge.shell.ShellPrompt;
import org.jboss.forge.shell.util.Streams;

/**
 * @author Andre Dietisheim
 */
public class ResourceUtils {

	public static String getRelativePath(Resource<?> destination, Resource<?> base) {
		int baseIndex = destination.getFullyQualifiedName().indexOf(base.getFullyQualifiedName());
		if (baseIndex == -1) {
			throw new RuntimeException(
					"Destination " + destination.getFullyQualifiedName() + " is not within folder "
							+ base.getFullyQualifiedName());
		}
		return destination.getFullyQualifiedName().substring(baseIndex);

	}

	public static Resource<?> createOrOverwrite(final ShellPrompt prompt, final FileResource<?> resource,
			final String content, final boolean overwrite) {
		return createOrOverwrite(prompt, resource, Streams.fromString(content), overwrite);
	}

	public static Resource<?> createOrOverwrite(final ShellPrompt prompt, final FileResource<?> resource,
			final InputStream content, final boolean overwrite) {
		if (!resource.exists() || overwrite
				|| prompt.promptBoolean("[" + resource.getFullyQualifiedName() + "] File exists, overwrite?")) {
			resource.createNewFile();
			resource.setContents(content);
			return resource;
		}
		return null;
	}

	public static String getFileName(String path) {
		return getFileName(path, true);
	}

	public static String getFileName(String path, boolean witjSuffix) {
		String[] segments = path.split("\\" + File.separatorChar);
		String resourceName = segments[segments.length - 1];
		int dotIndex = resourceName.indexOf('.');
		if (witjSuffix) {
			return resourceName;
		}
		if (dotIndex > -1) {
			return resourceName.substring(0, dotIndex);
		} else {
			return resourceName;
		}
	}

	public static void copy(FileResource<?> source, FileResource<?> destination) {
		copy(source, destination, null);
	}

	/**
	 * Copies the given source file or folder to the given destination folder. 
	 * 
	 * @param source the source (file or folder) to copy
	 * @param destination the destination folder to copy to
	 * @param visitor the visitor that can visit every file that gets copied
	 */
	public static void copy(FileResource<?> source, FileResource<?> destination, IFileResourceVisitor visitor) {
		if (!source.exists()) {
			return;
		}
		if (source.isDirectory()) {
			copyDirectory(source, destination, visitor);
		} else {
			ensureDirectoryExists(destination);
			FileResource<?> child = (FileResource<?>) destination.getChild(source.getName());
			child.setContents(source.getResourceInputStream());
			if (visitor != null) {
				visitor.visit(child);
			}
		}
	}

	private static void copyDirectory(FileResource<?> source, FileResource<?> destination,
			IFileResourceVisitor visitor) {
		FileResource<?> child = (FileResource<?>) destination.getChild(source.getName());
		ensureDirectoryExists(child);

		for (Resource<?> file : source.listResources()) {
			copy((FileResource<?>) file, child, visitor);
		}
	}

	private static void ensureDirectoryExists(FileResource<?> resource) {
		if (resource.exists()) {
			if (resource.isDirectory()) {
				return;
			}

			throw new ResourceException(
					"Could not create directory " + resource.getFullyQualifiedName()
							+ ". File with the same name already exists.");
		}

		resource.mkdirs();
	}

	public interface IFileResourceVisitor {
		public void visit(FileResource<?> file);
	}
}
