package io.rackshift.rackshiftproxy.service;


import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;

@Service
public class ImageService {
    @Resource
    private DockerClientService dockerClientService;

    public boolean mountISO(String filePath, String fileUploadBase, String mountDirName) {
        try {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("/etc/fstab")));
//            String line = null;
//            StringBuffer text = new StringBuffer();
//            while ((line = reader.readLine()) != null) {
//                text.append(line).append("\n");
//            }
//            reader.close();
//
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/etc/fstab")));
//            text.append(String.format("%s %s iso9660 ro 0 0", filePath, mountPath));
//            writer.write(text.toString());
//            writer.close();
            String mountPath = fileUploadBase + "/" + mountDirName;
            Runtime runtime = Runtime.getRuntime();
            System.out.println(getProcessOut(runtime.exec(String.format("mkdir -p /%s", mountDirName))));
            System.out.println(getProcessOut(runtime.exec(String.format("mount %s /%s", filePath, mountDirName))));
            System.out.println(getProcessOut(runtime.exec(String.format("cp -r /%s/ %s", mountDirName, mountPath))));
            System.out.println(getProcessOut(runtime.exec(String.format("umount /%s", mountDirName))));
            System.out.println(getProcessOut(runtime.exec(String.format("rm -rf /%s", mountDirName))));

            //mount 之后 必须重启http服务才能生效
            dockerClientService.restartContainer("rackhd/on-http");
        } catch (Exception e) {
            System.out.println("挂载失败！");
            return false;
        }
        return true;
    }

    public boolean umountISO(String filePath, String mountPath) {
        try {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("/etc/fstab")));
//            String line = null;
//            StringBuffer text = new StringBuffer();
//            while ((line = reader.readLine()) != null) {
//                if (line.contains(mountPath)) continue;
//                text.append(line).append("\n");
//            }
//            reader.close();
//
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/etc/fstab")));
//            writer.write(text.toString());
//            writer.close();
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(String.format("rm -rf %s", mountPath));
//            Runtime.getRuntime().exec(String.format("umount %s", mountPath));
        } catch (Exception e) {
            System.out.println("取消挂载失败！");
            return false;
        }
        return true;
    }

    private static String getProcessOut(Process p) throws IOException {
        InputStream in = p.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line = null;
        StringBuffer sb = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        in.close();
        reader.close();
        return sb.toString();
    }

}
