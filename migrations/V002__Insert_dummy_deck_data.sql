-- Insert dummy deck data
INSERT INTO decks (name, schema, record_name_key) VALUES
    ('Vocab', '{"fields": [{"name": "japanese", "label": "Japanese", "prompt": "Extract the Japanese word or phrase"}, {"name": "english", "label": "English", "prompt": "Extract the English translation"}]}', 'japanese'),
    ('Kanji', '{"fields": [{"name": "character", "label": "Kanji Character", "prompt": "Extract the kanji character"}, {"name": "reading", "label": "Reading", "prompt": "Extract the hiragana/katakana reading"}, {"name": "meaning", "label": "Meaning", "prompt": "Extract the English meaning"}]}', 'character'),
    ('Grammar', '{"fields": [{"name": "pattern", "label": "Grammar Pattern", "prompt": "Extract the grammar pattern or structure"}, {"name": "example", "label": "Example", "prompt": "Extract an example sentence"}, {"name": "explanation", "label": "Explanation", "prompt": "Extract the grammar explanation"}]}', 'pattern');
