package coop.stmla.tech.protocolsn.spike;

import com.datastax.oss.driver.api.core.data.TupleValue;

import java.util.List;
import java.util.UUID;

public class TableOneEntity {
    private UUID tableId;

    private String textField;

    private List<Integer> arrField;

    private TupleValue tupleField;

    private List<TupleValue> tupleList;

    public TableOneEntity() {}

    public TableOneEntity(UUID tableId,
                          String textField,
                          List<Integer> arrField,
                          TupleValue tupleField,
                          List<TupleValue> tupleList) {
        this.tableId = tableId;
        this.textField = textField;
        this.arrField = arrField;
        this.tupleField = tupleField;
        this.tupleList = tupleList;
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

    public TupleValue getTupleField() {
        return tupleField;
    }

    public void setTupleField(TupleValue tupleField) {
        this.tupleField = tupleField;
    }

    public List<TupleValue> getTupleList() {
        return tupleList;
    }

    public void setTupleList(List<TupleValue> tupleList) {
        this.tupleList = tupleList;
    }
}
