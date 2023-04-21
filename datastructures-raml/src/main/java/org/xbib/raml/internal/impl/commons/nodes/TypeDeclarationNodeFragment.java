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
import org.xbib.raml.internal.utils.RamlNodeUtils;
import org.xbib.raml.yagi.framework.nodes.Node;
import org.xbib.raml.yagi.framework.util.NodeSelector;
import org.xbib.raml.yagi.framework.util.NodeUtils;
import static org.xbib.raml.api.model.v10.RamlFragment.DataType;


public class TypeDeclarationNodeFragment extends TypeDeclarationNode implements RamlTypedFragment {

    private Node libraryNode;


    @Override
    public Node getContextNode() {
        return getLibraryNode() != null || getParent() == null ? this : NodeUtils.getContextNode(getParent());
    }

    @Override
    public Node getLibraryNode() {
        if (libraryNode == null) {
            libraryNode = NodeSelector.selectFrom(Raml10Grammar.USES_KEY_NAME, this);
        }
        return libraryNode;
    }

    public void
    resolveLibraryReference() {
        libraryNode = NodeSelector.selectFrom(Raml10Grammar.USES_KEY_NAME, this);
        if (libraryNode != null && !RamlNodeUtils.isErrorResult(libraryNode)) {
            // The parent is the key value pair
            final Node parent = libraryNode.getParent();
            removeChild(parent);
        }
    }

    @Override
    public RamlFragment getFragment() {
        return DataType;
    }
}
