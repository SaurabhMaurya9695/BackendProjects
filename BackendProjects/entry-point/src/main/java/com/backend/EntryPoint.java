package com.backend;

import com.backend.design.pattern.factory.OperatingSystem;
import com.backend.design.pattern.factory.OperatingSystemFactory;
import com.backend.design.pattern.randomQuestion.constants.EntityController;
import com.backend.design.pattern.randomQuestion.constants.TypeOfEntity;
import com.backend.design.pattern.sqlQueryOptimization.FetchingData;
import com.backend.design.pattern.sqlQueryOptimization.MakeSqlConnection;

import java.io.IOException;
import java.util.List;

public class EntryPoint {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        /* TODO - SingletonEntryPoint.exampleSerialization();

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

        /*
         TODO - write a method where we can just pass the userId and based on that we can fetch
          Fetch all liked entities for user 1
          FetchingData.getLikedEntities(1, null);
        */

        /* TODO - FactoryDesignPattern
            OperatingSystemFactory osFactory = new OperatingSystemFactory();
            OperatingSystem windowsOperatingSystem = osFactory.getOperatingSystem("1.0.0.", "x64", "windows");
            windowsOperatingSystem.deleteDirectory();
            windowsOperatingSystem.setDirectory();
            System.out.print("-----------------------------------------\n");
            OperatingSystem linuxOperatingSystem = osFactory.getOperatingSystem("1.0.0.", "x64", "linux");
            linuxOperatingSystem.deleteDirectory();
            linuxOperatingSystem.setDirectory();

         */

    }
}
