package com.shumencoin.databind;

import java.io.IOException;
import java.util.Iterator;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.shumencoin.convertion.Converter;

public class SignatureDeserializer extends JsonDeserializer<byte[][]> {

	@Override
	public byte[][] deserialize(JsonParser jp, DeserializationContext arg1) throws IOException, JsonProcessingException {
		ObjectCodec oc = jp.getCodec();

        JsonNode node = oc.readTree(jp);
        Iterator<JsonNode> elements = node.elements();

        byte[][] signature = new byte[2][];
        
        signature[0] = Converter.HexStringToByteArray(elements.next().textValue());
        signature[1] = Converter.HexStringToByteArray(elements.next().textValue());

		return signature;
	}
}
