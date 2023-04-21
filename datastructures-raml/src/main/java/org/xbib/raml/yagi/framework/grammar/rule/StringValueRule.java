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
import org.xbib.raml.internal.utils.StringUtils;
import org.xbib.raml.yagi.framework.nodes.Node;
import org.xbib.raml.yagi.framework.nodes.NodeType;
import org.xbib.raml.yagi.framework.nodes.StringNode;
import org.xbib.raml.yagi.framework.suggester.DefaultSuggestion;
import org.xbib.raml.yagi.framework.suggester.ParsingContext;
import org.xbib.raml.yagi.framework.suggester.Suggestion;

public class StringValueRule extends Rule {

    private final String value;
    private String description;
    private boolean caseSensitive = true;

    public StringValueRule(String value) {
        this.value = value;
    }


    @Override
    public List<Suggestion> getSuggestions(Node node, ParsingContext context) {
        return Collections.singletonList(new DefaultSuggestion(value, description, StringUtils.capitalize(value)));
    }


    @Override
    public boolean matches(Node node) {
        return node instanceof StringNode &&
                ((caseSensitive) ?
                        ((StringNode) node).getValue().equals(value) :
                        ((StringNode) node).getValue().equalsIgnoreCase(value));
    }

    public StringValueRule description(String description) {
        this.description = description;
        return this;
    }

    public StringValueRule caseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
        return this;
    }


    @Override
    public Node apply(Node node) {
        if (!(node instanceof StringNode)) {
            return ErrorNodeFactory.createInvalidType(node, NodeType.String);
        }
        if (!matches(node)) {
            return ErrorNodeFactory.createInvalidValue(node, value);
        }
        return createNodeUsingFactory(node, ((StringNode) node).getValue());

    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public String getDescription() {
        return "\"" + value + "\"";
    }

    public String getValue() {
        return value;
    }
}
