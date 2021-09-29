import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Document {

    public Application app;

    public File file;
    public String fileName;
    public String filePath;
    public String fileContent;

    public Boolean found;
    public Integer foundCount = 0;

    public String[] results;
    public String restOfContent;
    private final String catchRegex = "catch.\\(.*\\).\\{([^{}]*|\\{[^{}]*\\})*\\}";
    private Pattern pattern;
    private Pattern loggerPattern;

    public Document(File file, Application app){
        this.file = file;
        this.fileName = file.getName();
        this.filePath = file.getAbsolutePath();
        this.fileContent = this.getFileContent();
        this.pattern = Pattern.compile(this.catchRegex, Pattern.CASE_INSENSITIVE);
        this.loggerPattern = Pattern.compile("(looger|log)(\\.info)", Pattern.CASE_INSENSITIVE);
        this.app = app;
    }

    public String getFileContent(){
        try {
            return Files.readString(file.toPath());
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading file: "+file.getName());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void searchCatchBody(){
        Matcher matcher = this.pattern.matcher(this.fileContent);
        while(matcher.find()){
            this.searchLogger(matcher.group());
        }
    }

    public void searchLogger(String catchBody){
        Matcher matcher = this.loggerPattern.matcher(catchBody);
        while(matcher.find()){
            app.print("\u001B[31m<----------INFO LOGGER FOUND----------->\u001B[0m");
            app.print(this.filePath);
            app.print(catchBody.replace(matcher.group(), "\u001B[43m\u001B[30m"+matcher.group()+"\u001B[0m"));
            app.resultCount ++;
            app.print("\u001B[31m<----------*****************----------->\u001B[0m\n");
        }
    }


}
