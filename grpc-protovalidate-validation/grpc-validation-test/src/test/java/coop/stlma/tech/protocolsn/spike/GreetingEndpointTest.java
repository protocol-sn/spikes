package coop.stlma.tech.protocolsn.spike;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

@MicronautTest
class GreetingEndpointTest {

    @Inject
    GreeterGrpc.GreeterBlockingStub client;

    UUID testUUID = UUID.nameUUIDFromBytes("validationTest".getBytes());

    @Test
    void testGreet_happyPath() {
        HelloRequest req = HelloRequest.newBuilder()
                .setName("Fred")
                .build();
        HelloReply reply = client.sayHello(req);

        Assertions.assertEquals("GRPC says hello to Fred", reply.getMessage());
    }

    @Test
    void testSendValidated_happyPath() {
        ValidatedRequest req = ValidatedRequest.newBuilder()
                .setUuidField(testUUID.toString())
                .addStringListField("String 1")
                .addStringListField("String 2")
                .build();

        HelloReply reply = client.sendValidated(req);

        Assertions.assertEquals("Got validated message with uuid " + testUUID + " and fields [String 1, String 2]", reply.getMessage());
    }

    @Test
    void testSendValidated_uuidFails() {
        ValidatedRequest req = ValidatedRequest.newBuilder()
                .setUuidField("validationTest")
                .addStringListField("String 1")
                .addStringListField("String 2")
                .build();

        StatusRuntimeException thrown = Assertions.assertThrows(StatusRuntimeException.class, () -> client.sendValidated(req));

        Assertions.assertEquals(Status.Code.INVALID_ARGUMENT, thrown.getStatus().getCode());
        Assertions.assertEquals("value must be a valid UUID", thrown.getStatus().getDescription());
    }

    @Test
    void testSendValidated_multipleFails() {
        ValidatedRequest req = ValidatedRequest.newBuilder()
                .setUuidField("validationTest")
                .addStringListField("String")
                .addStringListField("String")
                .build();

        StatusRuntimeException thrown = Assertions.assertThrows(StatusRuntimeException.class, () -> client.sendValidated(req));

        Assertions.assertEquals(Status.Code.INVALID_ARGUMENT, thrown.getStatus().getCode());
        Assertions.assertEquals("value must be a valid UUID,All strings in the list must be unique", thrown.getStatus().getDescription());
    }
}
