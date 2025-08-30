package com.backend.design.pattern.structural.proxyPattern.Proxy;

import com.backend.design.pattern.structural.proxyPattern.ConcreteClasses.RealImage;
import com.backend.design.pattern.structural.proxyPattern.IImage;

// this class should treat as a Proxy -> this class will look like the RealImage class but it performed the lazy loading
public class ImageProxy implements IImage {

    private RealImage _realImage;
    private String _imageName;

    public ImageProxy(String imageName) {
        _imageName = imageName;
        _realImage = null;
    }

    @Override
    public void display() {
        // when client calls this display method then we will load all the content from the server etc .. heavy task
        if (_realImage == null) {
            _realImage = new RealImage(_imageName);
        }
        _realImage.display();
    }
}
