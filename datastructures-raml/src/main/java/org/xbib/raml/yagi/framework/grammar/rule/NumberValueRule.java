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
import org.xbib.raml.yagi.framework.nodes.FloatingNode;
import org.xbib.raml.yagi.framework.nodes.IntegerNode;
import org.xbib.raml.yagi.framework.nodes.Node;
import org.xbib.raml.yagi.framework.nodes.NodeType;
import org.xbib.raml.yagi.framework.nodes.StringNode;
import org.xbib.raml.yagi.framework.suggester.ParsingContext;
import org.xbib.raml.yagi.framework.suggester.Suggestion;

public class NumberValueRule extends AbstractTypeRule {
    private final Number value;

    public NumberValueRule(Number value) {
        this.value = value;
    }


    @Override
    public List<Suggestion> getSuggestions(Node node, ParsingContext context) {
        return Collections.emptyList();
    }

    @Override
    public boolean matches(Node node) {
        if (node instanceof FloatingNode) {
            return ((FloatingNode) node).getValue().doubleValue() == value.doubleValue();
        } else if (node instanceof IntegerNode) {
            return ((IntegerNode) node).getValue() == value.intValue();
        }
        if (node instanceof StringNode) {
            try {
                return Double.parseDouble(((StringNode) node).getValue()) == value.doubleValue();
            } catch (NumberFormatException ex) {
                return false;
            }
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "Number";
    }


    @Override
    NodeType getType() {
        return NodeType.Float;
    }
}
