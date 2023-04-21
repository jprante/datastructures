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

import java.util.ArrayList;
import java.util.List;
import org.xbib.raml.yagi.framework.nodes.Node;

public class ConditionalRule {
    private final Rule condition;
    private final List<KeyValueRule> rules;

    public ConditionalRule(Rule condition) {
        this.condition = condition;
        this.rules = new ArrayList<>();
    }

    public boolean matches(Node node) {
        return condition.matches(node);
    }

    public ConditionalRule add(KeyValueRule rule) {
        rules.add(rule);
        return this;
    }


    public List<KeyValueRule> getRules() {
        return rules;
    }
}
