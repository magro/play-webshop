package models

import net.fwbrasil.activate.ActivateContext
import net.fwbrasil.activate.storage.relational.async.AsyncPostgreSQLStorage
import net.fwbrasil.activate.storage.relational.idiom.postgresqlDialect
import com.github.mauricio.async.db.Configuration
import com.github.mauricio.async.db.pool.PoolConfiguration
import com.github.mauricio.async.db.postgresql.pool.PostgreSQLConnectionFactory
import play.api._
import com.github.mauricio.async.db.postgresql.util.URLParser
import play.api.Play.current

//import net.fwbrasil.activate.ActivateContext
//import net.fwbrasil.activate.storage.memory.TransientMemoryStorage
//import net.fwbrasil.activate.storage.relational.async.AsyncPostgreSQLStorage
//import com.github.mauricio.async.db.postgresql.pool.PostgreSQLConnectionFactory
//import com.github.mauricio.async.db.Configuration

object shopPersistenceContext extends ActivateContext {

  
//  val storage = new AsyncPostgreSQLStorage {
//
//    def configuration =
//      new Configuration(
//        username = "play",
//        host = "localhost",
//        password = Some("play"),
//        database = Some("play-webshop"))
//
//    lazy val objectFactory = new PostgreSQLConnectionFactory(configuration)
//  }

  import Play.current

  val storage = new AsyncPostgreSQLStorage {

    private def config(key: String)(implicit app: Application): Option[play.api.Configuration] = {
      app.configuration.getConfig(key)
    }

    private def playConfig = Play.application.mode match {
      case Mode.Prod | Mode.Dev => config("db.default").get
      case Mode.Test => config("db.test").getOrElse(config("db.default").get)
    }

    private def configuration: Configuration = {
      System.getenv("DATABASE_URL") match {
        case url: String => URLParser.parse(url)
        case _ => {
          val config = playConfig
          // maybe the url already has set the username and password or not.
          var dbConfig = URLParser.parse(config.getString("url").getOrElse("jdbc:postgresql://localhost:5432/play-webshop"))
          config.getString("user").foreach { user =>
            dbConfig = dbConfig.copy(username = user)
          }
          config.getString("password").foreach { pwd =>
            dbConfig = dbConfig.copy(password = Some(pwd))
          }
          dbConfig
        }
      }
    }

    // Override pool configuration to be able to serve more than 20 (10 + 10) concurrent clients
    // (see PoolConfiguration.Default)
    // Might use config parameters from application.conf
    override def poolConfiguration = new PoolConfiguration(200, 20, 500)

    override val dialect = postgresqlDialect(escape = noEscape, normalize = underscoreSeparated)

    lazy val objectFactory = new PostgreSQLConnectionFactory(configuration)
  }

}
