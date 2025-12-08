package coop.stmla.tech.protocolsn.spike;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.Statement;
import com.datastax.oss.driver.api.core.data.TupleValue;
import com.datastax.oss.driver.internal.core.type.DefaultTupleType;
import com.datastax.oss.driver.internal.core.type.PrimitiveType;
import com.datastax.oss.protocol.internal.ProtocolConstants;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.UUID;

@Singleton
public class TableOneRepository {
    public static final DefaultTupleType TWO_INTS_TUPLE_TYPE = new DefaultTupleType(List.of(
            new PrimitiveType(ProtocolConstants.DataType.INT),
            new PrimitiveType(ProtocolConstants.DataType.INT)));
    public static final DefaultTupleType INT_STRING_TUPLE_TYPE = new DefaultTupleType(List.of(
            new PrimitiveType(ProtocolConstants.DataType.INT),
            new PrimitiveType(ProtocolConstants.DataType.VARCHAR)));
    private final CqlSession session;
    private final PreparedStatement selectStatement;
    private final PreparedStatement insertStatement;

    public TableOneRepository(CqlSession session) {
        this.session = session;

        insertStatement = session.prepare("""
                INSERT INTO spike_keyspace.table_one (
                                        table_id,
                                        arr_field,
                                        text_field,
                                        tuple_field,
                                        tuple_list) 
                                        VALUES
                                        (?, ?, ?, ?, ?)
                """);
        selectStatement = session.prepare("""
                SELECT * 
                FROM spike_keyspace.table_one 
                WHERE table_id = ? 
                """);
    }

    public TableOneEntity insert(TableOneEntity entity) {
        UUID uuid = UUID.randomUUID();

        Statement<BoundStatement> bound = insertStatement.bind(uuid, entity.getArrField(), entity.getTextField(), entity.getTupleField(), entity.getTupleList());
        session.execute(bound);

        return getById(uuid);
    }

    public TableOneEntity getById(UUID id) {
        Statement<BoundStatement> bound = selectStatement.bind(id);
        ResultSet result = session.execute(bound);

        Row foundItem = result.one();

        if (foundItem != null) {
            return new TableOneEntity(
                    foundItem.getUuid("table_id"),
                    foundItem.getString("text_field"),
                    foundItem.getList("arr_field", Integer.class),
                    foundItem.getTupleValue("tuple_field"),
                    foundItem.getList("tuple_list", TupleValue.class));
        }
        return null;
    }
}
