package com.yep.server.utils;

import lombok.extern.slf4j.Slf4j;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * FastDFS工具类
 * @author HuangSir
 * @date 2022-02-16 20:03
 */
public class FastDFSUtil {
   private static StorageClient1 client1;
   private  static final Logger logger = LoggerFactory.getLogger(FastDFSUtil.class);
   static{
      try{
         ClientGlobal.initByProperties("fastdfs-client.properties");
         TrackerClient trackerClient=new TrackerClient();
         TrackerServer trackerServer=trackerClient.getConnection();
         client1=new StorageClient1(trackerServer,null);
      } catch (Exception e) {
         logger.debug("文件上传初始化失败！：{}",e.getMessage());
      }
   }
   public static String upload(MultipartFile file) throws IOException, MyException {
      //获取上传过来的文件名
      String oldName=file.getOriginalFilename();
      //返回上传到服务器的路径
      //文件拓展名oldName.substring(oldName.lastIndexOf(".")+1)
         return client1.upload_file1(file.getBytes(),oldName.substring(oldName.lastIndexOf(".")+1),null);
   }
}
