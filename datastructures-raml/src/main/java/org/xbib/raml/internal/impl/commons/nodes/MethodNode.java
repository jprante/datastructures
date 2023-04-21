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
package org.xbib.raml.internal.impl.commons.nodes;


import org.xbib.raml.yagi.framework.nodes.KeyValueNodeImpl;
import org.xbib.raml.yagi.framework.nodes.Node;
import org.xbib.raml.yagi.framework.nodes.StringNode;

public class MethodNode extends KeyValueNodeImpl {

    public MethodNode() {
    }

    public MethodNode(MethodNode node) {
        super(node);
    }

    public String getName() {
        Node key = getKey();
        if (key instanceof StringNode) {
            return ((StringNode) key).getValue();
        } else {
            throw new IllegalStateException("Key must be a string but was a " + key.getClass());
        }
    }


    @Override
    public Node copy() {
        return new MethodNode(this);
    }
}
