-- Create all tables for flashcard application

-- Decks table
CREATE TABLE decks (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    description TEXT NOT NULL,
    schema TEXT NOT NULL, -- JSON string
    record_name_key TEXT NOT NULL
);

-- Records table
CREATE TABLE records (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    deck_id INTEGER NOT NULL,
    fields TEXT NOT NULL, -- JSON string
    flagged BOOLEAN NOT NULL DEFAULT 0,
    staged BOOLEAN NOT NULL DEFAULT 0,
    FOREIGN KEY (deck_id) REFERENCES decks(id)
);

-- Card types table
CREATE TABLE card_types (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    deck_id INTEGER NOT NULL,
    front_tml TEXT NOT NULL, -- HTML template
    back_tml TEXT NOT NULL, -- HTML template
    FOREIGN KEY (deck_id) REFERENCES decks(id)
);

-- Cards table
CREATE TABLE cards (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    record_id INTEGER NOT NULL,
    card_type_id INTEGER NOT NULL,
    deck_id INTEGER NOT NULL,
    FOREIGN KEY (record_id) REFERENCES records(id),
    FOREIGN KEY (card_type_id) REFERENCES card_types(id),
    FOREIGN KEY (deck_id) REFERENCES decks(id)
);

-- Schedules table
CREATE TABLE schedules (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    card_id INTEGER NOT NULL,
    next_rep TIMESTAMP NOT NULL,
    interval_sec INTEGER NOT NULL,
    ease REAL NOT NULL,
    FOREIGN KEY (card_id) REFERENCES cards(id)
);

