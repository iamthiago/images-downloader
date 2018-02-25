package com.downloader

import akka.actor.{Actor, Props}
import akka.pattern.pipe
import akka.stream.scaladsl.{Keep, Sink, Source}
import com.downloader.ImagesProtocol._

import scala.concurrent.ExecutionContextExecutor

class ImageStream extends Actor {

  implicit val dispatcher: ExecutionContextExecutor = context.system.dispatcher

  def receive = {
    case DownloadImages(urls) =>
      val replyTo = sender()

      Source(urls)
        .map(downloadImage)
        .toMat(Sink.ignore)(Keep.right)
        .run()
        .map(_ => Acknowledgment)
        .pipeTo(replyTo)

  }

  private def downloadImage(url: String) = {
    url
  }
}

object ImageStream {
  val name = "image-stream"
  def props = Props[ImageStream]
}