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
import org.xbib.raml.yagi.framework.nodes.ErrorNode;
import org.xbib.raml.yagi.framework.nodes.Node;
import org.xbib.raml.yagi.framework.suggester.ParsingContext;
import org.xbib.raml.yagi.framework.suggester.Suggestion;

/**
 * Accepts if any rule matches and delegates the suggestion to the first one that matches.
 */
public class FirstOfRule extends AnyOfRule {

    public FirstOfRule(List<Rule> rules) {
        super(rules);
    }

    @Override

    public List<Suggestion> getSuggestions(Node node, ParsingContext context) {
        for (Rule rule : rules) {
            if (rule.matches(node)) {
                return rule.getSuggestions(node, context);
            }
        }
        return rules.get(0).getSuggestions(node, context);
    }

    @Override
    public Node apply(Node node) {
        Node apply = super.apply(node);
        if (apply instanceof ErrorNode) {
            apply = rules.get(0).apply(node);
        }
        return apply;
    }
}
