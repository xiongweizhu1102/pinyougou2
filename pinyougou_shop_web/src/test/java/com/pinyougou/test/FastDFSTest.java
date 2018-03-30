package com.pinyougou.test;

import com.pinyougou.common.util.FastDFSClient;
import org.csource.fastdfs.*;
import org.junit.Test;
import org.springframework.test.context.TestExecutionListeners;

/**
 * @author bear
 * @version 1.0
 * @description com.pinyougou.test
 * @date 2018/3/29
 */
public class FastDFSTest {
    @Test
    public void  testUpload() throws Exception {
        //1.创建配置文件fdfs_client.conf,配置tracker_server的地址
        //2加载配置文件
        ClientGlobal.init("D:\\IDEAProject\\pinyougou_parent\\pinyougou_shop_web\\src\\main\\resources\\config\\fdfs_client.conf");
        //3创建trackerClient对象来获得trackerServer
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        //4.构建一个storeServer对象 存储对象为空
        StorageServer storageServer = null;
        //5创建一个存储客户端来实现被trackerserver调用,去操作storageserver
        StorageClient storageClient = new StorageClient(trackerServer, storageServer);
        //6存储客户端进行文件上传
        /**
         * 参数1文件路径
         * 参数2后缀名不带点
         * 参数3 文件的元数据,就是大小,创建时间,时间蹉
         */
        String[] strings = storageClient.upload_file("D:\\图片\\墙纸.jpg", "jpg", null);
        for (String string : strings) {
            System.out.println(string);
        }

    }

    @Test
    public  void testFastDFSClient() throws Exception {
        //封装了存储Store客户端,后续调用上传文件方法进行文件上传
        FastDFSClient fastDFSClient = new FastDFSClient("D:\\IDEAProject\\pinyougou_parent\\pinyougou_shop_web\\src\\main\\resources\\config\\fdfs_client.conf");
        String uploadFile = fastDFSClient.uploadFile("D:\\图片\\深圳地铁.jpg");
        System.out.println(uploadFile);
    }
}
