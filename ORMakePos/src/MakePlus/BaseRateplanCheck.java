package MakePlus;

import OpenRate.process.AbstractRegexMatch;
import OpenRate.record.IRecord;

/**
 * This module attempts to associate a specific rate-plan at the RecordLevel and
 * ChargePacket Level.
 */
public class BaseRateplanCheck extends AbstractRegexMatch
{
  // this is the CVS version info
  public static String CVS_MODULE_INFO = "OpenRate, $RCSfile: BaseRateplanCheck.java,v $, $Revision: 1.7 $, $Date: 2011/09/11 23:06:54 $";

	// this is used for the lookup
  String[] tmpSearchParameters = new String[1];

  @Override
  public IRecord procValidRecord(IRecord r)
  {
	  String RegexGroup;
    String RegexResult;
    CDRRecord CurrentRecord;

    CurrentRecord = (CDRRecord) r;

    RegexGroup = "DEF";
    tmpSearchParameters[0] = CurrentRecord.UserTariff;
    RegexResult = getRegexMatch(RegexGroup,tmpSearchParameters);

    if (!RegexResult.equalsIgnoreCase("NOMATCH"))
    {
      // the customer-specific rateplan has a base one
      CurrentRecord.BaseRateplan=RegexResult;
      
      // note that we have a specific plan
      CurrentRecord.specificPlan = true;
    }
    else
    {
      // Set the base rate
      CurrentRecord.BaseRateplan = CurrentRecord.UserTariff;
    }

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
    return r;
  }
}
