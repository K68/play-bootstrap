import com.google.inject.AbstractModule
import java.time.{Clock, ZoneId}

import com.amzport.config._
import repo.RepoDao

/**
 * This class is a Guice module that tells Guice how to bind several
 * different types. This Guice module is created when the Play
 * application starts.

 * Play will automatically use any class called `Module` that is in
 * the root package. You can create modules in other locations by
 * adding `play.modules.enabled` settings to the `application.conf`
 * configuration file.
 */
class Module extends AbstractModule {

  override def configure(): Unit = {

    val zoneId: ZoneId = initTimeZone()

    // Use the system clock as the default implementation of Clock
    // bind(classOf[Clock]).toInstance(Clock.systemDefaultZone)
    bind(classOf[Clock]).toInstance(Clock.system(zoneId))

    // Set AtomicCounter as the implementation for Counter.
    // bind(classOf[Counter]).to(classOf[AtomicCounter])

    bind(classOf[RepoDao]).asEagerSingleton()

  }

}
