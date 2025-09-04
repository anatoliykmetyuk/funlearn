package funlearn.model

case class Card(front: String, back: String)

case class Deck(
  id: Long,
  name: String,
  schema: String, // JSON string
  recordNameKey: String
)