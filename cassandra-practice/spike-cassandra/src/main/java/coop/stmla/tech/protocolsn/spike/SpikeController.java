package coop.stmla.tech.protocolsn.spike;

import com.datastax.oss.driver.api.core.data.TupleValue;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class SpikeController {
    private final TableOneRepository tableOneRepository;

    public SpikeController(TableOneRepository tableOneRepository) {
        this.tableOneRepository = tableOneRepository;
    }

    @Get("/{id}")
    public TableOneModel getOneById(@PathVariable UUID id) {
        TableOneEntity entity = tableOneRepository.getById(id);
        return fromOneEntity(entity);
    }

    @Post
    public TableOneModel postOne(@Body TableOneModel tableOneEntity) {
        return fromOneEntity(tableOneRepository.insert(fromOneModel(tableOneEntity)));
    }

    private TableOneEntity fromOneModel(TableOneModel model) {
        TupleValue tuple = TableOneRepository.TWO_INTS_TUPLE_TYPE.newValue(model.getTupleFieldKey(), model.getTupleFieldValue());

        List<TupleValue> tuplesList = new ArrayList<>();
        model.getTupleList().forEach((integer, s) -> {
            tuplesList.add(TableOneRepository.INT_STRING_TUPLE_TYPE.newValue(integer, s));
        });

        return new TableOneEntity(
                model.getTableId(),
                model.getTextField(),
                model.getArrField(),
                tuple,
                tuplesList
        );
    }

    private TableOneModel fromOneEntity(TableOneEntity entity) {
        return new TableOneModel(entity.getTableId(), entity.getTextField(), entity.getArrField(), entity.getTupleField(), entity.getTupleList());

    }
}
