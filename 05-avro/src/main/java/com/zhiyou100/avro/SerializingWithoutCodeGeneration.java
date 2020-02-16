package com.zhiyou100.avro;

import java.io.File;
import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter;

public class SerializingWithoutCodeGeneration {

	public static void main(String[] args) throws IOException {

		// 通过 avro 不生成 java 类进行数据的序列化操作
		
		// 1. 设置需要保存的数据对象
		// 1.1 读取 Schema 文件
		// 绝对路径：E:/Workspace/ZY_BD_14_02/05-avro/src/main/avro/department.avsc
		// 相对路径：src/main/avro/department.avsc
		File schemaFile = new File("src/main/avro/department.avsc");
		Schema schema = new Schema.Parser().parse(schemaFile);
		
		// 1.2 生成对应的数据
		GenericRecord dep1 = new GenericData.Record(schema);
		dep1.put("id", 1001);
		dep1.put("name", "财务部");
		dep1.put("location", "西南三");
		
		GenericRecord dep2 = new GenericData.Record(schema);
		dep2.put("id", 1002);
		dep2.put("name", "招生部");
		dep2.put("location", "西北三");
		
		// 1.3 序列化写入硬盘
		File file = new File("D:/avro/department.avro");
		
		DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<GenericRecord>(schema);

		try(DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<GenericRecord>(datumWriter);) {
			
			dataFileWriter.create(schema, file);
			
			dataFileWriter.append(dep1);
			dataFileWriter.append(dep2);
		}
	}
}
