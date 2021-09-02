package io.github.msengbusch.unitsystem.di

import io.github.msengbusch.unitsystem.exception.InjectionException
import java.lang.reflect.Constructor
import java.lang.reflect.Modifier
import java.lang.reflect.Parameter
import java.lang.reflect.ParameterizedType
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

class InjectionScope(private val parent: InjectionScope? = null) {
    private val requestedClasses: MutableSet<Class<*>> = mutableSetOf()
    private val instantiableClasses: MutableSet<Class<*>> = mutableSetOf()
    private val singletons: MutableMap<Class<*>, Any> = mutableMapOf()
    private val providers: MutableMap<Class<*>, Provider<*>> = mutableMapOf()
    private val interfaceMappings: MutableMap<Class<*>, Class<*>> = mutableMapOf()

    fun <T> bindProvider(clazz: Class<T>, provider: Provider<T>) {
        if (singletons.containsKey(clazz)) {
            throw InjectionException("Class ${clazz.name} is already bound as singleton")
        }

        if (interfaceMappings.containsKey(clazz)) {
            throw InjectionException("Class ${clazz.name} is already bound as interface")
        }

        providers[clazz] = provider
    }

    fun <T> bindSingleton(clazz: Class<T>, instance: T) where T : Any {
        if (isAbstractOrInterface(clazz)) {
            throw InjectionException("Class ${clazz.name} can not be bound as singleton because it is an interface or abstract class")
        }

        if (providers.containsKey(clazz)) {
            throw InjectionException("Class ${clazz.name} is already bound with a provider")
        }

        singletons[clazz] = instance
    }

    fun <T> bindInterface(interfaceType: Class<T>, implementationType: Class<out T>) {
        if (!isAbstractOrInterface(interfaceType)) {
            throw InjectionException("Class ${interfaceType.name} is not an interface or abstract class. Expecting the first argument to be one")
        }

        if (isAbstractOrInterface(implementationType)) {
            throw InjectionException("Class ${implementationType.name} is an interface or abstract class. Expecting the second argument to be an actual implementing class")
        }

        if (providers.containsKey(interfaceType)) {
            throw InjectionException("Class ${interfaceType.name} is already bound as provider")
        }

        interfaceMappings[interfaceType] = implementationType
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getInstance(clazz: Class<T>): T {
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

        parent?.let {
            if (parent.knowsType(requestedType)) return parent.getInstance(requestedType)
        }

        return createNewInstance(requestedType)
    }

    private fun knowsType(clazz: Class<*>): Boolean {
        return singletons.containsKey(clazz) || providers.containsKey(clazz)
    }

    private fun <T> createNewInstance(clazz: Class<T>): T {
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