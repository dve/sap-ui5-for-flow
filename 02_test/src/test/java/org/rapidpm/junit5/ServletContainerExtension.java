package org.rapidpm.junit5;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.util.AnnotationUtils;
import org.rapidpm.vaadin.nano.CoreUIServiceJava;

import java.util.Optional;


public class ServletContainerExtension
    implements BeforeEachCallback, AfterEachCallback {

  @Override
  public void beforeEach(ExtensionContext ctx) throws Exception {

    final Optional<VaadinTutorial> vaadinTutorial = AnnotationUtils.findAnnotation(ctx.getTestClass(),
                                                                                   VaadinTutorial.class);
    final String   packageToDeploy = vaadinTutorial.get()
                                                   .packageToDeploy();
    final String[] args            = new String[2];
    args[0] = "-pkg";
    args[1] = packageToDeploy;
    final CoreUIServiceJava coreUIServiceJava = new CoreUIServiceJava().executeCLI(args);
    coreUIServiceJava.startup();
    ctx.getStore(ExtensionContext.Namespace.GLOBAL)
       .put(CoreUIServiceJava.class.getSimpleName(), coreUIServiceJava);
  }


  @Override
  public void afterEach(ExtensionContext ctx) throws Exception {
    ctx.getStore(ExtensionContext.Namespace.GLOBAL)
       .get(CoreUIServiceJava.class.getSimpleName(), CoreUIServiceJava.class)
       .shutdown();
  }
}
