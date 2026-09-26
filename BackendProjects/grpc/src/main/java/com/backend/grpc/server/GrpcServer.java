package com.backend.grpc.server;

import com.backend.grpc.service.UserServiceImpl;
import io.grpc.Server;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;

import java.io.IOException;

public class GrpcServer {

    private final Server server;

    public GrpcServer(int port) {
        this.server = NettyServerBuilder.forPort(port)
                .addService(new UserServiceImpl())
                .build();
    }

    public void start() throws IOException {
        server.start();
        System.out.println("gRPC server started on port " + server.getPort());
    }

    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    public void awaitTermination() throws InterruptedException {
        server.awaitTermination();
    }
}
