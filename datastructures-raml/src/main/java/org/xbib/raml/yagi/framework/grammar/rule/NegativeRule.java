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

import java.util.Collections;
import java.util.List;
import org.xbib.raml.yagi.framework.nodes.Node;
import org.xbib.raml.yagi.framework.suggester.ParsingContext;
import org.xbib.raml.yagi.framework.suggester.Suggestion;

public class NegativeRule extends Rule {

    private final Rule rule;


    public NegativeRule(Rule rule) {
        this.rule = rule;
    }

    @Override

    public List<Suggestion> getSuggestions(Node node, ParsingContext context) {
        return Collections.emptyList();
    }


    @Override
    public boolean matches(Node node) {
        return !rule.matches(node);
    }

    @Override
    public Node apply(Node node) {
        final Node result;
        if (this.matches(node)) {
            result = createNodeUsingFactory(node);
        } else {
            result = ErrorNodeFactory.createInvalidNode(node);
        }
        return result;
    }

    @Override
    public String getDescription() {
        String desc = "Not :" +
                rule.getDescription();
        return desc;
    }

    @Override
    public List<Suggestion> getSuggestions(List<Node> pathToRoot, ParsingContext context) {
        return Collections.emptyList();
    }
}
