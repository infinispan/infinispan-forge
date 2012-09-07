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

import java.net.URL;

import javax.inject.Inject;

import org.jboss.forge.env.Configuration;
import org.jboss.forge.project.dependencies.DependencyInstaller;
import org.jboss.forge.shell.Shell;
import org.jboss.forge.shell.ShellPrompt;
import org.jboss.forge.shell.plugins.Alias;
import org.jboss.forge.shell.plugins.RequiresProject;

/**
 * @author Andre Dietisheim
 */
@Alias("lifespan")
@RequiresProject
public class LifespanExample extends AbstractInfinispanExample {

	private static final String LIFESPAN_EXAMPLE = "/examples/LifespanExample.java";

	@Inject
	public LifespanExample(final DependencyInstaller installer,
			final Configuration config, final Shell shell,
			final ShellPrompt prompt) {
		super(installer, shell, prompt);
	}

	@Override
	protected URL[] getJavaSourceResources() {
		return new URL[] { getResource(LIFESPAN_EXAMPLE),
				getResource(SAMPLE_CACHE_CONTAINER) };
	}
}
