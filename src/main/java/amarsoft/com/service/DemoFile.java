package amarsoft.com.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Author lwp
 * @Date 2020/8/4 10:32
 * @Version
 */
@Service
public class DemoFile {
    public void run() throws IOException {
        String datas = "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试";
        System.out.println(datas);
//        String arr = datas;
//        File file = new File("/home/tyc/data/datas.txt");		//文件路径（路径+文件名）
//        if (!file.exists()) {	//文件不存在则创建文件，先创建目录
//            File dir = new File(file.getParent());
//            dir.mkdirs();
//            file.createNewFile();
//        }
//        FileOutputStream outStream = new FileOutputStream(file);	//文件输出流用于将数据写入文件
//
//        byte[] sourceByte = arr.getBytes();
//        outStream.write(sourceByte);
//        outStream.close();	//关闭文件输出流
    }
}
