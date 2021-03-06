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
package org.jboss.datavirt.commons.ui.header;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.junit.Test;

/**
 * Unit test for {@link DataVirtHeaderDataJS}.
 * @author mdrillin@redhat.com
 */
public class DataVirtHeaderJSTest {

    /**
     * Test method for {@link org.jboss.datavirt.commons.ui.header.DataVirtHeaderDataJS#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}.
     * @throws URISyntaxException
     * @throws IOException
     * @throws ServletException
     */
    @Test
    public void testDoGetHttpServletRequestHttpServletResponse() throws URISyntaxException, ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletConfig config = new MockServletConfig("app-1");

        File tempConfigDir = createAndPrepConfigDir();
        System.setProperty("org.jboss.datavirt.apps.config-dir", tempConfigDir.getCanonicalPath());

        try {
            DataVirtHeaderDataJS servlet = new DataVirtHeaderDataJS();
            servlet.init(config);
            servlet.doGet(request, response);

            String headers = response.getOutputHeadersAsString();
            String content = response.getOutputAsString();

//            Assert.assertEquals(normalize(EXPECTED_HEADERS), normalize(headers));
//            Assert.assertEquals(normalize(EXPECTED_CONTENT), normalize(content));
        } finally {
            System.setProperty("org.jboss.datavirt.apps.config-dir", "");
        }
    }

    /**
     * @throws IOException
     */
    private File createAndPrepConfigDir() throws IOException {
        File dir = File.createTempFile("_ovlunit", "configDir");
        if (dir.isFile()) {
            dir.delete();
        }
        dir.mkdirs();

        File configFile1 = new File(dir, "app1-datavirtapp.properties");
        Properties props = new Properties();
        props.setProperty("datavirtapp.app-id", "app-1");
        props.setProperty("datavirtapp.href", "/app-1/index.html");
        props.setProperty("datavirtapp.label", "Application One");
        props.setProperty("datavirtapp.primary-brand", "Unit Test");
        props.setProperty("datavirtapp.secondary-brand", "App One");
        props.store(new FileWriter(configFile1), "DataVirt App 1");

        File configFile2 = new File(dir, "app2-datavirtapp.properties");
        props = new Properties();
        props.setProperty("datavirtapp.app-id", "app-2");
        props.setProperty("datavirtapp.href", "/app-2/index.html");
        props.setProperty("datavirtapp.label", "Application Two");
        props.setProperty("datavirtapp.primary-brand", "Unit Test");
        props.setProperty("datavirtapp.secondary-brand", "App Two");
        props.store(new FileWriter(configFile2), "DataVirt App 2");

        File configFile3 = new File(dir, "app3-datavirtapp.properties");
        props = new Properties();
        props.setProperty("datavirtapp.app-id", "app-3");
        props.setProperty("datavirtapp.href", "/app-3/index.html");
        props.setProperty("datavirtapp.label", "Application Three");
        props.setProperty("datavirtapp.primary-brand", "Unit Test");
        props.setProperty("datavirtapp.secondary-brand", "App Three");
        props.store(new FileWriter(configFile3), "DataVirt App 3");

        return dir;
    }

    /**
     * Normalize line endings.
     * @param headers
     * @throws IOException
     */
    private String normalize(String multiLineValue) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new StringReader(multiLineValue));
        String line = null;
        while ( (line = reader.readLine()) != null) {
            builder.append(line.trim()).append("\r\n");
        }
        return builder.toString();
    }

    private static final String EXPECTED_HEADERS =
            "Cache-control: no-cache, no-store, must-revalidate\r\n" +
            "Content-Type: text/javascript\r\n" +
            "Date: <DATE VALUE>\r\n" +
            "Expires: <DATE VALUE>\r\n" +
            "Pragma: no-cache\r\n" +
            "";
    private static final String EXPECTED_CONTENT =
            "var DATAVIRT_HEADER_DATA = {\r\n" +
            "  \"username\" : \"ewittman\",\r\n" +
            "  \"logoutLink\" : \"?GLO=true\",\r\n" +
            "  \"primaryBrand\" : \"Unit Test\",\r\n" +
            "  \"secondaryBrand\" : \"App One\",\r\n" +
//            "  \"tabs\" : [ {\r\n" +
//            "    \"app-id\" : \"app-1\",\r\n" +
//            "    \"href\" : \"/app-1/index.html\",\r\n" +
//            "    \"label\" : \"Application One\",\r\n" +
//            "    \"active\" : true\r\n" +
//            "  }, {\r\n" +
//            "    \"app-id\" : \"app-2\",\r\n" +
//            "    \"href\" : \"/app-2/index.html\",\r\n" +
//            "    \"label\" : \"Application Two\",\r\n" +
//            "    \"active\" : false\r\n" +
//            "  }, {\r\n" +
//            "    \"app-id\" : \"app-3\",\r\n" +
//            "    \"href\" : \"/app-3/index.html\",\r\n" +
//            "    \"label\" : \"Application Three\",\r\n" +
//            "    \"active\" : false\r\n" +
//            "  } ]\r\n" +
            "};";

}
