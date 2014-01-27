/*
 * Copyright 2013 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.datavirt.commons.maven.plugin.featuresxml;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Models the karaf features.xml file.
 *
 * @author eric.wittmann@redhat.com
 */
public class FeaturesXml {
    
    private static final String FEATURES_XML_NS = "http://karaf.apache.org/xmlns/features/v1.0.0";
    
    private Document document;
    private Element rootElement;
    private Map<String, Element> features = new HashMap<String, Element>();

    /**
     * Constructor.
     */
    public FeaturesXml() throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setNamespaceAware(true);
        docFactory.setValidating(false);
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        document = docBuilder.newDocument();
        rootElement = document.createElementNS(FEATURES_XML_NS, "features");
        document.appendChild(rootElement);
    }

    /**
     * Adds a repository to the features.xml file.
     * @param repo
     */
    public void addRepository(String repo) {
        Element repoElem = document.createElementNS(FEATURES_XML_NS, "repository");
        rootElement.appendChild(repoElem);
        repoElem.setTextContent(repo);
    }

    /**
     * Adds a feature to the model.
     * @param name
     * @param version
     * @param comment
     */
    public void addFeature(String name, String version, String comment) {
        if (comment != null) {
            Comment commentNode = document.createComment(" " + comment + " ");
            rootElement.appendChild(commentNode);
        }
        Element featureElem = document.createElementNS(FEATURES_XML_NS, "feature");
        rootElement.appendChild(featureElem);
        featureElem.setAttribute("name", name);
        featureElem.setAttribute("version", version);
        String featureKey = name + "||" + version;
        features.put(featureKey, featureElem);
    }
    
    /**
     * Adds a feature dependency to a feature in the features.xml.
     * @param name
     * @param version
     * @param dependencyName
     * @param dependencyVersion
     */
    public void addFeatureDependency(String name, String version, String dependencyName, String dependencyVersion) {
        String featureKey = name + "||" + version;
        Element featureElem = features.get(featureKey);
        if (featureElem != null) {
            Element dependencyElem = document.createElementNS(FEATURES_XML_NS, "feature");
            featureElem.appendChild(dependencyElem);
            dependencyElem.setTextContent(dependencyName);
            if (dependencyVersion != null) {
                dependencyElem.setAttribute("version", dependencyVersion);
            }
        }
    }
    
    /**
     * Adds a bundle to the feature in the features.xml.
     * @param name
     * @param version
     * @param bundle
     */
    public void addBundle(String name, String version, String bundle) {
        String featureKey = name + "||" + version;
        Element featureElem = features.get(featureKey);
        if (featureElem != null) {
            Element bundleElem = document.createElementNS(FEATURES_XML_NS, "bundle");
            featureElem.appendChild(bundleElem);
            bundleElem.setTextContent(bundle);
        }
    }
    
    /**
     * Writes the features.xml file out.
     * @param outputFile
     * @throws Exception
     */
    public void writeTo(File outputFile) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute("indent-number", 3);
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(outputFile);
        transformer.transform(source, result);
    }

}
