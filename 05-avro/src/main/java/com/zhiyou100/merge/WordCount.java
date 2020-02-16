package com.zhiyou100.merge;
import java.io.IOException;

import org.apache.avro.mapred.AvroKey;
import org.apache.avro.mapreduce.AvroJob;
import org.apache.avro.mapreduce.AvroKeyInputFormat;
import org.apache.avro.mapreduce.AvroKeyOutputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCount {

	public static void main(String[] args) {
		
		try {
			
			Configuration conf = new Configuration();
			conf.set("fs.defaultFS", "hdfs://master:9000");

			Job job = Job.getInstance(conf, "wordcount");
			job.setJarByClass(WordCount.class);
			
			job.setMapperClass(WordCountMapper.class);
			job.setReducerClass(WordCountReducer.class);
			
			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(IntWritable.class);
			
			job.setOutputKeyClass(AvroKey.class);
			job.setOutputValueClass(NullWritable.class);
			
			
			// 设置输入类型和模式
			job.setInputFormatClass(AvroKeyInputFormat.class);
			AvroJob.setInputKeySchema(job, SmallFile.getClassSchema());
			
			Path inputPath = new Path("/AVRO-input/small_file.avro");
			FileInputFormat.addInputPath(job, inputPath);
			
			// 设置输出类型和模式
			job.setOutputFormatClass(AvroKeyOutputFormat.class);
			AvroJob.setOutputKeySchema(job, WordCountResult.getClassSchema());
			
			Path outputDir = new Path("/small-file-word-count");
			FileSystem.get(conf).delete(outputDir, true);
			FileOutputFormat.setOutputPath(job, outputDir);

			boolean flag = job.waitForCompletion(true);
			
			System.exit(flag ? 0 : 1);
		} catch (IOException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
	}

	
	public static class WordCountMapper extends Mapper<AvroKey<SmallFile>, NullWritable, Text, IntWritable> {
		
		private Text outputKey = new Text();
		
		// 对于至不会发生变化的 outputKey 或 outputValue 我们使用 static
		// 修饰可以提高代码执行的效率
		private static IntWritable outputValue = new IntWritable(1);
		
		
		@Override
		protected void map(AvroKey<SmallFile> key, NullWritable value, Mapper<AvroKey<SmallFile>, NullWritable, Text, IntWritable>.Context context)
				throws IOException, InterruptedException {
			
			SmallFile smallFile = key.datum();
			
			String content = smallFile.getContent().toString();
			
			String[] lines = content.split("\\n");
			
			for (String row : lines) {
				
				String[] words = row.split("\\s+");
				
				for (String word : words) {
					
					outputKey.set(word);
					
					context.write(outputKey, outputValue);
				}
			}
		}
	}
	
	public static class WordCountReducer extends Reducer<Text, IntWritable, AvroKey<WordCountResult>, NullWritable> {
		
		private AvroKey<WordCountResult> outputKey = new AvroKey<>();
		
		@Override
		protected void reduce(Text key, Iterable<IntWritable> value,
				Reducer<Text, IntWritable, AvroKey<WordCountResult>, NullWritable>.Context context) throws IOException, InterruptedException {
			
			int sum = 0;
			
			for (IntWritable one : value) {
				
				sum += one.get();
			}
		
			// 计算结果封装为 WordCountResult 对象
			WordCountResult wordCountResult = new WordCountResult(key.toString(), sum);
			
			// 把 WordCountResult 转换为 AvroKey    
			outputKey.datum(wordCountResult);
			
			context.write(outputKey, NullWritable.get());
		}
	}
}