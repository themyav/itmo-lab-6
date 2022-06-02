package server.models.parsers;

import org.w3c.dom.*;
import org.xml.sax.*;
import server.ServerManager;
import server.models.enumerations.fields.CoordinatesField;
import server.models.enumerations.fields.DradonField;
import server.models.enumerations.fields.PersonField;
import server.models.interfaces.Fieldable;
import server.models.elements.Coordinates;
import server.models.elements.ServerDragon;
import server.models.elements.ParsedObject;
import server.models.elements.Person;

import javax.xml.parsers.*;
import java.io.*;
import java.util.*;

/**
 * Парсер, производящий считывание из xml.
 */
public class XmlReader extends XmlParser{

    /**
     * Создает новый парсер.
     *
     * @param dragons  коллекция драконов
     * @param filePath путь к xml-файлу.
     */
    public XmlReader(Set<ServerDragon> dragons, String filePath){
        super(dragons, filePath);
    }

    /**
     * Set file reader buffered reader.
     *
     * @return the buffered reader
     */
    private BufferedReader setFileReader(){
        File xmlFile = new File(this.filePath);
        BufferedReader fileReader = null;
        try {
            fileReader = new BufferedReader(new FileReader(xmlFile));
        }
        catch (FileNotFoundException e){
            crushFinish(String.format("Файл %s не найден", filePath));
        }
        return fileReader;
    }

    /**
     * Set document builder document builder.
     *
     * @return the document builder
     */
    private DocumentBuilder setDocumentBuilder(){
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            crushFinish("Произошла ошибка при создании парсера");
        }
        return docBuilder;
    }

    /**
     * Set document document.
     *
     * @param fileReader the file reader
     * @param docBuilder the doc builder
     * @return the document
     */
    private Document setDocument(BufferedReader fileReader, DocumentBuilder docBuilder){
        InputSource file = new InputSource(fileReader);
        Document xmlDom = null;
        try {
            xmlDom = docBuilder != null ? docBuilder.parse(file) : null;
        } catch (SAXException | IOException e) {
            crushFinish("Произошла ошибка при создании xml-дерева");
        }
        return xmlDom;
    }

    /**
     * Get root node.
     *
     * @param xmlDom the xml dom
     * @return the node
     */
    private Node getRoot(Document xmlDom){
        if(xmlDom == null) {
            crushFinish("XML-файл пуст");
            return null;
        }
        else return xmlDom.getDocumentElement();

    }

    /**
     * Get children node list.
     *
     * @param root the root
     * @return the node list
     */
    private NodeList getChildren(Node root){
        if(root == null) return null;
        else return root.getChildNodes();
    }


    /**
     * Считыает данные из файла.
     */
    public void readData(){

        BufferedReader fileReader = setFileReader();
        DocumentBuilder docBuilder = setDocumentBuilder();
        Document xmlDom = setDocument(fileReader, docBuilder);
        Node root = getRoot(xmlDom);
        NodeList children = getChildren(root);
        if(children == null) return;


        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() != Node.TEXT_NODE) {
                ServerDragon serverDragon = new ServerDragon();
                parseXML(child, serverDragon, DradonField.values());
                dragons.add(serverDragon);
            }
        }
        checkCollection();
    }

    /**
     * Прекращает работу программы в случае некорректного входного файла.
     *
     * @param errorMessage сообщение об ошибке
     */
    private void crushFinish(String errorMessage){
        System.out.println("Ошибка при попытке чтения с файла:");
        System.out.println(errorMessage);
        System.exit(0);
    }

    /**
     * проверяет, что элемент, который необходимо добавить в коллекцию, кооректен.
     */
    private void checkCollection(){

        for(ServerDragon serverDragon : dragons){
            try{
                serverDragon.checkElement(DradonField.values());
            }catch (Exception e){
                crushFinish(e.getMessage());
            }
            ServerManager.dragonIdList.add(serverDragon.getId());
            if(serverDragon.getKiller() != null && serverDragon.getKiller().getPassportID() != null){
                ServerManager.personIdList.add(serverDragon.getKiller().getPassportID());
            }
        }
    }

    /**
     * Производит парсинг xml.
     *
     * @param <O>    тип объекта, значения полей которого считываются из xml
     * @param <T>    тип данных, соответсвующий enumeration, хранящему поля объекта
     * @param root   корневой элемент xml-поддерева
     * @param object объект, значения полей которого считываются из xml
     * @param values список полей объекта
     */
    public <O extends ParsedObject<O, T>, T extends Enum<T> & Fieldable<O>> void parseXML(Node root, O object, T[] values){
        NodeList children = root.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if(child.getNodeType() == Node.TEXT_NODE) continue;
            String childName = child.getNodeName().toUpperCase(Locale.ROOT);
            if(childName.equals("COORDINATES")){
                Coordinates coordinates = new Coordinates();
                parseXML(child, coordinates, CoordinatesField.values());
                ((ServerDragon)object).setCoordinates(coordinates);
            }
            else if(childName.equals("KILLER")) {
                Person killer = new Person();
                parseXML(child, killer, PersonField.values());
                ((ServerDragon) object).setKiller(killer);
            }
            else{
                try {
                    object.setFields(child, values);
                }catch (Exception e){
                    crushFinish(e.getMessage());
                }
            }

        }
    }
}
