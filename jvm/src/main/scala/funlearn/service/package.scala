package funlearn

import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

package object service:
    val dataSource: DataSource =
      val ds = HikariDataSource()
      ds.setJdbcUrl("jdbc:sqlite:../data/database.db")
      ds
