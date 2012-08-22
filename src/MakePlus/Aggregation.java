package MakePlus;

import OpenRate.exception.ProcessingException;
import OpenRate.process.AbstractAggregation;
import OpenRate.record.ErrorType;
import OpenRate.record.IRecord;
import OpenRate.record.RecordError;
import OpenRate.utils.ConversionUtils;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 
 */
public class Aggregation
  extends AbstractAggregation
{
  // this is the CVS version info
  public static String CVS_MODULE_INFO = "OpenRate, $RCSfile: Aggregation.java,v $, $Revision: 1.17 $, $Date: 2011/08/24 13:32:42 $";

  private String[] AggFields = new String[15];
  // this needs to be modified if aggregation keys are modified
  private static SimpleDateFormat df = new SimpleDateFormat("MMM");
  private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
  private static java.util.Date dateobj = new java.util.Date();
  private static Calendar cal = Calendar.getInstance();
  private static ConversionUtils conv = new ConversionUtils();
  private static int DECIMAL_PLACES = 4;

   /**
  * give us a better naming for the aggregation files than the transaction alone
  *
  * @param TransactionNumber The transaction to get the file base name for
  * @return The unmodified header record
  */
  @Override
  public String getAggregationFileBaseName(int TransactionNumber)
  {
    return "Agg-" + Calendar.getInstance().getTimeInMillis() + "-" + TransactionNumber;
  }
  // -----------------------------------------------------------------------------
  // ------------------ Start of inherited Plug In functions ---------------------
  // -----------------------------------------------------------------------------

  /**
  * This is called when a data record is encountered. You should do any normal
  * processing here.
  */
    @Override
  public IRecord procValidRecord(IRecord r)
  {
    CDRRecord CurrentRecord = (CDRRecord)r;
    short Stat = 0;
    if(CurrentRecord.AliasStatus.equalsIgnoreCase("Live")){
        Stat = 0;
    }else if(CurrentRecord.AliasStatus.equalsIgnoreCase("Test Live")){
        Stat = 1;
    }
    
    dateobj = CurrentRecord.EventStartDate;
    cal.setTime(dateobj);
    cal.set(Calendar.DAY_OF_MONTH,1);
    CurrentRecord.AggregationID=CurrentRecord.GuidingKey+df.format(CurrentRecord.EventStartDate)+Stat;
     

    // the field that will be used for the aggregation scenario
    AggFields[0] = CurrentRecord.GuidingKey;
    AggFields[1] = sdf.format(cal.getTime()); //String.valueOf(cal.get(Calendar.YEAR)) + "-"+ String.valueOf(cal.get(Calendar.MONTH)) + "-" + String.valueOf(cal.get(Calendar.DAY_OF_MONTH)) ; // the day of the event
    AggFields[2] = CurrentRecord.AggregationID;
    AggFields[3] = CurrentRecord.FinancialCode;
    AggFields[4] = CurrentRecord.AliasStatus;
    AggFields[5] = CurrentRecord.Network;
    AggFields[6] = Double.toString(CurrentRecord.Duration);
    AggFields[7] = Double.toString(conv.getRoundedValueRoundUp(CurrentRecord.retailPrice,DECIMAL_PLACES));
    AggFields[8] = Double.toString(conv.getRoundedValueRoundUp(CurrentRecord.wholeSalePrice,DECIMAL_PLACES));
    AggFields[9] = Double.toString(conv.getRoundedValueRoundUp(CurrentRecord.remotePolo,DECIMAL_PLACES));
    AggFields[10] = Double.toString(conv.getRoundedValueRoundUp(CurrentRecord.remoteRolo,DECIMAL_PLACES));
    AggFields[11] = Double.toString(conv.getRoundedValueRoundUp(CurrentRecord.localPolo,DECIMAL_PLACES));
    AggFields[12] = Double.toString(conv.getRoundedValueRoundUp(CurrentRecord.localRolo,DECIMAL_PLACES));
    AggFields[13] = Double.toString(conv.getRoundedValueRoundUp(CurrentRecord.localTransit,DECIMAL_PLACES));
    AggFields[14] = Double.toString(conv.getRoundedValueRoundUp(CurrentRecord.remoteTransit,DECIMAL_PLACES));
    
    
    // perform the aggregation
    try
    {
      CurrentRecord.AggFilter.add("Aggregate");
      Aggregate(AggFields,CurrentRecord.AggFilter);
    }
    catch (ProcessingException pe)
    {
      RecordError tmpError = new RecordError("ERR_AGGREGATION_FAILED",ErrorType.SPECIAL);
      tmpError.setModuleName(getSymbolicName());
      tmpError.setErrorDescription("Aggregation error:"+AggFields[0]+":"+AggFields[1]+":"+AggFields[2]+":"+AggFields[3]+":"+AggFields[4]+":"+AggFields[5]);
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
