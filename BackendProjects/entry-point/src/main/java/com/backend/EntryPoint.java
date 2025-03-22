package com.backend;

import com.backend.design.pattern.randomQuestion.constants.EntityController;
import com.backend.design.pattern.randomQuestion.constants.TypeOfEntity;
import com.backend.design.pattern.sqlQueryOptimization.FetchingData;
import com.backend.design.pattern.sqlQueryOptimization.MakeSqlConnection;

import java.io.IOException;
import java.util.List;

public class EntryPoint {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        // TODO - SingletonEntryPoint.exampleSerialization();

        /** TODO - FactoryDesignPattern
             EntityController controller = new EntityController();
             controller.handle(TypeOfEntity.ARTIST);
             controller.handle(TypeOfEntity.STUDIO);
             controller.handle(TypeOfEntity.DESIGNER);
        */

        /** TODO - it will create a connection to db and created a table name studio and artist
             MakeSqlConnection sqlConnection = new MakeSqlConnection();
             sqlConnection.MakeConnection();
         */

        // write a method where we can just pass the userId and based on that we can fetch

        // Fetch all liked entities for user 1
        FetchingData.getLikedEntities(1, null);

        // Fetch only artists liked by user 1
//        FetchingData.getLikedEntities(1, "ARTIST");
//
//        // Fetch only studios liked by user 1
//        FetchingData.getLikedEntities(1, "STUDIO");
    }
}
