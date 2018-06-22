package com.shumencoin.crypto.databind;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.shumencoin.convertion.Converter;

public class ByteArraySerializer extends JsonSerializer<byte[]> {

	@Override
	public void serialize(byte[] arg0, JsonGenerator generator, SerializerProvider arg2)
			throws IOException, JsonProcessingException {
		final String dateString = Converter.byteArrayToHexString(arg0);
		generator.writeString(dateString);
	}
}