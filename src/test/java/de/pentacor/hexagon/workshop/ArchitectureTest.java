package de.pentacor.hexagon.workshop;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;


@AnalyzeClasses(packages = "de.pentacor.hexagon.workshop",
        importOptions = ImportOption.DoNotIncludeTests.class // ignore test deps
)
public class ArchitectureTest {

    @ArchTest
    static final ArchRule layer_dependencies_are_respected = layeredArchitecture().consideringAllDependencies()

            .layer("ui").definedBy("..cli..")
            .layer("business").definedBy("..app..")
            .layer("db").definedBy("..db..")
            .layer("payment").definedBy("..payment..")

            .whereLayer("ui").mayNotBeAccessedByAnyLayer()
            .whereLayer("business").mayOnlyBeAccessedByLayers("ui")
            .whereLayer("db").mayOnlyBeAccessedByLayers("business")
            .whereLayer("payment").mayOnlyBeAccessedByLayers("business");

}
