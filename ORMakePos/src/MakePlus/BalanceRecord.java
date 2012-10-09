package MakePlus;

import OpenRate.record.ErrorType;
import OpenRate.record.IError;
import OpenRate.record.RatingRecord;
import OpenRate.record.RecordError;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * basic implementation of a record that will have to be aggregated into the balance table
 * @author marco
 */
public class BalanceRecord extends RatingRecord
{
  // this is the CVS version info
  public static String CVS_MODULE_INFO = "OpenRate, $RCSfile: BalanceRecord.java,v $, $Revision: 1.12 $, $Date: 2011/08/09 12:31:04 $";

    private static final String INPUT_DELIMITER = ";";
    private static final int INPUT_FIELD_COUNT = 17;
    
    public static final int IDX_USER = 1;
    public static final int IDX_DATE = 2;
    public static final int IDX_AGG_ID = 3;
    public static final int IDX_FIN_CD = 4;
    public static final int IDX_ALIAS_STAT = 5;
    public static final int IDX_NETWORK = 6;
    public static final int IDX_DURATION = 7;
    public static final int IDX_COUNT = 8;
    public static final int IDX_RETAIL = 9;
    public static final int IDX_WHOLESALE = 10;
    public static final int IDX_REMOTEPOLO = 11;
    public static final int IDX_REMOTEROLO = 12;
    public static final int IDX_LOCALPOLO = 13;
    public static final int IDX_LOCALROLO = 14;
    public static final int IDX_LOCALTRANSIT = 15;
    public static final int IDX_REMOTETRANSIT = 16;

  public String User;
  public String Financial_Code;
  public String Date;
  public String AggID;
  public String AliasStat;
  public String Network;
  public Integer Count;
  public double Duration;
  public double RetailCost;
  public double WholeSaleCost;
  public double RemotePolo;
  public double RemoteRolo;
  public double LocalPolo;
  public double LocalRolo;
  public double LocalTransit;
  public double RemoteTransit;

  public BalanceRecord(String user,String date,double newBal)
  {
    this.User=user;
    this.Date=date;
    this.RetailCost=newBal;
  }

  public BalanceRecord(String data)
  {
    this.OriginalData=data;
    this.fields = this.getOriginalData().split(INPUT_DELIMITER);
  }

  public boolean performFieldValidation(String moduleName)
  {
    if (fields.length != INPUT_FIELD_COUNT)
    {
      RecordError tmpError = new RecordError("ERR_INVALID_FIELD_NUMBER",ErrorType.DATA_VALIDATION);
      tmpError.setModuleName("Balance mapping");
      tmpError.setErrorDescription("Invalid Number of Fields");
      addError(tmpError);
      return false;
    }
    
    if (fields[IDX_USER]==null || fields[IDX_USER].length()==0)
    {
      RecordError tmpError = new RecordError("ERR_MISSING_USER",ErrorType.DATA_VALIDATION);
      tmpError.setModuleName("Balance mapping");
      tmpError.setErrorDescription("Missing user");
      addError(tmpError);
      return false;
    }
    
    if (fields[IDX_DATE]==null || fields[IDX_DATE].length()==0)
    {
      RecordError tmpError = new RecordError("ERR_MISSING_DATE",ErrorType.DATA_VALIDATION);
      tmpError.setModuleName("Balance mapping");
      tmpError.setErrorDescription("Missing date");
      addError(tmpError);
      return false;
    }
    
    if (fields[IDX_AGG_ID]==null || fields[IDX_AGG_ID].length()==0)
    {
      RecordError tmpError = new RecordError("ERR_MISSING_AGG_ID",ErrorType.DATA_VALIDATION);
      tmpError.setModuleName("Balance mapping");
      tmpError.setErrorDescription("Missing aggregatition ID");
      addError(tmpError);
      return false;
    }
    
    if (fields[IDX_FIN_CD]==null || fields[IDX_FIN_CD].length()==0)
    {
      RecordError tmpError = new RecordError("ERR_FIN_CD_MISSING",ErrorType.DATA_VALIDATION);
      tmpError.setModuleName("Balance mapping");
      tmpError.setErrorDescription("Missing financial code");
      addError(tmpError);
      return false;
    }
    
    if (fields[IDX_ALIAS_STAT]==null || fields[IDX_ALIAS_STAT].length()==0)
    {
      RecordError tmpError = new RecordError("ERR_ALIAS_STAT_MISSING",ErrorType.DATA_VALIDATION);
      tmpError.setModuleName("Balance mapping");
      tmpError.setErrorDescription("Missing ALIAS STATUS");
      addError(tmpError);
      return false;
    }
    
    if (fields[IDX_NETWORK]==null || fields[IDX_NETWORK].length()==0)
    {
      RecordError tmpError = new RecordError("ERR_NETWORK_MISSING",ErrorType.DATA_VALIDATION);
      tmpError.setModuleName("Balance mapping");
      tmpError.setErrorDescription("Missing NETWORK");
      addError(tmpError);
      return false;
    }
    
    if (fields[IDX_DURATION]==null || fields[IDX_DURATION].length()==0)
    {
      RecordError tmpError = new RecordError("ERR_DURATION_MISSING",ErrorType.DATA_VALIDATION);
      tmpError.setModuleName("Balance mapping");
      tmpError.setErrorDescription("Duration Information Missing");
      addError(tmpError);
      return false;
    }
    
    if (fields[IDX_COUNT]==null || fields[IDX_COUNT].length()==0)
    {
      RecordError tmpError = new RecordError("ERR_COUNT_MISSING",ErrorType.DATA_VALIDATION);
      tmpError.setModuleName("Balance mapping");
      tmpError.setErrorDescription("Count Information Missing");
      addError(tmpError);
      return false;
    }

    Double retail = Double.parseDouble(fields[IDX_RETAIL]);
    Double wholesale = Double.parseDouble(fields[IDX_WHOLESALE]);
    Double rp = Double.parseDouble(fields[IDX_REMOTEPOLO]);
    Double rr = Double.parseDouble(fields[IDX_REMOTEROLO]);
    Double lp = Double.parseDouble(fields[IDX_LOCALPOLO]);
    Double lr = Double.parseDouble(fields[IDX_LOCALROLO]);
    Double lt = Double.parseDouble(fields[IDX_LOCALTRANSIT]);
    Double rt = Double.parseDouble(fields[IDX_REMOTETRANSIT]);
      
    try
    {
      this.User=fields[IDX_USER];
      this.Date=fields[IDX_DATE];
      this.AggID=fields[IDX_AGG_ID];
      this.Financial_Code=fields[IDX_FIN_CD];
      this.AliasStat=fields[IDX_ALIAS_STAT];
      this.Network=fields[IDX_NETWORK];
      this.Duration=Double.parseDouble(fields[IDX_DURATION]);
      this.Count=Integer.valueOf(fields[IDX_COUNT]);
      this.RetailCost=retail.doubleValue();
      this.WholeSaleCost=wholesale.doubleValue();
      this.RemotePolo=rp.doubleValue();
      this.RemoteRolo=rr.doubleValue();
      this.LocalPolo=lp.doubleValue();
      this.LocalRolo=lr.doubleValue();
      this.LocalTransit=lt.doubleValue();
      this.RemoteTransit=rt.doubleValue();
    }
    catch (NumberFormatException e)
    {
      RecordError tmpError = new RecordError("ERR_INVALID_CAST",ErrorType.DATA_VALIDATION);
      tmpError.setModuleName("Balance mapping");
      tmpError.setErrorDescription("Balance invalid");
      addError(tmpError);
      return false;
    }

    return true;
  }

