package com.backend.design.pattern.bridgePattern;

import com.backend.design.pattern.bridgePattern.processor.VideoProcessor;

public class PrimeVideo extends Video {

    private final VideoProcessor _videoProcessor;

    public PrimeVideo(VideoProcessor videoProcessor) {
        super(videoProcessor);
        _videoProcessor = videoProcessor;
    }

    @Override
    public void play(String fileName) {
        _videoProcessor.play(fileName);
    }
}
