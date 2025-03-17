package com.backend.design.pattern.randomQuestion.controller;

import com.backend.design.pattern.randomQuestion.constants.TypeOfEntity;
import com.backend.design.pattern.randomQuestion.service.*;

import java.util.HashMap;
import java.util.Map;

public class EntityServiceFactory {

    private final Map<TypeOfEntity, FavoriteEntity> _entityServicesMapped;

    public EntityServiceFactory() {
        this._entityServicesMapped = new HashMap<>();
        loadHandle(new TattooService());
        loadHandle(new StudioService());
        loadHandle(new DesignerService());
    }

    private void loadHandle(FavoriteEntity service) {
        if (service instanceof TattooService) {
            this._entityServicesMapped.put(TypeOfEntity.ARTIST, service);
        } else if (service instanceof StudioService) {
            this._entityServicesMapped.put(TypeOfEntity.STUDIO, service);
        } else if (service instanceof DesignerService) {
            this._entityServicesMapped.put(TypeOfEntity.DESIGNER, service);
        }
    }

    public FavoriteEntity getService(TypeOfEntity type) {
        return _entityServicesMapped.get(type);
    }
}
