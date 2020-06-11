import jdk.internal.util.xml.impl.Input;

import java.io.*;

public class CopyCode {
    public static void main(String[] args){
        File readFrom = new File("D:\\wxProgram\\testCloud\\miniprogram\\pages");
        File writeTo = new File("D:\\copyFile.txt");

        try {
            dfsForFile(readFrom,writeTo);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void dfsForFile(File readFrom, File writeTo) throws IOException {
        //1、如果是文件夹，对每一个文件 递归直到最后一层里面全是文件
        //2、读取文件（输出文件内容）
        //3、把读取到的内容写入指定文件（格式为：文件名，换行，输出文件内容）
        //4、关闭文件，反复执行2,3，4直至搞定最后一个文件

        if(readFrom.isDirectory()){
            File[] files = readFrom.listFiles();
            for(int i=0;i<files.length;i++){
                System.out.println(readFrom.getAbsoluteFile()+"\\"+
                        files[i].getName());
                dfsForFile(new File(readFrom.getAbsoluteFile()+"\\"+
                        files[i].getName()), writeTo);
            }
        } else{
            //取消复制单独的图片或视频文件夹
            String name = readFrom.getParentFile().getName();
            if(!name.equals('images') || !name.equals('audios')){
                CC cc = new CC(readFrom, writeTo);
                FileReader fileReader = cc.loadReader();
                
                String readFile = cc.readFile(fileReader);

                FileWriter fileWriter = cc.loadWriter(writeTo);

                cc.writeFile(readFrom.getName(), fileWriter);
                cc.writeFile("\r\n", fileWriter);
                cc.writeFile(readFile, fileWriter);
                cc.writeFile("\r\n", fileWriter);
                cc.writeFile("\r\n", fileWriter);
                cc.writeFile("\r\n", fileWriter);
                cc = null;
                fileReader.close();
                fileWriter.close();
                
           }


        }


    }
}

class CC{
    private File readFrom;
    private File writeTo;

    public CC(File readFrom, File writeTo) {
        this.readFrom = readFrom;
        this.writeTo = writeTo;
    }

    /**
     * 创建读取文件流
     */
    public FileReader loadReader() throws FileNotFoundException {
        FileReader fileReader = new FileReader(readFrom);
        return fileReader != null? fileReader : null;
    }

    /**
     * 读取文件，返回字符串
     */
    public String readFile(FileReader fileReader) throws IOException {
        StringBuffer s = new StringBuffer();
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String r = bufferedReader.readLine();
        s.append(r);
        while(r != null){
            r = bufferedReader.readLine();
            if(r != null)
                s.append(r);
        }
        return s.toString();
    }

    /**
     * 创建写文件流
     */
    public FileWriter loadWriter(File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(writeTo, true);
        return fileWriter != null? fileWriter : null;
    }

    /**
     * 把字符串写入文件
     */
    public void writeFile(String text, FileWriter fileWriter) throws IOException {
        fileWriter.write(text);
    }
}
