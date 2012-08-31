package org.jboss.infinispan.forge;

import javax.inject.Inject;

import org.jboss.forge.maven.facets.MavenDependencyFacet;
import org.jboss.forge.project.Project;
import org.jboss.forge.project.dependencies.DependencyBuilder;
import org.jboss.forge.project.dependencies.DependencyInstaller;
import org.jboss.forge.shell.ShellPrompt;
import org.jboss.forge.shell.plugins.Alias;
import org.jboss.forge.shell.plugins.Plugin;
import org.jboss.forge.shell.plugins.RequiresFacet;
import org.jboss.forge.shell.plugins.RequiresProject;
import org.jboss.forge.shell.plugins.SetupCommand;

/**
 *
 */
@Alias("infinispan")
@RequiresProject
@RequiresFacet(MavenDependencyFacet.class)
public class InfinispanPlugin implements Plugin
{
   @Inject
   private ShellPrompt prompt;

   @Inject
   private Project project;

   @Inject
   private DependencyInstaller dependencyInstaller;

   @SetupCommand
   public void setup()
   {
      DependencyBuilder dependency = DependencyBuilder
               .create("org.infinispan:infinispan-core");
      dependencyInstaller.install(project, dependency);
   }

}
