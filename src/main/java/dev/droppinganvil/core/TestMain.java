package dev.droppinganvil.core;

import dev.droppinganvil.core.install.CopyrightUtils;

import java.io.File;
import java.io.IOException;

public class TestMain {
    public static TestMain instance;
    private File root;

    public TestMain() {}

    public static void main(String... args) throws IOException {
        instance = new TestMain();
        instance.root = new File("testRoot");
        if (!instance.root.exists()) instance.root.mkdir();
        CopyrightUtils.exportLicenses(instance.root, instance.getClass().getClassLoader());
    }
}
