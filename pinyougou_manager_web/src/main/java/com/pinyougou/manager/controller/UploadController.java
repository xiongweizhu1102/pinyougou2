package com.pinyougou.manager.controller;

import com.pinyougou.common.util.FastDFSClient;
import com.pinyougou.entity.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author bear
 * @version 1.0
 * @description com.pinyougou.shop.controller
 * @date 2018/3/30
 */
@RestController
public class UploadController {
    @Value("${IMAGE_SERVER_URL}")
    private String IMAGE_SERVER_URL;//文件服务器地址
   @RequestMapping("/upload")
    public Result upload(MultipartFile file){
       //1.取文件的扩展名
       String originalFilename = file.getOriginalFilename();
       String extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
       //2.得到fastDFS即存储客户端
       try {
           FastDFSClient fastDFSClient = new FastDFSClient("classpath:config/fdfs_client.conf");
           //3上传文件
           String path = fastDFSClient.uploadFile(file.getBytes(), extName);
           //4返回的url和ip地址拼成完整的url
           String url = IMAGE_SERVER_URL + path;
           System.out.println(IMAGE_SERVER_URL);
           System.out.println(url);
           return new Result(true,url);

       } catch (Exception e) {
           e.printStackTrace();
           return  new Result(false,"上传失败");
       }

   }

}
