package coop.stlma.tech.protocolsn.spike;

import io.grpc.stub.StreamObserver;
import jakarta.inject.Singleton;

import java.util.stream.Collectors;

@Singleton
public class GreetingEndpoint extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        responseObserver.onNext(HelloReply.newBuilder().setMessage("GRPC says hello to " + request.getName()).build());
        responseObserver.onCompleted();
    }

    @Override
    public void sendValidated(ValidatedRequest request, StreamObserver<HelloReply> responseObserver) {
        responseObserver.onNext(HelloReply.newBuilder().setMessage("Got validated message with uuid " +
                request.getUuidField() + " and fields [" +
                        String.join(", ", request.getStringListFieldList()) + "]")
                .build());
        responseObserver.onCompleted();
    }
}
