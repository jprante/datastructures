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
package org.xbib.raml.internal.impl.commons.model.parameter;

import org.xbib.raml.yagi.framework.model.AbstractNodeModel;
import org.xbib.raml.yagi.framework.nodes.KeyValueNode;
import org.xbib.raml.yagi.framework.nodes.Node;
import org.xbib.raml.yagi.framework.nodes.SimpleTypeNode;
import org.xbib.raml.yagi.framework.util.NodeSelector;

public class Parameter extends AbstractNodeModel<KeyValueNode> {
    public Parameter(KeyValueNode node) {
        super(node);
    }

    @Override
    public Node getNode() {
        return node.getValue();
    }

    public String name() {
        return ((SimpleTypeNode) node.getKey()).getLiteralValue();
    }

    public String defaultValue() {
        Object defaultValue = NodeSelector.selectType("default", getNode(), null);
        return defaultValue != null ? defaultValue.toString() : null;
    }

}
