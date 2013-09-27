/**
 *
 */
package com.microtripit.mandrillapp.lutung.controller;

import com.microtripit.mandrillapp.lutung.MandrillApi;
import com.microtripit.mandrillapp.lutung.MandrillApiTest;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.microtripit.mandrillapp.lutung.view.MandrillTemplate;
import com.microtripit.mandrillapp.lutung.view.MandrillTimeSeries;
import junit.framework.Assert;
import org.junit.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

/**
 * @author rschreijer
 * @since Mar 22, 2013
 */
public final class MandrillTemplatesApiTest {
    private static MandrillApi mandrillApi;
    private static String templateName;

    MandrillTemplate t = null;

    @BeforeClass
    public static final void runBeforeClass() {
        final String key = MandrillApiTest.getMandrillApiKey();
        if (key != null) {
            mandrillApi = new MandrillApi(key);
        } else {
            mandrillApi = null;
        }
        templateName = "lutung_templatename_unit_test_"
                + System.currentTimeMillis() + new Random().nextInt();
    }

    @Before
    public final void runBefore() {
        Assume.assumeNotNull(mandrillApi);
    }

    @After
    public final void after() throws Exception, MandrillApiError {
        if (t != null) {
            t = mandrillApi.templates().delete(t.getName());
            Assert.assertNotNull(t);
        }
    }

    @Test(expected = MandrillApiError.class)
    public final void testAddWithoutName() throws IOException, MandrillApiError {
        mandrillApi.templates().add(
                null,
                "<div><h1>Hello World!</h1></div>",
                null);
        Assert.fail();
    }

    @Test
    public final void testAddWithoutCode() throws IOException, MandrillApiError {
        t = mandrillApi.templates().add(
                templateName,
                null,
                null);
        Assert.assertNotNull(t);
        Assert.assertNotNull(t.getName());
    }

    @Test
    public final void testAdd() throws IOException, MandrillApiError {
        t = mandrillApi.templates().add(
                templateName,
                "<div><h1>Hello World!</h1></div>",
                false);
        Assert.assertNotNull(t);
        Assert.assertNotNull(t.getName());
    }

    @Test
    public final void testList() throws IOException, MandrillApiError {
        MandrillTemplate t1 = mandrillApi.templates().add(
                templateName + "_a",
                "<div><h1>Hello World!</h1></div>",
                false);
        MandrillTemplate t2 = mandrillApi.templates().add(
                templateName + "_b",
                "<div><h1>Hello World!</h1></div>",
                false);

        final MandrillTemplate[] templates = mandrillApi.templates().list();
        Assert.assertNotNull(templates);
        for (MandrillTemplate template : templates) {
            Assert.assertNotNull(template.getName());
            //Assert.assertNotNull(t.getCode());
            Assert.assertNotNull(template.getCreatedAt());
        }

        Assert.assertNotNull(mandrillApi.templates().delete(t1.getName()));
        Assert.assertNotNull(mandrillApi.templates().delete(t2.getName()));
    }

    @Test(expected = MandrillApiError.class)
    public final void testInfoWithoutName() throws IOException, MandrillApiError {
        mandrillApi.templates().info(null);
        Assert.fail();
    }

    @Test
    public final void testInfo() throws IOException, MandrillApiError {
        final MandrillTemplate[] templates = mandrillApi.templates().list();
        Assert.assertNotNull(templates);
        if (templates.length > 0) {
            t = mandrillApi.templates().info(templates[0].getName());
            Assert.assertNotNull(t);
            Assert.assertNotNull(t.getName());
            //Assert.assertNotNull(t.getCode());
            Assert.assertNotNull(t.getCreatedAt());
        }
    }

    @Test(expected = MandrillApiError.class)
    public final void testUpdateWithoutName() throws IOException, MandrillApiError {
        mandrillApi.templates().update(
                null,
                "<div><h1>Hello World!</h1></div>",
                "Hello World!",
                false);
        Assert.fail();
    }

    @Test(expected = MandrillApiError.class)
    public final void testUpdateWithoutCode() throws IOException, MandrillApiError {
        mandrillApi.templates().update(templateName, null, null, false);
        Assert.fail();
    }

    @Test
    @Ignore
    public final void testUpdate() throws IOException, MandrillApiError {
        t = mandrillApi.templates().add(
                templateName,
                "<div><h1>Hello World!</h1></div>",
                "Hello World!",
                false);
        Assert.assertNotNull(t);
        final String updatedCode = "<div><h1>Hello World! UPDATED</h1></div>";
        t = mandrillApi.templates().update(t.getName(), updatedCode, null, false);

        Assert.assertNotNull(t);
        Assert.assertNotNull(t.getName());
        Assert.assertEquals(updatedCode, t.getCode());
        Assert.assertNotNull(t.getCreatedAt());
    }

    @Test(expected = MandrillApiError.class)
    public final void testPublishWithoutName()
            throws IOException, MandrillApiError {

        mandrillApi.templates().publish(null);
        Assert.fail();
    }

    @Test
    public final void testPublish() throws IOException, MandrillApiError {
        t = mandrillApi.templates().add(
                templateName,
                "<div><h1>Hello World!</h1></div>",
                false);
        Assert.assertNotNull(t);
        Assert.assertNull(t.getPublishedAt());
        t = mandrillApi.templates().publish(t.getName());
        Assert.assertNotNull(t);
        Assert.assertNotNull(t.getPublishedAt());
    }

    @Test(expected = MandrillApiError.class)
    public final void testDeleteWithoutName()
            throws IOException, MandrillApiError {

        mandrillApi.templates().delete(null);
        Assert.fail();
    }

    @Test
    public final void testDelete() throws IOException, MandrillApiError {
        MandrillTemplate template = mandrillApi.templates().add(
                templateName,
                "<div><h1>Hello World!</h1></div>",
                false);
        Assert.assertNotNull(template);
        template = mandrillApi.templates().delete(template.getName());
        Assert.assertNotNull(template);
    }

    @Test(expected = MandrillApiError.class)
    public final void testTimeSeriesWithoutName()
            throws IOException, MandrillApiError {

        mandrillApi.templates().timeSeries(null);
        Assert.fail();
    }

    @Test
    public final void testTimeSeries()
            throws IOException, MandrillApiError {

        final MandrillTemplate[] templates = mandrillApi.templates().list();
        Assert.assertNotNull(templates);
        if (templates.length > 0) {
            final MandrillTimeSeries[] series =
                    mandrillApi.templates().timeSeries(templates[0].getName());
            Assert.assertNotNull(series);
            for (MandrillTimeSeries t : series) {
                Assert.assertNotNull(t.getTime());
            }
        }
    }

    @Test(expected = MandrillApiError.class)
    public final void testRenderWithoutName()
            throws IOException, MandrillApiError {

        mandrillApi.templates().render(
                null,
                new HashMap<String, String>(),
                null);
        Assert.fail();

    }

    @Test(expected = MandrillApiError.class)
    public final void testRenderWithoutContent()
            throws IOException, MandrillApiError {

        mandrillApi.templates().render(templateName, null, null);
    }

    @Test
    public final void testRender() throws IOException, MandrillApiError {
        final String content = "<div><h1>Hello World!</h1></div>";
        t = mandrillApi.templates().add(templateName,
                content, false);
        Assert.assertNotNull(t);
        final String rendered = mandrillApi.templates().render(
                t.getName(),
                new HashMap<String, String>(),
                null);
        Assert.assertNotNull(rendered);
        Assert.assertEquals(content, rendered);

    }

}
