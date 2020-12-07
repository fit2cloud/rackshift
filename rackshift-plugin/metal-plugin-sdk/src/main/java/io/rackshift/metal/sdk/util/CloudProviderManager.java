package io.rackshift.metal.sdk.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public final class CloudProviderManager extends BaseManager {

    private String targetFilePath;
    private JarFileClassLoader jarFileClassLoader;
    private List<Object> cloudProviders = new ArrayList<>();

    public CloudProviderManager(Class classz, String targetFilePath, String basePackage) {
        super(classz, basePackage);
        this.targetFilePath = targetFilePath;
    }

    private void scanFileToPath(String filePath, int innerLevel) {
        File file = new File(filePath);
        // get the folder list
        File[] array = file.listFiles();
        Optional.ofNullable(array).ifPresent(files -> {
            for (File tmp : array) {
                String tmpPath = tmp.getPath();
                int level = innerLevel - 1;
                if (level > 0) {
                    if (tmp.isDirectory()) {
                        scanFileToPath(tmpPath, level);
                    }
                }

                if (tmp.getName().endsWith(".jar")) {
                    try {
                        jarFileClassLoader.addFile(tmp);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void init() {
        try {
            Thread currentThread = Thread.currentThread();
            ClassLoader sysCloader = currentThread.getContextClassLoader();
            jarFileClassLoader = new JarFileClassLoader(new URL[]{new File(targetFilePath).toURI().toURL()}, sysCloader);
            currentThread.setContextClassLoader(jarFileClassLoader);
            scanFileToPath(targetFilePath, 2);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        super.init();
        Collection plugins = getPlugins();
        this.cloudProviders.addAll(plugins);
    }

    public <T> T getCloudProvider(String name) {
        for (Object cp : cloudProviders) {
            try {
                Method getName = cp.getClass().getMethod("getName");
                Object invoke = getName.invoke(cp);
                if (invoke != null && invoke.toString().equals(name)) {
                    return (T) cp;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}


