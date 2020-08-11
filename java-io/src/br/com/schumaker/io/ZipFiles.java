package br.com.schumaker.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author schumaker
 */
public final class ZipFiles {
    
    /**
     * @throws IOException 
     */
    public void unzipFile() throws IOException {
        this.createNASMDir();
        
        var nasmZip = System.getProperty("user.dir") + "/nasm.zip";
        var destDir = new File(System.getProperty("user.dir") + "/nasm");
        
        byte[] buffer = new byte[1024];
        var zis = new ZipInputStream(new FileInputStream(nasmZip));
        var zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            var newFile = this.newFile(destDir, zipEntry);
            var fos = new FileOutputStream(newFile);
            int length;
            while ((length = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
            fos.close();
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
    }
    
    public File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        var destFile = new File(destinationDir, zipEntry.getName());
        var destDirPath = destinationDir.getCanonicalPath();
        var destFilePath = destFile.getCanonicalPath();
        
        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("outside of target dir: " + zipEntry.getName());
        }
        return destFile;
    }
}
