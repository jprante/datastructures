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


import org.xbib.raml.api.model.v10.RamlFragment;
import org.xbib.raml.internal.impl.commons.phase.RamlTypedFragment;
import org.xbib.raml.internal.impl.v10.grammar.Raml10Grammar;
import org.xbib.raml.yagi.framework.nodes.AbstractObjectNode;
import org.xbib.raml.yagi.framework.nodes.Node;
import org.xbib.raml.yagi.framework.util.NodeSelector;

public class DefaultRamlTypedFragment extends AbstractObjectNode implements RamlTypedFragment {

    private final RamlFragment fragment;
    private Node libraryNode;

    public DefaultRamlTypedFragment(RamlFragment fragment) {
        this.fragment = fragment;
    }

    protected DefaultRamlTypedFragment(DefaultRamlTypedFragment node) {
        super(node);
        this.fragment = node.getFragment();
        this.libraryNode = node.getLibraryNode();
    }


    @Override
    public Node copy() {
        return new DefaultRamlTypedFragment(this);
    }


    @Override
    public Node getContextNode() {
        return getLibraryNode() != null || getParent() == null ? this : org.xbib.raml.yagi.framework.util.NodeUtils.getContextNode(getParent());
    }

    @Override
    public Node getLibraryNode() {
        return libraryNode;
    }

    public void resolveLibraryReference() {
        if (libraryNode == null) {
            libraryNode = NodeSelector.selectFrom(Raml10Grammar.USES_KEY_NAME, this);
            if (libraryNode != null) {
                // The parent is the key value pair
                final Node parent = libraryNode.getParent();
                removeChild(parent);
            }
        }
    }

    public RamlFragment getFragment() {
        return fragment;
    }
}
