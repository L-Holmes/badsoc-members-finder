import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

//https://stackoverflow.com/questions/40255039/how-to-choose-file-in-java
public class FileChooser {


    public File chooseCSVFile()
    {
        return chooseFile("CSV files", "csv");
    }

    public File chooseFile(String fileTypeInfoText, String fileExtension){
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                fileTypeInfoText, fileExtension);
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to open this file: " +
                    chooser.getSelectedFile().getName());

            return chooser.getSelectedFile();
        }
        return null;
    }
}
