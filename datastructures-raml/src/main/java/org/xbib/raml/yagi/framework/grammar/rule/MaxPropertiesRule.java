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
import org.xbib.raml.yagi.framework.suggester.ParsingContext;
import org.xbib.raml.yagi.framework.suggester.Suggestion;

public class MaxPropertiesRule extends Rule {
    private final int maxProperties;

    public MaxPropertiesRule(int maxProperties) {
        this.maxProperties = maxProperties;
    }

    @Override
    public boolean matches(Node node) {
        return node.getChildren().size() <= maxProperties;
    }


    @Override
    public Node apply(Node node) {
        if (matches(node)) {
            return createNodeUsingFactory(node);
        } else {
            return ErrorNodeFactory.createInvalidMaxProperties(maxProperties);
        }
    }

    @Override
    public String getDescription() {
        return "Max amount of properties " + maxProperties;
    }


    @Override
    public List<Suggestion> getSuggestions(Node node, ParsingContext context) {
        return Collections.emptyList();
    }
}
