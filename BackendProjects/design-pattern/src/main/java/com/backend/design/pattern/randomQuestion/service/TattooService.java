package com.backend.design.pattern.randomQuestion.service;

import com.backend.design.pattern.randomQuestion.repo.ArtistRepo;

public class TattooService implements FavoriteEntity {

    private final ArtistRepo _artistRepo;

    public TattooService() {
        this._artistRepo = new ArtistRepo();
    }

    @Override
    public void getName() {
        this._artistRepo.loadArtistRepo();
    }
}
