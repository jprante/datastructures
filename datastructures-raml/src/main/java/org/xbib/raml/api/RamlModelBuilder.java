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
package org.xbib.raml.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.xbib.raml.api.loader.ResourceLoader;
import org.xbib.raml.api.loader.ResourceLoaderFactories;
import org.xbib.raml.api.loader.ResourceLoaderFactory;
import org.xbib.raml.api.model.PermissiveURI;
import org.xbib.raml.api.model.common.ValidationResult;
import org.xbib.raml.api.model.v10.RamlFragment;
import org.xbib.raml.api.model.v10.api.DocumentationItem;
import org.xbib.raml.api.model.v10.api.Library;
import org.xbib.raml.api.model.v10.datamodel.ExampleSpec;
import org.xbib.raml.api.model.v10.datamodel.TypeDeclaration;
import org.xbib.raml.api.model.v10.methods.Trait;
import org.xbib.raml.api.model.v10.resources.ResourceType;
import org.xbib.raml.api.model.v10.security.SecurityScheme;
import org.xbib.raml.internal.impl.RamlBuilder;
import org.xbib.raml.internal.impl.commons.RamlHeader;
import org.xbib.raml.internal.impl.commons.model.Api;
import org.xbib.raml.internal.impl.commons.model.DefaultModelElement;
import org.xbib.raml.internal.impl.commons.model.RamlValidationResult;
import org.xbib.raml.internal.impl.commons.model.StringType;
import org.xbib.raml.internal.impl.commons.model.factory.TypeDeclarationModelFactory;
import org.xbib.raml.internal.impl.commons.nodes.RamlDocumentNode;
import org.xbib.raml.internal.utils.IOUtils;
import org.xbib.raml.internal.utils.RamlNodeUtils;
import org.xbib.raml.internal.utils.StreamUtils;
import org.xbib.raml.yagi.framework.model.DefaultModelBindingConfiguration;
import org.xbib.raml.yagi.framework.model.ModelBindingConfiguration;
import org.xbib.raml.yagi.framework.model.ModelProxyBuilder;
import org.xbib.raml.yagi.framework.nodes.ErrorNode;
import org.xbib.raml.yagi.framework.nodes.KeyValueNode;
import org.xbib.raml.yagi.framework.nodes.KeyValueNodeImpl;
import org.xbib.raml.yagi.framework.nodes.Node;
import org.xbib.raml.yagi.framework.nodes.StringNodeImpl;

/**
 * Entry point class to parse top level RAML descriptors.
 */
public class RamlModelBuilder {

    public static final String MODEL_PACKAGE = "org.xbib.raml.internal.impl.commons.model";
    private final ResourceLoaderFactory resourceLoaderFactory;
    private final RamlBuilder builder = new RamlBuilder();

    public RamlModelBuilder() {
        resourceLoaderFactory = ResourceLoaderFactories.defaultResourceLoaderFactory();
        // new DefaultResourceLoader(RootDirectoryFileAccessGuard.fromRootDir(Sets.newHashSet("file", "http", "https"),
        // ".")));
    }

    public RamlModelBuilder(ResourceLoader resourceLoaderFactory) {
        this.resourceLoaderFactory = ResourceLoaderFactories.identityFactory(resourceLoaderFactory);
    }


    public RamlModelResult buildApi(String ramlLocation) {
        String content = getRamlContent(ramlLocation);
        if (content == null) {
            return generateRamlApiResult("Raml does not exist at: " + ramlLocation);
        }
        return buildApi(content, ramlLocation);
    }


    public RamlModelResult buildApi(File ramlFile) {
        String content = getRamlContent(ramlFile);
        if (content == null) {
            return generateRamlApiResult("Files does not exist or is not a regular file: " + ramlFile.getPath());
        }
        return buildApi(content, ramlFile.getPath());
    }


    public RamlModelResult buildApi(Reader reader, String ramlLocation) {
        String content = getRamlContent(reader);
        if (content == null) {
            return generateRamlApiResult("Invalid reader provided with location: " + ramlLocation);
        }
        return buildApi(content, ramlLocation);
    }


    public RamlModelResult buildApi(String content, String ramlLocation) {
        if (content == null) {
            return buildApi(ramlLocation);
        }

        if (ramlLocation.matches("^[a-z]+:.*")) {

            String actualName = PermissiveURI.create(ramlLocation).toString();
            if (ramlLocation.startsWith("file:")) {
                actualName = new File(PermissiveURI.create(ramlLocation).getPath()).getParent();
            }
            Node ramlNode = builder.build(content, resourceLoaderFactory.createResourceLoader(actualName), ramlLocation);
            return generateRamlApiResult(ramlNode, getFragment(content));
        } else {

            Node ramlNode = builder.build(content, resourceLoaderFactory.createResourceLoader(Paths.get(ramlLocation).toAbsolutePath().getParent().toString()), ramlLocation);
            return generateRamlApiResult(ramlNode, getFragment(content));
        }
    }

    private RamlFragment getFragment(String content) {
        try {
            RamlHeader ramlHeader = RamlHeader.parse(content);
            return ramlHeader.getFragment();
        } catch (RamlHeader.InvalidHeaderException e) {
            // ignore, already handled by builder
        }
        return null;
    }

    private RamlModelResult generateRamlApiResult(Node ramlNode, RamlFragment fragment) {
        List<ValidationResult> validationResults = new ArrayList<>();
        List<ErrorNode> errors = RamlNodeUtils.getErrors(ramlNode);
        for (ErrorNode errorNode : errors) {
            validationResults.add(new RamlValidationResult(errorNode));
        }
        if (validationResults.isEmpty()) {
            return wrapTree(ramlNode, fragment);
        }
        return new RamlModelResult(validationResults);
    }

