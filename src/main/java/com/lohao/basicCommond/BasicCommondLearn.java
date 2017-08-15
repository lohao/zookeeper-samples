package com.lohao.basicCommond;

import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by frmark on 2017/8/15.
 */
public class BasicCommondLearn {
    private final static Logger logger = LoggerFactory.getLogger(BasicCommondLearn.class);

    private String serverAddress = "localhost:2181,localhost:2182,localhost:2183";
    private int sessionTimeOut = 60000;
    private String path = "/test/app1";

    private ZooKeeper zk;

    public static void main(String[] args){
        try {
            new BasicCommondLearn().test();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void test() throws IOException {
        zk = new ZooKeeper(serverAddress, sessionTimeOut, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                String path = watchedEvent.getPath();
                logger.info("watcher: " + watchedEvent.getType() + ", path " + path + ", type " + watchedEvent.getState());
            }
        });

        create();
        get();

        set();
        get();

        close();
    }

    public void get(){
        String result = null;
        try{
            byte[] data = zk.getData(path,null,null);
            result = new String(data);
        } catch (InterruptedException e){
            e.printStackTrace();
        } catch (KeeperException e){
            e.printStackTrace();
        }
        logger.info("get data is " + result);
    }

    public void create() {
        String data = "hello world";
        try {
            String result = zk.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
            logger.info("create data is " + result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    public void set() {
        String newData = "hello trick1";
        try {
            zk.setData(path,newData.getBytes(),-1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        logger.info("delete path " + path);
        try {
            zk.delete(path,-1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        logger.info("close zk");
        try {
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
