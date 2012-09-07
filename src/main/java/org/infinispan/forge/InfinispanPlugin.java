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
package org.infinispan.forge;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.infinispan.forge.facet.IInfinispanExample;
import org.infinispan.forge.facet.shell.InfinispanConfigurationCompleter;
import org.infinispan.forge.facet.shell.InfinispanExampleCompleter;
import org.infinispan.forge.util.CDIUtils;
import org.infinispan.forge.util.FacetUtils;
import org.jboss.forge.project.Project;
import org.jboss.forge.project.facets.events.InstallFacets;
import org.jboss.forge.shell.PromptType;
import org.jboss.forge.shell.ShellPrompt;
import org.jboss.forge.shell.plugins.Alias;
import org.jboss.forge.shell.plugins.Command;
import org.jboss.forge.shell.plugins.Option;
import org.jboss.forge.shell.plugins.PipeOut;
import org.jboss.forge.shell.plugins.Plugin;
import org.jboss.forge.shell.plugins.RequiresProject;

/**
 * @author Andre Dietisheim
 */
@Alias("infinispan")
@RequiresProject
public class InfinispanPlugin implements Plugin {

	@Inject
	private Project project;
	@Inject
	private ShellPrompt prompt;
	@Inject
	private Event<InstallFacets> installFacetEvent;
	@Inject @Any
	private Instance<IInfinispanExample> examples;

	@Command("create-example")
	public void createExample(
			@Option(name = "targetPackage", type = PromptType.JAVA_PACKAGE) String targetPackage,
			@Option(name = "name", required = true, completer = InfinispanExampleCompleter.class) final String name,
			@Option(name = "configuration", required = true, completer = InfinispanConfigurationCompleter.class) final String configuration,
			@Option(flagOnly = true, name = "overwrite") final boolean overwrite,
			PipeOut out) {

		IInfinispanExample example = CDIUtils.getByAlias(name, examples);
		if (example == null) {
			throw new RuntimeException("Could not find example " + name);
		}

		FacetUtils.installFacet(example.getClass(), project, installFacetEvent, out);
		IInfinispanExample facet = project.getFacet(example.getClass());
		facet.create(targetPackage, configuration);

	}
}
