package com.nittsu.kinjirou.update.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({ "/file" })
public class FileController {
    // @Secured({ "ROLE_ADMIN" })
    // @ResponseBody
    @RequestMapping(value = "/download/{fileName}", method = { RequestMethod.GET, RequestMethod.POST })
    public FileSystemResource downloadFile(HttpServletResponse response, @PathVariable("fileName") String fileName) {
        response.setContentType("application/x-zip");
        response.addHeader("Content-Disposition", "attachment; filename=" + fileName);

        return new FileSystemResource(fileName);
    }
}