package org.lql.startup;

/**
 * Created by liqiulin on 2016/12/13.
 */
public class Bootstrap {
    public static void main(String[] args) throws Exception {
//        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring-config.xml");
//        ctx.registerShutdownHook();

        System.out.println("Bootstrap start success...");
        Thread.currentThread().join();
    }
}
