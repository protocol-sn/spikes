package coop.stlma.tech.protocolsn.spike;

import io.grpc.ManagedChannel;
import io.micronaut.context.annotation.Factory;
import io.micronaut.grpc.annotation.GrpcChannel;
import jakarta.inject.Singleton;

@Factory
public class GrpcClientFactory {

    @Singleton
    GreeterGrpc.GreeterFutureStub reactiveStub(@GrpcChannel("greeter") ManagedChannel channel) {
        return GreeterGrpc.newFutureStub(channel);
    }
}
