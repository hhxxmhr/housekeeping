package com.mhr.housekeeping.controller;

import com.mhr.housekeeping.utils.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @RequestMapping(value = "/uploadPic")
    public Result uploadPic(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request) throws IOException {
        //目前这里是写死的本地硬盘路径
        String path = "E:/myCode/housekeeping/housekeeping/img";
        //获取文件名称
        String fileName = file.getOriginalFilename();
        //获取文件名后缀
        String suffix = fileName.substring(file.getOriginalFilename().lastIndexOf("."));
        suffix = suffix.toLowerCase();
        if (suffix.equals(".jpg") || suffix.equals(".jpeg") || suffix.equals(".png")) {
            fileName = System.currentTimeMillis() + suffix;
            File targetFile = new File(path, fileName);
            if (!targetFile.getParentFile().exists()) {    //注意，判断父级路径是否存在
                targetFile.getParentFile().mkdirs();
            }
            long size = 0;
            //保存
            try {
                file.transferTo(targetFile);
                size = file.getSize();
            } catch (Exception e) {
                e.printStackTrace();
                return Result.getFailure("上传失败");
            }
            return new Result<>(200, "上传成功", "img/" + fileName);
        } else {
            return Result.getFailure("上传头像图片只能是 JPG 格式!");
        }

    }


}