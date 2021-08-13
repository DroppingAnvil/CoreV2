package dev.droppinganvil.core.install;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

public class CopyrightUtils {
    public static void exportLicenses(File directory, ClassLoader cl) throws IOException {
        Enumeration<URL> urlE = cl.getResources("licenses");
        while (urlE.hasMoreElements()) {
            URL url = urlE.nextElement();
            System.out.println(url.getFile());
        }

    }
}
