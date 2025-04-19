package com.backend;

import com.backend.design.pattern.AbstactFactory.MainAbstractFactoryEntryPoint;
import com.backend.design.pattern.AbstactFactory.factory.MacFactory;
import com.backend.design.pattern.AbstactFactory.factory.WindowsFactory;
import com.backend.design.pattern.adaptorPattern.Adaptor.GroceryItemAdaptor;
import com.backend.design.pattern.adaptorPattern.Item1.FoodItem;
import com.backend.design.pattern.adaptorPattern.Item2.GroceryProduct;
import com.backend.design.pattern.adaptorPattern.SwiggyStore;
import com.backend.design.pattern.bridgePattern.NetflixVideo;
import com.backend.design.pattern.bridgePattern.PrimeVideo;
import com.backend.design.pattern.bridgePattern.Video;
import com.backend.design.pattern.bridgePattern.processor.EightKProcessor;
import com.backend.design.pattern.bridgePattern.processor.fourKProcessor;
import com.backend.design.pattern.builderPattern.Burger;
import com.backend.design.pattern.prototypePattern.Vehicle;
import com.backend.design.pattern.prototypePattern.VehicleRegistry;

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

        /* TODO - BuilderDesignPattern
            Burger burger = new Burger.BurgerBuilder()
                                        .egg(true)
                                        .extraCheese(true)
                                        .mayonese(true)
                                        .size("2")
                                        .onion(true)
                                         .build();
         */

        /* Todo -> Prototype Model
            VehicleRegistry vehicleRegistry = new VehicleRegistry();
            try {
                Vehicle vehicle = vehicleRegistry.getVehicle("TWO");
                System.out.println("Vehicle: " + vehicle.hashCode());
                System.out.println("Engine type : " + vehicle.getEngine());
                System.out.println("Model type : " + vehicle.getModel());
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
         */


        /* TODO -> Adaptor design pattern
            SwiggyStore swiggyStore = new SwiggyStore();
            swiggyStore.addItems(new FoodItem());
            swiggyStore.addItems(new FoodItem());
            swiggyStore.addItems(new GroceryItemAdaptor(new GroceryProduct()));
            swiggyStore.printItems();
         */

        /* TODO - Bridge Design Pattern
            Video netflixVideos = new NetflixVideo(new EightKProcessor());
            netflixVideos.play("teraHua.mp4");
            Video primeVideos = new PrimeVideo(new fourKProcessor());
            primeVideos.play("prime.mp4");
        */



    }
}
