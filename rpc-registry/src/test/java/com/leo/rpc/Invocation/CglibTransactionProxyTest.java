package com.leo.rpc.Invocation;

/**
 * @author: leo wang
 * @date: 2022-03-17
 * @description:
 **/
public class CglibTransactionProxyTest {

    public static void main(String[] args) {
        UserDao userDao = new UserDaoImpl();

        UserDao proxyInstance = (UserDao) new CglibTransactionProxy(userDao).genProxyInstance();

        proxyInstance.insert();
    }
}
