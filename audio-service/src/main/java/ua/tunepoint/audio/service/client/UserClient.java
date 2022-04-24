package ua.tunepoint.audio.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import ua.tunepoint.account.api.ProfileEndpoint;
import ua.tunepoint.audio.service.client.config.ClientConfiguration;

@FeignClient(name = "account-service", decode404 = true, configuration = ClientConfiguration.class)
public interface UserClient extends ProfileEndpoint {
}
