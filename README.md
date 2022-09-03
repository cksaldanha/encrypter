# Encrypter

## Summary 
A command-line tool made in Java used to encrypt and decrypt files. Useful for safe transferring of files.

## Details
Has options to use 128-bit AES symmetric keys or RSA asymmetric public/private keys.
_Built using Maven and NetBeans IDE 8.1_

Repository includes a Dockerfile to create image directly or you can download the Docker image from my repository:
[https://hub.docker.com/r/cksaldanha/encrypter](https://hub.docker.com/r/cksaldanha/encrypter)

## Typical usage
This will create an aes.key file, which holds the key and initialization vector for encryption using 128-bit AES.
```sh
java -jar Encrypter-0.0.1-SNAPSHOT.jar aes
```

This will create a rsa public/private key pair, with keys stored in the specified filepaths: 'public.key' and 'private.key'
```sh
java -jar Encrypter-0.0.1-SNAPSHOT.jar rsa --public=public.key --private=private.key
```

This will encrypt the provided files using AES encryption and specified key.
```sh
java -jar Encrypter-0.0.1-SNAPSHOT.jar encrypt --mode=aes --keypath=aes.key file1 [file2] [file3]
```

This will encrypt the provided files using RSA encryption, with the specified public key. Decryption can only be done
using the corresponding private key.
```sh
java -jar Encrypter-0.0.1-SNAPSHOT.jar encrypt --mode=rsa --keypath=public.key --type=public [file1] [file2] [file3]
```

## Docker
### AES mode
```sh
docker run -rm -ti -v ${pwd}:/test cksaldanha/encrypter:1.0 aes /test/my_aes.key
docker run -rm -ti -v ${pwd}:/test cksaldanha/encrypter:1.0 encrypt --mode=aes --keypath=/test/my_aes.key file1.txt
docker run -rm -ti -v ${pwd}:/test cksaldanha/encrypter:1.0 decrypt --mode=aes --keypath=/test/my_aes.key file1.secure
```

### RSA mode
```sh
docker run -rm -ti -v ${pwd}:/test cksaldanha/encrypter:1.0 rsa --public=/test/my_public.key --private=/test/my_private.key
docker run -rm -ti -v ${pwd}:/test cksaldanha/encrypter:1.0 encrypt --mode=rsa --type=public --keypath=/test/my_public.key file1.txt
docker run -rm -ti -v ${pwd}:/test cksaldanha/encrypter:1.0 decrypt --mode=rsa --type=private --keypath=/test/my_private.key file1.secure
```
