package com.yep.server.controller;

import com.yep.server.pojo.RespBean;
import com.yep.server.utils.FastDFSUtil;
import lombok.extern.slf4j.Slf4j;
import org.csource.common.MyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Date;

/**
 * 聊天发送图片上传到FastDFS文件服务器存储
 * @author HuangSir
 * @date 2022-02-16 20:14
 */
@RestController
@Slf4j
public class FileController {
//   @Value("${fastdfs.nginx.host}")
//   String nginxHost;
   @Value("${file.dir}")
   private String realPath;
   /**
    * 上传文件，返回指定的文件路径
    *
    * @param file
    * @return
    * @throws IOException
    * @throws MyException
    */
   @PostMapping("/file")
   public String uploadFile(MultipartFile file) throws IOException, MyException {
      log.debug("收到的文件是否为空：{}", file == null);
      if (file != null) {
         log.debug("收到的文件名：{}", file.getOriginalFilename());
         String originalFilename = file.getOriginalFilename();
         file.transferTo(new File(realPath, originalFilename));
//         return "http://120.24.179.229:8082/"+originalFilename;
         return "http://localhost:8082/"+originalFilename;
      }
      return "";
//      String fileId = FastDFSUtil.upload(file);
//      return nginxHost + fileId;
   }
}
