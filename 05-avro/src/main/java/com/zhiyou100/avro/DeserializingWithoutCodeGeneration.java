package com.zhiyou100.avro;

import java.io.File;
import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;

public class DeserializingWithoutCodeGeneration {

	public static void main(String[] args) throws IOException {
		
		File schemaFile = new File("src/main/avro/department.avsc");
		Schema schema = new Schema.Parser().parse(schemaFile);

		DatumReader<GenericRecord> userDatumReader = new GenericDatumReader<GenericRecord>(schema);
		
		File file = new File("D:/avro/department.avro");

		try(DataFileReader<GenericRecord> dataFileReader = new DataFileReader<GenericRecord>(file, userDatumReader);) {
			
			GenericRecord dep = null;
			while (dataFileReader.hasNext()) {
				
				dep = dataFileReader.next(dep);
				System.out.println(dep);
			}
		}
	}
}
