package ru.sbrf.docedit.util;

import java.io.*;

/**
 * Created by SBT-Bakhurskiy-IA on 10.02.2017.
 */
public class SerializationHelper {
    public static InputStream writeObject(Object o) {
        if (o == null)
            return null;

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {

            oos.writeObject(o);
            oos.flush();
            baos.flush();
            byte[] asArray = baos.toByteArray();
            return new ByteArrayInputStream(asArray);

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readObject(byte[] bytes, Class<? extends T> clazz) {
        if (bytes == null)
            return null;

        try (ByteArrayInputStream baip = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(baip)) {

            return clazz.cast(ois.readObject());

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
