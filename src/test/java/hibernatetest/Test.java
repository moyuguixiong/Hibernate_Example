package hibernatetest;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class Test {

  public static void main(String[] args) throws IOException, ClassNotFoundException {
    List<String> classNames = getPackageClassName("classpath:com/fang/**/*.class");

    if (classNames != null) {
      System.out.println(classNames.size());
      for (String className : classNames) {
        @SuppressWarnings("rawtypes")
        Class clazz = Class.forName(className);
        if (clazz.isMemberClass()) {
          System.out.println(className);
        }
      }
    }
  }

  /**
   * 获取扫描包中所有类的全路径
   *
   * @param packagePath
   * @return
   * @throws IOException
   */
  public static List<String> getPackageClassName(String packagePath) throws IOException {
    ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    Resource[] resources = resourcePatternResolver.getResources(packagePath);
    List<String> classNames = new ArrayList<String>();
    for (Resource resource : resources) {
      String url = resource.getURI().toString();
      int MAGIC = 0xCAFEBABE;
      DataInputStream in = new DataInputStream(new FileInputStream(url.replaceAll("file:/", "")));
      if (in.readInt() == MAGIC) {
        in.readUnsignedShort();// 次版本号
        in.readUnsignedShort();// 主版本号
        in.readUnsignedShort();// 长度
        in.readByte();// CLASS=7
        in.readUnsignedShort();// 忽略这个地方
        in.readByte();// UTF8=1
        String name = in.readUTF();// 类的名字!!!
        in.close();
        classNames.add(name.replaceAll("/", "."));
      }
    }
    return classNames;
  }

  static void testForClassFile() throws IOException {
    ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    Resource[] resources = resourcePatternResolver.getResources("classpath:com/fang/**/*.class");
    for (Resource resource : resources) {
      String url = resource.getURI().toString();
      System.out.println(url);
      int MAGIC = 0xCAFEBABE;
      DataInputStream in = new DataInputStream(new FileInputStream(url.replaceAll("file:/", "")));
      if (in.readInt() != MAGIC) {

      } else {
        int secondary = in.readUnsignedShort();// 次版本号
        System.out.println(secondary);
        int primary = in.readUnsignedShort();// 主版本号
        System.out.println(primary);
        int length = in.readUnsignedShort();// 长度
        System.out.println(length);
        byte byte1 = in.readByte();// CLASS=7
        System.out.println(byte1);
        int some = in.readUnsignedShort();// 忽略这个地方
        System.out.println(some);
        byte byte2 = in.readByte();// UTF8=1
        System.out.println(byte2);
        String name = in.readUTF();// 类的名字!!!
        in.close();
        System.out.println(name.replaceAll("/", "."));
      }
    }
  }
}
