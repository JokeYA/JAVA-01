package code.work;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HelloClassLoader extends ClassLoader {

    public static void main(String[] args){
        try {
            Class<?> clazz = new HelloClassLoader().findClass("code.work.Hello");
            Method declaredMethod = clazz.getDeclaredMethod("hello");
            declaredMethod.invoke(clazz.newInstance());
        } catch (ClassNotFoundException | NoSuchMethodException |
                IllegalAccessException | InstantiationException |
                InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bytes = new byte[0];
        try {
            bytes = processHelloClazz(name.replace(".", File.separator) + ".xlass");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bytes == null){
            return null;
        }
        return defineClass(name, bytes, 0, bytes.length);
    }

    private byte[] processHelloClazz(String path) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
        if (inputStream == null){
            System.out.println("类资源获取失败");
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int nextValue;
        while ((nextValue = inputStream.read()) != -1){
            byteArrayOutputStream.write(nextValue);
        }

        byte[] bytes = byteArrayOutputStream.toByteArray();

        for (int i = 0; i < bytes.length; i++){
            bytes[i] = (byte) (255 - bytes[i]);
        }
        return bytes;
    }

}
