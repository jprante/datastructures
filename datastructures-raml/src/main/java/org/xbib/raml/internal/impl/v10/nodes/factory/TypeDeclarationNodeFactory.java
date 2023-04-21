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


import org.xbib.raml.internal.impl.commons.nodes.TypeDeclarationNode;
import org.xbib.raml.internal.impl.commons.nodes.TypeDeclarationNodeFragment;
import org.xbib.raml.internal.impl.commons.phase.RamlTypedFragment;
import org.xbib.raml.yagi.framework.grammar.rule.ClassNodeFactory;
import org.xbib.raml.yagi.framework.grammar.rule.NodeFactory;
import org.xbib.raml.yagi.framework.nodes.Node;

public class TypeDeclarationNodeFactory implements NodeFactory {
    @Override
    public Node create(Node currentNode, Object... args) {
        if (currentNode instanceof RamlTypedFragment)
            return new ClassNodeFactory(TypeDeclarationNodeFragment.class).create(currentNode, args);
        return new ClassNodeFactory(TypeDeclarationNode.class).create(currentNode, args);
    }
}
