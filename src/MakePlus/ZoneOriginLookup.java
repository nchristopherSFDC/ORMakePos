package MakePlus;

import OpenRate.lang.DigitTree;
import OpenRate.process.AbstractBestMatch;
import OpenRate.record.RecordError;
import OpenRate.record.ErrorType;
import OpenRate.record.IRecord;

/**
 * This module attempts to retrieve the origin of the event by matching against
 * the origin_map table by type-of-service and User.
 */
public class ZoneOriginLookup extends AbstractBestMatch
{
  // this is the CVS version info
  public static String CVS_MODULE_INFO = "OpenRate, $RCSfile: ZoneOriginLookup.java,v $, $Revision: 1.8 $, $Date: 2011/08/15 17:57:43 $";

  // -----------------------------------------------------------------------------
  // ------------------ Start of inherited Plug In functions ---------------------
  // -----------------------------------------------------------------------------

  /**
  * This is called when a data record is encountered. You should do any normal
  * processing here.
  *
  * This transformation looks up the zone name prefix using the best match
  * ZoneCache lookup. Because this example does not care about services, we
  * define the service type as a default "DEF".
  */
  @Override
  public IRecord procValidRecord(IRecord r)
  {
    RecordError tmpError;
    String ZoneValue;
    CDRRecord CurrentRecord = (CDRRecord)r;

    try
    {
      // Look up the Origin
      ZoneValue = getBestMatch(CurrentRecord.Service, CurrentRecord.NumberTypeNorm);

      // Write the information back into the record
      if (ZoneValue.equalsIgnoreCase(DigitTree.NO_DIGIT_TREE_MATCH))
      {
        // error detected, add an error to the record
        tmpError = new RecordError("ERR_ZONE_ORIG_NOT_FOUND", ErrorType.DATA_NOT_FOUND);
        tmpError.setModuleName(getSymbolicName());
        tmpError.setErrorDescription("ZoneOrigin Value Not Found for ANumber:" + CurrentRecord.NumberTypeNorm);
        CurrentRecord.addError(tmpError);
      }
      else
      {  // the zoning was successful
        CurrentRecord.ZoneOrigin = ZoneValue;
      }
    }
    catch (Exception e)
    {
      // error detected, add an error to the record
      tmpError = new RecordError("ERR_ZONE_ORIG_NOT_FOUND", ErrorType.DATA_NOT_FOUND);
      tmpError.setModuleName(getSymbolicName());
      tmpError.setErrorDescription("ZoneOrigin Value Not Found for ANumber:" + CurrentRecord.NumberTypeNorm);
      CurrentRecord.addError(tmpError);
    }

    return r;
  }

  /**
  * This is called when a data record with errors is encountered. You should do
  * any processing here that you have to do for error records, e.g. stratistics,
  * special handling, even error correction!
  */
  @Override
  public IRecord procErrorRecord(IRecord r)
  {
    return r;
  }
}
