package com.zhiyou100.avro;

import java.io.File;
import java.io.IOException;

import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;

public class SerializingWithCodeGeneration {

	public static void main(String[] args) throws IOException {

		// 使用 avor 和生成的 User 类完成序列化操作
		
		// 1. 创建 User 对象，有三种方式：
		
		// 1.1 创建对象，通过 set 方法为属性赋值
		User user1 = new User();
		user1.setName("zhangsan");
		user1.setFavoriteNumber(3);
		user1.setFavoriteColor("white");
		
		// 1.2 通过构造方法创建对象，并为属性赋值
		User user2 = new User("lisi", 6, "red");
		
		// 1.3 使用 builder 建造者模式，创建对象并赋值
		// 一般用在属性特别多，组合复杂的情况下。
		// eg：类有 10+ 个属性，组合复杂的情况，使用 builer 可以任意组合属性
		User user3 = User.newBuilder()
				.setName("wangwu")
				.setFavoriteNumber(8)
				.setFavoriteColor("blue")
				.build();
		
		// 2. 转换为二进制保存在硬盘上

		File file = new File("D:/avro/user.avro");
		
		DatumWriter<User> userDatumWriter = new SpecificDatumWriter<User>(User.class);
		
		// 使用 try-with-resource 关闭流
		try(DataFileWriter<User> dataFileWriter = new DataFileWriter<User>(userDatumWriter);) {
			
			// 保存 Schema
			dataFileWriter.create(user1.getSchema(), file);
			
			// 保存 Data
			dataFileWriter.append(user1);
			dataFileWriter.append(user2);
			dataFileWriter.append(user3);
		}
	}
}





