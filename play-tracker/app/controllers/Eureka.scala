package controllers

import com.google.inject.Inject
import com.netflix.appinfo.{ApplicationInfoManager, HealthCheckHandler, InstanceInfo, MyDataCenterInstanceConfig}
import com.netflix.appinfo.providers.EurekaConfigBasedInstanceInfoProvider
import com.netflix.discovery.{DefaultEurekaClientConfig, DiscoveryClient, EurekaClient}
import javax.inject.Singleton
import play.api.Configuration


@Singleton
class Eureka @Inject()(config: Configuration) {

  val mode = config.get[String]("eureka")

  private val instanceConfig = new MyDataCenterInstanceConfig()
  private val instanceInfo: InstanceInfo = new EurekaConfigBasedInstanceInfoProvider(instanceConfig).get
  private val applicationInfoManager = new ApplicationInfoManager(instanceConfig, instanceInfo)
  private val eurekaClient: EurekaClient = new DiscoveryClient(applicationInfoManager,
    new DefaultEurekaClientConfig(mode))


  eurekaClient.registerHealthCheck(new HealthCheckHandler() {
    override def getStatus(currentStatus: InstanceInfo.InstanceStatus): InstanceInfo.InstanceStatus = InstanceInfo.InstanceStatus.UP
  })

  case class Instance(url: String, private val instance: InstanceInfo) {
    def markOutOfService() {
      instance.setStatus(InstanceInfo.InstanceStatus.OUT_OF_SERVICE)
    }
    def markDown() {
      instance.setStatus(InstanceInfo.InstanceStatus.DOWN)
    }
  }

  def ifServiceAvailable(service: String, url: String): Option[Instance] = {
    var response: Option[Instance] = None

    val application = eurekaClient.getApplication(service)

    if (application != null) {
      application.shuffleAndStoreInstances(true)
      val list = application.getInstances

      if (list.size() > 0) {
        val a = list.get(0)

        response = Some(Instance(s"http://${a.getHostName}:${a.getPort}${url}", a))

      } else {
        println("************* no instances to call")
      }

    } else {
      println("*********** no application")
    }
    response
  }
}
