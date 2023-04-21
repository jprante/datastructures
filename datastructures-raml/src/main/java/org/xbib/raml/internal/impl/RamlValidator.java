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
package org.xbib.raml.internal.impl;

import java.io.File;
import java.io.FileFilter;
import java.util.List;
import org.xbib.raml.internal.utils.RamlNodeUtils;
import org.xbib.raml.yagi.framework.nodes.ErrorNode;
import org.xbib.raml.yagi.framework.nodes.Node;
import org.xbib.raml.yagi.framework.nodes.Position;

public class RamlValidator {

    private static final String USAGE = "Arguments: [-dump] file|url|dir";

    private boolean dump;
    private String ramlLocation;

    private RamlValidator(String[] args) {
        parseArguments(args);
    }

    private void validate() {
        validate(new File(ramlLocation));
    }

    private void validate(File location) {
        if (isRamlFile(location)) {
            validateRaml(location);
        }

        File[] files = new File[]{};
        if (location.isDirectory()) {
            files = location.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.isDirectory() || isRamlFile(pathname);
                }
            });
        }
        for (File file : files) {
            validate(file);
        }
    }

    private void validateRaml(File ramlFile) {

        final Node raml = new RamlBuilder().build(ramlFile);
        List<ErrorNode> errors = RamlNodeUtils.getErrors(raml);
        if (!errors.isEmpty()) {
            logErrors(errors);
        }

    }

    private boolean isRamlFile(File pathname) {
        return pathname.isFile() && pathname.getName().endsWith(".raml");
    }

    public static void main(String[] args) {
        new RamlValidator(args).validate();
    }

    private void logErrors(List<ErrorNode> errors) {
        String label = errors.size() > 1 ? "errors" : "error";
        System.out.format("%d %s found:\n\n", errors.size(), label);
        for (ErrorNode error : errors) {
            String message = error.getErrorMessage();
            int idx = message.indexOf(". Options are");
            if (idx != -1) {
                message = message.substring(0, idx);
            }
            Position position = error.getSource() != null ? error.getSource().getStartPosition() : error.getStartPosition();
            System.out.format("\t- %s %s\n\n", message, position);
        }
    }

    private void parseArguments(String[] args) {
        if (args.length < 1 || args.length > 2) {
            throw new IllegalArgumentException(USAGE);
        }
        if (args.length == 2) {
            if (!"-dump".equals(args[0])) {
                throw new IllegalArgumentException(USAGE);
            }
            this.dump = true;
            this.ramlLocation = args[1];
        } else {
            this.ramlLocation = args[0];
        }
    }

}
