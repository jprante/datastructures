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


import java.util.stream.Collectors;
import org.xbib.raml.api.loader.ResourceLoader;
import org.xbib.raml.yagi.framework.nodes.Node;
import org.xbib.raml.yagi.framework.nodes.NodeType;
import org.xbib.raml.yagi.framework.nodes.ObjectNode;

public class JObjectNode extends JBaseRamlNode implements ObjectNode {

    public JObjectNode(com.fasterxml.jackson.databind.node.ObjectNode objectNode, String resourcePath, ResourceLoader resourceLoader) {
        super(objectNode, resourcePath, resourceLoader);
    }

    protected JObjectNode(JObjectNode node) {
        super(node);
    }

    @Override
    public String toString() {
        return "{\n" + getChildren().stream().map(Object::toString).collect(Collectors.joining("\n")) + "\n}";
    }


    @Override
    public Node copy() {
        return new JObjectNode(this);
    }

    @Override
    public NodeType getType() {
        return NodeType.Object;
    }
}
