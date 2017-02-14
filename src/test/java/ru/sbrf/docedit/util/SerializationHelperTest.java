package ru.sbrf.docedit.util;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by SBT-Bakhurskiy-IA on 13.02.2017.
 */
public class SerializationHelperTest {
    @Test
    @SuppressWarnings("unchecked")
    public void simpleTest() throws Exception {
        final List<Long> serializee = new ArrayList<>();

        serializee.add(100L);
        serializee.add(200L);

        try (InputStream is = SerializationHelper.writeObject(serializee);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            for (int c; (c = is.read()) != -1; )
                baos.write(c);
            final List<Long> deserialized = SerializationHelper.readObject(baos.toByteArray(), List.class);
            assertEquals(deserialized, serializee);
        }
    }
}