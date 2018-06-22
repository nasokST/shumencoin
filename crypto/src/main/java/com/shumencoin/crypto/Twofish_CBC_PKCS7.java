package com.shumencoin.crypto;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.TwofishEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

/**
 * 
 * @author 
 *
 */
class Twofish_CBC_PKCS7 {

	private final BlockCipher twofishCipher;

	private PaddedBufferedBlockCipher pbbc;
	private KeyParameter key;

	Twofish_CBC_PKCS7() {
		twofishCipher = new CBCBlockCipher(new TwofishEngine());
		pbbc = new PaddedBufferedBlockCipher(twofishCipher, new PKCS7Padding());
	}

	public void setKey(byte[] key) {
		this.key = new KeyParameter(key);
	}

	public byte[] encrypt(byte[] input, byte[] iv) throws DataLengthException, InvalidCipherTextException {
		return processing(input, true, iv);
	}

	public byte[] decrypt(byte[] input, byte[] iv) throws DataLengthException, InvalidCipherTextException {
		return processing(input, false, iv);
	}

	private byte[] processing(byte[] input, boolean encrypt, byte[] iv)
			throws DataLengthException, InvalidCipherTextException {

		ParametersWithIV piv = new ParametersWithIV(key, iv);

		pbbc.init(encrypt, piv);

		byte[] output = new byte[pbbc.getOutputSize(input.length)];
		int bytesWrittenOut = pbbc.processBytes(input, 0, input.length, output, 0);

		bytesWrittenOut += pbbc.doFinal(output, bytesWrittenOut);

		if (!encrypt) {
			// remove padding
			byte[] out = new byte[bytesWrittenOut];
			System.arraycopy(output, 0, out, 0, bytesWrittenOut);
			
			output = out;
		}

		return output;
	}
}
