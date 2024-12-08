package com.ayush.estore.AyushStore.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    String uploadImage(MultipartFile File, String path) throws IOException;

    InputStream getResource(String path, String name) throws FileNotFoundException;

}
