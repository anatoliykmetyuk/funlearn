package funlearn.services

import funlearn.model.{ Deck, CardType }
import funlearn.db

def IMPL_decks_POST(name: String, description: String,
    labelKey: String, schemaModel: Seq[Map[String, String]]): Long =
  // Schema is a JSON string formed from the keys and prompts
  val schema = upickle.default.write(schemaModel)
  val deck = Deck(-1, name, description, schema, labelKey)

  val deckId = db.decks.createDeck(deck)
  println(s"Deck created, deckId: $deckId")

  // Create the first card type
  val cardType = CardType(-1, "Default", deckId, "", "")
  val cardTypeId = db.cardTypes.createCardType(cardType)
  val newCardType = db.cardTypes.getCardTypeById(cardTypeId)
  println(s"Card type created: $newCardType")

  cardTypeId
end IMPL_decks_POST
