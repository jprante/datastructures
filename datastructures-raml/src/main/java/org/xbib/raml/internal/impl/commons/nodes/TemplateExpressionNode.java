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
package org.xbib.raml.internal.impl.commons.nodes;

import java.util.StringTokenizer;
import org.xbib.raml.yagi.framework.grammar.rule.ErrorNodeFactory;
import org.xbib.raml.yagi.framework.nodes.AbstractStringNode;
import org.xbib.raml.yagi.framework.nodes.ExecutableNode;
import org.xbib.raml.yagi.framework.nodes.ExecutionContext;
import org.xbib.raml.yagi.framework.nodes.Node;
import org.xbib.raml.yagi.framework.nodes.NodeType;
import org.xbib.raml.yagi.framework.nodes.SimpleTypeNode;

public class TemplateExpressionNode extends AbstractStringNode implements ExecutableNode {
    public TemplateExpressionNode(String value) {
        super(value);
    }

    public TemplateExpressionNode(TemplateExpressionNode node) {
        super(node);
    }


    public String getVariableName() {
        final StringTokenizer expressionTokens = getExpressionTokens();
        return expressionTokens.hasMoreTokens() ? expressionTokens.nextToken() : null;
    }

    public Node execute(ExecutionContext context) {
        final StringTokenizer expressionTokens = getExpressionTokens();
        Node result = null;
        if (expressionTokens.hasMoreTokens()) {
            final String token = expressionTokens.nextToken().trim();
            if (context.containsVariable(token)) {
                result = context.getVariable(token).copy();
            } else {
                return ErrorNodeFactory.createInvalidTemplateParameterExpression(this, token);
            }
        }
        while (expressionTokens.hasMoreTokens()) {
            if (!(result instanceof SimpleTypeNode)) {
                return ErrorNodeFactory.createInvalidType(result, NodeType.String);
            }
            final String token = expressionTokens.nextToken().trim();
            if (token.startsWith("!")) {
                /*try
                {
                    Method method = Inflector.class.getMethod(token.substring(1), String.class);
                    result = new StringNodeImpl(String.valueOf(method.invoke(null, ((SimpleTypeNode) result).getLiteralValue())));
                }
                catch (Exception e)
                {
                    return ErrorNodeFactory.createInvalidTemplateFunctionExpression(this, token);
                }*/
            } else {
                return ErrorNodeFactory.createInvalidTemplateFunctionExpression(this, token);
            }
        }

        return result;

    }

    private StringTokenizer getExpressionTokens() {
        final String value = getValue();
        return new StringTokenizer(value, "|");
    }


    @Override
    public Node copy() {
        return new TemplateExpressionNode(this);
    }
}
