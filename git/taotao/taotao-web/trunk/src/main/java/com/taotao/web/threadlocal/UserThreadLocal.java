package com.taotao.web.threadlocal;

import com.taotao.web.pojo.User;

public class UserThreadLocal  {

    // 定义ThreadLocal
    private static ThreadLocal<User> USER = new ThreadLocal<User>();

    // 把User放入ThreadLocal中
    public static void set(User user) {
        USER.set(user);
    }

    // 从ThreadLocal中取得对象
    public static User get() {
        return USER.get();
    }
}
