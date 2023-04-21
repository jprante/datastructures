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

package org.xbib.raml.internal.impl.v10.rules;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import org.xbib.raml.internal.impl.commons.rule.RamlErrorNodeFactory;
import org.xbib.raml.yagi.framework.grammar.rule.Rule;
import org.xbib.raml.yagi.framework.nodes.FloatingNode;
import org.xbib.raml.yagi.framework.nodes.Node;
import org.xbib.raml.yagi.framework.nodes.SimpleTypeNode;
import org.xbib.raml.yagi.framework.suggester.ParsingContext;
import org.xbib.raml.yagi.framework.suggester.Suggestion;

public class FormatValueRule extends Rule {
    private final String format;

    public FormatValueRule(String format) {
        this.format = format;
    }

    @Override
    public boolean matches(Node node) {
        if (node instanceof FloatingNode floatingNode && (format.equals("long") || format.startsWith("int"))) {
            BigDecimal value = floatingNode.getValue();
            BigDecimal roundedValue = floatingNode.getValue().setScale(0, RoundingMode.DOWN);
            return roundedValue.compareTo(value) == 0;
        }

        return true;
    }


    @Override
    public Node apply(Node node) {
        if (matches(node)) {
            return createNodeUsingFactory(node, ((SimpleTypeNode) node).getValue());
        } else {
            return RamlErrorNodeFactory.createInvalidFormatValue(
                    ((SimpleTypeNode) node).getValue().toString(),
                    format);
        }
    }

    @Override
    public String getDescription() {
        return "Format of value";
    }


    @Override
    public List<Suggestion> getSuggestions(Node node, ParsingContext context) {
        return Collections.emptyList();
    }
}
