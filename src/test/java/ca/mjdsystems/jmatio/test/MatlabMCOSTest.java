/*
 * Copyright 2014 Matthew Dawson <matthew@mjdsystems.ca>
 */
package ca.mjdsystems.jmatio.test;

import ca.mjdsystems.jmatio.io.MatFileReader;
import ca.mjdsystems.jmatio.types.MLArray;
import ca.mjdsystems.jmatio.types.MLChar;
import ca.mjdsystems.jmatio.types.MLObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.*;
import java.util.Collection;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * This test verifies that ReducedHeader generated mat files work correctly.
 *
 * @author Matthew Dawson <matthew@mjdsystems.ca>
 */
@RunWith(JUnit4.class)
public class MatlabMCOSTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testParsingSimpleEmptyMCOS() throws IOException
    {
        File file = fileFromStream("/mcos/simpleempty.mat");
        MatFileReader reader = new MatFileReader(file);
        Map<String, MLArray> content = reader.getContent();

        assertThat(content.size(), is(1));

        MLObject obj = (MLObject) content.get("obj");
        assertThat(obj, is(notNullValue()));

        assertThat(obj.getName(), is("obj"));
        assertThat(obj.getClassName(), is("SimpleEmpty"));
        assertThat(obj.getObject().getAllFields().size(), is(0));
    }

    @Test
    public void testParsingMultipleSimpleEmptyMCOS() throws IOException
    {
        File file = fileFromStream("/mcos/simpleempty_multiple.mat");
        MatFileReader reader = new MatFileReader(file);
        Map<String, MLArray> content = reader.getContent();

        assertThat(content.size(), is(2));

        MLObject obj = (MLObject) content.get("obj1");
        assertThat(obj, is(notNullValue()));

        assertThat(obj.getName(), is("obj1"));
        assertThat(obj.getClassName(), is("SimpleEmpty"));
        assertThat(obj.getObject().getAllFields().size(), is(0));


        obj = (MLObject) content.get("obj2");
        assertThat(obj, is(notNullValue()));

        assertThat(obj.getName(), is("obj2"));
        assertThat(obj.getClassName(), is("SimpleEmpty"));
        assertThat(obj.getObject().getAllFields().size(), is(0));
    }

    @Test
    public void testParsingSimpleSingleTextUnmodifiedMCOS() throws IOException
    {
        File file = fileFromStream("/mcos/simplesingletext_unmodified.mat");
        MatFileReader reader = new MatFileReader(file);
        Map<String, MLArray> content = reader.getContent();

        assertThat(content.size(), is(1));

        MLObject obj = (MLObject) content.get("obj");
        assertThat(obj, is(notNullValue()));

        assertThat(obj.getName(), is("obj"));
        assertThat(obj.getClassName(), is("SimpleSingleText"));
        Collection<MLArray> fields = obj.getObject().getAllFields();
        assertThat(fields.size(), is(1));

        MLChar field = (MLChar) fields.toArray()[0];
        assertThat(field.getString(0), is("Default text"));

        assertThat(obj.getObject().getFieldNames().iterator().next(), is("test_text"));
    }

    private File fileFromStream(String location) throws IOException
    {
        String outname = location.replace("/", "_");
        File f = folder.newFile(outname);
        InputStream stream = MatlabMCOSTest.class.getResourceAsStream(location);
        OutputStream fos = new BufferedOutputStream(new FileOutputStream(f));
        byte[] buffer = new byte[1024];
        int read = 0;
        while ((read = stream.read(buffer)) != -1) {
            fos.write(buffer, 0, read);
        }
        fos.flush();
        fos.close();
        return f;
    }
}
