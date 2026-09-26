package com.backend.grpc.client;

import com.backend.grpc.services.GetUserRequest;
import com.backend.grpc.services.User;
import com.backend.grpc.services.UserServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;

public class UserClient implements AutoCloseable {

    private final ManagedChannel channel;
    private final UserServiceGrpc.UserServiceStub userService;

    public UserClient(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build(); // since server is already staretd by server, client just need to connect with that
        // using channel, since we already started or connected to the server connection, for accessing the sever
        // method etc we need a stub for that from client
        //A stub is basically the client-side generated API through which we invoke the remote service.

        // This code is a blocking code which waits for the response from the code
        // this.userService = UserServiceGrpc.newBlockingStub(channel).withDeadlineAfter(3, TimeUnit.SECONDS);

        // This code is a non-blocking call; the response is delivered to StreamObserver callbacks.
        this.userService = UserServiceGrpc.newStub(channel);
    }

    public void getUserAsync(long userId, StreamObserver<User> responseObserver) {
        GetUserRequest request = GetUserRequest.newBuilder().setUserId(userId).build();
        try {
            // User user = userService.getUser(request); - this code we'll use when using a blocking stub
            // System.out.println("Client received: " + "id=" + user.getId() + ", name=" + user.getName() + ", email="
            //       + user.getEmail());

            userService.getUser(request, new StreamObserver<User>() {
                @Override
                public void onNext(User user) {
                    responseObserver.onNext(user);
                }

                @Override
                public void onError(Throwable throwable) {
                    responseObserver.onError(throwable);
                }

                @Override
                public void onCompleted() {
                    responseObserver.onCompleted();
                }
            });
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.DEADLINE_EXCEEDED) {
                System.out.println("The server did not respond within the deadline");
            } else {
                System.out.println("RPC failed: " + e.getStatus());
            }
        }

    }

    @Override
    public void close() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }
}
