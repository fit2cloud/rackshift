package io.rackshift.metal.sdk;

import org.apache.commons.io.IOUtils;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        System.out.println(IOUtils.toString(classLoader.getResourceAsStream("plugin.properties")));
    }

}
