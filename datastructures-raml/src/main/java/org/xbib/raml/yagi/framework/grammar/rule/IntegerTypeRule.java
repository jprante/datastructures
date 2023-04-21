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
import org.xbib.raml.yagi.framework.nodes.NodeType;
import org.xbib.raml.yagi.framework.nodes.StringNode;
import org.xbib.raml.yagi.framework.suggester.ParsingContext;
import org.xbib.raml.yagi.framework.suggester.Suggestion;

public class IntegerTypeRule extends AbstractTypeRule {

    private final boolean castStringsAsNumbers;

    private Long lowerBound;

    private Long upperBound;

    public IntegerTypeRule(Long lowerBound, Long upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.castStringsAsNumbers = false;
    }

    public IntegerTypeRule() {
        this(null, null);
    }

    public IntegerTypeRule(boolean castStringsAsNumbers) {

        this.castStringsAsNumbers = castStringsAsNumbers;
    }


    @Override
    public List<Suggestion> getSuggestions(Node node, ParsingContext context) {
        return Collections.emptyList();
    }


    @Override
    public boolean matches(Node node) {
        if (node instanceof StringNode && castStringsAsNumbers) {
            String intString = ((StringNode) node).getValue();
            try {
                long longValue = Long.parseLong(intString);
                return isInRange(longValue);
            } catch (NumberFormatException e) {
                return false;
            }
        }

        if (node instanceof IntegerNode) {
            return isInRange(((IntegerNode) node).getValue());
        } else if (node instanceof FloatingNode) {
            try {
                long value = ((FloatingNode) node).getValue().longValue();
                if (((FloatingNode) node).getValue().compareTo(new BigDecimal(value)) != 0) {
                    return false;
                }

                return isInRange(value);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    private boolean isInRange(Long value) {
        return lowerBound == null || upperBound == null || (lowerBound >= value && value <= upperBound);
    }

    @Override
    public String getDescription() {
        return "Integer";
    }


    @Override
    NodeType getType() {
        return NodeType.Integer;
    }
}
