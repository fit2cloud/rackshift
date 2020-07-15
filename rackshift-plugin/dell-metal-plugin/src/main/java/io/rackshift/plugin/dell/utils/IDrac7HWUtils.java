package io.rackshift.plugin.dell.utils;

import org.jdom.Document;
import org.jdom.Element;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class IDrac7HWUtils {
    public static <T> List<T> wrapObject(Document doc, String attributeValue, Class<T> tClass) {

        List<Element> elements = (List<Element>) doc.getRootElement().getChildren().stream().filter(c -> ((Element) c).getAttribute("Classname").getValue().equalsIgnoreCase(attributeValue)).collect(Collectors.toList());
        List<T> resLst = new LinkedList<>();
        elements.forEach(e -> {
            try {
                T obj = tClass.newInstance();
                Arrays.stream(tClass.getDeclaredFields()).forEach(f -> {
                    try {
                        f.setAccessible(true);
                        f.set(obj, ((List<Element>) e.getContent().stream().filter(e1 -> e1 instanceof Element).collect(Collectors.toList())).stream().filter(p -> p.getAttribute("NAME").getValue().equalsIgnoreCase(f.getName())).findFirst().get().getContent(3).getValue().trim());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                });
                resLst.add(obj);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        });
        return resLst;
    }
}
