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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.project.facets.JavaSourceFacet;
import org.jboss.forge.resources.DirectoryResource;
import org.jboss.forge.resources.FileResource;
import org.jboss.forge.resources.ResourceException;
import org.jboss.forge.shell.PromptType;
import org.jboss.forge.shell.util.Packages;

/**
 * @author Andre Dietisheim
 */
public class JavaSourceFacetUtils {

	/**
	 * Returns a java class with the given target package and the given content.
	 * 
	 * @param sourceURL the url at which the content shall get fetched
	 * @param targetPackage the target package for the java class
	 * @param javaSourceFacet
	 * @return
	 * @throws IOException
	 */
	public static JavaClass getJavaClass(InputStream in, String targetPackage, JavaSourceFacet javaSourceFacet)
			throws IOException {
		if (in == null) {
			throw new ResourceException("Could not find resource.");
		}
		JavaClass javaClass = JavaParser.parse(JavaClass.class, in);
		javaClass.setPackage(targetPackage);
		return javaClass;
	}

	/**
	 * Returns (and creates it if does not exist) the file resource for a given java package. 
	 * Returns <code>null</code> if the resource already exists and is a file.
	 * 
	 * @param javaPackage the java package we want to file resource for
	 * @param javaSourceFacet
	 * @param shell
	 * @return
	 * @throws FileNotFoundException
	 */
	public static FileResource<?> getOrCreateFileResource(String javaPackage, JavaSourceFacet javaSourceFacet)
			throws FileNotFoundException {
		FileResource<?> destination = getFileResource(javaPackage, javaSourceFacet);
		if (destination.exists()) {
			if (!destination.isDirectory()) {
				return null;
			}
			return destination;
		} else {
			destination.mkdirs();
		}
		return destination;
	}

	/**
	 * Returns the file resource for a given java package. Will not check if it exists nor if it's a file/directory.
	 *  
	 * @param javaPackage
	 * @param javaSourceFacet
	 * @return
	 */
	public static FileResource<?> getFileResource(String javaPackage, JavaSourceFacet javaSourceFacet) {
		FileResource<?> destination = (FileResource<?>)
				javaSourceFacet.getSourceFolder().getChild(Packages.toFileSyntax(javaPackage));
		return destination;
	}

	/**
	 * Returns the base package for the given java source facet. Will return the source folder if the default base package is invalid.
	 * @param javaSourceFacet
	 * @return
	 */
	public static DirectoryResource getBasePackageResource(JavaSourceFacet javaSourceFacet) {
		DirectoryResource basePackage = javaSourceFacet.getBasePackageResource();
		if (PromptType.JAVA_PACKAGE.matches(basePackage.toString())) {
			return basePackage;
		}
		return javaSourceFacet.getSourceFolder();
	}

	/**
	 * Returns the string representation for a given package resource. Returns <tt>"<default package>"</tt> for the default package, the full package name otherwise.
	 * @param packageResource
	 * @param javaSourceFacet
	 * @return
	 */
	public static String getPackageName(DirectoryResource packageResource, JavaSourceFacet javaSourceFacet) {
		if (isSourceFolder(packageResource, javaSourceFacet)) {
			// default package
			return "";
		} else {
			return packageResource.toString();
		}
	}

	public static boolean isSourceFolder(DirectoryResource directoryResource, JavaSourceFacet javaSourceFacet) {
		return javaSourceFacet.getSourceFolder().equals(directoryResource);
	}

}