    private RamlModelResult generateRamlApiResult(String errorMessage) {
        List<ValidationResult> validationResults = new ArrayList<>();
        validationResults.add(new RamlValidationResult(errorMessage));
        return new RamlModelResult(validationResults);
    }

    private RamlModelResult wrapTree(Node ramlNode, RamlFragment fragment) {
        if (ramlNode instanceof RamlDocumentNode) {
            org.xbib.raml.api.model.v10.api.Api apiV10 = ModelProxyBuilder.createModel(org.xbib.raml.api.model.v10.api.Api.class, new Api((RamlDocumentNode) ramlNode), createV10Binding());
            return new RamlModelResult(apiV10);
        }
        if (fragment == RamlFragment.Library) {
            Library library = ModelProxyBuilder.createModel(Library.class, new DefaultModelElement(ramlNode), createV10Binding());
            return new RamlModelResult(library);
        }
        if (fragment == RamlFragment.DataType || fragment == RamlFragment.AnnotationTypeDeclaration) {
            final org.xbib.raml.internal.impl.commons.model.type.TypeDeclaration delegateNode = new TypeDeclarationModelFactory().create(ramlNode);
            final ModelBindingConfiguration v10Binding = createV10Binding();
            TypeDeclaration typeDeclaration = ModelProxyBuilder.createModel((Class<TypeDeclaration>) v10Binding.reverseBindingOf(delegateNode), delegateNode, v10Binding);
            return new RamlModelResult(typeDeclaration);
        }
        if (fragment == RamlFragment.DocumentationItem) {
            DocumentationItem documentationItem = ModelProxyBuilder.createModel(DocumentationItem.class, new DefaultModelElement(ramlNode), createV10Binding());
            return new RamlModelResult(documentationItem);
        }
        if (fragment == RamlFragment.SecurityScheme) {
            SecurityScheme securityScheme = ModelProxyBuilder.createModel(SecurityScheme.class, new DefaultModelElement(ramlNode), createV10Binding());
            return new RamlModelResult(securityScheme);
        }
        if (fragment == RamlFragment.Trait) {
            Trait trait = ModelProxyBuilder.createModel(Trait.class, new DefaultModelElement(ramlNode), createV10Binding());
            return new RamlModelResult(trait);
        }
        if (fragment == RamlFragment.ResourceType) {
            ResourceType resourceType = ModelProxyBuilder.createModel(ResourceType.class, new DefaultModelElement(ramlNode), createV10Binding());
            return new RamlModelResult(resourceType);
        }
        if (fragment == RamlFragment.NamedExample) {
            if (!(ramlNode instanceof KeyValueNode)) {
                ramlNode = new KeyValueNodeImpl(new StringNodeImpl("__NamedExample_Fragment__"), ramlNode);
            }
            ExampleSpec exampleSpec = ModelProxyBuilder.createModel(ExampleSpec.class, new org.xbib.raml.internal.impl.commons.model.ExampleSpec((KeyValueNode) ramlNode), createV10Binding());
            return new RamlModelResult(exampleSpec);
        }
        throw new IllegalStateException("Invalid ramlNode type (" + ramlNode.getClass().getSimpleName() + ") or fragment (" + fragment + ") combination");
    }

    private ModelBindingConfiguration createV10Binding() {
        final DefaultModelBindingConfiguration bindingConfiguration = new DefaultModelBindingConfiguration();
        bindingConfiguration.bindPackage(MODEL_PACKAGE);
        // Bind all StringTypes to the StringType implementation they are only marker interfaces
        bindingConfiguration.bind(org.xbib.raml.api.model.v10.system.types.StringType.class, StringType.class);
        bindingConfiguration.bind(org.xbib.raml.api.model.v10.system.types.ValueType.class, StringType.class);
        bindingConfiguration.defaultTo(DefaultModelElement.class);
        bindingConfiguration.bind(TypeDeclaration.class, new TypeDeclarationModelFactory());
        bindingConfiguration.reverseBindPackage("org.xbib.raml.api.model.v10.datamodel");
        return bindingConfiguration;
    }

    private String getRamlContent(File ramlFile) {
        if (ramlFile == null || !ramlFile.isFile()) {
            return null;
        }

        ResourceLoader fileLoader = resourceLoaderFactory.createResourceLoader(ramlFile.getAbsoluteFile().getParent());
        return getRamlContent(ramlFile.getName(), fileLoader);
    }

    private String getRamlContent(Reader ramlReader) {
        if (ramlReader == null) {
            return null;
        }
        try {
            return IOUtils.toString(ramlReader);
        } catch (IOException e) {
            return null;
        } finally {
            IOUtils.closeQuietly(ramlReader);
        }
    }

    private String getRamlContent(String ramlLocation) {

        if (ramlLocation == null) {

            return null;
        }

        if (ramlLocation.startsWith("file:")) {
            Path p = Paths.get(URI.create(ramlLocation)).toAbsolutePath().getParent();
            return getRamlContent(ramlLocation, resourceLoaderFactory.createResourceLoader(p.toString()));
        } else {
            Path p = Paths.get(ramlLocation).toAbsolutePath().getParent();
            return getRamlContent(ramlLocation, resourceLoaderFactory.createResourceLoader(p.toString()));
        }
    }

    private String getRamlContent(String ramlLocation, ResourceLoader loader) {
        if (ramlLocation == null) {
            return null;
        }
        InputStream ramlStream = loader.fetchResource(ramlLocation);
        if (ramlStream != null) {
            return StreamUtils.toString(ramlStream);
        }
        return null;
    }
}
