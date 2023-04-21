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
package org.xbib.raml.internal.impl.commons.rule;

import java.util.Collections;
import java.util.List;
import org.xbib.raml.internal.impl.commons.nodes.ParametrizedReferenceNode;
import org.xbib.raml.internal.impl.commons.suggester.ReferenceSuggester;
import org.xbib.raml.yagi.framework.grammar.rule.ObjectRule;
import org.xbib.raml.yagi.framework.nodes.Node;
import org.xbib.raml.yagi.framework.suggester.ParsingContext;
import org.xbib.raml.yagi.framework.suggester.Suggestion;

public class ParametrizedNodeReferenceRule extends ObjectRule {

    private final ReferenceSuggester suggester;

    public ParametrizedNodeReferenceRule(String referenceKey) {
        suggester = new ReferenceSuggester(referenceKey);
    }

    @Override
    public boolean matches(Node node) {
        // It should have at least one key value pair
        return (super.matches(node) && hasOneKey(node)) || node instanceof ParametrizedReferenceNode;
    }

    private boolean hasOneKey(Node node) {
        return !node.getChildren().isEmpty() && !node.getChildren().get(0).getChildren().isEmpty();
    }


    @Override
    public List<Suggestion> getSuggestions(List<Node> pathToRoot, ParsingContext context) {
        if (pathToRoot.size() == 1) {
            return getSuggestions(pathToRoot.get(0), context);
        }
        return Collections.emptyList();
    }


    @Override
    public List<Suggestion> getSuggestions(Node node, ParsingContext context) {
        if (hasOneKey(node)) {
            final Node key = node.getChildren().get(0).getChildren().get(0);
            return suggester.getSuggestions(key);
        } else {
            return super.getSuggestions(node, context);
        }
    }

    @Override
    protected Node getResult(Node node) {
        if (node instanceof ParametrizedReferenceNode) {
            return node;
        }
        final String arg = node.getChildren().get(0).getChildren().get(0).toString();
        return createNodeUsingFactory(node, arg);
    }

    @Override
    public String getDescription() {
        return "Parametrized reference call.";
    }
}
