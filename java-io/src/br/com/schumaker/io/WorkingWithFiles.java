package br.com.schumaker.io;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author hudson schumaker
 */
public class WorkingWithFiles {

    public void writeStringUsingBufferedWritter(String fileName) throws IOException {
        String str = "Hallo Welt !!!";
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(str);
        writer.close();
    }

    public void writingStringToFileUsingPrintWriter(String fileName) throws IOException {
        FileWriter fileWriter = new FileWriter(fileName);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print("Some String");
        printWriter.printf("Product name is %s and its price is %d $", "iPhone", 1000);
        printWriter.close();

        // Result:
        // Some String
        // Product name is iPhone and its price is 1000$
    }

    public void writingStringToFileUsingFileOutputStream(String fileName) throws IOException {
        String str = "Hello";
        FileOutputStream outputStream = new FileOutputStream(fileName);
        byte[] strToBytes = str.getBytes();
        outputStream.write(strToBytes);

        outputStream.close();
    }

    public void writingToFileUsingDataOutputStream(String fileName) throws IOException {
        String value = "Hello";
        FileOutputStream fos = new FileOutputStream(fileName);
        DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(fos));
        outStream.writeUTF(value);
        outStream.close();

        // verify the results
        String result;
        FileInputStream fis = new FileInputStream(fileName);
        DataInputStream reader = new DataInputStream(fis);
        result = reader.readUTF();
        reader.close();
    }

    public void writingToSpecificPositionInFile(String fileName, String fileName2) throws IOException {
        int data1 = 2014;
        int data2 = 1500;

        writeToPosition(fileName, data1, 4);
        if (data1 == readFromPosition(fileName, 4)) {
            System.out.println("ok");
        }

        writeToPosition(fileName2, data2, 4);
        if (data2 == readFromPosition(fileName, 4)) {
            System.out.println("ok");
        }
    }

    private void writeToPosition(String filename, int data, long position) throws IOException {
        RandomAccessFile writer = new RandomAccessFile(filename, "rw");
        writer.seek(position);
        writer.writeInt(data);
        writer.close();
    }

    private int readFromPosition(String filename, long position) throws IOException {
        int result = 0;
        RandomAccessFile reader = new RandomAccessFile(filename, "r");
        reader.seek(position);
        result = reader.readInt();
        reader.close();
        return result;
    }

    public void ReadingFileUsingFileChannel(String fileName) throws IOException {
        RandomAccessFile stream = new RandomAccessFile(fileName, "rw");
        FileChannel channel = stream.getChannel();
        String value = "Hello";
        byte[] strBytes = value.getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(strBytes.length);
        buffer.put(strBytes);
        buffer.flip();
        channel.write(buffer);
        stream.close();
        channel.close();

        // verify
        RandomAccessFile reader = new RandomAccessFile(fileName, "r");
        if (value == null ? reader.readLine() == null : value.equals(reader.readLine())) {
            System.out.println("ok");
        }
        reader.close();
    }

    public void usingJava7ToWriteToFile(String fileName) throws IOException {
        String str = "Hello";

        Path path = Paths.get(fileName);
        byte[] strToBytes = str.getBytes();

        Files.write(path, strToBytes);

        String read = Files.readAllLines(path).get(0);
        //assertEquals(str, read);
        if (str.equals(read)) {
            System.out.println("ok");
        }
    }

    public void writeToTmpFile() throws IOException {
        String toWrite = "Hello";
        File tmpFile = File.createTempFile("test", ".tmp");
        FileWriter writer = new FileWriter(tmpFile);
        writer.write(toWrite);
        writer.close();

        BufferedReader reader = new BufferedReader(new FileReader(tmpFile));
        //assertEquals(toWrite, reader.readLine());
        reader.close();
    }

    public void whenTryToLockFile_thenItShouldBeLocked(String fileName) throws IOException {
        RandomAccessFile stream = new RandomAccessFile(fileName, "rw");
        FileChannel channel = stream.getChannel();

        FileLock lock = null;
        try {
            lock = channel.tryLock();
        } catch (final OverlappingFileLockException e) {
            stream.close();
            channel.close();
        }
        stream.writeChars("test lock");
        lock.release();

        stream.close();
        channel.close();
    }
    
    public List<String> read(String filePath) {
     
        List<String> lines = new ArrayList<>();
        
        try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
            lines = br.lines().collect(Collectors.toList());

        } catch (IOException e) {
            System.err.println(e);
        }
        
        return lines;
    }

}
