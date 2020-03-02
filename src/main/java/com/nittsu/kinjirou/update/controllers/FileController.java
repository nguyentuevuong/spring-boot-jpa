package com.nittsu.kinjirou.update.controllers;

import org.springframework.core.io.FileSystemResource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({ "/file" })
public class FileController {
    @Secured({ "ROLE_ADMIN" })
    @RequestMapping(value = "/download/{fileName}", method = RequestMethod.HEAD)
    public FileSystemResource downloadFile(@PathVariable("fileName") String fileName) {
        return new FileSystemResource(fileName);
    }
}