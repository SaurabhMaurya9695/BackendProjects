package com.backend.design.pattern.randomQuestion.service;

import com.backend.design.pattern.randomQuestion.repo.DesignerRepo;

public class DesignerService implements FavoriteEntity {

    private final DesignerRepo _designerRepo;

    public DesignerService() {
        this._designerRepo = new DesignerRepo();
    }

    @Override
    public void getName() {
        this._designerRepo.loadDesignerRepo();
    }
}
