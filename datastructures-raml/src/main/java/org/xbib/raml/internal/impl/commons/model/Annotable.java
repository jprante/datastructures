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
package org.xbib.raml.internal.impl.commons.model;

import java.util.ArrayList;
import java.util.List;
import org.xbib.raml.internal.impl.commons.nodes.AnnotationNode;
import org.xbib.raml.yagi.framework.model.AbstractNodeModel;
import org.xbib.raml.yagi.framework.nodes.Node;

public abstract class Annotable<T extends Node> extends AbstractNodeModel<T> {
    public Annotable(T node) {
        super(node);
    }

    public List<AnnotationRef> annotations() {
        List<AnnotationRef> result = new ArrayList<>();
        for (Node child : getNode().getChildren()) {
            if (child instanceof AnnotationNode) {
                result.add(new AnnotationRef((AnnotationNode) child));
            }
        }
        return result;
    }
}