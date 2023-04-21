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
package org.xbib.raml.internal.impl.commons.phase;

import java.util.Collection;
import java.util.HashSet;
import org.xbib.raml.internal.impl.commons.nodes.ResourceNode;
import org.xbib.raml.yagi.framework.grammar.rule.ErrorNodeFactory;
import org.xbib.raml.yagi.framework.nodes.Node;
import org.xbib.raml.yagi.framework.phase.Transformer;

public class DuplicatedPathsTransformer implements Transformer {
    private final Collection<String> paths = new HashSet<String>();

    @Override
    public boolean matches(Node node) {
        return node instanceof ResourceNode;
    }

    @Override
    public Node transform(Node node) {
        String currentPath = ((ResourceNode) node).getResourcePath();
        if (!paths.add(currentPath)) {
            return ErrorNodeFactory.createDuplicatedPathNode(currentPath);
        }

        return node;
    }
}
