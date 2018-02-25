package com.downloader

import java.util.UUID

object ImagesProtocol {
  case class RequestForDownload(transactionId: UUID, urls: List[String])
  case class DownloadImages(urls: List[String])
  case object Acknowledgment
  case class DownloadDone(transactionId: UUID)
  case class DownloadFailed(transactionId: UUID)
}
