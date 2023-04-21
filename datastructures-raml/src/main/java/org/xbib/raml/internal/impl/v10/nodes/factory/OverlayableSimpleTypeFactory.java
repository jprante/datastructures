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
package org.xbib.raml.internal.impl.v10.nodes.factory;


import org.xbib.raml.internal.impl.commons.nodes.OverlayableNode;
import org.xbib.raml.internal.impl.commons.nodes.OverlayableObjectNodeImpl;
import org.xbib.raml.internal.impl.commons.nodes.OverlayableStringNode;
import org.xbib.raml.internal.impl.commons.nodes.StringTemplateNode;
import org.xbib.raml.yagi.framework.grammar.rule.NodeFactory;
import org.xbib.raml.yagi.framework.nodes.AbstractObjectNode;
import org.xbib.raml.yagi.framework.nodes.KeyValueNodeImpl;
import org.xbib.raml.yagi.framework.nodes.Node;
import org.xbib.raml.yagi.framework.nodes.SimpleTypeNode;
import org.xbib.raml.yagi.framework.nodes.StringNodeImpl;


public class OverlayableSimpleTypeFactory implements NodeFactory {

    private final Boolean wrappInObject;

    public OverlayableSimpleTypeFactory(Boolean wrappInObject) {
        this.wrappInObject = wrappInObject;
    }

    @Override
    public Node create(Node currentNode, Object... args) {
        // If it is a template expression node we shouldn't apply this
        if (currentNode instanceof StringTemplateNode) {
            return currentNode;
        }

        if (!(currentNode instanceof SimpleTypeNode)) {
            throw new IllegalStateException("Factory incompatible with node of type " + currentNode.getClass().getSimpleName());
        }
        if (currentNode instanceof OverlayableNode) {
            return currentNode;
        }
        if (wrappInObject) {
            AbstractObjectNode result = new OverlayableObjectNodeImpl();
            OverlayableStringNode overlayableStringNode = new OverlayableStringNode(((SimpleTypeNode) currentNode).getLiteralValue());
            overlayableStringNode.setStartPosition(currentNode.getStartPosition());
            overlayableStringNode.setEndPosition(currentNode.getEndPosition());
            KeyValueNodeImpl keyValueNode = new KeyValueNodeImpl(new StringNodeImpl("value"), overlayableStringNode);
            if (currentNode.getParent() != null) {
                keyValueNode.setStartPosition(currentNode.getParent().getStartPosition());
                keyValueNode.setEndPosition(currentNode.getParent().getEndPosition());
            }
            result.addChild(keyValueNode);
            return result;
        } else {
            return new OverlayableStringNode(((SimpleTypeNode) currentNode).getLiteralValue());
        }
    }
}
