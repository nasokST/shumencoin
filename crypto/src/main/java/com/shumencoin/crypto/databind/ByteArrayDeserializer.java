package com.shumencoin.crypto.databind;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;
import com.shumencoin.convertion.Converter;

public class ByteArrayDeserializer extends JsonDeserializer<byte[]> {

	@Override
	public byte[] deserialize(JsonParser jp, DeserializationContext arg1) throws IOException, JsonProcessingException {
		ObjectCodec oc = jp.getCodec();
		TextNode node = (TextNode) oc.readTree(jp);
		String dataString = node.textValue();

		return Converter.HexStringToByteArray(dataString);
	}
}
