package com.leo.rpc.Invocation;

/**
 * @author: leo wang
 * @date: 2022-03-17
 * @description:
 **/
public class UserDaoImpl implements UserDao {
    @Override
    public void insert() {
        System.out.println("insert user success.");
    }
}
