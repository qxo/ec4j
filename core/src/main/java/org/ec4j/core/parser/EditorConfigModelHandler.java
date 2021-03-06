/**
 * Copyright (c) 2017 Angelo Zerr and other contributors as
 * indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ec4j.core.parser;

import org.ec4j.core.PropertyTypeRegistry;
import org.ec4j.core.model.EditorConfig;
import org.ec4j.core.model.Glob;
import org.ec4j.core.model.Property;
import org.ec4j.core.model.PropertyType;
import org.ec4j.core.model.PropertyType.PropertyValue;
import org.ec4j.core.model.Section;
import org.ec4j.core.model.Version;

/**
 * A {@link EditorConfigHandler} implementation that assemles {@link EditorConfig} instances out of the parse
 * notifications.
 *
 * @author <a href="mailto:angelo.zerr@gmail.com">Angelo Zerr</a>
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 */
public class EditorConfigModelHandler extends AbstractValidatingHandler {

    protected EditorConfig.Builder editorConfigBuilder;
    protected Property.Builder propertyBuilder;
    protected Section.Builder sectionBuilder;
    protected final Version version;

    public EditorConfigModelHandler(PropertyTypeRegistry registry, Version version) {
        super(registry);
        this.version = version;
    }

    /** {@inheritDoc} */
    @Override
    public void blankLine(ParseContext context) {
    }

    /** {@inheritDoc} */
    @Override
    public void endComment(ParseContext context, String comment) {
    }

    /** {@inheritDoc} */
    @Override
    public void endDocument(ParseContext context) {
    }

    /** {@inheritDoc} */
    @Override
    public void endProperty(ParseContext context) {
        propertyBuilder.closeProperty();
        propertyBuilder = null;
    }

    /** {@inheritDoc} */
    @Override
    public void endSection(ParseContext context) {
        sectionBuilder.applyDefaults().closeSection();
        sectionBuilder = null;
    }

    /**
     * @return the {@link EditorConfig} instance parsed out of the event stream
     */
    public EditorConfig getEditorConfig() {
        EditorConfig result = editorConfigBuilder.build();
        editorConfigBuilder = null;
        return result;
    }

    @Override
    protected void glob(ParseContext context, Glob glob) {
        sectionBuilder.glob(glob);
    }

    @Override
    protected void propertyValue(ParseContext context, PropertyValue<?> propValue) {
        propertyBuilder.value(propValue);
    }

    /** {@inheritDoc} */
    @Override
    public void startComment(ParseContext context) {
    }

    /** {@inheritDoc} */
    @Override
    public void startDocument(ParseContext context) {
        editorConfigBuilder = EditorConfig.builder().version(version);
    }

    /** {@inheritDoc} */
    @Override
    public void startProperty(ParseContext context) {
        propertyBuilder = sectionBuilder.openProperty();
    }

    /** {@inheritDoc} */
    @Override
    public void startSection(ParseContext context) {
        sectionBuilder = editorConfigBuilder.openSection();
    }

    @Override
    protected void type(ParseContext context, PropertyType<?> type) {
        propertyBuilder.type(type);
    }

    @Override
    protected void name(ParseContext context, String name) {
        propertyBuilder.name(name);
    }

}