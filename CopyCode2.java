import java.io.*;

public class CopyCode2 {
    public static void main(String[] args){
        File readFrom = new File(
                "C:\\Users\\Administrator\\Desktop\\.net\\abc");
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
            //把图片目录排除在外
            if(!readFrom.getParentFile().getName().equals("images")){
                CC cc = new CC(readFrom, writeTo);
                FileReader fileReader = cc.loadReader();
                FileWriter fileWriter = cc.loadWriter(writeTo);

                //使用buffered提供的readline方法
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String readFile = cc.readFile_line(bufferedReader);


                //开始写入文档正文
                cc.writeFile_line(readFrom.getName(), fileWriter);
                while(readFile != null){
                    cc.writeFile_line(readFile, fileWriter);
                    readFile = cc.readFile_line(bufferedReader);
                }

                //写入文档尾部的分隔
                cc.writeFile_line("\r\n", fileWriter);
                cc.writeFile_line("\r\n", fileWriter);
                cc.writeFile_line("\r\n", fileWriter);

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
     * 读取文件，返回字符串,每次操作一行数据
     */
    public String readFile_line(BufferedReader bufferedReader) throws IOException {
        String r = bufferedReader.readLine();
        System.out.println(r != null ? r:"null");
        return r != null ? r: null;
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

    /**
     * 把字符串写入文件，每次操作一行数据
     */
    public void writeFile_line(String text, FileWriter fileWriter) throws IOException {
        if(text != null){
            fileWriter.write(text);
            fileWriter.write("\r\n");
        }
    }

}
