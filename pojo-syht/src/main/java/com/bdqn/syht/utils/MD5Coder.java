package com.bdqn.syht.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * ������Ϣ���ܵĹ�����
 * @author Administrator
 *
 */
public class MD5Coder {
	
	private static final Logger log = LoggerFactory.getLogger(MD5Coder.class);
	
	private static MD5Coder md5 = null;
	
	private MD5Coder() {
    }
	
	public static synchronized MD5Coder getInstance() {
        if (md5 == null) {
            md5 = new MD5Coder();
        }
        return md5;
    }
	
	/** ���ַ����
     * 
     * @param string
     * @return */
    public static String encrypt(String string) {
        if (string != null && !"".equals(string)) {
            try {
				return getInstance().getMD5ByBytes(string.getBytes("UTF-8")).toUpperCase();
			} catch (UnsupportedEncodingException e) {
				log.error("MD5����תUTF-8����", e);
			}
        } 
        return string;
    }

    private String getMD5ByBytes(byte[] source) {
        String s = null;
        // �������ֽ�ת���� 16 ���Ʊ�ʾ���ַ�
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source);
            byte tmp[] = md.digest(); // MD5 �ļ�������һ��128λ�ĳ����� ���ֽڱ�ʾ����16���ֽ�
            char str[] = new char[16 * 2]; // ÿ���ֽ���16���Ʊ�ʾ�Ļ���ʹ�������ַ����Ա�ʾ��16������Ҫ32���ַ�
            int k = 0; // ��ʾת������ж�Ӧ���ַ�λ��
            for (int i = 0; i < 16; i++) { // �ӵ�һ���ֽڿ�ʼ���� MD5��ÿһ���ֽڣ�ת����16�����ַ��ת��
                byte byte0 = tmp[i]; // ȡ��i���ֽ�
                str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // ȡ�ֽ��и�4λ������ת��,>>>Ϊ�߼����ƣ������λһ������
                str[k++] = hexDigits[byte0 & 0xf]; // ȡ�ֽ��е�4λ������ת��
            }
            s = new String(str); // ����Ľ��ת��Ϊ�ַ�
        } catch (Exception e) {
            log.error("MD5 ���� [error]", e);
        }
        return s;
    }
    public static void main(String[] args) {
		System.out.println(MD5Coder.encrypt("123"));
	}
}
