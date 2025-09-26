package funlearn.service

import com.augustnagro.magnum.*
import funlearn.model.Deck

object decks:
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

  def getDeckById(deckId: Long): Deck =
    connect(xa):
      sql"SELECT id, name, description, schema, record_name_key FROM decks WHERE id = $deckId"
        .query[Deck].run().head

  def updateDeck(deck: Deck): Unit =
    connect(xa):
      sql"""
        UPDATE decks SET name = ${deck.name}, description = ${deck.description},
        schema = ${deck.schema}, record_name_key = ${deck.recordNameKey} WHERE id = ${deck.id}
      """.update.run()

  def deleteDeck(deckId: Long): Unit =
    connect(xa):
      sql"DELETE FROM decks WHERE id = $deckId".update.run()