  /**
   * Return the dump-ready data
   *
   * @return The dump info strings
   */
  public ArrayList<String> getDumpInfo()
  {
    ArrayList<String> tmpDumpList = null;
    tmpDumpList = new ArrayList<String>();

    // Format the fields
    tmpDumpList.add("============ BEGIN RECORD ============");
    tmpDumpList.add("  Record Number   = <" + this.RecordNumber + ">");
    tmpDumpList.add("  Output Streams  = <" + this.getOutputs() + ">");
    tmpDumpList.add("--------------------------------------");
    tmpDumpList.add("  User            = <" + this.User + ">");
    tmpDumpList.add("  Date            = <" + this.Date + ">");
    tmpDumpList.add("  AggID           = <" + this.AggID + ">");
    tmpDumpList.add("  Financial Code  = <" + this.Financial_Code + ">");
    tmpDumpList.add("  Alias Status    = <" + this.AliasStat + ">");
    tmpDumpList.add("  Network         = <" + this.Network + ">");
    tmpDumpList.add("  Duration        = <" + this.Duration + ">");
    tmpDumpList.add("  Retail Cost     = <" + this.RetailCost + ">");
    tmpDumpList.add("  Wholesale Cost  = <" + this.WholeSaleCost + ">");
    tmpDumpList.add("  Remote Polo     = <" + this.RemotePolo + ">");
    tmpDumpList.add("  Remote Rolo     = <" + this.RemoteRolo + ">");
    tmpDumpList.add("  Local Polo      = <" + this.LocalPolo + ">");
    tmpDumpList.add("  Local Rolo      = <" + this.LocalRolo + ">");
    tmpDumpList.add("  Local Transit   = <" + this.LocalTransit + ">");
    tmpDumpList.add("  Remote Transit  = <" + this.RemoteTransit + ">");
    tmpDumpList.add("  Count           = <" + this.Count + ">");

    Iterator<IError> it = this.getErrors().iterator();
    while (it.hasNext())
    {
      IError err = it.next();
      tmpDumpList.add("------------ Begin Error -------------");
      tmpDumpList.add("  Message:     " + err.getMessage());
      tmpDumpList.add("  Set by:      " + err.getModuleName());
      tmpDumpList.add("------------ End Error ---------------");
    }

    return tmpDumpList;
  }
}