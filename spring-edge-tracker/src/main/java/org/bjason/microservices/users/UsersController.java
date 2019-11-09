package org.bjason.microservices.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;


@RestController
public class UsersController {

    public static final String CACHE = "tokens";
    protected Logger logger = Logger.getLogger(UsersController.class
            .getName());


    @Autowired
    private CacheManager cache;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    RedisTemplate redisTemplate;

    public UsersController(UserRepository userRepository) {
        this.userRepository = userRepository;
        logger.info("UserController started");
    }

    @PostMapping("/valid")
    Integer valid(@RequestBody Token tokenId) {

        SimpleValueWrapper value = (SimpleValueWrapper) cache.getCache(CACHE).get(tokenId.getToken());

        if (value != null) {
            if (((Token) value.get()).isExpired()) {
                Token token = (Token) value.get();
                cache.getCache(CACHE).evict(token.getToken());
                logger.info("Token evicted " + token);
            } else {
                return ((Token) value.get()).getUserId();
            }
        }
        throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "token not valid");
    }

    @PostMapping("/createuser")
    User add(@RequestBody User user) {

        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        Long id = ops.increment("userIdKey:", 1);

        user.userId = id.intValue();
        User saved = userRepository.save(user);
        return saved;
    }

    @GetMapping("/allusers")
    List<User> add() {
        List<User> list = new ArrayList<>();
        userRepository.findAll().forEach(list::add);

        return list;

    }

    @GetMapping("/getusername")
    String getUserName(@RequestParam Integer userId) {
        Optional<User> u = userRepository.findById(userId);

        if ( u.isPresent() ) return u.get().userName;
        return null;

    }

    @PostMapping("/login")
    Token login(@RequestBody User login) {

        List<User> users = userRepository.findByUserNameAndUserPassword(login.userName, login.userPassword);

        logger.info("Search for " + login + " found " + users);

        if (users.size() == 1) {

            User u = users.get(0);

            Integer userId = u.userId;
            logger.info("Found " + login);
            Token token = new Token(userId, UUID.randomUUID().toString(), 3000);
            cache.getCache(CACHE).put(token.getToken(), token);
            logger.info("Token created " + token);

            return token;
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "not found");
        }
    }
}
