package MakePlus;

import OpenRate.process.AbstractRegexMatch;
import OpenRate.record.ErrorType;
import OpenRate.record.IRecord;
import OpenRate.record.RecordError;
import java.util.ArrayList;

/**
 * This module inspects records with an error, and sends the error record to
 * the output that is defined in the suspense map. This allows the categorisation
 * of errors.
 *
 * @author afzaal
 */
public class OutputPreparation
  extends AbstractRegexMatch
{
  // this is the CVS version info
  public static String CVS_MODULE_INFO = "OpenRate, $RCSfile: OutputPreparation.java,v $, $Revision: 1.8 $, $Date: 2011/07/16 13:14:20 $";

	// this is used for the lookup
  String[] tmpSearchParameters = new String[1];
  public static final String RATED_OUTPUT = "RatedOutput";

	/**
	  * Default Constructor
	  */
  public OutputPreparation()
  {
    super();
  }

  @Override
  public IRecord procValidRecord(IRecord r)
  {
    CDRRecord CurrentRecord = (CDRRecord) r;

    // set the good output
    CurrentRecord.addOutput(RATED_OUTPUT);

	  return r;
  }

 /**
  * Skip error record processing
  *
  * @param r The record we are working on
  * @return The processed record
  */
  @Override
  public IRecord procErrorRecord(IRecord r)
  {
    String RegexGroup;
    ArrayList<String> RegexResult;
    CDRRecord CurrentRecord;
    RecordError tmpError;
    String ErrorDescription = "";

    CurrentRecord = (CDRRecord) r;

    // see if the record has errors
    if (CurrentRecord.getErrorCount() > 0)
    {
      // 	if so, get the first error
      ErrorDescription = CurrentRecord.getErrors().get(0).getMessage();

      // 	Prepare the paramters to perform the search on
      tmpSearchParameters[0] = ErrorDescription;

      RegexGroup = CurrentRecord.Service;
      RegexResult = getRegexMatchWithChildData(RegexGroup,tmpSearchParameters);

    if (!RegexResult.get(0).equalsIgnoreCase("nomatch")){
          CurrentRecord.addOutput(RegexResult.get(0));
          CurrentRecord.SuspenseCategory = RegexResult.get(1);
      }
      else
      {
        // We could not get an output for this error - send to the default output and log it
        PipeLog.warning("Could not find output for error <" + ErrorDescription + ">, sending to default.");
        CurrentRecord.addOutput("RejectOutput");
        CurrentRecord.SuspenseCategory = "Default";

        // Add the error
        tmpError = new RecordError("ERR_SUSPENSE_LOOKUP", ErrorType.SPECIAL);
        tmpError.setModuleName(getSymbolicName());
        tmpError.setErrorDescription("Could not find suspense map for service:"+CurrentRecord.Service+" error:"+ErrorDescription);
        CurrentRecord.addError(tmpError);
      }
    }

    return r;
  }
}
