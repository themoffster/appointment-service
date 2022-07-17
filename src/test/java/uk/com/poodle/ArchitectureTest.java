package uk.com.poodle;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.library.Architectures;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JODATIME;

@SuppressWarnings("squid:S2187")
@AnalyzeClasses(packages = "uk.com.poodle")
class ArchitectureTest {

    private static final Architectures.LayeredArchitecture LAYERED_ARCHITECTURE_SHOULD_BE_RESPECTED =
        layeredArchitecture()
            .layer("Web")
            .definedBy("uk.com.poodle.rest..")
            .layer("Service")
            .definedBy("uk.com.poodle.service..")
            .layer("Repository")
            .definedBy("uk.com.poodle.data..")
            .whereLayer("Web")
            .mayNotBeAccessedByAnyLayer()
            .whereLayer("Service")
            .mayOnlyBeAccessedByLayers("Web")
            .whereLayer("Repository")
            .mayOnlyBeAccessedByLayers("Service");

    @ArchTest
    void layeredArchitectureShouldBeRespected(JavaClasses classes) {
        LAYERED_ARCHITECTURE_SHOULD_BE_RESPECTED.check(classes);
    }

    @ArchTest
    void noClassesShouldAccessStandardStreams(JavaClasses classes) {
        NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS.check(classes);
    }

    @ArchTest
    void noClassesShouldThrowGenericExceptions(JavaClasses classes) {
        NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS.check(classes);
    }

    @ArchTest
    void noClassesShouldUseJavaUtilLogging(JavaClasses classes) {
        NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING.check(classes);
    }

    @ArchTest
    void noClassesShouldUseJodaTime(JavaClasses classes) {
        NO_CLASSES_SHOULD_USE_JODATIME.check(classes);
    }
}
