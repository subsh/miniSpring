package com.minis.minispring.web.context.support;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 扫描配置文件，装载需要被扫描的包
 */
public class XmlScanComponentHelper {
    public static List<String> getNodeValue(URL xmlPath){
        List<String> packages = new ArrayList<>();
        SAXReader saxReader = new SAXReader();
        Document document = null;

        try{
            // 加载配置文件
            document = saxReader.read(xmlPath);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Element root = document.getRootElement();
        Iterator it = root.elementIterator();
        while(it.hasNext()){
            // 得到xml中所有的base-package节点
            Element element = (Element) it.next();
            packages.add(element.attributeValue("base-package"));
        }

        return packages;
    }
}
