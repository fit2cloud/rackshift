package io.rackshift.metal.sdk.util;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class OutBandUtil {
    public static Object escapeStr(String str) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        try {
            engine.eval("function escapeStr(str)\n" +
                    "{\n" +
                    "    var tmp = new Array();\n" +
                    "    var i;\n" +
                    "    var escstr=\"\";\n" +
                    "    var desc;\n" +
                    "\n" +
                    "    str = str.replace(/\\\\/g, \"\\\\\\\\\");\n" +
                    "    tmp = str.split(\"\");\n" +
                    "    for(i=0; i<str.length; i++)\n" +
                    "    {\n" +
                    "        switch(tmp[i])\n" +
                    "        {\n" +
                    "            case '@':\n" +
                    "            case '(':\n" +
                    "            case ')':\n" +
                    "            case ',':\n" +
                    "            case ':':\n" +
                    "            case '?':\n" +
                    "            case '=':\n" +
                    "            case '&':\n" +
                    "            case '#':\n" +
                    "            case '+':\n" +
                    "            case '%':\n" +
                    "                dec = (tmp[i]+'').charCodeAt(0);\n" +
                    "                escstr+=\"@0\" + dec.toString(16);\n" +
                    "                break;\n" +
                    "            default:\n" +
                    "                escstr+=tmp[i];\n" +
                    "\n" +
                    "        }\n" +
                    "    }\n" +
                    "    return(escstr);\n" +
                    "}");
            if (engine instanceof Invocable) {
                Invocable in = (Invocable) engine;
                return in.invokeFunction("escapeStr", str);
            }
        } catch (Exception e) {
            LogUtil.error("IBM 转换字符串出错.....");
        }
        return str;
    }

    public static Object handleGermanChar(String str) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        try {
            engine.eval("function escapeStr(str) {\n" +
                    "  var tmp = new Array();\n" +
                    "  var i;\n" +
                    "  var escstr=\"\";\n" +
                    "  var dec;\n" +
                    "  // DF489680: idrac6 web GUI login fails with special character \"\\\" in password\n" +
                    "  // str = str.replace(/\\\\/g, \"\\\\\\\\\");\n" +
                    "  tmp = str.split(\"\");\n" +
                    "  for(i=0; i<str.length; i++) {\n" +
                    "    switch(tmp[i]) {\n" +
                    "      case '@':\n" +
                    "      case '(':\n" +
                    "      case ')':\n" +
                    "      case ',':\n" +
                    "      case ':':\n" +
                    "      case '?':\n" +
                    "      case '=':\n" +
                    "      case '&':\n" +
                    "      case '#':\n" +
                    "      case '+':\n" +
                    "      case '%':\n" +
                    "        dec = (tmp[i]+'').charCodeAt(0);\n" +
                    "        escstr+= \"@0\"+ dec.toString(16);\n" +
                    "        break;\n" +
                    "      default:\n" +
                    "        escstr+=tmp[i];\n" +
                    "    }\n" +
                    "  }\n" +
                    "  return(escstr);\n" +
                    "};\n" +
                    "\n" +
                    "  function handleGermanChar(str) {\n" +
                    "        var tmp = new Array();\n" +
                    "        var i;\n" +
                    "        var escstr=\"\";\n" +
                    "        var dec;\n" +
                    "\n" +
                    "        //str = str.replace(/\\\\/g, \"\\\\\\\\\");\n" +
                    "        tmp = str.split(\"\");\n" +
                    "        for(i=0; i<str.length; i++) {\n" +
                    "           dec = (tmp[i]+'').charCodeAt(0);\n" +
                    "           if(dec == 167) {\n" +
                    "              escstr += escape(tmp[i]);\n" +
                    "           } else {\n" +
                    "              escstr += encodeURIComponent(tmp[i]);\n" +
                    "           }\n" +
                    "        }\n" +
                    "        return(escstr);\n" +
                    "    }");
            if (engine instanceof Invocable) {
                Invocable in = (Invocable) engine;
                return in.invokeFunction("handleGermanChar", str);
            }
        } catch (Exception e) {
            LogUtil.error("IBM 转换字符串出错.....");
        }
        return str;
    }
}
