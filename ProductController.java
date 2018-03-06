/**********************************************************************

 ***********************************************************************/
package com.productengine.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.productengine.business.ProductManager;
import com.productengine.common.BaseController;
import com.productengine.common.Mappings;
import com.productengine.model.Product;
import com.productengine.model.ProductResponse;
import com.productengine.model.QueryParams;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
/*import com.wordnik.swagger.annotations.ApiOperation;
 import com.wordnik.swagger.annotations.ApiParam;
 import com.wordnik.swagger.annotations.ApiResponse;
 import com.wordnik.swagger.annotations.ApiResponses;*/
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * *************************************************************************************************
 * 
 * ProductController class provides the functionalities to fetch product details.
 *
 *
 * 
 * ************************************************************************************************
 */
@RestController
@RequestMapping("/" + Mappings.PRODUCT_VERSION + "/products")
public class ProductController extends BaseController
{

	/** Field manager. */
	@Autowired
	private ProductManager manager;

	/**
	 * This method fetches the list of products with details based on brand.
	 *
	 * @param brand String
	 * @param limit String
	 * @param isAltSizeReqd Boolean
	 * @param isDescReqd Boolean
	 * @param isOffersReqd Boolean
	 * @param isColourReqd Boolean
	 * @param isExtDetailsReqd Boolean
	 * @return List<Product>
	 * @methodName : getProductsByBrand
	 *//*
	@ApiOperation(value = "Get products by brand name", notes = "Returns a list of product details by product brand name and query parameter values. Examples of brand names are No7, Nivea, Olay, etc.", response = Product.class, httpMethod = "GET")
	@ApiResponses(value =
	{
			@ApiResponse(code = 200, message = "Successful retrieval of product details", response = Product.class),
			@ApiResponse(code = 404, message = "Product with given brand name does not exist"),
			@ApiResponse(code = 500, message = "Internal server error"),
			@ApiResponse(code = 401, message = "Forebidden error"),
			@ApiResponse(code = 403, message = "Unauthorized access of product") })
	@ApiImplicitParams(
	{
			@ApiImplicitParam(name = "ApiKey", value = "ApiKey", paramType = "header", required = true, dataType = "String") })
	@RequestMapping(value = "", params =
	{ "brand" }, method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<List<Product>> getProductsByBrand(
			@ApiParam(name = "brand", value = "Product brand name") @RequestParam(value = "brand") String brand,
			@ApiParam(name = "limit", value = "This parameter expects a number to decide the limit of products in the respone. By default all matching products will be listed when there is no limit defined.") @RequestParam(value = "limit", required = false) String limit,
			@ApiParam(name = "alternateSize", value = "The details of product's alternate size will be included in the response based on the value of this parameter. The alternate size details are loaded from supplier input. This feature stands descoped for now in BY") @RequestParam(value = "alternateSize", required = false, defaultValue = "false") Boolean isAltSizeReqd,
			@ApiParam(name = "desc", value = "This boolean value indicates if the description of the product is included in the response.") @RequestParam(value = "desc", required = false, defaultValue = "false") Boolean isDescReqd,
			@ApiParam(name = "offers", value = "This boolean value indicates if the offer details for the product is included in the response.") @RequestParam(value = "offers", required = false, defaultValue = "false") Boolean isOffersReqd,
			@ApiParam(name = "colour", value = "This boolean value indicates if the colour details for the product is included in the response.") @RequestParam(value = "colour", required = false, defaultValue = "false") Boolean isColourReqd,
			@ApiParam(name = "extended-details", value = "This boolean value indicates if the extended details for the product is included in the response. The extended details includes infomations like ingredients, usageInstructions, productEditorialCopy, etc.") @RequestParam(value = "extended-details", required = false, defaultValue = "false") Boolean isExtDetailsReqd)
	{
		String prodLimit = !(limit == null || ("").equals(limit)) ? limit : Integer.toString(Integer.MAX_VALUE);
		QueryParams queryParam = getQueryParams(isAltSizeReqd, isDescReqd, isOffersReqd, isColourReqd, isExtDetailsReqd);

		ResponseEntity<List<Product>> responseEntity = setHeaders(null, null, MediaType.APPLICATION_JSON,
				HttpStatus.OK, manager.getProductsByBrand(brand, prodLimit, queryParam));

		return responseEntity;
	}
*/
	/**
	 * This method fetches the product details based on bic id.
	 *
	 * @param bic String
	 * @param isAltSizeReqd Boolean
	 * @param isDescReqd Boolean
	 * @param isOffersReqd Boolean
	 * @param isColourReqd Boolean
	 * @param isExtDetailsReqd Boolean
	 * @return Product
	 * @methodName : getProductByBic
	 */
	@ApiOperation(value = "Get product by bic id", notes = "Returns product details by product bic id and based on query parameters. The product bic id is a unique supplier id, examples of bic id are 2189410, 1020617, 1020609, etc.", response = Product.class, httpMethod = "GET")
	@ApiResponses(value =
	{
			@ApiResponse(code = 200, message = "Successful retrieval of product details", response = Product.class),
			@ApiResponse(code = 404, message = "Product with given bic id does not exist"),
			@ApiResponse(code = 500, message = "Internal server error"),
			@ApiResponse(code = 401, message = "Forebidden error"),
			@ApiResponse(code = 403, message = "Unauthorized access of product") })
	@ApiImplicitParams(
	{
			@ApiImplicitParam(name = "ApiKey", value = "ApiKey", paramType = "header", required = true, dataType = "String") })
	@RequestMapping(value = "", params =
	{ "bic" }, method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<Product> getProductByBic(
			@ApiParam(name = "bic", value = "Product bic id") @RequestParam(value = "bic") String bic,
			@ApiParam(name = "alternateSize", value = "The details of product's alternate size will be included in the response based on the value of this parameter. The alternate size details are loaded from supplier input. This feature stands descoped for now in BY") @RequestParam(value = "alternateSize", required = false, defaultValue = "false") Boolean isAltSizeReqd,
			@ApiParam(name = "desc", value = "This boolean value indicates if the description of the product is included in the response.") @RequestParam(value = "desc", required = false, defaultValue = "false") Boolean isDescReqd,
			@ApiParam(name = "offers", value = "This boolean value indicates if the offer details for the product is included in the response.") @RequestParam(value = "offers", required = false, defaultValue = "false") Boolean isOffersReqd,
			@ApiParam(name = "colour", value = "This boolean value indicates if the colour details for the product is included in the response.") @RequestParam(value = "colour", required = false, defaultValue = "false") Boolean isColourReqd,
			@ApiParam(name = "extended-details", value = "This boolean value indicates if the extended details for the product is included in the response. The extended details includes infomations like ingredients, usageInstructions, productEditorialCopy, etc.") @RequestParam(value = "extended-details", required = false, defaultValue = "false") Boolean isExtDetailsReqd)
	{
		QueryParams queryParam = getQueryParams(isAltSizeReqd, isDescReqd, isOffersReqd, isColourReqd, isExtDetailsReqd);
		ResponseEntity<Product> responseEntity = setHeaders(null, null, MediaType.APPLICATION_JSON, HttpStatus.OK,
				manager.getProductByBic(bic, queryParam));

		return responseEntity;

	}

	/**
	 * This method fetches the product details based on product id.
	 *
	 * @param productId String
	 * @param isAltSizeReqd Boolean
	 * @param isDescReqd Boolean
	 * @param isOffersReqd Boolean
	 * @param isColourReqd Boolean
	 * @param isExtDetailsReqd Boolean
	 * @return Product
	 * @methodName : getProductByProductId
	 */
	@ApiOperation(value = "Get product by product id", notes = "Returns product details by product id and based on query parameters. The product id is not a unique id and in case of colour products the product id will be same. Examples of product id are 1547818, 1238653, 1360814, etc.", response = Product.class, httpMethod = "GET")
	@ApiResponses(value =
	{
			@ApiResponse(code = 200, message = "Successful retrieval of product details", response = Product.class),
			@ApiResponse(code = 404, message = "Product with given product id does not exist"),
			@ApiResponse(code = 500, message = "Internal server error"),
			@ApiResponse(code = 401, message = "Forebidden error"),
			@ApiResponse(code = 403, message = "Unauthorized access of product") })
	@ApiImplicitParams(
	{
			@ApiImplicitParam(name = "ApiKey", value = "ApiKey", paramType = "header", required = true, dataType = "String") })
	@RequestMapping(value = "", params =
	{ "productId" }, method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<Product> getProductByProductId(
			@ApiParam(name = "productId", value = "Product product id") @RequestParam("productId") String productId,
			@ApiParam(name = "alternateSize", value = "The details of product's alternate size will be included in the response based on the value of this parameter. The alternate size details are loaded from supplier input. This feature stands descoped for now in BY") @RequestParam(value = "alternateSize", required = false, defaultValue = "false") Boolean isAltSizeReqd,
			@ApiParam(name = "desc", value = "This boolean value indicates if the description of the product is included in the response.") @RequestParam(value = "desc", required = false, defaultValue = "false") Boolean isDescReqd,
			@ApiParam(name = "offers", value = "This boolean value indicates if the offer details for the product is included in the response.") @RequestParam(value = "offers", required = false, defaultValue = "false") Boolean isOffersReqd,
			@ApiParam(name = "colour", value = "This boolean value indicates if the colour details for the product is included in the response.") @RequestParam(value = "colour", required = false, defaultValue = "false") Boolean isColourReqd,
			@ApiParam(name = "extended-details", value = "This boolean value indicates if the extended details for the product is included in the response. The extended details includes infomations like ingredients, usageInstructions, productEditorialCopy, etc.") @RequestParam(value = "extended-details", required = false, defaultValue = "false") Boolean isExtDetailsReqd)
	{
		QueryParams queryParam = getQueryParams(isAltSizeReqd, isDescReqd, isOffersReqd, isColourReqd, isExtDetailsReqd);

		ResponseEntity<Product> responseEntity = setHeaders(null, null, MediaType.APPLICATION_JSON, HttpStatus.OK,
				manager.getProductByProductId(productId, queryParam));

		return responseEntity;

	}

	/**
	 * This method fetches the product details based on bic id.
	 *
	 * @param bics String
	 * @param isAltSizeReqd Boolean
	 * @param isDescReqd Boolean
	 * @param isOffersReqd Boolean
	 * @param isColourReqd Boolean
	 * @param isExtDetailsReqd Boolean
	 * @return Product
	 * @methodName : getProductByBic
	 */
	@ApiOperation(value = "Get products by bic id's", notes = "Returns list of product details by a bic id's and based on query parameters.The product bic id is a unique id and this API will accept a list of comma separated bic id's.", response = ProductResponse.class, httpMethod = "GET")
	@ApiResponses(value =
	{
			@ApiResponse(code = 200, message = "Successful retrieval of product details", response = ProductResponse.class),
			@ApiResponse(code = 404, message = "Product with given bic id does not exist"),
			@ApiResponse(code = 500, message = "Internal server error"),
			@ApiResponse(code = 401, message = "Forebidden error"),
			@ApiResponse(code = 403, message = "Unauthorized access of product") })
	@ApiImplicitParams(
	{
			@ApiImplicitParam(name = "ApiKey", value = "ApiKey", paramType = "header", required = true, dataType = "String") })
	@RequestMapping(value = "", params =
	{ "bics" }, method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<ProductResponse> getProductByBics(
			@ApiParam(name = "bics", value = "Product bic id's(comma separated)") @RequestParam(value = "bics") String bics,
			@ApiParam(name = "alternateSize", value = "The details of product's alternate size will be included in the response based on the value of this parameter. The alternate size details are loaded from supplier input. This feature stands descoped for now in BY") @RequestParam(value = "alternateSize", required = false, defaultValue = "false") Boolean isAltSizeReqd,
			@ApiParam(name = "desc", value = "This boolean value indicates if the description of the product is included in the response.") @RequestParam(value = "desc", required = false, defaultValue = "false") Boolean isDescReqd,
			@ApiParam(name = "offers", value = "This boolean value indicates if the offer details for the product is included in the response.") @RequestParam(value = "offers", required = false, defaultValue = "false") Boolean isOffersReqd,
			@ApiParam(name = "colour", value = "This boolean value indicates if the colour details for the product is included in the response.") @RequestParam(value = "colour", required = false, defaultValue = "false") Boolean isColourReqd,
			@ApiParam(name = "extended-details", value = "This boolean value indicates if the extended details for the product is included in the response. The extended details includes infomations like ingredients, usageInstructions, productEditorialCopy, etc.") @RequestParam(value = "extended-details", required = false, defaultValue = "false") Boolean isExtDetailsReqd)
	{
		QueryParams queryParam = getQueryParams(isAltSizeReqd, isDescReqd, isOffersReqd, isColourReqd, isExtDetailsReqd);
		ResponseEntity<ProductResponse> responseEntity = setHeaders(null, null, MediaType.APPLICATION_JSON,
				HttpStatus.OK, manager.getProductByBics(bics, queryParam));

		return responseEntity;

	}

	/**
	 * This method fetches the product details based on product id.
	 *
	 * @param productIds String
	 * @param isAltSizeReqd Boolean
	 * @param isDescReqd Boolean
	 * @param isOffersReqd Boolean
	 * @param isColourReqd Boolean
	 * @param isExtDetailsReqd Boolean
	 * @return Product
	 * @methodName : getProductByProductId
	 */
	@ApiOperation(value = "Get products by product id's", notes = "Returns list of product details by a product id's and based on query parameters.This API will accept a list of product id's and returns the an array of product details.", response = ProductResponse.class, httpMethod = "GET")
	@ApiResponses(value =
	{
			@ApiResponse(code = 200, message = "Successful retrieval of product details", response = ProductResponse.class),
			@ApiResponse(code = 404, message = "Product with given bic id does not exist"),
			@ApiResponse(code = 500, message = "Internal server error"),
			@ApiResponse(code = 401, message = "Forebidden error"),
			@ApiResponse(code = 403, message = "Unauthorized access of product") })
	@ApiImplicitParams(
	{
			@ApiImplicitParam(name = "ApiKey", value = "ApiKey", paramType = "header", required = true, dataType = "String") })
	@RequestMapping(value = "", params =
	{ "productIds" }, method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<ProductResponse> getProductByProductIds(
			@RequestParam("productIds") String productIds,
			@ApiParam(name = "alternateSize", value = "The details of product's alternate size will be included in the response based on the value of this parameter. The alternate size details are loaded from supplier input. This feature stands descoped for now in BY") @RequestParam(value = "alternateSize", required = false, defaultValue = "false") Boolean isAltSizeReqd,
			@ApiParam(name = "desc", value = "This boolean value indicates if the description of the product is included in the response.") @RequestParam(value = "desc", required = false, defaultValue = "false") Boolean isDescReqd,
			@ApiParam(name = "offers", value = "This boolean value indicates if the offer details for the product is included in the response.") @RequestParam(value = "offers", required = false, defaultValue = "false") Boolean isOffersReqd,
			@ApiParam(name = "colour", value = "This boolean value indicates if the colour details for the product is included in the response.") @RequestParam(value = "colour", required = false, defaultValue = "false") Boolean isColourReqd,
			@ApiParam(name = "extended-details", value = "This boolean value indicates if the extended details for the product is included in the response. The extended details includes infomations like ingredients, usageInstructions, productEditorialCopy, etc.") @RequestParam(value = "extended-details", required = false, defaultValue = "false") Boolean isExtDetailsReqd)
	{
		QueryParams queryParam = getQueryParams(isAltSizeReqd, isDescReqd, isOffersReqd, isColourReqd, isExtDetailsReqd);
		ResponseEntity<ProductResponse> responseEntity = setHeaders(null, null, MediaType.APPLICATION_JSON,
				HttpStatus.OK, manager.getProductByProductIds(productIds, queryParam));

		return responseEntity;

	}

	/**
	 * Gets the query params.
	 *
	 * @param isAltSizeReqd boolean
	 * @param isDescReqd boolean
	 * @param isOffersReqd boolean
	 * @param isColourReqd boolean
	 * @param isExtDetailsReqd boolean
	 * @return QueryParams
	 */
	private QueryParams getQueryParams(boolean isAltSizeReqd, boolean isDescReqd, boolean isOffersReqd,
			boolean isColourReqd, boolean isExtDetailsReqd)
	{
		QueryParams queryParam = new QueryParams();
		queryParam.setIsAltSizeReqd(isAltSizeReqd);
		queryParam.setIsColourReqd(isColourReqd);
		queryParam.setIsDescReqd(isDescReqd);
		queryParam.setIsExtDetailsReqd(isExtDetailsReqd);
		queryParam.setIsOffersReqd(isOffersReqd);

		return queryParam;
	}

}
