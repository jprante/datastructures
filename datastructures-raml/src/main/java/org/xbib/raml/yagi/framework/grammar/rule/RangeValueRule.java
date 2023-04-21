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

public class RangeValueRule extends Rule {

    private final Number lowerBound;

    private final Number upperBound;

    public RangeValueRule(Number lowerBound, Number upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }


    @Override
    public List<Suggestion> getSuggestions(Node node, ParsingContext context) {
        return Collections.emptyList();
    }

    @Override
    public boolean matches(Node node) {
        if (node instanceof IntegerNode || node instanceof FloatingNode) {
            return true;
        } else if (node instanceof SimpleTypeNode) {
            try {
                Long.parseLong(((SimpleTypeNode) node).getLiteralValue());
                return true;
            } catch (NumberFormatException parseLongException) {
                try {
                    Double.parseDouble(((SimpleTypeNode) node).getLiteralValue());
                    return true;
                } catch (NumberFormatException parseDoubleException) {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    @Override
    public Node apply(Node node) {
        if (validate(node)) {
            return createNodeUsingFactory(node, ((SimpleTypeNode) node).getValue());
        } else {
            return ErrorNodeFactory.createInvalidRangeValue(node, lowerBound, upperBound);
        }
    }

    private boolean validate(Node node) {
        if (node instanceof IntegerNode) {
            Long value = ((IntegerNode) node).getValue();
            return containsLong(value);
        } else if (node instanceof FloatingNode) {
            BigDecimal value = ((FloatingNode) node).getValue();
            return containsDouble(value.doubleValue());
        } else if (node instanceof SimpleTypeNode) {
            try {
                long parseLong = Long.parseLong(((StringNode) node).getValue());
                return containsLong(parseLong);
            } catch (NumberFormatException parseLongException) {
                try {
                    double parseDouble = Double.parseDouble(((StringNode) node).getValue());
                    return containsDouble(parseDouble);
                } catch (NumberFormatException parseDoubleException) {
                    return false;
                }

            }
        } else {
            return false;
        }
    }

    @Override
    public String getDescription() {
        return "Maximum value";
    }

    private boolean containsLong(Long l) {
        return lowerBound == null || upperBound == null || (lowerBound.longValue() <= l && l <= upperBound.longValue());
    }

    private boolean containsDouble(Double d) {
        return lowerBound == null || upperBound == null || (lowerBound.doubleValue() <= d && d <= upperBound.doubleValue());
    }
}
