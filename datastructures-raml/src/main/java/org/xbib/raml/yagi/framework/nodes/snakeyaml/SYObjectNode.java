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


import java.util.stream.Collectors;
import org.xbib.raml.api.loader.ResourceLoader;
import org.xbib.raml.yagi.framework.nodes.Node;
import org.xbib.raml.yagi.framework.nodes.NodeType;
import org.xbib.raml.yagi.framework.nodes.ObjectNode;
import org.yaml.snakeyaml.nodes.MappingNode;

public class SYObjectNode extends SYBaseRamlNode implements ObjectNode {

    public SYObjectNode(MappingNode mappingNode, ResourceLoader resourceLoader, String resourcePath) {
        super(mappingNode, resourcePath, resourceLoader);
    }

    // For copy reasons
    public SYObjectNode(SYBaseRamlNode baseRamlNode) {
        super(baseRamlNode);
    }

    @Override
    public String toString() {
        return "{\n" + getChildren().stream().map(Object::toString).collect(Collectors.joining("\n")) + "\n}";
    }


    @Override
    public Node copy() {
        return new SYObjectNode(this);
    }

    @Override
    public NodeType getType() {
        return NodeType.Object;
    }
}
