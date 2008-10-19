package edu.hawaii.ics.csdl.jupiter.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Utilities for common file IO.
 * 
 * @author jsakuda
 */
public class FileUtil {
  
  /** Prevent instantiation. */
  private FileUtil() {
    // do nothing
  }
  
  /**
   * Copy the source file to the destination file.
   * 
   * @param sourceFile the source <code>File</code>.
   * @param destinationFile the destination <code>File</code>.
   * @throws IOException if problems occur.
   */
  public static void copy(File sourceFile, File destinationFile) throws IOException {
    FileChannel sourceChannel = new FileInputStream(sourceFile).getChannel();
    FileChannel destinationChannel = new FileOutputStream(destinationFile).getChannel();
    destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
    sourceChannel.close();
    destinationChannel.close();
  }
}
