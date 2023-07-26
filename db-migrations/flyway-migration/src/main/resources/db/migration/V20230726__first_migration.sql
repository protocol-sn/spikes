DROP TABLE IF EXISTS mytable;

CREATE TABLE IF NOT EXISTS mytable (

    id SERIAL PRIMARY KEY,
    field1 varchar(20),
    field2 int,
    date_of_birth timestamp

);