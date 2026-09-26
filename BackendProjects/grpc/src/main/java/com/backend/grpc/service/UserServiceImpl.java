package com.backend.grpc.service;

import com.backend.grpc.services.GetUserRequest;
import com.backend.grpc.services.User;
import com.backend.grpc.services.UserServiceGrpc;
import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    @Override
    public void getUser(GetUserRequest request, StreamObserver<User> responseObserver) {

        try {
            // Thread.sleep(5000); - to simulate the DEADLINESS of a code
            if (Context.current().isCancelled()) {
                System.out.println("Client deadline expired or call was cancelled");
                return;
            }

            User user = User.newBuilder().setId(request.getUserId()).setName("User " + request.getUserId()).setEmail(
                    "user" + request.getUserId() + "@okay.com").build();

            responseObserver.onNext(user);
            responseObserver.onCompleted();
        } catch (Exception e) {
            // Thread.currentThread().interrupt();
            responseObserver.onError(Status.CANCELLED.withDescription("Request was interrupted").withCause(e)
                    .asRuntimeException());
        }
    }
}
