

 *  
 **********************************************************************/


import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
//import org.springframework.cloud.netflix.hystrix.EnableHystrix;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
import com.netflix.hystrix.contrib.javanica.aop.aspectj.HystrixCommandAspect;

/**
 * *************************************************************************************************
 * The Class Application.
 *
 * @version : 1.0
 * @class : Application
 * @implements : none
 * @extends : none
 * 
 * ************************************************************************************************
 */
@Configuration
@EnableAspectJAutoProxy
@EnableAutoConfiguration
@EnableCaching
@EnableSwagger
//@EnableAsync

@EnableHystrix
@EnableCircuitBreaker
@ComponentScan("com.boots.*")
@PropertySource("classpath:application.properties")
@PropertySource(value = "classpath:application-${profile}.properties", ignoreResourceNotFound = true)
@PropertySource(value = "${dbproperties}", ignoreResourceNotFound = true)
@PropertySource(value = "${envproperties}", ignoreResourceNotFound = true)
public class Application
{

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws Exception the exception
	 */
	public static void main(String[] args) throws Exception
	{

		SpringApplication.run(Application.class, args);
	}

	
	
	/** Field spring swagger config. */
	private SpringSwaggerConfig springSwaggerConfig;

	/**
	 * Sets the spring swagger config.
	 *
	 * @param springSwaggerConfig the new spring swagger config
	 */
	@Autowired
	public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig)
	{
		this.springSwaggerConfig = springSwaggerConfig;
	}

	/**
	 * Gets the swagger spring mvc plugin.
	 *
	 * @return SwaggerSpringMvcPlugin
	 */
	@Bean
	public SwaggerSpringMvcPlugin customImplementation()
	{
		return new SwaggerSpringMvcPlugin(this.springSwaggerConfig).apiInfo(apiInfo()).includePatterns(".*/.*");
	}

	/**
	 * Gets the api info.
	 *
	 * @return ApiInfo
	 */
	private ApiInfo apiInfo()
	{

		ApiInfo apiInfo = new ApiInfo("Product Services", "Product service provides a set of utility API's which helps to access the product details.", "Terms",
				"Email", "License Type", "License URL");
		return apiInfo;
	}

	/**
	 * Gets the cache manager.
	 *
	 * @return CacheManager
	 */
	@Bean
	@Primary
	public CacheManager productCacheManager()
	{
		SimpleCacheManager cacheManager = new SimpleCacheManager();

		cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache("productByBic"), new ConcurrentMapCache("productByProductId"), new ConcurrentMapCache("findByBrand")));

		return cacheManager;
	}

	
	/**
	 * Gets the local validator factory bean.
	 *
	 * @return LocalValidatorFactoryBean
	 */
	@Bean
	public LocalValidatorFactoryBean validator()
	{
		return new LocalValidatorFactoryBean();
	}
	
	  @Bean
	  public HystrixCommandAspect hystrixAspect() {
	    return new HystrixCommandAspect();
	  }

	//}
	  
	 
	
	  
}
