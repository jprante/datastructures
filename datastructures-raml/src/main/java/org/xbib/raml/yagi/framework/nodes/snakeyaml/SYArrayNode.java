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

import java.util.List;
import java.util.stream.Collectors;
import org.xbib.raml.api.loader.ResourceLoader;
import org.xbib.raml.yagi.framework.nodes.ArrayNode;
import org.xbib.raml.yagi.framework.nodes.Node;
import org.xbib.raml.yagi.framework.nodes.NodeType;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.nodes.SequenceNode;

public class SYArrayNode extends SYBaseRamlNode implements ArrayNode {
    // For copy
    private SYArrayNode(SYArrayNode node) {
        super(node);
    }

    public SYArrayNode(SequenceNode sequenceNode, String resourcePath, ResourceLoader resourceLoader) {
        super(sequenceNode, resourcePath, resourceLoader);
    }

    @Override
    public String toString() {

        final List<Node> children = getChildren();
        final String join = children.stream().map(Object::toString).collect(Collectors.joining(", "));
        return "Array[" + join + "]";
    }


    @Override
    public Node copy() {
        return new SYArrayNode(this);
    }

    @Override
    public NodeType getType() {
        return NodeType.Array;
    }


    @Override
    public boolean isJsonStyle() {
        return ((SequenceNode) getYamlNode()).getFlowStyle() != DumperOptions.FlowStyle.BLOCK;
    }
}
