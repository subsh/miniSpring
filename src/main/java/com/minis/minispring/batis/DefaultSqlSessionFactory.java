package com.minis.minispring.batis;

import com.minis.minispring.beans.factory.annotation.Autowired;
import com.minis.minispring.jdbc.core.JdbcTemplate;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DefaultSqlSessionFactory implements SqlSessionFactory{
    @Autowired
    JdbcTemplate jdbcTemplate;

    String mapperLocations;
    Map<String,MapperNode> mapperNodeMap = new HashMap<>();

    public String getMapperLocations() {
        return mapperLocations;
    }

    public void setMapperLocations(String mapperLocations) {
        this.mapperLocations = mapperLocations;
    }

    public Map<String, MapperNode> getMapperNodeMap() {
        return mapperNodeMap;
    }

    public DefaultSqlSessionFactory() {
    }

    public void init(){
        scanLocation(this.mapperLocations);
    }

    private void scanLocation(String location) {
        String sLocationPath = this.getClass().getClassLoader().getResource("").getPath() + location;
        File dir = new File(sLocationPath);
        for(File file : dir.listFiles()){
            if(file.isDirectory()){
                scanLocation(location + "/" + file.getName());
            }else{
                buildMapperNodes(location + "/" + file.getName());
            }
        }
    }

    private Map<String, MapperNode> buildMapperNodes(String filePath){
        System.out.println(filePath);
        SAXReader saxReader = new SAXReader();
        URL xmlPath = this.getClass().getClassLoader().getResource(filePath);
        try{
            Document document = saxReader.read(xmlPath);
            Element rootElement = document.getRootElement();
            String namespace = rootElement.attributeValue("namesapce");
            Iterator<Element> nodes = rootElement.elementIterator();;
            while (nodes.hasNext()) {
                Element node = nodes.next();
                String id = node.attributeValue("id");
                String parameterType = node.attributeValue("parameterType");
                String resultType = node.attributeValue("resultType");
                String sql = node.getText();

                MapperNode selectnode = new MapperNode();
                selectnode.setNamespace(namespace);
                selectnode.setId(id);
                selectnode.setParameterType(parameterType);
                selectnode.setResultType(resultType);
                selectnode.setSql(sql);
                selectnode.setParameter("");

                this.mapperNodeMap.put(namespace + "." + id, selectnode);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return this.mapperNodeMap;
    }

    public MapperNode getMapperNode(String name){
        return this.mapperNodeMap.get(name);
    }

    @Override
    public SqlSession openSession() {
        SqlSession newSqlSession = new DefaultSqlSession();
        newSqlSession.setJdbcTemplate(jdbcTemplate);
        newSqlSession.setSqlSessionFactory(this);

        return newSqlSession;
    }
}
