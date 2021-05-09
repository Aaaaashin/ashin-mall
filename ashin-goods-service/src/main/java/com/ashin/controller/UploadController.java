package com.ashin.controller;

import com.ashin.entity.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@RestController
@RequestMapping("/image")
public class UploadController {

    @Value("${fileurl}")
    private String baseFileurl;

    @PostMapping("/fileupload")
    public Result uploadFile(MultipartFile uploadFile) throws Exception {
        //获取原始名称
        String originalFilename = uploadFile.getOriginalFilename();

        //获取后缀名
        String extName = originalFilename.substring(originalFilename.lastIndexOf("."));

        //组装新文件名
        String newFileName = UUID.randomUUID().toString()+extName;

        //获取图片存放目录 , 目前不考虑电商中图片存储问题, 案例中使用本地路径替代
        String aStatic = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        System.out.println(aStatic);
        File file = new File(aStatic, "static/img/goods");
        if(!file.exists()){
            file.mkdirs();
        }

        //文件存储
        String filepath = file.getAbsolutePath()+File.separator+newFileName;
        System.out.println(filepath);
        uploadFile.transferTo(new File(filepath));

        //访问URL
        String fileurl = baseFileurl + newFileName;

        return new Result(true,"上传成功",fileurl);
    }


    @DeleteMapping("/delete")
    public Result deleteFile(String fileurl){
        String filename = fileurl.substring(fileurl.lastIndexOf("/") + 1);
        System.out.println(filename);

        String aStatic = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        System.out.println(aStatic);
        File file = new File(aStatic, "static/img/goods");

        //文件存储
        String filepath = file.getAbsolutePath()+File.separator+filename;
        System.out.println(filepath);

        new File(filepath).delete();

        return new Result(true,"操作成功");
    }
}