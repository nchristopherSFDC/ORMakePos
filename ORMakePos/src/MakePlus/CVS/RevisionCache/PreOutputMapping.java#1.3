package MakePlus;

import OpenRate.exception.ProcessingException;
import OpenRate.process.AbstractStubPlugIn;
import OpenRate.record.IRecord;

/**
 * This module gathers the output from the charge packets and rolls them up
 * into the variables that we will use for outputting.
 */
public class PreOutputMapping extends AbstractStubPlugIn
{
  // this is the CVS version info
  public static String CVS_MODULE_INFO = "OpenRate, $RCSfile: PreOutputMapping.java,v $, $Revision: 1.3 $, $Date: 2011/06/28 13:23:57 $";

  public static String RETAIL_RATE    = "R";
  public static String WHOLESALE_RATE = "W";
  public static String LOCAL_COST     = "LC";
  public static String LOCAL_TRANSIT  = "LT";
  public static String REMOTE_COST    = "RC";
  public static String REMOTE_TRANSIT = "RT";

  @Override
  public IRecord procValidRecord(IRecord r) throws ProcessingException
  {
    double retail,wholesale,localCost,localTransit,remoteCost,remoteTransit;
    CDRRecord CurrentRecord = (CDRRecord) r;

    // Initialise
    retail = wholesale = localCost = localTransit = remoteCost = remoteTransit = 0.0;

    // Get the impacts
    for (int i=0; i<CurrentRecord.getBalanceImpactCount(); i++)
    {
      if (CurrentRecord.getBalanceImpact(i).Resource.equalsIgnoreCase("R"))
      {
        retail+=CurrentRecord.getBalanceImpact(i).BalanceDelta;
      }
      else if(CurrentRecord.getBalanceImpact(i).Resource.equalsIgnoreCase("W"))
      {
        wholesale+=CurrentRecord.getBalanceImpact(i).BalanceDelta;
      }
      else if(CurrentRecord.getBalanceImpact(i).Resource.equalsIgnoreCase("LC"))
      {
        localCost+=CurrentRecord.getBalanceImpact(i).BalanceDelta;
      }
      else if(CurrentRecord.getBalanceImpact(i).Resource.equalsIgnoreCase("LT"))
      {
        localTransit+=CurrentRecord.getBalanceImpact(i).BalanceDelta;
      }
      else if(CurrentRecord.getBalanceImpact(i).Resource.equalsIgnoreCase("RC"))
      {
        remoteCost+=CurrentRecord.getBalanceImpact(i).BalanceDelta;
      }
      else if(CurrentRecord.getBalanceImpact(i).Resource.equalsIgnoreCase("RT"))
      {
        remoteTransit+=CurrentRecord.getBalanceImpact(i).BalanceDelta;
      }
    }

    // Put them back into the CDR fields
    CurrentRecord.retailPrice = retail;
    CurrentRecord.wholeSalePrice = wholesale;
    
    if (localCost < 0.0)
    {
      CurrentRecord.localPolo = localCost;
    }
    else
    {
      CurrentRecord.localRolo = localCost;
    }

    CurrentRecord.localTransit = localTransit;

    if (remoteCost < 0.0)
    {
      CurrentRecord.remotePolo = remoteCost;
    }
    else
    {
      CurrentRecord.remoteRolo = remoteCost;
    }

    CurrentRecord.remoteTransit = remoteTransit;

    return r;
  }

  @Override
  public IRecord procErrorRecord(IRecord r) throws ProcessingException
  {
    return r;
  }

}
