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
package org.xbib.raml.internal.impl.commons.grammar;

import java.util.List;
import org.xbib.raml.internal.impl.commons.rule.RamlErrorNodeFactory;
import org.xbib.raml.internal.utils.UriTemplateValidation;
import org.xbib.raml.yagi.framework.grammar.rule.Rule;
import org.xbib.raml.yagi.framework.nodes.Node;
import org.xbib.raml.yagi.framework.nodes.snakeyaml.SYStringNode;
import org.xbib.raml.yagi.framework.suggester.ParsingContext;
import org.xbib.raml.yagi.framework.suggester.Suggestion;

public class UriTemplateValidationRule extends Rule {
    private final Rule rule;

    public UriTemplateValidationRule(Rule rule) {
        super();
        this.rule = rule;
    }

    @Override
    public boolean matches(Node node) {
        return rule.matches(node);
    }


    @Override
    public Node apply(Node node) {
        Node apply = rule.apply(node);
        if (node instanceof SYStringNode) {
            if (!UriTemplateValidation.isBalanced(((SYStringNode) node).getValue())) {
                apply = RamlErrorNodeFactory.createInvalidUriTemplate();
            }
        }

        return apply;
    }

    @Override
    public String getDescription() {
        return rule.getDescription();
    }


    @Override
    public List<Suggestion> getSuggestions(Node node, ParsingContext context) {
        return rule.getSuggestions(node, context);
    }
}
