package MakePlus;

import OpenRate.exception.InitializationException;
import OpenRate.exception.ProcessingException;
import OpenRate.process.AbstractRUMTimeMatch;
import OpenRate.record.ErrorType;
import OpenRate.record.IRecord;
import OpenRate.record.RecordError;

/**
 * This module tries to identify the Time Model Result, i.e ECO, PEAK or WKD
 */
public class TimeMatch extends AbstractRUMTimeMatch
{
  // this is the CVS version info
  public static String CVS_MODULE_INFO = "OpenRate, $RCSfile: TimeMatch.java,v $, $Revision: 1.5 $, $Date: 2011/04/27 16:10:07 $";

  // -----------------------------------------------------------------------------
  // ------------------ Start of inherited Plug In functions ---------------------
  // -----------------------------------------------------------------------------

 /**
  * Perform custom initialisation on this module - turn on delegated
  * exception reporting
  */
  @Override
  public void init(String PipelineName, String ModuleName)
            throws InitializationException
  {
    // Do the inherited work, e.g. setting the symbolic name etc
    super.init(PipelineName,ModuleName);

    // report core exceptions back to this module
    this.setExceptionReporting(true);
  }

 /**
  * This is called when a data record is encountered. You should do any normal
  * processing here.
  */
  @Override
  public IRecord procValidRecord(IRecord r)
  {
    RecordError tmpError;
    CDRRecord CurrentRecord = (CDRRecord)r;

    try
    {
      performRUMTimeMatch(CurrentRecord);
    }
    catch (ProcessingException pe)
    {
      tmpError = new RecordError("ERR_TIME_MATCH_FAILED", ErrorType.SPECIAL);
      tmpError.setModuleName(getSymbolicName());
      tmpError.setErrorDescription(pe.getMessage());
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

