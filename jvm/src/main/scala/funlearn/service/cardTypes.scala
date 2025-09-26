package funlearn.service

import com.augustnagro.magnum.*
import funlearn.model.CardType

object cardTypes:
  val xa = Transactor(dataSource)

  def createCardType(cardType: CardType): Long =
    connect(xa):
      sql"""
        INSERT INTO card_types (deck_id, front_tml, back_tml)
        VALUES (${cardType.deckId}, ${cardType.frontTml}, ${cardType.backTml})
        RETURNING id
      """
        .returning[Long]
        .run().head

  def getCardTypeById(cardTypeId: Long): CardType =
    connect(xa):
      sql"SELECT id, deck_id, front_tml, back_tml FROM card_types WHERE id = $cardTypeId"
        .query[CardType]
        .run().head