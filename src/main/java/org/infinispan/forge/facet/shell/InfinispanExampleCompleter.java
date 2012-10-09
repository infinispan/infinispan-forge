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
package org.infinispan.forge.facet.shell;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.infinispan.forge.facet.IInfinispanExample;
import org.jboss.forge.shell.completer.SimpleTokenCompleter;
import org.jboss.forge.shell.util.ConstraintInspector;

/**
 * @author Andre Dietisheim
 * 
 */
public class InfinispanExampleCompleter extends SimpleTokenCompleter {
   @Inject
   @Any
   private Instance<IInfinispanExample> impls;

   @Override
   public List<Object> getCompletionTokens()    {
      List<Object> result = new ArrayList<Object>();
      for (IInfinispanExample impl : impls) {
         result.add(ConstraintInspector.getName(impl.getClass()));
      }
      return result;
   }

}
