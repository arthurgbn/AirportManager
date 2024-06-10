package utils

import org.apache.pekko.Done
import play.api.cache.AsyncCacheApi

import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag

class CacheImplementation extends AsyncCacheApi {

  private val cache: mutable.Map[String, Any] = mutable.Map()

  override def set(key: String, value: Any, expiration: Duration): Future[Done] = {
    cache += (key -> value)
    Future.successful(Done)
  }

  override def remove(key: String): Future[Done] = {
    cache -= key
    Future.successful(Done)
  }

  override def getOrElseUpdate[A: ClassTag](key: String, expiration: Duration)(orElse: => Future[A]): Future[A] = {
    cache.get(key) match {
      case Some(value) =>
        Future.successful(value.asInstanceOf[A])
      case None =>
        orElse.map { value =>
          cache += (key -> value)
          value
        }
    }
  }

  override def get[T: ClassTag](key: String): Future[Option[T]] = {
    Future.successful(cache.get(key).map(_.asInstanceOf[T]))
  }

  override def removeAll(): Future[Done] = {
    cache.clear()
    Future.successful(Done)
  }
}
