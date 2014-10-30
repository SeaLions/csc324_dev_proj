import java.io.File;

public class UserInput
{
   private File rfpsFile;
   private File signalproFile;
   private File outputDirectory;
   
   public UserInput() {
      rfpsFile = null;
      signalproFile = null;
      outputDirectory = null;
   }
   
   public void setRfpsFile(File otherRfpsFile) {
      rfpsFile = otherRfpsFile;
   }
   
   public File getRfpsFile() {
      return rfpsFile;
   }

   public void setSignalProFile(File otherSignalproFile) {
      signalproFile = otherSignalproFile;
   }
   
   public File getSignalProFile() {
      return signalproFile;
   }
   
   public void setOutputDirectory(File outFileLoc) {
      outputDirectory = outFileLoc;
   }
   
   public File getOutputDirectory() {
      return outputDirectory;
   }
}