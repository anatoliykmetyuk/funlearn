package funlearn.db

import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

import com.augustnagro.magnum.*
import funlearn.model.{Deck, CardType}


private val dataSource: DataSource =
  val ds = HikariDataSource()
  ds.setJdbcUrl("jdbc:sqlite:../data/database.db")
  ds
private val transactor = Transactor(dataSource)

val decksRepo = DbAccessLayerRepo(Repo[Deck, Deck, Long])
val cardTypesRepo = CardTypesRepo(Repo[CardType, CardType, Long])
