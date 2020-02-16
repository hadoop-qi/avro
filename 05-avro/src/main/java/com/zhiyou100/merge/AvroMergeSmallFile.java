package com.zhiyou100.merge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;

import com.zhiyou100.avro.User;

public class AvroMergeSmallFile {

	public static void main(String[] args) {

		// A container file, to store persistent data.
		// 把多个小文件合并到一个 Avro 文件中
		
		String filePath1 = "D:/avro/aaa.txt";
		String filePath2 = "D:/avro/bbb.txt";
		
		try(FileReader fileReader1 = new FileReader(filePath1);
				BufferedReader bufferedReader1 = new BufferedReader(fileReader1);
				FileReader fileReader2 = new FileReader(filePath2);
				BufferedReader bufferedReader2 = new BufferedReader(fileReader2);) {
			
			StringBuilder content1 = new StringBuilder();
			String readText1 = null;
			
			while ((readText1 = bufferedReader1.readLine()) != null) {
				
				content1.append(readText1).append("\n");
			}
			
			StringBuilder content2 = new StringBuilder();
			String readText2 = null;
			
			while ((readText2 = bufferedReader2.readLine()) != null) {
				
				content2.append(readText2).append("\n");
			} 
			
			// 把名字和内容封装为对象
			SmallFile smallFile1 = new SmallFile(filePath1, content1);
			SmallFile smallFile2 = new SmallFile(filePath2, content2);
			
			// 序列化到硬盘
			File file = new File("D:/avro/smallFile.avro");
			
			DatumWriter<SmallFile> userDatumWriter = new SpecificDatumWriter<SmallFile>(SmallFile.class);
			
			// 使用 try-with-resource 关闭流
			try(DataFileWriter<SmallFile> dataFileWriter = new DataFileWriter<SmallFile>(userDatumWriter);) {
				
				// 保存 Schema
				dataFileWriter.create(smallFile1.getSchema(), file);
				
				// 保存 Data
				dataFileWriter.append(smallFile1);
				dataFileWriter.append(smallFile2);
			}
			
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		
	}
}
