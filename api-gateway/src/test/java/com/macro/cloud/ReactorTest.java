package com.macro.cloud;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author 黄泽滨 【huangzebin@i72.com】
 * @since 2023/10/17
 */
public class ReactorTest {

    /**
     * Flux 则表示包含零个或多个元素的异步数据流。
     * 可以将 Mono 看作是一个特殊的 Flux，它只包含一个元素或者没有元素。
     */
    @Test
    public void mono() {
        // 创建一个包含单个元素的 Mono
        Mono<String> mono = Mono.just("Hello, World!");
        // 对 Mono 进行转换操作
        Mono<Integer> transformedMono = mono.map(s -> s.length());

        // 订阅 Mono 或 Flux 来获取异步数据流中的元素
        mono.subscribe(System.out::println); // 打印 "Hello, World!"
        transformedMono.subscribe(System.out::println); // 打印元素长度
    }

    @Test
    public void flux() {
        // 创建一个包含多个元素的 Flux
        Flux<Integer> flux = Flux.range(1, 10);
        // 对 Flux 进行操作和处理
        Flux<Integer> processedFlux = flux.filter(n -> n % 2 == 0).map(n -> n * 2);

        // 订阅 Mono 或 Flux 来获取异步数据流中的元素
        flux.subscribe(System.out::println); // 打印数字 1 到 10
        processedFlux.subscribe(System.out::println); // 打印满足条件的偶数乘以 2
    }

}
