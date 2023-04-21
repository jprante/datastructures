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

import com.fasterxml.jackson.databind.node.TextNode;
import org.xbib.raml.api.loader.ResourceLoader;
import org.xbib.raml.yagi.framework.nodes.Node;
import org.xbib.raml.yagi.framework.nodes.NodeType;
import org.xbib.raml.yagi.framework.nodes.StringNode;

public class JStringNode extends JBaseRamlNode implements StringNode {

    public JStringNode(TextNode textNode, String resourcePath, ResourceLoader resourceLoader) {
        super(textNode, resourcePath, resourceLoader);
    }

    protected JStringNode(JStringNode node) {
        super(node);
    }

    @Override
    public String getValue() {
        return getJsonNode().textValue();
    }


    @Override
    public Node copy() {
        return new JStringNode(this);
    }

    @Override
    public String toString() {
        return getValue();
    }

    @Override
    public NodeType getType() {
        return NodeType.String;
    }
}
