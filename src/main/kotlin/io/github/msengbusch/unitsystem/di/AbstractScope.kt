package io.github.msengbusch.unitsystem.di

import io.github.msengbusch.unitsystem.exception.InjectionException
import java.lang.reflect.Constructor
import java.lang.reflect.Modifier
import java.lang.reflect.Parameter
import java.lang.reflect.ParameterizedType
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

abstract class AbstractScope : Scope {
    protected val requestedClasses: MutableSet<Class<*>> = mutableSetOf()
    protected val instantiableClasses: MutableSet<Class<*>> = mutableSetOf()
    protected val singletons: MutableMap<Class<*>, Any> = mutableMapOf()
    protected val providers: MutableMap<Class<*>, Provider<*>> = mutableMapOf()
    protected val interfaceMappings: MutableMap<Class<*>, Class<*>> = mutableMapOf()

    override fun <T> bindProvider(clazz: Class<T>, provider: Provider<T>) {
        if (singletons.containsKey(clazz)) {
            throw InjectionException("Class ${clazz.name} is already bound as singleton")
        }

        if (interfaceMappings.containsKey(clazz)) {
            throw InjectionException("Class ${clazz.name} is already bound as interface")
        }

        providers[clazz] = provider
    }

    override fun <T> bindSingleton(clazz: Class<T>, instance: T) where T : Any {
        if (isAbstractOrInterface(clazz)) {
            throw InjectionException("Class ${clazz.name} can not be bound as singleton because it is an interface or abstract class")
        }

        if (providers.containsKey(clazz)) {
            throw InjectionException("Class ${clazz.name} is already bound with a provider")
        }

        singletons[clazz] = instance
    }

    override fun <T> bindInterface(interfaceClass: Class<T>, implementationClass: Class<out T>) {
        if (!isAbstractOrInterface(interfaceClass)) {
            throw InjectionException("Class ${interfaceClass.name} is not an interface or abstract class. Expecting the first argument to be one")
        }

        if (isAbstractOrInterface(implementationClass)) {
            throw InjectionException("Class ${implementationClass.name} is an interface or abstract class. Expecting the second argument to be an actual implementing class")
        }

        if (providers.containsKey(interfaceClass)) {
            throw InjectionException("Class ${interfaceClass.name} is already bound as provider")
        }

        interfaceMappings[interfaceClass] = implementationClass
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getInstance(clazz: Class<T>): T {
        var requestedType = clazz

        if (isAbstractOrInterface(requestedType)) {
            if (providers.containsKey(requestedType)) {
                return createInstanceFromProvider(requestedType)
            }

            if (!interfaceMappings.containsKey(requestedType)) {
                throw InjectionException("For the interface or abstract class ${requestedType.name} is no mapping registered. Please register one or make the class instantiable")
            }

            requestedType = interfaceMappings[requestedType] as Class<T>
        }

        preventCircular(requestedType)

        if (singletons.containsKey(requestedType)) {
            return singletons[requestedType] as T
        }

        if (providers.containsKey(requestedType)) {
            val instance = createInstanceFromProvider(requestedType)

            if (isSingleton(requestedType)) {
                singletons[requestedType] = instance!!
            }

            return instance
        }

        return createNewInstance(requestedType)
    }

    override fun isTypeKnown(clazz: Class<*>): Boolean = singletons.containsKey(clazz) || providers.containsKey(clazz)

    protected open fun <T> createNewInstance(clazz: Class<T>): T {
        val constructor = findConstructor(clazz)
        val arguments = constructor.parameters.map {
            if (it.type == Provider::class.java) {
                getProviderArgument(it, clazz)
            } else {
                getInstance(it.type)
            }
        }

        try {
            val instance = constructor.newInstance(*arguments.toTypedArray())
            markInstantiable(clazz)

            return instance
        } catch (e: RuntimeException) {
            throw InjectionException("Failed to create instance of class ${clazz.name}", e)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> createInstanceFromProvider(clazz: Class<T>): T {
        try {
            val instance = providers[clazz]?.get() as T
            markInstantiable(clazz)
            return instance
        } catch (e: RuntimeException) {
            throw InjectionException("Failed to obtain instance of ${clazz.name} from provider", e)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> findConstructor(clazz: Class<T>): Constructor<T> {
        val constructors = clazz.constructors

        if (constructors.isEmpty()) {
            throw InjectionException("Class ${clazz.name} has no public constructor for dependency injection")
        }

        val injectionConstructors = constructors.filter { it.isAnnotationPresent(Inject::class.java) }

        if (injectionConstructors.isEmpty()) {
            throw InjectionException("To allow dependency injection of class ${clazz.name} annotate one public constructor with @Inject")
        }

        if (injectionConstructors.size > 1) {
            throw InjectionException("There is more than one public constructor marked with @Inject in class ${clazz.name}. Only one is allowed")
        }

        return injectionConstructors[0] as Constructor<T>
    }

    @Suppress("UNCHECKED_CAST")
    private fun getProviderArgument(param: Parameter, clazz: Class<*>): Provider<*> {
        if (param.parameterizedType is ParameterizedType) {
            val providedType = (param.parameterizedType as ParameterizedType).actualTypeArguments[0]
            return Provider {
                getInstance(providedType as Class<*>)
            }
        } else {
            throw InjectionException("There is a Provider without a type parameter declared as dependency: ${clazz.name}")
        }
    }

    private fun preventCircular(clazz: Class<*>) {
        if (requestedClasses.contains(clazz)) {
            if (!instantiableClasses.contains(clazz)) {
                throw InjectionException("Circular dependency detected: ${clazz.name}")
            }
        } else {
            requestedClasses.add(clazz)
        }
    }

    private fun isAbstractOrInterface(clazz: Class<*>) = clazz.isAnnotation || Modifier.isAbstract(clazz.modifiers)

    private fun isSingleton(clazz: Class<*>) = clazz.isAnnotationPresent(Singleton::class.java)

    private fun markInstantiable(clazz: Class<*>) {
        if (!instantiableClasses.contains(clazz)) {
            instantiableClasses.add(clazz)
        }
    }
}