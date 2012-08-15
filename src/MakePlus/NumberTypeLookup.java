package MakePlus;

import OpenRate.process.AbstractStubPlugIn;
import OpenRate.record.ErrorType;
import OpenRate.record.IRecord;
import OpenRate.record.RecordError;

/**
 * This module retrieves the Number Type of the event and performs a best
 * match on it.
 */
public class NumberTypeLookup extends AbstractStubPlugIn
{
  // this is the CVS version info
  public static String CVS_MODULE_INFO = "OpenRate, $RCSfile: NumberTypeLookup.java,v $, $Revision: 1.1 $, $Date: 2011/07/22 08:44:36 $";

  // -----------------------------------------------------------------------------
  // ------------------ Start of inherited Plug In functions ---------------------
  // -----------------------------------------------------------------------------

  /**
  * This transformation looks up the zone name prefix from the input CDR.
  */
  @Override
  public IRecord procValidRecord(IRecord r)
  {
    String tmpDest;
    RecordError tmpError;
    CDRRecord CurrentRecord = (CDRRecord)r;

    if (CurrentRecord.RECORD_TYPE == CDRRecord.CDR_RECORD)
    {
      tmpDest = CurrentRecord.getField(CDRRecord.IDX_NUMBER_TYPE);
      if (tmpDest == null || tmpDest.isEmpty())
      {
        // error detected, add an error to the record
        tmpError = new RecordError("ERR_ZONE_DEST_NOT_FOUND", ErrorType.DATA_NOT_FOUND);
        tmpError.setModuleName(getSymbolicName());
        tmpError.setErrorDescription("ZoneDestination Value Not Found");
        CurrentRecord.addError(tmpError);
      }
      else
      {
        // Look up the BNumber destination from the input field in the CDR
        CurrentRecord.ZoneDestination = CurrentRecord.getField(CDRRecord.IDX_NUMBER_TYPE);
      }
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
