\c postgres;

DROP DATABASE IF EXISTS tinfoil_test_db;
CREATE DATABASE tinfoil_test_db;
GRANT ALL PRIVILEGES ON DATABASE tinfoil_test_db TO justin;

DROP DATABASE IF EXISTS tinfoil_db;
CREATE DATABASE tinfoil_db;
GRANT ALL PRIVILEGES ON DATABASE tinfoil_db TO justin;

\c tinfoil_db;

CREATE TABLE customer (
    name TEXT PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);


CREATE TABLE store (
    name TEXT PRIMARY KEY,
    country TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);


CREATE TABLE visit (
    customer TEXT REFERENCES customer(name),
    store TEXT REFERENCES store(name),
    visited_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

\c tinfoil_test_db;

CREATE TABLE customer (
    name TEXT PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);


CREATE TABLE store (
    name TEXT PRIMARY KEY,
    country TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);


CREATE TABLE visit (
    customer TEXT REFERENCES customer(name),
    store TEXT REFERENCES store(name),
    visited_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
