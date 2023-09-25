package com.macro.cloud.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.macro.cloud.domain.CommonResult;
import com.macro.cloud.domain.User;
import com.netflix.hystrix.HystrixRequestCache;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheKey;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * HystrixCommand提供了一种隔离策略，用于在执行依赖调用时控制和限制资源的使用。
 * HystrixCommand的隔离是通过线程池隔离和信号量隔离两种机制来实现的。
 *
 *     线程池隔离（Thread Pool Isolation）：
 *         线程池隔离是Hystrix默认的隔离策略，通过为每个Hystrix命令使用独立的线程池来执行依赖调用。
 *         该线程池独立于应用程序的主线程池，可以为每个依赖提供独立的线程资源。
 *         线程池隔离能够有效隔离和限制依赖调用的资源使用，避免了由于某个依赖的延迟或故障导致整个应用程序线程池资源耗尽的情况。
 *
 *     信号量隔离（Semaphore Isolation）：
 *         信号量隔离是Hystrix的另一种隔离策略，通过使用信号量来控制对依赖调用的并发访问。
 *         每个命令都会使用一个固定数量的信号量，当需要执行依赖调用时，首先会尝试获取信号量，如果成功获取则执行依赖调用，否则会进入降级逻辑。
 *         信号量隔离适用于对某一依赖的并发访问不宜过多的情况，因为所有的依赖调用共享同一组信号量资源。
 *
 * HystrixCommand的隔离策略可以通过配置来进行调整。可以根据应用程序的需求和依赖服务的特点选择适合的隔离策略。
 * 线程池隔离适用于对依赖调用的资源限制较为严格的情况，而信号量隔离适用于对依赖调用的并发访问有一定限制要求的情况。
 *
 * Created by macro on 2019/9/3.
 */
@Service
public class UserService {

    private Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private RestTemplate restTemplate;
    @Value("${service-url.user-service}")
    private String userServiceUrl;

    /**
     * 服务降级，配置降级方法
     */
    @HystrixCommand(fallbackMethod = "getDefaultUser")
    public CommonResult getUser(Long id) {
        LOGGER.info("getUser id:{}, thread:{}", id, Thread.currentThread().getName());
        return restTemplate.getForObject(userServiceUrl + "/user/{1}", CommonResult.class, id);
    }

    public CommonResult getDefaultUser(@PathVariable Long id) {
        User defaultUser = new User(-1L, "defaultUser", "123456");
        return new CommonResult<>(defaultUser);
    }

    /**
     * 对指定异常进行忽略，不执行服务降级
     */
    @HystrixCommand(fallbackMethod = "getDefaultUser2", ignoreExceptions = {NullPointerException.class})
    public CommonResult getUserException(Long id) {
        if (id == 1) {
            throw new IndexOutOfBoundsException();
        } else if (id == 2) {
            throw new NullPointerException();
        }
        return restTemplate.getForObject(userServiceUrl + "/user/{1}", CommonResult.class, id);
    }

    public CommonResult getDefaultUser2(@PathVariable Long id, Throwable e) {
        LOGGER.error("getDefaultUser2 id:{},throwable class:{}", id, e.getClass());
        User defaultUser = new User(-2L, "defaultUser2", "123456");
        return new CommonResult<>(defaultUser);
    }

    /**
     * fallbackMethod：指定服务降级处理方法；
     * ignoreExceptions：忽略某些异常，不发生服务降级；
     * commandKey：命令名称，用于区分不同的命令，可以为每个命令配置不同的属性，例如超时时间、熔断策略、是否缓存等；
     * groupKey：分组名称，Hystrix会根据不同的分组来统计命令的告警及仪表盘信息；
     * threadPoolKey：线程池名称，用于划分线程池。
     */
    @HystrixCommand(fallbackMethod = "getDefaultUser",
            commandKey = "getUserCommand",
            groupKey = "getUserGroup",
            threadPoolKey = "PoolAAA")
    public CommonResult getUserCommand(@PathVariable Long id) {
        LOGGER.info("getUserCommand id:{}, thread:{}", id, Thread.currentThread().getName());
        return restTemplate.getForObject(userServiceUrl + "/user/{1}", CommonResult.class, id);
    }

    /**
     * {@link CacheResult} 开启缓存，默认所有参数作为缓存的key，cacheKeyMethod可以通过返回String类型的方法指定key；
     * {@link CacheKey}    指定缓存的key，可以指定参数或指定参数中的属性值为缓存key，cacheKeyMethod还可以通过返回String类型的方法指定；
     * {@link CacheRemove} 移除缓存，需要指定commandKey。
     * <p>
     * 缓存对象 {@link HystrixRequestCache}
     */
    @CacheResult(cacheKeyMethod = "getCacheKey")
    @HystrixCommand(fallbackMethod = "getDefaultUser", commandKey = "getUserCacheAAA")
    public CommonResult getUserCache(Long id) {
        LOGGER.info("getUserCache id:{}", id);
        return restTemplate.getForObject(userServiceUrl + "/user/{1}", CommonResult.class, id);
    }

    /**
     * 为缓存生成key的方法
     */
    public String getCacheKey(Long id) {
        return String.valueOf(id);
    }

    /**
     * commandKey 必须指定，才能删除到对应方法的缓存
     * cacheKey 可以不指定，默认所有参数作为缓存的key
     */
    @CacheRemove(commandKey = "getUserCacheAAA", cacheKeyMethod = "getCacheKey")
    @HystrixCommand
    public CommonResult removeCache(Long id) {
        LOGGER.info("removeCache id:{}", id);
        return restTemplate.postForObject(userServiceUrl + "/user/delete/{1}", null, CommonResult.class, id);
    }

    /**
     * {@link HystrixCollapser} 用于合并请求，从而达到减少通信消耗及线程数量的效果。
     *
     * batchMethod：用于设置请求合并的方法；
     * collapserProperties：请求合并属性，用于控制实例属性，有很多；
     * timerDelayInMilliseconds：用于控制每隔多少时间合并一次请求；
     */
    @HystrixCollapser(batchMethod = "getUserByIds", collapserProperties = {
            @HystrixProperty(name = "timerDelayInMilliseconds", value = "100")
    })
    public Future<User> getUserFuture(Long id) {
        return new AsyncResult<User>() {
            @Override
            public User invoke() {
                CommonResult commonResult = restTemplate.getForObject(userServiceUrl + "/user/{1}", CommonResult.class, id);
                Map data = (Map) commonResult.getData();
                User user = BeanUtil.mapToBean(data, User.class, true);
                LOGGER.info("getUserById username:{}", user.getUsername());
                return user;
            }
        };
    }

    @HystrixCommand
    public List<User> getUserByIds(List<Long> ids) {
        LOGGER.info("getUserByIds:{}", ids);
        CommonResult commonResult = restTemplate.getForObject(userServiceUrl + "/user/getUserByIds?ids={1}", CommonResult.class, CollUtil.join(ids, ","));
        return (List<User>) commonResult.getData();
    }
}
