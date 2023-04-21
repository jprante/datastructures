/*
 * Copyright 2013 (c) MuleSoft, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package org.xbib.raml.internal.impl.commons.phase;

import com.fasterxml.jackson.core.JsonParseException;
import org.xbib.raml.api.loader.ResourceLoader;
import org.xbib.raml.internal.impl.commons.nodes.ExternalSchemaTypeExpressionNode;
import org.xbib.raml.internal.impl.commons.type.JsonSchemaExternalType;
import org.xbib.raml.internal.impl.commons.type.ResolvedType;
import org.xbib.raml.internal.impl.commons.type.XmlSchemaExternalType;
import org.xbib.raml.internal.utils.SchemaGenerator;
import org.xbib.raml.yagi.framework.grammar.rule.ErrorNodeFactory;
import org.xbib.raml.yagi.framework.nodes.Node;
import org.xbib.raml.yagi.framework.phase.Transformer;

/**
 * Validates that the external schemas are valid schemas.
 */
public class SchemaValidationTransformer implements Transformer {


    private final ResourceLoader resourceLoader;

    public SchemaValidationTransformer(ResourceLoader resourceLoader) {

        this.resourceLoader = resourceLoader;
    }

    @Override
    public boolean matches(Node node) {
        return node instanceof ExternalSchemaTypeExpressionNode;
    }

    @Override
    public Node transform(Node node) {
        ExternalSchemaTypeExpressionNode schema = (ExternalSchemaTypeExpressionNode) node;
        try {
            final ResolvedType resolvedType = schema.generateDefinition();
            if (resolvedType instanceof XmlSchemaExternalType) {
                SchemaGenerator.generateXmlSchema(resourceLoader, (XmlSchemaExternalType) resolvedType);
            } else if (resolvedType instanceof JsonSchemaExternalType) {
                SchemaGenerator.generateJsonSchema((JsonSchemaExternalType) resolvedType);
            }
        } catch (JsonParseException ex) {
            return ErrorNodeFactory.createInvalidSchemaNode(ex.getOriginalMessage());
        } catch (Exception e) {
            return ErrorNodeFactory.createInvalidSchemaNode(e.getMessage());
        }
        return node;
    }
}
