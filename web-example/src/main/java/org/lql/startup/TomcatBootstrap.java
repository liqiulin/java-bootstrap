package org.lql.startup;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.AprLifecycleListener;
import org.apache.catalina.core.JreMemoryLeakPreventionListener;
import org.apache.catalina.core.StandardServer;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.http11.Http11NioProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by liqiulin on 2016/12/15.
 */
public class TomcatBootstrap {
    private static final Logger LOG = LoggerFactory.getLogger(TomcatBootstrap.class);

    public static void main(String[] args) throws Exception {
        //提升性能(https://wiki.apache.org/tomcat/HowTo/FasterStartUp)
        System.setProperty("tomcat.util.scan.StandardJarScanFilter.jarsToSkip", "*.jar");
        //System.setProperty("securerandom.source","file:/dev/./urandom");
        int port = Integer.parseInt(System.getProperty("server.port", "8080"));
        String contextPath = System.getProperty("server.contextPath", "");
        String docBase = System.getProperty("server.docBase", getDefaultDocBase());
        LOG.info("server port : {}, context path : {},doc base : {}", port, contextPath, docBase);
        final Tomcat tomcat = createTomcat(port, contextPath, docBase);
        tomcat.start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    tomcat.stop();
                } catch (LifecycleException e) {
                    LOG.error("stoptomcat error.", e);
                }
            }
        });
        tomcat.getServer().await();
    }

    private static String getDefaultDocBase() {
        File classpathDir = new File(Thread.currentThread().getContextClassLoader().getResource(".").getFile());
        File projectDir = classpathDir.getParentFile().getParentFile();
        return new File(projectDir, "src/main/webapp").getPath();
    }

    private static Tomcat createTomcat(int port, String contextPath, String docBase) throws Exception {
        String tmpdir = System.getProperty("java.io.tmpdir");
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir(tmpdir);
        tomcat.getHost().setAppBase(tmpdir);
        tomcat.getHost().setAutoDeploy(false);
        tomcat.getHost().setDeployOnStartup(false);
        tomcat.getEngine().setBackgroundProcessorDelay(-1);
        tomcat.setConnector(newNioConnector());
        tomcat.getConnector().setPort(port);
        tomcat.getService().addConnector(tomcat.getConnector());
        Context context = tomcat.addWebapp(contextPath, docBase);
        StandardServer server = (StandardServer) tomcat.getServer();
        //APR library loader. Documentation at /docs/apr.html
        server.addLifecycleListener(new AprLifecycleListener());
        //Prevent memory leaks due to use of particularjava/javax APIs
        server.addLifecycleListener(new JreMemoryLeakPreventionListener());
        return tomcat;
    }

    //在这里调整参数优化
    private static Connector newNioConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
        return connector;
    }

}
