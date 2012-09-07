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
package org.infinispan.forge.facet;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.infinispan.forge.util.FacetUtils;
import org.infinispan.forge.util.JavaSourceFacetUtils;
import org.infinispan.forge.util.ResourceUtils;
import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.project.dependencies.Dependency;
import org.jboss.forge.project.dependencies.DependencyBuilder;
import org.jboss.forge.project.dependencies.DependencyInstaller;
import org.jboss.forge.project.facets.BaseFacet;
import org.jboss.forge.project.facets.JavaSourceFacet;
import org.jboss.forge.project.facets.ResourceFacet;
import org.jboss.forge.resources.DirectoryResource;
import org.jboss.forge.resources.FileResource;
import org.jboss.forge.resources.java.JavaResource;
import org.jboss.forge.shell.PromptType;
import org.jboss.forge.shell.Shell;
import org.jboss.forge.shell.ShellColor;
import org.jboss.forge.shell.ShellPrompt;
import org.jboss.forge.shell.plugins.RequiresFacet;
import org.jboss.forge.shell.util.ConstraintInspector;

/**
 * @author Andre Dietisheim
 */
@RequiresFacet({ JavaSourceFacet.class, ResourceFacet.class })
public abstract class AbstractInfinispanExample extends BaseFacet implements IInfinispanExample {

	private static final String DEFAULT_BASEPACKAGE = "infinispan";
	// TODO: browse maven dependenencies and suggest available infinispan
	// versions
	private static final String INFINISPAN_VERSION = "5.1.6.FINAL";
	protected static final String SAMPLE_CACHE_CONTAINER = "/examples/common/SampleCacheContainer.java";
	protected static final String TARGET_CONFIGURATION_NAME = "infinispan.xml";

	private Shell shell;
	private ShellPrompt prompt;
	private DependencyInstaller installer;

	@Inject
	public AbstractInfinispanExample(DependencyInstaller installer, Shell shell, ShellPrompt prompt) {
		this.installer = installer;
		this.shell = shell;
		this.prompt = prompt;
	}

	@Override
	public void create(String targetPackage, String configurationName) {
		try {
			createJavaSource(getJavaSourceResources(), targetPackage);
			createConfiguration(InfinispanConfiguration.getByConfigurationName(configurationName));
		} catch (IOException e) {
			throw new RuntimeException("Could not create example: " + ConstraintInspector.getName(getClass()), e);
		}
	}

	private void createJavaSource(URL[] urls, String targetPackage) throws IOException {
		JavaSourceFacet javaSourceFacet = project.getFacet(JavaSourceFacet.class);

		FileResource<?> destination = null;
		while (destination == null) {
			targetPackage = getTargetPackage(destination, javaSourceFacet);
			destination = JavaSourceFacetUtils.getOrCreateFileResource(targetPackage, javaSourceFacet);
			if (destination == null) {
				shell.println(ShellColor.RED,
						JavaSourceFacetUtils.getFileResource(targetPackage, javaSourceFacet)
								.getFullyQualifiedName() + " already existist and is a file...");
			}
		}

		for (URL sourceURL : urls) {
			JavaClass javaClass = JavaSourceFacetUtils.getJavaClass(sourceURL.openStream(), targetPackage,
					javaSourceFacet);
			JavaResource targetResource = javaSourceFacet.getJavaResource(javaClass.getQualifiedName());
			ResourceUtils.createOrOverwrite(prompt, targetResource, javaClass.toString(), false);
		}
	}

	private String getTargetPackage(FileResource<?> destination, JavaSourceFacet javaSourceFacet) {
		String targetPackage = null;
		if (destination == null) {
			DirectoryResource basePackageResource = JavaSourceFacetUtils.getBasePackageResource(javaSourceFacet);
			if (JavaSourceFacetUtils.isSourceFolder(basePackageResource, javaSourceFacet)) {
				basePackageResource = basePackageResource.getChildDirectory(DEFAULT_BASEPACKAGE);
			}
			targetPackage = prompt.promptCommon(
					"Please provide the target package: ", PromptType.JAVA_PACKAGE, basePackageResource.toString());
		} else {
			targetPackage = ResourceUtils.getRelativePath(destination, javaSourceFacet.getBasePackageResource());
		}
		return targetPackage;
	}

	protected abstract URL[] getJavaSourceResources();

	private void createConfiguration(InfinispanConfiguration configuration) {
		ResourceFacet resource = project.getFacet(ResourceFacet.class);
		FileResource<?> configurationResource =
				(FileResource<?>) resource.getResourceFolder().getChild(TARGET_CONFIGURATION_NAME);

		ResourceUtils.createOrOverwrite(
				prompt, configurationResource, configuration.getResourceAsStream(), false);
	}

	@Override
	public boolean install() {
		FacetUtils.installFacets(getRequiredDependencies(), project, installer);
		return true;
	}

	@Override
	public boolean isInstalled() {
		return FacetUtils.areInstalled(getRequiredDependencies(), project);
	}

	protected List<Dependency> getRequiredDependencies() {
		return Arrays.asList(new Dependency[] {
				DependencyBuilder.create()
						.setGroupId("org.infinispan")
						.setArtifactId("infinispan-core")
						.setVersion(INFINISPAN_VERSION) });
	}

	protected InputStream getResourceAsStream(String name) {
		return getClass().getResourceAsStream(name);
	}

	protected URL getResource(String name) {
		return getClass().getResource(name);
	}

}
