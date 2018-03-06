/**********************************************************************
 
 *  
 ***********************************************************************/
package com.productengine.business;



import static com.productengine.common.Constants.DOT_STRING;
import static com.productengine.common.Constants.WCS_PRODUCTVIEW_SKU_IDENTIFIER_COLOR;
import static com.productengine.common.Constants.WCS_PRODUCTVIEW_SKU_IDENTIFIER_COLOUR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.productengine.async.AsncProductCMSDescription;
import com.productengine.async.AsncProductView;
import com.productengine.async.AsyncPriceView;
import com.productengine.async.AsyncProductsByProductId;
import com.productengine.async.AsyncProductsbyBicId;
import com.productengine.async.AsyncWcsInventorystatus;
import com.productengine.async.model.Attributes;
import com.productengine.async.model.CatalogEntryView;
import com.productengine.async.model.ProductColour;
import com.productengine.async.model.ProductPromotion;
import com.productengine.async.model.SKUs;
import com.productengine.async.model.Values;
import com.productengine.async.model.WCSPriceView;
//import com.productengine.dao.ProductColourDAO;
import com.productengine.dao.ProductDAO;
//import com.productengine.dao.ProductPromotionDAO;
//import comproductengine.dao.ProductSizesDAO;
import com.productengine.exception.Constants;
import com.productengine.exception.ProductEngineException;
import com.productengine.model.Description;
import com.productengine.model.Product;
//import com.productengine.model.ProductColour;
//import com.productengine.model.ProductPromotion;
import com.productengine.model.ProductResponse;
import com.productengine.model.QueryParams;

/**
 * *************************************************************************************************
 * 
 * ProductManager class provide the functionalities to fetch product by bic and
 * product id.
 *
 * 
 * 
 * 
 * ************************************************************************************************
 */
@Component
public class ProductManager  
{
	
	/** Field product dao. */
	@Autowired
	private ProductDAO productDAO;
	
	@Autowired
	AsyncProductsByProductId asyncProducts;
	
	@Autowired
	AsncProductView asncProductView;
	
	@Autowired
	AsyncPriceView asyncPriceView;
	
	@Autowired
	AsncProductCMSDescription asyncProductCMSDescription;

	@Autowired
	AsyncWcsInventorystatus asyncWcsInventorystatus;
	
	@Autowired
	AsyncProductsbyBicId  asyncProductsbyBicId;
	
	/** Field Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductManager.class);


	/** Field bic ids. */
	private Set<String> bicIds = new HashSet<String>();
	
	/** Field product promotions. */
	private List<ProductPromotion> productPromotions = new ArrayList<ProductPromotion>();
	
	/**
	 * This method fetches the product details by list of bics.
	 *
	 * @param bics String
	 * @param queryParams QueryParams
	 * @return : Product
	 * @methodName : getProductByBics
	 */
	
	public ProductResponse getProductByBics(String bics, QueryParams queryParams)
	{
		List<Product> prodList = new ArrayList<Product>();

		List<String> bicList = Arrays.asList(bics.split("\\s*,\\s*"));

		ProductResponse prodResp = new ProductResponse();
		List<String> skippedList = null;

		if (bicList == null)
		{
			throw new ProductEngineException(Constants.ERROR_PRODUCT_BIC_EMPTY + ": BIC ID VALUE IS EMPTY",
					HttpStatus.BAD_REQUEST);
		}
		/*else if (bicList.size() > 10)
		{
			throw new ProductEngineException(Constants.ERROR_PRODUCT_BIC_INVALID + ": ONLY 10 BICS PERMITTED AS INPUT",
					HttpStatus.BAD_REQUEST);
		}*/

		for (String bic : bicList)
		{
			try
			{
				prodList.add(getProductByBic(bic, queryParams));
			}
			catch (ProductEngineException e)
			{
				if (skippedList == null)
					skippedList = new ArrayList<String>();
				skippedList.add(e.getMessage());
			}
		}

		prodResp.setProdList(prodList);
		prodResp.setSkippedList(skippedList);

		return prodResp;
	}

