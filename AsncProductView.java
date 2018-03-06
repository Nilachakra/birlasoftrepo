
 *  
 * Class Names: AsncProductView.java
 * This class responsible for calling  productview Api  
 * 
 * 
 *  
 **********************************************************************/
package com.productengine.async;

import static com.boots.productengine.common.Constants.WCS_PRODUCTVIEW_QUERY_PARAMETERS;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.boots.productengine.async.model.CatalogEntryView;
import com.boots.productengine.async.model.WCSProductView;
import com.boots.productengine.common.ConsumerFactory;
import com.boots.productengine.model.Product;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;

@Component
public class AsncProductView {

	/** Field Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(AsncProductView.class);

	
	/** Field product view url. */
	@Value("${spring.productview.url}")
	private String productViewURL;


	/** Field api key. */
	@Value("${swcs.apikey}")
	private String apiKey;
	

	/** Field skipped products. */
	private Set<Product> skippedProducts = new HashSet<Product>();
 
	 
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

	public Future<CatalogEntryView> getViewByProductIdAsync(Product product) { 
         return new AsyncResult<CatalogEntryView>() { 
            @Override 
            public CatalogEntryView invoke() {   
            	          	
          	CatalogEntryView catalogEntryView = getProductViewByProductId(product);
            	    
            	return catalogEntryView;// new User(); // it should be network call 
            } 
        }; 
        
    } 


public CatalogEntryView fallback(Product product) { 
	LOGGER.info("Fall back for ProductView" ); 
    return new CatalogEntryView(); 
}


/**
 * Gets the product view by product id.
 *
 * @param product Product
 * @return CatalogEntryView
 */



private CatalogEntryView getProductViewByProductId(Product product)
{
	System.out.println("------------->"+product.getProductId());
	CatalogEntryView catalogEntryView = null;
	String wcsConsumerURL = productViewURL + product.getProductId() + WCS_PRODUCTVIEW_QUERY_PARAMETERS;
	ResponseEntity<WCSProductView> responseEntity;
	try
	{
		responseEntity = ConsumerFactory.processWcsGetRequest(wcsConsumerURL, WCSProductView.class, apiKey);
		
		if (responseEntity != null && responseEntity.getBody() != null
				&& CollectionUtils.isNotEmpty(responseEntity.getBody().getCatalogEntryView()))
		{
			WCSProductView wcsProductView = responseEntity.getBody();
			List<CatalogEntryView> catalogEntryViews = wcsProductView.getCatalogEntryView();
			catalogEntryView = catalogEntryViews.get(0);
		} else
		{
		/*	skippedProducts.add(product);
			LOGGER.warn("~" + product.getBic() + ", " + product.getName()
					+ ", This product is skipped because it's API response is empty.");*/
		}

	}
	catch (Exception e)
	{
		/*LOGGER.info(e.getMessage());
		skippedProducts.add(product);
		LOGGER.warn("~" + product.getBic() + ", " + product.getName() + ", Product View API error.");
*/	}
	return catalogEntryView;
}


}
