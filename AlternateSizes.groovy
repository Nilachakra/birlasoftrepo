/**********************************************************************
 * 
 *  
 **********************************************************************/


import com.fasterxml.jackson.annotation.JsonIgnore
import com.wordnik.swagger.annotations.ApiModel
import com.wordnik.swagger.annotations.ApiModelProperty

/**
 * *************************************************************************************************
 * The Class AlternateSizes.
 *
 * @version : 1.0
 * @class : AlternateSizes
 * @implements : none
 * @extends : none
 * 
 * ************************************************************************************************
 */
@ApiModel
public class AlternateSizes {
	
	/** Field bic. */
	@ApiModelProperty(position = 1, required = true, value = "The bic id of alternate product")
	String bic;
	
	/** Field product id. */
	@ApiModelProperty(position = 2, required = true, value = "The product id of alternate product")
	String productId;
	
	/** Field sku id. */
	@ApiModelProperty(position = 3, required = true, value = "The sku id of alternate product")
	String skuId;
	
	/** Field name. */
	@ApiModelProperty(position = 4, required = true, value = "The name of alternate product")
	String name;
	
	/** Field full description. */
	@ApiModelProperty(position = 5, required = false, value = "The full description of alternate product")
	String fullDescription;
	
	/** Field brand. */
	@ApiModelProperty(position = 6, required = true, value = "The brand name of alternate product")
	String brand;
	
	/** Field bdc url. */
	@ApiModelProperty(position = 7, required = true, value = "The boots URL of alternate product")
	String bdcURL;
	
	/** Field price. */
	@ApiModelProperty(position = 8, required = true, value = "The price of alternate product")
	BigDecimal price;
	
	/** Field currency symbol. */
	@ApiModelProperty(position = 9, required = true, value = "The currency symbol of alternate product")
	String currencySymbol;
	
	/** Field rating. */
	@ApiModelProperty(position = 10, required = true, value = "The rating of alternate product")
	String rating;
	
	/** Field review count. */
	@ApiModelProperty(position = 11, required = true, value = "The review count of alternate product")
	String reviewCount;
	
	/** Field status. */
	@ApiModelProperty(position = 12, required = true, value = "The status of of alternate product. The possible status values are I(instock), O(out of stock), N(not available) and R(recall)")
	String status;
	
	/** Field size. */
	@ApiModelProperty(position = 13, required = true, value = "The size of alternate product, eg: 50ml")
	String size;
	
	/** Field was price. */
	@ApiModelProperty(position = 14, required = true, value = "The was price of alternate product")
	String wasPrice;
	
	// TODO:Since we are using the promotions array for listing all promotions, no need to show the below field.
	
	/** Field promotional text. */
	@JsonIgnore
	String promotionalText;
	
	/** Field price in points. */
	@ApiModelProperty(position = 15, required = true, value = "The price in points of alternate product")
	String priceInPoints;
	
	/** Field price per unit. */
	@ApiModelProperty(position = 16, required = true, value = "The price per unit(ppu) of alternate product")
	String pricePerUnit;
	
	/** Field saving. */
	@ApiModelProperty(position = 17, required = true, value = "The savings of alternate product")
	String saving;
	
	/** Field points earned. */
	@ApiModelProperty(position = 18, required = true, value = "The points earned after alternate product purchase")
	String pointsEarned;
	
	/** Field product img. */
	@ApiModelProperty(position = 19, required = true, value = "The alternate product image details")
	ProductImage productImg;
	
	/** Field product colours. */
	//@ApiModelProperty(position = 20, required = false, value = "The alternate product colour details")
	//List<ProductColour> productColours;
}
