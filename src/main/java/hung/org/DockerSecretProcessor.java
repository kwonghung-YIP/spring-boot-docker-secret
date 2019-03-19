package hung.org;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

@Component
public class DockerSecretProcessor implements EnvironmentPostProcessor {

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		
		
		String bindPathPpty = environment.getProperty("docker-secret.bind-path");
		if (bindPathPpty!=null) {
			Path bindPath = Paths.get(bindPathPpty);
			if (Files.isDirectory(bindPath)) {				
				bindPath.forEach(file -> {
					
				});
			}
		}			
	}

}
