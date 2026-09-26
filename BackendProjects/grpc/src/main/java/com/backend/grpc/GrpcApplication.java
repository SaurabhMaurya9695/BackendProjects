package com.backend.grpc;

import com.backend.grpc.client.UserClient;
import com.backend.grpc.server.GrpcServer;
import com.backend.grpc.services.User;

public class GrpcApplication {

    private static final int PORT = 50051;

    public static void main(String[] args) throws Exception {
        GrpcServer server = new GrpcServer(PORT);
        server.start();

        try (UserClient client = new UserClient("localhost", PORT)) {
            client.getUserAsync(42, new io.grpc.stub.StreamObserver<User>() {
                @Override
                public void onNext(User user) {
                    System.out.printf("Client received: id=%d, name=%s, email=%s%n", user.getId(), user.getName(),
                            user.getEmail());
                }

                @Override
                public void onError(Throwable throwable) {
                    System.out.println("RPC failed: " + throwable.getMessage());
                }

                @Override
                public void onCompleted() {
                    System.out.println("RPC completed");
                }
            });

            Thread.sleep(500);
        } finally {
            server.stop();
        }
    }
}
