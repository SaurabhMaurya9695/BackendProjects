package com.backend.design.pattern.randomQuestion.service;

import com.backend.design.pattern.randomQuestion.repo.StudioRepo;

public class StudioService implements FavoriteEntity {

    private final StudioRepo _studioRepo;

    public StudioService() { // Changed from private to public
        this._studioRepo = new StudioRepo();
    }

    @Override
    public void getName() {
        this._studioRepo.loadStudioRepo();
    }
}
