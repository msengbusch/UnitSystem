package io.github.msengbusch.unitsystem.di

import io.github.msengbusch.unitsystem.exception.InjectionException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

class InjectionScopeTest {

    @Test
    fun testSingletonInjection() {
        val injection = DefaultInjectionScope()
        injection.bindSingleton(String::class.java, "test")

        val instance = injection.getInstance(OneArgumentTest::class.java)

        assertThat(instance.string).isEqualTo("test")
    }

    @Test
    fun testProviderInjection() {
        val injection = DefaultInjectionScope()

        var current = 0
        injection.bindProvider(String::class.java) {
            current++
            "$current"
        }

        val instance = injection.getInstance(OneArgumentTest::class.java)
        val instance2 = injection.getInstance(OneArgumentTest::class.java)

        assertThat(instance.string).isEqualTo("1")
        assertThat(instance2.string).isEqualTo("2")
    }

    @Test
    fun testInterfaceMappingSingletonInjection() {
        val injection = DefaultInjectionScope()
        injection.bindSingleton(String::class.java, "test")
        injection.bindInterface(TestInterface::class.java, OneArgumentTest::class.java)

        val instance = injection.getInstance(TestInterface::class.java)

        assertThat(instance.string).isEqualTo("test")
        assertThat(instance).isInstanceOf(OneArgumentTest::class.java)
    }

    @Test
    fun testInterfaceMappingInjectionFailNoInterfaceBound() {
        val injection = DefaultInjectionScope()

        assertThatExceptionOfType(InjectionException::class.java)
            .isThrownBy { injection.getInstance(TestInterface::class.java) }
            .withNoCause()
            .withMessage("For the interface or abstract class io.github.msengbusch.unitsystem.di.TestInterface is no mapping registered. Please register one or make the class instantiable")
    }

    @Test
    fun testInterfaceProviderInjection() {
        val injection = DefaultInjectionScope()
        injection.bindProvider(TestInterface::class.java) { OneArgumentTest("test") }

        val instance = injection.getInstance(TestInterface::class.java)

        assertThat(instance.string).isEqualTo("test")
    }

    @Test
    fun testProvidedSingletonInjection() {
        val injection = DefaultInjectionScope()

        var count = 0
        injection.bindProvider(SingletonTest::class.java, Provider {
            count++
            SingletonTest(count.toString())
        })

        val instance = injection.getInstance(SingletonTest::class.java)
        val instance2 = injection.getInstance(SingletonTest::class.java)

        assertThat(instance.string).isEqualTo("1")
        assertThat(instance2.string).isEqualTo("1")
    }

    @Test
    fun testProviderArgumentInjection() {
        val injection = DefaultInjectionScope()
        injection.bindSingleton(String::class.java, "test")

        val instance = injection.getInstance(ProviderTest::class.java)

        assertThat(instance.provider.get()).isEqualTo("test")
    }

    @Test
    fun testInjectionFailIfNoPublicConstructor() {
        val injection = DefaultInjectionScope()

        assertThatExceptionOfType(InjectionException::class.java)
            .isThrownBy { injection.getInstance(NoPublicConstructorTest::class.java) }
            .withNoCause()
            .withMessage("Class io.github.msengbusch.unitsystem.di.NoPublicConstructorTest has no public constructor for dependency injection")
    }

    @Test
    fun testInjectionFailIfNoAnnotatedConstructor() {
        val injection = DefaultInjectionScope()

        assertThatExceptionOfType(InjectionException::class.java)
            .isThrownBy { injection.getInstance(SingletonTest::class.java) }
            .withNoCause()
            .withMessage("To allow dependency injection of class io.github.msengbusch.unitsystem.di.SingletonTest annotate one public constructor with @Inject")
    }

    @Test
    fun testInjectionFailIfMoreThanOneConstructor() {
        val injection = DefaultInjectionScope()

        assertThatExceptionOfType(InjectionException::class.java)
            .isThrownBy { injection.getInstance(MoreThanOneConstructorTest::class.java) }
            .withNoCause()
            .withMessage("There is more than one public constructor marked with @Inject in class io.github.msengbusch.unitsystem.di.MoreThanOneConstructorTest. Only one is allowed")
    }

    @Test
    fun testInjectionCircularDependencyFail() {
        val injection = DefaultInjectionScope()

        assertThatExceptionOfType(InjectionException::class.java)
            .isThrownBy { injection.getInstance(CircularDependencyTestA::class.java) }
            .withNoCause()
            .withMessage("Circular dependency detected: io.github.msengbusch.unitsystem.di.CircularDependencyTestA")
    }

