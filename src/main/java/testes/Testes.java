package testes;

import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.MinioException;
import io.minio.messages.Item;
import io.minio.ListObjectsArgs;
import java.util.Iterator;

public class Testes {

    public String listarArquivosMinio(String endpoint, String accessKey, String secretKey, String bucketName) {
        String arquivos = "";
        try {
            MinioClient minioClient = MinioClient.builder()
                                                 .endpoint(endpoint)
                                                 .credentials(accessKey, secretKey)
                                                 .build();
            Iterable<Result<Item>> myObjects = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(bucketName).build()
            );
            for (Result<Item> result : myObjects) {
                Item item = result.get();
                System.out.println(item.objectName());
                arquivos += item.objectName() + "\n";
            }
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
        } catch (Exception e) {
            System.out.println("Error occurred: " + e);
        } finally {
            return arquivos;
        }
    }
}
