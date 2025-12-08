package coop.stmla.tech.protocolsn.spike;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Map;

@MicronautTest(startApplication = false)
@Testcontainers(disabledWithoutDocker = true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TableOneRepositoryTest implements TestPropertyProvider {

    @Inject
    TableOneRepository pluginRepository;

    @Container
    public static final CassandraContainer<?> cassandra
            = new CassandraContainer<>("cassandra:5").withExposedPorts(9042);

    @Test
    void testInsertRecord_happyPath() {
        TableOneEntity insertMe = new TableOneEntity(
                null,
                "some text",
                List.of(2, 89),
                TableOneRepository.TWO_INTS_TUPLE_TYPE.newValue(1, 7),
                List.of(TableOneRepository.INT_STRING_TUPLE_TYPE.newValue(0, "foo"), TableOneRepository.INT_STRING_TUPLE_TYPE.newValue(1, "bar"))
        );
        TableOneEntity result = pluginRepository.insert(insertMe);

        Assertions.assertNotNull(result.getTableId());
        Assertions.assertEquals(insertMe.getTextField(), result.getTextField());
        Assertions.assertEquals(insertMe.getArrField(), result.getArrField());
        Assertions.assertEquals(insertMe.getTupleField(), result.getTupleField());
        Assertions.assertEquals(insertMe.getTupleList(), result.getTupleList());
    }
    @Override
    public @NonNull Map<String, String> getProperties() {
        if (!cassandra.isRunning()) {
            cassandra.start();
        }

        Session keyspaceSession = Cluster.builder()
                .addContactPoint(cassandra.getHost() )
                .withPort(cassandra.getMappedPort(9042))
                .build().connect();

        //In other cases Flyway can be set to create a schema on connecting for the first time, but it doesn't seem to
        //have this capability for Cassandra. Understandable as Cassandra keyspaces are a bit more complicated than a
        //PSQL schema. As a result we must create the keyspace here before the application context fires up Flyway or
        //our migrations won't work.
        keyspaceSession.execute("""
                CREATE KEYSPACE IF NOT EXISTS SPIKE_KEYSPACE
                WITH REPLICATION = {
                    'class': 'SimpleStrategy',
                    'replication_factor': 1
                };
                """);
        keyspaceSession.close();

        return Map.of("cassandra.default.basic.contact-points[0]", cassandra.getHost() + ":" + cassandra.getMappedPort(9042),
                    "cassandra.default.basic.session-keyspace", "SPIKE_KEYSPACE",
                    "cassandra.default.basic.load-balancing-policy.local-datacenter", "datacenter1",
                "flyway.datasources.default.enabled", "true",
                "flyway.datasources.default.url", "jdbc:cassandra://" + cassandra.getHost() + ":" + cassandra.getMappedPort(9042) + "/spike_keyspace?localdatacenter=datacenter1");
    }
}
