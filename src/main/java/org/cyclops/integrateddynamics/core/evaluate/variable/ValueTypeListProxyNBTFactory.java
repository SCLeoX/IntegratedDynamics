package org.cyclops.integrateddynamics.core.evaluate.variable;

import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import org.cyclops.cyclopscore.persist.nbt.INBTProvider;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValue;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValueType;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValueTypeListProxy;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValueTypeListProxyFactoryTypeRegistry;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Factory for list proxies that implement {@link org.cyclops.cyclopscore.persist.nbt.INBTProvider}.
 * @author rubensworks
 */
public class ValueTypeListProxyNBTFactory<T extends IValueType<V>, V extends IValue, P extends IValueTypeListProxy<T, V> & INBTProvider> implements IValueTypeListProxyFactoryTypeRegistry.IProxyFactory<T, V, P> {

    private final String name;
    private final Class<P> proxyClass;

    public ValueTypeListProxyNBTFactory(String name, Class<P> proxyClass) {
        this.name = name;
        this.proxyClass = proxyClass;
    }

    @Override
    public String getName() {
        return this.name;
    }

    protected Class<P> getProxyClass() {
        return this.proxyClass;
    }

    @Override
    public String serialize(P values) throws IValueTypeListProxyFactoryTypeRegistry.SerializationException {
        NBTTagCompound tag = new NBTTagCompound();
        values.writeGeneratedFieldsToNBT(tag);
        return tag.toString();
    }

    @Override
    public P deserialize(String value) throws IValueTypeListProxyFactoryTypeRegistry.SerializationException {
        try {
            Constructor<P> constructor = getProxyClass().getConstructor();
            P proxy = constructor.newInstance();
            NBTTagCompound tag = JsonToNBT.getTagFromJson(value);
            proxy.readGeneratedFieldsFromNBT(tag);
            return proxy;
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | NBTException | IllegalAccessException e) {
            e.printStackTrace();
            throw new IValueTypeListProxyFactoryTypeRegistry.SerializationException(e.getMessage());
        }
    }
}
