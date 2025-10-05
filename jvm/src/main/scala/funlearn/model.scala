package funlearn.model

import com.augustnagro.magnum.*

@Table(SqliteDbType, SqlNameMapper.CamelToSnakeCase)
case class Deck(
  @Id id: Long,
  name: String,
  description: String,
  schema: String, // JSON string
  recordNameKey: String
)

@Table(SqliteDbType, SqlNameMapper.CamelToSnakeCase)
case class CardType(
  @Id id: Long,
  name: String,
  deckId: Long,
  frontTml: String,
  backTml: String
)