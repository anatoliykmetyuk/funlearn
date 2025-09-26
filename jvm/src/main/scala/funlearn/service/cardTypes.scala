package funlearn.service

import com.augustnagro.magnum.*
import funlearn.model.CardType

object cardTypes:
  val xa = Transactor(dataSource)

  def createCardType(cardType: CardType): Long =
    connect(xa):
      sql"""
        INSERT INTO card_types (name, deck_id, front_tml, back_tml)
        VALUES (${cardType.name}, ${cardType.deckId}, ${cardType.frontTml}, ${cardType.backTml})
        RETURNING id
      """.returning[Long].run().head

  def getCardTypeById(cardTypeId: Long): CardType =
    connect(xa):
      sql"SELECT id, name, deck_id, front_tml, back_tml FROM card_types WHERE id = $cardTypeId"
        .query[CardType].run().head

  def updateCardType(cardType: CardType): Unit =
    connect(xa):
      sql"""
        UPDATE card_types SET name = ${cardType.name}, front_tml = ${cardType.frontTml}, back_tml = ${cardType.backTml} WHERE id = ${cardType.id}
      """.update.run()

  def getCardTypesByDeckId(deckId: Long): List[CardType] =
    connect(xa):
      sql"SELECT id, name, deck_id, front_tml, back_tml FROM card_types WHERE deck_id = $deckId ORDER BY id"
        .query[CardType].run().toList

  def deleteCardType(cardTypeId: Long): Unit =
    connect(xa):
      sql"DELETE FROM card_types WHERE id = $cardTypeId".update.run()