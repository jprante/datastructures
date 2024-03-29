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
import org.xbib.raml.yagi.framework.nodes.NodeType;
import org.xbib.raml.yagi.framework.nodes.NullNode;
import org.yaml.snakeyaml.nodes.Node;

public class SYNullNode extends SYBaseRamlNode implements NullNode {
    public SYNullNode(SYNullNode node) {
        super(node);
    }

    public SYNullNode(Node yamlNode, String resourcePath, ResourceLoader resourceLoader) {
        super(yamlNode, resourcePath, resourceLoader);
    }


    @Override
    public org.xbib.raml.yagi.framework.nodes.Node copy() {
        return new SYNullNode(this);
    }

    @Override
    public NodeType getType() {
        return NodeType.Null;
    }

    @Override
    public String toString() {
        return "null";
    }
}