	/**
	 * This method fetches the product details by bic value.
	 *
	 * @param bic String
	 * @param queryParams QueryParams
	 * @return : Product
	 * @methodName : getEnrichedProductByBic
	 */
	public Product getProductByBic(String bic, QueryParams queryParams)
	{
		Product  products = new Product();

		try
		{
			if (bic != null)
			{
				
				 //Calling product by bic id Async
				Future<Product> asyncproductbybic=asyncProductsbyBicId.getProductByBicIdAsync(bic);
				 Product productBybic =asyncproductbybic.get();				 			 
				 
				 products.setBic(productBybic.getBic()); 
				 products.setProductId(productBybic.getProductId());
				 products.setBdcURL(productBybic.getBdcURL());
				
				 //Calling productDescripton async		 
				 Future<Description> apcd=asyncProductCMSDescription.getProductCMSDescByProductIdAsync(products.getProductId());
				 Description description =apcd.get();
				 products.setDescription(description);
				
				// calling product view api 	 
			    Future<CatalogEntryView> apv= asncProductView.getViewByProductIdAsync(productBybic);	
			    CatalogEntryView catalogEntryView =apv.get();    
				 
			    products.setSkuId(catalogEntryView.getSingleSKUCatalogEntryID());
			    
			    products.setSapId(catalogEntryView.getPartNumber()); 
			    
			    products.setName(catalogEntryView.getName()); 
			    products.setShortDescription(catalogEntryView.getShortDescription());
			    products.setFullDescription("");
			    products.setBrand(catalogEntryView.getManufacturer());  
			  
				 //Calling async Price view api
				 
				 Future<WCSPriceView> asyncPrice= asyncPriceView.getViewByPriceAsync(productBybic);			 
				 WCSPriceView price = asyncPrice.get();
			 //   products.setPrice(price.getNowPrice());
				 products.setCurrencySymbol("&");	
				// products.setRating(product.AverageReviewScore);
				 
				 products.setReviewCount(productBybic.getReviewCount()); 
				 products.setSize(price.getNetContents());
				 products.setWasPrice(price.getWasPrice());
				 
				 products.setPromotionalText("");
				 products.setPriceInPoints(price.getPriceInPoints());
				 products.setPricePerUnit(price.getPpu());
				 products.setSaving(price.getSaving());
				 products.setPointsEarned(price.getPointsEarned());
				 //products.setProductImage//db call
				 
				// products.setPromotions(price.getPromotions());
				 
				// products.setDescription("");
				 
				// products.setAlternateSizes(null);
				// products.setColor()
				 
			  // Calling WcsInventorystatus Api
							 
				 Future<String> inventorystatus= asyncWcsInventorystatus.getInventorystatusAsync(products);			 
				 String status = inventorystatus.get();
	             products.setStatus(status);
	            
				 			
			}
			else
			{
				throw new ProductEngineException(Constants.ERROR_PRODUCT_BIC_EMPTY + ": BIC ID VALUE IS EMPTY",
						HttpStatus.BAD_REQUEST);
			}

		
		}
		catch (NumberFormatException ex)
		{
			throw new ProductEngineException(Constants.ERROR_PRODUCT_BIC_INVALID
					+ ": NUMBERFORMAT EXCEPTION FOR BIC : " + bic, HttpStatus.NOT_ACCEPTABLE);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return products;
	}

	/**
	 * This method fetches the product details by list of productIds.
	 *
	 * @param productIds String
	 * @param queryParams QueryParams
	 * @return : Product
	 * @methodName : getProductByBics
	 */
	public ProductResponse getProductByProductIds(String productIds, QueryParams queryParams)
	{
		List<Product> prodList = new ArrayList<Product>();

		List<String> productIdList = Arrays.asList(productIds.split("\\s*,\\s*"));

		ProductResponse prodResp = new ProductResponse();
		List<String> skippedList = null;

		if (productIdList == null)
		{
			throw new ProductEngineException(Constants.ERROR_PRODUCT_ID_EMPTY + ": PRODUCT ID VALUE IS EMPTY",
					HttpStatus.BAD_REQUEST);
		}
		/*else if (productIdList.size() > 10)
		{
			throw new ProductEngineException(Constants.ERROR_PRODUCT_BIC_INVALID
					+ ": ONLY 10 PRODUCTIDs PERMITTED AS INPUT", HttpStatus.BAD_REQUEST);
		}*/

		for (String productId : productIdList)
		{
			try
			{
				prodList.add(getProductByProductId(productId, queryParams));
			}
			catch (ProductEngineException e)
			{
				if (skippedList == null)
					skippedList = new ArrayList<String>();
				skippedList.add(e.getMessage());
			}
		}

		prodResp.setProdList(prodList);
		prodResp.setSkippedList(skippedList);

		return prodResp;
	}

	/**
	 * This method fetches the product details by product id.
	 *
	 * @param productId String
	 * @param queryParams QueryParams
	 * @return : Product
	 * @methodName : getEnrichedProduct
	 */
	public Product getProductByProductId(String productId, QueryParams queryParams)
	{
	
		Product  products = new Product();
       	if (productId != null && !("").equals(productId))
		{
			 try {
				 
			 //Calling product async
			Future<Product> aps=asyncProducts.getProductIdAsync(productId);
			 Product product =aps.get();
			 products.setBic(product.getBic()); 
			 products.setProductId(product.getProductId());
			 products.setBdcURL(product.getBdcURL());			 		 
			 //Calling productDescripton async		 
			 Future<Description> apcd=asyncProductCMSDescription.getProductCMSDescByProductIdAsync(products.getProductId());
			 Description description =apcd.get();
			 products.setDescription(description);
			 
			// calling product view api 	 
		    Future<CatalogEntryView> apv= asncProductView.getViewByProductIdAsync(products);		         	    
		    CatalogEntryView catalogEntryView =apv.get();
		    
		    String parentBic = null;
			Boolean hasSingleSKU = catalogEntryView.getHasSingleSKU();			
			products.setName(catalogEntryView.getName());		
			
//			parentProduct.setImageURL(product.getImageURL());
			products.setRating(product.getRating());
			products.setReviewCount(product.getReviewCount());
			products.setPromotionalText(product.getPromotionalText());
			//parentProduct.setInStock(product.getInStock());
			products.setFullDescription(product.getFullDescription());
			String partNumber = catalogEntryView.getPartNumber();
			if (!StringUtils.isEmpty(partNumber) && partNumber.contains(DOT_STRING))
			{
				partNumber = partNumber.substring(0, partNumber.lastIndexOf(DOT_STRING));
			}
			products.setSapId(partNumber);
			products.setShortDescription(catalogEntryView.getShortDescription());
			if (hasSingleSKU)
			{
				parentBic = catalogEntryView.getMfPartNumber_ntk();
				if (!StringUtils.isEmpty(parentBic))
				{
					products.setBic(parentBic);
				}
				else
				{
					products.setBic(product.getBic());
				}

				products.setSkuId(catalogEntryView.getSingleSKUCatalogEntryID());

			}
			else
			{
				if (StringUtils.isEmpty(partNumber))
				{
					products.setBic(product.getBic());
				}
				else
				{
					products.setBic(partNumber);
				}
				products.setSkuId(catalogEntryView.getUniqueID());
				List<SKUs> skus = catalogEntryView.getSKUs();
				if (CollectionUtils.isNotEmpty(skus))
				{
				
					processSkus(skus, products);
				}
			}
	      			
			//products.getAlternateSizes();
			
			 Future<WCSPriceView> asyncPrice= asyncPriceView.getViewByPriceAsync(product);			 
			 WCSPriceView price = asyncPrice.get();
	//			products = setPriceDetails(products,price);
			 
			 
		 
			 //products.setProductImage//db call
			 
			// products.setPromotions(price.getPromotions());
			 
			// products.setDescription("");
			 
			// products.setAlternateSizes(null);
			// products.setColor()
			 
		  // Calling WcsInventorystatus Api
						 
			 Future<String> inventorystatus= asyncWcsInventorystatus.getInventorystatusAsync(products);			 
			 String status = inventorystatus.get();
             products.setStatus(status);
            			 
			} catch (InterruptedException | ExecutionException e) {
					
					e.printStackTrace();
				}
	
		}
		else
		{
			throw new ProductEngineException(Constants.ERROR_PRODUCT_ID_EMPTY + ": PRODUCT ID VALUE IS EMPTY",
					HttpStatus.BAD_REQUEST);
		}
    	return products;
	}

	/**
	 * This method fetches the product details by product id.
	 *
	 * @param brand String
	 * @param limit String
	 * @param queryParams QueryParams
	 * @return : Product
	 * @methodName : getEnrichedProduct
	 *//*
		
	public List<Product> getProductsByBrand(String brand, String limit, QueryParams queryParams)
	{
		
		List<Product> productList = null;
	
		if (brand != null && !("").equals(brand))
		{
			productList = productDAO.findByBrand(brand, limit);

			if (productList != null && !productList.isEmpty())
			{
				for (Product pro : productList)
				{
					List<ProductPromotion> productPromotions = productPromotionDAO.findBySkuId(pro.getSkuId());
					pro.setPromotions(productPromotions);
				//	setAdditionalDetails(pro, queryParams);

					if (queryParams.getIsColourReqd())
					{
						
						ProductColour productColour = proColourDAO.findBySkuId(pro.getSkuId());
						if (productColour.getSkuId() != null)
						{
							List<ProductColour> productColours = new ArrayList<ProductColour>();
							productColours.add(productColour);
							pro.setProductColours(productColours);
						}
					}
					
				}
				

			}
			else
			{
				throw new ProductEngineException(Constants.ERROR_PRODUCT_NOT_FOUND
						+ ": NO PRODUCTS FOUND FOR THE PROVIDED BRAND :" + brand, HttpStatus.NOT_FOUND);
			}
		}
		else
		{
			throw new ProductEngineException(Constants.ERROR_PRODUCT_BRAND_EMPTY + ": PRODUCT BRAND VALUE IS EMPTY",
					HttpStatus.BAD_REQUEST);
		}
		 System.out.println("getProductsByBrand-->2");
		return productList;
	}*/



/**
 * Checks if input string is integer convertable format or not.
 *
 * @param string String
 * @return true, if is integer
 */
static boolean isInteger(String string)
{
	try
	{
		Integer.parseInt(string);
		return true;
	}

	catch (NumberFormatException exception)
	{
		return false;
	}
}


/**
 * Process skus.
 *
 * @param skus List<SKUs>
 * @param product Product
 */
private void processSkus(List<SKUs> skus, Product product)
{
	
	for (SKUs sku : skus)
	{
		String skuBic = sku.getMfPartNumber_ntk();
		if (StringUtils.isEmpty(skuBic) && !isInteger(skuBic))
		{
			skuBic = sku.getPartNumber();
		}

		if (!bicIds.contains(skuBic))
		{
			ProductColour productColour = saveProductColour(sku, product.getProductId());
			Product skuProduct = new Product();
			skuProduct.setProductId(product.getProductId());
		//	skuProduct.setBarcodeId(product.getBarcodeId());
			skuProduct.setName(sku.getName());
			skuProduct.setBrand(product.getBrand());
			skuProduct.setBdcURL(product.getBdcURL());
			skuProduct.setPrice(product.getPrice());
			skuProduct.setCurrencySymbol(product.getCurrencySymbol());
			//skuProduct.setImageURL(product.getImageURL());
			skuProduct.setRating(product.getRating());
			skuProduct.setReviewCount(product.getReviewCount());
			skuProduct.setPromotionalText(product.getPromotionalText());
			//skuProduct.setInStock(product.getInStock());
			skuProduct.setSapId(sku.getPartNumber());
			skuProduct.setShortDescription(sku.getShortDescription());
			skuProduct.setFullDescription(product.getFullDescription());
			if (productColour != null)
			{
				productColour.setBic(skuBic);
				skuProduct.setSkuId(productColour.getSkuId());
				skuProduct.setBic(skuBic);
				//skuProduct = setPriceDetails(skuProduct);
				

				LOGGER.info("Added product colour : Bic : " + productColour.getBic() + ", skuId : "
						+ productColour.getSkuId() + ", Product Name : " + product.getName());

			}
			else
			{
				LOGGER.info("Colour details are missing : Bic : " + skuBic + ", Product Name : "
						+ product.getName());
			}
		}
	}

}

/**
 * Gets the product colour.
 *
 * @param sku SKUs
 * @param productId String
 * @return ProductColour
 */
private ProductColour saveProductColour(SKUs sku, String productId)
{
	ProductColour productColour = null;
	List<Attributes> colourAttributes = sku.getAttributes();
	if (CollectionUtils.isNotEmpty(colourAttributes))
	{
		for (Attributes attribute : colourAttributes)
		{
			if (productColour == null)
			{
				productColour = processColourAttributes(attribute, sku, productId);
			}
			else
			{
				break;
			}

		}

	}
	return productColour;
}

/**
 * Gets the product colour details.
 *
 * @param attribute Attributes
 * @param sku SKUs
 * @param productId String
 * @return ProductColour
 */
private ProductColour processColourAttributes(Attributes attribute, SKUs sku, String productId)
{

	ProductColour productColour = null;
	if (attribute.getIdentifier().equalsIgnoreCase(WCS_PRODUCTVIEW_SKU_IDENTIFIER_COLOUR)
			|| attribute.getIdentifier().equalsIgnoreCase(WCS_PRODUCTVIEW_SKU_IDENTIFIER_COLOR))
	{
		productColour = new ProductColour();
		List<Values> attributeValues = attribute.getValues();
		if (CollectionUtils.isNotEmpty(attributeValues))
		{
			productColour.setSkuId(sku.getUniqueID());
			productColour.setProductId(productId);
			productColour.setPartNumber(sku.getPartNumber());
			productColour.setColour(attributeValues.get(0).getValue());

		}
	}

	return productColour;
}


}
