package io.rackshift.config;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.constants.ServiceConstants;
import io.rackshift.engine.model.BaseTask;
import io.rackshift.engine.model.BaseTaskGraph;
import io.rackshift.engine.model.BaseTaskObject;
import io.rackshift.metal.sdk.util.LogUtil;
import io.rackshift.mybatis.domain.*;
import io.rackshift.mybatis.mapper.EndpointMapper;
import io.rackshift.mybatis.mapper.WorkflowMapper;
import io.rackshift.service.WorkflowService;
import io.rackshift.strategy.statemachine.LifeEventType;
import org.apache.commons.lang3.StringUtils;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

@Configuration
public class WorkflowConfig {
    @Resource
    private WorkflowMapper workflowMapper;
    @Resource
    private WorkflowService workflowService;
    @Resource
    private EndpointMapper endpointMapper;
    private List<Endpoint> endPoints;
    static EndpointMapper staticEndpointMapper;

    @Resource
    private void setEndpointMapper(EndpointMapper endpointMapper) {
        staticEndpointMapper = endpointMapper;
    }

    @Value("${run.mode:local}")
    private String runMode;

    @Value("${spring.datasource.url}")
    private String dataSource;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.flyway.table}")
    private String table;

    @PostConstruct
    public void initWorkflow() {

        Map<String, List<Workflow>> typeMap = workflowMapper.selectByExample(new WorkflowExample()).stream().collect(Collectors.groupingBy(Workflow::getEventType));
        if (typeMap != null && typeMap.keySet().size() > 0) {
            for (Map.Entry<String, List<Workflow>> wfEntry : typeMap.entrySet()) {
                if (StringUtils.isBlank(wfEntry.getKey())) continue;
                LifeEventType.valueOf(wfEntry.getKey()).addWorkflow(wfEntry.getValue().stream().map(Workflow::getInjectableName).collect(Collectors.toList()));
            }
        }
        EndpointExample e = new EndpointExample();
        endPoints = endpointMapper.selectByExample(e);
        endPoints = endPoints.stream().map(endpoint -> {
            if ("rackshift-proxy".equalsIgnoreCase(endpoint.getIp())) {
                try {
                    endpoint.setIp(getGateWayAddress(""));
                } catch (Exception unknownHostException) {
                    unknownHostException.printStackTrace();
                }
            }
            return endpoint;
        }).collect(Collectors.toList());

        Flyway flyway = Flyway.configure().dataSource(dataSource, username, password).table(table).baselineVersion("0").locations("classpath:db/migration").validateOnMigrate(false).load();

        flyway.migrate();
        //重写装机引擎 先执行变更脚本 后面要用到的
        Set<Map.Entry<String, BaseTaskGraph>> baseTasks = taskGraph().entrySet();
        baseTasks.forEach(baseTaskGraph -> {
            WorkflowWithBLOBs w = workflowService.getByInjectableName(baseTaskGraph.getValue().getInjectableName());
            if (w != null && w.getType().equalsIgnoreCase(ServiceConstants.TYPE_SYS)) {
                w.setTasks(baseTaskGraph.getValue().getTasks().toJSONString());
                if (baseTaskGraph.getValue().getOptions() != null) {
                    w.setOptions(baseTaskGraph.getValue().getOptions().toJSONString());
                }
                workflowService.update(w);
            }
        });
    }

    /**
     * 降低重写的成本 直接加载 RackHD 的源码
     *
     * @return
     */
    @Bean
    public Map<String, BaseTask> baseTask() {
        try {
            List<BaseTask> objs = new LinkedList<>();
            if ("local".equalsIgnoreCase(runMode)) {
                File[] files = new File(BaseTaskObject.class.getClassLoader().getResource("io/rackshift/engine/basetask").getFile()).listFiles();
                for (File f : files) {
                    if (f.getName().indexOf(".js") == -1)
                        continue;
                    String r = getString(f);
                    objs.add(JSONObject.parseObject(r, BaseTask.class));
                }
            } else {
                String urlStr = BaseTaskObject.class.getClassLoader().getResource("io/rackshift/engine/basetask").toString();
                String jarPath = urlStr.substring(0, urlStr.indexOf("!/") + 2);
                URL jarURL = new URL(jarPath);
                JarURLConnection jarCon = (JarURLConnection) jarURL.openConnection();
                JarFile jarFile = jarCon.getJarFile();
                Enumeration<JarEntry> jarEntrys = jarFile.entries();
                while (jarEntrys.hasMoreElements()) {
                    JarEntry entry = jarEntrys.nextElement();
                    String name = entry.getName();
                    if (name.startsWith("BOOT-INF/classes/io/rackshift/engine/basetask/") && !entry.isDirectory() && name.contains(".js")) {
                        // 开始读取文件内容
                        InputStream in = this.getClass().getClassLoader().getResourceAsStream(name);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        String r = getString(reader);
                        objs.add(JSONObject.parseObject(r, BaseTask.class));
                    }
                }
            }

            Map<String, BaseTask> map = new HashMap<>();
            for (BaseTask o : objs) {
                if (map.get(o.getInjectableName()) == null) {
                    map.put(o.getInjectableName(), o);
                }
            }
            return map;
        } catch (Exception e) {
            LogUtil.error("初始化 " + BaseTask.class + " 失败！", e);
        }

        return null;
    }


    /**
     * 降低重写的成本 直接加载 RackHD 的源码
     *
     * @return
     */
    @Bean
    public Map<String, BaseTaskGraph> taskGraph() {
        try {
            List<BaseTaskGraph> objs = new LinkedList<>();
            if ("local".equalsIgnoreCase(runMode)) {
                File[] files = new File(BaseTaskObject.class.getClassLoader().getResource("io/rackshift/engine/taskgraph").getFile()).listFiles();
                for (File f : files) {
                    if (f.getName().indexOf(".js") == -1)
                        continue;
                    String r = getString(f);
                    objs.add(JSONObject.parseObject(r, BaseTaskGraph.class));
                }
            } else {
                String urlStr = BaseTaskObject.class.getClassLoader().getResource("io/rackshift/engine/taskobject").toString();
                String jarPath = urlStr.substring(0, urlStr.indexOf("!/") + 2);
                URL jarURL = new URL(jarPath);
                JarURLConnection jarCon = (JarURLConnection) jarURL.openConnection();
                JarFile jarFile = jarCon.getJarFile();
                Enumeration<JarEntry> jarEntrys = jarFile.entries();
                while (jarEntrys.hasMoreElements()) {
                    JarEntry entry = jarEntrys.nextElement();
                    String name = entry.getName();
                    if (name.startsWith("BOOT-INF/classes/io/rackshift/engine/taskgraph/") && !entry.isDirectory() && name.contains(".js")) {
                        // 开始读取文件内容
                        InputStream in = this.getClass().getClassLoader().getResourceAsStream(name);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        String r = getString(reader);
                        objs.add(JSONObject.parseObject(r, BaseTaskGraph.class));
                    }
                }
            }

            Map<String, BaseTaskGraph> map = new HashMap<>();
            for (BaseTaskGraph o : objs) {
                if (map.get(o.getInjectableName()) == null) {
                    map.put(o.getInjectableName(), o);
                }
            }
            return map;
        } catch (Exception e) {
            LogUtil.error("初始化 " + BaseTaskGraph.class + " 失败！", e);
        }

        return null;
    }

    private String getString(File f) throws IOException, ScriptException, NoSuchMethodException {
        BufferedReader reader = new BufferedReader(new FileReader(f));
        return getString(reader);
    }

    private String getStringContent(File f) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(f));
        return getStringContent(reader);
    }

    private String getStringContent(BufferedReader reader) throws IOException {
        String line = null;
        StringBuffer sb = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        return sb.toString();
    }

    private String getString(BufferedReader reader) throws IOException, ScriptException, NoSuchMethodException {
        String line = null;
        StringBuffer sb = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("Nashorn");
        String a = sb.toString().replace("module.exports =", "var  a =") + "function json(){ return (JSON.stringify(a));}";
        engine.eval(a);
        Invocable invocable = (Invocable) engine;
        return (String) invocable.invokeFunction("json", "");
    }

    /**
     * 降低重写的成本 直接加载 RackHD 的源码
     *
     * @return
     */
    @Bean
    public Map<String, BaseTaskObject> taskObject() {
        try {
            List<BaseTaskObject> objs = new LinkedList<>();
            if ("local".equalsIgnoreCase(runMode)) {
                File[] files = new File(BaseTaskObject.class.getClassLoader().getResource("io/rackshift/engine/taskobject").getFile()).listFiles();
                for (File f : files) {
                    if (f.getName().indexOf(".js") == -1)
                        continue;
                    String r = getString(f);
                    objs.add(JSONObject.parseObject(r, BaseTaskObject.class));
                }
            } else {
                String urlStr = BaseTaskObject.class.getClassLoader().getResource("io/rackshift/engine/taskobject").toString();
                String jarPath = urlStr.substring(0, urlStr.indexOf("!/") + 2);
                URL jarURL = new URL(jarPath);
                JarURLConnection jarCon = (JarURLConnection) jarURL.openConnection();
                JarFile jarFile = jarCon.getJarFile();
                Enumeration<JarEntry> jarEntrys = jarFile.entries();
                while (jarEntrys.hasMoreElements()) {
                    JarEntry entry = jarEntrys.nextElement();
                    String name = entry.getName();
                    if (name.startsWith("BOOT-INF/classes/io/rackshift/engine/taskobject/") && !entry.isDirectory() && name.contains(".js")) {
                        // 开始读取文件内容
                        InputStream in = this.getClass().getClassLoader().getResourceAsStream(name);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        String r = getString(reader);
                        objs.add(JSONObject.parseObject(r, BaseTaskObject.class));
                    }
                }
            }

            Map<String, BaseTaskObject> map = new HashMap<>();
            for (BaseTaskObject o : objs) {
                if (map.get(o.getInjectableName()) == null) {
                    map.put(o.getInjectableName(), o);
                }
            }
            return map;
        } catch (Exception e) {
            LogUtil.error("初始化 " + BaseTaskObject.class + " 失败！", e);
        }

        return null;
    }

    /**
     * 降低重写的成本 直接加载 RackHD 的源码
     *
     * @return
     */
    @Bean
    public String commandParserScript() {
        try {
            String filePath = "io/rackshift/engine/util/command-parser.js";
            if ("local".equalsIgnoreCase(runMode)) {
                File file = new File(BaseTaskObject.class.getClassLoader().getResource(filePath).getFile());
                return getStringContent(file);
            } else {

                String urlStr = BaseTaskObject.class.getClassLoader().getResource(filePath).toString();
                String jarPath = urlStr.substring(0, urlStr.indexOf("!/") + 2);
                URL jarURL = new URL(jarPath);
                JarURLConnection jarCon = (JarURLConnection) jarURL.openConnection();
                JarFile jarFile = jarCon.getJarFile();
                Enumeration<JarEntry> jarEntrys = jarFile.entries();
                while (jarEntrys.hasMoreElements()) {
                    JarEntry entry = jarEntrys.nextElement();
                    String name = entry.getName();
                    if (name.startsWith("BOOT-INF/classes/" + filePath) && !entry.isDirectory() && name.contains(".js")) {
                        // 开始读取文件内容
                        InputStream in = this.getClass().getClassLoader().getResourceAsStream(name);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        return getStringContent(reader);
                    }
                }
                return null;
            }
        } catch (Exception e) {
            LogUtil.error("初始化  commandParserScript  失败！", e);
        }

        return null;
    }

    @Bean
    public String ejsScript() {
        try {
            String filePath = "io/rackshift/engine/util/ejs.min.js";
            if ("local".equalsIgnoreCase(runMode)) {
                File file = new File(BaseTaskObject.class.getClassLoader().getResource(filePath).getFile());
                return getStringContent(file);
            } else {

                String urlStr = BaseTaskObject.class.getClassLoader().getResource(filePath).toString();
                String jarPath = urlStr.substring(0, urlStr.indexOf("!/") + 2);
                URL jarURL = new URL(jarPath);
                JarURLConnection jarCon = (JarURLConnection) jarURL.openConnection();
                JarFile jarFile = jarCon.getJarFile();
                Enumeration<JarEntry> jarEntrys = jarFile.entries();
                while (jarEntrys.hasMoreElements()) {
                    JarEntry entry = jarEntrys.nextElement();
                    String name = entry.getName();
                    if (name.startsWith("BOOT-INF/classes/" + filePath) && !entry.isDirectory() && name.contains(".js")) {
                        // 开始读取文件内容
                        InputStream in = this.getClass().getClassLoader().getResourceAsStream(name);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        return getStringContent(reader);

                    }
                }
                return null;
            }
        } catch (Exception e) {
            LogUtil.error("初始化  ejsScript  失败！", e);
        }

        return null;
    }

    @Bean
    public String hoganScript() {
        try {
            String filePath = "io/rackshift/engine/util/hogan.js";
            if ("local".equalsIgnoreCase(runMode)) {
                File file = new File(BaseTaskObject.class.getClassLoader().getResource(filePath).getFile());
                return getStringContent(file);
            } else {

                String urlStr = BaseTaskObject.class.getClassLoader().getResource(filePath).toString();
                String jarPath = urlStr.substring(0, urlStr.indexOf("!/") + 2);
                URL jarURL = new URL(jarPath);
                JarURLConnection jarCon = (JarURLConnection) jarURL.openConnection();
                JarFile jarFile = jarCon.getJarFile();
                Enumeration<JarEntry> jarEntrys = jarFile.entries();
                while (jarEntrys.hasMoreElements()) {
                    JarEntry entry = jarEntrys.nextElement();
                    String name = entry.getName();
                    if (name.startsWith("BOOT-INF/classes/" + filePath) && !entry.isDirectory() && name.contains(".js")) {
                        // 开始读取文件内容
                        InputStream in = this.getClass().getClassLoader().getResourceAsStream(name);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        return getStringContent(reader);

                    }
                }
                return null;
            }
        } catch (Exception e) {
            LogUtil.error("初始化  hoganScript  失败！", e);
        }

        return null;
    }

    /**
     * 降低重写的成本 直接加载 RackHD 的源码
     *
     * @return
     */
    @Bean
    public String lodashScript() {
        try {
            String filePath = "io/rackshift/engine/util/lodash.min.js";
            if ("local".equalsIgnoreCase(runMode)) {
                File file = new File(BaseTaskObject.class.getClassLoader().getResource(filePath).getFile());
                return getStringContent(file);
            } else {

                String urlStr = BaseTaskObject.class.getClassLoader().getResource(filePath).toString();
                String jarPath = urlStr.substring(0, urlStr.indexOf("!/") + 2);
                URL jarURL = new URL(jarPath);
                JarURLConnection jarCon = (JarURLConnection) jarURL.openConnection();
                JarFile jarFile = jarCon.getJarFile();
                Enumeration<JarEntry> jarEntrys = jarFile.entries();
                while (jarEntrys.hasMoreElements()) {
                    JarEntry entry = jarEntrys.nextElement();
                    String name = entry.getName();
                    if (name.startsWith("BOOT-INF/classes/" + filePath) && !entry.isDirectory() && name.contains(".js")) {
                        // 开始读取文件内容
                        InputStream in = this.getClass().getClassLoader().getResourceAsStream(name);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        return getStringContent(reader);
                    }
                }
                return null;
            }
        } catch (Exception e) {
            LogUtil.error("初始化  lodash.min.js  失败！", e);
        }

        return null;
    }

    private static String getGateWayAddress(String ip) throws UnknownHostException, SocketException {
        String localhost = Inet4Address.getLocalHost().getHostAddress();
        if (StringUtils.isNotBlank(ip)) {
            localhost = ip;
        }
        int maskLength = NetworkInterface.getByInetAddress(Inet4Address.getLocalHost()).getInterfaceAddresses().get(0).getNetworkPrefixLength();
        StringBuffer sb = new StringBuffer();
        StringBuffer number = new StringBuffer();

        int i = 0;
        while (i < 32) {
            if (i < maskLength) {
                sb.append("1");
            }
            if ((i + 1) % 8 == 0) {
                number.append(Integer.valueOf(sb.toString().equals("") ? "0" : sb.toString()));
                sb = new StringBuffer();
                if (i != 31) {
                    number.append(".");
                }
            }
            i++;
        }

        String[] s1 = localhost.split("\\.");

        String[] s2 = number.toString().split("\\.");

        for (i = 0; i < s1.length; i++) {
            s1[i] = String.valueOf(Integer.valueOf(s1[i]) & Integer.valueOf(s2[i], 2));
        }
        s1[s1.length - 1] = "1";
        return StringUtils.join(s1, ".");
    }

    public static String geRackhdUrlById(String id) {
        Endpoint endpoint = staticEndpointMapper.selectByPrimaryKey(id);

        if ("rackshift-proxy".equalsIgnoreCase(endpoint.getIp())) {
            try {
                endpoint.setIp(getGateWayAddress(""));
            } catch (Exception unknownHostException) {
                unknownHostException.printStackTrace();
            }
        }

        if (endpoint != null && !endpoint.getIp().contains(":"))
            return "http://" + endpoint.getIp() + ":9090";
        if (endpoint == null)
            return null;

        return "http://" + endpoint.getIp();
    }

    public List<Endpoint> getEndPoints() {
        return endPoints;
    }
}
