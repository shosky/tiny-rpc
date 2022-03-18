package com.leo.rpc.Invocation;

import org.junit.Test;

/**
 * @author: leo wang
 * @date: 2022-03-17
 * @description:
 **/
public class TransactionProxyTest {

    @Test
    public void testProxy() {

        UserDao userDap = new UserDaoImpl();

        UserDao proxy = (UserDao) new TransactionProxy(userDap).getProxyInstance();

        proxy.insert();
    }
}
