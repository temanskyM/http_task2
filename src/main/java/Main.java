import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.NasaResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static final String REMOTE_SERVICE_URL = "https://api.nasa.gov/planetary/apod?api_key=Emh9abgFFXOWcKNd9vkQvCkgL6vqoZHciDr09NRa";
    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        NasaResponse nasaResponse;
        String urlToPicture;

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setUserAgent("My agent")
                .setDefaultRequestConfig(RequestConfig.DEFAULT)
                .build();
        HttpGet request = new HttpGet(REMOTE_SERVICE_URL);
        try {
            CloseableHttpResponse response = httpClient.execute(request);
            nasaResponse = getNasaResponse(response);
            urlToPicture = nasaResponse.getUrl();
            BufferedImage picture = getPicture(urlToPicture);
            savePicture(picture, "image.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static NasaResponse getNasaResponse(CloseableHttpResponse response) throws IOException {
        return mapper.readValue(response.getEntity().getContent(), new TypeReference<>() {
        });
    }

    private static BufferedImage getPicture(String URL) throws IOException {
        HttpEntity httpEntity = new BasicHttpEntity();
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setUserAgent("My agent")
                .setDefaultRequestConfig(RequestConfig.DEFAULT)
                .build();
        HttpGet request = new HttpGet(URL);
        CloseableHttpResponse response = httpClient.execute(request);
        httpEntity = response.getEntity();
        return ImageIO.read(httpEntity.getContent());
    }

    private static void savePicture(BufferedImage bufferedImage, String filename) throws IOException {
        File outputfile = new File(filename);
        ImageIO.write(bufferedImage, "jpg", outputfile);
    }
}
