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
import org.xbib.raml.yagi.framework.nodes.Node;
import org.xbib.raml.yagi.framework.nodes.StringNode;
import org.xbib.raml.yagi.framework.suggester.ParsingContext;
import org.xbib.raml.yagi.framework.suggester.Suggestion;
import org.xbib.raml.yagi.framework.util.DateType;
import org.xbib.raml.yagi.framework.util.DateUtils;

public class DateValueRule extends Rule {

    private final DateType dateType;
    private String rfc = "rfc3339";
    private final DateUtils dateUtils = DateUtils.createFromProperties();

    public DateValueRule(DateType dateType, String rfc) {
        this.dateType = dateType;
        if (rfc != null) {
            this.rfc = rfc;
        }
    }


    @Override
    public List<Suggestion> getSuggestions(Node node, ParsingContext context) {
        return Collections.emptyList();
    }

    @Override
    public boolean matches(Node node) {
        return node instanceof StringNode && dateUtils.isValidDate(((StringNode) node).getValue(), this.dateType, this.rfc);
    }

    @Override
    public Node apply(Node node) {
        if (matches(node)) {
            if (dateUtils.isValidDate(((StringNode) node).getValue(), this.dateType, this.rfc)) {
                return node;
            } else {
                return ErrorNodeFactory.createInvalidDateValue((StringNode) node, this.dateType.name(), this.rfc);
            }
        } else {
            if (node instanceof StringNode) {
                return ErrorNodeFactory.createInvalidDateValue((StringNode) node, this.dateType.name(), this.rfc);
            }
            return ErrorNodeFactory.createInvalidNode(node);
        }
    }

    @Override
    public String getDescription() {
        return "Multiple of value";
    }
}
