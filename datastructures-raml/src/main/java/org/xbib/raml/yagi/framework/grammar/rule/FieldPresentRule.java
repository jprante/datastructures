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
package org.xbib.raml.yagi.framework.grammar.rule;

import java.util.List;
import org.xbib.raml.yagi.framework.nodes.Node;
import org.xbib.raml.yagi.framework.suggester.ParsingContext;
import org.xbib.raml.yagi.framework.suggester.Suggestion;
import org.xbib.raml.yagi.framework.util.NodeSelector;

/**
 * Delegates to a rule if a selector expression returns a value
 */
public class FieldPresentRule extends Rule {

    private final String selector;

    private final Rule delegate;

    public FieldPresentRule(String selector, Rule then) {
        this.selector = selector;
        this.delegate = then;
    }

    @Override
    public List<Suggestion> getSuggestions(List<Node> pathToRoot, ParsingContext context) {
        return delegate.getSuggestions(pathToRoot, context);
    }


    @Override
    public List<Suggestion> getSuggestions(Node node, ParsingContext context) {
        return delegate.getSuggestions(node, context);
    }

    @Override
    public boolean matches(Node node) {
        return isPresent(node) && delegate.matches(node);
    }

    private boolean isPresent(Node node) {
        return NodeSelector.selectFrom(selector, node) != null;
    }

    @Override
    public Node apply(Node node) {
        if (isPresent(node)) {
            return delegate.apply(node);
        } else {
            return ErrorNodeFactory.createMissingField(selector);
        }
    }

    @Override
    public String getDescription() {
        return delegate.getDescription();
    }
}
