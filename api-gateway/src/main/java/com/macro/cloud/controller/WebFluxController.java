package com.macro.cloud.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * 同步：服务器接收到请求，一个线程会处理请求，直到该请求处理完成，返回给浏览器
 * 异步：服务器接收到请求，一个线程会处理请求，然后指派别的线程处理请求，请求的线程直接空闲出来。
 *
 * Spring WebFlux在应对高并发的请求时，借助于异步IO，能够以少量而稳定的线程处理更高吞吐量的请求，
 * 尤其是当请求处理过程如果因为业务复杂或IO阻塞等导致处理时长较长时，对比更加显著。
 *
 * https://blog.csdn.net/Java_3y/article/details/103117518
 *
 * @see com.sumkor.cloud.controller.MyZuulController
 * @author Sumkor
 * @since 2023/10/19
 */
@RestController
@RequestMapping("/web")
public class WebFluxController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 阻塞5秒钟
     */
    private String createStr() {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
        }
        return "some string";
    }

    /**
     * 普通的SpringMVC方法
     */
    @GetMapping("/1")
    public String get1() {
        log.info("get1 start");
        String result = createStr();
        log.info("get1 end.");
        return result;
    }

    /**
     * WebFlux(返回的是Mono)
     * 请求线程可以直接返回，但是浏览器还是需要等待5秒才有响应
     */
    @GetMapping("/2")
    public Mono<String> get2() {
        log.info("get2 start");
        Mono<String> result = Mono.fromSupplier(() -> createStr());
        log.info("get2 end.");
        return result;
    }

    /**
     * http://localhost:9201/web/3
     *
     * 经验证，没有定期推送的效果，而是等待一定时间后一次性返回结果
     */
    @GetMapping(value = "/3", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> get3() {
        Flux<String> result = Flux
                .fromStream(IntStream.range(1, 5).mapToObj(i -> {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                    }
                    return "flux data--" + i;
                }));
        return result;
    }

    /**
     * http://localhost:9201/web/4
     *
     * 在前台页面需要不停获取服务器端的数据时，无非有两种操作：
     * 一种是通过前台页面使用轮询的方式，定时向服务器后台发送请求，以获取最新的数据；
     * 另一种就是在前台页面和后台服务之间建立长连接，服务器端一有数据产生就向前端页面推送。
     *
     * WebFlux支持服务器推送(SSE -> Server Send Event)，注：需要指定MediaType
     */
    @GetMapping(path = "/4", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> sse() {
        return Flux.interval(Duration.ofSeconds(1)) // 每1秒推送一次
                .map(seq -> Tuples.of(seq, LocalDateTime.now()))
                .map(data -> ServerSentEvent.<String>builder()
                        .id(Long.toString(data.getT1())) // 为每次发送设置一个id
                        .data(data.getT2().toString())
                        .build());
    }

}
