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

import java.util.Arrays;
import java.util.List;
import org.xbib.raml.yagi.framework.nodes.BooleanNode;
import org.xbib.raml.yagi.framework.nodes.Node;
import org.xbib.raml.yagi.framework.nodes.NodeType;
import org.xbib.raml.yagi.framework.nodes.StringNode;
import org.xbib.raml.yagi.framework.suggester.DefaultSuggestion;
import org.xbib.raml.yagi.framework.suggester.ParsingContext;
import org.xbib.raml.yagi.framework.suggester.Suggestion;

public class BooleanTypeRule extends AbstractTypeRule {
    public static final String STRICT_BOOLEANS = "org.raml.strict_booleans";

    private final static String TRUE = "true";
    private final static String FALSE = "false";

    private final boolean strictBoolean;

    public BooleanTypeRule() {
        this.strictBoolean = Boolean.parseBoolean(System.getProperty(STRICT_BOOLEANS, "false"));
    }

    BooleanTypeRule(boolean strictBoolean) {
        this.strictBoolean = strictBoolean;
    }


    @Override
    public List<Suggestion> getSuggestions(Node node, ParsingContext context) {
        return Arrays.asList(new DefaultSuggestion("true", "Boolean true", "true"), new DefaultSuggestion("false", "Boolean false", "false"));
    }

    @Override
    public boolean matches(Node node) {
        if (!strictBoolean && (node instanceof StringNode)) {
            String value = ((StringNode) node).getValue();
            return TRUE.equals(value) || FALSE.equals(value);
        }
        return node instanceof BooleanNode;
    }


    @Override
    public String getDescription() {
        return "Boolean";
    }


    @Override
    NodeType getType() {
        return NodeType.Boolean;
    }
}
