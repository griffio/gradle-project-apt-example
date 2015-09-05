package griffio.apt;

import griffio.annotation.MyEntity;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

@SupportedAnnotationTypes(value = {"griffio.annotation.MyEntity"})
public class MyProcessor extends AbstractProcessor {

  private Filer filer;
  private Messager messager;

  public MyProcessor() {
  }

  @Override
  public synchronized void init(ProcessingEnvironment env) {
    filer = env.getFiler();
    messager = env.getMessager();
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

    if (roundEnv.processingOver()) {
      return false;
    }

    Collection<? extends Element> annotatedElements =
        roundEnv.getElementsAnnotatedWith(MyEntity.class);

    for (Element element : annotatedElements) {

      String myClassName = generatedClassName((TypeElement) element, "Gen_");

      String myClassContent = generateClassContent((TypeElement) element, "Gen_");

      JavaFileObject file;

      try {
        file = filer.createSourceFile(
            myClassName,
            element);
        file.openWriter()
            .append(myClassContent)
            .close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return true;
  }

  private String generateClassContent(TypeElement type, String prefix) {
    String name = type.getSimpleName().toString();

    Element enclosing = type.getEnclosingElement();

    Name pkg = ((PackageElement) enclosing).getQualifiedName();

    return "package " + pkg + ";\n" + "public class " + prefix + name + "{};";
  }

  private String generatedClassName(TypeElement type, String prefix) {

    String name = type.getSimpleName().toString();

    Element enclosing = type.getEnclosingElement();

    Name pkg = ((PackageElement) enclosing).getQualifiedName();

    String dot = pkg.toString().isEmpty() ? "" : ".";

    return pkg + dot + prefix + name;
  }
}