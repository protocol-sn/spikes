package coop.stlma.protocolsn.spike;

import coop.stlma.tech.protocolsn.spike.GreeterGrpc;
import coop.stlma.tech.protocolsn.spike.HelloReply;
import coop.stlma.tech.protocolsn.spike.HelloRequest;
import io.grpc.stub.StreamObserver;
import jakarta.inject.Singleton;

@Singleton
public class GreetingEndpoint extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        responseObserver.onNext(HelloReply.newBuilder().setMessage("GRPC says hello to " + request.getName()).build());
        responseObserver.onCompleted();
    }
}
