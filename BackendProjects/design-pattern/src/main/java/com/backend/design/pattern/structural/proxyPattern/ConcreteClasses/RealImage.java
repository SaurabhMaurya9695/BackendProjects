package com.backend.design.pattern.structural.proxyPattern.ConcreteClasses;

import com.backend.design.pattern.structural.proxyPattern.IImage;

public class RealImage implements IImage {

    private final String _imageName;

    public RealImage(final String imageName) {
        _imageName = imageName;
        // performed Heavy operation to load the data from the internet
        // such as download image, compress, save etc ..
        System.out.println("[RealImage] loading the Image from Server");
    }

    @Override
    public void display() {
        System.out.println("[RealImage] Image loaded : " + _imageName);
    }
}
