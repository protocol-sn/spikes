package coop.stlma.tech.protocolsn.spike;

import build.buf.protovalidate.ValidationResult;
import build.buf.protovalidate.Validator;
import build.buf.protovalidate.ValidatorFactory;
import build.buf.protovalidate.exceptions.ValidationException;
import com.google.protobuf.Message;
import io.grpc.ForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import jakarta.inject.Singleton;

import java.util.stream.Collectors;

@Singleton
public class GrpcValidator implements ServerInterceptor {

    private final Validator validator = ValidatorFactory.newBuilder().build();

    @Override
    public <Q, R> ServerCall.Listener<Q> interceptCall(ServerCall<Q, R> serverCall,
                                                       Metadata metadata, ServerCallHandler<Q, R> serverCallHandler) {

        return new ValidationServerCallListener<>(validator, serverCall, serverCallHandler.startCall(serverCall, metadata));
    }

    private static class ValidationServerCallListener<Q, R> extends ForwardingServerCallListener.SimpleForwardingServerCallListener<Q> {

        private final Validator validator;
        private final ServerCall<Q, R> call;

        protected ValidationServerCallListener(Validator validator,
                                               ServerCall<Q, R> call,
                                               ServerCall.Listener<Q> delegate) {
            super(delegate);
            this.validator = validator;
            this.call = call;
        }

        @Override
        public void onHalfClose() {
            if (this.call.isReady()) {
                super.onHalfClose();
            }
        }

        @Override
        public void onMessage(Q message) {
            if (!(message instanceof Message)) {
                throw new IllegalArgumentException("GRPC validation requires type " + Message.class.getName());
            }

            try {
                ValidationResult validationResult = validator.validate((com.google.protobuf.Message) message);

                if (validationResult.isSuccess()) {
                    super.onMessage(message);
                } else {
                    Status status = Status.Code.INVALID_ARGUMENT.toStatus()
                            .withDescription(validationResult.getViolations().stream()
                                    .map(violation -> {
                                        return violation.toProto().getMessage();
                                    })
                                    .collect(Collectors.joining(",")));
//                    .setMessage(Code.INVALID_ARGUMENT.getValueDescriptor().getName())
//                            .addDetails(Any.pack(validationResult.toProto()))
//                            .build();
                    StatusRuntimeException sre = status.asRuntimeException();
                    call.close(sre.getStatus(), new Metadata());
                }
            } catch (ValidationException e) {
//                Status status = Status.newBuilder()
//                        .setCode(Code.INTERNAL_VALUE)
//                        .setMessage(e.getMessage())
//                        .build();

                throw Status.fromThrowable(e).asRuntimeException();
            }
        }
    }
}
