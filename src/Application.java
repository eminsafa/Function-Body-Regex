import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Application {

    private File root;

    private List<String> fileTypes = new ArrayList<>();
    private List<Document> documents = new ArrayList<>();

    public Integer resultCount = 0;
    public Integer documentCount = 0;

    private final String rootDirectoryPath = ".";
    private final Boolean consoleOutput = true;
    private final Boolean consoleFilePathOutput = true;

    public static void main(String[] args) {

        Application app = new Application();
        //app.setRootDirectory(".");
        app.addFileType("java");
        app.run();

    }

    public Application(){
        this.root = new File(this.rootDirectoryPath);
    }

    private void addFileType(String fileType){
        fileType = fileType.replace(".", "");
        this.fileTypes.add(fileType);
    }

    public static String getExtension(String fileName){
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            return fileName.substring(i+1);
        }
        return null;
    }

    public Document createDocument(File file){
        Document document = new Document(file, this);
        this.documents.add(document);
        this.documentCount ++;
        return document;
    }

    public void explore(File file){
        if (file.isDirectory()) {
            File[] subFiles = file.listFiles();
            if (subFiles != null){
                for (File subFile : subFiles) {
                    explore(subFile);
                }
            }
        } else {
            if (this.fileTypes.contains(getExtension(file.getName()))) {
                Document document = createDocument(file);
                document.searchCatchBody();
            }
        }
    }

    private void run(){
        this.explore(this.root);
        this.report();
    }

    private void report(){
        if (consoleOutput){
            System.out.println("Number of Searched Files    :     "+this.documentCount);
            System.out.println("Number of Wrong Logger      :     "+this.resultCount);
        }
    }

    public void print(String text){
        if(consoleOutput){
            System.out.println(text);
        }
    }

}
