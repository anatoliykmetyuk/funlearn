package funlearn.services

import funlearn.model.{ Deck, CardType }
import funlearn.db.{decksRepo, cardTypesRepo}


object IMPL_decks_POST:
  case class Data(name: String, description: String,
      labelKey: String, schemaModel: Seq[Map[String, String]])

  def mkData(body: Seq[(String, String)]): Data =
    val name = body.find(_._1 == "name").map(_._2).get
    val description = body.find(_._1 == "description").map(_._2).get
    val labelKeyId = body.find(_._1 == "label_key").map(_._2).get
    val labelKey = body.find(_._1 == s"keys[$labelKeyId]").map(_._2).get

    val keys = body.filter(_._1.startsWith("keys")).map(_._2)
    val prompts = body.filter(_._1.startsWith("prompts")).map(_._2)
    val schemaModel = keys.zip(prompts).map { case (k, p) => Map("name" -> k, "prompt" -> p) }

    Data(name, description, labelKey, schemaModel)

  def impl(data: Data): Long =
    import data.*
    // Schema is a JSON string formed from the keys and prompts
    val schema = upickle.default.write(schemaModel)
    val deck = Deck(-1, name, description, schema, labelKey)

    val deckId = decksRepo.insertReturning(deck).id
    println(s"Deck created, deckId: $deckId")

    // Create the first card type
    val cardType = CardType(-1, "Default", deckId, "", "")
    val cardTypeId = cardTypesRepo.insertReturning(cardType).id

    cardTypeId
  end impl
end IMPL_decks_POST
