package springboottemplate.seeddata;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.Base64;

@Service
public class ImageService {
    private final Random random = new Random();
    private final List<String> images;

    public ImageService() {
        try {
            URL url = getClass().getClassLoader().getResource("static/images/picsum.photos-images.txt");
            if (url == null) {
                throw new RuntimeException("Resource not found");
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            images = reader.lines().collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load images", e);
        }
    }

    public byte[] getRandomImage() {
        String base64Image = images.get(random.nextInt(images.size()));
        return Base64.getDecoder().decode(base64Image);
    }
}