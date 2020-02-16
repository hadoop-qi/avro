package com.zhiyou100.avro;

import java.io.File;
import java.io.IOException;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.io.DatumReader;
import org.apache.avro.specific.SpecificDatumReader;

public class DeserializingWithCodeGeneration {

	public static void main(String[] args) throws IOException {

		// 使用反序列化获得生成的 User 类的对象
		File file = new File("D:/avro/user.avro");

		DatumReader<User> userDatumReader = new SpecificDatumReader<User>(User.class);
		
		// 使用 try-with-resource 关闭流
		try(DataFileReader<User> dataFileReader = new DataFileReader<User>(file, userDatumReader);) {
			
			User user = null;
			while (dataFileReader.hasNext()) {
				
				user = dataFileReader.next(user);
				System.out.println(user);
			}
		}
	}
}
