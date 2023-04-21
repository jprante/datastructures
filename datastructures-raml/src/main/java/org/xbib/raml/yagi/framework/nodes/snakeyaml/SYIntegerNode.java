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
package org.xbib.raml.yagi.framework.nodes.snakeyaml;


import org.xbib.raml.api.loader.ResourceLoader;
import org.xbib.raml.yagi.framework.nodes.IntegerNode;
import org.xbib.raml.yagi.framework.nodes.Node;
import org.xbib.raml.yagi.framework.nodes.NodeType;
import org.yaml.snakeyaml.nodes.ScalarNode;

public class SYIntegerNode extends SYBaseRamlNode implements IntegerNode {

    // For copy
    private SYIntegerNode(SYIntegerNode node) {
        super(node);
    }

    public SYIntegerNode(ScalarNode scalarNode, String resourcePath, ResourceLoader resourceLoader) {
        super(scalarNode, resourcePath, resourceLoader);
    }

    public Long getValue() {
        final String value = ((ScalarNode) getYamlNode()).getValue();
        if (value != null && (value.startsWith("0x") || value.startsWith("0X"))) {
            return Long.valueOf(value.substring(2), 16);
        }
        return Long.valueOf(value);
    }


    @Override
    public Node copy() {
        return new SYIntegerNode(this);
    }

    @Override
    public NodeType getType() {
        return NodeType.Integer;
    }
}
