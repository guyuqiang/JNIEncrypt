package com.mykj.classloader;

import com.redxun.core.exception.LicenseException;

import java.io.File;
import java.lang.reflect.Method;

public class ClassLoadUtil extends ClassLoader{

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
    private static native byte [] decryptClass(String classFilePath);

    private static String  getClassPath(){
        String classPath = ClassLoadUtil.class.getResource("/").getPath() ;
        if(File.separator.equals("\\")){
            classPath=classPath.substring(1);
        }
        return classPath;
    }

    public Class getClass(String className){
        String packageName=className.replace(".", "/");
        //获取class根路径
        String classPath = getClassPath();
        String classFilePath = classPath+packageName;
        //解密class
        byte [] bytes = decryptClass(classFilePath);
        Class clazz = defineClass(className,bytes,0,bytes.length);
        return clazz;
    }

    public  Object execute(String className, String methodName, Object... parameters) throws Exception{
        Class<?> cls = getClass(className);
        Object object  = cls.newInstance();
        Object result=null;

        try {

            Method method =getMethod(cls, methodName, parameters);
            result= method.invoke(object, parameters);

        } catch (Exception e) {
            e.printStackTrace();
            Throwable cause = e.getCause();
            if (cause instanceof LicenseException) {
                cause.printStackTrace();
                return "Error:" + cause.getMessage();
            }

        }

        return result;
    }

    public  Method getMethod(Class<?> cls, String methodName, Object[] parameters) throws NoSuchMethodException {
        Method[] methods = cls.getDeclaredMethods();
        Method[] var4 = methods;
        int var5 = methods.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Method method = var4[var6];
            Class<?>[] parameterTypes = method.getParameterTypes();
            int len = parameters == null ? 0 : parameters.length;
            if (methodName.equals(method.getName()) && parameterTypes.length == len) {
                return method;
            }
        }

        throw new NoSuchMethodException();
    }

    public static void main(String[] args) {
        ClassLoadUtil classLoadUtil = new ClassLoadUtil();
        String className = "com.redxun.sys.core.util.JsaasUtil";
        try {
            Object object = classLoadUtil.execute(className,"test",new Object[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


