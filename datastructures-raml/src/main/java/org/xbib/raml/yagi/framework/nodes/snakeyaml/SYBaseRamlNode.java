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
package org.xbib.raml.yagi.framework.nodes.snakeyaml;


import org.xbib.raml.api.loader.ResourceLoader;
import org.xbib.raml.api.loader.ResourceLoaderFactories;
import org.xbib.raml.yagi.framework.nodes.BaseNode;
import org.xbib.raml.yagi.framework.nodes.Position;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;

public abstract class SYBaseRamlNode extends BaseNode {

    private final Node yamlNode;
    private final String resourcePath;
    private final ResourceLoader resourceLoader;

    // For copy use cases
    protected SYBaseRamlNode(SYBaseRamlNode node) {
        super(node);
        this.yamlNode = node.yamlNode;
        this.resourcePath = node.getResourcePath();
        this.resourceLoader = node.getResourceLoader();
    }

    public SYBaseRamlNode(Node yamlNode, String resourcePath, ResourceLoader resourceLoader) {
        this.yamlNode = yamlNode;
        this.resourcePath = resourcePath;
        this.resourceLoader = resourceLoader;
    }

    protected Node getYamlNode() {
        return yamlNode;
    }

    public String getResourcePath() {
        return resourcePath;
    }


    @Override
    public Position getStartPosition() {
        return new SYPosition(yamlNode.getStartMark(), ResourceLoaderFactories.identityFactory(resourceLoader), resourcePath);
    }


    @Override
    public Position getEndPosition() {
        return new SYPosition(yamlNode.getEndMark(), ResourceLoaderFactories.identityFactory(resourceLoader), resourcePath);
    }


    public String getLiteralValue() {
        if (yamlNode instanceof ScalarNode) {
            return ((ScalarNode) getYamlNode()).getValue();
        }
        return null;
    }

    @Override
    public String toString() {
        return getLiteralValue();
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }
}
