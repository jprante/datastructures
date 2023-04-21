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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import org.xbib.raml.yagi.framework.nodes.FloatingNode;
import org.xbib.raml.yagi.framework.nodes.IntegerNode;
import org.xbib.raml.yagi.framework.nodes.Node;
import org.xbib.raml.yagi.framework.nodes.SimpleTypeNode;
import org.xbib.raml.yagi.framework.nodes.StringNode;
import org.xbib.raml.yagi.framework.suggester.ParsingContext;
import org.xbib.raml.yagi.framework.suggester.Suggestion;

public class MaximumValueRule extends Rule {

    private final Number maximumValue;

    public MaximumValueRule(Number maximumValue) {
        this.maximumValue = maximumValue;
    }


    @Override
    public List<Suggestion> getSuggestions(Node node, ParsingContext context) {
        return Collections.emptyList();
    }

    @Override
    public boolean matches(Node node) {
        BigDecimal value = null;
        if (node instanceof StringNode) {
            value = new BigDecimal(((StringNode) node).getValue());
        } else if (node instanceof IntegerNode) {
            value = BigDecimal.valueOf(((IntegerNode) node).getValue());
        } else if (node instanceof FloatingNode) {
            value = ((FloatingNode) node).getValue();
        }
        return value != null && value.compareTo(BigDecimal.valueOf(maximumValue.doubleValue())) <= 0;
    }

    @Override
    public Node apply(Node node) {
        if (matches(node)) {
            return createNodeUsingFactory(node, ((SimpleTypeNode) node).getValue());
        } else {
            return ErrorNodeFactory.createInvalidMaximumValue(maximumValue);
        }
    }


    @Override
    public String getDescription() {
        return "Maximum value";
    }
}
