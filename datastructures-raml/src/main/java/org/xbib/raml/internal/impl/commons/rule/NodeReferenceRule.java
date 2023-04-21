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

import java.util.List;
import org.xbib.raml.internal.impl.commons.suggester.ReferenceSuggester;
import org.xbib.raml.yagi.framework.grammar.rule.StringTypeRule;
import org.xbib.raml.yagi.framework.nodes.Node;
import org.xbib.raml.yagi.framework.nodes.ReferenceNode;
import org.xbib.raml.yagi.framework.nodes.SimpleTypeNode;
import org.xbib.raml.yagi.framework.suggester.ParsingContext;
import org.xbib.raml.yagi.framework.suggester.Suggestion;

public class NodeReferenceRule extends StringTypeRule {

    private final ReferenceSuggester suggester;

    public NodeReferenceRule(String referenceKey) {
        this.suggester = new ReferenceSuggester(referenceKey);
    }


    @Override
    public List<Suggestion> getSuggestions(Node node, ParsingContext context) {
        return suggester.getSuggestions(node);
    }

    @Override
    public Node apply(Node node) {
        if (node instanceof ReferenceNode) {
            return node;
        } else {
            return super.apply(node);
        }
    }

    @Override
    public boolean matches(Node node) {
        return node instanceof SimpleTypeNode || node instanceof ReferenceNode;
    }
}
