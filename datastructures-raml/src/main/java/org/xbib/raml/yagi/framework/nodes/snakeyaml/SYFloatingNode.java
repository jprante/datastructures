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

import java.math.BigDecimal;
import org.xbib.raml.api.loader.ResourceLoader;
import org.xbib.raml.yagi.framework.nodes.FloatingNode;
import org.xbib.raml.yagi.framework.nodes.Node;
import org.xbib.raml.yagi.framework.nodes.NodeType;
import org.yaml.snakeyaml.nodes.ScalarNode;

public class SYFloatingNode extends SYBaseRamlNode implements FloatingNode {
    public SYFloatingNode(SYFloatingNode node) {
        super(node);
    }

    public SYFloatingNode(ScalarNode yamlNode, String resourcePath, ResourceLoader resourceLoader) {
        super(yamlNode, resourcePath, resourceLoader);
    }

    @Override
    public BigDecimal getValue() {
        final String value = ((ScalarNode) getYamlNode()).getValue();
        return new BigDecimal(value);
    }

    @Override
    public String toString() {
        return ((ScalarNode) getYamlNode()).getValue();
    }


    @Override
    public Node copy() {
        return new SYFloatingNode(this);
    }

    @Override
    public NodeType getType() {
        return NodeType.Float;
    }
}
