package com.backend.design.pattern.bridgePattern;

import com.backend.design.pattern.bridgePattern.processor.VideoProcessor;

// one video has different-different types of VideoProcessor & VideoProcessor has 8k , 4k processor
public abstract class Video {

    protected VideoProcessor _videoProcessor;

    public Video(VideoProcessor videoProcessor) {
        this._videoProcessor = videoProcessor;
    }

    public abstract void play(String fileName);
}
