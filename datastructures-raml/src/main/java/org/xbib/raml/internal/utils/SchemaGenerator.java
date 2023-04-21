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
package org.xbib.raml.internal.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xbib.raml.api.loader.ResourceLoader;
import org.xbib.raml.internal.impl.commons.nodes.ExternalSchemaTypeExpressionNode;
import org.xbib.raml.internal.impl.commons.nodes.TypeDeclarationNode;
import org.xbib.raml.internal.impl.commons.nodes.TypeExpressionNode;
import org.xbib.raml.internal.impl.commons.type.JsonSchemaExternalType;
import org.xbib.raml.internal.impl.commons.type.ResolvedType;
import org.xbib.raml.internal.impl.commons.type.XmlSchemaExternalType;
import org.xbib.raml.internal.impl.v10.nodes.NamedTypeExpressionNode;
import org.xbib.raml.internal.utils.xml.XMLLocalConstants;
import org.xbib.raml.internal.utils.xml.XsdResourceResolver;
import org.xbib.raml.yagi.framework.util.NodeUtils;
import org.xml.sax.SAXException;

public class SchemaGenerator {
    private static final String DEFINITIONS = "/definitions/";

    /*private static LoadingCache<JsonSchemaExternalType, JsonSchema> jsonSchemaCache = CacheBuilder.newBuilder()
                                                                                                  .maximumSize(Integer.parseInt(System.getProperty("yagi.json_cache_size", "200")))
                                                                                                  .build(new CacheLoader<JsonSchemaExternalType, JsonSchema>()
                                                                                                  {

                                                                                                      @Override
                                                                                                      public JsonSchema load(JsonSchemaExternalType jsonTypeDefinition) throws IOException,
                                                                                                              ProcessingException
                                                                                                      {
                                                                                                          return loadJsonSchema(jsonTypeDefinition);
                                                                                                      }
                                                                                                  });*/

    public static Schema generateXmlSchema(ResourceLoader resourceLoader, XmlSchemaExternalType xmlTypeDefinition) throws SAXException {
        SchemaFactory factory = SchemaFactory.newInstance(XMLLocalConstants.XML_SCHEMA_VERSION);
        factory.setResourceResolver(new XsdResourceResolver(resourceLoader, xmlTypeDefinition.getSchemaPath()));
        String includedResourceUri = resolveResourceUriIfIncluded(xmlTypeDefinition);
        return factory.newSchema(new StreamSource(new StringReader(xmlTypeDefinition.getSchemaValue()), includedResourceUri));
    }

    private static JsonSchema loadJsonSchema(JsonSchemaExternalType jsonTypeDefinition) throws IOException, ProcessingException {
        final JsonSchema result;
        String includedResourceUri = resolveResourceUriIfIncluded(jsonTypeDefinition);

        JsonNode jsonSchema = JsonLoader.fromString(jsonTypeDefinition.getSchemaValue());
        JsonSchemaFactory factory = JsonSchemaFactory.byDefault();

        if (jsonTypeDefinition.getInternalFragment() != null) {
            if (includedResourceUri != null) {
                result = factory.getJsonSchema(includedResourceUri + "#" + DEFINITIONS + jsonTypeDefinition.getInternalFragment());
            } else {
                result = factory.getJsonSchema(jsonSchema, DEFINITIONS + jsonTypeDefinition.getInternalFragment());
            }
        } else {
            if (includedResourceUri != null) {
                result = factory.getJsonSchema(includedResourceUri);
            } else {
                result = factory.getJsonSchema(jsonSchema);
            }
        }
        return result;
    }

    public static JsonSchema generateJsonSchema(JsonSchemaExternalType jsonTypeDefinition) throws IOException, ProcessingException {
        try {
            //return jsonSchemaCache.get(jsonTypeDefinition);
            return loadJsonSchema(jsonTypeDefinition);
        } catch (Exception e) {
            if (e.getCause() instanceof JsonParseException)
                throw (JsonParseException) e.getCause();
            else if (e.getCause() instanceof IOException) {
                throw (IOException) e.getCause();
            } else
                throw new ProcessingException(e.getMessage(), e.getCause());
        }
    }


    private static String resolveResourceUriIfIncluded(ResolvedType typeDefinition) {
        // Getting the type holding the schema
        TypeExpressionNode typeExpressionNode = typeDefinition.getTypeExpressionNode();

        if (typeExpressionNode instanceof ExternalSchemaTypeExpressionNode schema) {

            return getIncludedResourceUri(schema);
        } else {
            // Inside the type declaration, we find the node containing the schema itself
            List<ExternalSchemaTypeExpressionNode> schemas = typeExpressionNode.findDescendantsWith(ExternalSchemaTypeExpressionNode.class);
            if (schemas.size() > 0) {
                return getIncludedResourceUri(schemas.get(0));
            } else {
                // If the array is empty, then it must be a reference to a previously defined type
                List<NamedTypeExpressionNode> refNode = typeExpressionNode.findDescendantsWith(NamedTypeExpressionNode.class);

                if (refNode.size() > 0) {
                    // If refNodes is not empty, then we obtain that type
                    typeExpressionNode = refNode.get(0).resolveReference();
                    if (typeExpressionNode != null) {
                        schemas = typeExpressionNode.findDescendantsWith(ExternalSchemaTypeExpressionNode.class);
                        if (schemas.size() > 0) {
                            return getIncludedResourceUri(schemas.get(0));
                        }
                    }
                }
            }
        }

        return null;
    }

    private static String getIncludedResourceUri(ExternalSchemaTypeExpressionNode schemaNode) {
        final String includedResourceUri = schemaNode.getStartPosition().getIncludedResourceUri();

        if (includedResourceUri == null) {
            final TypeDeclarationNode parentTypeDeclaration = NodeUtils.getAncestor(schemaNode, TypeDeclarationNode.class);
            if (parentTypeDeclaration != null)
                return parentTypeDeclaration.getStartPosition().getIncludedResourceUri();
        }

        return includedResourceUri;
    }


    public static boolean isJsonSchema(String schema) {
        return schema.trim().startsWith("{");
    }


    public static boolean isXmlSchema(String schema) {
        return schema.trim().startsWith("<");
    }

}
