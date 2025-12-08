CREATE KEYSPACE IF NOT EXISTS SPIKE_KEYSPACE
WITH REPLICATION = {
    'class': 'SimpleStrategy',
    'replication_factor': 1
};

CREATE TABLE SPIKE_KEYSPACE.TABLE_ONE (
    table_id uuid PRIMARY KEY,
    text_field text,
    arr_field list<int>,
    tuple_field tuple<int, int>,
    tuple_list list<frozen <tuple<int, text>>>
);

CREATE INDEX arr_field_ndx ON SPIKE_KEYSPACE.TABLE_ONE (arr_field);

CREATE INDEX tuple_list_index ON SPIKE_KEYSPACE.TABLE_ONE (tuple_list);

CREATE TABLE SPIKE_KEYSPACE.TABLE_TWO (
    table_id uuid,
    location text,
    favorite_date timestamp,
    PRIMARY KEY ( table_id, location )
);