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

import java.util.List;
import org.xbib.raml.yagi.framework.grammar.rule.Rule;
import org.xbib.raml.yagi.framework.nodes.Node;
import org.xbib.raml.yagi.framework.util.NodeUtils;

public class TypesUtils {

    public static void validateAllWith(Rule rule, List<? extends Node> nodes) {
        for (Node facet : nodes) {
            if (!NodeUtils.isErrorResult(facet)) {
                final Node apply = rule.apply(facet);
                facet.replaceWith(apply);
            }
        }
    }
}