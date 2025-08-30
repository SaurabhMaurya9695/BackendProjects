package com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.managers;

import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.strategies.SequentialPlayStrategy;
import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.strategies.RandomPlayStrategy;
import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.strategies.CustomQueueStrategy;
import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.strategies.PlayStrategy;
import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.enums.PlayStrategyType;

public class StrategyManager {

    private static StrategyManager _instance = null;
    private SequentialPlayStrategy _sequentialPlayStrategy;
    private RandomPlayStrategy _randomPlayStrategy;
    private CustomQueueStrategy _customQueueStrategy;

    private StrategyManager() {
        _sequentialPlayStrategy = new SequentialPlayStrategy();
        _randomPlayStrategy = new RandomPlayStrategy();
        _customQueueStrategy = new CustomQueueStrategy();
    }

    public static synchronized StrategyManager getInstance() {
        if (_instance == null) {
            _instance = new StrategyManager();
        }
        return _instance;
    }

    public PlayStrategy getStrategy(PlayStrategyType type) {
        if (type == PlayStrategyType.SEQUENTIAL) {
            return _sequentialPlayStrategy;
        } else if (type == PlayStrategyType.RANDOM) {
            return _randomPlayStrategy;
        } else {
            return _customQueueStrategy;
        }
    }
}
