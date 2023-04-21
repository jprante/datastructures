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
/*
 *
 */
package org.xbib.raml.internal.impl.commons.phase;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.xbib.raml.internal.impl.commons.nodes.BodyNode;
import org.xbib.raml.internal.impl.commons.nodes.OverridableNode;
import org.xbib.raml.internal.impl.commons.nodes.TypeDeclarationNode;
import org.xbib.raml.internal.impl.v10.grammar.Raml10Grammar;
import org.xbib.raml.internal.impl.v10.nodes.PropertyNode;
import org.xbib.raml.yagi.framework.grammar.rule.RegexValueRule;
import org.xbib.raml.yagi.framework.nodes.ArrayNode;
import org.xbib.raml.yagi.framework.nodes.ErrorNode;
import org.xbib.raml.yagi.framework.nodes.KeyValueNode;
import org.xbib.raml.yagi.framework.nodes.Node;
import org.xbib.raml.yagi.framework.nodes.NullNode;
import org.xbib.raml.yagi.framework.nodes.ObjectNode;
import org.xbib.raml.yagi.framework.nodes.SimpleTypeNode;
import org.xbib.raml.yagi.framework.util.NodeSelector;
import static org.xbib.raml.yagi.framework.nodes.DefaultPosition.isDefaultNode;

public class ResourceTypesTraitsMerger {


    static void merge(Node baseNode, Node copyNode) {
        if (copyNode instanceof NullNode) {
            // nothing to do here if copyNode is null
        } else if (baseNode instanceof ObjectNode && copyNode instanceof ObjectNode) {
            merge((ObjectNode) baseNode, (ObjectNode) copyNode);
        } else if (baseNode instanceof ArrayNode && copyNode instanceof ArrayNode) {
            merge((ArrayNode) baseNode, (ArrayNode) copyNode);
        } else if ((baseNode instanceof NullNode) || (copyNode instanceof ErrorNode)) {
            baseNode.replaceWith(copyNode);
        } else {
            throw new RuntimeException(String.format("Merging not supported for nodes of type %s and %s",
                    baseNode.getClass().getSimpleName(), copyNode.getClass().getSimpleName()));
        }
    }

    static void merge(ArrayNode baseNode, ArrayNode copyNode) {
        for (Node child : copyNode.getChildren()) {
            baseNode.addChild(child);
        }
    }

    static void merge(ObjectNode baseNode, ObjectNode copyNode) {
        for (final Node child : copyNode.getChildren()) {
            if (child instanceof ErrorNode) {
                baseNode.addChild(child);
                continue;
            }
            if (!(child instanceof KeyValueNode)) {
                throw new RuntimeException("Only expecting KeyValueNode and got " + child.getClass());
            }

            String key = ((KeyValueNode) child).getKey().toString();
            if (shouldIgnoreKey((KeyValueNode) child)) {
                continue;
            }

            boolean optional = key.endsWith("?");
            if (optional) {
                key = key.substring(0, key.length() - 1);
            }
            Node node = NodeSelector.selectFrom(NodeSelector.encodePath(key), baseNode);
            Node childValue = ((KeyValueNode) child).getValue();

            if (node == null) {
                // if merging children of body node, media type is defined under baseNode and child is not a mime type node,
                // child gets merge with the value of mediaType node. See #498
                RegexValueRule mimeTypeRegex = new Raml10Grammar().mimeTypeRegex();
                boolean isMimeType = mimeTypeRegex.matches(((KeyValueNode) child).getKey());
                if (baseNode.getParent() instanceof BodyNode && !isMimeType) {
                    if (baseNode.getChildren().size() > 0 && baseNode.getChildren().get(0) instanceof KeyValueNode mimeTypeNode) {
                        if (mimeTypeRegex.matches(mimeTypeNode.getKey())) {
                            merge(mimeTypeNode.getValue(), copyNode);
                        }
                    }
                } else if (baseNode.getParent() instanceof BodyNode && baseNode instanceof TypeDeclarationNode && isMimeType) {
                    merge(baseNode, ((KeyValueNode) child).getValue());
                } else {
                    if (child instanceof PropertyNode) {

                        Optional<PropertyNode> foundBasenodeChild = findPropertyNode(baseNode, (PropertyNode) child);

                        if (!foundBasenodeChild.isPresent()) {

                            baseNode.addChild(child);
                        } else {

                            merge(foundBasenodeChild.get().getValue(), ((PropertyNode) child).getValue());
                        }

                    } else {

                        baseNode.addChild(child);
                    }
                }
            } else if (childValue instanceof SimpleTypeNode) {
                if (isDefaultNode(node) && !isDefaultNode(childValue)) {
                    node.getParent().setChild(1, childValue);
                } else if (node instanceof OverridableNode) {
                    node.getParent().setChild(1, childValue);
                }
            } else {
                if (node instanceof SimpleTypeNode) {
                    merge(baseNode, childValue);
                } else {
                    merge(node, childValue);
                }
            }
        }
    }

    private static Optional<PropertyNode> findPropertyNode(ObjectNode baseNode, final PropertyNode child) {
        return baseNode.getChildren().stream()
                .filter(input -> input instanceof PropertyNode)
                .map(input -> ((PropertyNode) input))
                .filter(input -> nonOptionalName(input.getName()).equals(nonOptionalName(child.getName())))
                .findFirst();
    }

    private static String nonOptionalName(String name) {

        if (name.endsWith("?")) {
            return name.substring(0, name.length() - 1);
        } else {
            return name;
        }
    }

    private static boolean shouldIgnoreKey(KeyValueNode child) {
        Set<String> ignoreSet = new HashSet<>();
        ignoreSet.add("usage");
        if (!(child.getParent() instanceof TypeDeclarationNode)) {
            ignoreSet.add("type");
        }
        String key = child.getKey().toString();
        return ignoreSet.contains(key);
    }
}
