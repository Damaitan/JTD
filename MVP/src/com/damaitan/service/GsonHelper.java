package com.damaitan.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonHelper {
	Gson gson = null;
	public GsonHelper(){
		gson = new GsonBuilder()
		// .excludeFieldsWithoutExposeAnnotation() //������ʵ����û����@Exposeע�������
				.enableComplexMapKeySerialization() // ֧��Map��keyΪ���Ӷ������ʽ
				.serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss:SSS")// ʱ��ת��Ϊ�ض���ʽ
				.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)// ����ֶ�����ĸ��д,ע:����ʵ����ʹ����@SerializedNameע��Ĳ�����Ч.
				.setPrettyPrinting() // ��json�����ʽ��.
				.setVersion(1.0) // �е��ֶβ���һ��ʼ���е�,�����Ű汾��������ӽ���,��ô�ڽ������л��ͷ����л���ʱ��ͻ���ݰ汾����ѡ���Ƿ�Ҫ���л�.
									// @Since(�汾��)��������ʵ���������.�����ֶο���,���Ű汾��������ɾ��,��ô
									// @Until(�汾��)Ҳ��ʵ���������,GsonBuilder.setVersion(double)������Ҫ����.
				.create();
	}
	
	public String jsonString(Object obj){
		return gson.toJson(obj);
	}
	
	public <T> T fromJson(String json,Class<T> classOfT){
		return gson.fromJson(json, classOfT);
	}
	
	

}
