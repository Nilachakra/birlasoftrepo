
/**********************************************************************
 * 
 *  
 * Class Names: AsyncProductsByProductId.java
 * This class responsible for calling  Products by ProductId 
 * 
 *  
 * Date: 15 July 2016
 *  
 **********************************************************************/

package com.productengine.async;

import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.productengine.dao.ProductDAO;
import com.productengine.model.Product;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
@Component
public class AsyncProductsByProductId {
	
	@Autowired
	private ProductDAO productDAO;
	
	/** Field Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(AsyncProductsByProductId.class);
		 
	@HystrixCommand(fallbackMethod = "fallback", 
	            commandProperties = { 
	                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000"), 
	                    @HystrixProperty(name = "circuitBreaker.enabled", value = "true"), 
	                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "100"), 
	                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "200"), 
	                   },	            
	            threadPoolProperties = {
                        @HystrixProperty(name = "coreSize", value = "30"),
                        @HystrixProperty(name = "maxQueueSize", value = "101"),
                        @HystrixProperty(name = "keepAliveTimeMinutes", value = "2"),
                        @HystrixProperty(name = "queueSizeRejectionThreshold", value = "15"),
                        @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "12"),
                        @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "1440")
                        
        } )

	public Future<Product> getProductIdAsync(String productId) { 
		System.out.println("product id inside async "+productId);
        return new AsyncResult<Product>() { 
           @Override 
           public Product invoke() {  
        		Product product = null;
        		if (productId != null && !("").equals(productId))
        			product = productDAO.findByProductId(productId);
        		return product;
           } 
       }; 
       
   } 


public Product fallback(String productId) { 
	LOGGER.info("Fall back for ProductsByProductId" );  
    return new Product(); // it should be network call 
}


            

}
