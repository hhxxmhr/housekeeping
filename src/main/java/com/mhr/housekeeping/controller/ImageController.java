/*
package com.mhr.housekeeping.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.ServletResponse;
import java.io.*;

@Controller
public class ImageController {
    @GetMapping("/img/{photo}")
    public void test(ServletResponse resp, @PathVariable String photo) throws IOException {
        resp.setContentType("image/*");
        BufferedInputStream in = new BufferedInputStream(new FileInputStream("img/"+photo));
        ByteArrayOutputStream sto = new ByteArrayOutputStream();
        byte[] bytes = new byte[2048] ;
        int l;
        while ((l = in.read(bytes))!=-1) {
            sto.write(bytes,0,l);
        }
        resp.getOutputStream().write(sto.toByteArray());
    }
}
*/
