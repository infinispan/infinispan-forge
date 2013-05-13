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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.infinispan.forge.util.CDIUtils;
import org.jboss.forge.shell.util.ConstraintInspector;

/**
 * @author Andre Dietisheim
 */
public class InfinispanExamples {

	@Inject
	@Any
	private Instance<InfinispanExample> examples;

	public Collection<String> getNames() {
		List<String> result = new ArrayList<String>();
		for (InfinispanExample example : examples) {
			result.add(ConstraintInspector.getName(example.getClass()));
		}
		return result;
	}

	public String[] getNamesArray() {
		Collection<String> names = getNames();
		return names.toArray(new String[names.size()]);
	}

	
	public List<InfinispanExample> getExamples() {
		List<InfinispanExample> result = new ArrayList<InfinispanExample>();
		for (InfinispanExample example : examples) {
			result.add(example);
		}
		return result;
	}

	public InfinispanExample getByAlias(String alias) {
		return CDIUtils.getByAlias(alias, examples);
	}
}
