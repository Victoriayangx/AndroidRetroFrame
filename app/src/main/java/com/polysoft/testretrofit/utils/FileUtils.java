package com.polysoft.testretrofit.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Response;

public class FileUtils {

    /**
     * 下载文件到本地
     */

    public static void writeFile(Response response, String path, String fileName) {
        try {
            InputStream is = response.body().byteStream();
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            File downLoad = new File(file, fileName);
            FileOutputStream fos = new FileOutputStream(downLoad);
            int len = -1;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }

            fos.flush();
            fos.close();
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 判断文件是否存在
     *
     * @return
     */
    public static boolean fileIsExists(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return true;
    }

}
