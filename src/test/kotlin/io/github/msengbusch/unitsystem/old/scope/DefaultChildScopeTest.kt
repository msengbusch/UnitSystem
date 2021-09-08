package io.github.msengbusch.unitsystem.old.scope

import io.github.msengbusch.unitsystem.exception.InjectionException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test

class DefaultChildScopeTest {

    @Test
    fun testParentInjection() {
        val parentInjection = DefaultScope()
        val injection = DefaultChildScope(parentInjection)
        parentInjection.bindSingleton(OneArgumentTest::class.java, OneArgumentTest("test"))

        val instance = injection.getInstance(OneArgumentTest::class.java)

        assertThat(instance.string).isEqualTo("test")
    }

    @Test
    fun testNoParentInjectionIfExcluded() {
        val parentScope = DefaultScope()
        val scope = DefaultChildScope(parentScope)
        parentScope.bindSingleton(NoInjectAnnotation::class.java, NoInjectAnnotation("test"))
        scope.excludeParentInjection(NoInjectAnnotation::class.java)

        assertThatExceptionOfType(InjectionException::class.java)
            .isThrownBy { scope.getInstance(NoInjectAnnotation::class.java) }
            .withNoCause()
            .withMessage("To allow dependency injection of class io.github.msengbusch.unitsystem.di.NoInjectAnnotation annotate one public constructor with @Inject")
    }
}

class NoInjectAnnotation(string: String)