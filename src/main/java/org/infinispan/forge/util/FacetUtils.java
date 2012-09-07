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

import java.util.List;

import javax.enterprise.event.Event;

import org.jboss.forge.project.Facet;
import org.jboss.forge.project.Project;
import org.jboss.forge.project.dependencies.Dependency;
import org.jboss.forge.project.dependencies.DependencyInstaller;
import org.jboss.forge.project.dependencies.ScopeType;
import org.jboss.forge.project.facets.DependencyFacet;
import org.jboss.forge.project.facets.events.InstallFacets;
import org.jboss.forge.shell.ShellMessages;
import org.jboss.forge.shell.plugins.PipeOut;
import org.jboss.forge.shell.util.ConstraintInspector;

/**
 * @author Andre Dietisheim
 */
public class FacetUtils {

	public static <T extends Facet> void installFacet(Class<T> facetClass, Project project,
			Event<InstallFacets> installFacetEvent, PipeOut out) {
		if (!project.hasFacet(facetClass)) {
			installFacetEvent.fire(new InstallFacets(facetClass));
			if (project.hasFacet(facetClass)) {
				ShellMessages.success(out, facetClass.getSimpleName() + " is configured.");
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends Facet> T getFacet(Class<T> clazz, IFacetMatcher matcher, Project project) {
		T matchingFacet = null;

		for (Facet availableFacet : project.getFacets()) {
			if (clazz.isAssignableFrom(availableFacet.getClass())) {
				if (matcher.matches(availableFacet)) {
					matchingFacet = (T) availableFacet;
					break;
				}
			}
		}
		return matchingFacet;

	}

	public static interface IFacetMatcher {
		public boolean matches(Facet facet);
	}

	public static class FacetAliasMatcher implements IFacetMatcher {

		private String alias;

		public FacetAliasMatcher(String alias) {
			this.alias = alias;
		}

		@Override
		public boolean matches(Facet facet) {
			return ConstraintInspector.getName(facet.getClass()).equals(alias);
		}
	}

	/**
	 * Returns <code>true</code> if all the given dependencies are installed to the given project.
	 * 
	 * @param dependencies the dependencies to check for
	 * @param project the project whose dependencies shall be checked
	 * @return true if all dependencies are installed
	 */
	public static boolean areInstalled(List<Dependency> dependencies, Project project) {
		DependencyFacet dependencyFacet = project.getFacet(DependencyFacet.class);
		for (Dependency requirement : dependencies) {
			if (!dependencyFacet.hasEffectiveDependency(requirement))
			{
				return false;
			}
		}
		return true;

	}

	/**
	 * Installs the given dependencies to the given project.
	 * 
	 * @param dependencies the dependencies that shall be installed
	 * @param project the project to which the dependencies shall get installed to
	 * @param installer the installer used to install the dependencies
	 */
	public static void installFacets(List<Dependency> dependencies, Project project, DependencyInstaller installer) {
		for (Dependency requirement : dependencies) {
			if (!installer.isInstalled(project, requirement)) {
				installer.install(project, requirement, ScopeType.PROVIDED);
			}
		}
	}
}
