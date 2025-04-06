package de.pentacor.hexagon.workshop;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "de.pentacor.hexagon.workshop",
        importOptions = ImportOption.DoNotIncludeTests.class // ignore test deps
)
public class ArchitectureTest {

    @ArchTest
    static final ArchRule app_should_only_depend_on_internals =
            classes().that().resideInAPackage("..app..")
                    .should().onlyDependOnClassesThat().resideInAnyPackage("..app..", "java..", "lombok..");

    @ArchTest
    static final ArchRule model_should_only_depend_on_internals =
            classes().that().resideInAPackage("..model..")
                    .should().onlyDependOnClassesThat().resideInAnyPackage("..model..", "java..", "lombok..");

    @ArchTest
    static final ArchRule usecases_should_only_depend_on_internals =
            classes().that().resideInAPackage("..usecases..")
                    .should().onlyDependOnClassesThat().resideInAnyPackage("..ports..", "..model..", "java..", "lombok..");
    @ArchTest
    static final ArchRule ports_should_only_depend_on_internals =
            classes().that().resideInAPackage("..ports..")
                    .should().onlyDependOnClassesThat().resideInAnyPackage("..ports..", "..model..", "java..", "lombok..");

}
