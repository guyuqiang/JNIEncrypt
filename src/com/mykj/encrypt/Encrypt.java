
package com.mykj.encrypt;


import java.io.*;

/**
 * Auther: guyuqiang  <br/>
 * Date: 2021/5/7:17:55  <br/>
 * Description:
 */
public class Encrypt extends ClassLoader {

    private char [] password = {'M','y','k','j','2','0','2','1'};

    /**
     * 加密方法
     * @param classFilePath 需要加密的class文件路径
     */
    public void encryptClass(String classFilePath) {

        // 为待加密的class文件创建File对象
        File classFile = new File(classFilePath);
        if (!classFile.exists()) {
            System.out.println("没有获取到文件");
            return;
        }
        // 加密后的文件去掉文件后缀名
        String encryptedFile = classFile.getParent()+File.separator+classFile.getName();
        encryptedFile = encryptedFile.substring(0,encryptedFile.indexOf("."));
        try (
                FileInputStream fileInputStream = new FileInputStream(classFile);
                BufferedInputStream bis = new BufferedInputStream(fileInputStream);
                FileOutputStream fileOutputStream = new FileOutputStream(encryptedFile);
                BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream)
        ) {

            // 加密
            int data;
            int i=0;
            while ((data = bis.read()) != -1) {
                bos.write(data ^ password[i%password.length]);
                i++;
            }
            bos.flush();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * 解密方法
     * @param className 需要解密的文件名
     */
    protected byte[] decryptClass(String className) {
        String packageName=className.replace(".", "/");
        String classPath = getClassPath();
        // 为待加密的文件创建File对象
        String filePath = classPath+packageName;
        File decryptClassFile = new File(filePath);
        if (!decryptClassFile.exists()) {
            System.out.println("decryptClass() File:" + filePath + " not found!");
            return null;
        }
        byte[] result = null;
        BufferedInputStream bis = null;
        ByteArrayOutputStream bos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(decryptClassFile));
            bos = new ByteArrayOutputStream();
            // 解密
            int data;
            int i=0;
            while ((data = bis.read()) != -1) {
                bos.write(data ^ password[i%password.length]);
                i++;
            }
            bos.flush();
            result = bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private static String  getClassPath(){
        String classPath = Encrypt.class.getResource("/").getPath() ;
        if(File.separator.equals("\\")){
            classPath=classPath.substring(1);
        }
        return classPath;
    }


    // 测试
    public static void main(String[] args) {
        Encrypt encrypt = new Encrypt();
        //encrypt.encryptClass("X:\\workspace\\my\\JNIEncrypt\\target\\classes\\com\\redxun\\sys\\core\\util\\JsaasUtil.class");
        //Class c = encrypt.decryptClass("com.redxun.sys.core.util.JsaasUtil");
    }
}
