package coop.stmla.tech.protocolsn.spike;

import com.datastax.oss.driver.api.core.data.TupleValue;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Introspected
@Serdeable
public class TableOneModel {
    private UUID tableId;

    private String textField;

    private List<Integer> arrField;

    private Integer tupleFieldKey;

    private Integer tupleFieldValue;

    private Map<Integer, String> tupleList;

    public TableOneModel() {}

    public TableOneModel(UUID tableId,
                          String textField,
                          List<Integer> arrField,
                          Map<Integer, Integer> tupleField,
                          Map<Integer, String> tupleList) {
        this.tableId = tableId;
        this.textField = textField;
        this.arrField = arrField;
        this.tupleFieldKey = tupleField.get(0);
        this.tupleFieldValue = tupleField.get(1);
        this.tupleList = tupleList;
    }

    public TableOneModel(UUID tableId,
                         String textField,
                         List<Integer> arrField,
                         TupleValue tupleField,
                         List<TupleValue> tupleList) {
        this.tableId = tableId;
        this.textField = textField;
        this.arrField = arrField;
        this.tupleFieldKey = tupleField.getInt(0);
        this.tupleFieldValue = tupleField.getInt(1);
        this.tupleList = new HashMap<>();
        tupleList.forEach(tupleValue -> this.tupleList.put(tupleValue.getInt(0), tupleValue.getString(1)));
    }

    public UUID getTableId() {
        return tableId;
    }

    public void setTableId(UUID tableId) {
        this.tableId = tableId;
    }

    public String getTextField() {
        return textField;
    }

    public void setTextField(String textField) {
        this.textField = textField;
    }

    public List<Integer> getArrField() {
        return arrField;
    }

    public void setArrField(List<Integer> arrField) {
        this.arrField = arrField;
    }

    public Map<Integer, String> getTupleList() {
        return tupleList;
    }

    public void setTupleList(Map<Integer, String> tupleList) {
        this.tupleList = tupleList;
    }

    public Integer getTupleFieldKey() {
        return tupleFieldKey;
    }

    public void setTupleFieldKey(Integer tupleFieldKey) {
        this.tupleFieldKey = tupleFieldKey;
    }

    public Integer getTupleFieldValue() {
        return tupleFieldValue;
    }

    public void setTupleFieldValue(Integer tupleFieldValue) {
        this.tupleFieldValue = tupleFieldValue;
    }
}
