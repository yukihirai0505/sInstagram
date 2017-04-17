package com.yukihirai0505.sInstagram.utils

import com.typesafe.config.{Config, ConfigFactory}

import scala.util.Properties

/**
  * author Yuki Hirai on 2017/04/17.
  */
object Configurations extends ConfigurationDetector {

  val config: Config = ConfigFactory.load

  lazy val clientId: String = envVarOrConfig("INSTAGRAM_CLIENT_ID", "instagram.client.id")
  lazy val clientSecret: String = envVarOrConfig("INSTAGRAM_SECRET", "instagram.client.secret")
  lazy val callbackUrl: String = envVarOrConfig("INSTAGRAM_CALLBACK_URL", "instagram.callbackUrl")

}

trait ConfigurationDetector {

  def config: Config

  protected def envVarOrConfig(envVar: String, configName: String): String =
    try {
      environmentVariable(envVar) getOrElse configuration(configName)
    } catch {
      case _: Throwable =>
        val msg = s"[sInstagram] configuration missing: Environment variable $envVar or configuration $configName not found."
        throw new RuntimeException(msg)
    }

  protected def environmentVariable(name: String): Option[String] = Properties.envOrNone(name)

  protected def configuration(path: String): String = config.getString(path)

}