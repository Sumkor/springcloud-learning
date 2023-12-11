package com.macro.cloud.service.impl;

import com.macro.cloud.dao.OrderDao;
import com.macro.cloud.domain.Order;
import com.macro.cloud.service.AccountService;
import com.macro.cloud.service.OrderService;
import com.macro.cloud.service.StorageService;
import io.seata.rm.datasource.exec.AbstractDMLBaseExecutor;
import io.seata.rm.datasource.exec.ExecuteTemplate;
import io.seata.rm.datasource.exec.UpdateExecutor;
import io.seata.spring.annotation.GlobalTransactional;
import io.seata.spring.annotation.GlobalTransactionalInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 订单业务实现类
 * Created by macro on 2019/11/11.
 */
@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private StorageService storageService;
    @Autowired
    private AccountService accountService;

    /**
     * 创建订单->调用库存服务扣减库存->调用账户服务扣减账户余额->修改订单状态
     *
     * 在需要开启分布式事务的方法上标记 @GlobalTransactional，它属于 AT 模式，负责开启全局事务
     * @see GlobalTransactionalInterceptor
     *
     * AT模式分为两个阶段：
     *
     * 第一阶段，提交业务数据，生成回滚日志（undo log），注册事务分支，加全局锁
     * @see AbstractDMLBaseExecutor#doExecute(java.lang.Object...)
     *
     * 第二阶段，如果所有事务提交成功，则删除一阶段生成的 undo log，并释放全局锁
     *          如果部分事务参与者提交失败，则需要根据 undo log 对已经注册的事务分支进行回滚，并释放全局锁
     */
    @Override
    @GlobalTransactional(name = "fsp-create-order", rollbackFor = Exception.class)
    public void create(Order order) {
        LOGGER.info("------->下单开始");

        /**
         * 普通的查询语句，是一个 PlainExecutor，并不会加 beforeImage 和 afterImage
         * @see ExecuteTemplate#execute(io.seata.rm.datasource.sql.SQLRecognizer, io.seata.rm.datasource.StatementProxy, io.seata.rm.datasource.exec.StatementCallback, java.lang.Object...)
         */
        Order existOrder = orderDao.query(order.getUserId());

        /**
         * update 语句，需要生成 beforeImage 和 afterImage，用于生成 undo log，记录数据在修改前后的值
         * 其中，在生成 beforeImage 的时候，查询旧值，会自动构造一条 select for update 的语句，可以避免在生成 undo log 的时候其他事务修改该数据
         * @see UpdateExecutor#buildBeforeImageSQL(io.seata.rm.datasource.sql.struct.TableMeta, java.util.ArrayList)
         */
        orderDao.update(existOrder.getUserId(), 1);

        //本应用创建订单
        orderDao.create(order);

        //远程调用库存服务扣减库存
        LOGGER.info("------->order-service中扣减库存开始");
        storageService.decrease(order.getProductId(), order.getCount());
        LOGGER.info("------->order-service中扣减库存结束");

        //远程调用账户服务扣减余额
        LOGGER.info("------->order-service中扣减余额开始");
        accountService.decrease(order.getUserId(), order.getMoney());
        LOGGER.info("------->order-service中扣减余额结束");

        //修改订单状态为已完成
        LOGGER.info("------->order-service中修改订单状态开始");
        orderDao.update(order.getUserId(), 0);
        LOGGER.info("------->order-service中修改订单状态结束");

        LOGGER.info("------->下单结束");
    }
}
