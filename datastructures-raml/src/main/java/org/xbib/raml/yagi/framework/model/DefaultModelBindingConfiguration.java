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
package org.xbib.raml.yagi.framework.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


public class DefaultModelBindingConfiguration implements ModelBindingConfiguration {

    private final List<ModelBinding> bindings;
    private final List<ModelReverseBinding> reverseBinding;
    private NodeModelFactory defaultBinding;

    public DefaultModelBindingConfiguration() {
        this.bindings = new ArrayList<>();
        this.reverseBinding = new ArrayList<>();
        this.defaultBinding = new ClassNodeModelFactory(DefaultNodeBaseModel.class);
    }


    @Override
    public NodeModelFactory bindingOf(Class<?> className) {
        for (ModelBinding binding : bindings) {
            final NodeModelFactory result = binding.binding(className);
            if (result != null) {
                return result;
            }
        }
        if (defaultBinding != null) {
            return defaultBinding;
        } else {
            throw new RuntimeException("No binding found for " + className);
        }
    }


    @Override
    public Class<?> reverseBindingOf(NodeModel model) {
        for (ModelReverseBinding modelReverseBinding : reverseBinding) {
            final Class<?> aClass = modelReverseBinding.reverseBindingOf(model);
            if (aClass != null) {
                return aClass;
            }
        }
        throw new RuntimeException("No reverse bind found for " + model.getClass());
    }

    public DefaultModelBindingConfiguration bindPackage(String basePackageName) {
        this.bindings.add(new PackageModelBinding(basePackageName));
        return this;
    }

    public DefaultModelBindingConfiguration bindSimpleName(Class<? extends NodeModel> modelClass, String... names) {
        this.bindings.add(new SimpleClassNameBinding(new HashSet<>(Arrays.asList(names)), modelClass));
        return this;
    }

    public DefaultModelBindingConfiguration bind(Class<?> anInterface, Class<? extends NodeModel> model) {
        this.bindings.add(new SimpleBinding(anInterface, new ClassNodeModelFactory(model)));
        return this;
    }

    public DefaultModelBindingConfiguration bind(Class<?> clazz, NodeModelFactory factory) {
        this.bindings.add(new SimpleBinding(clazz, factory));
        return this;
    }

    public DefaultModelBindingConfiguration defaultTo(Class<? extends NodeModel> defaultClass) {
        this.defaultBinding = new ClassNodeModelFactory(defaultClass);
        return this;
    }

    public DefaultModelBindingConfiguration reverseBindPackage(String basePackage) {
        this.reverseBinding.add(new PackageReverseBinding(basePackage));
        return this;
    }
}
