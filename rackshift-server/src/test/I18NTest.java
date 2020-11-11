import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class I18NTest {

    //提取项目里面国际化的关键字
    public static void main(String[] args) throws IOException {
//        File file = new File("D:\\rackshift\\rackshift-web\\src\\common\\validator\\CommonValidator.js");
//        File file = new File("D:\\rackshift\\rackshift-web\\src\\common\\utils");
        File file = new File("D:\\rackshift\\rackshift-web\\src\\rackparams");
//        File file = new File("D:\\rackshift\\rackshift-web\\src\\components");
        File file2 = new File("D:\\rackshift\\rackshift-web\\src\\i18n\\zh-CN.json");
        BufferedReader reader = new BufferedReader(new FileReader(file2));
        String line = null;
        StringBuffer sb = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        JSONObject i18n = JSONObject.parseObject(sb.toString());
        JSONObject r = JSONObject.parseObject("{}");
        sb.setLength(0);
        if (file.isDirectory()) {
            for (File listFile : file.listFiles()) {
                if (listFile.isDirectory()) {
                    for (File file11 : listFile.listFiles())
                        extract(file11, i18n, r);
                } else {
                    extract(listFile, i18n, r);
                }
            }
        } else {
            extract(file, i18n, r);
        }

//        for (Map.Entry o : i18n.entrySet()) {
//            System.out.println("\"" + o.getKey() + "\"" + ":" + "\"" + o.getValue() + "\",");
//        }

        for (Map.Entry o : r.entrySet()) {
            System.out.println("\"" + o.getKey() + "\"" + ":" + "\"" + o.getValue() + "\",");
        }
    }

    private static void extract(File listFile, JSONObject i18n, JSONObject r) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(listFile));
        String line = null;
        StringBuffer sb = new StringBuffer();

        if (listFile.getName().contains("js") || listFile.getName().contains("vue")) {
            line = null;
            sb = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

//            Pattern p = Pattern.compile("t\\(['\"]*([a-zA-Z_0-9-!?]*)['\"]*\\)");
//            Pattern p = Pattern.compile("t\\(['\"]*([a-zA-Z_0-9-!?]*)['\"]*\\)");
            Pattern p = Pattern.compile("t\\(['\"]*([a-zA-Z_0-9-!?,/！ ]*)['\"],\\s*['\"]([a-zA-Z_0-9-!?,/！：， \\u4e00-\\u9fa5]*)['\"]*\\)");
            Matcher m = p.matcher(sb.toString());
            while (m.find()) {
//                if (!i18n.containsKey(m.group(1).trim())) {
//                    i18n.put(m.group(1), null);
//                    r.put(m.group(1), null);
//                }
                if (!r.containsKey(m.group(1)) && !i18n.containsKey(m.group(1)))
                    r.put(m.group(1), m.group(2));
            }
        }
    }


//    public static void main(String[] args) {
//        Pattern p = Pattern.compile("t\\(['\"]*([a-zA-Z_0-9-!?,/！ ]*)['\"],\\s*['\"]([a-zA-Z_0-9-!?,/！ \\u4e00-\\u9fa5]*)['\"]*\\)");
//        Matcher m = p.matcher("$t('i18n_di','第')");
//        m.find();
//        return m.group(2)
//    }
}
