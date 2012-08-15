package MakePlus;

import OpenRate.exception.ProcessingException;
import OpenRate.lang.CustProductInfo;
import OpenRate.lang.ProductList;
import OpenRate.process.AbstractCustomerLookupAudited;
import OpenRate.record.ErrorType;
import OpenRate.record.IError;
import OpenRate.record.IRecord;
import OpenRate.record.RecordError;

/**
 * This module tries to find a User(Customer) with a valid validity period from 
 * the cache.
 *
 * If a valid customer is found, then it loads all the products that are associated
 * with this particular customer.
 *
 * Then we assign the RatePlan Name and User Tariff associated with this product
 * to the Current Record Charge Packet
 */
public class CustomerLookup extends AbstractCustomerLookupAudited
{
  // this is the CVS version info
  public static String CVS_MODULE_INFO = "OpenRate, $RCSfile: CustomerLookup.java,v $, $Revision: 1.13 $, $Date: 2011/07/28 14:45:15 $";

  /**
  * This is called when a valid detail record is encountered.
  */
  @Override
  public IRecord procValidRecord(IRecord r)
  {
    CDRRecord CurrentRecord;

    CurrentRecord = (CDRRecord)r;
    IError tmpError;

    // Get the customer information
    if (!LookupCustomerID(CurrentRecord))
    {
      // we could not identify the customer that has to be charged for this event
      tmpError = new RecordError("ERR_CUSTOMER_NOT_FOUND", ErrorType.SPECIAL);
      tmpError.setModuleName(getSymbolicName());
      tmpError.setErrorDescription("Could not locate customer with guiding key:"+CurrentRecord.GuidingKey+" at date:"+CurrentRecord.UTCEventDate);
      CurrentRecord.addError(tmpError);
    }
    else if(!LookupProduct(CurrentRecord))
    {
      // we cannot find the rateplan associated to the alias
      tmpError = new RecordError("ERR_RATEPLAN_NOT_FOUND", ErrorType.SPECIAL);
      tmpError.setModuleName(getSymbolicName());
      tmpError.setErrorDescription("Could not locate customer rateplan for guiding key:"+CurrentRecord.GuidingKey+" at date:"+CurrentRecord.UTCEventDate);
      CurrentRecord.addError(tmpError);
    }

    return r;
  }

 /**
  * This is called when an error detail record is encountered.
  */
  @Override
  public IRecord procErrorRecord(IRecord r)
  {
    return null;
  }

  // -----------------------------------------------------------------------------
  // ------------------------ Start of custom functions --------------------------
  // -----------------------------------------------------------------------------

 /**
  * This looks up a customer record and the products associated with it,
  * based on the validity of the customer and of the individual products.
  *
  * @param CurrentRecord
  * @return 0 if everything went OK
  */
  private boolean LookupCustomerID(CDRRecord CurrentRecord)
  {
    Integer CustomerID = 0;

    try
    {
      // get the customer ID for the alias
      CustomerID = getCustId(CurrentRecord.GuidingKey,CurrentRecord.UTCEventDate);
      if ((CustomerID != null) && (CustomerID.intValue() != 0))
      {
        // Set the internal user Id for managing the balance group
        CurrentRecord.UserId = CustomerID;
        return true;
      }
    }
    catch (ProcessingException pe)
    {
      return false;
    }

    return false;
  }

   /**
  * This looks up a customer record and the products associated with it,
  * based on the validity of the customer and of the individual products.
  *
  * @param CurrentRecord
  * @return 0 if everything went OK
  */
  private boolean LookupProduct(CDRRecord CurrentRecord)
  {
    try
    {
      // Get all the product for the customer (all subscriptions)
      // ("null" subscription value means all)
      ProductList tmpProductList = CC.getProducts(CurrentRecord.UserId, CurrentRecord.GuidingKey,
              CurrentRecord.UTCEventDate);

      // we need to have at only 1 product for the rateplan (at the EventDate time)
      if (tmpProductList != null && tmpProductList.getProductCount() > 0)
      {
        // get the first product
        CustProductInfo product=tmpProductList.getProduct(0);
        CurrentRecord.UserTariff=product.getProductID();
        CurrentRecord.Reseller = product.getProductID();

        // warn if there was more than 1
        if( tmpProductList.getProductCount() > 1)
        {
          PipeLog.warning("Multiple products <" + tmpProductList.getProductCount() + "> found for alias <" + CurrentRecord.GuidingKey + ">");
        }
        
        return true;
      }
      else
      {
        return false;
      }
    }
    catch (ProcessingException pe)
    {
      return false;
    }
  }

 /**
  * This looks up a customer record and the products associated with it,
  * based on the validity of the customer and of the individual products.
  *
  * @param CurrentRecord
  * @return 0 if everything went OK
  */
  private boolean LookupERA(CDRRecord CurrentRecord,String key)
  {
    try
    {
      String reseller = getERA(CurrentRecord.GuidingKey, key, CurrentRecord.UTCEventDate);

      if (reseller==null || reseller.length()==0)
      {
        return false;
      }
      CurrentRecord.Reseller=reseller;
    }
    catch (ProcessingException pe)
    {
      return false;
    }

    return true;
  }
}

