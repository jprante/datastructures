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

import java.io.Reader;
import java.io.StringReader;
import org.xbib.raml.api.loader.ResourceLoader;
import org.xbib.raml.api.loader.ResourceLoaderFactories;
import org.xbib.raml.yagi.framework.grammar.rule.ErrorNodeFactory;
import org.xbib.raml.yagi.framework.nodes.DefaultPosition;
import org.xbib.raml.yagi.framework.nodes.ErrorNode;
import org.xbib.raml.yagi.framework.nodes.Node;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.error.MarkedYAMLException;
import org.yaml.snakeyaml.reader.ReaderException;

public class NodeParser {


    public static Node parse(ResourceLoader resourceLoader, String resourcePath, Reader reader) {

        SmartReader smartReader = new SmartReader(reader);

        try {
            Yaml yamlParser = new Yaml();
            org.yaml.snakeyaml.nodes.Node composedNode = yamlParser.compose(smartReader);
            if (composedNode == null) {
                return null;
            } else {
                try {
                    return new SYModelWrapper(resourceLoader, resourcePath).wrap(composedNode, 0);
                } catch (LimitsException e) {

                    return ErrorNodeFactory.limitsExceptionThrown(e);
                }
            }
        } catch (final MarkedYAMLException e) {
            return buildYamlErrorNode(e, resourcePath, resourceLoader);
        } catch (ReaderException e) {

            return buildYamlErrorNode(e, smartReader);
        }
    }

    private static Node buildYamlErrorNode(MarkedYAMLException e, String resourcePath, ResourceLoader resourceLoader) {
        final ErrorNode errorNode = new ErrorNode("Underlying error while parsing YAML syntax: '" + e.getMessage() + "'");
        final Mark problemMark = e.getProblemMark();
        errorNode.setStartPosition(new DefaultPosition(problemMark.getIndex(), problemMark.getLine(), 0, resourcePath, ResourceLoaderFactories.identityFactory(resourceLoader)));
        errorNode.setEndPosition(new DefaultPosition(problemMark.getIndex() + 1, problemMark.getLine(), problemMark.getColumn(), resourcePath, ResourceLoaderFactories.identityFactory(resourceLoader)));
        return errorNode;
    }

    private static Node buildYamlErrorNode(ReaderException e, SmartReader reader) {

        final ErrorNode errorNode = new ErrorNode("Underlying error while parsing YAML syntax: '" + e + " around: " + reader.getLastValidString() + "'");
        // errorNode.setStartPosition(new DefaultPosition(e.getPosition(), 0, 0, resourcePath, new DefaultResourceLoader()));
        // errorNode.setEndPosition(new DefaultPosition(e.getPosition() + 1, 0,0, resourcePath, new DefaultResourceLoader()));
        return errorNode;
    }


    public static Node parse(ResourceLoader resourceLoader, String resourcePath, String content) {
        return parse(resourceLoader, resourcePath, new StringReader(content));
    }


}
