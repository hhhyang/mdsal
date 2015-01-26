/*
 * Copyright (c) 2014 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.binding.data.codec.impl;

import com.google.common.collect.ImmutableMap;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.opendaylight.yangtools.concepts.Codec;
import org.opendaylight.yangtools.yang.binding.Identifier;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier.IdentifiableItem;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.data.api.YangInstanceIdentifier.NodeIdentifierWithPredicates;
import org.opendaylight.yangtools.yang.model.api.ListSchemaNode;

final class IdentifiableItemCodec implements Codec<NodeIdentifierWithPredicates, IdentifiableItem<?, ?>> {
    private final Map<QName, ValueContext> keyValueContexts;
    private final ListSchemaNode schema;
    private final Constructor<? extends Identifier<?>> constructor;
    private final Class<?> identifiable;

    public IdentifiableItemCodec(final ListSchemaNode schema, final Class<? extends Identifier<?>> keyClass,
            final Class<?> identifiable, final Map<QName, ValueContext> keyValueContexts) {
        this.schema = schema;
        this.identifiable = identifiable;
        this.constructor = getConstructor(keyClass);

        /*
         * We need to re-index to make sure we instantiate nodes in the order in which
         * they are defined.
         */
        final Map<QName, ValueContext> keys = new LinkedHashMap<>();
        for (final QName qname : schema.getKeyDefinition()) {
            keys.put(qname, keyValueContexts.get(qname));
        }
        this.keyValueContexts = ImmutableMap.copyOf(keys);
    }

    @Override
    public IdentifiableItem<?, ?> deserialize(final NodeIdentifierWithPredicates input) {
        final Collection<QName> keys = schema.getKeyDefinition();
        final ArrayList<Object> bindingValues = new ArrayList<>(keys.size());
        for (final QName key : keys) {
            final Object yangValue = input.getKeyValues().get(key);
            bindingValues.add(keyValueContexts.get(key).deserialize(yangValue));
        }

        final Identifier<?> identifier;
        try {
            identifier = constructor.newInstance(bindingValues.toArray());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(String.format("Failed to instantiate key class %s", constructor.getDeclaringClass()), e);
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        final IdentifiableItem identifiableItem = new IdentifiableItem(identifiable, identifier);
        return identifiableItem;
    }

    @Override
    public NodeIdentifierWithPredicates serialize(final IdentifiableItem<?, ?> input) {
        final Object value = input.getKey();

        final Map<QName, Object> values = new LinkedHashMap<>();
        for (final Entry<QName, ValueContext> valueCtx : keyValueContexts.entrySet()) {
            values.put(valueCtx.getKey(), valueCtx.getValue().getAndSerialize(value));
        }
        return new NodeIdentifierWithPredicates(schema.getQName(), values);
    }


    @SuppressWarnings("unchecked")
    private static Constructor<? extends Identifier<?>> getConstructor(final Class<? extends Identifier<?>> clazz) {
        for (@SuppressWarnings("rawtypes") final Constructor constr : clazz.getConstructors()) {
            final Class<?>[] parameters = constr.getParameterTypes();
            if (!clazz.equals(parameters[0])) {
                // It is not copy constructor;
                return constr;
            }
        }
        throw new IllegalArgumentException("Supplied class " + clazz + "does not have required constructor.");
    }

}