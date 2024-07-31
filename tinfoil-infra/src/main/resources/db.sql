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


