package com.mykj.classloader;

public class ClassLoadUtil {

    static{
        //系统加载其它语言的函数
        String arch = System.getProperty("os.arch");
        String os = System.getProperty("os.name").toLowerCase();
        String path=ClassLoadUtil.class.getResource("/").getPath() ;
        path=path.replace("classes/", "dll/");

        if(os.contains("win")){
            if(arch.contains("64")){
                // windows 64位
                System.load(path+"ClassLoaderTest64.dll");
            }
            else{
                // windows 32位
                System.load(path+"ClassLoader.dll");
            }
        }else{
            System.load(path+"ClassLoader.so");
        }
    }

    //natice关键字.标记这个接口,看起来像是abstract
    private static native byte [] decryptClass(String className);

    public static void main(String[] args) {
        ClassLoadUtil classLoadUtil = new ClassLoadUtil();
        byte []  bytes = decryptClass("com.redxun");
        String srt = bytes.toString();
        System.out.println(srt);
    }
}


