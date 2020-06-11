package es.upm.miw.easygive_spring.api_rest_controllers;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(SystemResource.SYSTEM)
public class SystemResource {

    public static final String SYSTEM = "/system";
    public static final String APP_INFO = "/app-info";
    public static final String VERSION_BADGE = "/version-badge";

    @Value("${application.name}")
    private String applicationName;

    @Value("${build.version}")
    private String buildVersion;

    @Value("${build.timestamp}")
    private String buildTimestamp;

    @Value("${spring.profiles.active}")
    private String profile;

    @GetMapping(value = VERSION_BADGE, produces = {"image/svg+xml"})
    public Mono<byte[]> generateBadge() { // http://localhost:8080/api/v0/system/version-badge
        return Mono.just(new Badge().generateBadge("v" + buildVersion).getBytes())
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }

    @GetMapping(SystemResource.APP_INFO)
    public Mono<AppInfoDto> readAppInfo() {
        return Mono.just(new AppInfoDto(this.applicationName, this.buildVersion, this.buildTimestamp, this.profile))
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }
}