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
package org.xbib.raml.yagi.framework.nodes.jackson;


import java.util.List;
import java.util.stream.Collectors;
import org.xbib.raml.api.loader.ResourceLoader;
import org.xbib.raml.yagi.framework.nodes.ArrayNode;
import org.xbib.raml.yagi.framework.nodes.Node;
import org.xbib.raml.yagi.framework.nodes.NodeType;

public class JArrayNode extends JBaseRamlNode implements ArrayNode {

    public JArrayNode(com.fasterxml.jackson.databind.node.ArrayNode arrayNode, String resourcePath, ResourceLoader resourceLoader) {
        super(arrayNode, resourcePath, resourceLoader);
    }

    private JArrayNode(JArrayNode node) {
        super(node);
    }

    @Override
    public String toString() {
        final List<Node> children = getChildren();
        final String join = children.stream().map(Object::toString).collect(Collectors.joining(", "));
        return "Array[" + join + "]";
    }


    @Override
    public Node copy() {
        return new JArrayNode(this);
    }

    @Override
    public NodeType getType() {
        return NodeType.Array;
    }

    @Override
    public boolean isJsonStyle() {
        return true;
    }
}
