package com.downloader

import java.util.UUID

import akka.actor.{Actor, ActorRef, Props, ReceiveTimeout}
import com.downloader.ImagesProtocol._

import scala.concurrent.duration._

class DownloadManager extends Actor {

  val imageStream: ActorRef = context.actorOf(ImageStream.props, ImageStream.name)

  context.setReceiveTimeout(30.minutes)

  def receive = {
    case RequestForDownload(transactionId, urls) =>
      imageStream ! DownloadImages(urls)
      val client = sender()
      context become awaitImagesDownload(transactionId, client)
  }

  def awaitImagesDownload(transactionId: UUID, client: ActorRef): Receive = {
    case Acknowledgment =>
      client ! DownloadDone(transactionId)
      context.stop(self)

    case ReceiveTimeout =>
      client ! DownloadFailed(transactionId)
      context.stop(self)
  }
}

object DownloadManager {
  val name = "download-manager"
  def props = Props[DownloadManager]
}
