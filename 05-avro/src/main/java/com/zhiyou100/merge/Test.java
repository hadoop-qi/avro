package com.zhiyou100.merge;

import java.io.File;
import java.io.IOException;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.io.DatumReader;
import org.apache.avro.specific.SpecificDatumReader;

import com.zhiyou100.avro.User;

public class Test {

	public static void main(String[] args) throws IOException {

		// 使用反序列化获得生成的 User 类的对象
		File file = new File("D:/avro/smallFile.avro");

		DatumReader<SmallFile> userDatumReader = new SpecificDatumReader<SmallFile>(SmallFile.class);
		
		// 使用 try-with-resource 关闭流
		try(DataFileReader<SmallFile> dataFileReader = new DataFileReader<SmallFile>(file, userDatumReader);) {
			
			SmallFile smallFile = null;
			while (dataFileReader.hasNext()) {
				
				smallFile = dataFileReader.next(smallFile);
				
				System.out.println(smallFile);
			}
		}
	}
}
