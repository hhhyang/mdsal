/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.mdsal.binding.generator.spi;

import org.opendaylight.mdsal.binding.model.api.Restrictions;
import org.opendaylight.mdsal.binding.model.api.Type;
import org.opendaylight.yangtools.yang.model.api.LeafSchemaNode;
import org.opendaylight.yangtools.yang.model.api.SchemaNode;
import org.opendaylight.yangtools.yang.model.api.TypeDefinition;

public interface TypeProvider {

    @Deprecated
    Type javaTypeForYangType(String type);

    /**
     * Resolve of yang Type Definition to it's java counter part.
     * If the Type Definition contains one of yang primitive types the method
     * will return java.lang. counterpart. (For example if yang type is int32
     * the java counterpart is java.lang.Integer). In case that Type
     * Definition contains extended type defined via yang typedef statement
     * the method SHOULD return Generated Type or Generated Transfer Object
     * if that Type is correctly referenced to resolved imported yang module.
     * The method will return <code>null</code> value in situations that
     * TypeDefinition can't be resolved (either due missing yang import or
     * incorrectly specified type).
     *
     *
     * @param type Type Definition to resolve from
     * @return Resolved Type
     */
    Type javaTypeForSchemaDefinitionType(TypeDefinition<?> type, SchemaNode parentNode);

    Type javaTypeForSchemaDefinitionType(TypeDefinition<?> type, SchemaNode parentNode, Restrictions restrictions);

    /**
     * Returns string containing code for creation of new type instance.
     */
    String getTypeDefaultConstruction(LeafSchemaNode node);

    String getConstructorPropertyName(SchemaNode node);

    String getParamNameFromType(TypeDefinition<?> type);
}
