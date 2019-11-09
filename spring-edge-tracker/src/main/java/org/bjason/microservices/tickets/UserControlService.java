package org.bjason.microservices.tickets;


import org.bjason.microservices.users.Token;
import org.bjason.microservices.users.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("user-control-service")
public interface UserControlService {

    @PostMapping("/valid")
    Integer valid(@RequestBody Token tokenId) ;

    @GetMapping("/getusername")
    String getUserName(@RequestParam Integer userId);
}
