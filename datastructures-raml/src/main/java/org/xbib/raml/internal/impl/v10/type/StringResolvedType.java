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
package org.xbib.raml.internal.impl.v10.type;

import java.util.ArrayList;
import java.util.List;
import org.xbib.raml.internal.impl.commons.nodes.TypeDeclarationNode;
import org.xbib.raml.internal.impl.commons.nodes.TypeExpressionNode;
import org.xbib.raml.internal.impl.commons.rule.RamlErrorNodeFactory;
import org.xbib.raml.internal.impl.commons.type.ResolvedCustomFacets;
import org.xbib.raml.internal.impl.commons.type.ResolvedType;
import org.xbib.raml.internal.impl.v10.grammar.Raml10Grammar;
import org.xbib.raml.internal.impl.v10.rules.TypesUtils;
import org.xbib.raml.yagi.framework.grammar.rule.AnyOfRule;
import org.xbib.raml.yagi.framework.nodes.ArrayNode;
import org.xbib.raml.yagi.framework.nodes.ErrorNode;
import org.xbib.raml.yagi.framework.nodes.Node;
import org.xbib.raml.yagi.framework.nodes.SimpleTypeNode;
import static org.xbib.raml.internal.impl.v10.grammar.Raml10Grammar.MAX_LENGTH_KEY_NAME;
import static org.xbib.raml.internal.impl.v10.grammar.Raml10Grammar.MIN_LENGTH_KEY_NAME;
import static org.xbib.raml.internal.impl.v10.grammar.Raml10Grammar.PATTERN_KEY_NAME;
import static org.xbib.raml.yagi.framework.util.NodeSelector.selectIntValue;
import static org.xbib.raml.yagi.framework.util.NodeSelector.selectStringValue;

public class StringResolvedType extends XmlFacetsCapableType {
    private static final int DEFAULT_MIN_LENGTH = 0;
    private static final int DEFAULT_MAX_LENGTH = Integer.MAX_VALUE;

    private Integer minLength;
    private Integer maxLength;
    private String pattern;
    private List<String> enums = new ArrayList<>();


    public StringResolvedType(TypeExpressionNode from) {
        super(getTypeName(from, TypeId.STRING.getType()), from, new ResolvedCustomFacets(MIN_LENGTH_KEY_NAME, MAX_LENGTH_KEY_NAME, PATTERN_KEY_NAME));
    }

    public StringResolvedType(String typeName, TypeExpressionNode declarationNode, XmlFacets xmlFacets, Integer minLength, Integer maxLength, String pattern, List<String> enums,
                              ResolvedCustomFacets customFacets) {
        super(typeName, declarationNode, xmlFacets, customFacets);
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.pattern = pattern;
        this.enums = enums;
    }

    protected StringResolvedType copy() {
        return new StringResolvedType(getTypeName(), getTypeExpressionNode(), getXmlFacets(), minLength, maxLength, pattern, enums, customFacets.copy());
    }


    @Override
    public ResolvedType overwriteFacets(TypeDeclarationNode from) {
        final StringResolvedType result = copy();
        result.customFacets = customFacets.copy().overwriteFacets(from);
        result.setMinLength(selectIntValue(MIN_LENGTH_KEY_NAME, from));
        result.setMaxLength(selectIntValue(MAX_LENGTH_KEY_NAME, from));
        result.setPattern(selectStringValue(PATTERN_KEY_NAME, from));
        result.setEnums(getEnumValues(from));
        return overwriteFacets(result, from);
    }


    private List<String> getEnumValues(Node typeNode) {

        Node values = typeNode.get("enum");
        List<String> enumValues = new ArrayList<>();
        if (values != null && values instanceof ArrayNode) {
            for (Node node : values.getChildren()) {
                enumValues.add(((SimpleTypeNode) node).getLiteralValue());
            }
        }
        return enumValues;
    }

    @Override
    public ResolvedType mergeFacets(ResolvedType with) {
        final StringResolvedType result = copy();
        if (with instanceof StringResolvedType stringTypeDefinition) {
            result.setMaxLength(stringTypeDefinition.getMaxLength());
            result.setMinLength(stringTypeDefinition.getMinLength());
            result.setPattern(stringTypeDefinition.getPattern());
            result.setEnums(stringTypeDefinition.getEnums());
        }
        result.customFacets = result.customFacets.mergeWith(with.customFacets());
        return mergeFacets(result, with);
    }


    @Override
    public void validateCanOverwriteWith(TypeDeclarationNode from) {
        customFacets.validate(from);
        final Raml10Grammar raml10Grammar = new Raml10Grammar();
        final AnyOfRule facetRule = new AnyOfRule()
                .add(raml10Grammar.patternField())
                .add(raml10Grammar.minLengthField())
                .add(raml10Grammar.maxLengthField())
                .add(raml10Grammar.enumField())
                .addAll(customFacets.getRules());
        TypesUtils.validateAllWith(facetRule, from.getFacets());
    }

    @Override
    public void validateState() {
        super.validateState();
        final ErrorNode errorNode = validateFacets();
        if (errorNode != null) {
            getTypeExpressionNode().replaceWith(errorNode);
        }
    }

    private ErrorNode validateFacets() {
        int minimumLength = minLength != null ? minLength : DEFAULT_MIN_LENGTH;
        int maximumLength = maxLength != null ? maxLength : DEFAULT_MAX_LENGTH;

        // Validating conflicts between the length facets
        if (maximumLength < minimumLength) {
            return RamlErrorNodeFactory.createInvalidFacetState(
                    getTypeName(),
                    "maxLength must be greater than or equal to minLength");
        }

        // For each enum in the list, it must be between the range defined by minLength and maxLength
        for (String thisEnum : enums) {
            if (thisEnum.length() < minimumLength || thisEnum.length() > maximumLength) {
                return RamlErrorNodeFactory.createInvalidFacetState(
                        getTypeName(),
                        "enum values must have between " + minimumLength + " and " + maximumLength + " characters");
            }
        }

        return null;
    }

    @Override
    public <T> T visit(TypeVisitor<T> visitor) {
        return visitor.visitString(this);
    }


    @Override
    public String getBuiltinTypeName() {
        return TypeId.STRING.getType();
    }

    private void setPattern(String pattern) {
        if (pattern != null) {
            this.pattern = pattern;
        }
    }

    private void setMinLength(Integer minLength) {
        if (minLength != null) {
            this.minLength = minLength;
        }
    }

    private void setMaxLength(Integer maxLength) {
        if (maxLength != null) {
            this.maxLength = maxLength;
        }
    }

    public List<String> getEnums() {
        return enums;
    }

    public void setEnums(List<String> enums) {
        if (enums != null && !enums.isEmpty()) {
            this.enums = enums;
        }
    }


    public Integer getMinLength() {
        return minLength;
    }


    public Integer getMaxLength() {
        return maxLength;
    }


    public String getPattern() {
        return pattern;
    }
}
