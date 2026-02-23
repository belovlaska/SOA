package com.example.navigator.config;
import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.NewService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

@Singleton
@Startup
public class ConsulConfig {

    private ConsulClient consulClient;
    private String serviceId;
    private String serviceName = "wildfly-service";

    @PostConstruct
    public void registerWithConsul() {
        try {
            // Получаем конфигурацию из переменных окружения
            String consulHost = System.getenv().getOrDefault("SPRING_CLOUD_CONSUL_HOST", "consul");
            int consulPort = Integer.parseInt(System.getenv().getOrDefault("SPRING_CLOUD_CONSUL_PORT", "8500"));
//            int servicePort = Integer.parseInt(System.getProperty("jboss.http.port", "13224"));
            String portEnv = System.getenv("JBOSS_HTTP_PORT");
            int servicePort = (portEnv != null) ? Integer.parseInt(portEnv) : 8080;

            // Создаем клиента Consul
            consulClient = new ConsulClient(consulHost, consulPort);

            // Получаем hostname контейнера
            String hostname = InetAddress.getLocalHost().getHostName();

            // Формируем уникальный идентификатор сервиса
            serviceId = serviceName + "-" + hostname + "-" + servicePort;

            // Создаем конфигурацию сервиса
            NewService newService = new NewService();
            newService.setId(serviceId);
            newService.setName(serviceName);
            newService.setAddress(hostname);
            newService.setPort(servicePort);

            // Настраиваем теги
            List<String> tags = new ArrayList<>();
            tags.add("wildfly");
            tags.add("ejb");
            newService.setTags(tags);

            // Настраиваем healthcheck
            NewService.Check serviceCheck = new NewService.Check();
            serviceCheck.setHttp("http://" + hostname + ":" + servicePort + "/api/navigator");
            serviceCheck.setInterval("10s");
            serviceCheck.setTimeout("5s");
            serviceCheck.setDeregisterCriticalServiceAfter("90s");
            newService.setCheck(serviceCheck);

            // Регистрируем сервис
            consulClient.agentServiceRegister(newService);

            System.out.println("Service registered in Consul: " + serviceId);

        } catch (Exception e) {
            System.err.println("Failed to register service in Consul: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void deregisterFromConsul() {
        if (consulClient != null && serviceId != null) {
            try {
                consulClient.agentServiceDeregister(serviceId);
                System.out.println("Service deregistered from Consul: " + serviceId);
            } catch (Exception e) {
                System.err.println("Failed to deregister service from Consul: " + e.getMessage());
            }
        }
    }
}