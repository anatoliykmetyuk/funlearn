package funlearn.db

import funlearn.model.CardType
import com.augustnagro.magnum.*

class CardTypesRepo(repo: Repo[CardType, CardType, Long]) extends DbAccessLayerRepo(repo):
  def getCardTypesByDeckId(deckId: Long): Vector[CardType] =
    findAll(Spec[CardType].where(sql"deck_id = $deckId"))

end CardTypesRepo