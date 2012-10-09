package org.jboss.infinispan.forge;

import org.infinispan.forge.InfinispanPlugin;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.forge.maven.facets.MavenDependencyFacet;
import org.jboss.forge.project.dependencies.Dependency;
import org.jboss.forge.project.dependencies.DependencyBuilder;
import org.jboss.forge.test.AbstractShellTest;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;

public class InfinispanPluginTest extends AbstractShellTest
{
   @Deployment
   public static JavaArchive getDeployment()
   {
      return AbstractShellTest.getDeployment().addPackages(true, InfinispanPlugin.class.getPackage());
   }

   @Test
   public void testSetupCommand() throws Exception
   {
      queueInputLines("");
      getShell().execute("infinispan setup");
      Dependency dependency = DependencyBuilder
               .create("org.infinispan:infinispan-core");
      MavenDependencyFacet facet = getProject().getFacet(MavenDependencyFacet.class);
      Assert.assertTrue(facet.hasEffectiveDependency(dependency));

   }
}
