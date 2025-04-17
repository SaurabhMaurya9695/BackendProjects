package com.backend;

import com.backend.design.pattern.AbstactFactory.MainAbstractFactoryEntryPoint;
import com.backend.design.pattern.AbstactFactory.factory.MacFactory;
import com.backend.design.pattern.AbstactFactory.factory.WindowsFactory;

import java.io.IOException;

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

        /* TODO  Abstract Design Pattern
            MainAbstractFactoryEntryPoint _factory = new MainAbstractFactoryEntryPoint(new WindowsFactory());
            _factory.paintOs();
            MainAbstractFactoryEntryPoint _factory = new MainAbstractFactoryEntryPoint(new MacFactory());
            _factory.paintOs();

         */

    }
}
