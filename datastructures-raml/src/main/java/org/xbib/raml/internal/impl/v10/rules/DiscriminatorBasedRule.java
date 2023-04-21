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
package org.xbib.raml.internal.impl.v10.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xbib.raml.internal.impl.commons.nodes.TypeDeclarationField;
import org.xbib.raml.internal.impl.commons.nodes.TypeDeclarationNode;
import org.xbib.raml.internal.impl.v10.nodes.LibraryLinkNode;
import org.xbib.raml.internal.impl.v10.type.TypeToRuleVisitor;
import org.xbib.raml.yagi.framework.grammar.rule.ErrorNodeFactory;
import org.xbib.raml.yagi.framework.grammar.rule.Rule;
import org.xbib.raml.yagi.framework.nodes.Node;
import org.xbib.raml.yagi.framework.nodes.SimpleTypeNode;
import org.xbib.raml.yagi.framework.suggester.ParsingContext;
import org.xbib.raml.yagi.framework.suggester.Suggestion;

public class DiscriminatorBasedRule extends Rule {
    private final TypeToRuleVisitor typeToRuleVisitor;
    private final Node rootElement;
    private final String discriminatorProperty;
    private final Map<String, Rule> typeRulesCache;


    public DiscriminatorBasedRule(TypeToRuleVisitor typeToRuleVisitor, Node rootElement, String discriminatorProperty) {
        this.typeToRuleVisitor = typeToRuleVisitor;
        this.rootElement = rootElement;
        this.discriminatorProperty = discriminatorProperty;
        this.typeRulesCache = new HashMap<>();
    }

    @Override
    public boolean matches(Node node) {
        final Node discriminatorNode = node.get(discriminatorProperty);
        return discriminatorNode instanceof SimpleTypeNode;
    }


    @Override
    public Node apply(Node node) {
        final SimpleTypeNode discriminatorValue = (SimpleTypeNode) node.get(discriminatorProperty);
        if (discriminatorValue == null) {
            node.replaceWith(ErrorNodeFactory.createInvalidType("discriminator not specified"));
        } else {
            final String literalValue = discriminatorValue.getLiteralValue();
            Rule value = findType(literalValue);
            if (value != null) {
                value.apply(node);
            } else {
                node.replaceWith(ErrorNodeFactory.createInvalidType(literalValue));
            }
        }
        return node;
    }


    protected Rule findType(String literalValue) {
        Rule value = null;
        if (!typeRulesCache.containsKey(literalValue)) {
            final TypeDeclarationNode typeDeclaration = findTypeDeclaration(literalValue);
            if (typeDeclaration != null) {
                typeToRuleVisitor.resolveDiscrimintor();
                value = typeToRuleVisitor.generateRule(typeDeclaration.getResolvedType());
                typeRulesCache.put(literalValue, value);
            }
        } else {
            value = typeRulesCache.get(literalValue);
        }
        return value;
    }


    protected TypeDeclarationNode findTypeDeclaration(String literalValue) {
        final List<TypeDeclarationField> descendantsWith = findTypeDeclarationField(rootElement);
        for (TypeDeclarationField typeDeclarationField : descendantsWith) {
            final Node typeDeclaration = typeDeclarationField.getValue();
            final Node discriminatorValue = typeDeclaration.get("discriminatorValue");
            String typeIdentifier;
            if (discriminatorValue instanceof SimpleTypeNode) {
                typeIdentifier = ((SimpleTypeNode) discriminatorValue).getLiteralValue();
            } else {
                typeIdentifier = ((SimpleTypeNode) typeDeclarationField.getKey()).getLiteralValue();
            }

            if (literalValue.equals(typeIdentifier)) {
                return (TypeDeclarationNode) typeDeclaration;
            }
        }
        return null;
    }


    public List<TypeDeclarationField> findTypeDeclarationField(Node node) {
        final List<TypeDeclarationField> result = new ArrayList<>();
        final List<Node> children = node.getChildren();
        for (Node child : children) {
            if (child instanceof TypeDeclarationField) {
                result.add((TypeDeclarationField) child);
            } else if (child instanceof LibraryLinkNode) {
                // Should search across libraries
                result.addAll(findTypeDeclarationField(((LibraryLinkNode) child).getRefNode()));
            }
            result.addAll(findTypeDeclarationField(child));
        }
        return result;
    }

    @Override
    public String getDescription() {
        return "Dynamic rule based on '" + discriminatorProperty + "'.";
    }


    @Override
    public List<Suggestion> getSuggestions(Node node, ParsingContext context) {
        return Collections.emptyList();
    }
}
