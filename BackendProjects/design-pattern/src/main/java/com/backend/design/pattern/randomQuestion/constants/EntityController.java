package com.backend.design.pattern.randomQuestion.constants;

import com.backend.design.pattern.randomQuestion.controller.EntityServiceFactory;
import com.backend.design.pattern.randomQuestion.service.FavoriteEntity;

public class EntityController {

    private final EntityServiceFactory _entityServiceFactory;

    public EntityController() {
        this._entityServiceFactory = new EntityServiceFactory();
    }

    public void handle(TypeOfEntity entityType) {
        System.out.println("EntityType is: " + entityType);
        FavoriteEntity type = _entityServiceFactory.getService(entityType);

        if (type == null) {
            System.out.println("Invalid Entity Type!");
            return;
        }

        System.out.println("Type Name is: " + type.getClass().getSimpleName());
        type.getName();
    }
}
