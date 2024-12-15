package faang.school.achievement.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "project-service", url = "${services.project-service.host}:${services.project-service.port}")
public interface ProjectServiceClient {
//    @GetMapping

}
