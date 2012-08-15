package MakePlus;

import OpenRate.exception.ProcessingException;
import OpenRate.lang.AuditSegment;
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
  public static String CVS_MODULE_INFO = "OpenRate, $RCSfile: CustomerLookup.java,v $, $Revision: 1.16 $, $Date: 2011/11/08 20:18:31 $";

  // used to improve performance - lookup customer fills it, then lookup product
  // and lookupERA can use it
  private AuditSegment tmpAuditSeg = null;
  
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
    if (LookupCustomerID(CurrentRecord))
    {
      // Look up the products using the audit segment we found, the subscription id
      // (we use the guiding key again) and store the results directly into the 
      // current record
      if(!LookupProduct(tmpAuditSeg,CurrentRecord.GuidingKey,CurrentRecord))
      {
        // we cannot find the rateplan associated to the alias
        tmpError = new RecordError("ERR_RATEPLAN_NOT_FOUND", ErrorType.SPECIAL);
        tmpError.setModuleName(getSymbolicName());
        tmpError.setErrorDescription("Could not locate customer rateplan for guiding key:"+CurrentRecord.GuidingKey+" at date:"+CurrentRecord.UTCEventDate);
        CurrentRecord.addError(tmpError);
      }
      // look up if there are any ERAs
      else
      {
        LookupERA(tmpAuditSeg,CurrentRecord);
      }
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
    IError tmpError;
    Integer CustomerID = 0;

    try
    {
      // get the customer ID for the alias
      CustomerID = getCustId(CurrentRecord.GuidingKey,CurrentRecord.UTCEventDate);
      if ((CustomerID != null) && (CustomerID.intValue() != 0))
      {
        // Set the internal user Id for managing the balance group
        CurrentRecord.UserId = CustomerID;
        CurrentRecord.MSN = getMSN(CustomerID);
        // Get the audit segment, as this will speed up the rest of the processing
        tmpAuditSeg = getAuditSegment(CustomerID, CurrentRecord.UTCEventDate);
        
        // if we didn't get an audit segment, then that is an error
        if (tmpAuditSeg == null)
        {
          // we could not identify the customer that has to be charged for this event
          tmpError = new RecordError("ERR_AUD_SEG_NOT_FOUND", ErrorType.SPECIAL);
          tmpError.setModuleName(getSymbolicName());
          tmpError.setErrorDescription("Could not locate audit segment for cust id:"+CustomerID+" at date:"+CurrentRecord.UTCEventDate);
          CurrentRecord.addError(tmpError);
          
          return false;
        }
        
        return true;
      }
      else
      {
        // we could not identify the customer that has to be charged for this event
        tmpError = new RecordError("ERR_CUSTOMER_ID_NOT_FOUND", ErrorType.SPECIAL);
        tmpError.setModuleName(getSymbolicName());
        tmpError.setErrorDescription("Could not locate customer id with guiding key:"+CurrentRecord.GuidingKey+" at date:"+CurrentRecord.UTCEventDate);
        CurrentRecord.addError(tmpError);
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
  * based on the validity of the customer and of the individual products. The
  * validity is taken from the audit segment, which is a snapshot of the customer
  * state at the time of the event.
  *
  * @param CurrentRecord
  * @return 0 if everything went OK
  */
  private boolean LookupProduct(AuditSegment tmpAuditSeg, String subscriptionID, CDRRecord CurrentRecord)
  {
    // Get all the product for the customer (all subscriptions)
    // ("null" subscription value means all)
    ProductList tmpProductList = CC.getProducts(tmpAuditSeg, subscriptionID);

    // we need to have at only 1 product for the rateplan (at the EventDate time)
    if (tmpProductList != null && tmpProductList.getProductCount() > 0)
    {
      // get the first product
      CustProductInfo product=tmpProductList.getProduct(0);
      CurrentRecord.UserTariff=product.getProductID();

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

 /**
  * This looks up a customer record and the products associated with it,
  * based on the validity of the customer and of the individual products.
  *
  * @param CurrentRecord
  * @return 0 if everything went OK
  */
  private boolean LookupERA(AuditSegment tmpAuditSeg,CDRRecord CurrentRecord)
  {
    String discount = getERA(tmpAuditSeg, "RD");

    if (discount == null)
    {
      // No discount found, just give a blank value
      CurrentRecord.DiscountOption="";
    }
    else
    {
      // transfer the discount value we found
      CurrentRecord.DiscountOption=discount;
    }

    return true;
  }
}

