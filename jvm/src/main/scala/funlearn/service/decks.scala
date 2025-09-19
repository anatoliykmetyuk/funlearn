package funlearn.service

import com.augustnagro.magnum.*
import funlearn.model.Deck
import javax.sql.DataSource
import com.zaxxer.hikari.HikariDataSource

object decks:
  val dataSource: DataSource =
    val ds = HikariDataSource()
    ds.setJdbcUrl("jdbc:sqlite:../data/database.db")
    ds

  val xa = Transactor(dataSource)

  def getAllDecks(): List[Deck] =
    connect(xa):
      sql"SELECT id, name, description, schema, record_name_key FROM decks ORDER BY id"
        .query[Deck]
        .run()
        .toList

  def createDeck(deck: Deck): Long =
    connect(xa):
      sql"""
        INSERT INTO decks (name, description, schema, record_name_key)
        VALUES (${deck.name}, ${deck.description}, ${deck.schema}, ${deck.recordNameKey})
        RETURNING id
      """
        .returning[Long]
        .run().head
