package com.alibaba.spring.boot.rsocket.demo;

import com.alibaba.spring.boot.rsocket.RSocketService;
import com.alibaba.user.User;
import com.alibaba.user.UserService;
import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * user service implementation
 *
 * @author leijuan
 */
@RSocketService(serviceInterface = UserService.class)
public class UserServiceImpl implements UserService {
    private Faker faker = new Faker();

    @Override
    public Mono<User> findById(Integer id) {
        User user = new User();
        user.setId(id);
        user.setNick(faker.name().name());
        user.setPhone(faker.phoneNumber().cellPhone());
        user.setEmail(faker.internet().emailAddress());
        return Mono.just(user);
    }

    @Override
    public Mono<User> findByEmailOrPhone(String email, String phone) {
        return Mono.fromCallable(() -> {
            User user = new User();
            user.setId(1);
            user.setEmail(email);
            user.setPhone(phone);
            user.setNick(faker.name().name());
            return user;
        });
    }

    @Override
    public Mono<List<User>> findAll() {
        return Mono.just(Arrays.asList(new User(1, "first"), new User(2, "second")));
    }

    @Override
    public Mono<Integer> save(User user) {
        return Mono.just(RandomUtils.nextInt());
    }

    @Override
    public void flush(String name) {
        System.out.println("flush");
    }

    @Override
    public Mono<String> getAppName() {
        return Mono.just("UserService");
    }

    @Override
    public Mono<Void> job1() {
        System.out.println("job1");
        return Mono.empty();
    }

    @Override
    public Flux<User> findAllPeople(String type) {
        return Flux.range(0, 10)
                .map(id -> new User(id, "nick:" + type));
    }

    @Override
    public Flux<User> recent(Flux<Date> point) {
        point.subscribe(t -> {
            System.out.println("time:" + point);
        });
        return Flux.interval(Duration.ofMillis(1000))
                .map(timestamp -> new User((int) (timestamp % 1000), "nick"));
    }

    @Override
    public Mono<String> error(String text) {
        return Mono.error(new Exception("this is an Exception!"));
    }
}
