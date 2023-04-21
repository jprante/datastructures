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
import org.xbib.raml.internal.impl.commons.nodes.RamlDocumentNode;
import org.xbib.raml.internal.impl.commons.nodes.ResourceNode;
import org.xbib.raml.yagi.framework.nodes.Node;
import static org.xbib.raml.internal.utils.RamlNodeUtils.getVersion;

public class Api extends Annotable<RamlDocumentNode> {
    public Api(RamlDocumentNode delegateNode) {
        super(delegateNode);
    }

    @Override
    public Node getNode() {
        return node;
    }


    public List<Resource> resources() {
        ArrayList<Resource> resultList = new ArrayList<>();
        for (Node item : node.getChildren()) {
            if (item instanceof ResourceNode) {
                resultList.add(new Resource((ResourceNode) item));
            }
        }
        return resultList;
    }

    public String ramlVersion() {
        return getVersion(node).value();
    }

}
