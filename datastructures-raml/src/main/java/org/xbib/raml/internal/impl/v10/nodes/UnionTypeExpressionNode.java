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
package org.xbib.raml.internal.impl.v10.nodes;

import java.util.ArrayList;
import java.util.List;
import org.xbib.raml.internal.impl.commons.nodes.TypeExpressionNode;
import org.xbib.raml.internal.impl.commons.type.ResolvedCustomFacets;
import org.xbib.raml.internal.impl.commons.type.ResolvedType;
import org.xbib.raml.internal.impl.v10.type.UnionResolvedType;
import org.xbib.raml.yagi.framework.nodes.AbstractRamlNode;
import org.xbib.raml.yagi.framework.nodes.Node;
import org.xbib.raml.yagi.framework.nodes.NodeType;
import org.xbib.raml.yagi.framework.nodes.Position;
import org.xbib.raml.yagi.framework.nodes.SimpleTypeNode;
import static org.xbib.raml.internal.impl.v10.nodes.ArrayTypeExpressionNode.addParenthesesIfNeeded;

public class UnionTypeExpressionNode extends AbstractRamlNode implements TypeExpressionNode, SimpleTypeNode<String> {

    public UnionTypeExpressionNode() {
    }

    private UnionTypeExpressionNode(UnionTypeExpressionNode unionTypeTypeNode) {
        super(unionTypeTypeNode);
    }


    @Override
    public Position getStartPosition() {
        return getChildren().get(0).getStartPosition();
    }


    @Override
    public Position getEndPosition() {
        return getChildren().get(getChildren().size() - 1).getEndPosition();
    }


    @Override
    public Node copy() {
        return new UnionTypeExpressionNode(this);
    }

    public List<TypeExpressionNode> of() {
        final List<TypeExpressionNode> of = new ArrayList<>();
        for (Node node : getChildren()) {
            if (node instanceof TypeExpressionNode) {
                of.add((TypeExpressionNode) node);
            }
        }
        return of;
    }

    @Override
    public NodeType getType() {
        return NodeType.String;
    }


    @Override
    public ResolvedType generateDefinition() {
        final List<TypeExpressionNode> of = of();
        List<ResolvedType> definitions = new ArrayList<>();
        ResolvedCustomFacets customFacets = new ResolvedCustomFacets();
        for (TypeExpressionNode typeExpressionNode : of) {
            final ResolvedType resolvedType = typeExpressionNode.generateDefinition();
            if (resolvedType != null) {
                definitions.add(resolvedType);
                customFacets = customFacets.mergeWith(resolvedType.customFacets());
            }
        }

        return new UnionResolvedType(this, definitions, customFacets);
    }

    @Override
    public String getTypeExpressionText() {
        return getValue();
    }

    @Override
    public String getValue() {
        String unionOperator = " | ";
        StringBuilder result = new StringBuilder();
        for (TypeExpressionNode typeExpressionNode : of()) {
            String typeExpression = typeExpressionNode.getTypeExpressionText();
            if (typeExpression != null) {
                result.append(addParenthesesIfNeeded(typeExpression)).append(unionOperator);
            } else {
                return null;
            }
        }
        return result.delete(result.length() - unionOperator.length(), result.length()).toString();
    }

    @Override
    public String getLiteralValue() {
        return getValue();
    }
}
