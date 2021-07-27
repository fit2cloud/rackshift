package io.rackshift.model;

import java.util.List;

public class MetalPlugin {

    private String name;
    private List<String> supportedBrands;
    private List<String> supportedModels;

    public MetalPlugin(String name, List<String> supportedBrands, List<String> supportedModels) {
        this.name = name;
        this.supportedBrands = supportedBrands;
        this.supportedModels = supportedModels;
    }
}
