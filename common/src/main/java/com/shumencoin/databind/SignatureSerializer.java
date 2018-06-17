package com.shumencoin.databind;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.shumencoin.convertion.Converter;

public class SignatureSerializer extends JsonSerializer<byte[][]> {

	@Override
	public void serialize(byte[][] arg0, JsonGenerator generator, SerializerProvider arg2)
			throws IOException, JsonProcessingException {
		final String s1 = Converter.byteArrayToHexString(arg0[0]);
		final String s2 = Converter.byteArrayToHexString(arg0[1]);
		
		generator.writeStartArray();
		generator.writeObject(s1);
		generator.writeObject(s2);
		generator.writeEndArray();
	}
}