    @Test
    fun testParentInjection() {
        val parentInjection = DefaultInjectionScope()
        val injection = DefaultInjectionScope(parentInjection)
        parentInjection.bindSingleton(OneArgumentTest::class.java, OneArgumentTest("test"))

        val instance = injection.getInstance(OneArgumentTest::class.java)

        assertThat(instance.string).isEqualTo("test")
    }

    @Test
    fun testBindSingletonFailIfProviderAlreadyBound() {
        val injection = DefaultInjectionScope()

        injection.bindProvider(String::class.java) { "provider" }

        assertThatExceptionOfType(InjectionException::class.java)
            .isThrownBy { injection.bindSingleton(String::class.java, "singleton") }
            .withNoCause()
            .withMessage("Class java.lang.String is already bound with a provider")
    }

    @Test
    fun testBindSingletonFailIfIsAbstractClass() {
        val injection = DefaultInjectionScope()

        assertThatExceptionOfType(InjectionException::class.java)
            .isThrownBy { injection.bindSingleton(TestInterface::class.java, OneArgumentTest("test")) }
            .withNoCause()
            .withMessage("Class io.github.msengbusch.unitsystem.di.TestInterface can not be bound as singleton because it is an interface or abstract class")
    }

    @Test
    fun testBindProviderFailIfSingletonAlreadyBound() {
        val injection = DefaultInjectionScope()

        injection.bindSingleton(String::class.java, "singleton")

        assertThatExceptionOfType(InjectionException::class.java)
            .isThrownBy { injection.bindProvider(String::class.java) { "provider" } }
            .withNoCause()
            .withMessage("Class java.lang.String is already bound as singleton")
    }

    @Test
    fun testBindProviderFailIfInterfaceAlreadyBound() {
        val injection = DefaultInjectionScope()

        injection.bindInterface(TestInterface::class.java, OneArgumentTest::class.java)

        assertThatExceptionOfType(InjectionException::class.java)
            .isThrownBy { injection.bindProvider(TestInterface::class.java) { OneArgumentTest("test") } }
            .withNoCause()
            .withMessage("Class io.github.msengbusch.unitsystem.di.TestInterface is already bound as interface")
    }

    @Test
    fun testBindInterfaceFailIfNoInterfaceType() {
        val injection = DefaultInjectionScope()

        assertThatExceptionOfType(InjectionException::class.java)
            .isThrownBy { injection.bindInterface(OneArgumentTest::class.java, OneArgumentTest::class.java) }
            .withNoCause()
            .withMessage("Class io.github.msengbusch.unitsystem.di.OneArgumentTest is not an interface or abstract class. Expecting the first argument to be one")
    }

    @Test
    fun testBindInterfaceFailIfInterfaceType() {
        val injection = DefaultInjectionScope()

        assertThatExceptionOfType(InjectionException::class.java)
            .isThrownBy { injection.bindInterface(TestInterface::class.java, TestInterface::class.java) }
            .withNoCause()
            .withMessage("Class io.github.msengbusch.unitsystem.di.TestInterface is an interface or abstract class. Expecting the second argument to be an actual implementing class")
    }

    @Test
    fun testBindInterfaceFailIfProviderAlreadyBound() {
        val injection = DefaultInjectionScope()

        injection.bindProvider(TestInterface::class.java) { OneArgumentTest("test") }

        assertThatExceptionOfType(InjectionException::class.java)
            .isThrownBy { injection.bindInterface(TestInterface::class.java, OneArgumentTest::class.java) }
            .withNoCause()
            .withMessage("Class io.github.msengbusch.unitsystem.di.TestInterface is already bound as provider")
    }
}

interface TestInterface {
    val string: String
}

class OneArgumentTest @Inject constructor(override val string: String) : TestInterface

@Singleton
class SingletonTest(val string: String)

class ProviderTest @Inject constructor(val provider: Provider<String>)

class ProviderNoTypeTest @Inject constructor(val provider: Provider<*>)

class NoPublicConstructorTest private constructor()

class MoreThanOneConstructorTest @Inject constructor() {

    @Inject
    constructor(test: String) : this()
}

class CircularDependencyTestA @Inject constructor(val circularDependencyTestB: CircularDependencyTestB)

class CircularDependencyTestB @Inject constructor(val circularDependencyTestA: CircularDependencyTestA)