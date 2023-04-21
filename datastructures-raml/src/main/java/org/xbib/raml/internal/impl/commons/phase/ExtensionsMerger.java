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

import org.xbib.raml.internal.impl.commons.nodes.AnnotationNode;
import org.xbib.raml.internal.impl.commons.nodes.AnnotationReferenceNode;
import org.xbib.raml.internal.impl.commons.nodes.DocumentationItemNode;
import org.xbib.raml.internal.impl.commons.nodes.ExampleDeclarationNode;
import org.xbib.raml.internal.impl.commons.nodes.ExtendsNode;
import org.xbib.raml.internal.impl.commons.nodes.OverlayableNode;
import org.xbib.raml.internal.impl.commons.nodes.RamlDocumentNode;
import org.xbib.raml.internal.impl.v10.nodes.LibraryLinkNode;
import org.xbib.raml.yagi.framework.grammar.rule.ErrorNodeFactory;
import org.xbib.raml.yagi.framework.nodes.ArrayNode;
import org.xbib.raml.yagi.framework.nodes.KeyValueNode;
import org.xbib.raml.yagi.framework.nodes.Node;
import org.xbib.raml.yagi.framework.nodes.NullNode;
import org.xbib.raml.yagi.framework.nodes.ObjectNode;
import org.xbib.raml.yagi.framework.nodes.SimpleTypeNode;
import org.xbib.raml.yagi.framework.util.NodeSelector;
import static org.xbib.raml.yagi.framework.nodes.DefaultPosition.isDefaultNode;

public class ExtensionsMerger {

    private final boolean overlay;

    public ExtensionsMerger(boolean overlay) {
        this.overlay = overlay;
    }

    public void merge(Node baseNode, Node copyNode) {
        if (baseNode instanceof ObjectNode && copyNode instanceof ObjectNode) {
            merge((ObjectNode) baseNode, (ObjectNode) copyNode);
        } else if (baseNode instanceof ArrayNode && copyNode instanceof ArrayNode) {
            merge((ArrayNode) baseNode, (ArrayNode) copyNode);
        } else {
            throw new RuntimeException(String.format("Merging not supported for nodes of type %s and %s",
                    baseNode.getClass().getSimpleName(), copyNode.getClass().getSimpleName()));
        }
    }

    private void merge(ArrayNode baseNode, ArrayNode copyNode) {
        for (Node child : copyNode.getChildren()) {
            baseNode.addChild(child);
        }
    }

    private void merge(ObjectNode baseNode, ObjectNode copyNode) {
        for (Node child : copyNode.getChildren()) {
            if (!(child instanceof KeyValueNode)) {
                throw new RuntimeException("only expecting KeyValueNode");
            }

            Node keyNode = ((KeyValueNode) child).getKey();
            String key = keyNode.toString();

            if (shouldIgnoreNode(child)) {
                continue;
            }

            Node valueNode = ((KeyValueNode) child).getValue();
            Node node = NodeSelector.selectFrom(NodeSelector.encodePath(key), baseNode);
            if (node == null) {
                overlayCheck(valueNode, valueNode);
                baseNode.addChild(child);
            } else if (valueNode instanceof NullNode) {
            } else if (valueNode instanceof LibraryLinkNode) {
            } else if (child instanceof AnnotationNode) {
                ((KeyValueNode) node.getParent()).setValue(valueNode);
            } else if (child instanceof ExampleDeclarationNode) {
                ((KeyValueNode) node.getParent()).setValue(valueNode);
            } else {
                if (isDefaultNode(child)) {
                    continue;
                }
                if (valueNode instanceof SimpleTypeNode) {
                    if (overlayCheck(node, valueNode)) {
                        node.replaceWith(valueNode);
                    }
                } else {
                    merge(node, valueNode);
                }
            }
        }
    }

    private boolean overlayCheck(Node baseNode, Node overlayNode) {
        boolean check = true;
        if (overlay) {
            if (!isOverlayableNode(overlayNode)) {
                baseNode.replaceWith(ErrorNodeFactory.createInvalidOverlayNode(overlayNode));
                check = false;
            }
        }
        return check;
    }

    private boolean isOverlayableNode(Node overlayNode) {
        Node parent = overlayNode.getParent();
        if ((overlayNode instanceof OverlayableNode) || (parent instanceof OverlayableNode)) {
            return true;
        }
        // annotations
        if (parent instanceof KeyValueNode && ((KeyValueNode) parent).getKey() instanceof AnnotationReferenceNode) {
            return true;
        }
        // libraries
        if (isLibraryNode(overlayNode)) {
            return true;
        }
        // documentation
        return !overlayNode.getChildren().isEmpty() && (overlayNode.getChildren().get(0) instanceof DocumentationItemNode);
    }

    private boolean isLibraryNode(Node overlayNode) {
        boolean isLibrary = false;
        if (overlayNode instanceof LibraryLinkNode) {
            isLibrary = true;
        } else if (overlayNode.getParent() instanceof KeyValueNode) {
            isLibrary = isNode(overlayNode.getParent(), "uses");
        }
        return isLibrary;
    }

    private boolean shouldIgnoreNode(Node node) {
        if (node instanceof ExtendsNode) {
            return true;
        }
        return isNode(node, "usage");
    }

    private boolean isNode(Node node, String keyName) {
        Node keyNode = ((KeyValueNode) node).getKey();
        String key = ((SimpleTypeNode) keyNode).getLiteralValue();
        return keyName.equals(key) && (node.getParent() instanceof RamlDocumentNode);
    }

}
