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
import java.util.List;
import org.xbib.raml.internal.impl.commons.nodes.TypeExpressionNode;
import org.xbib.raml.internal.impl.commons.suggester.ReferenceSuggester;
import org.xbib.raml.internal.impl.v10.type.TypeId;
import org.xbib.raml.internal.utils.StringUtils;
import org.xbib.raml.yagi.framework.grammar.rule.Rule;
import org.xbib.raml.yagi.framework.nodes.Node;
import org.xbib.raml.yagi.framework.nodes.SimpleTypeNode;
import org.xbib.raml.yagi.framework.nodes.StringNode;
import org.xbib.raml.yagi.framework.suggester.DefaultSuggestion;
import org.xbib.raml.yagi.framework.suggester.ParsingContext;
import org.xbib.raml.yagi.framework.suggester.Suggestion;

public class TypeExpressionReferenceRule extends Rule {

    private final ReferenceSuggester suggester;

    public TypeExpressionReferenceRule() {
        this.suggester = new ReferenceSuggester("types");
    }

    @Override
    public boolean matches(Node node) {
        // It should be a simple type expression if is an object type then it may be a TypeExpressionNode but it should not match here
        return node instanceof SimpleTypeNode;
    }


    @Override
    public Node apply(Node node) {
        if (node instanceof TypeExpressionNode) {
            return node;
        }
        return createNodeUsingFactory(node, ((StringNode) node).getLiteralValue());
    }

    @Override
    public String getDescription() {
        return "Type reference expression.";
    }


    @Override
    public List<Suggestion> getSuggestions(Node node, ParsingContext context) {
        final List<Suggestion> suggestions = new ArrayList<>(suggester.getSuggestions(node));
        final TypeId[] values = TypeId.values();
        for (TypeId value : values) {
            suggestions.add(new DefaultSuggestion(value.getType(), "", StringUtils.capitalize(value.getType())));
        }
        return suggestions;
    }
}
