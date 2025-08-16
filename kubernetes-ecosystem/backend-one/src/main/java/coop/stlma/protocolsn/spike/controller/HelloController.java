package coop.stlma.protocolsn.spike.controller;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import coop.stlma.tech.protocolsn.spike.GreeterGrpc;
import coop.stlma.tech.protocolsn.spike.HelloReply;
import coop.stlma.tech.protocolsn.spike.HelloRequest;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Named;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Controller("/")
@Secured(SecurityRule.IS_ANONYMOUS)
public class HelloController {

    private final GreeterGrpc.GreeterFutureStub backendTwoGreeter;
    private final Executor taskExecutor;

    public HelloController(GreeterGrpc.GreeterFutureStub backendTwoGreeter,
                           GreeterGrpc.GreeterBlockingStub backendTwoGreeterBlocking,
                           @Named(TaskExecutors.IO) Executor taskExecutor) {
        this.backendTwoGreeter = backendTwoGreeter;
        this.taskExecutor = taskExecutor;
    }

    @Get
    public String index() {
        return "Hello World";
    }

    @Get("/request")
    public Publisher<String> request() {
        HelloRequest request = HelloRequest.newBuilder().setName("backend one").build();
        return Mono.fromFuture(fromListenableFuture(backendTwoGreeter.sayHello(request), taskExecutor))
                .map(HelloReply::getMessage);
    }

    private <T> CompletableFuture<T> fromListenableFuture(ListenableFuture<T> listenableFuture, Executor executor) {
        CompletableFuture<T> future = new CompletableFuture<>();
        Futures.addCallback(listenableFuture, new FutureCallback<>() {
            @Override
            public void onSuccess(T result) {
                future.complete(result);
            }

            @Override
            public void onFailure(Throwable t) {
                future.completeExceptionally(t);
            }
        }, executor);
        return future;
    }
}
