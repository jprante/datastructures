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

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Set;

/**
 * Created. There, you have it.
 */
public class RamlParsingLimitsController {

    private final static boolean shouldVerify = Boolean.parseBoolean(System.getProperty("raml.verifyRaml", "true"));
    private final static boolean cycleCheck = Boolean.parseBoolean(System.getProperty("raml.verifyReferenceCycle", "true"));

    private final static int maxRamlDepth = Integer.parseInt(System.getProperty("raml.maxDepth", "2000"));
    private final static int maxRamlReferences = Integer.parseInt(System.getProperty("raml.maxReferences", "10000"));

    private final IdentityHashMap<org.yaml.snakeyaml.nodes.Node, Integer> seenNodes = new IdentityHashMap<>();
    private final Set<org.yaml.snakeyaml.nodes.Node> currentlyHandling = new HashSet<>();


    private final boolean verify;
    private final int maxDepth;
    private final int maxReferences;
    private final boolean verifyCycle;

    public RamlParsingLimitsController(boolean verify, int maxDepth, int maxRamlReferences, boolean verifyCycle) {
        this.verify = verify;
        this.maxDepth = maxDepth;
        this.maxReferences = maxRamlReferences;
        this.verifyCycle = verifyCycle;
    }

    public RamlParsingLimitsController() {

        verify = shouldVerify;
        maxDepth = maxRamlDepth;
        maxReferences = maxRamlReferences;
        verifyCycle = cycleCheck;
    }

    public void verifyNode(org.yaml.snakeyaml.nodes.Node node, int depth) {

        try {
            if (!verify) {
                return;
            }

            if (verifyCycle && currentlyHandling.contains(node)) {

                throw new LimitsException("cycle detected", node.getStartMark());
            } else {

                currentlyHandling.add(node);
            }

            if (depth > maxDepth) {
                throw new LimitsException("maximum depth exceeded: " + maxDepth, node.getStartMark());
            }

            if (seenNodes.containsKey(node)) {

                seenNodes.put(node, seenNodes.get(node) + 1);
            } else {

                seenNodes.put(node, 1);
            }

            if (seenNodes.get(node) > maxReferences) {

                throw new LimitsException("reference count exceeded: " + maxReferences, node.getStartMark());
            }
        } finally {
            currentlyHandling.remove(node);
        }
    }
}
