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
package org.xbib.raml.internal.impl.commons.phase;

import java.io.InputStream;
import java.net.URI;
import org.xbib.raml.api.loader.ResourceLoader;
import org.xbib.raml.api.loader.ResourceLoaderExtended;
import org.xbib.raml.api.loader.ResourceUriCallback;
import org.xbib.raml.api.model.v10.RamlFragment;
import org.xbib.raml.internal.impl.commons.RamlHeader;
import org.xbib.raml.internal.impl.commons.nodes.DefaultRamlTypedFragment;
import org.xbib.raml.internal.utils.IOUtils;
import org.xbib.raml.internal.utils.ResourcePathUtils;
import org.xbib.raml.internal.utils.StreamUtils;
import org.xbib.raml.yagi.framework.nodes.AbstractRamlNode;
import org.xbib.raml.yagi.framework.nodes.IncludeErrorNode;
import org.xbib.raml.yagi.framework.nodes.Node;
import org.xbib.raml.yagi.framework.nodes.NullNodeImpl;
import org.xbib.raml.yagi.framework.nodes.ObjectNode;
import org.xbib.raml.yagi.framework.nodes.Position;
import org.xbib.raml.yagi.framework.nodes.StringNodeImpl;
import org.xbib.raml.yagi.framework.nodes.snakeyaml.NodeParser;
import org.xbib.raml.yagi.framework.nodes.snakeyaml.SYIncludeNode;
import org.xbib.raml.yagi.framework.phase.Transformer;


public class IncludeResolver implements Transformer, ResourceUriCallback {

    private final ResourceLoader resourceLoader;
    private String includedResourceUri;

    public IncludeResolver(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public boolean matches(Node node) {
        return node instanceof SYIncludeNode;
    }

    @Override
    public Node transform(Node node) {
        final SYIncludeNode includeNode = (SYIncludeNode) node;
        final String resourcePath = ResourcePathUtils.toAbsoluteLocation(node.getStartPosition().getPath(), includeNode.getIncludePath());
        InputStream inputStream = null;

        try {
            if (resourceLoader instanceof ResourceLoaderExtended) {
                inputStream = ((ResourceLoaderExtended) resourceLoader).fetchResource(resourcePath, this);
            } else {
                inputStream = resourceLoader.fetchResource(resourcePath);
            }

            if (inputStream == null) {
                return new IncludeErrorNode("Include cannot be resolved: " + resourcePath);
            }
            Node result;
            String includeContent = StreamUtils.toString(inputStream);
            if (resourcePath.endsWith(".raml") || resourcePath.endsWith(".yaml") || resourcePath.endsWith(".yml")) {
                try {
                    RamlHeader ramlHeader = RamlHeader.parse(includeContent);
                    final RamlFragment fragment = ramlHeader.getFragment();
                    result = NodeParser.parse(resourceLoader, resourcePath, includeContent);
                    if (result != null && isTypedFragment(result, fragment)) {
                        final DefaultRamlTypedFragment newNode = new DefaultRamlTypedFragment(fragment);
                        result.replaceWith(newNode);
                        result = newNode;
                    }
                } catch (RamlHeader.InvalidHeaderException e) {
                    // no valid header defined => !supportUses
                    result = NodeParser.parse(resourceLoader, resourcePath, includeContent);
                }

            } else
            // scalar value
            {
                result = new StringNodeImpl(includeContent);
                setTempPositionWithResourceUri(result);
            }

            if (result == null) {
                result = new NullNodeImpl();
            }

            return result;
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    private void setTempPositionWithResourceUri(Node result) {
        // Position is always null, so it is generated on the fly
        Position startPosition = result.getStartPosition();
        Position endPosition = result.getEndPosition();

        startPosition.setIncludedResourceUri(includedResourceUri);
        endPosition.setIncludedResourceUri(includedResourceUri);

        ((AbstractRamlNode) result).setStartPosition(startPosition);
        ((AbstractRamlNode) result).setEndPosition(endPosition);
    }

    private boolean isTypedFragment(Node result, RamlFragment fragment) {
        return fragment != null && fragment != RamlFragment.Library && result instanceof ObjectNode;
    }


    @Override
    public void onResourceFound(URI resourceURI) {
        this.includedResourceUri = resourceURI.toString();
    }
}
